#!/usr/bin/env python3
"""Scrape recently-asked interview questions for a company from several public
sources, and map every mention back to a LeetCode problem.

Sources (pick with `--sources`, default all four):

    leetcode   leetcode.com Discuss   GraphQL   threads + bodies + comments
    reddit     r/leetcode & friends   Atom RSS  posts + comment threads
    blind      teamblind.com          HTML      search cards + post bodies
    hn         Hacker News            Algolia   stories + comments

Produces one markdown report (default `doc/g_recent_asked.md` for Google) listing the
LC problems referenced across all sources, with evidence quotes and links back to
every source post.

    python3 script/scrape_lc_discuss_company.py                        # google, all sources
    python3 script/scrape_lc_discuss_company.py --tag meta             # -> doc/meta_recent_asked.md
    python3 script/scrape_lc_discuss_company.py --sources reddit,blind # skip the slow one
    python3 script/scrape_lc_discuss_company.py --build-only           # rebuild doc from cache
    python3 script/scrape_lc_discuss_company.py --max-pages 2          # quick sample

Everything downloaded is cached under `--cache-dir` (default `data/.lc_discuss_cache/<tag>/`),
one file per post, so re-runs resume instead of re-fetching. Delete the cache dir for a
clean pull, or pass `--refresh-index` to re-download just the LC problem index.

WHY THIS EXISTS / WHAT IT IS NOT
--------------------------------
LeetCode's official company question list (`companyTag`) is Premium-gated and returns
`null` to anonymous requests. Every source here is *public chatter* instead, so the
output is self-reported interview experience, not anybody's frequency data. Treat the
counts as weak signal.

ADDING A SOURCE
---------------
Write `def source_x(ctx) -> list[record]` (use `record()` and `ctx.cache("x")`), honour
`ctx.build_only` by returning whatever is already cached, and add it to `SOURCES`.
Extraction, ranking and reporting are source-agnostic — they only read `record()` fields.

PER-SOURCE NOTES (all found the hard way; no source documents any of this)
-------------------------------------------------------------------------
leetcode
  * The *legacy* discuss API (`categoryTopicList`, category `interview-question`) still
    responds but is frozen at 2025-03-04 — LeetCode migrated Discuss during 2025. Live
    data lives behind the `ugcArticle*` fields used here.
  * Introspection is disabled; the schema below was found by reading error messages.
  * `tagSlugs` is required on `ugcArticleDiscussionArticles`; omitting it fails with
    "argument of type 'NoneType' is not iterable".
  * Variable types must match exactly: `$keywords: [String]!` but `$tagSlugs: [String!]`.
  * `content` is NULL in list mode — only `summary` is populated. Bodies need stage 2.
  * `totalNum` on the list connection is CAPPED (3000) and is not the real result count;
    page until a short page instead of trusting it.
  * The two `topicId` arguments have DIFFERENT types, and this is not a typo:
        ugcArticleDiscussionArticle(topicId:) -> ID
        topicComments(topicId:)               -> Int!
    Using one type for both fails on whichever call guessed wrong.
  * `topicComments.orderBy` is a plain String ("most_votes" / "newest_to_oldest" /
    "oldest_to_newest" / "hot"), not an enum.
  * Rapid requests trip a WAF that returns HTML 403s rather than JSON.

reddit
  * `www.reddit.com/...json` returns HTML 403 to anonymous clients, but the **`.rss`
    twin of the same path still serves anonymously** — that is the whole trick here.
  * Search feed: `/r/<sub>/search.rss?q=&restrict_sr=1&sort=new&limit=100`, paged with
    `after=t3_<id>` (the id of the last entry). `limit` caps at 100.
  * Comments feed: append `.rss` to a post permalink. Entry 1 is the post itself,
    the rest are comments (ids `t1_*`), newest first.
  * `<content type="html">` carries the full selftext — no per-post body fetch needed.
  * Rate limiting is aggressive and runs on a rolling window: two requests 4s apart can
    429, and so can the 5th request at a steady 5s. ~8s is a workable floor, and a 429
    needs a real sleep (~90s+) rather than a quick retry.

blind
  * No API and no login wall on search: `teamblind.com/search/<urlencoded query>` is
    server-rendered HTML. Cards are `<article data-article-alias="...">`.
  * There is NO pagination — `?page=2` returns page 1 again, and a query yields exactly
    20 cards. Breadth comes from asking several queries (see `BLIND_QUERIES`).
  * Card bodies are truncated at ~312 chars, so stage 2 fetches `/post/<slug>` for the
    full text. Comments are client-rendered and are NOT visible to this scraper.
  * The relative date on a card ("Jun 17") has an exact `title="MM/DD/YYYY, ..."`
    attribute next to it — parse that, not the human string.

hn
  * `hn.algolia.com/api/v1/search_by_date` is public, unauthenticated and generous:
    `hitsPerPage` up to 1000, `page` 0-based, `tags=story|comment`.
  * Matching is fuzzy/OR-ish, so results are noisy — this source is breadth, not signal.
"""

import argparse
import collections
import html
import json
import os
import re
import sys
import time
import urllib.error
import urllib.parse
import urllib.request
import xml.etree.ElementTree as ET

REPO_ROOT = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
PROBLEM_INDEX_URL = "https://leetcode.com/api/problems/all/"
HEADERS = {
    "Accept": "*/*",
    "Accept-Language": "en-US,en;q=0.9",
    "User-Agent": (
        "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 "
        "(KHTML, like Gecko) Chrome/131.0.0.0 Safari/537.36"
    ),
}

# Seconds between requests, per source. Below the leetcode/reddit values you get
# blocked rather than throttled; blind and hn are relaxed but not free.
DELAYS = {"leetcode": 2.5, "reddit": 8.0, "blind": 2.0, "hn": 1.0}


# --------------------------------------------------------------------------- net
def fetch(url, delay, data=None, headers=None, tries=4):
    """HTTP GET (or POST when `data` is given), with backoff. Returns bytes, or None
    when the request is hopeless (4xx that a retry cannot fix, or tries exhausted)."""
    hdrs = dict(HEADERS, **(headers or {}))
    for attempt in range(tries):
        try:
            req = urllib.request.Request(url, data=data, headers=hdrs)
            with urllib.request.urlopen(req, timeout=45) as resp:
                return resp.read()
        except urllib.error.HTTPError as exc:
            if exc.code in (400, 404):  # retrying cannot help
                detail = exc.read().decode("utf-8", "replace")[:200]
                print(f"    HTTP {exc.code} {url}: {detail}", file=sys.stderr)
                return None
            # Reddit throttles on a rolling window, so a 429 needs a real sleep —
            # retrying on the normal backoff just burns the remaining tries.
            wait = (90.0 if exc.code == 429 else max(30.0, delay * 12)) * (attempt + 1)
            print(f"    HTTP {exc.code}; sleeping {wait:.0f}s", file=sys.stderr)
        except Exception as exc:  # network hiccup, or a WAF serving HTML
            print(f"    {type(exc).__name__}: {exc}; retrying", file=sys.stderr)
            wait = max(30.0, delay * 12) * (attempt + 1)
        time.sleep(wait)
    return None


def fetch_json(url, delay, **kw):
    raw = fetch(url, delay, **kw)
    try:
        return json.loads(raw) if raw else None
    except ValueError:
        return None


def text_of(fragment):
    """HTML fragment -> plain text. Good enough for keyword extraction."""
    s = re.sub(r"<(script|style)[^>]*>.*?</\1>", " ", fragment or "", flags=re.S | re.I)
    s = re.sub(r"<!--.*?-->", "", s, flags=re.S)
    s = re.sub(r"<br\s*/?>|</p>|</div>|</li>|</h\d>", "\n", s, flags=re.I)
    s = re.sub(r"<[^>]+>", " ", s)
    return re.sub(r"[ \t]+", " ", html.unescape(s)).strip()


# ------------------------------------------------------------------------- state
class Cache:
    """One JSON file per key, so an interrupted run resumes instead of re-fetching."""

    def __init__(self, directory):
        self.dir = directory
        os.makedirs(self.dir, exist_ok=True)

    def path(self, *parts):
        return os.path.join(self.dir, *parts)

    def load(self, *parts, default=None):
        try:
            with open(self.path(*parts)) as fh:
                return json.load(fh)
        except Exception:
            return default

    def save(self, obj, *parts):
        os.makedirs(os.path.dirname(self.path(*parts)), exist_ok=True)
        with open(self.path(*parts), "w") as fh:
            json.dump(obj, fh)

    def has(self, *parts):
        return os.path.exists(self.path(*parts))


class Ctx:
    """Everything a source needs: what to look for, where to cache, how fast to go."""

    def __init__(self, args, root):
        self.args = args
        self.root = root
        self.tag = args.tag
        self.company = args.tag.replace("-", " ")
        self.build_only = args.build_only
        self.max_pages = args.max_pages
        self.want_comments = not args.no_comments

    def delay(self, source):
        return self.args.delay if self.args.delay is not None else DELAYS[source]

    def cache(self, source):
        # leetcode keeps the historical layout at the cache root so existing (slow to
        # rebuild) caches keep working; sources added later get their own subdir.
        return Cache(self.root if source == "leetcode"
                     else os.path.join(self.root, source))


def record(source, ident, title, text, url, date, meta=""):
    """The one shape every source returns and everything downstream reads.

    `text` is the full searchable blob for the thread: body plus comments, if any.
    `date` is `YYYY-MM-DD`; `meta` is a short source-specific label (subreddit,
    Blind channel, HN story title) shown in the raw feed.
    """
    return {"source": source, "key": f"{source}:{ident}", "title": title or "",
            "text": text or "", "url": url, "date": (date or "")[:10], "meta": meta}


# -------------------------------------------------------------------- source: LC
GRAPHQL_URL = "https://leetcode.com/graphql/"
GQL_HEADERS = {"Content-Type": "application/json", "Origin": "https://leetcode.com",
               "Referer": "https://leetcode.com/discuss/"}

LIST_Q = """query list($keywords: [String]!, $tagSlugs: [String!], $skip: Int, $first: Int) {
  ugcArticleDiscussionArticles(keywords: $keywords, tagSlugs: $tagSlugs,
                               skip: $skip, first: $first, orderBy: MOST_RECENT) {
    totalNum
    edges { node { uuid title slug summary createdAt updatedAt hitCount topicId
                   tags { name slug } author { userName } } }
  }
}"""

# topicId is ID here...
BODY_Q = """query body($topicId: ID) {
  ugcArticleDiscussionArticle(topicId: $topicId) { uuid title content createdAt }
}"""

# ...but Int! here. Not a typo; see module docstring.
COMMENTS_Q = """query comments($topicId: Int!, $pageNo: Int, $numPerPage: Int) {
  topicComments(topicId: $topicId, orderBy: "most_votes",
                pageNo: $pageNo, numPerPage: $numPerPage) {
    totalNum
    data { id numChildren post { content creationDate voteCount } }
  }
}"""


def gql(query, variables, delay):
    """POST a GraphQL query. Returns `data`, or None if the query is unrecoverable."""
    body = json.dumps({"query": query, "variables": variables}).encode()
    payload = fetch_json(GRAPHQL_URL, delay, data=body, headers=GQL_HEADERS)
    if payload is None:
        return None
    if payload.get("errors"):
        print(f'    GraphQL error: {payload["errors"][0].get("message", "")[:200]}',
              file=sys.stderr)
        return None
    return payload.get("data")


def source_leetcode(ctx):
    """Three calls, because the list endpoint returns no bodies and no comments —
    and the comments are where the actual questions usually are (most threads are
    compensation/team-match chatter with the problems buried in replies)."""
    cache, delay = ctx.cache("leetcode"), ctx.delay("leetcode")
    posts = {n["uuid"]: n for n in cache.load("posts.json", default=[])}

    if not ctx.build_only:
        # Stage 1: page the discuss list to exhaustion. Always re-run — it is cheap,
        # and it is the only way to notice new threads.
        skip = page = 0
        while ctx.max_pages is None or page < ctx.max_pages:
            data = gql(LIST_Q, {"keywords": [], "tagSlugs": [ctx.tag],
                                "skip": skip, "first": ctx.args.per_page}, delay)
            conn = (data or {}).get("ugcArticleDiscussionArticles")
            if not conn:
                print(f"  list stopped at skip={skip} (no data)", file=sys.stderr)
                break
            edges = conn["edges"]
            posts.update({e["node"]["uuid"]: e["node"] for e in edges})
            print(f"  skip={skip:<5} got={len(edges):<3} unique={len(posts)}")
            # `totalNum` is capped and unreliable; a short page is the real end marker.
            if len(edges) < ctx.args.per_page:
                break
            skip, page = skip + ctx.args.per_page, page + 1
            time.sleep(delay)
        cache.save(list(posts.values()), "posts.json")

        # Stage 2: bodies, one call per thread.
        todo = [n for n in posts.values()
                if n.get("topicId") and not cache.has("bodies", f'{n["uuid"]}.json')]
        print(f"  {len(todo)} bodies to fetch ({len(posts) - len(todo)} cached)")
        for i, n in enumerate(todo, 1):
            data = gql(BODY_Q, {"topicId": n["topicId"]}, delay)
            if data is None:
                print("  aborting body stage (unrecoverable)", file=sys.stderr)
                break
            node = data.get("ugcArticleDiscussionArticle")
            cache.save(node or {"uuid": n["uuid"], "content": ""},
                       "bodies", f'{n["uuid"]}.json')
            if i % 25 == 0:
                print(f"    {i}/{len(todo)}")
            time.sleep(delay)

        # Stage 3: comments, paged. Newest first — this is the slow stage, and a run
        # that is cut short should have spent its requests on the freshest threads.
        todo = sorted((n for n in posts.values() if ctx.want_comments and n.get("topicId")
                       and not cache.has("comments", f'{n["uuid"]}.json')),
                      key=lambda n: n["createdAt"], reverse=True)
        print(f"  {len(todo)} comment threads to fetch"
              f"{' (--no-comments)' if not ctx.want_comments else ''}")
        for i, n in enumerate(todo, 1):
            got, page = [], 1
            while page <= 4:
                data = gql(COMMENTS_Q, {"topicId": int(n["topicId"]),
                                        "pageNo": page, "numPerPage": 50}, delay)
                conn = (data or {}).get("topicComments")
                if not conn:
                    break
                got.extend(conn["data"])
                if not conn["data"] or len(got) >= conn["totalNum"]:
                    break
                page += 1
                time.sleep(delay)
            cache.save(got, "comments", f'{n["uuid"]}.json')
            if i % 25 == 0:
                print(f"    {i}/{len(todo)}")
            time.sleep(delay)

    out = []
    for uuid, n in posts.items():
        body = cache.load("bodies", f"{uuid}.json", default={}) or {}
        comments = cache.load("comments", f"{uuid}.json", default=[]) or []
        chunks = [n.get("summary") or "", body.get("content") or ""]
        chunks += [(c.get("post") or {}).get("content") or "" for c in comments]
        tags = [t["slug"] for t in (n.get("tags") or []) if t["slug"] != ctx.tag]
        out.append(record(
            "leetcode", uuid, n["title"], "\n".join(text_of(c) for c in chunks),
            f'https://leetcode.com/discuss/post/{n["topicId"]}/{n.get("slug") or ""}/',
            n["createdAt"], meta=fit(tags)))
    return out


# ---------------------------------------------------------------- source: reddit
REDDIT_SUBS = ["leetcode", "cscareerquestions", "csMajors", "ExperiencedDevs"]
ATOM = "{http://www.w3.org/2005/Atom}"
# Every RSS entry ends with this; it is feed furniture, and it pollutes the quotes.
REDDIT_FOOTER = re.compile(r"\s*submitted by\s*/u/\S+\s*\[link\]\s*\[comments\]\s*$", re.I)


def atom_entries(raw):
    """Parse a reddit Atom feed into dicts. Reddit serves well-formed XML; a parse
    failure means we got an error page instead, so treat it as an empty feed."""
    try:
        root = ET.fromstring(raw)
    except Exception:
        return []
    out = []
    for e in root.findall(ATOM + "entry"):
        def txt(tag):
            node = e.find(ATOM + tag)
            return (node.text or "") if node is not None else ""
        link = e.find(ATOM + "link")
        out.append({"id": txt("id"), "title": txt("title"),
                    "content": txt("content"), "date": txt("published"),
                    "url": link.get("href") if link is not None else ""})
    return out


def source_reddit(ctx):
    """Search feed per subreddit, then the comment feed of every interview-flavoured
    hit. The `.rss` twin of each blocked `.json` path is the whole trick — see the
    module docstring."""
    cache, delay = ctx.cache("reddit"), ctx.delay("reddit")
    posts = cache.load("posts.json", default={})
    query = urllib.parse.quote_plus(f"{ctx.company} interview")

    if not ctx.build_only:
        for sub in ctx.args.reddit_subs:
            after, page = None, 0
            while ctx.max_pages is None or page < ctx.max_pages:
                url = (f"https://www.reddit.com/r/{sub}/search.rss?q={query}"
                       f"&restrict_sr=1&sort=new&limit=100"
                       + (f"&after={after}" if after else ""))
                entries = atom_entries(fetch(url, delay) or b"")
                for e in entries:
                    e["sub"] = sub
                    posts[e["id"]] = e
                print(f"  r/{sub:<20} page={page} got={len(entries):<4} "
                      f"unique={len(posts)}")
                if len(entries) < 100:
                    break
                after, page = entries[-1]["id"], page + 1
                time.sleep(delay)
            time.sleep(delay)
        cache.save(posts, "posts.json")

        # Newest first: comment fetching is the slow part, and a run that is cut short
        # should have spent its requests on the freshest threads.
        todo = sorted((p for p in posts.values()
                       if ctx.want_comments and interviewish(p["title"], p["content"])
                       and not cache.has("comments", f'{p["id"]}.json')),
                      key=lambda p: p["date"], reverse=True)
        print(f"  {len(todo)} comment threads to fetch"
              f"{' (--no-comments)' if not ctx.want_comments else ''}")
        for i, p in enumerate(todo, 1):
            raw = fetch(p["url"].rstrip("/") + "/.rss?limit=100", delay) or b""
            # Entry 1 of a comment feed is the post itself; the rest are comments.
            cache.save([e["content"] for e in atom_entries(raw)[1:]],
                       "comments", f'{p["id"]}.json')
            if i % 25 == 0:
                print(f"    {i}/{len(todo)}")
            time.sleep(delay)

    out = []
    for pid, p in posts.items():
        comments = cache.load("comments", f"{pid}.json", default=[]) or []
        blob = "\n".join(REDDIT_FOOTER.sub("", text_of(c))
                         for c in [p["content"]] + comments)
        out.append(record("reddit", pid, p["title"], blob, p["url"], p["date"],
                          meta=f'r/{p.get("sub", "?")}'))
    return out


# ----------------------------------------------------------------- source: blind
# Blind serves exactly 20 cards per query and ignores `?page`, so breadth has to come
# from asking several questions instead of paging one.
BLIND_QUERIES = ["interview", "onsite", "leetcode", "phone screen", "online assessment"]
BLIND_CARD_RE = re.compile(
    r'data-testid="article-preview-title"[^>]*>(.*?)</h2>\s*<p[^>]*>(.*?)</p>', re.S)
BLIND_BODY_RE = re.compile(
    r'data-testid="article-title"[^>]*>(.*?)</h1>\s*<p[^>]*>(.*?)</p>', re.S)


def blind_cards(page):
    """Pull the search-result cards out of Blind's server-rendered HTML."""
    out = []
    for chunk in (page or "").split('data-article-alias="')[1:]:
        alias = chunk.split('"', 1)[0]
        card = chunk.split("</article>", 1)[0]
        body = BLIND_CARD_RE.search(card)
        slug = re.search(r'href="/post/([^"]+)"', card)
        if not (body and slug):
            continue
        # The human date ("Jun 17") is ambiguous; the tooltip next to it is not.
        date = re.search(r'title="(\d\d)/(\d\d)/(\d{4})', card)
        chan = re.search(r'article-preview-channel"[^>]*>(?:<[^>]*>)*([^<]+)', card)
        out.append({"id": alias, "slug": slug.group(1),
                    "title": text_of(body.group(1)), "preview": text_of(body.group(2)),
                    "date": (f"{date.group(3)}-{date.group(1)}-{date.group(2)}"
                             if date else ""),
                    "channel": text_of(chan.group(1)) if chan else ""})
    return out


def source_blind(ctx):
    """Search cards, then the post page for the full body — cards truncate at ~312
    chars. Comments are client-rendered and stay out of reach."""
    cache, delay = ctx.cache("blind"), ctx.delay("blind")
    posts = cache.load("posts.json", default={})

    if not ctx.build_only:
        for suffix in BLIND_QUERIES:
            q = urllib.parse.quote(f"{ctx.company} {suffix}")
            raw = fetch(f"https://www.teamblind.com/search/{q}", delay) or b""
            cards = blind_cards(raw.decode("utf-8", "replace"))
            for c in cards:
                posts[c["id"]] = c
            print(f'  "{ctx.company} {suffix}"'.ljust(38)
                  + f"got={len(cards):<3} unique={len(posts)}")
            time.sleep(delay)
        cache.save(posts, "posts.json")

        todo = [p for p in posts.values() if not cache.has("bodies", f'{p["id"]}.json')]
        print(f"  {len(todo)} bodies to fetch ({len(posts) - len(todo)} cached)")
        for i, p in enumerate(todo, 1):
            raw = fetch(f'https://www.teamblind.com/post/{p["slug"]}', delay) or b""
            m = BLIND_BODY_RE.search(raw.decode("utf-8", "replace"))
            cache.save(text_of(m.group(2)) if m else "", "bodies", f'{p["id"]}.json')
            if i % 25 == 0:
                print(f"    {i}/{len(todo)}")
            time.sleep(delay)

    out = []
    for pid, p in posts.items():
        body = cache.load("bodies", f"{pid}.json", default="") or ""
        out.append(record("blind", pid, p["title"],
                          body if len(body) > len(p["preview"]) else p["preview"],
                          f'https://www.teamblind.com/post/{p["slug"]}',
                          p["date"], meta=p.get("channel", "")))
    return out


# -------------------------------------------------------------------- source: hn
def source_hn(ctx):
    """Algolia's HN index: public, fast and generous. Matching is fuzzy, so this is a
    breadth source — expect noise, and lean on the evidence quotes."""
    cache, delay = ctx.cache("hn"), ctx.delay("hn")
    posts = cache.load("posts.json", default={})
    query = urllib.parse.quote_plus(f"{ctx.company} leetcode interview")

    if not ctx.build_only:
        for kind in ("story", "comment"):
            page = 0
            while page < (ctx.max_pages or 5):
                data = fetch_json(
                    f"https://hn.algolia.com/api/v1/search_by_date?query={query}"
                    f"&tags={kind}&hitsPerPage=100&page={page}", delay)
                hits = (data or {}).get("hits") or []
                for h in hits:
                    posts[h["objectID"]] = {
                        "id": h["objectID"],
                        "title": h.get("title") or h.get("story_title") or "",
                        "text": text_of(h.get("comment_text") or h.get("story_text") or ""),
                        "date": h.get("created_at", ""), "kind": kind,
                        "story": h.get("story_title") or ""}
                print(f"  {kind:<8} page={page} got={len(hits):<4} unique={len(posts)}")
                if page + 1 >= (data or {}).get("nbPages", 0) or len(hits) < 100:
                    break
                page += 1
                time.sleep(delay)
            time.sleep(delay)
        cache.save(posts, "posts.json")

    return [record("hn", p["id"], p["title"] or p["story"], p["text"],
                   f'https://news.ycombinator.com/item?id={p["id"]}', p["date"],
                   meta=(p["story"][:40] if p["kind"] == "comment" else "story"))
            for p in posts.values()]


SOURCES = {"leetcode": source_leetcode, "reddit": source_reddit,
           "blind": source_blind, "hn": source_hn}
LABELS = {"leetcode": "LeetCode Discuss", "reddit": "Reddit",
          "blind": "Blind", "hn": "Hacker News"}
ENDPOINTS = {
    "leetcode": ("`leetcode.com/graphql` — `ugcArticleDiscussionArticles`, "
                 "`ugcArticleDiscussionArticle`, `topicComments`",
                 "`skip` += `first`, then one call per thread",
                 "threads + bodies + comments"),
    "reddit": ("`reddit.com/r/<sub>/search.rss` + `<permalink>/.rss`",
               "`after=t3_<id>`, `limit=100`",
               "posts (full selftext) + comment threads"),
    "blind": ("`teamblind.com/search/<query>` + `/post/<slug>` (HTML)",
              "none — several queries instead (`?page` is ignored)",
              "search cards + full post bodies (**no comments**)"),
    "hn": ("`hn.algolia.com/api/v1/search_by_date`", "`page` 0..n, `hitsPerPage=100`",
           "stories + comments"),
}


# -------------------------------------------------------------------- extraction
URL_RE = re.compile(r"leetcode\.com/problems/([a-z0-9][a-z0-9\-]{2,})", re.I)
# "LC 200" / "leetcode 200" take 1+ digits; a bare "#200" needs 2+ so that prose like
# "#1 priority" does not resolve to a real problem number.
NUM_RE = re.compile(r"(?:\bLC\s*#?\s*|\bleetcode\s*#?\s*)(\d{1,4})\b"
                    r"|(?<![\w.])#(\d{2,4})\b", re.I)
# "leetcode 8+ hrs a day", "leetcode 300 problems" — that is a count, not a problem id.
UNIT_RE = re.compile(r"\s*(?:\+|k\b|%|hours?|hrs?|minutes?|mins?|days?|weeks?|months?|"
                     r"years?|times?|problems?|questions?|qs?\b|easy|mediums?|hards?)", re.I)
# "leetcode 75 / 150 / 169" is nearly always a study *list* (LeetCode 75, NeetCode 150,
# Grind 169), not problem #75. Written the problem way — "LC 75" — it still counts.
LIST_NUMS = {50, 75, 100, 150, 169}
# Titles that are ordinary English and would match constantly in prose.
BAD_TITLES = {"design", "sort colors", "word break", "jump game", "candy",
              "trapping rain water"}
INTERVIEW_HINT = re.compile(
    r"interview|onsite|phone screen|screen|round|oa\b|online assessment|asked|coding", re.I)


def interviewish(*chunks):
    return bool(INTERVIEW_HINT.search(" ".join(c or "" for c in chunks)))


def load_problem_index(cache, refresh, delay=1.0):
    """LeetCode's public problem index: slug/number/title/difficulty for every problem."""
    if refresh or not cache.has("all_problems.json"):
        print("  downloading LC problem index")
        raw = fetch(PROBLEM_INDEX_URL, delay)
        if not raw:
            sys.exit("could not download the LC problem index")
        cache.save(json.loads(raw), "all_problems.json")
    difficulty = {1: "Easy", 2: "Medium", 3: "Hard"}
    by_slug, by_num, by_title = {}, {}, {}
    for entry in cache.load("all_problems.json")["stat_status_pairs"]:
        stat = entry["stat"]
        rec = {"num": stat["frontend_question_id"],
               "slug": stat["question__title_slug"],
               "title": stat["question__title"],
               "difficulty": difficulty.get(entry["difficulty"]["level"], "?"),
               "paid_only": entry["paid_only"]}
        by_slug[rec["slug"]] = rec
        by_num.setdefault(rec["num"], rec)
        by_title.setdefault(rec["title"].lower(), rec)
    return by_slug, by_num, by_title


def load_repo_solved():
    """Problems already solved in this repo, for the 'In repo?' column. Optional."""
    path = os.path.join(REPO_ROOT, "_site", "data", "lc-problems.json")
    try:
        with open(path) as fh:
            return {int(p["id"]): p for p in json.load(fh)["problems"]}
    except Exception:
        return {}


def build_title_regex(by_title):
    """Only distinctive titles: long enough and multi-word, else prose false-positives."""
    candidates = {t: r for t, r in by_title.items() if len(t) >= 14 and " " in t}
    pattern = "|".join(sorted((re.escape(t) for t in candidates), key=len, reverse=True))
    return candidates, re.compile(r"(?<!\w)(" + pattern + r")(?!\w)", re.I)


def extract(posts, by_slug, by_num, by_title):
    candidates, title_re = build_title_regex(by_title)
    hits = {}

    def note(rec, post, how, text, pos):
        hit = hits.setdefault(rec["num"], {"rec": rec, "posts": {}, "seen": set(),
                                           "evidence": [], "methods": collections.Counter()})
        hit["posts"][post["key"]] = post
        hit["methods"][how] += 1
        if post["key"] in hit["seen"]:
            return  # one quote per (problem, thread): url/num/title all fire on one sentence
        hit["seen"].add(post["key"])
        start = max(0, pos - 70)
        hit["evidence"].append({
            "snippet": re.sub(r"\s+", " ", text[start:start + 200]).strip(),
            "post": post["title"], "date": post["date"], "source": post["source"],
            "url": post["url"], "how": how})

    for post in posts:
        text = post["title"] + "\n" + post["text"]
        for m in URL_RE.finditer(text):
            rec = by_slug.get(m.group(1).lower())
            if rec:
                note(rec, post, "url", text, m.start())
        for m in NUM_RE.finditer(text):
            num = int(m.group(1) or m.group(2))
            if UNIT_RE.match(text, m.end()):
                continue
            if num in LIST_NUMS and m.group(0).lower().lstrip("#").startswith("leetcode"):
                continue
            rec = by_num.get(num)
            if rec:
                note(rec, post, "number", text, m.start())
        for m in title_re.finditer(text):
            key = m.group(1).lower()
            rec = candidates.get(key)
            if rec and key not in BAD_TITLES:
                note(rec, post, "title", text, m.start())

    return sorted(hits.values(),
                  key=lambda h: (-len(h["posts"]), -sum(h["methods"].values()), h["rec"]["num"]))


# ------------------------------------------------------------------------ report
EV_PROBLEMS, EV_QUOTES, FEED_ROWS = 25, 3, 60


def fit(items, budget=60):
    """Join labels to a budget, dropping whole items rather than cutting one in half."""
    kept, used = [], 0
    for item in items:
        need = len(item) + (2 if kept else 0)
        if used + need > budget:
            return ", ".join(kept) + f" +{len(items) - len(kept)} more"
        kept.append(item)
        used += need
    return ", ".join(kept)


def render(ctx, posts, ranked, solved, out_path, sources, generated_on):
    company = ctx.tag.replace("-", " ").title()
    by_source = collections.defaultdict(list)
    for p in posts:
        by_source[p["source"]].append(p)
    dated = [p["date"] for p in posts if p["date"]]
    lines = []
    w = lines.append

    def type_of(rec):
        entry = solved.get(rec["num"])
        return ", ".join(entry["tags"][:3]) if entry and entry.get("tags") else "—"

    w(f"# {company} SWE — Recently Asked Interview Questions (scraped)\n")
    w(f"> **Generated**: {generated_on}  ")
    w(f'> **Sources**: {", ".join(LABELS[s] for s in sources)}  ')
    w(f"> **Regenerate**: `python3 script/scrape_lc_discuss_company.py --tag {ctx.tag} "
      f'--sources {",".join(sources)}`  ')
    w(f"> **Corpus**: {len(posts)} posts, {min(dated)} → {max(dated)}\n"
      if dated else f"> **Corpus**: {len(posts)} posts\n")

    w("| Source | Posts | Range | What is scraped |")
    w("|--------|-------|-------|-----------------|")
    for s in sources:
        got = by_source.get(s, [])
        span = sorted(p["date"] for p in got if p["date"])
        w(f"| {LABELS[s]} | {len(got)} | "
          f'{f"{span[0]} → {span[-1]}" if span else "—"} | {ENDPOINTS[s][2]} |')
    w("")

    w("## ⚠️ Read this first — what this data is and is not\n")
    w("- LeetCode's **official company tag list** (`companyTag`) is **Premium-gated** and "
      "returns `null` for anonymous requests. This doc is **not** that list.")
    w("- Everything here is **user-reported interview experience** from public forums. It "
      "is self-reported, unverified, and skewed toward whoever bothers to post.")
    w("- Sources differ in signal. LeetCode Discuss and Reddit posters cite problems by "
      "number or link; Blind and Hacker News posters mostly do not, so those two "
      "contribute breadth (and noise) rather than precise references.")
    w("- The **legacy** LeetCode discuss API (`categoryTopicList`, category "
      "`interview-question`) is frozen at **2025-03-04** — LeetCode migrated Discuss "
      "during 2025. Anything claiming to scrape \"recent\" questions from that endpoint "
      "is serving stale data.")
    w("- Treat problem counts as **weak signal** (mention frequency), not ground-truth "
      "interview frequency. A single well-linked compilation post can put a dozen problems "
      "on the board at once.")
    w("- Mentions are **not all interview reports** — some describe a practice routine, and "
      "a title match can even land inside a sentence saying the problem is *not* what was "
      "asked. The `Match` column and the quotes exist so you can check.\n")

    w("## 1) Most-referenced LC problems\n")
    w("**`Posts`** = number of **distinct threads** referencing the problem anywhere in "
      "`title + body + comments`. It counts threads, not mentions: a thread naming the same "
      "problem five times counts once, so `Posts` is *not* the sum of the quotes below.\n")
    w("`Where` = which sources the thread(s) came from. `Match` = how the reference was "
      "found, showing the **strongest** evidence anywhere in that thread set. **url** = the "
      "post linked `leetcode.com/problems/<slug>` (high confidence); **num** = wrote "
      "`LC 200` / `#200`; **title** = the exact title appeared in prose — weakest, worth "
      "eyeballing the quote before trusting it.\n")
    w("The table below is **complete** — every problem extracted from the corpus is listed.\n")
    if ranked:
        w("| # | Problem | Diff | Type / Tags | Posts | Where | Match | Last seen | In repo? |")
        w("|---|---------|------|-------------|-------|-------|-------|-----------|----------|")
        for hit in ranked:
            rec = hit["rec"]
            link = f'https://leetcode.com/problems/{rec["slug"]}/'
            paid = " 🔒" if rec["paid_only"] else ""
            # Strongest evidence, not most frequent: one link outweighs ten prose mentions.
            how = ("url" if hit["methods"]["url"]
                   else "num" if hit["methods"]["number"] else "title")
            where = ", ".join(sorted({p["source"] for p in hit["posts"].values()}))
            last = max((p["date"] for p in hit["posts"].values() if p["date"]), default="—")
            w(f'| {rec["num"]} | [{rec["title"]}]({link}){paid} | {rec["difficulty"]} '
              f'| {type_of(rec)} | {len(hit["posts"])} | {where} | {how} | {last} '
              f'| {"✅" if rec["num"] in solved else "—"} |')
    else:
        w("_No LC problem references extracted from the scraped corpus._")
    w("")

    w("### Evidence (quotes from the scraped posts)\n")
    w(f"**This is a sample, not a full audit trail.** It covers the top {EV_PROBLEMS} "
      f"problems of {len(ranked)}, with at most {EV_QUOTES} quotes each (one per thread, "
      "from the first match in that thread). Where a problem has more threads than quotes "
      "shown, the surplus is noted inline. For the rest, follow the links in the table and "
      "section 2.\n")
    for hit in ranked[:EV_PROBLEMS]:
        rec, n_posts = hit["rec"], len(hit["posts"])
        extra = n_posts - min(len(hit["evidence"]), EV_QUOTES)
        more = f' — _{extra} further thread{"s" if extra > 1 else ""} not quoted_' if extra > 0 else ""
        w(f'**LC {rec["num"]} — {rec["title"]}** ({rec["difficulty"]}) · '
          f'{n_posts} thread{"s" if n_posts > 1 else ""}{more}  ')
        for ev in hit["evidence"][:EV_QUOTES]:
            w(f'- `{ev["source"]}` _{ev["date"]}_ · [{ev["post"][:70]}]({ev["url"]})  ')
            w(f'  > …{ev["snippet"][:230]}…')
        w("")

    w("## 2) Recent interview posts (raw feed)\n")
    w("Newest first — the primary sources. Open them for full text and comment threads. "
      f"Only interview-flavoured posts are listed, at most {FEED_ROWS} per source.\n")
    for s in sources:
        feed = sorted((p for p in by_source.get(s, [])
                       if interviewish(p["title"], p["text"][:400])),
                      key=lambda p: p["date"], reverse=True)
        w(f"### {LABELS[s]}\n")
        if not feed:
            w("_Nothing matched._\n")
            continue
        w("| Date | Post | Context |")
        w("|------|------|---------|")
        for p in feed[:FEED_ROWS]:
            title = p["title"].replace("|", "\\|").strip()[:95] or "(untitled)"
            w(f'| {p["date"] or "—"} | [{title}]({p["url"]}) | {p["meta"]} |')
        if len(feed) > FEED_ROWS:
            w(f"\n_{len(feed) - FEED_ROWS} more not shown._")
        w("")

    w("## 3) Method\n")
    w("Generated by [`script/scrape_lc_discuss_company.py`](../script/"
      "scrape_lc_discuss_company.py). Each source is scraped independently, cached one "
      "file per post, then all posts are pooled and scanned for LeetCode references "
      "(problem link, `LC <n>` / `#<n>`, or an exact problem title in prose).\n")
    w("```bash")
    w(f"python3 script/scrape_lc_discuss_company.py --tag {ctx.tag}  # full run (slow)")
    w("python3 script/scrape_lc_discuss_company.py --build-only    # rebuild doc from cache")
    w("```\n")
    w("| Source | Endpoint | Pagination | Returns |")
    w("|--------|----------|------------|---------|")
    for s in sources:
        ep, pag, ret = ENDPOINTS[s]
        w(f"| {LABELS[s]} | {ep} | {pag} | {ret} |")
    w("")
    w("**Gotchas worth knowing** (none of this is documented by any of these sites):\n")
    w("- LeetCode: introspection is disabled, `tagSlugs` is required, `content` is null in "
      "list mode, `totalNum` is capped at 3000, and — **not a typo** — "
      "`ugcArticleDiscussionArticle` takes `topicId: ID` while `topicComments` takes "
      "`topicId: Int!`. Rapid probing trips a WAF returning HTML 403s, not JSON.")
    w("- Reddit: `.json` is 403 for anonymous clients but **the `.rss` twin of the same "
      "path is not**. Search feeds page with `after=t3_<id>`; comment feeds are the post "
      "permalink + `.rss`, first entry being the post itself. Rate limiting runs on a "
      "rolling window — ~8 s between requests is the floor, and a 429 needs a ~90 s sleep "
      "rather than a quick retry.")
    w("- Blind: no pagination at all (`?page=2` re-serves page 1, 20 cards per query), so "
      "breadth comes from several queries. Card bodies truncate at ~312 chars, hence the "
      "per-post fetch; comments are client-rendered and unreachable. The card's exact date "
      "hides in a `title=\"MM/DD/YYYY\"` attribute next to the human one.")
    w("- Hacker News: Algolia is the easy one — public, unauthenticated, `hitsPerPage` up "
      "to 1000. Matching is fuzzy, so expect noise.\n")

    # Surface sibling docs about the same company, rather than a hardcoded Google list.
    doc_dir = os.path.join(REPO_ROOT, "doc")
    needles = {ctx.tag, ctx.tag[:4]} | ({"goog"} if ctx.tag == "google" else set())
    related = sorted(
        name for name in (os.listdir(doc_dir) if os.path.isdir(doc_dir) else [])
        if name.endswith(".md") and name != os.path.basename(out_path)
        and any(n in name.lower() for n in needles))
    if related:
        w("## 4) Related docs in this repo\n")
        for name in related:
            w(f"- [`doc/{name}`](./{name})")
        w("")

    os.makedirs(os.path.dirname(out_path), exist_ok=True)
    with open(out_path, "w") as fh:
        fh.write("\n".join(lines))


# -------------------------------------------------------------------------- main
def main():
    ap = argparse.ArgumentParser(
        description="Scrape recently-asked LC problems for a company from LeetCode "
                    "Discuss, Reddit, Blind and Hacker News.",
        formatter_class=argparse.RawDescriptionHelpFormatter,
        epilog=__doc__.split("WHY THIS EXISTS")[0])
    ap.add_argument("--tag", default="google",
                    help="company to scrape; also the LeetCode discuss tag slug "
                         "(default: google)")
    ap.add_argument("--sources", default=",".join(SOURCES),
                    help=f'comma-separated subset of {",".join(SOURCES)} (default: all)')
    ap.add_argument("--out", default=None,
                    help="output markdown path (default: doc/<g|tag>_recent_asked.md)")
    ap.add_argument("--cache-dir", default=None,
                    help="cache dir (default: data/.lc_discuss_cache/<tag>/)")
    ap.add_argument("--delay", type=float, default=None,
                    help=f"seconds between requests, overriding the per-source defaults "
                         f"{DELAYS}")
    ap.add_argument("--per-page", type=int, default=25,
                    help="LeetCode list page size (default: 25)")
    ap.add_argument("--max-pages", type=int, default=None,
                    help="cap list pages per source (default: page to exhaustion)")
    ap.add_argument("--reddit-subs", default=",".join(REDDIT_SUBS),
                    help=f'subreddits to search (default: {",".join(REDDIT_SUBS)})')
    ap.add_argument("--no-comments", action="store_true",
                    help="skip comment fetching (much faster, but comments are where the "
                         "actual questions usually are)")
    ap.add_argument("--build-only", action="store_true",
                    help="rebuild the report from cache without any network calls")
    ap.add_argument("--refresh-index", action="store_true",
                    help="re-download the LeetCode problem index")
    args = ap.parse_args()
    # Runs take hours; keep progress visible when stdout is a pipe or a log file.
    sys.stdout.reconfigure(line_buffering=True)

    sources = [s.strip() for s in args.sources.split(",") if s.strip()]
    unknown = [s for s in sources if s not in SOURCES]
    if unknown:
        sys.exit(f'unknown source(s): {",".join(unknown)} — pick from {",".join(SOURCES)}')
    args.reddit_subs = [s.strip() for s in args.reddit_subs.split(",") if s.strip()]

    root = args.cache_dir or os.path.join(REPO_ROOT, "data", ".lc_discuss_cache", args.tag)
    default_name = "g_recent_asked.md" if args.tag == "google" else f"{args.tag}_recent_asked.md"
    out_path = args.out or os.path.join(REPO_ROOT, "doc", default_name)
    ctx = Ctx(args, root)

    posts = []
    for i, name in enumerate(sources, 1):
        print(f"[{i}/{len(sources)}] {LABELS[name]}"
              f'{" (cache only)" if args.build_only else ""}')
        got = SOURCES[name](ctx) or []
        print(f"  -> {len(got)} posts")
        posts.extend(got)
    if not posts:
        sys.exit("no posts found — check the tag, or the sites may be blocking us")

    by_slug, by_num, by_title = load_problem_index(
        Cache(root), args.refresh_index, ctx.delay("leetcode"))
    ranked = extract(posts, by_slug, by_num, by_title)
    render(ctx, posts, ranked, load_repo_solved(), out_path, sources,
           time.strftime("%Y-%m-%d"))

    method_counts = collections.Counter(
        "url" if h["methods"]["url"] else "num" if h["methods"]["number"] else "title"
        for h in ranked)
    shown = os.path.relpath(out_path, REPO_ROOT)
    per_source = collections.Counter(p["source"] for p in posts)
    print(f"\nwrote {out_path if shown.startswith('..') else shown}")
    print(f"  posts={len(posts)} {dict(per_source)}")
    print(f"  problems={len(ranked)} by strongest match: {dict(method_counts)}")


if __name__ == "__main__":
    main()

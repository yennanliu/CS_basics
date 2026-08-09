#!/usr/bin/env python3
"""Scrape recently-asked LeetCode problems for a company from LeetCode's public Discuss forum.

Produces a markdown report (default `doc/g_recent_asked.md` for Google) listing the LC
problems referenced in recent company-tagged discuss threads, with evidence quotes and
links back to every source post.

    python3 script/scrape_lc_discuss_company.py                     # google -> doc/g_recent_asked.md
    python3 script/scrape_lc_discuss_company.py --tag meta          # -> doc/meta_recent_asked.md
    python3 script/scrape_lc_discuss_company.py --build-only        # rebuild doc from cache, no network
    python3 script/scrape_lc_discuss_company.py --max-pages 4       # quick sample

Everything downloaded is cached under `--cache-dir` (default `data/.lc_discuss_cache/<tag>/`),
one file per post, so re-runs resume instead of re-fetching. Delete the cache dir for a
clean pull, or pass `--refresh-index` to re-download just the LC problem index.

WHY THIS EXISTS / WHAT IT IS NOT
--------------------------------
LeetCode's official company question list (`companyTag`) is Premium-gated and returns
`null` to anonymous requests. This scrapes the *public* Discuss forum instead, so the
output is self-reported interview experience, not LeetCode's own frequency data. Treat
the counts as weak signal.

Note also that the *legacy* discuss API (`categoryTopicList`, category
`interview-question`) still responds but is frozen at 2025-03-04 — LeetCode migrated
Discuss during 2025. Live data lives behind the `ugcArticle*` fields used here.

SCHEMA NOTES (introspection is disabled; these were found by reading error messages)
-----------------------------------------------------------------------------------
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
* Rapid requests trip a WAF that returns HTML 403s rather than JSON — keep ~2-3s
  between calls and parse defensively.
"""

import argparse
import collections
import json
import os
import re
import sys
import time
import urllib.error
import urllib.request

GRAPHQL_URL = "https://leetcode.com/graphql/"
PROBLEM_INDEX_URL = "https://leetcode.com/api/problems/all/"
HEADERS = {
    "Content-Type": "application/json",
    "Accept": "*/*",
    "Accept-Language": "en-US,en;q=0.9",
    "Origin": "https://leetcode.com",
    "Referer": "https://leetcode.com/discuss/",
    "User-Agent": (
        "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 "
        "(KHTML, like Gecko) Chrome/131.0.0.0 Safari/537.36"
    ),
}
REPO_ROOT = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))

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


# --------------------------------------------------------------------------- net
def gql(query, variables, delay, tries=3, quiet=False):
    """POST a GraphQL query. Returns `data`, or None if the query is unrecoverable."""
    body = json.dumps({"query": query, "variables": variables}).encode()
    for attempt in range(tries):
        try:
            req = urllib.request.Request(GRAPHQL_URL, data=body, headers=HEADERS)
            with urllib.request.urlopen(req, timeout=45) as resp:
                payload = json.loads(resp.read().decode())
        except urllib.error.HTTPError as exc:
            detail = exc.read().decode()[:300]
            if exc.code == 400:
                # Malformed query - retrying cannot help.
                print(f"    HTTP 400 (bad query): {detail}", file=sys.stderr)
                return None
            if not quiet:
                print(f"    HTTP {exc.code}; backing off", file=sys.stderr)
            time.sleep(max(45, delay * 18) * (attempt + 1))
            continue
        except Exception as exc:  # network hiccup, or WAF returning HTML instead of JSON
            if not quiet:
                print(f"    {type(exc).__name__}: {exc}; retrying", file=sys.stderr)
            time.sleep(max(30, delay * 12) * (attempt + 1))
            continue

        if payload.get("errors"):
            msg = payload["errors"][0].get("message", "")
            print(f"    GraphQL error: {msg[:200]}", file=sys.stderr)
            return None
        return payload.get("data")
    return None


# ------------------------------------------------------------------------ stages
def fetch_post_list(tag, cache_dir, delay, per_page, max_pages):
    """Stage 1: page the discuss list to exhaustion. Always re-run (cheap, and it is
    the only way to notice new threads)."""
    dst = os.path.join(cache_dir, "posts.json")
    posts = {}
    if os.path.exists(dst):
        posts = {n["uuid"]: n for n in json.load(open(dst))}
    skip, page = 0, 0
    while max_pages is None or page < max_pages:
        data = gql(LIST_Q, {"keywords": [], "tagSlugs": [tag],
                            "skip": skip, "first": per_page}, delay)
        conn = (data or {}).get("ugcArticleDiscussionArticles")
        if not conn:
            print(f"  list stopped at skip={skip} (no data)", file=sys.stderr)
            break
        edges = conn["edges"]
        for edge in edges:
            posts[edge["node"]["uuid"]] = edge["node"]
        print(f"  skip={skip:<5} got={len(edges):<3} unique={len(posts)}")
        # `totalNum` is capped and unreliable; a short page is the real end marker.
        if len(edges) < per_page:
            break
        skip += per_page
        page += 1
        time.sleep(delay)
    with open(dst, "w") as fh:
        json.dump(list(posts.values()), fh)
    return posts


def fetch_bodies(posts, cache_dir, delay):
    """Stage 2: one call per post; the list endpoint does not return bodies."""
    out_dir = os.path.join(cache_dir, "bodies")
    os.makedirs(out_dir, exist_ok=True)
    todo = [n for n in posts.values()
            if n.get("topicId") and not os.path.exists(os.path.join(out_dir, f'{n["uuid"]}.json'))]
    print(f"  {len(todo)} bodies to fetch ({len(posts) - len(todo)} cached)")
    for i, n in enumerate(todo, 1):
        data = gql(BODY_Q, {"topicId": n["topicId"]}, delay)
        node = (data or {}).get("ugcArticleDiscussionArticle")
        if data is None:
            print("  aborting body stage (unrecoverable)", file=sys.stderr)
            break
        with open(os.path.join(out_dir, f'{n["uuid"]}.json'), "w") as fh:
            json.dump(node or {"uuid": n["uuid"], "content": ""}, fh)
        if i % 25 == 0:
            print(f"    {i}/{len(todo)}")
        time.sleep(delay)


def fetch_comments(posts, cache_dir, delay, max_comment_pages=4):
    """Stage 3: comments, paged. This is where the actual questions usually are —
    most threads are compensation/team-match chatter with the problems in replies."""
    out_dir = os.path.join(cache_dir, "comments")
    os.makedirs(out_dir, exist_ok=True)
    todo = [n for n in posts.values()
            if n.get("topicId") and not os.path.exists(os.path.join(out_dir, f'{n["uuid"]}.json'))]
    print(f"  {len(todo)} comment threads to fetch ({len(posts) - len(todo)} cached)")
    for i, n in enumerate(todo, 1):
        got, page = [], 1
        while page <= max_comment_pages:
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
        with open(os.path.join(out_dir, f'{n["uuid"]}.json'), "w") as fh:
            json.dump({"uuid": n["uuid"], "comments": got}, fh)
        if i % 25 == 0:
            print(f"    {i}/{len(todo)}")
        time.sleep(delay)


def load_problem_index(cache_dir, refresh):
    """LeetCode's public problem index: slug/number/title/difficulty for every problem."""
    dst = os.path.join(cache_dir, "all_problems.json")
    if refresh or not os.path.exists(dst):
        print("  downloading LC problem index")
        req = urllib.request.Request(PROBLEM_INDEX_URL, headers=HEADERS)
        with urllib.request.urlopen(req, timeout=60) as resp:
            open(dst, "wb").write(resp.read())
    raw = json.load(open(dst))["stat_status_pairs"]
    difficulty = {1: "Easy", 2: "Medium", 3: "Hard"}
    by_slug, by_num, by_title = {}, {}, {}
    for entry in raw:
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
        return {int(p["id"]): p for p in json.load(open(path))["problems"]}
    except Exception:
        return {}


# -------------------------------------------------------------------- extraction
URL_RE = re.compile(r"leetcode\.com/problems/([a-z0-9][a-z0-9\-]{2,})", re.I)
# "LC 200" / "leetcode 200" take 1+ digits; a bare "#200" needs 2+ so that prose like
# "#1 priority" does not resolve to a real problem number.
NUM_RE = re.compile(r"(?:\bLC\s*#?\s*|\bleetcode\s*#?\s*)(\d{1,4})\b"
                    r"|(?<![\w.])#(\d{2,4})\b", re.I)
# Titles that are ordinary English and would match constantly in prose.
BAD_TITLES = {"design", "sort colors", "word break", "jump game", "candy",
              "trapping rain water"}


def build_title_regex(by_title):
    """Only distinctive titles: long enough and multi-word, else prose false-positives."""
    candidates = {t: r for t, r in by_title.items() if len(t) >= 14 and " " in t}
    pattern = "|".join(sorted((re.escape(t) for t in candidates), key=len, reverse=True))
    return candidates, re.compile(r"(?<!\w)(" + pattern + r")(?!\w)", re.I)


def extract(posts, comments, by_slug, by_num, by_title):
    candidates, title_re = build_title_regex(by_title)
    hits = {}

    def record(rec, post, how, text, pos):
        hit = hits.setdefault(rec["num"], {"rec": rec, "posts": {}, "seen": set(),
                                           "evidence": [], "methods": collections.Counter()})
        hit["posts"][post["uuid"]] = post
        hit["methods"][how] += 1
        if post["uuid"] in hit["seen"]:
            return  # one quote per (problem, thread): url/num/title all fire on one sentence
        hit["seen"].add(post["uuid"])
        start = max(0, pos - 70)
        hit["evidence"].append({
            "snippet": re.sub(r"\s+", " ", text[start:start + 200]).strip(),
            "post": post["title"], "date": post["createdAt"][:10],
            "url": post_url(post), "how": how})

    for post in posts.values():
        chunks = [post.get("title") or "", post.get("summary") or "", post.get("content") or ""]
        chunks += [(c.get("post") or {}).get("content") or ""
                   for c in comments.get(post["uuid"], [])]
        text = "\n".join(chunks)
        for m in URL_RE.finditer(text):
            rec = by_slug.get(m.group(1).lower())
            if rec:
                record(rec, post, "url", text, m.start())
        for m in NUM_RE.finditer(text):
            rec = by_num.get(int(m.group(1) or m.group(2)))
            if rec:
                record(rec, post, "number", text, m.start())
        for m in title_re.finditer(text):
            key = m.group(1).lower()
            if key in BAD_TITLES:
                continue
            rec = candidates.get(key)
            if rec:
                record(rec, post, "title", text, m.start())

    return sorted(hits.values(),
                  key=lambda h: (-len(h["posts"]), -sum(h["methods"].values()), h["rec"]["num"]))


# ------------------------------------------------------------------------ report
def post_url(node):
    return f'https://leetcode.com/discuss/post/{node["topicId"]}/{node.get("slug") or ""}/'


def fit_tags(slugs, budget=60):
    """Drop whole tags that do not fit; never cut a slug mid-token."""
    kept, used = [], 0
    for slug in slugs:
        need = len(slug) + (2 if kept else 0)
        if used + need > budget:
            return ", ".join(kept) + f" +{len(slugs) - len(kept)} more"
        kept.append(slug)
        used += need
    return ", ".join(kept)


INTERVIEW_HINT = re.compile(
    r"interview|onsite|phone screen|screen|round|oa\b|online assessment|asked|coding", re.I)
EV_PROBLEMS, EV_QUOTES = 25, 3


def render(tag, posts, comments, ranked, solved, out_path, generated_on):
    company = tag.replace("-", " ").title()
    recent = sorted(posts.values(), key=lambda n: n["createdAt"], reverse=True)
    dates = [n["createdAt"][:10] for n in recent]
    n_bodies = sum(1 for n in posts.values() if n.get("content"))
    n_comments = sum(len(v) for v in comments.values())
    lines = []
    w = lines.append

    def type_of(rec):
        entry = solved.get(rec["num"])
        return ", ".join(entry["tags"][:3]) if entry and entry.get("tags") else "—"

    w(f"# {company} SWE — Recently Asked LeetCode Questions (scraped)\n")
    w(f"> **Generated**: {generated_on}  ")
    w("> **Source**: `leetcode.com/graphql` — public Discuss API "
      "(`ugcArticleDiscussionArticles`, `ugcArticleDiscussionArticle`, `topicComments`)  ")
    w(f"> **Regenerate**: `python3 script/scrape_lc_discuss_company.py --tag {tag}`  ")
    w(f"> **Corpus**: {len(posts)} `{tag}`-tagged discuss posts, {dates[-1]} → {dates[0]} "
      f"({n_bodies} full bodies, {n_comments} comments)\n")

    w("## ⚠️ Read this first — what this data is and is not\n")
    w("- LeetCode's **official company tag list** (`companyTag`) is **Premium-gated** and "
      "returns `null` for anonymous requests. This doc is **not** that list.")
    w(f"- What is scraped here is **user-reported interview experience** from the public "
      f"Discuss forum, tagged `{tag}`. It is self-reported, unverified, and skewed toward "
      "whoever bothers to post.")
    w("- The **legacy** discuss API (`categoryTopicList`, category `interview-question`) is "
      "frozen at **2025-03-04** — LeetCode migrated Discuss during 2025. Anything claiming to "
      "scrape \"recent\" questions from that endpoint is serving stale data.")
    w("- Treat problem counts as **weak signal** (mention frequency), not ground-truth "
      "interview frequency. A single well-linked compilation post can put a dozen problems on "
      "the board at once.")
    w("- Mentions are **not all interview reports** — some describe a practice routine, and a "
      "title match can even land inside a sentence saying the problem is *not* what was asked. "
      "The `Match` column and the quotes exist so you can check.\n")

    w("## 1) Most-referenced LC problems\n")
    w("**`Posts`** = number of **distinct discuss threads** referencing the problem anywhere in "
      "`title + summary + body + comments`. It counts threads, not mentions: a thread naming the "
      "same problem five times counts once, so `Posts` is *not* the sum of the quotes below.\n")
    w("`Match` = how the reference was found, showing the **strongest** evidence anywhere in "
      "that thread set. **url** = the post linked `leetcode.com/problems/<slug>` (high "
      "confidence); **num** = wrote `LC 200` / `#200`; **title** = the exact title appeared in "
      "prose — weakest, worth eyeballing the quote before trusting it.\n")
    w("The table below is **complete** — every problem extracted from the corpus is listed.\n")
    if ranked:
        w("| # | Problem | Diff | Type / Tags | Posts | Match | Last seen | In repo? |")
        w("|---|---------|------|-------------|-------|-------|-----------|----------|")
        for hit in ranked:
            rec = hit["rec"]
            link = f'https://leetcode.com/problems/{rec["slug"]}/'
            paid = " 🔒" if rec["paid_only"] else ""
            # Strongest evidence, not most frequent: one link outweighs ten prose mentions.
            how = ("url" if hit["methods"]["url"]
                   else "num" if hit["methods"]["number"] else "title")
            last = max(p["createdAt"][:10] for p in hit["posts"].values())
            w(f'| {rec["num"]} | [{rec["title"]}]({link}){paid} | {rec["difficulty"]} '
              f'| {type_of(rec)} | {len(hit["posts"])} | {how} | {last} '
              f'| {"✅" if rec["num"] in solved else "—"} |')
    else:
        w("_No LC problem references extracted from the scraped corpus._")
    w("")

    w("### Evidence (quotes from the scraped posts)\n")
    w(f"**This is a sample, not a full audit trail.** It covers the top {EV_PROBLEMS} problems "
      f"of {len(ranked)}, with at most {EV_QUOTES} quotes each (one per thread, from the first "
      "match in that thread). Where a problem has more threads than quotes shown, the surplus is "
      "noted inline. For the rest, follow the links in the table and section 2.\n")
    for hit in ranked[:EV_PROBLEMS]:
        rec = hit["rec"]
        n_posts = len(hit["posts"])
        extra = n_posts - min(len(hit["evidence"]), EV_QUOTES)
        more = f' — _{extra} further thread{"s" if extra > 1 else ""} not quoted_' if extra > 0 else ""
        w(f'**LC {rec["num"]} — {rec["title"]}** ({rec["difficulty"]}) · '
          f'{n_posts} thread{"s" if n_posts > 1 else ""}{more}  ')
        for ev in hit["evidence"][:EV_QUOTES]:
            w(f'- _{ev["date"]}_ · [{ev["post"][:70]}]({ev["url"]})  ')
            w(f'  > …{ev["snippet"][:230]}…')
        w("")

    w("## 2) Recent interview posts (raw feed)\n")
    w("Newest first — the primary sources. Open them for full text and comment threads.\n")
    w(f"Every row already carries the `{tag}` tag (that is the scrape filter: "
      f"`tagSlugs: [\"{tag}\"]`), so it is omitted from `Tags` as redundant. Long tag lists are "
      "trimmed at a whole-tag boundary with the remainder as `+N more`.\n")
    w(f"| Date | Post | Tags (excl. `{tag}`) | Views |")
    w("|------|------|------------------------|-------|")
    for node in recent:
        if not INTERVIEW_HINT.search(node["title"] + " " + (node.get("summary") or "")):
            continue
        tags = fit_tags([t["slug"] for t in (node.get("tags") or []) if t["slug"] != tag])
        title = node["title"].replace("|", "\\|").strip()[:95]
        w(f'| {node["createdAt"][:10]} | [{title}]({post_url(node)}) | {tags} '
          f'| {node.get("hitCount", 0)} |')
    w("")

    w("## 3) Method\n")
    w(f"Generated by [`script/scrape_lc_discuss_company.py`](../script/"
      f"scrape_lc_discuss_company.py). The figures above come from a **full paginated run**: "
      f"all {len(posts)} threads listed by paging `skip` to exhaustion, then every thread's body "
      f"and comments fetched individually ({n_bodies} bodies, {n_comments} comments).\n")
    w("```bash")
    w(f"python3 script/scrape_lc_discuss_company.py --tag {tag}   # full run (slow, ~2.5s/request)")
    w("python3 script/scrape_lc_discuss_company.py --build-only  # rebuild doc from cache")
    w("```\n")
    w("Three calls are involved, because the list endpoint does **not** return bodies:\n")
    w("| Stage | Field | Paginate with | Returns |")
    w("|-------|-------|---------------|---------|")
    w("| 1 | `ugcArticleDiscussionArticles` | `skip` += `first` | thread list + `summary` "
      "(**no body**) |")
    w("| 2 | `ugcArticleDiscussionArticle` | one call per `topicId` | post body (`content`) |")
    w("| 3 | `topicComments` | `pageNo` 1..n | comment threads |\n")
    w("**Schema gotchas** (introspection is disabled; all found by reading error messages):\n")
    w("- `tagSlugs` is required on `ugcArticleDiscussionArticles`; omitting it returns "
      "`argument of type 'NoneType' is not iterable`.")
    w("- Variable types must be exact: `$keywords: [String]!` but `$tagSlugs: [String!]`.")
    w("- `ugcArticleDiscussionArticle` keys off **`topicId`**, not `uuid`.")
    w("- **The two `topicId` arguments have different types, and this is not a typo**: "
      "`ugcArticleDiscussionArticle` takes `ID`, `topicComments` takes `Int!`. Using one type "
      "for both fails on whichever call you guessed wrong.")
    w("- `content` is **null in list mode** — only `summary` is populated; bodies need stage 2.")
    w("- `totalNum` on the list connection is **capped** (3000), not the real result count — "
      "page until a short page instead of trusting it.")
    w("- `topicComments.orderBy` is a plain `String` (`most_votes` / `newest_to_oldest` / "
      "`oldest_to_newest` / `hot`), not an enum.")
    w("- Rapid probing trips a WAF returning **HTML 403s, not JSON** — parse defensively and "
      "keep ~2–3 s between requests.\n")

    # Surface sibling docs about the same company, rather than a hardcoded Google list.
    doc_dir = os.path.join(REPO_ROOT, "doc")
    needles = {tag, tag[:4]} | ({"goog"} if tag == "google" else set())
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
        description="Scrape recently-asked LC problems for a company from LeetCode Discuss.",
        formatter_class=argparse.RawDescriptionHelpFormatter,
        epilog=__doc__.split("WHY THIS EXISTS")[0])
    ap.add_argument("--tag", default="google",
                    help="LeetCode discuss tag slug to scrape (default: google)")
    ap.add_argument("--out", default=None,
                    help="output markdown path (default: doc/<g|tag>_recent_asked.md)")
    ap.add_argument("--cache-dir", default=None,
                    help="cache dir (default: data/.lc_discuss_cache/<tag>/)")
    ap.add_argument("--delay", type=float, default=2.5,
                    help="seconds between requests; below ~2s trips the WAF (default: 2.5)")
    ap.add_argument("--per-page", type=int, default=25, help="list page size (default: 25)")
    ap.add_argument("--max-pages", type=int, default=None,
                    help="cap list pages (default: page to exhaustion)")
    ap.add_argument("--no-comments", action="store_true",
                    help="skip comment fetching (much faster, but comments are where the "
                         "actual questions usually are)")
    ap.add_argument("--build-only", action="store_true",
                    help="rebuild the report from cache without any network calls")
    ap.add_argument("--refresh-index", action="store_true",
                    help="re-download the LeetCode problem index")
    args = ap.parse_args()

    cache_dir = args.cache_dir or os.path.join(REPO_ROOT, "data", ".lc_discuss_cache", args.tag)
    os.makedirs(cache_dir, exist_ok=True)
    default_name = "g_recent_asked.md" if args.tag == "google" else f"{args.tag}_recent_asked.md"
    out_path = args.out or os.path.join(REPO_ROOT, "doc", default_name)

    if args.build_only:
        posts_file = os.path.join(cache_dir, "posts.json")
        if not os.path.exists(posts_file):
            sys.exit(f"no cache at {cache_dir} — run without --build-only first")
        posts = {n["uuid"]: n for n in json.load(open(posts_file))}
        print(f"[build-only] {len(posts)} posts from cache")
    else:
        print(f"[1/3] listing `{args.tag}` discuss posts")
        posts = fetch_post_list(args.tag, cache_dir, args.delay, args.per_page, args.max_pages)
        if not posts:
            sys.exit("no posts found — check the tag slug, or the API/WAF may be blocking")
        print(f"[2/3] fetching post bodies")
        fetch_bodies(posts, cache_dir, args.delay)
        if args.no_comments:
            print("[3/3] skipping comments (--no-comments)")
        else:
            print(f"[3/3] fetching comments")
            fetch_comments(posts, cache_dir, args.delay)

    # Merge cached bodies + comments into the post records.
    body_dir = os.path.join(cache_dir, "bodies")
    if os.path.isdir(body_dir):
        for name in os.listdir(body_dir):
            node = json.load(open(os.path.join(body_dir, name)))
            uuid = name[:-5]
            if uuid in posts and node.get("content"):
                posts[uuid]["content"] = node["content"]
    comments = {}
    comment_dir = os.path.join(cache_dir, "comments")
    if os.path.isdir(comment_dir):
        for name in os.listdir(comment_dir):
            entry = json.load(open(os.path.join(comment_dir, name)))
            comments[entry["uuid"]] = entry["comments"]

    by_slug, by_num, by_title = load_problem_index(cache_dir, args.refresh_index)
    ranked = extract(posts, comments, by_slug, by_num, by_title)
    generated_on = time.strftime("%Y-%m-%d")
    render(args.tag, posts, comments, ranked, load_repo_solved(), out_path, generated_on)

    method_counts = collections.Counter(
        "url" if h["methods"]["url"] else "num" if h["methods"]["number"] else "title"
        for h in ranked)
    shown = os.path.relpath(out_path, REPO_ROOT)
    print(f"\nwrote {out_path if shown.startswith('..') else shown}")
    print(f"  posts={len(posts)} bodies={sum(1 for n in posts.values() if n.get('content'))} "
          f"comments={sum(len(v) for v in comments.values())}")
    print(f"  problems={len(ranked)} by strongest match: {dict(method_counts)}")


if __name__ == "__main__":
    main()

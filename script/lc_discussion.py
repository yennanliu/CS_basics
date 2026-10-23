#!/usr/bin/env python3
"""
Search recent LeetCode Discuss posts by keyword, read the ones worth reading,
and line the problems they name up against this repo's index and practice log.

    python3 script/lc_discussion.py search google                 # last 90 days, newest first
    python3 script/lc_discussion.py search google l3 --days 30    # every term must appear
    python3 script/lc_discussion.py search "meta e4" --order MOST_RELEVANT --pages 2
    python3 script/lc_discussion.py search google --json hits.json
    python3 script/lc_discussion.py fetch 8543506 8527382         # full text of chosen posts
    python3 script/lc_discussion.py fetch 8543506 --dir /tmp/posts
    python3 script/lc_discussion.py xref 1235 3026 963            # offline: README row + log verdict
    python3 script/lc_discussion.py snippets hits.json google     # offline: saved Blind/Glassdoor results

Why a script. leetcode.com/discuss/ renders in the browser, so a plain page
fetch returns an empty shell. The same GraphQL endpoint the page calls answers
without a login: `ugcArticleDiscussionArticles` lists posts (title, summary,
date, upvotes) and `ugcArticleDiscussionArticle` returns one post's markdown.

Why a filter. The API's keyword match is loose — a search for "google" returns
roadmap posts and AI/ML career threads that mention the word once — so by
default every search term has to appear in the title or summary. `--loose`
turns that off.

`xref` is the part that makes a summary actionable, and it never touches the
network: for each LC number it prints README's title and status cell and the
practice log's latest verdict, using the same parsers as script/l3_core.py and
script/suggest_review.py, so the three cannot disagree about a line.

How it differs from script/scrape_lc_discuss_company.py. That one is the bulk
pass: every post for a company tag, comments included, ~1 hour, ranked by how
often an LC number is mentioned, written as an English report. This one is the
interactive pass behind /lc-discussion: any keywords, a few calls, the posts
read by the agent, so a question that names no LC number is still mapped. It
reuses that script's `gql()` (retries, WAF back-off) rather than a second
client.

Blind and Glassdoor are read through search-engine snippets only, never
fetched: Blind's robots.txt opts out of AI crawlers and Glassdoor blocks
scripts. `snippets` shortlists the results the agent saved from its searches
(see the section above `source_of`).

It never writes README.md or data/progress.txt.
"""
import argparse
import json
import os
import re
import sys
import time
from collections import Counter
from urllib.parse import urlparse

HERE = os.path.dirname(os.path.abspath(__file__))
ROOT = os.path.dirname(HERE)
sys.path.insert(0, HERE)

ENDPOINT = "https://leetcode.com/graphql/"
POST_URL = "https://leetcode.com/discuss/post/{topic}/{slug}/"
PAGE_SIZE = 50
ORDERS = ("MOST_RECENT", "MOST_RELEVANT", "MOST_VOTES", "HOT")

LIST_QUERY = """
query discussPostItems($orderBy: ArticleOrderByEnum, $keywords: [String]!,
                       $tagSlugs: [String!], $skip: Int, $first: Int) {
  ugcArticleDiscussionArticles(orderBy: $orderBy, keywords: $keywords,
                               tagSlugs: $tagSlugs, skip: $skip, first: $first) {
    totalNum
    edges { node { title slug summary createdAt topicId
                   tags { slug } reactions { count reactionType } } }
  }
}"""

POST_QUERY = """
query discussPostDetail($topicId: ID) {
  ugcArticleDiscussionArticle(topicId: $topicId) { title slug content createdAt topicId }
}"""


# ── Network ──────────────────────────────────────────────────────────────────
# Sustained requests under ~2s apart trip LeetCode's WAF (see the scraper's
# docstring); a search is four calls, so only `fetch` needs the pause.
DELAY = 1.5


def graphql(query, variables, delay=DELAY):
    from scrape_lc_discuss_company import gql
    data = gql(query, variables, delay)
    if data is None:
        raise SystemExit("LeetCode GraphQL request failed (see the message above)")
    return data


def list_posts(keywords, order, pages, days=None):
    """Up to `pages` pages; newest-first, so a page that ends past the `days`
    window is the last one worth asking for."""
    cutoff = time.time() - days * 86400 if days else None
    nodes = []
    for page in range(pages):
        data = graphql(LIST_QUERY, {"orderBy": order, "keywords": [" ".join(keywords)],
                                    "tagSlugs": [], "skip": page * PAGE_SIZE,
                                    "first": PAGE_SIZE})
        edges = data["ugcArticleDiscussionArticles"]["edges"]
        nodes += [e["node"] for e in edges]
        if len(edges) < PAGE_SIZE:
            break
        if cutoff and order == "MOST_RECENT" and \
                time.mktime(time.strptime(nodes[-1]["createdAt"][:10], "%Y-%m-%d")) < cutoff:
            break
    return nodes


# ── Pure helpers (tested offline) ────────────────────────────────────────────
def upvotes(node):
    return sum(r["count"] for r in node.get("reactions") or []
               if r.get("reactionType") == "UPVOTE")


def post_url(node):
    return POST_URL.format(topic=node["topicId"], slug=node["slug"])


def has_terms(text, keywords):
    """True when every whitespace-separated term of `keywords` occurs in `text`."""
    text = text.lower()
    return all(t.lower() in text for k in keywords for t in k.split())


def too_old(date, days, now):
    """True when a YYYY-MM-DD `date` falls outside the last `days` days."""
    if days is None:
        return False
    created = time.mktime(time.strptime(date[:10], "%Y-%m-%d"))
    return now - created > days * 86400


def shortlist(nodes, keywords, days=None, loose=False, now=None):
    """Dedupe, drop posts older than `days`, keep only posts whose title or
    summary carries every keyword term, newest first."""
    now = time.time() if now is None else now
    seen, out = set(), []
    for n in nodes:
        if n["topicId"] in seen:
            continue
        seen.add(n["topicId"])
        if too_old(n["createdAt"], days, now):
            continue
        if not loose and not has_terms(n.get("title", "") + " " + (n.get("summary") or ""), keywords):
            continue
        out.append(n)
    return sorted(out, key=lambda n: n["createdAt"], reverse=True)


LC_LINK_RE = re.compile(r"leetcode\.com/problems/([a-z0-9-]+)")
LC_NUM_RE = re.compile(r"\b(?:LC|LeetCode)\s*#?\s*(\d{1,4})\b", re.I)


def named_problems(text):
    """-> (slugs, numbers) a post names explicitly. A guess from the story is
    not a name; the skill maps those by hand and labels them as a match."""
    return (sorted(set(LC_LINK_RE.findall(text))),
            sorted({int(n) for n in LC_NUM_RE.findall(text)}))


# ── Search-engine snippets: Blind and Glassdoor ──────────────────────────────
# Neither site is fetched. Blind's robots.txt disallows ClaudeBot and
# anthropic-ai site-wide, and Glassdoor answers scripts with a 403 bot wall.
# So the agent runs web searches (`site:teamblind.com google interview`) and
# saves each result's url, title, snippet and, when the snippet shows one, a
# YYYY-MM-DD date as a JSON list; `snippets` reads only that file.
SOURCES = (("teamblind", "Blind"), ("glassdoor", "Glassdoor"), ("leetcode", "LC"))


def source_of(url):
    """'Blind' / 'Glassdoor' / 'LC' by the URL's host, None for any other site.
    Matched on a whole host label, so glassdoor.co.uk counts and
    notteamblind.com does not."""
    labels = urlparse(url).netloc.lower().split(".")
    for label, name in SOURCES:
        if label in labels:
            return name
    return None


def canonical(url):
    """The URL with scheme, `www.`, query, fragment and trailing slash dropped,
    so one post found by two searches is one hit."""
    p = urlparse(url)
    return p.netloc.lower().removeprefix("www.") + p.path.rstrip("/")


def shortlist_snippets(hits, keywords, days=None, now=None):
    """Keep on-site, deduped hits whose title or snippet carries every keyword
    term, newest first. A hit with no date is kept (most snippets carry none)
    and sorts last. Each kept hit gains `source` and `named` (LC numbers)."""
    now = time.time() if now is None else now
    seen, out = set(), []
    for h in hits:
        source = source_of(h["url"])
        key = canonical(h["url"])
        if source is None or key in seen:
            continue
        seen.add(key)
        if h.get("date") and too_old(h["date"], days, now):
            continue
        text = h.get("title", "") + " " + h.get("snippet", "")
        if not has_terms(text, keywords):
            continue
        out.append(dict(h, source=source, named=named_problems(text)[1]))
    return sorted(out, key=lambda h: h.get("date") or "", reverse=True)


# ── Commands ─────────────────────────────────────────────────────────────────
def cmd_search(args):
    nodes = list_posts(args.keywords, args.order, args.pages, args.days)
    hits = shortlist(nodes, args.keywords, args.days, args.loose)
    for n in hits:
        print("%s %4d▲ %-9s %s" % (n["createdAt"][:10], upvotes(n), n["topicId"], n["title"].strip()))
        print("    %s" % post_url(n))
        if args.summary:
            print("    %s" % (n.get("summary") or "")[:240].replace("\n", " "))
    print("\n%d of %d fetched posts kept (keywords=%r, days=%s, order=%s)"
          % (len(hits), len(nodes), " ".join(args.keywords), args.days, args.order))
    if args.json:
        with open(args.json, "w", encoding="utf-8") as f:
            json.dump([dict(n, url=post_url(n), upvotes=upvotes(n)) for n in hits],
                      f, ensure_ascii=False, indent=1)
        print("wrote %s" % args.json)


def cmd_fetch(args):
    if args.dir:
        os.makedirs(args.dir, exist_ok=True)
    for i, topic in enumerate(args.topics):
        if i:
            time.sleep(DELAY)
        post = graphql(POST_QUERY, {"topicId": str(topic)})["ugcArticleDiscussionArticle"]
        if not post:
            print("===== %s: not found" % topic)
            continue
        slugs, nums = named_problems(post["content"] or "")
        head = "===== %s %s %s\n%s\nnamed: slugs=%s numbers=%s\n" % (
            post["topicId"], post["createdAt"][:10], post["title"].strip(),
            post_url(post), slugs or "-", nums or "-")
        if args.dir:
            path = os.path.join(args.dir, "%s.md" % post["topicId"])
            with open(path, "w", encoding="utf-8") as f:
                f.write(head + "\n" + (post["content"] or ""))
            print(head.rstrip() + "\n    -> %s" % path)
        else:
            print(head + (post["content"] or ""))


def cmd_snippets(args):
    with open(args.file, encoding="utf-8") as f:
        hits = json.load(f)
    kept = shortlist_snippets(hits, args.keywords, args.days)
    for h in kept:
        print("%-10s %-10s %s" % (h["source"], h.get("date") or "-", h.get("title", "").strip()))
        print("    %s" % h["url"])
        if args.summary:
            print("    %s" % h.get("snippet", "")[:240].replace("\n", " "))
        if h["named"]:
            print("    named: %s" % " ".join(map(str, h["named"])))
    by_source = Counter(h["source"] for h in kept)
    off_site = sum(1 for h in hits if source_of(h["url"]) is None)
    print("\n%d of %d results kept (%s); %d from other sites dropped"
          % (len(kept), len(hits),
             ", ".join("%s %d" % (s, by_source[s]) for _, s in SOURCES if by_source[s]) or "none",
             off_site))
    named = sorted({n for h in kept for n in h["named"]})
    if named:
        print("named: %s   (pass these to xref)" % " ".join(map(str, named)))


def cmd_xref(args):
    import l3_core
    import suggest_review as sr
    problems = sr.parse_readme(os.path.join(ROOT, "README.md"))
    log, _ = l3_core.latest_verdicts(os.path.join(ROOT, "data", "progress.txt"))
    core = set(l3_core.load_core())
    print("%-5s %-44s %-7s %-6s %-10s %s" % ("LC", "README title", "README", "log", "last", "tries"))
    for lc in args.ids:
        p = problems.get(lc)
        rec = log.get(lc, {})
        last = time.strftime("%Y-%m-%d", time.localtime(rec["ts"])) if rec.get("ts") else "-"
        print("%-5d %-44s %-7s %-6s %-10s %d%s" % (
            lc, (p["title"][:44] if p else "(not in README)"),
            (p["status"] or "-") if p else "-", rec.get("verdict", "never"), last,
            rec.get("attempts", 0), "  [L3 core]" if lc in core else ""))


def main(argv=None):
    ap = argparse.ArgumentParser(description=__doc__.split("\n\n")[0])
    sub = ap.add_subparsers(dest="cmd", required=True)

    s = sub.add_parser("search", help="list recent posts matching keywords")
    s.add_argument("keywords", nargs="+")
    s.add_argument("--days", type=int, default=90, help="drop posts older than this (default 90)")
    s.add_argument("--pages", type=int, default=4, help="pages of %d to fetch (default 4)" % PAGE_SIZE)
    s.add_argument("--order", choices=ORDERS, default="MOST_RECENT")
    s.add_argument("--loose", action="store_true", help="keep posts that miss a keyword term")
    s.add_argument("--summary", action="store_true", help="print each post's summary line")
    s.add_argument("--json", metavar="FILE", help="also write the kept posts as JSON")
    s.set_defaults(func=cmd_search)

    f = sub.add_parser("fetch", help="print the full markdown of posts by topic id")
    f.add_argument("topics", nargs="+")
    f.add_argument("--dir", help="write each post to DIR/<topicId>.md instead of stdout")
    f.set_defaults(func=cmd_fetch)

    n = sub.add_parser("snippets", help="shortlist saved Blind / Glassdoor search results (offline)")
    n.add_argument("file", help="JSON list of {url, title, snippet, date?} from web searches")
    n.add_argument("keywords", nargs="+")
    n.add_argument("--days", type=int, default=None, help="drop dated results older than this")
    n.add_argument("--summary", action="store_true", help="print each result's snippet")
    n.set_defaults(func=cmd_snippets)

    x = sub.add_parser("xref", help="README row and log verdict for LC numbers (offline)")
    x.add_argument("ids", nargs="+", type=int)
    x.set_defaults(func=cmd_xref)

    args = ap.parse_args(argv)
    args.func(args)


if __name__ == "__main__":
    main()

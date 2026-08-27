#!/usr/bin/env python3
"""Score LeetCode readiness against a Google SWE coding bar.

Pulls the public LeetCode GraphQL profile (no auth, no premium) and cross-references
it with this repo's README status column, then scores four axes:

  volume     - are enough problems solved, with the right Easy/Medium/Hard mix
  mastery    - how many solved problems are still marked AGAIN in README
  breadth    - per-topic coverage vs the topics Google actually asks
  signal     - contest rating + consistency, the only speed/pressure proxy available

Usage:
    python3 script/eval_lc_readiness.py                        # fetch + report (L3 bar)
    python3 script/eval_lc_readiness.py --level L4             # score against L4 instead
    python3 script/eval_lc_readiness.py --user someone_else
    python3 script/eval_lc_readiness.py --cache-dir /tmp/lc    # reuse fetched JSON
    python3 script/eval_lc_readiness.py --offline               # cache only, no network
    python3 script/eval_lc_readiness.py --json out.json         # machine-readable dump
"""

import argparse
import json
import os
import re
import sys
import urllib.error
import urllib.request
from collections import Counter, defaultdict
from datetime import datetime, timezone

GRAPHQL = "https://leetcode.com/graphql"
UA = (
    "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 "
    "(KHTML, like Gecko) Chrome/120.0 Safari/537.36"
)
ROOT = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))

# ---------------------------------------------------------------------------
# The bar, per level. A Google coding round is ~45 min for 1-2 problems, and the
# level changes what "enough" means rather than what gets asked:
#
#   L3  entry / new-grad. Coding rounds carry nearly the whole packet - there is
#       no system design round. Medium fluency is the bar; hard is upside, not a
#       requirement. Interviewers expect a clean medium, not a clever hard.
#   L4  mid. Same problems, less hand-holding, and hard-flavoured mediums show up
#       often enough that the hard tier has to be familiar.
#   L5  senior. Adds a system design round this data cannot see at all.
#
# Sources: Google's published interview guidance on topics, plus the widely
# reported medium-fluency threshold. Calibration, not official figures.
# ---------------------------------------------------------------------------
LEVELS = {
    "L3": {
        "volume": {"total": 300, "medium": 200, "hard": 60, "hard_share": 0.12},
        "signal": {"contest_rating": 1650, "contests": 8, "active_days_year": 120},
        # Advanced topics matter less at L3, so topic targets scale down together.
        "topic_scale": 0.70,
    },
    "L4": {
        "volume": {"total": 500, "medium": 300, "hard": 150, "hard_share": 0.20},
        "signal": {"contest_rating": 1800, "contests": 10, "active_days_year": 150},
        "topic_scale": 1.00,
    },
    "L5": {
        "volume": {"total": 600, "medium": 350, "hard": 200, "hard_share": 0.25},
        "signal": {"contest_rating": 1900, "contests": 12, "active_days_year": 150},
        "topic_scale": 1.15,
    },
}

# tagSlug -> (display, target solved, weight) ; weight = how often Google asks it
#
# LeetCode's skill-stats endpoint only reports a curated tag set, and silently
# omits Heap, BST, Prefix Sum and Intervals. For those, `fallback` is a regex run
# over the README rows so the topic is measured instead of scored as zero. A
# README count is a lower bound (only what this repo tracks), so it is flagged (~).
GOOGLE_TOPICS = [
    # slug,                   display,               target, weight, fallback
    ("array",                 "Array",                  120, 3, None),
    ("hash-table",            "Hash Table",              80, 3, None),
    ("string",                "String",                  80, 2, None),
    ("two-pointers",          "Two Pointers",            50, 2, None),
    ("sliding-window",        "Sliding Window",          40, 3, None),
    ("binary-search",         "Binary Search",           50, 3, None),
    ("sorting",               "Sorting",                 50, 1, None),
    ("stack",                 "Stack",                   40, 2, None),
    ("monotonic-stack",       "Monotonic Stack",         25, 2, None),
    ("heap-priority-queue",   "Heap / PQ",               45, 3,
     r"\bheap\b|priority ?queue|\bpq\b|kth largest|top k"),
    ("linked-list",           "Linked List",             30, 1, None),
    ("tree",                  "Tree",                    70, 2, None),
    # BST: the skill-stats API omits it and the README notes are full of "check
    # with BST" cross-references, so any regex over-counts. Tree coverage is the
    # honest proxy; left here as n/a rather than scored on a bad number.
    ("binary-search-tree",    "BST",                     25, 2, None),
    ("depth-first-search",    "DFS",                     80, 3, None),
    ("breadth-first-search",  "BFS",                     70, 3, None),
    ("graph",                 "Graph Theory",            60, 3, None),
    ("topological-sort",      "Topological Sort",        20, 3, None),
    ("shortest-path",         "Shortest Path (Dijkstra)",20, 3, None),
    ("union-find",            "Union-Find",              30, 3, None),
    ("trie",                  "Trie",                    25, 3, None),
    ("dynamic-programming",   "Dynamic Programming",    130, 3, None),
    ("backtracking",          "Backtracking",            50, 3, None),
    ("greedy",                "Greedy",                  70, 2, None),
    ("bit-manipulation",      "Bit Manipulation",        30, 1, None),
    ("math",                  "Math",                    60, 1, None),
    ("design",                "Design",                  40, 3, None),
    ("matrix",                "Matrix / Grid",           50, 2, None),
    ("divide-and-conquer",    "Divide & Conquer",        25, 1, None),
    ("segment-tree",          "Segment Tree",            15, 1, None),
    ("binary-indexed-tree",   "Binary Indexed Tree",     10, 1, None),
    ("prefix-sum",            "Prefix Sum",              40, 2,
     r"prefix ?sum|pre ?sum|presum|diff array|difference array"),
    ("sweep-line",            "Sweep Line / Intervals",  20, 2, None),
]



# ---------------------------------------------------------------------------
# Fetch
# ---------------------------------------------------------------------------
def gql(query, variables, user):
    body = json.dumps({"query": query, "variables": variables}).encode()
    req = urllib.request.Request(
        GRAPHQL,
        data=body,
        headers={
            "Content-Type": "application/json",
            "User-Agent": UA,
            "Referer": f"https://leetcode.com/u/{user}/",
        },
    )
    with urllib.request.urlopen(req, timeout=30) as r:
        return json.loads(r.read())


QUERIES = {
    "profile": """query q($username: String!) {
      matchedUser(username: $username) {
        username
        profile { ranking realName }
        submitStats {
          acSubmissionNum { difficulty count submissions }
          totalSubmissionNum { difficulty count submissions }
        }
      }
    }""",
    "tags": """query q($username: String!) {
      matchedUser(username: $username) {
        tagProblemCounts {
          advanced { tagName tagSlug problemsSolved }
          intermediate { tagName tagSlug problemsSolved }
          fundamental { tagName tagSlug problemsSolved }
        }
      }
    }""",
    "contest": """query q($username: String!) {
      userContestRanking(username: $username) {
        attendedContestsCount rating globalRanking totalParticipants topPercentage
      }
      userContestRankingHistory(username: $username) {
        attended rating ranking contest { title startTime }
      }
    }""",
    "recent": """query q($username: String!, $limit: Int) {
      recentAcSubmissionList(username: $username, limit: $limit) {
        title titleSlug timestamp
      }
    }""",
    "universe": """query pq($categorySlug: String, $limit: Int, $skip: Int,
                            $filters: QuestionListFilterInput) {
      problemsetQuestionList: questionList(categorySlug: $categorySlug, limit: $limit,
                                           skip: $skip, filters: $filters) {
        total: totalNum
      }
    }""",
    "calendar": """query q($username: String!, $year: Int) {
      matchedUser(username: $username) {
        userCalendar(year: $year) {
          activeYears streak totalActiveDays submissionCalendar
        }
      }
    }""",
}


def fetch_all(user, cache_dir, offline, year):
    """Return {key: payload}. Cached to cache_dir so re-runs are free."""
    out = {}
    os.makedirs(cache_dir, exist_ok=True)
    plan = [
        ("profile", {"username": user}),
        ("tags", {"username": user}),
        ("contest", {"username": user}),
        ("recent", {"username": user, "limit": 100}),
        ("calendar", {"username": user, "year": year}),
    ]
    for key, variables in plan:
        path = os.path.join(cache_dir, f"{key}.json")
        if offline or (os.path.exists(path) and offline):
            pass
        if offline:
            if not os.path.exists(path):
                sys.exit(f"--offline but {path} is missing; run once online first")
            out[key] = json.load(open(path))
            continue
        try:
            payload = gql(QUERIES[key], variables, user)
        except (urllib.error.URLError, urllib.error.HTTPError, TimeoutError) as e:
            if os.path.exists(path):
                print(f"warn: {key} fetch failed ({e}); using cache", file=sys.stderr)
                out[key] = json.load(open(path))
                continue
            sys.exit(f"error: {key} fetch failed and no cache: {e}")
        with open(path, "w") as f:
            json.dump(payload, f, indent=1)
        out[key] = payload

    # Per-tag problem totals, so topic coverage can be stated as a share of what
    # exists rather than against a target picked by hand. One request per tag,
    # cached in a single file.
    upath = os.path.join(cache_dir, "universe.json")
    universe = json.load(open(upath)) if os.path.exists(upath) else {}
    wanted = ["__all__"] + [t[0] for t in GOOGLE_TOPICS]
    missing = [t for t in wanted if t not in universe]
    if missing and not offline:
        for tag in missing:
            filters = {} if tag == "__all__" else {"tags": [tag]}
            try:
                d = gql(QUERIES["universe"],
                        {"categorySlug": "", "limit": 1, "skip": 0, "filters": filters},
                        user)
                universe[tag] = d["data"]["problemsetQuestionList"]["total"]
                if tag != "__all__" and universe[tag] == universe.get("__all__"):
                    universe[tag] = None   # filter was ignored -> bad slug
            except Exception as e:                       # non-fatal enrichment
                print(f"warn: universe fetch for {tag} failed: {e}", file=sys.stderr)
        with open(upath, "w") as f:
            json.dump(universe, f, indent=1)
    out["universe"] = universe
    return out


# ---------------------------------------------------------------------------
# README parsing
# ---------------------------------------------------------------------------
ROW = re.compile(r"^\|\s*(\d{1,4})\s*\|(.+)$")
STATUS = re.compile(r"\b(OK|AGAIN|NOT_OK|TODO)\b")
DIFF = re.compile(r"\b(Easy|Medium|Hard)\b")
COMPANIES = ("google", "amazon", "fb", "meta", "apple", "microsoft", "m\\$", "uber", "netflix")


def parse_readme(path):
    """Yield one dict per problem row, keyed by LC number (last row wins)."""
    problems = {}
    section = None
    with open(path, encoding="utf-8") as f:
        for line in f:
            if line.startswith("## "):
                section = line[3:].strip()
                continue
            m = ROW.match(line.rstrip())
            if not m:
                continue
            num = int(m.group(1))
            rest = m.group(2)
            cells = [c.strip() for c in rest.split("|")]
            status_cell = cells[-1] if cells[-1] else (cells[-2] if len(cells) > 1 else "")
            sm = STATUS.search(status_cell)
            dm = DIFF.search(rest)
            reps = status_cell.count("*")
            note = rest.lower()
            problems[num] = {
                "num": num,
                "section": section,
                "difficulty": dm.group(1) if dm else None,
                "status": sm.group(1) if sm else None,
                "reps": reps,
                "google": "google" in note,
                "must": "must" in note,
                "has_java": ".java" in rest,
                "has_python": "leetcode_python" in rest,
                "raw": note,
            }
    return problems


# ---------------------------------------------------------------------------
# Scoring
# ---------------------------------------------------------------------------
def score(value, target):
    """Ratio capped at 1.0; None-safe."""
    if not target:
        return 1.0
    if value is None:
        return 0.0
    return min(1.0, value / target)


def grade(pct):
    for cut, label in ((0.90, "A"), (0.78, "B+"), (0.66, "B"), (0.54, "C+"),
                       (0.42, "C"), (0.30, "D")):
        if pct >= cut:
            return label
    return "E"


def evaluate(data, problems, year, level="L4"):
    cfg = LEVELS[level]
    volume_targets, signal_targets = cfg["volume"], cfg["signal"]
    topic_scale = cfg["topic_scale"]
    universe = data.get("universe") or {}
    mu = data["profile"]["data"]["matchedUser"]
    ac = {d["difficulty"].lower(): d for d in mu["submitStats"]["acSubmissionNum"]}
    tot = {d["difficulty"].lower(): d for d in mu["submitStats"]["totalSubmissionNum"]}

    solved = {k: ac[k]["count"] for k in ("all", "easy", "medium", "hard")}
    accepted_subs = ac["all"]["submissions"]
    all_subs = tot["all"]["submissions"]

    tagmap = {}
    tc = data["tags"]["data"]["matchedUser"]["tagProblemCounts"]
    for bucket in ("fundamental", "intermediate", "advanced"):
        for t in tc[bucket]:
            tagmap[t["tagSlug"]] = {"name": t["tagName"], "solved": t["problemsSolved"],
                                    "bucket": bucket}

    cr = data["contest"]["data"]["userContestRanking"] or {}
    cal = data["calendar"]["data"]["matchedUser"]["userCalendar"]
    sc = json.loads(cal["submissionCalendar"])
    by_month = Counter()
    day_count = Counter()
    for ts, n in sc.items():
        d = datetime.fromtimestamp(int(ts), tz=timezone.utc)
        by_month[d.strftime("%Y-%m")] += n
        day_count[d.strftime("%Y-%m")] += 1

    # --- volume
    vol = {
        "total": (solved["all"], volume_targets["total"]),
        "medium": (solved["medium"], volume_targets["medium"]),
        "hard": (solved["hard"], volume_targets["hard"]),
    }
    hard_share = solved["hard"] / solved["all"] if solved["all"] else 0
    vol_score = sum(score(v, t) for v, t in vol.values()) / 3
    vol_score = 0.75 * vol_score + 0.25 * score(hard_share, volume_targets["hard_share"])

    # --- breadth
    topics = []
    for slug, disp, target, weight, fallback in GOOGLE_TOPICS:
        s = tagmap.get(slug, {}).get("solved")
        src = "api"
        if s is None and fallback:
            rx = re.compile(fallback)
            s = sum(1 for pr in problems.values() if rx.search(pr["raw"]))
            src = "readme"
        elif s is None:
            src = "missing"
        tot_tag = universe.get(slug) if src == "api" else None
        target = max(1, round(target * topic_scale))
        # A target above the tag's whole problem set is unreachable by definition.
        eff_target = min(target, tot_tag) if tot_tag else target
        topics.append({
            "slug": slug, "topic": disp, "solved": s, "target": eff_target,
            "weight": weight, "source": src,
            "score": None if src == "missing" else score(s, eff_target),
            "tag_total": tot_tag,
            "penetration": (s / tot_tag) if (tot_tag and s is not None) else None,
        })
    # Topics the API cannot report and that have no usable fallback are excluded
    # rather than scored as zero - an unmeasured topic is not a proven gap.
    scored = [t for t in topics if t["score"] is not None]
    wsum = sum(t["weight"] for t in scored)
    breadth_score = sum(t["score"] * t["weight"] for t in scored) / wsum

    # --- mastery (README status column)
    tracked = [p for p in problems.values() if p["status"]]
    ok = [p for p in tracked if p["status"] == "OK"]
    again = [p for p in tracked if p["status"] == "AGAIN"]
    mastery_ratio = len(ok) / len(tracked) if tracked else 0
    # a healthy spaced-repetition backlog is fine; being >70% AGAIN is not
    mastery_score = score(mastery_ratio, 0.55)

    by_section = defaultdict(lambda: {"ok": 0, "again": 0, "google": 0,
                                      "google_again": 0, "reps": 0})
    for p in tracked:
        b = by_section[p["section"] or "?"]
        b["ok" if p["status"] == "OK" else "again"] += 1
        b["reps"] += p["reps"]
        if p["google"]:
            b["google"] += 1
            if p["status"] == "AGAIN":
                b["google_again"] += 1

    # Chronic: still queued for review after many passes. The single most
    # actionable list in the report - these are the recurring blind spots.
    chronic = sorted(
        ({"num": p["num"], "reps": p["reps"], "difficulty": p["difficulty"],
          "section": p["section"], "google": p["google"], "must": p["must"]}
         for p in tracked if p["status"] == "AGAIN" and p["reps"] >= 12),
        key=lambda x: -x["reps"])

    # Submission efficiency: LeetCode gives no first-attempt stat, but
    # (failed / total) per difficulty is a usable proxy for how often a first
    # cut is wrong - the thing a whiteboard round actually punishes.
    efficiency = {}
    for d in ("easy", "medium", "hard"):
        t, a = tot[d]["submissions"], ac[d]["submissions"]
        efficiency[d] = {
            "problems": ac[d]["count"],
            "submissions": t,
            "accepted": a,
            "reject_rate": (t - a) / t if t else 0,
            "ac_per_problem": a / ac[d]["count"] if ac[d]["count"] else 0,
        }

    g_tracked = [p for p in tracked if p["google"]]
    g_ok = [p for p in g_tracked if p["status"] == "OK"]
    must = [p for p in tracked if p["must"]]
    must_ok = [p for p in must if p["status"] == "OK"]

    # --- signal
    rating = cr.get("rating")
    contests = cr.get("attendedContestsCount") or 0
    active = cal["totalActiveDays"]
    signal_score = (
        0.55 * score(rating, signal_targets["contest_rating"])
        + 0.25 * score(contests, signal_targets["contests"])
        + 0.20 * score(active, signal_targets["active_days_year"])
    )

    # Personal baseline: share of the whole problem set solved. A topic sitting
    # well under this is under-practised *relative to how you practise*, which is
    # a stronger claim than missing an absolute target.
    lc_total = universe.get("__all__")
    baseline = (solved["all"] / lc_total) if lc_total else None
    for t in topics:
        t["vs_baseline"] = (t["penetration"] / baseline) if (baseline and t["penetration"]) else None

    overall = (0.30 * vol_score + 0.30 * breadth_score
               + 0.20 * mastery_score + 0.20 * signal_score)

    return {
        "level": level,
        "targets": {"volume": volume_targets, "signal": signal_targets,
                    "topic_scale": topic_scale},
        "user": mu["username"],
        "ranking": mu["profile"]["ranking"],
        "solved": solved,
        "accepted_submissions": accepted_subs,
        "all_submissions": all_subs,
        "acceptance_rate": accepted_subs / all_subs if all_subs else 0,
        "reps_per_problem": accepted_subs / solved["all"] if solved["all"] else 0,
        "hard_share": hard_share,
        "tags": tagmap,
        "topics": topics,
        "lc_total": lc_total,
        "baseline": baseline,
        "contest": {"rating": rating, "contests": contests,
                    "global": cr.get("globalRanking"),
                    "top_pct": cr.get("topPercentage")},
        "calendar": {"year": year, "streak": cal["streak"], "active_days": active,
                     "active_years": cal["activeYears"],
                     "by_month": dict(sorted(by_month.items())),
                     "days_by_month": dict(sorted(day_count.items()))},
        "readme": {
            "tracked": len(tracked), "ok": len(ok), "again": len(again),
            "mastery_ratio": mastery_ratio,
            "google_tracked": len(g_tracked), "google_ok": len(g_ok),
            "must_tracked": len(must), "must_ok": len(must_ok),
            "by_section": {k: v for k, v in sorted(by_section.items())},
            "chronic": chronic,
        },
        "efficiency": efficiency,
        "scores": {
            "volume": vol_score, "breadth": breadth_score,
            "mastery": mastery_score, "signal": signal_score, "overall": overall,
        },
        "grades": {k: grade(v) for k, v in
                   {"volume": vol_score, "breadth": breadth_score,
                    "mastery": mastery_score, "signal": signal_score,
                    "overall": overall}.items()},
    }


# ---------------------------------------------------------------------------
# Render
# ---------------------------------------------------------------------------
def bar(pct, width=22):
    n = int(round(pct * width))
    return "█" * n + "░" * (width - n)


def render(r):
    L = []
    p = L.append
    s, g = r["scores"], r["grades"]
    vt, st = r["targets"]["volume"], r["targets"]["signal"]
    p(f"LeetCode readiness — {r['user']}  (global rank #{r['ranking']:,})")
    p("=" * 74)
    p(f"OVERALL vs Google {r['level']} SWE coding bar: {g['overall']}  ({s['overall']*100:.0f}%)")
    p("")
    for k in ("volume", "breadth", "mastery", "signal"):
        p(f"  {k.capitalize():<9} {bar(s[k])} {s[k]*100:3.0f}%  {g[k]}")
    p("")
    p("-- Volume " + "-" * 63)
    sv = r["solved"]
    for k, target in (("all", vt["total"]), ("medium", vt["medium"]),
                      ("hard", vt["hard"])):
        mark = "OK " if sv[k] >= target else "GAP"
        p(f"  {mark} {k:<7} {sv[k]:>4} / {target:<4} target")
    p(f"      easy    {sv['easy']:>4}")
    p(f"      hard share {r['hard_share']*100:.1f}%  (target {vt['hard_share']*100:.0f}%)")
    p(f"      {r['accepted_submissions']:,} accepted / {r['all_submissions']:,} submissions "
      f"= {r['acceptance_rate']*100:.0f}% ; {r['reps_per_problem']:.1f} AC per problem")
    p("")
    p("-- Precision & drill depth, by difficulty " + "-" * 32)
    p(f"  {'':<8}{'solved':>7}{'subs':>8}{'rejected':>10}{'AC/problem':>12}")
    for d in ("easy", "medium", "hard"):
        e = r["efficiency"][d]
        p(f"  {d:<8}{e['problems']:>7}{e['submissions']:>8}"
          f"{e['reject_rate']*100:>9.0f}%{e['ac_per_problem']:>12.1f}")
    p("")
    p("-- Breadth: topics Google asks " + "-" * 43)
    if r["baseline"]:
        p(f"  You have solved {r['solved']['all']}/{r['lc_total']} = "
          f"{r['baseline']*100:.0f}% of all LeetCode. A topic's 'x base' is its own")
        p("  coverage over that baseline: <1.0x means under-practised for you.")
    p(f"  {'topic':<26}{'solved':>7}{'of':>6}{'cov':>6}{'x base':>8}  w")
    for t in sorted(r["topics"], key=lambda x: (x["score"] is None, x["score"], -x["weight"])):
        sv_ = "-" if t["solved"] is None else t["solved"]
        if t["source"] == "readme":
            sv_ = f"~{sv_}"
        if t["score"] is None:
            p(f"  n/a {t['topic']:<22}{'-':>7}{'-':>6}{'-':>6}{'-':>8}  {t['weight']}"
              f"  (not reported by the API)")
            continue
        flag = "GAP " if t["score"] < 0.6 and t["weight"] >= 2 else ("thin" if t["score"] < 0.6 else "    ")
        tt = t["tag_total"] or "-"
        cov = f"{t['penetration']*100:.0f}%" if t["penetration"] else "-"
        vb = f"{t['vs_baseline']:.2f}x" if t["vs_baseline"] else "-"
        p(f"  {flag}{t['topic']:<22}{str(sv_):>7}{str(tt):>6}{cov:>6}{vb:>8}  {t['weight']}"
          f"  {bar(t['score'], 10)}")
    p("  (~ = counted from README rows; LeetCode's skill-stats API omits that tag)")
    p("")
    p("-- Mastery: this repo's README status column " + "-" * 29)
    rd = r["readme"]
    p(f"  tracked rows {rd['tracked']}   OK {rd['ok']}   AGAIN {rd['again']}"
      f"   -> {rd['mastery_ratio']*100:.0f}% marked solid")
    p(f"  google-tagged  {rd['google_ok']}/{rd['google_tracked']} OK")
    p(f"  MUST-tagged    {rd['must_ok']}/{rd['must_tracked']} OK")
    p("")
    p("  Cost curve - mean review passes per problem, by README section.")
    p("  High mean = the topic keeps costing you re-learns. Ranked worst first:")
    rows = [(k, v) for k, v in rd["by_section"].items() if v["ok"] + v["again"] >= 8]
    rows.sort(key=lambda kv: -kv[1]["reps"] / (kv[1]["ok"] + kv[1]["again"]))
    p(f"    {'section':<26}{'n':>4}{'OK':>5}{'AGAIN':>7}{'mean passes':>13}")
    for k, v in rows:
        n = v["ok"] + v["again"]
        p(f"    {k:<26}{n:>4}{v['ok']:>5}{v['again']:>7}{v['reps']/n:>10.1f}  "
          f"{bar(min(1, v['reps']/n/8), 10)}")
    p("")
    p(f"  Chronic blind spots - AGAIN after 12+ passes ({len(rd['chronic'])} problems):")
    for c in rd["chronic"][:25]:
        tagbits = " ".join(t for t, on in
                           (("google", c["google"]), ("MUST", c["must"])) if on)
        p(f"    LC {c['num']:<5}{c['reps']:>3} passes  {c['difficulty'] or '?':<7}"
          f"{(c['section'] or '?'):<24}{tagbits}")
    if len(rd["chronic"]) > 25:
        p(f"    ... and {len(rd['chronic']) - 25} more")
    p("")
    p("-- Signal: pressure / speed proxy " + "-" * 40)
    c = r["contest"]
    if c["rating"]:
        p(f"  contest rating {c['rating']:.0f}  (target {st['contest_rating']}) "
          f"from {c['contests']} contest(s), top {c['top_pct']:.1f}%")
    else:
        p("  no contest history — zero timed-pressure evidence")
    cal = r["calendar"]
    p(f"  {cal['year']}: {cal['active_days']} active days, current streak {cal['streak']}")
    p(f"  active years: {', '.join(str(y) for y in cal['active_years'])}")
    p("  submissions by month:")
    for m, n in cal["by_month"].items():
        p(f"    {m}  {n:>4} subs / {cal['days_by_month'][m]:>2} days  {bar(min(1, n/500), 16)}")
    return "\n".join(L)


def main():
    ap = argparse.ArgumentParser(description=__doc__,
                                 formatter_class=argparse.RawDescriptionHelpFormatter)
    ap.add_argument("--user", default="yennanliu")
    ap.add_argument("--level", default="L3", choices=sorted(LEVELS),
                    help="Google level to score against (default L3)")
    ap.add_argument("--readme", default=os.path.join(ROOT, "README.md"))
    ap.add_argument("--year", type=int, default=datetime.now().year)
    ap.add_argument("--cache-dir", default=os.path.join(ROOT, ".lc_cache"))
    ap.add_argument("--offline", action="store_true", help="use cached JSON only")
    ap.add_argument("--json", dest="json_out", help="also write the raw result here")
    a = ap.parse_args()

    data = fetch_all(a.user, a.cache_dir, a.offline, a.year)
    problems = parse_readme(a.readme)
    result = evaluate(data, problems, a.year, a.level)
    print(render(result))
    if a.json_out:
        slim = {k: v for k, v in result.items() if k != "tags"}
        with open(a.json_out, "w") as f:
            json.dump(slim, f, indent=2)
        print(f"\nwrote {a.json_out}")


if __name__ == "__main__":
    main()

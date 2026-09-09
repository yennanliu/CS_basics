#!/usr/bin/env python3
"""
suggest_review.py — what to practise next, chosen to keep the practice *balanced*.

The problem this solves is not "what have I not done" — it is bias. Practice
drifts: a good week on DP turns into three weeks on DP, and binary search,
heap and linked list quietly go a month untouched while still being exactly
what a coding round is made of. A flat "most important, least recently seen"
ranking makes that worse, because the biggest sections have the most important
problems and would fill the whole list.

So the pick is made in two stages:

  1. Score every problem:  importance x staleness
  2. Spend the picks across *categories* in order of how under-practised each
     one is, capped at --per-category, so one hot topic cannot own the list.

Three signals feed it, all already in the repo — nothing is invented here:

  README.md            the problem universe, and this repo's own judgement of
                       what matters: the `MUST` marker, the curated-list tags
                       (`blind75` / `neetcode150` / `neetcode250` / `top100liked`),
                       the company tags, and the status column's `OK`/`AGAIN`
                       plus its `*` run of review passes.
                       `doc/must_lc_list.md` is generated from these same rows
                       (script/extract_must_lc.py), so reading README covers it.
  git history          when each problem was last *worked on* — the commit that
                       touched its solution file, or named its LC number.
  data/progress.txt    when it was last *practised*, which is not the same
                       thing: a re-read that produced no commit still counts.

Usage:
    python3 script/suggest_review.py                     # the balanced plan
    python3 script/suggest_review.py --top 20 --per-category 3
    python3 script/suggest_review.py --only must         # MUST rows only
    python3 script/suggest_review.py --only top100liked
    python3 script/suggest_review.py --section "Binary Search" --top 10
    python3 script/suggest_review.py --no-balance        # plain importance rank
    python3 script/suggest_review.py --json out.json
    python3 script/suggest_review.py --markdown doc/review_suggestions.md

Read the balance table first and the picks second: the table is the finding
(where the practice is lopsided), the picks are just one way to act on it.
"""
import argparse
import json
import os
import re
import subprocess
import sys
import time
from collections import Counter, OrderedDict, defaultdict

REPO = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
DAY = 86400.0

# ── Sections ────────────────────────────────────────────────────────────────
# The LC tables start at `## Array`; everything above it is intro prose.
FIRST_LC_SECTION = "Array"
# Not DSA practice, or not verified solutions — kept out of the balance maths so
# they cannot distort a category's share. `--all-sections` puts them back.
EXCLUDED_SECTIONS = {
    "SQL",
    "Shell Script",
    "Concurrency",
    "Newly Added (kamyu104 gap)",
}

# ── Importance weights ──────────────────────────────────────────────────────
# Deliberately coarse. These are priorities, not measurements, and a tenth of a
# point of tuning here is noise next to "this topic has not been touched in six
# weeks", which is what the staleness and balance factors are for.
W_MUST = 5.0            # the repo's own strongest marker
W_TOP100 = 2.5          # LeetCode's Top 100 Liked
W_BLIND75 = 2.5         # the lists nest: only the narrowest one scores
W_NEETCODE150 = 1.5
W_NEETCODE250 = 0.7
W_GOOGLE = 1.5          # the stated target is a Google SWE loop
W_COMPANY_EACH = 0.2    # every other FAANG-ish tag
W_COMPANY_CAP = 1.0
W_PASS = 0.2            # per recorded review pass on an `AGAIN` row ...
W_PASS_CAP = 2.4        # ... a problem that has fought back 12 times is a gap
W_DIFFICULTY = {"Medium": 0.5, "Hard": 0.3, "Easy": 0.0}

COMPANIES = ("fb", "amazon", "apple", "netflix", "microsoft",
             "uber", "linkedin", "bloomberg")
LIST_TAGS = ("blind75", "neetcode150", "neetcode250", "top100liked")

# Staleness half-life: a problem untouched this many days is half-way to fully
# stale. 21 days is roughly the point where a solved-once problem stops being
# recallable without re-deriving it.
DEFAULT_HALF_LIFE = 21.0
COLD_DAYS = 400.0       # "never seen" — beyond any real gap, so it saturates

# How hard the balance factor pushes. 1.0 means a category with no attention at
# all doubles its problems' scores, and one with twice its fair share is halved.
BALANCE_GAIN = 1.0
BALANCE_FLOOR = 0.4
# A category's best candidate must score at least this fraction of the whole
# pool's best, or the category is passed over — breadth is the point, but not
# at the price of filling a slot with a problem nothing recommends.
MIN_SCORE_FRAC = 0.3

DIFFS = ("Easy", "Medium", "Hard")
MUST_ANY_CASE = re.compile(r"must", re.IGNORECASE)
MUST_TAG_TOKEN = re.compile(r"(?<![A-Za-z])MUST(?![A-Za-z])")
STATUS_WORD = re.compile(r"\b(OK|AGAIN|NOT_OK|TODO)\b")


# ── README ──────────────────────────────────────────────────────────────────
def parse_readme(path):
    """README.md -> {lc: problem}.  Columns are
    `# | Title | Solution | Time | Space | Difficulty | Note | Status`.

    A handful of problems are filed under two sections (LC 200 under both DFS
    and Graph, say). The first sighting owns the row — the balance maths needs
    one home per problem or a shared problem inflates two categories at once —
    and the other sections are kept in `also_in` so the row can still be found
    by `--section`.
    """
    problems = OrderedDict()
    section = None
    seen_first = False

    with open(path, encoding="utf-8") as f:
        for raw in f:
            line = raw.rstrip("\n")

            head = re.match(r"^##\s+(.*)", line)
            if head:
                name = head.group(1).strip()
                if name == FIRST_LC_SECTION:
                    seen_first = True
                if seen_first:
                    section = name
                continue

            if not seen_first or "|" not in line:
                continue

            cols = [c.strip() for c in line.strip().strip("|").split("|")]
            if len(cols) < 8 or not re.match(r"^\d+$", cols[0]):
                continue

            num = int(cols[0])
            title_m = re.search(r"\[([^\]]+)\]\(([^)]+)\)", cols[1])
            title = (title_m.group(1) if title_m else cols[1]).strip()
            url = title_m.group(2).strip() if title_m else ""
            difficulty = next((c for c in cols if c in DIFFS), "")
            note, status = cols[-2], cols[-1]

            # `MUST` is read the way script/extract_must_lc.py reads it: any
            # casing in the status cell, but only a standalone ALL-CAPS token in
            # the note cell, so prose ("the window must be non-decreasing")
            # is not mistaken for the marker.
            must = bool(MUST_ANY_CASE.search(status) or MUST_TAG_TOKEN.search(note))
            sm = STATUS_WORD.search(status)
            tags = set(re.findall(r"`([^`]+)`", note))

            if num in problems:
                # The duplicate row is the same problem filed a second time, and
                # the two rows are rarely identical — LC 547 is `Friend Circles`
                # with no marker under DFS and `Number of Provinces` with `MUST`
                # under Graph. Keeping only the first sighting's flags loses the
                # marker, so the rows are folded together: the strongest claim
                # about a problem wins.
                first = problems[num]
                first["also_in"].append(section)
                first["note"] += ", " + note
                first["must"] = first["must"] or must
                first["tags"] |= tags
                first["passes"] = max(first["passes"], status.count("*"))
                first["status"] = first["status"] or (sm.group(1) if sm else None)
                first["paths"] += [q.split("#")[0].lstrip("./")
                                   for q in re.findall(r"\]\(([^)]+)\)", cols[2])
                                   if not q.startswith("http")]
                continue

            problems[num] = {
                "lc": num,
                "title": title,
                "url": url,
                "difficulty": difficulty,
                "section": section,
                "also_in": [],
                "note": note,
                "status": sm.group(1) if sm else None,
                # The `*` run counts review passes; see doc/lc-readiness-guide.md.
                "passes": status.count("*"),
                "must": must,
                "tags": tags,
                "paths": [p.split("#")[0].lstrip("./")
                          for p in re.findall(r"\]\(([^)]+)\)", cols[2])
                          if not p.startswith("http")],
            }

    return problems


def load_problem_lists(path):
    """data/problem_lists.json -> {lc: {list names}}.

    README only tags the narrowest list a row sits on, so this file is the
    complete membership and is what the scoring reads.
    """
    if not os.path.exists(path):
        return {}
    with open(path, encoding="utf-8") as f:
        data = json.load(f)
    out = {}
    for p in data.get("problems", []):
        try:
            out[int(p["id"])] = set(p.get("lists", []))
        except (KeyError, ValueError):
            continue
    return out


# ── git ─────────────────────────────────────────────────────────────────────
# Two independent readings of "when did I last work on this", because neither
# alone is complete: most sessions edit the solution file, but plenty land in a
# dev/ws scratch file whose name says nothing, and there the commit subject
# ("update 1353 py") is the only record.
SUBJECT_LC = re.compile(
    r"\bLC[\s#]*(\d{1,4})\b(?![\s]*[-–]\s*\d)"        # "expand LC 131", not "LC 1118-2000"
    r"|\b(\d{2,4})\s+(?:py|python|java|js|scala)\b",  # "update 131 py"
    re.IGNORECASE,
)

# A commit touching more solution files than this is a bulk import or a
# sweeping refactor, not a study session — the repo has commits adding 393
# generated Java files at once. Counted naively they bury the real signal: they
# made every one of those problems look practised on the same day, and put 563
# problems inside a 30-day window that actually saw a few dozen. Their paths are
# therefore ignored; an LC number written into the subject by hand still counts.
BULK_FILE_LIMIT = 6
# Same reasoning for a subject: more than a few numbers in one line is prose
# ("add 148 solutions, completing the LC 1001-2000 gap"), not a work record.
BULK_SUBJECT_LIMIT = 3


def git_touch_history(repo, universe, bulk_limit=BULK_FILE_LIMIT):
    """-> ({lc: [unix_ts, ...]} newest first, n_commits_skipped).

    `universe` maps a repo-relative solution path to the LC numbers that claim
    it, built from README, so a commit is attributed to a problem only when the
    repo already says that file belongs to it.
    """
    cmd = ["git", "-C", repo, "log", "--no-merges",
           "--name-only", "--pretty=format:\x01%ct\x01%s"]
    try:
        out = subprocess.run(cmd, capture_output=True, text=True,
                             check=True).stdout
    except (OSError, subprocess.CalledProcessError) as exc:
        print("warning: could not read git history (%s); "
              "falling back to the practice log alone" % exc, file=sys.stderr)
        return {}, 0

    touches = defaultdict(list)
    ts = None
    hits = set()
    bulk = 0

    def close_commit():
        nonlocal bulk
        if ts is None or not hits:
            return
        if len(hits) > bulk_limit:
            bulk += 1
            return
        for lc in hits:
            touches[lc].append(ts)

    for line in out.split("\n"):
        if line.startswith("\x01"):
            close_commit()
            hits = set()
            _, stamp, subject = line.split("\x01", 2)
            try:
                ts = int(stamp)
            except ValueError:
                ts = None
                continue
            named = {int(a or b) for a, b in SUBJECT_LC.findall(subject)}
            if len(named) <= BULK_SUBJECT_LIMIT:
                for lc in named:
                    touches[lc].append(ts)
            continue
        path = line.strip()
        if not path or ts is None:
            continue
        hits.update(universe.get(path, ()))
    close_commit()

    for lc in touches:
        touches[lc].sort(reverse=True)
    return touches, bulk


# ── data/progress.txt ───────────────────────────────────────────────────────
# The practice log. Its shape is hand-written and loose; this mirrors the rules
# site/build-review-plan.js already pins with tests, so the two agree on what a
# line means. Anything it cannot place is counted and reported, never dropped
# silently.
STATUS_WORDS = ("again", "todo", "ok")


def _split_top_level(payload):
    """Split on commas outside parens — "(again, 2 pointers)" is one entry.
    A period splits too: "39(again*).79(again*)" is a typo the log actually
    contains, and without this LC 79 vanishes into LC 39's note.
    """
    parts, buf, depth = [], "", 0
    for ch in payload:
        if ch == "(":
            depth += 1
        elif ch == ")":
            depth = max(0, depth - 1)
        if depth == 0 and ch in ",.":
            parts.append(buf)
            buf = ""
            continue
        buf += ch
    parts.append(buf)
    return parts


# A day is often written in labelled runs — "| DP: 44(todo), 10(todo)" or
# "top 100 (backtrack): 51(todo)" or "| (LC must) 438(again)". The label is not
# an entry, and the first problem after one does not start with a digit once the
# label is glued to it, so without this it is dropped on the floor: 8 sessions'
# worth in the current log, and always the first problem of a run.
# site/build-review-plan.js does not strip labels and loses those same entries.
#
# The colon that ends a label is at paren depth 0. The one inside
# "2289(todo: mono stack + dp)" is not, which is why the depth matters.
def _strip_label(entry):
    depth, cut = 0, -1
    for i, ch in enumerate(entry):
        if ch == "(":
            depth += 1
        elif ch == ")":
            depth = max(0, depth - 1)
        elif ch == ":" and depth == 0:
            cut = i
    if cut >= 0:
        entry = entry[cut + 1:].lstrip()
    # A parenthesised label with no colon: "(LC must) 438(again)".
    m = re.match(r"^\([^)]*\)\s*(?=\d)", entry)
    return entry[m.end():] if m else entry


def _classify(note):
    text = (note or "").lower()
    for word in STATUS_WORDS:          # `again` before `ok`: "(ok, but again)"
        if word in text:               # is a problem that still needs a pass
            return word
    return "other" if text else "none"


def parse_progress(path):
    """-> ({lc: [epoch_seconds, ...]} newest first, {lc: Counter(status)}, warnings)."""
    if not os.path.exists(path):
        return {}, {}, ["%s not found" % path]

    dates = defaultdict(list)
    notes = defaultdict(Counter)
    warnings = []
    current_ts = None
    pending = None

    def depth_of(text):
        depth = 0
        for ch in text:
            if ch == "(":
                depth += 1
            elif ch == ")":
                depth = max(0, depth - 1)
        return depth

    def flush():
        if pending is None or current_ts is None:
            return
        for chunk in _split_top_level(pending.replace("|", ",")):
            entry = chunk.strip()
            if not entry:
                continue
            m = re.match(r"^(\d+)\s*(?:\((.*)\))?", _strip_label(entry))
            if not m:      # named drills — topo_sort, weekly_331 — not LC rows
                continue
            lc = int(m.group(1))
            dates[lc].append(current_ts)
            notes[lc][_classify(m.group(2))] += 1

    with open(path, encoding="utf-8") as f:
        lines = f.read().split("\n")

    for i, raw in enumerate(lines):
        trimmed = raw.strip()
        mid_annotation = pending is not None and depth_of(pending) > 0

        if not trimmed or re.match(r"^[-=]{2,}", trimmed):
            flush()
            pending = None
            continue

        header = re.match(r"^(\d{8})\s*[:.]?\s*(.*)$", trimmed)
        # An 8-digit line is a date, never the tail of a wrapped annotation —
        # an LC number is at most 4 digits. It therefore wins over a still-open
        # paren, which would otherwise swallow every following day.
        if header:
            if mid_annotation:
                warnings.append("line %d: previous entry left '(' unclosed" % (i + 1))
            flush()
            try:
                current_ts = time.mktime(time.strptime(header.group(1), "%Y%m%d"))
            except ValueError:
                warnings.append("line %d: unreadable date %r" % (i + 1, header.group(1)))
                current_ts = None
            pending = header.group(2)
            continue

        if pending is None:
            if current_ts is None:
                warnings.append("line %d: content before any date" % (i + 1))
                continue
            pending = trimmed          # a bare continuation of the day above
        else:
            pending += ("" if mid_annotation else ",") + trimmed

    flush()

    for lc in dates:
        dates[lc].sort(reverse=True)
    return dict(dates), dict(notes), warnings


# ── Scoring ─────────────────────────────────────────────────────────────────
def importance(p, lists):
    """Why this problem is worth a pass at all, in points. Returns (score, reasons)."""
    score, reasons = 0.0, []

    if p["must"]:
        score += W_MUST
        reasons.append("MUST")

    on = lists.get(p["lc"], set()) | (p["tags"] & set(LIST_TAGS))
    if "top100liked" in on:
        score += W_TOP100
        reasons.append("top100liked")
    # The NeetCode lists nest, so only the narrowest one is credited.
    if "blind75" in on:
        score += W_BLIND75
        reasons.append("blind75")
    elif "neetcode150" in on:
        score += W_NEETCODE150
        reasons.append("neetcode150")
    elif "neetcode250" in on:
        score += W_NEETCODE250
        reasons.append("neetcode250")

    if "google" in p["tags"]:
        score += W_GOOGLE
        reasons.append("google")
    others = [c for c in COMPANIES if c in p["tags"]]
    if others:
        score += min(len(others) * W_COMPANY_EACH, W_COMPANY_CAP)

    # An `AGAIN` row that has already cost many passes is the opposite of
    # settled — the marker never graduates in this repo (see the readiness
    # guide), so the pass count is read as difficulty, not as progress.
    if p["status"] == "AGAIN" and p["passes"]:
        bump = min(p["passes"] * W_PASS, W_PASS_CAP)
        score += bump
        reasons.append("AGAIN x%d" % p["passes"])

    score += W_DIFFICULTY.get(p["difficulty"], 0.0)
    return score, reasons


def staleness(days, half_life):
    """0 (touched today) -> 1 (long gone). Halves the remaining freshness every
    `half_life` days, so the curve is steep where it matters and flat after."""
    if days is None:
        days = COLD_DAYS
    return 1.0 - 0.5 ** (max(0.0, days) / half_life)


def build_rows(problems, lists, git_touch, prog_dates, prog_notes, now, half_life):
    rows = []
    for lc, p in problems.items():
        imp, reasons = importance(p, lists)

        git_last = git_touch.get(lc, [None])[0]
        prog_last = prog_dates.get(lc, [None])[0]
        last = max([t for t in (git_last, prog_last) if t], default=None)
        days = None if last is None else (now - last) / DAY
        stale = staleness(days, half_life)

        rows.append(dict(
            p,
            importance=imp,
            reasons=reasons,
            last_ts=last,
            days=days,
            stale=stale,
            git_touches=len(git_touch.get(lc, ())),
            practices=len(prog_dates.get(lc, ())),
            again_notes=prog_notes.get(lc, Counter()).get("again", 0),
            base=imp * stale,
        ))
    return rows


def category_balance(rows, now, window_days):
    """Per category: the share of importance it holds vs the share of recent
    attention it got. The gap between the two is the bias this script exists to
    surface.

    Attention is recency-weighted inside the window rather than a flat count,
    so yesterday's session weighs more than one three weeks ago, and it counts
    *touch events* (commits and practice-log entries), not distinct problems —
    grinding one problem five times is five units of attention spent on that
    category, which is exactly the behaviour being measured.
    """
    imp = defaultdict(float)
    att = defaultdict(float)
    last_seen = {}
    cutoff = now - window_days * DAY

    for r in rows:
        imp[r["section"]] += r["importance"]
        if r["last_ts"]:
            prev = last_seen.get(r["section"])
            if prev is None or r["last_ts"] > prev:
                last_seen[r["section"]] = r["last_ts"]

    for r in rows:
        for ts in r["_events"]:
            if ts >= cutoff:
                att[r["section"]] += 0.5 ** ((now - ts) / DAY / (window_days / 2.0))

    total_imp = sum(imp.values()) or 1.0
    total_att = sum(att.values()) or 1.0

    cats = {}
    for section, weight in imp.items():
        imp_share = weight / total_imp
        att_share = att.get(section, 0.0) / total_att
        # ratio 1.0 = getting exactly its fair share of practice.
        ratio = att_share / imp_share if imp_share else 0.0
        cats[section] = {
            "section": section,
            "importance_share": imp_share,
            "attention_share": att_share,
            "ratio": ratio,
            "deficit": imp_share - att_share,
            "multiplier": max(BALANCE_FLOOR,
                              min(1.0 + BALANCE_GAIN,
                                  1.0 + BALANCE_GAIN * (1.0 - ratio))),
            "last_ts": last_seen.get(section),
            "n": 0,
        }
    for r in rows:
        cats[r["section"]]["n"] += 1
    return cats


def pick(rows, cats, top, per_category, balanced, min_score_frac=MIN_SCORE_FRAC):
    """Spend `top` picks. Balanced: walk the categories in order of how much
    absolute importance they are missing and take each one's best candidate in
    turn, so the list cannot be swallowed by whichever topic is largest or
    hottest. Unbalanced: straight score order.

    Breadth has a floor. Ordering by deficit alone eventually reaches a
    five-row section whose best candidate is a Hard problem carrying one
    company tag — technically neglected, but not a topic worth a slot ahead of
    a second pass at a `MUST`. A category is skipped when its best candidate
    scores below `min_score_frac` of the strongest pick, and the slot goes back
    to the categories that cleared the bar.
    """
    ranked = sorted(rows, key=lambda r: -r["score"])
    if not balanced:
        return ranked[:top]

    by_cat = defaultdict(list)
    for r in ranked:
        by_cat[r["section"]].append(r)

    floor = (ranked[0]["score"] * min_score_frac) if ranked else 0.0
    # Absolute deficit, not the ratio: a tiny category can be 100% neglected and
    # still not be worth a pick ahead of a big one that is half-neglected.
    order = sorted(by_cat, key=lambda s: -cats[s]["deficit"])
    picks, cursor, used = [], {s: 0 for s in order}, defaultdict(int)

    def take(section, cap, bar):
        queue = by_cat[section]
        if used[section] >= cap:
            return False
        while cursor[section] < len(queue):
            cand = queue[cursor[section]]
            cursor[section] += 1
            if cand["score"] <= 0 or cand["score"] < bar:
                continue
            picks.append(cand)
            used[section] += 1
            return True
        return False

    while len(picks) < top:
        progressed = False
        for section in order:
            if len(picks) >= top:
                break
            progressed |= take(section, per_category, floor)
        if not progressed:
            break

    # The cap and the floor are a preference for breadth, not a quota: once
    # every eligible category has had its share, the rest of the request is
    # filled by score. Without this, `--section "Binary Search" --top 5` returns
    # two problems, which reads as a bug rather than as a policy.
    while len(picks) < top:
        progressed = False
        for section in order:
            if len(picks) >= top:
                break
            progressed |= take(section, top, 0.0)
        if not progressed:
            break
    return picks


# ── Rendering ───────────────────────────────────────────────────────────────
def fmt_days(days):
    if days is None:
        return "never"
    if days < 1:
        return "today"
    return "%dd" % round(days)


def bar(fraction, width=14):
    filled = int(round(max(0.0, min(1.0, fraction)) * width))
    return "#" * filled + "." * (width - filled)


def truncate(text, width):
    return text if len(text) <= width else text[:width - 1] + "…"


def render_text(picks, cats, recent, args, warnings, out=None):
    # Resolved at call time, not bound as a default: a default would capture the
    # real stdout at import and write straight past a redirect_stdout.
    w = (out or sys.stdout).write

    w("\n== Recent focus (last %d days) ==\n\n" % args.window)
    if recent["events"]:
        w("  %d touches on %d problems across %d categories.\n"
          % (recent["events"], recent["problems"], len(recent["by_cat"])))
        if recent["bulk_commits"]:
            w("  (%d bulk commits ignored — a commit touching more than %d solution\n"
              "   files is an import, not a session.)\n"
              % (recent["bulk_commits"], args.bulk_limit))
        for section, n in recent["by_cat"].most_common(8):
            w("    %-26s %3d  %s\n"
              % (truncate(section, 26), n, bar(n / recent["by_cat"].most_common(1)[0][1])))
    else:
        w("  nothing in the window — every category counts as neglected.\n")

    w("\n== Balance: attention vs importance ==\n\n")
    w("  %-26s %5s  %-14s %-14s %8s  %s\n"
      % ("category", "n", "importance", "attention", "ratio", "last"))
    ordered = sorted(cats.values(), key=lambda c: -c["deficit"])
    scale = max(max((c["importance_share"] for c in ordered), default=0.01),
                max((c["attention_share"] for c in ordered), default=0.01))
    # A five-row section rounds to nothing on both axes and is noise in the
    # table — unless it produced a pick, in which case hiding it would leave
    # that pick unexplained.
    picked_sections = {p["section"] for p in picks}
    for c in ordered:
        if (c["importance_share"] < 0.005 and c["attention_share"] < 0.005
                and c["section"] not in picked_sections):
            continue
        flag = ("UNDER" if c["ratio"] < 0.5 else
                "over " if c["ratio"] > 1.8 else "     ")
        w("  %-26s %5d  %s %s %7.2f %s  %s\n"
          % (truncate(c["section"], 26), c["n"],
             bar(c["importance_share"] / scale, 12),
             bar(c["attention_share"] / scale, 12),
             c["ratio"], flag,
             fmt_days(None if not c["last_ts"]
                      else (recent["now"] - c["last_ts"]) / DAY)))
    w("\n  ratio = share of recent attention / share of importance."
      "  1.00 is a fair share; UNDER is the bias to fix.\n")

    w("\n== Suggested review (%d problems, %d categories) ==\n\n" % (
        len(picks), len({p["section"] for p in picks})))
    if not args.no_balance:
        w("  Ordered by how neglected the category is, not by score — that order\n"
          "  is the recommendation.\n\n")
    w("  %-3s %-5s %-34s %-7s %-20s %6s %6s  %s\n"
      % ("#", "LC", "title", "diff", "category", "last", "score", "why"))
    for i, p in enumerate(picks, 1):
        w("  %-3d %-5d %-34s %-7s %-20s %6s %6.2f  %s\n"
          % (i, p["lc"], truncate(p["title"], 34), p["difficulty"] or "?",
             truncate(p["section"], 20), fmt_days(p["days"]), p["score"],
             " · ".join(p["reasons"]) or "-"))

    if warnings:
        w("\n  %d parse warning(s) in data/progress.txt:\n" % len(warnings))
        for warning in warnings[:5]:
            w("    %s\n" % warning)
    w("\n")


def render_markdown(picks, cats, recent, args):
    lines = [
        "# Suggested LeetCode Review",
        "",
        "Generated by `script/suggest_review.py` — importance x staleness, "
        "then balanced across categories so no one topic owns the list.",
        "",
        "## Balance: attention vs importance",
        "",
        "| Category | Problems | Importance share | Attention share (%dd) | Ratio | Last touched |"
        % args.window,
        "|---|---:|---:|---:|---:|---|",
    ]
    for c in sorted(cats.values(), key=lambda c: -c["deficit"]):
        if c["importance_share"] < 0.005 and c["attention_share"] < 0.005:
            continue
        lines.append("| %s | %d | %.1f%% | %.1f%% | %.2f | %s |" % (
            c["section"], c["n"], c["importance_share"] * 100,
            c["attention_share"] * 100, c["ratio"],
            fmt_days(None if not c["last_ts"]
                     else (recent["now"] - c["last_ts"]) / DAY)))

    lines += ["", "## Picks", "",
              "| # | LC | Title | Difficulty | Category | Last touched | Score | Why |",
              "|---:|---:|---|---|---|---|---:|---|"]
    for i, p in enumerate(picks, 1):
        title = "[%s](%s)" % (p["title"], p["url"]) if p["url"] else p["title"]
        lines.append("| %d | %d | %s | %s | %s | %s | %.2f | %s |"
                     % (i, p["lc"], title, p["difficulty"] or "?",
                        p["section"], fmt_days(p["days"]), p["score"],
                        ", ".join(p["reasons"]) or "-"))
    return "\n".join(lines) + "\n"


# ── Self-test ───────────────────────────────────────────────────────────────
def self_test(args):
    """`--self-test` — run script/test_suggest_review.py, quietly.

    The assertions live in that file, not here, so there is one copy of them:
    this flag exists because the planner is most often run as a lone script and
    "does it still read the files correctly" should be one command away.
    """
    import unittest

    sys.path.insert(0, os.path.dirname(os.path.abspath(__file__)))
    try:
        import test_suggest_review
    except ImportError:
        print("script/test_suggest_review.py is not next to this script — "
              "run the tests from a full checkout.", file=sys.stderr)
        return 1

    suite = unittest.defaultTestLoader.loadTestsFromModule(test_suggest_review)
    result = unittest.TextTestRunner(verbosity=1).run(suite)
    return 0 if result.wasSuccessful() else 1



# ── CLI ─────────────────────────────────────────────────────────────────────
def main(argv=None):
    ap = argparse.ArgumentParser(
        description=__doc__.split("\n\n")[0],
        formatter_class=argparse.RawDescriptionHelpFormatter,
        epilog="Read the balance table first; the picks are one way to act on it.")
    ap.add_argument("--readme", default=os.path.join(REPO, "README.md"))
    ap.add_argument("--progress", default=os.path.join(REPO, "data", "progress.txt"))
    ap.add_argument("--lists", default=os.path.join(REPO, "data", "problem_lists.json"))
    ap.add_argument("--repo", default=REPO)
    ap.add_argument("--top", type=int, default=15, help="how many problems to suggest (default 15)")
    ap.add_argument("--per-category", type=int, default=2,
                    help="most picks any one category may take (default 2)")
    ap.add_argument("--window", type=int, default=30,
                    help="days of history that count as 'recent attention' (default 30)")
    ap.add_argument("--half-life", type=float, default=DEFAULT_HALF_LIFE,
                    help="days for a problem to go half stale (default 21)")
    ap.add_argument("--bulk-limit", type=int, default=BULK_FILE_LIMIT,
                    help="a commit touching more solution files than this is a bulk "
                         "import, not practice, and is ignored (default %d)" % BULK_FILE_LIMIT)
    ap.add_argument("--min-score-frac", type=float, default=MIN_SCORE_FRAC,
                    help="skip a category whose best candidate scores below this "
                         "fraction of the top pick (default %.2f)" % MIN_SCORE_FRAC)
    ap.add_argument("--no-balance", action="store_true",
                    help="rank by score alone, without spreading across categories")
    ap.add_argument("--only", choices=["must", "blind75", "neetcode150",
                                       "neetcode250", "top100liked", "google", "again"],
                    help="restrict the pool to one marker")
    ap.add_argument("--section", action="append", default=[],
                    help="restrict to a README section (repeatable, case-insensitive)")
    ap.add_argument("--difficulty", action="append", default=[], choices=list(DIFFS),
                    help="restrict to a difficulty (repeatable)")
    ap.add_argument("--all-sections", action="store_true",
                    help="include SQL / Shell / Concurrency / the kamyu104 drafts")
    ap.add_argument("--json", metavar="PATH", help="also write the full result as JSON")
    ap.add_argument("--markdown", metavar="PATH", help="also write a markdown report")
    ap.add_argument("--self-test", action="store_true",
                    help="check the parsers against known input shapes and exit")
    args = ap.parse_args(argv)

    if args.self_test:
        return self_test(args)

    now = time.time()

    problems = parse_readme(args.readme)
    if not problems:
        print("no problem rows found in %s" % args.readme, file=sys.stderr)
        return 1
    lists = load_problem_lists(args.lists)

    path_owner = defaultdict(set)
    for lc, p in problems.items():
        for path in p["paths"]:
            path_owner[path].add(lc)

    git_touch, bulk_commits = git_touch_history(args.repo, path_owner,
                                                args.bulk_limit)
    prog_dates, prog_notes, warnings = parse_progress(args.progress)

    rows = build_rows(problems, lists, git_touch, prog_dates, prog_notes,
                      now, args.half_life)
    for r in rows:
        r["_events"] = git_touch.get(r["lc"], []) + prog_dates.get(r["lc"], [])

    if not args.all_sections:
        rows = [r for r in rows if r["section"] not in EXCLUDED_SECTIONS]

    # The balance is measured over the whole pool, before --only / --section
    # narrow it: the question "which topic am I neglecting" is not answerable
    # from inside a filter.
    cats = category_balance(rows, now, args.window)

    recent = {
        "now": now,
        "by_cat": Counter(),
        "events": 0,
        "problems": 0,
        "lcs": Counter(),
        "bulk_commits": bulk_commits,
    }
    cutoff = now - args.window * DAY
    for r in rows:
        hits = [t for t in r["_events"] if t >= cutoff]
        if hits:
            recent["by_cat"][r["section"]] += len(hits)
            recent["events"] += len(hits)
            recent["problems"] += 1
            recent["lcs"][r["lc"]] += len(hits)

    for r in rows:
        r["balance"] = cats[r["section"]]["multiplier"] if not args.no_balance else 1.0
        r["score"] = r["base"] * r["balance"]

    pool = rows
    if args.only == "must":
        pool = [r for r in pool if r["must"]]
    elif args.only == "google":
        pool = [r for r in pool if "google" in r["tags"]]
    elif args.only == "again":
        pool = [r for r in pool if r["status"] == "AGAIN"]
    elif args.only:
        pool = [r for r in pool
                if args.only in (lists.get(r["lc"], set()) | r["tags"])]
    if args.section:
        wanted = {s.lower() for s in args.section}
        pool = [r for r in pool
                if r["section"].lower() in wanted
                or any(s.lower() in wanted for s in r["also_in"])]
    if args.difficulty:
        pool = [r for r in pool if r["difficulty"] in set(args.difficulty)]

    if not pool:
        print("no problems match those filters", file=sys.stderr)
        return 1

    picks = pick(pool, cats, args.top, args.per_category,
                 not args.no_balance, args.min_score_frac)
    render_text(picks, cats, recent, args, warnings)

    if args.json:
        payload = {
            "generated": time.strftime("%Y-%m-%d %H:%M:%S"),
            "window_days": args.window,
            "half_life_days": args.half_life,
            "categories": sorted(cats.values(), key=lambda c: -c["deficit"]),
            "picks": [{k: v for k, v in p.items()
                       if k not in ("_events", "tags", "paths", "note")}
                      | {"tags": sorted(p["tags"])} for p in picks],
            "warnings": warnings,
        }
        with open(args.json, "w", encoding="utf-8") as f:
            json.dump(payload, f, indent=1, default=list)
        print("wrote %s" % args.json)

    if args.markdown:
        with open(args.markdown, "w", encoding="utf-8") as f:
            f.write(render_markdown(picks, cats, recent, args))
        print("wrote %s" % args.markdown)

    return 0


if __name__ == "__main__":
    sys.exit(main())

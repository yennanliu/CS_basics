# Project Review — September 2026

> **Scope** — Does this repository make a candidate pass a FAANG SWE coding loop faster?
> One question, asked across five axes: LeetCode coverage, the solution code, the docs,
> the site, and the direction the project is heading. The earlier reviews asked
> narrower ones — [`site-review-2026-08-31.md`](./site-review-2026-08-31.md) whether the
> built site was *correct*, [`cheatsheet-review-2026-08.md`](./cheatsheet-review-2026-08.md)
> whether the cheatsheets were *organised*, and the draft in PR #155 whether a cheatsheet
> teaches derivation. This one asks whether the whole thing is pointed at the goal.
> **See also**: [`lc-readiness-guide.md`](./lc-readiness-guide.md) — the per-candidate
> readiness score this review borrows its bar from.
> **繁體中文版**: [`project-review-2026-09.zh.md`](./project-review-2026-09.zh.md) — same
> numbers, same conclusions.

Reviewed at `953902764` (master, 2026-09-23), against the live site at
<https://yennj12.js.org/CS_basics/>. Every number below was produced from the tree by
the commands in the [appendix](#appendix--how-the-numbers-were-produced); nothing is
typed from memory. The bar throughout is the one already recorded for this project: a
**Google L3 coding loop** — a clean medium in 35–45 minutes, complexity stated, edges
handled, out loud — not L5, and not a system-design round.

**Conventions this review takes as given**, confirmed by the owner on 2026-09-23 and
corrected here where a first draft had them wrong:

- **`# V0` / `// V0` is the favoured, canonical solution** in every file; `V1+` are
  references. It is *not* a first attempt.
- **Python is the tier-1 interview language; Java is tier 2.** The Java coverage gap is
  therefore not on the critical path.
- **`data/progress.txt` is the primary progress record.** README's status column is
  updated only when needed, so it lags the log; mastery is measured from the log here,
  with the README column quoted as corroboration.
- **The target is DSA foundations, algorithm patterns and clean code** for the L3 loop.
  `doc/faq/` and `system_design/` are out of scope by decision; documentation effort goes
  to `doc/cheatsheet/`.

## Table of Contents

- [Verdict](#verdict)
- [What was measured](#what-was-measured)
- [1. LC coverage — breadth is finished; the gaps are depth-shaped](#1-lc-coverage--breadth-is-finished-the-gaps-are-depth-shaped)
- [2. Solution code — an archive of attempts, not a set of model answers](#2-solution-code--an-archive-of-attempts-not-a-set-of-model-answers)
- [3. Docs — the shape is right, the entry point is not](#3-docs--the-shape-is-right-the-entry-point-is-not)
- [4. Site — strong tooling, no session loop](#4-site--strong-tooling-no-session-loop)
- [5. Direction — the repo optimises for accumulation; the goal needs conversion](#5-direction--the-repo-optimises-for-accumulation-the-goal-needs-conversion)
- [Prioritised action list](#prioritised-action-list)
- [What this review did not cover](#what-this-review-did-not-cover)
- [Appendix — how the numbers were produced](#appendix--how-the-numbers-were-produced)

---

## Verdict

**As a reference, this is an A. As preparation material, it is a B−, and the two grades
have the same cause.**

The repository has already done the part most interview repos never finish: every
problem on Blind 75, NeetCode 150, NeetCode 250 and LeetCode's Top 100 Liked is indexed,
solved and linked (3,270 problems, 2,897 Python and 1,210 Java solutions), there are 135
cheatsheets totalling 145,000 lines, 36 visualizers, a dependency-ordered roadmap, a
208-question complexity quiz, a spaced-repetition planner driven by a six-year practice
log, and a CI pipeline that walks every generated page and fails on a broken link. The
site's own test suite is 418 tests, all green. Very little of that needs more of itself.

What the material does not yet do is **convert**. The repo's own records say so:

- In the practice log, the **latest verdict on Blind 75 is `ok` for 14 problems and
  `again` for 34**, and 25 have never received a verdict at all. NeetCode 150 is 18 `ok`
  / 69 `again`; Top 100 Liked 22 / 53. README's status column, updated less often, says
  the same thing — 15 `OK` / 60 `AGAIN` on Blind 75, **6 `OK` of 150 tracked Hards**.
- **83 problems whose latest verdict is `again` have been attempted eight or more
  times** (LC 300, 207, 105, 322, 139, 97, 394, 153, 295, 104, 133, 79 …); only 12
  problems with that many attempts end on `ok`. README's marker agrees — 125 rows carry
  `AGAIN` after 12+ passes. The loop from *solved* to *solid* is not closing.
- The pass-count curve is steepest exactly where a coding round lives: Recursion
  averages **7.4** passes per problem, BST 7.0, Stack and Backtracking 6.3, BFS 6.2,
  Tree 6.1 — against Math 1.1, SQL 1.6, Bit Manipulation 1.8.
- Verification is almost absent from the artefacts: **73 of 2,897** Python files run
  anything, Java has **no test tree**, and the August readiness run found a 31–44%
  submission rejection rate that is *higher* on Easy than on Hard.

So the highest-value work is no longer adding — it is closing loops: turning `again`
into `ok` on a fixed 75-problem set under a stated bar, adding a timed session mode to a
site that has every other tool, teaching derivation (brute force → observation →
template) in the sheets that currently open at the answer, and making each file's `V0`
read as the model answer it is meant to be — which, for an L3 loop that scores coding
quality, is a clean-code job as much as a tidying one.

| Axis | Grade | The one thing that would move it |
|---|---|---|
| LC coverage | **A** (breadth) / **C** (mastery) | Run `/lc-again` as a campaign over Blind 75's 34 `again` + 25 unverdicted problems, under the four-question bar, measured in the log |
| Solution code | **B−** | Make `V0` self-evidently the model answer: strip the AI-attribution and debug narration, calm the `NOTE !!!` commentary, dedupe the javadoc; emit the docstring's examples as a runnable check |
| Docs | **B** | A one-screen "in the room" block at the top of each tier-5 cheatsheet; an index for `doc/` that says which plan is current |
| Site | **B+** | A timed practice mode wired to the random picker and the log |
| Direction | **B** | Stop adding problems; ship a named "L3 core" list and measure its `ok` share from the log monthly |

---

## What was measured

| | Count | Source |
|---|---|---|
| Problems indexed (unique LC ids) | 3,270 — 800 Easy · 1,715 Medium · 747 Hard · 8 unknown | `README.md` via `parseReadmeProblems` |
| README rows | 3,291 — 1,310 in the main `##` tables, 1,981 under `## Newly Added (kamyu104 gap)`; 21 ids in both | `README.md` |
| Rows with a tracked status | 1,266 — 997 `AGAIN`, 269 `OK` (all in the main set; every imported row is blank) | status column |
| Distinct spellings of the status cell | 420 | status column |
| Solutions linked | 4,296 files — 2,897 Python, 1,210 Java, 1,006 both, 165 MySQL, 21 C++, 6 Scala, 4 Shell | solution column |
| Broken solution links | 42 — 16 Python, 21 C++ (the `C++/` tree is not in the repo), 4 Shell, 1 MySQL | link check |
| Solution files no README row links to | 53 Python, **347 Java** (22% of `LeetCodeJava/`) | tree walk |
| `time = O(...)` annotated | Python 2,867 / 2,897 (99%) · Java 1,247 / 1,554 (80%) | grep |
| Solution variants (`# V0` canonical, `# V1`… references) | Python 8,479 blocks over 2,897 files (avg 2.9; 753 files with ≥ 4) · Java 5,990 over 1,554 | grep |
| Files carrying tool attribution in the IDEA line (`(gpt)`, `fixed by gemini`) | 457 Java, 202 Python | grep |
| Clean-code signals, Python | 1,027 `NOTE !!!` comments in 395 files; 875 files with triple-quoted comment blocks inside function bodies; 2,750 `class Solution(object)`; 205 files with type hints; 42 with `xrange` / print-statement spellings | grep |
| Files that execute anything (`assert`, `__main__`, `main()`) | 73 Python, 2 Java; `leetcode_java/src/test` does not exist on master | grep |
| Cheatsheets | 135 files, 145,163 lines; 14 sheets over 1,900 lines; every fence tagged; 42 mention an invariant | `doc/cheatsheet/` |
| FAQs | 49, 100% translated | `doc/faq/` |
| Practice log | 827 days, 2020-04-29 → 2026-09-23; 30 of the last 30 days, 87 of the last 90; 3,261 attempt mentions over 860 distinct problems; 405 problems touched in 2026 (latest verdict: 207 `again`, 33 `ok`, 159 none); one impossible date (`20260229`, line 269) | `data/progress.txt` |
| Latest log verdict on the curated lists | Blind 75: 14 `ok` · 34 `again` · 25 no verdict — NC150: 18 · 69 · 62 — Top 100: 22 · 53 · 24 | `data/progress.txt` × `problem_lists.json` |
| Curated-list coverage | Blind 75 · NC150 · NC250 · Top 100: **100% indexed**; NeetCode All 905 / 972 (29 of the 67 missing are the JavaScript-only group) | `data/problem_lists.json` |
| Java (tier 2) on the curated lists | Blind 75 and Top 100: complete; NC150 lacks one (LC 704); 300 `google`-tagged rows and 13 `MUST` rows have no Java | solution column |
| Site | 18 hand-written pages, 36 visualizers, 29 roadmap topics, 208 quiz questions, 14 agent skills; 418 unit tests passing | `site/`, `data/`, `.claude/skills/` |

---

## 1. LC coverage — breadth is finished; the gaps are depth-shaped

### 1.1 Coverage of the lists that matter is 100%, so coverage is not the lever

Every problem on Blind 75, NeetCode 150, NeetCode 250 and Top 100 Liked has a README
row and a solution, and Blind 75 and Top 100 are complete in **both** languages. Of the
NeetCode All catalogue, 905 of 972 are indexed and 29 of the remainder are the
JavaScript-only group that the roadmap deliberately maps to `null`. By NeetCode's own
taxonomy the thinnest groups are Greedy (59/67), Heap (29/33) and Trees (88/93) — single
digits everywhere.

The August readiness run scored Volume and Breadth at A for the L3 bar and named only
three graph-shaped topic gaps (Dijkstra 7, topological sort 8, sweep line 3). **More
solved problems cannot move the grade.** Every recommendation in this section is
therefore about the problems already here.

### 1.2 The mastery record says "not ready" on exactly the lists an interviewer draws from

The practice log is the primary record, so the measure here is *the latest verdict the
log gives each problem* — `ok`, `again`, or nothing (a `todo` or a bare number). README's
status column is quoted beside it as the slower-moving corroboration.

| List | Indexed | Log: latest `ok` | Log: latest `again` | Log: no verdict | README `OK` / `AGAIN` |
|---|---|---|---|---|---|
| Blind 75 | 75 | 14 (19%) | 34 | 25 | 15 / 60 |
| NeetCode 150 | 150 | 18 (12%) | 69 | 62 | 24 / 125 |
| NeetCode 250 | 250 | 21 (8%) | 99 | 122 | 39 / 206 |
| Top 100 Liked | 100 | 22 (22%) | 53 | 24 | 16 / 84 |

The two records agree. README adds the difficulty split — Easy 168 `OK` / 171 `AGAIN`,
Medium 95 / 682, **Hard 6 / 144** — and the log adds the shape of the backlog: **a third
of Blind 75 has never been given a verdict at all.** Those 25 are not failures; they are
attempts that were logged without the `(ok)` / `(again)` the whole schedule keys on.

Both markers are floors rather than measurements — [`lc-readiness-guide.md`](./lc-readiness-guide.md)
explains why (`AGAIN` is only ever *added*), and `/lc-again` exists to graduate a row when
the owner chooses to update README. But `/lc-again` is a per-problem tool and the backlog
is a list. **Recommendation:** run it as a campaign with a fixed scope and a finish line,
not opportunistically:

1. Scope: Blind 75's 34 `again` and 25 unverdicted problems. Fifty-nine problems is four
   weeks at two a day.
2. Bar: the four `/lc-again` questions — re-derived unaided, invariant stated, the line
   that sets the complexity named, edges handled — plus the readiness guide's time box
   (under 20 minutes for a medium). A pass is logged as `(ok)`; anything short as
   `(again)`. **Every attempt gets a verdict** — that alone closes the 25.
3. Finish line: Blind 75 latest-`ok` share ≥ 80% in the log. Then NeetCode 150's 131.

Publish the share on the landing page beside the existing `269 OK / 997 AGAIN` counts so
the number is looked at — computed from `progress.json`, which `build-review-plan.js`
already derives from the log, so it moves the day the log does rather than the day
README is next edited.

### 1.3 The cost curve is steepest on interview-core topics

Average recorded passes per tracked problem, by main README section:

| Section | Rows | `AGAIN` | Avg passes |
|---|---|---|---|
| Recursion | 30 | 28 | **7.4** |
| Binary Search Tree | 20 | 16 | **7.0** |
| Queue | 5 | 4 | 7.0 |
| Stack | 44 | 41 | **6.3** |
| Backtracking | 34 | **34** | **6.3** |
| Prefix Sum | 3 | 3 | 6.3 |
| Breadth-First Search | 44 | 39 | **6.2** |
| Tree | 62 | 51 | **6.1** |
| Linked list | 24 | 21 | 5.7 |
| Depth-First Search | 56 | 51 | 5.0 |
| Heap | 28 | 25 | 4.5 |
| Binary Search | 48 | 41 | 4.3 |
| Dynamic Programming | 95 | 89 | 4.2 |
| … | | | |
| Bit Manipulation | 28 | 16 | 1.8 |
| SQL | 165 | 76 | 1.6 |
| Math | 93 | 71 | 1.1 |

Backtracking is 34 for 34 `AGAIN`. Recursive structure costs three to six times what
array manipulation costs, and a Google L3 loop is mostly recursive structure.

The log names the individuals. **83 problems end on `again` after eight or more logged
attempts**; the most-attempted are LC 300 (LIS), 207 (Course Schedule), 105 (build tree
from preorder/inorder), 322 (Coin Change), 139 (Word Break), 97 (Interleaving String),
394 (Decode String), 153 (rotated-array minimum), 295 (median stream), 104, 133, 79,
323, 261, 297, 206, 1143, 32, 310, 776, 518, 647, 53, 316. Only 12 problems with that
many attempts end on `ok`.

**Recommendation:** a *derivation card* for each of the ~40 highest-cost problems (the
list above, extended by README's 12+-pass `AGAIN` rows in interview-core sections),
written once and reviewed instead of re-solved:
the invariant in one sentence, the recursion's contract (what the call returns, what it
may assume), the one line that sets the complexity, the two edges that keep failing.
The readiness guide's "Acting on it" §3 already says *"write invariants for the chronic
list; stop re-solving"*; nothing in the repo yet has a place for the invariant to live.
`/lc-cheatsheet 1650 into binary_tree as variation` is close but files into a 2,000-line
sheet; a card wants to be the row's own note. The cheapest home is a short section per
problem in a new `doc/derivation_cards.md` linked from the README row's Note column.

### 1.4 The README's topic taxonomy under-represents what it holds

The main tables have `Scan Line` with 1 row, `Prefix Sum` with 3, `Slide Window` with 12
and `Graph` with 21, next to `SQL` 165 and `Math` 93. Sliding-window problems are not
rare here — they are filed under Array, String and Hash Table by the technique first
seen — so a reader who opens `## Slide Window` concludes the repo has twelve. The roadmap
and the review plan already resolve this by reading the tag column rather than the
heading; the README page and the explorer do not. **Recommendation:** when PR #171's
in-place filter lands, the section heading matters less; until then, add the row's
`**tag**` to the explorer's tag facet (it is already in `lc-problems.json`'s `tags`) and
say on `## Slide Window` that the tag, not the heading, is the index.

### 1.5 The imported table set dilutes the index for a visitor

1,981 of 3,291 rows — 60% — sit under `## Newly Added (kamyu104 gap)` with an empty
status cell. The landing page's `3,270 problems` counts them; the `269 / 997` counts do
not, and nothing on the page says which rows are practised and which are imported. 21
ids appear in both sets (`363 381 499 604 631 641 715 874 937 959` among them), and
`CLAUDE.md` already records that the imported set is where `/lc-python` used to misfile
rows. **Recommendation:** either (a) give imported rows an explicit `imported` status so
the explorer can offer a *tracked only* facet and the count can read `1,266 practised ·
3,270 indexed`, or (b) move the imported set to its own page. (a) is a `fix_readme_tags.py`
pass; (b) is a `build-site.js` change. Either makes the 21 duplicates a build error.

### 1.6 Index data quality: nothing gates it

- **42 solution links are dead.** Sixteen Python paths — typos (`letcode_python/…`,
  `eetcode_python/…`, `logger_rate_lmiter.py.py`), the eight concurrency problems
  (LC 1114–1279) linked under a `Python/` prefix that does not exist, four files that
  were never committed — plus 21 `C++/…` links to a tree that is not in the repo, the
  four `leetcode_shell/` links whose files moved to `archived/`, and one SQL file.
  `e2e-check.js` cannot see any of them because they are absolute GitHub URLs.
- **347 Java files and 53 Python files are linked from no row**, so they are invisible to
  the index, to `find_missing_java.py`, and to the site. For Java that is 22% of the tree.
- **420 distinct spellings of the status cell**, including `not start`, `Again (1)`,
  `Again******* (2)(again)`, and every casing of `again`. `/lc-again` deliberately does
  not standardise them; nothing else does either.
- **One impossible date** in the practice log, `20260229` at line 269 — parsed as a
  string, so it sorts and schedules, but any date arithmetic on it will throw.

Nothing gates the README the way `e2e-check.js` gates `_site/` and `check_skills.py`
gates `.claude/skills/` (`check_lc_format.py`, despite the name, audits the cheatsheets'
LC-example headers). **Recommendation:** a `script/check_readme.py` in the same mould —
reads the rows through the parser the site already uses, and fails on a dead solution
link, an unlinked solution file, a duplicate id across the two table sets, a status cell
that does not match one grammar (`(OK|AGAIN)\*{0,}( \(\d+\))?( \(MUST\))?`), and an
unparseable log date — run from `validate-pages.yml`. The grammar migration is one regex
pass over 1,266 cells; the 420 spellings collapse to a few dozen.

### 1.7 Python is the interview language; treat the Java gap as tier 2

Python has 2,897 solutions to Java's 1,210. Both cover Blind 75 and Top 100 fully; NC150
lacks only LC 704 in Java; 300 `google`-tagged rows and 13 `MUST` rows have no Java. The
owner's decision is that **Python is the language the interview will be in** and Java —
used for LeetCode earlier — is the second tier. Two things follow:

- **The Java gap is not on the critical path.** `CLAUDE.md`'s framing of `/lc-java` as
  closing the directory "furthest behind" (`1244` Java rows against `2898` Python)
  measures a tier-2 goal; the recommendation is to say so where it is stated, so that a
  future session does not spend a week on Java parity. `/lc-java` stays useful for
  filing a Java draft when one exists; it should not drive what gets solved.
- **Nothing in the repo records the decision.** One line in the README's Resource section
  — *Python first; Java kept for reference* — is enough, and it is the line every skill
  and review will otherwise have to rediscover.

---

## 2. Solution code — an archive of attempts, not a set of model answers

The house layout is good and consistently applied: 2,897 of 2,897 Python files open with
the problem docstring and a `# V0` block; 99% carry a `time =` line. The problem is
what sits inside that layout.

### 2.1 `V0` is the canonical solution — and nothing tells the reader so

8,479 `# V…` blocks over 2,897 Python files; 753 files carry four or more variants. Java
is 5,990 blocks over 1,554 files. The convention is clear to the owner: **`V0` is the
favoured solution, the one to learn; `V1+` are references.** It is stated nowhere a
reader would see it — not in the files, not in `CLAUDE.md`'s description of the house
layout (`# V0` → `# IDEA` → `# time = …`), not on the site, whose explorer links to the
top of the file. A visitor sees three to five solutions and no signal about which is the
model answer.

**Recommendation:** make the convention visible, not add a new one. One sentence in the
house-layout description in `CLAUDE.md` and both filing skills (*`V0` is the solution to
learn; later blocks are references*); a `# V0` comment line that says the same in the
file template `/lc-python` writes; and the explorer and roadmap linking to `V0`'s line
(`#L<n>`) rather than the file top. A later pass can fold the near-duplicate `V0'`/`V0''`
spellings the August cheatsheet review counted (~450 in the sheets alone) — without
deleting variants, because the `// V` marker names the method in Java and `CLAUDE.md`
records what renumbering costs.

### 2.2 Tool attribution and debugging commentary have leaked into the archive

457 Java files and 202 Python files carry an IDEA line like `// IDEA: DFS (fixed by gpt)`,
`// IDEA: DP (gemini)`, `# IDEA: PREFIX SUM (gpt)`. The sampled
`SlideWindow/CountSubarraysWhereMaxElementAppearsAtLeastKTimes.java` also carries the
assistant's own commentary as code comments — *"1. Correctly find the maximum element…
The original loop was missing the comparison logic"* — and **two** javadoc blocks stating
the complexity in two formats (`Time Complexity: O(N)` then `time = O(N)`).

None of this helps a reader, and some of it hurts: *"fixed by gemini"* sits on `V0` —
the canonical block — and tells them the model answer needed a fix without saying what
the fix was, which is the one thing a learner would want to know. **Recommendation:** a
normalisation pass in the spirit of
`db49955` (the 1,481-file Java header cleanup) — move the attribution to a single
trailing `# ref:` line, delete debugging narration, dedupe the complexity javadoc — and
one rule added to both filing skills' `## Do not`: *no tool attribution in the IDEA line;
say what was wrong, not who fixed it.*

### 2.3 Verification is absent from the artefacts, and it is the weakest measured signal

73 of 2,897 Python files execute anything; Java has two `main` methods and no
`src/test` (PR #129 adds three sorting tests). Meanwhile the August readiness run's
weakest signal was a **31–44% submission rejection rate that is higher on Easy (44%)
than on Hard (31%)** — the signature of speed without a verification habit — and the
readiness guide's second priority is *"timed, no-run practice"*.

Every Python file already carries the problem's examples in its docstring, and
`/lc-python` already smoke-tests against them before filing. The test is run once and
thrown away. **Recommendation:** have `/lc-python` emit what it ran, as an
`if __name__ == "__main__":` block of asserts built from the docstring's examples, and
have `python-syntax-check.yml` *execute* the files that carry one. Cost: a few lines per
new file. Effect: the archive becomes a regression suite over time, and — the actual
point — writing the asserts before running is the verification habit the interview
scores, practised on every filing.

For Java, PR #129's `pom.xml` and `src/test` tree is the right foundation; land it and
have `/lc-java` add one JUnit method per filing.

### 2.4 Complexity annotations: finish Java, then check them

Python is at 99%; Java at 80% (307 files without a `time = O` javadoc). `/add-time-space`
exists for exactly this and is documented as a per-directory sweep. Once coverage is
complete, the higher-value check is *correctness*: the complexity quiz's grader
(`site/complexity.js`) can already parse and normalise a bound, so a script that
compares each file's `time =` line against the README row's complexity column would
find the ones that disagree. Nothing currently compares them.

### 2.5 Clean code is a scored signal, and `V0` is what gets reproduced under pressure

An L3 loop scores *coding* as one of four signals — naming, structure, idiom, the
absence of noise — and what a candidate writes in 35 minutes is whatever their hands
have practised. In this archive that is `V0`. Sampled at LC 438 (filed 2026-09-22),
`V0` is a correct O(n) fixed-window `Counter` solution, and inside its twenty-line loop
there are three triple-quoted comment blocks and two `# NOTE !!!` markers saying *add the
new char anyway*, *shrink the left pointer*. Across the tree:

- **1,027 `NOTE !!!` comments in 395 files**, and **875 files with triple-quoted string
  blocks used as comments inside function bodies**;
- **2,750 files declare `class Solution(object)`** — the Python 2 spelling, which
  LeetCode's Python 3 runtime accepts but no 2026 codebase writes — against **205 files
  with type hints**; 42 still carry `xrange` or print-statement spellings;
- the tool attribution and debug narration of §2.2.

These are learning notes, and useful as such — but they live *inside* the block that is
meant to be the model answer, so the model answer is not what a clean submission would
look like. **Recommendation:** a house style for `V0` only, enforced by `/lc-python`
going forward: type-hinted signature (`def findAnagrams(self, s: str, p: str) -> List[int]`),
the reasoning in the `# IDEA` block above the code, at most one short comment per
non-obvious line inside it, no shouting. Then a one-off clean-`V0` pass over the **L3
core set only** (§5.3, ~100 files) — not the archive — and `lc-coach`'s coding rubric
run against a handful of them to check the style reads as *Hire* on that signal.

---

## 3. Docs — the shape is right, the entry point is not

### 3.1 The cheatsheets are a reference library asked to be a course

135 sheets, 145,163 lines, 14 of them over 1,900 lines. Every one has a Scope line
(0 missing — the August cleanup held), the two-skeleton rule is in force, a 12-sheet
"start here" ladder exists, and cards carry a 1–5 tier. This is a very good library.

It is a poor *six-week syllabus*, because the unit of reading is the sheet and the sheet
is 2,000 lines. The tier-5 set is 22 sheets — the roadmap's 29 topics roughly — and a
candidate cannot read 40,000 lines. **Recommendation:** each tier-5 sheet gets a
one-screen block directly under its Scope line, in a fixed shape: the template (one
language, ~20 lines), the invariant in one sentence, the three problems to prove it on,
the follow-up an interviewer asks. The sheet's `Pattern Selection Strategy` and
`Summary` sections already hold this content; it is a matter of moving it to the top
and cutting. `build-site.js` can then render that block as the card's expanded state on
`cheatsheets.html`, so the index becomes the syllabus and the sheet stays the reference.

### 3.2 Recognition is strong; derivation is thin

42 of 135 sheets use the word *invariant*; 42 mention what to say to an interviewer.
The draft review in PR #155 measured this directly — **17 of 22 tier-5 sheets never name
a brute-force baseline**, opening at the optimal template with no route back — and found
one correctness defect (the `(l+r)/2` overflow shipped in a template that lists it as a
pitfall; 19 such blocks against 48 safe). This review confirms the shape of that
finding and endorses landing that PR. Two additions:

- `lc-coach`'s `references/talk-track.md` and `references/patterns.md` already hold the
  in-the-room structure (brute → observation → template → complexity line → follow-up).
  The sheets should *link* to them rather than restate, so the coach and the sheet agree.
- `pattern_recognition.md` (157 lines, built to `patterns.html`) is the single highest-
  leverage doc for recognition and the smallest. Each row maps a keyword to a pattern
  and a sheet; add the *visualizer* and the *brute-force baseline* columns, and it
  becomes the page a candidate reads the night before.

### 3.3 `doc/` has no index, and a reader cannot tell which plan is current

`doc/` holds three Google prep plans (`goog_swe_prep_plan_claude.md`, `_gpt.md`,
`_gpt_v2.md`), six `leetcode_company_V1`–`V6` directories of vendored PDFs, a 10-line
`code_interview_general_cheatsheet.md`, a 15-line `routine.md`, a 29-line
`priority_queue.md`, and text dumps (`crack_fanng_interview.txt`, `tech_blog.txt`,
`solved_1000_LC.txt`). Alongside them sit the documents that *are* current —
`lc-readiness-guide.md`, `google_swe_lc_essentials.md`, `g_swe_final_week_review.md`,
the two reviews and the skills plan — and nothing says which is which. (The August
review's `lc_category.md` finding has been fixed: it is now a pointer to the upstream
taxonomy, as it should be.)

**Recommendation:** a `doc/README.md` with three lists — *current*, *historical*,
*vendored* — and a move of the superseded prep plans into `archived/` (which already
exists for this purpose). Merge the three prep plans into one that names the L3 bar, and
fold `code_interview_general_cheatsheet.md` into a neighbour as the August review said.

### 3.4 The FAQs and system design are out of scope — label them so

Forty-nine FAQs (Java, JVM, Kafka, Spark, Redis, Flink, Airflow) and eleven
`system_design/` case studies are backend and data-engineering interview material, not
what a Google L3 coding loop tests. The owner's decision is that they stay as they are
and documentation effort goes to `doc/cheatsheet/`. This review makes no recommendation
about their content. The one thing worth doing is on the *landing page*, not in those
trees: mark them as outside the coding-loop path, so a visitor preparing for one does
not spend the evening in `faq_kafka.md`.

### 3.5 The formatting rules are holding

Every one of the 135 sheets has a Scope line, every opening fence carries a language
tag, and no sheet lacks a `cheatsheet_meta.json` entry (the build would fail). The
August cleanup's rules have held for a month without a gate on the markdown itself; the
one thing still unenforced is the *"complexity stated once"* and *"one canonical
solution"* pair, which need a reader rather than a regex.

---

## 4. Site — strong tooling, no session loop

The site is the strongest part of the project as engineering: `build.sh` is the one
recipe, `e2e-check.js` walks every page and fails on the classes of breakage that have
actually shipped, the planner and the problems filter are lifted verbatim out of the
*built* page and run in jsdom, and 418 unit tests pass. The August 31 review's fixes
all held. What follows is about what the site does for a candidate, not whether it works.

### 4.1 What a first visit sees

The live landing page shows three groups of four cards, a nine-card agent-skills band,
and six counts (`3,270 · 134 · 49 · 36 · 269 OK · 997 AGAIN`). Two things a first-time
visitor will trip on:

- **`OK` / `AGAIN` is unexplained.** They are the two most important numbers on the page
  and they are internal vocabulary. One sentence under the count — *"problems this
  engineer has re-derived clean, against ones still on the review list"* — fixes it.
- **Fifteen entry points and no time-boxed path.** A candidate with 45 minutes has no
  card that says *start here for 45 minutes*. See 4.2.

### 4.2 There is no timed practice mode

No page implements a timer. The random picker draws a problem; the review plan picks a
session; neither times it, neither asks for the complexity before revealing the
solution, and neither produces the line to paste into `/lc-log`. Given that the
measured weakness is timed execution and verification, this is the single largest gap
between what the site has and what the goal needs.

**Recommendation — a *session* page, or a mode on the random picker:** choose a length
(20 / 35 / 45 minutes) and a list; the page draws, starts a countdown, and hides the
solution link; at the end it asks for the time and space bound (graded by the
`complexity.js` parser the quiz already uses) and *then* reveals the repo's solution;
finally it prints the `progress.txt` line for the session (`1234(ok), 567(again!!)`)
ready to paste. Entirely client-side, no new data, and it reuses three things the site
already owns — the list picker, the complexity grader and the log grammar.

### 4.3 Three progress records, and the primary one is not the one the site leads with

Progress lives in three places: `data/progress.txt` (the log — **the primary record**,
written daily via `/lc-log`), the README status column (`OK`/`AGAIN`, updated only when
needed via `/lc-again`, so it lags), and the roadmap's per-browser `localStorage`
checkboxes, which the page itself says are *"stored in this browser only and never
uploaded"*. The review plan already compiles the log into `progress.json` at build time
and folds README in. But the landing page's `269 OK / 997 AGAIN` is the README column,
and the roadmap reads neither. **Recommendation:** make `progress.json` — the log's
compiled form — the source for every progress display: the landing counts become
*latest verdict in the log*, the roadmap renders `ok` as done and `again` as in-progress,
and `localStorage` keeps only the browser's own ticks. Then the roadmap's locks mean
something, and a candidate sees one picture of where they are that moves the day the
log does.

### 4.4 The explorer's facets do not include the ones a candidate uses

`lc-explorer.html` filters by tag, difficulty and acceptance rate. It does not filter by
status (`OK` / `AGAIN` / untracked), by curated list (the `blind75` … tags are in the
tag column, so this is one facet away), by language available, or by pass count. The
data is all in `lc-problems.json` or one build step from it. Adding *status* and *list*
as first-class facets turns the explorer into the campaign tracker §1.2 asks for.

### 4.5 Open items from the August 31 review, still open

Not re-verified here; listed so they are not lost: search indexes titles and headings
only, `search.html` fetches ~900 KB on load, the TOC is not sticky, the site defaults
to dark without reading `prefers-color-scheme` (no stylesheet in `site/` references it),
`https://` redirects to `http://`. None of them is an interview lever; the colour-scheme
one is a five-line fix.

### 4.6 In flight, and worth landing

PR #171 (filter the problem index in place), PR #155 (interviewer-perspective cheatsheet
review), PR #129 (Java `pom.xml` + `src/test` in CI). Each closes something this review
would otherwise recommend. Eleven PRs are open and 122 remote branches exist, several of
them `backup-*` and `worktree-*` from earlier sessions; a branch sweep would make the
in-flight set legible.

---

## 5. Direction — the repo optimises for accumulation; the goal needs conversion

### 5.1 Two projects share one repository

CS_basics is a **personal preparation log** (the status column, the practice log, the
readiness script, `/lc-log`, `/lc-again`, `/lc-coach`) and a **public reference** (the
cheatsheets, visualizers, roadmap, quiz, FAQs). The landing page's lede — *"the notes and
solutions behind one engineer's interview preparation"* — names this honestly. The
tension shows where the personal layer leaks into the public one without explanation
(`AGAIN` on the front page, `(fixed by gemini)` in the archive, three prep plans in
`doc/`). Declaring the two layers — a *my status* toggle on the site, a `doc/README.md`
that separates the log from the library — resolves most of §3.3, §4.1 and §2.2 at once.

### 5.2 The activity is extraordinary; point it at conversion

The log shows practice on **30 of the last 30 days** and 87 of the last 90 — 469 logged
days across 2025–26. That effort is currently spent mostly on *new* attempts: 650 `again`
annotations against 163 `ok` in the log, and the OK share on Blind 75 is 20%. The
readiness guide's own priority order puts volume last and says *"if Volume already
scores A, more solved problems cannot move the overall grade."* Volume scores A.

**A 90-day direction, in the order the readiness guide gives:**

1. **Signal (weeks 1–12):** two timed sessions a week on the §4.2 session page or a
   LeetCode virtual contest. One contest in nine years is the weakest measured number.
2. **Verification (weeks 1–12):** asserts before running, on every filing (§2.3).
3. **Cost curve (weeks 1–8):** derivation cards for the 40 highest-cost problems (§1.3);
   review the card, not the problem.
4. **Mastery (weeks 2–10):** the Blind 75 campaign to 80% latest-`ok` in the log (§1.2),
   then NeetCode 150.
5. **Volume:** none. Freeze the problem count at 3,270 for the quarter and say so in the
   README.

### 5.3 Measure monthly, on a fixed set

Ship a named list — call it **`l3-core`** — in `data/roadmap.json` (`from: curated`, or
`list:` over a new flag in `problem_lists.json`): Blind 75 ∪ (NeetCode 150 ∩ `google`),
which is roughly 100 problems. Then the numbers to watch each month, all of which the
build already computes or one step from it:

| Metric | Source | Now | Target (90 days) |
|---|---|---|---|
| Latest-`ok` share on Blind 75 | log | 19% (14/75) | 80% |
| Blind 75 problems with no verdict | log | 25 | 0 |
| Latest-`ok` share on NeetCode 150 | log | 12% (18/150) | 50% |
| Problems ending on `again` after 8+ attempts | log | 83 | < 40 |
| Avg passes — Recursion / BST / Backtracking | README stars | 7.4 / 7.0 / 6.3 | flat (stop re-solving them) |
| Timed sessions logged | log | 0 in the log's grammar | 24 |
| Submission rejection rate | readiness script | 31–44% | < 25%, Easy below Hard |

`eval_lc_readiness.py --json data/readiness-YYYY-MM.json` is already the tracking
command; the guide says monthly. Add the log-based `ok`-share lines to its output and it
is done.

---

## Prioritised action list

Ordered by impact on the goal per unit of effort. **S** is an afternoon, **M** a
weekend, **L** a multi-week thread.

| # | Action | Axis | Effort | Where |
|---|---|---|---|---|
| 1 | Blind 75 campaign to 80% latest-`ok` in the log, under the four-question bar; every attempt gets a verdict | Mastery | L (practice time, not code) | `data/progress.txt` via `/lc-log`; README via `/lc-again` when the owner chooses |
| 2 | Timed session mode: countdown, complexity check before reveal, `progress.txt` line out | Site | M | `site/pages/lc-random-picker.html` or a new `lc-session.html` |
| 3 | `/lc-python` emits the docstring examples as `__main__` asserts; CI runs them | Code | S + S | `.claude/skills/lc-python/SKILL.md`, `python-syntax-check.yml` |
| 4 | Derivation cards for the ~40 highest-cost problems | Docs | L | new `doc/derivation_cards.md`, linked from the Note column |
| 5 | One-screen "in the room" block under the Scope line of each tier-5 sheet; land PR #155 | Docs | L | `doc/cheatsheet/*.md`, `build-site.js` card render |
| 6 | Gate the index: dead links, unlinked files, duplicate ids, status grammar, log dates | Coverage | M | new `script/check_readme.py`, `validate-pages.yml` |
| 7 | Normalise the archive: attribution → `ref:` line, delete debug narration, dedupe javadoc; then a clean-`V0` pass (type hints, no `NOTE !!!`, reasoning in `# IDEA`) over the L3 core set only | Code | M (scripted) + M | `leetcode_python/`, `leetcode_java/` |
| 8 | Say that `V0` is the canonical solution — in `CLAUDE.md`'s layout description, both filing skills and the file template; site links to `V0`'s line | Code | S | `CLAUDE.md`, skills, `build-leetcode.js` |
| 9 | Explorer facets: status, curated list, language | Site | S | `site/pages/lc-explorer.html`, `build-leetcode.js` |
| 10 | Roadmap and landing counts read `progress.json` (the log); latest `ok` = done | Site | S | `site/roadmap.js`, `build-roadmap.js`, `build-site.js` |
| 11 | `l3-core` list + its log-based `ok` share on the landing page and in the readiness JSON | Direction | S | `data/roadmap.json`, `build-site.js`, `eval_lc_readiness.py` |
| 12 | `doc/README.md` (current / historical / vendored); archive the two superseded prep plans | Docs | S | `doc/` |
| 13 | Explain `ok` / `again` on the landing page; mark the FAQs and system design as outside the coding-loop path | Site | S | `build-site.js` |
| 14 | Mark imported rows (`imported` status) so counts and facets can distinguish them; make the 21 duplicates a build error | Coverage | M | `fix_readme_tags.py`, `build-roadmap.js` |
| 15 | Finish Java `time =` coverage (307 files) with `/add-time-space` — tier 2, when there is nothing above it to do | Code | M | `leetcode_java/` |
| 16 | Record "Python first, Java for reference" in README; re-word `CLAUDE.md`'s `/lc-java` framing so Java parity stops reading as a goal | Coverage | S | `README.md`, `CLAUDE.md` |
| 17 | Sweep stale `backup-*` / `worktree-*` branches; close or land the eleven open PRs | Direction | S | GitHub |

Items 1–5 are the ones that move the grade. Items 6–17 are the ones that stop it
sliding back.

---

## What this review did not cover

- **The correctness of individual solutions.** 4,000 files were counted, sampled and
  grepped, not read. The one file read in full (§2.2) was chosen because it was recent,
  not because it was suspected.
- **Per-sheet cheatsheet content.** That is PR #155's job; this review only measured the
  corpus and confirmed the shape of its finding.
- **`system_design/`, the FAQs and the translation backlog** beyond noting that they are
  outside the L3 coding-loop bar this project has set for itself.
- **The LeetCode-side numbers** (rejection rate, contest count, topic gaps by LeetCode
  tag). They are quoted from the August readiness run, not re-fetched.
- **The August 31 site findings** are listed as still open, not re-verified.

---

## Appendix — how the numbers were produced

All from the repo root at `953902764`, with `site/node_modules` installed.

```bash
# README: unique problems, difficulty, languages, curated-list coverage
node -e '
const fs=require("fs");
const {parseReadmeProblems}=require("./site/build-roadmap.js");
const p=parseReadmeProblems(fs.readFileSync("README.md","utf8"));
const pl=JSON.parse(fs.readFileSync("data/problem_lists.json","utf8"));
console.log(p.size);
for (const L of Object.keys(pl.counts)) {
  const items=pl.problems.filter(x=>x.lists.includes(L));
  console.log(L, items.length, items.filter(x=>p.has(String(x.id))).length);
}'

# README: the status column, per table set and per section
# (parseReadmeProblems drops the status cell, so read the rows directly)
awk -F'|' '/^## Newly Added/ { nw = 1 }
           /^\| *[0-9]{3,4} *\|/ { s = $(NF-1); gsub(/^ +| +$/, "", s); print (nw ? "NEW" : "MAIN"), s }' README.md \
  | sort | uniq -c | sort -rn | head

# Solution links that do not resolve, and files no row links to
node -e '
const fs=require("fs"),path=require("path");
const {parseReadmeProblems}=require("./site/build-roadmap.js");
const p=parseReadmeProblems(fs.readFileSync("README.md","utf8"));
const linked=new Set(), broken=[];
for (const r of p.values()) for (const [lang,u] of Object.entries(r.solutions)) {
  const rel=u.replace(/^https:\/\/github.com\/yennanliu\/CS_basics\/blob\/master\//,"");
  linked.add(rel); if (!fs.existsSync(rel)) broken.push(r.id+" "+lang+" "+rel);
}
const walk=(d,ext)=>fs.readdirSync(d,{withFileTypes:true}).flatMap(f=>f.isDirectory()?walk(path.join(d,f.name),ext):f.name.endsWith(ext)?[path.join(d,f.name)]:[]);
console.log("broken", broken.length);
console.log("py unlinked", walk("leetcode_python",".py").filter(f=>!linked.has(f)).length);
console.log("java unlinked", walk("leetcode_java/src/main/java/LeetCodeJava",".java").filter(f=>!linked.has(f)).length);'

# Solution archive: variants, attribution, verification, complexity lines
grep -hoE "^# V[0-9]" leetcode_python/*/*.py | wc -l
grep -rhoE "^\s*// V[0-9]" leetcode_java/src/main/java/LeetCodeJava | wc -l
grep -rliE "fixed by gemini|by gpt|\(gemini\)|\(gpt\)" leetcode_java/src/main/java/LeetCodeJava | wc -l
grep -liE "gemini|gpt" leetcode_python/*/*.py | wc -l
grep -lE "^\s*(assert |if __name__)" leetcode_python/*/*.py | wc -l
grep -rl "public static void main" leetcode_java/src/main/java/LeetCodeJava | wc -l
grep -l "time *= *O" leetcode_python/*/*.py | wc -l
grep -rl "time = O" leetcode_java/src/main/java/LeetCodeJava | wc -l

# Cheatsheets and FAQs
ls doc/cheatsheet/*.md | wc -l; wc -l doc/cheatsheet/*.md | tail -1
grep -li "invariant" doc/cheatsheet/*.md | wc -l
find doc/faq -name '*.md' | wc -l

# Practice log
grep -oE "^[0-9]{8}" data/progress.txt | sort -u | wc -l
grep -oE "[0-9]{1,4}\((again!*|ok\*?|todo)" data/progress.txt | sed -E 's/.*\(//; s/[!*]//g' | sort | uniq -c

# Practice log: latest verdict per problem, then the share per curated list
python3 - <<'EOF'
import json, re, collections
txt = open('data/progress.txt').read(); lists = json.load(open('data/problem_lists.json'))
latest, cur = {}, None
for line in txt.split('\n'):
    m = re.match(r'^(\d{8})', line)
    if m: cur = m.group(1)
    if not cur: continue
    for num, note in re.findall(r'(?<![\w\d])(\d{1,4})\(([^)]*)\)', line):
        n = note.lower()
        latest[num] = 'again' if 'again' in n else 'ok' if re.search(r'\bok\b', n) else 'none'
    bare = re.sub(r'\d{1,4}\([^)]*\)', '', line)
    for num in re.findall(r'(?<![\w\d])(\d{1,4})(?=[,| \t]|$)', bare):
        if not re.match(r'^\d{8}$', num): latest[num] = 'none'
for L in ('blind75', 'neetcode150', 'neetcode250', 'top100liked'):
    ids = [str(p['id']) for p in lists['problems'] if L in p['lists']]
    print(L, collections.Counter(latest.get(i, 'never') for i in ids))
EOF

# Clean-code signals in the Python archive
grep -c "NOTE !!!" leetcode_python/*/*.py | awk -F: '{s+=$2} END{print s}'
grep -lE '^\s{8,}"""' leetcode_python/*/*.py | wc -l
grep -l "class Solution(object)" leetcode_python/*/*.py | wc -l
grep -lE "def [a-zA-Z_]+\(self, [^)]*: " leetcode_python/*/*.py | wc -l

# Site
npm test --prefix site           # 418 tests
ls site/pages | wc -l; ls algo_demo/*.html | wc -l
python3 -c "import json;print(len(json.load(open('data/roadmap.json'))['nodes']), len(json.load(open('data/complexity_quiz.json'))['questions']))"
```

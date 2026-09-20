# Agent Skills — Expansion Plan (Sep 2026)

**Status: implemented, Sep 2026.** Phases 1–7 landed in three commits; `lc-sql` and the
`system-architecture` upgrade remain deferred, as planned. This document is kept as the
record of *why* each skill earns its place and *what else had to move* when it landed — see
[Order of work](#order-of-work) for what actually happened per phase.

Background: the survey this plan came out of is in the "Where the gaps are" section below;
the skills are documented in [CLAUDE.md](../CLAUDE.md) under *Filing a solved problem*,
*Tracking the practice*, *Maintaining the site*, *Updating a cheatsheet* and *Coaching a
coding interview*.

---

## The naming convention this plan settles

**Every repo-native skill is `lc-` plus a kebab-case name.** The `lc-` prefix is not
decoration — it is what separates a skill that knows this repo's house style from a generic
persona that does not, and it keeps the whole family adjacent in `.claude/skills/`, in the
navbar's *agent skills* group and in a `/lc-<tab>` completion.

What follows the prefix is whatever reads most clearly at the call site, which in practice
is a mix: a verb where the skill *does* something to a thing (`lc-log`, `lc-again`,
`lc-zh-translate`), and the thing itself where the verb would be noise (`lc-python`,
`lc-java`, `lc-site-data`, `lc-algo-demo` — "file a Python solution" is the only thing
`/lc-python` could mean). `check_skills.py` enforces the kebab-case rule and that the name
matches the directory; it does not enforce a part of speech.

The directory name **is** the slash command, and `script/check_skills.py` pins the
frontmatter `name` to the directory — so a rename is a rename of the command, and the two
can never drift. That is the reason the rename in Part 1 is worth doing now rather than
after six more skills are wired to the old name.

---

## Where the gaps are

Measured over the last 300 commits and the current tree:

| Signal | Number | What it says |
|---|---|---|
| `leetcode_java` file-touches | **1481** | the most-churned directory in the repo — and it has no skill |
| `leetcode_python` file-touches | 1099 | less churn, and it *does* have a skill (`lc-add`) |
| `update progress` commits | **63 of 300** | the single most repeated hand edit, on a file no skill may touch |
| `update ws` commits | 14 | Java drafts staged in `ws/Workspace26.java`, filed by hand |
| README rows linking Java / Python | **1244 / 2898** | a ~1650-row Java gap |
| README status cells | **325 AGAIN / 124 OK** | the AGAIN marker only ever accretes |
| `data/again_problems.txt` generated | **Nov 2025** | ten months stale |
| `i18n` file-touches | 123 | translation is done in campaigns, 97% (6040/6220) with 4 sheets at 0% |
| `algo_demo/` pages | 36 + 2 shared files | no generator, every invariant enforced by convention only |
| `leetcode_SQL/` files | 167 | real README rows, zero tooling |

The two highest-frequency activities in this repo — **filing Java solutions** and **logging
practice** — have no skill at all, while the lower-churn Python path has one. That asymmetry
is what this plan corrects first.

---

## Part 1 — Rename `lc-add` → `lc-python`, add `lc-java`

### Why rename

`lc-add` was named when Python was the only target. "Add" no longer says *what* it adds, and
the moment a Java sibling exists the name actively misleads: `/lc-add 25 LinkedList` reads
like it would file either. Two commands whose names name their language cannot be confused:

```text
/lc-python 4038 Hash_table      # → leetcode_python/Hash_table/<slug>.py
/lc-java   25 LinkedList        # → leetcode_java/src/main/java/LeetCodeJava/LinkedList/<Class>.java
```

The recipe inside `lc-add/SKILL.md` does not change — it is already Python-specific
throughout (the `# V0` / `# IDEA` / `# time = O(...)` layout, `leetcode_python/<Pattern_Dir>/`,
the smoke test against the docstring's own examples). Only the name, the invocation lines and
the wiring move.

### What `lc-java` owns

The Java counterpart, which is a genuinely different recipe, not a translation of the Python one:

- **Package placement.** `leetcode_java/src/main/java/LeetCodeJava/<Pattern>/` — the directory
  decides the `package` line, and a wrong one compiles nowhere. Weekly-contest work goes to
  `LCWeekly/` instead.
- **The header block.** The house Java file opens with the `package` line, then the
  `https://leetcode.com/problems/<slug>/description/` and neetcode url comments, then the
  `/** N. Title / Difficulty / full statement / Examples / Constraints */` javadoc. The url
  comment is not cosmetic — it is one of the three join signals `script/find_missing_java.py`
  needs to answer "which problems still need Java?", and ~60 files already lack it.
- **Shared data structures.** `import LeetCodeJava.DataStructure.ListNode;` (and `TreeNode`,
  `Node`) rather than a re-declared inner class.
- **The complexity line**, in the form `add-time-space` already normalises.
- **The README edit is an *update*, not an insert.** A problem with a Python solution already
  has a row; `lc-java` adds `[Java](./leetcode_java/...)` to that row's Solution cell. This is
  the failure the skill exists to prevent — a second row for a problem that already has one.
- **The `ws/` hand-off.** 14 of the last 300 commits are `update ws`: drafts are written in
  `ws/Workspace26.java` and filed later. `lc-java` takes a draft from there (or pasted) and
  files it, leaving the scratchpad clean.

Gates, as `lc-python` and `lc-cheatsheet` already do: compile the file, run it against the
javadoc's own examples, then `python3 script/find_missing_java.py` and
`python3 script/check_skills.py`. It never touches `data/progress.txt`.

### Fixing the drifting Tier 2

Six skills predate the house style and know nothing about it — no README row format, no
`LeetCodeJava/<Pattern>` packages, no `V0`/`IDEA` layout, no `progress.txt`:

| Skill | Lines | Disposition |
|---|---|---|
| `java-developer` | 145 | **fold into `lc-java`** — it is a generic "write good Java" persona that now has a repo-native replacement, and leaving it in place gives two competing triggers for the same request |
| `java-python-code-reviewer` | 257 | **fold into `lc-coach`** — substantially overlaps `lc-coach`'s review mode, which scores on the six-point scale the repo actually uses |
| `code-refactor-master` | 639 | **keep, unprefixed** — generic clean-code advice with no LeetCode hook; it is not a repo skill and should not pretend to be one |
| `markdown-doc-writer` | 507 | **keep, unprefixed** — same reasoning; `lc-cheatsheet` already owns the repo-shaped writing |
| `system-architecture` | 348 | **keep, upgrade later** — it only knows `system_design/00_template.md`'s path; teaching it the case-study layout and `capacity_estimation_cheatsheet.md` is a smaller job than a new skill (see *Deferred* below) |
| `add-time-space` | 70 | **keep as-is** — narrow, works, and `lc-java` will call it rather than duplicate it |

Also to check while in there: `code-refactor-master` and `java-python-code-reviewer` declare
`allowed-tools: ..., LSP`, which is not a tool name `check_skills.py` validates. Confirm it is
not silently narrowing what those skills can do.

---

## Part 2 — Six new skills

All `lc-`-prefixed. Listed in the order they should be built.

### `lc-log` — append today's session to `data/progress.txt`

**Why:** 63 of the last 300 commits are `update progress`. CLAUDE.md deliberately forbids
`lc-python` and `lc-cheatsheet` from touching this file, so it is edited entirely by hand —
and it is **the only copy** of the review schedule that `lc-review-plan.html` is built from.

**What it does:** takes what was practised today and writes the line in the log's own shape —
the `YYYYMMDD:` date, the bucket labels (`top 100 like(dp):`, `others:`, `LC weekly:`), the
`|` separators, and the carry-over buckets from yesterday's line that are still open. Gets the
annotations right, because they are what the whole planner keys on: `again!!!` vs `again` vs
`ok` vs `ok*` vs `todo`, where `again` beats `ok` when a note says both and the bang count sorts.

**Prevents:** a line `site/build-review-plan.js` cannot parse — which does not error, it
silently shrinks the schedule — a dropped carry-over bucket, and date-format drift.

**Gate:** `node site/build-review-plan.js` then `npm test --prefix site`
(`site/test/build-review-plan.test.js` pins each shape against the real file).

### `lc-again` — close the AGAIN loop

**Why:** README holds **325 AGAIN** cells against **124 OK**; `data/again_problems.txt` was
last regenerated in **Nov 2025**. Nothing in the repo ever promotes a row from `AGAIN***` to
`OK` — the marker only accretes, which the stored readiness assessment already names as a
known failure mode.

**What it does:** takes a problem just re-solved and decides whether it *graduates* — **all
four** of: re-solved unaided, the invariant stated out loud, the line that sets the complexity
named, and the edge cases handled — then edits the README status cell (`AGAIN***` → `OK`,
preserving the attempt count), and refreshes
`data/again_problems.txt` via `script/get_again_problems.sh`. Refuses to graduate a problem the
user could not re-derive; that is the whole point of the marker.

**Prevents:** the measured failure — a status column that is 72% AGAIN and therefore carries
no information.

**Gate:** `bash script/get_again_problems.sh`, plus the README format checks.

### `lc-zh-translate` — work the translation backlog

**Why:** `node script/zh.js status` reports **6040/6220** sections (97%) across 179/183
documents, **17 orphaned entries**, and four sheets untouched: `dp_loop_order` 0/33,
`patience_sorting` 0/24, `math_logic_puzzles` 0/14, `memory_constrained_algorithms` 0/14.
123 `i18n` file-touches and ~15 consecutive translation commits show this is done in campaigns.
`lc-cheatsheet` only re-translates what *an edit* parked; nothing drives the standing backlog.

**What it does:** runs the documented loop — `sync` → `todo <id>` → write each section back
into `i18n/zh/<id>.md` under its `<!-- key -->` → `sync` → `status --write` — while holding the
rules that make an overlay legal: every `<!--CODE-->` marker kept, in order (`compose` throws
otherwise); structure and heading order taken from the English document; links keeping their
English targets; no `category`/`tier`/`kind`; the Scope line becoming `> **範圍** — …`; and API,
class and command names staying in English.

**Prevents:** a translation that `compose` rejects at build time, a parked entry thrown away
rather than adapted, and a progress doc edited by hand (it is generated).

**Gate:** `node script/zh.js status`, `bash site/build.sh`, `node site/e2e-check.js _site`.

### `lc-algo-demo` — add an algorithm visualizer

**Why:** 36 hand-written pages plus `common.js` and `style.css`, copied wholesale by
`build.sh` with **no generator** — so every invariant is enforced by convention alone. CLAUDE.md
already lists the ways it breaks, each because it broke.

**What it does:** writes a new `algo_demo/<name>.html` against the shared contract:

- colours read from `VIZ` (the `--viz-*` tokens), never a literal, so both themes and any
  repaint stay in one place;
- no re-implementation of the `getContext('2d')` wrapper — the devicePixelRatio scaling and
  the `font` setter are already there for every page;
- `createLogger(id)`'s three message shapes (numbered step, indented reason, `--- x ---` phase
  heading), with a whole-message `<span class="highlight">` reserved for the run's outcome;
- the trace in its own full-width `.viz-trace` panel below the canvas, never back in the 300px
  control column;
- `draw = VIZ.repaintable(draw)` plus `window.addEventListener('resize', draw.repaint)` for any
  `draw()` that takes arguments — a theme switch fires a resize, so the plain handler throws the
  highlight away mid-run.

Anything that should look or behave the same on all 37 pages goes in the two shared files.

**Gate:** `npm test --prefix site` (`site/test/algo-demo.test.js` evaluates the shipped
`common.js` in jsdom), `bash site/build.sh`, `node site/e2e-check.js _site`.

### `lc-site-data` — add a roadmap topic or a complexity-quiz question

**Why:** low frequency (42 `site` touches) but every attempt is a build-break-and-retry loop,
because both files are validated hard and the contracts span three or four files.

**What it does:** adds the entry to `data/roadmap.json` or `data/complexity_quiz.json` and
satisfies the validators in one pass, per the *Adding a topic to the Study Roadmap* and
*Adding a complexity-quiz question* sections of CLAUDE.md:

- roadmap: `problems` ids that README knows, `sheets` slugs that exist, `row` strictly greater
  than every prereq's row, no cycle, and no edge the graph already implies (the roadmap must
  stay a transitive reduction);
- quiz: no duplicate `id`, an `lc` number README knows, `title`/`difficulty` set **only** for
  `"lc": null` entries, `accept` always an array, and every answer parseable by
  `site/complexity.js` — whose identifiers are single letters, so `O(n * a)` with a `vars` line,
  never `O(n * amount)`.

Never repeats titles, difficulty or solution links in either file — those come from README at
build time.

**Gate:** `bash site/build.sh` (the validators fail the build), `node site/e2e-check.js _site`,
`npm test --prefix site`.

### `lc-faq-add` — file a Q&A into `doc/faq/`

**Why:** 49 documents across 12 sub-trees, **fully translated (902/902)** — so every new English
answer immediately owes a 中文 section, or the tree stops being 100% and the gap is invisible
until `status` is next run. No skill knows the tree; `markdown-doc-writer` knows neither it nor
the overlay.

**What it does:** places the answer in the FAQ that owns the topic, in the house shape, then
writes the matching `i18n/zh/faq/<path>.md` section in the same pass — including the lead
paragraph, because an FAQ has no Scope line and its card on `faqs.zh.html` is summarised from
the *composed Chinese*.

**Gate:** `node script/zh.js status faq`, `bash site/build.sh`, `node site/e2e-check.js _site`.

### Deferred, deliberately

- **`lc-sql`** — 167 `.sql` files with real README rows (complexity columns, `**sql**` tags) and
  zero tooling, plus inconsistent filenames (`big_countries.sql`, `Second_Highest_Salary.sql`
  among 160 kebab-case slugs). A thin `lc-python` sibling. Worth doing, but after the six above.
- **`system-architecture` upgrade** — teach the existing skill the `system_design/` case-study
  layout, `00_template.md` and `capacity_estimation_cheatsheet.md` rather than adding a tenth
  skill. Most case-study directories are currently a lone `README.md`.

---

## Part 3 — Site and wiring

A skill is not landed until it is wired. Three things move for **every** new or renamed skill.

### 3a. The rename's blast radius

`lc-add` is named in 12 places outside its own directory. All of them move together, in one
commit, or `check_skills.py` / `e2e-check.js` / `npm test` fails:

| File | What changes |
|---|---|
| `.claude/skills/lc-add/` | directory → `lc-python/`; frontmatter `name:` follows (the gate pins them equal) |
| `.claude/skills/lc-python/SKILL.md` | the `**Invocation**` line and the worked example at the foot |
| `CLAUDE.md` | the section heading + its anchor, and the 5 other `/lc-add` mentions — including the cross-reference from the cheatsheet-slug note, and `site/pages/` in the skills-gate section |
| `site/pages/lc-add.html` | → `lc-python.html`; ~15 in-page mentions, the install `cp -r`, the zip line, the raw-GitHub URL, `data-page` |
| `site/pages/skills.html`, `site/pages/lc-cheatsheet.html` | the sibling links and prose |
| `site/build-site.js` | the `AGENT_SKILLS` entry (href, `/command`, title, blurb) |
| `site/nav.js` | the `agent-skills` group child, and the comment above `MORE` that names the family |
| `site/e2e-check.js` | the page list (line ~52) and the hand-written-pages list (line ~418) |
| `site/test/nav.test.js` | four assertions naming `lc-add` by id and by rendered `<a>` |
| `script/check_skills.py` | `WIRING_SOURCES` |
| `.github/workflows/skills-check.yml` | two path filters |
| `doc/utility-scripts.md` | the wiring row, if it names the page |

**Open decision — the old URL.** `lc-add.html` is linked only from inside the site (nav,
landing, two sibling pages), never from README, but it is in the published `sitemap.xml` and so
may be externally indexed. There is no HTML-redirect precedent in this repo (the cheatsheet
`kind: "stub"` is an index chip, not a redirect). Recommendation: **rename outright**, accept
the 404 on the old URL, and let `finalize-pages.js` rewrite the sitemap — the page is ten weeks
old and the cost of inventing a redirect mechanism for one URL is higher than the loss.

### 3b. What each new skill needs on the site

Following exactly what `lc-add` and `lc-cheatsheet` already do:

1. **A page** — `site/pages/lc-<name>.html`, hand-maintained (copied by `build.sh`, given its
   canonical / Open Graph / Twitter tags by `finalize-pages.js`). In the vocabulary the three
   existing skill pages share: the failure modes it exists to prevent, the steps as a stepper,
   a real worked run, per-agent install. **Editing `SKILL.md` does not update the page** — the
   two are kept in step by hand, and that is stated on each page for a reason.
2. **A navbar entry** — a child of the `agent-skills` group in `site/nav.js`, plus the matching
   ids in `site/test/nav.test.js`. Not a top-level slot: the group exists so the family can grow
   without spending one per skill.
3. **A landing-page card** — an `AGENT_SKILLS` entry in `site/build-site.js`, in the
   `.skills-band`. **Not** in `ENTRY_GROUPS`: agent skills are a different kind of thing from
   the rest of the site — markdown you install into your own agent, working on your code rather
   than on these pages.

Plus, off the site: a `## <Skill> — /lc-<name>` section in **CLAUDE.md** (prose, cross-links and
the failure modes — never the steps, which live once in `SKILL.md`), and the new page added to
`WIRING_SOURCES` in `script/check_skills.py` and to the path filters in
`.github/workflows/skills-check.yml`.

The landing page's skill **count** is read from `.claude/skills` at build time
(`build-site.js` ~line 939), so it updates itself. Do not hardcode it — and the same goes for
every other count on that page.

### 3c. What the band looks like when this is done

**Landed at ten cards, grouped** — see the note under [Order of work](#order-of-work) on why
this came forward. Ten in one flat grid is a list, not a band, so `AGENT_SKILLS` is three
labelled groups the way `ENTRY_GROUPS` already groups the main cards:

| Group | Skills |
|---|---|
| **File what you solved** | `lc-python`, `lc-java`, `lc-cheatsheet`, (later `lc-sql`) |
| **Track the practice** | `lc-log`, `lc-again`, `lc-coach` |
| **Maintain the site** | `lc-algo-demo`, `lc-site-data`, `lc-zh-translate`, `lc-faq-add` |

`lc-coach` sits under *Track the practice* rather than with the filing skills: it scores the
attempt, it does not file anything. `lc-sql` would join the first group when it lands.

---

## Order of work

Each phase is its own commit (or PR), and each ends green on
`python3 script/check_skills.py --install`, `bash site/build.sh`,
`node site/e2e-check.js _site`, `npm test --prefix site`.

| Phase | Work | Outcome |
|---|---|---|
| 1–2 | Rename `lc-add` → `lc-python`, add `lc-java`, retire `java-developer` | **done** — all 12 wiring sites in one commit |
| 3–4 | `lc-log`, `lc-again`, retire `java-python-code-reviewer` into `lc-coach` | **done** — plus the `LSP` entry dropped from `code-refactor-master`'s `allowed-tools` |
| 5–7 | `lc-zh-translate`, `lc-algo-demo`, `lc-site-data`, `lc-faq-add`; regroup the band | **done** — the band became three labelled groups at ten cards |
| — | `lc-sql`, the `system-architecture` upgrade | **deferred**, as planned |

Three things the implementation changed from what this document predicted:

- **`/lc-log`'s job is bigger than the annotations.** Measuring the log before writing the
  skill turned up two shapes that lose data with no warning — a number glued to its bucket
  label (**93 attempts**, which `suggest_review.py` recovers via `_strip_label` and
  `build-review-plan.js` does not) and a number written after its description
  (`2D LIS (354)` → LC 2, **16 entries**). So the skill writes a line that round-trips, and
  its gate is reading the numbers back rather than trusting an exit code. Fixing the 109
  historical entries is still open, and moves `build-review-plan.js` and its tests first.
- **Phase 7 came forward.** Ten flat cards is a list, so the band was regrouped in the same
  commit as the last four skills rather than left for later.
- **Cross-skill links are absolute.** `check_skills.py --install` proved its worth
  immediately: a sibling referenced as `../lc-java/SKILL.md` resolves in this repo and
  resolves to nothing once the skill is installed on its own. Every cross-skill link is a
  `github.com` URL for that reason.

## Out of scope

- No change to `lc-coach`, `lc-cheatsheet` or `add-time-space` beyond the wiring above.
- No new *generic* personas — the unprefixed skills stay unprefixed and stay generic.
- No redirect mechanism for renamed pages (see the open decision in 3a).
- No change to `data/progress.txt`'s format. `lc-log` writes the format that exists; if the
  format ever changes, `site/build-review-plan.js` and its tests move first.

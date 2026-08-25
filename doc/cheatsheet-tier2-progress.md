# Tier 2 Split — Measurements and Plan

Execution log for Tier 2 of [`cheatsheet-split-plan-2026-08.md`](cheatsheet-split-plan-2026-08.md): the sixteen sheets between ~2,000 and ~4,000 lines. Tier 1 is merged — see [`cheatsheet-tier1-progress.md`](cheatsheet-tier1-progress.md) for how that went and what it left open.

**Status: batch 1 of 3 complete — six sheets split, 18 files on disk.** Batches 2 and 3 not started. `data/cheatsheet_meta.json` is deliberately unregistered for the 12 new sheets, so `site/build.sh` fails by design until the central pass runs; see [Central pass still to run](#central-pass-still-to-run).

---

## Re-measured against merged master

The plan doc's Tier 2 numbers were taken before Tier 1 ran. Re-measured on `beddbb504` (the Tier 1 merge):

| file | lines | example-tail | share | dec | ex | adv | dup LC | `V0/V1` | skeleton |
|---|---:|---:|---:|---:|---:|---:|---:|---:|:--|
| `bst` | 3,956 | 1,468 | 37% | 3 | 2 | 0 | 0 | 16 | **MIXED** |
| `python_trick` | 3,672 | 0 | 0% | 0 | 0 | 0 | 0 | 5 | *neither* |
| `string` | 3,512 | 1,310 | 37% | 2 | 2 | 2 | 0 | 17 | **MIXED** |
| `stack` | 3,483 | 2,723 | **78%** | **0** | 1 | 0 | 4 | 32 | A |
| `backtrack` | 3,423 | 1,729 | 50% | 1 | 1 | 1 | 0 | 35 | **MIXED** |
| `java_trick` | 3,419 | 0 | 0% | 1 | 0 | 3 | 0 | 6 | B |
| `sliding_window` | 3,132 | 831 | 26% | 1 | 2 | 0 | 3 | 15 | **MIXED** |
| `graph` | 3,079 | 421 | 13% | 2 | 2 | 1 | 3 | 6 | **MIXED** |
| `linked_list` | 2,868 | 1,419 | 49% | **0** | 1 | 0 | 0 | 21 | A |
| `design` | 2,647 | 1,308 | 49% | **0** | 1 | 0 | 0 | 21 | A |
| `array` | 2,474 | 689 | 27% | **0** | 1 | 0 | 1 | 28 | A |
| `topology_sorting` | 2,454 | 1,266 | 51% | 2 | 1 | 0 | 0 | 20 | **MIXED** |
| `tree2` | 2,426 | 47 | 1% | 2 | 1 | 1 | 3 | 0 | B |
| `Dijkstra` | 2,359 | 1,338 | 56% | 3 | 2 | 0 | **8** | 4 | B |
| `prefix_sum` | 2,317 | 812 | 35% | 2 | 2 | 0 | **14** | 9 | **MIXED** |
| `matrix` | 2,035 | 1,185 | 58% | 1 | 2 | 0 | 2 | 10 | B |

**47,256 lines.** `dec` / `ex` / `adv` count top-level decision-or-summary, example, and advanced-or-catch-all sections; more than one `dec` means competing summary sections to merge.

Two findings change the approach versus the plan doc:

**Seven of sixteen mix both skeletons** — Skeleton B's `Overview` / `Problem Categories` / `Templates & Algorithms` up top, with Skeleton A's `0) Concept` / `1) General form` / `2) LC Example` wedged in below. [`CLAUDE.md`](../CLAUDE.md#which-skeleton) forbids mixing outright. Tier 1 found this in 4 files; it is more widespread here, and un-mixing is now a per-file requirement rather than an occasional fix.

**Duplicate counts are measured at section level.** Tier 1 initially over-reported duplicates by counting a template heading *plus its own subsections* — `### Template 2 … LC 64` and its child `#### Pattern (LC 64 …)` scored as two. Counting only `###` headings, the real hot spots are `prefix_sum` (14) and `Dijkstra` (8, which solves LC 1631 **eight** times). Those two are dedup jobs more than split jobs.

Also notable: `stack`, `linked_list`, `design` and `array` have **no** summary or decision section at all. For those, that section gets written, not merged — a reader asking "when do I reach for a stack" currently has nowhere to look.

---

## Per-file plan

Same contract as Tier 1: one owner per file, satellites named up front, move-not-rewrite with byte-for-byte extraction, deletion only for verified duplicates (same problem, same language, same approach), and `data/cheatsheet_meta.json` registered centrally at the end so parallel passes cannot conflict over it.

### Batch 1 — the six largest

| file | → target | satellites | the specific problem |
|---|---:|---|---|
| `bst` | 1,200 | `bst_examples`, `bst_advanced` | un-mix skeletons; three competing decision sections; 1,810-line templates section |
| `string` | 1,200 | `string_examples`, `string_operations` | `Advanced String Algorithms` bolt-on duplicates two existing sheets; examples sit *after* the summary |
| `stack` | 1,100 | `stack_examples`, `stack_expression_parsing` | 78% example tail; no summary section; LC 227 solved three times |
| `backtrack` | 1,200 | `backtrack_examples`, `backtrack_advanced` | a `🧭 Summary Table` heading holding 780 lines of prose and code; 35 `V0/V1` markers |
| `sliding_window` | 1,100 | `sliding_window_examples`, `sliding_window_advanced` | 1,784-line templates section; two overlapping quick-reference sections |
| `graph` | 1,200 | `graph_advanced`, `graph_examples` | 71% of the file is one templates section re-deriving what dfs/bfs/toposort/union-find/Dijkstra already own |

`graph` gets treatment the plan doc did not anticipate. Its Scope claims representation and traversal, but it re-implements the algorithms five neighbouring sheets own. A prior review already replaced its shortest-path re-implementations with a chooser table; that treatment now extends to the rest, leaving representation, degree, cycle detection, connected components, and a routing table.

#### Batch 1 result

| parent | before | after | target | satellites |
|---|---:|---:|---:|---|
| `bst` | 3,956 | **1,001** | 1,200 | `bst_examples` 987 · `bst_advanced` 1,552 |
| `string` | 3,512 | **898** | 1,200 | `string_examples` 1,350 · `string_operations` 236 |
| `stack` | 3,483 | **718** | 1,100 | `stack_examples` 1,835 · `stack_expression_parsing` 628 |
| `backtrack` | 3,423 | **1,195** | 1,200 | `backtrack_examples` 1,381 · `backtrack_advanced` 652 |
| `sliding_window` | 3,132 | **776** | 1,100 | `sliding_window_examples` 471 · `sliding_window_advanced` 1,527 |
| `graph` | 3,079 | **733** | 1,200 | `graph_examples` 846 · `graph_advanced` 1,046 |

**20,585 lines in 6 files → 17,832 in 18.** Every parent is comfortably under target; `stack` came down 79%. The 2,753-line net reduction is verified duplication, and an unusual share of it was **owned by other sheets** rather than internal to the file:

- **`string.md` — 406 lines of Manacher's Algorithm** (three Python variants, a Java version, traces, a proof) that `palindrome.md` Template 7 already carries in both languages. The largest single verified duplicate found in either tier.
- **`graph.md` — 330 lines of `Template 7: Shortest Path Algorithms`**, full Bellman-Ford and Floyd-Warshall in both languages, owned by `Bellman-Ford.md` (1,118 lines) and `Floyd-Warshall.md` (863). The Aug 2026 review recorded that it had *"replaced `graph.md`'s re-implementations with a chooser table"* — the chooser table was added at the end of the file and the template it was meant to replace was never removed.
- `string.md` also shed KMP/Rabin-Karp (81 lines, owned by `string_matching_kmp_rolling_hash.md`), its sliding-window template (91, owned by `sliding_window.md`) and String DP (42, owned by `dp.md`). Of its 999 deleted lines, **734 belonged to other sheets**.

Duplication *within* a single section turned out to be worse than the section-level heading count could show, because the copies sat under one heading:

- **`sliding_window.md` — LC 992 had four copies of the same Python at-most-k loop** in one section: an abstract `at_most_k`/`exactly_k` pair, a concrete `subarraysWithKDistinct`, a "Template Code to Memorize", and an `At-Most K → Exactly K` block.
- **LC 438 had three Java copies**; the survivor is 15 lines of `int[26]` + `Arrays.equals` against 165 lines of map-of-String comparison.
- **`stack.md` — LC 227 in three places**, one of them (108 lines) marked `TODO: fix output format` and carrying a real bug: `if c == '('` nested inside the `isdigit` branch.
- **`bst.md`** deleted a 179-line *second complete treatment* of LC 669 — same Java, same Python, same core-idea/visual/mistakes structure as Template 3b.

Four files gained a `Summary & Quick Reference` that never existed (`stack`, and — pending batch 2 — `linked_list`, `design`, `array`), and five had competing summary sections merged: `bst` 3 → 1 (322 lines → 189), `string` 3 → 1 (370 → 147), `sliding_window` 2 → 1 (482 → 163), `graph` 3 → 1 (411 → 166).

#### Where a brief was wrong

Two instructions in the per-file briefs were incorrect, and both agents were right to refuse them. Recorded because the same mistakes are easy to repeat in batches 2 and 3:

- `stack`: the brief said to collapse LC 224's two treatments into the universal calculator. LC 224's Python is a *different algorithm* — a running `res`+`sign` that stacks only the suspended `(res, sign)` per paren, needs no recursion, and does not generalise to `*`/`/`. Both were kept, one relocated with the reason stated. Hard rule "different approach ⇒ keep both" correctly outranked the specific instruction.
- `graph`: the brief listed MST (Kruskal/Prim) as material to move into `graph_advanced.md`. There was no MST implementation in the file — only table rows mentioning it. Pointers to `union_find.md` and `heap.md` were added instead of inventing code.

#### A hazard worth a permanent check

Three of the six source files had **no trailing newline**, which hides the final line from `wc -l` and from line-range extraction. The `string` pass caught two near-misses this way and restored both: the file's last table row, and LC 890, which existed *only* as a row inside a table being deleted. The central pass therefore re-runs Tier 1's LC-coverage diff over every family — it is exactly the failure mode that catches.

### Batch 2

| file | → target | satellites |
|---|---:|---|
| `linked_list` | 1,000 | `linked_list_examples` — plus convert A→B and write a summary section |
| `design` | 1,000 | `design_examples`, `design_patterns` (the 1,128-line `System Design Coding Patterns` block) |
| `array` | 1,000 | `array_examples` — the 1,737-line `1) General form` is the real target |
| `topology_sorting` | 1,000 | `topology_sorting_examples` — merge three tail sections |
| `Dijkstra` | 1,000 | `Dijkstra_examples` — collapse LC 1631 ×8 and LC 64 ×4; its `Algorithm Comparison` section duplicates `shortest_path_comparison.md` |
| `prefix_sum` | 900 | `prefix_sum_examples` — 14 section-level duplicates, the worst in the corpus; its `Range Addition` section duplicates `difference_array.md` |

Batch 1 settled one ownership question that batch 2 would otherwise get wrong. `prefix_sum.md` already owns **LC 1423** as a starred template with both Java and Python, and **LC 1658** as its variation — and `sliding_window_advanced.md` has just created near-duplicates of both, plus overlap on LC 1031 and LC 1248. Rather than let batch 2 produce a third copy and defer the whole thing, `prefix_sum.md` keeps LC 1423 / 1658 / 1031 / 1248 outright and the sliding-window side is reduced to pointers. That instruction goes into `prefix_sum`'s brief.

### Batch 3

| file | → target | satellites |
|---|---:|---|
| `matrix` | 900 | `matrix_examples` — two separate example sections to merge |
| `tree2` | 1,200 | reconcile against Tier 1, see below |
| `python_trick` | 1,200 | categorise the flat dump — 3,672 lines under a *single* `## 1) Examples` heading with 68 unsorted `###`s |
| `java_trick` | 1,200 | delete the `6) Other tricks` and `9) Others` catch-alls and refile |

`tree2` is the delicate one. Tier 1 created `tree_lca_distance.md` and `tree_construction.md`, which now own exactly what `tree2`'s `§4) Distance and LCA Templates` and `§6) Tree Construction Templates` teach. It already shared 21 worked problems with `tree.md` before Tier 1. So `tree2` is a reconciliation, not a split, and it should be sequenced last.

---

## Four contract changes carried over from Tier 1

Each of these is a defect Tier 1 actually hit:

1. **Anchors use GitHub's slug rule**, not the site's. The repo has two competing conventions (93 anchors vs 74); GitHub's is dominant and is what untouched sheets use. Tier 1 came one command away from "fixing" 14 working links into broken ones.
2. **Fence-tagging checks must match indented fences** (`^\s*```\S*$`). Both the Aug 2026 audit and Tier 1's own checks matched only column-0 fences, which is how an untagged block inside a list item survived into `bfs_advanced.md` and had to be caught by review.
3. **Incomplete code must be captioned as an outline.** Tier 1 shipped a Median-of-Medians block with `pass` bodies and an undefined helper, advertised by a new Scope line as if delivered.
4. **Per-agent scratchpad filename prefixes.** Parallel Tier 1 agents wrote scripts to the same paths and clobbered each other mid-run.

## Central pass still to run

Deliberately held until all three batches land, so it happens once:

1. **Register the new sheets in [`data/cheatsheet_meta.json`](../data/cheatsheet_meta.json)** — 12 from batch 1, more to come. The build fails without them, by design. Each pass reported a recommended `category` / `tier`; two need a decision rather than a copy: `bst_examples` is borderline 3/4 (tier 4 only because it carries the path-sum family, which the cross-file phase may move away), and `backtrack_advanced` was argued up to 4 against `dfs_advanced`'s 3 because LC 212/282/301 are recurring hards.
2. **Content-preservation diff** — Tier 1's LC-coverage check, per family, plus a code-body provenance check with comments stripped. Non-optional given the trailing-newline hazard above.
3. **Repair what the deletions dangled.** One is already known: `stack.md`'s surviving `Common Mistakes #5` recommends the LC 394 typed-stack form whose code block was deleted as a duplicate. That inconsistency was *introduced* by this work, unlike the pre-existing warts below, so it gets fixed here.
4. **Anchor sweep** repo-wide, GitHub slug rule, intra- and cross-file.
5. **`SKIP_FONTS=1 bash site/build.sh`** — cannot pass until step 1 is done.

### Carried-over warts, reported not fixed

Pre-existing defects the passes moved verbatim rather than silently repairing, since this is a move:

- `stack.md`'s monotonic Java template is commented "traverse the array from right to left" above a left-to-right loop, and is headed `// LC 239` — a monotonic-*queue* problem.
- `backtrack.md`'s deleted `0-2)` LC 77 Python was not only a duplicate but wrong (`help(idx+1)` where it needs `help(i+1)`); the correct copy survived.
- Four `c++` blocks survive in `backtrack_examples.md` — the only C++ in the sheet, duplicating the Python/Java logic. Not deletable under the same-language rule, so kept and flagged.

## Sequencing

Three batches rather than one wave of sixteen. Running eight at once in Tier 1 cost two agents to an API session limit mid-pass; their output survived and was verified centrally, but sequencing is cheaper than recovering.

## Still open from Tier 1

Unchanged and not part of this tier:

- **Cross-file consolidation** — the phase Tier 2 will feed rather than finish. `stack` × `monotonic_stack` (12 shared problems), `graph` × the dfs/bfs satellites, `prefix_sum` × `hash_map_examples`, `tree2` × the tree family.
- **`2_pointers_quickselect.md` vs `advanced_divide_and_conquer.md`** — the latter already owns QuickSelect in more depth; the satellite is registered but should probably be folded in.
- **~25 pre-existing correctness bugs** surfaced by the PR #114 review, verified as untouched-by-Tier-1 code: un-seeded BFS queues, undefined identifiers, missing null guards, Hoare's return value used as a pivot index. Worth its own PR.
- **The site slugify fix** — one line in `site/build-lib.js`, repairs ~93 anchors on the published site.

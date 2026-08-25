# Tier 2 Split — Measurements and Plan

Execution log for Tier 2 of [`cheatsheet-split-plan-2026-08.md`](cheatsheet-split-plan-2026-08.md): the sixteen sheets between ~2,000 and ~4,000 lines. Tier 1 is merged — see [`cheatsheet-tier1-progress.md`](cheatsheet-tier1-progress.md) for how that went and what it left open.

**Status: measured and planned; the per-file passes are in flight.** No `doc/cheatsheet/*.md` has changed yet. This document records the measurement pass and the split decisions so the plan is on record before any file moves.

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

### Batch 2

| file | → target | satellites |
|---|---:|---|
| `linked_list` | 1,000 | `linked_list_examples` — plus convert A→B and write a summary section |
| `design` | 1,000 | `design_examples`, `design_patterns` (the 1,128-line `System Design Coding Patterns` block) |
| `array` | 1,000 | `array_examples` — the 1,737-line `1) General form` is the real target |
| `topology_sorting` | 1,000 | `topology_sorting_examples` — merge three tail sections |
| `Dijkstra` | 1,000 | `Dijkstra_examples` — collapse LC 1631 ×8 and LC 64 ×4; its `Algorithm Comparison` section duplicates `shortest_path_comparison.md` |
| `prefix_sum` | 900 | `prefix_sum_examples` — 14 section-level duplicates, the worst in the corpus; its `Range Addition` section duplicates `difference_array.md` |

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

## Sequencing

Three batches rather than one wave of sixteen. Running eight at once in Tier 1 cost two agents to an API session limit mid-pass; their output survived and was verified centrally, but sequencing is cheaper than recovering.

## Still open from Tier 1

Unchanged and not part of this tier:

- **Cross-file consolidation** — the phase Tier 2 will feed rather than finish. `stack` × `monotonic_stack` (12 shared problems), `graph` × the dfs/bfs satellites, `prefix_sum` × `hash_map_examples`, `tree2` × the tree family.
- **`2_pointers_quickselect.md` vs `advanced_divide_and_conquer.md`** — the latter already owns QuickSelect in more depth; the satellite is registered but should probably be folded in.
- **~25 pre-existing correctness bugs** surfaced by the PR #114 review, verified as untouched-by-Tier-1 code: un-seeded BFS queues, undefined identifiers, missing null guards, Hoare's return value used as a pivot index. Worth its own PR.
- **The site slugify fix** — one line in `site/build-lib.js`, repairs ~93 anchors on the published site.

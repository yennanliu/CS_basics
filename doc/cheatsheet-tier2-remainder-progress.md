# Tier 2 Remainder — Measurements and Plan

Working log for the ten Tier 2 sheets that [`cheatsheet-split-plan-2026-08.md`](cheatsheet-split-plan-2026-08.md) lists but the first Tier 2 pass did not reach. **Delete this file before the PR merges** — same as the Tier 1 and Tier 2 logs; its findings belong in commit messages, not the repo.

**Status: measured and planned; batch A of 2 is in flight.** No `doc/cheatsheet/*.md` has changed yet.

---

## Why this branch exists

Tier 2 was scoped at sixteen sheets. [PR #116](https://github.com/yennanliu/CS_basics/pull/116) delivered **six** — `bst`, `string`, `stack`, `backtrack`, `sliding_window`, `graph` — and batches 2 and 3 were never launched. Ten sheets remain, and they are not the leftovers: **every one of them is larger than every sheet in Tier 3** — the smallest, `matrix` at 2,034, clears Tier 3's largest, `union_find` at 1,882 — and two hold the worst duplication measured anywhere in the corpus.

Measured on `c46a1dece` (the PR #116 merge):

| file | lines | example-tail | share | dec | dup LC | `V0/V1` | skeleton |
|---|---:|---:|---:|---:|---:|---:|:--|
| `python_trick` | 3,672 | 0 | 0% | 0 | 0 | 5 | *neither* |
| `java_trick` | 3,418 | 0 | 0% | 1 | 0 | 6 | B |
| `linked_list` | 2,867 | 1,419 | 49% | **0** | 0 | 21 | A |
| `design` | 2,647 | 1,308 | 49% | **0** | 0 | 21 | A |
| `array` | 2,474 | 689 | 27% | **0** | 1 | 28 | A |
| `topology_sorting` | 2,454 | 1,266 | 51% | 2 | 0 | 20 | **MIXED** |
| `tree2` | 2,425 | 47 | 1% | 2 | 3 | 0 | B |
| `Dijkstra` | 2,359 | 1,338 | 56% | 3 | **8** | 4 | B |
| `prefix_sum` | 2,316 | 812 | 35% | 2 | **14** | 9 | **MIXED** |
| `matrix` | 2,034 | 1,185 | 58% | 1 | 2 | 10 | B |

**26,666 lines.** `dec` counts top-level decision-or-summary sections: **0 means the sheet cannot answer "which technique does this problem want"** — true of `linked_list`, `design` and `array`, so those sections get *written*, not merged. `dup LC` counts distinct LC numbers appearing in more than one `###` heading: `prefix_sum` at 14 is the worst in the corpus and `Dijkstra` solves LC 1631 **eight times**.

For comparison, Tier 3 — deliberately deferred until this is done — is twelve sheets totalling 18,012 lines, none over 1,900 (`union_find` 1,882 is the largest), so per the plan it is a **dedup-only** pass with no satellites. Its hot spots are `binary_indexed_tree` (87% example tail), `bit_manipulation` (74%), `greedy` (71%) and `monotonic_stack` (9 duplicates).

---

## Batch A — in flight

| file | → target | satellites |
|---|---:|---|
| `python_trick` | 1,200 | `python_trick_stdlib`, `python_trick_indexing` |
| `java_trick` | 1,200 | `java_trick_collections`, `java_trick_strings_sorting` |
| `linked_list` | 1,000 | `linked_list_examples` |
| `design` | 1,000 | `design_examples`, `design_patterns` |
| `array` | 1,000 | `array_examples` |

Three of these get treatment the plan doc did not specify, because the measurement changed the diagnosis:

**`array.md` is a hub, not a technique sheet.** Its whole family — `2_pointers`, `sliding_window`, `prefix_sum`, `difference_array`, `matrix`, `sort`, `n_sum`, `binary_search` — has already been split or reviewed in an earlier tier, so re-deriving those techniques inside `array.md` is pure duplication. It keeps genuine array *mechanics* (in-place compaction, rotation, index-as-hash, cyclic sort, marking by negation, 2D building) plus a chooser table routing everything else to its owner. This is the treatment that took `graph.md` from 3,079 to 733.

**`python_trick.md` needs a structure, not a split.** 3,672 lines under a *single* `## 1) Examples` heading, 68 `###` entries numbered `0-1)`, `1-11''')`, `1-27-3)` in no thematic order. It is the only sheet in the corpus matching neither skeleton. The job is categorisation into themed headings plus a lookup table, with the library-by-library reference (`heapq` 365 lines, `SortedDict` 269, `itertools` 105, `bisect` 98) and the index-arithmetic deep dives (`Insert into array` 251, `Index distance vs element count` 186) moved out.

**`java_trick.md`'s two catch-alls get dissolved, not renamed.** `6) Other tricks` (527 lines) and `9) Others` (286) are exactly what `CLAUDE.md` forbids. The Aug 2026 review's first attempt at the eleven `Missing Google Patterns` sections renamed them to one generic heading and was sent back in code review for merely relocating the catch-all — so every item here is filed under the topic it belongs to, and no replacement generic heading is allowed.

## Batch B — queued

| file | → target | satellites |
|---|---:|---|
| `topology_sorting` | 1,000 | `topology_sorting_examples` |
| `tree2` | 1,200 | reconcile against Tier 1 — see below |
| `Dijkstra` | 1,000 | `Dijkstra_examples` |
| `prefix_sum` | 900 | `prefix_sum_examples` |
| `matrix` | 900 | `matrix_examples` |

Two ownership questions are already settled so batch B does not get them wrong:

- **`prefix_sum.md` keeps LC 1423 / 1658 / 1031 / 1248.** It already owns LC 1423 as a starred template in both languages and LC 1658 as its variation — and Tier 2 batch 1's `sliding_window_advanced.md` created near-duplicates of both. The sliding-window side reduces to pointers.
- **`tree2.md` is a reconciliation, not a split.** Tier 1 created `tree_lca_distance.md` and `tree_construction.md`, which now own precisely what `tree2`'s `§4) Distance and LCA Templates` and `§6) Tree Construction Templates` teach. `tree2` already shared 21 worked problems with `tree.md` before Tier 1 touched it. Sequenced last.

---

## Contract carried forward

Each item is a defect an earlier tier actually hit:

1. **GitHub's anchor slug rule**, not the site's — lowercase, strip punctuation, *each* space → `-`, trailing `-` after a star run, ` — ` → `--`. The repo has both conventions (93 anchors vs 74); GitHub's is dominant. Tier 1 came one command from "fixing" 14 working links into broken ones.
2. **Fence checks must match indented fences** (`^\s*```\S*$`). Both the Aug 2026 audit and Tier 1's own checks matched only column-0 fences, which is how an untagged block inside a list item reached review in `bfs_advanced.md`.
3. **Incomplete code must be captioned as an outline.** Tier 1 shipped a Median-of-Medians block with `pass` bodies and an undefined helper, advertised by a new Scope line as if delivered.
4. **Per-agent scratchpad filename prefixes.** Parallel agents clobbered each other's scripts in Tier 1.
5. **Register each batch in `data/cheatsheet_meta.json` as it lands.** `site/build-site.js` throws when any sheet is unregistered — deliberately. Deferring registration to one central pass left CI red on every intermediate push during Tier 2 and had to be fixed reactively.
6. **Check for a missing trailing newline before slicing.** Three of Tier 2 batch 1's six source files lacked one, which hides the final line from `wc -l` and from range extraction. The `string` pass caught two near-misses that way, including an LC number that existed only inside a table being deleted.

## Central pass per batch

1. Register the new sheets in [`data/cheatsheet_meta.json`](../data/cheatsheet_meta.json).
2. **Content-preservation diff — three inventories, not one.** LC coverage alone passes a batch that
   silently drops prose, and these sheets are exactly the ones where the non-LC material *is* the
   content: `array.md`'s chooser table, `python_trick.md`'s library reference, `java_trick.md`'s two
   dissolved catch-alls. So compare before/after on all three:
   - **LC numbers** per family — every number in the source appears in the parent or a named satellite.
   - **Code bodies**, comments stripped and whitespace normalised — provenance for each block.
   - **Headings and non-LC blocks** — every `###`+ heading and every non-code block of the source is
     accounted for as *kept*, *moved to `<file>`*, or *deleted as a duplicate of `<heading>`*. A
     heading that appears in no column is the bug this catches; "deleted" needs the duplicate named.
3. Repair inbound anchors the batch broke, and check them against **both** slug rules. Already known:
   `linked_list.md` has at least four (`#1-1-1-reverse-linked-list-iteration--lc-206`,
   `#1-1-4-reverse-nodes-in-k-group--linked-list-iteration--lc-25`, `#2-4-reverse-linked-list-ii--lc-92`,
   `#2-9-reorder-list--lc-143`) and `python_trick.md` two
   (`#1-32-collectionsdeque-double-ended-queue`, `#1-11-multi-key-tuple-sort-keylambda-x-x0-x1`),
   all from files the owning agents may not edit.

   The two rules disagree, so a link can only be verified against the one its reader uses:
   `site/build-lib.js:19` collapses *every* run of non-alphanumerics to a single `-`, so ` — ` → `-`,
   where GitHub gives `--`. Contract item 1 is the GitHub rule; the site is the divergent one, and
   fixing it is the one-line change already listed under *Outstanding*. Until that lands, a link like
   `#2-4-reverse-linked-list-ii--lc-92` resolves on GitHub and 404s on the published page — so the
   batch's link check must resolve each anchor under GitHub's rule **and** report which anchors the two
   rules disagree on, rather than rewriting anchors to match whichever renderer was tested last.
4. **Moved headings keep their inbound links.** When a section leaves the parent for a satellite, the
   parent keeps a one-line pointer under a heading with the *same text*, so the old anchor still lands
   somewhere that names the new home. This is what Tier 2's `graph.md` did; it costs a line per moved
   section and is the only redirect mechanism the site has — there is no alias table, and
   `build.sh` starts with `rm -rf _site`, so a moved page leaves no orphan behind to catch the link.
5. `SKIP_FONTS=1 bash site/build.sh`.

## Outstanding from earlier tiers

- **Cross-file consolidation.** The phase every tier feeds and none finishes. Hard numbers so far: LC 449 byte-identical between `bst_examples` and `dfs_examples`; LC 701 duplicated in both languages; LC 98 in five files; LC 49 in four; `stack_examples` holds 10 of the 12 problems `monotonic_stack.md` owns; ~350–400 lines recoverable in the graph family alone.
- **`2_pointers_quickselect.md` vs `advanced_divide_and_conquer.md`** — the latter owns QuickSelect in more depth; the satellite is registered but should probably be folded in.
- **~25 pre-existing correctness bugs** from the PR #114 review, verified as untouched-by-Tier-1 code: un-seeded BFS queues, undefined identifiers, missing null guards, Hoare's return value used as a pivot index.
- **One inconsistency Tier 2 introduced**: `stack.md`'s surviving `Common Mistakes #5` recommends the LC 394 typed-stack form whose code block was deleted as a duplicate.
- **The site slugify fix** — one line in `site/build-lib.js`, repairs ~93 anchors on the published site.

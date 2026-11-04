# Cheatsheet Review — November 2026: Are They Interview Material?

A review of all 135 markdown files in [`doc/cheatsheet/`](cheatsheet/) (150,285 lines), asking a question neither earlier pass asked: **does a sheet make you faster in a FAANG coding interview** — Google L3, Python first, Java second?

> 繁體中文版： [`cheatsheet-review-2026-11.zh-tw.md`](cheatsheet-review-2026-11.zh-tw.md)

| Review | Asked | Status |
|---|---|---|
| [Aug 2026](cheatsheet-review-2026-08.md) | Is the **format** consistent? | Held: 0 untagged fences, 0 heading jumps, Scope lines on 135/135 |
| [Split plan](cheatsheet-split-plan-2026-08.md) | Are the files **too big**? | Tiers 1–3 done. Ten sheets became twenty-four; nothing over 3,200 lines |
| **This one** | Can a reader **recognise the pattern, recall the template, and avoid the trap** under time pressure? | The rules land in [`00_template.md`](cheatsheet/00_template.md) and [`CLAUDE.md`](../CLAUDE.md#cheatsheet-style-guide) in this PR. Sheet edits are proposed below, not made |

The review was done in six parallel reads, one per category, against one rubric. A metrics script ran over every file. Every bug in [§ Correctness](#9-correctness--bugs-in-the-templates-people-memorise) marked **verified** was re-read by hand.

The figures were re-measured on 2026-11-05 against `master` at `6ad1564dd`, after 84 commits landed on top of the first draft. Those commits added about 500 lines to six sheets. They moved each finding's count by at most one, and none of the nine verified bugs was fixed. The new sections (`binary_tree.md` §0-5, `time_space_complexity.md`'s V + E vs V × E, `prefix_sum.md` Template 5) are Python first, each block carrying `IDEA` and then `time`. Two of them are listed under [What good already looks like](#what-good-already-looks-like).

---

## Verdict

The best sections are better than most published interview material. The worst are someone's solution folder pasted under a heading. **The difference between them is shape, not knowledge.** The good sections all do the same five things in the same order, and the weak ones skip them:

1. say **how you would spot it** from the problem statement;
2. state the **key point** in one line, before the code: why it works, as a fact the code keeps true (an invariant);
3. give **one** canonical template, Python then Java, with `time = O(...)`;
4. **dry-run** the non-obvious step on a tiny input;
5. name the **trap** with the input that springs it.

The corpus has no single front door. A reader holding a problem statement has no page that answers "which sheet do I open?", and [`lc_pattern.md`](cheatsheet/lc_pattern.md), which claims that job, links to **zero** sheets.

Nine template bugs were confirmed. They sit in exactly the code a reader memorises: `dp.md`'s first template, a "✅ correct" snippet in `binary_tree.md`, the DFS topological sort, the Bellman-Ford K-edge template.

---

## What was measured

| Check | Now | Note |
|---|---:|---|
| Files / lines | 135 / 150,285 | 3 redirect stubs |
| Skeleton B / A / mixed / neither | 92 / 20 / 8 / 15 | "mixed" is what the template forbids |
| Files with a **recognition** heading (signal / when to use / how to identify) | **37 / 135** | 55 tier ≥ 4 sheets have none |
| Files with a **pitfalls / traps** heading | 70 / 135 | |
| Files with a **decision / selection** section | 61 / 135 | |
| Java/Python blocks ≥ 4 lines carrying `time = O(...)` | 1,621 / 3,184 = **51%** | Up from 35% (Aug) and 47% (split plan) |
| Python vs Java fences | 1,824 vs 1,425 | Python leads overall, but *examples* sheets often lead Java (§5) |
| `### … LC n` worked-example headings | 1,330 | |
| …followed **directly by code**, no prose | **401 (30%)** | Worst: `dfs_examples` 25, `heap_examples` 22, `design_examples` 14 |
| …with no insight / idea / trick line in the next 14 lines | **570 (43%)** | |
| `NOTE !!!` comment walls | **583 lines in 69 files** | |
| `V0'`-style unexplained variants | 68 in 23 files | |
| IPython `In [n]:` transcripts | 224 lines in 10 files | `python_trick` 119, `array` 13 sections |
| Emoji in headings | 195 headings in 24 files | 1,272 emoji overall |
| Cryptic `1-1)` / `1-1-3)` numbering | 665 headings | |
| `gpt` mentions ("fixed by gpt", "comments created by gpt") | 25 lines in 17 files | |
| Blog links (csdn / cnblogs / bilibili / itdaan) | 30 lines in 19 files | some inside code blocks |
| Java subtraction comparators `(a, b) -> a - b` | 26 in 13 files | `java_trick_strings_sorting` warns against them |
| Code that runs to a bare `pass` | 25 lines in 10 files | proof sketches and "universal templates" |

Reader scores for interview usefulness, 1–5 (scored from reading, not computed):

| Score | Sheets |
|---|---|
| **5 — copy this** | `sliding_window`, `prefix_sum_advanced`, `minimum_spanning_tree`, `dp_loop_order`, `stack_expression_parsing`, `patience_sorting`, `bit_manipulation`, `python_complexity` |
| **≤ 2 — needs the most work** | `dfs_examples` (1), `2_pointers_examples`, `2_pointers_linkedlist`, `n_sum`, `add_x_sum`, `array_examples`, `string_examples`, `dp_examples`, `graph_examples`, `Bellman-Ford`, `Floyd-Warshall`, `topology_sorting`, `topology_sorting_examples`, `hashing`, `queue`, `set`, `stack_examples`, `advanced_divide_and_conquer`, `binary_search_examples`, `sort`, `math`, `lc_pattern`, `python_trick`, `design_examples`, `streaming_algorithms`, `advanced_simulation`, `array_overlap_explaination` |

The low scorers are mostly `_examples` satellites. The split plan moved the long tail into them and nobody curated it afterwards.

---

## Findings

### 1. There is no front door

A candidate's first question is "*what kind of problem is this?*". Nothing in the corpus answers it end to end:

- [`lc_pattern.md`](cheatsheet/lc_pattern.md) (tier 5) runs pattern → problems, never signal → pattern. Its body links **zero** sheets. Union-Find, Topological Sort and Greedy each appear twice (`:225`/`:571`, `:234`/`:594`, `:85`/`:549`). It also mis-files problems: LC 1 under "Two Sum (sorted)", LC 424 under "Fixed Size Window", LC 279 under the sieve.
- [`lc_category.md`](cheatsheet/lc_category.md) maps an external taxonomy, not problem signals.
- The **constraint ruler** (n ≤ 20 → 2ⁿ, n ≤ 10⁵ → n log n) exists twice, at `complexity_cheatsheet.md:668` and `time_space_complexity.md:30`, and nothing routes to either.
- The tables that *do* go signal → pattern are good but local and scattered: `dfs.md:31`, `graph.md:617`, `Collection.md:288`, `shortest_path_comparison.md:149`, `sliding_window.md:767`, `greedy.md:457`.

### 2. Recognition is the missing layer

Only 37 files have any recognition heading. Among the 24 Arrays & Strings / Linked List sheets, **none** uses a constraint cue. In the whole corpus only `dp_bitmask` (n ≤ 20) and `Floyd-Warshall` (n ≤ 400) do.

Where recognition exists, it often repeats the title. `tree2.md`'s *Use Case* column says "Maximum path sum problems" under the Maximum Path Sum template.

The highest-value variant is **anti-recognition**, "looks like X, but is not". It appears exactly once (`difference_array.md:972`), and it is one of the best tables in the set.

### 3. Worked examples are archives, not lessons

43% of LC example headings have no key-insight line, and 30% go straight into code. The `_examples` files switch style halfway through. Their newer halves follow *Core idea → Trace → Py/Java → Pitfalls → Similar*. Their older halves are pasted solution files:

- `NOTE !!!` walls inside the code: `stack_examples` 44, `2_pointers_examples` 26, `backtrack_examples` 26, `string` 17, `tree_examples` 15, `tree_lca_distance` 15, `heap_examples` 14.
- **V0 is sometimes the worst solution**, kept for history. Examples:
  - LC 39 in `backtrack_examples:183` is brute force plus dedup.
  - LC 62 in `dp_examples:154` is solved with BFS.
  - LC 146 in `design_examples:31` is an O(n) list LRU, and the hash map + doubly linked list answer is missing.
  - LC 15 in `n_sum:514` scans `tmp not in res`.
- Leftovers from how solutions were written: "fixed by gpt", blog URLs, commented-out `print`s, `### NOTE` lines whose comment contradicts the next line of code (`dfs_examples:761`).

### 4. One problem, many homes

The split plan fixed size *within* a sheet. Ownership *across* sheets is still open:

| Problem / topic | Worked out in | Files |
|---|---|---:|
| LC 200 Number of Islands | bfs, dfs, graph, union_find families | 6 |
| 0/1 knapsack | `dp` T4, `dp_pattern` §6, `dp_advanced`, `knapsack`, `knapsack_01_zh`, `dp_loop_order` | 6 |
| LC 309 stock cooldown | `dp`, `dp_pattern`, `dp_advanced`, `dp_examples`, `stock_trading` | 5 |
| Shortest-path comparison table | `Dijkstra`, `Bellman-Ford`, `Floyd-Warshall`, `shortest_path_comparison`, and the copies already disagree | 4 |
| LC 300 LIS | `binary_search`, `binary_search_examples`, `patience_sorting`, `sort` | 4 |
| LC 113 / 437 tree paths | `tree2`, `tree_backtrack`, `binary_tree`, `bst_examples` (≈600 lines of non-BST paths) | 4 |
| LC 743 | 3× in `Dijkstra.md`, 1× in `Dijkstra_examples` | 4 |
| LC 215 quickselect | `2_pointers_quickselect`, `advanced_divide_and_conquer`, `sort`, `heap` | 4 |
| `monotonic_stack` ↔ `stack_examples` | 10 shared LC headings (32, 84, 402, 496, 503, 735, 739, 901, 907, 2104) | 2 |
| KMP / rolling hash | `string_matching_kmp_rolling_hash` ↔ `advanced_string_algorithms` (LC 28, 459, 686, 1044, 796) | 2 |
| Tree templates | `tree2` says it is "the single home"; `binary_tree` still carries Templates 1–7 | 2 |

The Scope lines say who owns which *topic*. Nothing says who owns which *problem*, so a reader looking for LC 113 lands in one of four places. Those four can differ, and two of them can be wrong.

### 5. Python-first is not followed where it matters

Python leads overall (1,824 vs 1,425 fences). The sheets that teach a template or an advanced topic often lead with Java:

- `dp_monotonic_stack` 0 Python vs 7 Java; `dp_string` 1 vs 12; `knapsack` 3 vs 13; `dp_bitmask` 3 vs 10.
- `binary_search_on_answer`: 11 Python vs 17 Java, and every example leads with Java.
- `greedy_examples` leads with Java in 13 of 15 sections; `combinatorics_math_patterns` in 17 of 21.
- `sliding_window_advanced` has six "Template (Java)" headings.
- `Bellman-Ford` and `Floyd-Warshall` have no Java at all, and `n_sum`'s general form is **C++**.

### 6. Noise reduces how much a reader can take in per page

- **Emoji** in 195 headings (`dp_advanced` 32, `knapsack` 21, `Dijkstra` 52 overall, `java_trick` 61). `lc_pattern` puts 🔹 before every H2. The site already turns `⭐` runs into badges, so other emoji only compete with them.
- **Numbering** like `1-1-2-1)` (665 headings). It cannot be referred to in speech and breaks whenever a section moves. Examples: `bst_advanced` runs its templates 5b, 8, 9, 6b, 3c, 4b; `bfs.md` has to explain its own gaps (`:138`).
- **REPL transcripts as templates**: `array.md:53-112` opens a tier-5 sheet with `In [28]: p.insert(1, [6,1])`.
- **Generic filler**: "Common Mistakes: forgetting to handle window state correctly", proofs whose code is `pass` (`greedy:493-554`), "Universal Template" pseudocode with undefined names (`binary_tree:453`, `tree:417`, `heap:192`), invented statistics ("Template Usage Frequency: 15 problems", a ms benchmark in `Dijkstra.md:775`), and a 45-line flowchart ending in "Design custom solution" (`palindrome:508`).
- **Pasted chat**: `tree_backtrack.md`'s H1 is "Template (gpt)", and its line 6 opens "Great — many…".
- **Inline 中文 in English sheets.** `knapsack_01_zh.md` is a Chinese-only parallel sheet outside the i18n overlay. `dp_advanced:882-940` nearly copies it, and `dp_loop_order` repeats its English content in Chinese. The 100% zh gate cannot see any of this.

### 7. Teaching order is upside down in places

- `binary_search.md`'s canonical template is at line **1,194 of 1,604**.
- `stock_trading.md`'s best section (§7, the one recurrence every stock problem plugs into) comes **last**, after per-problem dumps it makes unnecessary.
- `linked_list.md` teaches LRU/LFU (`:597`) before list reversal (`:1362`).
- `monotonic_stack` (`:686`), `queue` (`:1197`), `sort` (`:1411`) and `intervals` (`:660`) still put `LC Examples` **after** `Summary`, the August anti-pattern. So do `dp_pattern:1941`, `kadane:937`, `recursion_to_dp:636` and `dp_advanced:2906`.
- `monotonic_stack`'s first ~680 lines are generic. Its real content starts at §2-2.

### 8. No drill path

No sheet orders its problems easy → hard **and** marks which ones are must-know. "Problems by Pattern" tables list difficulty in no particular order. Stars mark sections, not problems.

The repo already knows the answer. [`data/l3_core.json`](../data/l3_core.json) holds the L3 core set and [`data/progress.txt`](../data/progress.txt) the verdicts. The sheets just never show either.

### 9. Correctness — bugs in the templates people memorise

**Verified** means the code was re-read for this review and the defect is unambiguous. The rest were reported by a reader with file:line and must be re-checked when fixed.

| File:line | Defect | |
|---|---|---|
| `dp.md:151-170` | Template 1 "1-D Linear DP — LC 53" uses `dp[i] = max(dp[i-1], nums[i])`, which returns the **maximum element**, not the max subarray. It is the first template in the tier-5 DP sheet. | **verified** |
| `binary_tree.md:194-203` | The "✅ correct" reset-don't-stop snippet only recurses inside `else`. A continuing streak never recurses, the right child is never visited, and the max is never updated. | **verified** |
| `topology_sorting.md:263-275` | Inserts with `stack.insert(0, v)` and then returns `stack[::-1]`, a double reversal, so it returns a **reverse** topological order. The comment "append is wrong" has it backwards. | **verified** |
| `Bellman-Ford.md:176-180` | The K-edge template stops early when `dist[dst]` is unchanged for a round. `dst` can improve on a later round, so the early stop can return a too-high cost. | **verified** |
| `2_pointers_linkedlist.md:53-79` | `detectCycle` is labelled LC 141 (it is 142). The no-cycle guard is commented out, so an acyclic list throws an NPE in the second loop. | **verified** |
| `greedy.md:80` | "Template 2 — LC 621" pops tasks with no cooldown `n` and returns a list. It does not solve LC 621. | **verified** |
| `binary_search_examples.md:223, 299` | `mid = (left + right) / 2` is float division in Python 3, so LC 209 raises `TypeError` and LC 367 gives wrong answers. | **verified** |
| `stack.md:336-354` | A next-greater template fragment with an undefined `tmp`. `res[tmp[_idx]]` should be `res[_idx]`, and the Java's comment says right-to-left over a left-to-right loop. | **verified** |
| `Collection.md:327` | "`Counter` … create keys on read" is false. `Counter.__missing__` returns 0 without inserting; only `defaultdict` inserts. | **verified** |
| `heap.md:575` | "Template 1 — LC 215" returns the heap, not the kth element; its Java solves LC 347. | reported |
| `add_x_sum.md:203` | LC 371 "V0" is `log(exp(a) * exp(b))`, which overflows past ~709 and has float error. | reported |
| `design_examples.md:31-77` | The canonical LRU is O(n) per op; LC 146 requires O(1). | reported |
| `binary_search_on_answer.md:52, 164, 1428` | The maximize rule `left = mid + 1` contradicts the sheet's own `left = mid` / upper-mid code. | reported |
| `advanced_divide_and_conquer.md:56` | "LC 315" returns a total inversion count; LC 315 needs a count per index. | reported |
| `Floyd-Warshall.md:478` | Applies O(n³) to LC 1627 (n ≤ 10⁴), against its own sizing table. | reported |
| `backtrack.md:90, 247` | Subsets tip uses the permutations idiom; LC 377 is filed as pass-`i`. | reported |
| `tree.md:958` | "Re-rooting DP" sits over LC 310 leaf-trimming. | reported |
| `2_pointers_quickselect.md:46` | The ⭐5 template uses a fixed Lomuto pivot, so it is O(n²) on LC 215's all-equal tests. | reported |
| `lc_pattern.md:49, 55, 60, 279, 552, 554, 621` | Problems filed under the wrong pattern. | reported |
| 26 Java comparators | `a - b` subtraction in `intervals`, `scanning_line`, `scanning_line_examples`, `greedy_examples`, … | reported |

---

## What good already looks like

These sections are the model. The new template is derived from them rather than invented.

| Section | Why it works |
|---|---|
| [`minimum_spanning_tree.md`](cheatsheet/minimum_spanning_tree.md) `:57-70`, `:166-194`, `:307-327` | Why greedy is safe *before* the code. One graph is traced through both Kruskal and Prim as state tables ("same tree, different order"). A "Kruskal or Prim?" table keyed on **what you are handed**. A second snippet comes with its reason written down. |
| [`dp.md`](cheatsheet/dp.md) `:383-540` (Template 1b, LC 139) | A pattern / state / base / transition / answer / complexity table before code. A one-line insight, a dp trace, Python + Java with complexity, "Three traps", and an honest-complexity table (n² vs n³ vs n·L²). |
| [`dp_loop_order.md`](cheatsheet/dp_loop_order.md) `:16-190` | Teaches a *judgement*: one rule, a failure table (what broke → symptom → canonical case), a 3-step decision procedure, and a 10-second shuffle test. |
| [`prefix_sum_advanced.md`](cheatsheet/prefix_sum_advanced.md) `:489-690` | Maps the new pattern onto a known one, gives "when to reach for it" as three conditions, then the **smallest failing case** that shows why the undo step exists. Ends with "when *not* to use it". |
| [`tree_lca_distance.md`](cheatsheet/tree_lca_distance.md) `:95-110` | A family table: one row per variant, *what changed vs the baseline*, the template that fits, then "the one question that picks the template". |
| [`binary_tree.md`](cheatsheet/binary_tree.md) `:221-235` (§0-5, depth vs height) | The same family-table shape, one level down: depth, height and distance, each with *who knows it first* and *which way it flows*, so the argument-vs-return-value choice is made once. The units (edges vs nodes) are stated before the code. |
| [`time_space_complexity.md`](cheatsheet/time_space_complexity.md) `:423` (V + E vs V × E) | States one rule, then a *you see → time → space → example* table. The last row is "a bug, not an algorithm", which is the anti-recognition that §2 says is missing almost everywhere. |
| [`stack_expression_parsing.md`](cheatsheet/stack_expression_parsing.md) `:12-60`, `:618-628` | Breaks the family into 3 sub-problems, traces `"3+2*2"` one row per character, and ends on a Trap → Fix table. |
| [`patience_sorting.md`](cheatsheet/patience_sorting.md) `:25-100` | The model short sheet: three rules → trace → two-line proof → "when it applies" → a one-line discriminator. |
| [`difference_array.md`](cheatsheet/difference_array.md) `:972-979` | The only **anti-recognition** table: "looks like a diff array, but is not — the tell". |
| [`Collection.md`](cheatsheet/Collection.md) `:288-313` | Signal in the problem → reach for → why → LC. This is the recognition layer most sheets lack. |
| [`scanning_line.md`](cheatsheet/scanning_line.md) tie-break section | Turns the most common sweep-line bug into a decision you make once. |

---

## Ideas

Five ideas, each a change in *how a sheet is written* rather than a one-off fix.

1. **Every template is a *pattern card*.** Its heading names the **pattern** and its **anchor problem**: `### Template 2: Variable-Size Window — LC 3`. The pattern is narrower than the sheet's topic, so `hash_map.md` holds *Complement Lookup — LC 1* rather than a card called "Hash Table". Then come the same slots in a fixed order: **Recognise → Key point → Template → Complexity → *Dry run* → Traps → *Not this if* → Drill**. **Key point** is why it works, as a fact the code keeps true, written before the code. **Complexity** names the line that sets each bound, which is what you say out loud. *Dry run* is written whenever a step is non-obvious. *Not this if* is written whenever a look-alike exists, and it sits after Traps because it is for a reader who already knows the pattern. An optional slot is left out only when it would be empty, and the rest keep their order. A reader who knows the order can find "what's the trap?" in any sheet without reading it. Every exemplar above already has most of this shape.
2. **Every worked example is a *problem card*.** It has a key-insight blockquote and a "Reduces to" link to its card, then one solution with `time =`, plus a trace and traps where needed. Raw code with `NOTE !!!` is a solution file, and those already live in `leetcode_python/` and `leetcode_java/`. The cheatsheet should link to them, not paste them.
3. **One home per problem.** A worked LC problem lives in exactly one sheet. Every other mention is a one-row pointer. The rule already exists for topics (Scope lines); it needs to exist for problems.
4. **A front door.** Rebuild `lc_pattern.md` as *signal → pattern → sheet → card*, with the constraint ruler and an anti-recognition column. Every row links to a card.
5. **Show the drill path that is already in the data.** The L3 core set and the practice-log verdicts already exist. A sheet's drill list should come from them, ordered and flagged, not typed by hand and left to go stale.

---

## Proposed improvements

Ordered by **value per hour**. Each item is independently reviewable, in the same way the split plan's tiers were.

### P0 — fix the confirmed bugs (one small PR)

Fix the nine **verified** rows in [§9](#9-correctness--bugs-in-the-templates-people-memorise), then re-check and fix the **reported** ones. Smoke-test every touched snippet against the problem's own examples, the way `/lc-cheatsheet` step 6 does. Sweep the 26 subtraction comparators to `Integer.compare`, as `heap.md` was in August.

*Size: ~25 snippets. Each touched section also needs its 中文 overlay re-keyed (the cheatsheets are gated at 100%).*

### P1 — the front door

Rewrite [`lc_pattern.md`](cheatsheet/lc_pattern.md) as the router:

| Signal in the statement | Constraint cue | Pattern | Card | Looks like it, but is not |
|---|---|---|---|---|
| "contiguous subarray", "at most k distinct" | n ≤ 10⁵ | variable sliding window | [sliding_window § Template 2](cheatsheet/sliding_window.md) | negatives in the array → prefix sum + hash map |

- Pull the constraint ruler into it as a link. Merge the two copies into `complexity_cheatsheet.md` and leave `time_space_complexity.md` a pointer.
- Fix the mis-filings and remove the duplicate sections.
- Add it to `startHere` in `cheatsheet_meta.json` as the first rung.

### P2 — retrofit the pattern card onto the 24 tier-5 sheets

Start with the sheets an L3 loop leans on hardest: `binary_search`, `sliding_window`, `2_pointers`, `hash_map`, `tree`/`binary_tree`, `bfs`, `dfs`, `graph`, `heap`, `dp`, `backtrack`, `stack`, `linked_list`. Per sheet:

- give each template a heading that names its pattern and anchor problem, and add the **Recognise**, **Key point** and **Complexity** slots where they are missing;
- move the canonical template up into the first ~300 lines (`binary_search`);
- move a best-last section up (`stock_trading` §7, `monotonic_stack` §2-2+);
- add a `## Drill Path` (§ P5);
- delete "Universal Template" placeholders and `pass`-stub proofs. Each one either becomes runnable code or goes.

*Measure: recognition headings 37 → all tier ≥ 4 main sheets.*

### P3 — one home per problem

Work the hot-spot table in [§4](#4-one-problem-many-homes), cluster by cluster as the split plan did:

1. Shortest-path comparison → `shortest_path_comparison.md` owns it; the three algorithm sheets keep a pointer.
2. 0/1 knapsack → `knapsack.md` owns it. `knapsack_01_zh.md` becomes its i18n overlay (see P6), and `dp_advanced`'s copy goes.
3. Tree paths → `tree_lca_distance §4` (root-to-leaf path templates) owns LC 112/113/124/437. `bst_examples` Template 7 moves out, since it is not BST material. LC 1110 is also in 4 files, but it deletes nodes and returns a forest: it is tree modification, not a path problem, so its home is chosen with the tree-modification material, not here.
4. `monotonic_stack` owns the monotonic problems; `stack_examples` keeps the rest.
5. KMP / rolling hash → one owner.
6. Quickselect → `2_pointers_quickselect` owns it (rename to `quickselect.md`, leaving a stub). Make the 3-way-partition version the ⭐5 template.

Never delete a block whose duplicate is only *similar*: same problem, same language and same approach first (the split plan's rule).

### P4 — curate the `_examples` archives

The 13 `_examples` sheets that score ≤ 3. For each worked example:

- add the `> **Key point** —` line, and `**Reduces to**: [Template N](#…)`;
- **V0 is the canonical answer**, not the first one written. Demote or delete brute-force V0s. A second solution stays only with its reason written beside it;
- strip `NOTE !!!` walls into one or two prose bullets; drop "gpt", blog URLs and commented-out `print`;
- link to the full solution file in `leetcode_python/` rather than pasting a second copy.

*Measure: code-first example headings 401 → 0; no-insight 570 → < 100; `NOTE !!!` 583 → < 50.*

### P5 — drill path from the data

Add a `## Drill Path` section to every main sheet: **Core** (in [`l3_core.json`](../data/l3_core.json), ⭐), then **Practice**, then **Stretch**, each ordered easy → hard. Phase 1 writes it by hand from the core list.

Phase 2 (an idea, not this PR): have `build-site.js` render the verdict from `progress.txt` next to each LC number in a Drill Path, the way `build-roadmap.js` already stamps the roadmap, so the sheet shows `ok` / `again` / not tried.

### P6 — noise and language

- Emoji out of headings (keep `⭐` runs, which the site renders). Keep ✅ / ❌ only as table cell values.
- Retire `n-n)` numbering in new or rewritten sections. Number templates `Template 1…N` without gaps; examples need no number.
- Python first in every block pair. Add the missing Python to the Java-only template sheets (`dp_monotonic_stack`, `dp_string`, `knapsack`, `binary_search_on_answer`) and the missing Java to `Bellman-Ford` / `Floyd-Warshall`. Replace `n_sum`'s C++.
- Move inline 中文 out of English sheets into `i18n/zh/`. Turn `knapsack_01_zh.md` into the overlay for `knapsack.md` (it is a parallel tree that can drift).
- IPython transcripts become plain ` ```python ` with the output as a `# -> …` comment.
- Rewrite `tree_backtrack.md`'s chat-shaped header; fix `array_overlap_explaination`'s filename (leave a stub).

### P7 — a lint that ratchets

Add `script/check_cheatsheets.py`, a sibling of `check_readme.py` with the same **baseline ratchet**. Today's counts become the baseline, a new occurrence fails, and `--update-baseline` runs after a fix. Run it from `check-readme.yml` or a new workflow. Rules, each from a finding above:

| Rule | Finding |
|---|---|
| no `NOTE !!!`, no `In [n]:`, no `gpt`, no csdn/cnblogs/bilibili link | §3, §6 |
| no emoji in a heading other than a trailing `⭐` run | §6 |
| an `### … LC n` heading is followed by prose before its first fence | §3 |
| no Java `(a, b) -> a - b` comparator | §9 |
| no Python `(l + r) / 2` used as an index | §9 |
| no `## LC Examples` after `## Summary` | §7 |
| a tier ≥ 4 main sheet has a recognition section and a `## Drill Path` | §2, §8 |

Rules that need judgement (one home per problem, V0 is canonical) stay review items, not lint.

---

## Suggested order

| Step | Item | Why now |
|---|---|---|
| 1 | **This PR** — the template, the style guide and this review | Every later PR is reviewed against them |
| 2 | P0 bugs | Wrong templates are being memorised today |
| 3 | P7 lint with baseline | Stops the counts growing while P2–P6 shrink them |
| 4 | P1 front door | One file; the highest value per line |
| 5 | P2 tier-5 cards, a cluster per PR | tree → graph → arrays/strings → dp → hashing/stacks, the split plan's order |
| 6 | P3 + P4 together per cluster | Dedup and curation touch the same sections |
| 7 | P5 phase 2, P6 | Polish |

## Not proposed

- **No third skeleton.** A and B stay. The pattern card is what goes *inside* a template section of either.
- **No rewrite of a whole sheet in one PR.** Every sheet change has to re-key its 中文 overlay, and section-sized diffs are what that system is built for.
- **No deleting a variant that is only similar** (split-plan rule, kept).
- **No hand-typed counts in a sheet** ("Seventeen problems", `linked_list.md:1767`, was already wrong). Count from the data or do not count.

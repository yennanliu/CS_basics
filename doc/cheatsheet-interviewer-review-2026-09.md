# Cheatsheet Review — September 2026, from the interviewer's chair

A review of all 133 files in [`doc/cheatsheet/`](cheatsheet/) (142,931 lines) against a
single question: **does reading this make a candidate score better in a FAANG loop?**

This is a different axis from [`cheatsheet-review-2026-08.md`](cheatsheet-review-2026-08.md),
which asked whether the corpus was *organised*. It was, and it still is. This one asks
whether it is *coachable* — whether a reader ends up able to derive the solution unaided,
or only to recognise it.

> **[Addendum — is this good enough for Google L3?](#addendum--is-this-good-enough-for-google-l3-sep-2026)**
> narrows the same question to one bar, measured against this repo's own `google` tags.
> Short version: **coverage is done (172 of the 174 high-signal problems), allocation is not** —
> greedy gets 1.3% of the corpus for 4.0% of the Google ask, and 71% of that high-signal set is
> still marked `AGAIN`.

> **Method** — the corpus was scored as if it were a candidate, on the four signals a real
> packet uses (communication, problem solving, coding, verification) and the six-point scale
> `SH` / `H` / `LH` / `LNH` / `NH` / `SNH`. Every finding below names the file and line that
> produced it; nothing here is asserted without the input that shows it.

---

## Verdict

**LH (Lean Hire) — strong coverage, weak derivation.**

The corpus would pass a FAANG screen on *coding* and fail it on *problem solving*. It is an
excellent recognition index: 684 distinct problems, a decision table in almost every sheet,
and — in `graph.md` — the single best interview artefact in the repo. What it almost never
does is show where a solution *came from*. **17 of the 22 tier-5 sheets contain the phrase
"brute force" zero times.** They open at the optimal template.

That is the exact shape of the candidate a committee rejects as *memorised*: produces the
right structure, cannot say why that structure and not another, and has no safety net when
the problem is one rung off the template.

The corpus is also, on its own terms, self-contradicting in at least one place a candidate
would copy into a live interview — see finding 3.

---

## What was measured

| Check | Result | Interview signal it maps to |
|---|---:|---|
| Files / lines | 133 / 142,931 | — |
| Tier-5 sheets naming a **brute force** baseline | **5 / 22** | Problem solving |
| Tier-5 sheets stating an **invariant** | 11 / 22 | Problem solving |
| Tier-5 sheets with a **follow-up** question | 8 / 22 → **21 / 22** ✅ | Problem solving (`H` → `SH`) |
| Sheets saying **when the pattern does *not* work** | 10 / 133 | Problem solving |
| Sheets mentioning **clarifying questions** | 22 / 133 | Communication |
| Sheets with a **dry run / trace** | 31 / 133 | Verification |
| Java/Python blocks carrying `time = O(...)` | **43%** (1,327 / 3,120) | Verification |
| Sheets sizing an algorithm from **input constraints** (`n ≤ 10⁵`) | 39 / 133 | Problem solving |
| Cheatsheets carrying *any* link into this repo's own solutions | **5 / 133** (14 links) | — |
| LC numbers cited as drills with no README entry | **196 distinct, 598 mentions** | — |
| Untagged code fences / missing Scope lines | **0 / 0** | — (clean, still clean) |

Complexity annotation is up from the 35% the August review left it at — real progress, and
still the weakest verification number in the table.

---

## Findings, ranked by how much they cost in a real loop

### 1. The sheets start at the answer — no brute-force rung ⭐⭐⭐⭐⭐

*Problem solving.* The interview is scored on **brute force → bottleneck → optimal**. The
brute force is the safety net: a candidate who states one has a working solution on the board
within five minutes and buys the rest of the hour to improve it. A candidate who has only
memorised the optimal template has nothing to fall back to when it does not fit.

Counts of the phrase in tier-5 sheets:

```text
the 5 that mention it   backtrack.md 2   dp.md 2   binary_tree.md 1
                        lc_pattern.md 1  string.md 1

the 17 that do not      2_pointers  array  bfs  binary_search  binary_search_on_answer
                        bst  complexity_cheatsheet  dfs  graph  hash_map  heap
                        knapsack  linked_list  sliding_window  stack  tree
                        tree_lca_distance
```

`knapsack.md` is the sharpest case. 1,401 lines on 0/1 and unbounded knapsack, and it
contains **no recursion or memoisation derivation at all** — grep for `memo|recursi|top.down`
returns one hit, a "Memory Trick" heading at line 799. It opens at the loop-order decision
table (line 53). But every knapsack recurrence *is* a memoised brute force with the memo
flattened, and that derivation is what lets a candidate rebuild the loop order under pressure
instead of guessing which of the two it was.

Worse, the sheet that does this correctly already exists — [`recursion_to_dp.md`](cheatsheet/recursion_to_dp.md)
walks LC 322 through recursion → memoisation → bottom-up (line 441). `knapsack.md` also
covers LC 322, and **the two sheets do not link to each other.**

> **Fix** — in each tier-5 sheet, one paragraph before the first template: the brute force,
> its complexity, and the single line of repeated work the template removes. `bfs.md`'s
> Pattern 3.1 (line 238) is the in-repo model for how to write it.

---

### 2. Pitfalls are asserted, not traced ⭐⭐⭐⭐⭐

*Verification.* A warning with no failing input is not verifiable, so a reader cannot tell
whether their own code has the bug. `graph.md:706`:

```text
🚫 Common Mistakes:
- Not handling disconnected components
- Incorrect visited state management
- Missing cycle detection in recursive DFS
- Wrong graph representation choice
```

Four true statements, zero inputs. Compare what the same claim looks like when traced — this
is `bfs.md:238`, and it is the best-written section in the corpus:

> **Pattern 3.1: The Visited-Set Placement Rule — Mark Before Enqueue** — states the rule,
> gives the graph on which mark-after-enqueue enqueues a node twice, and shows the resulting
> wrong count.

That section teaches. The `graph.md` list reminds. Only one of them survives into a room.

> **Fix** — every `Common Mistakes` bullet gets the smallest input that triggers it and the
> wrong answer it produces, or it gets deleted. There are 94 sheets with a pitfall section;
> this is the highest-yield edit in this document.

---

### 3. `binary_search.md` contradicts its own pitfall list ⭐⭐⭐⭐⭐

*Coding.* The sheet lists the overflow bug as a pitfall:

```text
binary_search.md:1376
- Integer overflow in `mid = (left + right) / 2` → Use `mid = left + (right - left) / 2`
```

And then ships the Java template for LC 33 with exactly that bug:

```java
// binary_search.md:403 — LC 33, Search in Rotated Sorted Array
int mid = (l + r) / 2;
```

This is not theoretical for a candidate: a Java interviewer who sees `(l + r) / 2` asks
"what if the array were `Integer.MAX_VALUE` long?", and a candidate who copied the template
has no answer, because the sheet never made them derive it.

Corpus-wide: **19 Java blocks use the overflow-prone form, 48 use the safe one.** The
inconsistent files are `binary_search.md`, `binary_search_examples.md`,
`binary_search_on_answer.md`, `advanced_divide_and_conquer.md`, `binary_indexed_tree.md`,
`advanced_string_algorithms.md`.

> **Fix** — mechanical sweep to `lo + (hi - lo) / 2` in every Java block, and one sentence at
> the site of the change saying why. This is the only finding here that is a *correctness*
> defect rather than a pedagogy gap, so it should go first.

**Done (Sep 2026).** All **19** sites rewritten to `lo + (hi - lo) / 2`, across
`binary_search.md`, `binary_search_examples.md` (4), `binary_search_on_answer.md` (2),
`segment_tree.md` (4), `advanced_divide_and_conquer.md` (3), `binary_indexed_tree.md`,
`Dijkstra_examples.md`, `advanced_string_algorithms.md`, `prefix_sum_examples.md`,
`matrix_examples.md`. Zero remain.

Every rewritten site was checked for a negative lower bound first — the two forms round
differently when `lo < 0` — and all 19 start at `l = 0` or `l = 1`, so the change is exactly
equivalent as well as overflow-safe. Python blocks were left alone: `//` on unbounded ints
cannot overflow, and rewriting them would teach a defensive habit the language does not need.

---

### 4. No "when this pattern does *not* apply" ⭐⭐⭐⭐

*Problem solving.* 10 sheets of 133 say when their own technique fails. Knowing which
neighbouring problem is **not** a sibling is most of what pattern recognition is — it is the
difference between a candidate who reaches for a sliding window because the problem says
"subarray" and one who first checks that the window predicate is monotone.

The look-alikes the corpus never disambiguates, each of which is a real interview trap:

| Looks like | Actually needs | Why the default fails |
|---|---|---|
| Sliding window on "longest subarray, sum ≤ k" | prefix sums + deque | with negatives, shrinking the window does not lower the sum |
| Two pointers on a sorted-pair problem | hash map | the array is not sorted and sorting loses the indices |
| Greedy on interval scheduling | DP | weighted intervals — earliest-finish is no longer optimal |
| Monotonic stack on `LC 84` vs deque on `LC 239` | different invariants | nothing leaves a window in 84; drilling them as a pair teaches the wrong grouping |
| BFS for shortest path | Dijkstra | edges are weighted; BFS's level = distance only when they are not |

> **Fix** — a `Does not apply when` block in each tier-5 sheet's Overview, with the
> look-alike problem named. Five lines per sheet.

---

### 5. The follow-up question is missing — and it is what separates `H` from `SH` ⭐⭐⭐⭐

*Problem solving.* The main problem filters; the variant separates a Hire from a Strong Hire.
Eight tier-5 sheets mention a follow-up at all, and most of those are in prose rather than
as a posed question.

Nothing in the corpus prepares a candidate for "now the array is streaming", "now `k` changes
per query", "now do it in O(1) space", "now it has to work if the interviewer adds duplicates" —
which is what the last eight minutes of a real round are.

> **Fix** — one `### Follow-up` heading per template section, stating the variant and the one
> line of the template that has to change. `dfs.md` (4 mentions) is the closest existing model.

**Done (Sep 2026).** A `Follow-ups — the variants that separate H from SH` section was added to
the 13 tier-5 sheets that had none: `2_pointers`, `array`, `bfs`, `binary_search_on_answer`,
`binary_tree`, `bst`, `graph`, `knapsack`, `linked_list`, `sliding_window`, `stack`, `tree`,
`tree_lca_distance`. Coverage is now **21 / 22**.

Each is a table of 6–8 rows: *what you just solved* → *the follow-up an interviewer asks* →
*the one line of the template that changes*. Every one of the 80 LC numbers cited resolves to a
README entry, so no row sends a reader to a problem this repo cannot show (see finding 8).

`lc_pattern.md` is deliberately excluded — its own Scope line says "index and triage only, no
templates here", so there is no template for a follow-up to modify.

Two things this deliberately did **not** do, and which remain open:

- The sections are **consolidated per sheet**, not placed inline under each template section as
  the fix above specifies. Consolidated is greppable and testable; inline is better teaching.
  Splitting them out is the follow-up to this follow-up.
- The 8 sheets that already mention follow-ups still do so **in prose**, not as posed questions
  (`dfs.md:237`, `hash_map.md:1041`, `dp.md:1548`). They were left alone.

---

### 6. Input constraints are never turned into an algorithm budget ⭐⭐⭐⭐

*Problem solving.* "The constraints say `n ≤ 10⁵`, so O(n²) is 10¹⁰ operations and is out,
which leaves O(n log n) or better" is the sentence an interviewer most wants to hear in the
first two minutes — it is the one that proves the candidate *chose* the structure rather than
recalled it.

The corpus already has the table that produces that sentence.
[`complexity_cheatsheet.md:679`](cheatsheet/complexity_cheatsheet.md) is exactly right:

```text
N ≤ 20        → O(2^N)              bitmask DP, subsets
N ≤ 1,000     → O(N²)               nested loops, naive DP
N ≤ 100,000   → O(N log N)          sorting, heap, balanced BST
N ≤ 10^9      → O(log N) or O(√N)   binary search, math tricks
```

**It is unreachable.** Zero of the 20 tier-5 topic sheets link to `complexity_cheatsheet.md`,
and only 39 of 133 sheets mention a constraint bound at all. The artefact is written and
filed where nobody working through `dp.md` or `graph.md` will meet it.

> **Fix** — not authoring. One link line in each tier-5 sheet's Overview, next to the existing
> `**Complexity**:` bullet. This is the cheapest item in this document.

---

### 7. `graph.md`'s best table exists in exactly one sheet ⭐⭐⭐⭐

*Communication.* `graph.md:617` is the single most interview-useful artefact in the corpus:

```text
| Signal                                       | Pattern                              |
| "shortest path, non-negative weights"        | Dijkstra                             |
| "course prerequisites, ordering"             | Topological sort (Kahn's BFS)        |
| "connected because they share a row/email"   | Make the attribute a DSU node        |
| "longest path, but moves strictly increasing"| Implicit DAG → DFS + memo (LC 329)   |
```

It maps *the interviewer's own words* to a pattern. That is exactly the lookup a candidate
performs in the first 60 seconds, and it exists nowhere else in 133 files.

Meanwhile the same file's `Interview Tips` (line 722) is the opposite — "Draw small examples",
"Handle edge cases", "Optimize incrementally". Generic, unquotable, and a candidate cannot
say any of it out loud to earn a point.

> **Fix** — promote the signal→pattern table to every tier-5 sheet; delete the generic
> `Interview Tips` blocks rather than porting them.

---

### 8. The cheatsheets and the 1,300 solutions are two disconnected islands ⭐⭐⭐

*Not a signal — a workflow gap.* **5 sheets of 133** carry any link into `leetcode_python/`
or `leetcode_java/` — 14 links in total, against 113 outbound links to leetcode.com and 684
problems with a code block. A reader who finishes `sliding_window.md` and wants to practise
has to go and search for the file.

Of those 14, only **4 are relative paths**; the other **10 are absolute
`github.com/yennanliu/...` URLs**, which bounce the reader off the site entirely and which
`e2e-check.js` cannot validate — the same blind spot `check_skills.py` exists to cover for
`.claude/skills/`.

Compounding it: **598 drill mentions cite LC numbers with no README entry**, including
`LC 704` — the canonical binary search problem, cited 9 times, with no solution in this repo.
Others: `LC 1235` (×22), `LC 1044` (×17), `LC 132`, `LC 301`.

Both halves of the `/lc-coach` rule apply here — *exists* and *solved here* are different
claims, and an unmarked drill list conflates them.

> **Fix** — (a) a build step that resolves every `LC n` in a cheatsheet against README and
> marks it, the way `build-roadmap.js` already fails the build on an unknown id;
> (b) rewrite the 10 absolute URLs as relative paths so they resolve in-site and `e2e-check.js`
> can see them; (c) file `LC 704` — a tier-5 sheet should not cite a problem the repo cannot
> show.

---

### 9. Language coverage is lopsided in the sheets where it matters ⭐⭐⭐

*Coding.* The house rule is both languages when applicable. Tier-5 reality:

| Sheet | Python | Java |
|---|---:|---:|
| `graph.md` | 14 | **1** |
| `dp.md` | 25 | 7 |
| `hash_map.md` | 20 | 5 |
| `binary_tree.md` | 29 | 10 |
| `knapsack.md` | **3** | 13 |

`graph.md` has one Java block in 735 lines. `knapsack.md` is inverted — a Python candidate
gets three. Given `leetcode_java/` holds 508 solutions, the Java reader is being served a
Python sheet with a gap in it.

> **Fix** — bring the tier-5 sheets to parity, starting with `graph.md` and `knapsack.md`.
> Not a sweep; each block needs the idiomatic translation (`ArrayDeque` not `Stack`, `long`
> where a sum can overflow).

---

### 10. Pattern numbering has holes ⭐⭐

*Cosmetic, but it reads as missing content.* A reader who sees `Pattern 5` then `Pattern 7`
assumes a section was lost and goes looking for it.

```text
bfs.md          has 1,2,3,4,5,7,11,13          missing 6, 8, 9, 10, 12
bfs_advanced.md has 1,2,4,6,7,8,9,10,12,14,15,16  missing 3, 5, 11, 13
prefix_sum.md   has 1..9, 15                   missing 10–14
bst.md          has 1..6, 10                   missing 7, 8, 9
00_template.md  has 1, 4, 7                    missing 2, 3, 5, 6
```

> **Fix** — renumber contiguously, or drop the numbers and let the titles carry it.

---

### 11. Duplicate heading text inside one file ⭐⭐

*Named as an anti-pattern in `CLAUDE.md`; still present.* Worst offenders:

```text
tree2.md                  "template code" ×27, "leetcode problems" ×27
dp_pattern.md             "common problems:" ×15, "template code:" ×13
2_pointers_examples.md    "core idea" ×10, "similar problems" ×8
sliding_window_advanced.md "core idea" ×6
hash_map_examples.md      "key concept" ×5
```

Beyond the style rule, this breaks the site: repeated heading text produces colliding anchor
slugs, so a `#core-idea` link lands on whichever one came first.

(`recursion_to_dp.md`'s `"step 1: recursion" ×3` is *correct* — three worked examples running
the same three-step ladder — and should be left alone. The rule is "qualify them", not
"never repeat".)

---

### 12. The same problem is solved in up to 6 sheets, unmarked ⭐⭐

684 distinct problems carry a code block; they appear across **1,174 (problem, sheet) pairs** —
an average of 1.72 sheets each. 21 problems appear in five or more:

```text
LC 763  ×6   2_pointers_examples, greedy_examples, hash_map_examples, intervals,
             sliding_window_examples, string
LC 300  ×6   binary_search, binary_search_examples, dp, java_trick_collections,
             segment_tree, time_space_complexity
LC 647  ×6   2_pointers, 2_pointers_examples, java_trick, palindrome, …
LC 322  ×5   dp, dp_pattern, knapsack, recursion_to_dp, time_space_complexity
```

Some of this is *right*: LC 300 in `binary_search.md` and in `dp.md` is the whole lesson —
O(n²) DP versus O(n log n) patience sorting. The problem is that **none of them say so**. A
reader landing on the DP version never learns there is a better one two files over.

> **Fix** — not deduplication. A one-line cross-reference at each site: *"also solved in
> `binary_search.md` at O(n log n) — compare the two."* The duplication becomes the teaching.

---

## Recommended order

Ranked by interview impact per hour of editing:

| # | Action | Scope | Why first |
|---|---|---|---|
| 1 | ~~Fix the 19 `(l + r) / 2` Java blocks~~ — **done**, 19/19 | — | the only correctness defect; a candidate ships this bug |
| 2 | Give every pitfall bullet a failing input | 94 sheets, incremental | converts the corpus from reminder to teacher |
| 3 | Add a brute-force rung to the 22 tier-5 sheets | 1 paragraph each | the missing problem-solving signal |
| 4 | Promote the signal→pattern table out of `graph.md` | 22 sheets | the highest-value artefact, currently stranded |
| 5 | `Does not apply when` block per tier-5 sheet | 5 lines each | stops the look-alike trap |
| 6 | Link tier-5 Overviews to the existing constraint table | 20 one-line edits | cheapest item here; earns the point in minute two |
| 7 | ~~`### Follow-up` per template section~~ — **done**, consolidated per sheet (13 sheets, 21/22 coverage) | — | the `H` → `SH` rung |
| 8 | Link cheatsheets to this repo's solutions; validate `LC n` in the build | tooling | closes the practice loop |
| 9 | Java/Python parity in `graph.md`, `knapsack.md`, `hash_map.md`, `dp.md` | per-block | serves half the readership |
| 10 | Renumber patterns; qualify duplicate headings; cross-reference the 21 multi-sheet problems | cosmetic + anchors | cheap, and fixes real anchor collisions |

---

## What is already good, and should not be touched

Worth stating, because a findings list reads as if nothing works:

- **Zero untagged fences, zero missing Scope lines, zero broken structure.** The August
  cleanup held, through a near-doubling of the file count (74 → 133) at flat line count.
- **`bfs.md` Pattern 3.1 (mark before enqueue)** — states a rule, proves it with the graph
  that breaks the alternative, then generalises. This is the template every other pitfall
  section should be rewritten against.
- **`graph.md`'s Scope block** — names what the file owns *and* what ten neighbouring sheets
  own, each with the reason you would go there. The best-executed instance of the house rule.
- **`recursion_to_dp.md`** — the one sheet that teaches derivation rather than recall. Finding
  1 is largely a request to do what this file already does, elsewhere.
- **`graph.md:617`, the signal→pattern table.** Finding 7 is a request to copy it, not change it.
- **Complexity annotation is climbing** — 35% → 43% since August.

---

---

## Addendum — is this good enough for Google L3? (Sep 2026)

The review above asks whether the corpus is *coachable*. This addendum asks a narrower,
harder question: **would working through `doc/cheatsheet/` prepare someone for a Google L3
SWE loop specifically?** L3 is entry-level — two coding rounds, ~45 minutes each, the mass of
questions Medium, at most one Hard, and essentially no systems round.

Everything below is measured against this repo's own Google data: the `google` tag in
`README.md` (928 tagged problems, written from [`data/company_lc_tags.json`](../data/company_lc_tags.json)),
crossed with the curated lists.

### Verdict — `H` on coverage, `LNH` on allocation

**Coverage is not the problem, and it is worth being clear about that.** Take the
high-signal L3 set — problems that are Google-tagged **and** on Blind 75 / NeetCode 150 /
LeetCode Top 100 Liked — and the corpus covers essentially all of it:

| | |
|---|---:|
| Google-tagged ∩ curated list | **174** |
| …mentioned somewhere in `doc/cheatsheet/` | **172 (99%)** |
| …not mentioned anywhere | **2** |

The two gaps are `LC 118` Pascal's Triangle (Easy) and `LC 1899` Merge Triplets to Form
Target Triplet (Medium). Neither is worth a paragraph. **As a breadth index for Google L3,
this corpus is done.**

What is *not* done is where the effort sits, and what the reader is told to skip.

### Finding L1 — effort is allocated against the wrong distribution ⭐⭐⭐⭐⭐

Line count per topic versus how often Google actually asks that topic. `%eff` is the
bucket's share of the corpus; `%ask` is its share of the 905 Google-tagged problems that fall
in a mapped README section. Ratio = `%eff / %ask`.

| Bucket | sheets | lines | %eff | Google | %ask | ratio |
|---|---:|---:|---:|---:|---:|---:|
| Heap / priority queue | 5 | 4,795 | 3.7% | 14 | 1.5% | **2.39×** |
| Two pointers & sliding window | 7 | 8,024 | 6.2% | 33 | 3.6% | **1.70×** |
| Graphs & traversal (BFS/DFS/UF/topo/SP) | 19 | 22,895 | 17.7% | 96 | 10.6% | **1.67×** |
| Trees & BST | 11 | 13,897 | 10.7% | 71 | 7.8% | 1.37× |
| Design & streaming | 7 | 6,116 | 4.7% | 39 | 4.3% | 1.10× |
| Dynamic programming | 11 | 14,562 | 11.2% | 100 | 11.0% | 1.02× |
| Stack & monotonic | 6 | 6,557 | 5.1% | 46 | 5.1% | 1.00× |
| Linked list | 2 | 2,761 | 2.1% | 22 | 2.4% | 0.88× |
| Arrays, prefix, intervals, sort | 18 | 16,958 | 13.1% | 144 | 15.9% | 0.82× |
| Hashing & sets | 5 | 5,744 | 4.4% | 53 | 5.9% | 0.76× |
| Strings & palindrome | 7 | 7,238 | 5.6% | 75 | 8.3% | 0.67× |
| Binary search | 3 | 4,015 | 3.1% | 45 | 5.0% | **0.62×** |
| Backtracking & recursion | 4 | 4,890 | 3.8% | 58 | 6.4% | **0.59×** |
| Math & bits | 5 | 5,273 | 4.1% | 73 | 8.1% | **0.50×** |
| Greedy | 2 | 1,717 | 1.3% | 36 | 4.0% | **0.33×** |

**Greedy is the standout.** 36 Google-tagged problems — more than heaps and linked lists put
together — served by 1,717 lines, the smallest allocation in the table. Google asks greedy a
lot, because "is the obvious greedy actually optimal?" is a cheap way to separate candidates
who reason from candidates who pattern-match.

> **Fix** — grow `greedy.md`, `binary_search.md` and `backtrack.md` toward their ask share
> before adding another line anywhere above 1.0×. This is a *reallocation*, not a request for
> 10,000 more lines.

**Two caveats on this table, because both nearly produced a wrong finding.** README's `Graph`
section holds only 10 Google-tagged problems — `LC 200`, `LC 207`, `LC 133` and the rest are
filed under `Breadth-First Search` / `Depth-First Search` / `Tree`. Measuring `graph*.md`
against that section alone gives a meaningless 11×, so the buckets above merge graphs with
both traversal sections. For the same reason `trie.md` / `segment_tree.md` /
`binary_indexed_tree.md` are **excluded entirely** — their 24 Google-tagged problems are
scattered across seven other sections, so no honest ratio can be computed for them from the
README taxonomy.

### Finding L2 — ~13,000 lines are above the L3 bar, and nothing says so ⭐⭐⭐⭐

About **10% of the corpus** teaches material an L3 candidate will not be asked:

```text
dp_advanced 3,179   bfs_advanced 2,072   dfs_advanced 1,781   streaming 1,623
bst_advanced 1,552  sliding_window_advanced 1,527   segment_tree 1,316
advanced_string_algorithms 1,174   advanced_simulation 1,151
advanced_divide_and_conquer 1,138   kmp_rolling_hash 1,092   heap_advanced 1,091
graph_advanced 1,046 (Tarjan, max flow, bipartite matching)
combinatorics 890   binary_indexed_tree 813   dp_digit 673   dp_bitmask 653
```

None of this is *wrong* to have — it is right for L4+, for competitive programming, and for
the occasional Hard. It is wrong to leave **unmarked** in front of a reader with eight weeks
and a specific bar to clear. Suffix automata and digit DP are not an L3 risk; spending a
weekend on them instead of greedy is.

The tier data already exists — [`data/cheatsheet_meta.json`](../data/cheatsheet_meta.json)
scores every sheet 5 down to 2 — but it is surfaced **only on `cheatsheets.html`**. A reader
who opens `dp_digit.md` from a search result, or reads it on GitHub, gets no signal at all.

> **Fix** — put the tier in the sheet, in the Scope block: one line such as
> `> **Bar** — tier 2 (niche). Above the L3 ask; read after the tier-4/5 sheets.`
> Generated from the metadata, not hand-typed, so it cannot drift from the index.

### Finding L3 — the real L3 gap is retention, not coverage ⭐⭐⭐⭐⭐

The same 174-problem high-signal set, scored against the practice log in
[`data/progress.txt`](../data/progress.txt):

| Status | Count | Share |
|---|---:|---:|
| `OK` | 48 | 28% |
| `AGAIN` | **123** | **71%** |
| never attempted | 3 | 2% |

**71% of the exact problems a Google L3 loop draws from are still marked AGAIN.** Where they
cluster:

```text
Dynamic Programming 19   BFS 11   Array 9   Binary Search 9   Backtracking 9
Greedy 9   Recursion 8   Tree 7   Stack 6   Two Pointers 6   Hash Table 5   String 5
```

Note what that list agrees with: four of the six largest AGAIN clusters — binary search,
backtracking, greedy, recursion — are the same four buckets that finding L1 shows are
*under-resourced*. That is not a coincidence, and it is the single most actionable thing in
this document. The sheets that are thinnest relative to the Google ask are the sheets whose
problems are not graduating.

This also confirms, from the corpus side, what
[`doc/lc-readiness-guide.md`](lc-readiness-guide.md) measures from the profile side: the
constraint is not how many problems have been seen, it is how many have been *retired*.

**Acted on (Sep 2026).** [`lc-stuck-22-drill.md`](lc-stuck-22-drill.md) takes the **22**
problems in this set at 16+ `AGAIN` marks — ~391 attempts that retired none of them — and gives
each one its brute force, the recomputation the optimal solution removes, the invariant to say
out loud before typing, and the specific reason that problem keeps failing. It deliberately
contains no code.

The entries are grouped by recurrence family rather than by attempt count, because six of the
top seven are 1-D sequence DP wearing different clothes — a much smaller problem than a list of
22 suggests.

> **Fix** — the corpus cannot fix a practice habit, but it can stop being the bottleneck:
> the derivation gap (finding 1), the untraced pitfalls (finding 2) and the missing
> constraint budget (finding 6) are precisely what makes a problem fail to graduate. Work
> them in the four under-resourced buckets first, not evenly across all 133 sheets.

### Finding L4 — `greedy.md` is the weakest high-yield sheet ⭐⭐⭐⭐

It is the worst cell in finding L1 (0.33×) and it has a second, independent problem: of its
16 Java/Python blocks, **zero carry a `time = O(...)` line** — the lowest annotation rate of
any tier-4 sheet.

Structurally it is better than its size suggests: it *does* have the exchange argument
(line 350) and a `When Greedy Fails` escape hatch (line 276), which is exactly the right
content and is more than most greedy references carry. But the exchange-argument section is
four lines long and ends with a wrong pointer:

```text
greedy.md:361
Common exchange argument problems: LC 435 (Non-overlapping Intervals),
                                   LC 452 (Burst Balloons), Job Scheduling.
```

`LC 452` is **Minimum Number of Arrows to Burst Balloons** — a greedy interval problem, so the
*number* is right. **Burst Balloons is `LC 312`**, an O(n³) interval-DP Hard, and the closest
thing to the opposite of a greedy exchange argument. A reader who searches the title rather
than the number loses an evening to the wrong problem, which is the exact failure the
`/lc-coach` rule about never mis-pairing a number and a title exists to prevent.

> **Fix** — corrected in this commit. The sheet still needs the proof section expanded from
> four lines to the three canonical arguments (stay-ahead, exchange, cut-and-paste), each with
> the LC problem it settles.

### What an L3 reader should actually be told

The corpus has no reading order for this bar. Given the numbers above, the honest one is:

1. **Clear the 123 AGAIN problems in the high-signal set** before reading anything new. That
   is the bar, and it is where the gap is.
2. **Tier 5 first**, and inside it favour the under-resourced four — binary search, greedy,
   backtracking/recursion, math & bits — because that is where the AGAIN markers and the thin
   sheets coincide.
3. **Skip every `*_advanced.md`, `segment_tree`, `binary_indexed_tree`, `dp_digit`,
   `dp_bitmask` and `graph_advanced`** on a first pass. ~13,000 lines, near-zero L3 yield.
4. **Read `complexity_cheatsheet.md:679` once** — the input-size → complexity table — and be
   able to produce it from memory. It is minute two of every round (finding 6).

> **Not done** — this list is prose here, not a page. The natural home is the Study Roadmap
> (`data/roadmap.json`), which already has the machinery for an ordered, prerequisite-aware
> path and a list picker with a `readme:google` source. An `L3` list filed against these
> priorities would make this addendum executable instead of advisory.

## One-line summary for the next reviewer

The corpus knows every pattern and teaches almost none of them from first principles; the
single edit that would move it from `LH` to `H` is putting the brute force back at the top of
each tier-5 sheet, because everything a candidate is actually scored on hangs off that rung.

And for Google L3 specifically: **stop adding problems.** The index is complete. Move lines
from heaps and advanced graphs into greedy, binary search, backtracking and math — the four
buckets where thin sheets and un-graduated `AGAIN` markers are the same four buckets.

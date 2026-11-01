# Java port progress — Nov 2026

Tracking doc for porting every `leetcode_python/` solution that has no Java counterpart into
`leetcode_java/`, one PR per batch, following [`/lc-java`](../../.claude/skills/lc-java/SKILL.md).
`doc/cheatsheet/` is out of scope and untouched by every PR below.

Snapshot: **2026-11-01**. Re-measure with `python3 script/find_missing_java.py` (it reads
`master`, so the numbers only move as PRs merge).

## Summary

| | Problems | PRs |
|---|---:|---:|
| Starting gap (`find_missing_java.py` on master) | 1556 | — |
| **Done** — ported, compiled, tested, PR open | **1373** | **64** |
| **Doing** — agent running | ~176 | 6 |
| **Todo** — not started | 0 batches | — |
| Won't port (pandas-only, no Java on LC) | 5 | — |
| Not a real gap (matcher false positive) | 1 | — |

How the work was split:

- **Wave 1** — the ~55 problems whose README row is in a main `## ` table (or that the script
  could not number): one PR per Python category.
- **Wave 2** — the ~1500 problems indexed only in the imported `## Newly Added (kamyu104 gap)`
  tables: each category in batches of ~30, sorted by LC number, one PR per batch.

Every batch was done in its own git worktree off `origin/master`, as a single commit that touches
only `leetcode_java/` and `README.md`. The README change is always `, [Java](...)` appended to the
existing row's Solution cell — no row added (one exception, see [Follow-ups](#follow-ups)), no
other cell touched. Each file was compiled with `javac` and run against its docstring examples;
where several versions are filed they were cross-checked against each other, and the riskier ones
against a brute force on random inputs.

## Done

### Wave 1 — main-table problems

| PR | Branch | Files | Problems |
|---|---|---:|---|
| #223 | `java-port/array` | 5 | 2033, 2075, 4052, 4083, 4084 |
| #220 | `java-port/math` | 6 | 1131, 2028, 2029, 2091, 4053, 4078 |
| #218 | `java-port/sort` | 2 | 4056, 4057 |
| #242 | `java-port/dynamic_programming` | 6 | 44, 132, 1218, 2188, 4040, 4072 |
| #265 | `java-port/two_pointers` | 1 | 1498 |
| #222 | `java-port/tree` | 1 | 4085 |
| #261 | `java-port/hash_table` | 4 | 2025, 3026, 4038, 4066 |
| #264 | `java-port/string` | 4 | 2211, 4043, 4070, 4071 |
| #217 | `java-port/stack` | 2 | 4045, 4054 |
| #233 | `java-port/backtracking` | 3 | 282, 301, 425 |
| #237 | `java-port/binary_search` | 2 | 704, 1287 |
| #234 | `java-port/bfs` | 2 | 1136, 2812 |
| #263 | `java-port/slide_window` | 4 | 1423, 1461, 1888, 4067 |
| #262 | `java-port/prefix_sum` | 3 | 2012, 2017, 4058 |
| #219 | `java-port/heap` | 2 | 1642, 1882 |
| #253 | `java-port/greedy` | 2 | 1249, 4079 |
| #240 | `java-port/design` | 1 | 4080 |
| #241 | `java-port/dfs` | 2 | 1376, 3590 |

### Wave 2 — imported-table batches

| Category | PRs (batch → LC range, files) |
|---|---|
| Array | #224 `-01` 1002–1404 (30) · #225 `-02` 1413–1646 (30) · #226 `-03` 1652–2007 (30) · #227 `-04` 2011–2270 (30) · #228 `-05` 2271–2574 (30) · #229 `-06` 2580–2884 (27) · #230 `-07` 2885–3206 (28) · #231 `-08` 3212–3522 (30) · #232 `-09` 3537–3687 (25) |
| Dynamic_Programming | #243 `-01` 1035–1416 (32) · #244 `-02` 1420–1692 (32) · #245 `-03` 1745–2060 (32) · #246 `-04` 2088–2431 (32) · #247 `-05` 2435–2826 (32) · #248 `-06` 2827–3253 (32) · #249 `-07` 3250–3525 (32) · #250 `-08` 3530–3686 (26) · #279 `-09` 3213 (1, missed by `-06`) |
| Greedy | #254 `-01` 1005–1526 (29) · #255 `-02` 1536–1758 (29) · #256 `-03` 1759–1936 (29) · #257 `-04` 1946–2285 (29) · #258 `-05` 2294–2522 (29) · #259 `-06` 2541–2789 (29) · #260 `-07` 2790–3221 (29) · #266 `-08` 3228–3681 (29) |
| Hash_table | #268 `-01` 1001–2085 (32) · #267 `-02` 2120–2404 (32) · #269 `-03` 2423–3039 (32) · #270 `-04` 3043–3682 (32) |
| Math | #273 `-01` 1006–1330 (31) · #271 `-02` 1344–1823 (31) · #272 `-03` 1828–2176 (31) · #274 `-04` 2177–2521 (31) · #278 `-05` 2523–3032 (31) · #276 `-07` 3426–3677 (25) |
| String | #275 `-01` 1016–1545 (32) · #277 `-02` 1554–1957 (32) · #281 `-04` 2288–3104 (32) |
| Binary_Search | #238 `-01` 1044–2476 (30) · #239 `-02` 2513–3639 (29) |
| Breadth-First-Search | #235 `-01` 1034–2050 (28) · #236 `-02` 2059–3666 (28) |
| Graph | #251 `-01` 1042–2127 (28) · #252 `-02` 2172–3656 (28) |
| Design | #221 `-01` 1570, 2102, 2254 (3) |

Branch names are `java-port/<category>-<nn>`; a file lands in the Java package that matches the
technique its solution uses, so a "Greedy" batch can put a file in `Heap/` or `DynamicProgramming/`.

## Doing

Agents running at the time of writing. Each pushes `java-port/<batch>` and opens its own PR.

| Batch | Branch | LC range | Problems |
|---|---|---|---:|
| Math_06 | `java-port/math-06` | 3044–3416 | 31 |
| String_03 | `java-port/string-03` | 1961–2278 | 32 |
| String_05 | `java-port/string-05` | 3110–3675 | 29 |
| Two_Pointers_01 | `java-port/two_pointers-01` | 1033–2024 | 28 |
| Two_Pointers_02 | `java-port/two_pointers-02` | 2040–2570 | 28 |
| Two_Pointers_03 | `java-port/two_pointers-03` | 2609–3679 | 28 |

## Todo

- Merge the PRs above. Each changes different README lines, but two rows that sit next to each
  other can still conflict; the later PR then needs a rebase onto `master`.
- After everything merges, re-run `python3 script/find_missing_java.py`. The expected remainder
  is the 5 pandas problems plus the LC 380 false gap below, so anything else is a real miss.

## Won't port

| LC | Why |
|---|---|
| 2882, 2883, 2884, 2885, 2886 | pandas DataFrame problems; LeetCode offers no Java version |

## Follow-ups

Found while porting; none was changed in these PRs, because each touches only `leetcode_java/`
and README's Solution cell.

| Where | Issue |
|---|---|
| README, LC 1131 | Row added by #220 (no row existed); status `AGAIN(1)`, complexity and note filled in by the agent — review it. Line ~486 is "Rank Transform of an Array" mis-numbered **1131** (should be 1331, which already has its own row), so 1131 now appears twice. |
| LC 380 | Not a real gap: `Design/InsertDeleteGetRandom0_1.java` exists and README links it, but the Python file has no url and is named `..._O_1` (letter O) vs the Java `..._0_1` (zero), so `find_missing_java.py` cannot join them. A url in the Python header fixes it. |
| `leetcode_python/Math/abbreviating-the-product-of-a-range.py` (LC 2117) | Wrong for `left=4838, right=6186`: keeps 15 leading digits and truncates each step, so it returns `36087...` instead of `36088...`. The Java V1 keeps 30 digits. |
| `leetcode_python/...` LC 3261 | Docstring Example 2's expected output `[15,9,11,5,5]` is wrong — the code itself returns `[15,9,9,6,6]`. |
| `leetcode_python/String/...` LC 2315 | Docstring Examples 2–3 lost their `*`s when copied, so they no longer give the stated outputs; the Java javadoc uses LeetCode's real inputs. |
| `leetcode_python/...` LC 1728 | Can enqueue the food cell's terminal state twice and over-count; the Java version enqueues each terminal state once. |
| `find_missing_java.py` | Only reads LC numbers up to 4000, so 4xxx contest problems are matched by url only. |
| V0 ordering, LC 1289 / 1563 | The Python's slower approach was kept as `V0` and the plainer/faster one filed as `V1`, contrary to the skill's "most standard first" — swap if preferred. |
| Unverified signatures | Some premium/contest problems use a method signature inferred from the Python file (e.g. LC 2031, 4054, 4079); check against LeetCode when practising them. |

# Patterns — cue, invariant, target, classic bug

One row per pattern. The **invariant** column is the teaching payload: a candidate who can
state it can rebuild the code from scratch; one who cannot has memorised a template and will
lose it under pressure.

`n` = input size unless the row says otherwise.

## Arrays and strings

| Pattern | Recognition cue | Invariant | Target | Classic bug |
|---|---|---|---|---|
| **Hash map lookup** | "find the pair / has it been seen" | the map holds everything left of `i` | O(n) / O(n) | inserting before looking up, so an element pairs with itself |
| **Two pointers, opposite** | sorted array, pair with a target sum | the answer, if any, lies within `[lo, hi]` | O(n) / O(1) | forgetting to skip duplicates → repeated triplets |
| **Two pointers, same direction** | "remove / partition in place" | everything left of `write` is finished | O(n) / O(1) | advancing `write` when nothing was written |
| **Sliding window, fixed** | "every subarray of size k" | the window is exactly `[i-k+1, i]` | O(n) / O(1) | starting to emit before the window is full |
| **Sliding window, variable** | "longest / shortest subarray such that…" | the window is always valid after the inner `while` | O(n) / O(k) | shrinking with `if` instead of `while` |
| **Prefix sum** | repeated range sums, "subarrays summing to k" | `pre[i]` = sum of the first `i` elements | O(n) / O(n) | off-by-one between `pre[i]` and `pre[i+1]`; forgetting the seed `{0: 1}` |
| **Interval merge** | overlapping ranges | sort by start; the last kept interval is the only one that can extend | O(n log n) / O(n) | treating touching intervals (`[1,2],[2,3]`) inconsistently |
| **Cyclic sort / index-as-hash** | values are `1..n`, "find the missing one" | after the swap loop, `nums[i] == i+1` where possible | O(n) / O(1) | `if` instead of `while` around the swap |

## Search

| Pattern | Recognition cue | Invariant | Target | Classic bug |
|---|---|---|---|---|
| **Binary search, exact** | sorted, find a value | the target, if present, is in `[lo, hi]` | O(log n) | `(lo+hi)/2` overflow in Java; `lo <= hi` vs `lo < hi` mismatched with the update |
| **Binary search, boundary** | "first / last index that…" | everything `< lo` fails the predicate | O(log n) | returning `mid` instead of `lo`; an infinite loop when `hi = mid` meets `mid = (lo+hi)/2` on a 2-element range |
| **Binary search on the answer** | "minimum capacity / speed such that…" | `feasible()` is monotone in the answer | O(n log range) | a `feasible` that is not actually monotone |

## Stacks and queues

| Pattern | Recognition cue | Invariant | Target | Classic bug |
|---|---|---|---|---|
| **Monotonic stack** | "next greater / smaller", histogram spans | the stack is monotone; each index is pushed and popped once | O(n) / O(n) | storing values when the answer needs the *distance*, so indices are required |
| **Monotonic deque** | max/min of a sliding window | indices in the window, values monotone; front is the extremum | O(n) / O(k) | evicting by value rather than by index leaving the window |
| **Two stacks / stack + min** | "O(1) min alongside push/pop" | the aux stack's top is the min of everything below | O(1) amortised | pushing to the aux stack only on a strict `<`, breaking on duplicates |

## Trees and graphs

| Pattern | Recognition cue | Invariant | Target | Classic bug |
|---|---|---|---|---|
| **DFS recursion** | "path / subtree property" | the return value summarises the subtree completely | O(n) / O(h) | conflating "best through this node" with "best to return upward" |
| **BFS level order** | shortest path, "level by level" | the queue holds exactly one frontier per outer iteration | O(V+E) | not snapshotting `len(queue)` before the inner loop |
| **BFS multi-source** | "rotting oranges", nearest of several sources | all sources are seeded at distance 0 | O(V+E) | marking visited on dequeue, so a node enters the queue many times |
| **Topological sort** | "order with prerequisites" | every node in the queue has in-degree 0 | O(V+E) | not checking that all `V` nodes came out → a cycle went unreported |
| **Union-find** | connectivity, "number of components" | `find` returns the component root | ~O(α(n)) | union without rank/size, or forgetting path compression |
| **Dijkstra** | weighted shortest path, non-negative | popping a node fixes its distance forever | O(E log V) | not skipping a stale heap entry with an outdated distance |
| **Backtracking** | "all permutations / combinations / subsets" | the state at depth `d` is a valid partial answer | O(branch^depth) | appending the mutable path itself instead of a copy |

## Dynamic programming

| Pattern | Recognition cue | Invariant | Target | Classic bug |
|---|---|---|---|---|
| **1-D DP** | "ways / min cost to reach i" | `dp[i]` is the answer for the prefix ending at `i` | O(n) | a base case that does not match the recurrence's first real step |
| **2-D grid DP** | paths, edit distance, LCS | `dp[i][j]` is the answer for the two prefixes | O(nm) | the first row/column initialised as if it had a predecessor |
| **Knapsack** | pick items under a capacity | `dp[c]` uses each item at most once (0/1) or freely (unbounded) | O(n·C) | iterating capacity ascending for 0/1, which silently reuses an item |
| **Interval DP** | "burst / merge, order matters" | `dp[i][j]` is the answer for the closed interval | O(n³) | looping by `i,j` rather than by increasing length |
| **DP on state** | stocks with cooldown, "at most k transactions" | one dp array per machine state | O(n·k) | updating states in an order where one read sees this step's write |

## Heaps and selection

| Pattern | Recognition cue | Invariant | Target | Classic bug |
|---|---|---|---|---|
| **Size-k heap** | "top k / k-th largest" | the heap holds the best `k` seen; its root is the weakest of those | O(n log k) | using a max-heap for "k largest" — it needs a **min**-heap of size k |
| **Two heaps** | "running median" | max-heap `lo` holds the smaller half; sizes differ by ≤ 1 | O(log n) per op | rebalancing sizes without moving the element across first |
| **k-way merge** | merge k sorted lists/streams | the heap holds one live head per list | O(n log k) | pushing whole lists rather than heads |

---

## Choosing under pressure

When the pattern will not come, ask these three, in order — they resolve most mediums:

1. **What is repeated?** Name the recomputation out loud. It picks the structure for you
   (see the bottleneck table in `SKILL.md`).
2. **What does the answer depend on?** A prefix → DP or prefix sums. A window → two pointers.
   A neighbour relation → stack or graph. An ordering → sort or heap.
3. **Is the search space monotone?** If "feasible at x" implies "feasible at x+1", binary
   search on the answer, and the hard part becomes writing `feasible()`.

If none land in 3 minutes: **code the brute force**. A correct O(n²) with a stated plan to
improve it beats a blank screen, and writing it often exposes the repetition in step 1.

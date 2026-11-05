# LeetCode Pattern Guide

> **Scope** — The front door: from a problem statement to the card that solves it — the signal in the wording, the constraint cue, the pattern, a link to its card, and the look-alike that is not it. A router only: every template lives in the card a row links to.
> **See also**: [complexity_cheatsheet.md § 6](./complexity_cheatsheet.md#6-interview-decision-guide) — the constraint ruler the "constraint cue" column reads from; [lc_category.md](./lc_category.md) — the wisdompeak taxonomy, and which sheet covers each of its categories.

---

## How to use this page

1. **Read `n` first.** The constraints usually name the complexity before the statement names the pattern: `n ≤ 20` invites O(2ⁿ), `n ≤ 10⁵` invites O(n log n) or O(n), and a value range of 10⁹ invites a binary search on the answer. The full ruler is [complexity_cheatsheet.md § 6](./complexity_cheatsheet.md#6-interview-decision-guide).
2. **Find the row whose signal matches the wording**, in the family table below. The signal is the phrase in the statement, not the topic name.
3. **Open the card, then read the last column before you code.** "Looks like it, but is not" is the mistake the row exists to stop: the problem that matches the signal and still wants a different card.

---

## Recognition — signal → pattern → card ⭐⭐⭐⭐⭐

One row per pattern. The **Card** column links to the template that solves the anchor problem; the problems themselves live in that sheet's examples.

### Arrays & strings

| Signal in the statement | Constraint cue | Pattern | Card | Looks like it, but is not |
|---|---|---|---|---|
| "longest / shortest contiguous subarray or substring", "at most k distinct" | n ≤ 10⁵ | variable sliding window | [sliding_window § Template 3](./sliding_window.md#template-3-longest-window-satisfying-p--lc-3-) (longest), [Template 4](./sliding_window.md#template-4-shortest-window-satisfying-p--lc-209-) (shortest) | an exact sum, `sum == k`, where values can be negative → prefix sum + hash map (LC 560) |
| "smallest substring containing all of t", anagram / permutation of t in s | n ≤ 10⁵, alphabet 26–128 | char-count window | [sliding_window § Template 5](./sliding_window.md#template-5-char-count-window-with-the-haveneed-counter--lc-76-) | a subsequence, not a substring → two pointers (LC 392) |
| "number of subarrays with exactly k …" | n ≤ 10⁵ | exactly k = at most k − at most (k−1) | [sliding_window § Template 6](./sliding_window.md#template-6-exactly-k-via-at-most-subtraction--lc-992-) | the "exactly k" is a **sum** (`sum == k`) and values can be negative → prefix sum + hash map; an exact count of *distinct* values stays here, since signs do not matter to a counter |
| "count subarrays whose sum is k / divisible by k" | n ≤ 10⁵, negatives allowed | prefix sum + count map | [prefix_sum § Template 2](./prefix_sum.md#template-2-hashmap--prefix-sum-subarray-target-sum--lc-560) (sum), [Template 3](./prefix_sum.md#template-3-modulo-prefix-sum-divisibility-problems--lc-974) (divisible: count equal remainders) | all values positive and you want the longest / shortest → sliding window |
| "many range-sum queries" | q ≤ 10⁵ | prefix sum array | [prefix_sum § Template 1](./prefix_sum.md#template-1-basic-prefix-sum-range-queries--lc-303) | the array changes between queries → Fenwick / segment tree (LC 307) |
| "add v to every element in [l, r]", many times | n, updates ≤ 10⁵ | difference array | [difference_array § Template 1](./difference_array.md#template-1-basic-difference-array) | you must read a max after each update → segment tree with lazy propagation |
| "pair / triplet with sum target" on a **sorted** array, "container", "palindrome check" | n ≤ 10⁵ | converging two pointers | [2_pointers § Template 2](./2_pointers.md#template-2-converging-bidirectional-pointers-) | unsorted and you need indices → hash map (LC 1) |
| "remove / deduplicate in place", "move zeros" | O(1) extra space | read/write two pointers | [2_pointers § Template 1](./2_pointers.md#template-1-fastslow-read-write-compaction--lc-26-lc-27-) | order does not matter → swap with the last element |
| "sort 0s, 1s and 2s in one pass" | O(1) space | three-way partition | [2_pointers § Template 6](./2_pointers.md#template-6-three-way-partition-dutch-national-flag--lc-75-) | more than three keys → counting sort |
| "longest palindromic substring", "count palindromic substrings" | n ≤ 1,000 | expand from centre | [2_pointers § Template 3](./2_pointers.md#template-3-expand-from-centre--lc-5-lc-647-) | palindromic **subsequence** → interval DP (LC 516) |
| "two sum" on an unsorted array, "seen before?" | n ≤ 10⁵ | complement lookup | [hash_map § Template 2](./hash_map.md#template-2-seen-before-index-map-two-sum-shape) | sorted input and O(1) space wanted → two pointers (LC 167) |
| "group anagrams", "count frequencies", "first unique" | n ≤ 10⁴ | grouping / counting by a computed key | [hash_map § Template 3](./hash_map.md#template-3-grouping-by-a-computed-key), [Template 1](./hash_map.md#template-1-frequency-counter) | top-k by frequency → heap or bucket sort (LC 347) |
| "maximum subarray sum / product" | n ≤ 10⁵ | Kadane: best run ending here | [kadane_algorithm § 0-2](./kadane_algorithm.md#0-2-algorithm-pattern--template) (sum), [§ 1-3](./kadane_algorithm.md#1-3-maximum-product-subarray-lc-152) (product: keep the min as well as the max) | a subarray of length ≥ k, or a sum = k → prefix sums |
| "merge / insert intervals", "minimum rooms", "remove fewest to stop overlap" | n ≤ 10⁴ | sort by start (merge) or by end (schedule) | [intervals § Pattern 1](./intervals.md#pattern-1-interval-merging--lc-56-), [Pattern 2](./intervals.md#pattern-2-interval-scheduling-greedy--lc-435-) | many overlapping events at a time → sweep line (LC 253, [scanning_line](./scanning_line.md#template-1-basic-interval-overlap--lc-253)) |
| "kth largest / smallest", "k closest" | n ≤ 10⁵, one shot | quickselect | [2_pointers_quickselect § Template 1](./2_pointers_quickselect.md#template-1-kth-largest-element--lc-215-) | a stream, or you need all k in order → size-k heap |

### Binary search

| Signal in the statement | Constraint cue | Pattern | Card | Looks like it, but is not |
|---|---|---|---|---|
| "sorted array", "find target / first position / insert position" | n ≤ 10⁵, O(log n) asked | classic binary search (exact match), lower / upper bound (first position, insert position) | [binary_search § 2.1](./binary_search.md#21-standard-binary-search-template--lc-704) (exact), [§ 1.3](./binary_search.md#13-find-boundaries--lower-and-upper-bound-lc-34-) (bounds) | unsorted but you need a count → sort first, or a hash map |
| "minimum capacity / speed / largest-sum so that it fits in k", "maximize the minimum distance" | values up to 10⁹ | binary search on the answer | [binary_search_on_answer § The Unified Template](./binary_search_on_answer.md#the-unified-template) | feasibility flips more than once as the answer grows (not a single true → false or false → true switch) → DP or greedy |
| "rotated sorted array" | n ≤ 5,000, O(log n) asked | binary search, one half is always sorted | [binary_search](./binary_search.md) | duplicates allowed → worst case O(n) (LC 81) |

### Linked list

| Signal in the statement | Constraint cue | Pattern | Card | Looks like it, but is not |
|---|---|---|---|---|
| "reverse the list / a sublist / k-group", "reorder" | O(1) space | pointer reversal, dummy head | [linked_list § 0-2](./linked_list.md#0-2-pattern) | you may use O(n) space → push to a list, rebuild |
| "cycle", "start of the cycle", "middle node", "kth from the end" | O(1) space | fast / slow pointers | [2_pointers § Template 7](./2_pointers.md#template-7-fastslow-cycle-detection--lc-141-lc-142-) | duplicates in an array of 1..n → the same trick on indices (LC 287) |
| "merge k sorted lists" | k ≤ 10⁴ | k-way merge with a heap | [heap § Template 2](./heap.md#template-2-k-way-merge-pattern--lc-23) | only two lists → a plain merge with a dummy head (LC 21) |

### Trees

| Signal in the statement | Constraint cue | Pattern | Card | Looks like it, but is not |
|---|---|---|---|---|
| "level by level", "right side view", "zigzag", "minimum depth" | nodes ≤ 10⁴ | BFS with a level loop | [bfs § Template 1](./bfs.md#template-1-level-order-bfs--lc-102-) | a value aggregated up from the leaves → post-order DFS |
| "diameter", "balanced?", "max path sum", anything a node computes from its children | recursion depth = height | post-order DFS returning a value | [dfs § Template 6](./dfs.md#template-6-bottom-up-post-order-dfs--lc-543-) | the answer depends on the path from the root → pass it down as an argument (LC 112) |
| "root-to-leaf path with sum", "all paths" | nodes ≤ 5,000 | pre-order DFS carrying the path | [tree_lca_distance § 4](./tree_lca_distance.md#template-4-root-to-leaf-path-with-backtracking--lc-113) | any downward path, not only from the root → prefix sum on the path (LC 437) |
| "lowest common ancestor" | nodes ≤ 10⁵ | LCA by returned presence | [tree_lca_distance § 1](./tree_lca_distance.md#template-1-lca-by-returned-presence--lc-236) | it is a **BST** → walk down by value, O(h) (LC 235) |
| "valid BST", "kth smallest in a BST", "BST iterator" | nodes ≤ 10⁴ | in-order is sorted | [bst § Template 4](./bst.md#template-4-validate-with-bounds--lc-98), [Template 5](./bst.md#template-5-in-order-walk-is-sorted--lc-230) | a plain binary tree → no ordering to lean on |
| "build the tree from preorder and inorder" | nodes ≤ 3,000 | divide and conquer with an index map | [binary_tree § Template 3](./binary_tree.md#template-3-build-from-preorder--inorder--lc-105) | serialize / deserialize → pre-order with null markers ([tree_codec](./tree_codec.md)) |
| "prefix search", "word dictionary with `.`", "many words in a grid" | words ≤ 3·10⁴ | trie | [trie](./trie.md#templates--algorithms) | one word at a time against a set → a hash set is enough |

### Graphs

| Signal in the statement | Constraint cue | Pattern | Card | Looks like it, but is not |
|---|---|---|---|---|
| "number of islands", "connected regions", "flood fill" | grid ≤ 300 × 300 | grid DFS / BFS, mark visited | [dfs § Template 2](./dfs.md#template-2-graph--grid-dfs-flood-fill--lc-200-) | edges arrive one at a time and you query connectivity → union-find |
| "shortest path / minimum steps", every move costs 1 | V + E ≤ 10⁵ | BFS (mark when enqueued) | [bfs § Pattern 3](./bfs.md#pattern-3-graph-bfs-with-visited-set--lc-200) | moves have different costs → Dijkstra; costs are 0 or 1 → [0-1 BFS](./shortest_path_comparison.md#variant-0-1-bfs) |
| "rotting oranges", "distance to the nearest 0 / gate" | grid ≤ 10⁵ cells | multi-source BFS | [bfs § Pattern 4](./bfs.md#pattern-4-multi-source-bfs-distance-calculation--lc-542--lc-994-) | the distance from one source only → plain BFS |
| "weighted shortest path", "network delay", "minimum effort path" | E ≤ 10⁵, weights ≥ 0 | Dijkstra | [Dijkstra § Template 1](./Dijkstra.md#template-1-basic-dijkstra) (additive cost), [§ LC 1631](./Dijkstra.md#1-is-the-cost-additive-and-can-you-order-the-cells--lc-64-vs-lc-1631-) (effort: the max edge on the path, not the sum) | negative weights or "at most k stops" → [Bellman-Ford](./Bellman-Ford.md) (LC 787); all pairs with n ≤ 400 → [Floyd-Warshall](./Floyd-Warshall.md) |
| "course schedule", "order of tasks with prerequisites", "alien dictionary" | V + E ≤ 10⁵ | topological sort (Kahn's BFS) | [topology_sorting § Template 1](./topology_sorting.md#template-1-bfs-kahns-algorithm-) | an undirected graph → there is no order; check for a cycle with union-find |
| "redundant connection", "number of provinces", "accounts merge" | n ≤ 10⁵ | union-find | [union_find § 0-3](./union_find.md#0-3-algorithm-pattern--template-) | you also need path lengths → BFS / DFS |
| "can it be split into two groups", "is it bipartite" | n ≤ 100 | BFS 2-colouring | [graph § Template 6](./graph.md#template-6-bipartite-check-2-coloring--lc-785-) | three or more groups → backtracking colouring |
| "evaluate division", ratio queries | V ≤ 100 | DFS over weighted edges, multiplying along the path | [dfs § Template 10](./dfs.md#template-10-weighted-graph-dfs-divisionratio-queries--lc-399) | many queries on a fixed graph → union-find with weights |

### Dynamic programming

| Signal in the statement | Constraint cue | Pattern | Card | Looks like it, but is not |
|---|---|---|---|---|
| "how many ways to climb / decode", "rob houses that are not adjacent" | n ≤ 10⁵ | 1-D DP, rolling variables | [dp § Template 6](./dp.md#template-6-memoization--tabulation--rolling-variables---lc-198) | choices form a tree with no overlap → plain recursion or backtracking |
| "can the string be segmented into words" | n ≤ 300 | prefix partition DP | [dp § Template 1b](./dp.md#template-1b-prefix-partition-dp---lc-139) | you must list every segmentation → backtracking with memo (LC 140) |
| "unique paths", "minimum path sum" in a grid | m, n ≤ 200 | 2-D grid DP | [dp § Template 2](./dp.md#template-2-2-d-grid-dp---lc-64) | the path can go in any direction → shortest path (Dijkstra / BFS) |
| "subset with sum target", "partition into equal sums", "target sum" | n ≤ 200, sum ≤ 10⁴ | 0/1 knapsack | [dp § Template 4](./dp.md#template-4-01-knapsack---lc-416) | items can be reused (coin change, LC 322 / 518) → unbounded knapsack, loop order flips ([dp_loop_order](./dp_loop_order.md)) |
| "longest increasing subsequence", "Russian dolls" | n ≤ 10⁵ | LIS: O(n²) DP, or O(n log n) with tails (the one n ≤ 10⁵ needs) | [dp § Template 7](./dp.md#template-7-longest-increasing-subsequence---lc-300) (O(n²)), [patience_sorting § 1-1](./patience_sorting.md#1-1-basic-op--the-tails-scan-) (O(n log n)) | contiguous → sliding window or Kadane |
| "longest common subsequence", "edit distance", "interleaving" | m, n ≤ 1,000 | 2-D string DP over prefixes | [dp_string § LCS deep dive](./dp_string.md#deep-dive-the-prefix-based-indexing-pattern-lcs--variants--) | a common **substring** → reset to 0 on a mismatch |
| "buy and sell stock with cooldown / fee / k transactions" | n ≤ 10⁵ | state machine DP | [dp § Template 5](./dp.md#template-5-state-machine-dp--lc-121--lc-309) | one transaction → track the running minimum (LC 121) |
| "burst balloons", "minimum cost to merge stones" | n ≤ 300 | interval DP | [dp § Template 3](./dp.md#template-3-interval-dp--lc-312) | the order of operations does not matter → greedy |
| "assign n ≤ 16 items to k groups", "visit all nodes" | n ≤ 16–20 | bitmask DP over the used set; with a current node, BFS over (node, mask) | [bit_manipulation § 2-3](./bit_manipulation.md#2-3-fill-buckets-one-at-a-time-bitmask-dp--lc-698-) (assign), [shortest_path_comparison § LC 847](./shortest_path_comparison.md#variant-1--bfs-over-node-bitmask--lc-847) (visit all) | n in the thousands → it is not bitmask; look for a greedy or a DP on values |

### Backtracking

| Signal in the statement | Constraint cue | Pattern | Card | Looks like it, but is not |
|---|---|---|---|---|
| "all subsets", "all combinations of k" | n ≤ 10–20 | backtracking with `start_idx` | [backtrack § Template 3](./backtrack.md#template-3-subsets--lc-78-), [Template 6](./backtrack.md#template-6-combinations--lc-77) | only the **count** is asked → DP |
| "all permutations" | n ≤ 8 | backtracking with `used[]` | [backtrack § Template 5](./backtrack.md#template-5-permutations--lc-46-) | the next permutation only → a two-pass scan (LC 31) |
| "combinations summing to target", reuse allowed or not | candidates ≤ 30 | pass `i` or `i + 1` | [backtrack § Template 7](./backtrack.md#template-7-combination-sum--lc-39--lc-40-) | order matters and you only count → DP (LC 377) |
| "word search in a grid", "N-Queens", "sudoku" | board ≤ 9 × 9 or m·n ≤ 36 | backtracking on a board | [backtrack § Template 9](./backtrack.md#template-9-grid--word-search--lc-79-), [Template 10](./backtrack.md#template-10-n-queens--lc-51) | many words in one grid → trie + backtracking (LC 212) |
| "partition the string into palindromes" | n ≤ 16 | backtracking over cut points | [backtrack § Template 8](./backtrack.md#template-8-palindrome-partitioning--lc-131-) | the **minimum** number of cuts → DP (LC 132) |

### Heaps and greedy

| Signal in the statement | Constraint cue | Pattern | Card | Looks like it, but is not |
|---|---|---|---|---|
| "top k frequent", "k closest", "kth largest in a stream" | n ≤ 10⁵ | size-k heap | [heap § Template 1](./heap.md#template-1-top-k-elements-pattern--lc-215) | one shot over a fixed array → quickselect, O(n) average |
| "median of a data stream" | 5 · 10⁴ calls | two heaps | [heap § Template 3](./heap.md#template-3-two-heaps-pattern-median-finding--lc-295) | a sliding window median → also delete from the window ([monotonic_queue § Template 4](./monotonic_queue.md#template-4-sliding-window-median--ordered-multiset--two-heaps-lc-480-)) |
| "task scheduler with cooldown", "reorganize the string so no two are adjacent" | n ≤ 10⁴, 26 kinds | greedy on the most frequent | [greedy § Template 2](./greedy.md#template-2-cooldown-windows-with-a-max-heap--lc-621-), [Template 5](./greedy.md#template-5-string-reorganization--lc-767-) | the order of the input must be kept → a queue simulation |
| "can you reach the end", "minimum jumps" | n ≤ 10⁴ | greedy farthest reach | [greedy § Template 4](./greedy.md#template-4-jump-game-pattern--lc-55-) | each jump has a cost → DP or Dijkstra |

### Stacks and design

| Signal in the statement | Constraint cue | Pattern | Card | Looks like it, but is not |
|---|---|---|---|---|
| "valid parentheses", "decode string", "simplify path" | n ≤ 10⁴ | stack of open items | [stack § Template 2](./stack.md#template-2-bracket-matching--lc-20-) (brackets), [stack_expression_parsing § 3](./stack_expression_parsing.md#3-decode-string--lc-394-) (decode string), [stack § Template 6](./stack.md#template-6-scope--context-ledger--lc-388-lc-636-) (simplify path) | only one bracket type → a counter is enough |
| "next greater / warmer day", "stock span" | n ≤ 10⁵ | monotonic stack | [stack § Template 3](./stack.md#template-3-monotonic-stack--next-greater--smaller--lc-739-) | max of every window of size k → monotonic deque (LC 239) |
| "largest rectangle in histogram", "maximal rectangle" | n ≤ 10⁵ | monotonic stack, left and right bounds | [monotonic_stack § Template 3](./monotonic_stack.md#template-3-largest-rectangle-in-histogram--lc-84) | trapped water → two pointers is simpler (LC 42) |
| "max / min of every window of size k" | n ≤ 10⁵ | monotonic deque | [monotonic_queue § Template 1](./monotonic_queue.md#template-1-sliding-window-maximum-decreasing-deque--lc-239) | a sum, not a max → a plain sliding window |
| "min stack", "LRU cache", "O(1) get and put" | O(1) per op | one structure per query, kept in step | [stack § Template 4](./stack.md#template-4-min-stack--o1-getmin--lc-155-), [design § 0-2](./design.md#0-2-pattern) | eviction by frequency, not recency → LFU, two maps (LC 460) |
| "range sum with point updates" | n, q ≤ 3 · 10⁴ | Fenwick tree / segment tree | [segment_tree § Template 1](./segment_tree.md#template-1-binary-indexed-tree-fenwick-tree) | no updates → a prefix sum array |
| "single number", "count bits", "subsets as masks" | values fit in 32 bits | bit tricks | [bit_manipulation § 1-2](./bit_manipulation.md#1-2-single-bit-tricks-with-code) | values unbounded → a hash map |

---

## Before you pick a pattern — the six-step loop

> Picking the right pattern is step 3 of six. The five around it are what an interviewer is
> actually grading, and they are the same for every question on this page. Read this once
> before your first mock, not during revision.

1. **Understand the question**: requirement, input and output, the accepted form of the answer.
2. **Ask about scope and limits**: the scale of the data and the range of the numbers (this is the constraint cue above), the boundaries, the expected time and space.
3. **Come up with approaches**: say a brute force out loud, then a better one with its algorithm and data structure. Write pseudocode, name the edge cases, and agree on it before coding.
4. **Write the code**: clean, with names that say what they hold, and with test cases written down.
5. **Run it**: walk the tests, fix bugs, state the time and space complexity and the line that sets each.
6. **Follow-ups**: a better bound; input that scales, arrives as a stream, or does not fit in memory.

---

## Drill Path

One anchor problem per family, so a first pass touches every family above. ⭐ marks the [L3 core set](https://github.com/yennanliu/CS_basics/blob/master/data/l3_core.json). Each list runs easy → hard.

- **Core ⭐**: LC 1 Two Sum · LC 20 Valid Parentheses · LC 206 Reverse Linked List · LC 121 Best Time to Buy and Sell Stock · LC 3 Longest Substring Without Repeating Characters · LC 102 Level Order Traversal · LC 200 Number of Islands · LC 322 Coin Change · LC 207 Course Schedule · LC 78 Subsets · LC 347 Top K Frequent Elements · LC 739 Daily Temperatures · LC 875 Koko Eating Bananas · LC 76 Minimum Window Substring
- **Practice**: LC 167 Two Sum II · LC 560 Subarray Sum Equals K · LC 236 Lowest Common Ancestor · LC 994 Rotting Oranges · LC 743 Network Delay Time · LC 416 Partition Equal Subset Sum · LC 1143 Longest Common Subsequence · LC 295 Find Median from Data Stream
- **Stretch**: LC 787 Cheapest Flights Within K Stops · LC 239 Sliding Window Maximum · LC 84 Largest Rectangle in Histogram · LC 212 Word Search II · LC 312 Burst Balloons

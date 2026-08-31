# Google SWE — Recently Asked Interview Questions (scraped)

> **Generated**: 2026-08-11  
> **Sources**: LeetCode Discuss, Reddit, Blind, Hacker News  
> **Regenerate**: `python3 script/scrape_lc_discuss_company.py --tag google --sources leetcode,reddit,blind,hn`  
> **Corpus**: 1719 posts, 2016-11-07 → 2026-08-11

| Source | Posts | Range | What is scraped |
|--------|-------|-------|-----------------|
| LeetCode Discuss | 274 | 2026-01-23 → 2026-08-08 | threads + bodies + comments |
| Reddit | 851 | 2018-09-18 → 2026-08-11 | posts (full selftext) + rationed comment threads |
| Blind | 62 | 2017-09-24 → 2026-08-10 | search cards + full post bodies (**no comments**) |
| Hacker News | 532 | 2016-11-07 → 2026-08-04 | stories + comments |

## ⚠️ Read this first — what this data is and is not

- LeetCode's **official company tag list** (`companyTag`) is **Premium-gated** and returns `null` for anonymous requests. This doc is **not** that list.
- Everything here is **user-reported interview experience** from public forums. It is self-reported, unverified, and skewed toward whoever bothers to post.
- Sources differ in signal. LeetCode Discuss and Reddit posters cite problems by number or link; Blind and Hacker News posters mostly do not, so those two contribute breadth (and noise) rather than precise references.
- The **legacy** LeetCode discuss API (`categoryTopicList`, category `interview-question`) is frozen at **2025-03-04** — LeetCode migrated Discuss during 2025. Anything claiming to scrape "recent" questions from that endpoint is serving stale data.
- Treat problem counts as **weak signal** (mention frequency), not ground-truth interview frequency. A single well-linked compilation post can put a dozen problems on the board at once.
- Mentions are **not all interview reports** — some describe a practice routine, and a title match can even land inside a sentence saying the problem is *not* what was asked. The `Match` column and the quotes exist so you can check.

## 1) Most-referenced LC problems

**`Posts`** = number of **distinct threads** referencing the problem anywhere in `title + body + comments`. It counts threads, not mentions: a thread naming the same problem five times counts once, so `Posts` is *not* the sum of the quotes below.

`Where` = which sources the thread(s) came from. `Match` = how the reference was found, showing the **strongest** evidence anywhere in that thread set. **url** = the post linked `leetcode.com/problems/<slug>` (high confidence); **num** = wrote `LC 200` / `#200`; **title** = the exact title appeared in prose — weakest, worth eyeballing the quote before trusting it.

The table below is **complete** — every problem extracted from the corpus is listed.

| # | Problem | Diff | Type / Tags | Posts | Where | Match | Last seen | In repo? |
|---|---------|------|-------------|-------|-------|-------|-----------|----------|
| 200 | [Number of Islands](https://leetcode.com/problems/number-of-islands/) | Medium | Array, Depth-First Search, Breadth-First Search | 6 | leetcode, reddit | url | 2026-06-02 | ✅ |
| 56 | [Merge Intervals](https://leetcode.com/problems/merge-intervals/) | Medium | Array, Sorting | 3 | leetcode, reddit | title | 2026-06-02 | ✅ |
| 207 | [Course Schedule](https://leetcode.com/problems/course-schedule/) | Medium | Depth-First Search, Breadth-First Search, Graph | 2 | leetcode | url | 2026-05-18 | ✅ |
| 253 | [Meeting Rooms II](https://leetcode.com/problems/meeting-rooms-ii/) 🔒 | Medium | Array, Two Pointers, Greedy | 2 | leetcode | url | 2026-05-11 | ✅ |
| 1944 | [Number of Visible People in a Queue](https://leetcode.com/problems/number-of-visible-people-in-a-queue/) | Hard | array, stack, monotonic-stack | 2 | leetcode | url | 2026-05-11 | ✅ |
| 23 | [Merge k Sorted Lists](https://leetcode.com/problems/merge-k-sorted-lists/) | Hard | Linked List, Divide and Conquer, Heap (Priority Queue) | 2 | leetcode | url | 2026-05-11 | ✅ |
| 300 | [Longest Increasing Subsequence](https://leetcode.com/problems/longest-increasing-subsequence/) | Medium | Array, Binary Search, Dynamic Programming | 2 | leetcode | url | 2026-05-11 | ✅ |
| 3 | [Longest Substring Without Repeating Characters](https://leetcode.com/problems/longest-substring-without-repeating-characters/) | Medium | Hash Table, String, Sliding Window | 2 | leetcode, reddit | url | 2026-08-03 | ✅ |
| 286 | [Walls and Gates](https://leetcode.com/problems/walls-and-gates/) 🔒 | Medium | Array, Breadth-First Search, Matrix | 2 | leetcode, reddit | title | 2026-04-12 | ✅ |
| 994 | [Rotting Oranges](https://leetcode.com/problems/rotting-oranges/) | Medium | Array, Breadth-First Search, Matrix | 2 | leetcode, reddit | title | 2026-06-02 | ✅ |
| 210 | [Course Schedule II](https://leetcode.com/problems/course-schedule-ii/) | Medium | Depth-First Search, Breadth-First Search, Graph | 2 | leetcode | url | 2026-05-18 | ✅ |
| 269 | [Alien Dictionary](https://leetcode.com/problems/alien-dictionary/) 🔒 | Hard | Array, String, Depth-First Search | 2 | hn | title | 2025-11-30 | ✅ |
| 208 | [Implement Trie (Prefix Tree)](https://leetcode.com/problems/implement-trie-prefix-tree/) | Medium | Hash Table, String, Design | 1 | reddit | num | 2026-07-06 | ✅ |
| 239 | [Sliding Window Maximum](https://leetcode.com/problems/sliding-window-maximum/) | Hard | Array, Queue, Sliding Window | 1 | leetcode | url | 2026-05-11 | ✅ |
| 354 | [Russian Doll Envelopes](https://leetcode.com/problems/russian-doll-envelopes/) | Hard | Array, Binary Search, Dynamic Programming | 1 | leetcode | url | 2026-05-11 | ✅ |
| 4 | [Median of Two Sorted Arrays](https://leetcode.com/problems/median-of-two-sorted-arrays/) | Hard | Array, Binary Search, Divide and Conquer | 1 | leetcode | url | 2026-05-11 | ✅ |
| 10 | [Regular Expression Matching](https://leetcode.com/problems/regular-expression-matching/) | Hard | String, Dynamic Programming, Recursion | 1 | leetcode | url | 2026-05-11 | ✅ |
| 31 | [Next Permutation](https://leetcode.com/problems/next-permutation/) | Medium | — | 1 | leetcode | url | 2026-05-11 | — |
| 33 | [Search in Rotated Sorted Array](https://leetcode.com/problems/search-in-rotated-sorted-array/) | Medium | Array, Binary Search | 1 | leetcode | url | 2026-05-11 | ✅ |
| 34 | [Find First and Last Position of Element in Sorted Array](https://leetcode.com/problems/find-first-and-last-position-of-element-in-sorted-array/) | Medium | Array, Binary Search | 1 | leetcode | url | 2026-05-11 | ✅ |
| 60 | [Permutation Sequence](https://leetcode.com/problems/permutation-sequence/) | Hard | — | 1 | leetcode | url | 2026-02-10 | — |
| 84 | [Largest Rectangle in Histogram](https://leetcode.com/problems/largest-rectangle-in-histogram/) | Hard | Array, Stack, Monotonic Stack | 1 | leetcode | url | 2026-05-11 | ✅ |
| 85 | [Maximal Rectangle](https://leetcode.com/problems/maximal-rectangle/) | Hard | Array, Dynamic Programming, Stack | 1 | leetcode | url | 2026-05-11 | ✅ |
| 93 | [Restore IP Addresses](https://leetcode.com/problems/restore-ip-addresses/) | Medium | String, Backtracking | 1 | leetcode | url | 2026-02-28 | ✅ |
| 128 | [Longest Consecutive Sequence](https://leetcode.com/problems/longest-consecutive-sequence/) | Medium | Array, Hash Table, Union Find | 1 | leetcode | url | 2026-05-11 | ✅ |
| 410 | [Split Array Largest Sum](https://leetcode.com/problems/split-array-largest-sum/) | Hard | Array, Binary Search, Dynamic Programming | 1 | leetcode | url | 2026-05-11 | ✅ |
| 424 | [Longest Repeating Character Replacement](https://leetcode.com/problems/longest-repeating-character-replacement/) | Medium | hash-table, string, sliding-window | 1 | leetcode | url | 2026-05-11 | ✅ |
| 560 | [Subarray Sum Equals K](https://leetcode.com/problems/subarray-sum-equals-k/) | Medium | Array, Hash Table, Prefix Sum | 1 | leetcode | url | 2026-05-11 | ✅ |
| 875 | [Koko Eating Bananas](https://leetcode.com/problems/koko-eating-bananas/) | Medium | Array, Binary Search | 1 | leetcode | url | 2026-05-11 | ✅ |
| 5 | [Longest Palindromic Substring](https://leetcode.com/problems/longest-palindromic-substring/) | Medium | String, Dynamic Programming | 1 | leetcode | url | 2026-05-11 | ✅ |
| 37 | [Sudoku Solver](https://leetcode.com/problems/sudoku-solver/) | Hard | Array, Backtracking, Matrix | 1 | leetcode | url | 2026-05-11 | ✅ |
| 42 | [Trapping Rain Water](https://leetcode.com/problems/trapping-rain-water/) | Hard | Array, Two Pointers, Dynamic Programming | 1 | leetcode | url | 2026-05-11 | ✅ |
| 48 | [Rotate Image](https://leetcode.com/problems/rotate-image/) | Medium | Array, Math, Matrix | 1 | leetcode | url | 2026-05-11 | ✅ |
| 72 | [Edit Distance](https://leetcode.com/problems/edit-distance/) | Medium | String, Dynamic Programming | 1 | leetcode | url | 2026-05-11 | ✅ |
| 146 | [LRU Cache](https://leetcode.com/problems/lru-cache/) | Medium | Hash Table, Linked List, Design | 1 | leetcode | url | 2026-05-11 | ✅ |
| 351 | [Android Unlock Patterns](https://leetcode.com/problems/android-unlock-patterns/) 🔒 | Medium | dynamic-programming, backtracking, array | 1 | leetcode | url | 2026-02-08 | ✅ |
| 378 | [Kth Smallest Element in a Sorted Matrix](https://leetcode.com/problems/kth-smallest-element-in-a-sorted-matrix/) | Medium | Array, Binary Search, Sorting | 1 | reddit | num | 2026-03-12 | ✅ |
| 394 | [Decode String](https://leetcode.com/problems/decode-string/) | Medium | String, Stack, Recursion | 1 | leetcode | url | 2026-05-11 | ✅ |
| 1970 | [Last Day Where You Can Still Cross](https://leetcode.com/problems/last-day-where-you-can-still-cross/) | Hard | — | 1 | leetcode | url | 2026-02-08 | — |
| 2013 | [Detect Squares](https://leetcode.com/problems/detect-squares/) | Medium | Array, Hash Table, Design | 1 | leetcode | title | 2026-02-07 | ✅ |
| 2402 | [Meeting Rooms III](https://leetcode.com/problems/meeting-rooms-iii/) | Hard | — | 1 | leetcode | url | 2026-03-25 | — |
| 2812 | [Find the Safest Path in a Grid](https://leetcode.com/problems/find-the-safest-path-in-a-grid/) | Medium | — | 1 | leetcode | url | 2026-02-08 | — |
| 3169 | [Count Days Without Meetings](https://leetcode.com/problems/count-days-without-meetings/) | Medium | — | 1 | leetcode | num | 2026-02-04 | — |
| 3671 | [Sum of Beautiful Subsequences](https://leetcode.com/problems/sum-of-beautiful-subsequences/) | Hard | — | 1 | leetcode | url | 2026-02-05 | — |
| 3676 | [Count Bowl Subarrays](https://leetcode.com/problems/count-bowl-subarrays/) | Medium | — | 1 | leetcode | url | 2026-02-05 | — |
| 3677 | [Count Binary Palindromic Numbers](https://leetcode.com/problems/count-binary-palindromic-numbers/) | Hard | — | 1 | leetcode | url | 2026-02-05 | — |
| 3680 | [Generate Schedule](https://leetcode.com/problems/generate-schedule/) | Medium | — | 1 | leetcode | url | 2026-02-05 | — |
| 3681 | [Maximum XOR of Subsequences](https://leetcode.com/problems/maximum-xor-of-subsequences/) | Hard | — | 1 | leetcode | url | 2026-02-05 | — |
| 3685 | [Subsequence Sum After Capping Elements](https://leetcode.com/problems/subsequence-sum-after-capping-elements/) | Medium | — | 1 | leetcode | url | 2026-02-05 | — |
| 3686 | [Number of Stable Subsequences](https://leetcode.com/problems/number-of-stable-subsequences/) | Hard | — | 1 | leetcode | url | 2026-02-05 | — |
| 3690 | [Split and Merge Array Transformation](https://leetcode.com/problems/split-and-merge-array-transformation/) | Medium | — | 1 | leetcode | url | 2026-02-05 | — |
| 3691 | [Maximum Total Subarray Value II](https://leetcode.com/problems/maximum-total-subarray-value-ii/) | Hard | — | 1 | leetcode | url | 2026-02-05 | — |
| 3695 | [Maximize Alternating Sum Using Swaps](https://leetcode.com/problems/maximize-alternating-sum-using-swaps/) | Hard | — | 1 | leetcode | url | 2026-02-05 | — |
| 3699 | [Number of ZigZag Arrays I](https://leetcode.com/problems/number-of-zigzag-arrays-i/) | Hard | — | 1 | leetcode | url | 2026-02-05 | — |
| 3700 | [Number of ZigZag Arrays II](https://leetcode.com/problems/number-of-zigzag-arrays-ii/) | Hard | — | 1 | leetcode | url | 2026-02-05 | — |
| 3703 | [Remove K-Balanced Substrings](https://leetcode.com/problems/remove-k-balanced-substrings/) | Medium | — | 1 | leetcode | url | 2026-02-05 | — |
| 3704 | [Count No-Zero Pairs That Sum to N](https://leetcode.com/problems/count-no-zero-pairs-that-sum-to-n/) | Hard | — | 1 | leetcode | url | 2026-02-05 | — |
| 3709 | [Design Exam Scores Tracker](https://leetcode.com/problems/design-exam-scores-tracker/) | Medium | — | 1 | leetcode | url | 2026-02-05 | — |
| 3710 | [Maximum Partition Factor](https://leetcode.com/problems/maximum-partition-factor/) | Hard | — | 1 | leetcode | url | 2026-02-05 | — |
| 3714 | [Longest Balanced Substring II](https://leetcode.com/problems/longest-balanced-substring-ii/) | Medium | — | 1 | leetcode | url | 2026-02-05 | — |
| 3715 | [Sum of Perfect Square Ancestors](https://leetcode.com/problems/sum-of-perfect-square-ancestors/) | Hard | — | 1 | leetcode | url | 2026-02-05 | — |
| 3720 | [Lexicographically Smallest Permutation Greater Than Target](https://leetcode.com/problems/lexicographically-smallest-permutation-greater-than-target/) | Medium | — | 1 | leetcode | url | 2026-02-05 | — |
| 3721 | [Longest Balanced Subarray II](https://leetcode.com/problems/longest-balanced-subarray-ii/) | Hard | — | 1 | leetcode | url | 2026-02-05 | — |
| 3724 | [Minimum Operations to Transform Array](https://leetcode.com/problems/minimum-operations-to-transform-array/) | Medium | — | 1 | leetcode | url | 2026-02-05 | — |
| 3733 | [Minimum Time to Complete All Deliveries](https://leetcode.com/problems/minimum-time-to-complete-all-deliveries/) | Medium | — | 1 | leetcode | url | 2026-02-05 | — |
| 3738 | [Longest Non-Decreasing Subarray After Replacing at Most One Element](https://leetcode.com/problems/longest-non-decreasing-subarray-after-replacing-at-most-one-element/) | Medium | — | 1 | leetcode | url | 2026-02-05 | — |
| 3739 | [Count Subarrays With Majority Element II](https://leetcode.com/problems/count-subarrays-with-majority-element-ii/) | Hard | — | 1 | leetcode | url | 2026-02-05 | — |
| 3742 | [Maximum Path Score in a Grid](https://leetcode.com/problems/maximum-path-score-in-a-grid/) | Medium | — | 1 | leetcode | url | 2026-02-05 | — |
| 3743 | [Maximize Cyclic Partition Score](https://leetcode.com/problems/maximize-cyclic-partition-score/) | Hard | — | 1 | leetcode | url | 2026-02-05 | — |
| 3747 | [Count Distinct Integers After Removing Zeros](https://leetcode.com/problems/count-distinct-integers-after-removing-zeros/) | Medium | — | 1 | leetcode | url | 2026-02-05 | — |
| 3748 | [Count Stable Subarrays](https://leetcode.com/problems/count-stable-subarrays/) | Hard | — | 1 | leetcode | url | 2026-02-05 | — |
| 3753 | [Total Waviness of Numbers in Range II](https://leetcode.com/problems/total-waviness-of-numbers-in-range-ii/) | Hard | — | 1 | leetcode | url | 2026-02-05 | — |
| 3757 | [Number of Effective Subsequences](https://leetcode.com/problems/number-of-effective-subsequences/) | Hard | — | 1 | leetcode | url | 2026-02-05 | — |
| 3761 | [Minimum Absolute Distance Between Mirror Pairs](https://leetcode.com/problems/minimum-absolute-distance-between-mirror-pairs/) | Medium | — | 1 | leetcode | url | 2026-02-05 | — |
| 3762 | [Minimum Operations to Equalize Subarrays](https://leetcode.com/problems/minimum-operations-to-equalize-subarrays/) | Hard | — | 1 | leetcode | url | 2026-02-05 | — |
| 3767 | [Maximize Points After Choosing K Tasks](https://leetcode.com/problems/maximize-points-after-choosing-k-tasks/) | Medium | — | 1 | leetcode | url | 2026-02-05 | — |
| 3771 | [Total Score of Dungeon Runs](https://leetcode.com/problems/total-score-of-dungeon-runs/) | Medium | — | 1 | leetcode | url | 2026-02-05 | — |
| 3772 | [Maximum Subgraph Score in a Tree](https://leetcode.com/problems/maximum-subgraph-score-in-a-tree/) | Hard | — | 1 | leetcode | url | 2026-02-05 | — |
| 3776 | [Minimum Moves to Balance Circular Array](https://leetcode.com/problems/minimum-moves-to-balance-circular-array/) | Medium | — | 1 | leetcode | url | 2026-02-05 | — |
| 3777 | [Minimum Deletions to Make Alternating Substring](https://leetcode.com/problems/minimum-deletions-to-make-alternating-substring/) | Hard | — | 1 | leetcode | url | 2026-02-05 | — |
| 3781 | [Maximum Score After Binary Swaps](https://leetcode.com/problems/maximum-score-after-binary-swaps/) | Medium | — | 1 | leetcode | url | 2026-02-05 | — |
| 3785 | [Minimum Swaps to Avoid Forbidden Values](https://leetcode.com/problems/minimum-swaps-to-avoid-forbidden-values/) | Hard | — | 1 | leetcode | url | 2026-02-05 | — |
| 3786 | [Total Sum of Interaction Cost in Tree Groups](https://leetcode.com/problems/total-sum-of-interaction-cost-in-tree-groups/) | Hard | — | 1 | leetcode | url | 2026-02-05 | — |
| 3790 | [Smallest All-Ones Multiple](https://leetcode.com/problems/smallest-all-ones-multiple/) | Medium | — | 1 | leetcode | url | 2026-02-05 | — |
| 3791 | [Number of Balanced Integers in a Range](https://leetcode.com/problems/number-of-balanced-integers-in-a-range/) | Hard | — | 1 | leetcode | url | 2026-02-05 | — |
| 3796 | [Find Maximum Value in a Constrained Sequence](https://leetcode.com/problems/find-maximum-value-in-a-constrained-sequence/) | Medium | — | 1 | leetcode | url | 2026-02-05 | — |
| 3797 | [Count Routes to Climb a Rectangular Grid](https://leetcode.com/problems/count-routes-to-climb-a-rectangular-grid/) | Hard | — | 1 | leetcode | url | 2026-02-05 | — |
| 3800 | [Minimum Cost to Make Two Binary Strings Equal](https://leetcode.com/problems/minimum-cost-to-make-two-binary-strings-equal/) | Medium | — | 1 | leetcode | url | 2026-02-05 | — |
| 3801 | [Minimum Cost to Merge Sorted Lists](https://leetcode.com/problems/minimum-cost-to-merge-sorted-lists/) | Hard | — | 1 | leetcode | url | 2026-02-05 | — |
| 3805 | [Count Caesar Cipher Pairs](https://leetcode.com/problems/count-caesar-cipher-pairs/) | Medium | — | 1 | leetcode | url | 2026-02-05 | — |
| 3806 | [Maximum Bitwise AND After Increment Operations](https://leetcode.com/problems/maximum-bitwise-and-after-increment-operations/) | Hard | — | 1 | leetcode | url | 2026-02-05 | — |
| 3811 | [Number of Alternating XOR Partitions](https://leetcode.com/problems/number-of-alternating-xor-partitions/) | Medium | — | 1 | leetcode | url | 2026-02-05 | — |
| 3812 | [Minimum Edge Toggles on a Tree](https://leetcode.com/problems/minimum-edge-toggles-on-a-tree/) | Hard | — | 1 | leetcode | url | 2026-02-05 | — |
| 3815 | [Design Auction System](https://leetcode.com/problems/design-auction-system/) | Medium | — | 1 | leetcode | url | 2026-02-05 | — |
| 3816 | [Lexicographically Smallest String After Deleting Duplicate Characters](https://leetcode.com/problems/lexicographically-smallest-string-after-deleting-duplicate-characters/) | Hard | — | 1 | leetcode | url | 2026-02-05 | — |
| 3820 | [Pythagorean Distance Nodes in a Tree](https://leetcode.com/problems/pythagorean-distance-nodes-in-a-tree/) | Medium | — | 1 | leetcode | url | 2026-02-05 | — |
| 3821 | [Find Nth Smallest Integer With K One Bits](https://leetcode.com/problems/find-nth-smallest-integer-with-k-one-bits/) | Hard | — | 1 | leetcode | url | 2026-02-05 | — |
| 3825 | [Longest Strictly Increasing Subsequence With Non-Zero Bitwise AND](https://leetcode.com/problems/longest-strictly-increasing-subsequence-with-non-zero-bitwise-and/) | Medium | — | 1 | leetcode | url | 2026-02-05 | — |
| 3826 | [Minimum Partition Score](https://leetcode.com/problems/minimum-partition-score/) | Hard | — | 1 | leetcode | url | 2026-02-05 | — |
| 3829 | [Design Ride Sharing System](https://leetcode.com/problems/design-ride-sharing-system/) | Medium | — | 1 | leetcode | url | 2026-02-05 | — |
| 3830 | [Longest Alternating Subarray After Removing At Most One Element](https://leetcode.com/problems/longest-alternating-subarray-after-removing-at-most-one-element/) | Hard | — | 1 | leetcode | url | 2026-02-05 | — |
| 6 | [Zigzag Conversion](https://leetcode.com/problems/zigzag-conversion/) | Medium | String | 1 | reddit | url | 2026-06-27 | ✅ |
| 22 | [Generate Parentheses](https://leetcode.com/problems/generate-parentheses/) | Medium | String, Dynamic Programming, Backtracking | 1 | leetcode | url | 2026-02-28 | ✅ |
| 47 | [Permutations II](https://leetcode.com/problems/permutations-ii/) | Medium | — | 1 | leetcode | num | 2026-02-06 | — |
| 53 | [Maximum Subarray](https://leetcode.com/problems/maximum-subarray/) | Medium | Array, Divide and Conquer, Dynamic Programming | 1 | hn | title | 2024-04-27 | ✅ |
| 68 | [Text Justification](https://leetcode.com/problems/text-justification/) | Hard | Array, String, Simulation | 1 | hn | title | 2021-10-13 | ✅ |
| 118 | [Pascal's Triangle](https://leetcode.com/problems/pascals-triangle/) | Easy | Array, Dynamic Programming | 1 | hn | title | 2025-01-14 | ✅ |
| 206 | [Reverse Linked List](https://leetcode.com/problems/reverse-linked-list/) | Easy | linked-list, recursion | 1 | reddit | title | 2026-08-04 | ✅ |
| 226 | [Invert Binary Tree](https://leetcode.com/problems/invert-binary-tree/) | Easy | Tree, Depth-First Search, Breadth-First Search | 1 | hn | url | 2021-09-07 | ✅ |
| 303 | [Range Sum Query - Immutable](https://leetcode.com/problems/range-sum-query-immutable/) | Easy | Immutable - Array, Design, Prefix Sum | 1 | leetcode | title | 2026-03-04 | ✅ |
| 307 | [Range Sum Query - Mutable](https://leetcode.com/problems/range-sum-query-mutable/) | Medium | Mutable - Array, Design, Binary Indexed Tree | 1 | leetcode | title | 2026-03-04 | ✅ |
| 332 | [Reconstruct Itinerary](https://leetcode.com/problems/reconstruct-itinerary/) | Hard | Depth-First Search, Graph, Eulerian Circuit | 1 | reddit | title | 2026-06-29 | ✅ |
| 338 | [Counting Bits](https://leetcode.com/problems/counting-bits/) | Easy | Dynamic Programming, Bit Manipulation | 1 | reddit | num | 2026-06-29 | ✅ |
| 355 | [Design Twitter](https://leetcode.com/problems/design-twitter/) | Medium | — | 1 | leetcode | title | 2026-02-07 | — |
| 359 | [Logger Rate Limiter](https://leetcode.com/problems/logger-rate-limiter/) 🔒 | Easy | Hash Table, Design | 1 | leetcode | title | 2026-02-26 | ✅ |
| 518 | [Coin Change II](https://leetcode.com/problems/coin-change-ii/) | Medium | Array, Dynamic Programming | 1 | reddit | title | 2026-07-30 | ✅ |
| 753 | [Cracking the Safe](https://leetcode.com/problems/cracking-the-safe/) | Hard | Depth-First Search, Graph, Eulerian Circuit | 1 | blind | num | 2019-07-06 | ✅ |
| 809 | [Expressive Words](https://leetcode.com/problems/expressive-words/) | Medium | Array, Two Pointers, String | 1 | leetcode | url | 2026-04-10 | ✅ |
| 907 | [Sum of Subarray Minimums](https://leetcode.com/problems/sum-of-subarray-minimums/) | Medium | Array, Dynamic Programming, Stack | 1 | leetcode | url | 2026-03-28 | ✅ |
| 1277 | [Count Square Submatrices with All Ones](https://leetcode.com/problems/count-square-submatrices-with-all-ones/) | Medium | array, dynamic-programming, matrix | 1 | leetcode | url | 2026-02-07 | ✅ |
| 1671 | [Minimum Number of Removals to Make Mountain Array](https://leetcode.com/problems/minimum-number-of-removals-to-make-mountain-array/) | Hard | — | 1 | leetcode | url | 2026-02-11 | — |
| 1937 | [Maximum Number of Points with Cost](https://leetcode.com/problems/maximum-number-of-points-with-cost/) | Medium | array, dynamic-programming, stack | 1 | leetcode | url | 2026-02-24 | ✅ |
| 2026 | [Low-Quality Problems](https://leetcode.com/problems/low-quality-problems/) 🔒 | Easy | — | 1 | leetcode | num | 2026-02-07 | — |
| 2093 | [Minimum Cost to Reach City With Discounts](https://leetcode.com/problems/minimum-cost-to-reach-city-with-discounts/) 🔒 | Medium | — | 1 | leetcode | url | 2026-05-22 | — |
| 2254 | [Design Video Sharing Platform](https://leetcode.com/problems/design-video-sharing-platform/) 🔒 | Hard | — | 1 | reddit | title | 2026-03-12 | — |
| 2615 | [Sum of Distances](https://leetcode.com/problems/sum-of-distances/) | Medium | — | 1 | leetcode | title | 2026-03-27 | — |
| 2810 | [Faulty Keyboard](https://leetcode.com/problems/faulty-keyboard/) | Easy | — | 1 | leetcode | title | 2026-04-10 | — |
| 3481 | [Apply Substitutions](https://leetcode.com/problems/apply-substitutions/) 🔒 | Medium | — | 1 | leetcode | title | 2026-05-14 | — |
| 3592 | [Inverse Coin Change](https://leetcode.com/problems/inverse-coin-change/) | Medium | — | 1 | reddit | title | 2026-07-30 | — |
| 3670 | [Maximum Product of Two Integers With No Common Bits](https://leetcode.com/problems/maximum-product-of-two-integers-with-no-common-bits/) | Medium | — | 1 | leetcode | url | 2026-02-05 | — |
| 3694 | [Distinct Points Reachable After Substring Removal](https://leetcode.com/problems/distinct-points-reachable-after-substring-removal/) | Medium | — | 1 | leetcode | url | 2026-02-05 | — |
| 3725 | [Count Ways to Choose Coprime Integers from Rows](https://leetcode.com/problems/count-ways-to-choose-coprime-integers-from-rows/) | Hard | — | 1 | leetcode | url | 2026-02-05 | — |
| 3728 | [Stable Subarrays With Equal Boundary and Interior Sum](https://leetcode.com/problems/stable-subarrays-with-equal-boundary-and-interior-sum/) | Medium | — | 1 | leetcode | url | 2026-02-05 | — |
| 3729 | [Count Distinct Subarrays Divisible by K in Sorted Array](https://leetcode.com/problems/count-distinct-subarrays-divisible-by-k-in-sorted-array/) | Hard | — | 1 | leetcode | url | 2026-02-05 | — |
| 3734 | [Lexicographically Smallest Palindromic Permutation Greater Than Target](https://leetcode.com/problems/lexicographically-smallest-palindromic-permutation-greater-than-target/) | Hard | — | 1 | leetcode | url | 2026-02-05 | — |
| 3752 | [Lexicographically Smallest Negated Permutation that Sums to Target](https://leetcode.com/problems/lexicographically-smallest-negated-permutation-that-sums-to-target/) | Medium | — | 1 | leetcode | url | 2026-02-05 | — |
| 3756 | [Concatenate Non-Zero Digits and Multiply by Sum II](https://leetcode.com/problems/concatenate-non-zero-digits-and-multiply-by-sum-ii/) | Medium | — | 1 | leetcode | url | 2026-02-05 | — |
| 3768 | [Minimum Inversion Count in Subarrays of Fixed Length](https://leetcode.com/problems/minimum-inversion-count-in-subarrays-of-fixed-length/) | Hard | — | 1 | leetcode | url | 2026-02-05 | — |
| 3782 | [Last Remaining Integer After Alternating Deletion Operations](https://leetcode.com/problems/last-remaining-integer-after-alternating-deletion-operations/) | Hard | — | 1 | leetcode | url | 2026-02-05 | — |

### Evidence (quotes from the scraped posts)

**This is a sample, not a full audit trail.** It covers the top 25 problems of 139, with at most 3 quotes each (one per thread, from the first match in that thread). Where a problem has more threads than quotes shown, the surplus is noted inline. For the rest, follow the links in the table and section 2.

**LC 200 — Number of Islands** (Medium) · 6 threads — _3 further threads not quoted_  
- `leetcode` _2026-05-11_ · [Google Phone Interview Questions](https://leetcode.com/discuss/post/8195929/google-phone-interview-questions-by-anon-mk7j/)  
  > …problems/russian-doll-envelopes/) * Number of Islands [#200](https://leetcode.com/problems/number-of-islands/) * Koko Eating Bananas [#875](https://leetcode.com/problems/koko-eating-bananas/) * Nex…
- `leetcode` _2026-04-22_ · [# 🔥 Top Google DSA Questions I Practiced (With Approach)](https://leetcode.com/discuss/post/8054808/top-google-dsa-questions-i-practiced-wit-quup/)  
  > …o Sum → HashMap Subarray Sum = K → Prefix Sum Graph Word Ladder → BFS Number of Islands → Hi everyone 👋 I am currently preparing for product-based companies like Google, and I wanted to share some im…
- `leetcode` _2026-04-12_ · [I have rotting oranges now. 994. Rotten Oranges — finally clicked.](https://leetcode.com/discuss/post/7883464/i-have-rotting-oranges-now-994-rotten-or-t4bw/)  
  > …Rotten Oranges after 3 days of struggling with BFSSpent days stuck on Number of Islands, then this problem hit me again like wtf.But the whole thing clicked with just one line:This line freezes which…

**LC 56 — Merge Intervals** (Medium) · 3 threads  
- `leetcode` _2026-04-11_ · [Google L4 | Bengaluru | Rejected](https://leetcode.com/discuss/post/7867127/google-l4-bengaluru-reject-by-anonymous_-vho0/)  
  > …m (bid price and execute lowest bid) ##### Round 2 (DSA) Variant of Merge Intervals and Overlapping Intervals (Line Sweep & Priority Queue) ##### Round 3 (Googlyness) Standard behavioural Question…
- `reddit` _2026-03-06_ · [Google L3 vs. Amazon SDE2](https://www.reddit.com/r/cscareerquestions/comments/1rm7ip0/google_l3_vs_amazon_sde2/)  
  > …o I take the pay cut and title hit for the dream? Edit - I was asked Merge Intervals, Permutations of a string, and code for nested recycler view android…
- `reddit` _2026-06-02_ · [The 80/20 DSA Framework: How I stopped doing random LeetCode questions](https://www.reddit.com/r/leetcode/comments/1tuoc5j/the_8020_dsa_framework_how_i_stopped_doing_random/)  
  > …ys and finding pairs. Fast & Slow Pointers: For Linked List cycles. Merge Intervals: For overlapping scheduling problems. Modified Binary Search: Essential for O(log n) constraints. Phase 3: Advan…

**LC 207 — Course Schedule** (Medium) · 2 threads  
- `leetcode` _2026-05-18_ · [Google L4 Interview || Reject](https://leetcode.com/discuss/post/8265899/google-l4-interview-reject-by-anonymous_-xsc3/)  
  > …Sort Question very similar to the below problems.Question 1 : https://leetcode.com/problems/course-schedule/description/ Question 2 : https://leetcode.com/problems/ # Google L4 Interview Location : B…
- `leetcode` _2026-05-11_ · [Google Phone Interview Questions](https://leetcode.com/discuss/post/8195929/google-phone-interview-questions-by-anon-mk7j/)  
  > …eetcode.com/problems/rotate-image/) * Course Schedule [#207](https://leetcode.com/problems/course-schedule/) * Regular Expression Matching [#10](https://leetcode.com/problems/regular-expression-matc…

**LC 253 — Meeting Rooms II** (Medium) · 2 threads  
- `leetcode` _2026-05-11_ · [Google Phone Interview Questions](https://leetcode.com/discuss/post/8195929/google-phone-interview-questions-by-anon-mk7j/)  
  > …/leetcode.com/problems/lru-cache/) * Meeting Rooms II [#253](https://leetcode.com/problems/meeting-rooms-ii/) * Longest Repeating Character Replacement [#424](https://leetcode.com/problems/longest-r…
- `leetcode` _2026-01-23_ · [Google DSA Question](https://leetcode.com/discuss/post/7516532/google-dsa-question-by-anonymous_user-3tjh/)  
  > …e starting time could be used in one CPU? If so, then this is exactly meeting rooms II problem but with duplicate intervals (with same starting times) removed variation.\n\nDid you ask clarifying ques…

**LC 1944 — Number of Visible People in a Queue** (Hard) · 2 threads  
- `leetcode` _2026-05-11_ · [Google Phone Interview Questions](https://leetcode.com/discuss/post/8195929/google-phone-interview-questions-by-anon-mk7j/)  
  > …ecode-string/) * Number of Visible People in a Queue [#1944](https://leetcode.com/problems/number-of-visible-people-in-a-queue/) * LRU Cache [#146](https://leetcode.com/problems/lru-cache/) * Meeti…
- `leetcode` _2026-03-04_ · [Interview Experience: Google | L3 Web Solutions Engineer (GTech)](https://leetcode.com/discuss/post/7624355/interview-experience-google-l3-web-solut-ger7/)  
  > …sounds similar to [1944. Number of Visible People in a Queue](https://leetcode.com/problems/number-of-visible-people-in-a-queue/description/).\n for section 3\n```\n#include \nusing namespace std;\n\n…

**LC 23 — Merge k Sorted Lists** (Hard) · 2 threads  
- `leetcode` _2026-05-11_ · [Google Phone Interview Questions](https://leetcode.com/discuss/post/8195929/google-phone-interview-questions-by-anon-mk7j/)  
  > …oblems/sliding-window-maximum/) * Merge k Sorted Lists [#23](https://leetcode.com/problems/merge-k-sorted-lists/) Some questions are the closest that it can get to the actual question. Especially LR…
- `leetcode` _2026-04-22_ · [# 🔥 Top Google DSA Questions I Practiced (With Approach)](https://leetcode.com/discuss/post/8054808/top-google-dsa-questions-i-practiced-wit-quup/)  
  > …FS * Number of Islands → DFS --- ## Heap / Priority Queue * Merge K Sorted Lists --- ## Dynamic Programming * Longest Increasing Subsequence * 0/1 Knapsack --- ## My Learning…

**LC 300 — Longest Increasing Subsequence** (Medium) · 2 threads  
- `leetcode` _2026-05-11_ · [Google Phone Interview Questions](https://leetcode.com/discuss/post/8195929/google-phone-interview-questions-by-anon-mk7j/)  
  > …lems/edit-distance/) * Longest Increasing Subsequence [#300](https://leetcode.com/problems/longest-increasing-subsequence/) * Split Array Largest Sum [#410](https://leetcode.com/problems/split-array…
- `leetcode` _2026-04-22_ · [# 🔥 Top Google DSA Questions I Practiced (With Approach)](https://leetcode.com/discuss/post/8054808/top-google-dsa-questions-i-practiced-wit-quup/)  
  > …Queue * Merge K Sorted Lists --- ## Dynamic Programming * Longest Increasing Subsequence * 0/1 Knapsack --- ## My Learning * Most Google questions focus on **Graph + DP + Optimiz…

**LC 3 — Longest Substring Without Repeating Characters** (Medium) · 2 threads  
- `leetcode` _2026-05-11_ · [Google Phone Interview Questions](https://leetcode.com/discuss/post/8195929/google-phone-interview-questions-by-anon-mk7j/)  
  > …ater/) * Longest Substring Without Repeating Characters [#3](https://leetcode.com/problems/longest-substring-without-repeating-characters/) * Longest Palindromic Substring [#5](https://leetcode.com/…
- `reddit` _2026-08-03_ · [Codeforces vs LeetCode for Product-Based Company Placements – Need Adv](https://www.reddit.com/r/leetcode/comments/1vecluk/codeforces_vs_leetcode_for_productbased_company/)  
  > …for 1400-1600 Codeforces rating by end of second year, then switch to LeetCode 3-4 months before internship season. In second year, focus on Codeforces. In third year, shift to LC for interview simula…

**LC 286 — Walls and Gates** (Medium) · 2 threads  
- `leetcode` _2026-04-12_ · [I have rotting oranges now. 994. Rotten Oranges — finally clicked.](https://leetcode.com/discuss/post/7883464/i-have-rotting-oranges-now-994-rotten-or-t4bw/)  
  > …ven starts. That's literally it. Two ideas. One problem. **Trying Walls and Gates tomorrow. If you've solved it — does the same pattern hold? Upvote if you read this, shows me someone out there is…
- `reddit` _2026-01-23_ · [interviewer led me to direction of unoptimal solution should I be worr](https://www.reddit.com/r/csMajors/comments/1qki32w/interviewer_led_me_to_direction_of_unoptimal/)  
  > …, I was asked a standard matrix BFS question (I believe its literally walls and gates LC) originally I was describing a multisource BFS to my interviewer, but he seemed hesitant about it and kept push…

**LC 994 — Rotting Oranges** (Medium) · 2 threads  
- `leetcode` _2026-04-12_ · [I have rotting oranges now. 994. Rotten Oranges — finally clicked.](https://leetcode.com/discuss/post/7883464/i-have-rotting-oranges-now-994-rotten-or-t4bw/)  
  > …I have rotting oranges now. 994. Rotten Oranges — finally clicked. Finally got Rotten Oranges after 3 days of struggling with BFSSpent days stuck on Number of Islands, then this problem hit me again l…
- `reddit` _2026-06-02_ · [The 80/20 DSA Framework: How I stopped doing random LeetCode questions](https://www.reddit.com/r/leetcode/comments/1tuoc5j/the_8020_dsa_framework_how_i_stopped_doing_random/)  
  > …Advanced Structures BFS/DFS on Trees and Graphs: (Number of Islands, Rotting Oranges). Backtracking/Subsets: (Permutations, N-Queens). Top K Elements (Heaps): Whenever you see "Find the Kth largest…

**LC 210 — Course Schedule II** (Medium) · 2 threads  
- `leetcode` _2026-05-18_ · [Google L4 Interview || Reject](https://leetcode.com/discuss/post/8265899/google-l4-interview-reject-by-anonymous_-xsc3/)  
  > …etcode.com/problems/course-schedule/description/ Question 2 : https://leetcode.com/problems/course-schedule-ii/description/ ## Round 2 : Standard Behavioral questions. The below link helped me a lot…
- `leetcode` _2026-03-28_ · [L4 DSA Round Question](https://leetcode.com/discuss/post/7705895/google-l4-dsa-round-question-by-anonymou-nh2t/)  
  > …ave been successfully compiled. This problem is similar to https://leetcode.com/problems/course-schedule-ii/description/, but with the added complexity of multithreading. Points to consider:…

**LC 269 — Alien Dictionary** (Hard) · 2 threads  
- `hn` _2025-11-30_ · [Americans no longer see four-year college degrees as worth the cost](https://news.ycombinator.com/item?id=46094362)  
  > …ok bad if you don’t. Let’s not pretend that any of us are ready to do alien dictionary at the spur of a moment, or thats a useful skill for our role.…
- `hn` _2022-06-05_ · [I cheated on my Microsoft interview (2019)](https://news.ycombinator.com/item?id=31630843)  
  > …off balance (it became adversarial after I pointed out this was just alien dictionary, I probably would have had a better result pretending not to have seen the problem before). Or maybe that’s just…

**LC 208 — Implement Trie (Prefix Tree)** (Medium) · 1 thread  
- `reddit` _2026-07-06_ · [I Built a Boggle Game. LeetCode Problem #208 Saved It.](https://www.reddit.com/r/leetcode/comments/1uop0fq/i_built_a_boggle_game_leetcode_problem_208_saved/)  
  > …I Built a Boggle Game. LeetCode Problem #208 Saved It. I never thought I'd write the words "LeetCode saved my app." I am writing them now. Last week I shipped de Broglie , a Boggle-style word game fo…

**LC 239 — Sliding Window Maximum** (Hard) · 1 thread  
- `leetcode` _2026-05-11_ · [Google Phone Interview Questions](https://leetcode.com/discuss/post/8195929/google-phone-interview-questions-by-anon-mk7j/)  
  > …ms/split-array-largest-sum/) * Sliding Window Maximum [#239](https://leetcode.com/problems/sliding-window-maximum/) * Merge k Sorted Lists [#23](https://leetcode.com/problems/merge-k-sorted-lists/)…

**LC 354 — Russian Doll Envelopes** (Hard) · 1 thread  
- `leetcode` _2026-05-11_ · [Google Phone Interview Questions](https://leetcode.com/discuss/post/8195929/google-phone-interview-questions-by-anon-mk7j/)  
  > …lems/subarray-sum-equals-k/) * Russian Doll Envelopes [#354](https://leetcode.com/problems/russian-doll-envelopes/) * Number of Islands [#200](https://leetcode.com/problems/number-of-islands/) * Ko…

**LC 4 — Median of Two Sorted Arrays** (Hard) · 1 thread  
- `leetcode` _2026-05-11_ · [Google Phone Interview Questions](https://leetcode.com/discuss/post/8195929/google-phone-interview-questions-by-anon-mk7j/)  
  > …e too. Here is the list: * Median of Two Sorted Arrays [#4](https://leetcode.com/problems/median-of-two-sorted-arrays/) * Trapping Rain Water [#42](https://leetcode.com/problems/trapping-rain-water…

**LC 10 — Regular Expression Matching** (Hard) · 1 thread  
- `leetcode` _2026-05-11_ · [Google Phone Interview Questions](https://leetcode.com/discuss/post/8195929/google-phone-interview-questions-by-anon-mk7j/)  
  > …oblems/course-schedule/) * Regular Expression Matching [#10](https://leetcode.com/problems/regular-expression-matching/) * Sudoku Solver [#37](https://leetcode.com/problems/sudoku-solver/) * Edit D…

**LC 31 — Next Permutation** (Medium) · 1 thread  
- `leetcode` _2026-05-11_ · [Google Phone Interview Questions](https://leetcode.com/discuss/post/8195929/google-phone-interview-questions-by-anon-mk7j/)  
  > ….com/problems/koko-eating-bananas/) * Next Permutation [#31](https://leetcode.com/problems/next-permutation/) * Search in Rotated Sorted Array [#33](https://leetcode.com/problems/search-in-rotated-s…

**LC 33 — Search in Rotated Sorted Array** (Medium) · 1 thread  
- `leetcode` _2026-05-11_ · [Google Phone Interview Questions](https://leetcode.com/discuss/post/8195929/google-phone-interview-questions-by-anon-mk7j/)  
  > …ms/next-permutation/) * Search in Rotated Sorted Array [#33](https://leetcode.com/problems/search-in-rotated-sorted-array/) * Decode String [#394](https://leetcode.com/problems/decode-string/) * Nu…

**LC 34 — Find First and Last Position of Element in Sorted Array** (Medium) · 1 thread  
- `leetcode` _2026-05-11_ · [Google Phone Interview Questions](https://leetcode.com/discuss/post/8195929/google-phone-interview-questions-by-anon-mk7j/)  
  > …Find First and Last Position of Element in Sorted Array [#34](https://leetcode.com/problems/find-first-and-last-position-of-element-in-sorted-array/) * Maximal Rectangle [#85](https://leetcode.com/pr…

**LC 60 — Permutation Sequence** (Hard) · 1 thread  
- `leetcode` _2026-02-10_ · [[Interview Experience] Sharing First In-Person Google Onsite After Yea](https://leetcode.com/discuss/post/7567827/interview-experience-sharing-first-in-pe-oyg1/)  
  > …sked a problem similiar to this permutation sequence problem: https://leetcode.com/problems/permutation-sequence/description/ It's not the exact same but if you know how to solve this one, you can…

**LC 84 — Largest Rectangle in Histogram** (Hard) · 1 thread  
- `leetcode` _2026-05-11_ · [Google Phone Interview Questions](https://leetcode.com/discuss/post/8195929/google-phone-interview-questions-by-anon-mk7j/)  
  > …s/maximal-rectangle/) * Largest Rectangle in Histogram [#84](https://leetcode.com/problems/largest-rectangle-in-histogram/) * Rotate Image [#48](https://leetcode.com/problems/rotate-image/) * Cours…

**LC 85 — Maximal Rectangle** (Hard) · 1 thread  
- `leetcode` _2026-05-11_ · [Google Phone Interview Questions](https://leetcode.com/discuss/post/8195929/google-phone-interview-questions-by-anon-mk7j/)  
  > …ition-of-element-in-sorted-array/) * Maximal Rectangle [#85](https://leetcode.com/problems/maximal-rectangle/) * Largest Rectangle in Histogram [#84](https://leetcode.com/problems/largest-rectangle-…

**LC 93 — Restore IP Addresses** (Medium) · 1 thread  
- `leetcode` _2026-02-28_ · [Understanding Time Complexity for Backtracking with Pruning](https://leetcode.com/discuss/post/7614127/understanding-time-complexity-for-backtr-rn7u/)  
  > …tps://leetcode.com/problems/generate-parentheses/description/ https://leetcode.com/problems/restore-ip-addresses/description/ In problems like Restore IP Addresses, the maximum length is constant, so…

**LC 128 — Longest Consecutive Sequence** (Medium) · 1 thread  
- `leetcode` _2026-05-11_ · [Google Phone Interview Questions](https://leetcode.com/discuss/post/8195929/google-phone-interview-questions-by-anon-mk7j/)  
  > …alindromic-substring/) * Longest Consecutive Sequence [#128](https://leetcode.com/problems/longest-consecutive-sequence/) * Subarray Sum Equals K [#560](https://leetcode.com/problems/subarray-sum-eq…

## 2) Recent interview posts (raw feed)

Newest first — the primary sources. Open them for full text and comment threads. Only interview-flavoured posts are listed, at most 60 per source.

### LeetCode Discuss

| Date | Post | Context |
|------|------|---------|
| 2026-08-08 | [LeetCode problems for AI Interviews](https://leetcode.com/discuss/post/8448866/leetcode-problems-for-ai-interviews-by-a-cnj8/) | amazon, openai, career, feedback, machine-learning-engineer +4 more |
| 2026-08-06 | [Can I ask for virtual onsite at Google?](https://leetcode.com/discuss/post/8444095/can-i-ask-for-virtual-onsite-at-google-b-19jx/) |  |
| 2026-08-03 | [Google Staff Software Engineer (L6)](https://leetcode.com/discuss/post/8439367/google-staff-software-engineer-l6-by-abh-n36g/) | interview |
| 2026-08-03 | [Google L4 chances](https://leetcode.com/discuss/post/8439101/google-l4-chances-by-anonymous_user-n5il/) | career, feedback, interview, l4-google |
| 2026-08-03 | [Google L4 Chances](https://leetcode.com/discuss/post/8438964/google-l4-chances-by-pushya_bansal-y3ph/) |  |
| 2026-07-27 | [Screening Round with Recruiter for Web Solutions Engineer at Google](https://leetcode.com/discuss/post/8423751/screening-round-with-recruiter-for-web-s-5897/) | career, interview, l4-google |
| 2026-07-25 | [Feedback from reqruiter](https://leetcode.com/discuss/post/8418035/feedback-from-reqruiter-by-barez59-spgt/) | feedback |
| 2026-07-22 | [What to expect in Google's tech lead interview and role](https://leetcode.com/discuss/post/8412228/what-to-expect-in-googles-tech-lead-inte-aq1g/) | technical-interview |
| 2026-07-20 | [Google L4](https://leetcode.com/discuss/post/8409212/google-l4-by-anonymous_user-n4gg/) |  |
| 2026-07-20 | [Need help in coding round of L4 PhD early careers Google India](https://leetcode.com/discuss/post/8408470/need-help-in-coding-round-of-l4-phd-earl-z9o4/) | interview |
| 2026-07-19 | [Forward Deployed Engineer Interview Google Cloud US](https://leetcode.com/discuss/post/8407969/forward-deployed-engineer-interview-goog-90sp/) |  |
| 2026-07-17 | [Google SRE (Software Developer III) Interview Coming Up – Looking for Recent Interview Insights](https://leetcode.com/discuss/post/8403716/google-sre-software-developer-iii-interv-n5mg/) | interview |
| 2026-07-17 | [Google L3 (India) Interview Experience need help (ex interviewers especially welcome)](https://leetcode.com/discuss/post/8403050/google-l3-india-interview-experience-nee-ya6h/) | interview |
| 2026-07-14 | [Google L4 \| Team Match](https://leetcode.com/discuss/post/8397122/google-l4-team-match-by-anonymous_user-kcfb/) | interview |
| 2026-07-12 | [Can someone help with last 6 months Google Questions ?](https://leetcode.com/discuss/post/8392512/can-someone-help-with-last-6-months-goog-k1do/) | career, l4-google, google-interview-questions |
| 2026-07-10 | [System Design Board Built for Mock Interviews](https://leetcode.com/discuss/post/8388985/system-design-board-built-for-mock-inter-5dsq/) | microsoft, leetcode, backend, interview, l4-google +3 more |
| 2026-07-04 | [Any Updates for Google Software engineer intern 2027 summer?](https://leetcode.com/discuss/post/8375211/any-updates-for-google-software-engineer-0cye/) | career, interview, internship-2 |
| 2026-07-02 | [c# in Google Interview](https://leetcode.com/discuss/post/8371223/c-in-google-interview-by-anonymous_user-pslg/) |  |
| 2026-07-02 | [After 6 Months, It's Time to Build Again](https://leetcode.com/discuss/post/8370596/after-6-months-its-time-to-build-again-b-6cu6/) | microsoft, amazon, leetcode, career, interview +1 more |
| 2026-07-01 | [I need advice on my Google Intern Interview](https://leetcode.com/discuss/post/8370237/i-need-advice-on-my-google-intern-interv-owuo/) | interview, google-interview-questions |
| 2026-06-30 | [Google L4 Team Matching Phase](https://leetcode.com/discuss/post/8368243/google-l4-team-matching-phase-by-vj_tirt-k0vq/) | l4-google, l1-google, google-interview-questions |
| 2026-06-30 | [Google Googliness round 2025 and 2026](https://leetcode.com/discuss/post/8367035/google-googliness-round-2025-and-2026-by-qemm/) |  |
| 2026-06-28 | [Data and AI roles Job interviews guidance](https://leetcode.com/discuss/post/8362765/data-and-ai-roles-job-interviews-guidanc-4q47/) | microsoft, amazon, uber, flipkart, data-science +4 more |
| 2026-06-28 | [Google Doesn't Just Want Answers, They Want Every Detail - My HR Round Breakdown](https://leetcode.com/discuss/post/8362764/google-doesnt-just-want-answers-they-wan-26hk/) | backend, interview |
| 2026-06-27 | [Google L4 \| Onsite Expectations](https://leetcode.com/discuss/post/8361591/google-l4-onsite-expectations-by-anonymo-728k/) | onsite, interview |
| 2026-06-26 | [Seeking Guidance for Cracking Google + Looking for Serious Study Partners](https://leetcode.com/discuss/post/8359851/seeking-guidance-for-cracking-google-loo-zlg6/) | amazon, career, feedback, dsa, interview, study-group-2 +2 more |
| 2026-06-26 | [Buddy For DSA and Interview Prepration](https://leetcode.com/discuss/post/8359294/buddy-for-dsa-and-interview-prepration-b-3kh3/) | interview |
| 2026-06-25 | [Google Offer](https://leetcode.com/discuss/post/8357173/google-offer-by-anonymous_user-ik5u/) | career, feedback, compensation, interview, l4-google +1 more |
| 2026-06-21 | [Google \| SWE-2 (L3)  \| Team Match Query](https://leetcode.com/discuss/post/8348520/google-swe-2-team-match-query-by-anonymo-7ftp/) | team-fit |
| 2026-06-21 | [Learn System Design for Interviews](https://leetcode.com/discuss/post/8348146/learn-system-design-for-interviews-by-ar-o1sz/) | microsoft, dsa, dsa-resources, system-design-2 |
| 2026-06-20 | [How long does Google usually take to make a decision after onsite interviews?](https://leetcode.com/discuss/post/8347721/how-long-does-google-usually-take-to-mak-tx0m/) | feedback, interview |
| 2026-06-20 | [Google L4 Interview feedback](https://leetcode.com/discuss/post/8347017/google-l4-interview-feedback-by-anonymou-unin/) | feedback |
| 2026-06-19 | [Google L3 SWE (India) Interview Experience \| Offer Received](https://leetcode.com/discuss/post/8345294/google-l3-swe-india-interview-experience-z8un/) | interview |
| 2026-06-17 | [Google L4 \| Banglore](https://leetcode.com/discuss/post/8340444/google-l4-banglore-by-anonymous_user-i96q/) |  |
| 2026-06-15 | [Referaal status while in team match](https://leetcode.com/discuss/post/8334901/referaal-status-while-in-team-match-by-a-m244/) |  |
| 2026-06-14 | [Have Google virtual rounds (Domain specific + DSA and GL) on Wednesday any suggestions?](https://leetcode.com/discuss/post/8334107/have-google-virtual-rounds-domain-specif-ufky/) | career, interview, l4-google |
| 2026-06-14 | [Upcoming Amazon interview Prep - Need Help](https://leetcode.com/discuss/post/8332865/upcoming-amazon-interview-prep-need-help-ocdw/) | amazon, career, feedback, dsa, interview, amazon-sde1-2 |
| 2026-06-13 | [SDE-2 Microsoft Azure Team Interview Questions (LLD + HLD)](https://leetcode.com/discuss/post/8331863/sde-2-microsoft-azure-team-interview-que-93y6/) | microsoft, amazon, career, feedback, compensation, interview +2 more |
| 2026-06-12 | [L5 Google Rating Feedback](https://leetcode.com/discuss/post/8330164/l5-google-rating-feedback-by-anonymous_u-yku7/) | career, feedback, interview |
| 2026-06-10 | [Google L4 India - Compensation](https://leetcode.com/discuss/post/8326052/salary-expectations-for-google-l4-india-y5m1h/) | career, feedback, compensation, interview, l4-google |
| 2026-06-10 | [Google \| L4 \| Interview Experience \| Chances](https://leetcode.com/discuss/post/8325811/google-l4-interview-experience-chances-b-3qmq/) | career, feedback, interview, l4-google |
| 2026-06-09 | [Google L4 - Team Match](https://leetcode.com/discuss/post/8323053/google-l4-team-match-by-anonymous_user-zx63/) | career, feedback, interview, l4-google, team-fit |
| 2026-06-08 | [From Zero to Offer Interview Preparation Roadmap](https://leetcode.com/discuss/post/8321890/from-zero-to-offer-interview-preparation-ek23/) | facebook, microsoft, amazon, uber, linkedin, salesforce +3 more |
| 2026-06-08 | [Google SRE-SWE (L3/L4) prep advice for someone strong on systems but rusty on DSA?](https://leetcode.com/discuss/post/8321689/google-sre-swe-l3l4-prep-advice-for-some-6jnl/) | interview |
| 2026-06-07 | [Google L4 \| Interview Experience \| Bangalore \| Next Steps](https://leetcode.com/discuss/post/8319665/google-l4-interview-experience-bangalore-mwp1/) | interview, l4-google |
| 2026-06-07 | [Cracking MANG with 10 YEO - Reality check](https://leetcode.com/discuss/post/8319567/cracking-mang-with-10-yeo-reality-check-zi6j7/) | microsoft, amazon |
| 2026-06-05 | [Google L3 Interview - looking for prep advice!](https://leetcode.com/discuss/post/8315751/google-l3-interview-looking-for-prep-adv-udzw/) | interview, google-interview-questions, swe-ii-google |
| 2026-06-05 | [Google Cloud Web Application Engineer (Gurugram/Pune) – Updates After GHA?](https://leetcode.com/discuss/post/8315591/google-cloud-web-application-engineer-gu-dbb4/) | interview |
| 2026-06-05 | [Google L4 Team match India](https://leetcode.com/discuss/post/8314530/google-l4-team-match-india-by-anonymous_-fyaa/) | career, feedback, compensation, interview |
| 2026-06-04 | [I had questions about system design round.](https://leetcode.com/discuss/post/8313663/i-had-questions-about-system-design-roun-jqo2/) | uber, career, interview-experience, system-design, interview |
| 2026-06-02 | [Anyone given Domain Specific round (Android/iOS) at Google?](https://leetcode.com/discuss/post/8308783/anyone-given-domain-specific-round-andro-jdtq/) | interview |
| 2026-06-01 | [Uber/Google Phone Screen Feedback Counts?](https://leetcode.com/discuss/post/8306742/uber-phone-screen-feedback-counts-by-ano-naz8/) | uber, interview |
| 2026-06-01 | [Amaon SDE - I : In Person Interview at BLR office \|\| 4th Round(Leadership Principles)](https://leetcode.com/discuss/post/8306214/amaon-sde-i-in-person-interview-at-blr-o-ey3v/) | amazon, low-level-design, dsa-java, amazon-sde1-2 |
| 2026-06-01 | [Stuck in Google Team Matching for 4 Months, Any Advice?](https://leetcode.com/discuss/post/8306204/stuck-in-google-team-matching-for-4-mont-0qzb/) | interview |
| 2026-05-31 | [Preparing for Google Interview in Embedded Domain](https://leetcode.com/discuss/post/8304833/preparing-for-google-interview-in-embedd-y1l2/) | interview |
| 2026-05-31 | [Google(L4) intervew loop - In progess \| Need partner for further Prep](https://leetcode.com/discuss/post/8304156/uber-hld-upcoming-by-anonymous_user-vk6g/) |  |
| 2026-05-31 | [Google Interview Process Timeline Question](https://leetcode.com/discuss/post/8303584/google-interview-process-timeline-questi-gyjd/) | l4-google |
| 2026-05-30 | [2 YOE and completely lost on System Design — help!](https://leetcode.com/discuss/post/8303012/2-yoe-and-completely-lost-on-system-desi-r7ot/) | microsoft, amazon, uber, career, system-design, compensation +1 more |
| 2026-05-30 | [Google interview question about unnoticed bugs](https://leetcode.com/discuss/post/8302587/google-interview-question-about-unnotice-6tjm/) | l4-google, google-interview-questions |
| 2026-05-30 | [UBER Freight - SDE - II : Round - 3](https://leetcode.com/discuss/post/8302462/uber-freight-sde-ii-round-3-by-anonymous-mr1m/) | uber, low-level-design, dsa, sde-2-3 |

_156 more not shown._

### Reddit

| Date | Post | Context |
|------|------|---------|
| 2026-08-11 | [Google interview next week: forward deployed engineer](https://www.reddit.com/r/leetcode/comments/1vlahav/google_interview_next_week_forward_deployed/) | r/leetcode |
| 2026-08-11 | [Rants of a laid off developer: LinkedIn office visit got me rage applying to jobs again](https://www.reddit.com/r/cscareerquestions/comments/1vl9hxq/rants_of_a_laid_off_developer_linkedin_office/) | r/cscareerquestions |
| 2026-08-10 | [Google 3 weeks post onsite and still no feedback? Is this normal?](https://www.reddit.com/r/leetcode/comments/1vkx47f/google_3_weeks_post_onsite_and_still_no_feedback/) | r/leetcode |
| 2026-08-10 | [Google FDE (GenAI) — team match before HC? Curious about others' timelines](https://www.reddit.com/r/leetcode/comments/1vkwmrl/google_fde_genai_team_match_before_hc_curious/) | r/leetcode |
| 2026-08-10 | [Google SWE ML/AI III interview process: what are the first two interviews](https://www.reddit.com/r/leetcode/comments/1vkwhig/google_swe_mlai_iii_interview_process_what_are/) | r/leetcode |
| 2026-08-10 | [Google SRE-SWE Interview - LeetCode](https://www.reddit.com/r/leetcode/comments/1vkv8b6/google_sreswe_interview_leetcode/) | r/leetcode |
| 2026-08-10 | [Google interview exp](https://www.reddit.com/r/leetcode/comments/1vkr2hq/google_interview_exp/) | r/leetcode |
| 2026-08-10 | [Google Interview](https://www.reddit.com/r/leetcode/comments/1vkizpj/google_interview/) | r/leetcode |
| 2026-08-09 | [Google Cloud Silicon Validation Engineer Interview – What Topics Are Typically Covered?](https://www.reddit.com/r/csMajors/comments/1vjf10j/google_cloud_silicon_validation_engineer/) | r/csMajors |
| 2026-08-08 | [Google SRE-SWE interview coming up — looking for recent prep advice/resources](https://www.reddit.com/r/leetcode/comments/1vj8exr/google_sreswe_interview_coming_up_looking_for/) | r/leetcode |
| 2026-08-08 | [Google STEP interview](https://www.reddit.com/r/leetcode/comments/1vik2x7/google_step_interview/) | r/leetcode |
| 2026-08-08 | [Google STEP interview difficulty](https://www.reddit.com/r/csMajors/comments/1vik1n7/google_step_interview_difficulty/) | r/csMajors |
| 2026-08-07 | [ML Domain interview at Google](https://www.reddit.com/r/leetcode/comments/1vig549/ml_domain_interview_at_google/) | r/leetcode |
| 2026-08-07 | [Reapplying timeline to google after interview rejection](https://www.reddit.com/r/leetcode/comments/1vidqmx/reapplying_timeline_to_google_after_interview/) | r/leetcode |
| 2026-08-07 | [Reapplying timeline to google after interview rejection](https://www.reddit.com/r/csMajors/comments/1vidrqf/reapplying_timeline_to_google_after_interview/) | r/csMajors |
| 2026-08-06 | [Google recruiter went silent for 6+ weeks after 6 months in "Team Matching" (L3 SWE)](https://www.reddit.com/r/leetcode/comments/1vhklxw/google_recruiter_went_silent_for_6_weeks_after_6/) | r/leetcode |
| 2026-08-06 | [SWE 2: 6 months of consistently reaching final rounds and still getting rejected, where am I go](https://www.reddit.com/r/leetcode/comments/1vgsosy/swe_2_6_months_of_consistently_reaching_final/) | r/leetcode |
| 2026-08-06 | [Google Team Matching (USA) – Entered TM on June 3, Still No Team Match Calls. Looking for Advic](https://www.reddit.com/r/csMajors/comments/1vhgwyb/google_team_matching_usa_entered_tm_on_june_3/) | r/csMajors |
| 2026-08-05 | [I need serious help, have Google Non-Tech and Amazon Tech interview in next week](https://www.reddit.com/r/leetcode/comments/1vgmyl9/i_need_serious_help_have_google_nontech_and/) | r/leetcode |
| 2026-08-05 | [Bizarre Google Screening Experience (3.3 YOE): Mixed signals, a second chance, and an instant f](https://www.reddit.com/r/leetcode/comments/1vg3sfj/bizarre_google_screening_experience_33_yoe_mixed/) | r/leetcode |
| 2026-08-05 | [Google Forward Deploy Engineer interview europe](https://www.reddit.com/r/leetcode/comments/1vg2id0/google_forward_deploy_engineer_interview_europe/) | r/leetcode |
| 2026-08-05 | [Terrified of freezing up during my Google interview this Friday (L4) — any advice for when you ](https://www.reddit.com/r/leetcode/comments/1vg1u0p/terrified_of_freezing_up_during_my_google/) | r/leetcode |
| 2026-08-05 | [undergrad program acceptance](https://www.reddit.com/r/csMajors/comments/1vgiso9/undergrad_program_acceptance/) | r/csMajors |
| 2026-08-05 | [System design learning resources for Agentic solutions](https://www.reddit.com/r/ExperiencedDevs/comments/1vg886s/system_design_learning_resources_for_agentic/) | r/ExperiencedDevs |
| 2026-08-04 | [Google TPM interview questions](https://www.reddit.com/r/leetcode/comments/1vfbn31/google_tpm_interview_questions/) | r/leetcode |
| 2026-08-04 | [Leetcode is slowing dying and that's a good thing!!](https://www.reddit.com/r/leetcode/comments/1vf8yn6/leetcode_is_slowing_dying_and_thats_a_good_thing/) | r/leetcode |
| 2026-08-04 | [Google cool off period](https://www.reddit.com/r/leetcode/comments/1vf36n9/google_cool_off_period/) | r/leetcode |
| 2026-08-03 | [Google HC Approved → Team Match → 1 Additional Coding Interview. What are my chances?](https://www.reddit.com/r/leetcode/comments/1vetlfo/google_hc_approved_team_match_1_additional_coding/) | r/leetcode |
| 2026-08-03 | [How technical is a Google TPM I interview?](https://www.reddit.com/r/leetcode/comments/1veq4km/how_technical_is_a_google_tpm_i_interview/) | r/leetcode |
| 2026-08-03 | [Google L4 (US) Onsite next week](https://www.reddit.com/r/leetcode/comments/1vehy6i/google_l4_us_onsite_next_week/) | r/leetcode |
| 2026-08-03 | [Google Team Matching Question: SRE-SWE L3 to SWE L3?](https://www.reddit.com/r/leetcode/comments/1vef29w/google_team_matching_question_sreswe_l3_to_swe_l3/) | r/leetcode |
| 2026-08-03 | [Codeforces vs LeetCode for Product-Based Company Placements – Need Advice from Experienced Peop](https://www.reddit.com/r/leetcode/comments/1vecluk/codeforces_vs_leetcode_for_productbased_company/) | r/leetcode |
| 2026-08-02 | [What to expect at Google L4 Onsite ?](https://www.reddit.com/r/leetcode/comments/1vdx7uv/what_to_expect_at_google_l4_onsite/) | r/leetcode |
| 2026-08-02 | [What does “waiting for the feedback regarding your candidacy” usually mean at Google?](https://www.reddit.com/r/leetcode/comments/1vdk6w0/what_does_waiting_for_the_feedback_regarding_your/) | r/leetcode |
| 2026-08-02 | [Hi folks ,is there any cool off period at Google to re-apply for other roles ,I have interview ](https://www.reddit.com/r/leetcode/comments/1vdjqs8/hi_folks_is_there_any_cool_off_period_at_google/) | r/leetcode |
| 2026-08-02 | [Need tips for Google L4 SWE interview loop (SWE - III) US](https://www.reddit.com/r/leetcode/comments/1vdjj5a/need_tips_for_google_l4_swe_interview_loop_swe/) | r/leetcode |
| 2026-08-02 | [Google 2027 SWE Internship Interview](https://www.reddit.com/r/csMajors/comments/1vdtp1p/google_2027_swe_internship_interview/) | r/csMajors |
| 2026-08-01 | [Google L5 Preparation](https://www.reddit.com/r/leetcode/comments/1vcmy9q/google_l5_preparation/) | r/leetcode |
| 2026-08-01 | [Getting an offer with Google in NYC is impossible right?](https://www.reddit.com/r/cscareerquestions/comments/1vcrl5w/getting_an_offer_with_google_in_nyc_is_impossible/) | r/cscareerquestions |
| 2026-08-01 | [Need tips for Google L4 SWE interview loop (SWE - III)](https://www.reddit.com/r/csMajors/comments/1vczfec/need_tips_for_google_l4_swe_interview_loop_swe_iii/) | r/csMajors |
| 2026-07-31 | [SWE Interview Review 1](https://www.reddit.com/r/leetcode/comments/1vc6vw1/swe_interview_review_1/) | r/leetcode |
| 2026-07-31 | [Ai coding rounds?](https://www.reddit.com/r/cscareerquestions/comments/1vbks0g/ai_coding_rounds/) | r/cscareerquestions |
| 2026-07-30 | [Google US L4 interview experience, waiting for results , was asked sys design??](https://www.reddit.com/r/leetcode/comments/1vb5ra2/google_us_l4_interview_experience_waiting_for/) | r/leetcode |
| 2026-07-30 | [Interview Guidance for Software Engineer III, AI/ML, Google Cloud AI - United States - 2026](https://www.reddit.com/r/leetcode/comments/1vb390p/interview_guidance_for_software_engineer_iii_aiml/) | r/leetcode |
| 2026-07-30 | ["Even someone who doesn't know about recursion could see this DP problem" Google Poland L4 SWE ](https://www.reddit.com/r/leetcode/comments/1vb32h2/even_someone_who_doesnt_know_about_recursion/) | r/leetcode |
| 2026-07-30 | [How to get back to speed after 1 year off? Did I miss anything?](https://www.reddit.com/r/leetcode/comments/1vaqx29/how_to_get_back_to_speed_after_1_year_off_did_i/) | r/leetcode |
| 2026-07-30 | [Google L4 Interview Experience \| Ratings: H, NH -> H, H, LH \| Will I survive Team Matching?](https://www.reddit.com/r/leetcode/comments/1vam78s/google_l4_interview_experience_ratings_h_nh_h_h/) | r/leetcode |
| 2026-07-30 | [Google Onsite](https://www.reddit.com/r/leetcode/comments/1vaer7b/google_onsite/) | r/leetcode |
| 2026-07-29 | [Google AI Catalyst Interviews](https://www.reddit.com/r/leetcode/comments/1va86p9/google_ai_catalyst_interviews/) | r/leetcode |
| 2026-07-29 | [GOOG Senior SWE Prep](https://www.reddit.com/r/leetcode/comments/1v9z5lq/goog_senior_swe_prep/) | r/leetcode |
| 2026-07-29 | [Staff Software Engineer L6 Tech screen system design](https://www.reddit.com/r/leetcode/comments/1v9l576/staff_software_engineer_l6_tech_screen_system/) | r/leetcode |
| 2026-07-29 | [My Resume isn’t getting any responses despite FAANG internship](https://www.reddit.com/r/csMajors/comments/1v9l36n/my_resume_isnt_getting_any_responses_despite/) | r/csMajors |
| 2026-07-28 | [Google SRE Coding Interview Question I Was Asked](https://www.reddit.com/r/leetcode/comments/1v98vik/google_sre_coding_interview_question_i_was_asked/) | r/leetcode |
| 2026-07-28 | [How to land an interview at Google (ZH/UK)](https://www.reddit.com/r/leetcode/comments/1v98gw5/how_to_land_an_interview_at_google_zhuk/) | r/leetcode |
| 2026-07-28 | [Could anyone share their recent Google SDE interview experience?](https://www.reddit.com/r/leetcode/comments/1v97yre/could_anyone_share_their_recent_google_sde/) | r/leetcode |
| 2026-07-28 | [Cleared Google Round 1 and have onsite in ~1 month: looking for DSA prep advice](https://www.reddit.com/r/leetcode/comments/1v8kxiq/cleared_google_round_1_and_have_onsite_in_1_month/) | r/leetcode |
| 2026-07-28 | [I analyzed 1.2M candidate job experiences. Offer rates fell from 51% in 2015 to 38.6% in 2026](https://www.reddit.com/r/cscareerquestions/comments/1v9ekv1/i_analyzed_12m_candidate_job_experiences_offer/) | r/cscareerquestions |
| 2026-07-28 | [Career advice: Asked to do FDE sort of work after an unexpected reorg in new job](https://www.reddit.com/r/cscareerquestions/comments/1v8og9x/career_advice_asked_to_do_fde_sort_of_work_after/) | r/cscareerquestions |
| 2026-07-27 | [I need serious help, have Google and Amazon interview in 3 weeks; haven’t done DSA since 1 year](https://www.reddit.com/r/leetcode/comments/1v896mg/i_need_serious_help_have_google_and_amazon/) | r/leetcode |
| 2026-07-27 | [Looking for Claude prompts/skills for Google L5/L6](https://www.reddit.com/r/leetcode/comments/1v81bj3/looking_for_claude_promptsskills_for_google_l5l6/) | r/leetcode |

_637 more not shown._

### Blind

| Date | Post | Context |
|------|------|---------|
| 2026-08-10 | [Google in person interviews](https://www.teamblind.com/post/google-in-person-interviews-073vm4jp) | India |
| 2026-08-10 | [Google Product Manager Interview](https://www.teamblind.com/post/google-product-manager-interview-70qnr6o8) | Product Management Career |
| 2026-08-08 | [google interviewer not submitting feedback](https://www.teamblind.com/post/google-interviewer-not-submitting-feedback-an58202o) | Interview Experiences |
| 2026-08-08 | [Didn't pass google interview](https://www.teamblind.com/post/didnt-pass-google-interview-jr5na3i8) | Software Engineering Career |
| 2026-08-08 | [Google Product Vision Interview](https://www.teamblind.com/post/google-product-vision-interview-sz6j3zx8) | Product Management Career |
| 2026-08-07 | [L5 Google Interviews](https://www.teamblind.com/post/l5-google-interviews-xp23wa87) | Tech Industry |
| 2026-08-05 | [Google on-site interviews](https://www.teamblind.com/post/google-on-site-interviews-pdwl8eqv) | Tech Industry |
| 2026-08-03 | [Messed up Google interview](https://www.teamblind.com/post/messed-up-google-interview-zdb0hk43) | Software Engineering Career |
| 2026-08-01 | [Google coding interview in 90 days?](https://www.teamblind.com/post/google-coding-interview-in-90-days-vmofjxr8) | Software Engineering Career |
| 2026-07-31 | [Why are Google Interviewers all assholes??](https://www.teamblind.com/post/why-are-google-interviewers-all-assholes-lebbprxx) | Software Engineering Career |
| 2026-07-28 | [Google recruiter call after onsite](https://www.teamblind.com/post/google-recruiter-call-after-onsite-8rqba7cn) | Software Engineering Career |
| 2026-07-20 | [GOOGLE HIRING PROCESS](https://www.teamblind.com/post/google-hiring-process-qvs84vh0) | Software Engineering Career |
| 2026-07-17 | [Google onsite](https://www.teamblind.com/post/google-onsite-46hhgcjq) | Tech Industry |
| 2026-07-16 | [New Interview Process at Google](https://www.teamblind.com/post/new-interview-process-at-google-wegovb2j) | Interview Experiences |
| 2026-07-12 | [Google onsite tomorrow. Nervous as hell](https://www.teamblind.com/post/google-onsite-tomorrow-nervous-as-hell-17kems3d) | Software Engineering Career |
| 2026-07-08 | [Google L4 Onsite](https://www.teamblind.com/post/google-l4-onsite-ptzk1xrz) | Software Engineering Career |
| 2026-07-02 | [What difficulty leetcode questions does Google ask these days?](https://www.teamblind.com/post/what-difficulty-leetcode-questions-does-google-ask-these-days-wmrcng78) | Software Engineering Career |
| 2026-07-02 | [What difficulty leetcode questions does Google ask these days?](https://www.teamblind.com/post/what-difficulty-leetcode-questions-does-google-ask-these-days-f8r01nam) | Software Engineering Career |
| 2026-06-17 | [Google interview leetcode?](https://www.teamblind.com/post/google-interview-leetcode-3xyv28x5) | Interview Experiences |
| 2026-06-09 | [Google Phone Screen result](https://www.teamblind.com/post/google-phone-screen-result-vn0cn634) | Software Engineering Career |
| 2026-05-14 | [Please don't interview at Google if you want to do 996.](https://www.teamblind.com/post/please-dont-interview-at-google-if-you-want-to-do-996-3pwm2j0f) | Tech Industry |
| 2026-05-01 | [Average leetcode rating of people who pass Google algo interviews USA?](https://www.teamblind.com/post/average-leetcode-rating-of-people-who-pass-google-algo-interviews-usa-vyotx22n) | Software Engineering Career |
| 2026-04-11 | [Why is Google interview still so tough ?](https://www.teamblind.com/post/why-is-google-interview-still-so-tough-fzefcodm) | Software Engineering Career |
| 2026-04-06 | [Google return to in-person interviews](https://www.teamblind.com/post/google-return-to-in-person-interviews-fhuewkip) | FAANG Lounge |
| 2025-11-16 | [Why google asking Leetcode hards ?](https://www.teamblind.com/post/why-google-asking-leetcode-hards-yqytmhk0) | Tech Industry |
| 2025-06-28 | [Google reject after passing online assessment](https://www.teamblind.com/post/google-reject-after-passing-online-assessment-t8tedg8w) | FAANG Lounge |
| 2024-10-02 | [Google DS Online Assessment](https://www.teamblind.com/post/google-ds-online-assessment-0k7ccquy) | Data Science Career |
| 2024-08-15 | [Google interview questions](https://www.teamblind.com/post/google-interview-questions-rqwvdhem) | Interview Experiences |
| 2024-03-19 | [Google online assessment has only HR questions](https://www.teamblind.com/post/google-online-assessment-has-only-hr-questions-pky1zaad) | Software Engineering Career |
| 2024-02-14 | [Google online hiring assessment for PM-1](https://www.teamblind.com/post/google-online-hiring-assessment-for-pm-1-y8o8pbvr) | Product Management Career |
| 2024-02-05 | [Google PM online assessment](https://www.teamblind.com/post/google-pm-online-assessment-5ynpjk3w) | Product Management Career |
| 2024-02-01 | [Google PM interview - Online Assessment](https://www.teamblind.com/post/google-pm-interview-online-assessment-ydkkzqpd) | Tech Industry |
| 2024-02-01 | [Google PM interview - Online assessment questions](https://www.teamblind.com/post/google-pm-interview-online-assessment-questions-xc3uh4hs) | Product Management Career |
| 2022-12-02 | [SQL online assessment at Google](https://www.teamblind.com/post/sql-online-assessment-at-google-6nzekyz2) | Tech Industry |
| 2022-06-16 | [Google Online Assessment](https://www.teamblind.com/post/google-online-assessment-lrcg7ugy) | Tech Industry |
| 2022-06-13 | [Google Onsite - All LeetCode Hard](https://www.teamblind.com/post/google-onsite-all-leetcode-hard-pgwdrgsb) | Software Engineering Career |
| 2022-03-28 | [What to expect on Microsoft/Google online assessments?](https://www.teamblind.com/post/what-to-expect-on-microsoftgoogle-online-assessments-iygur8pj) | Tech Industry |
| 2021-10-04 | [Google Summer 2022 internship online assessment](https://www.teamblind.com/post/google-summer-2022-internship-online-assessment-zlh7zsvk) | Software Engineering Career |
| 2021-09-24 | [Can you Google during Amazon online assessment?](https://www.teamblind.com/post/can-you-google-during-amazon-online-assessment-pxzxut32) | Software Engineering Career |
| 2021-09-23 | [Fuck Google Interviews](https://www.teamblind.com/post/fuck-google-interviews-1zrxgfcy) | Tech Industry |
| 2021-06-21 | [Google online assessment insights](https://www.teamblind.com/post/google-online-assessment-insights-oydejp31) | Tech Industry |
| 2021-04-25 | [Mr Pichai ban LeetCode questions at Google!](https://www.teamblind.com/post/mr-pichai-ban-leetcode-questions-at-google-b0k8bufo) | Tech Industry |
| 2021-03-20 | [How can I motivate my bf to leetcode and interview with Google?](https://www.teamblind.com/post/how-can-i-motivate-my-bf-to-leetcode-and-interview-with-google-qzu8smb0) | Software Engineering Career |
| 2020-11-23 | [A Guy got rejected by Google , has done 900+ Leetcode. It got me sad.](https://www.teamblind.com/post/a-guy-got-rejected-by-google-has-done-900-leetcode-it-got-me-sad-2h838wte) | Software Engineering Career |
| 2020-10-15 | [Google 90 min online interview assessment](https://www.teamblind.com/post/google-90-min-online-interview-assessment-k7htconv) | Tech Industry |
| 2020-04-26 | [Google interview and Leetcode](https://www.teamblind.com/post/google-interview-and-leetcode-gvqukyv2) | Tech Industry |
| 2020-04-25 | [Leetcode Banned at Google](https://www.teamblind.com/post/leetcode-banned-at-google-carcakb6) | Tech Industry |
| 2019-09-28 | [How to study for Google online assessment](https://www.teamblind.com/post/how-to-study-for-google-online-assessment-alntcpst) | Tech Industry |
| 2019-08-19 | [Google leetcode strategy](https://www.teamblind.com/post/google-leetcode-strategy-gzttdhtt) | Tech Industry |
| 2019-08-07 | [Google online assessment for internship](https://www.teamblind.com/post/google-online-assessment-for-internship-446mjlsv) | Tech Industry |
| 2019-07-06 | [Google really ask questions like Leetcode #753?](https://www.teamblind.com/post/google-really-ask-questions-like-leetcode-753-yjcn8vtp) | Tech Industry |
| 2019-05-02 | [Did You Try To Google Answers In Online Assessment?](https://www.teamblind.com/post/did-you-try-to-google-answers-in-online-assessment-rstgbu2e) | Tech Industry |
| 2019-05-01 | [Do you google while taking pre onsite online assessment test?](https://www.teamblind.com/post/do-you-google-while-taking-pre-onsite-online-assessment-test-3w7xwdet) | Auto |
| 2018-12-27 | [Google tag leetcode accuracy](https://www.teamblind.com/post/google-tag-leetcode-accuracy-g2bxn11i) | Tech Industry |
| 2018-11-02 | [Google Onsite. Leetcode level?](https://www.teamblind.com/post/google-onsite-leetcode-level-omsjwsqe) | Tech Industry |
| 2018-01-09 | [Google Onsite Interview. Leetcode difficulty](https://www.teamblind.com/post/google-onsite-interview-leetcode-difficulty-dctfcjfq) | Software Engineering Career |
| 2017-09-24 | [Leetcode questions level - google / Facebook](https://www.teamblind.com/post/leetcode-questions-level-google-facebook-hrjr1dax) | Housing |

### Hacker News

| Date | Post | Context |
|------|------|---------|
| 2026-08-04 | [That time when I failed the Microsoft interview](https://news.ycombinator.com/item?id=49169912) | That time when I failed the Microsoft in |
| 2026-08-04 | [That time when I failed the Microsoft interview](https://news.ycombinator.com/item?id=49167166) | That time when I failed the Microsoft in |
| 2026-06-06 | [Ask HN: Will your company be doing "LeetCode" interviews a year from now?](https://news.ycombinator.com/item?id=48420519) | story |
| 2026-06-05 | [Technical Interviews Reject the Wrong Engineers](https://news.ycombinator.com/item?id=48414386) | Technical Interviews Reject the Wrong En |
| 2026-05-31 | [The Last Technical Interview](https://news.ycombinator.com/item?id=48341805) | The Last Technical Interview |
| 2026-05-30 | [The Last Technical Interview](https://news.ycombinator.com/item?id=48339793) | The Last Technical Interview |
| 2026-05-30 | [The Last Technical Interview](https://news.ycombinator.com/item?id=48331694) | The Last Technical Interview |
| 2026-05-14 | [Rewrite Bun in Rust has been merged](https://news.ycombinator.com/item?id=48141599) | Rewrite Bun in Rust has been merged |
| 2026-04-12 | [I propose a new programming language, CPC](https://news.ycombinator.com/item?id=47735439) | story |
| 2026-03-22 | [Brute-forcing my algorithmic ignorance](https://news.ycombinator.com/item?id=47478764) | Brute-forcing my algorithmic ignorance |
| 2026-03-06 | [Tech employment now significantly worse than the 2008 or 2020 recessions](https://news.ycombinator.com/item?id=47281406) | Tech employment now significantly worse  |
| 2026-03-05 | [Stop the Interviews](https://news.ycombinator.com/item?id=47266491) | Stop the Interviews |
| 2026-02-26 | [Google Street View in 2026](https://news.ycombinator.com/item?id=47172067) | Google Street View in 2026 |
| 2026-02-26 | [Google Street View in 2026](https://news.ycombinator.com/item?id=47170161) | Google Street View in 2026 |
| 2025-12-18 | [Why Senior Engineers Fail "Google SRE" Interviews (2026 Analysis)](https://news.ycombinator.com/item?id=46314406) | story |
| 2025-11-30 | [Americans no longer see four-year college degrees as worth the cost](https://news.ycombinator.com/item?id=46094362) | Americans no longer see four-year colleg |
| 2025-11-25 | [Show HN: AlgoVoice – Voice-based mock technical interviews for L3-L4 roles](https://news.ycombinator.com/item?id=46047402) | Show HN: AlgoVoice – Voice-based mock te |
| 2025-11-20 | [RLCDev](https://news.ycombinator.com/item?id=45988586) | RLCDev |
| 2025-11-17 | [The fate of "small" open source](https://news.ycombinator.com/item?id=45952961) | The fate of "small" open source |
| 2025-11-02 | [AI Broke Interviews](https://news.ycombinator.com/item?id=45793634) | AI Broke Interviews |
| 2025-11-01 | [AI Broke Interviews](https://news.ycombinator.com/item?id=45786239) | AI Broke Interviews |
| 2025-09-14 | [Ask HN: Getting over Burnout with Imposter Syndrome](https://news.ycombinator.com/item?id=45237339) | Ask HN: Getting over Burnout with Impost |
| 2025-09-12 | [Many hard LeetCode problems are easy constraint problems](https://news.ycombinator.com/item?id=45225782) | Many hard LeetCode problems are easy con |
| 2025-09-12 | [Many hard LeetCode problems are easy constraint problems](https://news.ycombinator.com/item?id=45224301) | Many hard LeetCode problems are easy con |
| 2025-08-22 | [What is going on right now?](https://news.ycombinator.com/item?id=44984772) | What is going on right now? |
| 2025-08-11 | [An engineer's perspective on hiring](https://news.ycombinator.com/item?id=44863439) | An engineer's perspective on hiring |
| 2025-08-09 | [An engineer's perspective on hiring](https://news.ycombinator.com/item?id=44845567) | An engineer's perspective on hiring |
| 2025-08-01 | [Live coding interviews measure stress, not coding skills](https://news.ycombinator.com/item?id=44760142) | Live coding interviews measure stress, n |
| 2025-08-01 | [Live coding interviews measure stress, not coding skills](https://news.ycombinator.com/item?id=44756983) | Live coding interviews measure stress, n |
| 2025-08-01 | [Live coding interviews measure stress, not coding skills](https://news.ycombinator.com/item?id=44756578) | Live coding interviews measure stress, n |
| 2025-07-27 | [Teach Yourself Programming in Ten Years (1998)](https://news.ycombinator.com/item?id=44705133) | Teach Yourself Programming in Ten Years  |
| 2025-07-27 | [Teach Yourself Programming in Ten Years (1998)](https://news.ycombinator.com/item?id=44699328) | Teach Yourself Programming in Ten Years  |
| 2025-07-18 | [Ask HN: Bad at Interviewing](https://news.ycombinator.com/item?id=44599709) | story |
| 2025-07-09 | [Async Queue – One of my favorite programming interview questions](https://news.ycombinator.com/item?id=44507101) | Async Queue – One of my favorite program |
| 2025-07-08 | [Mercury: Ultra-fast language models based on diffusion](https://news.ycombinator.com/item?id=44497595) | Mercury: Ultra-fast language models base |
| 2025-06-22 | [How to negotiate your salary package](https://news.ycombinator.com/item?id=44349857) | How to negotiate your salary package |
| 2025-05-30 | [Show HN: Every problem and solution in Beyond Cracking the Coding Interview](https://news.ycombinator.com/item?id=44137995) | Show HN: Every problem and solution in B |
| 2025-05-16 | [Material 3 Expressive](https://news.ycombinator.com/item?id=44003899) | Material 3 Expressive |
| 2025-05-01 | [Office is too slow, so Microsoft is making it load at Windows startup](https://news.ycombinator.com/item?id=43855620) | Office is too slow, so Microsoft is maki |
| 2025-04-29 | [Ask HN: CS degrees, do they matter again?](https://news.ycombinator.com/item?id=43833350) | Ask HN: CS degrees, do they matter again |
| 2025-04-15 | [Ask HN: What Tools Did You Use to Land Your Current Job?](https://news.ycombinator.com/item?id=43696711) | Ask HN: What Tools Did You Use to Land Y |
| 2025-04-08 | [Interviewing a software engineer who prepared with AI](https://news.ycombinator.com/item?id=43624302) | Interviewing a software engineer who pre |
| 2025-04-08 | [Interviewing a software engineer who prepared with AI](https://news.ycombinator.com/item?id=43622997) | Interviewing a software engineer who pre |
| 2025-04-03 | [Ask HN: Who is hiring? (April 2025)](https://news.ycombinator.com/item?id=43566269) | Ask HN: Who is hiring? (April 2025) |
| 2025-04-02 | [The Reality of Tech Interviews in 2025](https://news.ycombinator.com/item?id=43560980) | The Reality of Tech Interviews in 2025 |
| 2025-03-20 | [Ask HN: Moving from startups (mixed success) into product?](https://news.ycombinator.com/item?id=43427736) | Ask HN: Moving from startups (mixed succ |
| 2025-03-10 | [We built a crowdsourced interview question database for tech interviews](https://news.ycombinator.com/item?id=43321014) | We built a crowdsourced interview questi |
| 2025-03-09 | [Layoffs Don't Work](https://news.ycombinator.com/item?id=43311966) | Layoffs Don't Work |
| 2025-03-07 | [Show HN: Stealth Interview](https://news.ycombinator.com/item?id=43287174) | story |
| 2025-02-26 | [TypeScript types can run DOOM [video]](https://news.ycombinator.com/item?id=43188691) | TypeScript types can run DOOM [video] |
| 2025-02-22 | [AI killed the tech interview. Now what?](https://news.ycombinator.com/item?id=43137804) | AI killed the tech interview. Now what? |
| 2025-02-07 | [The impact of AI on the technical interview process](https://news.ycombinator.com/item?id=42978231) | The impact of AI on the technical interv |
| 2025-02-05 | [Google kills diversity hiring targets](https://news.ycombinator.com/item?id=42955439) | Google kills diversity hiring targets |
| 2025-02-03 | [Ask HN: What is interviewing like now with everyone using AI?](https://news.ycombinator.com/item?id=42914690) | Ask HN: What is interviewing like now wi |
| 2025-02-03 | [Ask HN: What is interviewing like now with everyone using AI?](https://news.ycombinator.com/item?id=42914387) | Ask HN: What is interviewing like now wi |
| 2025-02-03 | [Ask HN: What is interviewing like now with everyone using AI?](https://news.ycombinator.com/item?id=42913848) | Ask HN: What is interviewing like now wi |
| 2025-01-14 | [Ask HN: How do you guard against ChatGPT use in technical interviews?](https://news.ycombinator.com/item?id=42704252) | Ask HN: How do you guard against ChatGPT |
| 2025-01-14 | [Meta announces 5% cuts in preparation for 'intense year'](https://news.ycombinator.com/item?id=42702197) | Meta announces 5% cuts in preparation fo |
| 2025-01-10 | [Meta's memo to employees rolling back DEI programs](https://news.ycombinator.com/item?id=42661782) | Meta's memo to employees rolling back DE |
| 2025-01-09 | [Scientists uncover how the brain washes itself during sleep](https://news.ycombinator.com/item?id=42649717) | Scientists uncover how the brain washes  |

_377 more not shown._

## 3) Method

Generated by [`script/scrape_lc_discuss_company.py`](../script/scrape_lc_discuss_company.py). Each source is scraped independently, cached one file per post, then all posts are pooled and scanned for LeetCode references (problem link, `LC <n>` / `#<n>`, or an exact problem title in prose).

```bash
python3 script/scrape_lc_discuss_company.py --tag google  # full run (slow)
python3 script/scrape_lc_discuss_company.py --build-only    # rebuild doc from cache
```

| Source | Endpoint | Pagination | Returns |
|--------|----------|------------|---------|
| LeetCode Discuss | `leetcode.com/graphql` — `ugcArticleDiscussionArticles`, `ugcArticleDiscussionArticle`, `topicComments` | `skip` += `first`, then one call per thread | threads + bodies + comments |
| Reddit | `reddit.com/r/<sub>/search.rss` + `<permalink>/.rss` | `after=t3_<id>`, `limit=100` | posts (full selftext) + rationed comment threads |
| Blind | `teamblind.com/search/<query>` + `/post/<slug>` (HTML) | none — several queries instead (`?page` is ignored) | search cards + full post bodies (**no comments**) |
| Hacker News | `hn.algolia.com/api/v1/search_by_date` | `page` 0..n, `hitsPerPage=100` | stories + comments |

**Gotchas worth knowing** (none of this is documented by any of these sites):

- LeetCode: introspection is disabled, `tagSlugs` is required, `content` is null in list mode, `totalNum` is capped at 3000, and — **not a typo** — `ugcArticleDiscussionArticle` takes `topicId: ID` while `topicComments` takes `topicId: Int!`. Rapid probing trips a WAF returning HTML 403s, not JSON.
- Reddit: `.json` is 403 for anonymous clients but **the `.rss` twin of the same path is not**. Search feeds page with `after=t3_<id>` and carry the full selftext, so ~12 requests fetch hundreds of posts. Comment feeds (permalink + `.rss`) are another matter: measured anonymous throughput is **~1 request per 52 s**, so they are rationed by `--reddit-comments N`. The `x-ratelimit-reset` header on a 429 is accurate and is obeyed.
- Blind: no pagination at all (`?page=2` re-serves page 1, 20 cards per query), so breadth comes from several queries. Card bodies truncate at ~312 chars, hence the per-post fetch; comments are client-rendered and unreachable. The card's exact date hides in a `title="MM/DD/YYYY"` attribute next to the human one.
- Hacker News: Algolia is the easy one — public, unauthenticated, `hitsPerPage` up to 1000. Matching is fuzzy, so expect noise.

## 4) Related docs in this repo

- [`doc/LC_google_problem_patterns_summary.md`](./LC_google_problem_patterns_summary.md)
- [`doc/goog_swe_prep_plan_claude.md`](./goog_swe_prep_plan_claude.md)
- [`doc/goog_swe_prep_plan_gpt.md`](./goog_swe_prep_plan_gpt.md)
- [`doc/goog_swe_prep_plan_gpt_v2.md`](./goog_swe_prep_plan_gpt_v2.md)
- [`doc/google_leetcode_problems_by_tags.md`](./google_leetcode_problems_by_tags.md)
- [`doc/google_swe_lc_essentials.md`](./google_swe_lc_essentials.md)

# Google SWE — Recently Asked LeetCode Questions (scraped)

> **Generated**: 2026-08-09  
> **Source**: `leetcode.com/graphql` — public Discuss API (`ugcArticleDiscussionArticles`, `ugcArticleDiscussionArticle`, `topicComments`)  
> **Regenerate**: `python3 script/scrape_lc_discuss_company.py --tag google`  
> **Corpus**: 274 `google`-tagged discuss posts, 2026-01-23 → 2026-08-09 (274 full bodies, 1153 comments)

## ⚠️ Read this first — what this data is and is not

- LeetCode's **official company tag list** (`companyTag`) is **Premium-gated** and returns `null` for anonymous requests. This doc is **not** that list.
- What is scraped here is **user-reported interview experience** from the public Discuss forum, tagged `google`. It is self-reported, unverified, and skewed toward whoever bothers to post.
- The **legacy** discuss API (`categoryTopicList`, category `interview-question`) is frozen at **2025-03-04** — LeetCode migrated Discuss during 2025. Anything claiming to scrape "recent" questions from that endpoint is serving stale data.
- Treat problem counts as **weak signal** (mention frequency), not ground-truth interview frequency. A single well-linked compilation post can put a dozen problems on the board at once.
- Mentions are **not all interview reports** — some describe a practice routine, and a title match can even land inside a sentence saying the problem is *not* what was asked. The `Match` column and the quotes exist so you can check.

## 1) Most-referenced LC problems

**`Posts`** = number of **distinct discuss threads** referencing the problem anywhere in `title + summary + body + comments`. It counts threads, not mentions: a thread naming the same problem five times counts once, so `Posts` is *not* the sum of the quotes below.

`Match` = how the reference was found, showing the **strongest** evidence anywhere in that thread set. **url** = the post linked `leetcode.com/problems/<slug>` (high confidence); **num** = wrote `LC 200` / `#200`; **title** = the exact title appeared in prose — weakest, worth eyeballing the quote before trusting it.

The table below is **complete** — every problem extracted from the corpus is listed.

| # | Problem | Diff | Type / Tags | Posts | Match | Last seen | In repo? |
|---|---------|------|-------------|-------|-------|-----------|----------|
| 200 | [Number of Islands](https://leetcode.com/problems/number-of-islands/) | Medium | Array, Depth-First Search, Breadth-First Search | 4 | url | 2026-05-11 | ✅ |
| 207 | [Course Schedule](https://leetcode.com/problems/course-schedule/) | Medium | Depth-First Search, Breadth-First Search, Graph | 2 | url | 2026-05-18 | ✅ |
| 1944 | [Number of Visible People in a Queue](https://leetcode.com/problems/number-of-visible-people-in-a-queue/) | Hard | array, stack, monotonic-stack | 2 | url | 2026-05-11 | ✅ |
| 23 | [Merge k Sorted Lists](https://leetcode.com/problems/merge-k-sorted-lists/) | Hard | Linked List, Divide and Conquer, Heap (Priority Queue) | 2 | url | 2026-05-11 | ✅ |
| 300 | [Longest Increasing Subsequence](https://leetcode.com/problems/longest-increasing-subsequence/) | Medium | Array, Binary Search, Dynamic Programming | 2 | url | 2026-05-11 | ✅ |
| 210 | [Course Schedule II](https://leetcode.com/problems/course-schedule-ii/) | Medium | Depth-First Search, Breadth-First Search, Graph | 2 | url | 2026-05-18 | ✅ |
| 239 | [Sliding Window Maximum](https://leetcode.com/problems/sliding-window-maximum/) | Hard | Array, Queue, Sliding Window | 1 | url | 2026-05-11 | ✅ |
| 354 | [Russian Doll Envelopes](https://leetcode.com/problems/russian-doll-envelopes/) | Hard | Array, Binary Search, Dynamic Programming | 1 | url | 2026-05-11 | ✅ |
| 4 | [Median of Two Sorted Arrays](https://leetcode.com/problems/median-of-two-sorted-arrays/) | Hard | Array, Binary Search, Divide and Conquer | 1 | url | 2026-05-11 | ✅ |
| 10 | [Regular Expression Matching](https://leetcode.com/problems/regular-expression-matching/) | Hard | String, Dynamic Programming, Recursion | 1 | url | 2026-05-11 | ✅ |
| 31 | [Next Permutation](https://leetcode.com/problems/next-permutation/) | Medium | — | 1 | url | 2026-05-11 | — |
| 33 | [Search in Rotated Sorted Array](https://leetcode.com/problems/search-in-rotated-sorted-array/) | Medium | Array, Binary Search | 1 | url | 2026-05-11 | ✅ |
| 34 | [Find First and Last Position of Element in Sorted Array](https://leetcode.com/problems/find-first-and-last-position-of-element-in-sorted-array/) | Medium | Array, Binary Search | 1 | url | 2026-05-11 | ✅ |
| 60 | [Permutation Sequence](https://leetcode.com/problems/permutation-sequence/) | Hard | — | 1 | url | 2026-02-10 | — |
| 84 | [Largest Rectangle in Histogram](https://leetcode.com/problems/largest-rectangle-in-histogram/) | Hard | Array, Stack, Monotonic Stack | 1 | url | 2026-05-11 | ✅ |
| 85 | [Maximal Rectangle](https://leetcode.com/problems/maximal-rectangle/) | Hard | Array, Dynamic Programming, Stack | 1 | url | 2026-05-11 | ✅ |
| 93 | [Restore IP Addresses](https://leetcode.com/problems/restore-ip-addresses/) | Medium | String, Backtracking | 1 | url | 2026-02-28 | ✅ |
| 128 | [Longest Consecutive Sequence](https://leetcode.com/problems/longest-consecutive-sequence/) | Medium | Array, Hash Table, Union Find | 1 | url | 2026-05-11 | ✅ |
| 253 | [Meeting Rooms II](https://leetcode.com/problems/meeting-rooms-ii/) 🔒 | Medium | Array, Two Pointers, Greedy | 1 | url | 2026-05-11 | ✅ |
| 410 | [Split Array Largest Sum](https://leetcode.com/problems/split-array-largest-sum/) | Hard | Array, Binary Search, Dynamic Programming | 1 | url | 2026-05-11 | ✅ |
| 424 | [Longest Repeating Character Replacement](https://leetcode.com/problems/longest-repeating-character-replacement/) | Medium | hash-table, string, sliding-window | 1 | url | 2026-05-11 | ✅ |
| 560 | [Subarray Sum Equals K](https://leetcode.com/problems/subarray-sum-equals-k/) | Medium | Array, Hash Table, Prefix Sum | 1 | url | 2026-05-11 | ✅ |
| 875 | [Koko Eating Bananas](https://leetcode.com/problems/koko-eating-bananas/) | Medium | Array, Binary Search | 1 | url | 2026-05-11 | ✅ |
| 3 | [Longest Substring Without Repeating Characters](https://leetcode.com/problems/longest-substring-without-repeating-characters/) | Medium | Hash Table, String, Sliding Window | 1 | url | 2026-05-11 | ✅ |
| 5 | [Longest Palindromic Substring](https://leetcode.com/problems/longest-palindromic-substring/) | Medium | String, Dynamic Programming | 1 | url | 2026-05-11 | ✅ |
| 6 | [Zigzag Conversion](https://leetcode.com/problems/zigzag-conversion/) | Medium | String | 1 | num | 2026-03-25 | ✅ |
| 37 | [Sudoku Solver](https://leetcode.com/problems/sudoku-solver/) | Hard | Array, Backtracking, Matrix | 1 | url | 2026-05-11 | ✅ |
| 42 | [Trapping Rain Water](https://leetcode.com/problems/trapping-rain-water/) | Hard | Array, Two Pointers, Dynamic Programming | 1 | url | 2026-05-11 | ✅ |
| 48 | [Rotate Image](https://leetcode.com/problems/rotate-image/) | Medium | Array, Math, Matrix | 1 | url | 2026-05-11 | ✅ |
| 72 | [Edit Distance](https://leetcode.com/problems/edit-distance/) | Medium | String, Dynamic Programming | 1 | url | 2026-05-11 | ✅ |
| 146 | [LRU Cache](https://leetcode.com/problems/lru-cache/) | Medium | Hash Table, Linked List, Design | 1 | url | 2026-05-11 | ✅ |
| 286 | [Walls and Gates](https://leetcode.com/problems/walls-and-gates/) 🔒 | Medium | Array, Breadth-First Search, Matrix | 1 | title | 2026-04-12 | ✅ |
| 351 | [Android Unlock Patterns](https://leetcode.com/problems/android-unlock-patterns/) 🔒 | Medium | dynamic-programming, backtracking, array | 1 | url | 2026-02-08 | ✅ |
| 394 | [Decode String](https://leetcode.com/problems/decode-string/) | Medium | String, Stack, Recursion | 1 | url | 2026-05-11 | ✅ |
| 994 | [Rotting Oranges](https://leetcode.com/problems/rotting-oranges/) | Medium | Array, Breadth-First Search, Matrix | 1 | title | 2026-04-12 | ✅ |
| 1970 | [Last Day Where You Can Still Cross](https://leetcode.com/problems/last-day-where-you-can-still-cross/) | Hard | — | 1 | url | 2026-02-08 | — |
| 2013 | [Detect Squares](https://leetcode.com/problems/detect-squares/) | Medium | Array, Hash Table, Design | 1 | title | 2026-02-07 | ✅ |
| 2402 | [Meeting Rooms III](https://leetcode.com/problems/meeting-rooms-iii/) | Hard | — | 1 | url | 2026-03-25 | — |
| 2812 | [Find the Safest Path in a Grid](https://leetcode.com/problems/find-the-safest-path-in-a-grid/) | Medium | — | 1 | url | 2026-02-08 | — |
| 3169 | [Count Days Without Meetings](https://leetcode.com/problems/count-days-without-meetings/) | Medium | — | 1 | num | 2026-02-04 | — |
| 3671 | [Sum of Beautiful Subsequences](https://leetcode.com/problems/sum-of-beautiful-subsequences/) | Hard | — | 1 | url | 2026-02-05 | — |
| 3676 | [Count Bowl Subarrays](https://leetcode.com/problems/count-bowl-subarrays/) | Medium | — | 1 | url | 2026-02-05 | — |
| 3677 | [Count Binary Palindromic Numbers](https://leetcode.com/problems/count-binary-palindromic-numbers/) | Hard | — | 1 | url | 2026-02-05 | — |
| 3680 | [Generate Schedule](https://leetcode.com/problems/generate-schedule/) | Medium | — | 1 | url | 2026-02-05 | — |
| 3681 | [Maximum XOR of Subsequences](https://leetcode.com/problems/maximum-xor-of-subsequences/) | Hard | — | 1 | url | 2026-02-05 | — |
| 3685 | [Subsequence Sum After Capping Elements](https://leetcode.com/problems/subsequence-sum-after-capping-elements/) | Medium | — | 1 | url | 2026-02-05 | — |
| 3686 | [Number of Stable Subsequences](https://leetcode.com/problems/number-of-stable-subsequences/) | Hard | — | 1 | url | 2026-02-05 | — |
| 3690 | [Split and Merge Array Transformation](https://leetcode.com/problems/split-and-merge-array-transformation/) | Medium | — | 1 | url | 2026-02-05 | — |
| 3691 | [Maximum Total Subarray Value II](https://leetcode.com/problems/maximum-total-subarray-value-ii/) | Hard | — | 1 | url | 2026-02-05 | — |
| 3695 | [Maximize Alternating Sum Using Swaps](https://leetcode.com/problems/maximize-alternating-sum-using-swaps/) | Hard | — | 1 | url | 2026-02-05 | — |
| 3699 | [Number of ZigZag Arrays I](https://leetcode.com/problems/number-of-zigzag-arrays-i/) | Hard | — | 1 | url | 2026-02-05 | — |
| 3700 | [Number of ZigZag Arrays II](https://leetcode.com/problems/number-of-zigzag-arrays-ii/) | Hard | — | 1 | url | 2026-02-05 | — |
| 3703 | [Remove K-Balanced Substrings](https://leetcode.com/problems/remove-k-balanced-substrings/) | Medium | — | 1 | url | 2026-02-05 | — |
| 3704 | [Count No-Zero Pairs That Sum to N](https://leetcode.com/problems/count-no-zero-pairs-that-sum-to-n/) | Hard | — | 1 | url | 2026-02-05 | — |
| 3709 | [Design Exam Scores Tracker](https://leetcode.com/problems/design-exam-scores-tracker/) | Medium | — | 1 | url | 2026-02-05 | — |
| 3710 | [Maximum Partition Factor](https://leetcode.com/problems/maximum-partition-factor/) | Hard | — | 1 | url | 2026-02-05 | — |
| 3714 | [Longest Balanced Substring II](https://leetcode.com/problems/longest-balanced-substring-ii/) | Medium | — | 1 | url | 2026-02-05 | — |
| 3715 | [Sum of Perfect Square Ancestors](https://leetcode.com/problems/sum-of-perfect-square-ancestors/) | Hard | — | 1 | url | 2026-02-05 | — |
| 3720 | [Lexicographically Smallest Permutation Greater Than Target](https://leetcode.com/problems/lexicographically-smallest-permutation-greater-than-target/) | Medium | — | 1 | url | 2026-02-05 | — |
| 3721 | [Longest Balanced Subarray II](https://leetcode.com/problems/longest-balanced-subarray-ii/) | Hard | — | 1 | url | 2026-02-05 | — |
| 3724 | [Minimum Operations to Transform Array](https://leetcode.com/problems/minimum-operations-to-transform-array/) | Medium | — | 1 | url | 2026-02-05 | — |
| 3733 | [Minimum Time to Complete All Deliveries](https://leetcode.com/problems/minimum-time-to-complete-all-deliveries/) | Medium | — | 1 | url | 2026-02-05 | — |
| 3738 | [Longest Non-Decreasing Subarray After Replacing at Most One Element](https://leetcode.com/problems/longest-non-decreasing-subarray-after-replacing-at-most-one-element/) | Medium | — | 1 | url | 2026-02-05 | — |
| 3739 | [Count Subarrays With Majority Element II](https://leetcode.com/problems/count-subarrays-with-majority-element-ii/) | Hard | — | 1 | url | 2026-02-05 | — |
| 3742 | [Maximum Path Score in a Grid](https://leetcode.com/problems/maximum-path-score-in-a-grid/) | Medium | — | 1 | url | 2026-02-05 | — |
| 3743 | [Maximize Cyclic Partition Score](https://leetcode.com/problems/maximize-cyclic-partition-score/) | Hard | — | 1 | url | 2026-02-05 | — |
| 3747 | [Count Distinct Integers After Removing Zeros](https://leetcode.com/problems/count-distinct-integers-after-removing-zeros/) | Medium | — | 1 | url | 2026-02-05 | — |
| 3748 | [Count Stable Subarrays](https://leetcode.com/problems/count-stable-subarrays/) | Hard | — | 1 | url | 2026-02-05 | — |
| 3753 | [Total Waviness of Numbers in Range II](https://leetcode.com/problems/total-waviness-of-numbers-in-range-ii/) | Hard | — | 1 | url | 2026-02-05 | — |
| 3757 | [Number of Effective Subsequences](https://leetcode.com/problems/number-of-effective-subsequences/) | Hard | — | 1 | url | 2026-02-05 | — |
| 3761 | [Minimum Absolute Distance Between Mirror Pairs](https://leetcode.com/problems/minimum-absolute-distance-between-mirror-pairs/) | Medium | — | 1 | url | 2026-02-05 | — |
| 3762 | [Minimum Operations to Equalize Subarrays](https://leetcode.com/problems/minimum-operations-to-equalize-subarrays/) | Hard | — | 1 | url | 2026-02-05 | — |
| 3767 | [Maximize Points After Choosing K Tasks](https://leetcode.com/problems/maximize-points-after-choosing-k-tasks/) | Medium | — | 1 | url | 2026-02-05 | — |
| 3771 | [Total Score of Dungeon Runs](https://leetcode.com/problems/total-score-of-dungeon-runs/) | Medium | — | 1 | url | 2026-02-05 | — |
| 3772 | [Maximum Subgraph Score in a Tree](https://leetcode.com/problems/maximum-subgraph-score-in-a-tree/) | Hard | — | 1 | url | 2026-02-05 | — |
| 3776 | [Minimum Moves to Balance Circular Array](https://leetcode.com/problems/minimum-moves-to-balance-circular-array/) | Medium | — | 1 | url | 2026-02-05 | — |
| 3777 | [Minimum Deletions to Make Alternating Substring](https://leetcode.com/problems/minimum-deletions-to-make-alternating-substring/) | Hard | — | 1 | url | 2026-02-05 | — |
| 3781 | [Maximum Score After Binary Swaps](https://leetcode.com/problems/maximum-score-after-binary-swaps/) | Medium | — | 1 | url | 2026-02-05 | — |
| 3785 | [Minimum Swaps to Avoid Forbidden Values](https://leetcode.com/problems/minimum-swaps-to-avoid-forbidden-values/) | Hard | — | 1 | url | 2026-02-05 | — |
| 3786 | [Total Sum of Interaction Cost in Tree Groups](https://leetcode.com/problems/total-sum-of-interaction-cost-in-tree-groups/) | Hard | — | 1 | url | 2026-02-05 | — |
| 3790 | [Smallest All-Ones Multiple](https://leetcode.com/problems/smallest-all-ones-multiple/) | Medium | — | 1 | url | 2026-02-05 | — |
| 3791 | [Number of Balanced Integers in a Range](https://leetcode.com/problems/number-of-balanced-integers-in-a-range/) | Hard | — | 1 | url | 2026-02-05 | — |
| 3796 | [Find Maximum Value in a Constrained Sequence](https://leetcode.com/problems/find-maximum-value-in-a-constrained-sequence/) | Medium | — | 1 | url | 2026-02-05 | — |
| 3797 | [Count Routes to Climb a Rectangular Grid](https://leetcode.com/problems/count-routes-to-climb-a-rectangular-grid/) | Hard | — | 1 | url | 2026-02-05 | — |
| 3800 | [Minimum Cost to Make Two Binary Strings Equal](https://leetcode.com/problems/minimum-cost-to-make-two-binary-strings-equal/) | Medium | — | 1 | url | 2026-02-05 | — |
| 3801 | [Minimum Cost to Merge Sorted Lists](https://leetcode.com/problems/minimum-cost-to-merge-sorted-lists/) | Hard | — | 1 | url | 2026-02-05 | — |
| 3805 | [Count Caesar Cipher Pairs](https://leetcode.com/problems/count-caesar-cipher-pairs/) | Medium | — | 1 | url | 2026-02-05 | — |
| 3806 | [Maximum Bitwise AND After Increment Operations](https://leetcode.com/problems/maximum-bitwise-and-after-increment-operations/) | Hard | — | 1 | url | 2026-02-05 | — |
| 3811 | [Number of Alternating XOR Partitions](https://leetcode.com/problems/number-of-alternating-xor-partitions/) | Medium | — | 1 | url | 2026-02-05 | — |
| 3812 | [Minimum Edge Toggles on a Tree](https://leetcode.com/problems/minimum-edge-toggles-on-a-tree/) | Hard | — | 1 | url | 2026-02-05 | — |
| 3815 | [Design Auction System](https://leetcode.com/problems/design-auction-system/) | Medium | — | 1 | url | 2026-02-05 | — |
| 3816 | [Lexicographically Smallest String After Deleting Duplicate Characters](https://leetcode.com/problems/lexicographically-smallest-string-after-deleting-duplicate-characters/) | Hard | — | 1 | url | 2026-02-05 | — |
| 3820 | [Pythagorean Distance Nodes in a Tree](https://leetcode.com/problems/pythagorean-distance-nodes-in-a-tree/) | Medium | — | 1 | url | 2026-02-05 | — |
| 3821 | [Find Nth Smallest Integer With K One Bits](https://leetcode.com/problems/find-nth-smallest-integer-with-k-one-bits/) | Hard | — | 1 | url | 2026-02-05 | — |
| 3825 | [Longest Strictly Increasing Subsequence With Non-Zero Bitwise AND](https://leetcode.com/problems/longest-strictly-increasing-subsequence-with-non-zero-bitwise-and/) | Medium | — | 1 | url | 2026-02-05 | — |
| 3826 | [Minimum Partition Score](https://leetcode.com/problems/minimum-partition-score/) | Hard | — | 1 | url | 2026-02-05 | — |
| 3829 | [Design Ride Sharing System](https://leetcode.com/problems/design-ride-sharing-system/) | Medium | — | 1 | url | 2026-02-05 | — |
| 3830 | [Longest Alternating Subarray After Removing At Most One Element](https://leetcode.com/problems/longest-alternating-subarray-after-removing-at-most-one-element/) | Hard | — | 1 | url | 2026-02-05 | — |
| 22 | [Generate Parentheses](https://leetcode.com/problems/generate-parentheses/) | Medium | String, Dynamic Programming, Backtracking | 1 | url | 2026-02-28 | ✅ |
| 47 | [Permutations II](https://leetcode.com/problems/permutations-ii/) | Medium | — | 1 | num | 2026-02-06 | — |
| 56 | [Merge Intervals](https://leetcode.com/problems/merge-intervals/) | Medium | Array, Sorting | 1 | title | 2026-04-11 | ✅ |
| 303 | [Range Sum Query - Immutable](https://leetcode.com/problems/range-sum-query-immutable/) | Easy | Immutable - Array, Design, Prefix Sum | 1 | title | 2026-03-04 | ✅ |
| 307 | [Range Sum Query - Mutable](https://leetcode.com/problems/range-sum-query-mutable/) | Medium | Mutable - Array, Design, Binary Indexed Tree | 1 | title | 2026-03-04 | ✅ |
| 355 | [Design Twitter](https://leetcode.com/problems/design-twitter/) | Medium | — | 1 | title | 2026-02-07 | — |
| 359 | [Logger Rate Limiter](https://leetcode.com/problems/logger-rate-limiter/) 🔒 | Easy | Hash Table, Design | 1 | title | 2026-02-26 | ✅ |
| 809 | [Expressive Words](https://leetcode.com/problems/expressive-words/) | Medium | Array, Two Pointers, String | 1 | url | 2026-04-10 | ✅ |
| 907 | [Sum of Subarray Minimums](https://leetcode.com/problems/sum-of-subarray-minimums/) | Medium | Array, Dynamic Programming, Stack | 1 | url | 2026-03-28 | ✅ |
| 1277 | [Count Square Submatrices with All Ones](https://leetcode.com/problems/count-square-submatrices-with-all-ones/) | Medium | array, dynamic-programming, matrix | 1 | url | 2026-02-07 | ✅ |
| 1671 | [Minimum Number of Removals to Make Mountain Array](https://leetcode.com/problems/minimum-number-of-removals-to-make-mountain-array/) | Hard | — | 1 | url | 2026-02-11 | — |
| 1937 | [Maximum Number of Points with Cost](https://leetcode.com/problems/maximum-number-of-points-with-cost/) | Medium | array, dynamic-programming, stack | 1 | url | 2026-02-24 | ✅ |
| 2026 | [Low-Quality Problems](https://leetcode.com/problems/low-quality-problems/) 🔒 | Easy | — | 1 | num | 2026-02-07 | — |
| 2093 | [Minimum Cost to Reach City With Discounts](https://leetcode.com/problems/minimum-cost-to-reach-city-with-discounts/) 🔒 | Medium | — | 1 | url | 2026-05-22 | — |
| 2615 | [Sum of Distances](https://leetcode.com/problems/sum-of-distances/) | Medium | — | 1 | title | 2026-03-27 | — |
| 2810 | [Faulty Keyboard](https://leetcode.com/problems/faulty-keyboard/) | Easy | — | 1 | title | 2026-04-10 | — |
| 3481 | [Apply Substitutions](https://leetcode.com/problems/apply-substitutions/) 🔒 | Medium | — | 1 | title | 2026-05-14 | — |
| 3670 | [Maximum Product of Two Integers With No Common Bits](https://leetcode.com/problems/maximum-product-of-two-integers-with-no-common-bits/) | Medium | — | 1 | url | 2026-02-05 | — |
| 3694 | [Distinct Points Reachable After Substring Removal](https://leetcode.com/problems/distinct-points-reachable-after-substring-removal/) | Medium | — | 1 | url | 2026-02-05 | — |
| 3725 | [Count Ways to Choose Coprime Integers from Rows](https://leetcode.com/problems/count-ways-to-choose-coprime-integers-from-rows/) | Hard | — | 1 | url | 2026-02-05 | — |
| 3728 | [Stable Subarrays With Equal Boundary and Interior Sum](https://leetcode.com/problems/stable-subarrays-with-equal-boundary-and-interior-sum/) | Medium | — | 1 | url | 2026-02-05 | — |
| 3729 | [Count Distinct Subarrays Divisible by K in Sorted Array](https://leetcode.com/problems/count-distinct-subarrays-divisible-by-k-in-sorted-array/) | Hard | — | 1 | url | 2026-02-05 | — |
| 3734 | [Lexicographically Smallest Palindromic Permutation Greater Than Target](https://leetcode.com/problems/lexicographically-smallest-palindromic-permutation-greater-than-target/) | Hard | — | 1 | url | 2026-02-05 | — |
| 3752 | [Lexicographically Smallest Negated Permutation that Sums to Target](https://leetcode.com/problems/lexicographically-smallest-negated-permutation-that-sums-to-target/) | Medium | — | 1 | url | 2026-02-05 | — |
| 3756 | [Concatenate Non-Zero Digits and Multiply by Sum II](https://leetcode.com/problems/concatenate-non-zero-digits-and-multiply-by-sum-ii/) | Medium | — | 1 | url | 2026-02-05 | — |
| 3768 | [Minimum Inversion Count in Subarrays of Fixed Length](https://leetcode.com/problems/minimum-inversion-count-in-subarrays-of-fixed-length/) | Hard | — | 1 | url | 2026-02-05 | — |
| 3782 | [Last Remaining Integer After Alternating Deletion Operations](https://leetcode.com/problems/last-remaining-integer-after-alternating-deletion-operations/) | Hard | — | 1 | url | 2026-02-05 | — |

### Evidence (quotes from the scraped posts)

**This is a sample, not a full audit trail.** It covers the top 25 problems of 125, with at most 3 quotes each (one per thread, from the first match in that thread). Where a problem has more threads than quotes shown, the surplus is noted inline. For the rest, follow the links in the table and section 2.

**LC 200 — Number of Islands** (Medium) · 4 threads — _1 further thread not quoted_  
- _2026-05-11_ · [Google Phone Interview Questions](https://leetcode.com/discuss/post/8195929/google-phone-interview-questions-by-anon-mk7j/)  
  > …problems/russian-doll-envelopes/) * Number of Islands [#200](https://leetcode.com/problems/number-of-islands/) * Koko Eating Bananas [#875](https://leetcode.com/problems/koko-eating-bananas/) * Nex…
- _2026-04-22_ · [# 🔥 Top Google DSA Questions I Practiced (With Approach)](https://leetcode.com/discuss/post/8054808/top-google-dsa-questions-i-practiced-wit-quup/)  
  > …o Sum → HashMap Subarray Sum = K → Prefix Sum Graph Word Ladder → BFS Number of Islands → Hi everyone 👋 I am currently preparing for product-based companies like Google, and I wanted to share som…
- _2026-04-12_ · [I have rotting oranges now. 994. Rotten Oranges — finally clicked.](https://leetcode.com/discuss/post/7883464/i-have-rotting-oranges-now-994-rotten-or-t4bw/)  
  > …Rotten Oranges after 3 days of struggling with BFSSpent days stuck on Number of Islands, then this problem hit me again like wtf.But the whole thing clicked with just one line:This line freezes which…

**LC 207 — Course Schedule** (Medium) · 2 threads  
- _2026-05-18_ · [Google L4 Interview || Reject](https://leetcode.com/discuss/post/8265899/google-l4-interview-reject-by-anonymous_-xsc3/)  
  > …Sort Question very similar to the below problems.Question 1 : https://leetcode.com/problems/course-schedule/description/ Question 2 : https://leetcode.com/problems/ # Google L4 Interview Location : B…
- _2026-05-11_ · [Google Phone Interview Questions](https://leetcode.com/discuss/post/8195929/google-phone-interview-questions-by-anon-mk7j/)  
  > …eetcode.com/problems/rotate-image/) * Course Schedule [#207](https://leetcode.com/problems/course-schedule/) * Regular Expression Matching [#10](https://leetcode.com/problems/regular-expression-matc…

**LC 1944 — Number of Visible People in a Queue** (Hard) · 2 threads  
- _2026-05-11_ · [Google Phone Interview Questions](https://leetcode.com/discuss/post/8195929/google-phone-interview-questions-by-anon-mk7j/)  
  > …ecode-string/) * Number of Visible People in a Queue [#1944](https://leetcode.com/problems/number-of-visible-people-in-a-queue/) * LRU Cache [#146](https://leetcode.com/problems/lru-cache/) * Meeti…
- _2026-03-04_ · [Interview Experience: Google | L3 Web Solutions Engineer (GTech)](https://leetcode.com/discuss/post/7624355/interview-experience-google-l3-web-solut-ger7/)  
  > …sounds similar to [1944. Number of Visible People in a Queue](https://leetcode.com/problems/number-of-visible-people-in-a-queue/description/).\n for section 3\n```\n#include <bits/stdc++.h>\nusing nam…

**LC 23 — Merge k Sorted Lists** (Hard) · 2 threads  
- _2026-05-11_ · [Google Phone Interview Questions](https://leetcode.com/discuss/post/8195929/google-phone-interview-questions-by-anon-mk7j/)  
  > …oblems/sliding-window-maximum/) * Merge k Sorted Lists [#23](https://leetcode.com/problems/merge-k-sorted-lists/) Some questions are the closest that it can get to the actual question. Especially LR…
- _2026-04-22_ · [# 🔥 Top Google DSA Questions I Practiced (With Approach)](https://leetcode.com/discuss/post/8054808/top-google-dsa-questions-i-practiced-wit-quup/)  
  > …S * Number of Islands → DFS --- ## Heap / Priority Queue * Merge K Sorted Lists --- ## Dynamic Programming * Longest Increasing Subsequence * 0/1 Knapsack --- ## My Learni…

**LC 300 — Longest Increasing Subsequence** (Medium) · 2 threads  
- _2026-05-11_ · [Google Phone Interview Questions](https://leetcode.com/discuss/post/8195929/google-phone-interview-questions-by-anon-mk7j/)  
  > …lems/edit-distance/) * Longest Increasing Subsequence [#300](https://leetcode.com/problems/longest-increasing-subsequence/) * Split Array Largest Sum [#410](https://leetcode.com/problems/split-array…
- _2026-04-22_ · [# 🔥 Top Google DSA Questions I Practiced (With Approach)](https://leetcode.com/discuss/post/8054808/top-google-dsa-questions-i-practiced-wit-quup/)  
  > …ueue * Merge K Sorted Lists --- ## Dynamic Programming * Longest Increasing Subsequence * 0/1 Knapsack --- ## My Learning * Most Google questions focus on **Graph + DP + Optimi…

**LC 210 — Course Schedule II** (Medium) · 2 threads  
- _2026-05-18_ · [Google L4 Interview || Reject](https://leetcode.com/discuss/post/8265899/google-l4-interview-reject-by-anonymous_-xsc3/)  
  > …etcode.com/problems/course-schedule/description/ Question 2 : https://leetcode.com/problems/course-schedule-ii/description/ ## Round 2 : Standard Behavioral questions. The below link helped me a lot…
- _2026-03-28_ · [L4 DSA Round Question](https://leetcode.com/discuss/post/7705895/google-l4-dsa-round-question-by-anonymou-nh2t/)  
  > …ave been successfully compiled. This problem is similar to https://leetcode.com/problems/course-schedule-ii/description/, but with the added complexity of multithreading. Points to consider:…

**LC 239 — Sliding Window Maximum** (Hard) · 1 thread  
- _2026-05-11_ · [Google Phone Interview Questions](https://leetcode.com/discuss/post/8195929/google-phone-interview-questions-by-anon-mk7j/)  
  > …ms/split-array-largest-sum/) * Sliding Window Maximum [#239](https://leetcode.com/problems/sliding-window-maximum/) * Merge k Sorted Lists [#23](https://leetcode.com/problems/merge-k-sorted-lists/)…

**LC 354 — Russian Doll Envelopes** (Hard) · 1 thread  
- _2026-05-11_ · [Google Phone Interview Questions](https://leetcode.com/discuss/post/8195929/google-phone-interview-questions-by-anon-mk7j/)  
  > …lems/subarray-sum-equals-k/) * Russian Doll Envelopes [#354](https://leetcode.com/problems/russian-doll-envelopes/) * Number of Islands [#200](https://leetcode.com/problems/number-of-islands/) * Ko…

**LC 4 — Median of Two Sorted Arrays** (Hard) · 1 thread  
- _2026-05-11_ · [Google Phone Interview Questions](https://leetcode.com/discuss/post/8195929/google-phone-interview-questions-by-anon-mk7j/)  
  > …e too. Here is the list: * Median of Two Sorted Arrays [#4](https://leetcode.com/problems/median-of-two-sorted-arrays/) * Trapping Rain Water [#42](https://leetcode.com/problems/trapping-rain-water…

**LC 10 — Regular Expression Matching** (Hard) · 1 thread  
- _2026-05-11_ · [Google Phone Interview Questions](https://leetcode.com/discuss/post/8195929/google-phone-interview-questions-by-anon-mk7j/)  
  > …oblems/course-schedule/) * Regular Expression Matching [#10](https://leetcode.com/problems/regular-expression-matching/) * Sudoku Solver [#37](https://leetcode.com/problems/sudoku-solver/) * Edit D…

**LC 31 — Next Permutation** (Medium) · 1 thread  
- _2026-05-11_ · [Google Phone Interview Questions](https://leetcode.com/discuss/post/8195929/google-phone-interview-questions-by-anon-mk7j/)  
  > ….com/problems/koko-eating-bananas/) * Next Permutation [#31](https://leetcode.com/problems/next-permutation/) * Search in Rotated Sorted Array [#33](https://leetcode.com/problems/search-in-rotated-s…

**LC 33 — Search in Rotated Sorted Array** (Medium) · 1 thread  
- _2026-05-11_ · [Google Phone Interview Questions](https://leetcode.com/discuss/post/8195929/google-phone-interview-questions-by-anon-mk7j/)  
  > …ms/next-permutation/) * Search in Rotated Sorted Array [#33](https://leetcode.com/problems/search-in-rotated-sorted-array/) * Decode String [#394](https://leetcode.com/problems/decode-string/) * Nu…

**LC 34 — Find First and Last Position of Element in Sorted Array** (Medium) · 1 thread  
- _2026-05-11_ · [Google Phone Interview Questions](https://leetcode.com/discuss/post/8195929/google-phone-interview-questions-by-anon-mk7j/)  
  > …Find First and Last Position of Element in Sorted Array [#34](https://leetcode.com/problems/find-first-and-last-position-of-element-in-sorted-array/) * Maximal Rectangle [#85](https://leetcode.com/pr…

**LC 60 — Permutation Sequence** (Hard) · 1 thread  
- _2026-02-10_ · [[Interview Experience] Sharing First In-Person Google Onsite After Yea](https://leetcode.com/discuss/post/7567827/interview-experience-sharing-first-in-pe-oyg1/)  
  > …sked a problem similiar to this permutation sequence problem: https://leetcode.com/problems/permutation-sequence/description/ It's not the exact same but if you know how to solve this one, you can…

**LC 84 — Largest Rectangle in Histogram** (Hard) · 1 thread  
- _2026-05-11_ · [Google Phone Interview Questions](https://leetcode.com/discuss/post/8195929/google-phone-interview-questions-by-anon-mk7j/)  
  > …s/maximal-rectangle/) * Largest Rectangle in Histogram [#84](https://leetcode.com/problems/largest-rectangle-in-histogram/) * Rotate Image [#48](https://leetcode.com/problems/rotate-image/) * Cours…

**LC 85 — Maximal Rectangle** (Hard) · 1 thread  
- _2026-05-11_ · [Google Phone Interview Questions](https://leetcode.com/discuss/post/8195929/google-phone-interview-questions-by-anon-mk7j/)  
  > …ition-of-element-in-sorted-array/) * Maximal Rectangle [#85](https://leetcode.com/problems/maximal-rectangle/) * Largest Rectangle in Histogram [#84](https://leetcode.com/problems/largest-rectangle-…

**LC 93 — Restore IP Addresses** (Medium) · 1 thread  
- _2026-02-28_ · [Understanding Time Complexity for Backtracking with Pruning](https://leetcode.com/discuss/post/7614127/understanding-time-complexity-for-backtr-rn7u/)  
  > …tps://leetcode.com/problems/generate-parentheses/description/ https://leetcode.com/problems/restore-ip-addresses/description/ In problems like Restore IP Addresses, the maximum length is constant, so…

**LC 128 — Longest Consecutive Sequence** (Medium) · 1 thread  
- _2026-05-11_ · [Google Phone Interview Questions](https://leetcode.com/discuss/post/8195929/google-phone-interview-questions-by-anon-mk7j/)  
  > …alindromic-substring/) * Longest Consecutive Sequence [#128](https://leetcode.com/problems/longest-consecutive-sequence/) * Subarray Sum Equals K [#560](https://leetcode.com/problems/subarray-sum-eq…

**LC 253 — Meeting Rooms II** (Medium) · 1 thread  
- _2026-05-11_ · [Google Phone Interview Questions](https://leetcode.com/discuss/post/8195929/google-phone-interview-questions-by-anon-mk7j/)  
  > …/leetcode.com/problems/lru-cache/) * Meeting Rooms II [#253](https://leetcode.com/problems/meeting-rooms-ii/) * Longest Repeating Character Replacement [#424](https://leetcode.com/problems/longest-r…

**LC 410 — Split Array Largest Sum** (Hard) · 1 thread  
- _2026-05-11_ · [Google Phone Interview Questions](https://leetcode.com/discuss/post/8195929/google-phone-interview-questions-by-anon-mk7j/)  
  > …st-increasing-subsequence/) * Split Array Largest Sum [#410](https://leetcode.com/problems/split-array-largest-sum/) * Sliding Window Maximum [#239](https://leetcode.com/problems/sliding-window-maxi…

**LC 424 — Longest Repeating Character Replacement** (Medium) · 1 thread  
- _2026-05-11_ · [Google Phone Interview Questions](https://leetcode.com/discuss/post/8195929/google-phone-interview-questions-by-anon-mk7j/)  
  > …-rooms-ii/) * Longest Repeating Character Replacement [#424](https://leetcode.com/problems/longest-repeating-character-replacement/) * Find First and Last Position of Element in Sorted Array [#34](h…

**LC 560 — Subarray Sum Equals K** (Medium) · 1 thread  
- _2026-05-11_ · [Google Phone Interview Questions](https://leetcode.com/discuss/post/8195929/google-phone-interview-questions-by-anon-mk7j/)  
  > …ongest-consecutive-sequence/) * Subarray Sum Equals K [#560](https://leetcode.com/problems/subarray-sum-equals-k/) * Russian Doll Envelopes [#354](https://leetcode.com/problems/russian-doll-envelope…

**LC 875 — Koko Eating Bananas** (Medium) · 1 thread  
- _2026-05-11_ · [Google Phone Interview Questions](https://leetcode.com/discuss/post/8195929/google-phone-interview-questions-by-anon-mk7j/)  
  > …om/problems/number-of-islands/) * Koko Eating Bananas [#875](https://leetcode.com/problems/koko-eating-bananas/) * Next Permutation [#31](https://leetcode.com/problems/next-permutation/) * Search i…

**LC 3 — Longest Substring Without Repeating Characters** (Medium) · 1 thread  
- _2026-05-11_ · [Google Phone Interview Questions](https://leetcode.com/discuss/post/8195929/google-phone-interview-questions-by-anon-mk7j/)  
  > …ater/) * Longest Substring Without Repeating Characters [#3](https://leetcode.com/problems/longest-substring-without-repeating-characters/) * Longest Palindromic Substring [#5](https://leetcode.com/…

**LC 5 — Longest Palindromic Substring** (Medium) · 1 thread  
- _2026-05-11_ · [Google Phone Interview Questions](https://leetcode.com/discuss/post/8195929/google-phone-interview-questions-by-anon-mk7j/)  
  > …-repeating-characters/) * Longest Palindromic Substring [#5](https://leetcode.com/problems/longest-palindromic-substring/) * Longest Consecutive Sequence [#128](https://leetcode.com/problems/longest…

## 2) Recent interview posts (raw feed)

Newest first — the primary sources. Open them for full text and comment threads.

Every row already carries the `google` tag (that is the scrape filter: `tagSlugs: ["google"]`), so it is omitted from `Tags` as redundant. Long tag lists are trimmed at a whole-tag boundary with the remainder as `+N more`.

| Date | Post | Tags (excl. `google`) | Views |
|------|------|------------------------|-------|
| 2026-08-08 | [LeetCode problems for AI Interviews](https://leetcode.com/discuss/post/8448866/leetcode-problems-for-ai-interviews-by-a-cnj8/) | amazon, openai, career, feedback, machine-learning-engineer +4 more | 172 |
| 2026-08-06 | [Can I ask for virtual onsite at Google?](https://leetcode.com/discuss/post/8444095/can-i-ask-for-virtual-onsite-at-google-b-19jx/) |  | 424 |
| 2026-08-03 | [Google Staff Software Engineer (L6)](https://leetcode.com/discuss/post/8439367/google-staff-software-engineer-l6-by-abh-n36g/) | interview | 1342 |
| 2026-08-03 | [Google L4 chances](https://leetcode.com/discuss/post/8439101/google-l4-chances-by-anonymous_user-n5il/) | career, feedback, interview, l4-google | 602 |
| 2026-08-03 | [Google L4 Chances](https://leetcode.com/discuss/post/8438964/google-l4-chances-by-pushya_bansal-y3ph/) |  | 606 |
| 2026-07-27 | [Screening Round with Recruiter for Web Solutions Engineer at Google](https://leetcode.com/discuss/post/8423751/screening-round-with-recruiter-for-web-s-5897/) | career, interview, l4-google | 454 |
| 2026-07-25 | [Feedback from reqruiter](https://leetcode.com/discuss/post/8418035/feedback-from-reqruiter-by-barez59-spgt/) | feedback | 592 |
| 2026-07-22 | [What to expect in Google's tech lead interview and role](https://leetcode.com/discuss/post/8412228/what-to-expect-in-googles-tech-lead-inte-aq1g/) | technical-interview | 540 |
| 2026-07-20 | [Google L4](https://leetcode.com/discuss/post/8409212/google-l4-by-anonymous_user-n4gg/) |  | 1411 |
| 2026-07-20 | [Need help in coding round of L4 PhD early careers Google India](https://leetcode.com/discuss/post/8408470/need-help-in-coding-round-of-l4-phd-earl-z9o4/) | interview | 255 |
| 2026-07-19 | [Forward Deployed Engineer Interview Google Cloud US](https://leetcode.com/discuss/post/8407969/forward-deployed-engineer-interview-goog-90sp/) |  | 434 |
| 2026-07-17 | [Google SRE (Software Developer III) Interview Coming Up – Looking for Recent Interview Insights](https://leetcode.com/discuss/post/8403716/google-sre-software-developer-iii-interv-n5mg/) | interview | 578 |
| 2026-07-17 | [Google L3 (India) Interview Experience need help (ex interviewers especially welcome)](https://leetcode.com/discuss/post/8403050/google-l3-india-interview-experience-nee-ya6h/) | interview | 1567 |
| 2026-07-14 | [Google L4 \| Team Match](https://leetcode.com/discuss/post/8397122/google-l4-team-match-by-anonymous_user-kcfb/) | interview | 1692 |
| 2026-07-12 | [Can someone help with last 6 months Google Questions ?](https://leetcode.com/discuss/post/8392512/can-someone-help-with-last-6-months-goog-k1do/) | career, l4-google, google-interview-questions | 1942 |
| 2026-07-10 | [System Design Board Built for Mock Interviews](https://leetcode.com/discuss/post/8388985/system-design-board-built-for-mock-inter-5dsq/) | microsoft, leetcode, backend, interview, l4-google +3 more | 662 |
| 2026-07-04 | [Any Updates for Google Software engineer intern 2027 summer?](https://leetcode.com/discuss/post/8375211/any-updates-for-google-software-engineer-0cye/) | career, interview, internship-2 | 1005 |
| 2026-07-02 | [c# in Google Interview](https://leetcode.com/discuss/post/8371223/c-in-google-interview-by-anonymous_user-pslg/) |  | 265 |
| 2026-07-02 | [After 6 Months, It's Time to Build Again](https://leetcode.com/discuss/post/8370596/after-6-months-its-time-to-build-again-b-6cu6/) | microsoft, amazon, leetcode, career, interview +1 more | 1926 |
| 2026-07-01 | [I need advice on my Google Intern Interview](https://leetcode.com/discuss/post/8370237/i-need-advice-on-my-google-intern-interv-owuo/) | interview, google-interview-questions | 496 |
| 2026-06-30 | [Google L4 Team Matching Phase](https://leetcode.com/discuss/post/8368243/google-l4-team-matching-phase-by-vj_tirt-k0vq/) | l4-google, l1-google, google-interview-questions | 1267 |
| 2026-06-30 | [Google Googliness round 2025 and 2026](https://leetcode.com/discuss/post/8367035/google-googliness-round-2025-and-2026-by-qemm/) |  | 1508 |
| 2026-06-28 | [Data and AI roles Job interviews guidance](https://leetcode.com/discuss/post/8362765/data-and-ai-roles-job-interviews-guidanc-4q47/) | microsoft, amazon, uber, flipkart, data-science +4 more | 431 |
| 2026-06-28 | [Google Doesn't Just Want Answers, They Want Every Detail - My HR Round Breakdown](https://leetcode.com/discuss/post/8362764/google-doesnt-just-want-answers-they-wan-26hk/) | backend, interview | 1874 |
| 2026-06-27 | [Google L4 \| Onsite Expectations](https://leetcode.com/discuss/post/8361591/google-l4-onsite-expectations-by-anonymo-728k/) | onsite, interview | 2426 |
| 2026-06-26 | [Seeking Guidance for Cracking Google + Looking for Serious Study Partners](https://leetcode.com/discuss/post/8359851/seeking-guidance-for-cracking-google-loo-zlg6/) | amazon, career, feedback, dsa, interview, study-group-2 +2 more | 781 |
| 2026-06-26 | [Buddy For DSA and Interview Prepration](https://leetcode.com/discuss/post/8359294/buddy-for-dsa-and-interview-prepration-b-3kh3/) | interview | 291 |
| 2026-06-25 | [Google Offer](https://leetcode.com/discuss/post/8357173/google-offer-by-anonymous_user-ik5u/) | career, feedback, compensation, interview, l4-google +1 more | 4156 |
| 2026-06-21 | [Google \| SWE-2 (L3)  \| Team Match Query](https://leetcode.com/discuss/post/8348520/google-swe-2-team-match-query-by-anonymo-7ftp/) | team-fit | 1890 |
| 2026-06-21 | [Learn System Design for Interviews](https://leetcode.com/discuss/post/8348146/learn-system-design-for-interviews-by-ar-o1sz/) | microsoft, dsa, dsa-resources, system-design-2 | 630 |
| 2026-06-20 | [How long does Google usually take to make a decision after onsite interviews?](https://leetcode.com/discuss/post/8347721/how-long-does-google-usually-take-to-mak-tx0m/) | feedback, interview | 447 |
| 2026-06-20 | [Google L4 Interview feedback](https://leetcode.com/discuss/post/8347017/google-l4-interview-feedback-by-anonymou-unin/) | feedback | 556 |
| 2026-06-19 | [Google L3 SWE (India) Interview Experience \| Offer Received](https://leetcode.com/discuss/post/8345294/google-l3-swe-india-interview-experience-z8un/) | interview | 2015 |
| 2026-06-17 | [Google L4 \| Banglore](https://leetcode.com/discuss/post/8340444/google-l4-banglore-by-anonymous_user-i96q/) |  | 2209 |
| 2026-06-15 | [Referaal status while in team match](https://leetcode.com/discuss/post/8334901/referaal-status-while-in-team-match-by-a-m244/) |  | 704 |
| 2026-06-14 | [Have Google virtual rounds (Domain specific + DSA and GL) on Wednesday any suggestions?](https://leetcode.com/discuss/post/8334107/have-google-virtual-rounds-domain-specif-ufky/) | career, interview, l4-google | 1213 |
| 2026-06-14 | [Upcoming Amazon interview Prep - Need Help](https://leetcode.com/discuss/post/8332865/upcoming-amazon-interview-prep-need-help-ocdw/) | amazon, career, feedback, dsa, interview, amazon-sde1-2 | 1133 |
| 2026-06-13 | [SDE-2 Microsoft Azure Team Interview Questions (LLD + HLD)](https://leetcode.com/discuss/post/8331863/sde-2-microsoft-azure-team-interview-que-93y6/) | microsoft, amazon, career, feedback, compensation, interview +2 more | 4058 |
| 2026-06-12 | [L5 Google Rating Feedback](https://leetcode.com/discuss/post/8330164/l5-google-rating-feedback-by-anonymous_u-yku7/) | career, feedback, interview | 1031 |
| 2026-06-10 | [Google L4 India - Compensation](https://leetcode.com/discuss/post/8326052/salary-expectations-for-google-l4-india-y5m1h/) | career, feedback, compensation, interview, l4-google | 5855 |
| 2026-06-10 | [Google \| L4 \| Interview Experience \| Chances](https://leetcode.com/discuss/post/8325811/google-l4-interview-experience-chances-b-3qmq/) | career, feedback, interview, l4-google | 3293 |
| 2026-06-09 | [Google L4 - Team Match](https://leetcode.com/discuss/post/8323053/google-l4-team-match-by-anonymous_user-zx63/) | career, feedback, interview, l4-google, team-fit | 1204 |
| 2026-06-08 | [From Zero to Offer Interview Preparation Roadmap](https://leetcode.com/discuss/post/8321890/from-zero-to-offer-interview-preparation-ek23/) | facebook, microsoft, amazon, uber, linkedin, salesforce +3 more | 4549 |
| 2026-06-08 | [Google SRE-SWE (L3/L4) prep advice for someone strong on systems but rusty on DSA?](https://leetcode.com/discuss/post/8321689/google-sre-swe-l3l4-prep-advice-for-some-6jnl/) | interview | 647 |
| 2026-06-07 | [Google L4 \| Interview Experience \| Bangalore \| Next Steps](https://leetcode.com/discuss/post/8319665/google-l4-interview-experience-bangalore-mwp1/) | interview, l4-google | 3206 |
| 2026-06-07 | [Cracking MANG with 10 YEO - Reality check](https://leetcode.com/discuss/post/8319567/cracking-mang-with-10-yeo-reality-check-zi6j7/) | microsoft, amazon | 552 |
| 2026-06-05 | [Google L3 Interview - looking for prep advice!](https://leetcode.com/discuss/post/8315751/google-l3-interview-looking-for-prep-adv-udzw/) | interview, google-interview-questions, swe-ii-google | 687 |
| 2026-06-05 | [Google Cloud Web Application Engineer (Gurugram/Pune) – Updates After GHA?](https://leetcode.com/discuss/post/8315591/google-cloud-web-application-engineer-gu-dbb4/) | interview | 336 |
| 2026-06-05 | [Google L4 Team match India](https://leetcode.com/discuss/post/8314530/google-l4-team-match-india-by-anonymous_-fyaa/) | career, feedback, compensation, interview | 2221 |
| 2026-06-04 | [I had questions about system design round.](https://leetcode.com/discuss/post/8313663/i-had-questions-about-system-design-roun-jqo2/) | uber, career, interview-experience, system-design, interview | 659 |
| 2026-06-02 | [Anyone given Domain Specific round (Android/iOS) at Google?](https://leetcode.com/discuss/post/8308783/anyone-given-domain-specific-round-andro-jdtq/) | interview | 368 |
| 2026-06-01 | [Uber/Google Phone Screen Feedback Counts?](https://leetcode.com/discuss/post/8306742/uber-phone-screen-feedback-counts-by-ano-naz8/) | uber, interview | 999 |
| 2026-06-01 | [Amaon SDE - I : In Person Interview at BLR office \|\| 4th Round(Leadership Principles)](https://leetcode.com/discuss/post/8306214/amaon-sde-i-in-person-interview-at-blr-o-ey3v/) | amazon, low-level-design, dsa-java, amazon-sde1-2 | 1095 |
| 2026-06-01 | [Stuck in Google Team Matching for 4 Months, Any Advice?](https://leetcode.com/discuss/post/8306204/stuck-in-google-team-matching-for-4-mont-0qzb/) | interview | 950 |
| 2026-05-31 | [Preparing for Google Interview in Embedded Domain](https://leetcode.com/discuss/post/8304833/preparing-for-google-interview-in-embedd-y1l2/) | interview | 291 |
| 2026-05-31 | [Google(L4) intervew loop - In progess \| Need partner for further Prep](https://leetcode.com/discuss/post/8304156/uber-hld-upcoming-by-anonymous_user-vk6g/) |  | 937 |
| 2026-05-31 | [Google Interview Process Timeline Question](https://leetcode.com/discuss/post/8303584/google-interview-process-timeline-questi-gyjd/) | l4-google | 853 |
| 2026-05-30 | [2 YOE and completely lost on System Design — help!](https://leetcode.com/discuss/post/8303012/2-yoe-and-completely-lost-on-system-desi-r7ot/) | microsoft, amazon, uber, career, system-design, compensation +1 more | 1755 |
| 2026-05-30 | [Google interview question about unnoticed bugs](https://leetcode.com/discuss/post/8302587/google-interview-question-about-unnotice-6tjm/) | l4-google, google-interview-questions | 1097 |
| 2026-05-30 | [UBER Freight - SDE - II : Round - 3](https://leetcode.com/discuss/post/8302462/uber-freight-sde-ii-round-3-by-anonymous-mr1m/) | uber, low-level-design, dsa, sde-2-3 | 912 |
| 2026-05-30 | [Google L4 \| Domain Specific Round for Full Stack?](https://leetcode.com/discuss/post/8302430/google-l4-domain-specific-round-for-full-d33s/) |  | 569 |
| 2026-05-30 | [Google L4 Team Matching — Looking for Recent Experiences](https://leetcode.com/discuss/post/8301980/google-l4-team-matching-looking-for-rece-hbpj/) | india, feedback, interview, l4-google | 735 |
| 2026-05-29 | [Google Interview Experience - L4](https://leetcode.com/discuss/post/8301524/google-interview-experience-l4-by-anonym-gjl3/) | career, feedback, interview, l4-google | 2429 |
| 2026-05-29 | [Looking for a study / mock partner (SSE and above)](https://leetcode.com/discuss/post/8301486/looking-for-a-study-mock-partner-sse-and-wsxr/) | career, interview | 141 |
| 2026-05-29 | [Google L4 \| Interviewer No Show](https://leetcode.com/discuss/post/8300512/google-l4-interviewer-no-show-by-anonymo-24hw/) | interview | 880 |
| 2026-05-28 | [Google L4/L5 Prep](https://leetcode.com/discuss/post/8299704/google-l4l5-prep-by-anonymous_user-gtay/) | career, online-assessment, compensation, interview +2 more | 4181 |
| 2026-05-28 | [Interview prep for Forward Deployed Engineer FDE , GENAI, AI Engineer for Big Tech Companies](https://leetcode.com/discuss/post/8298597/interview-prep-for-forward-deployed-engi-jq9b/) | databricks, openai, career, machine-learning-engineer +4 more | 1213 |
| 2026-05-27 | [Seeking Advice from FAANG Engineers — How to Break In with ~10 Months of Experience?](https://leetcode.com/discuss/post/8297185/seeking-advice-from-faang-engineers-how-1mho4/) | microsoft, amazon, flipkart, career | 438 |
| 2026-05-27 | [Google L4 - Software Engineer III - Bengaluru](https://leetcode.com/discuss/post/8296795/google-l4-software-engineer-iii-bengalur-1kgb/) | career, l4-google | 1622 |
| 2026-05-27 | [Google L5 team matching](https://leetcode.com/discuss/post/8295978/google-l5-team-matching-by-anonymous_use-yk2e/) | career, feedback, interview, senior-level | 628 |
| 2026-05-25 | [Google L4 Chance of Team Match and HC approval](https://leetcode.com/discuss/post/8292554/google-l4-chance-of-team-match-and-hc-ap-sdvk/) | career, interview, l4-google | 1721 |
| 2026-05-23 | [Google  \| Amazon \| SDE-2 \| Bangalore](https://leetcode.com/discuss/post/8287855/google-amazon-sde-2-bangalore-by-anonymo-8qew/) | amazon, career, interview, l4-google | 4010 |
| 2026-05-22 | [Google \| Application Engineer \| Upcoming Coding Rounds - Insights Needed](https://leetcode.com/discuss/post/8286412/google-application-engineer-upcoming-cod-99aa/) | technical-interview, interview-experience, interview +2 more | 656 |
| 2026-05-22 | [Onsite feedback delay](https://leetcode.com/discuss/post/8285817/onsite-feedback-delay-by-anonymous_user-tfw4/) |  | 277 |
| 2026-05-19 | [Google \| Application Engineer \| Upcoming Coding Rounds - Insights Needed](https://leetcode.com/discuss/post/8277469/google-application-engineer-upcoming-cod-ndk6/) | technical-interview, interview-experience, interview +2 more | 553 |
| 2026-05-19 | [Google L4 \| Banglore](https://leetcode.com/discuss/post/8276966/google-l4-banglore-by-anonymous_user-97gh/) | career, interview, l4-google | 5758 |
| 2026-05-18 | [Google L3 \|\| Bangalore, India \|\| Interview Experience](https://leetcode.com/discuss/post/8268786/google-l3-bangalore-india-interview-expe-dt1h/) | career, feedback, interview, job-transition | 4390 |
| 2026-05-18 | [Google L4 Interview \|\| Reject](https://leetcode.com/discuss/post/8265899/google-l4-interview-reject-by-anonymous_-xsc3/) | bengaluru, interview, l4-google | 4628 |
| 2026-05-18 | [Onsite Feedback delay](https://leetcode.com/discuss/post/8263531/onsite-feedback-delay-by-anonymous_user-co18/) |  | 429 |
| 2026-05-16 | [Targeting L5/Senior (Google, Meta, HFT) \| UK{LONDON} Based](https://leetcode.com/discuss/post/8252864/targeting-l5senior-google-meta-hft-uklon-5pvr/) | facebook, microsoft, amazon, bloomberg, optiver, career +3 more | 1656 |
| 2026-05-15 | [Amazon SDE - I In - Person Interview at BLR Office](https://leetcode.com/discuss/post/8241486/amazon-sde-i-in-person-interview-at-blr-7drz2/) | amazon, java, career, low-level-design, dsa, interview +3 more | 1635 |
| 2026-05-14 | [Google L4 In-person Interview](https://leetcode.com/discuss/post/8218498/google-l4-in-person-interview-by-anonymo-eqbq/) | interview, l4-google | 3902 |
| 2026-05-13 | [Google L4 interview](https://leetcode.com/discuss/post/8217371/google-l4-interview-by-anonymous_user-n071/) | career, interview, l4-google | 2277 |
| 2026-05-12 | [Next Steps after Onsite Interviews](https://leetcode.com/discuss/post/8198530/next-steps-after-onsite-interviews-by-an-4jnw/) | l4-google | 748 |
| 2026-05-11 | [Google Phone Interview Questions](https://leetcode.com/discuss/post/8195929/google-phone-interview-questions-by-anon-mk7j/) | interview | 1404 |
| 2026-05-11 | [[URGENT] - Google Onsite Interview](https://leetcode.com/discuss/post/8193404/urgent-google-onsite-interview-by-anonym-wi2v/) | career, feedback, interview, l4-google | 1297 |
| 2026-05-08 | [🚀 Google Interview Experience (2 Rounds) SWE - II (L3)](https://leetcode.com/discuss/post/8173874/google-interview-experience-2-rounds-swe-ku5d/) | interview, l3-google, swe-2-google, swe-ii-google | 2439 |
| 2026-05-07 | [[PART-2] 100+ interviews \| 50+ companies \| 1 offer](https://leetcode.com/discuss/post/8162098/part-2-100-interviews-50-companies-1-off-cjdq/) | microsoft, amazon, uber, salesforce, career, oraclesql +2 more | 3063 |
| 2026-05-07 | [Google L4 OA + Interviews](https://leetcode.com/discuss/post/8159666/google-l4-oa-interviews-by-anonymous_use-6mmf/) | interview | 1164 |
| 2026-05-04 | [Google \| Senior Software Engineer \| Preparation Help](https://leetcode.com/discuss/post/8140583/google-senior-software-engineer-preparat-b4fw/) | interview | 1334 |
| 2026-05-03 | [Google L4 screening](https://leetcode.com/discuss/post/8134146/google-l4-screening-by-user9501hm-jgmz/) |  | 539 |
| 2026-05-03 | [ONSITE GOOGLE INTERVIEW L4 Bangalore](https://leetcode.com/discuss/post/8133843/onsite-google-interview-l4-bangalore-by-czhvf/) | top-interview-questions, interview-experience, india +2 more | 1702 |
| 2026-04-29 | [Google L4](https://leetcode.com/discuss/post/8113604/google-l4-by-anonymous_user-y9xg/) | interview | 1869 |
| 2026-04-27 | [Google L4 Team matching](https://leetcode.com/discuss/post/8102178/google-l4-team-matching-looker-team-by-a-lhav/) | interview, l4-google, team-fit | 1353 |
| 2026-04-25 | [Google SDEIII L4 Interview Experience](https://leetcode.com/discuss/post/8096071/google-sdeiii-interview-experience-by-an-67tm/) | dsa, l4-google, g-l | 2982 |
| 2026-04-24 | [Onsite Interviews in May first week](https://leetcode.com/discuss/post/8095461/onsite-interviews-in-may-first-week-by-a-18s8/) |  | 725 |
| 2026-04-22 | [Google Bangalore Onsite (L4) – Final 2 DSA Rounds (In-Person, Back-to-Back) – What to Expect?](https://leetcode.com/discuss/post/8052794/google-bangalore-onsite-l4-final-2-dsa-r-okwd/) | technical-interview, career, india, interview-question +2 more | 2105 |
| 2026-04-20 | [Google Onsite SWE-II](https://leetcode.com/discuss/post/8019003/google-onsite-swe-ii-by-anonymous_user-yd0w/) | onsite, interview | 717 |
| 2026-04-20 | [Help needed for the coming Google onsite full loop interview(L5)](https://leetcode.com/discuss/post/8008818/help-needed-for-the-coming-google-onsite-ps8y/) |  | 744 |
| 2026-04-19 | [What theory questions are commonly asked for Java (Core Java) interviews?](https://leetcode.com/discuss/post/7994659/what-theory-questions-are-commonly-asked-pxwg/) | nike, blinkit, java, career, interview-experience | 398 |
| 2026-04-17 | [Advice for Google L4 Rounds](https://leetcode.com/discuss/post/7967734/advice-for-google-l4-rounds-by-anonymous-5dzr/) | career, feedback, interview, l4-google | 1340 |
| 2026-04-16 | [Just Got Amazon 6m Internhip offer 🧿🎊](https://leetcode.com/discuss/post/7945762/just-got-amazon-6m-internhip-offer-by-an-brp9/) | amazon, technical-interview, career, interview-experience +5 more | 718 |
| 2026-04-16 | [Google Interview Update](https://leetcode.com/discuss/post/7939900/google-interview-update-by-anonymous_use-7q3a/) |  | 1018 |
| 2026-04-16 | [Google \| Hiring Manager Round \| Gemini Applications \| SWE III ML \| Bengaluru](https://leetcode.com/discuss/post/7936329/google-hiring-manager-round-gemini-appli-mb5k/) | gemini, data-science, machine-learning-engineer +4 more | 2067 |
| 2026-04-15 | [Amazon \| SDE-I Contract(1 year) \| Expectations \| Location - BLR \| YOE - 2 years](https://leetcode.com/discuss/post/7926825/amazon-sde-i-contract1-year-expectations-mc10/) | microsoft, amazon, technical-interview, java, career +2 more | 735 |
| 2026-04-14 | [Google Recruitment Process - 2 weeks and no update](https://leetcode.com/discuss/post/7911003/google-recruitment-process-2-weeks-and-n-vosh/) | career, feedback, interview, l4-google | 1064 |
| 2026-04-14 | [Google L4 onsite: strong DSA, failed coding round, invited to retry](https://leetcode.com/discuss/post/7909727/google-l4-onsite-strong-dsa-failed-codin-bed9/) | l4-google | 1385 |
| 2026-04-13 | [Google Web Solutions Engineer – Delay after final round](https://leetcode.com/discuss/post/7889060/google-web-solutions-engineer-delay-afte-pmz3/) | career, feedback, interview | 419 |
| 2026-04-12 | [Google Interview Retake: Do Interviewers See Previous Questions or Vary Problem Types?](https://leetcode.com/discuss/post/7882921/google-interview-retake-do-interviewers-24928/) | interview | 770 |
| 2026-04-12 | [Google L4 Chances](https://leetcode.com/discuss/post/7881477/google-l4-chances-by-anonymous_user-ik8e/) | interview, l4-google | 3216 |
| 2026-04-11 | [reverser LL nodes with verifying hash values coding question onsite google](https://leetcode.com/discuss/post/7873505/reverser-ll-nodes-with-verifying-hash-va-wng3/) | interview | 618 |
| 2026-04-11 | [Google Web Solutions Engineer \| Need help](https://leetcode.com/discuss/post/7870214/google-web-solutions-engineer-need-help-rhubf/) | career, feedback, compensation, interview | 664 |
| 2026-04-11 | [Google L4 \|](https://leetcode.com/discuss/post/7869055/google-l4-by-anonymous_user-i3e3/) | career, interview, l4-google | 2764 |
| 2026-04-11 | [Google L4 \| Bengaluru \| Rejected](https://leetcode.com/discuss/post/7867127/google-l4-bengaluru-reject-by-anonymous_-vho0/) | interview-experience, interview | 2846 |
| 2026-04-10 | [Google L5 screening : Reject](https://leetcode.com/discuss/post/7855988/google-l5-screening-reject-by-anonymous_-4a2i/) | interview | 2239 |
| 2026-04-09 | [Stuck in Google team match (4 months)](https://leetcode.com/discuss/post/7845117/stuck-in-google-team-match-4-months-by-a-aa4e/) | l4-google, team-fit | 896 |
| 2026-04-08 | [Google SDE-3 \| Phone Screen & Behavioral \| Got a second chance for a coding round](https://leetcode.com/discuss/post/7827768/google-sde-3-phone-screen-behavioral-got-pao9/) | feedback, phone-screening, interview, l4-google +1 more | 1145 |
| 2026-04-08 | [Google Forward Deployed Engineer (FDE)](https://leetcode.com/discuss/post/7821696/google-forward-deployed-engineer-fde-by-l4th5/) | microsoft, amazon, linkedin, palantir, atlassian, career +3 more | 2085 |
| 2026-04-08 | [Google L3 Team Matching - 11 Months and Expiring Soon. Anyone else in the same boat?](https://leetcode.com/discuss/post/7819881/google-l3-team-matching-11-months-and-ex-xwiq/) | career | 819 |
| 2026-04-07 | [are google interviews happening f2f ?](https://leetcode.com/discuss/post/7811123/are-google-interviews-happening-f2f-by-r-9fv3/) |  | 893 |
| 2026-04-07 | [Switch Company PBC](https://leetcode.com/discuss/post/7807781/switch-company-pbc-by-nidhirajpatel-te4n/) |  | 369 |
| 2026-04-05 | [Google L4 DSA Phone Screen (India, 5 YOE)](https://leetcode.com/discuss/post/7785661/google-l4-dsa-phone-screen-india-5-yoe-b-8umr/) | technical-interview, top-interview-questions +4 more | 2279 |
| 2026-04-04 | [Is Goolge asking multithreading/concurrency questions ?](https://leetcode.com/discuss/post/7777574/is-goolge-asking-multithreadingconcurren-mdft/) | interview | 997 |
| 2026-04-02 | [Google WSE First Round - What to expect?](https://leetcode.com/discuss/post/7753715/google-wse-first-round-what-to-expect-by-ire9/) |  | 440 |
| 2026-04-01 | [Google \| Team Match round with tech lead](https://leetcode.com/discuss/post/7742939/google-team-match-round-with-tech-lead-b-3frb/) | l4-google, team-fit, l1-google | 1352 |
| 2026-03-29 | [google](https://leetcode.com/discuss/post/7714230/google-by-anonymous_user-qmbu/) |  | 1443 |
| 2026-03-28 | [L3 google onsite Arithmetic sequence question.](https://leetcode.com/discuss/post/7708808/l3-google-onsite-arithmetic-sequence-que-85x3/) | interview, l3-google | 1518 |
| 2026-03-28 | [Google L5 Interview phone screening](https://leetcode.com/discuss/post/7706368/google-l5-interview-phone-screening-by-a-4tjw/) | interview, google-interview-questions | 1831 |
| 2026-03-28 | [L4 DSA Round Question](https://leetcode.com/discuss/post/7705895/google-l4-dsa-round-question-by-anonymou-nh2t/) | facebook, microsoft, amazon, career, interview-question +1 more | 2581 |
| 2026-03-27 | [Google Recently Asked Coding Questions Compilation [Mar 2025-26, SWE-L3,L4]](https://leetcode.com/discuss/post/7701662/google-recently-asked-coding-questions-c-296m/) | career, interview, l4-google, google-interview-questions | 3524 |
| 2026-03-27 | [Google Interview for PSE (Product Solutions Engineer) role (India)](https://leetcode.com/discuss/post/7700921/google-interview-for-pse-product-solutio-apwn/) | interview-experience, interview-question, interview | 495 |
| 2026-03-26 | [Amazon Logical and Maintainability Round](https://leetcode.com/discuss/post/7697546/amazon-logical-and-maintainability-round-kx6e/) | amazon, interview | 293 |
| 2026-03-23 | [Google - Partner Engineer, Device Ops YouTube- Bangalore](https://leetcode.com/discuss/post/7684068/google-partner-engineer-device-ops-youtu-h7t2/) |  | 417 |
| 2026-03-18 | [How I failed L3 interview at Google](https://leetcode.com/discuss/post/7662694/how-i-failed-l3-interview-at-google-by-m-dbh2/) | career, interview | 2335 |
| 2026-03-18 | [Google Team Matching Stuck ~1 Year , Is This Basically Dead?](https://leetcode.com/discuss/post/7659329/google-team-matching-stuck-1-year-is-thi-q4kx/) | interview | 1134 |
| 2026-03-17 | [Offer Suggestion Needed: Google vs NVIDIA](https://leetcode.com/discuss/post/7654767/offer-comparison-google-vs-nvidia-by-ano-rar5/) | nvidia, career, feedback, compensation | 3048 |
| 2026-03-14 | [Google L3 Team Matching Process Info](https://leetcode.com/discuss/post/7646569/google-l3-team-matching-process-info-by-aohqg/) |  | 1158 |
| 2026-03-13 | [[URGENT HELP NEEDED] Google Interview Stuck in “Approval Pending to start interview process”](https://leetcode.com/discuss/post/7644812/google-interview-stuck-in-approval-pendi-8p0j/) | technical-interview, india, dsa, interview, l4-google | 989 |
| 2026-03-12 | [google virtual onsite deny](https://leetcode.com/discuss/post/7643272/google-virtual-onsite-deny-by-anonymous_-6vgl/) | interview | 813 |
| 2026-03-12 | [Sharing Regular Off-Campus Job Opportunities (0–5 YOE + Internships)](https://leetcode.com/discuss/post/7643229/sharing-regular-off-campus-job-opportuni-8f9u/) | microsoft, amazon, apple, flipkart, career, interview | 957 |
| 2026-03-12 | [Google Team Matching Hell](https://leetcode.com/discuss/post/7642901/google-team-matching-hell-by-sachinbhola-dt1h/) | l4-google, team-fit | 921 |
| 2026-03-11 | [Google L4 chances](https://leetcode.com/discuss/post/7641721/google-l4-chances-by-anonymous_user-m7h3/) | feedback, interview, l4-google | 1441 |
| 2026-03-11 | [What are my chances of proceeding to team matching? (L3 - Google Europe)](https://leetcode.com/discuss/post/7641689/what-are-the-chances-to-pass-to-the-team-5sog/) | interview | 866 |
| 2026-03-11 | [Google L3 Interview Process - Recruiter Asked for Documents, Is this pre-HC?](https://leetcode.com/discuss/post/7641112/google-l3-interview-process-recruiter-as-5e6t/) | career, interview | 932 |
| 2026-03-10 | [DSA Mock Interview Partner - Google](https://leetcode.com/discuss/post/7639416/dsa-mock-interview-partner-google-by-ano-oqyi/) | interview | 490 |
| 2026-03-09 | [Beginner Preparing for Google Interviews — Where Should I Start?](https://leetcode.com/discuss/post/7637023/beginner-preparing-for-google-interviews-3vb4/) |  | 471 |
| 2026-03-08 | [Google interview upcoming](https://leetcode.com/discuss/post/7635145/google-interview-upcoming-dsa-partner-by-hhav/) | dsa, interview-question, interview | 1106 |
| 2026-03-07 | [🔥 Honest Poll for People Preparing for Google Interviews](https://leetcode.com/discuss/post/7632214/honest-poll-for-people-preparing-for-goo-djwn/) | interview | 1506 |
| 2026-03-07 | [Google Interview query](https://leetcode.com/discuss/post/7632037/google-interview-query-by-anonymous_user-0xnw/) |  | 973 |
| 2026-03-06 | [Extremely sad Google L5 interview experience](https://leetcode.com/discuss/post/7631077/extremely-sad-google-l5-interview-experi-jm8b/) | atlassian, interview | 2732 |
| 2026-03-06 | [Google L3 interview experience - HC approval chances](https://leetcode.com/discuss/post/7629725/google-l3-interview-experience-hc-approv-akpm/) | interview | 1900 |
| 2026-03-05 | [Allowed time gap between each round of interview at Google](https://leetcode.com/discuss/post/7626255/allowed-time-gap-between-each-round-of-i-yyb6/) |  | 492 |
| 2026-03-05 | [Looking for LLD/HLD Practice Partner](https://leetcode.com/discuss/post/7626143/looking-for-lldhld-practice-partner-by-a-junj/) | microsoft, amazon, career, compensation, interview | 1257 |
| 2026-03-04 | [Google L5 Interview Experience](https://leetcode.com/discuss/post/7624444/google-l5-interview-experience-by-anonym-o9ja/) | interview | 2757 |
| 2026-03-04 | [Interview Experience: Google \| L3 Web Solutions Engineer (GTech)](https://leetcode.com/discuss/post/7624355/interview-experience-google-l3-web-solut-ger7/) | interview-experience | 1355 |
| 2026-03-03 | [Advice for Google onsite interview](https://leetcode.com/discuss/post/7623228/advice-for-google-onsite-interview-by-vk-zzte/) | interview, l4-google, google-interview-questions | 1139 |
| 2026-03-02 | [Need advice, Google onsite round offline](https://leetcode.com/discuss/post/7619877/need-advice-google-onsite-round-offline-q7ewb/) | career, interview | 1754 |
| 2026-03-01 | [Google Team Match \| SWE L3](https://leetcode.com/discuss/post/7617480/google-team-match-swe-l3-by-anonymous_us-b7jn/) | career, feedback, interview, team-fit | 786 |
| 2026-03-01 | [Google L5 interview screening](https://leetcode.com/discuss/post/7617354/google-l5-interview-screening-by-anonymo-ld26/) | technical-interview, interview-experience, senior-level +1 more | 2157 |
| 2026-02-28 | [Google \| Team Matching \| L3 \| Waitlist](https://leetcode.com/discuss/post/7615069/google-team-matching-l3-doubt-by-anonymo-fe5h/) | interview-experience, general-discussion-2, team-fit | 1189 |
| 2026-02-28 | [Understanding Time Complexity for Backtracking with Pruning](https://leetcode.com/discuss/post/7614127/understanding-time-complexity-for-backtr-rn7u/) | amazon, apple, atlassian, technical-interview, dsa | 714 |
| 2026-02-26 | [Google Web Solutions Engineer \| L3](https://leetcode.com/discuss/post/7611418/google-web-solutions-engineer-l3-by-anon-fo7v/) | interview | 1637 |
| 2026-02-26 | [Google L5 Interview Experience \| Onsite Bangalore 2025](https://leetcode.com/discuss/post/7610148/google-l5-interview-experience-onsite-ba-mk1z/) | google-interview-questions | 2206 |
| 2026-02-24 | [GOOGLE L4 DSA Round: Difficulty Converting to Bottom-Up DP, Interview Advice Needed](https://leetcode.com/discuss/post/7605894/google-l4-dsa-round-difficulty-convertin-cy0n/) |  | 1700 |
| 2026-02-23 | [Google L3 SWE AI/ML Onsite Confusion: In-Person vs “Virtual” Docs](https://leetcode.com/discuss/post/7603486/google-l3-swe-aiml-onsite-confusion-in-p-kkya/) |  | 567 |
| 2026-02-23 | [Compensation at GOOGLE](https://leetcode.com/discuss/post/7602545/compensation-at-google-by-anonymous_user-019s/) | compensation, l4-google | 5026 |
| 2026-02-20 | [Google L3 interview 2026](https://leetcode.com/discuss/post/7595211/google-l3-interview-2026-by-anonymous_us-cxtr/) | interview, l3-google | 2836 |
| 2026-02-20 | ["My code Passed all test cases, but i failed the interview." ---Let's talk about the 2026 'Oper](https://leetcode.com/discuss/post/7594408/my-code-passed-all-test-cases-but-i-fail-dz5r/) | rejection-sampling, career, interview, job-search-2 | 1850 |
| 2026-02-19 | [Need help with Integration Round.](https://leetcode.com/discuss/post/7591308/need-help-with-integration-round-by-nish-5t41/) |  | 124 |
| 2026-02-18 | [Google Interview.](https://leetcode.com/discuss/post/7589816/google-interview-by-helloarjun111-7r88/) | interview | 2421 |
| 2026-02-18 | [Google Team Match \| L3](https://leetcode.com/discuss/post/7589707/google-team-match-l3-by-anonymous_user-bg7i/) |  | 1202 |
| 2026-02-17 | [Google L3 Android Domain Interview \|\| Need Help](https://leetcode.com/discuss/post/7585533/google-l3-android-domain-interview-need-30ty7/) |  | 327 |
| 2026-02-17 | [Need LeetCode Premium for a Day](https://leetcode.com/discuss/post/7585292/need-leetcode-premium-for-a-day-by-anony-anoz/) | leetcode, career, google-interview-questions | 1117 |
| 2026-02-15 | [Google Interview Process Changed \| Need Help](https://leetcode.com/discuss/post/7581438/google-interview-process-changed-need-he-oaqx/) | interview, l4-google | 3210 |
| 2026-02-14 | [Google DSA Mock](https://leetcode.com/discuss/post/7579291/google-dsa-mock-by-tharun55-i5qq/) | interview | 899 |
| 2026-02-13 | [Google dream ruined \| L 3](https://leetcode.com/discuss/post/7577253/google-dream-ruined-l-3-by-anonymous_use-isia/) | interview-experience | 2126 |
| 2026-02-13 | [Google L4 USA (moved to TM)](https://leetcode.com/discuss/post/7577153/google-l4-usa-moved-to-tm-by-user9071bo-4sbr/) | career, feedback, interview, l4-google | 1004 |
| 2026-02-13 | [System Design for Google L4 (India)?](https://leetcode.com/discuss/post/7576666/system-design-for-google-l4-india-by-ano-kx61/) | india, interview, l4-google | 1061 |
| 2026-02-12 | [Offerretriever Marketing Tactics](https://leetcode.com/discuss/post/7574980/offerretriever-marketing-tactics-by-anon-kb2i/) |  | 150 |
| 2026-02-12 | [Looking for Career Transition Advice After 2.5 Years at OCI](https://leetcode.com/discuss/post/7573941/looking-for-career-transition-advice-aft-bwg9/) | oci, career, compensation | 917 |
| 2026-02-12 | [Google L4 infinite loop](https://leetcode.com/discuss/post/7572765/google-l4-infinite-loop-by-anonymous_use-3dgh/) | l4-google | 1321 |
| 2026-02-11 | [Google Interview Experience \| L4 \| Onsite MountainView(UPDATE: Moved to TM)](https://leetcode.com/discuss/post/7571793/google-interview-experience-l4-onsite-mo-u7cr/) | career, feedback, interview, l4-google +1 more | 2126 |
| 2026-02-11 | [Google Interview Experience \| L3 \| Onsite Banglore](https://leetcode.com/discuss/post/7570658/google-interview-experience-l3-onsite-ba-6eig/) | career, interview-experience, interview | 1923 |
| 2026-02-10 | [[Interview Experience] Sharing First In-Person Google Onsite After Years of Virtual](https://leetcode.com/discuss/post/7567827/interview-experience-sharing-first-in-pe-oyg1/) | technical-interview, onsite, google-interview-questions | 2032 |
| 2026-02-09 | [Free resources for recent OA (online assessment) questions.](https://leetcode.com/discuss/post/7565779/free-resources-for-recent-oa-online-asse-q9he/) | microsoft, amazon, salesforce, technical-interview, career +4 more | 2302 |
| 2026-02-08 | [Google INDIA Bangalore L3 Cleared](https://leetcode.com/discuss/post/7563535/google-india-bangalore-l3-cleared-by-ano-u1kd/) |  | 1880 |
| 2026-02-08 | [Google L4 Interview](https://leetcode.com/discuss/post/7563483/google-l4-interview-by-ay_ila-n7yp/) | interview, l4-google, google-interview-questions | 3191 |
| 2026-02-07 | [Why Your System Design Interview Failed (And You Didn’t Even Realize It)](https://leetcode.com/discuss/post/7561018/why-your-system-design-interview-failed-uxbk5/) | microsoft, amazon, top-interview-questions, career +5 more | 1152 |
| 2026-02-07 | [US: Google coding interview - find squares-ish](https://leetcode.com/discuss/post/7558860/us-google-coding-interview-find-squares-l4ngs/) | backend, feedback, interview, senior-level | 1216 |
| 2026-02-06 | [Essential Graph Problems for SDE Interviews](https://leetcode.com/discuss/post/7556431/essential-graph-problems-for-sde-intervi-4iuy/) | graph, microsoft, amazon, leetcode, career +4 more | 1876 |
| 2026-02-05 | [Q3 & Q4 Problems from Recent 34 Contests for OA Preparation](https://leetcode.com/discuss/post/7553438/q3-q4-problems-from-recent-34-contests-f-r1n8/) | microsoft, amazon, leetcode, career, online-assessment +3 more | 1187 |
| 2026-02-04 | [Google Product Support Engineer (University Graduate 2026) What Questions asked In Interview?](https://leetcode.com/discuss/post/7551791/google-product-support-engineer-universi-l4sa/) | top-interview-questions, career, interview-experience +6 more | 460 |
| 2026-02-04 | [Google \| L3 New York \| Unseen segments Problem. Need Advices please!](https://leetcode.com/discuss/post/7550619/google-l3-new-york-unseen-segments-probl-5hd3/) | interview-experience | 1111 |
| 2026-02-03 | [Beyond the "Accepted" Screen: What 2026 Interviews Actually Want from You](https://leetcode.com/discuss/post/7549546/beyond-the-accepted-screen-what-2026-int-an4a/) | amazon, career, feedback, compensation, interview +4 more | 892 |
| 2026-02-02 | [Google L3 \| Onsite \| Delayed interview once but I think it worked](https://leetcode.com/discuss/post/7544550/google-l3-onsite-delayed-interview-once-592ok/) | interview-experience | 1833 |
| 2026-02-01 | [Google Interview Query](https://leetcode.com/discuss/post/7543997/google-interview-query-by-anonymous_user-gq6g/) |  | 401 |
| 2026-02-01 | [Google Interview: Unknown Question](https://leetcode.com/discuss/post/7542615/google-interview-unknown-question-by-ano-4s4y/) | interview | 1936 |
| 2026-02-01 | [Complete Greedy Problems & Resources Guide](https://leetcode.com/discuss/post/7541035/complete-greedy-problems-resources-guide-woyo/) | greedy, microsoft, amazon, leetcode, career, beginner +2 more | 912 |
| 2026-01-31 | [Queries regarding the Latest GOOGLE Interview Process](https://leetcode.com/discuss/post/7540443/urgent-queries-regarding-the-latest-goog-eh4y/) | technical-interview, top-interview-questions +7 more | 1504 |
| 2026-01-31 | [Don't waste time giving interviews at Google India](https://leetcode.com/discuss/post/7539543/dont-waste-time-giving-interviews-at-goo-k5ih/) | career, compensation, l4-google | 4390 |
| 2026-01-31 | [Complete String Problems & Resources Guide](https://leetcode.com/discuss/post/7538070/complete-string-problems-resources-guide-5xk8/) | string, microsoft, amazon, leetcode, career, interview +2 more | 726 |
| 2026-01-30 | [Complete Array Problems & Resources Guide](https://leetcode.com/discuss/post/7535587/complete-array-problems-resources-guide-6ssxp/) | array, microsoft, amazon, leetcode, career, interview +2 more | 1030 |
| 2026-01-29 | [Google \| Onsite Invite Banglore \| L3](https://leetcode.com/discuss/post/7535385/google-onsite-invite-banglore-l3-by-anon-li3m/) | interview-experience, interview | 1447 |
| 2026-01-29 | [Google L4 Interview Loop (India) – HC Outcome & Team Matching Chances?](https://leetcode.com/discuss/post/7534319/google-l4-interview-loop-india-hc-outcom-p70d/) | career, feedback, interview, l4-google, team-fit | 1331 |
| 2026-01-29 | [Google \| Recruiter Call \| Feedback \| Team Matching \| L3](https://leetcode.com/discuss/post/7534265/google-recruiter-call-feedback-team-matc-6f35/) | team-matching-2, wait-list | 1128 |
| 2026-01-29 | [Google Frontend L4/L5 - Prep](https://leetcode.com/discuss/post/7533263/google-frontend-l4l5-prep-by-up32guy-buc2/) | frontend | 928 |
| 2026-01-26 | [Complete Binary Search Problems & Resources Guide](https://leetcode.com/discuss/post/7525228/complete-binary-search-problems-resource-qj2y/) | binary-search, microsoft, amazon, leetcode, career +3 more | 1499 |
| 2026-01-25 | [Interview experience](https://leetcode.com/discuss/post/7523211/interview-experience-by-anonymous_user-sbsr/) | microsoft, flipkart | 904 |
| 2026-01-25 | [SDE-1 AMAZON ON CONTRACT BASIS FOR 1 YEAR...](https://leetcode.com/discuss/post/7522876/sde-1-amazon-on-contract-basis-for-1-yea-7ofg/) | amazon, career, feedback, compensation, interview, l4-google +3 more | 1174 |
| 2026-01-24 | [Google SDE-3 \| India Position \| Rejected](https://leetcode.com/discuss/post/7521416/google-sde-3-india-position-rejected-by-73mi8/) | india, sde-3-2 | 2280 |
| 2026-01-24 | [gRPC Explained Like You Actually Need It in System Design Interviews](https://leetcode.com/discuss/post/7521173/grpc-explained-like-you-actually-need-it-4ci6/) | facebook, microsoft, amazon, top-interview-questions, career +4 more | 1041 |
| 2026-01-24 | [Google Interview for SWE/SRE internship EMEA](https://leetcode.com/discuss/post/7520741/google-interview-for-swesre-internship-e-cbd5/) | interview | 552 |
| 2026-01-24 | [Google \| L4 \| SWE 3 \| India \| 3.5 YOE](https://leetcode.com/discuss/post/7520006/google-l4-swe-3-india-35-yoe-by-anonymou-8pvq/) | career, india, compensation, bengaluru, l4-google | 15466 |
| 2026-01-24 | [Google SWE Intern](https://leetcode.com/discuss/post/7519819/google-swe-intern-by-anonymous_user-fn4f/) | singapore, career | 370 |
| 2026-01-23 | [Need Guidance: Not Getting Interview Calls Despite DSA Preparation](https://leetcode.com/discuss/post/7517620/need-guidance-not-getting-interview-call-ywuz/) | microsoft, amazon, uber, career, system-design, feedback +3 more | 743 |

## 3) Method

Generated by [`script/scrape_lc_discuss_company.py`](../script/scrape_lc_discuss_company.py). The figures above come from a **full paginated run**: all 274 threads listed by paging `skip` to exhaustion, then every thread's body and comments fetched individually (274 bodies, 1153 comments).

```bash
python3 script/scrape_lc_discuss_company.py --tag google   # full run (slow, ~2.5s/request)
python3 script/scrape_lc_discuss_company.py --build-only  # rebuild doc from cache
```

Three calls are involved, because the list endpoint does **not** return bodies:

| Stage | Field | Paginate with | Returns |
|-------|-------|---------------|---------|
| 1 | `ugcArticleDiscussionArticles` | `skip` += `first` | thread list + `summary` (**no body**) |
| 2 | `ugcArticleDiscussionArticle` | one call per `topicId` | post body (`content`) |
| 3 | `topicComments` | `pageNo` 1..n | comment threads |

**Schema gotchas** (introspection is disabled; all found by reading error messages):

- `tagSlugs` is required on `ugcArticleDiscussionArticles`; omitting it returns `argument of type 'NoneType' is not iterable`.
- Variable types must be exact: `$keywords: [String]!` but `$tagSlugs: [String!]`.
- `ugcArticleDiscussionArticle` keys off **`topicId`**, not `uuid`.
- **The two `topicId` arguments have different types, and this is not a typo**: `ugcArticleDiscussionArticle` takes `ID`, `topicComments` takes `Int!`. Using one type for both fails on whichever call you guessed wrong.
- `content` is **null in list mode** — only `summary` is populated; bodies need stage 2.
- `totalNum` on the list connection is **capped** (3000), not the real result count — page until a short page instead of trusting it.
- `topicComments.orderBy` is a plain `String` (`most_votes` / `newest_to_oldest` / `oldest_to_newest` / `hot`), not an enum.
- Rapid probing trips a WAF returning **HTML 403s, not JSON** — parse defensively and keep ~2–3 s between requests.

## 4) Related docs in this repo

- [`doc/LC_google_problem_patterns_summary.md`](./LC_google_problem_patterns_summary.md)
- [`doc/goog_swe_prep_plan_claude.md`](./goog_swe_prep_plan_claude.md)
- [`doc/goog_swe_prep_plan_gpt.md`](./goog_swe_prep_plan_gpt.md)
- [`doc/goog_swe_prep_plan_gpt_v2.md`](./goog_swe_prep_plan_gpt_v2.md)
- [`doc/google_leetcode_problems_by_tags.md`](./google_leetcode_problems_by_tags.md)
- [`doc/google_swe_lc_essentials.md`](./google_swe_lc_essentials.md)

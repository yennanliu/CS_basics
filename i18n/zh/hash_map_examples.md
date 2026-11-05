<!-- 16454bbfdfc5 -->
# 雜湊表 — 實戰題解

> **範圍** — 雜湊表家族的經典 LC 題目，寫成題目卡，各自歸在它所歸約到的 [hash_map.md](./hash_map.md) 模板底下；本檔沒有自己的模板。
> **另見** — *核心文件*：[hash_map.md](./hash_map.md) — 這些題目歸約到的十個模板、辨識表，以及有序 map（`TreeMap` / `SortedDict`）的 API 參考。
> *鄰近文件*：[prefix_sum.md](./prefix_sum.md) — 前綴和自成一家的完整說明；[hashing.md](./hashing.md) — 雜湊怎麼運作，以及計數與 rolling hash 的慣用寫法；[set.md](./set.md) — 只管有沒有，不管值。

<!-- d19c3a8130bb -->
## LeetCode 題目清單

- [Hash Table](https://leetcode.com/problem-list/hash-table/)

<!-- dbe57a2f3490 -->
## 總覽

這份是 [hash_map.md](./hash_map.md) 的題目集：模板在那邊，這裡的每一題都是一張卡，寫明它歸約到哪個模板、以及多加了哪一步。各個 `##` 章節依核心文件十個模板的順序排列。最好的詳解放在別份文件的題目，在這裡只留一列指標，所以 [Index](#index) 仍然列出整組題目。

<!-- 1e1193d3f9a0 -->
## 索引

| LC | 題目 | 歸約到 | 難度 | 詳解在 |
|---|---|---|---|---|
| 1 | Two Sum | [hash_map § Template 1](./hash_map.md#template-1-complement-lookup--lc-1) | 簡單 | hash_map.md（錨點題） |
| 1010 | Pairs of Songs With Total Durations Divisible by 60 | [hash_map § Template 1](./hash_map.md#template-1-complement-lookup--lc-1) | 中等 | [下方](#pairs-of-songs-with-total-durations-divisible-by-60--lc-1010) |
| 532 | K-diff Pairs in an Array | [hash_map § Template 1](./hash_map.md#template-1-complement-lookup--lc-1) | 中等 | [set_examples.md](./set_examples.md#variation-b-probe-a-transformed-key-not-the-element-itself--lc-532) |
| 336 | Palindrome Pairs | [hash_map § Template 1](./hash_map.md#template-1-complement-lookup--lc-1) | 困難 | [palindrome.md § Template 9](./palindrome.md#template-9-palindrome-pairs-via-hashmap-split--lc-336-) |
| 352 | Data Stream as Disjoint Intervals | [hash_map § Template 1](./hash_map.md#template-1-complement-lookup--lc-1) | 困難 | [下方](#data-stream-as-disjoint-intervals--lc-352) |
| 242 | Valid Anagram | [hash_map § Template 2](./hash_map.md#template-2-frequency-count--lc-242) | 簡單 | hash_map.md（錨點題） |
| 554 | Brick Wall | [hash_map § Template 2](./hash_map.md#template-2-frequency-count--lc-242) | 中等 | [下方](#brick-wall--lc-554) |
| 846 | Hand of Straights | [hash_map § Template 2](./hash_map.md#template-2-frequency-count--lc-242) | 中等 | [下方](#hand-of-straights--lc-846) |
| 1726 | Tuple with Same Product | [hash_map § Template 2](./hash_map.md#template-2-frequency-count--lc-242) | 中等 | [下方](#tuple-with-same-product--lc-1726) |
| 187 | Repeated DNA Sequences | [hash_map § Template 2](./hash_map.md#template-2-frequency-count--lc-242) | 中等 | [hashing.md § 2-5](./hashing.md#2-5-repeated-dna-sequences-lc-187--sliding-window-hashset) |
| 763 | Partition Labels | [string § Template 8](./string.md#template-8-greedy-partition-by-last-occurrence--lc-763-) | 中等 | string.md |
| 49 | Group Anagrams | [hash_map § Template 3](./hash_map.md#template-3-grouping-by-a-computed-key--lc-49) | 中等 | hash_map.md（錨點題） |
| 734 | Sentence Similarity | [hash_map § Template 3](./hash_map.md#pattern-unordered-pair-key--min-max-counts-x-y-in-either-order-) | 簡單 | [下方](#sentence-similarity--lc-734) |
| 560 | Subarray Sum Equals K | [hash_map § Template 4](./hash_map.md#template-4-prefix-sum-count-map--lc-560-) | 中等 | hash_map.md（錨點題） |
| 525 | Contiguous Array | [hash_map § Template 4](./hash_map.md#template-4-prefix-sum-count-map--lc-560-) | 中等 | [下方](#contiguous-array--lc-525) |
| 325 | Maximum Size Subarray Sum Equals k | [hash_map § Template 4](./hash_map.md#template-4-prefix-sum-count-map--lc-560-) | 中等 | [prefix_sum_examples.md § 1](./prefix_sum_examples.md#1-maximum-size-subarray-sum-equals-k--lc-325) |
| 523 | Continuous Subarray Sum | [hash_map § Template 4](./hash_map.md#template-4-prefix-sum-count-map--lc-560-) | 中等 | [prefix_sum_examples.md § 2](./prefix_sum_examples.md#2-continuous-subarray-sum--lc-523) |
| 974 | Subarray Sums Divisible by K | [hash_map § Template 4](./hash_map.md#template-4-prefix-sum-count-map--lc-560-) | 中等 | [prefix_sum.md § Template 3](./prefix_sum.md#template-3-modulo-prefix-sum-divisibility-problems--lc-974) |
| 1248 | Count Number of Nice Subarrays | [hash_map § Template 4](./hash_map.md#template-4-prefix-sum-count-map--lc-560-) | 中等 | [prefix_sum.md § Template 6](./prefix_sum.md#template-6-transform-and-count--lc-1248) |
| 438 | Find All Anagrams in a String | [hash_map § Template 5](./hash_map.md#template-5-fixed-window-character-counts--lc-438) | 中等 | hash_map.md（錨點題） |
| 3 | Longest Substring Without Repeating Characters | [sliding_window § Template 3](./sliding_window.md#template-3-longest-window-satisfying-p--lc-3-) | 中等 | sliding_window.md |
| 424 | Longest Repeating Character Replacement | [sliding_window § Template 3](./sliding_window.md#template-3-longest-window-satisfying-p--lc-3-) | 中等 | [sliding_window_examples.md § 5](./sliding_window_examples.md#5-longest-repeating-character-replacement--lc-424-) |
| 953 | Verifying an Alien Dictionary | [hash_map § Template 6](./hash_map.md#template-6-rank-map--value-to-position--lc-953-) | 簡單 | hash_map.md（錨點題） |
| 670 | Maximum Swap | [hash_map § Template 6](./hash_map.md#template-6-rank-map--value-to-position--lc-953-) | 中等 | [下方](#maximum-swap--lc-670) |
| 2471 | Minimum Number of Operations to Sort a Binary Tree by Level | [hash_map § Template 6](./hash_map.md#template-6-rank-map--value-to-position--lc-953-) | 中等 | [下方](#minimum-number-of-operations-to-sort-a-binary-tree-by-level--lc-2471) |
| 205 | Isomorphic Strings | [hash_map § Template 7](./hash_map.md#template-7-bijection-with-two-maps--lc-205) | 簡單 | hash_map.md（錨點題） |
| 290 | Word Pattern | [hash_map § Template 7](./hash_map.md#template-7-bijection-with-two-maps--lc-205) | 簡單 | hash_map.md |
| 133 | Clone Graph | [hash_map § Template 8](./hash_map.md#template-8-old-to-new-node-map--lc-133) | 中等 | hash_map.md（錨點題） |
| 1257 | Smallest Common Region | [hash_map § Template 8](./hash_map.md#template-8-old-to-new-node-map--lc-133) | 中等 | [下方](#smallest-common-region--lc-1257) |
| 710 | Random Pick with Blacklist | [hash_map § Template 9](./hash_map.md#template-9-virtual-map-remapping--lc-710) | 困難 | hash_map.md（錨點題） |
| 692 | Top K Frequent Words | [hash_map § Template 10](./hash_map.md#template-10-map-plus-heap--lc-692) | 中等 | hash_map.md（錨點題，堆積與分桶兩種解法） |
| 347 | Top K Frequent Elements | [heap § Template 2](./heap.md#template-2-count-then-a-size-k-heap-on-the-count--lc-347-) | 中等 | heap.md（堆積與分桶兩種解法） |
| 621 | Task Scheduler | [heap § Template 8](./heap.md#template-8-max-heap--cooldown-queue--lc-358) | 中等 | [heap_examples.md](./heap_examples.md#task-scheduler--lc-621-) |
| 767 | Reorganize String | [heap § Template 7](./heap.md#template-7-greedy-build-with-a-run-length-cap--lc-1405) | 中等 | [heap_examples.md](./heap_examples.md#reorganize-string--lc-767-) |
| 36 | Valid Sudoku | [hashing § Template 6](./hashing.md#template-6-canonical-composite-key-lc-36) | 中等 | hashing.md |
| 146 | LRU Cache | [linked_list § Template 7](./linked_list.md#template-7-hash-map--doubly-linked-list-lru--lc-146-) | 中等 | linked_list.md |
| 139 | Word Break | [dp § Template 1b](./dp.md#template-1b-prefix-partition-dp--lc-139-) | 中等 | dp.md |
| 322 | Coin Change | [knapsack § Template 4](./knapsack.md#template-4-unbounded-min-count--lc-322-) | 中等 | knapsack.md |
| 1048 | Longest String Chain | [dp_advanced § Longest String Chain](./dp_advanced.md#longest-string-chain--lc-1048) | 中等 | dp_advanced.md |
| 1044 | Longest Duplicate Substring | [string_matching_kmp_rolling_hash § 2-3](./string_matching_kmp_rolling_hash.md#2-3-longest-duplicate-substring-lc-1044) | 困難 | string_matching_kmp_rolling_hash.md |
| 204 | Count Primes | [combinatorics_math_patterns § Sieve](./combinatorics_math_patterns.md#sieve-of-eratosthenes--lc-204) | 中等 | combinatorics_math_patterns.md |
| 496 | Next Greater Element I | [monotonic_stack § Template 1](./monotonic_stack.md#template-1-next-greater-element-decreasing-stack--lc-496) | 簡單 | monotonic_stack.md |
| 503 | Next Greater Element II | [monotonic_stack § Template 4](./monotonic_stack.md#template-4-circular-array-processing--lc-503) | 中等 | monotonic_stack.md |
| 739 | Daily Temperatures | [stack § Template 3](./stack.md#template-3-monotonic-stack--next-greater--smaller--lc-739-) | 中等 | stack.md |
| 853 | Car Fleet | [monotonic_stack § 2-10](./monotonic_stack.md#2-10-the-car-fleet-family-lc-853--4045--1776--when-a-stack-is-really-a-greedy-) | 中等 | monotonic_stack.md |
| 729 | My Calendar I | [hash_map § Language APIs](./hash_map.md#language-apis--sorted-map-treemap--sorteddict) | 中等 | [design_examples.md](./design_examples.md#my-calendar-i--lc-729) |
| 731 | My Calendar II | [hash_map § Language APIs](./hash_map.md#language-apis--sorted-map-treemap--sorteddict) | 中等 | [design_examples.md](./design_examples.md#my-calendar-ii--lc-731) |
| 732 | My Calendar III | [hash_map § Language APIs](./hash_map.md#language-apis--sorted-map-treemap--sorteddict) | 困難 | [design_examples.md](./design_examples.md#my-calendar-iii--lc-732) |

**歸約到**指向別份文件的那一列，就在那份文件詳解；列在這裡，是為了讓在本檔搜尋時還找得到它。

<!-- 8e8db4cc0061 -->
## 查補數的例題

<!-- 27600f2276b5 -->
### Pairs of Songs With Total Durations Divisible by 60 — LC 1010

> **關鍵** — 一首歌的補數是**餘數**，不是值：`t` 會和之前每一首 `t % 60` 等於 `(60 - t % 60) % 60` 的歌配對，所以 map 依餘數計歌數，答案是把次數加上去，而不是回傳一個索引。

**歸約到**：[hash_map § Template 1](./hash_map.md#template-1-complement-lookup--lc-1) — 鍵是 `t % 60`，值是之前有幾首歌是這個餘數，因為題目要數配對，而不是找出一組。

<!--CODE-->

**陷阱**
- `need = 60 - rem` 沒有外層的 `% 60`，遇到 60 的倍數就會去查鍵 60：`[60, 60, 60]` 回傳 0 而不是 3。
- 先記錄再查找，餘數是 0 或 30 的歌會和自己配對：`[60]` 回傳 1 而不是 0。

<!-- 7437337c9682 -->
### Data Stream as Disjoint Intervals — LC 352

> **關鍵** — 新值 `v` 只可能碰到**結束**於 `v - 1` 的區間，以及**開始**於 `v + 1` 的區間，所以兩個邊界 map —— `{start: end}` 和 `{end: start}` —— 讓每次合併都只是兩次 O(1) 查找。這兩個 map 永遠剛好存著目前的不相交區間。

**歸約到**：[hash_map § Template 1](./hash_map.md#template-1-complement-lookup--lc-1) — 要探查的「夥伴」是鄰居 `v - 1` 和 `v + 1`，各在一個邊界 map 裡；只有 `getIntervals` 需要排序。

<!--CODE-->

**試跑**

<!--CODE-->

**陷阱**
- 沒有 `seen` 集合：依序加入 `1, 3, 2` 之後再加一次 `2`，第二次找不到相鄰的邊界，於是在 `[1, 3]` 裡面多出一個 `[2, 2]`。
- 合併時只 pop 一邊，會留下過時的邊界：把 `[1,1]` 和 `[3,3]` 接起來後，若還留著 `end_to_start[1]`，之後的 `2` 又會再合併一次。

**不適用時** — `getIntervals` 呼叫得遠比 `addNum` 頻繁：把區間存在有序 map 裡，讀取就是 O(k)、不必每次重新排序（[hash_map § Language APIs](./hash_map.md#language-apis--sorted-map-treemap--sorteddict)）。

| 也歸約到 Template 1 | 題目 | 詳解在 | 難度 |
|---|---|---|---|
| LC 1 | Two Sum | [hash_map § Template 1](./hash_map.md#template-1-complement-lookup--lc-1) | 簡單 |
| LC 532 | K-diff Pairs in an Array | [set_examples.md](./set_examples.md#variation-b-probe-a-transformed-key-not-the-element-itself--lc-532) | 中等 |
| LC 336 | Palindrome Pairs | [palindrome.md § Template 9](./palindrome.md#template-9-palindrome-pairs-via-hashmap-split--lc-336-) | 困難 |

<!-- 4bf67a416162 -->
## 頻率計數的例題

<!-- b1a3dcb9acbf -->
### Brick Wall — LC 554

> **關鍵** — 垂直線在**磚縫**最多的地方穿過的磚最少，所以去數磚縫的位置（每一列累積的寬度，不含牆的右邊界），答案是 `列數 - 最大的次數`。

**歸約到**：[hash_map § Template 2](./hash_map.md#template-2-frequency-count--lc-242) — 被計數的東西是前綴寬度，而不是輸入裡的元素。

<!--CODE-->

**陷阱**
- 把每列的最後一塊磚也算進去：牆的右邊界每一列都有，`most_edges` 會變成 `len(wall)`，每個輸入都回傳 0。
- 每列都只有一塊磚（`[[1],[1],[1]]`）：沒有任何內部磚縫，map 是空的，對它取 `max()` 會丟例外 —— 把 `most_edges` 從 0 開始，答案就是 3。

<!-- bce403b8fe6c -->
### Hand of Straights — LC 846

> **關鍵** — 剩下最小的那張牌只能**當一組的開頭**，所以依排序走過相異的牌，對接下來 `groupSize` 張牌各花掉 `count[card]` 張，永遠不會做出本來可以更好的選擇。

**歸約到**：[hash_map § Template 2](./hash_map.md#template-2-frequency-count--lc-242) — 計數表依**鍵的排序**讀取；Java 的 `TreeMap` 免費維持這個順序（[hash_map § Language APIs](./hash_map.md#language-apis--sorted-map-treemap--sorteddict)）。

<!--CODE-->

**複雜度** — 排序相異的牌是 O(n log n)；內層迴圈只對當開頭的牌執行，而每次至少用掉 `groupSize` 張牌，所以總共是 O(n)。

**陷阱**
- 依插入順序走過鍵：`hand = [2, 1], groupSize = 2` 從 2 開始，找不到 3，回傳 `False`；依排序則從 1 開始，回傳 `True`。
- 一次只扣一組而不是 `starts` 組：結果正確，但 `[1]*k + [2]*k` 配 `groupSize = 2` 會把同一段重走 k 次。

<!-- 486bd4a0247c -->
### Tuple with Same Product — LC 1726

> **關鍵** — 數出每個乘積有幾組索引配對；同一個乘積有 `c` 組配對，就有 `C(c, 2)` 種「兩組配對」的選法，而每一種 `(a, b), (c, d)` 會給出 8 個有序 tuple，所以答案是 `sum(8 * C(c, 2))`。

**歸約到**：[hash_map § Template 2](./hash_map.md#template-2-frequency-count--lc-242) — 被計數的是一組配對的乘積，答案是把次數組合起來，而不是互相比較。

<!--CODE-->

<!--CODE-->

**陷阱**
- 忘了乘 8：`[2, 3, 4, 6]` 只有一種兩組配對（`2·6 = 3·4`），答案是 8 而不是 1。這些順序是：第一組內 2 種 × 第二組內 2 種 × 哪一組在前 2 種。
- 乘 8 的前提是兩組配對沒有共用元素。這只因為值都**相異**才成立：`a·b = a·c` 會推出 `b = c`。

| 也歸約到 Template 2 | 題目 | 詳解在 | 難度 |
|---|---|---|---|
| LC 242 | Valid Anagram | [hash_map § Template 2](./hash_map.md#template-2-frequency-count--lc-242) | 簡單 |
| LC 187 | Repeated DNA Sequences | [hashing.md § 2-5](./hashing.md#2-5-repeated-dna-sequences-lc-187--sliding-window-hashset) | 中等 |
| LC 763 | Partition Labels | [string § Template 8](./string.md#template-8-greedy-partition-by-last-occurrence--lc-763-) | 中等 |

<!-- bf3bf77890b8 -->
## 用算出來的鍵分組的例題

<!-- 8fdadb7e0cd7 -->
### Sentence Similarity — LC 734

> **關鍵** — 相似關係是**對稱但不遞移**的，所以每一組配對都用無序鍵 `(min, max)` 只存一次，每個字的位置就是一次 O(1) 的成員查詢；不需要圖。

**歸約到**：[hash_map § Template 3 — 無序配對鍵](./hash_map.md#pattern-unordered-pair-key--min-max-counts-x-y-in-either-order-) — 標準鍵讓 `["great", "fine"]` 和 `["fine", "great"]` 成為同一筆；因為不需要計數，用集合就夠了。

<!--CODE-->

**陷阱**
- 在原始配對串列裡查 `[a, b]`：結果正確，但每個字都是 O(p)，而且還得再試一次 `[b, a]`。
- 一個字和自己相似，不需要列出配對：沒有任何配對時 `["great"]` 對 `["great"]` 是 `True`，`a != b` 的檢查會給出這個結果。
- 沒有遞移性：`great ~ fine` 和 `fine ~ good` 在這裡**不會**推出 `great ~ good`。LC 737 加上了遞移性，那就需要 union-find（[dfs_examples.md](./dfs_examples.md)）。

| 也歸約到 Template 3 | 題目 | 詳解在 | 難度 |
|---|---|---|---|
| LC 49 | Group Anagrams | [hash_map § Template 3](./hash_map.md#template-3-grouping-by-a-computed-key--lc-49) | 中等 |

<!-- beae4283469c -->
## 前綴和計數表的例題

<!-- 8221908a8ac8 -->
### Contiguous Array — LC 525

> **關鍵** — 把每個 `0` 當成 `-1`，子陣列裡 0 和 1 一樣多，若且唯若它的和是 0，也就是若且唯若它兩端的累積和相等。map 記下每個累積和**第一次**出現的索引，並預先放入 `{0: -1}`，所以 `i - first[sum]` 就是結尾在 `i` 的最長平衡子陣列。

**歸約到**：[hash_map § Template 4](./hash_map.md#template-4-prefix-sum-count-map--lc-560-) — 目標是相同的前綴和（k = 0），而 map 存的是第一次出現的索引而不是次數，因為題目問的是最長。

<!--CODE-->

<!--CODE-->

**試跑**

<!--CODE-->

<p align="center"><img src="../pic/lc_525_1.png"></p>

**陷阱**
- 沒有預先放入 `{0: -1}`：`[0, 1]` 回傳 0 而不是 2，因為那個平衡子陣列從索引 0 開始。
- 每次重複都更新索引，留下的是**最後**一次出現：`[0, 1, 0, 1]` 回傳 2 而不是 4。
- 計數表（LC 560 的 `{sum: count}`）回答的是「有幾個」，不是「有多長」；這裡的值必須是索引。

| 也歸約到 Template 4 | 題目 | 詳解在 | 難度 |
|---|---|---|---|
| LC 560 | Subarray Sum Equals K | [hash_map § Template 4](./hash_map.md#template-4-prefix-sum-count-map--lc-560-) | 中等 |
| LC 325 | Maximum Size Subarray Sum Equals k | [prefix_sum_examples.md § 1](./prefix_sum_examples.md#1-maximum-size-subarray-sum-equals-k--lc-325) | 中等 |
| LC 523 | Continuous Subarray Sum | [prefix_sum_examples.md § 2](./prefix_sum_examples.md#2-continuous-subarray-sum--lc-523) | 中等 |
| LC 974 | Subarray Sums Divisible by K | [prefix_sum.md § Template 3](./prefix_sum.md#template-3-modulo-prefix-sum-divisibility-problems--lc-974) | 中等 |
| LC 1248 | Count Number of Nice Subarrays | [prefix_sum.md § Template 6](./prefix_sum.md#template-6-transform-and-count--lc-1248) | 中等 |

<!-- 60c974be00b0 -->
## 固定視窗字元計數的例題

錨點題在核心文件裡詳解；長度可變、同樣維護計數表的視窗，在滑動視窗的文件裡詳解。

| 歸約到 Template 5，或歸約到滑動視窗 | 題目 | 詳解在 | 難度 |
|---|---|---|---|
| LC 438 | Find All Anagrams in a String | [hash_map § Template 5](./hash_map.md#template-5-fixed-window-character-counts--lc-438) | 中等 |
| LC 3 | Longest Substring Without Repeating Characters | [sliding_window § Template 3](./sliding_window.md#template-3-longest-window-satisfying-p--lc-3-) | 中等 |
| LC 424 | Longest Repeating Character Replacement | [sliding_window_examples.md § 5](./sliding_window_examples.md#5-longest-repeating-character-replacement--lc-424-) | 中等 |

<!-- 221a8291db33 -->
## 排名表的例題

<!-- b88f9ffab153 -->
### Maximum Swap — LC 670

> **關鍵** — `last[d]` 是數字 `d` 最右邊的索引。第一個「後面還有更大數字」的位置 `i`，是一次交換唯一能把最高位數字變大的地方，而最好的交換對象是這些數字裡**最大**的那個、取它的**最後**一個位置。

**歸約到**：[hash_map § Template 6](./hash_map.md#template-6-rank-map--value-to-position--lc-953-) — 一次建好的值 → 位置 map，所以「我右邊有沒有更大的數字、在哪裡？」最多只要 9 次查找。不用 map 的三指標掃描在 [array_examples.md § 4](./array_examples.md#4-maximum-swap--lc-670)。

<!--CODE-->

<!--CODE-->

**試跑**

<!--CODE-->

**陷阱**
- 和較大數字的**第一個**位置交換：`1993` 會得到 `9193`；用最後一個位置才是 `9913`。
- 取第一個找到的較大數字，而不是最大的：`2736` 會把 2 和 3 交換，回傳 `3726`。
- 沒有可以改進的位置（`9973`）：原封不動回傳 `num`。

<!-- 1992234827ac -->
### Minimum Number of Operations to Sort a Binary Tree by Level — LC 2471

> **關鍵** — 各層彼此獨立，所以答案是把每一層排好所需最少交換次數的總和。`{value: current index}` map 讓每次交換都是 O(1)：在索引 `i`，取出該放在這裡的值目前在哪，更新 map 的兩筆，再交換。每次交換都會把一個值放到最終位置，所以次數是最少的。

**歸約到**：[hash_map § Template 6](./hash_map.md#template-6-rank-map--value-to-position--lc-953-) — 一個值 → 位置的 map，隨著陣列變動保持最新；BFS 負責提供每一層。

<!--CODE-->

<!--CODE-->

**試跑**

<!--CODE-->

**陷阱**
- 先交換、再更新 map：`arr[i]` 已經是 `want` 了，被換走的值的新索引會寫到錯的鍵底下。
- 用 `arr.index(want)` 找 `want` 而不是查 map：結果正確，但每次交換 O(w)，每層 O(w²)。

| 也歸約到 Template 6 | 題目 | 詳解在 | 難度 |
|---|---|---|---|
| LC 953 | Verifying an Alien Dictionary | [hash_map § Template 6](./hash_map.md#template-6-rank-map--value-to-position--lc-953-) | 簡單 |

<!-- 2cd3fb60dd4b -->
## 雙射的例題

這張卡的兩題都在核心文件裡詳解。

| 歸約到 Template 7 | 題目 | 詳解在 | 難度 |
|---|---|---|---|
| LC 205 | Isomorphic Strings | [hash_map § Template 7](./hash_map.md#template-7-bijection-with-two-maps--lc-205) | 簡單 |
| LC 290 | Word Pattern | [hash_map § Template 7](./hash_map.md#template-7-bijection-with-two-maps--lc-205) | 簡單 |

<!-- 387d6f055dad -->
## 舊節點對新節點表的例題

<!-- 924145f919b0 -->
### Smallest Common Region — LC 1257

> **關鍵** — 每個串列列出一個父區域和它的子區域，所以 `{child: parent}` map 把這些串列變成一棵有父指標的樹，答案就是最低共同祖先：把 `region1` 的所有祖先放進集合，再讓 `region2` 往上走，直到碰到其中一個。

**歸約到**：[hash_map § Template 8](./hash_map.md#template-8-old-to-new-node-map--lc-133) — map 是 `{child: parent}` 而不是 `{original: clone}`；往上走的那段就是 [tree_lca_distance.md](./tree_lca_distance.md#lca-variant--parent-pointers-no-root-lc-1650-) 的父指標 LCA。

<!--CODE-->

<!--CODE-->

**陷阱**
- 沒把 `region1` 放進它自己的祖先集合：當 `region1` 包含 `region2`（`"North America"`、`"New York"`）時，往上走會越過 `"North America"`，答成 `"Earth"`。
- 改存 `{parent: [children]}`：那是由上往下 DFS 的正確形狀，但要找祖先就得從根開始搜尋。

| 也歸約到 Template 8 | 題目 | 詳解在 | 難度 |
|---|---|---|---|
| LC 133 | Clone Graph | [hash_map § Template 8](./hash_map.md#template-8-old-to-new-node-map--lc-133) | 中等 |

<!-- b45c8493acef -->
## 虛擬映射的例題

| 歸約到 Template 9 | 題目 | 詳解在 | 難度 |
|---|---|---|---|
| LC 710 | Random Pick with Blacklist | [hash_map § Template 9](./hash_map.md#template-9-virtual-map-remapping--lc-710) | 困難 |

<!-- c9692a5997d5 -->
## map 加堆積的例題

LC 692 在核心文件裡詳解，有堆積和依次數分桶兩種解法。其他從計數表出發的 top-K 與排程題，在堆積的文件裡詳解。

| 歸約到 Template 10，或歸約到堆積的卡 | 題目 | 詳解在 | 難度 |
|---|---|---|---|
| LC 692 | Top K Frequent Words | [hash_map § Template 10](./hash_map.md#template-10-map-plus-heap--lc-692) | 中等 |
| LC 347 | Top K Frequent Elements | [heap § Template 2](./heap.md#template-2-count-then-a-size-k-heap-on-the-count--lc-347-) | 中等 |
| LC 621 | Task Scheduler | [heap_examples.md](./heap_examples.md#task-scheduler--lc-621-)，歸約到 [heap § Template 8](./heap.md#template-8-max-heap--cooldown-queue--lc-358) | 中等 |
| LC 767 | Reorganize String | [heap_examples.md](./heap_examples.md#reorganize-string--lc-767-)，歸約到 [heap § Template 7](./heap.md#template-7-greedy-build-with-a-run-length-cap--lc-1405) | 中等 |

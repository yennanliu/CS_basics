<!-- b89aea764814 -->
# 前綴和 — 範例詳解

> **範圍** — [prefix_sum.md](./prefix_sum.md) 背後的解題存檔：七道模板無法從頭到尾解掉的題目，依照它們需要哪種前綴和形狀分組。
> **另見**：[prefix_sum.md](./prefix_sum.md) — 母文件：模板 1–8、觀念與決策框架；[prefix_sum_advanced.md](./prefix_sum_advanced.md) — 模板 9–13；[difference_array.md](./difference_array.md) — 區間更新本身的完整討論，含 LC 370；[sliding_window.md](./sliding_window.md) — 當所有數值都非負時的替代解法；[hash_map.md](./hash_map.md) — 這裡有四題的關鍵結構。

<!-- 1d1496862506 -->
## LeetCode 題目清單

- [Prefix Sum](https://leetcode.com/problem-list/prefix-sum/)

<!-- bc6b5aa57dcd -->
## 總覽

這是 [prefix_sum.md](./prefix_sum.md) 的長尾，而且刻意寫得很短。母文件的十三個模板各自標明了自己解掉的
LC 題號，所以一個把那些題目重解一遍的範例區塊，就是整份檔案最大的重複來源 — 有十四個 LC 題號
出現在不只一個章節標題裡，是整個文件庫測出來最糟的數字。

留下來的，是沒有任何模板能直接完整解掉的題目。

<!-- ad9da13e6da0 -->
### 關鍵性質
- **複雜度**：下面每一個解法在前綴和陣列建好之後都是 O(n) 時間，只有 LC 1292 例外，它是 O(m·n·log(min(m,n)))
- **核心想法**：七題裡有四題其實是同一招 — 用雜湊表記錄「前綴值 → 出現過幾次」 — 只是套在輸入的不同轉換上
- **什麼時候用**：等母文件的決策框架先幫你選好模板之後

<!-- 0f5d1897e7c0 -->
## 用雜湊表處理子陣列和

<!-- caa894b7f13c -->
### 3) Longest Well-Performing Interval — LC 1124


**模式：** 雜湊表 + 前綴和 — 找出總和為正的最長子陣列

**核心想法：**
把每一天做轉換：疲勞（`hours[i] > 8`）→ `+1`，不疲勞 → `-1`。問題就變成：找出總和 > 0 的最長子陣列。

<!--CODE-->

**和模板 2 的關鍵差異：**
- 模板 2 存的是 `{prefix_sum: count}`，用來計算子陣列個數。
- 這個變形存的是 `{prefix_sum: first_index}`，用來求最大長度 — 只有第一次出現才有意義，因為起點越早，區間越長。

**Java 程式碼：**
<!--CODE-->

**類似題目：**
| 題目 | LC # | 相似之處 |
|---------|------|------------|
| Contiguous Array | 525 | 0 和 1 一樣多的最長子陣列 — 同一個模式，目標和 = 0 |
| Maximum Size Subarray Sum Equals k | 325 | 總和 = k 的最長子陣列，首次出現位置的雜湊表 |
| Subarray Sum Equals K | 560 | 計數版（存次數，不是索引） |
| Binary Subarrays With Sum | 930 | 計算二元轉換後總和 = k 的子陣列個數 |

<!-- 9163151c4d18 -->
## 固定視窗與成對視窗

<!-- 3cab18e5b6b4 -->
### 5) Maximum Sum of Two Non-Overlapping Subarrays — LC 1031


**核心想法（LC 1031）：**
<!--CODE-->

**模式：** 前綴和 + 滾動最大值（掃兩趟）
- 前綴和陣列只建一次：O(n)
- 每一趟都把第二個視窗往右滑，同時維護 `maxFirst`（到目前為止最好的第一個視窗）
- 兩趟就涵蓋了所有不重疊的擺法

<!--CODE-->

**另一種輔助函式寫法（比較乾淨）：**
<!--CODE-->

**Python（前綴和 + 滾動最大值）：**
<!--CODE-->

**為什麼這樣是對的（核心想法回顧）：**
<!--CODE-->

**類似題目：**
| 題目 | LC # | 相似之處 |
|---------|------|------------|
| Maximum Subarray | 53 | 滾動最大子陣列（Kadane） |
| Best Time to Buy and Sell Stock III | 123 | 兩段不重疊的操作，前綴 + 後綴 |
| Maximum Sum of 3 Non-Overlapping Subarrays | 689 | 同一個模式擴充到 3 個視窗 |
| Subarray Sum Equals K | 560 | 前綴和 + 雜湊表 |
| Maximum Average Subarray II | 644 | 固定／可變視窗搭配前綴和 |

<!-- 42d1bd636b9b -->
## 二維前綴和

<!-- 29a162fe4d1c -->
### 6) Maximum Side Length of a Square with Sum ≤ Threshold — LC 1292


**模式：** 二維前綴和 + 二分搜尋 **或** 二維前綴和 + 貪婪

**核心想法：**
1. 建一張二維前綴和表（大小 `(m+1) x (n+1)`），這樣任何一個正方形的總和都能 O(1) 算出來。
2. **二分搜尋解法**：對邊長 `[1, min(m,n)]` 做二分搜尋。對每個候選邊長 `mid`，掃過所有合法的左上角，檢查是否存在總和 ≤ threshold 的正方形。→ O(m·n·log(min(m,n)))
3. **貪婪解法**：掃過所有格子一次；在每個格子 `(i,j)` 只測試邊長 `maxSide+1` 的正方形放不放得下。放得下就把 `maxSide` 加一。→ O(m·n)

**二維前綴和公式（以 (i,j) 為右下角、邊長 `k` 的正方形）：**
<!--CODE-->

**二分搜尋解法（Java）：**
<!--CODE-->

**貪婪解法（Java）：**
<!--CODE-->

**貪婪為什麼可行：** 我們只在乎能達到的*最大*邊長。由左到右、由上到下掃描保證不會漏掉任何合法正方形 — 如果某處存在更大的正方形，走到它的右下角時就一定會被發現。

**類似題目：**
| 題目 | LC # | 相似之處 |
|---------|------|------------|
| Range Sum Query 2D | 304 | 二維前綴和的核心模板 |
| Matrix Block Sum | 1314 | 固定半徑的二維區間查詢 |
| Number of Submatrices That Sum to Target | 1074 | 二維前綴和 + 計數（更難） |
| Maximal Square | 221 | 矩陣中的最大正方形（DP 解法） |
| Largest 1-Bordered Square | 1139 | 有邊框條件的最大正方形 |

<!-- ebb3e9da4efa -->
## 區間更新

<!-- 495bee970bef -->
### 7) Range Addition II / 差分陣列上的前綴和 — LC 1094


<!--CODE-->

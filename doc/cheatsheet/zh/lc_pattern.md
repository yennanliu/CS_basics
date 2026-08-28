# LeetCode 模式指南

> **範圍** — 最上層的地圖：拿到一題，它想要的是哪個模式（也就是該翻哪份小抄）。這裡只做索引與分流，不放模板。
> **另見**：[lc_category.md](./lc_category.md) — wisdompeak 的分類法，以及它每個類別對應到這裡哪一份；[complexity_cheatsheet.md](./complexity_cheatsheet.md) — 面試官會叫你講出來的那些複雜度。

⸻

## 🔹 0. 在挑模式之前 — 六步循環

> 挑對模式只是六步裡的第三步。它前後那五步，才是面試官真正在打分的東西，而且這頁上每一題都適用。這段在你第一次模擬面試前讀一次就好，不必留到複習時。

 
- 1) 弄懂題目（需求、輸入輸出、可接受的形式）
- 2) 問清楚
	- 範圍
		- 資料規模
		- 數值範圍
	- 限制
		- 題目的邊界
		- 預期的時間與空間複雜度
- 3) 提出 V1 解法
	- 演算法想法
	- 暴力解沒關係，但要把想法講清楚
	- 接著提出 V2 解法，說明想法、演算法與資料結構
	- 寫虛擬碼
	- 討論邊界情況
	- 跟面試官對齊，沒問題就開始寫程式
- 4) 寫程式
	- 程式要乾淨、邊寫邊講、變數名稱要好懂
	- 寫 `test cases`
- 5) 跑程式
	- 修 bug
	- 說明時間與空間複雜度
	- 驗證測資
- 6) 追問環節
	- 最佳化時間與空間複雜度
	- 如果輸入要能水平擴展呢
	- 如果輸入是串流（而不是批次）呢
	- 如果輸入塞不進伺服器記憶體呢

⸻

## 🔹 1. 陣列與字串

### **雙指標** — LC 167 ⭐⭐⭐⭐⭐
- **兩端往中間**：從頭尾同時往中間靠
  - LC 1: Two Sum（已排序）, LC 15: 3Sum, LC 42: Trapping Rain Water
  - LC 125: Valid Palindrome, LC 167: Two Sum II
- **快慢指標**：用不同速度偵測環
  - LC 141: Linked List Cycle, LC 142: Cycle II, LC 287: Find Duplicate
  - LC 26: Remove Duplicates, LC 80: Remove Duplicates II
- **同向移動**：兩個指標往同一個方向走
  - LC 283: Move Zeros, LC 75: Sort Colors, LC 11: Container With Water

### **滑動視窗** — LC 3 ⭐⭐⭐⭐⭐
- **固定大小視窗**：視窗長度不變
  - LC 643: Max Average Subarray, LC 1456: Max Vowels in Substring
  - LC 424: Longest Repeating Character Replacement
- **可變大小視窗**：依條件擴張／收縮
  - LC 3: Longest Substring Without Repeating, LC 76: Minimum Window Substring
  - LC 209: Minimum Size Subarray Sum, LC 904: Fruit Into Baskets
- **模板**：右邊擴張，不合法就從左邊收縮

### **前綴和與差分陣列** — LC 560 ⭐⭐⭐⭐
- **前綴和**：高效計算子陣列和
  - LC 560: Subarray Sum Equals K, LC 523: Continuous Subarray Sum
  - LC 325: Maximum Size Subarray Sum Equals K
- **二維前綴和**：矩陣的區域和查詢
  - LC 304: Range Sum Query 2D, LC 1314: Matrix Block Sum
- **差分陣列**：用 O(1) 做區間更新
  - LC 1109: Corporate Flight Bookings, LC 370: Range Addition

### **二分搜尋** — LC 704 ⭐⭐⭐⭐⭐
- **經典搜尋**：在已排序陣列中找目標
  - LC 704: Binary Search, LC 35: Search Insert Position
  - LC 34: Find First/Last Position, LC 33: Search in Rotated Array
- **對答案做二分**：搜的是答案空間
  - LC 875: Koko Eating Bananas, LC 1011: Ship Packages in D Days
  - LC 410: Split Array Largest Sum, LC 774: Minimize Max Distance
- **矩陣二分**：二維已排序矩陣
  - LC 74: Search 2D Matrix, LC 240: Search 2D Matrix II

### **排序 + 貪婪** — LC 56
- **區間**：合併、排程、互不重疊
  - LC 56: Merge Intervals, LC 57: Insert Interval
  - LC 435: Non-overlapping Intervals, LC 252: Meeting Rooms
- **陣列配對**：最佳配對策略
  - LC 455: Assign Cookies, LC 881: Boats to Save People
  - LC 870: Advantage Shuffle, LC 976: Largest Perimeter Triangle

### **字串演算法** — LC 28
- **模式比對**：KMP、Rabin-Karp、Z-algorithm
  - LC 28: Implement strStr(), LC 214: Shortest Palindrome
- **回文**：中心擴展、Manacher 演算法
  - LC 5: Longest Palindromic Substring, LC 647: Palindromic Substrings

⸻

## 🔹 2. 鏈結串列

### **反轉類** — LC 206
- **基本反轉**：迭代與遞迴兩種寫法
  - LC 206: Reverse Linked List, LC 92: Reverse Linked List II
- **分組反轉**：每 k 個一組反轉
  - LC 25: Reverse Nodes in k-Group, LC 24: Swap Nodes in Pairs
- **條件式反轉**：依條件決定要不要反轉
  - LC 2130: Maximum Twin Sum, LC 143: Reorder List

### **環偵測與快慢指標** — LC 141 ⭐⭐⭐⭐
- **Floyd 演算法**：偵測環並找出環的起點
  - LC 141: Linked List Cycle, LC 142: Linked List Cycle II
- **中點元素**：用快慢指標找中點
  - LC 876: Middle of Linked List, LC 234: Palindrome Linked List
- **移除倒數第 N 個節點**：利用快慢指標之間的間距
  - LC 19: Remove Nth Node from End

### **合併與排序** — LC 21
- **兩條串列合併**：合併兩條已排序串列
  - LC 21: Merge Two Sorted Lists, LC 1669: Merge In Between
- **多條串列合併**：用堆積或分治法
  - LC 23: Merge k Sorted Lists
- **串列排序**：有效率地排序鏈結串列
  - LC 148: Sort List（合併排序）, LC 147: Insertion Sort List

### **虛擬頭節點技巧** — LC 203
- **移除節點**：有虛擬頭節點，刪除好寫得多
  - LC 203: Remove Linked List Elements, LC 83: Remove Duplicates
- **建構串列**：用虛擬頭節點組出結果串列
  - LC 2: Add Two Numbers, LC 445: Add Two Numbers II
- **切分**：依條件把串列拆開
  - LC 86: Partition List, LC 725: Split Linked List in Parts

### **進階操作** — LC 138
- **深拷貝**：複製結構複雜的串列
  - LC 138: Copy List with Random Pointer
- **交點**：找出兩條串列的交會節點
  - LC 160: Intersection of Two Linked Lists
- **設計題**：用串列實作出的資料結構
  - LC 146: LRU Cache, LC 460: LFU Cache

⸻

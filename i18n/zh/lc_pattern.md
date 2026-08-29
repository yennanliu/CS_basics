<!-- 9d6318b50cd6 -->
# LeetCode 模式指南

> **範圍** — 最上層的地圖：拿到一題，它想要的是哪個模式（也就是該翻哪份小抄）。這裡只做索引與分流，不放模板。
> **另見**：[lc_category.md](./lc_category.md) — wisdompeak 的分類法，以及它每個類別對應到這裡哪一份；[complexity_cheatsheet.md](./complexity_cheatsheet.md) — 面試官會叫你講出來的那些複雜度。

⸻

<!-- 63ea2c2c6eb0 -->
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

<!-- 2cb72e89f444 -->
## 🔹 1. 陣列與字串

<!-- 95a9fa8d70f4 -->
### **雙指標** — LC 167 ⭐⭐⭐⭐⭐
- **兩端往中間**：從頭尾同時往中間靠
  - LC 1: Two Sum（已排序）, LC 15: 3Sum, LC 42: Trapping Rain Water
  - LC 125: Valid Palindrome, LC 167: Two Sum II
- **快慢指標**：用不同速度偵測環
  - LC 141: Linked List Cycle, LC 142: Cycle II, LC 287: Find Duplicate
  - LC 26: Remove Duplicates, LC 80: Remove Duplicates II
- **同向移動**：兩個指標往同一個方向走
  - LC 283: Move Zeros, LC 75: Sort Colors, LC 11: Container With Water

<!-- fdad3ad75bbe -->
### **滑動視窗** — LC 3 ⭐⭐⭐⭐⭐
- **固定大小視窗**：視窗長度不變
  - LC 643: Max Average Subarray, LC 1456: Max Vowels in Substring
  - LC 424: Longest Repeating Character Replacement
- **可變大小視窗**：依條件擴張／收縮
  - LC 3: Longest Substring Without Repeating, LC 76: Minimum Window Substring
  - LC 209: Minimum Size Subarray Sum, LC 904: Fruit Into Baskets
- **模板**：右邊擴張，不合法就從左邊收縮

<!-- 36905e14e7cd -->
### **前綴和與差分陣列** — LC 560 ⭐⭐⭐⭐
- **前綴和**：高效計算子陣列和
  - LC 560: Subarray Sum Equals K, LC 523: Continuous Subarray Sum
  - LC 325: Maximum Size Subarray Sum Equals K
- **二維前綴和**：矩陣的區域和查詢
  - LC 304: Range Sum Query 2D, LC 1314: Matrix Block Sum
- **差分陣列**：用 O(1) 做區間更新
  - LC 1109: Corporate Flight Bookings, LC 370: Range Addition

<!-- 3035073c72b0 -->
### **二分搜尋** — LC 704 ⭐⭐⭐⭐⭐
- **經典搜尋**：在已排序陣列中找目標
  - LC 704: Binary Search, LC 35: Search Insert Position
  - LC 34: Find First/Last Position, LC 33: Search in Rotated Array
- **對答案做二分**：搜的是答案空間
  - LC 875: Koko Eating Bananas, LC 1011: Ship Packages in D Days
  - LC 410: Split Array Largest Sum, LC 774: Minimize Max Distance
- **矩陣二分**：二維已排序矩陣
  - LC 74: Search 2D Matrix, LC 240: Search 2D Matrix II

<!-- 8ab72367b78d -->
### **排序 + 貪婪** — LC 56
- **區間**：合併、排程、互不重疊
  - LC 56: Merge Intervals, LC 57: Insert Interval
  - LC 435: Non-overlapping Intervals, LC 252: Meeting Rooms
- **陣列配對**：最佳配對策略
  - LC 455: Assign Cookies, LC 881: Boats to Save People
  - LC 870: Advantage Shuffle, LC 976: Largest Perimeter Triangle

<!-- b03eb7de21e2 -->
### **字串演算法** — LC 28
- **模式比對**：KMP、Rabin-Karp、Z-algorithm
  - LC 28: Implement strStr(), LC 214: Shortest Palindrome
- **回文**：中心擴展、Manacher 演算法
  - LC 5: Longest Palindromic Substring, LC 647: Palindromic Substrings

⸻

<!-- b4ae208155dd -->
## 🔹 2. 鏈結串列

<!-- 9ea53aca6366 -->
### **反轉類** — LC 206
- **基本反轉**：迭代與遞迴兩種寫法
  - LC 206: Reverse Linked List, LC 92: Reverse Linked List II
- **分組反轉**：每 k 個一組反轉
  - LC 25: Reverse Nodes in k-Group, LC 24: Swap Nodes in Pairs
- **條件式反轉**：依條件決定要不要反轉
  - LC 2130: Maximum Twin Sum, LC 143: Reorder List

<!-- 617ee2211301 -->
### **環偵測與快慢指標** — LC 141 ⭐⭐⭐⭐
- **Floyd 演算法**：偵測環並找出環的起點
  - LC 141: Linked List Cycle, LC 142: Linked List Cycle II
- **中點元素**：用快慢指標找中點
  - LC 876: Middle of Linked List, LC 234: Palindrome Linked List
- **移除倒數第 N 個節點**：利用快慢指標之間的間距
  - LC 19: Remove Nth Node from End

<!-- 6f7d8e11bce8 -->
### **合併與排序** — LC 21
- **兩條串列合併**：合併兩條已排序串列
  - LC 21: Merge Two Sorted Lists, LC 1669: Merge In Between
- **多條串列合併**：用堆積或分治法
  - LC 23: Merge k Sorted Lists
- **串列排序**：有效率地排序鏈結串列
  - LC 148: Sort List（合併排序）, LC 147: Insertion Sort List

<!-- de7c05f053b5 -->
### **虛擬頭節點技巧** — LC 203
- **移除節點**：有虛擬頭節點，刪除好寫得多
  - LC 203: Remove Linked List Elements, LC 83: Remove Duplicates
- **建構串列**：用虛擬頭節點組出結果串列
  - LC 2: Add Two Numbers, LC 445: Add Two Numbers II
- **切分**：依條件把串列拆開
  - LC 86: Partition List, LC 725: Split Linked List in Parts

<!-- f229fdf7ec4b -->
### **進階操作** — LC 138
- **深拷貝**：複製結構複雜的串列
  - LC 138: Copy List with Random Pointer
- **交點**：找出兩條串列的交會節點
  - LC 160: Intersection of Two Linked Lists
- **設計題**：用串列實作出的資料結構
  - LC 146: LRU Cache, LC 460: LFU Cache

⸻

<!-- c452ee519502 -->
## 🔹 3. 二元樹

<!-- 9cd583249f92 -->
### **DFS 走訪模式** — LC 94 ⭐⭐⭐⭐⭐
- **前序**：根 → 左 → 右（由上往下）
  - LC 144: Binary Tree Preorder, LC 257: Binary Tree Paths
  - LC 112: Path Sum, LC 113: Path Sum II
- **中序**：左 → 根 → 右（二元搜尋樹的遞增順序）
  - LC 94: Binary Tree Inorder, LC 98: Validate BST
  - LC 230: Kth Smallest in BST, LC 285: Inorder Successor
- **後序**：左 → 右 → 根（由下往上）
  - LC 145: Binary Tree Postorder, LC 543: Diameter of Tree
  - LC 124: Binary Tree Maximum Path Sum

<!-- 29d63b1aae58 -->
### **BFS／層序走訪模式** — LC 102 ⭐⭐⭐⭐⭐
- **逐層處理**：一層一層地處理節點
  - LC 102: Level Order Traversal, LC 107: Level Order II
  - LC 199: Right Side View, LC 515: Find Largest Value
- **鋸齒走訪**：左→右、右→左交替
  - LC 103: Binary Tree Zigzag Traversal
- **串接指標**：把同一層的節點連起來
  - LC 116: Populating Next Right Pointers, LC 117: Populating Next Right II

<!-- 9d36e26a3ffb -->
### **建樹（分治法）** — LC 105
- **由走訪序列建樹**：用中序／前序／後序組回樹
  - LC 105: Construct from Preorder/Inorder, LC 106: Construct from Inorder/Postorder
  - LC 889: Construct from Preorder/Postorder
- **由特殊陣列建樹**：從其他表示法組回樹
  - LC 108: Convert Sorted Array to BST, LC 109: Convert Sorted List to BST
  - LC 297: Serialize/Deserialize Binary Tree

<!-- ce960126d344 -->
### **二元搜尋樹模式** — LC 98
- **BST 驗證**：檢查是否符合 BST 性質
  - LC 98: Validate BST, LC 99: Recover BST
- **BST 搜尋與插入**：善用 BST 的性質
  - LC 700: Search in BST, LC 701: Insert into BST
  - LC 450: Delete Node in BST
- **BST 統計量**：找第 k 個元素、最接近的值
  - LC 230: Kth Smallest, LC 272: Closest BST Values
- **BST 上的 LCA**：利用 BST 的大小關係
  - LC 235: LCA in BST, LC 270: Closest BST Value

<!-- 200c70aac701 -->
### **樹形 DP（由下往上）** — LC 543
- **子樹性質**：從子節點的結果算出自己的
  - LC 543: Diameter of Tree, LC 124: Maximum Path Sum
  - LC 687: Longest Univalue Path, LC 968: Binary Tree Cameras
- **打家劫舍**：帶限制的最佳選擇
  - LC 337: House Robber III
- **樹的著色**：最佳地指派顏色／狀態
  - LC 979: Distribute Coins, LC 1145: Binary Tree Coloring Game

<!-- bcb87a34d3e0 -->
### **路徑與祖先問題** — LC 236 ⭐⭐⭐⭐
- **根到葉的路徑**：所有從根走到葉子的路徑
  - LC 257: Binary Tree Paths, LC 113: Path Sum II
- **任意路徑**：任兩個節點之間的路徑
  - LC 124: Binary Tree Maximum Path Sum, LC 687: Longest Univalue Path
- **最近共同祖先**：找兩個節點的 LCA
  - LC 236: LCA of Binary Tree, LC 1644: LCA II, LC 1650: LCA III

<!-- 697ad5b4eb89 -->
### **樹的改寫** — LC 226
- **攤平成串列**：把樹轉成鏈結串列結構
  - LC 114: Flatten Binary Tree, LC 430: Flatten Multilevel List
- **鏡像**：把樹翻面或映射
  - LC 226: Invert Binary Tree, LC 951: Flip Equivalent Trees
- **剪枝**：依條件移除子樹
  - LC 814: Binary Tree Pruning, LC 1325: Delete Leaves with Given Value

⸻

<!-- 8c2740279c33 -->
## 🔹 4. 圖

<!-- 33571d30de11 -->
### **圖的走訪** — LC 200 ⭐⭐⭐⭐⭐
- **BFS（廣度優先搜尋）**：一層一層往外探
  - LC 200: Number of Islands, LC 994: Rotting Oranges
  - LC 127: Word Ladder, LC 815: Bus Routes
  - **最短路徑**（無權重）：LC 1091: Shortest Path in Binary Matrix
- **DFS（深度優先搜尋）**：先鑽到底再回溯
  - LC 695: Max Area of Island, LC 130: Surrounded Regions
  - LC 417: Pacific Atlantic Water Flow, LC 79: Word Search
  - **連通分量**：LC 323: Number of Connected Components

<!-- a8b73fd2d0b8 -->
### **併查集（Disjoint Set Union）** — LC 547
- **基本併查集**：動態地把分量接起來
  - LC 200: Number of Islands, LC 547: Number of Provinces
  - LC 684: Redundant Connection, LC 685: Redundant Connection II
- **按秩合併 + 路徑壓縮**：最佳化過的版本
  - LC 721: Accounts Merge, LC 990: Satisfiability of Equality Equations
- **應用**：最小生成樹、環偵測
  - LC 1135: Connecting Cities, LC 1584: Min Cost to Connect Points

<!-- 4edfb696bebf -->
### **拓撲排序** — LC 207 ⭐⭐⭐⭐
- **Kahn 演算法（BFS）**：以入度為基礎
  - LC 207: Course Schedule, LC 210: Course Schedule II
  - LC 269: Alien Dictionary, LC 1136: Parallel Courses
- **DFS 版**：偵測環並產生拓撲順序
  - LC 802: Find Eventual Safe States
- **應用**：任務排程、相依性解析

<!-- 1e18ec409965 -->
### **最短路徑演算法** — LC 743
- **Dijkstra（戴克斯特拉）演算法**：單源最短路徑（權重非負）
  - LC 743: Network Delay Time, LC 787: Cheapest Flights Within K Stops
  - LC 1631: Path With Minimum Effort, LC 1514: Path with Maximum Probability
- **Bellman-Ford**：可以處理負權邊
  - 偵測負環、帶限制的最短路徑
- **Floyd-Warshall**：全點對最短路徑
  - LC 1334: Find City With Smallest Number of Neighbors

<!-- 48eff38bc3ae -->
### **進階圖論模式** — LC 785
- **二分圖**：用 BFS/DFS 做二著色
  - LC 785: Is Graph Bipartite, LC 886: Possible Bipartition
- **最小生成樹**：Kruskal 與 Prim 演算法
  - LC 1135: Connecting Cities, LC 1584: Min Cost to Connect All Points
- **強連通分量**：Tarjan 與 Kosaraju 演算法
  - 找橋與關節點
- **最大流**：Ford-Fulkerson、Edmonds-Karp
  - 網路流問題、二分圖匹配

<!-- ea0afeda2201 -->
### **格子上的圖問題** — LC 200
- **島嶼問題**：二維格子上的連通分量
  - LC 200: Number of Islands, LC 695: Max Area of Island
  - LC 305: Number of Islands II（併查集）
- **格子上的尋路**：用 BFS 找最短路徑
  - LC 1091: Shortest Path in Binary Matrix, LC 542: 01 Matrix
  - LC 934: Shortest Bridge, LC 1293: Shortest Path in Grid with Obstacles

<!-- af97ddd40624 -->
### **特殊圖型** — LC 310
- **樹狀圖**：沒有環，n 個節點恰好 n-1 條邊
  - LC 310: Minimum Height Trees, LC 1245: Tree Diameter
- **DAG（有向無環圖）**：可以做拓撲排序
  - LC 329: Longest Increasing Path in Matrix
- **完全圖**：任兩個頂點之間都有邊
  - 旅行推銷員的各種變形、Hamiltonian 路徑

⸻

<!-- 256c4b9c8a23 -->
## 🔹 5. 動態規劃（DP）

<!-- 8314d9d48635 -->
### **一維線性 DP** — LC 70 ⭐⭐⭐⭐⭐
- **費氏數列變形**：經典的遞推序列題
  - LC 70: Climbing Stairs, LC 198: House Robber, LC 213: House Robber II
  - LC 91: Decode Ways, LC 264: Ugly Number II
- **決策型 DP**：當前元素選或不選
  - LC 198: House Robber, LC 152: Maximum Product Subarray
  - LC 53: Maximum Subarray（Kadane 演算法）
- **計數型 DP**：數有幾種方式達成目標
  - LC 62: Unique Paths, LC 70: Climbing Stairs, LC 96: Unique BSTs

<!-- 379581e24e96 -->
### **二維格子 DP** — LC 62 ⭐⭐⭐⭐
- **路徑計數**：數矩陣上有幾條路徑
  - LC 62: Unique Paths, LC 63: Unique Paths II
  - LC 64: Minimum Path Sum, LC 120: Triangle
- **矩陣最佳化**：找最佳的子矩陣
  - LC 221: Maximal Square, LC 85: Maximal Rectangle
  - LC 1277: Count Square Submatrices with All Ones
- **二維決策**：狀態空間是二維的 DP
  - LC 174: Dungeon Game, LC 741: Cherry Pickup

<!-- 3bc1f0db6d80 -->
### **背包問題** — LC 416 ⭐⭐⭐⭐
- **0/1 背包**：每個物品只能用一次
  - LC 416: Partition Equal Subset Sum, LC 494: Target Sum
  - LC 1049: Last Stone Weight II
- **完全背包**：物品可以重複使用
  - LC 322: Coin Change, LC 518: Coin Change II
  - LC 279: Perfect Squares, LC 377: Combination Sum IV
- **多維背包**：同時有多個限制
  - LC 474: Ones and Zeroes, LC 879: Profitable Schemes

<!-- bacafc627dfe -->
### **字串 DP** — LC 72
- **編輯距離**：字串之間的轉換
  - LC 72: Edit Distance, LC 583: Delete Operation for Two Strings
  - LC 712: Minimum ASCII Delete Sum, LC 97: Interleaving String
- **最長共同子序列（LCS）**：
  - LC 1143: Longest Common Subsequence, LC 1035: Uncrossed Lines
  - LC 300: Longest Increasing Subsequence
- **回文**：回文子字串與回文子序列
  - LC 5: Longest Palindromic Substring, LC 516: Longest Palindromic Subsequence
  - LC 131: Palindrome Partitioning, LC 132: Palindrome Partitioning II
- **斷詞**：把字串切開
  - LC 139: Word Break, LC 140: Word Break II

<!-- 52c0fb5464f4 -->
### **區間 DP** — LC 312
- **矩陣連乘**：最佳的加括號方式
  - LC 312: Burst Balloons, LC 1000: Minimum Cost to Merge Stones
- **區間查詢**：在區間上做最佳化
  - LC 877: Stone Game, LC 1039: Minimum Score Triangulation
- **區間回文**：檢查某個區間是不是回文
  - LC 1312: Minimum Insertion Steps to Make String Palindrome

<!-- d961d3c145cf -->
### **狀態壓縮 DP（位元遮罩）** — LC 847
- **旅行推銷員**：要走過所有狀態
  - LC 943: Find Shortest Superstring, LC 980: Unique Paths III
- **子集合 DP**：追蹤哪些元素已被使用
  - LC 691: Stickers to Spell Word, LC 1125: Smallest Sufficient Team
- **圖的狀態**：追蹤拜訪過哪些節點
  - LC 847: Shortest Path Visiting All Nodes

<!-- 6c69dbe717b2 -->
### **進階 DP 模式** — LC 337
- **樹形 DP**：在樹結構上做 DP
  - LC 337: House Robber III, LC 968: Binary Tree Cameras
  - LC 124: Binary Tree Maximum Path Sum
- **數位 DP**：對數字的每一位做 DP
  - 數出符合特定性質的數字有幾個
- **機率 DP**：算期望值
  - LC 808: Soup Servings, LC 837: New 21 Game
- **賽局 DP**：雙方都下最佳手的 minimax
  - LC 292: Nim Game, LC 464: Can I Win, LC 486: Predict the Winner

<!-- c43a5e7b5c48 -->
### **DP 最佳化技巧**
- **空間最佳化**：從二維降成一維
  - 滾動陣列，只留下真正需要的狀態
- **單調佇列／單調堆疊**：加速區間查詢
  - LC 239: Sliding Window Maximum 的 DP 版
- **矩陣快速冪**：快速求遞推式
  - 適用於超大的費氏數列類序列

⸻

<!-- ab14c0d42b26 -->
## 🔹 6. 回溯

<!-- b21c3066ed24 -->
### **子集合與組合** — LC 78 ⭐⭐⭐⭐
- **子集合**：列出所有可能的子集合
  - LC 78: Subsets, LC 90: Subsets II（有重複元素）
  - LC 320: Generalized Abbreviation
- **組合**：從 n 個裡挑 k 個
  - LC 77: Combinations, LC 39: Combination Sum
  - LC 40: Combination Sum II, LC 216: Combination Sum III
- **模板**：用 start index 避免重複

<!-- 193b83f3a9fe -->
### **排列** — LC 46
- **基本排列**：所有的排列方式
  - LC 46: Permutations, LC 47: Permutations II（有重複元素）
  - LC 31: Next Permutation, LC 60: Permutation Sequence
- **帶條件的排列**：有額外限制
  - LC 996: Number of Squareful Arrays
- **不需要 start index** — 改用 visited 陣列

<!-- cd83801885cf -->
### **格子與棋盤問題** — LC 51
- **N 皇后**：擺皇后且互不攻擊
  - LC 51: N-Queens, LC 52: N-Queens II
- **單字搜尋**：在格子裡找單字
  - LC 79: Word Search, LC 212: Word Search II（Trie）
- **尋路**：把所有路徑都走一遍
  - LC 980: Unique Paths III, LC 1219: Path with Maximum Gold

<!-- 3b3b5b995cd4 -->
### **字串切分** — LC 131
- **回文切分**：切成一段段回文
  - LC 131: Palindrome Partitioning, LC 132: Palindrome Partitioning II
- **IP 位址**：組出所有合法的 IP
  - LC 93: Restore IP Addresses
- **斷詞**：用字典把字串切開
  - LC 140: Word Break II

<!-- 7d2394c3d6e7 -->
### **限制滿足問題** — LC 37
- **數獨**：在限制下把格子填滿
  - LC 37: Sudoku Solver
- **運算式**：組出等於目標值的運算式
  - LC 282: Expression Add Operators, LC 241: Different Ways to Add Parentheses
- **括號**：產生所有合法組合
  - LC 22: Generate Parentheses

<!-- 6bf173ba76a5 -->
### **進階回溯** — LC 698
- **賽局**：最佳策略
  - LC 464: Can I Win, LC 294: Flip Game II
- **排程**：把任務做最佳指派
  - LC 698: Partition to K Equal Sum Subsets
- **圖著色**：相鄰節點不同色
  - N 皇后概念的延伸

<!-- 96a43e7329f5 -->
### **剪枝技巧**
- **限制剪枝**：狀態一旦不合法就提早結束
  - 遞迴呼叫前先檢查邊界
- **上下界剪枝**：善用上界／下界
  - LC 39: Combination Sum（先排序，總和超標就 break）
- **對稱性剪枝**：跳過等價的狀態
  - LC 40: Combination Sum II（同一層跳過重複值）
- **記憶化**：把重複狀態的結果快取起來
  - 等於把回溯轉成動態規劃

<!-- cce56b1e7b38 -->
### **回溯模板**
- **以選擇為主**：做選擇 → 遞迴 → 撤銷
- **以索引為主**：逐個位置依序處理
- **以狀態為主**：追蹤當前狀態並修改它

⸻

<!-- c4f6eb30bf9b -->
## 🔹 7. 堆積與優先佇列

<!-- b13aece43b93 -->
### **Top-K 問題** — LC 215 ⭐⭐⭐⭐
- **第 k 大／第 k 小**：用堆積維持 k 個元素
  - LC 215: Kth Largest Element, LC 703: Kth Largest in Stream
  - LC 973: K Closest Points to Origin, LC 692: Top K Frequent Words
- **以頻率為基礎**：計數再配上堆積
  - LC 347: Top K Frequent Elements, LC 451: Sort Characters by Frequency
- **求最大用最小堆，求最小用最大堆**

<!-- 536267bb77b3 -->
### **合併多個已排序結構** — LC 23
- **合併 k 條串列**：用堆積追蹤各串列當前的最小值
  - LC 23: Merge k Sorted Lists, LC 378: Kth Smallest in Sorted Matrix
- **合併區間**：排序 + 堆積處理重疊區間
  - LC 253: Meeting Rooms II, LC 1229: Meeting Scheduler
- **合併 k 個陣列**：合併 k 條串列的延伸
  - LC 632: Smallest Range Covering Elements

<!-- a63d7bdd70d4 -->
### **資料串流與線上演算法** — LC 295
- **維護中位數**：兩個堆積（最大堆 + 最小堆）
  - LC 295: Find Median from Data Stream
  - LC 480: Sliding Window Median
- **即時統計量**：資料一邊進來一邊維護統計值
  - 即時第 k 大、即時平均
- **串流處理**：處理無窮無盡的資料流

<!-- d66b22a01c9a -->
### **排程與區間** — LC 621
- **任務排程**：把任務執行順序最佳化
  - LC 621: Task Scheduler, LC 358: Rearrange String k Distance Apart
- **會議室**：最佳地安排會議
  - LC 252: Meeting Rooms, LC 253: Meeting Rooms II
- **CPU 排程**：行程排程演算法
  - LC 1834: Single-Threaded CPU

<!-- e944443a24de -->
### **用到堆積的圖論演算法** — LC 743
- **Dijkstra 演算法**：用最小堆求最短路徑
  - LC 743: Network Delay Time, LC 787: Cheapest Flights
- **A* 搜尋**：帶啟發式函數的優先佇列搜尋
- **MST（Prim 演算法）**：最小生成樹
  - LC 1584: Min Cost to Connect All Points

<!-- 7536d01017f2 -->
### **進階堆積技巧**
- **延遲刪除**：把元素標記為已刪除，而不是真的移除
- **多層堆積**：堆積裡放堆積，處理更複雜的結構
- **自訂比較器**：定義自己的排序規則
  - lambda 函式、自訂物件
- **堆積 + 雜湊表**：組合起來達成 O(log n) 的更新
  - LC 355: Design Twitter, LC 146: 搭配堆積的 LRU Cache

⸻

<!-- d567d65fb1e4 -->
## 🔹 8. 雜湊與計數

<!-- 461f232e59ae -->
### **頻率表** — LC 242 ⭐⭐⭐⭐⭐
- **字元／元素計數**：數出現次數再做分析
  - LC 242: Valid Anagram, LC 383: Ransom Note, LC 387: First Unique Character
  - LC 169: Majority Element, LC 229: Majority Element II
- **依頻率分組**：照出現次數把元素分類
  - LC 49: Group Anagrams, LC 347: Top K Frequent Elements
  - LC 451: Sort Characters by Frequency
- **變位詞偵測**：用頻率表或排序後的字串
  - LC 438: Find All Anagrams in a String, LC 567: Permutation in String
- **模板**：`Counter()`，或自己用 dictionary 數

<!-- 2255e1ddeeb2 -->
### **前綴雜湊／滾動雜湊** — LC 28
- **字串模式比對**：Rabin-Karp 演算法
  - LC 28: Find the Index of First Occurrence, LC 459: Repeated Substring Pattern
- **子字串問題**：用雜湊做高效比較
  - LC 187: Repeated DNA Sequences, LC 1044: Longest Duplicate Substring
- **滾動視窗雜湊**：增量地更新雜湊值
  - LC 1316: Distinct Echo Substrings, LC 1554: Strings Differ by One Character
- **多項式雜湊**：以底數次方對字串做雜湊
  - 用質數當模數降低碰撞機率

<!-- 0e58b9f4c870 -->
### **用雜湊集合記錄看過的狀態** — LC 217
- **環偵測**：追蹤拜訪過的狀態
  - LC 202: Happy Number, LC 141: Linked List Cycle
  - LC 287: Find the Duplicate Number
- **重複偵測**：O(1) 的快速查找
  - LC 217: Contains Duplicate, LC 219: Contains Duplicate II
  - LC 128: Longest Consecutive Sequence
- **路徑追蹤**：記住走過的位置
  - LC 36: Valid Sudoku, LC 694: Number of Distinct Islands
- **狀態空間搜尋**：避免重複拜訪同一狀態
  - LC 127: Word Ladder, LC 752: Open the Lock

<!-- cf2ec9da7899 -->
### **以雜湊為基礎的資料結構** — LC 1
- **用 HashMap 記錄對應關係**：鍵值映射
  - LC 1: Two Sum, LC 454: 4Sum II, LC 525: Contiguous Array
  - LC 560: Subarray Sum Equals K, LC 523: Continuous Subarray Sum
- **雜湊 + 索引追蹤**：跟位置有關的問題
  - LC 409: Longest Palindrome, LC 290: Word Pattern
  - LC 205: Isomorphic Strings, LC 890: Find and Replace Pattern
- **前綴和 + 雜湊**：累積和搭配次數統計
  - LC 930: Binary Subarrays with Sum, LC 974: Subarray Sums Divisible by K

<!-- 81e24bb49541 -->
### **進階雜湊技巧**
- **多鍵雜湊**：把多個屬性組成一個 key
  - LC 356: Line Reflection, LC 447: Number of Boomerangs
- **自訂雜湊函式**：針對題目量身訂做
  - 二維問題的座標雜湊
  - 模式比對前先把字串正規化
- **處理雜湊碰撞**：管理雜湊衝突
  - 雙重雜湊、鏈結法的取捨

⸻

<!-- a260f2a8c52f -->
## 🔹 9. 進階模式

<!-- 54f4c88af14d -->
### **單調堆疊／單調佇列** — LC 496
- **下一個更大／更小的元素**：以堆疊為主的模式
  - LC 496: Next Greater Element I, LC 503: Next Greater Element II
  - LC 739: Daily Temperatures, LC 901: Online Stock Span
- **最大矩形**：用堆疊解直方圖問題
  - LC 84: Largest Rectangle in Histogram, LC 85: Maximal Rectangle
- **滑動視窗最大值**：以雙端佇列為主的模式
  - LC 239: Sliding Window Maximum, LC 1438: Longest Subarray with Difference ≤ K
- **單調性質**：維持遞增／遞減的順序
  - LC 402: Remove K Digits, LC 321: Create Maximum Number

<!-- e30b087fa246 -->
### **排序後的貪婪演算法** — LC 435
- **區間排程**：照結束時間排序，才挑得最好
  - LC 435: Non-overlapping Intervals, LC 452: Minimum Number of Arrows
  - LC 253: Meeting Rooms II, LC 1024: Video Stitching
- **分數背包**：照「價值／重量」比排序
  - LC 134: Gas Station, LC 135: Candy
- **活動選擇**：選出最多互不重疊的活動
  - LC 646: Maximum Length of Pair Chain
- **字典序**：組出字典序最佳的字串／陣列
  - LC 316: Remove Duplicate Letters, LC 1081: Smallest Subsequence

<!-- 72820bff6100 -->
### **樹狀陣列（BIT）／線段樹** — LC 307
- **區間和查詢**：高效的前綴和
  - LC 307: Range Sum Query - Mutable, LC 308: Range Sum Query 2D
- **逆序對計數**：數陣列裡的逆序對
  - LC 315: Count of Smaller Numbers After Self
  - LC 493: Reverse Pairs, LC 327: Count of Range Sum
- **座標壓縮**：處理超大的座標範圍
  - LC 218: The Skyline Problem（搭配 Fenwick tree）
- **區間更新**：用 lazy propagation 做更新
  - 支援區間加減的線段樹

<!-- de83a70299c6 -->
### **併查集（Disjoint Set Union）** — LC 200
- **連通分量**：把元素分進各個分量
  - LC 200: Number of Islands, LC 305: Number of Islands II
  - LC 547: Number of Provinces, LC 684: Redundant Connection
- **路徑壓縮**：最佳化 find 操作
  - LC 721: Accounts Merge, LC 737: Sentence Similarity II
- **按秩合併**：平衡樹高
  - LC 1101: The Earliest Moment When Everyone Become Friends
- **最小生成樹**：Kruskal 演算法
  - LC 1135: Connecting Cities, LC 1584: Min Cost to Connect All Points

<!-- 5b5d105c1485 -->
### **字典樹（Trie）** — LC 208
- **單字搜尋與儲存**：高效的前綴操作
  - LC 208: Implement Trie, LC 211: Design Add and Search Words
  - LC 212: Word Search II, LC 472: Concatenated Words
- **前綴比對**：找出有共同前綴的字
  - LC 14: Longest Common Prefix, LC 421: Maximum XOR
- **自動補完**：給出補完建議
  - LC 642: Design Search Autocomplete System
- **XOR Trie**：解 XOR 問題的二進位字典樹
  - LC 421: Maximum XOR of Two Numbers
  - LC 1707: Maximum XOR With an Element From Array

<!-- 94578d320ecf -->
### **進階圖論演算法** — LC 207
- **拓撲排序**：把 DAG 的節點排序
  - LC 207: Course Schedule, LC 210: Course Schedule II
  - LC 269: Alien Dictionary, LC 329: Longest Increasing Path
- **Tarjan 演算法**：找強連通分量
  - LC 1192: Critical Connections in a Network
- **二分圖匹配**：二分圖的最大匹配
  - 匈牙利演算法、König 定理的應用
- **網路流**：最大流最小割問題
  - Ford-Fulkerson、Edmonds-Karp 演算法

<!-- 432aaa0dc71a -->
### **字串演算法（進階）** — LC 28
- **KMP（Knuth-Morris-Pratt）**：用失敗函數做模式比對
  - LC 28: Find Index of First Occurrence
- **Manacher 演算法**：O(n) 找出所有回文
  - LC 5: Longest Palindromic Substring 的最佳化
- **Z-Algorithm**：字串比對與模式尋找
  - 線性時間的字串比對
- **後綴陣列**：進階字串處理
  - LC 1044: Longest Duplicate Substring（進階解法）

<!-- 297ad26f4d69 -->
### **數學與數論** — LC 204
- **快速冪**：高效計算次方
  - LC 50: Pow(x, n), LC 372: Super Pow
- **擴展歐幾里得演算法**：解丟番圖方程
  - LC 365: Water and Jug Problem
- **埃拉托斯特尼篩法**：產生質數
  - LC 204: Count Primes, LC 279: Perfect Squares
- **矩陣快速冪**：快速求遞推式
  - 費氏數列、線性遞迴關係

<!-- d5e5e33deec9 -->
### **賽局理論** — LC 292
- **Minimax 演算法**：零和賽局的最佳策略
  - LC 464: Can I Win, LC 486: Predict the Winner
- **Nim 遊戲**：以 XOR 為基礎的必勝策略
  - LC 292: Nim Game, LC 294: Flip Game II
- **賽局上的動態規劃**：分析賽局狀態
  - LC 877: Stone Game, LC 1140: Stone Game II

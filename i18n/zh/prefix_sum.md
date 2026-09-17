<!-- 43f173b6c2fc -->
# Prefix Sum (前綴和)

> **範圍** — 前綴和／累積和 — 子陣列和、二維前綴和、前綴和搭配雜湊表做計數。
> **另見**：[prefix_sum_advanced.md](./prefix_sum_advanced.md) — 模板 9–13，也就是那些要借用其他資料結構的；[prefix_sum_examples.md](./prefix_sum_examples.md) — 模板沒有直接解掉的實作題；[difference_array.md](./difference_array.md) — 區間*更新*而非區間查詢；[binary_indexed_tree.md](./binary_indexed_tree.md) — 陣列本身也會變動時；[kadane_algorithm.md](./kadane_algorithm.md) — 不靠前綴和求最大子陣列；[tree_backtrack.md](./tree_backtrack.md) — 模板 14 所一般化的那個 root→leaf 路徑 DFS。

<p align="center"><img src="../pic/prefix_sum.png"></p>

<!-- 1d1496862506 -->
## LeetCode 題目清單

- [Prefix Sum](https://leetcode.com/problem-list/prefix-sum/)

<!-- a664ac807e54 -->
## 總覽

**前綴和**是一種預處理技巧：先花 O(n) 前處理，之後任何子陣列的和都能在 O(1) 時間算出來。核心想法就是預先算好「從陣列開頭累加到每個位置」的和。

<!-- d1ed3170b36f -->
### 關鍵性質
- **時間複雜度**：
  - 前處理：O(n)
  - 查詢子陣列和：O(1)
  - 整體：O(n) 前處理 + 每次查詢 O(1)
- **空間複雜度**：O(n)，用來存前綴和
- **核心想法**：`prefixSum[i] = nums[0] + nums[1] + ... + nums[i-1]`
- **子陣列和**：`sum(i, j) = prefixSum[j+1] - prefixSum[i]`
- **什麼時候用**：
  - 有多次區間求和查詢
  - 帶條件的子陣列問題
  - 搭配 HashMap 把 O(n²) 降成 O(n)
  - 二維區間求和查詢

<!-- 6d6a68f28e71 -->
### 參考資料
- [Fucking Algorithm - Prefix Sum](https://labuladong.github.io/algo/2/19/22/)
- [LeetCode Prefix Sum Problems](https://leetcode.com/tag/prefix-sum/)
- [LeetCode Problem Set Discussion](https://leetcode.com/discuss/general-discussion/563022/prefix-sum-problems)
- [Hash Map Cheatsheet](https://github.com/yennanliu/CS_basics/blob/master/doc/cheatsheet/hash_map.md)

<!-- 5ab0cfa81ec9 -->
## 題型分類

<!-- d664a6e68bed -->
### **模式 1：基本區間求和** — LC 303
- **說明**：求任意區間 [i, j] 內元素的總和
- **例子**：LC 303 - Range Sum Query、LC 304 - Range Sum Query 2D
- **模式**：直接套前綴和公式
- **關鍵洞見**：`sum[i:j] = prefixSum[j+1] - prefixSum[i]`

<!-- b194f2a72c88 -->
### **模式 2：子陣列和等於目標值** — LC 560
- **說明**：找出／計數總和等於目標值的子陣列
- **例子**：LC 560 - Subarray Sum Equals K、LC 325 - Maximum Size Subarray Sum Equals k
- **模式**：用 HashMap 存前綴和，然後檢查 `(current_sum - target)` 在不在裡面
- **關鍵洞見**：若 `prefixSum[j] - prefixSum[i] = k`，則 `prefixSum[i] = prefixSum[j] - k`

<!-- 3163ceaf7228 -->
### **模式 3：帶整除／取餘的子陣列** — LC 523
- **說明**：牽涉到整除、餘數或取模運算的題目
- **例子**：LC 523 - Continuous Subarray Sum、LC 974 - Subarray Sums Divisible by K
- **模式**：HashMap 裡存的是餘數，不是實際的和
- **關鍵洞見**：若 `(prefixSum[j] - prefixSum[i]) % k = 0`，則 `prefixSum[j] % k = prefixSum[i] % k`

<!-- 7dae266908cf -->
### **模式 4：區間加值／差分陣列** — LC 370
- **說明**：高效率地對陣列套用區間更新
- **例子**：LC 370 - Range Addition、LC 1094 - Car Pooling
- **模式**：差分陣列技巧搭配前綴和
- **關鍵洞見**：在起點加、在 end+1 減，最後再算一次前綴和

<!-- ac3e7de94e92 -->
### **模式 5：二維前綴和** — LC 304
- **說明**：求二維矩陣中任意矩形區域的總和
- **例子**：LC 304 - Range Sum Query 2D、LC 1314 - Matrix Block Sum
- **模式**：建二維前綴和矩陣，用排容原理
- **關鍵洞見**：`sum = total - left - top + topleft`

<!-- 48899045ca83 -->
### **模式 6：先轉換再計數** — LC 1248
- **說明**：先把陣列元素轉換過，再用前綴和來計數
- **例子**：LC 1248 - Count Nice Subarrays、LC 926 - Flip String to Monotone
- **模式**：把元素轉成 0/1，再套帶條件的前綴和
- **關鍵洞見**：把題目轉化成更單純的前綴和問題

<!-- 4cfb62d640dd -->
### **模式 7：距離總和（左右拆分）** — LC 2615
- **說明**：高效率地算出索引之間絕對差值的總和
- **例子**：LC 2615 - Sum of Distances（LC 2121 - Intervals Between Identical Elements 是**同一題**）、LC 1685 - Sum of Absolute Differences、LC 2602 - Minimum Operations to Make All Array Elements Equal
- **模式**：先依值分組，再把每一組拆成左右兩半，套 `count * value - sum` 這條公式
- **關鍵洞見**：對索引 `i`，距離 = `(i * countLeft - sumLeft) + (sumRight - i * countRight)`，而它可以收成 `total - 2*prefixSum + i*(2*rank - groupSize)`

<!-- 807b3b1bf27d -->
### **模式 8：前綴最大值（貪婪分塊／分割）** — LC 769
- **說明**：一路追蹤陣列的累積最大值。當 `maxSoFar == i` 時，前綴 `[0..i]` 剛好裝著 `{0, 1, ..., i}` 這些元素，可以獨立成一個排序區塊。
- **例子**：LC 769 - Max Chunks To Make Sorted、LC 768 - Max Chunks To Make Sorted II、LC 2012 - Sum of Beauty in the Array
- **模式**：單趟掃描搭配一個 `maxSoFar` 變數；每當 `maxSoFar == currentIndex` 就把區塊數加一
- **關鍵洞見**：因為陣列是 `[0, n-1]` 的一個排列，所以只要目前看過的最大值等於當前索引，位置 `0..i` 需要的所有值就一定已經在 `arr[0..i]` 裡

<!-- cc3362daafe3 -->
### **模式 9：前綴 + 後綴拆分（在拆分點上做 minimax）** — LC 2017
- **說明**：題目給的整個選擇空間塌縮成**一個索引**；它左邊是前綴和、右邊是後綴和，所以每個候選解都能在同一趟掃描裡算完
- **例子**：LC 2017 - Grid Game、LC 724 - Find Pivot Index、LC 1422 - Maximum Score After Splitting a String、LC 2483 - Minimum Penalty for a Shop
- **模式**：讓拆分點從左掃到右，手上握著兩個累加值 —— 比較**之前**先縮後綴、比較**之後**才長前綴，這樣拆分點那一格就不屬於任何一邊
- **關鍵洞見**：一旦「一個選擇」被化簡成「一個索引」，對所有選擇取 `min`/`max` 就只是 O(n) 的掃描，不是搜尋 —— 不需要 DP，也不需要圖論演算法（見模板 15）

<!-- 442b0cdc3b57 -->
## 0) 概念

<!-- 55c2b60e2258 -->
### 前綴和陣列怎麼建（核心）

整套技巧都建立在**一行核心程式**上。把它背下來，其他都是推論：

<!--CODE-->

**一步一步來：**

<!--CODE-->

**追蹤（為什麼索引是 `i + 1` 而不是 `i`）：**

<!--CODE-->

> **重點：** `prefix` 比 `cnt` 多一個元素。`prefix[i+1]` 回答的是
> 「前 `i+1` 個元素的和」= `cnt[0] + ... + cnt[i]`。

**一行版寫法**（`itertools.accumulate` 前面補一個 0）：

<!--CODE-->

<!-- b294157c3ced -->
### 為什麼 `sum(l, r) = prefix[r+1] - prefix[l]`

<p align="center"><img src="../pic/prefix_sum_2.png"></p>

<!--CODE-->

**為什麼大小是 `n+1`？** 多出來的 `prefix[0] = 0` 專門處理 `l = 0` 這個邊界情況：
<!--CODE-->
沒有它的話，就得特別寫 `if (left == 0)` 的判斷（見 LC 303 的 V0）。

<!-- d6a154fab973 -->
### 具體範例 — LC 303

<!--CODE-->

<!-- b337d291532f -->
### 兩種寫法比較

| 寫法 | prefix 大小 | 建表 | 查詢 `sum(l, r)` | 邊界情況 |
|-------|-------------|-------|--------------------|-----------|
| **大小 `n+1`**（推薦） | `n + 1` | `prefix[i+1] = prefix[i] + nums[i]` | `prefix[r+1] - prefix[l]` | 不需要特例 |
| **大小 `n`** | `n` | `prefix[i] = prefix[i-1] + nums[i]` | `prefix[r] - (l > 0 ? prefix[l-1] : 0)` | 需要 `if (l == 0)` 判斷 |

<!-- ddc7f484e4af -->
## 模板與演算法

<!-- 0d0a747f161e -->
### 模板比較表

| 模板類型 | 適用情境 | 關鍵資料結構 | 什麼時候用 |
|---------------|----------|-------------------|-------------|
| **基本前綴和** | 區間求和查詢 | 陣列 | 需要多次算區間和 |
| **HashMap + 前綴和** | 找目標和的子陣列 | HashMap | 找出／計數特定和的子陣列 |
| **取模前綴和** | 整除類問題 | 存餘數的 HashMap | 子陣列和可被 k 整除 |
| **差分陣列** | 區間更新 | 標記起訖點的陣列 | 多次區間加值 |
| **二維前綴和** | 矩形求和查詢 | 二維矩陣 | 二維區間求和 |
| **距離總和** | 絕對差值的總和 | HashMap + 前綴和 | 相同元素之間 |i-j| 的總和 |
| **前綴 + 後綴拆分** | 最佳拆分點／minimax | 兩個累加值 | 整個答案由單一個索引決定 |

<!-- b04dcff0aa31 -->
### 通用模板

<!--CODE-->

<!-- 956686534feb -->
### 模板 1：基本前綴和（區間查詢） — LC 303

<!--CODE-->

<!--CODE-->

<!-- 513b7c737f88 -->
### 模板 2：HashMap + 前綴和（子陣列目標和） — LC 560

<!--CODE-->

<!--CODE-->

> **為什麼 map 存的是次數而不是索引** — 同一個前綴和可能在很多位置出現，
> 而每一個位置都能跟現在這裡構成一個合法的子陣列。存最新索引只會算到其中一個；
> 存「這個和出現過幾次」才會全部算到，所以更新寫成 `map[sum] += 1`，
> 而讀取寫成 `count += map[sum - k]`。

<!-- 635f9618f9d4 -->
### 模板 3：取模前綴和（整除類問題） — LC 974

**核心數學洞見：**
<!--CODE-->

<!--CODE-->

<!-- 090f87748f69 -->
### 模板 4：差分陣列（區間更新） — LC 370

<!--CODE-->

<!-- 0af742df2d1d -->
### 模板 5：二維前綴和 — LC 304

<!--CODE-->

<!-- e693d19b1885 -->
### 模板 6：先轉換再計數 — LC 1248

<!--CODE-->

> **你不一定要真的把轉換後的陣列建出來。** 上面的 `transformed` 是為了讓
> 「奇數 → 1、偶數 → 0」這一步看得見，但在時間壓力下該寫的形式，是在維護前綴和的
> 那個迴圈裡直接測 `x % 2` — 這個轉換是一個判斷式，不是一趟掃描。

<!-- 1448396664ab -->
### 模板 7：距離總和（左右拆分） — LC 2615

這個模式能高效率算出索引之間絕對差值 `|i - j|` 的總和。

<!-- 0e46c6a886f3 -->
#### 核心想法

兩個動作，順序如下：

1. **先把索引依值分組**（`{value: [indices]}`），因為 `|i - j|` 只會發生在相同的值之間 ——
   每一組都是彼此獨立、自成一題的子問題。
2. **在一組之內，靠拆分點把絕對值拿掉。**拆分點左邊全是 `pivot - other`，右邊全是
   `other - pivot`；絕對值符號一個都不剩，而每一半都是 `count * pivot ∓ sum` —— 差一個前綴和而已。

> **每一組的索引是免費附贈排序好的。**你是從左到右掃描時把 `i` 一個個 append 進去的，所以
> 每個清單本來就遞增。這正是整個做法能是 `O(n)` 的原因 —— 去排序那些組要花 `O(n log n)`，
> 而且什麼也換不到。這裡千萬不要排序；一排序就露出了「沒搞懂分組這一步」。

<!-- 358490d01731 -->
#### 推導

**先講記號 —— 本節後面都照這套用**

<!--CODE-->

> **這裡有兩個不同的索引在跑，把它們搞混是最經典的失誤。**`k` 是*組內*的序號 —— 它索引的是
> `indices` 與 `prefix`。`idx = indices[k]` 才是*在 `nums` 裡*的位置 —— 它是被加總的那個值，
> 也是答案要寫回去的地方（`res[idx]`，絕對不是 `res[k]`）。

**怎麼讀一個前綴索引 —— 整段推導就靠這一條規則**

<!--CODE-->

**具體例子** —— 組 `[2, 5, 8, 12]`，`m = 4`，所以 `prefix = [0, 2, 7, 15, 27]`：

<!--CODE-->


**1) 左邊的距離** —— 左邊每個索引都比較小，所以 `|idx - i| = idx - i`，絕對值符號就掉了：

<!--CODE-->

**2) 右邊的距離** —— 右邊每個索引都比較大，所以 `|idx - i| = i - idx`：

<!--CODE-->

> **為什麼右半減的是 `prefix[k + 1]` 而不是 `prefix[k]`。**`prefix[k]` 停在 `idx` *之前*，
> 所以 `prefix[m] - prefix[k]` 裡面還含著 `idx`；把它配上 `right_cnt = m - 1 - k`，
> 答案就剛好多算了一個 `idx`。總和與個數必須對「拆分點算不算在右半」這件事有一致的看法；
> `prefix[k + 1]` 搭 `m - 1 - k` 說的就是「不算」。

**3) 總距離**

<!--CODE-->

在對每一組做完 `O(m)` 的前綴掃描之後，每個元素只要 `O(1)`，所以整體是 `O(n)` ——
相對於逐對比較的 `O(n^2)`。

<!-- f8ab1437d7b3 -->
#### 圖解

<!--CODE-->

**用 LC 2615 的例子從頭走到尾** —— `nums = [1,3,1,1,2]`，預期答案 `[5,0,3,4,0]`：

<!--CODE-->

<!-- f998ad44be41 -->
#### Python 模板
<!--CODE-->

<!-- 683c4398ea2a -->
#### Java 模板
<!--CODE-->

<!-- fb2da565f479 -->
#### 替代作法：邊走邊累加（不建前綴陣列）
<!--CODE-->

<!-- 82f4f9b5c1cf -->
#### 同一件事寫成一行（值得背下來的恆等式）

左右拆分是你在白板上*推導*答案的方式，但這兩半在代數上可以收成單一個運算式 ——
沒有分支，也不必維護 `countLeft` / `countRight`：

<!--CODE-->

<!--CODE-->

> **`m == 1` 的特判就不需要了。**對只出現一次的組，這條公式自己就會算出
> `idx - 0 + idx*(0 - 1) = 0`，正好是題目要的答案。拆分寫法之所以需要
> `if m == 1: continue`，只是因為它被寫成了兩塊。

<!-- efc03b22e2d6 -->
#### 公式整理

在大小為 `m` 的組裡、序號 `k` 的位置上，令 `idx = indices[k]`、`prefix` 是長度 `m+1` 的前綴和：

| 部分 | 公式 | 意義 |
|-----------|---------|---------|
| **左側個數／總和** | `left_cnt = k`、`sum_left = prefix[k]` | `idx` 之前那 `k` 個相同的值 |
| **右側個數／總和** | `right_cnt = m - 1 - k`、`sum_right = prefix[m] - prefix[k + 1]` | `idx` 之後那些相同的值，不含拆分點本身 |
| **左側距離** | `idx * left_cnt - prefix[k]` | `(idx - smaller_idx)` 的總和 |
| **右側距離** | `(prefix[m] - prefix[k + 1]) - idx * right_cnt` | `(larger_idx - idx)` 的總和 |
| **總距離** | `left + right` → `res[idx]` | 所有 `\|idx - other_idx\|` 的總和 |

<!-- 93166010af66 -->
#### 類似題 —— `count * value − sum` 家族

每一題都是同一條恆等式；變的只是*那個排序清單裝什麼*，以及*它從哪來*。

| 題目 | 排序清單裝的是… | 和 LC 2615 的差別 |
|---|---|---|
| **LC 2121** Intervals Between Identical Elements | 依值分組後的索引 | **完全同一題。**LC 2615 的題敘自己就這麼說 —— 同樣的輸入、同樣的輸出，只是換了標題 |
| **LC 1685** Sum of Absolute Differences in a Sorted Array | 那些*值*，而且本來就排好了 | 不用分組也不用雜湊表 —— 陣列本身就是那一組，所以它是「只有一組」的情況 |
| **LC 2602** Minimum Operations to Make All Array Elements Equal | 排序後的值，外加一個前綴陣列 | 拆分點是**查詢值**而不是某個元素，所以先二分搜出它的插入位置，然後套同樣的兩半 |
| **LC 2448** Minimum Cost to Make Array Equal | 排序後的值，各自帶權重 | 每個元素算 `w` 次：個數變成權重和，所以要對 `w` 與 `w*v` 各做一份前綴和 |
| **LC 462** Minimum Moves to Equal Array Elements II | 排序後的值 | 只問所有拆分點裡的*最小值*，而那就是中位數 —— 連逐元素掃描都不用 |
| **LC 834** Sum of Distances in Tree | — | 「那條線」變成一棵樹，所以左右拆分變成「子樹／其餘部分」，靠換根去算 |

> **在面試現場怎麼認出它。**觸發條件是「在一個你可以排序的集合上求 `|x − y|` 的總和」。
> 排序把絕對值拿掉 —— 拆分點之前的都是減、之後的都是加 —— 而絕對值符號一消失，
> 每一半就是 `count × pivot ∓ sum`，前綴和 `O(1)` 就回答得出來。
> 把這句話講出來，`O(n^2)` 的暴力解就已經被你甩在後面了。

<!-- 79bd2f675079 -->
### 模板 8：前綴最大值（貪婪分塊／分割） — LC 769

**核心想法：** 對於 `[0, n-1]` 的一個排列，前綴 `arr[0..i]` 能獨立成一個排序區塊，當且僅當 `max(arr[0..i]) == i`。用一個 `maxSoFar` 變數就能追蹤這件事。

<!--CODE-->

<!--CODE-->

**等價的前綴和寫法**（同樣是 O(n)/O(1)）：
<!--CODE-->

> **為什麼 LC 769 光比總和就夠。**因為值是 `0..n-1` 的一個排列，所以 `arr` 的某段前綴
> 只有在裝著跟排序後陣列同長度前綴「同一組值」（順序可以不同）時，兩者的和才會相等。
> 而那正是「這段前綴自成一個區塊」的條件 —— 所以這個和的檢查根本不需要排序。

**什麼時候該升級成 PrefixMax + SuffixMin（LC 768，一般陣列）：**
<!--CODE-->

> **同一對陣列，問的卻是元素而不是切點。**LC 2012 一樣留著 `prefixMax` 與
> `suffixMin`，但它檢查的是 `prefixMax[i] < nums[i] < suffixMin[i]`，而且只有後綴那一側
> 需要真的開陣列 —— 完整走過一遍在
> [prefix_sum_examples.md § Prefix max / suffix min scans](./prefix_sum_examples.md#prefix-max--suffix-min-scans)。

<!-- d8cb34d38d01 -->
### 模板 15：前綴 + 後綴拆分 — 在拆分點上做 minimax ⭐⭐⭐⭐ — LC 2017

> 之所以編號 15，是因為 9–14 是下一節的進階組；這一個屬於核心模板。

<!-- a42b3eadaf63 -->
#### 核心想法

有些題目讀起來像是要在路徑或分割方式上做搜尋，但整個選擇空間其實塌縮成**一個索引**。
一旦如此，這個索引的兩側就是一個**前綴和**與一個**後綴和**，而最佳選擇只要一趟線性掃描 ——
不用 DP，也不用圖論搜尋。

LC 2017 是最乾淨的例子。格子是 `2 x n`，合法路徑是沿著 row 0 往右走、**恰好往下掉一次**、
再沿著 row 1 往右走 —— 所以**一條路徑就等於它往下轉彎的那一欄 `i`**。
路徑只有 `n` 條，不是指數多條。

<!--CODE-->

機器人 1 在 `i` 轉彎，會把 `row0[0..i]` 與 `row1[i..n-1]` 清成 0，於是留給機器人 2 的
剛好是兩塊沒被碰過的區域 —— 而它們落在 `i` 的**兩側**：

| 區塊 | 格子 | 是哪種和 |
|---|---|---|
| 上 | `row0[i+1 ... n-1]` | row 0 的**後綴** |
| 下 | `row1[0 ... i-1]` | row 1 的**前綴** |

機器人 2 同樣只掉一次，所以它**只能**拿到兩塊中的一塊；而因為每個值都 `>= 1`，
它會把那一塊整塊拿走（要上面那塊就在第 `n-1` 欄掉，要下面那塊就在第 `0` 欄掉）。
所以機器人 2 拿到 `max(top, bottom)`，而機器人 1 要挑讓它最小的那個 `i`：

<!--CODE-->

<!-- 269510172aee -->
#### 模式

兩個累加值往相反方向移動，而**迴圈裡那三行的順序就是整個模板**：

<!--CODE-->

把第 3 步放到第 2 步之前，第 `i` 欄就被重複算進下面那塊；把第 1 步放到第 2 步之後，
它就還留在上面那塊裡。兩種失誤都會去評分一條根本不存在的路徑。

<!--CODE-->

<!--CODE-->

<!-- e6efb550552e -->
#### 步驟追蹤 — `grid = [[2,5,4],[1,5,1]]`

<!--CODE-->

<!-- 504f69f6b7a9 -->
#### 陷阱

- **這是 minimax，不是貪婪。**機器人 1 並*不是*在最大化自己拿到的分數。抓走最肥的那條路徑，
  可能反而留給機器人 2 更肥的殘局 —— 目標函數是 `min(max(...))`，而只有掃過全部 `n` 個
  拆分點才看得出這件事。
- **不要去搬圖論演算法。**Dijkstra／最長路徑回答的是「對我最好的路線」，那是錯的目標函數；
  而且 `2 x n` 這個形狀本來就沒什麼可搜的：整個決策就是一個欄位索引。
- **拆分點那一格不屬於任何一邊。**見上面關於順序的說明 —— 那是這個模板唯一真正的 bug 來源。
- **Java 會溢位。**`n <= 5 * 10^4`、值 `<= 10^5`，一整列的總和就到 `5 * 10^9`；
  請用 `long` 累加。

<!-- ee30647c070a -->
#### 類似題 —— 拆分點家族

每次都是同一個形狀：一個索引決定答案，它左邊是前綴、右邊是後綴。

| 題目 | LC # | 拆分索引是什麼 | 和 LC 2017 的差別 |
|---|---|---|---|
| **Find Pivot Index** | 724 | 那個 pivot | 最素的情況：找出前綴 `==` 後綴的切點，沒有 min/max |
| **Product of Array Except Self** | 238 | 每一個索引輪流當 | 前綴**乘積** × 後綴乘積，而不是和 |
| **Maximum Score After Splitting a String** | 1422 | 那一刀 | **最大化**「左邊的 0」+「右邊的 1」—— 一趟掃描，同樣兩個計數器 |
| **Minimum Penalty for a Shop** | 2483 | 打烊的那個小時 | **最小化**「之前流失的客人」+「之後沒客人的時數」 |
| **Flip String to Monotone Increasing** | 926 | `0 → 1` 的分界 | 最小化「左邊的 1」+「右邊的 0」；本來就是模板 6 的轉換 |
| **Partition Array Into Three Parts With Equal Sum** | 1013 | 兩刀 | 前綴必須兩次命中 `total/3` —— 一趟掃描帶兩個檢查點 |
| **Ways to Split Array Into Three Subarrays** | 1712 | 兩刀 | 第二刀相對第一刀是單調的，所以拿二分搜去找它 |
| **Trapping Rain Water** | 42 | 每一個索引 | 用前綴**最大值**／後綴**最大值**而不是和（對比模板 8） |
| **Maximum Trailing Zeros in a Cornfield Path** | 2245 | 格子裡轉彎的那一格 | LC 2017 的「轉一次」搬到完整格子上：四個方向，對因數 2 與 5 各做前綴計數 |

> **在面試現場怎麼認出它。**問自己：*真正不同的選擇到底有幾種？*如果答案是「每個索引一種」——
> 一個轉彎欄、一刀、一個 pivot —— 就別再找 DP 了，直接寫那兩個累加值的掃描。
> LC 2017 的破綻是 `grid.length == 2`：兩列加上只能掉一次，代表一條路徑再沒有別的自由度。

<!-- bd55c5e87bef -->
## 進階模板

模板 **9–14** 搬到 **[prefix_sum_advanced.md](./prefix_sum_advanced.md)** 了。它們是那些
已經不只是「建個陣列、相減兩項」，而是開始借用其他資料結構的模板：

| # | 模板 | 借來的想法 | LC |
|---|---|---|---|
| 9 | [補集技巧 — 總和 − 中間視窗](./prefix_sum_advanced.md#template-9-complement-trick--total--middle-window---lc-1423) ⭐⭐⭐⭐⭐ | 頭尾繞回來的選法，等於一段要*排除*的連續視窗 | 1423 |
| 10 | [前綴和 + 單調雙端佇列](./prefix_sum_advanced.md#template-10-prefix-sum--monotonic-deque-shortest-subarray-allows-negatives---lc-862) | 用雙端佇列，因為負數會讓雙指標視窗失效 | 862 |
| 11 | [列對壓縮](./prefix_sum_advanced.md#template-11-row-pair-compression--collapse-2d-into-1d-prefix-sum---lc-363) ⭐⭐⭐⭐ | 固定一對列，把二維壓成一維 | 363, 1074 |
| 12 | [前綴 XOR](./prefix_sum_advanced.md#template-12-prefix-xor---lc-1310) ⭐⭐⭐⭐ | XOR 的反運算是自己，所以同一條相減恆等式仍然成立 | 1310 |
| 13 | [用 HashMap 做稀疏差分陣列](./prefix_sum_advanced.md#template-13-sparse-difference-array-via-hashmap-line-sweep---lc-2021) ⭐⭐⭐⭐⭐ | 座標範圍太大時，用雜湊表取代陣列 | 2021 |
| 14 | [樹上的前綴和](./prefix_sum_advanced.md#template-14-prefix-sum-on-a-tree-dfs--hashmap--backtrack---lc-437) ⭐⭐⭐⭐⭐ | DFS 的堆疊*就是*那個陣列 —— 模板 2 再加上回溯時的復原 | 437 |

<!-- c8b75ab397b3 -->
## 依模式分類的題目

<!-- cae2934e6af7 -->
### 按模式分類的題目清單

<!-- 6af6a009a0d0 -->
#### **模式 1：基本區間求和**
| 題目 | LC # | 關鍵技巧 | 難度 | 模板 |
|---------|------|---------------|------------|----------|
| Range Sum Query - Immutable | 303 | 基本前綴和陣列 | Easy | 模板 1 |
| Range Sum Query 2D - Immutable | 304 | 二維前綴和 | Medium | 模板 5 |
| Product of Array Except Self | 238 | 左右前綴乘積 | Medium | 模板 1 改寫 |
| Running Sum of 1d Array | 1480 | 直接前綴和 | Easy | 模板 1 |
| Find Pivot Index | 724 | 左邊和 vs 右邊和 | Easy | 模板 1 |

<!-- 1feda95ca9d1 -->
#### **模式 2：子陣列和等於目標值**
| 題目 | LC # | 關鍵技巧 | 難度 | 模板 |
|---------|------|---------------|------------|----------|
| Subarray Sum Equals K | 560 | HashMap + 前綴和 | Medium | 模板 2 |
| Maximum Size Subarray Sum Equals k | 325 | HashMap 存索引 | Medium | 模板 2 |
| Subarray Sum Equals K II | 713 | 乘積版本 | Medium | 模板 2 改寫 |
| Binary Subarrays With Sum | 930 | 轉換成求和等於目標 | Medium | 模板 6 |
| Number of Subarrays with Bounded Maximum | 795 | 區間求和技巧 | Medium | 模板 2 |
| Longest Well-Performing Interval | 1124 | 首次出現 map + 分數 ±1 技巧 | Medium | 模板 2 變形 |

<!-- 3e09aff9357b -->
#### **模式 3：帶整除／取餘的子陣列**
| 題目 | LC # | 關鍵技巧 | 難度 | 模板 |
|---------|------|---------------|------------|----------|
| Subarray Sums Divisible by K | 974 | 取模前綴和 | Medium | 模板 3 |
| Continuous Subarray Sum | 523 | 取模再檢查長度 | Medium | 模板 3 |
| Make Sum Divisible by P | 1590 | 進階取模技巧 | Medium | 模板 3 |
| Check If Array Pairs Are Divisible by k | 1497 | 統計餘數頻率 | Medium | 模板 3 改寫 |

<!-- a180984e164d -->
#### **模式 4：區間加值／更新**
| 題目 | LC # | 關鍵技巧 | 難度 | 模板 |
|---------|------|---------------|------------|----------|
| Range Addition | 370 | 差分陣列 | Medium | 模板 4 |
| Car Pooling | 1094 | 時間軸模擬 | Medium | 模板 4 |
| Corporate Flight Bookings | 1109 | 區間更新 | Medium | 模板 4 |
| Maximum Population Year | 1854 | 事件處理 | Easy | 模板 4 |
| Meeting Rooms II | 253 | 重疊計數 | Medium | 模板 4 |
| Brightest Position on Street | 2021 | 稀疏差分陣列（HashMap） | Medium | 模板 13 |
| Describe the Painting | 1943 | 稀疏差分陣列（HashMap） | Medium | 模板 13 |

<!-- d14b785f2ce5 -->
#### **模式 5：二維矩陣**
| 題目 | LC # | 關鍵技巧 | 難度 | 模板 |
|---------|------|---------------|------------|----------|
| Range Sum Query 2D | 304 | 二維前綴和 | Medium | 模板 5 |
| Matrix Block Sum | 1314 | 二維區間查詢 | Medium | 模板 5 |
| Number of Submatrices That Sum to Target | 1074 | 二維 + HashMap | Hard | 模板 5 + 2 |
| Maximum Side Length Square | 1292 | 二分搜尋 + 二維前綴和 | Medium | 模板 5 |

<!-- 92de526bb04a -->
#### **模式 6：先轉換再計數**
| 題目 | LC # | 關鍵技巧 | 難度 | 模板 |
|---------|------|---------------|------------|----------|
| Count Number of Nice Subarrays | 1248 | 轉成奇偶 | Medium | 模板 6 |
| Flip String to Monotone Increasing | 926 | 轉成 0/1 再計數 | Medium | 模板 6 |
| Max Chunks To Make Sorted | 769 | 比較總和 | Medium | 模板 6 |
| Longest Arithmetic Subsequence | 1027 | 轉成差值 | Medium | 模板 6 |

<!-- 3268821dda10 -->
#### **模式 7：距離總和**
| 題目 | LC # | 關鍵技巧 | 難度 | 模板 |
|---------|------|---------------|------------|----------|
| Sum of Distances | 2615 | 分組 + 左右拆分 | Medium | 模板 7 |
| Intervals Between Identical Elements | 2121 | **和 2615 完全同一題**，只是換標題 | Medium | 模板 7 |
| Sum of Absolute Differences in a Sorted Array | 1685 | 只有一組 —— 陣列本來就排好，不用 map | Medium | 模板 7 |
| Minimum Operations to Make All Array Elements Equal | 2602 | 拆分點是**查詢值**：先二分搜出它的序號，再套同樣的兩半 | Hard | 模板 7 + 二分搜尋 |
| Minimum Cost to Make Array Equal | 2448 | 帶權重 —— 對 `w` 與 `w*v` 各做前綴和 | Hard | 模板 7 加權版 |
| Minimum Moves to Equal Array Elements II | 462 | 只要最好的那個拆分點，而那就是中位數 | Medium | 模板 7（中位數捷徑） |
| Sum of Distances in Tree | 834 | 樹上的版本（DFS + 換根） | Hard | 模板 7 + DFS |
| Minimum Total Distance Traveled | 2463 | DP + 距離計算 | Hard | 模板 7 + DP |

<!-- 9d9b8929ecb6 -->
#### **模式 8：前綴最大值**
| 題目 | LC # | 關鍵技巧 | 難度 | 模板 |
|---------|------|---------------|------------|----------|
| Max Chunks To Make Sorted | 769 | 前綴最大值 == 索引 | Medium | 模板 8 |
| Max Chunks To Make Sorted II | 768 | PrefixMax + SuffixMin 陣列 | Hard | 模板 8 |
| Find the Longest Turbulent Subarray | 978 | 邊走邊追蹤狀態 | Medium | 模板 8 改寫 |
| Sum of Beauty in the Array | 2012 | PrefixMax + SuffixMin，逐元素判斷 | Medium | 模板 8 變形 |

<!-- a57cc336d718 -->
#### **模式 9：前綴 + 後綴拆分**
| 題目 | LC # | 關鍵技巧 | 難度 | 模板 |
|---------|------|---------------|------------|----------|
| Grid Game | 2017 | 轉彎欄 + 後綴(row 0)／前綴(row 1)，minimax | Medium | 模板 15 |
| Find Pivot Index | 724 | 找前綴 == 後綴的切點 | Easy | 模板 15（最素的情況） |
| Product of Array Except Self | 238 | 前綴乘積 × 後綴乘積 | Medium | 模板 15（乘積版） |
| Maximum Score After Splitting a String | 1422 | 最大化「左邊的 0」+「右邊的 1」 | Easy | 模板 15 |
| Minimum Penalty for a Shop | 2483 | 最小化「之前流失」+「之後空轉」 | Medium | 模板 15 |
| Partition Array Into Three Parts With Equal Sum | 1013 | 在 `total/3` 處切兩刀 | Easy | 模板 15（兩刀） |
| Ways to Split Array Into Three Subarrays | 1712 | 兩刀，第二刀用二分搜 | Medium | 模板 15 + 二分搜尋 |
| Trapping Rain Water | 42 | 每個索引的前綴最大值／後綴最大值 | Hard | 模板 15（取 max 而非和） |
| Maximum Trailing Zeros in a Cornfield Path | 2245 | 在格子裡轉一次，對 2 與 5 做前綴計數 | Medium | 模板 15（格子版） |

<!-- e40dcddaf5fb -->
#### **進階／混合模式**
| 題目 | LC # | 關鍵技巧 | 難度 | 模板 |
|---------|------|---------------|------------|----------|
| Maximum Sum of Two Non-Overlapping Subarrays | 1031 | 多個前綴陣列 | Medium | 模板 1 + DP |
| Subarrays with K Different Integers | 992 | 「最多 K 個」技巧 | Hard | 模板 2 |
| Minimum Window Subsequence | 727 | 滑動視窗 + 前綴和 | Hard | 模板 2 + SW |
| Split Array With Same Average | 805 | 子集合和問題 | Hard | 模板 2 |
| Largest Rectangle in Histogram | 84 | 堆疊 + 前綴和 | Hard | 模板 1 + 堆疊 |

<!-- 22480b1f64d1 -->
### 補充練習題

<!-- f5e8a3271072 -->
#### **Easy（打底）**
| 題目 | LC # | 重點 | 模板 |
|---------|------|------------|----------|
| Two Sum | 1 | HashMap 基本功 | 模板 2 改寫 |
| Contains Duplicate II | 219 | 滑動視窗 + map | 模板 2 |
| Maximum Average Subarray I | 643 | 固定長度子陣列 | 模板 1 |
| Degree of an Array | 697 | 元素頻率 | 模板 2 |

<!-- 07a7dfba6fba -->
#### **Medium（核心模式）**
| 題目 | LC # | 重點 | 模板 |
|---------|------|------------|----------|
| Contiguous Array | 525 | 平衡 0 和 1 | 模板 6 |
| Shortest Unsorted Continuous Subarray | 581 | 陣列分析 | 模板 1 |
| Random Pick with Weight | 528 | 帶權重的隨機選取 | 模板 1 |
| Path Sum III | 437 | 樹 + 前綴和 | [模板 14](./prefix_sum_advanced.md#template-14-prefix-sum-on-a-tree-dfs--hashmap--backtrack---lc-437) |

<!-- cbfa7b4d9d5a -->
#### **Hard（進階技巧）**
| 題目 | LC # | 重點 | 模板 |
|---------|------|------------|----------|
| Count of Range Sum | 327 | 合併排序 + 前綴和 | 進階 |
| Reverse Pairs | 493 | 合併排序技巧 | 進階 |
| Create Maximum Number | 321 | 貪婪 + 前綴 | 進階 |
| Count Different Palindromic Subsequences | 730 | DP + 前綴 | 進階 |

<!-- 25bf1aebdd68 -->
## 模式選擇策略

<!-- 49ebef43be36 -->
### 前綴和題目的決策框架

<!--CODE-->

<!-- 0cb4ec2c47ac -->
### 模板選擇指南

| 題目關鍵字 | 建議模板 | 例題 |
|------------------|---------------------|------------------|
| 「range sum」、「query」 | 模板 1 | LC 303, 304 |
| 「subarray sum equals」、「count subarrays」 | 模板 2 | LC 560, 325 |
| 「divisible by」、「remainder」、「modulo」 | 模板 3 | LC 974, 523 |
| 「range addition」、「updates」、「intervals」 | 模板 4 | LC 370, 1094 |
| 「2D」、「matrix」、「rectangle」 | 模板 5 | LC 304, 1314 |
| 「odd numbers」、「binary」、「transform」 | 模板 6 | LC 1248, 926 |
| 「sum of distances」、「absolute differences」、「identical elements」，以及任何在可排序集合上求 `sum of \|x-y\|` | 模板 7 | LC 2615, 2121, 1685, 2602 |
| 「max chunks」、「partition to sort」、「split into sorted segments」 | 模板 8 | LC 769, 768 |
| 「take from both ends」、「remove from left or right」 | 模板 9 | LC 1423, 1658 |
| 「shortest subarray with sum ≥ K」**且允許負數** | 模板 10 | LC 862（對比 LC 209 的視窗解） |
| 「submatrix sum ≤ k」、「count submatrices」、「rectangle + condition」 | 模板 11 | LC 363, 1074 |
| 「XOR of subarray」、「even count of every letter」、「parity」 | 模板 12 | LC 1310, 1915, 1738 |
| 「2 x n grid」、「one turn」、「best split point」、「both play optimally」 | 模板 15 | LC 2017, 724, 1422, 2483 |

> 模板 **9–13** 的完整內容寫在 [prefix_sum_advanced.md](./prefix_sum_advanced.md)。

<!-- a80576cca94b -->
### 怎麼認出各個模板

<!-- f94aad5b4e93 -->
#### **認出該用模板 1：**
- 題目提到：「range sum query」、「immutable array」、「multiple queries」
- 輸入：陣列 + 多組 (left, right) 查詢
- 輸出：區間 [left, right] 內元素的總和

<!-- 7be5714160a1 -->
#### **認出該用模板 2：**
- 題目提到：「subarray sum equals K」、「count subarrays」、「target sum」
- 關鍵洞見：要找出滿足 `prefixSum[j] - prefixSum[i] = target` 的 (i, j) 配對
- HashMap 存的是：`{prefixSum: count}` 或 `{prefixSum: index}`

<!-- 913ba5bb1ebc -->
#### **認出該用模板 3：**
- 題目提到：「divisible by K」、「remainder」、「modulo」、「continuous sum」
- 關鍵洞見：`(prefixSum[j] - prefixSum[i]) % k = 0` 代表兩者餘數相同
- HashMap 存的是：`{remainder: count}` 或 `{remainder: index}`

<!-- 2fbff85dac8f -->
#### **認出該用模板 4：**
- 題目提到：「range updates」、「add value to range」、「difference array」
- 有多次這種操作：「對索引 [start, end] 加上 val」
- 關鍵洞見：先標記起訖點，最後再算前綴和
- **如果座標範圍很大或會是負的 → 改用模板 13（HashMap）**

<!-- 230519721a00 -->
#### **認出該用模板 5：**
- 題目提到：「2D matrix」、「rectangle sum」、「submatrix」
- 需要求 (r1,c1) 到 (r2,c2) 這塊矩形的總和
- 公式：`total - left - top + topleft`

<!-- 6eaaf1ebe7a6 -->
#### **認出該用模板 6：**
- 題目提到：「count odd/even」、「binary conditions」、「transform array」
- 先轉換陣列（例如奇數→1、偶數→0），再套前綴和
- 會化簡成更單純的前綴和問題

<!-- 668d2a52df95 -->
#### **認出該用模板 7：**
- 題目提到：「sum of distances」、「absolute differences」、「identical elements」
- 真正的觸發條件更廣：**在一個你可以排序的集合上求 `|x - y|` 的總和。**排序把絕對值拿掉，
  於是每一側都是 `count * pivot ∓ sum`
- 需要對相同值的元素算出 `sum of |i - j|`
- 關鍵洞見：拆成左右兩半，套 `count * value - sum` 公式
- HashMap 存的是：`{value: [索引清單]}` —— 而那些清單是免費附贈排序好的，所以**不要**再去排序
- 時間複雜度從 O(n²) 降到 O(n)
- 如果拆分點是*查詢值*而不是某個元素（LC 2602），先二分搜出它的序號 —— 那兩半的算法完全不變

<!-- 30e7f06faeb9 -->
#### **認出該用模板 8：**
- 題目提到：「max chunks」、「切成幾段讓每段能各自排序」、「split to sort」
- 輸入陣列是 `[0, n-1]` 的一個排列（或可以用前綴／後綴陣列推廣）
- 關鍵洞見：`maxSoFar == i` 代表前綴 `[0..i]` 已經是一組完整、自成一體、可以直接排序的集合
- 等價的檢查：`arr[0..i]` 的前綴和等於排序後陣列 `[0..i]` 的前綴和

<!-- b28178c30b78 -->
#### **認出該用模板 15：**
- 題目提到：「split the array」、「pivot」、「turning point」、「close the shop at hour i」、
  「both robots play optimally」
- 真正的觸發條件：**候選答案的數量是「每個索引一個」。**一個只能往下掉一次的 `2 x n` 格子、
  把字串切成兩段、分成左右兩部分
- 關鍵洞見：索引左邊是前綴和、右邊是後綴和 —— 掃過索引，兩側都能 O(1) 算出來
- 注意順序：比較*之前*先縮後綴、比較*之後*才長前綴，這樣拆分點那一格才會落在兩邊之外
- 如果目標函數是 `min(max(...))` 或 `max(min(...))`，那就是 minimax —— 貪婪地「拿對我最好的」
  是錯的目標函數，不只是弱一點而已

<!-- d5c1654db5b7 -->
## 實作範例

八題實作放在 **[prefix_sum_examples.md](./prefix_sum_examples.md)** — 都是上面的模板沒有從頭到尾解掉的：

| 分組 | 題目 |
|---|---|
| [用 HashMap 求子陣列和](./prefix_sum_examples.md#subarray-sums-with-a-hashmap) | LC 325, 523, 1124, 926 |
| [固定視窗與成對視窗](./prefix_sum_examples.md#fixed-and-paired-windows) | LC 1031 |
| [二維前綴和](./prefix_sum_examples.md#2d-prefix-sums) | LC 1292 |
| [區間更新](./prefix_sum_examples.md#range-updates) | LC 1094 |
| [前綴最大值／後綴最小值的掃描](./prefix_sum_examples.md#prefix-max--suffix-min-scans) | LC 2012 |

另外五題以前有自己的範例章節，現在沒有了：LC 370、560、769、1248 和 2615 都已經被
點名它們的那個模板解掉，多出來的第二份實作並沒有補上模板缺的東西。那些副本*真正*
有價值的部分 — 為什麼 map 存次數而不是索引、為什麼 LC 769 光比和就夠、以及為什麼
轉換不必真的建出陣列 — 已經以註解的形式併進模板裡。

<!-- ea4fa3974f42 -->
## 總結與速查

<!-- c71d0ab17c25 -->
### 複雜度速查

| 操作 | 時間 | 空間 | 備註 |
|-----------|------|-------|--------|
| 建前綴和陣列 | O(n) | O(n) | 一次性前處理 |
| 區間求和查詢 | O(1) | O(1) | 前處理完之後 |
| 用 HashMap 求子陣列和 | O(n) | O(n) | 平均情況，最壞 O(n²) |
| 建二維前綴和 | O(mn) | O(mn) | m×n 的矩陣 |
| 二維區間查詢 | O(1) | O(1) | 前處理完之後 |
| 差分陣列更新 | O(k) | O(n) | k 次更新，陣列大小 n |
| 前綴 + 後綴拆分的掃描 | O(n) | O(1) | 兩個累加值，不留陣列 |

<!-- dba405ad7383 -->
### 模板速查

| 模板 | 模式 | 關鍵程式片段 |
|----------|---------|------------------|
| **模板 1** | 基本區間求和 | `prefix[i+1] = prefix[i] + nums[i]` |
| **模板 2** | HashMap + 目標值 | `if prefix_sum - k in map: count += map[prefix_sum - k]` |
| **模板 3** | 取模／整除 | `remainder = prefix_sum % k; if remainder in map...` |
| **模板 4** | 區間更新 | `diff[start] += val; diff[end+1] -= val` |
| **模板 5** | 二維矩陣 | `prefix[i][j] = val + left + top - topleft` |
| **模板 6** | 轉換後計數 | `先轉換陣列，再套前綴和` |
| **模板 7** | 距離總和 | `left = idx*countLeft - sumLeft; right = sumRight - idx*countRight` —— 或寫成一行，`total - 2*prefixSum + idx*(2*i - m)` |
| **模板 8** | 前綴最大值 | `maxSoFar = max(maxSoFar, arr[i]); if (maxSoFar == i) chunks++` |
| **模板 9** | 補集（取兩端） | `ans = total - min(window of length n-k)` |
| **模板 10** | 單調雙端佇列（有負數） | `while p[i]-p[dq[0]]>=k: ans=min(ans,i-dq.popleft())` |
| **模板 11** | 列對壓縮 | `for top: for bot: colSum[c]+=mat[bot][c]` → 再用一維解法 |
| **模板 12** | 前綴 XOR | `p[i+1] = p[i] ^ a[i]; xor(l,r) = p[r+1] ^ p[l]` |
| **模板 13** | 稀疏差分（HashMap） | `d[start]+=v; d[end+1]-=v; for k in sorted(d): cur+=d[k]` |
| **模板 15** | 前綴 + 後綴拆分 | `top-=a[i]; res=min(res,max(top,bottom)); bottom+=b[i]` |

> 模板 **9–13** 的完整內容寫在 [prefix_sum_advanced.md](./prefix_sum_advanced.md)。

<!-- 2cf2928ac75c -->
### 核心數學洞見

<!-- b63df1959626 -->
#### **前綴和公式**
<!--CODE-->

<!-- 91789a95678c -->
#### **HashMap 的關鍵洞見**
<!--CODE-->

<!-- c3d0316b51ed -->
### 常見模式與技巧

<!-- bbd301354b6c -->
#### **模式 1：Two Sum 的延伸**
<!--CODE-->

<!-- b4bca7a11ed4 -->
#### **模式 2：差分陣列的魔法**
<!--CODE-->

<!-- 9ae7249eb1d7 -->
#### **模式 3：先轉換再求和**
<!--CODE-->

<!-- a3637b1ad2c9 -->
### 解題步驟

1. **先認出模式**
   - 仔細讀題，抓關鍵字（range、subarray、sum、count 等等）
   - 確認是多次查詢還是單趟掃描
   - 找找有沒有數學關係（整除、取模等等）

2. **挑對模板**
   - 用決策流程圖選出合適的模板
   - 考慮時間／空間複雜度的要求
   - 確認套前綴和之前需不需要先做轉換

3. **處理邊界情況**
   - 空陣列或只有一個元素
   - 負數（取模運算時尤其要小心）
   - 大數相加時的整數溢位
   - 值為 0 的元素對整除判斷的影響

4. **最佳化實作**
   - HashMap 要先放基底情況（通常是 `{0: 1}`）
   - 取模時處理負餘數
   - 能寫成單趟就寫成單趟
   - 只需要次數時，考慮省掉空間

<!-- 5c995747fbaa -->
### 常見錯誤與提示

**🚫 常見錯誤：**
- **忘了基底情況**：子陣列題沒有先把 HashMap 初始化成 `{0: 1}`
- **差一錯誤**：前綴和陣列的索引算錯
- **負餘數**：取模時沒處理 `remainder < 0`
- **HashMap 的時機**：先寫進 map 還是先檢查條件，順序搞反
- **二維索引**：二維前綴和裡列跟行搞混
- **區間更新**：差分陣列忘了在 `end+1` 減回去

**✅ 最佳實務：**
- 前綴和陣列**一律**開 `n+1` 大小，用 1-based 索引
- 子陣列題**一律**先在 HashMap 放 `{0: 1}`，處理掉邊界情況
- **再三確認**順序：先檢查條件，再更新 HashMap
- **處理負數**：取模用 `remainder = (remainder % k + k) % k`
- **檢查邊界**：用到 `end+1` 索引時記得檢查陣列範圍
- **測邊界情況**：空陣列、單一元素、全負數

<!-- 1104feefaf1a -->
### 面試提示

1. **模式辨識**
   - 看到「subarray sum equals K」→ 直接 HashMap + 前綴和
   - 看到「range queries」→ 基本前綴和陣列
   - 看到「divisible by K」→ HashMap 搭配取模技巧
   - 看到「多次區間更新」→ 差分陣列
   - 如果整個選擇就是一個索引（一刀、一個 pivot、一個轉彎欄）→ 前綴 + 後綴的掃描

2. **表達策略**
   - 把數學洞見講出來：「我們是在找兩個前綴和的配對」
   - 畫例子示範前綴和怎麼運作
   - 主動提複雜度的改善：「這把 O(n²) 降到 O(n)」
   - 討論時間與空間的取捨

3. **實作技巧**
   - 先寫暴力解確認自己理解對了
   - 再用合適的前綴和模板去最佳化
   - 解釋為什麼 HashMap 的初始化很重要
   - 拿一個小例子一步一步走過去

4. **常見追問**
   - 討論變形：「如果要的是最大長度而不是數量呢？」
   - 說明推廣到二維：「在矩陣上該怎麼做？」
   - 考慮限制條件：「如果數字非常大呢？」（溢位）

<!-- 7f1fdab70006 -->
### 相關主題

- **HashMap／雜湊表**：大多數進階前綴和題目的必需品
- **滑動視窗**：可以跟前綴和結合起來最佳化
- **Two Sum**：很多前綴和題目其實是 two sum 的延伸
- **動態規劃**：前綴和常拿來當 DP 的最佳化手段
- **二分搜尋**：可以跟前綴和結合做區間查詢
- **線段樹**：需要邊更新邊查詢區間和時的替代方案
- **單調堆疊**：有時會跟前綴和一起用來最佳化
- **樹的 DFS**：一條 root→node 的鏈就是一個陣列，所以模板 2 可以用來數往下的路徑 —— [模板 14](./prefix_sum_advanced.md#template-14-prefix-sum-on-a-tree-dfs--hashmap--backtrack---lc-437)，LC 437

<!-- 46c1b9564342 -->
### 進階延伸

- **稀疏陣列**：座標壓縮搭配前綴和
- **線上查詢**：需要更新 + 查詢時改用線段樹或樹狀陣列
- **二維區間更新**：二維差分陣列搭配二維前綴和
- **帶權前綴和**：處理各元素權重不同的情況
- **環狀陣列**：改寫模板來處理繞回頭的情況

這份 cheatsheet 涵蓋了所有主要的前綴和模式，並提供一套有系統的方法，讓你能高效率地解掉 40 多題 LeetCode。

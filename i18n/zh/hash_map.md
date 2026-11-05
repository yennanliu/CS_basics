<!-- ed69fd89b797 -->
# Hash Map Cheatsheet（雜湊表速查）

> **範圍** — 鍵→值的題型模式：查找、分組、索引表、前綴和表、重新映射。
> **另見** — *題目集*：[hash_map_examples.md](./hash_map_examples.md) — 經典題目寫成題目卡，各自歸在它所歸約到的下列模板底下。
> *鄰近的 cheatsheet*：[hashing.md](./hashing.md) — 雜湊本身怎麼運作，以及計數與 rolling hash 的慣用寫法；[set.md](./set.md) — 只管有沒有，不存值；[Collection.md](./Collection.md) — 一開始該選哪個容器。

<!-- d19c3a8130bb -->
## LeetCode 題目清單

- [Hash Table](https://leetcode.com/problem-list/hash-table/)

<!-- 257a4e61d9d1 -->
## 時間複雜度

| 資料結構 | 搜尋   | 插入   | 刪除   | 最小／最大  |
| -------------- | -------- | -------- | -------- | -------- |
| 雜湊表（平均） | O(1)     | O(1)     | O(1)     | O(n)     |

> 表中是平均情況。**最壞情況（所有鍵都碰撞）：O(n)。** 求最小／最大值必須全掃，因為雜湊不保證任何順序。

<!-- 52cd70ffcefe -->
## 總覽
雜湊表（Hash Table／Dictionary）是最基本的資料結構之一，提供高效率的鍵值儲存與取用。

<!-- 809dc69d1a04 -->
### 關鍵性質
- **複雜度**：見上面的[時間複雜度](#time-complexity)表；空間是 **O(n)**
- **底層實作**：陣列 + 鏈結串列／紅黑樹（Java HashMap）
- **雜湊碰撞**：用鏈接法（chaining）或開放定址法處理

<!-- 7939259f2c61 -->
### 什麼時候會發生雜湊碰撞
- **負載因子 > 0.75**：效能開始退化
- **雜湊函式不好**：很多鍵落到同一個 bucket
- **Java HashMap**：鏈結串列長度 > 8 時會轉成紅黑樹
- **解法**：鏈接法（每個 bucket 一條串列或一棵樹）或開放定址 —— 見 [hash_map_collision.md](https://github.com/yennanliu/CS_basics/blob/master/doc/hash_map_collision.md)

<p align="center"><img src="../pic/hash_op_101.png"></p>

- [NC - HashMap under the hood](https://www.linkedin.com/posts/neetcodeio_how-do-hashmaps-work-under-the-hood-activity-7298370869301526530-DsIi?utm_source=social_share_send&utm_medium=member_desktop_web&rcm=ACoAAA6fzw4BpOSBO1YeSrJwPZ-dNBhjC3jXTDE)

<!-- a0cffe7ae6d5 -->
### 為什麼查找是 O(1)

- FAQ
    - 為什麼 hashmap 的搜尋時間複雜度 ~= O(1)？怎麼解釋？
        - 一句話：O(1) 是平均與最好情況。最壞情況可能是 O(N)（雜湊碰撞）
        - 雜湊函式很關鍵 -> 決定資料怎麼存、以及碰撞會不會發生
        - 操作
            - 插入
                - 拿到 key，用雜湊函式算出 hash 值
                - 依 hash 值在記憶體中找到對應 bucket
                - 把 key 和 value 存進該 bucket
            - 查詢
                - 依 key 算出 index
                - 依 index 找到 bucket 位置
                    - 注意！！！這裡用位元運算（`int pos = (n - 1) & hash`），所以這步是 O(1)（直接算出 bucket 位址，不必掃過所有元素）
                - 走訪該 key 底下的所有元素（只有一個的話就一次）
                - 回傳 value
        <p align="center"><img src="../pic/hash_map1.png"></p>
        <p align="center"><img src="../pic/hash_map2.jpeg"></p>
        - [ref 1](https://blog.csdn.net/junqing_wu/article/details/104606619)
        - [ref 2](https://blog.csdn.net/john1337/article/details/104727895)

<!-- 816df24e5e9d -->
### 什麼時候該用／不該用

- 該用的時候 
	- 需要 ~ O(1) 讀寫的場景
    - 用快取換效能（空間換時間）
    - `sum, pair, continuous` 這類題目
    - 想避開雙層迴圈（O(N^2)）

- 不該用的時候
	- 資料是時間序列 
	- 資料需要保持順序 
	- https://www.reddit.com/r/learnprogramming/comments/29t4s4/when_is_it_bad_to_use_a_hash_table/

<!-- e2c577fb6057 -->
### 基本操作

- `get`：從 dict 取值，鍵不存在時給預設值
<!--CODE-->

- `setdefault()`
	- https://www.w3schools.com/python/ref_dictionary_setdefault.asp
<!--CODE-->

- 對 ***hashmap (dict)*** 做排序
<!--CODE-->

<!--CODE-->

<!-- 4f4b2a7caaa9 -->
### 參考資料

- [Java HashMap](https://bbs.huaweicloud.com/blogs/276884?utm_source=juejin&utm_medium=bbs-ex&utm_campaign=other&utm_content=content)
    - 底層：陣列 + 鏈結串列／紅黑樹
        - 鏈結串列長度 > 8 -> 把串列轉成紅黑樹
        - 鏈結串列長度 < 6 -> 把紅黑樹轉回串列
- N sum：
    - [n_sum.md](https://github.com/yennanliu/CS_basics/blob/master/doc/cheatsheet/n_sum.md)
- LC 參考
    - [prefix_sum.md](https://github.com/yennanliu/CS_basics/blob/master/doc/cheatsheet/prefix_sum.md)
- 其他 
	- https://blog.techbridge.cc/2017/01/21/simple-hash-table-intro/
	- https://www.freecodecamp.org/news/hash-tables/

<!-- 06b1bc95636e -->
## 辨識

| 題目敘述裡的訊號 | 限制條件線索 | 該用什麼 | 不是這個的情況 |
|---|---|---|---|
| 「回傳兩個加起來等於 `target` 的數的索引」 | 未排序，n ≤ 10⁴–10⁵ → O(n) | [模板 1](#template-1-complement-lookup--lc-1) 查補數 | 輸入已排序 → 雙指標，O(1) 空間（[2_pointers.md](./2_pointers.md)） |
| 「anagram」、「出現最多／最少次」、「第一個不重複的」、「`a` 能不能用 `b` 的字母拼出來」 | 只有 26 個小寫字母 → 一個 `int[26]` 就是 map | [模板 2](#template-2-frequency-count--lc-242) 頻率計數 | 只問存不存在、不管次數 → [set.md](./set.md) |
| 「把在 … 意義下相同的字串／點分成一組」 | n 個長度 k 的字 → O(n·k log k) | [模板 3](#template-3-grouping-by-a-computed-key--lc-49) 用算出來的鍵分組 | 關係不具遞移性（「只差一個字母」）→ 圖／BFS |
| 「和等於 k／可被 k 整除的子陣列有幾個」，值可能是負的 | n ≤ 2·10⁴–10⁵ → O(n)，不是枚舉所有配對 | [模板 4](#template-4-prefix-sum-count-map--lc-560-) 前綴和計數表 | 所有值都是正的，且問「最長／最短」→ [sliding_window.md](./sliding_window.md) |
| 「`p` 的所有 anagram 在 `s` 中的起點」、「`s2` 裡有沒有 `s1` 的排列」 | 視窗長度固定為 `len(p)` | [模板 5](#template-5-fixed-window-character-counts--lc-438) 固定視窗的字元計數 | 視窗長度會變（「最多 k 種字元的最長子字串」）→ [sliding_window.md](./sliding_window.md) |
| 「在這套外星語言中是否已排序」、「依照 … 給定的順序」 | 字母表 ≤ 26 → 每次查排名 O(1) | [模板 6](#template-6-rank-map--value-to-position--lc-953-) 排名表 | |
| 「isomorphic」、「符合同一個 pattern」、「不能有兩個字元對到同一個」 | 長度相同，O(n) | [模板 7](#template-7-bijection-with-two-maps--lc-205) 雙射 | |
| 「O(1) 的 `get` 和 `put`」、「淘汰最久沒用到的鍵」 | 最多 2·10⁵ 次呼叫 → 每個操作都要 O(1) | [linked_list § Template 7](./linked_list.md#template-7-hash-map--doubly-linked-list-lru--lc-146-) map + 雙向鏈結串列 | 淘汰的是*使用次數*最少的 → LC 460，每個頻率一條串列 |
| 「回傳深拷貝」、「clone 這張圖／這條串列」 | V, E ≤ 10⁴ → O(V + E) | [模板 8](#template-8-old-to-new-node-map--lc-133) 舊節點對新節點的表 | 只問連不連得到 → 一個 visited 集合（[graph.md](./graph.md)） |
| 「從 `[0, n)` 均勻隨機挑，但絕不能挑到黑名單裡的值」 | n ≤ 10⁹、黑名單 ≤ 10⁵ → 不可能把合法值列出來 | [模板 9](#template-9-virtual-map-remapping--lc-710) 虛擬重新映射 | |
| 「出現次數前 k 名，同次數依字母序」 | k ≪ n → 堆積勝過整個排序 | [模板 10](#template-10-map-plus-heap--lc-692) map 加堆積 | 沒有同分規則（LC 347）→ O(n) 的桶排序 |

> 限制條件線索依照 [complexity_cheatsheet.md](./complexity_cheatsheet.md) 裡的尺規。

<!-- ddc7f484e4af -->
## 模板與演算法

<!-- 31af9e0edb17 -->
### 模板對照表

| # | 模板 | map 的形狀 | 怎麼認出來 | 代表 LC |
|---|----------|-----------|-----------------|------------|
| 1 | 查補數 | `{value: index}` | 「找一組配對」、「湊出 target」、補數 | 1, 15, 532, 1010 |
| 2 | 頻率計數 | `{item: count}`，或 `{item: (first, last, count)}` | 「count」、「frequency」、「anagram」、「top-K」、「所有出現位置都在同一個連續區塊」 | 242, 49, 347, 451, 4038 |
| 3 | 用算出來的鍵分組 | `{canonical_key: [items]}`，或 `{(min, max): count}` | 「group」、「同一條線」、「同一個特徵」、「`(x, y)` 等同 `(y, x)`」 | 49, 149, 609, 987, 4066 |
| 4 | 前綴和計數表 | `{prefixSum: count}` / `{prefixSum: firstIndex}` / `{startValue: minPrefix}` | 「子陣列和等於 K／可被 K 整除」、「兩端滿足 ... 的最大和」 | 560, 974, 525, 325, 3026 |
| 5 | 固定視窗的字元計數 | `{char: count in window}` | 「`p` 在 `s` 裡的 anagram」、「permutation in string」 | 438, 567, 30 |
| 6 | 排名表 | `{value: rank}` | 「依照 ... 給定的順序」 | 953, 791, 105 |
| 7 | 雙射（兩個 map） | `{x: y}` **和** `{y: x}` | 「一對一」、「isomorphic」、「符合這個 pattern」 | 205, 290 |
| 8 | 舊節點對新節點的表 | `{original node: clone}`、`{child: parent}` | 「clone」、「深拷貝」、「父指標」 | 133, 138, 652 |
| 9 | 虛擬映射（重新映射） | `{badIndex: goodIndex}` | 「隨機挑，但要排除 ...」 | 710 |
| 10 | map 加堆積 | `{item: count}` + 依次數排序的堆積 | 「top-K」、「同分時依 ...」 | 692, 347, 451 |

> 每個模板的實戰題都在 [hash_map_examples.md](./hash_map_examples.md#index)，依模板分組。其他地方：有序 map（`TreeMap` / `SortedDict`）→ 下面的 [Language APIs](#language-apis--sorted-map-treemap--sorteddict)；O(n) top-K 的桶排序 → [heap § Template 2](./heap.md#template-2-count-then-a-size-k-heap-on-the-count--lc-347-)；rolling hash → [string_matching_kmp_rolling_hash.md](./string_matching_kmp_rolling_hash.md)；拆半探測的配對查找 → [palindrome § Template 9](./palindrome.md#template-9-palindrome-pairs-via-hashmap-split--lc-336-)；最大頻率算術（LC 621）→ [heap_examples.md](./heap_examples.md)。

<!-- 4da59da56b4a -->
### 模板 1：查補數 —— LC 1

**辨識** —— 「回傳兩個加起來等於 `target` 的數的索引」、「和／差／和取餘數為 … 的一組配對」，輸入**未排序**，而且索引要保留。n ≤ 10⁴–10⁵ → O(n²) 的雙層配對迴圈就是要打敗的暴力解。

**關鍵** —— 在記錄索引 `i` 之前，`seen` 裝的剛好是所有索引 `< i` 的值，所以一次查詢 `target - x in seen` 就回答了「我左邊有沒有搭檔？」每一組配對都恰好被找到一次，由它右邊那個元素找到。

<!--CODE-->

<!--CODE-->

**複雜度**
- **時間 O(n)**：`for` 迴圈每個索引只走一次，`need in seen`／`seen[x] = i` 平均都是 O(1) 的雜湊操作。
- **空間 O(n)**：當答案是最後兩個元素時，`seen` 會裝下每一個值。

**手動推演**

<!--CODE-->

**陷阱**
- 先記錄 `x` 再查詢，元素就會跟自己配對：`[3,2,4]`、target 6 會回傳 `[0, 0]`。
- 重複值：`[3,3]`、target 6 —— 覆寫 `seen[3]` 之所以無害，只是因為查詢先跑；答案是 `[0, 1]`。
- 回傳 `[i, seen[need]]` 會把較晚的索引放前面；較早的索引是 `seen[need]`。

**不是這個的情況** —— 輸入已經**排序**（LC 167）→ 雙指標，O(1) 空間（[2_pointers.md](./2_pointers.md)）；題目是在相異值上**數**配對（LC 532）→ 每個相異鍵只探一次，免得一組配對從兩端各數一次。

**練習** —— LC 1 ⭐ · LC 1010 *（鍵 = 除以 60 的餘數，改成累加次數而不是回傳）* · LC 532 *（相異值，只探 `x + k`；k = 0 需要次數）* · LC 15 ⭐ *（固定一個值，剩下的做 two-sum —— [n_sum.md](./n_sum.md)）*

<!-- 248e7575787d -->
### 模板 2：頻率計數 —— LC 242

**辨識** —— 「anagram」、「相同的字母、相同的次數」、「出現最多／最少次」、「第一個不重複的字元」、「`ransomNote` 能不能用 `magazine` 拼出來」。只有小寫英文字母 → 一個固定的 `int[26]` 就是 map，空間 O(1)。

**關鍵** —— 兩個字串互為 anagram，若且唯若它們的計數表相等。在**同一個** map 裡把 `s` 往上數、`t` 往下數，多重集合相同時每個項目剛好都是零，所以檢查只要一趟加上掃一次計數。

<!--CODE-->

<!--CODE-->

**複雜度**
- **時間 O(n)**：`zip` 迴圈把 n 對字元各碰一次；最後只掃最多 k 個相異鍵。
- **空間 O(k)**：`count` 每個相異字元一個項目，對固定的 26 個字母來說就是 O(1)。

**陷阱**
- 跳過長度檢查：`"a"` 對 `"ab"` —— `zip` 在較短的字串就停了，每個計數都是 0，於是回傳 `True`。
- Java 的 `count[c - 'a']` 遇到大寫或 Unicode 輸入（`"Ä"`）就會丟例外。追問「如果是 Unicode 呢？」要的是 `HashMap<Character, Integer>`。
- `Counter` 相減會丟掉非正的次數：`Counter("a") - Counter("ab")` 是空的，所以「差是空的」會說 `"a"` 和 `"ab"` 相符。改用 `Counter(s) == Counter(t)`。

**不是這個的情況** —— 只在乎存不存在、不在乎次數（LC 217）→ 用集合（[set.md](./set.md)）；在乎的是值出現在*哪裡*而不是幾次 → 見下面的索引跨度變形。

**練習** —— LC 242 ⭐ · LC 383 *（數 magazine，花在 note 上）* · LC 387 *（依字串順序再掃一次，第一個次數為 1 的）* · LC 451 *（依次數排序鍵）* · LC 347 ⭐ *（依次數取前 k —— [堆積或桶排序](./heap.md#template-2-count-then-a-size-k-heap-on-the-count--lc-347-)）*

<!-- da08f7e1d36d -->
#### 變形：索引跨度表 —— `span == count` 證明連續 ⭐⭐⭐

**模式**：有時候題目問的不是某個值出現*幾次*，而是它出現在*哪裡*。改成記錄位置而不是次數，一條算術恆等式就能回答「這個值的所有出現位置是不是同一個連續區塊？」：

<!--CODE-->

**關鍵想法**：span 是「如果中間沒夾雜別的東西，這個值的區塊會佔幾格」。只要有任何別的值夾在兩次出現之間，span 就會變大、count 卻不變，所以 `span > count` ⇔ 區塊斷掉了。不用排序、不用走訪索引清單 —— 每個不同的值 O(1)。

<!--CODE-->

**一次掃描的改良版** —— 檢查只會讀三個數字，所以根本不需要索引清單。時間一樣 O(n)，但只用 O(distinct) 格而不是 O(n)：

<!--CODE-->

**四個陷阱**：
1. **特判只出現一次的情況** —— 不需要任何分支：單一索引給出 `span = last - first + 1 = 1` 且 `count = 1`，跟其他區塊一樣通過同一條恆等式。
2. **走訪（或排序）索引清單**來檢查是否連續 —— 索引本來就是遞增進來的，`span == count` 只讀兩端。
3. **數的是出現次數而不是值** —— 答案是通過檢查的不同*鍵*的數量，所以一個在同一區塊裡出現 5 次的值只算 1。
4. **把連續的*索引*跟連續的*值*搞混** —— 這條恆等式講的是陣列中的位置。連續值的序列（LC 128）是另一份 cheatsheet 的招式（一個 set，再加上「`v-1` 在不在？」）。

**變形**（同一招「存位置，而不只是次數」）：

| 題目 | LC# | 變化點 —— map 存的是什麼 |
|---------|-----|---------------------------------|
| Partition Labels | 763 | `{char: last index}`；由左往右掃，把區塊的結尾延伸到目前看過最遠的 `last` —— 恰好在 `i == end` 的地方切一刀 |
| Contains Duplicate II | 219 | `{value: last index}`；命中的條件是 `i - last <= k`，所以只有最近一次的位置有意義 |
| Count Pairs of Equal Substrings With Minimum Difference | 1794 | 兩個字串各一張 map：一邊存 `{char: first index}`，另一邊存 `{char: last index}`；把 `first - last` 壓到最小，再數有幾個字元達到這個最小值 —— [完整解法在 string_examples.md](./string_examples.md#count-pairs-of-equal-substrings-with-minimum-difference--lc-1794) |
| Longest Consecutive Sequence | 128 | 對照組：跨度是在**值**上而不是索引上 —— 一個 set，加上「只從 `v-1` 不存在的地方開始」 |

<!-- 932c9ea149d0 -->
### 模板 3：用算出來的鍵分組 —— LC 49

**辨識** —— 「把在 … 意義下相同的字串／點／元素分成一組」（重排、平移、旋轉、斜率），「以任意順序回傳各組」。n ≤ 10⁴ 個長度 k ≤ 100 的字 → 用排序當鍵是 O(n·k log k)，用計數當鍵是 O(n·k)。

**關鍵** —— 拿元素的**正規形式**去雜湊，而不是元素本身：鍵函數剛好在同一個等價類上相等（anagram 就是排序後的字母），所以一趟 `groups[key(x)].append(x)` 就把每一組都分好，兩個元素落在同一個 bucket，若且唯若它們屬於同一組。

<!--CODE-->

<!--CODE-->

**複雜度**
- **時間 O(n·k log k)**：每個字跑一次 `sorted(word)`，這是主要成本；改用 26 格計數 tuple 當鍵就變成 O(n·k)。
- **空間 O(n·k)**：每個字在它的組裡存一次，每組再加一把鍵。

**陷阱**
- 拿 list 當 dict 的鍵：`groups[sorted(word)]` 會丟 `TypeError: unhashable type: 'list'`。把它接成字串，或轉成 tuple。
- 在 Java 拿 `char[]` 當鍵：陣列是依身分雜湊的，所以 `"eat"` 和 `"tea"` 會落到不同的 bucket。要建一個 `String`。
- 計數鍵接起來時沒有分隔符：計數 `[1, 11]` 和 `[11, 1]` 印出來都是 `"111"`。用 `#` 接起來（[分隔符陷阱](./hashing.md#template-6-canonical-composite-key-lc-36)）。

**不是這個的情況** —— 這個關係不是等價關係，例如「只差一個字母」（LC 127）：它不具遞移性，所以沒有正規鍵 → 圖／BFS。

**練習** —— LC 49 ⭐ · LC 249 *（鍵 = 字母間差距取 26 的餘數）* · LC 609 *（鍵 = 檔案內容）* · LC 149 *（鍵 = 約分後的斜率，見下方）* · LC 4066 *（鍵 = 無序配對，見下方）*

<!-- a582cf9e4395 -->
#### 一般化：鍵就是一個正規化的不變量 ⭐⭐⭐⭐

**模式**：map 的鍵不是原始的值 —— 它是值與值之間某種**關係的正規形式**。兩個元素會在 map 裡撞在一起，剛好就是它們共享你在意的那個性質。

**關鍵想法（幾何）**：「通過錨點 P 的同一條線」⇔「同一個方向向量 `(dx, dy)`」。原始的 `(dx, dy)` 不能當鍵（`(1,2)` 和 `(2,4)` 是同一條線），而 `dy/dx` 用浮點數會掉精度、遇到垂直線還會除以零。**正規化**：先除以 `gcd`，再強制統一符號。

<!--CODE-->

<!--CODE-->

**三個陷阱**（都是面試常見的追問）：
1. **浮點斜率** `dy/dx` —— 精度會掉，垂直線還會 `ZeroDivisionError`。改用約分後的整數配對。
2. **忘了正規化符號** —— `(1,2)` 和 `(-1,-2)` 是同一條線，卻變成兩個不同的鍵。要強制成同一個方向。
3. **忘了 `+1`** —— map 數的是錨點的*夥伴*，錨點自己不在 map 裡。

**變形**（都是同一招「自己發明一個正規化的鍵」）：

| 題目 | LC# | 變化點 —— 這個鍵編碼了什麼 |
|---------|-----|----------------------------------|
| Minimum Area Rectangle | 939 | 鍵 = 放進 set 的點本身；走訪**對角線配對** `(x1,y1),(x2,y2)`（`x1!=x2 && y1!=y2`），再檢查另外兩個角在不在 |
| Most Stones Removed with Same Row or Column | 947 | 鍵 = `row` 與 `~col`（位元取反讓列與行落在互不重疊的 id 空間）→ 在 map 上跑併查集 |
| Vertical Order Traversal of a Binary Tree | 987 | 鍵 = **行偏移量** `col`（root = 0、左 = `col-1`、右 = `col+1`）；值 = 待排序的 `(row, val)` 清單 |

<!-- 941571cbdd46 -->
#### 模式：無序配對鍵 —— 用 `(min, max)` 不分順序地計數 `{x, y}` ⭐⭐⭐

**模式**：當你要數的關係是**對稱**的 —— `(x, y)` 和 `(y, x)` 意思一樣 —— 就用排好順序的配對 `(min(x, y), max(x, y))` 當 map 的鍵。兩個方向會落進同一個桶，桶裡的計數就是你真正要的數字。這是最小的一種正規化不變量：無序配對的正規形式。

<!--CODE-->

**關鍵想法（LC 4066）**：最多一次，把所有 `x` 換成 `y`，讓相等的相鄰對數最大。先問：一次替換 `x -> y` 對每一組相鄰對做了什麼？

<!--CODE-->

所以原本相等的對永遠不會失去，而增加的量**剛好**等於「一邊是 `x`、一邊是 `y`」的相鄰位置數 —— 不分左右。這就是一個無序配對的計數：

<!--CODE-->

<!--CODE-->

<!--CODE-->

<!--CODE-->

**四個陷阱**：
1. **用有序的鍵** —— 直接用 `(x, y)` 會把 `[1,2,1,2,1]` 拆成 `(1,2): 2` 和 `(2,1): 2`，答案變 2 而不是 4。替換不在乎 `x` 在哪一邊，鍵也不能在乎。
2. **擔心替換會破壞原本相等的對** —— `(x, x)` 變成 `(y, y)`，依然相等。沒有任何東西要扣掉，所以答案就是單純相加。
3. **最佳增量從 1 開始，或強制一定要操作** —— 「最多一次」代表可以不做；map 是空的時候貢獻必須是 0。
4. **Java 用字串當鍵** —— 沒有分隔符時 `"1" + "23"` 和 `"12" + "3"` 會撞在一起（見[分隔符陷阱](./hashing.md#template-6-canonical-composite-key-lc-36)）。值放得進 32 bits 時就打包成一個 `long`，不然用 `x + "," + y`。

> **為什麼邊走邊取 max 是安全的**：每次遞增後做 `max(best, cnt[pair])`，會等於最終各計數的最大值，前提是計數永遠不會減少。在滑動視窗裡計數會下降，就要另外再掃一次，或用支援刪除的結構。

**變形**（同一種無序配對鍵）：

| 題目 | LC# | 變化點 —— `(min, max)` 鍵數的是什麼 |
|---------|-----|----------------------------------------------|
| Number of Equivalent Domino Pairs | 1128 | `[a, b]` ≡ `[b, a]`；每個鍵數骨牌張數，再加 `k*(k-1)/2` —— 或在每次遞增前先加上目前的計數 |
| Maximal Network Rank | 1615 | 把無向邊存成 `(min, max)` 鍵的 set，「`u` 和 `v` 有沒有相連？」就是一次 O(1) 查找，不管順序 |
---

<!-- e37d79e451a1 -->
### 模板 4：前綴和計數表 —— LC 560 ⭐⭐⭐⭐⭐

**辨識** —— 「和等於 k／可被 k 整除／0 和 1 一樣多的（連續）子陣列有幾個」，而且值**可能是負的**，所以滑動視窗沒辦法安全地收縮。n ≤ 2·10⁴–10⁵ → O(n)，不是枚舉所有 `(i, j)` 的 O(n²)。

**關鍵** —— `sum(nums[i..j]) = pre[j+1] - pre[i]`，所以以 `j` 結尾、和為 k 的子陣列，剛好對應先前等於 `pre[j+1] - k` 的那些前綴。map 數的是每個先前的前綴出現過幾次，並以 `{0: 1}` 代表空前綴作為種子，所以一次查詢就把所有合法起點一起加進來。

<!--CODE-->

<!--CODE-->

**複雜度**
- **時間 O(n)**：對 `nums` 掃一趟；每一步是一次 `get` 加一次 map 更新。
- **空間 O(n)**：`count_of_prefix` 最多裝 n + 1 個相異的前綴和。

**手動推演**

<!--CODE-->

**陷阱**
- 沒有 `{0: 1}` 這個種子，就會漏掉每一個從索引 0 開始的子陣列：`[1,1,1]`、k = 2 會回傳 1 而不是 2。
- 先記錄前綴再查詢，k = 0 時會把空子陣列也數進去：`[1]`、k = 0 會回傳 1 而不是 0。
- 用 `{prefix: index}` 取代次數，會丟掉重複出現的前綴：`[1,-1,1,-1]`、k = 0 有 4 個子陣列，但每個前綴只記一個索引，每個前綴最多只找得到一個起點。
- 取餘數當鍵（LC 974）又有負值：Java 的 `-7 % 5 == -2`；要用 `((p % k) + k) % k` 正規化。Python 的 `%` 本來就不會是負的。

**不是這個的情況** —— 每個值都是正的，且問的是和 ≤ 或 ≥ k 的最長／最短視窗 → 滑動視窗，O(1) 空間（[sliding_window.md](./sliding_window.md)）；和為 k 的**最長**子陣列 → 每個前綴只記**第一次出現的索引**，以 `{0: -1}` 當種子（LC 325、LC 525）。

**練習** —— LC 560 · LC 930 *（值只有 0／1，程式碼一樣）* · LC 974 *（鍵 = 前綴取 k 的餘數）* · LC 525 *（0 → −1，每個前綴記第一次的索引）* · LC 325 *（最長：記第一次的索引）* · LC 3026 *（見下方：以起點的值為鍵，存最小前綴）*

**依題目問的東西，map 該存什麼**：

| 題型 | map 的值 | 例子 | 特別注意 |
|-------------|-----------|---------|---------------|
| 數出所有子陣列 | `count` | LC 560, 930, 974 | 先查再更新 |
| 計數（帶取餘） | `count` | **LC 974** | **用餘數當鍵；處理負數！** |
| 找最長子陣列 | `index`（第一次出現） | LC 325, 525 | 只存第一次出現 |
| 找最長（需先轉換） | `index`（第一次出現） | **LC 525** | **把 0→-1、1→+1；初始化 {0:-1}** |
| 判斷是否存在 | `boolean/index` | LC 523 | 任一次出現都可以 |
| 找最大和，條件落在兩端 | `min prefix`，以**起點的值**為鍵 | **LC 3026** | **鍵是 `nums[i]`，不是前綴和** —— 見[下面的變形](#variation-key-by-the-starts-value-keep-the-min-prefix--lc-3026-) |

<!-- 3ac689388b40 -->
#### 變形：以起點的值為鍵，存最小前綴 — LC 3026 ⭐⭐⭐

> **關鍵** —— 這就是 Kadane 的「減掉目前最小的前綴」（LC 53），只是按起點的值拆成一個值一個桶。計數表回答「有幾個」；最小前綴表回答「最多多少」。

**化約為**：[模板 4](#template-4-prefix-sum-count-map--lc-560-) —— 一樣是對前綴和掃一趟，只是鍵換成起點的值，值換成最小前綴。

**模式**：條件落在子陣列的**兩端**，而不是它的和上，問的又是**最大和**。所以鍵換了，值也換了：

- **鍵 = 起點的值** `nums[i]`，因為「good」（`|nums[i] - nums[j]| == k`）限定的是起點的*值*，不是它的前綴和；
- **值 = 這個值目前見過的最小 `pre[i]`**，因為以 `j` 結尾的和是 `pre[j+1] - pre[i]`，`pre[i]` 越小，和就越大。

<!--CODE-->

<!--CODE-->

<!--CODE-->

**走查** —— `nums = [-1,3,2,4,5], k = 3`：

<!--CODE-->

**四個陷阱**：
1. **`res` 從 0 開始** —— good 子陣列的和可以是負的（`[-1,-2,-3,-4], k = 2` → `-6`）。用 `-inf`，只有在**完全沒有** good 子陣列時才回傳 0。
2. **存了*包含*起點的前綴** —— 子陣列包含 `nums[i]`，所以要減的是 `pre[i]`，也就是它*之前*的和。`best[x]` 要在 `pre += x` 之前記下。
3. **只存某個值最新或最早的前綴** —— 兩者都不一定是最好的起點。要存**最小值**，就像 LC 325 存第一次出現的索引那樣。
4. **Java 用 `int`** —— 1e5 個 1e9 會溢位；前綴和答案都要用 `long`（鍵 `x ± k` 落在 ±2e9 內，`int` 放得下）。

**變形**（同一招「每個鍵存最好的起點」）：

| 題目 | LC# | map 存什麼，為什麼 |
|---------|-----|-----------------------------|
| Maximum Subarray | 53 | 每個起點都合格，所以整張表縮成**一個**全域最小前綴 —— 就是 Kadane 演算法 |
| Maximum Good Subarray Sum | 3026 | `{起點的值: 最小前綴}`；查 `x - k` 和 `x + k` |
| Maximum Size Subarray Sum Equals k | 325 | `{前綴和: 第一次出現的索引}` —— 這裡「最好的起點」是*最早的*，因為要的是最長 |

---

<!-- 5ec283d4fd0e -->
### 模板 5：固定視窗的字元計數 —— LC 438

**辨識** —— 「找出 `p` 的所有 anagram 在 `s` 中的起點」、「`s2` 裡有沒有 `s1` 的排列」：一個長度**固定**為 `len(p)` 的視窗，用字母計數來比較。n ≤ 3·10⁴ 個小寫字母 → O(26·n)。

**關鍵** —— 第 `right` 步之後，視窗剛好是 `s[right-m+1..right]`：每一步在右邊加一個字元、移掉從左邊掉出去的那個，所以 `window` 永遠是目前這個長度 m 的視窗的計數表，而 `window == need` 就是 anagram 的判斷。

<!--CODE-->

<!--CODE-->

**複雜度**
- **時間 O(26·n)**：對 `s` 掃一趟；每次 `window == need` 最多比 26 個鍵。
- **空間 O(26)**：兩個 map 都只裝小寫字母。

**陷阱**
- 在 dict 裡留下次數為零的項目：`{'a': 0, 'b': 1} != {'b': 1}`，所以 `s = "ab", p = "b"` 會漏掉索引 1。次數降到 0 就把鍵刪掉（Java 的 `int[26]` 沒有這個問題）。
- 從 `right >= m` 才開始比、而不是 `right >= m - 1`，會跳過第一個完整的視窗：`s = "ab", p = "ab"` 回傳 `[]`。
- 固定視窗不需要「matched」計數器：比較兩個 map 就是全部的檢查。LC 567 在第一次相等時就回傳 `True`。

**不是這個的情況** —— 視窗長度會變（「最多 k 種字元的最長子字串」，LC 3、LC 76）→ [sliding_window.md](./sliding_window.md) 的先擴張再收縮；計數表一樣，視窗規則不一樣。

**練習** —— LC 438 · LC 567 ⭐ *（第一次相符就回傳）* · LC 30 *（「字元」換成整個單字）* · LC 76 ⭐ *（可變視窗加上 `matched` 計數器，在 sliding_window）*

<!-- be6c2a151207 -->
### 模板 6：排名表 —— 值對應到位置 —— LC 953 ⭐⭐⭐⭐⭐

**辨識** —— 「**在這套外星語言中**是否依字典序排好」、「依照 `order` 給定的順序排序 `s`」、「從前序和中序建出這棵樹」：題目自己定義了一套順序。順序字串 ≤ 26 個字母 → 每次查排名都是 O(1)。

**關鍵** —— 先一次算好 `rank[c]` = `c` 在給定順序裡的位置；之後比較排名就等於用外星順序比較字母，每次比較都是 O(1) 的整數比較，而不是 O(m) 的 `order.index(c)` 掃描。

<!--CODE-->

<!--CODE-->

**複雜度**
- **時間 O(total chars)**：建 `rank` 是 O(26)；每對相鄰的字最多比到較短的那個長度，所以每個字元最多被讀兩次。
- **空間 O(1)**：`rank` 最多 26 個項目。

**陷阱**
- 前綴規則：`["apple", "app"]` **不是**排序好的。共同前綴比完之後，較短的字必須排在前面；字母迴圈跑完就回傳 `True` 會漏掉這個情況。
- 在比較裡呼叫 `order.index(c)` 雖然正確，但每個字元都要 O(26)；排名要事先算好一次。
- LC 791：不在 `order` 裡的字元，`rank[c]` 會丟 `KeyError`；給它一個排在最後面的排名，`rank.get(c, len(order))`。

**練習** —— LC 953 · LC 791 *（把排名當排序鍵，或用計數排序）* · LC 1122 *（relative sort array）* · LC 105 ⭐ *（排名 = 在中序裡的位置，見下方）* · LC 106

**變形 —— 用「值 → 索引」表切分陣列（LC 105 / LC 106）**：同一個 map，只是這裡的「排名」是*在中序序列中的位置*，把 O(n²) 的「掃中序找 root」變成 O(1)，整個建樹變成 O(n)。

<!--CODE-->

---

<!-- 5436ea967821 -->
### 模板 7：用兩個 map 做雙射 —— LC 205

**辨識** —— 「isomorphic strings」、「符合同一個 pattern」、「不能有兩個字元對到同一個字元」。兩個序列等長，n ≤ 5·10⁴ → O(n)。

**關鍵** —— 一個映射是雙射，若且唯若它在**兩個**方向上都一致：`s2t` 擋掉一個 `a` 對到兩個不同的 `b`，`t2s` 擋掉兩個不同的 `a` 對到同一個 `b`。記錄之前兩邊都先檢查，就能讓兩個 map 在每一步都維持是部分雙射。

<!--CODE-->

<!--CODE-->

**複雜度**
- **時間 O(n)**：對所有配對掃一趟；每次檢查與記錄都是 O(1)。
- **空間 O(k)**：每個 map 對每個相異字元（LC 290 裡則是單字）各有一個項目。

**陷阱**
- 只用一個 map：`s = "badc", t = "baba"` 通過每一個 `s → t` 的檢查（`b→b, a→a, d→b, c→a`）而回傳 `True`；是 `t → s` 那個 map 抓到 `b` 同時來自 `b` 和 `d`。
- LC 290 沒做長度檢查：`pattern = "aaa", s = "dog dog dog dog"` —— `zip` 在三個單字後就停了，回傳 `True`。
- Java 的 `s2t.get(a) != b` 是拿包裝過的 `Character` 跟 `char` 比，會先拆箱，所以是安全的；拿兩個包裝過的 `Character` 用 `!=` 比就不安全了。

**練習** —— LC 205 · LC 290 *（字母 ↔ 單字）* · LC 890 *（對每個單字都跑一次這個檢查）* · LC 291 *（pattern ↔ 子字串：在同樣的兩個 map 上回溯）*

---

<!-- ca82c9206716 -->
### 模板 8：舊節點對新節點的表 —— LC 133

**辨識** —— 「回傳這張圖／這條串列的**深拷貝**」、「clone」，節點型別帶有可能形成環、或可以指向任何地方的指標（random 指標）。V, E ≤ 10⁴ → O(V + E)。

**關鍵** —— `copies[node]` 在第一次走到 `node` 時就建立，而且在走訪它的鄰居**之前**，所以繞回 `node` 的環會在 map 裡找到它的副本，而不是再遞迴一次。這個 map 同時是 visited 集合，也是舊 → 新的對照表。

<!--CODE-->

<!--CODE-->

**複雜度**
- **時間 O(V + E)**：`clone` 的本體每個節點只跑一次（`if original in copies` 的 return 擋掉重複），每條鄰接串列也只走一次。
- **空間 O(V)**：`copies` 每個節點一個項目，遞迴堆疊在一條路徑圖上可以深到 V。

**陷阱**
- 在鄰居迴圈之後才登記副本：兩個節點的圖 `1 — 2` 會遞迴 `1 → 2 → 1 → …`，直到堆疊爆掉。
- 用 `val` 當鍵在 LC 133 行得通（值都不同），但在值會重複的 LC 138 就壞了。要用節點本身當鍵。
- `cloneGraph(None)` 必須回傳 `None`，而不是一個值為 0 的節點 —— 空圖本身就是 LC 133 的範例之一。

**不是這個的情況** —— 只問連不連得到或數量（LC 200）→ 一個 visited 集合，不需要 clone 表（[graph.md](./graph.md)）。

**練習** —— LC 133 ⭐ · LC 138 *（random 指標：同一個 map，兩趟或交錯著做）* · LC 1490 *（N 元樹，沒有環）* · LC 652 *（map 存的是子樹特徵 → 次數）*

---

<!-- f47ff99f40ee -->
### 模板 9：虛擬映射（重新映射）—— LC 710

**辨識** —— 「在 `[0, n)` 裡隨機挑一個**不在** `blacklist` 裡的整數」、「盡量少呼叫亂數函式」。n ≤ 10⁹、黑名單 ≤ 10⁵ → 沒辦法把合法值列出來，而 `pick()` 必須是 O(1)。

**關鍵** —— 合法的值剛好有 `bound = n - B` 個，所以只從 `[0, bound)` 裡抽。這個範圍內的每個黑名單位置，都映射到尾端 `[bound, n)` 裡一個相異的合法值 —— 尾端的合法值個數剛好等於前面壞位置的個數 —— 所以這個 map 是對合法集合的雙射，每個合法值被抽到的機率都一樣。

<!--CODE-->

<!--CODE-->

**複雜度**
- **建構 O(B)、每次挑選 O(1)**：建構子的迴圈每個黑名單值走一次，`last` 只會在 B 個尾端位置上往下移；`pick` 是一次抽籤加一次 map 查詢。
- **空間 O(B)**：`black_set` 和 `mapping` 各最多 B 個項目。

**手動推演**

<!--CODE-->

**陷阱**
- 從 `[0, n)` 抽、抽到黑名單就重抽：期望的抽籤次數是 n / (n − B)，當黑名單逼近 n 時沒有上限。
- 映射到 `last` 時沒跳過也在黑名單裡的尾端值：`n = 3, blacklist = [0, 2]` 會把 0 映射到 2，一個黑名單值。
- 為 `b ≥ bound` 的黑名單值做映射：它永遠不會被抽到，在它身上花掉一個尾端值，可能讓 `last` 跑到 `bound` 以下。

**練習** —— LC 710 · LC 398 *（蓄水池抽樣）* · LC 528 *（前綴和 + 二分搜尋）* · LC 384 *（Fisher–Yates 洗牌）*

---

<!-- 15f4cb426aa5 -->
### 模板 10：map 加堆積 —— LC 692

**辨識** —— 「回傳出現次數最多的 k 個單字，依次數排序，同次數依字母序」、「依次數取前 k」：map 負責計數，第二個結構負責把 map 數出來的東西排序。k ≪ n → 堆積勝過把每個鍵都排序。

**關鍵** —— map 一趟就把輸入變成 `(item, count)` 配對；依 `(-count, word)` 排序的堆積，pop 出來的順序就剛好是題目要的 —— 次數高的先出，同分時字母序較小的先出 —— 所以前 k 次 pop 就是答案。

<!--CODE-->

<!--CODE-->

**第二種解法 —— 依次數分桶，不用堆積。** 次數最多是 `len(words)`，所以可以拿來當串列的索引：`buckets[c]` 放剛好出現 `c` 次的單字。從高到低走一遍次數，頻率順序就自動排好了，而同分規則只會比較次數相同的單字。這就是 LC 347 的 O(n) 桶排序（[heap § Template 2](./heap.md#template-2-count-then-a-size-k-heap-on-the-count--lc-347-)）再加上每個桶內排序 —— 面試官要你不用堆積做 top-K 時，值得會。

<!--CODE-->

**複雜度**
- **Python 是 O(n + k log n)**：計數一趟，`heapify` 是 O(n)，k 次 `heappop` 每次 O(log n)。Java 的 `addAll` 是一個一個插入，所以是 O(n log n)。分桶解法是 O(n)，再加上它讀到的每個桶的排序。
- **空間 O(n)**：`count` 和堆積（或桶）對每個相異單字各一個項目。

**陷阱**
- 堆積裡放 `(freq, word)` 卻沒加負號，會先 pop 出**最少**出現的單字。
- 大小為 k 的 min-heap（O(n log k) 的追問）需要**反過來**的同分順序，讓字母序較大的單字留在根部、最先被淘汰；沿用 `(-count, word)` 的順序會淘汰錯的單字。
- Java 比較器寫成 `count.get(b) - count.get(a)` 就是減法比較器；要用 `Integer.compare`。

**不是這個的情況** —— 沒有同分規則、只要出現最多的 k 個值（LC 347）→ 依頻率做桶排序，O(n)（[heap § Template 2](./heap.md#template-2-count-then-a-size-k-heap-on-the-count--lc-347-)）；要的是第 k 大的**值**而不是次數 → [heap.md](./heap.md)。

**練習** —— LC 692 · LC 347 ⭐ *（沒有同分規則：堆積，或 O(n) 的桶）* · LC 451 *（每個鍵，依次數排序）* · LC 1636 *（次數遞增，同分時值遞減）*

> 沒有同分規則時，top-K 不用堆積的 O(n) 解法見 [heap § Template 2](./heap.md#template-2-count-then-a-size-k-heap-on-the-count--lc-347-)；用單調堆疊算出 `next greater` 再存進 map（LC 496），見 [monotonic_stack § Template 1](./monotonic_stack.md#template-1-next-greater-element-decreasing-stack--lc-496)。

<!-- 2654cd9c0187 -->
## 各語言 API —— 有序 map（TreeMap / SortedDict）

雜湊表不保留順序。題目需要最接近的鍵（floor / ceiling）、最小或最大的鍵，或某個範圍內的所有鍵時，改用**有序 map**：Java 的 `TreeMap`，或 Python 的 `SortedDict`。Python 標準函式庫沒有有序 map；`SortedDict` 來自第三方套件 `sortedcontainers`，LeetCode 有預先安裝（本機用 `pip install sortedcontainers`）。

| 你需要的 | Java `TreeMap` | Python `SortedDict`（`d`） |
|---|---|---|
| 最小／最大的鍵 | `firstKey()` / `lastKey()` | `d.keys()[0]` / `d.keys()[-1]` |
| 最小／最大的項目 | `firstEntry()` / `lastEntry()` | `d.peekitem(0)` / `d.peekitem(-1)` → `(k, v)` |
| ≤ x 的最大鍵（floor） | `floorKey(x)` → 鍵或 `null` | `i = d.bisect_right(x) - 1`，`i >= 0` 時取 `d.keys()[i]` |
| ≥ x 的最小鍵（ceiling） | `ceilingKey(x)` → 鍵或 `null` | `i = d.bisect_left(x)`，`i < len(d)` 時取 `d.keys()[i]` |
| < x 的最大鍵／> x 的最小鍵 | `lowerKey(x)` / `higherKey(x)` | `bisect_left(x) - 1` / `bisect_right(x)`，同樣要檢查邊界 |
| `[lo, hi]` 內的鍵 | `subMap(lo, true, hi, true)` | `d.irange(lo, hi)` |
| 移除最小／最大的項目 | `pollFirstEntry()` / `pollLastEntry()` | `d.popitem(0)` / `d.popitem(-1)` |
| 第 k 小的鍵 | 不支援（只能逐一走訪） | `d.keys()[k]`，O(log n) |
| 單純查值 `get(k)` | O(log n)，沿樹往下走 | O(1)，雜湊查找 |
| 自訂順序 | `Comparator` | `SortedDict(key_func)` —— 是鍵的轉換，不是比較器 |

**能擋掉大多數 bug 的規則**：Java 的 `floorKey` / `ceilingKey` 回傳的是**鍵**或 `null`；Python 的 `bisect_*` 回傳的是**索引**，可能是 `-1` 或 `len(d)`。取值前先檢查索引，因為 `d.keys()[-1]` 會默默回傳最大的鍵，而不是「沒有」。另外要呼叫 `d.bisect_left(x)`，不要寫 `bisect.bisect_left(list(d.keys()), x)`：`list(...)` 的複製是 O(n)。

<!--CODE-->

<!--CODE-->

**用在哪裡**：訂房與重疊檢查（LC 729、731、732）在 [design_examples.md](./design_examples.md#sorted-map--time-index-examples)；剩下最小的牌（LC 846）和區間串流（LC 352）在 [hash_map_examples.md](./hash_map_examples.md#index)；時間戳的 floor 查找（LC 981）在 [design_examples.md](./design_examples.md#time-based-key-value-store--lc-981)。如果鍵只需要排序一次，就排序一個串列 —— 有序 map 為了保持有序，每次更新都要付 O(log n)。

<!-- 124e9f25f025 -->
## 練習路徑

| 階段 | 題目（由易到難） | 新增的能力 |
|---|---|---|
| **核心** ⭐ | LC 1 · LC 242 · LC 49 · LC 347 · LC 146 | 查補數、計數、依鍵分組、依次數取前 K、map + 串列做快取（[linked_list § Template 7](./linked_list.md#template-7-hash-map--doubly-linked-list-lru--lc-146-)） |
| **練習** | LC 205 · LC 290 · LC 383 · LC 953 · LC 438 · LC 560 · LC 133 | 雙射、把計數當預算、排名表、固定視窗計數、前綴和計數、clone 表 |
| **延伸** | LC 692 · LC 3026 · LC 149 · LC 710 | 帶同分規則的 top-K、每個鍵存最小前綴、正規化的斜率鍵、虛擬重新映射 |

<!-- ea4fa3974f42 -->
## 總結與速查

<!-- b7b376a44537 -->
### 題目 → 模式決策表

| 怎麼認出來 | 模式 | 模板 | 時間／空間 | 題目 |
|-----------------|---------|----------|--------------|----------|
| 湊出 target 的配對、三元組或補數；「two sum」、「k-diff」、「可被 60 整除」 | 查補數 | [T1](#template-1-complement-lookup--lc-1) | O(n) / O(n) | 1, 15, 16, 18, 167, 532, 653, 1010, 1679, 1711, 2006 |
| 元素、字元或 pattern 的出現次數；「出現最多次」、「anagram」、重複值，或某個值出現在*哪裡*（「同一個連續區塊」） | 計數／頻率表，含[索引跨度變形](#variation-index-span-map--span--count-proves-contiguity-) | [T2](#template-2-frequency-count--lc-242) | O(n) / O(n) | 242, 49, 451, 347, 692, 387, 819, 811, 1207, 383, 299, 349, 350, 4038, 763, 219, 1794 |
| 依某種*推導出來*的形式歸為一類；「group」、「同一條線」、「同一列或同一行」；`(x, y)` 算作 `(y, x)` 的**對稱**配對 | 用算出來的鍵分組，含[無序配對鍵](#pattern-unordered-pair-key--min-max-counts-x-y-in-either-order-) | [T3](#template-3-grouping-by-a-computed-key--lc-49) | O(n·k) / O(n) | 49, 149, 609, 939, 947, 987, 4066, 1128, 1615 |
| 子陣列的某個性質：和等於 k、和可被 k 整除、0 和 1 一樣多、剛好 k 個奇數；或**兩端**滿足某條件的子陣列的最大和 | 前綴和 → 計數表，含[每個起點值存最小前綴的變形](#variation-key-by-the-starts-value-keep-the-min-prefix--lc-3026-) | [T4](#template-4-prefix-sum-count-map--lc-560-) | O(n) / O(n) | 560, 325, 523, 525, 930, 974, 1248, 724, 3026 |
| 用字母計數比較的字元視窗（這裡是固定長度；先擴張再收縮的放在 [sliding_window.md](./sliding_window.md)） | 視窗 + 字元計數 | [T5](#template-5-fixed-window-character-counts--lc-438) | O(n) / O(k) | 3, 76, 424, 438, 567, 159, 340, 904, 1004, 1208, 1234 |
| 「依照 ... 給定的順序」、自訂字母表、某個排列、依索引切分 | 排名表 | [T6](#template-6-rank-map--value-to-position--lc-953-) | O(n) / O(n) | 953, 791, 105, 106 |
| 一個**雙向**都必須是一對一的映射 | 雙射（兩個 map） | [T7](#template-7-bijection-with-two-maps--lc-205) | O(n) / O(n) | 205, 290 |
| 「設計一個 cache」、O(1) get + put、淘汰機制，或值得記憶化的遞迴 | 快取／記憶化 | [linked_list § T7](./linked_list.md#template-7-hash-map--doubly-linked-list-lru--lc-146-) | 攤還 O(1) / O(n) | 146, 460, 705, 706, 380, 381, 432, 355, 981, 1244, 139, 322, 1048 |
| 節點之間的關係：clone、父指標、子樹特徵、把等式當成邊 | 圖／樹的節點表 | [T8](#template-8-old-to-new-node-map--lc-133) | O(n) / O(n) | 133, 138, 652, 721, 734, 399, 947, 1257 |
| 「均勻隨機挑一個，但絕不能挑到這些值」 | 虛擬映射（重新映射） | [T9](#template-9-virtual-map-remapping--lc-710) | 每次挑 O(1) / O(B) | 710, 398, 528, 384 |
| 光有 map 不夠 —— 你還需要順序、堆積或堆疊 | map + 另一個結構 | [T10](#template-10-map-plus-heap--lc-692) | 視情況 | 347, 496, 503, 739, 853, 729, 846, 352 |

> 上表每一列的最壞情況，在所有鍵都碰撞時都是**每次操作 O(n)**。每一題實戰題，連同難度和它被詳解的那份文件，都在 [hash_map_examples.md → Index](./hash_map_examples.md#index)。

<!-- bcad8c07f58c -->
### 關鍵洞見與辨識訊號

1. **空間換時間**：雜湊表用額外的 O(n) 空間換平均 O(1) 的查找
2. **前綴和的魔法**：`subarray[i,j] = prefixSum[j] - prefixSum[i-1]`
3. **視窗狀態**：用雜湊表有效率地維護視窗的性質
4. **補數思維**：不要窮舉所有配對，改成把元素存起來再查它的補數
5. **索引還是值**：想清楚 map 的值要存索引、存值，還是兩個都存
6. **頻率計數**：大部分字串／陣列題都能靠頻率分析解掉

**面試時要留意的訊號：**
- 「找重複／重複出現的子字串」→ rolling hash，或二分搜尋 + 雜湊
- 「把一組值一致地映射到另一組」→ 雙射（兩個 map）
- 「把每個 `x` 換成 `y`」／「`(a, b)` 等同 `(b, a)`」→ 無序配對鍵 `(min, max)`
- 「把快取最佳化」→ 用 OrderedDict／雙向鏈結串列做 LRU
- 追問「如果陣列非常大呢？」→ 省空間的雜湊（rolling hash、座標壓縮）

<!-- 81adf5e94fe9 -->
### 實作最佳實務

<!-- 0843105b457a -->
#### Python 最佳實務
<!--CODE-->

<!-- 3c5e6849a65d -->
#### Java 最佳實務
<!--CODE-->

**效能**：

1. **選對雜湊函式**：Python 內建的 hash 通常已經夠好
2. **避免不必要的 rehash**：能預先給定大小就給
3. **記得清理**：頻率表裡計數歸零的項目要移除
4. **用合適的負載因子**：預設的 0.75 通常就是最佳解

<!-- 12f7da4e3bf6 -->
### 該避開的常見錯誤

1. **以為不會碰撞**：記住最壞情況的時間複雜度是 O(n)，不是 O(1)

2. **索引越界**： 
<!--CODE-->

3. **在走訪過程中修改 dict**：
<!--CODE-->

4. **忽略邊界情況**：
   - 空陣列
   - 只有一個元素的陣列
   - 所有元素都相同
   - target 根本湊不出來

5. **選錯資料結構**：
   - 只要判斷存不存在就用 `set()`
   - 需要鍵值映射才用 `dict()`
   - 純粹算次數就用 `Counter()`

<!-- bca3c22ce88c -->
### 面試準備檢查清單

- [ ] 熟練上面的每一份模板，以及各自的使用時機
- [ ] 每個分類練 3-5 題
- [ ] 搞懂每個模式的時間／空間複雜度
- [ ] 知道常見的邊界情況，以及怎麼處理
- [ ] 能把雜湊碰撞的解法講清楚
- [ ] Python dict 和 Java HashMap 的 API 都要用得順
- [ ] 知道什麼時候**不該**用雜湊表（已排序的資料、範圍查詢等等）

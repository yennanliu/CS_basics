<!-- 5f5563581520 -->
# Java 技巧與慣用寫法

> **範圍** — 決定「正確的演算法會不會算出正確答案」的那些 Java 語言語意：字元其實是整數、傳值與傳參考的差別，以及整數運算。函式庫 API 則放在另外兩份姊妹速查表。
> **另見**：[java_trick_collections.md](./java_trick_collections.md) — 陣列、list、map、佇列、堆積(heap)、堆疊與 pair；[java_trick_strings_sorting.md](./java_trick_strings_sorting.md) — String 與 StringBuilder 的操作，以及所有跟 comparator 有關的東西；[python_trick.md](./python_trick.md) — 同一片領域的 Python 版；[complexity_cheatsheet.md](./complexity_cheatsheet.md) — 這些 API 所包裝的每種結構的 Big-O 查表。

<!-- 44f58a05e9aa -->
## LeetCode 題目清單

- [Java](https://leetcode.com/problem-list/java/)

<!-- e5b831cc6244 -->
## 總覽

在 Java 面試裡，幾乎所有「邏輯明明是對的，答案卻錯了」都出自三類 bug，而這三類全都是語言語意問題，不是演算法選錯：

| 類別 | 這個 bug 長什麼樣 | 在哪裡解決 |
|---|---|---|
| **字元就是整數** | `charAt(i)` 給你的是 `char`，你把它當數字用時，拿到的其實是它的 ASCII 碼，而且不會報錯 | [字元與數字](#characters--digits) |
| **傳值 vs 傳參考** | 你以為複製了一份物件卻改到原本那個，或以為在共用某個基本型別卻只是複製了一份 | [傳值 vs 傳參考](#value-vs-reference--the-rule-behind-most-java-bugs-here) |
| **整數運算** | `/` 會朝零截斷、`%` 可能回傳負數，而 `int` 在 2^31 就溢位 | [整數運算與運算子](#integer-math--operators) |

<!-- 0e58ee55081b -->
### 參考資料

- [Java Documentation](https://docs.oracle.com/en/java/)
- [LeetCode Java Solutions](https://leetcode.com/problemset/all/?languageTags=java)

<!-- 9a39dfc6d27a -->
## 字元與數字

<!-- 89406de35e62 -->
### `charAt`、字元比較與「字母 → 索引」


**關鍵方法**：`charAt()`、字元比較、ASCII 運算

<!--CODE-->

<!--CODE-->

**效能備註**：對字串而言 `charAt(i)` 是 O(1)，所以逐字元處理很有效率。

<!-- 7f511cbc91d4 -->
### 數字字元轉整數值（`char - '0'`）


**關鍵概念**：用 ASCII 相減，把數字字元（'0'-'9'）轉成它的整數值。

<!--CODE-->

**常見模式：對數字字元做遞增／遞減（LC 752 - Open the Lock）**

<!--CODE-->

**速查表：**

| 操作 | 程式碼 | 範例 |
|-----------|------|---------|
| Char → Int | `c - '0'` | `'7' - '0'` → `7` |
| Int → Char | `(char)('0' + n)` | `(char)('0' + 7)` → `'7'` |
| 遞增（繞回） | `(digit + 1) % 10` | `9 + 1` → `0` |
| 遞減（繞回） | `(digit - 1 + 10) % 10` | `0 - 1` → `9` |

**遞減時為什麼要 `+ 10`？**
<!--CODE-->

**對照：字母 vs 數字的對映**

| 類型 | Char → 索引 | 索引 → Char | 範圍 |
|------|--------------|--------------|-------|
| **字母** | `c - 'a'` | `(char)('a' + i)` | 'a'-'z' → 0-25 |
| **數字** | `c - '0'` | `(char)('0' + i)` | '0'-'9' → 0-9 |

<!-- 0b924786e4a7 -->
### `charAt()` 回傳的是 `char`，不是那個數字的值 ⭐


> **陷阱**：`new Integer(s.charAt(i))` 拿到的是 ASCII 碼（例如 51），不是數字本身（例如 3）。

<!-- 501d454eba40 -->
#### 錯誤寫法

<!--CODE-->

<!-- 884f649d0a67 -->
#### 正確寫法

<!--CODE-->

<!-- 0ea7050cd1bd -->
#### 原因：`char` 是以 ASCII/Unicode 值儲存的

| 運算式    | 結果       |
|---------------|--------------|
| `'3'`         | 51 (ASCII)   |
| `'0'`         | 48           |
| `'3' - '0'`  | 3            |

<!-- ee9d27577cef -->
#### `new Integer(...)` 也已經被棄用

<!--CODE-->

<!-- 515b42ffb7df -->
#### 多位數的子字串要用 `parseInt`

<!--CODE-->

<!-- b57b55b1a964 -->
#### 一句話總結

<!--CODE-->

---

<!-- 370ee0c0cbd7 -->
### 把字母對映成陣列索引（`c - 'a'`）

因為 `char` 本身是整數型別，減掉 `'a'` 就把一個小寫字母變成
**0 起始的索引**，這正是能用 26 格陣列取代 `HashMap<Character, ?>` 的原因：

<!--CODE-->

值得這麼做的理由是複雜度，不是寫起來比較短：讀陣列是 O(1)，而看似等價的
`order.indexOf(c)` 每次都要重掃字串，是**每次查詢 O(n)**。放在一個比較迴圈裡，
差別就是 O(n) 和 O(n²)。

同樣的相減對數字也成立，只是把 `'a'` 換成 `'0'` — 見上面那張字母 vs 數字的表，
那張才是要背起來的。

<!-- 4d8adb565f9c -->
### 走訪一段字元範圍


<!--CODE-->

<!-- 7f163dd003cb -->
## 傳值 vs 傳參考 — 這裡多數 Java bug 背後的那條規則

<!-- 7a975476523d -->
### `equals()` vs `==` — 什麼時候用哪一個 ⭐


> **核心規則**：`==` 比的是**參考**（是不是同一個物件？）。`equals()` 比的是**內容**（值一不一樣？）。

<!-- 37bf55bad248 -->
#### 規則本身 — `equals()` vs `==`

<!--CODE-->

<!-- 5a799e26fb9e -->
#### 比較集合（List、Set、Map）

<!--CODE-->

<!-- f421c764eb0c -->
#### 比較字串

<!--CODE-->

<!-- aa8e3adf1a78 -->
#### 比較包裝型別（Integer、Long 等）

<!--CODE-->

<!-- 3f13f0660f0b -->
#### 比較基本型別

<!--CODE-->

<!-- 11a35726cc66 -->
#### 總結表 — `equals()` vs `==`

| 類型 | 用 `==` | 用 `equals()` | 陷阱 |
|------|----------|----------------|---------|
| `int`、`long`、`char`……（基本型別） | **是** | 不適用（沒有這個方法） | 無 |
| `String` | **否** | **是** | 字面值可能共用參考，但別依賴它 |
| `Integer`、`Long`……（包裝型別） | **否** | **是** | `==` 只在 -128..127 有效（快取） |
| `List`、`Set`、`Map` | **否** | **是** | `==` 比的是身分，不是內容 |
| 自訂物件 | **否** | **是**（若有覆寫） | 預設的 `equals()` 等同於 `==` |
| null 檢查 | **是**（`x == null`） | **否**（會 NPE！） | 檢查 null 一律用 `==` |

<!-- fc10235810ff -->
#### 面試速記規則

<!--CODE-->

<!-- 06fc40fe5fca -->
### 遞迴中的基本型別 vs 參考型別 — 回溯的判斷規則 ⭐


> **核心規則**：基本型別是傳值 → 每次呼叫都拿到自己的副本 → **不需要回溯**。
> 參考型別（集合、陣列、物件）是傳參考 → 狀態共用 → **必須回溯**。

<!-- 9eb8d8b10806 -->
#### 規則本身 — 遞迴中的基本型別 vs 參考型別

<!--CODE-->

<!-- 210739c06692 -->
#### 情況 1：基本型別 — 不需要回溯（LC 112 Path Sum）

<!--CODE-->

**記憶體模型：**
<!--CODE-->

<!-- 6be5a1a722f2 -->
#### 情況 2：全域變數 — 需要回溯（LC 112 V0-2）

<!--CODE-->

<!-- bfa399ab688a -->
#### 情況 3：參考型別（List）— 需要回溯（LC 113 Path Sum II）

<!--CODE-->

<!-- bacf3f4e503c -->
#### 情況 4：StringBuilder — 需要回溯（LC 988 Smallest String Starting From Leaf）

<!--CODE-->

<!-- 029b97e6ee91 -->
#### 總結表 — 遞迴中的基本型別 vs 參考型別

| 狀態類型 | 範例 | 要回溯嗎？ | 原因 |
|---|---|---|---|
| 基本型別參數 | `int curSum` | **否** | 每次呼叫都有自己的副本 |
| 包裝型別參數（自動裝箱） | `Integer curSum` | **否** | 自動裝箱(autoboxing)會建出新物件 |
| 區域變數 | `int newSum = curSum + val` | **否** | 只屬於當前的堆疊框架 |
| 實例／全域變數 | `this.curSum` | **是** | 所有呼叫共用 |
| 集合參數 | `List<Integer> path` | **是** | 是參考，會就地被修改 |
| 陣列參數 | `int[] path` | **是** | 是參考，會就地被修改 |
| StringBuilder 參數 | `StringBuilder sb` | **是** | 是參考，透過 `append()`/`deleteCharAt()` 就地被修改 |

<!-- 0c2c45c17ca8 -->
#### 面試提示

<!--CODE-->

---

<!-- 9c2c3999d988 -->
### 遞迴的參數傳遞


<!--CODE-->

**重要觀念**：在 Java 裡，基本型別是**傳值**的（會建立副本）。

<!--CODE-->

**重點帶走**：需要跨遞迴呼叫追蹤狀態時，要嘛用實例變數，要嘛把遞迴設計成回傳值再合併。

<!-- d02a0c209604 -->
### 把值傳出方法之外 — 可變容器（mutable holder）模式


> **核心觀念**：參考型別（StringBuilder、List、int[]、Map 等）是以**參考**傳遞的，不是傳值。在函式內做的修改，在函式回傳之後依然存在。

<!-- c3d6ed857638 -->
#### 模式 1：用 StringBuilder 組路徑／字串（LC 694）

<!--CODE-->

**記憶體模型：**
<!--CODE-->

<!-- 60518943ec74 -->
#### 模式 2：用 List 收集結果（LC 113 Path Sum II）

<!--CODE-->

**和基本型別的關鍵差異：**
<!--CODE-->

<!-- 2358b495e986 -->
#### 模式 3：通用模式 — 建立、傳入、修改、使用

<!--CODE-->

<!-- 155b196d2ed8 -->
#### 這個模式常用到的參考型別

| 類型 | 修改方法 | 需要回溯嗎？ | 使用情境 |
|------|----------------------|-------------------|----------|
| `StringBuilder` | `append(x)`、`setCharAt(i, c)`、`deleteCharAt(i)` | ✅ 是 | 帶回溯的字串組建 |
| `List<T>` | `add(x)`、`remove(i)`、`set(i, x)` | ✅ 是 | 路徑／結果收集 |
| `int[]` / `char[]` | `arr[i] = value` | ✅ 是 | 陣列修改 |
| `Map<K,V>` | `put(k, v)`、`remove(k)` | ✅ 是 | 次數統計 |
| `Queue<T>` | `add(x)`、`poll()`、`offer(x)` | ✅ 看情況 | BFS 逐層走訪 |
| `Set<T>` | `add(x)`、`remove(x)` | ✅ 是 | 已訪記錄 |
| 基本型別 `int`、`long` | 不適用（傳值） | ❌ 否 | 只能靠回傳值或實例變數 |
| `String` | 不適用（不可變） | ❌ 否 | 改用 StringBuilder |

**什麼時候要回溯：**
<!--CODE-->

<!-- b86925b34377 -->
#### 模式 4：List 收集（LC 131 Palindrome Partitioning）

<!--CODE-->

**關鍵區別：**
<!--CODE-->

<!-- a14743f9c33b -->
### 基本型別是以傳值方式傳遞的


<!--CODE-->

<!-- fe886372e47f -->
### 直接修改自訂類別的欄位（`v.field -= 1`）⭐


> **核心問題**：什麼時候可以寫 `v.cnt -= 1`，什麼時候不行？

<!-- bddfe8f64128 -->
#### 什麼時候「可以」直接修改

三個條件必須同時成立：

1. **欄位可存取**（不是 `private`，或者你就在該類別內部）
2. **欄位不是 `final`**
3. **參考不是 `null`**

<!--CODE-->

<!-- 2d1dfb39fe4b -->
#### 什麼時候「不能」直接修改

**情況 1 — `private` 欄位**（在類別外部）
<!--CODE-->

**情況 2 — `final` 欄位**
<!--CODE-->

**情況 3 — null 參考**
<!--CODE-->

<!-- 9f844f2f1a47 -->
#### 常見誤解：`final` 的參考 vs `final` 的欄位

<!--CODE-->

變數上的 `final` 代表你不能讓 `v` 指向另一個物件，
它**並不會**阻止你修改那個物件的欄位。

<!-- 85fdd460c0a9 -->
#### `Integer`（包裝型別）欄位 — 可以用，但會自動裝箱

<!--CODE-->

<!-- bae1f3b9069a -->
#### 總結表 — 就地修改自訂類別

| 情境 | 允許 `v.cnt -= 1` 嗎？ |
|-----------|----------------------|
| `int cnt`（package-private） | ✅ 可以 |
| `private int cnt`（在類別外部） | ❌ 編譯錯誤 |
| `final int cnt` | ❌ 編譯錯誤 |
| `final ValCnt v`（參考是 final） | ✅ 可以 — 欄位仍然可變 |
| `v == null` | ❌ NullPointerException |
| `Integer cnt`（包裝型別） | ✅ 可以，但會自動裝箱成新物件 |

<!-- 5c419a687d22 -->
#### 面試陷阱：`v.cnt--` vs `--v.cnt` vs `v.cnt -= 1`

三者都會把 `cnt` 減 1，差別在於**運算式回傳的值**：

<!--CODE-->

當你只在意副作用、不在意回傳值時，就用 `v.cnt -= 1`。

---

<!-- 7158fa55f6f7 -->
### 賦值複製的是參考，不是物件


- https://github.com/yennanliu/CS_basics/blob/master/leetcode_java/src/main/java/LeetCodeJava/LinkedList/ReverseLinkedList.java


<!--CODE-->

<!-- c80d9b745df5 -->
### 重新建構節點，而不是就地修改


- LC 116
- https://github.com/yennanliu/CS_basics/blob/master/leetcode_java/src/main/java/LeetCodeJava/Recursion/PopulatingNextRightPointersInEachNode.java

<!--CODE-->

<!-- 6ff00ad03e1c -->
### 取得目前實例的防禦性複本

<!--CODE-->

<!-- 0f3b0f6fe679 -->
## 整數運算與運算子

<!-- 6e1960ce48cc -->
### `ceil` vs `floor` — 定義


| 操作 | 意義 | 範例 |
|-----------|---------|---------|
| `Math.ceil(x)` | **向上**取到最近的整數 | `ceil(7.0/3)` → `3.0` |
| `Math.floor(x)` | **向下**取到最近的整數 | `floor(7.0/3)` → `2.0` |
| `(int)(a/b)` | **朝零截斷**（正數時等同 floor） | `7/3` → `2` |

<!--CODE-->

**關鍵地雷**：Java 的整數除法**一律朝零截斷**（正數時等於 floor）。
<!--CODE-->

---

<!-- 966fed9932df -->
### 整數版的天花板除法 — 不需要 `double` ⭐


**公式**：只用整數算出 `ceil(a / b)`：

<!--CODE-->

**為什麼會成立：**
<!--CODE-->

**對照：計算天花板除法的兩種寫法**

<!--CODE-->

**經典用法 — LC 1283 Find the Smallest Divisor Given a Threshold：**
<!--CODE-->

---

<!-- 93cde1b031b4 -->
### 整數版的地板除法


對正整數來說，`/` 本身就是 floor：
<!--CODE-->

遇到**負數**時，要用 `Math.floorDiv`：
<!--CODE-->

---

<!-- 3ab1634b4419 -->
### 天花板／地板速查

| 目標 | 程式碼 | 備註 |
|------|------|-------|
| Ceil（用 double） | `(int) Math.ceil((double) a / b)` | 好讀，但必須轉型 |
| Ceil（整數技巧） | `(a + b - 1) / b` | 快、不用轉型，但僅限正數 |
| Floor（正數） | `a / b` | 整數除法會截斷 |
| Floor（任意正負） | `Math.floorDiv(a, b)` | 能正確處理負數 |
| 四捨五入（.5 進位） | `(int) Math.round((double) a / b)` | 最接近的整數 |
| 取中點且不溢位 | `l + (r - l) / 2` | 標準的二分搜尋中點寫法 |

---

<!-- aa668f0f5514 -->
### 用到天花板除法的經典 LC 題


| LC | 題目 | 天花板除法的用途 |
|----|---------|----------------------|
| **1283** | Find the Smallest Divisor Given a Threshold | 每個元素算 `(val + d - 1) / d` |
| **1011** | Capacity To Ship Packages Within D Days | 需要的天數 `(wt + cap - 1) / cap` |
| **875**  | Koko Eating Bananas | 每堆要吃的小時數 `(pile + k - 1) / k` |
| **2064** | Minimized Maximum of Products Distributed | 每組的天花板 `(n + m - 1) / m` |

**模式**：這些全都是**對答案做二分搜尋**的題目，而檢查函式都需要天花板除法來數「Y 裡面塞得下幾個 X」。

<!--CODE-->

---

<!-- b1ad100f2ed8 -->
### 用 `long` 避免 `int` 溢位


<!--CODE-->

<!-- 8554221a40d9 -->
### 三個數的最大值與最小值


<!--CODE-->

<!-- 8105bd9df3f1 -->
### 計算整數中設為 1 的位元數

<!--CODE-->

<!-- 2c30900ddc1b -->
### 預先算好 N 以內的完全平方數


> **技巧**：先把所有 ≤ N 的完全平方數算進一個 list，之後就走訪這個 list，而不是每次重算 `i * i`。在 LC 279（Perfect Squares）這類 BFS/DP 題裡很好用。

<!--CODE-->

<!-- 21abfafa66a7 -->
### 用 `random.nextInt` 取隨機整數

<!--CODE-->

<!-- b4933c2986d4 -->
## 速查表

<!-- 9ab757c4c10b -->
### 最常見的寫法

<!-- b78408489e1d -->
#### 資料結構初始化
<!--CODE-->

<!-- b9921e406195 -->
#### 必備的型別轉換
<!--CODE-->

<!-- 8c5034744ca9 -->
#### 常見操作
<!--CODE-->

<!-- c1952b6565ca -->
### 效能提示


| 操作 | 有效率的做法 | 該避免的做法 |
|-----------|-------------------|-------|
| **字串組建** | `StringBuilder` | 在迴圈裡做字串串接 |
| **字元存取** | 先 `toCharArray()` 再走訪 | 在密集迴圈裡用 `charAt()` |
| **排序** | `Arrays.sort()`、`Collections.sort()` | 對大量資料用 stream 排序 |
| **印出陣列** | `Arrays.toString()`、`Arrays.deepToString()` | 手動走訪 |
| **字元對映** | `char - 'a'` | 重複呼叫 `indexOf()` |

<!-- 10e15cf42a8a -->
### 常見的 LeetCode 模式

<!-- 5a1409457dbb -->
#### 次數統計
<!--CODE-->

<!-- d8f0e4ba736a -->
#### 雙指標搭配字元比較
<!--CODE-->

<!-- c3759f4ca85c -->
#### 用優先佇列處理 Top-K 問題
<!--CODE-->

<!-- 5c9a056482cc -->
### 記憶體管理


- **基本型別陣列**：比物件陣列更省記憶體
- **ArrayList**：會自動擴充，資料量大時初始容量很重要
- **StringBuilder**：迴圈中做字串串接時請用它
- **字元陣列**：處理字元時比直接操作 String 更有效率

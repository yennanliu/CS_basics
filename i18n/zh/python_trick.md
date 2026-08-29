<!-- d0810a6a2056 -->
# Python 技巧與慣用寫法

> **範圍** — 解題過程中會用到的 Python 語言慣用寫法 — 複製、字串處理、排序 key、整數運算、生成式(comprehension)與作用域 — 依「你想做什麼」來分組。標準函式庫與索引運算另有專屬速查表。
> **另見**：[python_trick_stdlib.md](./python_trick_stdlib.md) — `heapq`、`bisect`、`SortedDict`、`collections`、`itertools`、`functools`；[python_trick_indexing.md](./python_trick_indexing.md) — 插入、切片(slicing)以及背後的差一(off-by-one)運算；[python_gotchas.md](./python_gotchas.md) — 那些「令人意外」而不只是「好用」的行為；[java_trick.md](./java_trick.md) — 同一片領域的 Java 版。

<!-- c2e966b4e731 -->
## LeetCode 題目清單

- [Python](https://leetcode.com/problemset/all/?languageTags=python3)

<!-- e6a20af27bfd -->
## 總覽

這份速查表以前是 3,672 行、全部塞在單一個 `## 1) Examples` 標題底下，68 個條目以
`0-1)`、`1-11''')`、`1-27-3)` 這種毫無規律的編號排列。編號已經拿掉了；
請用「它在做什麼」來找東西。

| 我想要… | 前往 |
|---|---|
| 複製一份 list 或 dict，而且不是別名 | [複製與參考](#copying--references) |
| 對字串做切片、補零、去空白、切分或重組 | [字串](#strings) |
| 用自然順序以外的規則排序 | [排序與比較](#sorting--comparison) |
| 做除法、取整、取餘數，或避開溢位的意外 | [數字與數學](#numbers--math) |
| 同時走訪兩個東西，或用一行建出一個 list | [走訪、生成式與函數式工具](#iteration-comprehensions--functional-tools) |
| 計數，或用預設值取代 `KeyError` | [Dict 與 Set](#dicts--sets)，或 [python_trick_stdlib.md](./python_trick_stdlib.md) 裡的 `Counter` / `defaultdict` |
| 在巢狀函式裡面寫入外層變數 | [結構、作用域與回傳值](#structure-scope--return-values) |
| 用堆積(heap)、二分搜尋、有序 map 或 `itertools` | [python_trick_stdlib.md](./python_trick_stdlib.md) |
| 插入 list、切出子陣列，或把差一算對 | [python_trick_indexing.md](./python_trick_indexing.md) |

<!-- 111d3bcbf551 -->
## 複製與參考

<!-- eb123fd9a375 -->
### 賦值 vs 淺複製 vs 深複製

- https://www.runoob.com/w3cnote/python-understanding-dict-copy-shallow-or-deep.html
- https://iter01.com/578999.html
- 複製的種類：深複製(deep copy)、淺複製(shallow copy)、參考複製(reference copy)
<!--CODE-->

<!-- 8b95f8f04900 -->
### 哪一種複製才會得到「獨立」的物件


**問題是：** 要怎麼複製，才能讓改動*原本那個*不會影響到*複本*（反之亦然）？

<!--CODE-->

**關鍵差異 — 取決於這個 list 是「扁平」還是「巢狀」：**

<!--CODE-->

**決策表 —「我要一份原件動不到的複本」**

| 資料形狀 | 用什麼 | 獨立嗎？ |
|------------|-----|--------------|
| `x = y`（賦值） | — | ❌ 同一個物件，所有改動都會外洩 |
| 扁平 list `[1,2,3]` | `x[:]` / `x.copy()` / `list(x)` | ✅ 完全獨立 |
| 巢狀 list `[[..],[..]]` | `x[:]`（淺） | ⚠️ 只有外層 — 內層會外洩 |
| 巢狀 list / dict / 物件 | `copy.deepcopy(x)` | ✅ 完全獨立 |
| Dict（值是不可變的） | `d.copy()` / `dict(d)` / `{**d}` | ✅（值若可變則仍共用） |

**經驗法則**
- `[:]`、`.copy()`、`list()` → **淺複製**：只有在元素是*不可變的*
  （int、str、tuple）或你只動最上層時才安全。
- `copy.deepcopy()` → **深複製**：任何巢狀都安全，但比較慢 — 只有在你真的會
  修改巢狀元素時才用。

> 經典的回溯用法（見上面的 LC 77）：`result.append(current[:])` 會把*當前*路徑快照下來，
> 後續的 `current.pop()` / `current.append()` 就不會弄壞已存的結果 —
> 這能成立是因為路徑元素是不可變的 int。

<!-- 7310dc3c9017 -->
## 字串

<!-- d274fa33280b -->
### 對字串的字元做排序

<!--CODE-->

<!-- b24bb1c1ae45 -->
### 依索引取代某個字元（切片 + 串接）


**重點：Python 的字串是不可變的** — 你**不能**寫 `s[i] = ch`
（會丟出 `TypeError: 'str' object does not support item assignment`）。
要「改掉索引 `i` 上的字元」，就用 `i` 前後的切片重組出一個**新**字串。

<!--CODE-->

<!--CODE-->

**為什麼是 `i+1`？** `s[i+1:]` 從 `i` *之後*開始，所以舊字元 `s[i]` 被丟掉、
換成 `ch`。如果改用 `s[i:]`，舊字元會被保留（變成插入而不是取代）：

<!--CODE-->

**經典 LC 用法 — LC 433 Minimum Genetic Mutation**（BFS，一次變異一個基因字元）：

<!--CODE-->

> **替代做法**：如果你要改很多個位置，先轉成 `list`
> （`arr = list(s); arr[i] = ch; s = "".join(arr)`）— list 是可變的，
> 所以可以就地做索引賦值，也免去一再重組字串。
> 只改一個位置的話，上面的切片寫法最乾淨。

<!-- 3e38a9177c28 -->
### 把字串補零

<!--CODE-->

<!-- 672c4e0b931d -->
### `lstrip` / `rstrip` / `strip`


<!--CODE-->

<!--CODE-->

**常見 LC 用法 — 組出數字之後把前導零去掉**

<!--CODE-->

> **經驗法則**：`lstrip('0')` 是把數字字串正規化（去掉前導零）的慣用寫法 —
> 但一定要處理**空字串**的結果（`res or "0"`），
> 因為 `"0"`/`"0000"` 會被剝成 `""`。反向操作（補零）見 [`zfill`](#zero-padding-a-string)。

<!-- 1f814dcaa146 -->
### `ord()`、`chr()`、`isalpha()`、`isdigit()`

<!--CODE-->

<!-- afa2f84a57f4 -->
### 字串方法速查

<!--CODE-->

<!-- 0492c323d9be -->
### 從字串中抓出數字字元

<!--CODE-->

<!-- 44ce25990e01 -->
### 反向走訪字串


**關鍵差異：`range()` 的 stop 值是「不含」的**

<!--CODE-->

| 寫法 | stop 值 | 走訪到的索引 | 含索引 0 嗎？ |
|------|-----------|-----------------|-------------------|
| `range(len(x)-1, -1, -1)` | `-1`（不含） | `len-1 … 0` | **是** |
| `range(len(x)-1,  0, -1)` | ` 0`（不含） | `len-1 … 1` | **否** |

**經驗法則：** 要反向走訪「所有」索引，stop 值一律用 `-1`。

<!--CODE-->

**當你「確實」想跳過索引 0 時**（例如要拿 `x[i]` 跟 `x[i-1]` 比較）：
<!--CODE-->

<!-- a5c16186d611 -->
## 排序與比較

<!-- 56e03c69d638 -->
### 用 `lambda` 當 key 的 `sort`

<!--CODE-->

<!--CODE-->

<!-- ec4269f4d47c -->
### 降冪：`key=lambda x: -x[0]` vs `reverse=True` vs `[::-1]`


降冪排序有三種寫法，各有各適用的場合。

<!--CODE-->

<!-- a29ec8cf261e -->
### 多重 key 的 tuple 排序：`key=lambda x: (x[0], x[1])` ⭐⭐⭐⭐⭐

**關鍵想法**：讓 `key` 回傳一個 **tuple**。Python 比較 tuple 是**由左到右**，
遇到第一個不相等的元素就短路。所以 `(-x[0], x[1])` 的意思是*「主要依 x[0] 降冪，平手時依 x[1] 升冪」*。

**快速決策表**

| 目標 | 寫法 |
|------|---------|
| key1 升冪 | `key = lambda x : x[0]` |
| key1 降冪 | `key = lambda x : -x[0]` **（僅限數值）** 或 `reverse = True` |
| key1 升冪、key2 升冪 | `key = lambda x : (x[0], x[1])` |
| key1 降冪、key2 降冪 | `key = lambda x : (x[0], x[1]), reverse = True` |
| key1 降冪、key2 升冪 | `key = lambda x : (-x[0], x[1])` **（key1 為數值）** |
| key1 升冪、key2 降冪 | `key = lambda x : (x[0], -x[1])` **（key2 為數值）** |
| 方向混合且 key **非數值** | **兩次穩定排序** — 先依*最後*一個 key 排（見下文） |
| 需要兩兩比較的自訂規則 | `functools.cmp_to_key(my_cmp)` |

<!--CODE-->

**視覺化追蹤** — tuple key 是怎麼把上面的資料排出來的：

<!--CODE-->

<!-- 11ccfa3bb292 -->
#### **`reverse = True` vs 把 key 取負**

<!--CODE-->

<!-- 94d9e450c742 -->
#### **非數值 key 又要方向混合 → 兩次穩定排序**

Timsort 是**穩定的**，所以你可以把排序串起來。規則：**先依最後（最不重要）的 key 排。**

<!--CODE-->

> ⚠️ **千萬不要**把這兩趟的順序顛倒 — 先依 `len` 再依 `s` 排，會把 `len` 的分組整個丟掉。

<!-- 17c03dfbd42f -->
#### **`functools.cmp_to_key` — 當根本寫不出 key 函式時**

當順序取決於**把兩個元素拿來一起比較**（沒有任何單一元素的值能表達這個規則）時就用它。

<!--CODE-->

<!-- 26564f335364 -->
#### **其他好用的排序 key**

<!--CODE-->

**Java 對應寫法備註：**

<!--CODE-->

**🚫 常見錯誤：**

<!--CODE-->

**💡 面試提示：**

- 先把規則唸出來 —— *「依 A 降冪排，平手時依 B 升冪」* —— 然後照著寫出 tuple key。
- **複雜度**：`O(n log n)` 次比較；k 個 key 的話每次組 tuple key 是 `O(k)` → `O(n log n * k)`。
- `list.sort()` 和 `sorted()` 都是**穩定的** — 這正是多趟排序技巧和 `LC 406` 那種插入法能成立的原因。
- 如果你沒辦法把規則寫成「每個元素各自算一個 key」，那就是該用 `cmp_to_key`（py）／自訂 `Comparator`（java）的訊號。

**相關 LeetCode 題目：**

| 題目 | LC# | 排序 key |
|---------|-----|----------|
| **Queue Reconstruction by Height** | **406** | `(-h, k)` — 高的先，再依 k 升冪 |
| Largest Number | 179 | `cmp_to_key(a+b vs b+a)` |
| Top K Frequent Words | 692 | `(-count, word)` |
| K Closest Points to Origin | 973 | `x² + y²` |
| Merge Intervals | 56 | `start` 升冪 |
| Meeting Rooms II | 253 | `start` 升冪（+ 對 end 用最小堆積） |
| Non-overlapping Intervals | 435 | `end` 升冪（貪婪） |
| Group Anagrams | 49 | `"".join(sorted(word))` |
| Custom Sort String | 791 | `order.index(ch)` |
| Sort Array By Parity | 905 | `x % 2` |
| Relative Sort Array | 1122 | `(rank.get(x, len), x)` |
| Car Fleet | 853 | `position` 降冪（+ 堆疊） |
| Boats to Save People | 881 | `weight` 升冪（+ 雙指標） |

**總結：**
- ✅ tuple key = 多重 key 排序，比較方式是**由左到右**
- ✅ `-key` 只翻轉一個欄位（**僅限數值**）；`reverse=True` 翻轉**全部**欄位
- ✅ 非數值又要方向混合 → **兩次穩定排序，先排最不重要的 key**
- ✅ 寫不出單一元素的 key → `functools.cmp_to_key` / java `Comparator`
- ✅ Java：用 `Integer.compare(b, a)` 而不是 `b - a` 以避開溢位；`.reversed()` 會作用在整條鏈上

<!-- eb69c283cbfe -->
### 用具名 `key` 函式寫條件式的 tuple key


當排序 key 取決於某個**條件**（A 群 vs B 群、合法 vs 不合法等）時，
一行 lambda 會變得很難讀。改寫成一個**回傳 tuple 的具名 `key` 函式** —
tuple 依然是逐元素（由左到右）比較，所以第一個欄位就是主要排序依據、
下一個是平手時的判準，以此類推。

**模式：開頭放「群組標籤」+ 各群組自己的排序規則**

<!--CODE-->

**開頭那個 `0` / `1` 是幹嘛的？** 那是**群組標籤** — 所有第 0 群的項目都會排在
所有第 1 群之前（因為 tuple 比較會先看第一個元素）。剩下的 tuple 欄位只在
*同一群之內*才有意義，所以每一群都可以有自己的排序規則（升冪、取負做降冪、
甚至用完全不同的欄位）。

**關鍵規則**
- 所有分支必須回傳**長度相同**的 tuple，而且每個位置的型別要**可互相比較**
  （別在同一個欄位裡混用 `str` 和 `int`）。
- 把某個數值欄位取負（`-item.priority`），就能讓那個欄位降冪、其餘維持升冪 —
  跟 [1-11'] 節同一招。
- `key` 函式**每個元素只會被呼叫一次**（Schwartzian transform），所以就算裡面
  邏輯比較重也還是有效率。

**經典 LC 用法 — LC 937 Reorder Data in Log Files**（字母 log 排在數字 log 之前，
字母 log 先依內容再依 id 排序）：

<!--CODE-->

> **經驗法則**：只要排序規則出現*分支*，就該改用回傳 tuple 的具名 `key` 函式 —
> 這比把 `if/else` 硬塞進 lambda 好讀太多。

<!-- cbf75432ec6c -->
### 依出現次數排序一個 dict

<!--CODE-->

<!-- a996b01c56af -->
### 帶 `key` 的 `min()` / `max()`

<!--CODE-->

<!-- 78754fde5d79 -->
### `sorted()`、`reversed()`、`sum()`、`abs()`

<!--CODE-->

<!-- 5a5c5fa65042 -->
## 數字與數學

<!-- 707b9304b563 -->
### 一次拿到商和餘數 — `divmod`

<!--CODE-->

<!-- ae476da0a6ea -->
### 除以某個數之後的餘數

<!--CODE-->

<!--CODE-->

<!-- 48de31cb91e8 -->
### `pow(x, n, mod)` — 快速模冪

<!--CODE-->

<!-- 14401e6fbed5 -->
### 整數除法 `//` 與位元運算

<!--CODE-->

<!-- f5f721d649b6 -->
### 把 N 進位的整數轉成十進位

<!--CODE-->

<!-- cd393bda7b15 -->
### 無限大與邊界值

<!--CODE-->

<!-- 1092b2453cf5 -->
## 走訪、生成式與函數式工具

<!-- 6c441085a4ac -->
### `all()`

- 會針對 list 中「所有」元素檢查條件，回傳 Boolean（true 或 false）
<!--CODE-->

<!-- bdaad4cd6785 -->
### `not` 邏輯

<!--CODE-->

<!-- 149cf6fd7e68 -->
### 走訪 dict

<!--CODE-->

<!-- 532a42670c53 -->
### 星號（`*`）運算式

<!--CODE-->

<!-- 77696a2eced5 -->
### List 生成式

<!--CODE-->

<!-- ca15a89341d0 -->
### `map()` 與生成器運算式

<!--CODE-->

<!-- 135e5be852e8 -->
### 三元（條件）運算式

<!--CODE-->

<!-- 7d0a76b6a1f5 -->
## Dict 與 Set

<!-- faf86fb91f9e -->
### Dict 的 `get()`、`setdefault()`、生成式

<!--CODE-->

<!-- 8c7a98c1ff3b -->
### Set 運算

<!--CODE-->

<!-- d7c1ed9bb944 -->
### 用 `or` 檢查「其中一個元素存在」

<!--CODE-->

<!-- 4b02a87eb7e5 -->
## 結構、作用域與回傳值

<!-- f5c842750f60 -->
### 二維陣列（矩陣）初始化

<!--CODE-->

<!-- e1d144984ec2 -->
### 巢狀函式中的 `nonlocal` 與 `global`

<!--CODE-->

<!-- fb485df4ab20 -->
### `isinstance()` 與型別檢查

<!--CODE-->

<!-- 4f0f65bbfe4a -->
### 用小型類別承載多個回傳值


當一個 DFS／遞迴需要**一次回傳好幾個值**（例如高度 + 大小 + 一個旗標）時，Java 的慣用寫法是開一個小的 `private static class SubtreeInfo`。在 Python 裡最接近的做法是 `@dataclass`、普通類別，或 `NamedTuple`。

<!--CODE-->

<!-- 49b0039d3ea0 -->
#### **選項 1：`@dataclass`（推薦）**

`@dataclass` 會自動產生 `__init__`、`__repr__`、`__eq__` — 樣板程式碼最少、可讀性最高。

<!--CODE-->

<!-- 9f7c3f8830a1 -->
#### **選項 2：傳統類別（不需 import）**

<!--CODE-->

<!-- f2adb02fd843 -->
#### **選項 3：`NamedTuple`（輕量 + 不可變）**

當這組資料應該是**唯讀**時就用它（也能像 tuple 一樣解包）。

<!--CODE-->

<!-- a9c7ef81b9db -->
#### **快速比較**

| 選項 | 樣板程式碼 | 可變嗎？ | 最適合 |
|--------|-------------|----------|----------|
| `@dataclass`   | 少  | 可（要不可變就加 `frozen=True`） | **預設選擇** — 乾淨又好讀 |
| 普通類別    | 多 | 可  | 不允許 import／非常舊的 Python |
| `NamedTuple`   | 少  | **不可** | 不可變的資料組，同時能 tuple 解包 |

> **偷懶的替代做法**：一次性的 DFS 你其實可以直接 `return (height, size, is_perfect)` 再解包 — 但一旦欄位到 3 個以上，具名類別／`NamedTuple` 好讀太多了。對 LeetCode 風格的解法而言，`@dataclass` 通常是取代 Java `private static class` 最乾淨的選擇。

> **經驗法則：** 如果你*修改*的是一個共用容器（`append`/`add`），你就必須把它復原（`pop`/`remove`）。如果你每次呼叫都建立一個*新*物件（字串串接、`tmp + [x]`、tuple），那份複本本身就是回溯 — 沒有東西需要復原。另見 [0-2) 賦值 vs 淺／深複製](#assignment-vs-shallow-copy-vs-deep-copy)。

<!-- 10de60233575 -->
### 對較長的陣列做交換

<!--CODE-->

<!-- e2a0f752d050 -->
### DFS 路徑：`str`（不可變、不用回溯）vs `list`（可變、需要回溯）


在 DFS／回溯遞迴中往下傳 `path` 時，**資料型別決定了你必不必須復原（回溯）**：

<!--CODE-->

**為什麼？**
- **`str`** 是不可變的：`path + "->" + str(node.val)` 每次呼叫都會建出一個**全新的字串**。父層的 `path` 完全沒被動到，所以每個分支自動拿到自己那份獨立的複本 — 沒有東西要復原。
- **`list`** 是可變的：`path.append(...)` 修改的是所有遞迴呼叫**共用的那同一個物件**。探索完一個分支之後，你必須 `path.pop()` 把狀態還原給兄弟分支 — 否則殘留資料會跨分支外洩。

<!--CODE-->

**同樣的道理，其他不可變的載體** — tuple 和「傳一個新 list 進去」也同樣不用顯式 pop，因為它們交給每個子節點的是一個全新的物件，而不是共用同一個：

<!--CODE-->

**`int` 累加器（`cur_sum`）遵循「同一條」不可變規則 — 不用回溯** ⭐

一個很常見的困惑：在一個**同時**帶著累加和（`cur_sum`，一個 `int`）**和**路徑
list（`cache`）的 DFS 裡，為什麼我們要 `cache.pop()`，卻從來不用把 `cur_sum`
「加回去」？因為**整數是不可變的** — `cur_sum += root.val` 並不會就地修改父層那個
整數；它是把*區域*的 `cur_sum` **重新綁定**到一個全新的 int 物件。子層的堆疊框架
被銷毀時，父層的 `cur_sum` 毫髮無傷。

| 變數 | Python 怎麼傳它 | 需要回溯嗎？ | 為什麼 |
|----------|----------------------|-----------------|-----|
| **`cur_sum`**（`int`） | **傳值**（不可變的複本） | **❌ 否** | `+= val` 會產生一個新的 int 綁到區域名稱上；父層的值從未被覆寫，所以子層框架結束時它就自動還原了。 |
| **`cache`**（`list`） | **傳參考**（同一個共用物件） | **✅ 是** | 整棵遞迴樹共用同一個 list 實例。子層的 `append` 父層看得到，所以我們「必須」`pop()` 清乾淨。 |

<!--CODE-->

**記憶體逐步演練** — 父層處於 `cur_sum = 5`、`cache = [5]`，往值為 `3` 的子節點走：

| | 往下進入子層 | 往上回到父層 |
|---|---|---|
| **`cache`（list）** | `cache.append(3)` → `[5, 3]`（同一個物件） | 沒有 `pop()` 就會一直是 `[5, 3]` → **父層被汙染 → 必須回溯** |
| **`cur_sum`（int）** | `cur_sum + 3` → `8`（新的 int，區域的） | 子層框架被銷毀 → 父層的 `cur_sum` 仍是 `5` → **不需要回溯** |

| `path` ／累加器型別 | 可變嗎？ | 每次呼叫都是新物件？ | 需要回溯（`pop`）？ |
|-------------|----------|----------------------|-------------------------|
| `int`（`cur_sum`） | 否 | 是（`n + x` 重新綁定） | **否**                  |
| `str`       | 否       | 是（`s + x`）        | **否**                  |
| `tuple`     | 否       | 是（`t + (x,)`）     | **否**                  |
| `list` + `tmp + [x]` | 否（重新綁定） | 是 | **否** |
| `list` + `append` | **是** | 否（共用）     | **是 — `path.pop()`**  |

<!-- ce370f70d98a -->
### 實作對照 — LC 445 Add Two Numbers II 與 LC 394 Decode String

- String -> Int
<!--CODE-->

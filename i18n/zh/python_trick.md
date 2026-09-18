<!-- 4fffb422a786 -->
# Python 技巧與慣用寫法

> **範圍** — 解題過程中會用到的 Python 語言慣用寫法 — 內建操作的成本、複製、字串處理、排序 key、整數運算、生成式(comprehension)與作用域 — 依「你想做什麼」來分組。標準函式庫與索引運算另有專屬速查表。
> **另見**：[python_trick_stdlib.md](./python_trick_stdlib.md) — `heapq`、`bisect`、`SortedDict`、`collections`、`itertools`、`functools`；[python_trick_indexing.md](./python_trick_indexing.md) — 插入、切片(slicing)以及背後的差一(off-by-one)運算；[python_gotchas.md](./python_gotchas.md) — 那些「令人意外」而不只是「好用」的行為；[complexity_cheatsheet.md](./complexity_cheatsheet.md) — 同一份 Big-O 查表，但對象是資料結構與經典演算法而非 Python 內建；[java_trick.md](./java_trick.md) — 同一片領域的 Java 版。

<!-- c2e966b4e731 -->
## LeetCode 題目清單

- [Python](https://leetcode.com/problemset/all/?languageTags=python3)

<!-- 746c757f81c6 -->
## 總覽

這份速查表以前是 3,672 行、全部塞在單一個 `## 1) Examples` 標題底下，68 個條目以
`0-1)`、`1-11''')`、`1-27-3)` 這種毫無規律的編號排列。編號已經拿掉了；
請用「它在做什麼」來找東西。

| 我想要… | 前往 |
|---|---|
| 知道某個內建操作要花多少成本 — `len`、`max`、`in`、`+`、`split`、切片、`dict`/`set` 操作 | [常見操作的複雜度](#complexity-of-common-operations-) |
| 複製一份 list 或 dict，而且不是別名 | [複製與參考](#copying--references) |
| 對字串做切片、補零、去空白、切分或重組 | [字串](#strings) |
| 用自然順序以外的規則排序 | [排序與比較](#sorting--comparison) |
| 做除法、取整、取餘數，或避開溢位的意外 | [數字與數學](#numbers--math) |
| 同時走訪兩個東西，或用一行建出一個 list | [走訪、生成式與函數式工具](#iteration-comprehensions--functional-tools) |
| 計數，或用預設值取代 `KeyError` | [Dict 與 Set](#dicts--sets)，或 [python_trick_stdlib.md](./python_trick_stdlib.md) 裡的 `Counter` / `defaultdict` |
| 在巢狀函式裡面寫入外層變數 | [結構、作用域與回傳值](#structure-scope--return-values) |
| 用堆積(heap)、二分搜尋、有序 map 或 `itertools` | [python_trick_stdlib.md](./python_trick_stdlib.md) |
| 插入 list、切出子陣列，或把差一算對 | [python_trick_indexing.md](./python_trick_indexing.md) |

<!-- bdbf335112e9 -->
## 常見操作的複雜度 ⭐⭐⭐⭐⭐

**這一節為什麼存在**：幾乎每一次「你的解法是 O(n²)，能更快嗎？」的時刻，都來自把某個內建操作
當成了 O(1)。`len()` 確實是免費的；`x in my_list`、`res += ch` 和 `arr[1:]` 都不是。

`n` = 被操作物件的大小，`m` = 另一個運算元的大小，`k` = 切片的大小。

<!-- c4229b925477 -->
### 內建函式

| 操作 | 時間 | 空間 | 為什麼（底層） |
|---|---|---|---|
| `len(x)` | **O(1)** | O(1) | 不是走一遍 — 長度就存在物件標頭的欄位裡（list/tuple/str 的 `ob_size`、dict 的 `ma_used`、set 的 `used`） |
| `max(it)` / `min(it)` | O(n) | O(1) | 掃一遍，途中保留目前最佳值 |
| `max(it, key=f)` | O(n · f) | O(1) | `f` 是**每個元素呼叫一次**（Schwartzian transform），不是每次比較呼叫一次 |
| `sum(it)` | O(n) | O(1) | 線性掃描。⚠️ `sum(list_of_lists, [])` 是 **O(n²)** — 每個 `+` 都建一個新 list |
| `any(it)` / `all(it)` | 最壞 O(n) | O(1) | 遇到第一個 `True` / `False` 就**短路**離開 — 最好情況 O(1) |
| `sorted(it)` / `list.sort()` | O(n log n) | O(n) / O(n) | Timsort。`sorted()` 還要另外配置新 list；`.sort()` 是原地的，但合併時仍需暫存緩衝區 |
| `reversed(seq)` | **O(1)** | O(1) | 回傳的是*惰性迭代器* — 什麼都沒複製。對照 `seq[::-1]`，那是 O(n) 時間**加上** O(n) 空間 |
| `enumerate(it)` / `zip(*its)` | 建立是 O(1) | O(1) | 惰性迭代器；O(n) 是由把它走完的那個迴圈付的 |
| `list(it)` / `set(it)` / `dict(it)` | O(n) | O(n) | 把迭代器實體化 |
| `abs()`、`divmod()`、`ord()`、`chr()` | O(1) | O(1) | 面試規模的整數就是單一機器字的運算 |
| `pow(a, b, mod)` | O(log b) | O(1) | 平方－相乘法。⚠️ `(a ** b) % mod` **不一樣** — 它會先把完整的 `a**b` 算出來 |
| `str(n)` / `int(s)` | O(d) | O(d) | `d` = 位數。對超大整數是超線性的（CPython 3.11+ 甚至預設上限 4300 位） |
| `bin(n)` / `hex(n)` | O(log n) | O(log n) | 每個 bit / nibble 一個字元 |

<!-- ad437811a690 -->
### `list` — 一個指標的動態陣列

CPython 的 list 是一塊連續的 `PyObject **` 記憶體，外加長度與容量。**不管裝什麼，每個元素都佔一個
指標寬度** — 這就是為什麼索引是 O(1)，也是為什麼往中間插入必須把後半段 `memmove` 一次。

| 操作 | 時間 | 空間 | 為什麼（底層） |
|---|---|---|---|
| `l[i]`、`l[i] = v` | **O(1)** | O(1) | 在那塊記憶體上做指標運算 |
| `l.append(x)` | **攤還 O(1)** | O(1) | 以幾何倍率預留空間（約 1.125×），所以重新配置雖是 O(n)，但只發生 O(log n) 次 |
| `l.pop()` | **O(1)** | O(1) | 丟掉最後一個指標 |
| `l.pop(0)`、`l.insert(i, x)`、`del l[0]` | **O(n)** | O(1) | 洞後面的東西全部 `memmove`。⚠️ 這就是經典的 BFS 效能 bug — 請用 `collections.deque` |
| `l.remove(x)` | O(n) | O(1) | 先線性找到它，再做上面那次搬移 |
| `x in l`、`l.index(x)`、`l.count(x)` | **O(n)** | O(1) | 逐一用 `==` 比對。⚠️ 這是最常見的「不小心變成 O(n²)」 |
| `l[i:j]`（切片） | O(k) | O(k) | 把 `k` 個指標複製到一個**新的** list |
| `l[::-1]` | O(n) | O(n) | 產生新 list。只是要走訪的話請用 `reversed(l)` |
| `l1 + l2` | O(n + m) | O(n + m) | 產生新 list。⚠️ 在迴圈裡用 `+=` 是 O(n²)；改用 `append` |
| `l * k` | O(n · k) | O(n · k) | ⚠️ `[[0] * n] * m` 會共用**同一個**列物件 — 見 [2D array initialization](#2d-array-matrix-initialization) |
| `l[:]` / `l.copy()` / `list(l)` | O(n) | O(n) | 淺複製 — 複製 n 個指標，不是複製物件本身 |
| `copy.deepcopy(l)` | O(全部節點) | O(全部節點) | 走遍整張物件圖，並用一個 memo dict 處理共用參考 |
| `l.sort()` | O(n log n) | O(n) | Timsort；**已排序的輸入是 O(n)**（它會偵測既有的遞增段 run） |
| `l.reverse()` | O(n) | O(1) | 原地交換指標 |

<!--CODE-->

<!-- 0c60834fe7e1 -->
### `str` — 一個不可變的碼位陣列

字串是不可變的，所以**每一次「修改」都是配置一個新字串並複製過去**。CPython 以精簡格式儲存
（PEP 393：依最大碼位決定每字元 1、2 或 4 bytes），所以 `s[i]` 仍然是 O(1) 索引，而不是走一遍 UTF-8。

| 操作 | 時間 | 空間 | 為什麼（底層） |
|---|---|---|---|
| `len(s)`、`s[i]` | **O(1)** | O(1) | 長度在標頭裡；碼元寬度固定 |
| `s1 + s2` | O(n + m) | O(n + m) | 配置新緩衝區，把兩邊 `memcpy` 過去。⚠️ 在迴圈裡 `s += ch` 是 **O(n²)** |
| `"".join(parts)` | **O(總長度)** | O(總長度) | 兩趟：先加總長度，**只配置一次**，再逐段 `memcpy`。這就是 `join` 贏過 `+=` 的原因 |
| `s[i:j]` | O(k) | O(k) | 新的字串緩衝區 |
| `s[::-1]` | O(n) | O(n) | 新字串 |
| `s[:i] + ch + s[i+1:]` | O(n) | O(n) | 「換掉一個字元」的慣用寫法會重建整個字串 — 做一次沒問題，放在迴圈裡就是 **O(n²)** |
| `sub in s`、`s.find`、`s.index` | 最壞 O(n · m)，一般約 O(n) | O(1) | 帶 Bloom filter 的 Boyer–Moore–Horspool；CPython 3.10+ 對較長的樣式改用 two-way 演算法（O(n + m)） |
| `s.replace(a, b)`、`s.count(a)` | 最壞 O(n · m) | O(n) | 先做上面的搜尋，再建一個新字串 |
| `s.split(sep)` | O(n) | O(n) | 掃一遍；結果所有片段加起來共 n 個字元 |
| `s.strip()`、`s.lstrip()` | 最壞 O(n) | O(n) | 只掃兩端，但仍然回傳新字串 |
| `s.lower()`、`s.upper()` | O(n) | O(n) | 新字串 |
| `sorted(s)` / `"".join(sorted(s))` | O(n log n) | O(n) | 字母異位詞 key 的慣用寫法（LC 49） |
| `s * k` | O(n · k) | O(n · k) | |
| `hash(s)` | 第一次 O(n)，之後 **O(1)** | O(1) | hash 要掃過所有 bytes，算完後會**快取在字串物件裡** |

<!--CODE-->

> CPython 確實有一個 `+=` 的原地最佳化（當字串的 refcount 是 1 時可以 `realloc` 而不必複製），
> 有時候會把 O(n²) 藏起來。**不要依賴它** — 只要有第二個名字指向這個字串就失效，而且 PyPy/Jython
> 上根本沒有。面試時就講 `join`。

<!-- 27206e624e23 -->
### `dict` 與 `set` — 開放定址的雜湊表

兩者都是開放定址表（碰撞時往下一個 slot 探測，沒有 bucket 串鏈）。3.6 之後的 dict 是*分離式*的：
一個稀疏的索引陣列，加上一個依插入順序排列的**緊密** `(hash, key, value)` 條目陣列 — 順序保證與
省記憶體都是從這裡來的。

| 操作 | 時間（平均） | 時間（最壞） | 空間 | 為什麼（底層） |
|---|---|---|---|---|
| `d[k]`、`k in d`、`d.get(k)` | **O(1)** | O(n) | O(1) | hash → slot → 探測。最壞情況是每個 key 都碰撞（對抗式輸入） |
| `d[k] = v`、`s.add(x)` | **攤還 O(1)** | O(n) | O(1) | 超過負載係數就重新配置（dict 是 2/3 滿，set 是 3/5）— 重新配置會**把所有 key 重新 hash**，O(n) |
| `del d[k]`、`s.discard(x)` | **O(1)** | O(n) | O(1) | 會留下一個 *dummy* 標記，免得後面的探測鏈斷掉 |
| `hash(key)` | O(key 的長度) | — | O(1) | ⚠️ 對**字串或 tuple 當 key** 而言 hash 是 O(len) 而非 O(1) — 用長度 L 的字串當 key，每次操作就是 O(L) |
| 走訪 `d` / `d.items()` | O(n) | — | O(1) | 走的是緊密條目陣列（view 本身的建立是 O(1)） |
| 走訪 `set` | O(容量) | — | O(1) | set **沒有**緊密陣列 — 走訪時連空 slot 也要走過 |
| `d.keys() \| other`、set 聯集 `a \| b` | O(n + m) | — | O(n + m) | |
| `a & b`（交集） | **O(min(n, m))** | — | O(min) | 走訪*比較小*的那個集合，到大的那個去探測 |
| `a - b`（差集） | O(n) | — | O(n) | |
| `a ^ b`（對稱差） | O(n + m) | — | O(n + m) | |
| `Counter(it)`、`collections.defaultdict` | O(n) | — | O(n) | 同一種表，每個元素插入一次 |
| `max(d, key=d.get)` | O(n) | — | O(1) | 整個掃一遍 — 雜湊表裡沒有「最大 key」的捷徑 |

> **面試用的一句話版本**：雜湊表是用 O(n) 空間換來 O(1) 的成員查詢。把 `x in nums`（list，O(n)）
> 換成 `x in set(nums)`（O(1)），就是多數暴力 O(n²) 解法塌縮成 O(n) 的關鍵 — 而 `set(nums)`
> 本身的建立也是 O(n)。

<!-- 33af96d90602 -->
### 其他值得知道的容器

| 型別 | 操作 | 時間 | 為什麼 |
|---|---|---|---|
| `tuple` | `t[i]`、`len(t)` | O(1) | 和 list 同樣的排版，只是沒有多餘容量 — 不可變，所以可雜湊 |
| `collections.deque` | `append` / `appendleft` / `pop` / `popleft` | **O(1)** | 由 64 格區塊串成的雙向鏈結串列 — 這就是 `list.pop(0)` 的解法 |
| `collections.deque` | 中間的 `dq[i]` | **O(n)** | 必須一塊一塊走過去 — deque 不是隨機存取的 |
| `heapq` | `heappush` / `heappop` | O(log n) | 在一個普通 list 上做二元堆積的上浮／下沉 |
| `heapq` | `heapify(l)` | **O(n)** | 由下而上下沉 — 不是 `n` 次 push。`nlargest(k, it)` 是 O(n log k) |
| `bisect` | `bisect_left` / `bisect_right` | O(log n) | 在已排序 list 上做二分搜尋 |
| `bisect` | `insort` | **O(n)** | 找位置 O(log n)，但**搬移後半段是 O(n)** — 這是陷阱 |
| `int` | `a + b`、`a - b` | O(d) | 任意精度：以 30-bit 為一「位」，共 d 位。面試規模的整數視為 O(1) |
| `int` | `a * b` | O(d²) | 直式乘法，約 70 位以上改用 Karatsuba。階乘／大次方類的題目才會有感 |
| 任何序列 | `a == b` | O(n) | 先做 O(1) 的同一性與長度短路，再逐一元素比較 |

<!-- 9953fd5dcf43 -->
### 真的會在面試中扣分的陷阱

<!--CODE-->

**要講出口的話**：先講容器，再講成本。*「`seen` 是一個 set，所以成員查詢平均是 O(1) — 這讓整個
迴圈是 O(n) 時間、O(n) 空間。」* 講出容器是什麼，才讓你的複雜度宣稱是可被檢驗的，而這正是
面試官在等的那一句。

> 想看資料結構與經典演算法層級的同一份查表，見 [complexity_cheatsheet.md](./complexity_cheatsheet.md)；
> `heapq` / `bisect` / `collections` 的深入說明見
> [python_trick_stdlib.md](./python_trick_stdlib.md)。

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

<!-- 3407770955c2 -->
### `split`

<!--CODE-->

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

<!-- 9f48099ab509 -->
### `math.ceil`

<!--CODE-->

<!-- abd83264c9dd -->
### `math.floor`

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

<!-- 6d12f36a86b9 -->
### `any()`

<!--CODE-->

<!-- bdaad4cd6785 -->
### `not` 邏輯

<!--CODE-->

<!-- 0dee84a2fa71 -->
### `enumerate()`

<!--CODE-->

<!-- bedb789be80b -->
### `zip()`

<!--CODE-->

<!-- 149cf6fd7e68 -->
### 走訪 dict

<!--CODE-->

<!-- 532a42670c53 -->
### 星號（`*`）運算式

<!--CODE-->

<!-- 93cb23fc55d3 -->
### `filter()`

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

<!-- b6913cbd820c -->
### `eval()`

<!--CODE-->

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

<!-- f0ccda41a8fd -->
# Python 操作複雜度

> **範圍** — 每一個 Python 內建與容器操作實際上要花多少成本，以及為什麼：內建函式、`list`、`str`、`dict`/`set`、`tuple`、`deque`、`heapq`、`bisect` 與 `int` 的逐項查表，每個成本背後的 CPython 記憶體排版，以及那七個會悄悄把 O(n) 解法變成 O(n²) 的寫法。
> **另見**：[python_trick.md](./python_trick.md) — 這些成本所規範的 Python 慣用寫法；[complexity_cheatsheet.md](./complexity_cheatsheet.md) — 同一份 Big-O 查表，但對象是資料結構與經典演算法而非 Python 內建；[python_trick_stdlib.md](./python_trick_stdlib.md) — `heapq`、`bisect`、`collections` 以及標準函式庫其餘部分的深入說明；[time_space_complexity.md](./time_space_complexity.md) — 論證一整個 LC 解法（而非單一呼叫）的複雜度；[java_trick_collections.md](./java_trick_collections.md) — 同一片領域的 Java 版。

<!-- c2e966b4e731 -->
## LeetCode 題目清單

- [Python](https://leetcode.com/problemset/all/?languageTags=python3)

<!-- 40cb94ffc000 -->
## 總覽

**這份速查表為什麼存在**：幾乎每一次「你的解法是 O(n²)，能更快嗎？」的時刻，都來自把某個內建操作
當成了 O(1)。`len()` 確實是免費的；`x in my_list`、`res += ch` 和 `arr[1:]` 都不是。

`n` = 被操作物件的大小，`m` = 另一個運算元的大小，`k` = 切片的大小。

| 我想知道…的成本 | 前往 |
|---|---|
| `len`、`max`、`sum`、`sorted`、`reversed`、`pow`、`int`/`str` 轉換 | [內建函式](#built-in-functions) |
| 索引、`append`、`pop(0)`、`in`、切片、`+=`、複製、排序一個 list | [`list`](#list--a-dynamic-array-of-pointers) |
| 串接、`join`、切片、`find`、`split`、`replace`、`hash` | [`str`](#str--an-immutable-array-of-code-points) |
| `dict` 或 `set` 的查詢、插入、刪除、走訪，以及 `\|` `&` `-` `^` | [`dict` 與 `set`](#dict-and-set--open-addressing-hash-tables-) |
| `tuple`、`deque`、`heapq`、`bisect`、大整數運算、`==` | [其他容器](#other-containers-worth-knowing) |
| 那七個會默默多吃一個成長級數的寫法 | [陷阱](#the-traps-that-actually-cost-interview-points-) |

<!-- 995cac967980 -->
## 內建函式

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

<!-- 8bb085f315fa -->
## `list` — 一個指標的動態陣列

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
| `l1 + l2` | O(n + m) | O(n + m) | 產生新 list。⚠️ 在迴圈裡寫 `l = l + chunk` 是 **O(n²)** — 每次都重建一份。`l += chunk` *不是*同一個操作：它會呼叫 `extend`，原地修改，攤還 O(k) |
| `l * k` | O(n · k) | O(n · k) | ⚠️ `[[0] * n] * m` 會共用**同一個**列物件 — 見 [2D array initialization](./python_trick.md#2d-array-matrix-initialization) |
| `l[:]` / `l.copy()` / `list(l)` | O(n) | O(n) | 淺複製 — 複製 n 個指標，不是複製物件本身 |
| `copy.deepcopy(l)` | O(全部節點) | O(全部節點) | 走遍整張物件圖，並用一個 memo dict 處理共用參考 |
| `l.sort()` | O(n log n) | O(n) | Timsort；**已排序的輸入是 O(n)**（它會偵測既有的遞增段 run） |
| `l.reverse()` | O(n) | O(1) | 原地交換指標 |

<!--CODE-->

<!-- 5e3d2f083620 -->
## `str` — 一個不可變的碼位陣列

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

<!-- 752e2ff06ec8 -->
## `dict` 與 `set` — 開放定址的雜湊表 ⭐⭐⭐⭐

兩者都是開放定址表（碰撞時往下一個 slot 探測，沒有 bucket 串鏈）。3.6 之後的 dict 是*分離式*的：
一個稀疏的索引陣列，加上一個依插入順序排列的**緊密** `(hash, key, value)` 條目陣列 — 順序保證與
省記憶體都是從這裡來的。

| 操作 | 時間（平均） | 時間（最壞） | 空間 | 為什麼（底層） |
|---|---|---|---|---|
| `d[k]`、`k in d`、`d.get(k)` | **O(1)** | O(n) | O(1) | hash → slot → 探測。最壞情況是每個 key 都碰撞（對抗式輸入） |
| `d[k] = v`、`s.add(x)` | **攤還 O(1)** | O(n) | O(1) | 超過負載係數就重新配置（dict 是 2/3 滿，set 是 3/5）— 重新配置會**把所有 key 重新 hash**，O(n) |
| `del d[k]`、`s.discard(x)` | **O(1)** | O(n) | O(1) | 會留下一個 *dummy* 標記，免得後面的探測鏈斷掉 |
| `hash(key)` | 快取後 **O(1)** | 第一次 O(len) | O(1) | hash 要掃過整個 key，但 `str` 算完後會把它**快取**在物件裡 — 所以只有*第一次*對新字串取 hash 才是 O(L)，不是每次查詢都付。⚠️ `tuple` 在 CPython 3.14 之前沒有這個快取，在那些版本上用長度 L 的 tuple 當 key，**每次**操作都是 O(L) |
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

<!-- 3312efbe5861 -->
## 其他值得知道的容器

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

<!-- b154ac900e0b -->
## 真的會在面試中扣分的陷阱 ⭐⭐⭐⭐⭐

<!--CODE-->

**要講出口的話**：先講容器，再講成本。*「`seen` 是一個 set，所以成員查詢平均是 O(1) — 這讓整個
迴圈是 O(n) 時間、O(n) 空間。」* 講出容器是什麼，才讓你的複雜度宣稱是可被檢驗的，而這正是
面試官在等的那一句。

> 想看資料結構與經典演算法層級的同一份查表，見 [complexity_cheatsheet.md](./complexity_cheatsheet.md)；
> 這些成本所規範的慣用寫法見 [python_trick.md](./python_trick.md)；`heapq` / `bisect` /
> `collections` 的深入說明見 [python_trick_stdlib.md](./python_trick_stdlib.md)。

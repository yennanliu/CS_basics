<!-- bf4cd2c157d4 -->
# Python FAQ

> **範圍** — Python 語言本身：物件模型、可變性、函式與裝飾器、迭代器、OOP、記憶體管理、型別註記、打包，以及面試官真的會探的那些坑。
> **另見**：[`faq_python_concurrency.md`](./faq_python_concurrency.md) — GIL、
> 執行緒與行程的取捨，以及 `asyncio`。

答案以 **Python 3.10+** 為準。版本有差異的地方會特別註明。

---

<!-- 68dfca3e5b32 -->
## 1) 物件模型 ⭐⭐⭐⭐⭐

<!-- fc40c8d46646 -->
### 名字是繫結，不是盒子

Python 的變數是**繫結到某個物件的名字**，不是裝著值的記憶體盒子。
賦值是重新繫結那個名字；它從來不複製物件。

<!--CODE-->

這就是為什麼「Python 是傳值還是傳參考？」只有一個正確答案：
**傳物件參考（pass-by-object-reference）**，也叫 pass-by-assignment。函式收到的是一個
繫結到呼叫端那個物件的新名字 —— 改動它，呼叫端看得到；
重新繫結它，呼叫端看不到。

<!--CODE-->

<!-- 86d5988fa3c7 -->
### `is` 與 `==`

| 運算子 | 問的是 | 背後靠 |
|----------|------|-----------|
| `==` | 同一個**值**？ | `__eq__` |
| `is` | 同一個**物件**（同一個 `id()`）？ | 身分 |

`is` 只用在單例上：`x is None`、`x is True`、`x is NotImplemented`。

<!--CODE-->

> **坑**：`if x == None` 能跑但寫法不對；`is None` 更快，而且不會被自訂的 `__eq__` 騙過去。

<!-- 44a7ac083739 -->
### 可變與不可變

| 不可變 | 可變 |
|-----------|---------|
| `int`、`float`、`bool`、`str`、`bytes`、`tuple`、`frozenset`、`range` | `list`、`dict`、`set`、`bytearray`，以及大多數自訂類別 |

**只有可雜湊的物件才能當 `dict` 的 key 或 `set` 的成員**，因為雜湊要求那個值永遠不變。
一個裝著 `list` 的 `tuple` 本身就是不可雜湊的。

<!-- 39e806c16a4b -->
### 真假值（Truthiness）

`if x:` 會呼叫 `__bool__`，沒有的話退回 `__len__`。假值有：`False`、`None`、`0`、`0.0`、
`""`、`[]`、`{}`、`set()`、`()`。其他都是真值。

<!--CODE-->

---

<!-- 6c5b8aca6a3f -->
## 2) 可變性的坑 ⭐⭐⭐⭐⭐

<!-- 71ef6950ca5a -->
### 可變的預設參數

預設值是在**函式定義時求值一次**，所以每次呼叫都共用同一個。

<!--CODE-->

<!-- 27e9b74abcec -->
### 淺複製與深複製

<!--CODE-->

`deepcopy` 能處理環，但很慢；最好是重新設計結構，讓你根本不需要它。

<!-- fa7349e0e006 -->
### 重複一個可變物件

<!--CODE-->

`[0] * 3` 沒問題，因為 `int` 是不可變的 —— 別名共用只有在可變物件上才咬人。

<!-- a72d78fffe3e -->
### 可變的類別屬性

<!--CODE-->

---

<!-- 3e191519afd6 -->
## 3) 函式 ⭐⭐⭐⭐

<!-- c1ff0b67daf4 -->
### 參數

<!--CODE-->

- `/` 之前的全部是**只能位置傳遞**（3.8+）。
- `*` 或 `*args` 之後的全部是**只能關鍵字傳遞**。
- `*args` 把多出來的位置參數收成 tuple；`**kwargs` 把多出來的關鍵字參數收成 dict。
- 在呼叫端，`*` 與 `**` 是**展開**：`f(*seq, **mapping)`。

<!-- e94c4d993938 -->
### 閉包與延遲繫結

閉包捕獲的是**變數**，不是捕獲當下的值。

<!--CODE-->

`nonlocal` 重新繫結外層函式作用域裡的名字；`global` 則是模組層級。

<!-- ad8f79434ae9 -->
### 作用域：LEGB

名稱查找依序走 **L**ocal → **E**nclosing → **G**lobal → **B**uiltins。在函式裡任何地方
對一個名字賦值，就會讓它*在整個函式裡*都是區域變數：

<!--CODE-->

<!-- ed16736b050b -->
### 裝飾器

裝飾器接受一個函式並回傳替代品。`@deco` 就等於
`f = deco(f)`。

<!--CODE-->

**一定要用 `functools.wraps`** —— 少了它，被包裝的函式會失去名字、
docstring 與簽章，於是 logging、`help()`，以及任何會內省 handler 的框架都會壞掉。

**帶參數的**裝飾器多一層 —— 一個回傳裝飾器的函式：

<!--CODE-->

好用的內建裝飾器：`@functools.lru_cache` / `@functools.cache`（memoization）、
`@property`、`@staticmethod`、`@classmethod`、`@dataclasses.dataclass`、
`@functools.singledispatch`、`@contextlib.contextmanager`。

---

<!-- 36a00d26780e -->
## 4) 迭代器與產生器 ⭐⭐⭐⭐⭐

<!-- da8bb6a96b10 -->
### 兩個協定

- **Iterable（可迭代物件）**：有 `__iter__()` 並回傳一個迭代器（`list`、`dict`、`str`、檔案……）。
- **Iterator（迭代器）**：有 `__next__()`，而且 `__iter__()` 回傳自己。耗盡時丟出
  `StopIteration`。**迭代器只能被消費一次。**

<!--CODE-->

「為什麼 `open(f)` 我只能讀一次？」的答案就在這裡：檔案物件*本身*就是它各行的
迭代器，而它的位置就是狀態。要重來就呼叫 `f.seek(0)`，或者需要多次走訪就先讀進 list。

<!-- b8f016388bbd -->
### 產生器

帶 `yield` 的函式回傳一個產生器：它**延遲**計算，一次產生一個值，
並在兩次 `next()` 之間保住自己的區域狀態。

<!--CODE-->

| | List | 產生器 |
|---|------|-----------|
| 記憶體 | 所有元素都留著 | 一次一個元素 |
| `len()` | 可以 | 不行 |
| 重複迭代 | 可以 | 不行 —— 走完一次就耗盡 |
| 怎麼建 | `[x for x in it]` | `(x for x in it)`，或 `yield` |
| 什麼時候用 | 需要索引／多次走訪 | 大量或無窮的資料流、管線 |

<!--CODE-->

`yield from sub_generator()` 把工作委派給另一個產生器（也會轉送 `send`/throw）。

<!-- fab2bbbdbf8d -->
### 把產生器當協程用

`gen.send(value)` 恢復一個產生器，讓 `yield` 的求值結果是 `value` —— 這就是
`asyncio` 最早建立在上面的機制。見
[`faq_python_concurrency.md`](./faq_python_concurrency.md)。

---

<!-- 789f16fc0faa -->
## 5) 綜合運算式（Comprehension）⭐⭐⭐⭐

<!--CODE-->

- 綜合運算式有**自己的作用域**（Python 3）：迴圈變數不會洩漏出來。
- 它比顯式的 `append` 迴圈快（不用每個元素都查一次屬性），但如果一個綜合運算式的
  唯一目的是產生副作用，那就該寫成普通的 `for` 迴圈。
- **海象運算子**（`:=`，3.8+）讓你重複使用算好的值：
  `[y for x in data if (y := f(x)) is not None]`。

---

<!-- e8b4c97b37df -->
## 6) Python 的 OOP ⭐⭐⭐⭐

<!-- 19d089695938 -->
### 方法

<!--CODE-->

用 `@property` 把一個屬性變成計算出來的，而**不必改動呼叫端** ——
這就是為什麼 Python 不需要一堆 getter/setter 樣板碼。

<!-- a16e241ff517 -->
### 值得記住的 dunder 方法

| 方法 | 支撐什麼 |
|--------|--------|
| `__repr__` | `repr(x)`、除錯器與 REPL —— 要明確無歧義，給開發者看 |
| `__str__` | `str(x)`、`print` —— 要好讀，給使用者看（沒有就退回 `__repr__`） |
| `__eq__` + `__hash__` | `==`、`set`/`dict` 的成員判斷 —— **要一起**定義 |
| `__len__`、`__getitem__`、`__contains__` | `len()`、`x[i]`、`in` |
| `__iter__`、`__next__` | `for` 迴圈 |
| `__enter__`、`__exit__` | `with` 區塊 |
| `__call__` | 讓實例可以被呼叫 |
| `__lt__`（+ `functools.total_ordering`） | 排序、`heapq` |

> 定義了 `__eq__` 會讓 `__hash__` 變成 `None`（不可雜湊），除非你也一起定義
> `__hash__` —— 和 Java 的 `equals`/`hashCode` 是同一份契約。

<!-- dc3b2bce1f9f -->
### `__init__` 與 `__new__`

`__new__` **建立**實例（很少需要覆寫 —— 單例、不可變型別的子類別）；
`__init__` **初始化**那個已經被建好的實例。

<!-- d397b6d1d736 -->
### 繼承、MRO 與 `super()`

Python 允許多重繼承；由 **MRO**（方法解析順序，C3 線性化）決定用哪一個實作。
`super()` 跟著 MRO 走，不是跟著「父類別」走。

<!--CODE-->

協作式的多重繼承只有在鏈上的**每一個**類別都呼叫
`super().__init__(...)` 時才成立。

<!-- 7c71f38b7315 -->
### 鴨子型別、ABC 與 Protocol

Python 依行為分派，不依宣告的型別（「會叫得像鴨子就是鴨子」）。要把契約
寫明的話：

<!--CODE-->

<!-- 021a155fc318 -->
### 裝資料的類別

<!--CODE-->

`@dataclass` 會生成 `__init__`、`__repr__`、`__eq__`（加上 `order=True` 還有比較）。
注意 `frozen=True` 是**淺層**的：它擋掉 `p.x = 1`，但可變的欄位還是可以就地被改動，
而且對它取雜湊會丟 `TypeError` —— 所以要凍結就要搭配不可變的欄位型別。替代方案：
`NamedTuple`（不可變、像 tuple、輕量）、`enum.Enum`（一組封閉的常數）、
`TypedDict`（形狀固定的 dict）。

<!-- 3f56abc59faf -->
### `__slots__`

宣告 `__slots__ = ("x", "y")` 會移除每個實例的 `__dict__`：更省記憶體、
屬性存取更快，代價是不能再動態加屬性。要建幾百萬個小物件時很值得。

---

<!-- 1fb6f066ad74 -->
## 7) 情境管理器 ⭐⭐⭐

`with` 保證就算丟例外也會收尾 —— 這是 Python 版的 try-with-resources。

<!--CODE-->

`__exit__(exc_type, exc, tb)` 回傳 `True` 會**吞掉**例外 —— 除非壓下例外就是目的，
否則請回傳 `False`/`None`。`contextlib.suppress`、`ExitStack` 與 `closing` 涵蓋了常見情況。

---

<!-- c00f5d5589d6 -->
## 8) 錯誤與例外 ⭐⭐⭐⭐

<!-- aa1df57f7b3c -->
### EAFP 優於 LBYL

Python 偏好「請求原諒比請求許可容易」（先做，再處理失敗），
而不是「跳之前先看」—— 後者有競態，而且在正常路徑上更慢。

<!--CODE-->

<!-- e848b90efc69 -->
### 完整語句

<!--CODE-->

<!-- 14654b46adca -->
### Review 時會被抓的規則

- **永遠不要用裸的 `except:`** —— 它連 `KeyboardInterrupt` 與 `SystemExit` 都會接住。
  最糟也要用 `except Exception:`。
- 只接住你真的處理得了、範圍**最窄**的那個例外。
- `raise X from e` 保留了原因；在 `except` 裡裸寫 `raise X` 也會隱含地串起來，
  但寫明的形式把意圖也寫進去了。
- 在 `finally` 裡 `return` 會**吞掉**正在往外傳的例外 —— 不要這樣寫。
- 自訂例外：繼承 `Exception`（不是 `BaseException`），每個套件一個基底類別，
  讓呼叫端可以接住整個家族。

`ExceptionGroup` 與 `except*`（3.11+）用來處理同時被丟出的多個例外 ——
也就是 `asyncio.TaskGroup` 丟出來的那種形狀。

---

<!-- f126c9bc7599 -->
## 9) 記憶體管理與 GC ⭐⭐⭐⭐

CPython 用的是**參考計數**加上一個**環收集器**：

1. 每個物件都有參考計數；計數歸零時**立刻**被釋放 ——
   這就是為什麼 CPython 的記憶體用量可預測，而 `with` 區塊感覺很即時。
2. 參考環（`a.b = b; b.a = a`）永遠不會歸零，所以有一個**分代 GC**
   （三個世代，最常掃描年輕物件）負責找出它們並釋放。

<!--CODE-->

**常見的洩漏** —— 那些一直可達的物件：

- 模組層級的快取與只會長大的 list（改用 `functools.lru_cache(maxsize=…)`）；
- 註冊了 callback／observer 卻從來不解除註冊（用 `weakref`）；
- 長期保存例外的 traceback（它抓著每一個 frame 的區域變數）；
- 環裡的物件定義了 `__del__` 以前會讓它們無法被回收（3.4 起已修正，但
  `__del__` 的時機依然不可預測 —— 優先用情境管理器）。

其他要點：CPython 從**arena/pool**（`pymalloc`）配置小物件，而且可能不會把釋放的記憶體
還給作業系統；像識別字那樣的字串會被 intern，所以相同的字面值共用同一個物件；
`sys.intern` 對雜湊繁重的工作負載可能有幫助。

---

<!-- c00de2e5fdcf -->
## 10) 型別註記 ⭐⭐⭐

註記在**執行期不會被強制檢查** —— 它們是給讀者、IDE 與檢查工具
（`mypy`、`pyright`）看的。

<!--CODE-->

- 優先用內建泛型（`list[str]`、`dict[str, int]`），不要用 `typing.List`（3.9+）。
- 為**公開的**函式簽章與 dataclass 加註記；顯而易見的區域變數就別加了。
- `from __future__ import annotations` 讓註記變成延遲求值的字串，解決前向參考與
  循環匯入的痛。
- `Any` 等於關掉檢查 —— 把它當成一個 TODO。

---

<!-- 0a6e5b6e2bbc -->
## 11) 標準函式庫工具帶 ⭐⭐⭐⭐

這些模組能把 20 行的答案變成 5 行（在 coding 回合裡也一直出現）：

| 模組 | 什麼時候拿出來用 |
|--------|-------------------|
| `collections` | `defaultdict`（自動初始化的桶）、`Counter`（次數統計 + `most_common`）、`deque`（兩端都是 **O(1)** 的新增與彈出 —— BFS 佇列的正確選擇）、`OrderedDict`（`move_to_end`、LRU） |
| `heapq` | Top-K、優先佇列。只有最小堆 —— 要最大堆就 push `-x` 或 `(-key, item)` |
| `bisect` | 在已排序的 list 上做二分搜尋：`bisect_left`、`insort` |
| `itertools` | `product`、`permutations`、`combinations`、`groupby`（輸入要先排序！）、`accumulate`、`islice`、`chain`、`pairwise` |
| `functools` | `cache`/`lru_cache`（一行就把 DP memoize 掉）、`reduce`、`partial`、`cmp_to_key` |
| `dataclasses`、`enum`、`typing` | 建模 |
| `pathlib`、`json`、`csv`、`datetime`、`re`、`logging` | 日常的水電工程 |

<!--CODE-->

<!-- 77b12f5456c6 -->
### 排序

<!--CODE-->

Python 的排序是 **Timsort**：`O(n log n)`、**穩定** —— 所以你可以先按次要 key 排，
再按主要 key 排，相同的會維持先前的順序。

---

<!-- 0dd315701279 -->
## 12) 字串、位元組與格式化 ⭐⭐⭐

- `str` 是一串 **Unicode code point**；`bytes` 是原始位元組。要明確轉換：
  `s.encode("utf-8")` / `b.decode("utf-8")`。以文字模式開啟的檔案會幫你解碼；
  `"rb"` 不會。
- 字串是不可變的，所以在迴圈裡 `s += x` 是 **O(n²)**。改成把片段收進 list 再
  `"".join(parts)`。
- f-string 是預設做法（`f"{name!r} scored {score:.2f}"`）；`f"{x=}"`（3.8+）會印出
  `x=值`，是這個語言裡最快的除錯工具。
- `str.find` 找不到時回傳 `-1`；`str.index` 會丟例外。兩者都從左往右掃。

<!--CODE-->

---

<!-- f0a1efbfc584 -->
## 13) 模組、匯入與打包 ⭐⭐⭐

- 一個模組在第一次被匯入時執行**一次**，之後快取在 `sys.modules` 裡。
- `if __name__ == "__main__":` 守住那些只該在檔案被直接執行時才跑的程式碼。
  在 `spawn` / `forkserver` 的啟動方式下（macOS 與 Windows 的預設），子行程會
  重新匯入主模組，所以**啟動行程或行程池的程式碼一定要放在這個守衛後面**，
  否則就會無限遞迴。
- 優先用**絕對匯入**（`from myapp.db import conn`）。循環匯入通常代表分層有問題；
  局部的解法是把匯入搬進函式裡。
- 環境：標準工具是 `python -m venv .venv`；`uv` / `poetry` /
  `pip-tools` 多了 lockfile。應用程式要鎖死依賴版本，函式庫則保留範圍。
- `pyproject.toml` 是現代的打包描述檔（`setup.py` 是舊時代的）；
  `pip install -e .` 以可編輯模式安裝本地專案。

---

<!-- b9574cbae055 -->
## 14) 測試 ⭐⭐⭐

<!--CODE-->

patch 要打在物件**被查找**的地方，不是它被定義的地方：
`mock.patch("myapp.service.requests.get")`，不是 `mock.patch("requests.get")`。

---

<!-- 16a1f8cc1339 -->
## 15) 效能 ⭐⭐⭐

先量測：微基準用 `timeit`，整個程式用 `cProfile`，記憶體用 `tracemalloc`。

| 成本 | 修法 |
|------|-----|
| `x in list` 是 `O(n)` | 改用 `set`/`dict` —— 平均 `O(1)` |
| `list.insert(0, x)` / `pop(0)` 是 `O(n)` | `collections.deque` |
| 迴圈裡的字串 `+=` 是 `O(n²)` | `"".join(parts)` |
| 每個元素都在 Python 層做事 | 把迴圈推進 C：內建函式、綜合運算式、`map`、NumPy/pandas |
| 重複的純函式呼叫 | `functools.cache` |
| 熱迴圈裡的屬性／全域查找 | 先綁成區域變數（`append = out.append`） |
| 大量的小物件 | `__slots__`，或改用陣列／NumPy |

CPU 密集的程式碼不會因為開執行緒而變快，因為有 GIL —— 見
[`faq_python_concurrency.md`](./faq_python_concurrency.md)。

---

<!-- e5283622c657 -->
## 16) 版本重點

| 版本 | 值得知道的 |
|---------|---------------|
| 3.6 | f-string、`dict` 有序（當時只是實作細節） |
| 3.7 | `dict` 插入順序**正式保證**、dataclass、`breakpoint()` |
| 3.8 | 海象 `:=`、只能位置傳遞的 `/`、`f"{x=}"`、`functools.cached_property` |
| 3.9 | 內建泛型 `list[int]`、用 `\|` 合併 dict |
| 3.10 | `match` 語句、`X \| Y` 聯集型別、錯誤訊息更好 |
| 3.11 | 大幅加速、`ExceptionGroup`/`except*`、`asyncio.TaskGroup`、`tomllib` |
| 3.12 | f-string 更乾淨、`type` 別名語法、per-interpreter GIL 的前置工作 |
| 3.13 | 實驗性的 free-threaded（無 GIL）版本、JIT 的前置工作 |

Python 2 已經死了；如果你看到 `xrange`、當成語句用的 `print`，或 `dict.iteritems()`，
那就是 Python 2 的程式碼 —— Python 3 的對應物是 `range`（本來就是延遲的）、
`print()` 與 `dict.items()`。

---

<!-- 2925cbd11ca4 -->
## 17) 快問快答

**Q：`list` 和 `tuple` 差在哪？**
可變與不可變。tuple 是可雜湊的 —— 因此可以當 dict 的 key —— **前提是它裝的每一個元素
都可雜湊**（`hash(([],))` 會丟 `TypeError`）。tuple 也稍微小一點、快一點，而且它在語意上
表達「一筆固定的紀錄」；list 表達的是「一個會變動的同質集合」。

**Q：`dict` 是怎麼實作的？**
一個用開放定址的雜湊表，外加一個緊湊的索引陣列（3.6+），這就是為什麼迭代順序
和插入順序一致。查找平均 `O(1)`，最差 `O(n)`；大約滿到三分之二時會擴容。

**Q：`range` 會建一個 list 嗎？**
不會。`range` 是一個延遲的序列物件：記憶體 `O(1)`，而且支援 `len`、索引與
`in`（最後這個是 `O(1)`，因為它只是一個算術檢查）。

**Q：`if __name__ == "__main__"` 是做什麼的？**
只有當檔案被當成腳本執行時，`__name__` 才是 `"__main__"`，所以模組被匯入時
那段被守住的程式碼就會被跳過。

**Q：`staticmethod` 和 `classmethod` 差在哪？**
`classmethod` 會收到類別（`cls`）—— 用在替代建構子，以及任何必須尊重子類別的地方。
`staticmethod` 什麼都不收 —— 它就是一個被放在類別命名空間裡的普通函式。

**Q：淺複製與深複製？**見 §2。

**Q：什麼是 monkey patching？**
在執行期替換掉某個模組或類別的屬性。在測試裡是正當做法（`mock.patch`）；
在正式環境裡它會讓行為變得無法追蹤。

**Q：`@lru_cache` —— 什麼情況會壞？**
參數必須可雜湊、被快取的函式必須是純的，而在實例方法上用無上限的快取
（`maxsize=None`）會讓每一個 `self` 都活著 —— 經典的洩漏。

**Q：為什麼 `0.1 + 0.2 != 0.3`？**
二進位浮點數（IEEE 754）沒辦法精確表示那些十進位小數。要比較就用
`math.isclose`，金額請用 `decimal.Decimal`。

**Q：什麼是 GIL？**
見 [`faq_python_concurrency.md`](./faq_python_concurrency.md) §1。

---

<!-- 32edb6127977 -->
## 18) 重點檢查表

<!--CODE-->

---

<!-- bdd3eeb7fae5 -->
## 參考資料

- [Python 官方文件 —— 資料模型](https://docs.python.org/3/reference/datamodel.html)
- [Python 官方文件 —— 標準函式庫](https://docs.python.org/3/library/)
- [`faq_python_concurrency.md`](./faq_python_concurrency.md) — GIL、執行緒、行程、asyncio
- [`faq_software_runtime.md`](./faq_software_runtime.md) — 行程、記憶體佈局、JIT 與直譯

# Python 技巧與慣用寫法

> **範圍** — 解題過程中會用到的 Python 語言慣用寫法 — 複製、字串處理、排序 key、整數運算、生成式(comprehension)與作用域 — 依「你想做什麼」來分組。標準函式庫與索引運算另有專屬速查表。
> **另見**：[python_trick_stdlib.md](./python_trick_stdlib.md) — `heapq`、`bisect`、`SortedDict`、`collections`、`itertools`、`functools`；[python_trick_indexing.md](./python_trick_indexing.md) — 插入、切片(slicing)以及背後的差一(off-by-one)運算；[python_gotchas.md](./python_gotchas.md) — 那些「令人意外」而不只是「好用」的行為；[java_trick.md](./java_trick.md) — 同一片領域的 Java 版。

## LeetCode 題目清單

- [Python](https://leetcode.com/problemset/all/?languageTags=python3)

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


## 複製與參考

### 賦值 vs 淺複製 vs 深複製

- https://www.runoob.com/w3cnote/python-understanding-dict-copy-shallow-or-deep.html
- https://iter01.com/578999.html
- 複製的種類：深複製(deep copy)、淺複製(shallow copy)、參考複製(reference copy)
```text
# LC 138

#-------------------------------------------------
# CASE 1) assignment : point to the same instance
#-------------------------------------------------

In [112]: z = [1,2,3]

In [113]: x = y = z

In [114]: x
Out[114]: [1, 2, 3]

In [115]: y
Out[115]: [1, 2, 3]

In [116]: z
Out[116]: [1, 2, 3]

In [117]: z.append(4)

In [118]: z
Out[118]: [1, 2, 3, 4]

In [119]: x
Out[119]: [1, 2, 3, 4]

In [120]: y
Out[120]: [1, 2, 3, 4]

In [121]: z
Out[121]: [1, 2, 3, 4]


#-------------------------------------------------
# CASE 2) shallow copy : copy "parent" instance, but NOT sub instance
#-------------------------------------------------

# https://docs.python.org/zh-tw/3/tutorial/datastructures.html
# form 1
a.copy()

# form 2
a[:]

# demo
In [90]: x = [1,2,3]

In [91]: y = x[:]

In [92]: y
Out[92]: [1, 2, 3]

In [93]: x.append(4)

In [94]: x
Out[94]: [1, 2, 3, 4]

In [96]: y
Out[96]: [1, 2, 3]


# LC 77 Combinations
class Solution(object):
    def combine(self, n, k):
        result = []
        
        def dfs(current, start):
            if(len(current) == k):
                result.append(current[:])
                return
            
            for i in range(start, n + 1):
                current.append(i)
                dfs(current, i + 1)
                current.pop()
            
        dfs([], 1)
        return result

#-------------------------------------------------
# CASE 3) deep copy : copy "parent" instance, AND sub instance
#-------------------------------------------------

import copy

In [25]: import copy
    ...:
    ...: x = [1,2,3]
    ...: z =  copy.deepcopy(x)
    ...:
    ...: x
Out[25]: [1, 2, 3]

In [26]:

In [26]: z
Out[26]: [1, 2, 3]

In [27]: x.append(4)

In [28]: x
Out[28]: [1, 2, 3, 4]

In [29]: z
Out[29]: [1, 2, 3]

In [31]: z.append(5)

# NOTE : x, z NOT affect on each other

In [32]: z
Out[32]: [1, 2, 3, 5]

In [33]: x
Out[33]: [1, 2, 3, 4]
```

### 哪一種複製才會得到「獨立」的物件


**問題是：** 要怎麼複製，才能讓改動*原本那個*不會影響到*複本*（反之亦然）？

```python
# ── The idiom you'll see everywhere (shallow copy of a list) ──
# NOTE !!! how we make copy in py
intervals_cache = intervals[:]        # copy 1) slice
intervals_cache = intervals.copy()    # copy 2) .copy()  (same effect)
intervals_cache = list(intervals)     # copy 3) list()   (same effect)
```

**關鍵差異 — 取決於這個 list 是「扁平」還是「巢狀」：**

```python
#----------------------------------------------------------
# CASE A) FLAT list (ints, strings, …) → shallow copy IS enough
#----------------------------------------------------------
original = [1, 2, 3]
copy_    = original[:]         # independent copy

original.append(4)
original[0] = 99
print(original)   # [99, 2, 3, 4]
print(copy_)      # [1, 2, 3]     ← NOT affected  ✅

#----------------------------------------------------------
# CASE B) NESTED list (list of lists / objects) → shallow copy is NOT enough
#----------------------------------------------------------
original = [[1, 2], [3, 4]]
shallow  = original[:]        # copies OUTER list only; inner lists are SHARED

original.append([5, 6])       # outer-level change → safe
print(shallow)                # [[1, 2], [3, 4]]   ← NOT affected  ✅

original[0][0] = 99           # inner-level change → LEAKS through!
print(shallow)                # [[99, 2], [3, 4]]  ← AFFECTED  ❌

# → to be FULLY independent, use deepcopy:
import copy
deep = copy.deepcopy(original)
original[0][0] = -1
print(deep)                   # inner list unchanged  ✅
```

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

## 字串

### 對字串的字元做排序

```python
def _sort(x):
    _x = list(x)
    _x.sort()
    return "".join(_x)

x = "bca"
print (x)
x_ = _sort(x)
print (x_)
```

### 依索引取代某個字元（切片 + 串接）


**重點：Python 的字串是不可變的** — 你**不能**寫 `s[i] = ch`
（會丟出 `TypeError: 'str' object does not support item assignment`）。
要「改掉索引 `i` 上的字元」，就用 `i` 前後的切片重組出一個**新**字串。

```python
# ── The idiom ──
#   s[:i]      -> everything BEFORE index i   (i excluded)
#   ch         -> the new character to place at index i
#   s[i+1:]    -> everything AFTER index i    (i excluded, i.e. drop old s[i])
new_s = s[:i] + ch + s[i+1:]
```

```python
# demo
In [1]: s = "AACCGGTT"

In [2]: s[:3] + "X" + s[3+1:]     # replace index 3 ('C') with 'X'
Out[2]: 'AACXGGTT'

# original is untouched (immutable) — a NEW string is returned
In [3]: s
Out[3]: 'AACCGGTT'
```

**為什麼是 `i+1`？** `s[i+1:]` 從 `i` *之後*開始，所以舊字元 `s[i]` 被丟掉、
換成 `ch`。如果改用 `s[i:]`，舊字元會被保留（變成插入而不是取代）：

```python
s = "abc"
s[:1] + "X" + s[1+1:]   # 'aXc'  ← REPLACE index 1  (skip old 'b')
s[:1] + "X" + s[1:]     # 'aXbc' ← INSERT before index 1 (old 'b' kept)
```

**經典 LC 用法 — LC 433 Minimum Genetic Mutation**（BFS，一次變異一個基因字元）：

```python
# for each position i, try each candidate char ch
for i in range(len(cur_gene)):
    for ch in "ACGT":
        if ch == cur_gene[i]:
            continue
        # build the neighbor gene with position i mutated to ch
        new_gene = cur_gene[:i] + ch + cur_gene[i+1:]
        ...
```

> **替代做法**：如果你要改很多個位置，先轉成 `list`
> （`arr = list(s); arr[i] = ch; s = "".join(arr)`）— list 是可變的，
> 所以可以就地做索引賦值，也免去一再重組字串。
> 只改一個位置的話，上面的切片寫法最乾淨。

### `split`

```python
# python
# syntax : split(separator, number_of_split_result) 
# example 1
# In [17]: x = 'dig1 8 1 5 1'

# In [18]: x
# Out[18]: 'dig1 8 1 5 1'

# In [19]: x.split(" ")
# Out[19]: ['dig1', '8', '1', '5', '1']

# In [20]: x.split(" ", 1)
# Out[20]: ['dig1', '8 1 5 1']

# In [21]: x.split(" ", 2)
# Out[21]: ['dig1', '8', '1 5 1']

# In [22]: x.split(" ", 3)
# Out[22]: ['dig1', '8', '1', '5 1']

# In [23]: x.split(" ", 4)
# Out[23]: ['dig1', '8', '1', '5', '1']

# In [24]: x.split(" ", 100)
# Out[24]: ['dig1', '8', '1', '5', '1']

# example 2
# LC 937 Reorder Data in Log Files
class Solution:
    def reorderLogFiles(self, logs):
        def f(log):
            id_, rest = log.split(" ", 1)
            return (0, rest, id_) if rest[0].isalpha() else (1,)

        logs.sort(key = lambda x : f(x))
        return logs #sorted(logs, key = f)
```


### 把字串補零

```python
# LC 67. Add Binary
#NOTE : zfill syntax
#    -> fill n-1 "0" to a string at beginning

#example :
In [10]: x = '1'

In [11]: x.zfill(2)
Out[11]: '01'

In [12]: x.zfill(3)
Out[12]: '001'

In [13]: x.zfill(4)
Out[13]: '0001'

In [14]: x.zfill(10)
Out[14]: '0000000001'
```

### `lstrip` / `rstrip` / `strip`


```python
# python
# syntax : s.lstrip(chars) / s.rstrip(chars) / s.strip(chars)
#   - lstrip : remove matching chars from the LEFT  (leading)
#   - rstrip : remove matching chars from the RIGHT (trailing)
#   - strip  : remove matching chars from BOTH ends
#   - `chars` is a SET of characters to remove (NOT a substring/prefix!)
#   - no arg -> strips whitespace (space, \t, \n, \r ...)
#   - it does NOT touch chars in the MIDDLE, and returns a NEW string
```

```python
#----------------------------
# example 1 : strip leading zeros (lstrip)
#----------------------------
In [1]: x = "0000123"
In [2]: x.lstrip("0")
Out[2]: '123'

# no leading zeros -> unchanged (safe, no error)
In [3]: y = "123"
In [4]: y.lstrip("0")
Out[4]: '123'

# all chars match -> empty string (WATCH OUT!)
In [5]: "0000".lstrip("0")
Out[5]: ''

#----------------------------
# example 2 : rstrip (trailing)
#----------------------------
In [6]: "12300".rstrip("0")
Out[6]: '123'

In [7]: "hello!!!".rstrip("!")
Out[7]: 'hello'

# common: drop trailing newline / whitespace when reading input
In [8]: "  line\n".rstrip()
Out[8]: '  line'

#----------------------------
# example 3 : strip (both ends) + whitespace default
#----------------------------
In [9]: "  hi  ".strip()
Out[9]: 'hi'

In [10]: "xxhixx".strip("x")
Out[10]: 'hi'

#----------------------------
# example 4 : GOTCHA — `chars` is a char SET, not a prefix string
#----------------------------
# removes ANY leading char that is 'a','b', or 'c' (in any order),
# NOT the literal prefix "abc"
In [11]: "cabbage".lstrip("abc")
Out[11]: 'ge'          # 'c','a','b','b','a' all stripped, stops at 'g'

# to remove a real PREFIX/SUFFIX (py3.9+), use removeprefix / removesuffix:
In [12]: "test.py".removesuffix(".py")
Out[12]: 'test'
In [13]: "img_001".removeprefix("img_")
Out[13]: '001'
```

**常見 LC 用法 — 組出數字之後把前導零去掉**

```python
# LC 402. Remove K Digits
# after building the result digits in a stack, the front may have leading zeros
# e.g. stack -> "0200"  (must return "200", and "" must become "0")

res = "".join(stack).lstrip('0')   # "0200" -> "200",  "0000" -> ""
return res if res else "0"         # handle the all-zero / empty case
```

> **經驗法則**：`lstrip('0')` 是把數字字串正規化（去掉前導零）的慣用寫法 —
> 但一定要處理**空字串**的結果（`res or "0"`），
> 因為 `"0"`/`"0000"` 會被剝成 `""`。反向操作（補零）見 [`zfill`](#zero-padding-a-string)。

### `ord()`、`chr()`、`isalpha()`、`isdigit()`

```python
#-------------------------------
# ord() : char -> ASCII int
# chr() : ASCII int -> char
#-------------------------------
print(ord('a'))   # 97
print(ord('A'))   # 65
print(ord('0'))   # 48

print(chr(97))    # 'a'
print(chr(65))    # 'A'

# Common pattern: normalize letter to 0-25 index
ch = 'c'
idx = ord(ch) - ord('a')   # 2

# Shift a character by n positions
def shift(ch, n):
    return chr((ord(ch) - ord('a') + n) % 26 + ord('a'))

#-------------------------------
# String check methods
#-------------------------------
"abc".isalpha()     # True  — all letters
"abc123".isalpha()  # False
"123".isdigit()     # True  — all digits
"abc123".isalnum()  # True  — letters + digits
"  ".isspace()      # True
"ABC".isupper()     # True
"abc".islower()     # True

# LC 125 Valid Palindrome
def isPalindrome(s):
    filtered = [c.lower() for c in s if c.isalnum()]
    return filtered == filtered[::-1]

# NOTE: lower() has NO effect on digits — safe to call on any alphanumeric char
# >>> "0".lower()
# '0'
# >>> "A".lower()
# 'a'
# So the one-liner below works for both letters and numbers:
fixed_s = ''.join(c.lower() for c in s if c.isalnum())
```

### 字串方法速查

```python
s = "  Hello, World!  "

# Strip whitespace (or specific chars)
s.strip()          # "Hello, World!"
s.lstrip()         # "Hello, World!  "
s.rstrip()         # "  Hello, World!"
s.strip("!")       # "  Hello, World!  " (only strips specified chars from ends)

# Case
s.lower()          # "  hello, world!  "
s.upper()          # "  HELLO, WORLD!  "
s.title()          # "  Hello, World!  "
s.swapcase()       # "  hELLO, wORLD!  "

# Search
s.find("World")    # 9  (-1 if not found)
s.index("World")   # 9  (raises ValueError if not found)
s.count("l")       # 3
s.startswith("  H")  # True
s.endswith("!  ")    # True

# Replace and join
s.replace("World", "Python")   # "  Hello, Python!  "
", ".join(["a", "b", "c"])     # "a, b, c"
"a,b,c".split(",")             # ['a', 'b', 'c']

# Reverse a string
rev = s[::-1]

# String multiplication
"ab" * 3   # "ababab"
"-" * 10   # "----------"

# Check if string is a palindrome
def is_palindrome(s):
    return s == s[::-1]
```

### 從字串中抓出數字字元

```python
# LC 008
s = '4193 with words'
res = re.search('(^[\+\-]?\d+)', s).group()
print (res)
```

### 反向走訪字串


**關鍵差異：`range()` 的 stop 值是「不含」的**

```python
x = "332"   # indices: 0, 1, 2

# ── Form 1: range(len(x)-1, -1, -1)  → stop = -1 (exclusive) → covers 2, 1, 0 (ALL indices)
for i in range(len(x)-1, -1, -1):
    print(i)
# 2
# 1
# 0   ← index 0 IS included

# ── Form 2: range(len(x)-1, 0, -1)   → stop = 0 (exclusive) → covers 2, 1 (MISSES index 0)
for i in range(len(x)-1, 0, -1):
    print(i)
# 2
# 1   ← index 0 is NOT included
```

| 寫法 | stop 值 | 走訪到的索引 | 含索引 0 嗎？ |
|------|-----------|-----------------|-------------------|
| `range(len(x)-1, -1, -1)` | `-1`（不含） | `len-1 … 0` | **是** |
| `range(len(x)-1,  0, -1)` | ` 0`（不含） | `len-1 … 1` | **否** |

**經驗法則：** 要反向走訪「所有」索引，stop 值一律用 `-1`。

```python
# Equivalent ways to iterate a string/array in reverse (all indices)
x = "abc"

# Form A: range
for i in range(len(x) - 1, -1, -1):
    print(x[i])

# Form B: reversed() — cleaner, no index needed
for ch in reversed(x):
    print(ch)

# Form C: slice — creates a reversed copy
for ch in x[::-1]:
    print(ch)
```

**當你「確實」想跳過索引 0 時**（例如要拿 `x[i]` 跟 `x[i-1]` 比較）：
```python
# Safe to start from index 1 in forward loops, or stop before 0 in reverse loops
for i in range(len(x) - 1, 0, -1):   # compares x[i] vs x[i-1]; never i-1 = -1
    if x[i] == x[i - 1]:
        print(f"duplicate at {i}")
```

## 排序與比較

### 用 `lambda` 當 key 的 `sort`

```text
# example 1
# LC 973. K Closest Points to Origin
# IDEA : sort + lambda
class Solution(object):
    def kClosest(self, points, K):
        points.sort(key = lambda x : x[0]**2 +  x[1]**2)
        return points[:K]


# example 2
In [28]: def my_func(x):
    ...:     return x**2
    ...:
    ...: x = [-4,-5,0,1,2,5]
    ...: x.sort(key=lambda x: my_func(x))
    ...: print (x)
[0, 1, 2, -4, -5, 5]
```

```text
# LC 937
# https://leetcode.com/problems/reorder-data-in-log-files/solution/
def my_func(input):
    # do sth
    if condition:
        return key1, key2, key3....
    else:
        return key4, key5, key6....

my_array=["a1 9 2 3 1","g1 act car","zo4 4 7","ab1 off key dog","a8 act zoo"]
my_array.sort(key=lambda x : my_func(x))
```

### 降冪：`key=lambda x: -x[0]` vs `reverse=True` vs `[::-1]`


降冪排序有三種寫法，各有各適用的場合。

```python
# ── Context: LC 853 Car Fleet ──
# We have pos_speed = [[pos, speed, time], ...] and want to sort by position DESC.

# ── Form 1: negate the key  (in-place, fine-grained control) ──
pos_speed.sort(key=lambda x: -x[0])
# Use when:
#   • You need MIXED direction: primary DESC, secondary ASC
#     e.g. sort(key=lambda x: (-x[0], x[1]))  ← impossible with reverse=True alone
#   • Works for int and float keys

# ── Form 2: reverse=True  (cleaner for single-direction reversal) ──
pos_speed.sort(key=lambda x: x[0], reverse=True)
sorted_cars = sorted(cars, reverse=True)   # creates a NEW list
# Use when:
#   • ALL keys go the same direction (all DESC)
#   • More readable for simple cases
#   • sorted() is preferred over sort() when you need to keep the original

# ── Form 3: sort ASC then reverse/slice  (separate steps) ──
times = [(target - pos) / spe for pos, spe in sorted(cars)]   # ASC sort
for time in times[::-1]:        # iterate in reverse  — does NOT mutate list
    ...
# -- or --
for time in reversed(times):    # same effect, no extra list copy
    ...
# Use when:
#   • You want to keep the sorted-ASC list around for other uses
#   • reversed() is O(1) memory; [::-1] creates a new list copy

# ── Quick comparison ──
# Method              | In-place? | New list? | Mixed direction? | Readability
# -x[0]               |   yes     |    no     |      YES         |  moderate
# reverse=True        |   yes     |    no     |      no          |  high
# sorted(reverse=True)|   no      |    YES    |      no          |  high
# sort ASC + reversed |   yes     |    no     |      no          |  moderate

# ── Multi-key mixed direction example (only negation works here) ──
# Sort by position DESC, then by speed ASC as tiebreaker:
data.sort(key=lambda x: (-x[0], x[1]))
```

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

```python
# python
y = [[7,0], [4,4], [7,1], [5,0], [6,1], [5,2]]
print (y)
# sort by x[0] DESC, then x[1] ASC
y.sort(key = lambda x : (-x[0], x[1]))
print (y)
# [[7, 0], [4, 4], [7, 1], [5, 0], [6, 1], [5, 2]]
# [[7, 0], [7, 1], [6, 1], [5, 0], [5, 2], [4, 4]]
```

**視覺化追蹤** — tuple key 是怎麼把上面的資料排出來的：

```text
elem     | key = (-x[0], x[1])
---------------------------------
[7,0]    | (-7, 0)
[7,1]    | (-7, 1)
[6,1]    | (-6, 1)
[5,0]    | (-5, 0)
[5,2]    | (-5, 2)
[4,4]    | (-4, 4)

sort keys ASC  ->  (-7,0) < (-7,1) < (-6,1) < (-5,0) < (-5,2) < (-4,4)
                    ^^^^^^^^^^^^^^ 1st slot tied (-7) -> compare 2nd slot (0 < 1)
=> [[7,0], [7,1], [6,1], [5,0], [5,2], [4,4]]
```

#### **`reverse = True` vs 把 key 取負**

```python
# python
arr = [[1,'b'], [1,'a'], [2,'a']]

# (a) reverse=True flips the WHOLE ordering (every key), not just the 1st
sorted(arr, key = lambda x : (x[0], x[1]), reverse = True)
# [[2,'a'], [1,'b'], [1,'a']]   <-- x[0] DESC *and* x[1] DESC

# (b) negation flips only the negated slot
sorted(arr, key = lambda x : (-x[0], x[1]))
# [[2,'a'], [1,'a'], [1,'b']]   <-- x[0] DESC, x[1] ASC

# NOTE !!! `-` only works on numbers
sorted(arr, key = lambda x : (-x[0], -x[1]))   # ❌ TypeError : bad operand type for unary -: 'str'
```

#### **非數值 key 又要方向混合 → 兩次穩定排序**

Timsort 是**穩定的**，所以你可以把排序串起來。規則：**先依最後（最不重要）的 key 排。**

```python
# python
# want : len(s) ASC, then s DESC  (can't do -s)
words = ["bb", "a", "ab", "c", "ba"]

words.sort(reverse = True)          # step 1 : least significant key (s DESC)
words.sort(key = len)               # step 2 : most  significant key (len ASC) - stable
print (words)
# ['c', 'a', 'bb', 'ba', 'ab']
#  ^^^^^^^^ len 1, DESC        ^^^^^^^^^^^^^^ len 2, DESC
```

> ⚠️ **千萬不要**把這兩趟的順序顛倒 — 先依 `len` 再依 `s` 排，會把 `len` 的分組整個丟掉。

#### **`functools.cmp_to_key` — 當根本寫不出 key 函式時**

當順序取決於**把兩個元素拿來一起比較**（沒有任何單一元素的值能表達這個規則）時就用它。

```python
# python
# LC 179 Largest Number : concat digits to form the biggest number
# rule : a before b  iff  a+b > b+a  -> not expressible as a single key
import functools

class Solution(object):
    def largestNumber(self, nums):
        # time = O(n log n * k), space = O(n)   (k = digit length)
        def cmp(a, b):
            if a + b > b + a:
                return -1      # a comes FIRST
            elif a + b < b + a:
                return 1       # b comes FIRST
            return 0

        strs = [str(x) for x in nums]
        strs.sort(key = functools.cmp_to_key(cmp))
        res = "".join(strs)
        return "0" if res[0] == "0" else res

# nums = [3,30,34,5,9] -> "9534330"
```

#### **其他好用的排序 key**

```python
# python
# 1) sort dict by VALUE desc, then KEY asc  (LC 692 Top K Frequent Words)
from collections import Counter
cnt = Counter(["i","love","leetcode","i","love","coding"])
res = sorted(cnt.keys(), key = lambda w : (-cnt[w], w))
# ['i', 'love', 'coding', 'leetcode']

# 2) sort by "distance" (LC 973 K Closest Points to Origin)
points = [[1,3],[-2,2],[5,8],[0,1]]
points.sort(key = lambda p : p[0]**2 + p[1]**2)
# [[0,1], [-2,2], [1,3], [5,8]]

# 3) sort intervals by start (LC 56 / 57 / 253 / 435)
intervals = [[1,3],[8,10],[2,6],[15,18]]
intervals.sort(key = lambda x : x[0])
# [[1,3], [2,6], [8,10], [15,18]]

# 4) sort by end   (LC 435 Non-overlapping Intervals - greedy)
intervals.sort(key = lambda x : x[1])

# 5) sort chars of a string as the canonical key (LC 49 Group Anagrams)
"".join(sorted("tea"))    # 'aet'

# 6) sort with index preserved (need original position afterwards)
nums = [5,2,8]
idx_sorted = sorted(range(len(nums)), key = lambda i : nums[i])
# [1, 0, 2]   <-- indices in value order

# 7) sort intervals by BOTH ends (LC 56 Merge Intervals, LC 252 / 253 Meeting Rooms)
intervals.sort(key = lambda x : (x[0], x[1]))

# 8) primary ASC, secondary DESC -- negate only the secondary slot
events.sort(key = lambda x : (x[0], -x[1]))

# 9) sort a list of dicts by two fields
people = [{"age": 30, "name": "bob"}, {"age": 30, "name": "amy"}]
people.sort(key = lambda p : (p["age"], p["name"]))

# 10) three keys, same rule -- tuples compare left to right for any length
tasks.sort(key = lambda t : (t[0], t[1], t[2]))

# 11) sorted() returns a NEW list; .sort() mutates in place and returns None
sorted_events = sorted(event_list, key = lambda x : (x[0], x[1]))
```

**Java 對應寫法備註：**

```java
// java
int[][] people = {{7,0},{4,4},{7,1},{5,0},{6,1},{5,2}};

// V1 : explicit comparator - x[0] DESC, tie-break x[1] ASC
Arrays.sort(people, (a, b) -> {
    if (a[0] != b[0]) {
        return b[0] - a[0];   // NOTE !!! b - a  => DESC
    }
    return a[1] - b[1];       //          a - b  => ASC
});

// V2 : Comparator chaining (more readable, Java 8+)
Arrays.sort(people,
        Comparator.<int[]>comparingInt(a -> a[0]).reversed()   // key1 DESC
                  .thenComparingInt(a -> a[1]));               // key2 ASC

// List version
List<int[]> list = new ArrayList<>();
list.sort(Comparator.<int[]>comparingInt(a -> a[0]).reversed()
                    .thenComparingInt(a -> a[1]));

// ⚠️ `b[0] - a[0]` can OVERFLOW for large/negative values
//    -> use Integer.compare(b[0], a[0]) instead
Arrays.sort(people, (a, b) -> Integer.compare(b[0], a[0]));

// ⚠️ `.reversed()` after `.thenComparing(...)` reverses the WHOLE chain
//    (same trap as python's reverse=True)
Comparator.comparingInt((int[] a) -> a[0])
          .thenComparingInt(a -> a[1])
          .reversed();          // <-- key1 DESC *and* key2 DESC
```

**🚫 常見錯誤：**

```python
# 1) Expecting reverse=True to flip only the primary key
arr.sort(key = lambda x : (x[0], x[1]), reverse = True)   # ❌ flips BOTH keys
arr.sort(key = lambda x : (-x[0], x[1]))                  # ✅ only key1 DESC

# 2) Negating a string / tuple
arr.sort(key = lambda x : (-x[0], -x[1]))   # ❌ TypeError if x[1] is str
# ✅ use 2 stable sorts, or cmp_to_key

# 3) Comparing mixed types inside the key
sorted([1, "a"])            # ❌ TypeError : '<' not supported between 'str' and 'int'

# 4) Multi-pass sorts in the WRONG order
words.sort(key = len); words.sort(reverse = True)   # ❌ len grouping destroyed
words.sort(reverse = True); words.sort(key = len)   # ✅ least significant key FIRST

# 5) Forgetting the sort is what makes the greedy/2-pointer step valid
#    -> for interval problems, ALWAYS state which key you sorted on
```

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

### 用具名 `key` 函式寫條件式的 tuple key


當排序 key 取決於某個**條件**（A 群 vs B 群、合法 vs 不合法等）時，
一行 lambda 會變得很難讀。改寫成一個**回傳 tuple 的具名 `key` 函式** —
tuple 依然是逐元素（由左到右）比較，所以第一個欄位就是主要排序依據、
下一個是平手時的判準，以此類推。

**模式：開頭放「群組標籤」+ 各群組自己的排序規則**

```python
# Return a tuple of sort keys; the FIRST element groups items,
# the rest order items WITHIN each group.
def compare(item):
    if condition:
        return (0, item.value, item.name)   # group 0 first; ASC by value, then name
    else:
        return (1, -item.priority, item.id)  # group 1 next; DESC by priority, ASC by id

items.sort(key=compare)        # in-place
# items = sorted(items, key=compare)   # or build a new list
```

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

```python
class Solution:
    def reorderLogFiles(self, logs):
        def compare(log):
            id_, rest = log.split(" ", 1)
            if rest[0].isalpha():
                return (0, rest, id_)   # letter-logs: group 0, by content, then id
            else:
                return (1,)             # digit-logs: group 1, keep original order

        return sorted(logs, key=compare)
```

> **經驗法則**：只要排序規則出現*分支*，就該改用回傳 tuple 的具名 `key` 函式 —
> 這比把 `if/else` 硬塞進 lambda 好讀太多。

### 依出現次數排序一個 dict

```python
# LC 451 Sort Characters By Frequency
# V1
import collections
class Solution(object):
    def frequencySort(self, s):
        d = collections.Counter(s)
        d_dict = dict(d)
        res = []
        for x in sorted(d_dict, key=lambda k : -d_dict[k]):
            res.append(x)
        return res

x= [1, 2, 3, 1, 2, 1, 2, 1]
s = Solution()
r = s.frequencySort(x)

# V2
# https://stackoverflow.com/questions/613183/how-do-i-sort-a-dictionary-by-value
import collections
class Solution(object):
    def frequencySort(self, s):
        d = collections.Counter(s)
        d_dict = dict(d)
        res = []
        #for x in sorted(d_dict.items(), key=lambda items: -items[1]):
        for _ in sorted(d_dict.items(), key=lambda x: -x[1]):
            res.append(_)
        return res

x= [1, 2, 3, 1, 2, 1, 2, 1]
s = Solution()
r = s.frequencySort(x)
```

### 帶 `key` 的 `min()` / `max()`

```python
# key= works exactly like sort's key= parameter
nums = [-3, -1, 2, 4]
print(max(nums, key=abs))   # -3  (largest absolute value)
print(min(nums, key=abs))   # -1  (smallest absolute value)

# With iterable of tuples
points = [(1, 5), (3, 2), (2, 8)]
print(max(points, key=lambda p: p[1]))  # (2, 8)

# min/max with default (avoids error on empty iterable)
print(min([], default=0))   # 0

# clamp a value between lo and hi
val = max(lo, min(val, hi))
```

### `sorted()`、`reversed()`、`sum()`、`abs()`

```python
# sorted() returns a NEW list; list.sort() is in-place
nums = [3, 1, 4, 1, 5]
print(sorted(nums))            # [1, 1, 3, 4, 5]  — nums unchanged
print(sorted(nums, reverse=True))  # [5, 4, 3, 1, 1]

# Sort list of tuples: primary key asc, secondary key desc
pairs = [(1, 3), (2, 1), (1, 5)]
pairs.sort(key=lambda x: (x[0], -x[1]))
# [(1, 5), (1, 3), (2, 1)]

# reversed() returns an iterator
for x in reversed([1, 2, 3]):
    print(x)  # 3 2 1

list(reversed([1,2,3]))  # [3, 2, 1]

# sum() with start
sum([1, 2, 3], 10)   # 16

# sum of 2D list
matrix = [[1,2],[3,4]]
total = sum(sum(row) for row in matrix)  # 10

# abs()
abs(-5)    # 5
abs(3+4j)  # 5.0  (complex magnitude)
```

## 數字與數學

### 一次拿到商和餘數 — `divmod`

```text
In [1]: x,y = divmod(100, 3)

In [2]: x
Out[2]: 33

In [3]: y
Out[3]: 1
```

### 除以某個數之後的餘數

```text
#-----------------
# V1 : %=
#-----------------

In [7]: x = 100

In [8]: x %= 60

In [9]: x
Out[9]: 40

In [10]: y = 120

In [11]: y %= 60

In [12]: y
Out[12]: 0

#-----------------
# V2 : divmod
#-----------------
In [13]: a = 100

In [14]: q, r = divmod(a, 60)

In [15]: q
Out[15]: 1

In [16]: r
Out[16]: 40

In [17]: b = 120

In [18]: q2, r2 = divmod(b, 60)

In [19]: q2
Out[19]: 2

In [20]: r2
Out[20]: 0
```

```python
# LC 1010
# V0
# IDEA : dict
class Solution(object):
    def numPairsDivisibleBy60(self, time):
        rem = {}
        pairs = 0
        for t in time:
            #print ("rem = " + str(rem))
            t %= 60
            if (60 - t) % 60 in rem:
                pairs += rem[(60 - t) % 60]
            if t not in rem:
                rem[t] = 1
            else:
                rem[t] += 1
        return pairs
```

### `math.ceil`

```text
# https://www.runoob.com/python/func-number-ceil.html
# https://www.runoob.com/python/func-number-ceil.html

"""
The method ceil(x) in Python returns a ceiling value of x 
-> i.e., the SMALLEST integer GREATER than or EQUAL to x.
"""
In [9]:
   ...: import math
   ...:
   ...: # prints the ceil using ceil() method
   ...: print ("math.ceil(-23.11) : ", math.ceil(-23.11))
   ...: print ("math.ceil(300.16) : ", math.ceil(300.16))
   ...: print ("math.ceil(300.72) : ", math.ceil(300.72))
math.ceil(-23.11) :  -23
math.ceil(300.16) :  301
math.ceil(300.72) :  301


# LC 875. Koko Eating Bananas
#...
# Iterate over the piles and calculate hour_spent.
# We increase the hour_spent by ceil(pile / middle)
for pile in piles:
    # python ceil : https://www.runoob.com/python/func-number-ceil.html
    hour_spent += math.ceil(pile / middle)
# Check if middle is a workable speed, and cut the search space by half.
if hour_spent <= h:
    right = middle
else:
    left = middle + 1
#...
```

### `math.floor`

```text
# https://www.geeksforgeeks.org/floor-ceil-function-python/

"""
floor() method in Python returns the floor of x 
-> i.e., the LARGEST integer NOT GREATER than x. 
"""

# This will import math module
import math   
  
In [8]: import math
   ...:
   ...: # prints the ceil using floor() method
   ...: print ("math.floor(-23.11) : ", math.floor(-23.11))
   ...: print ("math.floor(300.16) : ", math.floor(300.16))
   ...: print ("math.floor(300.72) : ", math.floor(300.72))
math.floor(-23.11) :  -24
math.floor(300.16) :  300
math.floor(300.72) :  300
```

### `pow(x, n, mod)` — 快速模冪

```python
# Built-in 3-arg pow is O(log n), much faster than (x**n) % mod
MOD = 10**9 + 7

print(pow(2, 10, MOD))    # 1024
print(pow(2, 100, MOD))   # 976371285  (computed efficiently)

# Modular inverse (when mod is prime): pow(a, mod-2, mod)
inv = pow(3, MOD - 2, MOD)   # modular inverse of 3

# LC 50 Pow(x, n) — manual fast power
def myPow(x, n):
    if n < 0:
        x, n = 1 / x, -n
    res = 1
    while n:
        if n % 2 == 1:
            res *= x
        x *= x
        n //= 2
    return res
```

### 整數除法 `//` 與位元運算

```python
#-------------------------------
# Integer division (floor division)
#-------------------------------
7 // 2    # 3
-7 // 2   # -4  (rounds toward -inf, NOT toward 0!)
int(-7/2) # -3  (truncation toward 0)

# Safe mid-point (avoids overflow in other languages)
lo, hi = 0, 100
mid = (lo + hi) // 2

#-------------------------------
# Bit operations (common in LC)
#-------------------------------
# AND, OR, XOR, NOT
5 & 3    # 1   (101 & 011 = 001)
5 | 3    # 7   (101 | 011 = 111)
5 ^ 3    # 6   (101 ^ 011 = 110)
~5       # -6  (bitwise NOT: ~x = -(x+1))

# Shift
5 << 1   # 10  (multiply by 2)
5 >> 1   # 2   (floor divide by 2)

# Check/set/clear bit i
x = 13          # 1101
x & (1 << i)    # check bit i (non-zero if set)
x | (1 << i)    # set bit i
x & ~(1 << i)   # clear bit i
x ^ (1 << i)    # flip bit i

# Count set bits
bin(13).count('1')   # 3
# or: use Brian Kernighan
def count_bits(n):
    count = 0
    while n:
        n &= n - 1   # clear lowest set bit
        count += 1
    return count

# x & (x-1) removes lowest set bit — useful for power-of-2 check
def is_power_of_two(n):
    return n > 0 and (n & (n - 1)) == 0
```

### 把 N 進位的整數轉成十進位

```python
# https://github.com/yennanliu/CS_basics/blob/master/doc/cheatsheet/math.md

# How does int(x[,base]) work?
# -> https://stackoverflow.com/questions/33664451/how-does-intx-base-work
# -> int(string, base) accepts an arbitrary base. You are probably familiar with binary and hexadecimal, and perhaps octal; these are just ways of noting an integer number in different bases:
# exmaple :
# In [76]: int('10',2)      # transform '10' from 2 based to 10 based                                                  
# Out[76]: 2
#
# In [77]: int('11',2)      # # transform '11' from 2 based to 10 based                                                 
# Out[77]: 3
#
# In [78]: int('100',2)     # # transform '100' from 2 based to 10 based                                                 
# Out[78]: 4

# LC 089
```

### 無限大與邊界值

```python
# Use float('inf') / float('-inf') instead of sys.maxsize for clarity
INF = float('inf')
NEG_INF = float('-inf')

# Works with min/max comparisons
min_val = float('inf')
for x in [3, 1, 4, 1, 5]:
    min_val = min(min_val, x)
print(min_val)  # 1

# Common in DP initialization
dp = [[float('inf')] * n for _ in range(m)]

# Python int has no overflow — safe to use large numbers
# But float('inf') is cleaner for "unbounded" semantics
```

## 走訪、生成式與函數式工具

### `all()`

- 會針對 list 中「所有」元素檢查條件，回傳 Boolean（true 或 false）
```python
# example 1
In [36]: a = "000"

In [37]: all( i == "0" for i in a )
Out[37]: True

# example 2
In [38]: b = "abc123"

In [39]: all ( i == "a" for i in b )
Out[39]: False

# LC 763. Partition Labels
class Solution(object):
    def partitionLabels(self, s):
        d = {val:idx for idx, val in enumerate(list(s))}
        #print (d)
        res = []
        tmp = set()
        for idx, val in enumerate(s):
            """
            NOTE : below condition
            """
            if idx == d[val] and all(idx >= d[t] for t in tmp):
                res.append(idx+1)
            else:
                tmp.add(val)
        _res = [res[0]] + [ res[i] - res[i-1] for i in range(1, len(res)) ]
        return _res
```

### `any()`

```python
# Returns True if ANY element in iterable is True (short-circuits)
In [1]: any([False, False, True])
Out[1]: True

In [2]: any([False, False, False])
Out[2]: False

In [3]: any(x > 3 for x in [1, 2, 5])
Out[3]: True

# Complement to all():
# all() -> every element must be True
# any() -> at least one element must be True
```

### `not` 邏輯

```python
#----------------------------
# can be either None, [], ""
#----------------------------
In [32]: x = None

In [34]: not x
Out[34]: True

In [35]: y = []

In [36]: not y
Out[36]: True

In [37]: z = ""

In [38]: not z
Out[38]: True
```

### `enumerate()`

```python
# Returns (index, value) pairs — avoids manual index tracking
fruits = ['apple', 'banana', 'cherry']

for i, v in enumerate(fruits):
    print(i, v)
# 0 apple
# 1 banana
# 2 cherry

# start parameter
for i, v in enumerate(fruits, start=1):
    print(i, v)
# 1 apple  2 banana  3 cherry

# Build index map (very common in LC)
s = "abcba"
idx_map = {v: i for i, v in enumerate(s)}
print(idx_map)  # {'a': 4, 'b': 3, 'c': 2}  (last occurrence)
```

### `zip()`

```text
# python
In [1]: for x, y in zip([-1, 1, 0, 0], [0, 0, -1, 1]):
   ...:     print (x, y)
   ...:
-1 0
1 0
0 -1
0 1

In [2]: for x, y, z in zip([-1, 1, 0, 0], [0, 0, -1, 1], [0,0,0,0]):
   ...:     print (x,y,z)
   ...:
-1 0 0
1 0 0
0 -1 0
0 1 0

In [3]: for x, y, z, u in zip([-1, 1, 0, 0], [0, 0, -1, 1], [0,0,0,0], [9,9,9,9]):
   ...:     print (x,y,z,u)
   ...:
-1 0 0 9
1 0 0 9
0 -1 0 9
0 1 0 9
```

### 走訪 dict

```python
d = {'a':1, 'b':2, 'c': 3}
# loop over key, value
for k, v in d.items():
    print (k, v)

# loop over key
for k in d.keys():
    print (k)

# loop over value
for v in d.values():
    print (v)
```

### 星號（`*`）運算式

```text
# Extended Iterable Unpacking

# https://www.python.org/dev/peps/pep-3132/
# http://swaywang.blogspot.com/2012/01/pythonstarred-expression.html

# example 1
In [38]: a, *b, c = range(5)

In [39]: a
Out[39]: 0

In [40]: b
Out[40]: [1, 2, 3]

In [41]: c
Out[41]: 4

# example 2
In [43]: for a, *b in [(1, 2, 3), (4, 5, 6, 7)]:
    ...:     print ("a = " + str(a) + " b = " + str(b))
    ...:
a = 1 b = [2, 3]
a = 4 b = [5, 6, 7]

# example 3
In [44]: first, *rest = [1, 2, 3, 4, 5]

In [45]: first
Out[45]: 1

In [46]: rest
Out[46]: [2, 3, 4, 5]

# example 4
In [47]: *directories, executable = "/usr/local/bin/vim".split("/")
    ...: print (directories)
    ...: print (executable)
['', 'usr', 'local', 'bin']
vim

# example 5
args = [1,3]
print (range(*args))
```

### `filter()`

```text
# https://www.runoob.com/python/python-func-filter.html

#-----------------------------------------------
# syntax : filter(<filter_func>, <iterable>)
#-----------------------------------------------

# note !!! : in py 3, it will return iterable instance; while in py 2, it will return a list directly

#----------------------------
# example 1
#----------------------------
In [13]: def is_odd(n):
    ...:     return n % 2 == 1
    ...:
    ...: newlist = filter(is_odd, [1, 2, 3, 4, 5, 6, 7, 8, 9, 10])
    ...: print(newlist)
<filter object at 0x7fc71c3dced0>

In [14]:

In [14]: list(newlist)
Out[14]: [1, 3, 5, 7, 9]


#----------------------------
# example 2
#----------------------------
In [15]: import math
    ...: def is_sqr(x):
    ...:     return math.sqrt(x) % 1 == 0
    ...:
    ...: newlist = filter(is_sqr, range(1, 101))
    ...: print(newlist)
<filter object at 0x7fc71bb10450>

In [16]:

In [16]: list(newlist)
Out[16]: [1, 4, 9, 16, 25, 36, 49, 64, 81, 100]
```

### List 生成式

```text
#----------------------------
# example 1
#----------------------------
# https://stackoverflow.com/questions/4260280/if-else-in-a-list-comprehension

In [8]: [ x for x in range(5) ]
Out[8]: [0, 1, 2, 3, 4]

# NOTE this !!!!
In [9]: [ x if x % 2 == 0 else -1 for x in range(5) ]
   ...:
   ...:
Out[9]: [0, -1, 2, -1, 4]

In [10]: def my_func(x):
    ...:     if x % 2 ==0:
    ...:         return True
    ...:     return False
    ...:
    ...: [ x if my_func(x) else 999  for x in range(5)]
Out[10]: [0, 999, 2, 999, 4]
```

### `map()` 與生成器運算式

```python
# map(func, iterable) — lazy, returns iterator
nums = ["1", "2", "3"]
ints = list(map(int, nums))    # [1, 2, 3]

# map with lambda
doubled = list(map(lambda x: x * 2, [1, 2, 3]))  # [2, 4, 6]

# Generator expression (lazy list comprehension) — memory efficient
gen = (x**2 for x in range(1000000))   # nothing computed yet
total = sum(x**2 for x in range(1000000))  # computed on the fly

# Prefer generator expression over list comprehension inside sum/any/all/max/min
max_val = max(abs(x) for x in nums)
has_neg = any(x < 0 for x in nums)
```

### 三元（條件）運算式

```python
# syntax: <value_if_true> if <condition> else <value_if_false>
x = 5
result = "even" if x % 2 == 0 else "odd"   # "odd"

# Nested ternary (keep shallow — hard to read beyond two levels)
sign = "positive" if x > 0 else ("zero" if x == 0 else "negative")

# Common LC use
ans = left if left else right          # return whichever is not None
val = node.val if node else 0
```

## Dict 與 Set

### Dict 的 `get()`、`setdefault()`、生成式

```python
d = {'a': 1, 'b': 2}

# get(key, default) — safe access
d.get('c', 0)     # 0  (no KeyError)
d.get('a', 0)     # 1

# setdefault(key, default) — insert if missing, return value
d.setdefault('c', []).append(3)   # d['c'] = [3]
d.setdefault('c', []).append(4)   # d['c'] = [3, 4]

# dict comprehension
squares = {x: x**2 for x in range(5)}
# {0: 0, 1: 1, 2: 4, 3: 9, 4: 16}

# invert a dict (assuming unique values)
inv = {v: k for k, v in squares.items()}

# filter dict
evens = {k: v for k, v in squares.items() if v % 2 == 0}
```

### Set 運算

```python
a = {1, 2, 3, 4}
b = {3, 4, 5, 6}

# Basic ops
a | b    # union        {1, 2, 3, 4, 5, 6}
a & b    # intersection {3, 4}
a - b    # difference   {1, 2}
a ^ b    # symmetric diff {1, 2, 5, 6}

# Membership: O(1) average
3 in a   # True

# Mutation
a.add(5)
a.discard(99)   # no error if missing (vs remove() which raises KeyError)
a.remove(1)     # raises KeyError if missing

# NOTE: can directly remove a specific element from a set by value (not index)
# Common in sliding window problems (LC 3)
seen = set()
seen.add('a')
seen.remove('a')   # removes 'a' directly — no index needed

# Set comprehension
squares = {x**2 for x in range(5)}   # {0, 1, 4, 9, 16}

# Freeze (hashable, usable as dict key)
fs = frozenset([1, 2, 3])
```

### 用 `or` 檢查「其中一個元素存在」

```text
In [8]: def test(l1, l2):
   ...:     if l1 or l2:
   ...:         return l1 or l2
   ...:
   ...: res = test("l1", None)
   ...: print (res)
   ...:
   ...: res2 = test(None, "l2")
   ...: print (res2)
l1
l2
```

## 結構、作用域與回傳值

### 二維陣列（矩陣）初始化

```python
#-------------------------------------------------
# CORRECT way — use list comprehension (independent rows)
#-------------------------------------------------
m, n = 3, 4
grid = [[0] * n for _ in range(m)]
grid[0][0] = 1
# Only grid[0][0] is changed

#-------------------------------------------------
# WRONG way — all rows share the same list!
#-------------------------------------------------
bad = [[0] * n] * m
bad[0][0] = 1
# ALL rows become [1, 0, 0, 0]  — common bug!

#-------------------------------------------------
# Common DP patterns
#-------------------------------------------------
# 1D DP
dp = [0] * (n + 1)

# 2D DP (m rows, n cols, filled with False)
dp = [[False] * (n + 1) for _ in range(m + 1)]

# Fill with infinity
dp = [[float('inf')] * n for _ in range(m)]
```

### 巢狀函式中的 `nonlocal` 與 `global`

```python
# nonlocal: modify a variable in the ENCLOSING (not global) scope
def outer():
    count = 0
    def inner():
        nonlocal count
        count += 1
    inner()
    inner()
    print(count)  # 2

# Without nonlocal, count += 1 raises UnboundLocalError

# global: modify a module-level variable inside a function
total = 0
def add(x):
    global total
    total += x

# Common LC pattern: DFS with mutable result
def maxDepth(root):
    res = [0]
    def dfs(node, depth):
        if not node:
            return
        res[0] = max(res[0], depth)  # list trick avoids nonlocal
        dfs(node.left, depth + 1)
        dfs(node.right, depth + 1)
    dfs(root, 1)
    return res[0]
```

### `isinstance()` 與型別檢查

```python
isinstance(3, int)        # True
isinstance(3.0, float)    # True
isinstance("hi", str)     # True
isinstance([], list)      # True
isinstance({}, dict)      # True

# Check multiple types at once
isinstance(3, (int, float))   # True

# type() for exact type (no inheritance)
type(3) == int    # True
type(True) == int # False  (bool is subclass of int, but type() is exact)
isinstance(True, int)  # True  (True IS an int!)
```

### 用小型類別承載多個回傳值


當一個 DFS／遞迴需要**一次回傳好幾個值**（例如高度 + 大小 + 一個旗標）時，Java 的慣用寫法是開一個小的 `private static class SubtreeInfo`。在 Python 裡最接近的做法是 `@dataclass`、普通類別，或 `NamedTuple`。

```java
// java — the pattern we want to port
private static class SubtreeInfo {
    int height;
    int size;
    boolean isPerfect;
}
```

#### **選項 1：`@dataclass`（推薦）**

`@dataclass` 會自動產生 `__init__`、`__repr__`、`__eq__` — 樣板程式碼最少、可讀性最高。

```python
# python
from dataclasses import dataclass

@dataclass
class SubtreeInfo:
    height: int
    size: int
    is_perfect: bool


def dfs(node):
    # returns SubtreeInfo carrying 3 values up the recursion
    if node is None:
        return SubtreeInfo(0, 0, True)

    left = dfs(node.left)
    right = dfs(node.right)

    is_perfect = (
        left.is_perfect
        and right.is_perfect
        and left.height == right.height
    )

    size = left.size + right.size + 1
    height = max(left.height, right.height) + 1

    if is_perfect:
        perfect_sizes.append(size)

    return SubtreeInfo(height, size, is_perfect)

# usage
info = dfs(root)
print(info.height, info.size, info.is_perfect)
```

#### **選項 2：傳統類別（不需 import）**

```python
# python
class SubtreeInfo:
    def __init__(self, height, size, is_perfect):
        self.height = height
        self.size = size
        self.is_perfect = is_perfect

# usage is identical
info = dfs(root)
print(info.height)
```

#### **選項 3：`NamedTuple`（輕量 + 不可變）**

當這組資料應該是**唯讀**時就用它（也能像 tuple 一樣解包）。

```python
# python
from typing import NamedTuple

class SubtreeInfo(NamedTuple):
    height: int
    size: int
    is_perfect: bool

info = dfs(root)
print(info.height)          # attribute access
h, s, p = info              # tuple unpacking also works
```

#### **快速比較**

| 選項 | 樣板程式碼 | 可變嗎？ | 最適合 |
|--------|-------------|----------|----------|
| `@dataclass`   | 少  | 可（要不可變就加 `frozen=True`） | **預設選擇** — 乾淨又好讀 |
| 普通類別    | 多 | 可  | 不允許 import／非常舊的 Python |
| `NamedTuple`   | 少  | **不可** | 不可變的資料組，同時能 tuple 解包 |

> **偷懶的替代做法**：一次性的 DFS 你其實可以直接 `return (height, size, is_perfect)` 再解包 — 但一旦欄位到 3 個以上，具名類別／`NamedTuple` 好讀太多了。對 LeetCode 風格的解法而言，`@dataclass` 通常是取代 Java `private static class` 最乾淨的選擇。

> **經驗法則：** 如果你*修改*的是一個共用容器（`append`/`add`），你就必須把它復原（`pop`/`remove`）。如果你每次呼叫都建立一個*新*物件（字串串接、`tmp + [x]`、tuple），那份複本本身就是回溯 — 沒有東西需要復原。另見 [0-2) 賦值 vs 淺／深複製](#assignment-vs-shallow-copy-vs-deep-copy)。
### `eval()`

```text
# https://www.runoob.com/python/python-func-eval.html
# https://www.programiz.com/python-programming/methods/built-in/eval
# The eval() method parses the expression passed to this method and runs python expression (code) within the program.

# LC 640
# LC 150

# syntax : eval(expression[, globals[, locals]])

# eample
In [51]: x = 7
    ...: eval('3 * x')
    ...:
Out[51]: 21

In [52]: eval ('2 + 2')
    ...:
Out[52]: 4

In [53]: n = 81
    ...: eval('n + 4')
Out[53]: 85
```

### 對較長的陣列做交換

```python
if len(l1) < len(l2):
   l1, l2 = l2, l1
```

### DFS 路徑：`str`（不可變、不用回溯）vs `list`（可變、需要回溯）


在 DFS／回溯遞迴中往下傳 `path` 時，**資料型別決定了你必不必須復原（回溯）**：

```text
- if path is `str` type, we DON'T need to undo (backtrack)
     -> since it's IMMUTABLE.

- if path is `array` ([]) type, we NEED to undo (backtrack)
     -> since it's MUTABLE.
```

**為什麼？**
- **`str`** 是不可變的：`path + "->" + str(node.val)` 每次呼叫都會建出一個**全新的字串**。父層的 `path` 完全沒被動到，所以每個分支自動拿到自己那份獨立的複本 — 沒有東西要復原。
- **`list`** 是可變的：`path.append(...)` 修改的是所有遞迴呼叫**共用的那同一個物件**。探索完一個分支之後，你必須 `path.pop()` 把狀態還原給兄弟分支 — 否則殘留資料會跨分支外洩。

```python
# LC 257 - Binary Tree Paths
# https://github.com/yennanliu/CS_basics/blob/master/leetcode_python/Depth-First-Search/binary-tree-paths.py

#-------------------------------------------------
# CASE 1) path as ARRAY (mutable) -> MUST backtrack (path.pop())
#-------------------------------------------------
class Solution(object):
    def binaryTreePaths(self, root):
        self.res = []
        self.helper(root, [])      # use array ([]) as tmp cache
        return self.res

    def helper(self, root, path):
        if not root:
            return
        path.append(str(root.val))            # mutate shared list
        # leaf node
        if not root.left and not root.right:
            self.res.append("->".join(path))
        else:
            self.helper(root.left, path)
            self.helper(root.right, path)
        # NOTE !!! backtrack at the final stage (undo the append)
        path.pop()

#-------------------------------------------------
# CASE 2) path as STRING (immutable) -> NO backtrack needed
#-------------------------------------------------
class Solution(object):
    def binaryTreePaths(self, root):
        res = []
        def dfs(node, cur):
            if not node:
                return
            cur = str(node.val) if cur == "" else cur + "->" + str(node.val)  # new string each call
            if not node.left and not node.right:
                res.append(cur)
                return
            # NOTE !!! no path.pop() — each branch got its own string copy
            dfs(node.left, cur)
            dfs(node.right, cur)
        dfs(root, "")
        return res
```

**同樣的道理，其他不可變的載體** — tuple 和「傳一個新 list 進去」也同樣不用顯式 pop，因為它們交給每個子節點的是一個全新的物件，而不是共用同一個：

```python
# trick: build a NEW list per call (tmp + [x]) instead of append+pop
def dfs(r, tmp):
    if not r.left and not r.right:
        ans.append("->".join(tmp))
    if r.left:
        dfs(r.left, tmp + [str(r.left.val)])    # tmp + [..] -> new list, no pop
    if r.right:
        dfs(r.right, tmp + [str(r.right.val)])
```

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

```python
# LC 113 - Path Sum II
# https://github.com/yennanliu/CS_basics/blob/master/leetcode_python/Depth-First-Search/path-sum-ii.py
class Solution(object):
    def pathSum(self, root, targetSum):
        self.res = []
        if not root:
            return self.res
        self.helper(root, targetSum, 0, [])
        return self.res

    def helper(self, root, targetSum, cur_sum, cache):
        if not root:
            return

        cur_sum += root.val        # int  -> rebinds LOCAL name to a new int (immutable)
        cache.append(root.val)     # list -> mutates the ONE shared list

        if not root.left and not root.right and cur_sum == targetSum:
            self.res.append(cache[:])    # snapshot — cache[:] copies, else later pops corrupt it

        self.helper(root.left,  targetSum, cur_sum, cache)
        self.helper(root.right, targetSum, cur_sum, cache)

        cache.pop()                # MUST backtrack the list ...
        # NOTE: NO `cur_sum -= root.val` — the int never changed for the parent
```

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

### 實作對照 — LC 445 Add Two Numbers II 與 LC 394 Decode String

- String -> Int
```text
# 445 Add Two Numbers II
# 394 Decode String
def str_2_int(x):
    r=0
    for i in x:
        r = int(r)*10 + int(i)
        print (i, r)
    return r

def str_2_int_v2(x):
    res = 0
    for i in x:
        res = (res + int(i) % 10) * 10
    return int(res / 10)

# example 1
x="131"
r=str_2_int(x)
print (r)
# 1 1
# 3 13
# 1 131
# 131

# examle 2
In [62]: z
Out[62]: '5634'

In [63]: ans = 0

In [64]: for i in z:
    ...:     ans = 10 * ans + int(i)
    ...:

In [65]: ans
Out[65]: 5634
```

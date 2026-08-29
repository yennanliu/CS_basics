# Iterator（迭代器）

> **範圍** — 迭代器契約：`hasNext`／`next` 搭配延遲狀態、攤平、預看與合併，單一觀念獨立成篇。
> **另見**：[design.md](./design.md) — 內嵌迭代器的大型設計題；[stack.md](./stack.md) — 撐起巢狀與 BST 迭代器的那個堆疊；[heap.md](./heap.md) — k 路合併的迭代。

## LeetCode 題目清單

- [Iterator](https://leetcode.com/problem-list/iterator/)
- [Design](https://leetcode.com/problem-list/design/)

## 0) 概念

**迭代器**用 `hasNext()` / `next()` 提供對集合的循序存取，同時不洩漏底層結構。
面試的變形通常要你**包裝**一個既有的迭代器（或巢狀結構），加上一個能力——
peek、攤平、合併——而且 `next()` 還要維持攤還 `O(1)`。

### 0-1) 種類

- **Peeking（預看）** — 看下一個元素但不消耗它（往前快取一個元素）
- **Flattening（攤平）** — 把巢狀／二維結構當成扁平的來走訪（用堆疊）
- **Merging（合併）** — 交錯多個迭代器（用堆積／佇列）

### 0-2) 模式

- **延遲 vs 提早** — 被問到才去算／取下一個元素；面對大型串流可以省記憶體
- **往前快取** — 先把一個元素抓進緩衝區，用來支援 `peek()`
- **迭代器堆疊** — 需要時才把子清單推入堆疊，藉此攤平巢狀清單

## 1) 通用形式

### 1-1) 基本操作

```python
# python
class MyIterator:
    def __init__(self, data):
        self.data = data
        self.idx = 0

    def hasNext(self):
        return self.idx < len(self.data)

    def next(self):
        val = self.data[self.idx]
        self.idx += 1
        return val
```

### 1-2) `hasNext()` / `next()` 契約 ⭐⭐⭐⭐⭐

迭代器面試最常見的死法，就是寫出一個會**消耗元素**的 `hasNext()`。
每個包裝層都必須遵守的契約：

1. **`hasNext()` 必須冪等** — 在 `next()` 之前呼叫 0 次、1 次還是 100 次，
   `next()` 給出的序列都得完全一樣。
2. **`hasNext()` 可以改內部狀態，但不能改可觀察狀態** — 它可以為了*判斷*而延遲地
   推進堆疊／從來源取值，但它挖出來的那個元素，仍然必須由下一次 `next()` 交出去。
3. **`next()` 只在 `hasNext()` 為真時有定義** — 該丟例外（`NoSuchElementException`）
   或回傳一個有寫在文件裡的哨兵值；絕不要默默回傳垃圾。
4. **`next()` 是攤還 O(1)** — 單次呼叫可以做 O(k) 的工，但走完 `n` 個元素的整趟
   必須維持在 O(n)。

「推進」這件工只有兩個合法的擺放位置。選一個並且貫徹到底——
混用就是重複消耗這類 bug 的源頭。

| 寫法 | 工作發生在哪 | `hasNext()` | 適合 |
|-------|------------------------|-------------|----------|
| **往前緩衝一格**（cache-ahead） | 建構子 + `next()` | 只檢查緩衝區旗標 | 包裝另一個迭代器、支援 `peek()`（LC 284） |
| **在 `hasNext()` 裡正規化** | `hasNext()` | 負責延遲拆解，必須可重入 | 巢狀／樹狀結構，「有沒有下一個」需要往下挖（LC 341、LC 173） |

```java
// java
// IDEA: idiom A - buffer one element ahead; hasNext() is a pure test.
//       Use an explicit boolean flag, NOT a null sentinel, so a stream that
//       legitimately contains null still works.
// time = O(1) amortized per next(), space = O(1) extra
class BufferedIterator<T> {
    private final Iterator<T> src;
    private T buffer;
    private boolean hasBuffered;

    BufferedIterator(Iterator<T> src) {
        this.src = src;
        advance();                       // prime once
    }

    private void advance() {             // the ONLY place that pulls from src
        hasBuffered = src.hasNext();
        buffer = hasBuffered ? src.next() : null;
    }

    public boolean hasNext() { return hasBuffered; }   // pure, idempotent

    public T peek() {                                  // non-destructive
        if (!hasBuffered) throw new NoSuchElementException();
        return buffer;
    }

    public T next() {
        if (!hasBuffered) throw new NoSuchElementException();
        T ret = buffer;
        advance();
        return ret;
    }
}
```

```python
# python
# IDEA: idiom B - do the lazy work inside hasNext(); it must be safe to call twice.
#       hasNext() only UNCOVERS the next element, next() is what removes it.
# time = O(1) amortized per next(), space = O(depth) for the pending stack
class LazyIterator:
    def __init__(self, items):
        self.stack = list(reversed(items))

    def hasNext(self):
        # normalize: pop away anything that is not a yieldable element.
        # re-entrant -> calling it again just re-checks an already-clean top
        while self.stack and not self._is_leaf(self.stack[-1]):
            top = self.stack.pop()
            self.stack.extend(reversed(self._children(top)))
        return len(self.stack) > 0

    def next(self):
        if not self.hasNext():
            raise StopIteration
        return self.stack.pop()          # ONLY next() consumes

    def _is_leaf(self, x):
        return not isinstance(x, list)

    def _children(self, x):
        return x
```

**面試官會追問、你要準備好的東西**

- *「支援 `remove()`」* — 你得記住 `next()` 剛剛回傳的那個元素，並且擋掉
  「還沒 `next()` 就 `remove()`」以及「連續兩次 `remove()`」。留一個 `lastReturned`
  欄位加一個 `canRemove` 布林值；`next()` 設起來，`remove()` 清掉。
  在往前緩衝一格的寫法下這其實很難：來源迭代器*早就*越過你想刪的那個元素了，
  所以 `remove()` 會刪錯人——這正是 `java.util` 的迭代器不做預取的原因。
- *「來源是無限的／一個超大檔案怎麼辦？」* — 上面那些延遲寫法本來就能處理；
  重點是強調沒有任何一版會把整個序列具現化出來。
- *「`next()` 真的是 O(1) 嗎？」* — 看 **2-5** 的攤還分析。

## 2) LC 範例

### 2-1) Peeking Iterator — LC 284

```python
# python
# LC 284. Peeking Iterator
# IDEA: wrap the given iterator and cache the next value so peek() is non-destructive
class PeekingIterator:
    def __init__(self, iterator):
        self.it = iterator
        self.buffer = self.it.next() if self.it.hasNext() else None

    def peek(self):
        return self.buffer

    def next(self):
        ret = self.buffer
        self.buffer = self.it.next() if self.it.hasNext() else None
        return ret

    def hasNext(self):
        return self.buffer is not None
```

### 2-2) Flatten Nested List Iterator — LC 341

```python
# python
# LC 341. Flatten Nested List Iterator
# IDEA: keep a stack of NestedInteger; lazily unwrap lists in hasNext()
class NestedIterator:
    def __init__(self, nestedList):
        # push in reverse so the first element ends up on top of the stack
        self.stack = nestedList[::-1]

    def next(self):
        return self.stack.pop().getInteger()

    def hasNext(self):
        while self.stack:
            top = self.stack[-1]
            if top.isInteger():
                return True
            self.stack.pop()
            self.stack.extend(top.getList()[::-1])
        return False
```

> **相關題——用顯式堆疊走訪樹。** LC 173（Binary Search Tree Iterator）
> 就是同一個「先推入邊界、需要時才展開」的形狀，套在中序走訪上
> （推入左脊、彈出、再推入右子樹的左脊）。完整模板放在
> [`stack.md`](stack.md) §2-19 和 [`bst.md`](bst.md) §2-5——
> **1-2** 的契約規則原封不動適用。

### 2-3) 遊程編碼／整段跳過的迭代器 — LC 900 ⭐⭐⭐⭐

**模式**：來源是**壓縮過的**（`[count, value, count, value, ...]`），而 `next(n)`
要求一次消耗 `n` 個元素。千萬不要把遊程展開——`count` 可以到 `10^9`。
**核心想法**：維護一個指向當前遊程的游標，加上這段遊程已經 `used` 掉多少，
然後**整段整段扣掉**，直到 `n` 塞得進當前這段為止。

```java
// java
// LC 900 - RLE Iterator
// IDEA: cursor (i) on the current run + how many of it are used.
//       While n overflows the remaining part of the run, consume the remainder
//       and jump to the next run. Exhausted -> -1.
// time = O(1) amortized per next() (each run is skipped at most once,
//        so a whole run of calls is O(len(encoding))), space = O(1)
class RLEIterator {
    private final int[] encoding;
    private int i = 0;        // index of the current COUNT
    private long used = 0;    // how many of encoding[i] already consumed

    public RLEIterator(int[] encoding) { this.encoding = encoding; }

    public int next(int n) {
        long k = n;           // long: count and n are both up to 1e9 -> int overflows
        while (i < encoding.length && used + k > encoding[i]) {
            k -= encoding[i] - used;   // eat what is left of this run
            used = 0;
            i += 2;                    // move to the next (count, value) pair
        }
        if (i >= encoding.length) return -1;   // stream exhausted
        used += k;
        return encoding[i + 1];
    }
}
```

```python
# python
# LC 900 - RLE Iterator
# IDEA: same cursor + used counter; Python ints don't overflow so no cast needed
# time = O(1) amortized per next(), space = O(1)
class RLEIterator:
    def __init__(self, encoding):
        self.enc = encoding
        self.i = 0        # index of the current count
        self.used = 0     # consumed portion of enc[i]

    def next(self, n):
        while self.i < len(self.enc) and self.used + n > self.enc[self.i]:
            n -= self.enc[self.i] - self.used
            self.used = 0
            self.i += 2
        if self.i >= len(self.enc):
            return -1
        self.used += n
        return self.enc[self.i + 1]
```

**陷阱**

- **長度為 0 的遊程**（`[0, 9]`）必須跳過——`while` 免費幫你處理掉了，
  因為任何 `n >= 1` 都會讓 `used + n > 0`。
- **Java 的溢位**：`used + n` 兩邊都逼近 `10^9` 時會超過 `int`。用 `long`。
- 一旦耗盡，之後每次 `next()` 都必須持續回傳 `-1`（`i >= length` 這個守衛是黏著的，
  因為 `i` 只增不減）。

### 2-4) 走訪一個生成序列的迭代器 — LC 1286 ⭐⭐⭐

**模式**：底下根本沒有集合——元素是**組合生成**出來的。天真解會在建構子裡
先算完所有 `C(n, k)` 種組合（`O(C(n,k) * k)` 記憶體）；迭代器版只存*當前*狀態，
並用 `O(k)` 推進。

**核心想法**：把選中的 `k` 個索引當成一個**里程表**。要推進時，找出最右邊那個
還沒到頂的索引（`idx[i] == i + n - k` 表示到頂），把它加一，再把它右邊全部
重設成連續的值。

```java
// java
// LC 1286 - Iterator for Combination
// IDEA: keep only the k current indices; advance like an odometer.
//       characters is already sorted, so index order == lexicographic order.
// time = O(k) per next(), O(1) hasNext(), space = O(k)  (NOT O(C(n,k)))
class CombinationIterator {
    private final char[] s;
    private final int k;
    private final int[] idx;      // current combination as indices into s
    private boolean done = false;

    public CombinationIterator(String characters, int combinationLength) {
        this.s = characters.toCharArray();
        this.k = combinationLength;
        this.idx = new int[k];
        for (int i = 0; i < k; i++) idx[i] = i;   // first combination = 0,1,...,k-1
    }

    public boolean hasNext() { return !done; }    // pure, idempotent

    public String next() {
        StringBuilder sb = new StringBuilder();
        for (int i : idx) sb.append(s[i]);        // emit CURRENT, then advance

        int i = k - 1;
        while (i >= 0 && idx[i] == i + s.length - k) i--;   // idx[i] maxed out?
        if (i < 0) {
            done = true;                          // last combination just emitted
        } else {
            idx[i]++;
            for (int j = i + 1; j < k; j++) idx[j] = idx[j - 1] + 1;  // reset the tail
        }
        return sb.toString();
    }
}
```

```python
# python
# LC 1286 - Iterator for Combination
# IDEA: odometer over the k chosen indices; emit current, then step forward
# time = O(k) per next(), O(1) hasNext(), space = O(k)
class CombinationIterator:
    def __init__(self, characters, combinationLength):
        self.s = characters
        self.k = combinationLength
        self.idx = list(range(self.k))
        self.done = False

    def hasNext(self):
        return not self.done

    def next(self):
        res = "".join(self.s[i] for i in self.idx)   # emit current

        i = self.k - 1
        while i >= 0 and self.idx[i] == i + len(self.s) - self.k:
            i -= 1
        if i < 0:
            self.done = True
        else:
            self.idx[i] += 1
            for j in range(i + 1, self.k):
                self.idx[j] = self.idx[j - 1] + 1
        return res
```

**變形——位元遮罩計數器**（同樣是 LC 1286）：因為 `n <= 15`，可以讓 `mask` 從
`(1 << n) - 1` 往下數到 `0`，只留 `popcount == k` 的遮罩；第 `n-1-i` 個位元被設起來
代表選了 `s[i]`，所以遞減的遮罩剛好照字典序吐出來。狀態是 `O(1)`，但 `next()`
最壞已經不是 `O(k)` 了，因為它可能要掃過一堆被否決的遮罩。

**和回溯的對照**：普通的 `combine()` 遞迴會產生同樣的序列，但遞迴沒辦法暫停。
里程表*就是*把那份遞迴的呼叫堆疊攤平成 `idx[]` 之後的樣子——
而這正是「把它做成迭代器」的意思。

### 2-5) 攤還 O(1) 的 `next()`——延遲搬移 — LC 232 ⭐⭐⭐⭐

**模式**：以儲存結構不支援的順序來消耗資料。與其每次操作都付錢，不如
**把重排延後，只在輸出側見底時一次批量做完**。這就是 **1-2** 規則 4 背後的
標準證明義務。

**核心想法**：兩個堆疊。`in` 收 push；`out` 負責讀取。**只有在 `out` 空了**
才從 `in` 補貨——那個 `if` 正是讓它變成攤還 O(1) 的關鍵；每次呼叫都急著搬，
就會變成每次操作 O(n)。

```java
// java
// LC 232 - Implement Queue using Stacks
// IDEA: in-stack absorbs writes, out-stack serves reads; flip only when out is empty
// time = O(1) amortized per op (worst case O(n) for one pop), space = O(n)
class MyQueue {
    private final Deque<Integer> in = new ArrayDeque<>();
    private final Deque<Integer> out = new ArrayDeque<>();

    public void push(int x) { in.push(x); }

    public int pop()  { shift(); return out.pop(); }
    public int peek() { shift(); return out.peek(); }   // the "hasNext must not consume" analogue

    public boolean empty() { return in.isEmpty() && out.isEmpty(); }

    private void shift() {
        if (out.isEmpty()) {            // the guard IS the amortization
            while (!in.isEmpty()) out.push(in.pop());
        }
    }
}
```

```python
# python
# LC 232 - Implement Queue using Stacks
# time = O(1) amortized per op, space = O(n)
class MyQueue:
    def __init__(self):
        self.in_st = []
        self.out_st = []

    def push(self, x):
        self.in_st.append(x)

    def _shift(self):
        if not self.out_st:                  # only when drained
            while self.in_st:
                self.out_st.append(self.in_st.pop())

    def pop(self):
        self._shift()
        return self.out_st.pop()

    def peek(self):
        self._shift()
        return self.out_st[-1]

    def empty(self):
        return not self.in_st and not self.out_st
```

**攤還論證（面試時要講出來）**

> 每個元素被推進 `in` 剛好一次、搬到 `out` 剛好一次、從 `out` 彈出剛好一次——
> **整個生命週期就 3 次堆疊操作，不管呼叫順序長怎樣**。所以 `n` 次操作總共
> `O(n)`，也就是攤還 `O(1)`。*單一次* `pop()` 仍然可能是 `O(n)`；攤還不等於
> 最壞情況，而面試官真的會戳這個差別。

用會計法／位能法來看：每次 `push` 收 3 個單位——1 個用來做這次 push，
2 個存起來付未來的搬移與彈出。存款永遠不會變負的，所以攤還成本是常數。

同一套推論也適用於 **2-2** / LC 341 的延遲堆疊：每個 `NestedInteger` 最多被推入
與彈出一次，所以整趟走訪是 `O(總節點數)`，即使某一次 `hasNext()` 呼叫可能要拆開
一條巢狀很深的鏈。

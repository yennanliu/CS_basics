# Set — 實戰題解

> **範圍** — [set.md](./set.md) 背後的解法檔案庫：十四題，依「這個集合到底被拿來做什麼」分組 — 記住看過什麼、集合運算、用 O(1) 索引取代掃描，或是當成更大演算法裡的一個零件。
> **另見**：[set.md](./set.md) — 母表：型別、基本操作、決策框架，以及 Python 對 Java 的差異筆記；[hash_map.md](./hash_map.md) — 當你除了 key 還需要 value；[hashing.md](./hashing.md) — 怎麼設計 key 本身，LC 694 和 LC 939 就靠這個；[bfs.md](./bfs.md) — LC 127 裡的前沿集合；[design.md](./design.md) — 從設計題角度看 LC 380。

## LeetCode 題目清單

- [Hash Table](https://leetcode.com/problem-list/hash-table/)

## 概觀

這是 [set.md](./set.md) 的長尾 — 原本那份有 79% 都是範例尾巴。母表留下操作、實作比較與決策框架；這份留下*套用*它們的題目。

### 關鍵性質
- **複雜度**：每次成員查詢平均 O(1)，LC 975 用到的有序集合則是 O(log n) — 重點就是拿它取代一次 O(n) 掃描
- **核心想法**：集合幾乎從來不是用來*存*東西的。它是用來回答「我看過這個嗎」、把一個形狀編碼成 key，或當作搜尋的前沿 — 這份文件就是照這三種用途分組的
- **什麼時候用**：你只需要成員判斷、不需要別的時候；一旦你需要附帶的值，那就是[雜湊表](./hash_map.md)的事了


## 「我之前看過這個嗎？」

### 1) Contains Duplicate — LC 217 ⭐⭐⭐⭐⭐

```python
# LC 217. Contains Duplicate
# V0
# IDEA: Set to detect duplicates
class Solution:
    def containsDuplicate(self, nums):
        return len(nums) != len(set(nums))

# V0'
# IDEA: Build set while checking
class Solution:
    def containsDuplicate(self, nums):
        seen = set()
        for num in nums:
            if num in seen:
                return True
            seen.add(num)
        return False
```

```java
// Java
// LC 217
public boolean containsDuplicate(int[] nums) {
    Set<Integer> seen = new HashSet<>();
    for (int num : nums) {
        if (seen.contains(num)) {
            return true;
        }
        seen.add(num);
    }
    return false;
}
```

#### 變化型 A：用兩個集合去重*輸出* — LC 187

**變化點**：當答案是「所有重複出現的東西」時，一個 `seen` 集合不夠 — 出現 3 次的項目會被回報兩次。加一個 **`repeated` 集合**就能免費吸收掉這些重複。

```python
# python
# LC 187 - Repeated DNA Sequences
# IDEA: seen-set for detection + 2nd set so the output is auto-deduped
# time = O(n * L) (L = 10, substring cost), space = O(n * L)
class Solution(object):
    def findRepeatedDnaSequences(self, s):
        seen, repeated = set(), set()
        for i in range(len(s) - 9):
            sub = s[i:i + 10]
            if sub in seen:
                repeated.add(sub)   # 2nd set keeps the output deduped
            else:
                seen.add(sub)
        return list(repeated)
```

```java
// java
// LC 187 - Repeated DNA Sequences
// time = O(n * L), space = O(n * L)
public List<String> findRepeatedDnaSequences(String s) {
    Set<String> seen = new HashSet<>(), repeated = new HashSet<>();
    for (int i = 0; i + 10 <= s.length(); i++) {
        String sub = s.substring(i, i + 10);
        // add() returns false when the element was already present -> one lookup, not two
        if (!seen.add(sub)) repeated.add(sub);
    }
    return new ArrayList<>(repeated);
}
```

> **慣用法**：Java 的 `set.add(x)` 在 `x` 已存在時回傳 `false`，`set.remove(x)` 在真的移除時回傳 `true`。直接用回傳值，不要另外呼叫一次 `contains()`。

#### 變化型 B：查的是*轉換過*的 key，不是元素本身 — LC 532

**變化點**：不要問「我看過 `num` 嗎？」，改問「`num + k` 在集合裡嗎？」。去重的做法是走訪**集合**（不是陣列），這樣每個相異的配對只會被算一次。`k == 0` 是另一個問題（需要次數）→ 退回去用頻率表。`k < 0` **不存在**任何合法配對，必須在進到那個分支前就擋掉，否則會回傳重複元素的數量。

```python
# python
# LC 532 - K-diff Pairs in an Array
# IDEA: pairs = distinct x where x+k also exists; k==0 needs counts, not a set
# time = O(n), space = O(n)
class Solution(object):
    def findPairs(self, nums, k):
        ### NOTE !!! k < 0 has NO valid pairs -- |i - j| = k is impossible for a negative k.
        ###          Falling through to the k <= 0 branch would return the duplicate count.
        if k < 0:
            return 0
        if k > 0:
            pool = set(nums)
            return sum(1 for x in pool if x + k in pool)
        # k == 0 is a different question: how many values occur more than once
        from collections import Counter
        return sum(1 for x, c in Counter(nums).items() if c > 1)
```

```java
// java
// LC 532 - K-diff Pairs in an Array
// time = O(n), space = O(n)
public int findPairs(int[] nums, int k) {
    /** NOTE !!! k < 0 has no valid pairs; without this guard it returns the duplicate count */
    if (k < 0) return 0;
    if (k > 0) {
        Set<Integer> pool = new HashSet<>();
        for (int n : nums) pool.add(n);
        int cnt = 0;
        for (int x : pool) if (pool.contains(x + k)) cnt++;  // probe x+k, not x
        return cnt;
    }
    Map<Integer, Integer> freq = new HashMap<>();
    for (int n : nums) freq.merge(n, 1, Integer::sum);
    int cnt = 0;
    for (int c : freq.values()) if (c > 1) cnt++;
    return cnt;
}
```

### 2) Single Number — LC 136 — 以及為什麼這題用集合是選錯工具

```python
# LC 136. Single Number
# V0
# IDEA: XOR all numbers (duplicates cancel out)
class Solution:
    def singleNumber(self, nums):
        result = 0
        for num in nums:
            result ^= num
        return result

# V0'
# IDEA: Set addition/removal
class Solution:
    def singleNumber(self, nums):
        return 2 * sum(set(nums)) - sum(nums)
```

### 3) Happy Number — LC 202 — 把集合當環偵測器

```python
# LC 202. Happy Number
# V0
# IDEA: Use set to detect cycles
class Solution:
    def isHappy(self, n):
        def get_next(num):
            total = 0
            while num > 0:
                digit = num % 10
                total += digit * digit
                num //= 10
            return total

        seen = set()
        while n != 1 and n not in seen:
            seen.add(n)
            n = get_next(n)

        return n == 1
```

### 4) Linked List Cycle Detection — LC 141 — 同一個想法搬到鏈結串列上

```python
# LC 141. Linked List Cycle
# V0
# IDEA: Use set to track visited nodes
class Solution:
    def hasCycle(self, head):
        visited = set()
        current = head

        while current:
            if current in visited:
                return True
            visited.add(current)
            current = current.next

        return False

# V0'
# IDEA: Two pointers (Floyd's algorithm) - O(1) space
class Solution:
    def hasCycle(self, head):
        if not head:
            return False

        slow = head
        fast = head.next

        while slow != fast:
            if not fast or not fast.next:
                return False
            slow = slow.next
            fast = fast.next.next

        return True
```

## 集合運算

### 5) Intersection of Two Arrays — LC 349

```python
# LC 349. Intersection of Two Arrays
# V0
# IDEA: Set intersection
class Solution:
    def intersection(self, nums1, nums2):
        return list(set(nums1) & set(nums2))

# V0'
# IDEA: Convert to sets and use intersection
class Solution:
    def intersection(self, nums1, nums2):
        set1 = set(nums1)
        set2 = set(nums2)
        return list(set1.intersection(set2))
```

```java
// Java
// LC 349
public int[] intersection(int[] nums1, int[] nums2) {
    Set<Integer> set1 = new HashSet<>();
    for (int num : nums1) {
        set1.add(num);
    }

    Set<Integer> result = new HashSet<>();
    for (int num : nums2) {
        if (set1.contains(num)) {
            result.add(num);
        }
    }

    return result.stream().mapToInt(i -> i).toArray();
}
```

### 6) Word Pattern — LC 290 — 一對一映射要雙向都檢查

```python
# LC 290. Word Pattern
# V0
# IDEA: Use 2 sets to track bijection
class Solution:
    def wordPattern(self, pattern, s):
        words = s.split()

        if len(pattern) != len(words):
            return False

        char_to_word = {}
        word_to_char = {}

        for c, word in zip(pattern, words):
            if c in char_to_word:
                if char_to_word[c] != word:
                    return False
            else:
                char_to_word[c] = word

            if word in word_to_char:
                if word_to_char[word] != c:
                    return False
            else:
                word_to_char[word] = c

        return True
```

## 把集合當索引

### 7) Longest Consecutive Sequence — LC 128 — 只從一段連續的開頭起算 ⭐⭐⭐⭐⭐


#### 核心想法

**集合 + 「序列起點」閘門 — O(n) 時間**

關鍵觀察：一個數字 `num` 是**某段序列的起點**，當且僅當 `num - 1` 不在集合裡。這個閘門避免了從序列中間每個元素都重數一次。

```text
Without the gate: starting from 2 in [1,2,3,4] would count [2,3,4] (length 3),
                  double-counting work already done from 1.
With the gate:    only 1 passes (1-1=0 not in set), so we count exactly once.
```

找到序列起點之後，就用 `num + length` 在集合裡查詢來往下延伸 — 每一步都是 O(1)。整體來看每個元素最多被拜訪兩次 → **總共 O(n)**。

```text
Pointer role:
  num    — sequence start (anchor): only enters if num-1 ∉ set
  length — implicit "right pointer": increments while num+length ∈ set
```

```python
# python
# LC 128. Longest Consecutive Sequence
# Time: O(n), Space: O(n)
class Solution(object):
    def longestConsecutive(self, nums):
        num_set = set(nums)
        longest = 0

        for num in num_set:
            # Gate: only start counting from the sequence's first element
            if num - 1 not in num_set:
                length = 1

                # Extend right as long as the next consecutive number exists
                while num + length in num_set:
                    length += 1

                longest = max(longest, length)

        return longest
```

**手動追蹤 — `nums = [100, 4, 200, 1, 3, 2]`：**
```text
num_set = {100, 4, 200, 1, 3, 2}

num=100: 99 ∉ set → start, extend: 101 ∉ set → length=1
num=4:    3 ∈ set → SKIP (not a start)
num=200: 199 ∉ set → start, extend: 201 ∉ set → length=1
num=1:    0 ∉ set → start, extend: 2∈,3∈,4∈,5∉ → length=4  ← winner
num=3:    2 ∈ set → SKIP
num=2:    1 ∈ set → SKIP

return 4
```

```java
// java
// LC 128 - Longest Consecutive Sequence
// time: O(n), space: O(n)
public int longestConsecutive(int[] nums) {
    Set<Integer> numSet = new HashSet<>();
    for (int num : nums) numSet.add(num);

    int longest = 0;

    for (int num : numSet) {
        // Gate: only process sequence starts
        if (!numSet.contains(num - 1)) {
            int length = 1;

            while (numSet.contains(num + length)) {
                length++;
            }

            longest = Math.max(longest, length);
        }
    }

    return longest;
}
```

#### 為什麼是 O(n) 而不是 O(n²)？

內層 `while` 看起來每次外層迭代都可能跑 O(n)，但那個**閘門**保證每個數字最多只當一次序列起點。把所有起點加起來，所有內層迴圈的總步數剛好等於 `len(nums)`。所以攤還下來每個元素 O(1) → **總共 O(n)**。

#### 類似題目

| 題目 | LC# | 差異 | 關鍵技巧 |
|---------|-----|------------|-----------|
| Longest Consecutive Sequence | 128 | 未排序陣列 | 集合 + 序列起點閘門 |
| Arithmetic Slices | 413 | 已排序、固定差值 1 | DP／滑動視窗 |
| Missing Ranges | 163 | 找區間中的缺口 | 走訪「預期值 vs 實際值」 |
| Find All Numbers Disappeared | 448 | 1..n 範圍，找缺的 | 原地標記或用集合 |
| Longest Arithmetic Subsequence | 1027 | 任意公差，不只 1 | DP + hashmap |
| Contains Duplicate | 217 | 只要偵測有沒有重複 | 比對集合大小 |

### 8) Valid Sudoku — LC 36 — 九個列、行、宮格的集合

```python
# LC 36. Valid Sudoku
# V0
# IDEA: Use sets to track seen values
class Solution:
    def isValidSudoku(self, board):
        # Track seen elements in rows, cols, boxes
        seen = set()

        for i in range(9):
            for j in range(9):
                if board[i][j] != '.':
                    val = board[i][j]
                    box_idx = (i // 3) * 3 + j // 3

                    # Create unique keys for row, col, box
                    row_key = f"row_{i}_{val}"
                    col_key = f"col_{j}_{val}"
                    box_key = f"box_{box_idx}_{val}"

                    if row_key in seen or col_key in seen or box_key in seen:
                        return False

                    seen.add(row_key)
                    seen.add(col_key)
                    seen.add(box_key)

        return True
```

#### 變化型：同樣三組集合，但**回溯時要加入／移除** — LC 37

**變化點**：LC 36 只做驗證，所以集合只會單調變大。LC 37 是要*解出來*，每次落子都必須能撤銷 — 集合於是變成一個**可變的限制索引**：遞迴前 `add`，分支失敗就 `remove`。這個 O(1) 的撤銷，正是集合比「重掃一次列／行／宮」划算的地方。

```python
# python
# LC 37 - Sudoku Solver
# IDEA: 3 constraint sets (row/col/box) + backtracking; undo = set.remove()
# time = O(9^E) worst (E = empty cells), space = O(E) recursion + O(81) sets
class Solution(object):
    def solveSudoku(self, board):
        rows = [set() for _ in range(9)]
        cols = [set() for _ in range(9)]
        boxes = [set() for _ in range(9)]
        empties = []

        for i in range(9):
            for j in range(9):
                v = board[i][j]
                if v == '.':
                    empties.append((i, j))
                else:
                    rows[i].add(v); cols[j].add(v); boxes[(i // 3) * 3 + j // 3].add(v)

        def dfs(k):
            if k == len(empties):
                return True
            i, j = empties[k]
            b = (i // 3) * 3 + j // 3
            for v in "123456789":
                if v in rows[i] or v in cols[j] or v in boxes[b]:
                    continue                                       # O(1) legality check
                rows[i].add(v); cols[j].add(v); boxes[b].add(v)     # place
                board[i][j] = v
                if dfs(k + 1):
                    return True
                rows[i].remove(v); cols[j].remove(v); boxes[b].remove(v)  # undo
                board[i][j] = '.'
            return False

        dfs(0)
```

```java
// java
// LC 37 - Sudoku Solver
// time = O(9^E) worst (E = empty cells), space = O(E) recursion + O(81) sets
Set<Character>[] rows, cols, boxes;
List<int[]> empties;

@SuppressWarnings("unchecked")
public void solveSudoku(char[][] board) {
    rows = new HashSet[9]; cols = new HashSet[9]; boxes = new HashSet[9];
    for (int i = 0; i < 9; i++) {
        rows[i] = new HashSet<>(); cols[i] = new HashSet<>(); boxes[i] = new HashSet<>();
    }
    empties = new ArrayList<>();

    for (int i = 0; i < 9; i++) {
        for (int j = 0; j < 9; j++) {
            char v = board[i][j];
            if (v == '.') empties.add(new int[]{i, j});
            else { rows[i].add(v); cols[j].add(v); boxes[(i / 3) * 3 + j / 3].add(v); }
        }
    }
    dfs(board, 0);
}

private boolean dfs(char[][] board, int k) {
    if (k == empties.size()) return true;
    int i = empties.get(k)[0], j = empties.get(k)[1], b = (i / 3) * 3 + j / 3;

    for (char v = '1'; v <= '9'; v++) {
        if (rows[i].contains(v) || cols[j].contains(v) || boxes[b].contains(v)) continue;
        rows[i].add(v); cols[j].add(v); boxes[b].add(v);            // place
        board[i][j] = v;
        if (dfs(board, k + 1)) return true;
        rows[i].remove(v); cols[j].remove(v); boxes[b].remove(v);   // undo on backtrack
        board[i][j] = '.';
    }
    return false;
}
```

### 9) Minimum Area Rectangle — LC 939 — 一個裝編碼過的點的集合

#### 核心想法

**對*複合* key 做成員查詢 — 把幾何搜尋變成 O(1) 查表。**

任取兩個點當**對角線**；它們決定的矩形是唯一確定的，所以另外兩個角的座標是*完全已知*的。剩下的問題只是它們存不存在 — 那是一次集合查詢，不是搜尋。

```text
(x1,y1) and (x2,y2) with x1!=x2 and y1!=y2  ->  need (x1,y2) and (x2,y1)

     (x1,y2) o---------o (x2,y2)
             |         |
             |         |
     (x1,y1) o---------o (x2,y1)
```

同一列或同一行的兩點不可能是對角線 → 跳過。暴力枚舉所有 4 元組是 O(n^4)；這個做法是 **O(n²)**。

**key 的編碼方式**：Python 可以直接對 `tuple` 做雜湊。Java 不能對 `int[]` 做雜湊，所以要嘛編碼成單一 `int`/`long`（`x * BIG + y`），要嘛用 `Set<String>`。

```python
# python
# LC 939 - Minimum Area Rectangle
# IDEA: fix a diagonal pair, the other 2 corners are determined -> O(1) set lookups
# time = O(n^2), space = O(n)
class Solution(object):
    def minAreaRect(self, points):
        pts = set(map(tuple, points))    # tuples are hashable; lists are not
        best = float('inf')
        n = len(points)

        for i in range(n):
            x1, y1 = points[i]
            for j in range(i + 1, n):
                x2, y2 = points[j]
                if x1 == x2 or y1 == y2:
                    continue             # same row/col -> not a diagonal
                if (x1, y2) in pts and (x2, y1) in pts:
                    best = min(best, abs(x1 - x2) * abs(y1 - y2))

        return 0 if best == float('inf') else best
```

```java
// java
// LC 939 - Minimum Area Rectangle
// time = O(n^2), space = O(n)
public int minAreaRect(int[][] points) {
    // int[] has no value-based hashCode -> encode (x,y) into ONE key (0 <= x,y <= 40000)
    Set<Integer> pts = new HashSet<>();
    for (int[] p : points) pts.add(p[0] * 40001 + p[1]);

    int best = Integer.MAX_VALUE;
    for (int i = 0; i < points.length; i++) {
        for (int j = i + 1; j < points.length; j++) {
            int x1 = points[i][0], y1 = points[i][1];
            int x2 = points[j][0], y2 = points[j][1];
            if (x1 == x2 || y1 == y2) continue;          // same row/col -> not a diagonal
            if (pts.contains(x1 * 40001 + y2) && pts.contains(x2 * 40001 + y1)) {
                best = Math.min(best, Math.abs(x1 - x2) * Math.abs(y1 - y2));
            }
        }
    }
    return best == Integer.MAX_VALUE ? 0 : best;
}
```

> **可雜湊性小抄** — Python：`tuple`/`frozenset` 可雜湊，`list`/`set`/`dict` 不行。Java：`int[]` 是以**identity** 做雜湊（`new HashSet<int[]>` 永遠什麼都查不到）— 請編碼成 `Integer`/`Long`/`String`，或用 `List<Integer>`，它是以值做雜湊的。

### 10) Number of Distinct Islands — LC 694 — 一個裝正規化形狀的集合

```python
# LC 694. Number of Distinct Islands
# V0
# IDEA: Use set to store unique island shapes
class Solution:
    def numDistinctIslands(self, grid):
        if not grid:
            return 0

        def dfs(i, j, i0, j0):
            # Record relative position from starting point
            if (0 <= i < len(grid) and 0 <= j < len(grid[0]) and
                grid[i][j] == 1):
                grid[i][j] = 0
                path.append((i - i0, j - j0))
                dfs(i+1, j, i0, j0)
                dfs(i-1, j, i0, j0)
                dfs(i, j+1, i0, j0)
                dfs(i, j-1, i0, j0)

        shapes = set()
        for i in range(len(grid)):
            for j in range(len(grid[0])):
                if grid[i][j] == 1:
                    path = []
                    dfs(i, j, i, j)
                    # Convert list to tuple for hashing
                    shapes.add(tuple(path))

        return len(shapes)
```

## 集合在其他演算法裡

### 11) Insert Delete GetRandom O(1) — LC 380 — 集合語意，陣列儲存 ⭐⭐⭐⭐


#### 核心想法

**集合 + 密集陣列 — 所謂的「randomized set」**

`HashSet` 提供 O(1) 的 `insert` / `remove` / `contains`，但它**沒辦法在 O(1) 內做 `getRandom()`** — 它沒有位置索引，要均勻隨機挑一個成員得花 O(n)。

陣列可以 O(1) 索引，但沒辦法 O(1) 判斷成員。**兩個都用**，並保持同步：

```text
arr  : dense array of members        -> getRandom = arr[rand(size)]     O(1)
idx  : member -> its position in arr -> contains / locate for delete    O(1)
```

唯一麻煩的是**刪除**：從陣列中間移除是 O(n)。解法是**把最後一個元素換到那個洞裡**，再把尾巴 pop 掉 — `arr` 裡的順序無所謂，反正我們只會隨機取樣。

```text
remove(2) from arr=[1,2,3,4], idx={1:0,2:1,3:2,4:3}

  step 1: overwrite hole with last     arr=[1,4,3,4]  idx[4]=1
  step 2: pop the tail                 arr=[1,4,3]
  step 3: drop the key                 idx={1:0,4:1,3:2}
```

```python
# python
# LC 380 - Insert Delete GetRandom O(1)
# IDEA: hash index (val -> position) + dense array; delete = swap-with-last
# time = O(1) per op, space = O(n)
import random

class RandomizedSet(object):
    def __init__(self):
        self.arr = []      # dense array of members
        self.idx = {}      # val -> position in arr

    def insert(self, val):
        if val in self.idx:
            return False
        self.idx[val] = len(self.arr)
        self.arr.append(val)
        return True

    def remove(self, val):
        if val not in self.idx:
            return False
        i = self.idx[val]
        last = self.arr[-1]
        self.arr[i] = last       # move last member into the hole
        self.idx[last] = i
        self.arr.pop()
        del self.idx[val]        # delete AFTER the overwrite (val may BE the last element)
        return True

    def getRandom(self):
        return random.choice(self.arr)
```

```java
// java
// LC 380 - Insert Delete GetRandom O(1)
// time = O(1) per op, space = O(n)
class RandomizedSet {
    private final List<Integer> arr = new ArrayList<>();        // dense array of members
    private final Map<Integer, Integer> idx = new HashMap<>();  // val -> position in arr
    private final Random rand = new Random();

    public boolean insert(int val) {
        if (idx.containsKey(val)) return false;
        idx.put(val, arr.size());
        arr.add(val);
        return true;
    }

    public boolean remove(int val) {
        Integer i = idx.get(val);
        if (i == null) return false;
        int last = arr.get(arr.size() - 1);
        arr.set(i, last);                 // move last member into the hole
        idx.put(last, i);
        arr.remove(arr.size() - 1);       // remove(int) = remove BY INDEX -> O(1) at the tail
        idx.remove(val);                  // remove AFTER the overwrite (val may BE last)
        return true;
    }

    public int getRandom() {
        return arr.get(rand.nextInt(arr.size()));
    }
}
```

> **兩個陷阱**：(1) `idx.remove(val)` 要放在 `idx.put(last, i)` **之後** — 當 `val` *就是*最後一個元素時，順序反過來會把你剛寫進去的 key 刪掉；(2) 在 Java 裡，`arr.remove(arr.size()-1)` 選到的是 `remove(int index)` 這個多載，傳 `Integer` 進去會呼叫到 `remove(Object)` 變成按值刪除。

### 12) Word Ladder — LC 127 — 用兩個前沿集合做雙向 BFS ⭐⭐⭐⭐


#### 核心想法

**兩個前沿集合 + 「刪除即代表拜訪過」 + O(1) 相遇判斷**

集合在這題做了三件完全不同的事，這也是為什麼這題是集合題而不是佇列題：

| 集合 | 職責 |
|-----|-----|
| `words` | 字典 — O(1) 回答「這是不是一個真的單字？」 |
| `words.remove(cand)` | **用刪除來標記拜訪過**，所以不需要額外的 `visited` 集合 |
| `begin` / `end` | 兩個 BFS 前沿 — `cand in end` 就是 O(1) 的**相遇判斷** |

**關鍵技巧**：從兩端一起搜，而且**永遠展開比較小的那個前沿**（只要把兩個集合的參考對調即可）。單向 BFS 會探索 `b^d` 個節點；在中間會合只要探索 `2 * b^(d/2)` — 在分支很多的單字圖上是巨大的勝利。

```text
one-directional:  begin ------------------------> end     b^d
bidirectional:    begin -------><------- end             2 * b^(d/2)
                            meet here
```

```python
# python
# LC 127 - Word Ladder
# IDEA: bidirectional BFS; frontiers are sets, deleting from the pool = marking visited
# time = O(N * L * 26), space = O(N * L)
import string

class Solution(object):
    def ladderLength(self, beginWord, endWord, wordList):
        words = set(wordList)
        if endWord not in words:
            return 0
        words.discard(beginWord)

        begin, end = {beginWord}, {endWord}
        steps = 1

        while begin and end:
            if len(begin) > len(end):          # always expand the SMALLER frontier
                begin, end = end, begin

            nxt = set()
            for w in begin:
                for i in range(len(w)):
                    for c in string.ascii_lowercase:
                        cand = w[:i] + c + w[i + 1:]
                        if cand in end:        # frontiers met -> done
                            return steps + 1
                        if cand in words:
                            words.remove(cand) # mark visited by deleting from the pool
                            nxt.add(cand)
            begin = nxt
            steps += 1

        return 0
```

```java
// java
// LC 127 - Word Ladder
// time = O(N * L * 26), space = O(N * L)
public int ladderLength(String beginWord, String endWord, List<String> wordList) {
    Set<String> words = new HashSet<>(wordList);
    if (!words.contains(endWord)) return 0;
    words.remove(beginWord);

    Set<String> begin = new HashSet<>(), end = new HashSet<>();
    begin.add(beginWord);
    end.add(endWord);

    int steps = 1;
    while (!begin.isEmpty() && !end.isEmpty()) {
        if (begin.size() > end.size()) {              // always expand the SMALLER frontier
            Set<String> tmp = begin; begin = end; end = tmp;
        }

        Set<String> next = new HashSet<>();
        for (String w : begin) {
            char[] ch = w.toCharArray();
            for (int i = 0; i < ch.length; i++) {
                char old = ch[i];
                for (char c = 'a'; c <= 'z'; c++) {
                    ch[i] = c;
                    String cand = new String(ch);
                    if (end.contains(cand)) return steps + 1;   // frontiers met
                    if (words.remove(cand)) next.add(cand);     // remove() == mark visited
                }
                ch[i] = old;
            }
        }
        begin = next;
        steps++;
    }
    return 0;
}
```

**手動追蹤 — `begin="hit"`、`end="cog"`、字典 `[hot,dot,dog,lot,log,cog]`：**
```text
words = {hot,dot,dog,lot,log,cog}          ("hit" discarded up front)

steps=1  begin={hit}      end={cog}         expand hit -> nxt={hot}
steps=2  begin={hot}      end={cog}         expand hot -> nxt={dot,lot}
         |begin|=2 > |end|=1  -> SWAP
steps=3  begin={cog}      end={dot,lot}     expand cog -> nxt={dog,log,cog*}
         |begin|=3 > |end|=2  -> SWAP
steps=4  begin={dot,lot}  end={dog,log,cog}
         "dot" -> "dog"  IS IN end  ->  return steps + 1 = 5

* cog re-enters its own frontier (it is still in the pool). Harmless — add
  words.discard(endWord) up front if you prefer to keep the frontiers clean.
```

> **為什麼 `cand in end` 要檢查在 `cand in words` 之前**：前沿裡的單字在被產生的當下就已經從 `words` 刪掉了，所以用 `words` 做成員判斷會漏掉它們。相遇判斷必須排在前面。

### 13) Odd Even Jump — LC 975 — 需要的是「有序」集合，用來查 floor/ceiling


#### 核心想法

**當你需要「最接近且 ≥ x 的值」或「最接近且 ≤ x 的值」時，雜湊集合完全沒用 — 你要的是有序集合。**

這正是 `TreeSet`/`TreeMap` 相對於 `HashSet` 唯一多給你的東西：**O(log n) 的前驅／後繼查詢**。雜湊集合只能精確回答「`x` 在不在？」。

| 需求 | Java（`TreeSet` / `TreeMap`） | Python（在已排序 list 上用 `bisect`） |
|------|------------------------------|-------------------------------------|
| 最小的 **≥ x** 的值（ceiling） | `ceiling(x)` / `ceilingEntry(x)` | `i = bisect_left(a, x)` → `a[i]` |
| 最大的 **≤ x** 的值（floor） | `floor(x)` / `floorEntry(x)` | `i = bisect_right(a, x)` → `a[i-1]` |
| 最小的 **> x** 的值（higher） | `higher(x)` | `i = bisect_right(a, x)` → `a[i]` |
| 最大的 **< x** 的值（lower） | `lower(x)` | `i = bisect_left(a, x)` → `a[i-1]` |
| min / max | `first()` / `last()` | `a[0]` / `a[-1]` |

**對應到題目**：**由右往左**掃，維護一個包含所有索引 `> i` 之值的有序集合。那麼從 `i` 出發的奇數次（往上）跳就是 `ceiling(arr[i])`，偶數次（往下）跳就是 `floor(arr[i])`。同值時要取**最小索引**，這點免費附贈：反向掃描時，後寫入的一定是比較小的索引。

DP 是每個索引兩個布林值 — 「從這裡出發、以奇數／偶數次跳，能不能走到終點」：
```text
odd[i]  = even[j]   where j = index of ceiling(arr[i])
even[i] = odd[j]    where j = index of floor(arr[i])
odd[n-1] = even[n-1] = True        answer = count of odd[i] == True
```

```java
// java
// LC 975 - Odd Even Jump
// IDEA: ordered set (TreeMap) gives ceiling/floor of the values to the RIGHT of i
// time = O(n log n), space = O(n)
public int oddEvenJumps(int[] arr) {
    int n = arr.length;
    boolean[] odd = new boolean[n], even = new boolean[n];
    odd[n - 1] = even[n - 1] = true;

    // value -> smallest index > i holding it
    TreeMap<Integer, Integer> seen = new TreeMap<>();
    seen.put(arr[n - 1], n - 1);

    for (int i = n - 2; i >= 0; i--) {
        Map.Entry<Integer, Integer> hi = seen.ceilingEntry(arr[i]);  // smallest value >= arr[i]
        Map.Entry<Integer, Integer> lo = seen.floorEntry(arr[i]);    // largest  value <= arr[i]
        if (hi != null) odd[i]  = even[hi.getValue()];
        if (lo != null) even[i] = odd[lo.getValue()];
        seen.put(arr[i], i);   // scanning backwards -> this index is the smallest so far
    }

    int cnt = 0;
    for (boolean b : odd) if (b) cnt++;
    return cnt;
}
```

```python
# python
# LC 975 - Odd Even Jump
# IDEA: python has no TreeSet -> keep a sorted list + bisect for ceiling/floor
# time = O(n^2) worst with insort (O(n log n) with sortedcontainers.SortedList), space = O(n)
import bisect

class Solution(object):
    def oddEvenJumps(self, arr):
        n = len(arr)
        odd, even = [False] * n, [False] * n
        odd[n - 1] = even[n - 1] = True

        vals = [arr[n - 1]]          # sorted DISTINCT values at indices > i
        pos = {arr[n - 1]: n - 1}    # value -> smallest such index

        for i in range(n - 2, -1, -1):
            a = arr[i]
            k = bisect.bisect_left(vals, a)

            if k < len(vals):                       # ceiling = smallest value >= a
                odd[i] = even[pos[vals[k]]]

            if k < len(vals) and vals[k] == a:      # floor = largest value <= a
                even[i] = odd[pos[vals[k]]]
            elif k > 0:
                even[i] = odd[pos[vals[k - 1]]]

            if a not in pos:
                bisect.insort(vals, a)
            pos[a] = i               # later in the loop = smaller index -> overwrite

        return sum(odd)
```

> **Python 沒有內建的有序集合。** 選項有：`sortedcontainers.SortedList`（真正的 O(log n)，但不是每個語言版本的 LeetCode 預設環境都有）、在你自己維持排序的 list 上用 `bisect`（查詢 O(log n)，但**插入 O(n)**），或乾脆繞過它 — LC 975 也可以用「照值排索引 + 單調堆疊」在 O(n log n) 解掉。

### 14) Lowest Common Ancestor of a Binary Tree III — LC 1650 — 一個裝祖先的集合

```python
# LC 1650. Lowest Common Ancestor of a Binary Tree III
# NOTE : there are also dict, recursive.. approaches

# V0''
# IDEA : set - track ancestry path
# Time: O(h) where h is tree height
# Space: O(h) for storing ancestors
class Solution:
    def lowestCommonAncestor(self, p, q):
        # Store all ancestors of p
        visited = set()
        while p:
            visited.add(p)
            p = p.parent

        # Find first common ancestor with q
        while q:
            if q in visited:
                return q
            q = q.parent
```

# 回溯 — LC 題目實作

> **範圍** — 回溯題解的長尾（LC 17、39、79、78、90、77、46、22、93、139、140、207），附遞迴軌跡與差一點就一樣的變形，每題每種語言只留一份正典解 — 它本身不教任何模板，每一節都指回它所實例化的那一個。
> **另見**：[backtrack.md](./backtrack.md) — 這裡每個範例所實例化的模板，以及挑模板用的決策表；[backtrack_advanced.md](./backtrack_advanced.md) — Hard 等級的模板（LC 212、282、301）；[tree_backtrack.md](./tree_backtrack.md) — 樹上 root→leaf 的路徑題。

## LeetCode 題目清單

- [Backtracking](https://leetcode.com/problem-list/backtracking/)
- [Recursion](https://leetcode.com/problem-list/recursion/)

## 總覽

這是 [backtrack.md](./backtrack.md) 的範例倉庫。每一節都在實例化那張表裡的某個模板 — 先讀模板，
再回來看完整解、遞迴軌跡，以及那些只改一行就沿用同一個迴圈的鄰居題。

### 關鍵性質

- **複雜度**：見母表的 [Time Complexity by Problem Type](./backtrack.md#time-complexity-by-problem-type) 表格
- **核心想法**：每題每種語言只放一份正典解；只有當上方註解說明第二種寫法教到了第一種沒教的東西時，才會出現第二個版本
- **什麼時候用**：當你已經知道該套哪個模板，想看它從頭到尾跑一遍的時候

## 題型分類

| § | 題目 | 實例化的模板 |
|---|---------|--------------|
| 1 | LC 17 Letter Combinations | [Template 1](./backtrack.md#template-1-choose--explore--un-choose-) — 由索引驅動的選擇清單 |
| 2 | LC 39 Combination Sum（＋ LC 216） | [Template 7](./backtrack.md#template-7-combination-sum--lc-39--lc-40-) |
| 3 | LC 79 Word Search（＋ LC 980、1219） | [Template 9](./backtrack.md#template-9-grid--word-search--lc-79-) |
| 4 | LC 78 Subsets | [Template 3](./backtrack.md#template-3-subsets--lc-78-) |
| 5 | LC 90 Subsets II | [Template 4](./backtrack.md#template-4-subsets-ii-skip-same-level-duplicates--lc-90-) |
| 6 | LC 77 Combinations | [Template 6](./backtrack.md#template-6-combinations--lc-77) |
| 7 | LC 46 Permutations（＋ LC 526、996、784） | [Template 5](./backtrack.md#template-5-permutations--lc-46-) |
| 8 | LC 22 Generate Parentheses | 用計數器當限制條件 |
| 9 | LC 93 Restore IP Addresses | [Template 8](./backtrack.md#template-8-palindrome-partitioning--lc-131-)，換成數值判斷式 |
| 10 | LC 139 Word Break | 子字串之間的可達性（BFS，不是回溯） |
| 11 | LC 140 Word Break II | [Template 8](./backtrack.md#template-8-palindrome-partitioning--lc-131-)，換成字典判斷式 |
| 12 | LC 207 Course Schedule | DFS 搭配*在 visiting 集合上做 undo* — 環偵測 |

## LC 範例

### 1) Letter Combinations of a Phone Number — LC 17

```java
// java
// LC 17
// IDEA: BACKTRACK + start_idx (on digit)
List<String> _res = new ArrayList<String>();
public List<String> letterCombinations(String _digits) {

    if (_digits.length() == 0){
        return new ArrayList<>();
    }

    HashMap<java.lang.String, java.lang.String> letters = new HashMap<>();
    letters.put("2", "abc");
    letters.put("3", "def");
    letters.put("4", "ghi");
    letters.put("5", "jkl");
    letters.put("6", "mno");
    letters.put("7", "pqrs");
    letters.put("8", "tuv");
    letters.put("9", "wxyz");

    _letter_builder(letters, 0, _digits, new StringBuilder());
    return this._res;
}

private void _letter_builder(HashMap<String, String> map, int start_idx, String digits, StringBuilder builder){

    /**
     *  NOTE !!!
     *
     *   if builder (StringBuilder) length equals digits length,
     *   -> means we first one of the `all digit visit`
     *   -> we should add this cur to our result
     */
    if (builder.length() == digits.length()){
        this._res.add(builder.toString()); // NOTE this
        return;
    }

    /**
     *  NOTE !!!
     *
     *
     *   1) the `start_idx`  is for `digits` .
     *   e.g.
     *
     *    -> if digits = "23",
     *       the start_idx is 0,
     *       and could become 1, ...
     *
     *
     *   2) via `start_idx` we can focus on specific digit (e.g. "2" only, from "23")
     *      then we can loop over its `alphabet` in recursive call
     *      e.g. "abc" for "2"
     *
     *      letters.put("2", "abc");
     *
     */
    String _digit = String.valueOf(digits.toCharArray()[start_idx]); // NOTE this
    String _alphabets = map.get(_digit);

    // backtrack
    /**
     *  NOTE !!!
     *
     *   we loop over `_alphabets` (digit with idx),
     *   (instead of digit)
     *
     *   -> so we can build our cur string accordingly
     *
     */
    for (char a : _alphabets.toCharArray()){
        builder.append(a);
        _letter_builder(map, start_idx + 1, digits, builder);


        // undo
        // builder.deleteCharAt(0); // NOTE !!! in backtrack, we remove LAST element (idx = len - 1), instead of first element
        builder.deleteCharAt(builder.toString().length() - 1);
        // no need to `undo` start_idx, since it's primary type
        // in java, it is copied as `new var` when pass the recursive call
        // https://github.com/yennanliu/CS_basics/blob/master/doc/cheatsheet/backtrack.md#template-14-when-to-undo--mutable-vs-immutable-state-
        // start_idx -= 1; // this is WRONG!!!
    }
}
```

```python
# 017   Letter Combinations of a Phone Number
# V0
# IDEA : backtracking
class Solution(object):
    def letterCombinations(self, digits):
        # help func
        def help(idx, cur):
            if len(cur) == len(digits):
                tmp = "".join(cur[:])
                res.append(tmp)
                cur = []
                return
            if len(cur) > len(digits):
                cur = []
                return
            for a in d[digits[idx]]:
                cur.append(a)
                help(idx+1, cur)
                cur.pop(-1)  # NOTE this !!! : we pop last element
        # edge case
        if not digits:
            return []
        res = []
        cur = []
        idx = 0
        d =  {'2': 'abc', '3': 'def', '4': 'ghi', '5': 'jkl', '6': 'mno', '7': 'pqrs', '8': 'tuv', '9': 'wxyz'}
        help(idx, cur)
        return res

# V1 (no recursion — kept because it is a different algorithm, not a
#     different spelling: build the answer iteratively, O(1) call stack)
# idea : for loop
class Solution(object):
    def letterCombinations(self, digits):
        """
        :type digits: str
        :rtype: List[str]
        """
        if digits == "": return []
        d = {'2' : "abc", '3' : "def", '4' : "ghi", '5' : "jkl", '6' : "mno", '7' : "pqrs", '8' : "tuv", '9' : "wxyz"}
        res = ['']
        for e in digits:
            res = [w + c for c in d[e] for w in res]
        return res
```

### 2) Combination Sum — LC 39 ⭐⭐⭐⭐

> 下面的 **V0** 是對的，但**很浪費**：沒有 `start_idx`，所以它會把每一種*排列順序*都跑一遍
> （例如 `[2,3]` 和 `[3,2]`），最後再靠 `sort()` + `tmp not in res` 去重。
> 請優先用 **V1**（有 start_idx，傳 `i` 讓元素可以重複使用）— 它根本不會產生重複解。

```python
# LC 039 combination-sum
# V0 (brute + dedup — correct but slow)
# IDEA : DFS + BACKTRACK
class Solution(object):
    def combinationSum(self, candidates, target):

        def dfs(tmp):
            if sum(tmp) == target:
                tmp.sort()
                if tmp not in res:
                    res.append(tmp)
                return
            if sum(tmp) > target:
                return
            for c in candidates:
                dfs(tmp + [c])

        res = []
        tmp = []
        dfs(tmp)
        return res

# V1 (start_idx — preferred)
# IDEA : DFS + BACKTRACK + start_idx (pass `i` to allow reuse)
class Solution(object):
    def combinationSum(self, candidates, target):

        def dfs(start, tmp, total):
            if total == target:
                res.append(tmp[:])
                return
            if total > target:
                return
            for i in range(start, len(candidates)):
                tmp.append(candidates[i])
                # NOTE: pass `i` (NOT i+1) -> candidates[i] can be reused
                dfs(i, tmp, total + candidates[i])
                tmp.pop()

        res = []
        dfs(0, [], 0)
        return res
``` 

**視覺化軌跡（遞迴樹）** — `candidates = [2, 3, 6, 7]`、`target = 7` → 答案 `[[2,2,3],[7]]`

> 每個節點是一次 `dfs(start, path, total)` 呼叫。我們傳的是 **`i`**（不是 `i+1`），
> 所以同一個候選值可以**重複使用**。只要 `total > target` 就剪掉這條分支（`✗`）；`total == target` 時記錄（`✅`）。

```text
dfs(0, [], 0)
├─ pick 2 → dfs(0, [2], 2)
│  ├─ pick 2 → dfs(0, [2,2], 4)
│  │  ├─ pick 2 → dfs(0, [2,2,2], 6)
│  │  │  ├─ pick 2 → total=8  ✗ prune
│  │  │  └─ pick 3 → total=9  ✗ prune
│  │  ├─ pick 3 → dfs(1, [2,2,3], 7)   ✅ record [2,2,3]
│  │  ├─ pick 6 → total=10 ✗
│  │  └─ pick 7 → total=11 ✗
│  ├─ pick 3 → dfs(1, [2,3], 5)
│  │  ├─ pick 3 → total=8  ✗
│  │  ├─ pick 6 → total=11 ✗
│  │  └─ pick 7 → total=12 ✗
│  ├─ pick 6 → total=8  ✗
│  └─ pick 7 → total=9  ✗
├─ pick 3 → dfs(1, [3], 3)
│  ├─ pick 3 → dfs(1, [3,3], 6)
│  │  └─ (3→9 ✗, 6→12 ✗, 7→13 ✗)   ✗
│  ├─ pick 6 → total=9  ✗
│  └─ pick 7 → total=10 ✗
├─ pick 6 → dfs(2, [6], 6)
│  └─ (6→12 ✗, 7→13 ✗)              ✗
└─ pick 7 → dfs(3, [7], 7)          ✅ record [7]
```

> **怎麼看這棵樹**：深度 = `path` 裡有幾個數字；`start` 索引（0/1/2/3）往下走時會縮小選擇清單，
> 所以我們永遠不會回頭去拿較早的候選值 → 不會有重複組合。把遞迴呼叫改成 `i + 1`（每個只能用一次）
> 就變成 LC 40。


#### 2') 變形 — Combination Sum III — LC 216


**轉折**：候選池是*隱含*的排序清單 `1..9`，而且現在有**兩個**停止條件 — `len(path) == k`
**以及** `total == n`。因為候選池已排序，超過目標時用 `break`（不是 `continue`）可以把迴圈剩下的尾巴全部剪掉。

```python
# python
# LC 216 - Combination Sum III
# time = O(C(9,k) * k), space = O(k)
# IDEA: LC 39/40 template, pool = 1..9, pass i+1 (each digit used at most once),
#       size check `len(path) == k` on top of the sum check.
class Solution:
    def combinationSum3(self, k, n):
        res = []

        def backtrack(start, path, total):
            ### NOTE !!! size condition comes FIRST -> return regardless of sum
            if len(path) == k:
                if total == n:
                    res.append(path[:])
                return

            for i in range(start, 10):
                ### NOTE !!! pool is sorted -> break (kills the rest), not continue
                if total + i > n:
                    break
                path.append(i)
                backtrack(i + 1, path, total + i)   # i+1 -> digit used once
                path.pop()

        backtrack(1, [], 0)
        return res
```

### 3) Word Search — LC 79 ⭐⭐⭐⭐

> 原地改成 `board[r][c] = '#'` 的版本是正典的
> [Template 9](./backtrack.md#template-9-grid--word-search--lc-79-)。下面是
> **`visited[][]` 矩陣**版 — 演算法完全一樣，但它不會改動輸入，而面試官有時候就是要你這樣寫。

```python
# python
# LC 079 Word Search
# IDEA : DFS + backtracking (explicit `visited` matrix)
class Solution(object):
 
    def exist(self, board, word):
        ### NOTE : construct the visited matrix
        visited = [[False for j in range(len(board[0]))] for i in range(len(board))]

        ### NOTE : we visit every element in board and trigger the dfs
        for i in range(len(board)):
            for j in range(len(board[0])):
                if self.dfs(board, word, 0, i, j, visited):
                    return True

        return False

    def dfs(self, board, word, cur, i, j, visited):
        # if "not false" till cur == len(word), means we already found the wprd in board
        if cur == len(word):
            return True

        ### NOTE this condition
        # 1) if idx out of range
        # 2) if already visited
        # 3) if board[i][j] != word[cur] -> not possible to be as same as word
        if i < 0 or i >= len(board) or j < 0 or j >= len(board[0]) or visited[i][j] or board[i][j] != word[cur]:
            return False

        # mark as visited
        visited[i][j] = True
        ### NOTE THIS TRICK (run the existRecu on 4 directions on the same time)
        result = self.dfs(board, word, cur + 1, i + 1, j, visited) or\
                 self.dfs(board, word, cur + 1, i - 1, j, visited) or\
                 self.dfs(board, word, cur + 1, i, j + 1, visited) or\
                 self.dfs(board, word, cur + 1, i, j - 1, visited)
        # mark as non-visited
        visited[i][j] = False

        return result
```

```java
// java
// LC 079
// IDEA : DFS + BACKTRACK (modified by GPT)
public boolean exist_0(char[][] board, String word) {
    if (board == null || board.length == 0) {
        return false;
    }

    int l = board.length;
    int w = board[0].length;

    boolean[][] visited = new boolean[l][w];

    for (int i = 0; i < l; i++) {
        for (int j = 0; j < w; j++) {
            if (dfs_(board, i, j, 0, word, visited)) {
                return true;
            }
        }
    }

    return false;
}

private boolean dfs_(char[][] board, int y, int x, int idx, String word, boolean[][] visited) {

    if (idx == word.length()) {
        return true;
    }

    int l = board.length;
    int w = board[0].length;

    if (y < 0 || y >= l || x < 0 || x >= w || visited[y][x] || board[y][x] != word.charAt(idx)) {
        return false;
    }

    /** NOTE !!! we update visited on x, y here */
    visited[y][x] = true;

    int[][] dirs = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}};
    /**
     *  NOTE !!!
     *
     *   instead of below structure:
     *
     *       boolean didFindNextCharacter =
     *                 dfs2(row + 1, col, word, lvl + 1, visited, board) ||
     *                 dfs2(row - 1, col, word, lvl + 1, visited, board) ||
     *                 dfs2(row, col + 1, word, lvl + 1, visited, board) ||
     *                 dfs2(row, col - 1, word, lvl + 1, visited, board);
     *
     *   we can use below logic as well:
     *
     *          for (int[] dir : dirs) {
     *             if (dfs_(board, y + dir[0], x + dir[1], idx + 1, word, visited)) {
     *                 return true;
     *             }
     *         }
     *
     */
    for (int[] dir : dirs) {
        if (dfs_(board, y + dir[0], x + dir[1], idx + 1, word, visited)) {
            return true;
        }
    }

    /** NOTE !!! we undo (backtrack) updated x, y here */
    visited[y][x] = false;

    return false;
}
```

#### 3') 變形 — 同一個格子模板，不同回傳值

LC 79 回傳 **boolean** 而且會短路（`if dfs(...): return True`）。下面兩題沿用一模一樣的
*標記 → 四方向遞迴 → 取消標記*骨架，但它們必須**把每條路走到底**，所以沒有提前退出 —
它們累積的是一個計數或一個最大值。

| LC | 回傳 | 標記技巧 | 為什麼不能提前退出 |
|----|---------|-----------|----------------------|
| 79 Word Search | `bool` | `visited[][]` 或 `board[r][c]='#'` | 第一個找到的就算贏 |
| 980 Unique Paths III | `int` 計數 | 把格子設成 `-1`（障礙值） | 必須數出**所有**合法路徑 |
| 1219 Path with Maximum Gold | `int` 最大值 | 把格子設成 `0`（空值） | 必須比較**所有**路徑 |

**轉折（LC 980）** — 「走過所有格子」這個條件變成一個額外的 `remain` 計數器貫穿整個遞迴；
只有 `remain == 0` 時走到終點格才算數。

```python
# python
# LC 980 - Unique Paths III
# time = O(4^(M*N)), space = O(M*N) recursion depth
# IDEA: LC 79 grid backtrack, but COUNT paths instead of early-return.
#       reuse the obstacle value (-1) as the "visited" mark -> no extra matrix.
class Solution:
    def uniquePathsIII(self, grid):
        rows, cols = len(grid), len(grid[0])

        ### NOTE !!! todo = number of walkable cells (start + end + empties)
        todo = sum(v != -1 for row in grid for v in row)
        for r in range(rows):
            for c in range(cols):
                if grid[r][c] == 1:
                    sr, sc = r, c

        self.res = 0

        def dfs(r, c, remain):
            if grid[r][c] == 2:
                ### NOTE !!! end cell only counts if EVERY walkable cell was used
                if remain == 0:
                    self.res += 1
                return

            tmp = grid[r][c]
            grid[r][c] = -1                 # mark visited (as obstacle)
            for nr, nc in ((r+1, c), (r-1, c), (r, c+1), (r, c-1)):
                if 0 <= nr < rows and 0 <= nc < cols and grid[nr][nc] != -1:
                    dfs(nr, nc, remain - 1)
            grid[r][c] = tmp                # undo (backtrack)

        dfs(sr, sc, todo - 1)
        return self.res
```

**轉折（LC 1219）** — 沒有固定起點，所以 DFS 要從**每一格**啟動；遞迴是*回傳*最佳子路徑的值，
而不是寫進一個共用的 list。

```python
# python
# LC 1219 - Path with Maximum Gold
# time = O(M*N*4^(M*N)), space = O(M*N) recursion depth
# IDEA: LC 79 grid backtrack, start from EVERY cell, return max instead of bool.
#       gold value 0 doubles as the "visited / blocked" mark.
class Solution:
    def getMaximumGold(self, grid):
        rows, cols = len(grid), len(grid[0])

        def dfs(r, c):
            ### NOTE !!! 0 means empty cell OR currently-on-path cell -> stop
            if not (0 <= r < rows and 0 <= c < cols) or grid[r][c] == 0:
                return 0

            gold = grid[r][c]
            grid[r][c] = 0                  # mark visited
            best = max(dfs(r+1, c), dfs(r-1, c), dfs(r, c+1), dfs(r, c-1))
            grid[r][c] = gold               # undo (backtrack)
            return gold + best

        return max(dfs(r, c) for r in range(rows) for c in range(cols))
```

> 另見 [backtrack_advanced.md Template 1](./backtrack_advanced.md#template-1-trie--grid-backtracking--lc-212-word-search-ii-) — 這個格子模板的多字版本，
> 用一個 **Trie 節點**取代指向單一字串的 `idx` 游標。


### 4) Subsets — LC 78 ⭐⭐⭐⭐⭐

> 在每個節點都記錄一次的正典解是
> [Template 3](./backtrack.md#template-3-subsets--lc-78-)。這裡的兩個版本是
> *不同的演算法*：Python 那個是**依大小**建子集（`k = 0..n`），而 Java 的
> `helper` 是**選／不選的二元決策樹**。

```python
# python
# LC 078 Subsets
# IDEA : Backtracking (build subsets by size k)
class Solution:
    def subsets(self, nums):
        def backtrack(first = 0, curr = []):
            # if the combination is done
            if len(curr) == k:  
                output.append(curr[:])
                return
            for i in range(first, n):
                # add nums[i] into the current combination
                curr.append(nums[i])
                # use next integers to complete the combination
                backtrack(i + 1, curr)
                # backtrack
                curr.pop()
        
        output = []
        n = len(nums)
        for k in range(n + 1):
            backtrack()
        return output

```

**視覺化軌跡（遞迴樹）** — `nums = [1, 2, 3]` → `2^3 = 8` 個子集

> 節點 = 一次 `backtrack(start, path)` 呼叫。和組合／排列題不同，子集是在
> **每一個節點都記錄 `path`**（前序），不是只在葉節點記錄。`start` 只會往前走（`i + 1`），
> 所以每個元素最多用一次，也就不會出現重複子集。

```text
backtrack(start=0, path=[])            record []
├─ i=0 pick 1 → (start=1, [1])         record [1]
│  ├─ i=1 pick 2 → (start=2, [1,2])    record [1,2]
│  │  └─ i=2 pick 3 → (start=3, [1,2,3]) record [1,2,3]
│  └─ i=2 pick 3 → (start=3, [1,3])    record [1,3]
├─ i=1 pick 2 → (start=2, [2])         record [2]
│  └─ i=2 pick 3 → (start=3, [2,3])    record [2,3]
└─ i=2 pick 3 → (start=3, [3])         record [3]

result = [] [1] [1,2] [1,2,3] [1,3] [2] [2,3] [3]   → 8 subsets
```

> **關鍵對照**：記錄前沒有 `end_condition` 這道關卡 — 子集在任何深度都是合法的。
> 把它看成「選／不選」的二元視角（見下面的 Java `helper`），畫出來就是一棵高度 `n`、
> 有 `2^n` 個葉節點的完滿二元樹。

```java
// java
// LC 78
// V0 (build by size k)
// IDEA : Backtracking
// https://leetcode.com/problems/subsets/editorial/
    List<List<Integer>> output = new ArrayList();
    int n, k;

    public void backtrack(int first, ArrayList<Integer> curr, int[] nums) {
        // if the combination is done
        if (curr.size() == k) {
            output.add(new ArrayList(curr));
            return;
        }
        /** NOTE HERE !!!
         *
         *  ++i : i+1 first,  then do op
         *  i++ : do op first, then i+1
         *
         *  -> i++ or ++i is both OK here
         */
        for (int i = first; i < n; i++) {
            // add i into the current combination
            curr.add(nums[i]);
            // use next integers to complete the combination
            backtrack(i + 1, curr, nums);
            // backtrack
            curr.remove(curr.size() - 1);
        }
    }

    public List<List<Integer>> subsets(int[] nums) {
        n = nums.length;
        /** NOTE HERE !!!
         *
         *  ++k : k+1 first,  then do op
         *  k++ : do op first, then k+1
         *
         *  -> k++ or ++k is both OK here
         */
        for (k = 0; k < n + 1; k++) {
            backtrack(0, new ArrayList<Integer>(), nums);
        }
        return output;
    }


// V1
// IDEA : BACKTRACK
// https://www.youtube.com/watch?v=REOH22Xwdkk&t=4s
// https://github.com/neetcode-gh/leetcode/blob/main/java/0078-subsets.java
    public List<List<Integer>> subsets_1_2(int[] nums) {
        List<List<Integer>> ans = new ArrayList<>();
        List<Integer> list = new ArrayList<>();
        helper(ans, 0, nums, list);
        return ans;
    }

    public void helper(
            List<List<Integer>> ans,
            int start,
            int[] nums,
            List<Integer> list
    ) {
        if (start >= nums.length) {
            ans.add(new ArrayList<>(list));
        } else {

            // decision tree :  add the element and start the  recursive call
            list.add(nums[start]);
            helper(ans, start + 1, nums, list);

            // decision tree :  remove the element and do the backtracking call.
            list.remove(list.size() - 1);
            helper(ans, start + 1, nums, list);
        }
    }    
```

```c++
// c++
// backtrack
// (algorithm book (labu) p.303)

// save all subset
vector<vector<int>> res;

/* main func */
vector<vector<int>> subsets(vector<int> & nums){
    // record visited routes
    vector<int> track;
    backtrack(nums, 0, track);
    return res;
}

/* use backtrack pattern */
void backtrack(vector<int> & nums, int start, vector<int> & track){
    // pre-order tranverse
    res.push_back(track);
    // start from `start`, avoid duplivated subset
    for (int i = start; i < nums.size(); i++){
        // make choice
        track.push_back(nums[i]);
        // iteration backtrack
        backtrack(nums, i+1, track);
        // undo choice
        track.pop_back();
    }
}
```

### 5) Subsets II — LC 90

```python
# LC 90 Subsets II
# V0
# IDEA : BACKTRACKING + LC 078 Subsets
from collections import Counter
class Solution(object):
    def subsetsWithDup(self, nums):
        def help(start, tmp, _cnt):
            tmp.sort()
            if tmp not in res:
                res.append(tmp)
            if start >= len(nums):
                return
            for i in range(start, len(nums)):
                if _cnt[nums[i]]  > 0:
                    _cnt[nums[i]] -= 1
                    help(start+1, tmp + [nums[i]], _cnt)
                    """
                    NOTE : here we "undo" the "_cnt[nums[i]] -= 1" op,
                          -> so next recursive can still have the "capacity" of such element
                    """
                    _cnt[nums[i]] += 1

        # edge case
        if not nums:
            return []

        # edge case
        if len(nums) == 1:
            res = [[]]
            res.append([nums[0]])
            return res

        res = [[]]
        _cnt = Counter(nums)
        help(0, [], _cnt)
        print ("res = " + str(res))
        return res

# V1
# IDEA : BRUTE FORCE (iterative power set)
class Solution:
    def subsetsWithDup(self, nums):
        # small trick (init with a null array)
        ans=[[]]
        for i in nums:
            for l in list(ans):
                # sorted here, since we want to the "non-duplicated" power set
                temp=sorted(l+[i])
                # avoid duplicated
                if temp not in ans:
                    ans.append(temp) 
        return ans

```

### 6) Combinations — LC 77

> `len(path) == k` 那個版本是 [Template 6](./backtrack.md#template-6-combinations--lc-77)。
> 下面是**選／跳的二元**寫法 — 完全沒有 `for` 迴圈，只有兩個遞迴呼叫。

```python
# python
# LC 77. Combinations
# IDEA : BACKTRACK (binary pick / skip)
class Solution:
    def combine(self, n, k):
        res=[]
        def go(i,ma,ans):
            if ma==k:
                res.append(list(ans))
                return
            if i>n:
                return
            ans.append(i)
            go(i+1,ma+1,ans)
            ans.pop()
            go(i+1,ma,ans)
        go(1,0,[])
        return res
```

```c++
// c++
// backtrack
// (algorithm book (labu) p.305)

// record all combinations
vector<vector<int>> res;

/* main func */
vector<vector<int>> combine(int n, int k){
    if (k <= 0 || n <= 0) return res;
    vector<int> track;
    backtrack(n, k, 1, track);
    return res;
}

/* use backtrack pattern */
void backtrack(int n, int k, int start, vector<int> & track){
    // not update res till visit leaf node
    if (k == track.size()){
        res.push_back(track);
        return;
    }

    // increase from i
    for (int i = start; i <= n; i ++){
        // do choice
        track.push_back(i);
        // backtrack
        backtrack(n, k, i+1, track);
        // undo choice
        track.pop_back();
    }
}
```

### 7) Permutations — LC 46 ⭐⭐⭐⭐⭐

> `visited[]` 那個版本是 [Template 5](./backtrack.md#template-5-permutations--lc-46-)。
> 下面是 `if i not in cur` 的版本 — 形狀一樣，但成員檢查是 O(n) 而不是 O(1)，
> 這正是面試時該寫 `visited[]` 的理由。

```python
# python
# LC 46. Permutations
# IDEA : BACKTRACK, 
# similar idea as LC 77 -> difference : contains VS start
class Solution(object):
    def permute(self, nums):
        def help(cur):
            if len(cur) == n_len:
                if cur not in res:
                    res.append(list(cur))
                    return
            if len(cur) > n_len:
                return
            for i in nums:
                #print ("i = " + str(i) + " cur = " + str(cur))
                if i not in cur:
                    cur.append(i)
                    help(cur)
                    cur.pop(-1)
        # edge case
        if not nums:
            return [[]]
        n_len = len(nums)
        res = []
        help([])
        #print ("res = " + str(res))
        return res
```

**視覺化軌跡（遞迴樹）** — `nums = [1, 2, 3]` → `3! = 6` 種排列

> 節點 = 一次帶著 `visited` 集合的 `dfs(path)` 呼叫。排列題**沒有 `start_idx`** —
> 每一層都掃過**全部** `nums`，只跳過已經在 `visited` 裡的元素。只有在**葉節點**、
> 也就是 `len(path) == len(nums)` 時才記錄 `path`（`✅`）。

```text
dfs([])                       visited={}
├─ 1 → dfs([1])               visited={1}
│  ├─ 2 → dfs([1,2])          visited={1,2}
│  │  └─ 3 → [1,2,3] ✅
│  └─ 3 → dfs([1,3])          visited={1,3}
│     └─ 2 → [1,3,2] ✅
├─ 2 → dfs([2])               visited={2}
│  ├─ 1 → [2,1] → 3 → [2,1,3] ✅
│  └─ 3 → [2,3] → 1 → [2,3,1] ✅
└─ 3 → dfs([3])               visited={3}
   ├─ 1 → [3,1] → 2 → [3,1,2] ✅
   └─ 2 → [3,2] → 1 → [3,2,1] ✅
```

> **和子集的關鍵對照**：分支數每往下一層就**變少**（3 → 2 → 1），因為 `visited` 一直在長；
> 而且結果**只出現在葉節點** — 所以是 `n!` 個葉節點，而不是 `2^n` 個節點。

#### 7') 變形 — 排列迴圈只多加一個 `if`

下面每一題都是 LC 46 的骨架（`對每個還沒用過的值 → 選 → 遞迴 → 取消選`）。
唯一改變的是迴圈裡多加的那道**守門條件**：

| LC | 迴圈裡多加的守門條件 | 換到了什麼 |
|----|-----------------------------|--------------|
| 46 Permutations | *（無）* | 全部 `n!` 種順序 |
| 47 Permutations II | `i > 0 and a[i] == a[i-1] and not used[i-1]` | 跳過同一層的重複值 |
| 526 Beautiful Arrangement | `v % pos == 0 or pos % v == 0` | n=15 時把樹剪到只剩幾千個節點 |
| 996 Number of Squareful Arrays | 上面兩個都要，再加 `is_square(path[-1] + a[i])` | 去重**加上**相鄰限制 |

**轉折（LC 526）** — 改成對**位置**遞迴（`pos = 1..n`）、對*值*做迴圈，這樣值一放下去就能
馬上檢查整除條件。因為只需要**個數**，所以根本不用建 `path` 這個 list。

```python
# python
# LC 526 - Beautiful Arrangement
# time = O(k) where k = #valid arrangements (far below n! due to pruning), space = O(n)
# IDEA: LC 46 backtrack driven by POSITION; used[] marks consumed values.
#       constraint (v % pos == 0 or pos % v == 0) is checked BEFORE recursing -> heavy pruning.
class Solution:
    def countArrangement(self, n):
        used = [False] * (n + 1)

        def backtrack(pos):
            ### NOTE !!! filled every position -> 1 valid arrangement
            if pos > n:
                return 1

            cnt = 0
            for v in range(1, n + 1):
                ### NOTE !!! prune BEFORE recursing (this is the whole optimization)
                if not used[v] and (v % pos == 0 or pos % v == 0):
                    used[v] = True
                    cnt += backtrack(pos + 1)
                    used[v] = False       # undo (backtrack)
            return cnt

        return backtrack(1)
```

**轉折（LC 996）** — 把 LC 47 的去重規則*疊在*相鄰限制*之上*。注意去重規則需要陣列先**排序**，
而且讀的是 `not used[i-1]`（相等的前一個值不在目前路徑上 → 代表我們在同一層 → 跳過）。

```python
# python
# LC 996 - Number of Squareful Arrays
# time = O(n!) worst case (heavily pruned in practice), space = O(n)
# IDEA: LC 47 (permutations with duplicates) + a pairwise constraint.
#       sort -> `i>0 and a[i]==a[i-1] and not used[i-1]` kills same-level duplicates.
class Solution:
    def numSquarefulPerms(self, nums):
        nums.sort()                        # NOTE !!! sort enables the dedup rule
        n = len(nums)
        used = [False] * n
        self.res = 0

        def is_square(x):
            r = int(x ** 0.5)
            return any((r + d) * (r + d) == x for d in (-1, 0, 1))

        def backtrack(path):
            if len(path) == n:
                self.res += 1
                return

            for i in range(n):
                if used[i]:
                    continue
                ### NOTE !!! same-level duplicate skip (identical to LC 47 / LC 90)
                if i > 0 and nums[i] == nums[i-1] and not used[i-1]:
                    continue
                ### NOTE !!! adjacency constraint -> prune before recursing
                if path and not is_square(path[-1] + nums[i]):
                    continue

                used[i] = True
                path.append(nums[i])
                backtrack(path)
                path.pop()                 # undo (backtrack)
                used[i] = False

        backtrack([])
        return self.res
```

**轉折（LC 784, Letter Case Permutation）** — 它*根本不是*排列：順序是固定的，我們是**逐索引**分支，
字母兩條路、數字一條路。它其實是 LC 78 子集的形狀（每個位置做二元選擇），只是頂著「permutation」這個名字。

```python
# python
# LC 784 - Letter Case Permutation
# time = O(2^L * n) where L = #letters, space = O(n) recursion depth
# IDEA: fixed order, branch per index: letter -> {lower, upper}, digit -> single branch.
class Solution:
    def letterCasePermutation(self, s):
        res = []

        def backtrack(i, path):
            if i == len(s):
                res.append("".join(path))
                return

            ch = s[i]
            if ch.isalpha():
                ### NOTE !!! 2 branches on a letter
                backtrack(i + 1, path + [ch.lower()])
                backtrack(i + 1, path + [ch.upper()])
            else:
                backtrack(i + 1, path + [ch])   # digit -> no choice

        backtrack(0, [])
        return res
```

### 8) Generate Parentheses — LC 22

> 三個版本、三種不同的演算法：**V0** 產生所有長度 `2n` 的字串再去*驗證*
> （指數級的浪費 — 放在這裡是因為它是最直覺的第一個想法）；
> **V0'** 帶著剩餘的 `(` / `)` 數量，所以只會蓋出合法的前綴；
> **Java** 版就是 V0'，但改用 `StringBuilder` 和明確的 undo，而不是字串串接。

```python
# python
# LC 022 Generate Parentheses
# V0
# IDEA : bracktrack + Valid Parentheses (LC 020)
class Solution(object):
    def generateParenthesis(self, n):
        # help func for backtracking
        def help(tmp, res, n):
            if len(tmp) == n * 2 and check(tmp):
                res.append(tmp)
                return
            if len(tmp) == n * 2:
                return
            for l in _list:
                print ("l = " + str(l))
                help(tmp + l, res, n)

        """
        LC 020 Valid Parentheses
        """
        def check(s):
            lookup = {"(":")", "[":"]", "{":"}"}
            q = []
            for i in s:
                if i not in lookup and len(q) == 0:
                    return False
                elif i in lookup:
                    q.append(i)
                else:
                    tmp = q.pop()
                    if lookup[tmp] != i:
                        return False
            return True if len(q) == 0 else False

        _list = ['(', ')']
        if n == 1:
            return ["()"]
        res = []
        help("", res, n)
        return res

# V0'
# https://blog.csdn.net/fuxuemingzhu/article/details/79362373
# IDEA: BACKTRACKING + DFS 
# NOTE : KEEP DFS WHEN MEAT 2 CONDTIONS:
#  1) len(path) < n 
#  2) # of "("  > # of ")" (means it's still possible to form a "paratheses" as expected)
class Solution(object):
    def generateParenthesis(self, n):
        res = []
        self.dfs(res, n, n, '')
        return res
        
    def dfs(self, res, left, right, path):
        if left == 0 and right == 0:
            res.append(path)
            return
        if left > 0:
            self.dfs(res, left - 1, right, path + '(')
        if left < right:
            self.dfs(res, left, right - 1, path + ')')
```
```c++
// c++
// LC 022 Generate Parentheses
// (algorithm book (labu) p.316)

/* main func */
vector<string> generateParentheses(int n){
    if (n == 0) return {};
    // record all legal collections
    vector<string> res;
    // backtrack the routes (in process)
    string track;
    // init : available left Parentheses and right Parentheses counts as n
    backtrack(n, n, track, res);
    return res;
}

/* remain left Parentheses count : left ;.. remain right Parentheses : right */
void backtrack(int left, int right, string& track, vector<string> & res){
    // if count < 0 : illegal
    if (left < 0 || right < 0) return;
    // if remain  left Parentheses count >  right Parentheses count : illegal
    if (right < left) return;
    // if all Parentheses are used : legal, we got one OK solution
    if (left == 0 && right == 0){
        res.push_back(track);
        return;
    }

    // add one more left Parentheses
    track.push_back('('); // do choice
    backtrack(left - 1, right, track, res);
    track.pop_back(); // undo choice

    // add one more right Parentheses
    track.push_back(')'); // do choice
    backtrack(left, right - 1, track, res);
    track.pop_back(); // undo choice
}
```

```java
// java
// V2
// IDEA :  Backtracking, Keep Candidate Valid
// https://leetcode.com/problems/generate-parentheses/editorial/
public List<String> generateParenthesis_3(int n) {
    List<String> answer = new ArrayList<>();
    backtracking(answer, new StringBuilder(), 0, 0, n);

    return answer;
}

private void backtracking(List<String> answer, StringBuilder curString, int leftCount, int rightCount, int n) {
    if (curString.length() == 2 * n) {
        answer.add(curString.toString());
        return;
    }
    if (leftCount < n) {
        curString.append("(");
        backtracking(answer, curString, leftCount + 1, rightCount, n);
        curString.deleteCharAt(curString.length() - 1);
    }
    if (leftCount > rightCount) {
        curString.append(")");
        backtracking(answer, curString, leftCount, rightCount + 1, n);
        curString.deleteCharAt(curString.length() - 1);
    }
}
```

### 9) Restore IP Addresses — LC 93

```python
# python
# 093 Restore IP Addresses
# IDEA : DFS
class Solution(object):
    def restoreIpAddresses(self, s):
        # if not valid input form (ip address length should < 12)
        if len(s) > 12:
            return []
        res = []
        self.dfs(s, [], res)
        return res
        
    def dfs(self, s, path, res):
        # if not remaining elments (not s) and path is in "xxx.xxx.xxx.xxx" form
        if not s and len(path) == 4:
            res.append('.'.join(path))
            return
        for i in [1,2,3]:
            # avoid "out of index" error
            if i > len(s):
                continue
            number = int(s[:i])
            # str(number) == s[:i] for checking if digit is not starting from "0"
            # e.g. 030 is not accepted form, while 30 is OK
            if str(number) == s[:i] and number <= 255:
                self.dfs(s[i:], path + [s[:i]], res)
```

### 10) Word Break — LC 139

> 放在這裡是因為它是下面 LC 140 的*判定版*雙胞胎 — 但注意它的解法是
> **對起始索引做 BFS**，不是回溯：沒有東西需要 undo。

```python
# python
# LC 139 Word Break
# IDEA : BFS
class Solution:
    def wordBreak(self, s, wordDict):
        if not s or not wordDict:
            return
        q = collections.deque()
        q.append(0)
        visited = [None]*len(s)
        while q:
            i = q.popleft()
            if not visited[i]:
                for j in range(i+1,len(s)+1):                 
                    if s[i:j] in wordDict:                    
                        if j == len(s):
                            return True  
                        q.append(j)
                visited[i]=True
```

### 11) Word Break II — LC 140

> **V0** 是列舉*字典*（把每個單字接上去，再測試接出來的前綴）；**V1** 是列舉*字串*
> （切出每一個前綴，再測試它在不在字典裡）— 該寫的是第二種，它遇到第一個不是單字的前綴就會剪枝。
> **V1'** 兩者都不是：它用迭代方式建一張 parent-pointer DAG，再倒著走一遍，所以完全不遞迴。

```python
# LC 140 Word Break II
# NOTE : there is also dfs, dp approaches
# V0
# IDEA : BACKTRCK, LC 078 Subsets
class Solution(object):
    def wordBreak(self, s, wordDict):
        def help(cur):
            """
            NOTE this !!! : 
                -> shallow copy cur[:]
            """
            if "".join(cur[:]) == s:
                res.append(" ".join(cur[:]))
                return
            if len("".join(cur[:])) > len(s):
                return
            for i in range(len(wordDict)):
                cur.append(wordDict[i])
                help(cur)
                # NOTE this
                cur.pop()

        # edge case
        if not wordDict:
            return []
        res = []
        cur = []
        cnt = 0
        help(cur)
        print ("res = " + str(res))
        return res

# V1
# IDEA : RECURSION
# https://leetcode.com/problems/word-break-ii/discuss/1426014/Python-interview-friendly-simple-recursion
class Solution:
    def wordBreak(self, s: str, wordDict: List[str]) -> List[str]:
        def recur(s, path):
            if not s:
                out.append(' '.join(path))
                return
            for i in range(1,len(s)+1):
                w,new_s = s[:i], s[i:]
                if w in wordDict:
                    recur(new_s, path + [w])
        wordDict, out = set(wordDict), []
        recur(s,[])
        return out

# V1'
# IDEA : BACKTRCK
# https://leetcode.com/problems/word-break-ii/discuss/44404/Python-backtracking
class Solution:
    def wordBreak(self, s, dic):
        if not dic:
            return []
        n = max(len(d) for d in dic)
        stack, parents = [0], collections.defaultdict(set)
        while stack:
            parent = stack.pop()
            for child in range(parent+1, parent+n+1):
                if s[parent:child] in dic:
                    if child not in parents:
                        stack.append(child)
                    parents[child].add(parent)
        stack, res = [[len(s)]], []
        while stack:
            r = stack.pop()
            if r[0] == 0:
                r = [s[i:j] for i, j in zip(r[:-1], r[1:])]
                res.append(' '.join(r))
            for parent in parents[r[0]]:
                stack.append([parent]+r)
        return res
```

### 12) Course Schedule — LC 207

> 從回溯的角度看：`visiting.remove(crs)` **就是**取消選擇那一步 — 這個集合裝的是目前的 DFS 路徑，
> 所以在裡面重複出現就代表有環。Kahn／入度的做法見
> [topology_sorting.md](./topology_sorting.md)。

```java
// java
// LC 207
// IDEA : DFS (fix by gpt)
// NOTE !!! instead of maintain status (0,1,2), below video offers a simpler approach
//      -> e.g. use a set, recording the current visiting course, if ANY duplicated (already in set) course being met,
//      -> means "cyclic", so return false directly
// https://www.youtube.com/watch?v=EgI5nU9etnU
public boolean canFinish(int numCourses, int[][] prerequisites) {
    // Initialize adjacency list for storing prerequisites
    /**
     *  NOTE !!!
     *
     *  init prerequisites map
     *  {course : [prerequisites_array]}
     *  below init map with null array as first step
     */
    Map<Integer, List<Integer>> preMap = new HashMap<>();
    for (int i = 0; i < numCourses; i++) {
        preMap.put(i, new ArrayList<>());
    }

    // Populate the adjacency list with prerequisites
    /**
     *  NOTE !!!
     *
     *  update prerequisites map
     *  {course : [prerequisites_array]}
     *  so we go through prerequisites,
     *  then append each course's prerequisites to preMap
     */
    for (int[] pair : prerequisites) {
        int crs = pair[0];
        int pre = pair[1];
        preMap.get(crs).add(pre);
    }

    /** NOTE !!!
     *
     *  init below set for checking if there is "cyclic" case
     */
    // Set for tracking courses during the current DFS path
    Set<Integer> visiting = new HashSet<>();

    // Recursive DFS function
    for (int c = 0; c < numCourses; c++) {
        if (!dfs(c, preMap, visiting)) {
            return false;
        }
    }
    return true;
}

private boolean dfs(int crs, Map<Integer, List<Integer>> preMap, Set<Integer> visiting) {
    /** NOTE !!!
     *
     *  if visiting contains current course,
     *  means there is a "cyclic",
     *  (e.g. : needs to take course a, then can take course b, and needs to take course b, then can take course a)
     *  so return false directly
     */
    if (visiting.contains(crs)) {
        return false;
    }
    /**
     *  NOTE !!!
     *
     *  if such course has NO preRequisite,
     *  return true directly
     */
    if (preMap.get(crs).isEmpty()) {
        return true;
    }

    /**
     *  NOTE !!!
     *
     *  add current course to set (Set<Integer> visiting)
     */
    visiting.add(crs);
    for (int pre : preMap.get(crs)) {
        if (!dfs(pre, preMap, visiting)) {
            return false;
        }
    }
    /**
     *  NOTE !!!
     *
     *  remove current course from set,
     *  since already finish visiting
     *
     *  e.g. undo changes
     */
    visiting.remove(crs);
    preMap.get(crs).clear(); // Clear prerequisites as the course is confirmed to be processed
    return true;
}
```

## 總結與速查

| 如果上面的範例讓你覺得陌生 | 回去看 |
|---|---|
| 為什麼要 `path.pop()` ／ 什麼時候不用 | [Template 14](./backtrack.md#template-14-when-to-undo--mutable-vs-immutable-state-) |
| 遞迴呼叫要傳 `i` 還是 `i + 1` | [Template 2](./backtrack.md#template-2-start_idx--i-vs-i--1-) |
| 怎麼跳過重複值 | [Duplicate skipping](./backtrack.md#duplicate-skipping--the-same-level-skip-rule-) |
| 剪枝時該用 `break` 還是 `continue` | [Sort, dedup, prune — when](./backtrack.md#sort-dedup-prune--when) |
| 怎麼從題目敘述挑出對的形狀 | [Decision Table](./backtrack.md#decision-table--which-backtrack-shape-) |

### 相關主題

- [backtrack.md](./backtrack.md) — 模板本身
- [backtrack_advanced.md](./backtrack_advanced.md) — LC 212 / 282 / 301
- [tree_backtrack.md](./tree_backtrack.md) — root→leaf 路徑題（LC 113、257、129、437）
- [dfs.md](./dfs.md) — 不需要 undo 的走訪

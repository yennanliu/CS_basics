# Backtracking — Worked LC Examples

> **Scope** — The long tail of worked backtracking solutions (LC 17, 39, 79, 78, 90, 77, 46, 22, 93, 139, 140, 207) with their recursion traces and near-miss variations, one canonical solution per problem per language — it teaches no templates of its own, every section points back to the one it instantiates.
> **See also**: [backtrack.md](./backtrack.md) — the templates every example here instantiates, and the decision table for picking one; [backtrack_advanced.md](./backtrack_advanced.md) — the hard-tier templates (LC 212, 282, 301); [tree_backtrack.md](./tree_backtrack.md) — root→leaf path problems on a tree.

## LeetCode Problem Lists

- [Backtracking](https://leetcode.com/problem-list/backtracking/)
- [Recursion](https://leetcode.com/problem-list/recursion/)

## Overview

This is the example archive for [backtrack.md](./backtrack.md). Every section instantiates a
template from that sheet — read the template first, then come here for the full solution, the
recursion trace, and the neighbouring problems that reuse the same loop with one line changed.

### Key Properties

- **Complexity**: see the [Time Complexity by Problem Type](./backtrack.md#time-complexity-by-problem-type) table in the parent sheet
- **Core Idea**: one canonical solution per problem per language; a second variant appears only where the note above it says what it teaches that the first does not
- **When to Use**: after you know which template applies and want to see it run end to end

## Problem Categories

| § | Problem | Instantiates |
|---|---------|--------------|
| 1 | LC 17 Letter Combinations | [Template 1](./backtrack.md#template-1-choose--explore--un-choose-) — index-driven choice list |
| 2 | LC 39 Combination Sum (+ LC 216) | [Template 7](./backtrack.md#template-7-combination-sum--lc-39--lc-40-) |
| 3 | LC 79 Word Search (+ LC 980, 1219) | [Template 9](./backtrack.md#template-9-grid--word-search--lc-79-) |
| 4 | LC 78 Subsets | [Template 3](./backtrack.md#template-3-subsets--lc-78-) |
| 5 | LC 90 Subsets II | [Template 4](./backtrack.md#template-4-subsets-ii-skip-same-level-duplicates--lc-90-) |
| 6 | LC 77 Combinations | [Template 6](./backtrack.md#template-6-combinations--lc-77) |
| 7 | LC 46 Permutations (+ LC 526, 996, 784) | [Template 5](./backtrack.md#template-5-permutations--lc-46-) |
| 8 | LC 22 Generate Parentheses | counters as constraints |
| 9 | LC 93 Restore IP Addresses | [Template 8](./backtrack.md#template-8-palindrome-partitioning--lc-131-) with a numeric predicate |
| 10 | LC 139 Word Break | reachability over substrings (BFS, not backtracking) |
| 11 | LC 140 Word Break II | [Template 8](./backtrack.md#template-8-palindrome-partitioning--lc-131-) with a dictionary predicate |
| 12 | LC 207 Course Schedule | DFS with an *undo on the visiting set* — cycle detection |

## LC Examples

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

> **V0** below is correct but **wasteful**: with no `start_idx` it explores every *ordering*
> (e.g. `[2,3]` and `[3,2]`), then dedups via `sort()` + `tmp not in res`.
> Prefer **V1** (start_idx, pass `i` to allow reuse) — it never generates duplicates.

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

**Visual trace (recursion tree)** — `candidates = [2, 3, 6, 7]`, `target = 7` → answer `[[2,2,3],[7]]`

> Each node is a call `dfs(start, path, total)`. We pass **`i`** (not `i+1`) so a candidate
> can be **reused**. A branch is pruned (`✗`) as soon as `total > target`; recorded (`✅`) when `total == target`.

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

> **Reading the tree**: depth = how many numbers are in `path`; the `start` index (0/1/2/3)
> shrinks the choice list going down so we never revisit an earlier candidate → no duplicate
> combinations. Switching the recursive call to `i + 1` (use-once) turns this into LC 40.


#### 2') Variation — Combination Sum III — LC 216


**Twist**: the candidate pool is the *implicit* sorted list `1..9`, and there are now **two**
stop conditions — `len(path) == k` **and** `total == n`. Because the pool is sorted, `break`
(not `continue`) on overflow prunes the whole tail of the loop.

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

> The in-place `board[r][c] = '#'` version is the canonical
> [Template 9](./backtrack.md#template-9-grid--word-search--lc-79-). Below is the
> **`visited[][]` matrix** variant — same algorithm, but it never mutates the input, which is
> what an interviewer sometimes asks for.

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

#### 3') Variations — same grid template, different return value

LC 79 returns a **boolean** and short-circuits (`if dfs(...): return True`). The two problems
below reuse the identical *mark → 4-way recurse → unmark* skeleton but must **explore every
path to the end**, so there is no early exit — they accumulate a count / a maximum instead.

| LC | Returns | Mark trick | No-early-exit reason |
|----|---------|-----------|----------------------|
| 79 Word Search | `bool` | `visited[][]` or `board[r][c]='#'` | first match wins |
| 980 Unique Paths III | `int` count | set cell to `-1` (obstacle value) | must count **all** valid paths |
| 1219 Path with Maximum Gold | `int` max | set cell to `0` (empty value) | must compare **all** paths |

**Twist (LC 980)** — the "visited all cells" condition becomes an extra `remain` counter
threaded through the recursion; reaching the end cell only counts when `remain == 0`.

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

**Twist (LC 1219)** — no fixed start, so the DFS is launched from **every** cell; the recursion
*returns* the best sub-path value rather than writing into a shared list.

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

> See also [backtrack_advanced.md Template 1](./backtrack_advanced.md#template-1-trie--grid-backtracking--lc-212-word-search-ii-) — the multi-word version of this grid template,
> where a **Trie node** replaces the `idx` cursor into a single word.


### 4) Subsets — LC 78 ⭐⭐⭐⭐⭐

> The canonical record-at-every-node solution is
> [Template 3](./backtrack.md#template-3-subsets--lc-78-). The two versions here are
> *different algorithms*: the Python one builds subsets **by size** (`k = 0..n`), and the Java
> `helper` is the **binary include/exclude** decision tree.

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

**Visual trace (recursion tree)** — `nums = [1, 2, 3]` → `2^3 = 8` subsets

> Node = a call `backtrack(start, path)`. Unlike combination/permutation problems, subsets
> **record the `path` at EVERY node** (pre-order), not only at leaves. `start` only ever
> moves forward (`i + 1`), so each element is used at most once and no duplicate subset appears.

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

> **Key contrast**: no `end_condition` gate before recording — a subset is valid at every
> depth. The binary "include / exclude" view (see the Java `helper` below) draws the same
> `2^n` leaves as a full binary tree of height `n`.

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

> The `len(path) == k` version is [Template 6](./backtrack.md#template-6-combinations--lc-77).
> Below is the **binary pick/skip** formulation — no `for` loop at all, two recursive calls.

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

> The `visited[]` version is [Template 5](./backtrack.md#template-5-permutations--lc-46-).
> Below is the `if i not in cur` version — same shape, but the membership test is O(n)
> instead of O(1), which is exactly why `visited[]` is the one to write in an interview.

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

**Visual trace (recursion tree)** — `nums = [1, 2, 3]` → `3! = 6` permutations

> Node = a call `dfs(path)` carrying a `visited` set. Permutations use **no `start_idx`** —
> at every level we scan **all** `nums` and only skip elements already in `visited`. A `path`
> is recorded (`✅`) only at a **leaf**, where `len(path) == len(nums)`.

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

> **Key contrast with subsets**: the branching factor **shrinks** each level (3 → 2 → 1) as
> `visited` grows, and results appear **only at leaves** — giving `n!` leaves instead of `2^n` nodes.

#### 7') Variations — the permutation loop with one extra `if`

Every problem below is the LC 46 skeleton (`for each unused value → pick → recurse → unpick`).
The only thing that changes is the **guard** added inside the loop:

| LC | Extra guard inside the loop | What it buys |
|----|-----------------------------|--------------|
| 46 Permutations | *(none)* | all `n!` orders |
| 47 Permutations II | `i > 0 and a[i] == a[i-1] and not used[i-1]` | skip duplicate values at the same level |
| 526 Beautiful Arrangement | `v % pos == 0 or pos % v == 0` | prunes the tree to ~few thousand nodes for n=15 |
| 996 Number of Squareful Arrays | both of the above + `is_square(path[-1] + a[i])` | dedup **and** adjacency constraint |

**Twist (LC 526)** — recurse over **positions** (`pos = 1..n`) and loop over *values*, so the
divisibility constraint can be checked the moment a value is placed. Only the **count** is
needed, so no `path` list is built at all.

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

**Twist (LC 996)** — stacks the LC 47 dedup rule *on top of* an adjacency constraint. Note the
dedup rule needs the array **sorted** and reads `not used[i-1]` (the equal predecessor is not
on the current path → we are at the same tree level → skip).

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

**Twist (LC 784, Letter Case Permutation)** — *not* a permutation at all: the order is fixed and
we branch **per index**, 2 ways on a letter and 1 way on a digit. It is the LC 78 subsets shape
(binary choice per position) wearing a "permutation" name.

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

> Three variants, three different algorithms: **V0** generates every `2n`-length string and
> *validates* it (exponentially wasteful — shown because it is the obvious first idea);
> **V0'** carries the remaining `(` / `)` counts so only valid prefixes are ever built; the
> **Java** version is V0' with a `StringBuilder` and an explicit undo instead of string
> concatenation.

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

> Listed here because it is the *decision* twin of LC 140 below — but note the solution is a
> **BFS over start indices**, not a backtrack: there is nothing to undo.

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

> **V0** enumerates the *dictionary* (append each word, test the joined prefix); **V1**
> enumerates the *string* (slice every prefix, test membership) — the second is the one to
> write, it prunes on the first non-word prefix. **V1'** is neither: it builds a
> parent-pointer DAG iteratively, then walks it backwards, so it never recurses.

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

> The backtracking angle: `visiting.remove(crs)` **is** the un-choose step — the set holds the
> current DFS path, so a repeat inside it means a cycle. See
> [topology_sorting.md](./topology_sorting.md) for the Kahn / in-degree treatment.

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

## Summary & Quick Reference

| If the example above felt unfamiliar | Go back to |
|---|---|
| why `path.pop()` / when it is not needed | [Template 14](./backtrack.md#template-14-when-to-undo--mutable-vs-immutable-state-) |
| `i` vs `i + 1` in the recursive call | [Template 2](./backtrack.md#template-2-start_idx--i-vs-i--1-) |
| skipping duplicate values | [Duplicate skipping](./backtrack.md#duplicate-skipping--the-same-level-skip-rule-) |
| `break` vs `continue` when pruning | [Sort, dedup, prune — when](./backtrack.md#sort-dedup-prune--when) |
| picking the right shape from the problem statement | [Decision Table](./backtrack.md#decision-table--which-backtrack-shape-) |

### Related Topics

- [backtrack.md](./backtrack.md) — the templates
- [backtrack_advanced.md](./backtrack_advanced.md) — LC 212 / 282 / 301
- [tree_backtrack.md](./tree_backtrack.md) — root→leaf path problems (LC 113, 257, 129, 437)
- [dfs.md](./dfs.md) — traversal without undo

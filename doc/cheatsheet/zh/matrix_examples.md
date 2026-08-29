# Matrix — 實戰題解

> **範圍** — [matrix.md](./matrix.md) 背後的題解庫：十七道題，依各自吃的幾何性質或技巧分組 —— 走訪順序、原地變換、階梯搜尋、格子搜尋、二維 DP，以及列對壓縮。
> **另見**：[matrix.md](./matrix.md) — 母頁：這些題解所套用的走訪模板、索引↔座標換算與模式選擇策略；[dfs.md](./dfs.md) 與 [bfs.md](./bfs.md) — 格子搜尋本身；[prefix_sum.md](./prefix_sum.md) — 二維前綴和與列對壓縮的原理；[dp.md](./dp.md) — 格子 DP；[monotonic_stack.md](./monotonic_stack.md) — LC 85 裡面那一步直方圖。

## LeetCode 題目清單

- [Matrix](https://leetcode.com/problem-list/matrix/)
- [Array](https://leetcode.com/problem-list/array/)

## 總覽

這裡是 [matrix.md](./matrix.md) 的長尾。母頁放模板、矩陣的關鍵性質與模式選擇策略；這個檔案放實際*套用*它們的題目。

### 關鍵性質
- **複雜度**：各題各自標註；多數是 O(m·n)，不是的那幾題，那個複雜度本身就是題目的重點
- **核心想法**：幾乎每一道矩陣題都可以化約成兩個選擇之一 —— 選一種*走訪順序*，或*降維成一維*；底下的分組就是這些選擇
- **什麼時候用**：母頁的決策樹已經幫你指名模式之後


## 走訪與對角線

### 1) Spiral Matrix — LC 54 ⭐⭐⭐⭐⭐

> 用邊界指標以螺旋順序走訪矩陣。

```python
# LC 54 - Spiral Matrix
# V0
# IDEA : 4 cases: right, down, left, up + boundary condition
class Solution(object):
    def spiralOrder(self, matrix):
        if not matrix:
            return []
        res = []
        left, right = 0, len(matrix[0]) - 1
        top, bottom = 0, len(matrix) - 1
        while left <= right and top <= bottom:
            # right
            for j in range(left, right + 1):
                res.append(matrix[top][j])
            # down
            for i in range(top + 1, bottom):
                res.append(matrix[i][right])
            # left
            for j in range(left, right + 1)[::-1]:
                if top < bottom:
                    res.append(matrix[bottom][j])
            # up
            for i in range(top + 1, bottom)[::-1]:
                if left < right:
                    res.append(matrix[i][left])
            left += 1
            right -= 1
            top += 1
            bottom -= 1
        return res
```

```java
// LC 54 - Spiral Matrix
// IDEA: Four boundary pointers (left, right, top, bottom); shrink after each direction
// time = O(M*N), space = O(1)
public List<Integer> spiralOrder(int[][] matrix) {
    List<Integer> res = new ArrayList<>();
    int left = 0, right = matrix[0].length - 1, top = 0, bottom = matrix.length - 1;
    while (left <= right && top <= bottom) {
        for (int j = left; j <= right; j++) res.add(matrix[top][j]);
        for (int i = top + 1; i <= bottom; i++) res.add(matrix[i][right]);
        if (top < bottom) for (int j = right - 1; j >= left; j--) res.add(matrix[bottom][j]);
        if (left < right) for (int i = bottom - 1; i > top; i--) res.add(matrix[i][left]);
        left++; right--; top++; bottom--;
    }
    return res;
}
```

---

### 2) Diagonal Traverse — LC 498 — `(r+c) % 2` 奇偶性

> 以之字形對角線順序走訪每個元素：和為偶數的對角線往右上走，奇數的往左下走。

**核心想法：**

每個格子 `(r, c)` 都屬於由 `r + c` 決定的某條對角線。
這個和的**奇偶性**決定走的方向：
- `(r+c) % 2 == 0` → 往**右上**（`r--, c++`）
- `(r+c) % 2 == 1` → 往**左下**（`r++, c--`）

邊界條件永遠優先於一般的移動：

| 方向 | 撞到哪面牆 | 改成怎麼走 |
|-----------|----------------|-----------------|
| 右上 | 右牆（`c == n-1`） | `r++`（往下掉一格） |
| 右上 | 上牆（`r == 0`）    | `c++`（往右滑一格） |
| 左下 | 下牆（`r == m-1`） | `c++`（往右滑一格） |
| 左下 | 左牆（`c == 0`）   | `r++`（往下掉一格） |

**模式：**用單一個 `for` 迴圈跑完全部 `m*n` 個元素；下一個 `(r, c)` 由奇偶性加邊界檢查決定。

```java
// LC 498 - Diagonal Traverse
// IDEA: (r+c)%2 parity → even = UP-RIGHT, odd = DOWN-LEFT; boundary checks first
// time = O(M*N), space = O(1)
public int[] findDiagonalOrder(int[][] mat) {
    if (mat == null || mat.length == 0 || mat[0].length == 0) return new int[]{};
    int m = mat.length, n = mat[0].length;
    int[] res = new int[m * n];
    int r = 0, c = 0;
    for (int i = 0; i < res.length; i++) {
        res[i] = mat[r][c];
        if ((r + c) % 2 == 0) {          // UP-RIGHT
            if      (c == n - 1) r++;     // hit right wall → go down
            else if (r == 0)     c++;     // hit top wall   → go right
            else               { r--; c++; }
        } else {                          // DOWN-LEFT
            if      (r == m - 1) c++;     // hit bottom wall → go right
            else if (c == 0)     r++;     // hit left wall   → go down
            else               { r++; c--; }
        }
    }
    return res;
}
```

**手動跑一遍 —— `mat = [[1,2,3],[4,5,6],[7,8,9]]`：**

```text
Step | (r,c) | val | r+c | direction  | boundary/move
-----|-------|-----|-----|------------|---------------------
  0  | (0,0) |  1  |  0  | UP-RIGHT   | r==0  → c++ (right)
  1  | (0,1) |  2  |  1  | DOWN-LEFT  | c==0? no, r==m-1? no → r++,c--
  2  | (1,0) |  4  |  1  | DOWN-LEFT  | r==m-1? no, c==0 → r++ (down)
  3  | (2,0) |  7  |  2  | UP-RIGHT   | r==m-1? c++ (right)
  4  | (2,1) |  8  |  3  | DOWN-LEFT  | r==m-1 → c++ (right)
  5  | (2,2) |  9  |  4  | UP-RIGHT   | c==n-1 → r++ (but done)
```
→ 輸出：`[1, 2, 4, 7, 5, 3, 6, 8, 9]` ✓

**另一種做法 —— 一條對角線一條對角線處理（V0-0-1）：**
```java
// Iterate over each diagonal d = 0..m+n-2; set start (r,c) and walk
// time = O(M*N), space = O(1)
public int[] findDiagonalOrder(int[][] mat) {
    int m = mat.length, n = mat[0].length;
    int[] res = new int[m * n];
    int idx = 0;
    for (int d = 0; d < m + n - 1; d++) {
        if (d % 2 == 0) {                       // UP-RIGHT
            int r = Math.min(d, m - 1), c = d - r;
            while (r >= 0 && c < n) { res[idx++] = mat[r--][c++]; }
        } else {                                 // DOWN-LEFT
            int c = Math.min(d, n - 1), r = d - c;
            while (c >= 0 && r < m) { res[idx++] = mat[r++][c--]; }
        }
    }
    return res;
}
```

**相似的 LC 題目：**
| 題目 | LC # | 關鍵 | 技巧 |
|---------|------|-----|-----------|
| Diagonal Traverse | 498 | `(r+c)%2` 奇偶性 | 邊界模擬 |
| Diagonal Traverse II | 1424 | `r+c` 分組鍵 | 依反對角線分組，照順序收集 |
| Sort the Matrix Diagonally | 1329 | `r-c` 分組鍵 | 依主對角線分組，各組排序 |
| Spiral Matrix | 54 | 邊界指標 | 每繞完一圈就縮 4 條邊界 |
| Spiral Matrix II | 59 | 邊界指標 | 同一套螺旋，改成填值 |
| Rotate Image | 48 | 座標運算 | 轉置後把每列反轉 |

---

### 3) Sort the Matrix Diagonally — LC 1329 — 用 `i - j` 分組

> 依 `i - j` 把格子分組（同一條對角線），各組排序後，再照 row-major 順序寫回矩陣。

**核心洞見**：兩個格子 `(i1,j1)` 與 `(i2,j2)` 在同一條左上→右下對角線上，等價於 `i1 - j1 == i2 - j2`。拿這個當 HashMap 的鍵去收集、排序，再把每條對角線寫回去。

```java
// LC 1329 - Sort the Matrix Diagonally
// IDEA: Group by diagonal key (i-j) → min-heap per diagonal → refill row-major
// time = O(M*N*log(min(M,N))), space = O(M*N)
public int[][] diagonalSort(int[][] mat) {
    int m = mat.length, n = mat[0].length;
    Map<Integer, PriorityQueue<Integer>> map = new HashMap<>();
    // Pass 1: collect each diagonal into a min-heap
    for (int i = 0; i < m; i++)
        for (int j = 0; j < n; j++)
            map.computeIfAbsent(i - j, k -> new PriorityQueue<>()).add(mat[i][j]);
    // Pass 2: refill — row-major order matches diagonal top-to-bottom order
    for (int i = 0; i < m; i++)
        for (int j = 0; j < n; j++)
            mat[i][j] = map.get(i - j).poll();
    return mat;
}
```

**另一種寫法（降冪排序 + 從尾端取出）：**
```java
// Sort list descending, remove from end to get ascending values
for (List<Integer> list : map.values())
    Collections.sort(list, Collections.reverseOrder());
for (int i = 0; i < m; i++)
    for (int j = 0; j < n; j++)
        mat[i][j] = map.get(i - j).remove(map.get(i - j).size() - 1);
```

**同樣用對角線分組鍵的相似題：**
| 題目 | 鍵 | 條件 |
|---------|-----|-----------|
| Toeplitz Matrix (LC 766) | `i - j` | 同組所有格子都要等於第一個 |
| Diagonal Traverse II (LC 1424) | `i + j` | 依反對角線分組；每組再反轉 |
| Sort Matrix Diagonally (LC 1329) | `i - j` | 每組升冪排序 |

---

## 變換與原地修改

### 4) Rotate Image — LC 48 ⭐⭐⭐⭐⭐

> 原地把矩陣順時針轉 90°：先轉置，再把每一列反轉。

```python
# LC 48 - Rotate Image
# V0
# IDEA : TRANSPOSE (i,j -> j,i) -> REVERSE each row
class Solution(object):
    def rotate(self, matrix):
        if not matrix:
            return
        l = len(matrix)
        w = len(matrix[0])
        # Step 1: Transpose — swap matrix[i][j] with matrix[j][i]
        for i in range(l):
            for j in range(i + 1, w):
                matrix[i][j], matrix[j][i] = matrix[j][i], matrix[i][j]
        # Step 2: Reverse each row
        for i in range(l):
            matrix[i] = matrix[i][::-1]
        return matrix
```

```java
// LC 48 - Rotate Image
// IDEA: Transpose (swap [i][j] with [j][i]) then reverse each row
// time = O(N^2), space = O(1)
public void rotate(int[][] matrix) {
    int n = matrix.length;
    for (int i = 0; i < n; i++)
        for (int j = i + 1; j < n; j++) { int t = matrix[i][j]; matrix[i][j] = matrix[j][i]; matrix[j][i] = t; }
    for (int[] row : matrix) { int l = 0, r = row.length - 1; while (l < r) { int t = row[l]; row[l++] = row[r]; row[r--] = t; } }
}
```

---

### 5) Game of Life — LC 289 — 原地狀態轉移

> 依 8 鄰居規則，同時算出所有格子的下一個狀態。

```python
# LC 289 - Game of Life
# V0
# IDEA : copy board, apply all 4 rules using 8-directional neighbors
# Time: O(m*n), Space: O(m*n)
class Solution:
    def gameOfLife(self, board) -> None:
        neighbors = [(1,0),(1,-1),(0,-1),(-1,-1),(-1,0),(-1,1),(0,1),(1,1)]
        rows, cols = len(board), len(board[0])
        copy_board = [[board[r][c] for c in range(cols)] for r in range(rows)]
        for row in range(rows):
            for col in range(cols):
                live_neighbors = sum(
                    copy_board[row + dr][col + dc]
                    for dr, dc in neighbors
                    if 0 <= row + dr < rows and 0 <= col + dc < cols
                )
                # Rule 1 & 3: live cell dies
                if copy_board[row][col] == 1 and (live_neighbors < 2 or live_neighbors > 3):
                    board[row][col] = 0
                # Rule 4: dead cell becomes alive
                elif copy_board[row][col] == 0 and live_neighbors == 3:
                    board[row][col] = 1
```

```java
// LC 289 - Game of Life
// IDEA: Encode next state in same cell: 2 = was dead now alive, -1 = was alive now dead
// time = O(M*N), space = O(1)
public void gameOfLife(int[][] board) {
    int m = board.length, n = board[0].length;
    int[][] dirs = {{0,1},{0,-1},{1,0},{-1,0},{1,1},{1,-1},{-1,1},{-1,-1}};
    for (int i = 0; i < m; i++) for (int j = 0; j < n; j++) {
        int live = 0;
        for (int[] d : dirs) { int r = i+d[0], c = j+d[1]; if (r>=0&&r<m&&c>=0&&c<n&&Math.abs(board[r][c])==1) live++; }
        if (board[i][j] == 1 && (live < 2 || live > 3)) board[i][j] = -1;
        if (board[i][j] == 0 && live == 3) board[i][j] = 2;
    }
    for (int i = 0; i < m; i++) for (int j = 0; j < n; j++) board[i][j] = board[i][j] > 0 ? 1 : 0;
}
```

---

### 6) Set Matrix Zeroes — LC 73 ⭐⭐⭐⭐

> 先標記哪些列／行要清零，再用兩趟掃描套用。

```python
# LC 73 - Set Matrix Zeroes
# V0
# IDEA : collect zero positions first, then set rows/cols to 0
# Time: O(m*n), Space: O(m+n)
class Solution(object):
    def setZeroes(self, matrix):
        if not matrix:
            return
        l, w = len(matrix), len(matrix[0])
        x_zeros = set()  # columns to zero
        y_zeros = set()  # rows to zero
        for i in range(l):
            for j in range(w):
                if matrix[i][j] == 0:
                    x_zeros.add(j)
                    y_zeros.add(i)
        # zero entire rows
        for i in y_zeros:
            matrix[i] = [0] * w
        # zero entire columns
        for j in x_zeros:
            for i in range(l):
                matrix[i][j] = 0
```

```java
// LC 73 - Set Matrix Zeroes
// IDEA: Use first row/col as markers; scan once to mark, once to apply
// time = O(M*N), space = O(1)
public void setZeroes(int[][] matrix) {
    int m = matrix.length, n = matrix[0].length;
    boolean firstRowZero = false, firstColZero = false;
    for (int j = 0; j < n; j++) if (matrix[0][j] == 0) firstRowZero = true;
    for (int i = 0; i < m; i++) if (matrix[i][0] == 0) firstColZero = true;
    for (int i = 1; i < m; i++) for (int j = 1; j < n; j++)
        if (matrix[i][j] == 0) { matrix[i][0] = 0; matrix[0][j] = 0; }
    for (int i = 1; i < m; i++) for (int j = 1; j < n; j++)
        if (matrix[i][0] == 0 || matrix[0][j] == 0) matrix[i][j] = 0;
    if (firstRowZero) Arrays.fill(matrix[0], 0);
    if (firstColZero) for (int i = 0; i < m; i++) matrix[i][0] = 0;
}
```

---

### 7) Image Smoother — LC 661 — 8 方向鄰域

```python
def imageSmoother(M):
    """
    Smooth image by averaging 8-connected neighbors
    Time: O(m*n), Space: O(m*n)
    """
    if not M or not M[0]:
        return []
    
    rows, cols = len(M), len(M[0])
    result = [[0] * cols for _ in range(rows)]
    
    # 8-directional + current cell
    directions = [(di, dj) for di in [-1, 0, 1] for dj in [-1, 0, 1]]
    
    for i in range(rows):
        for j in range(cols):
            total = 0
            count = 0
            
            for di, dj in directions:
                ni, nj = i + di, j + dj
                if 0 <= ni < rows and 0 <= nj < cols:
                    total += M[ni][nj]
                    count += 1
            
            result[i][j] = total // count
    
    return result
```

## 搜尋

### 8) Search a 2D Matrix — LC 74 — 在攤平索引上做二分搜尋

> 把完全排序好的矩陣當成一維陣列，直接二分搜尋。

```python
# LC 74 - Search a 2D Matrix
# V0
# IDEA : BINARY SEARCH — treat matrix as flat sorted array
# Time: O(log(m*n)), Space: O(1)
class Solution(object):
    def searchMatrix(self, matrix, target):
        if not matrix:
            return False
        m, n = len(matrix), len(matrix[0])
        left, right = 0, m * n - 1
        while left <= right:
            mid = (left + right) // 2
            val = matrix[mid // n][mid % n]
            if val == target:
                return True
            elif val < target:
                left = mid + 1
            else:
                right = mid - 1
        return False
```

```java
// LC 74 - Search a 2D Matrix
// IDEA: Binary search treating matrix as flat 1D array; row = mid/n, col = mid%n
// time = O(log(M*N)), space = O(1)
public boolean searchMatrix(int[][] matrix, int target) {
    int m = matrix.length, n = matrix[0].length, l = 0, r = m * n - 1;
    while (l <= r) {
        int mid = (l + r) / 2, val = matrix[mid / n][mid % n];
        if (val == target) return true;
        else if (val < target) l = mid + 1;
        else r = mid - 1;
    }
    return false;
}
```

---

### 9) Search a 2D Matrix II — LC 240 — 階梯式消去 ⭐⭐⭐⭐

> 從右上角出發；每一步消掉一整列或一整行。

```python
# LC 240 - Search a 2D Matrix II
# V0
# IDEA : Start from top-right, eliminate row/col each iteration
# Time: O(m+n), Space: O(1)
class Solution:
    def searchMatrix(self, matrix, target):
        if not matrix or not matrix[0]:
            return False
        row, col = 0, len(matrix[0]) - 1
        while row < len(matrix) and col >= 0:
            if matrix[row][col] == target:
                return True
            elif matrix[row][col] < target:
                row += 1      # eliminate current row
            else:
                col -= 1      # eliminate current column
        return False
```

```java
// LC 240 - Search a 2D Matrix II
// IDEA: Start top-right; if val > target shrink col, if val < target grow row
// time = O(M+N), space = O(1)
public boolean searchMatrix(int[][] matrix, int target) {
    int row = 0, col = matrix[0].length - 1;
    while (row < matrix.length && col >= 0) {
        if (matrix[row][col] == target) return true;
        else if (matrix[row][col] < target) row++;
        else col--;
    }
    return false;
}
```

---

### 10) Kth Smallest Element in a Sorted Matrix — LC 378 — 對答案二分搜尋


> 每一列、每一行都排序好，但整個矩陣**沒有**全域排序 —— 所以 LC 74 那招「攤平成一維排序陣列」在這裡**不能用**。改成對*答案的值*做二分搜尋，再用 O(n) 的階梯走法數出有多少格子 `<= mid`。

**關鍵想法（⭐⭐⭐⭐⭐ —— 矩陣版的「對答案二分搜尋」模式）**

1. 搜尋空間是**值域** `[matrix[0][0], matrix[n-1][n-1]]`，不是索引。
2. `countLessOrEqual(target)` 從**左下角**開始走：若 `mat[r][c] <= target`，則 `(r,c)` 上方整行也都符合 → `cnt += r + 1`，往右移；否則往上移。每次計數 O(n)。
3. 往「使 `count(v) >= k` 成立的最小值 `v`」收斂。這個 `v` 保證是矩陣裡真正存在的元素（計數只會在真實值上達到 `k`），所以不必再檢查它在不在矩陣裡。

| | LC 74 | LC 240 | LC 378 |
|---|---|---|---|
| 矩陣性質 | 整體按 row-major 排序 | 列、行各自排序 | 列、行各自排序 |
| 搜尋空間 | 索引 `0..m*n-1` | 格子 | **值域** |
| 移動規則 | mid → `(mid/n, mid%n)` | 右上角階梯 | 左下角階梯（計數用） |
| 時間 | O(log(m*n)) | O(m+n) | O(n·log(maxV-minV)) |

```java
// LC 378 - Kth Smallest Element in a Sorted Matrix
// IDEA: binary search on VALUE range + O(n) staircase count of cells <= mid
// time = O(N * log(maxVal - minVal)), space = O(1)
public int kthSmallest(int[][] matrix, int k) {
    int n = matrix.length;
    int lo = matrix[0][0], hi = matrix[n - 1][n - 1];
    while (lo < hi) {
        int mid = lo + (hi - lo) / 2;           // avoid overflow
        if (countLessOrEqual(matrix, mid) >= k) hi = mid;   // enough → answer is <= mid
        else lo = mid + 1;                                  // too few → answer is > mid
    }
    return lo;   // lo == hi == smallest value whose count reaches k
}

// count cells <= target, walking from BOTTOM-LEFT
private int countLessOrEqual(int[][] mat, int target) {
    int n = mat.length, cnt = 0;
    int r = n - 1, c = 0;
    while (r >= 0 && c < n) {
        if (mat[r][c] <= target) { cnt += (r + 1); c++; }  // whole column up to r qualifies
        else r--;                                          // too big → move up
    }
    return cnt;
}
```

```python
# LC 378 - Kth Smallest Element in a Sorted Matrix
# IDEA : BINARY SEARCH on value range + staircase counting from bottom-left
# Time: O(n * log(maxVal - minVal)), Space: O(1)
class Solution:
    def kthSmallest(self, matrix, k):
        n = len(matrix)

        def count_le(target):
            cnt, r, c = 0, n - 1, 0
            while r >= 0 and c < n:
                if matrix[r][c] <= target:
                    cnt += r + 1      # all cells above (r,c) in this column qualify
                    c += 1
                else:
                    r -= 1
            return cnt

        lo, hi = matrix[0][0], matrix[n-1][n-1]
        while lo < hi:
            mid = lo + (hi - lo) // 2
            if count_le(mid) >= k:
                hi = mid
            else:
                lo = mid + 1
        return lo
```

**手動跑一遍 —— `matrix = [[1,5,9],[10,11,13],[12,13,15]], k = 8`：**

```text
lo=1, hi=15
mid=8   count<=8  = 2   (<8)  → lo=9
mid=12  count<=12 = 6   (<8)  → lo=13
mid=14  count<=14 = 8   (>=8) → hi=14
mid=13  count<=13 = 8   (>=8) → hi=13
lo == hi == 13  ✓
```

**變形 —— Find the Kth Smallest Sum of a Matrix With Sorted Rows (LC 1439)**
> 同一副「對答案二分搜尋」的骨架，但候選值換成*跨列組合的和*；計數那一步從階梯走法變成對各列做有界的 DFS／堆積。比較好寫也能過的替代解法：一次折疊一列，每次合併後只保留最小的 k 個和。

---

## 格子上的 DFS / BFS

### 11) Number of Islands — LC 200 ⭐⭐⭐⭐⭐

> 用 DFS 把每座島「沉掉」，數出 '1' 的連通塊個數。

```python
# LC 200 - Number of Islands
# V0
# IDEA : DFS — sink each visited land cell to '0'
# Time: O(m*n), Space: O(m*n) recursion stack
class Solution(object):
    def numIslands(self, grid):
        def dfs(grid, x, y):
            if grid[y][x] == "0":
                return
            grid[y][x] = "0"
            for dx, dy in [(0,1),(0,-1),(1,0),(-1,0)]:
                nx, ny = x + dx, y + dy
                if 0 <= nx < w and 0 <= ny < l and grid[ny][nx] == "1":
                    dfs(grid, nx, ny)
        if not grid:
            return 0
        l, w = len(grid), len(grid[0])
        count = 0
        for i in range(l):
            for j in range(w):
                if grid[i][j] == "1":
                    count += 1
                    dfs(grid, j, i)
        return count
```

```java
// LC 200 - Number of Islands
// IDEA: DFS from each unvisited '1'; sink visited cells to '0'
// time = O(M*N), space = O(M*N) recursion stack
public int numIslands(char[][] grid) {
    int count = 0;
    for (int i = 0; i < grid.length; i++)
        for (int j = 0; j < grid[0].length; j++)
            if (grid[i][j] == '1') { dfs(grid, i, j); count++; }
    return count;
}
private void dfs(char[][] grid, int i, int j) {
    if (i < 0 || i >= grid.length || j < 0 || j >= grid[0].length || grid[i][j] != '1') return;
    grid[i][j] = '0';
    dfs(grid, i+1, j); dfs(grid, i-1, j); dfs(grid, i, j+1); dfs(grid, i, j-1);
}
```

---

### 12) Longest Increasing Path in a Matrix — LC 329 — 在 DAG 上做記憶化 DFS


> **關鍵想法**：「嚴格遞增」讓整張格子變成一個 **DAG** —— 同一條路徑上不可能重訪任何格子，所以**不需要 `visited` 集合，也不需要回溯**。只要記憶化就好：`memo[i][j] = 從 (i,j) 出發的最長遞增路徑`。

**為什麼這不是普通的 flood fill**：每條邊都從小值指向大值，所以圖是無環的。每個格子的答案只依賴嚴格更大的鄰居，因此可以安全快取（每格只算一次 → O(m·n)）。

```java
// LC 329 - Longest Increasing Path in a Matrix
// IDEA: DFS + memo on a DAG (edges go small -> large, so no cycle, no visited set)
// time = O(M*N), space = O(M*N)
private static final int[][] DIRS = {{0,1},{0,-1},{1,0},{-1,0}};

public int longestIncreasingPath(int[][] matrix) {
    if (matrix == null || matrix.length == 0 || matrix[0].length == 0) return 0;
    int m = matrix.length, n = matrix[0].length;
    int[][] memo = new int[m][n];        // 0 = not computed yet
    int best = 0;
    for (int i = 0; i < m; i++)
        for (int j = 0; j < n; j++)
            best = Math.max(best, dfs(matrix, i, j, memo));
    return best;
}

private int dfs(int[][] mat, int i, int j, int[][] memo) {
    if (memo[i][j] != 0) return memo[i][j];   // cached
    int best = 1;                              // the cell itself
    for (int[] d : DIRS) {
        int r = i + d[0], c = j + d[1];
        if (r < 0 || r >= mat.length || c < 0 || c >= mat[0].length) continue;
        if (mat[r][c] <= mat[i][j]) continue;  // must strictly increase
        best = Math.max(best, 1 + dfs(mat, r, c, memo));
    }
    memo[i][j] = best;
    return best;
}
```

```python
# LC 329 - Longest Increasing Path in a Matrix
# IDEA : DFS + MEMOIZATION on a DAG (strictly increasing => acyclic => no visited set)
# Time: O(m*n), Space: O(m*n)
from functools import lru_cache

class Solution:
    def longestIncreasingPath(self, matrix):
        if not matrix or not matrix[0]:
            return 0
        m, n = len(matrix), len(matrix[0])

        @lru_cache(maxsize=None)
        def dfs(i, j):
            best = 1
            for di, dj in ((0,1), (0,-1), (1,0), (-1,0)):
                r, c = i + di, j + dj
                if 0 <= r < m and 0 <= c < n and matrix[r][c] > matrix[i][j]:
                    best = max(best, 1 + dfs(r, c))
            return best

        return max(dfs(i, j) for i in range(m) for j in range(n))
```

**常見錯誤**
- 加上 `visited` 集合再回溯 → 答案對，但變成 O(4^(m·n))；讓它變線性的是記憶化。
- 用 `>=` 而不是 `>` → 相等的值之間會形成環，遞迴永遠停不下來。
- `memo` 初始化為 `0` 之所以安全，只是因為真正的答案一定 `>= 1`。

---

## 二維動態規劃

### 13) Minimum Path Sum — LC 64

> DP：每一格累積走到它的最小成本。

```python
# LC 64 - Minimum Path Sum
# V0
# IDEA : DP — dp[i][j] = grid[i][j] + min(dp[i-1][j], dp[i][j-1])
# Time: O(m*n), Space: O(1) (modify grid in-place)
class Solution:
    def minPathSum(self, grid):
        if not grid:
            return 0
        m, n = len(grid), len(grid[0])
        # fill first column
        for i in range(1, m):
            grid[i][0] += grid[i-1][0]
        # fill first row
        for j in range(1, n):
            grid[0][j] += grid[0][j-1]
        # fill rest
        for i in range(1, m):
            for j in range(1, n):
                grid[i][j] += min(grid[i-1][j], grid[i][j-1])
        return grid[-1][-1]
```

```java
// LC 64 - Minimum Path Sum
// IDEA: DP in-place; dp[i][j] += min(dp[i-1][j], dp[i][j-1])
// time = O(M*N), space = O(1)
public int minPathSum(int[][] grid) {
    int m = grid.length, n = grid[0].length;
    for (int i = 1; i < m; i++) grid[i][0] += grid[i-1][0];
    for (int j = 1; j < n; j++) grid[0][j] += grid[0][j-1];
    for (int i = 1; i < m; i++)
        for (int j = 1; j < n; j++)
            grid[i][j] += Math.min(grid[i-1][j], grid[i][j-1]);
    return grid[m-1][n-1];
}
```

---

### 14) Maximal Square — LC 221 ⭐⭐⭐

> `dp[i][j]` = 以 (i,j) 為右下角的最大正方形邊長。

```python
# LC 221 - Maximal Square
# V0
# IDEA : DP — dp[i][j] = min(dp[i-1][j], dp[i][j-1], dp[i-1][j-1]) + 1
# Time: O(m*n), Space: O(m*n)
class Solution:
    def maximalSquare(self, matrix):
        if not matrix:
            return 0
        m, n = len(matrix), len(matrix[0])
        dp = [[0] * n for _ in range(m)]
        ans = 0
        for i in range(m):
            for j in range(n):
                dp[i][j] = int(matrix[i][j])
                if i and j and dp[i][j]:
                    dp[i][j] = min(dp[i-1][j-1], dp[i][j-1], dp[i-1][j]) + 1
                ans = max(ans, dp[i][j])
        return ans * ans
```

```java
// LC 221 - Maximal Square
// IDEA: dp[i][j] = min(left, top, diag) + 1 when cell is '1'; ans = max dp^2
// time = O(M*N), space = O(M*N)
public int maximalSquare(char[][] matrix) {
    int m = matrix.length, n = matrix[0].length, ans = 0;
    int[][] dp = new int[m + 1][n + 1];
    for (int i = 1; i <= m; i++)
        for (int j = 1; j <= n; j++)
            if (matrix[i-1][j-1] == '1') {
                dp[i][j] = Math.min(dp[i-1][j-1], Math.min(dp[i-1][j], dp[i][j-1])) + 1;
                ans = Math.max(ans, dp[i][j]);
            }
    return ans * ans;
}
```

**變形 —— Count Square Submatrices with All Ones (LC 1277)**
> 同一條 `min(left, top, diag) + 1` 遞迴式 —— 差別在**把所有 `dp` 值加總，而不是取最大值**：`dp[i][j] == k` 的格子，剛好是 k 個全 1 正方形（邊長 1..k）的右下角。

```java
// LC 1277 - Count Square Submatrices with All Ones
// IDEA: Maximal Square DP, but accumulate dp[i][j] instead of max; reuse grid as dp table
// time = O(M*N), space = O(1)
public int countSquares(int[][] matrix) {
    int m = matrix.length, n = matrix[0].length, total = 0;
    for (int i = 0; i < m; i++) {
        for (int j = 0; j < n; j++) {
            if (matrix[i][j] == 1 && i > 0 && j > 0) {
                matrix[i][j] = 1 + Math.min(matrix[i-1][j-1],
                                   Math.min(matrix[i-1][j], matrix[i][j-1]));
            }
            total += matrix[i][j];   // dp[i][j] squares end at (i,j)
        }
    }
    return total;
}
```

```python
# LC 1277 - Count Square Submatrices with All Ones
# IDEA : Maximal Square DP, but accumulate dp[i][j] instead of max
# Time: O(m*n), Space: O(1) (in-place)
class Solution:
    def countSquares(self, matrix):
        m, n = len(matrix), len(matrix[0])
        total = 0
        for i in range(m):
            for j in range(n):
                if matrix[i][j] == 1 and i > 0 and j > 0:
                    matrix[i][j] = 1 + min(matrix[i-1][j-1], matrix[i-1][j], matrix[i][j-1])
                total += matrix[i][j]
        return total
```

> 延伸：**LC 1504 Count Submatrices With All Ones** 數的是*矩形*（不只正方形）—— 正方形那套 DP 就不適用了；改用逐行的連續 1 高度加上單調堆疊（見下面第 2-15 節）。

---

### 15) Maximal Rectangle — LC 85 — 逐列降維成直方圖


> **關鍵想法（⭐⭐⭐⭐⭐）**：把二維問題降成一維。由上而下掃每一列，維護 `heights[j]` = 第 j 行在當前列結尾的連續 `1` 個數。這樣每一列就是一座直方圖 → 對它跑 **Largest Rectangle in Histogram (LC 84)**，取最大值。

```text
matrix                heights after each row
1 0 1 0 0             [1,0,1,0,0]  → max area 1
1 0 1 1 1             [2,0,2,1,1]  → max area 3
1 1 1 1 1             [3,1,3,2,2]  → max area 6   ← answer
1 0 0 1 0             [4,0,0,3,0]  → max area 4
```

```java
// LC 85 - Maximal Rectangle
// IDEA: per-row histogram of consecutive 1s + LC 84 monotonic stack
// time = O(M*N), space = O(N)
public int maximalRectangle(char[][] matrix) {
    if (matrix == null || matrix.length == 0 || matrix[0].length == 0) return 0;
    int n = matrix[0].length, best = 0;
    int[] heights = new int[n];
    for (char[] row : matrix) {
        // build histogram for this row: reset to 0 on '0', else grow
        for (int j = 0; j < n; j++) heights[j] = (row[j] == '1') ? heights[j] + 1 : 0;
        best = Math.max(best, largestRectangleArea(heights));
    }
    return best;
}

// LC 84 - Largest Rectangle in Histogram (increasing monotonic stack of indices)
private int largestRectangleArea(int[] h) {
    int n = h.length, best = 0;
    Deque<Integer> st = new ArrayDeque<>();
    for (int i = 0; i <= n; i++) {
        int cur = (i == n) ? 0 : h[i];          // sentinel 0 flushes the stack
        while (!st.isEmpty() && h[st.peek()] >= cur) {
            int height = h[st.pop()];
            int left = st.isEmpty() ? -1 : st.peek();   // previous smaller index
            best = Math.max(best, height * (i - left - 1));
        }
        st.push(i);
    }
    return best;
}
```

```python
# LC 85 - Maximal Rectangle
# IDEA : per-row histogram of consecutive 1s + LC 84 monotonic stack
# Time: O(m*n), Space: O(n)
class Solution:
    def maximalRectangle(self, matrix):
        if not matrix or not matrix[0]:
            return 0
        n = len(matrix[0])
        heights = [0] * n
        best = 0
        for row in matrix:
            for j in range(n):
                heights[j] = heights[j] + 1 if row[j] == '1' else 0
            best = max(best, self.largestRectangleArea(heights))
        return best

    def largestRectangleArea(self, h):
        st, best = [], 0
        for i in range(len(h) + 1):
            cur = 0 if i == len(h) else h[i]      # sentinel flush
            while st and h[st[-1]] >= cur:
                height = h[st.pop()]
                left = st[-1] if st else -1
                best = max(best, height * (i - left - 1))
            st.append(i)
        return best
```

**用同一套「列直方圖降維」的相關題：**

| 題目 | LC # | 變化點 |
|---------|------|-------|
| Maximal Rectangle | 85 | 求全 `1` 矩形的最大面積 |
| Count Submatrices With All Ones | 1504 | 改成**數**全 `1` 矩形，而不是求最大（堆疊要維護逐行的累加值） |
| Maximal Square | 221 | 只要正方形 → 用更簡單的 `min(left, top, diag)+1` DP（見 14) Maximal Square） |

---

## 前綴和與列對壓縮

### 16) Matrix Block Sum — LC 1314 — 二維前綴和

> 先建二維前綴和矩陣，之後每一格的區塊和都能 O(1) 查詢。

```java
// LC 1314 - Matrix Block Sum
// V0
// IDEA: 2D Prefix Sum (Summed-Area Table)
// Time: O(m*n), Space: O(m*n)

/**
 * Key Insight:
 * - Without prefix sum: O(m*n*k²) — for each cell, scan k×k block
 * - With prefix sum: O(m*n) build + O(1) per query
 *
 * Formula:
 * - Build:  pref[i+1][j+1] = mat[i][j] + pref[i][j+1] + pref[i+1][j] - pref[i][j]
 * - Query:  sum = pref[r2+1][c2+1] - pref[r1][c2+1] - pref[r2+1][c1] + pref[r1][c1]
 *
 * The +1 offset allows pref[0][j] and pref[i][0] to be zero padding,
 * preventing IndexOutOfBounds when querying edges.
 */
public int[][] matrixBlockSum(int[][] mat, int k) {
    int m = mat.length;
    int n = mat[0].length;

    // 1. Build 2D prefix sum matrix (size m+1 x n+1)
    int[][] pref = new int[m + 1][n + 1];
    for (int i = 0; i < m; i++) {
        for (int j = 0; j < n; j++) {
            pref[i + 1][j + 1] = mat[i][j]
                    + pref[i][j + 1]      // top
                    + pref[i + 1][j]      // left
                    - pref[i][j];         // top-left (subtracted twice)
        }
    }

    int[][] res = new int[m][n];

    // 2. Calculate sum for each block [i-k, j-k] to [i+k, j+k]
    for (int i = 0; i < m; i++) {
        for (int j = 0; j < n; j++) {
            // Clamp boundaries to valid matrix indices
            int r1 = Math.max(0, i - k);
            int c1 = Math.max(0, j - k);
            int r2 = Math.min(m - 1, i + k);
            int c2 = Math.min(n - 1, j + k);

            // Query using prefix sum formula (adjust for 1-based pref)
            res[i][j] = pref[r2 + 1][c2 + 1]
                    - pref[r1][c2 + 1]    // subtract top region
                    - pref[r2 + 1][c1]    // subtract left region
                    + pref[r1][c1];       // add back top-left (double subtracted)
        }
    }

    return res;
}
```

**二維前綴和查詢的圖解：**
```text
For rectangle (r1,c1) to (r2,c2):

     0    c1        c2   n
   ┌──────┬─────────┬────┐
 0 │      │    A    │    │
   │      │         │    │
r1 ├──────┼─────────┼────┤
   │      │         │    │
   │  C   │ TARGET  │    │
   │      │         │    │
r2 ├──────┼─────────┼────┤
   │      │         │    │
 m └──────┴─────────┴────┘

TARGET = pref[r2+1][c2+1] - A - C + TopLeft
       = pref[r2+1][c2+1] - pref[r1][c2+1] - pref[r2+1][c1] + pref[r1][c1]
```

**相似題：**
- LC 304: Range Sum Query 2D - Immutable（同一套二維前綴和）
- LC 308: Range Sum Query 2D - Mutable（需要線段樹／樹狀陣列）
- LC 1292: Maximum Side Length of Square（二維前綴和 + 二分搜尋）

---

### 17) Number of Submatrices That Sum to Target — LC 1074 — 列對壓縮


> **關鍵想法（⭐⭐⭐⭐⭐）**：固定一對行（或列），把中間這條帶壓成**一維陣列**，然後套一維的「子陣列和等於 K」雜湊表技巧。這招把任何「在所有子矩陣上計數／最佳化」的問題，變成 `O(n²)` 條帶 × 一趟一維掃描。

**做法**
1. 對每一**列**做前綴和，這樣第 i 列 `[c1..c2]` 這段的和是 O(1)。
2. 對每一組行對 `(c1 <= c2)`：往下走每一列並累加 `sum`，用一個以 `{0: 1}` 起始的 HashMap 數出先前出現過幾次等於 `sum - target` 的前綴。
3. 總計：時間 `O(m·n²)`，額外空間 `O(m)`（把比較小的那個維度當作「配對」的維度）。

```java
// LC 1074 - Number of Submatrices That Sum to Target
// IDEA: row prefix sums -> fix column pair (c1,c2) -> 1-D "subarray sum == target" hashmap
// time = O(M*N*N), space = O(M)
// NOTE: mutates the input matrix into row prefix sums; copy first if that matters
public int numSubmatrixSumTarget(int[][] matrix, int target) {
    int m = matrix.length, n = matrix[0].length;

    // 1. prefix sum along each row
    for (int i = 0; i < m; i++)
        for (int j = 1; j < n; j++)
            matrix[i][j] += matrix[i][j - 1];

    int res = 0;
    Map<Integer, Integer> cnt = new HashMap<>();
    // 2. every column pair defines a vertical strip
    for (int c1 = 0; c1 < n; c1++) {
        for (int c2 = c1; c2 < n; c2++) {
            cnt.clear();
            cnt.put(0, 1);          // empty prefix
            int sum = 0;
            // 3. 1-D subarray-sum-equals-target scan down the rows
            for (int i = 0; i < m; i++) {
                sum += matrix[i][c2] - (c1 > 0 ? matrix[i][c1 - 1] : 0);
                res += cnt.getOrDefault(sum - target, 0);
                cnt.merge(sum, 1, Integer::sum);
            }
        }
    }
    return res;
}
```

```python
# LC 1074 - Number of Submatrices That Sum to Target
# IDEA : row prefix sums -> fix column pair -> 1-D subarray-sum-equals-target hashmap
# Time: O(m*n^2), Space: O(m)
from collections import defaultdict

class Solution:
    def numSubmatrixSumTarget(self, matrix, target):
        m, n = len(matrix), len(matrix[0])
        # 1. prefix sum along each row
        for i in range(m):
            for j in range(1, n):
                matrix[i][j] += matrix[i][j-1]

        res = 0
        for c1 in range(n):                 # 2. fix left column
            for c2 in range(c1, n):         #    fix right column
                cnt = defaultdict(int)
                cnt[0] = 1
                cur = 0
                for i in range(m):          # 3. scan rows as a 1-D array
                    cur += matrix[i][c2] - (matrix[i][c1-1] if c1 > 0 else 0)
                    res += cnt[cur - target]
                    cnt[cur] += 1
        return res
```

**變形 —— Max Sum of Rectangle No Larger Than K (LC 363)**
> 一樣的列對壓縮，但內層那一維的動作從「雜湊表查相等」換成「在有序結構裡找最小的、`>= cur - k` 的前綴」（Java 用 `TreeSet.ceiling`，Python 用 `sortedcontainers` 或在維護好的有序串列上 `bisect`）→ `O(m·n²·log m)`。

**壓縮技巧速查**

| 在所有子矩陣上的目標 | 內層一維的做法 | LC |
|---|---|---|
| 數出和 == target 的個數 | 前綴計數的 HashMap | 1074 |
| 最大和 <= K | 有序集合 + `ceiling` | 363 |
| 最大和（不設上限） | Kadane 演算法 | — |
| 任意矩形的區間和 | 二維前綴和（見 16) Matrix Block Sum） | 304, 1314 |

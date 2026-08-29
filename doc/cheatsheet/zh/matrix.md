# Matrix Data Structure

> **範圍** — 把二維格子當成一個獨立主題來看 —— 走訪的幾何（螺旋、對角、旋轉、轉置）、原地標記，以及索引↔座標的換算。
> **另見**：[matrix_examples.md](./matrix_examples.md) — 撐起這些模板的十七題詳解；[dfs.md](./dfs.md) 和 [bfs.md](./bfs.md) — 格子搜尋；[array.md](./array.md) — 這些操作所倚賴的一維基礎；[prefix_sum.md](./prefix_sum.md) — 二維區間和；[dp.md](./dp.md) — 格子 DP。

## LeetCode 題目清單

- [Matrix](https://leetcode.com/problem-list/matrix/)

## 總覽

**矩陣（Matrix）**是一種二維陣列資料結構，把元素放在列與行組成的格子裡。舉凡格子、影像、棋盤、數學運算相關的問題，它都是基礎。

### 關鍵性質
- **時間複雜度**： 
  - 存取：O(1)
  - 走訪：O(m*n)，m=列數、n=行數
  - 搜尋：未排序是 O(m*n)，已排序矩陣是 O(log(m*n))
- **空間複雜度**：O(m*n) 儲存空間
- **核心想法**：元素以 [row][column] 索引存取
- **什麼時候用**：格子類問題、二維變換、路徑搜尋、格子上的動態規劃

### 題型分類

#### **模式 1：矩陣走訪**
- **說明**：用特定的順序在矩陣元素間移動（螺旋、對角、之字形）
- **例子**：LC 54, 59, 498, 885
- **關鍵模式**：以邊界為準的移動，配合轉向

#### **模式 2：矩陣變換** 
- **說明**：旋轉、轉置或翻轉矩陣，可以原地做也可以另開一個
- **例子**：LC 48, 867, 189, 1886
- **關鍵模式**：座標的數學對應

#### **模式 3：矩陣搜尋**
- **說明**：在已排序或部分排序的二維矩陣中找元素
- **例子**：LC 74, 240, 378, 668
- **關鍵模式**：二分搜尋的各種變形，或以「排除」為主的搜尋

#### **模式 4：矩陣修改**
- **說明**：依條件更新矩陣元素（歸零、平滑化）
- **例子**：LC 73, 661, 289, 1314
- **關鍵模式**：兩趟掃描，或借助額外空間

#### **模式 5：矩陣乘法與運算**
- **說明**：矩陣之間的數學運算或矩陣計算
- **例子**：LC 311, 348, 1572, 1351
- **關鍵模式**：乘法用三層巢狀迴圈；稀疏矩陣另有最佳化

#### **模式 6：矩陣路徑與動態規劃**
- **說明**：在矩陣上找路徑、數路徑條數，或最佳化路徑上的值
- **例子**：LC 62, 63, 64, 120, 931
- **關鍵模式**：以相鄰格子為基礎的 DP 狀態轉移

## 模板與演算法

### 模板對照表

| 模板類型 | 適用情境 | 關鍵結構 | 什麼時候用 |
|---------------|----------|---------------|-------------|
| **矩陣走訪** | 螺旋、對角移動 | 邊界追蹤 + 方向向量 | 有順序的元素處理 |
| **矩陣變換** | 旋轉、轉置、翻轉 | 座標的數學對應 | 原地修改 |
| **矩陣搜尋** | 在排序矩陣中找目標 | 二分搜尋或排除法 | 已排序／部分排序的矩陣 |
| **矩陣修改** | 歸零、平滑化 | 兩趟掃描或額外記錄 | 依條件更新元素 |
| **矩陣乘法** | 內積運算 | 三層巢狀迴圈 | 數學計算 |
| **二維前綴和** | 區間和查詢 | 先建前綴 + O(1) 查詢 | 區塊和、子矩陣和 |
| **矩陣路徑 DP** | 數路徑、最小／最大路徑 | DP 狀態轉移 | 格子上的最佳化問題 |

### 通用矩陣模板 ⭐⭐⭐⭐

```python
def solve_matrix_problem(matrix):
    if not matrix or not matrix[0]:
        return default_result
    
    rows, cols = len(matrix), len(matrix[0])
    
    # Initialize result structure
    result = initialize_result(rows, cols)
    
    # Main processing loop
    for i in range(rows):
        for j in range(cols):
            # Process current cell
            process_cell(matrix, i, j, result)
    
    return result

def process_cell(matrix, row, col, result):
    # Template for cell processing
    # - Check boundaries
    # - Apply logic
    # - Update result
    pass
```

### 各模式專屬模板

#### 模板 1：矩陣走訪（螺旋／對角）
```python
def spiral_traversal(matrix):
    if not matrix or not matrix[0]:
        return []
    
    result = []
    rows, cols = len(matrix), len(matrix[0])
    
    # Define boundaries
    top, bottom = 0, rows - 1
    left, right = 0, cols - 1
    
    while top <= bottom and left <= right:
        # Right movement
        for j in range(left, right + 1):
            result.append(matrix[top][j])
        top += 1
        
        # Down movement  
        for i in range(top, bottom + 1):
            result.append(matrix[i][right])
        right -= 1
        
        # Left movement (if still valid row)
        if top <= bottom:
            for j in range(right, left - 1, -1):
                result.append(matrix[bottom][j])
            bottom -= 1
        
        # Up movement (if still valid column)
        if left <= right:
            for i in range(bottom, top - 1, -1):
                result.append(matrix[i][left])
            left += 1
    
    return result
```

#### 模板 2：矩陣變換（旋轉／轉置）
```python
def rotate_matrix_90_clockwise(matrix):
    """
    Two-step approach: Transpose + Reverse rows
    """
    n = len(matrix)
    
    # Step 1: Transpose matrix (swap matrix[i][j] with matrix[j][i])
    for i in range(n):
        for j in range(i + 1, n):  # Start from i+1 to avoid double swap
            matrix[i][j], matrix[j][i] = matrix[j][i], matrix[i][j]
    
    # Step 2: Reverse each row
    for i in range(n):
        matrix[i].reverse()
    
    return matrix

def transpose_matrix(matrix):
    """
    Create new matrix with swapped dimensions
    """
    if not matrix or not matrix[0]:
        return []
    
    rows, cols = len(matrix), len(matrix[0])
    result = [[0] * rows for _ in range(cols)]
    
    for i in range(rows):
        for j in range(cols):
            result[j][i] = matrix[i][j]
    
    return result
```

#### 模板 3：矩陣搜尋
```python
def search_matrix_binary(matrix, target):
    """
    Binary search on sorted matrix (treat as 1D array)
    """
    if not matrix or not matrix[0]:
        return False
    
    rows, cols = len(matrix), len(matrix[0])
    left, right = 0, rows * cols - 1
    
    while left <= right:
        mid = (left + right) // 2
        mid_row, mid_col = mid // cols, mid % cols
        mid_val = matrix[mid_row][mid_col]
        
        if mid_val == target:
            return True
        elif mid_val < target:
            left = mid + 1
        else:
            right = mid - 1
    
    return False

def search_matrix_elimination(matrix, target):
    """
    Search in row-wise and column-wise sorted matrix
    Start from top-right or bottom-left corner
    """
    if not matrix or not matrix[0]:
        return False
    
    rows, cols = len(matrix), len(matrix[0])
    row, col = 0, cols - 1  # Start from top-right
    
    while row < rows and col >= 0:
        if matrix[row][col] == target:
            return True
        elif matrix[row][col] > target:
            col -= 1  # Move left
        else:
            row += 1  # Move down
    
    return False
```

#### 模板 4：矩陣修改
```python
def set_matrix_zeros(matrix):
    """
    Set entire row and column to zero if any element is zero
    """
    if not matrix or not matrix[0]:
        return
    
    rows, cols = len(matrix), len(matrix[0])
    
    # Use first row and column as markers
    first_row_zero = any(matrix[0][j] == 0 for j in range(cols))
    first_col_zero = any(matrix[i][0] == 0 for i in range(rows))
    
    # Mark zeros in first row and column
    for i in range(1, rows):
        for j in range(1, cols):
            if matrix[i][j] == 0:
                matrix[0][j] = 0  # Mark column
                matrix[i][0] = 0  # Mark row
    
    # Set zeros based on markers
    for i in range(1, rows):
        for j in range(1, cols):
            if matrix[0][j] == 0 or matrix[i][0] == 0:
                matrix[i][j] = 0
    
    # Handle first row and column
    if first_row_zero:
        for j in range(cols):
            matrix[0][j] = 0
    if first_col_zero:
        for i in range(rows):
            matrix[i][0] = 0
```

#### 模板 5：矩陣乘法
```python
def multiply_matrices(A, B):
    """
    Standard matrix multiplication: C[i][j] = sum(A[i][k] * B[k][j])
    """
    if not A or not A[0] or not B or not B[0]:
        return []
    
    rows_A, cols_A = len(A), len(A[0])
    rows_B, cols_B = len(B), len(B[0])
    
    if cols_A != rows_B:
        return []  # Invalid dimensions
    
    result = [[0] * cols_B for _ in range(rows_A)]
    
    for i in range(rows_A):
        for j in range(cols_B):
            for k in range(cols_A):
                result[i][j] += A[i][k] * B[k][j]
    
    return result

def multiply_sparse_matrices(A, B):
    """
    Optimized multiplication for sparse matrices
    """
    if not A or not A[0] or not B or not B[0]:
        return []
    
    rows_A, cols_A = len(A), len(A[0])
    rows_B, cols_B = len(B), len(B[0])
    result = [[0] * cols_B for _ in range(rows_A)]
    
    for i in range(rows_A):
        for k in range(cols_A):
            if A[i][k] != 0:  # Skip if zero
                for j in range(cols_B):
                    result[i][j] += A[i][k] * B[k][j]
    
    return result
```

#### 模板 6：二維前綴和（區間和查詢）
```python
def build_prefix_sum_2d(mat):
    """
    Build 2D prefix sum matrix for O(1) range sum queries.

    Key Formula:
    - Build: pref[i+1][j+1] = mat[i][j] + pref[i][j+1] + pref[i+1][j] - pref[i][j]
    - Query: sum(r1,c1 to r2,c2) = pref[r2+1][c2+1] - pref[r1][c2+1] - pref[r2+1][c1] + pref[r1][c1]

    Time: O(m*n) build, O(1) query
    Space: O(m*n)
    """
    if not mat or not mat[0]:
        return []

    m, n = len(mat), len(mat[0])

    # Size (m+1) x (n+1) for easier boundary handling (row 0 and col 0 are zeros)
    pref = [[0] * (n + 1) for _ in range(m + 1)]

    # Build prefix sum
    for i in range(m):
        for j in range(n):
            pref[i + 1][j + 1] = mat[i][j] + pref[i][j + 1] + pref[i + 1][j] - pref[i][j]

    return pref

def range_sum_2d(pref, r1, c1, r2, c2):
    """
    Get sum of rectangle from (r1,c1) to (r2,c2) inclusive.

    Visual explanation:
    ┌───────────────────┐
    │   A    │    B     │
    │────────┼──────────│ ← r1
    │   C    │ TARGET   │
    │────────┼──────────│ ← r2
    └───────────────────┘
              c1       c2

    TARGET = Total - A - C + TopLeft (since TopLeft subtracted twice)
           = pref[r2+1][c2+1] - pref[r1][c2+1] - pref[r2+1][c1] + pref[r1][c1]
    """
    return pref[r2 + 1][c2 + 1] - pref[r1][c2 + 1] - pref[r2 + 1][c1] + pref[r1][c1]


def matrix_block_sum(mat, k):
    """
    LC 1314: For each cell, return sum of all elements within k distance.
    """
    m, n = len(mat), len(mat[0])
    pref = build_prefix_sum_2d(mat)
    res = [[0] * n for _ in range(m)]

    for i in range(m):
        for j in range(n):
            # Clamp boundaries
            r1 = max(0, i - k)
            c1 = max(0, j - k)
            r2 = min(m - 1, i + k)
            c2 = min(n - 1, j + k)

            res[i][j] = range_sum_2d(pref, r1, c1, r2, c2)

    return res
```

#### 模板 7：矩陣路徑 DP
```python
def min_path_sum(grid):
    """
    Find minimum path sum from top-left to bottom-right
    """
    if not grid or not grid[0]:
        return 0
    
    rows, cols = len(grid), len(grid[0])
    
    # Initialize DP table (can modify grid in-place to save space)
    dp = [[0] * cols for _ in range(rows)]
    dp[0][0] = grid[0][0]
    
    # Fill first row
    for j in range(1, cols):
        dp[0][j] = dp[0][j-1] + grid[0][j]
    
    # Fill first column
    for i in range(1, rows):
        dp[i][0] = dp[i-1][0] + grid[i][0]
    
    # Fill rest of the table
    for i in range(1, rows):
        for j in range(1, cols):
            dp[i][j] = grid[i][j] + min(dp[i-1][j], dp[i][j-1])
    
    return dp[rows-1][cols-1]

def unique_paths(m, n):
    """
    Count unique paths from top-left to bottom-right
    """
    dp = [[1] * n for _ in range(m)]
    
    for i in range(1, m):
        for j in range(1, n):
            dp[i][j] = dp[i-1][j] + dp[i][j-1]
    
    return dp[m-1][n-1]
```

### 必備的矩陣性質

#### 對角線性質

##### 主對角線（Primary / Main Diagonal）
- **公式**：`matrix[i][i]` —— 列索引等於行索引
- **性質**：位置 (i,j) 滿足 `i - j = 常數`（主對角線恆為 0）
- **方向**：左上 → 右下
- **元素**：(0,0), (1,1), (2,2), ..., (n-1,n-1)

##### 副對角線（Secondary / Anti-Diagonal）
- **公式**：`matrix[i][n - 1 - i]` —— 列 + 行 = n - 1
- **性質**：位置 (i,j) 滿足 `i + j = n - 1`
- **方向**：右上 → 左下
- **元素**：(0,n-1), (1,n-2), (2,n-3), ..., (n-1,0)

##### 對角線分組的鍵（所有「左上 → 右下」方向的對角線）
- **公式**：所有 `i - j` 相同的格子 `(i, j)` 都在同一條對角線上
- **性質**：`i - j = 常數` → 同一條對角線；主對角線為 0，下方為正、上方為負
- **方向**：左上 → 右下（跟主對角線同一族）
- **使用情境**：把每條對角線獨立分組、排序或處理（LC 1329、LC 766）

```text
Example (3×4 matrix) — diagonal keys (i - j):
       j=0   j=1   j=2   j=3
i=0  [  0 ] [ -1 ] [ -2 ] [ -3 ]
i=1  [  1 ] [  0 ] [ -1 ] [ -2 ]
i=2  [  2 ] [  1 ] [  0 ] [ -1 ]

Diagonal key 0  → (0,0), (1,1), (2,2)       ← main diagonal
Diagonal key -1 → (0,1), (1,2), (2,3)       ← above main
Diagonal key  1 → (1,0), (2,1)              ← below main
```

**核心演算法（把每條對角線排序／處理）：**
```java
// Step 1: group elements by diagonal key
Map<Integer, PriorityQueue<Integer>> map = new HashMap<>();
for (int i = 0; i < m; i++)
    for (int j = 0; j < n; j++)
        map.computeIfAbsent(i - j, k -> new PriorityQueue<>()).add(mat[i][j]);

// Step 2: refill matrix — traverse in same row-major order
for (int i = 0; i < m; i++)
    for (int j = 0; j < n; j++)
        mat[i][j] = map.get(i - j).poll();   // min-heap gives ascending order
```

**為什麼照列優先順序回填就對了**：走訪時每條對角線的格子都是由左上往右下拜訪，所以照這個順序從最小堆積取值，寫回去的值沿著每條對角線就是遞增的。

##### 視覺化範例（4×4 矩陣，n=4）

```text
Primary Diagonal (i, i):           Secondary Diagonal (i, n-1-i):

      Col 0   Col 1   Col 2   Col 3        Col 0   Col 1   Col 2   Col 3
Row 0 [ X ]   [ . ]   [ . ]   [ . ]  Row 0 [ . ]   [ . ]   [ . ]   [ X ]  ← (0, 3)
Row 1 [ . ]   [ X ]   [ . ]   [ . ]  Row 1 [ . ]   [ . ]   [ X ]   [ . ]  ← (1, 2)
Row 2 [ . ]   [ . ]   [ X ]   [ . ]  Row 2 [ . ]   [ X ]   [ . ]   [ . ]  ← (2, 1)
Row 3 [ . ]   [ . ]   [ . ]   [ X ]  Row 3 [ X ]   [ . ]   [ . ]   [ . ]  ← (3, 0)
        ↓       ↓       ↓       ↓
      (0,0)   (1,1)   (2,2)   (3,3)

Both Diagonals Together:
      Col 0   Col 1   Col 2   Col 3
Row 0 [ P ]   [ . ]   [ . ]   [ S ]    P = Primary, S = Secondary
Row 1 [ . ]   [ P ]   [ S ]   [ . ]
Row 2 [ . ]   [ S ]   [ P ]   [ . ]
Row 3 [ S ]   [ . ]   [ . ]   [ P ]
```

##### 邊界情況：奇數大小的矩陣（兩條對角線會相交）
當矩陣邊長 `n` 是**奇數**時，主對角線與副對角線會在正中央那格**相交**。

```text
3×3 Matrix (n=3):
      Col 0   Col 1   Col 2
Row 0 [ P ]   [ . ]   [ S ]    (0,0) and (0,2)
Row 1 [ . ]   [P&S]   [ . ]    (1,1) is BOTH primary AND secondary!
Row 2 [ S ]   [ . ]   [ P ]    (2,0) and (2,2)

Center cell (1,1): i=1, i=1 (primary) AND n-1-i=3-1-1=1 (secondary)
```

**重要**：同時走兩條對角線時，別把中心那格算了兩次！

##### 對角線存取模板（Python）
```python
def get_diagonal_elements(matrix):
    """
    Get all elements on both diagonals of a square matrix.
    """
    if not matrix or not matrix[0]:
        return []

    n = len(matrix)
    elements = set()  # Use set to avoid double-counting center in odd-sized matrix

    for i in range(n):
        # Primary diagonal: (i, i)
        elements.add(matrix[i][i])

        # Secondary diagonal: (i, n - 1 - i)
        elements.add(matrix[i][n - 1 - i])

    return list(elements)

def process_diagonals(matrix):
    """
    Process both diagonals with explicit handling of intersection.
    """
    n = len(matrix)
    result = 0

    for i in range(n):
        # Process primary diagonal
        result = process(matrix[i][i], result)

        # Process secondary diagonal (skip if same as primary to avoid double-processing)
        if i != n - 1 - i:  # Not the center cell
            result = process(matrix[i][n - 1 - i], result)

    return result
```

##### 對角線存取模板（Java）
```java
// From LC 2614 - Prime In Diagonal
public int diagonalPrime(int[][] nums) {
    int n = nums.length;
    int maxPrime = 0;

    for (int i = 0; i < n; i++) {
        // 1. Primary Diagonal: (i, i)
        int val1 = nums[i][i];
        if (val1 > maxPrime && isPrime(val1)) {
            maxPrime = val1;
        }

        // 2. Secondary Diagonal: (i, n - 1 - i)
        int val2 = nums[i][n - 1 - i];
        if (val2 > maxPrime && isPrime(val2)) {
            maxPrime = val2;
        }
    }
    return maxPrime;
}
```

##### 對角線座標總表
| 對角線類型 | 格子形式 | 性質 | 方向 |
|---------------|-------------|----------|-----------|
| **主對角線** | `(i, i)` | `row == col` | ↘（左上到右下） |
| **副對角線** | `(i, n-1-i)` | `row + col == n-1` | ↙（右上到左下） |

##### 相關題目
| 題目 | LC # | 對角線鍵 | 技巧 |
|---------|------|--------------|-----------|
| Sort the Matrix Diagonally | 1329 | `i - j` | 依鍵分組 → 排序（PQ 或 sort+reverse）→ 回填 |
| Toeplitz Matrix | 766 | `i - j` | 同一條對角線上的格子值必須相同 |
| Diagonal Traverse II | 1424 | `i + j` | 依副對角線分組（鍵 = `i + j`） |
| Diagonal Traverse | 498 | 方向旗標 | 每條對角線輪流往上／往下 |
| Matrix Diagonal Sum | 1572 | `i == j` / `i + j == n-1` | 主 + 副對角線求和 |
| Prime In Diagonal | 2614 | `i == j` / `i + j == n-1` | 在兩條對角線上找最大質數 |

> **關鍵區分**：左上→右下的對角線用 `i - j`；右上→左下（副對角線）用 `i + j`。

#### 座標系統
```python
# Standard matrix indexing: matrix[row][col]
# For matrix[i][j]:
# - i represents row (vertical position)
# - j represents column (horizontal position)

# Direction vectors for 4-directional movement
directions = [(0,1), (1,0), (0,-1), (-1,0)]  # right, down, left, up

# Direction vectors for 8-directional movement  
directions_8 = [(0,1), (1,0), (0,-1), (-1,0), (1,1), (1,-1), (-1,1), (-1,-1)]

# Boundary checking
def is_valid(row, col, rows, cols):
    return 0 <= row < rows and 0 <= col < cols
```


## 依模式分類的題目

### 依模式分類的題目一覽

#### **模式 1：矩陣走訪題**
| 題目 | LC # | 關鍵技巧 | 難度 | 使用模板 |
|---------|------|---------------|------------|---------------|
| Spiral Matrix | 54 | 四個方向的邊界追蹤 | Medium | 走訪模板 |
| Spiral Matrix II | 59 | 依螺旋順序填滿矩陣 | Medium | 走訪模板 |
| Diagonal Traverse | 498 | 交替方向並處理邊界 | Medium | 走訪模板 |
| Walking Robot Simulation | 885 | 方向向量 + 障礙檢查 | Easy | 走訪模板 |
| Spiral Matrix III | 885 | 逐步外擴的螺旋並檢查界線 | Medium | 走訪模板 |
| Matrix Cells in Distance Order | 1030 | 依曼哈頓距離排序 | Easy | 走訪模板 |
| Shift 2D Grid | 1260 | 二維上的環狀陣列位移 | Easy | 走訪模板 |

#### **模式 1b：對角線分組題**（`key = i - j`）
> **核心想法**：`i - j` 相同的格子落在同一條左上→右下的對角線上 → 依鍵分組、處理、回填。

| 題目 | LC # | 關鍵技巧 | 難度 |
|---------|------|---------------|------------|
| Sort the Matrix Diagonally | 1329 | 依 `i-j` 分組 → 最小堆積排序 → 回填 | Medium |
| Toeplitz Matrix | 766 | 依 `i-j` 分組 → 值必須全部相同 | Easy |
| Diagonal Traverse II | 1424 | 依 `i+j`（副對角線）分組 → 反轉順序 | Medium |

#### **模式 2：矩陣變換題**
| 題目 | LC # | 關鍵技巧 | 難度 | 使用模板 |
|---------|------|---------------|------------|---------------|
| Rotate Image | 48 | 轉置 + 反轉每一列 | Medium | 變換模板 |
| Transpose Matrix | 867 | 列與行對調 | Easy | 變換模板 |
| Flip Image | 832 | 水平翻轉 + 位元反轉 | Easy | 變換模板 |
| Flipping an Image | 832 | 反轉每列並做位元運算 | Easy | 變換模板 |
| Rotate Array | 189 | 用反轉法做陣列旋轉 | Medium | 變換模板 |
| Determine Whether Matrix Can Be Obtained by Rotation | 1886 | 多次 90° 旋轉 | Easy | 變換模板 |

#### **模式 3：矩陣搜尋題**
| 題目 | LC # | 關鍵技巧 | 難度 | 使用模板 |
|---------|------|---------------|------------|---------------|
| Search a 2D Matrix | 74 | 攤平成一維後二分搜尋 | Medium | 搜尋模板（二分） |
| Search a 2D Matrix II | 240 | 從右上角開始排除 | Medium | 搜尋模板（排除法） |
| Kth Smallest Element in Sorted Matrix | 378 | 對值域做二分搜尋 | Medium | 搜尋模板（二分） |
| Find K Pairs with Smallest Sums | 373 | 優先佇列搭配矩陣性質 | Medium | 搜尋模板 |
| Shortest Distance from All Buildings | 317 | 從每棟建築各跑一次 BFS | Hard | 搜尋模板 |
| Count Negative Numbers in Sorted Matrix | 1351 | 二分搜尋或排除法 | Easy | 搜尋模板 |
| Find a Peak Element II | 1901 | 二維峰值的二分搜尋 | Medium | 搜尋模板 |
| Median in a Row-Wise Sorted Matrix | - | 對中位數值做二分搜尋 | Medium | 搜尋模板 |

#### **模式 4：矩陣修改題**
| 題目 | LC # | 關鍵技巧 | 難度 | 使用模板 |
|---------|------|---------------|------------|---------------|
| Set Matrix Zeroes | 73 | 借第一列／第一行做原地標記 | Medium | 修改模板 |
| Image Smoother | 661 | 八方向取平均 | Easy | 修改模板 |
| Game of Life | 289 | 原地做狀態轉移 | Medium | 修改模板 |
| Range Sum Query 2D - Mutable | 308 | 線段樹或樹狀陣列 | Hard | 修改模板 |
| Bomb Enemy | 361 | DP 搭配障礙處理 | Medium | 修改模板 |
| Shortest Distance from All Buildings | 317 | BFS 並累加距離 | Hard | 修改模板 |
| Max Area of Island | 695 | DFS 搭配走訪標記 | Medium | 修改模板 |
| Number of Islands | 200 | DFS/BFS 並直接改格子 | Medium | 修改模板 |

#### **模式 5 的模板：矩陣乘法與運算**
| 題目 | LC # | 關鍵技巧 | 難度 | 使用模板 |
|---------|------|---------------|------------|---------------|
| Sparse Matrix Multiplication | 311 | 跳過零元素的最佳化 | Medium | 乘法模板 |
| Design Tic-Tac-Toe | 348 | 追蹤列／行／對角線的和 | Medium | 運算模板 |
| Matrix Diagonal Sum | 1572 | 主 + 副對角線求和 | Easy | 運算模板 |
| Count Negative Numbers in Sorted Matrix | 1351 | 在排序矩陣中高效計數 | Easy | 運算模板 |
| Lucky Numbers in a Matrix | 1380 | 列最小 + 行最大 | Easy | 運算模板 |
| Maximum Side Length of Square | 1292 | 二維前綴和 + 二分搜尋 | Medium | 運算模板 |
| Range Sum Query 2D Immutable | 304 | 二維前綴和 | Medium | 運算模板 |
| Minimum Falling Path Sum | 931 | DP 搭配相鄰格子轉移 | Medium | 運算模板 |

#### **模式 6 的模板：矩陣路徑與動態規劃**
| 題目 | LC # | 關鍵技巧 | 難度 | 使用模板 |
|---------|------|---------------|------------|---------------|
| Unique Paths | 62 | DP 數路徑條數 | Medium | 路徑 DP 模板 |
| Unique Paths II | 63 | 有障礙的 DP | Medium | 路徑 DP 模板 |
| Minimum Path Sum | 64 | DP 做成本最佳化 | Medium | 路徑 DP 模板 |
| Triangle | 120 | 三角形矩陣上的 DP | Medium | 路徑 DP 模板 |
| Minimum Falling Path Sum | 931 | 有相鄰限制的 DP | Medium | 路徑 DP 模板 |
| Cherry Pickup | 741 | 來回路徑的三維 DP | Hard | 路徑 DP 模板 |
| Dungeon Game | 174 | 從終點反推的 DP | Hard | 路徑 DP 模板 |
| Minimum Path Sum | 64 | 基本格子 DP | Medium | 路徑 DP 模板 |
| Maximum Path Sum | 124 | 把樹 DP 搬到格子上 | Hard | 路徑 DP 模板 |
| Path with Maximum Gold | 1219 | DFS 搭配回溯 | Medium | 路徑 DP 模板 |

### 依難度分類的其他矩陣題

#### **Easy（打底）**
| 題目 | LC # | 模式 | 學到什麼 |
|---------|------|---------|--------------|
| Reshape the Matrix | 566 | 變換 | 一維與二維互轉 |
| Toeplitz Matrix | 766 | 模式辨識 | 檢查對角線性質 |
| Available Captures for Rook | 999 | 走訪 | 以方向為主的移動 |
| Find Winner on a Tic Tac Toe Game | 1275 | 運算 | 盤面狀態判定 |
| Cells with Odd Values in Matrix | 1252 | 修改 | 以索引為準的更新 |
| Matrix Block Sum | 1314 | **二維前綴和** | 建前綴、O(1) 區間查詢 |
| Sum of All Odd Length Subarrays | 1588 | 運算 | 子陣列的貢獻度 |

#### **Medium（核心功力）**
| 題目 | LC # | 模式 | 學到什麼 |
|---------|------|---------|--------------|
| Valid Sudoku | 36 | 驗證 | 用集合檢查唯一性 |
| Word Search | 79 | DFS/回溯 | 邊探索邊回溯 |
| Surrounded Regions | 130 | DFS/BFS | 從邊界反推區域 |
| Rotate Array | 189 | 變換 | 多種旋轉技巧 |
| Maximal Square | 221 | DP | 用二維 DP 做形狀最佳化 |
| Longest Increasing Path in Matrix | 329 | DFS + 記憶化 | DAG 最長路徑 |
| Island Perimeter | 463 | 走訪 | 邊界計數 |
| Pacific Atlantic Water Flow | 417 | DFS | 多源可達性 |

#### **Hard（進階技巧）**
| 題目 | LC # | 模式 | 學到什麼 |
|---------|------|---------|--------------|
| Sudoku Solver | 37 | 回溯 | 約束滿足問題 |
| N-Queens | 51 | 回溯 | 複雜的約束檢查 |
| The Maze III | 499 | Dijkstra/BFS | 帶方向的最短路徑 |
| Robot Room Cleaner | 489 | DFS | 探索未知的格子空間 |
| Minimum Number of Taps | 1326 | 貪婪/DP | 區間覆蓋最佳化 |
| Cherry Pickup II | 1463 | 三維 DP | 多個代理人的路徑最佳化 |
| Largest Rectangle in Histogram | 84 | 堆疊 | 以直方圖為基礎的最佳化 |


### 模式選擇策略

```text
Matrix Problem Analysis Flowchart:

1. Is the problem about traversing matrix in a specific order?
   ├── YES → Use Matrix Traversal Template
   │   ├── Spiral order? → Boundary tracking approach
   │   ├── Diagonal order? → Direction alternation approach
   │   └── Custom order? → Direction vectors approach
   └── NO → Continue to 2

2. Does the problem require matrix transformation (rotate, flip, transpose)?
   ├── YES → Use Matrix Transformation Template
   │   ├── Rotate 90°? → Transpose + reverse rows
   │   ├── General rotation? → Mathematical coordinate mapping
   │   └── Transpose? → Swap (i,j) with (j,i)
   └── NO → Continue to 3

3. Is it a search problem in a sorted/partially sorted matrix?
   ├── YES → Use Matrix Search Template
   │   ├── Fully sorted (row-wise + col-wise)? → Binary search as 1D array
   │   ├── Row-wise and column-wise sorted? → Elimination approach
   │   └── Partially sorted? → Modified binary search
   └── NO → Continue to 4

4. Does the problem modify matrix elements based on conditions?
   ├── YES → Use Matrix Modification Template
   │   ├── Set zeros? → Use first row/column as markers
   │   ├── Smooth/average? → 8-directional neighbor processing
   │   └── State transitions? → Two-pass or auxiliary space
   └── NO → Continue to 5

5. Is it a mathematical operation between matrices?
   ├── YES → Use Matrix Multiplication Template
   │   ├── Standard multiplication? → Triple nested loop
   │   ├── Sparse matrices? → Skip-zero optimization
   │   └── Special operations? → Customize based on operation
   └── NO → Continue to 6

6. Is it about finding paths or optimizing values through the matrix?
   ├── YES → Use Matrix Path DP Template
   │   ├── Count paths? → DP with path counting
   │   ├── Min/Max path cost? → DP with optimization
   │   └── Complex constraints? → DFS + memoization
   └── NO → Use Universal Matrix Template
```

### 實作決策樹

#### **步驟 1：把問題歸類**
1. **把題目讀仔細**，抓出關鍵需求
2. **確認輸入限制**（矩陣大小、數值範圍）
3. **確認要輸出什麼**（改過的矩陣、單一數值、清單）
4. **找關鍵字**：走訪、旋轉、搜尋、修改、相乘、路徑

#### **步驟 2：辨認模式**
1. **走訪的訊號**：「spiral」「diagonal」「clockwise」「order」
2. **變換的訊號**：「rotate」「transpose」「flip」「mirror」
3. **搜尋的訊號**：「find」「search」「locate」「sorted matrix」
4. **修改的訊號**：「set」「update」「smooth」「change」
5. **數學運算的訊號**：「multiply」「sum」「product」「diagonal」
6. **路徑／DP 的訊號**：「path」「minimum」「maximum」「count」「ways」

#### **步驟 3：挑模板**
1. **挑最貼合這個模式的模板**
2. **把模板改寫**成符合本題需求的樣子
3. **想清楚邊界情況**：空矩陣、只有一個元素、長方形 vs 正方形
4. **依限制做最佳化**：原地還是額外空間、時間複雜度要求

## 總結與速查

### 複雜度速查

| 操作 | 時間複雜度 | 空間複雜度 | 備註 |
|-----------|----------------|------------------|--------|
| **矩陣存取** | O(1) | O(1) | 直接索引 |
| **完整走訪** | O(m*n) | O(1) | 每個元素都拜訪 |
| **螺旋走訪** | O(m*n) | O(1) | 邊界追蹤 |
| **二分搜尋（已排序）** | O(log(m*n)) | O(1) | 當成一維陣列處理 |
| **排除法搜尋** | O(m+n) | O(1) | 從角落出發 |
| **矩陣旋轉** | O(m*n) | O(1) | 轉置 + 反轉 |
| **矩陣乘法** | O(m*n*p) | O(m*p) | 標準演算法 |
| **稀疏矩陣乘法** | O(m*n*k) | O(m*p) | k = 每列平均非零元素數 |
| **建二維前綴和** | O(m*n) | O(m*n) | 一次性前處理 |
| **二維前綴和查詢** | O(1) | O(1) | 前處理完之後 |
| **DP 路徑題** | O(m*n) | O(m*n) 或 O(n) | 空間可以再壓 |

### 模板速查

| 模板 | 模式 | 關鍵程式碼結構 |
|----------|---------|-------------------|
| **通用** | 一般處理 | `for i in range(rows): for j in range(cols):` |
| **走訪** | 螺旋／對角 | 邊界追蹤搭配方向向量 |
| **變換** | 旋轉／轉置 | 座標的數學對應 |
| **搜尋** | 找元素 | 二分搜尋或排除法 |
| **修改** | 更新元素 | 兩趟掃描或借助額外空間 |
| **乘法** | 數學運算 | 三層巢狀迴圈加最佳化 |
| **二維前綴和** | 區間和查詢 | 建 (m+1)×(n+1) 前綴，查詢 O(1) |
| **路徑 DP** | 最佳化 | 相鄰格子之間的 DP 狀態轉移 |

### 常見模式與小技巧

#### **邊界追蹤（螺旋）**
```python
top, bottom = 0, rows - 1
left, right = 0, cols - 1
while top <= bottom and left <= right:
    ...        # process boundaries, then move them inward
```

#### **方向向量**
```python
# 4-directional movement
directions = [(0,1), (1,0), (0,-1), (-1,0)]
# 8-directional movement  
directions = [(di,dj) for di in [-1,0,1] for dj in [-1,0,1]]
```

#### **原地修改**
```python
# Use first row/column as markers
first_row_zero = any(matrix[0][j] == 0 for j in range(cols))
first_col_zero = any(matrix[i][0] == 0 for i in range(rows))
```

#### **座標變換**
```python
# 90° clockwise rotation: (i,j) → (j, n-1-i)
# Transpose: (i,j) → (j,i)
# Flip horizontal: (i,j) → (i, n-1-j)
```

#### **矩陣座標轉一維索引**
```python
# Matrix[row][col] → index = row * cols + col
# Index → row = index // cols, col = index % cols
```

### 解題步驟

1. **先讀懂題目**
   - 確認輸入格式（矩陣維度、限制）
   - 確認輸出要求（改過的矩陣、數值、座標）
   - 留意特殊性質（已排序、稀疏、正方形 vs 長方形）

2. **選對模式**
   - 用決策流程圖找出模式
   - 挑最貼合的模板
   - 考慮時間／空間複雜度的要求

3. **處理邊界情況**
   - 空矩陣：`if not matrix or not matrix[0]:`
   - 單一元素：1x1 矩陣要特別處理
   - 長方形矩陣：列數與行數不同

4. **依限制最佳化**
   - 依需求決定原地做還是開額外空間
   - 依矩陣大小挑合適的演算法
   - 適用時考慮稀疏矩陣的最佳化

### 常見錯誤與提醒

**🚫 常見錯誤：**
- **索引搞混**：把 `matrix[row][col]` 跟 `matrix[col][row]` 弄反
- **邊界錯誤**：range 算範圍時差一
- **方向寫錯**：方向向量算錯
- **原地修改**：邊讀邊改矩陣（該用標記）
- **忽略邊界情況**：沒處理空矩陣或只有一個元素的矩陣
- **座標變換出錯**：旋轉／轉置公式寫錯

**✅ 最佳實務：**
- **一定要檢查界線**：用 `0 <= i < rows and 0 <= j < cols`
- **變數取有意義的名字**：用 `rows, cols` 而不是 `m, n`
- **先處理邊界情況**：先檢查空的／不合法的輸入
- **畫例子**：拿小矩陣把變換畫出來
- **善用方向向量**：比寫死的移動好讀太多
- **考慮空間最佳化**：能原地改就原地改
- **各種形狀都測**：正方形、長方形、只有一列／一行

### 面試提示

1. **先問清楚矩陣的性質**
   - 問維度和限制
   - 確認能不能原地修改
   - 確認矩陣是否保證非空

2. **從暴力解開始**
   - 先把最直觀的 O(m*n) 解寫出來
   - 再依題目限制最佳化
   - 動手寫之前先把想法講清楚

3. **拿例子走一遍**
   - 用小矩陣（2x2、3x3）驗證邏輯
   - 一步一步把邊界條件走過
   - 拿邊界情況檢驗你的演算法

4. **有系統地最佳化**
   - 找出初版解法的瓶頸
   - 想想有沒有可利用的數學性質
   - 跟面試官討論時間空間的取捨

5. **常見的面試模式**
   - **矩陣走訪**：重點在邊界管理
   - **矩陣變換**：要熟旋轉／轉置的套路
   - **矩陣搜尋**：把二分搜尋跟排除法練熟
   - **矩陣 DP**：搞懂狀態轉移和空間最佳化

### 相關主題
- **陣列**：矩陣就是陣列概念的二維延伸
- **動態規劃**：很多矩陣題都吃 DP 模式
- **圖論演算法**：矩陣可以表示圖（鄰接矩陣）
- **二分搜尋**：排序矩陣搜尋題的必備工具  
- **回溯**：矩陣探索題會用到（N-Queens、數獨）
- **字串處理**：矩陣題常牽涉樣式比對
- **貪婪演算法**：有些矩陣最佳化題吃貪婪

### 延伸資源
- **視覺化工具**：用 Draw.io 把矩陣變換畫出來
- **練習平台**：LeetCode 上依難度分類的矩陣題
- **數學背景**：進階矩陣運算的線性代數
- **演算法分析**：矩陣演算法複雜度的 Big-O 表示法


## 詳解範例

十七道題放在 **[matrix_examples.md](./matrix_examples.md)**，依各題吃的幾何性質或技巧分組：

| 分組 | 題目 |
|---|---|
| [走訪與對角線](./matrix_examples.md#traversal--diagonals) | LC 54, 498, 1329 |
| [變換與原地修改](./matrix_examples.md#transformation--in-place-modification) | LC 48, 289, 73, 661 |
| [搜尋](./matrix_examples.md#search) | LC 74, 240, 378 |
| [格子上的 DFS / BFS](./matrix_examples.md#dfs--bfs-on-a-grid) | LC 200, 329 |
| [二維動態規劃](./matrix_examples.md#2d-dynamic-programming) | LC 64, 221, 85 |
| [前綴和與列對壓縮](./matrix_examples.md#prefix-sums--row-pair-compression) | LC 1314, 1074 |

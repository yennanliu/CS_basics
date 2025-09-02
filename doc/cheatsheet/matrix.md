# Matrix Data Structure

## Overview

**Matrix** is a 2-dimensional array data structure that stores elements in rows and columns. It's fundamental for solving problems involving grids, images, game boards, and mathematical computations.

### Key Properties
- **Time Complexity**: 
  - Access: O(1)
  - Traversal: O(m*n) where m=rows, n=columns
  - Search: O(m*n) for unsorted, O(log(m*n)) for sorted matrices
- **Space Complexity**: O(m*n) for storage
- **Core Idea**: Elements accessed via [row][column] indexing
- **When to Use**: Grid-based problems, 2D transformations, path finding, dynamic programming on grids

### Problem Categories

#### **Pattern 1: Matrix Traversal**
- **Description**: Navigate through matrix elements using specific patterns (spiral, diagonal, zigzag)
- **Examples**: LC 54, 59, 498, 885
- **Key Pattern**: Boundary-based movement with direction changes

#### **Pattern 2: Matrix Transformation** 
- **Description**: Rotate, transpose, or flip matrices in-place or create new ones
- **Examples**: LC 48, 867, 189, 1886
- **Key Pattern**: Mathematical coordinate mapping

#### **Pattern 3: Matrix Search**
- **Description**: Find elements in sorted or partially sorted 2D matrices
- **Examples**: LC 74, 240, 378, 668
- **Key Pattern**: Binary search variants or elimination-based search

#### **Pattern 4: Matrix Modification**
- **Description**: Update matrix elements based on conditions (set zeros, smooth values)
- **Examples**: LC 73, 661, 289, 1314
- **Key Pattern**: Two-pass or auxiliary space approaches

#### **Pattern 5: Matrix Multiplication & Operations**
- **Description**: Mathematical operations between matrices or matrix computations
- **Examples**: LC 311, 348, 1572, 1351
- **Key Pattern**: Triple nested loops for multiplication, optimization for sparse matrices

#### **Pattern 6: Matrix Path & Dynamic Programming**
- **Description**: Find paths, count paths, or optimize values through matrix traversal
- **Examples**: LC 62, 63, 64, 120, 931
- **Key Pattern**: DP state transitions based on adjacent cells

## Templates & Algorithms

### Template Comparison Table

| Template Type | Use Case | Key Structure | When to Use |
|---------------|----------|---------------|-------------|
| **Matrix Traversal** | Spiral, diagonal movement | Boundary tracking + direction vectors | Ordered element processing |
| **Matrix Transformation** | Rotate, transpose, flip | Mathematical coordinate mapping | In-place modifications |
| **Matrix Search** | Find target in sorted matrix | Binary search or elimination | Sorted/partially sorted matrices |
| **Matrix Modification** | Set zeros, smooth values | Two-pass or auxiliary tracking | Conditional element updates |
| **Matrix Multiplication** | Dot product operations | Triple nested loops | Mathematical computations |
| **Matrix Path DP** | Path counting, min/max path | DP state transitions | Optimization problems on grids |

### Universal Matrix Template

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

### Pattern-Specific Templates

#### Template 1: Matrix Traversal (Spiral/Diagonal)
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

#### Template 2: Matrix Transformation (Rotate/Transpose)
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

#### Template 3: Matrix Search
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

#### Template 4: Matrix Modification
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

#### Template 5: Matrix Multiplication
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

#### Template 6: Matrix Path DP
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

### Essential Matrix Properties

#### Diagonal Properties
- **Main Diagonal**: For position (i,j), i - j = constant
  - Elements where row - col = 0: (0,0), (1,1), (2,2)...
- **Anti-Diagonal**: For position (i,j), i + j = constant  
  - Elements where row + col = n-1: (0,n-1), (1,n-2)...

#### Coordinate System
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


## Problems by Pattern

### Pattern-Based Problem Classification

#### **Pattern 1: Matrix Traversal Problems**
| Problem | LC # | Key Technique | Difficulty | Template Used |
|---------|------|---------------|------------|---------------|
| Spiral Matrix | 54 | Boundary tracking with 4 directions | Medium | Traversal Template |
| Spiral Matrix II | 59 | Fill matrix in spiral order | Medium | Traversal Template |
| Diagonal Traverse | 498 | Direction alternation with boundary handling | Medium | Traversal Template |
| Walking Robot Simulation | 885 | Direction vectors + obstacle checking | Easy | Traversal Template |
| Spiral Matrix III | 885 | Expanding spiral with bounds checking | Medium | Traversal Template |
| Matrix Cells in Distance Order | 1030 | Manhattan distance sorting | Easy | Traversal Template |
| Shift 2D Grid | 1260 | Circular array shifting in 2D | Easy | Traversal Template |

#### **Pattern 2: Matrix Transformation Problems**
| Problem | LC # | Key Technique | Difficulty | Template Used |
|---------|------|---------------|------------|---------------|
| Rotate Image | 48 | Transpose + reverse rows | Medium | Transformation Template |
| Transpose Matrix | 867 | Swap rows and columns | Easy | Transformation Template |
| Flip Image | 832 | Horizontal flip + bit inversion | Easy | Transformation Template |
| Flipping an Image | 832 | Row reversal with bit operations | Easy | Transformation Template |
| Rotate Array | 189 | Array rotation using reversal | Medium | Transformation Template |
| Determine Whether Matrix Can Be Obtained by Rotation | 1886 | Multiple 90° rotations | Easy | Transformation Template |

#### **Pattern 3: Matrix Search Problems**
| Problem | LC # | Key Technique | Difficulty | Template Used |
|---------|------|---------------|------------|---------------|
| Search a 2D Matrix | 74 | Binary search on flattened matrix | Medium | Search Template (Binary) |
| Search a 2D Matrix II | 240 | Elimination from top-right corner | Medium | Search Template (Elimination) |
| Kth Smallest Element in Sorted Matrix | 378 | Binary search on value range | Medium | Search Template (Binary) |
| Find K Pairs with Smallest Sums | 373 | Priority queue with matrix properties | Medium | Search Template |
| Shortest Distance from All Buildings | 317 | BFS from each building | Hard | Search Template |
| Count Negative Numbers in Sorted Matrix | 1351 | Binary search or elimination | Easy | Search Template |
| Find a Peak Element II | 1901 | Binary search on 2D peak | Medium | Search Template |
| Median in a Row-Wise Sorted Matrix | - | Binary search on median value | Medium | Search Template |

#### **Pattern 4: Matrix Modification Problems**
| Problem | LC # | Key Technique | Difficulty | Template Used |
|---------|------|---------------|------------|---------------|
| Set Matrix Zeroes | 73 | In-place marking using first row/column | Medium | Modification Template |
| Image Smoother | 661 | 8-directional averaging | Easy | Modification Template |
| Game of Life | 289 | In-place state transitions | Medium | Modification Template |
| Range Sum Query 2D - Mutable | 308 | Segment tree or binary indexed tree | Hard | Modification Template |
| Bomb Enemy | 361 | DP with obstacle handling | Medium | Modification Template |
| Shortest Distance from All Buildings | 317 | BFS with distance accumulation | Hard | Modification Template |
| Max Area of Island | 695 | DFS with visited marking | Medium | Modification Template |
| Number of Islands | 200 | DFS/BFS with grid modification | Medium | Modification Template |

#### **Pattern 5: Matrix Multiplication & Operations**
| Problem | LC # | Key Technique | Difficulty | Template Used |
|---------|------|---------------|------------|---------------|
| Sparse Matrix Multiplication | 311 | Skip zeros optimization | Medium | Multiplication Template |
| Design Tic-Tac-Toe | 348 | Row/column/diagonal sum tracking | Medium | Operations Template |
| Matrix Diagonal Sum | 1572 | Main + anti-diagonal sum | Easy | Operations Template |
| Count Negative Numbers in Sorted Matrix | 1351 | Efficient counting in sorted matrix | Easy | Operations Template |
| Lucky Numbers in a Matrix | 1380 | Row min + column max | Easy | Operations Template |
| Maximum Side Length of Square | 1292 | 2D prefix sum + binary search | Medium | Operations Template |
| Range Sum Query 2D Immutable | 304 | 2D prefix sum | Medium | Operations Template |
| Minimum Falling Path Sum | 931 | DP with adjacent cell transitions | Medium | Operations Template |

#### **Pattern 6: Matrix Path & Dynamic Programming**
| Problem | LC # | Key Technique | Difficulty | Template Used |
|---------|------|---------------|------------|---------------|
| Unique Paths | 62 | DP counting paths | Medium | Path DP Template |
| Unique Paths II | 63 | DP with obstacles | Medium | Path DP Template |
| Minimum Path Sum | 64 | DP with cost optimization | Medium | Path DP Template |
| Triangle | 120 | DP on triangular matrix | Medium | Path DP Template |
| Minimum Falling Path Sum | 931 | DP with adjacent constraints | Medium | Path DP Template |
| Cherry Pickup | 741 | 3D DP for round trip | Hard | Path DP Template |
| Dungeon Game | 174 | Reverse DP from destination | Hard | Path DP Template |
| Minimum Path Sum | 64 | Basic grid DP | Medium | Path DP Template |
| Maximum Path Sum | 124 | Tree DP adapted to grid | Hard | Path DP Template |
| Path with Maximum Gold | 1219 | DFS with backtracking | Medium | Path DP Template |

### Additional Matrix Problems by Difficulty

#### **Easy Problems (Foundation)**
| Problem | LC # | Pattern | Key Learning |
|---------|------|---------|--------------|
| Reshape the Matrix | 566 | Transformation | 1D to 2D conversion |
| Toeplitz Matrix | 766 | Pattern Recognition | Diagonal property checking |
| Available Captures for Rook | 999 | Traversal | Direction-based movement |
| Find Winner on a Tic Tac Toe Game | 1275 | Operations | Game state evaluation |
| Cells with Odd Values in Matrix | 1252 | Modification | Index-based updates |
| Matrix Block Sum | 1314 | Operations | 2D range sum |
| Sum of All Odd Length Subarrays | 1588 | Operations | Subarray contribution |

#### **Medium Problems (Core Skills)**
| Problem | LC # | Pattern | Key Learning |
|---------|------|---------|--------------|
| Valid Sudoku | 36 | Validation | Set operations for uniqueness |
| Word Search | 79 | DFS/Backtracking | Path exploration with backtracking |
| Surrounded Regions | 130 | DFS/BFS | Boundary-based region identification |
| Rotate Array | 189 | Transformation | Multiple rotation techniques |
| Maximal Square | 221 | DP | 2D DP for shape optimization |
| Longest Increasing Path in Matrix | 329 | DFS + Memoization | DAG longest path |
| Island Perimeter | 463 | Traversal | Boundary counting |
| Pacific Atlantic Water Flow | 417 | DFS | Multi-source reachability |

#### **Hard Problems (Advanced Techniques)**
| Problem | LC # | Pattern | Key Learning |
|---------|------|---------|--------------|
| Sudoku Solver | 37 | Backtracking | Constraint satisfaction |
| N-Queens | 51 | Backtracking | Complex constraint checking |
| The Maze III | 499 | Dijkstra/BFS | Shortest path with direction |
| Robot Room Cleaner | 489 | DFS | Unknown grid exploration |
| Minimum Number of Taps | 1326 | Greedy/DP | Interval covering optimization |
| Cherry Pickup II | 1463 | 3D DP | Multi-agent path optimization |
| Largest Rectangle in Histogram | 84 | Stack | Histogram-based optimization |


### Pattern Selection Strategy

```
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

### Implementation Decision Tree

#### **Step 1: Problem Classification**
1. **Read the problem carefully** and identify key requirements
2. **Determine the input constraints** (matrix size, value ranges)
3. **Identify the expected output** (modified matrix, single value, list)
4. **Look for keywords**: traverse, rotate, search, modify, multiply, path

#### **Step 2: Pattern Recognition**
1. **Traversal indicators**: "spiral", "diagonal", "clockwise", "order"
2. **Transformation indicators**: "rotate", "transpose", "flip", "mirror"
3. **Search indicators**: "find", "search", "locate", "sorted matrix"
4. **Modification indicators**: "set", "update", "smooth", "change"
5. **Math operation indicators**: "multiply", "sum", "product", "diagonal"
6. **Path/DP indicators**: "path", "minimum", "maximum", "count", "ways"

#### **Step 3: Template Selection**
1. **Choose the most specific template** that matches the pattern
2. **Adapt the template** to problem-specific requirements
3. **Consider edge cases**: empty matrix, single element, rectangular vs square
4. **Optimize for constraints**: in-place vs extra space, time complexity

## Code Examples & Implementations

### Advanced Example 1: Set Matrix Zeroes (LC 73)
```python
def setZeroes(matrix):
    """
    Set entire row and column to zero if element is zero
    Time: O(m*n), Space: O(1)
    """
    if not matrix or not matrix[0]:
        return
    
    rows, cols = len(matrix), len(matrix[0])
    
    # Check if first row and column need to be zero
    first_row_zero = any(matrix[0][j] == 0 for j in range(cols))
    first_col_zero = any(matrix[i][0] == 0 for i in range(rows))
    
    # Use first row and column as markers
    for i in range(1, rows):
        for j in range(1, cols):
            if matrix[i][j] == 0:
                matrix[0][j] = 0
                matrix[i][0] = 0
    
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

### Advanced Example 2: Search 2D Matrix II (LC 240)
```python
def searchMatrix(matrix, target):
    """
    Search in row-wise and column-wise sorted matrix
    Time: O(m+n), Space: O(1)
    """
    if not matrix or not matrix[0]:
        return False
    
    rows, cols = len(matrix), len(matrix[0])
    row, col = 0, cols - 1  # Start from top-right
    
    while row < rows and col >= 0:
        current = matrix[row][col]
        if current == target:
            return True
        elif current > target:
            col -= 1  # Eliminate current column
        else:
            row += 1  # Eliminate current row
    
    return False
```

### Advanced Example 3: Image Smoother (LC 661)
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

## Summary & Quick Reference

### Complexity Quick Reference

| Operation | Time Complexity | Space Complexity | Notes |
|-----------|----------------|------------------|--------|
| **Matrix Access** | O(1) | O(1) | Direct indexing |
| **Full Traversal** | O(m*n) | O(1) | Visit every element |
| **Spiral Traversal** | O(m*n) | O(1) | Boundary tracking |
| **Binary Search (Sorted)** | O(log(m*n)) | O(1) | Treat as 1D array |
| **Elimination Search** | O(m+n) | O(1) | Start from corner |
| **Matrix Rotation** | O(m*n) | O(1) | Transpose + reverse |
| **Matrix Multiplication** | O(m*n*p) | O(m*p) | Standard algorithm |
| **Sparse Multiplication** | O(m*n*k) | O(m*p) | k = average non-zeros per row |
| **DP Path Problems** | O(m*n) | O(m*n) or O(n) | Can optimize space |

### Template Quick Reference

| Template | Pattern | Key Code Structure |
|----------|---------|-------------------|
| **Universal** | General processing | `for i in range(rows): for j in range(cols):` |
| **Traversal** | Spiral/Diagonal | Boundary tracking with direction vectors |
| **Transformation** | Rotate/Transpose | Mathematical coordinate mapping |
| **Search** | Find elements | Binary search or elimination approach |
| **Modification** | Update elements | Two-pass or auxiliary space techniques |
| **Multiplication** | Math operations | Triple nested loop with optimizations |
| **Path DP** | Optimization | DP state transitions between adjacent cells |

### Common Patterns & Tricks

#### **Boundary Tracking (Spiral)**
```python
top, bottom = 0, rows - 1
left, right = 0, cols - 1
while top <= bottom and left <= right:
    # Process boundaries and update them
```

#### **Direction Vectors**
```python
# 4-directional movement
directions = [(0,1), (1,0), (0,-1), (-1,0)]
# 8-directional movement  
directions = [(di,dj) for di in [-1,0,1] for dj in [-1,0,1]]
```

#### **In-Place Modifications**
```python
# Use first row/column as markers
first_row_zero = any(matrix[0][j] == 0 for j in range(cols))
first_col_zero = any(matrix[i][0] == 0 for i in range(rows))
```

#### **Coordinate Transformation**
```python
# 90° clockwise rotation: (i,j) → (j, n-1-i)
# Transpose: (i,j) → (j,i)
# Flip horizontal: (i,j) → (i, n-1-j)
```

#### **Matrix to 1D Index Conversion**
```python
# Matrix[row][col] → index = row * cols + col
# Index → row = index // cols, col = index % cols
```

### Problem-Solving Steps

1. **Understand the Problem**
   - Identify input format (matrix dimensions, constraints)
   - Determine output requirements (modified matrix, values, coordinates)
   - Look for special properties (sorted, sparse, square vs rectangular)

2. **Choose the Right Pattern**
   - Use the decision flowchart to identify the pattern
   - Select the most specific template that matches
   - Consider time/space complexity requirements

3. **Handle Edge Cases**
   - Empty matrix: `if not matrix or not matrix[0]:`
   - Single element: special handling for 1x1 matrices
   - Rectangular matrices: different row and column counts

4. **Optimize for Constraints**
   - In-place vs extra space based on requirements
   - Choose appropriate algorithm based on matrix size
   - Consider sparse matrix optimizations when applicable

### Common Mistakes & Tips

**🚫 Common Mistakes:**
- **Index confusion**: Mixing up `matrix[row][col]` vs `matrix[col][row]`
- **Boundary errors**: Off-by-one errors in range calculations
- **Direction mistakes**: Incorrect direction vector calculations
- **In-place modification**: Modifying matrix while reading (use markers)
- **Edge case neglect**: Not handling empty or single-element matrices
- **Coordinate transformation errors**: Incorrect rotation/transpose formulas

**✅ Best Practices:**
- **Always check bounds**: Use `0 <= i < rows and 0 <= j < cols`
- **Use meaningful names**: `rows, cols` instead of `m, n`
- **Handle edge cases first**: Check for empty/invalid input
- **Draw examples**: Visualize transformations on small matrices
- **Use direction vectors**: More readable than hardcoded movements
- **Consider space optimization**: In-place modifications when possible
- **Test with different shapes**: Square, rectangular, single row/column

### Interview Tips

1. **Clarify Matrix Properties**
   - Ask about matrix dimensions and constraints
   - Confirm if modification in-place is allowed
   - Check if matrix is guaranteed to be non-empty

2. **Start with Brute Force**
   - Implement the straightforward O(m*n) solution first
   - Then optimize based on specific problem constraints
   - Explain your approach clearly before coding

3. **Trace Through Examples**
   - Use small matrices (2x2, 3x3) to verify logic
   - Walk through boundary conditions step-by-step
   - Check your algorithm with edge cases

4. **Optimize Systematically**
   - Identify bottlenecks in your initial solution
   - Consider mathematical properties for optimizations
   - Discuss space-time tradeoffs with the interviewer

5. **Common Interview Patterns**
   - **Matrix traversal**: Focus on boundary management
   - **Matrix transformation**: Know rotation/transpose patterns
   - **Matrix search**: Master binary search and elimination approaches
   - **Matrix DP**: Understand state transitions and space optimization

### Related Topics
- **Arrays**: Matrix is a 2D extension of array concepts
- **Dynamic Programming**: Many matrix problems use DP patterns
- **Graph Algorithms**: Matrix can represent graphs (adjacency matrix)
- **Binary Search**: Essential for sorted matrix search problems  
- **Backtracking**: Used in matrix exploration problems (N-Queens, Sudoku)
- **String Processing**: Matrix problems often involve pattern matching
- **Greedy Algorithms**: Some matrix optimization problems use greedy approach

### Additional Resources
- **Visualization Tools**: Draw.io for matrix transformation visualization
- **Practice Platforms**: LeetCode matrix problems by difficulty
- **Mathematical Background**: Linear algebra for advanced matrix operations
- **Algorithm Analysis**: Big-O notation for matrix algorithm complexity
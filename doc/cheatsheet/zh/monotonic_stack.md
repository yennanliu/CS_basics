# 單調堆疊資料結構

> **範圍** — next greater／previous smaller／span／直方圖這類題目 — 堆疊本身保持有序，所以每個元素只被推入與彈出一次。
> **另見**：[stack.md](./stack.md) — 單純的 LIFO 題目；[monotonic_queue.md](./monotonic_queue.md) — 滑動視窗版的對應物；[heap.md](./heap.md) — 當你要的是全域極值而不是鄰近極值時。

## LeetCode 題目清單

- [Monotonic Stack](https://leetcode.com/problem-list/monotonic-stack/)
- [Stack](https://leetcode.com/problem-list/stack/)

## 總覽
**單調堆疊**是一種特化的堆疊，內部元素永遠維持單調（嚴格遞增或嚴格遞減）的順序。它能有效率地解決 next greater／smaller 元素、直方圖面積，以及序列最佳化這類問題。

### 關鍵性質
- **時間複雜度**：大多數操作是 O(n)（每個元素只推入／彈出一次）
- **空間複雜度**：O(n)，用來存堆疊
- **核心想法**：依序處理元素的同時，維持堆疊的單調性
- **什麼時候用**：找 next/previous greater/smaller 元素、直方圖題、序列最佳化

### 參考資料
- [LeetCode Monotonic Stack Pattern](https://leetcode.com/tag/monotonic-stack/)
- [Stack Data Structure Fundamentals](https://en.wikipedia.org/wiki/Stack_(abstract_data_type))

## 題型分類

### **模式 1：Next/Previous Greater Element** — LC 739
- **描述**：找出比目前元素大的下一個或前一個元素
- **範例**：LC 496（Next Greater Element I）、LC 503（Next Greater Element II）、LC 739（Daily Temperatures）
- **模式**：用遞減單調堆疊，遇到更大的元素就彈出

### **模式 2：Next/Previous Smaller Element** — LC 84
- **描述**：找出比目前元素小的下一個或前一個元素
- **範例**：LC 84（Largest Rectangle）、LC 42（Trapping Rain Water）、LC 907（Sum of Subarray Minimums）
- **模式**：用遞增單調堆疊，遇到更小的元素就彈出

### **模式 3：直方圖與面積問題** — LC 84
- **描述**：用高度資訊算面積、矩形或體積
- **範例**：LC 84（Largest Rectangle in Histogram）、LC 42（Trapping Rain Water）、LC 85（Maximal Rectangle）
- **模式**：用單調堆疊找出邊界，再算邊界之間的面積

### **模式 4：序列順序與驗證** — LC 456
- **描述**：驗證序列、找出特定樣式，或維持順序限制
- **範例**：LC 456（132 Pattern）、LC 901（Online Stock Span）、LC 1856（Maximum Subarray Min-Product）
- **模式**：用堆疊維持序列性質並驗證樣式

### **模式 5：最佳化與最大／最小** — LC 1793
- **描述**：在最大或最小限制下找最佳解
- **範例**：LC 1944（Number of Visible People）、LC 2104（Sum of Subarray Ranges）、LC 1793（Maximum Score）
- **模式**：用單調性質維持最佳候選

### **模式 6：環狀陣列** — LC 503
- **描述**：處理環狀或循環的陣列問題
- **範例**：LC 503（Next Greater Element II）、LC 853（Car Fleet II）
- **模式**：把陣列走兩遍，或用模運算搭配單調堆疊

## 模板與演算法

### 模板比較表
| 模板類型 | 使用情境 | 堆疊順序 | 什麼時候用 |
|---------------|----------|-------------|-------------|
| **遞減堆疊** | Next/Previous Greater | 遞減 | 找比目前元素大的元素 |
| **遞增堆疊** | Next/Previous Smaller | 遞增 | 找比目前元素小的元素 |
| **直方圖面積** | 矩形／面積問題 | 遞增 | 用高度算面積 |
| **環狀陣列** | 循環問題 | 視情況 | 處理環狀序列 |
| **樣式驗證** | 序列驗證 | 視情況 | 驗證特定樣式 |
| **最佳化堆疊** | 最大／最小問題 | 視情況 | 維持最佳候選 |

### 通用模板

```python
def monotonic_stack_template(arr):
    """
    Universal template for monotonic stack problems
    Modify the condition and processing logic based on problem requirements
    """
    stack = []  # Store indices or values
    result = []
    
    for i, val in enumerate(arr):
        # Pop elements that violate monotonic property
        while stack and should_pop(stack, val, i):
            # Process the popped element
            popped = stack.pop()
            process_popped_element(popped, i, result)
        
        # Add current element to stack
        stack.append(i)  # or val depending on problem
    
    # Process remaining elements in stack
    while stack:
        popped = stack.pop()
        process_remaining_element(popped, result)
    
    return result

def should_pop(stack, current_val, current_idx):
    """Define when to pop based on problem requirements"""
    # For next greater: return arr[stack[-1]] <= current_val
    # For next smaller: return arr[stack[-1]] >= current_val
    pass

def process_popped_element(popped_idx, current_idx, result):
    """Process element when it's popped (found its next greater/smaller)"""
    pass

def process_remaining_element(popped_idx, result):
    """Process elements remaining in stack at the end"""
    pass
```

```java
// Java Universal Template
public int[] monotonicStackTemplate(int[] arr) {
    Stack<Integer> stack = new Stack<>();
    int[] result = new int[arr.length];
    
    for (int i = 0; i < arr.length; i++) {
        // Pop elements that violate monotonic property
        while (!stack.isEmpty() && shouldPop(stack, arr, i)) {
            int poppedIdx = stack.pop();
            processElement(poppedIdx, i, result, arr);
        }
        
        // Add current element to stack
        stack.push(i);
    }
    
    // Process remaining elements
    while (!stack.isEmpty()) {
        int poppedIdx = stack.pop();
        processRemainingElement(poppedIdx, result);
    }
    
    return result;
}

private boolean shouldPop(Stack<Integer> stack, int[] arr, int currentIdx) {
    // Define condition based on problem requirements
    return arr[stack.peek()] <= arr[currentIdx]; // For next greater
}
```

### 模板 1：Next Greater Element（遞減堆疊） — LC 496

```python
def next_greater_element(nums):
    """
    Find next greater element for each element
    LC 496, LC 503, LC 739
    """
    n = len(nums)
    result = [-1] * n
    stack = []  # Store indices
    
    for i in range(n):
        # Pop smaller or equal elements
        while stack and nums[stack[-1]] < nums[i]:
            idx = stack.pop()
            result[idx] = nums[i]  # Found next greater
        
        stack.append(i)
    
    return result
```

```java
// Java Template 1
public int[] nextGreaterElement(int[] nums) {
    int n = nums.length;
    int[] result = new int[n];
    Arrays.fill(result, -1);
    Stack<Integer> stack = new Stack<>();
    
    for (int i = 0; i < n; i++) {
        while (!stack.isEmpty() && nums[stack.peek()] < nums[i]) {
            result[stack.pop()] = nums[i];
        }
        stack.push(i);
    }
    
    return result;
}
```

### 模板 2：Next Smaller Element（遞增堆疊） — LC 84

```python
def next_smaller_element(nums):
    """
    Find next smaller element for each element
    Used in LC 84, LC 42
    """
    n = len(nums)
    result = [-1] * n
    stack = []  # Store indices
    
    for i in range(n):
        # Pop greater or equal elements
        while stack and nums[stack[-1]] > nums[i]:
            idx = stack.pop()
            result[idx] = nums[i]  # Found next smaller
        
        stack.append(i)
    
    return result
```

### 模板 3：Largest Rectangle in Histogram — LC 84

```python
def largest_rectangle_area(heights):
    """
    Find largest rectangle area in histogram
    LC 84, LC 85
    """
    stack = []  # Store indices
    max_area = 0
    heights.append(0)  # Add sentinel
    
    for i, h in enumerate(heights):
        # Pop taller bars and calculate area
        while stack and heights[stack[-1]] > h:
            height = heights[stack.pop()]
            width = i if not stack else i - stack[-1] - 1
            max_area = max(max_area, height * width)
        
        stack.append(i)
    
    return max_area
```

```java
// Java Template 3
public int largestRectangleArea(int[] heights) {
    Stack<Integer> stack = new Stack<>();
    int maxArea = 0;
    int n = heights.length;
    
    for (int i = 0; i <= n; i++) {
        int h = (i == n) ? 0 : heights[i];
        
        while (!stack.isEmpty() && heights[stack.peek()] > h) {
            int height = heights[stack.pop()];
            int width = stack.isEmpty() ? i : i - stack.peek() - 1;
            maxArea = Math.max(maxArea, height * width);
        }
        
        stack.push(i);
    }
    
    return maxArea;
}
```

### 模板 4：環狀陣列處理 — LC 503

```python
def next_greater_circular(nums):
    """
    Find next greater element in circular array
    LC 503
    """
    n = len(nums)
    result = [-1] * n
    stack = []
    
    # Process array twice to handle circular nature
    for i in range(2 * n):
        # Pop smaller elements
        while stack and nums[stack[-1]] < nums[i % n]:
            idx = stack.pop()
            result[idx] = nums[i % n]
        
        # Only add indices from first pass
        if i < n:
            stack.append(i)
    
    return result
```

### 模板 5：堆疊中帶額外資訊

```python
def monotonic_stack_with_info(nums):
    """
    Store additional information with stack elements
    Used for complex calculations
    """
    stack = []  # Store (index, value, additional_info)
    result = []
    
    for i, val in enumerate(nums):
        while stack and stack[-1][1] <= val:
            idx, old_val, info = stack.pop()
            # Process with additional information
            result.append(calculate_result(idx, i, old_val, val, info))
        
        # Calculate additional information for current element
        additional_info = calculate_info(val, stack)
        stack.append((i, val, additional_info))
    
    return result
```

### 模板 6：樣式驗證（132 Pattern） — LC 456

```python
def find_132_pattern(nums):
    """
    Find 132 pattern in array
    LC 456
    """
    n = len(nums)
    if n < 3:
        return False
    
    stack = []  # Store potential k values (decreasing)
    second = float('-inf')  # The "2" in 132 pattern
    
    # Traverse from right to left
    for i in range(n - 1, -1, -1):
        if nums[i] < second:  # Found "1" < "2"
            return True
        
        # Pop smaller values and update second
        while stack and stack[-1] < nums[i]:
            second = stack.pop()
        
        stack.append(nums[i])
    
    return False
```

## 依模式分類的題目

### 以模式分類的題目清單

#### **模式 1：Next/Previous Greater Element 題目**
| 題目 | LC # | 關鍵技巧 | 難度 | 模板 |
|---------|------|---------------|------------|----------|
| Next Greater Element I | 496 | 遞減堆疊 | Easy | 模板 1 |
| Next Greater Element II | 503 | 環狀陣列 | Medium | 模板 4 |
| Daily Temperatures | 739 | 計算距離 | Medium | 模板 1 |
| Remove K Digits | 402 | 貪婪 + 堆疊 | Medium | 模板 1 |
| Remove Duplicate Letters | 316 | 字典序 + 堆疊 | Medium | 模板 1 |
| Sliding Window Maximum | 239 | 單調雙端佇列 | Hard | 模板 1 |
| Shortest Unsorted Array | 581 | 兩趟堆疊 | Medium | 模板 1 |
| Sum of Subarray Ranges | 2104 | next greater + smaller | Medium | 模板 1+2 |

#### **模式 2：Next/Previous Smaller Element 題目**
| 題目 | LC # | 關鍵技巧 | 難度 | 模板 |
|---------|------|---------------|------------|----------|
| Largest Rectangle in Histogram | 84 | 計算面積 | Hard | 模板 3 |
| Maximal Rectangle | 85 | 二維直方圖 | Hard | 模板 3 |
| Sum of Subarray Minimums | 907 | 貢獻法 | Medium | 模板 2 |
| Number of Valid Subarrays | 1063 | 數較小元素 | Medium | 模板 2 |
| Minimum Cost Tree From Leaf Values | 1130 | 最佳合併 | Medium | 模板 2 |
| Find the Most Competitive Subsequence | 1673 | 選子序列 | Medium | 模板 2 |
| Maximum Subarray Min-Product | 1856 | 以最小值為樞紐 | Medium | 模板 2 |

#### **模式 3：直方圖與面積題目**
| 題目 | LC # | 關鍵技巧 | 難度 | 模板 |
|---------|------|---------------|------------|----------|
| Trapping Rain Water | 42 | 計算水位 | Hard | 模板 2 |
| Container With Most Water | 11 | 也可用雙指標 | Medium | 模板 2 |
| Maximal Rectangle | 85 | 逐列直方圖 | Hard | 模板 3 |
| Maximum Rectangle | 221 | DP + 直方圖 | Medium | 模板 3 |
| Minimum Number of Taps | 1326 | 區間覆蓋 | Hard | 模板 2 |
| Constrained Subsequence Sum | 1425 | DP + 單調雙端佇列 | Hard | 模板 2 |

#### **模式 4：序列順序與驗證題目**
| 題目 | LC # | 關鍵技巧 | 難度 | 模板 |
|---------|------|---------------|------------|----------|
| 132 Pattern | 456 | 樣式偵測 | Medium | 模板 6 |
| Online Stock Span | 901 | 單調堆疊 | Medium | 模板 1 |
| Score of Parentheses | 856 | 巢狀結構 | Medium | 模板 5 |
| Valid Parenthesis String | 678 | 平衡驗證 | Medium | 模板 5 |
| Minimum Add to Make Parentheses Valid | 921 | 平衡計數 | Medium | 模板 5 |
| Validate Stack Sequences | 946 | 模擬序列 | Medium | 模板 5 |
| Maximum Nesting Depth of Parentheses | 1614 | 追蹤深度 | Easy | 模板 5 |
| Minimum Remove to Make Valid Parentheses | 1249 | 平衡 + 移除 | Medium | 模板 5 |

#### **模式 5：最佳化與最大／最小題目**
| 題目 | LC # | 關鍵技巧 | 難度 | 模板 |
|---------|------|---------------|------------|----------|
| Maximum Score of Good Subarray | 1793 | 雙指標 + 堆疊 | Hard | 模板 2 |
| Number of Visible People in Queue | 1944 | 視線問題 | Medium | 模板 1 |
| Car Fleet | 853 | 計算時間 | Medium | 模板 1 |
| Car Fleet II | 1776 | 碰撞時間 | Hard | 模板 1 |
| Buildings With Ocean View | 1762 | 由右往左掃 | Medium | 模板 1 |
| Find the Winner of Circular Game | 1823 | 約瑟夫問題 | Medium | 模板 4 |
| Maximum Width Ramp | 962 | 索引差 | Medium | 模板 1 |
| Pancake Sorting | 969 | 反轉操作 | Medium | 模板 1 |

#### **模式 6：環狀陣列題目**
| 題目 | LC # | 關鍵技巧 | 難度 | 模板 |
|---------|------|---------------|------------|----------|
| Next Greater Element II | 503 | 走兩遍陣列 | Medium | 模板 4 |
| Car Fleet II | 1776 | 環狀碰撞 | Hard | 模板 4 |
| Circular Array Loop | 457 | 環偵測 | Medium | 模板 4 |
| Design Circular Queue | 622 | 環狀緩衝區 | Medium | 模板 4 |
| Design Circular Deque | 641 | 雙端環狀 | Medium | 模板 4 |

#### **進階／混合模式題目**
| 題目 | LC # | 關鍵技巧 | 難度 | 模板 |
|---------|------|---------------|------------|----------|
| Sum of Total Strength of Wizards | 2281 | 多個堆疊 | Hard | 多個 |
| Number of Ways to Rearrange Sticks | 1866 | 組合數學 + 堆疊 | Hard | 模板 5 |
| Basic Calculator | 224 | 運算式求值 | Hard | 模板 5 |
| Basic Calculator II | 227 | 運算子優先序 | Medium | 模板 5 |
| Basic Calculator III | 772 | 完整運算式解析 | Hard | 模板 5 |
| Evaluate Reverse Polish Notation | 150 | 後序求值 | Medium | 模板 5 |
| Decode String | 394 | 巢狀解碼 | Medium | 模板 5 |
| Find Duplicate Subtrees | 652 | 樹的序列化 | Medium | 模板 5 |
| Exclusive Time of Functions | 636 | 模擬呼叫堆疊 | Medium | 模板 5 |
| Minimum Window Subsequence | 727 | 雙指標 + 堆疊 | Hard | 模板 5 |

### 題目難度分布
- **Easy（8 題）**：基本的 next greater/smaller、簡單驗證
- **Medium（28 題）**：最常見的難度，涵蓋各種模式
- **Hard（16 題）**：複雜的面積計算、進階最佳化

### 模板使用頻率
- **模板 1（Next Greater）**：15 題
- **模板 2（Next Smaller）**：12 題  
- **模板 3（直方圖）**：8 題
- **模板 4（環狀）**：6 題
- **模板 5（驗證／複雜）**：11 題
- **多個模板混用**：8 題

## 模式選擇策略

### 決策流程圖

```text
Problem Analysis for Monotonic Stack:

1. Does the problem involve finding next/previous elements?
   ├── YES: Next/Previous GREATER elements?
   │   ├── YES: Use Template 1 (Decreasing Stack)
   │   │   ├── Array is circular? → Use Template 4 (Circular)
   │   │   └── Standard case → Template 1
   │   └── NO: Next/Previous SMALLER elements?
   │       ├── YES: Use Template 2 (Increasing Stack)
   │       └── NO: Continue to step 2
   └── NO: Continue to step 2

2. Does the problem involve heights/areas/rectangles?
   ├── YES: Rectangle area calculation?
   │   ├── YES: Use Template 3 (Histogram)
   │   └── NO: Water trapping/volume?
   │       └── YES: Use Template 2 (Next Smaller)
   └── NO: Continue to step 3

3. Does the problem involve sequence validation/patterns?
   ├── YES: Parentheses/brackets?
   │   ├── YES: Use Template 5 (Validation)
   │   └── NO: Specific pattern (like 132)?
   │       └── YES: Use Template 6 (Pattern Detection)
   └── NO: Continue to step 4

4. Does the problem involve optimization/max-min constraints?
   ├── YES: Multiple criteria optimization?
   │   ├── YES: Use Template 5 (Complex Info)
   │   └── NO: Simple max/min tracking?
   │       └── YES: Use Template 1 or 2
   └── NO: Continue to step 5

5. Does the problem involve circular arrays or cyclic behavior?
   ├── YES: Use Template 4 (Circular Processing)
   └── NO: Consider if monotonic stack is the right approach
       └── May need different data structure/algorithm
```

### 逐步分析題目

1. **先看清核心需求**
   - 查詢 next/previous 元素 → 模板 1、2、4
   - 計算面積／矩形 → 模板 3
   - 樣式驗證 → 模板 5、6
   - 最佳化問題 → 模板 1、2、5

2. **決定堆疊順序**
   - 要找較大的元素 → 遞減堆疊（彈出較小的）
   - 要找較小的元素 → 遞增堆疊（彈出較大的）
   - 面積計算 → 通常是遞增堆疊
   - 樣式偵測 → 視樣式而定

3. **決定處理方向**
   - 由左往右：最常見，也最自然
   - 由右往左：找「next」元素時有時比較好寫
   - 環狀：把陣列處理多遍

4. **決定堆疊裡放什麼**
   - 索引：需要位置資訊時
   - 值：只需要比大小時
   - Tuple：需要額外資訊時

### 模板選擇速查

| 題型 | 模板 | 堆疊內容 | 處理順序 |
|--------------|----------|---------------|------------------|
| **Next Greater** | 模板 1 | 索引 | 由左往右 |
| **Next Smaller** | 模板 2 | 索引 | 由左往右 |
| **Previous Greater** | 模板 1 | 索引 | 由左往右 |
| **Previous Smaller** | 模板 2 | 索引 | 由左往右 |
| **直方圖面積** | 模板 3 | 索引 | 由左往右 |
| **環狀陣列** | 模板 4 | 索引 | 走兩遍 |
| **樣式偵測** | 模板 6 | 值 | 由右往左 |
| **複雜驗證** | 模板 5 | Tuple | 視情況 |

## 總結與速查

### 複雜度速查
| 操作 | 時間 | 空間 | 備註 |
|-----------|------|-------|-------|
| **推入堆疊** | O(1) | - | 每個元素只推入一次 |
| **彈出堆疊** | O(1) | - | 每個元素只彈出一次 |
| **整體演算法** | O(n) | O(n) | 攤還線性時間 |
| **Next Greater/Smaller** | O(n) | O(n) | 單趟掃過陣列 |
| **直方圖面積** | O(n) | O(n) | 帶堆疊的線性掃描 |
| **環狀陣列** | O(n) | O(n) | 走兩趟，複雜度不變 |

### 模板速查
| 模板 | 模式 | 關鍵程式碼 |
|----------|---------|------------------|
| **模板 1** | Next Greater | `while stack and nums[stack[-1]] < nums[i]` |
| **模板 2** | Next Smaller | `while stack and nums[stack[-1]] > nums[i]` |
| **模板 3** | 直方圖 | `while stack and heights[stack[-1]] > h` |
| **模板 4** | 環狀 | `for i in range(2 * n)` |
| **模板 5** | 驗證 | 在堆疊中存額外資訊 |
| **模板 6** | 樣式偵測 | 由右往左，同時追蹤條件 |

### 常見模式與技巧

#### **Next Greater Element 模式**
```python
# Standard next greater element
def next_greater_elements(nums):
    stack, result = [], [-1] * len(nums)
    for i, num in enumerate(nums):
        while stack and nums[stack[-1]] < num:
            result[stack.pop()] = num
        stack.append(i)
    return result
```

#### **子陣列的貢獻法**
```python
# Count contribution of each element
def sum_subarray_mins(arr):
    n = len(arr)
    left = [-1] * n    # Previous smaller element
    right = [n] * n    # Next smaller element
    
    # Calculate left boundaries
    stack = []
    for i in range(n):
        while stack and arr[stack[-1]] >= arr[i]:
            stack.pop()
        left[i] = stack[-1] if stack else -1
        stack.append(i)
    
    # Calculate contribution
    result = 0
    for i in range(n):
        result += arr[i] * (i - left[i]) * (right[i] - i)
    return result % (10**9 + 7)
```

#### **直方圖面積計算**
```python
# Largest rectangle with height as key
def largest_rectangle_area(heights):
    stack = []
    max_area = 0
    for i, h in enumerate(heights + [0]):
        while stack and heights[stack[-1]] > h:
            height = heights[stack.pop()]
            width = i if not stack else i - stack[-1] - 1
            max_area = max(max_area, height * width)
        stack.append(i)
    return max_area
```

### 解題步驟

1. **步驟 1：判斷模式類型**
   - 找關鍵字：next/previous、greater/smaller、面積、矩形
   - 確認有沒有環狀／循環的需求
   - 判斷是否需要驗證或樣式偵測

2. **步驟 2：挑對模板**
   - 用決策流程圖
   - 想清楚堆疊順序（遞增還是遞減）
   - 決定堆疊裡要存什麼資訊

3. **步驟 3：實作核心邏輯**
   - 準備好堆疊與結果容器
   - 寫出 while 迴圈，彈出條件要對
   - 妥善處理彈出的元素
   - 處理最後留在堆疊裡的元素

4. **步驟 4：處理邊界情況**
   - 空陣列
   - 只有一個元素
   - 所有元素都相同
   - 嚴格遞增／遞減的序列

5. **步驟 5：最佳化與驗證**
   - 確認時間複雜度是 O(n)
   - 檢查空間複雜度
   - 用範例輸入驗證
   - 必要時處理整數溢位

### 常見錯誤與提醒

#### 常見錯誤
- **堆疊順序搞反**：next greater 題卻用遞增堆疊（應該用遞減）
- **索引與值混淆**：需要算距離時卻只存了值
- **處理不完整**：忘了處理最後留在堆疊裡的元素
- **邊界問題**：沒有妥善處理堆疊為空的情況
- **環狀邏輯**：環狀陣列處理不正確（漏了第二趟）
- **條件寫錯**：比較運算子用錯（< vs <=、> vs >=）

#### 最佳實務
- 需要位置資訊時，**一律存索引**
- 用**哨兵值**（例如 0）簡化邊界處理
- 除非真的需要由右往左，否則**由左往右處理**
- **變數名要清楚**：用 `stack`、`result`、`current_idx`，不要用 `s`、`res`、`i`
- **在 while 條件加註解**，說明維持的單調性質
- **先處理邊界情況**，再寫主要演算法

### 面試技巧

1. **辨識模式**
   - 聽到「next greater/smaller」這類關鍵字就要有反應
   - 面積／矩形題常常用得上單調堆疊
   - 序列驗證題可能需要以堆疊為基礎的做法

2. **解題流程**
   - 先從暴力解開始，把題目搞懂
   - 判斷單調性質能不能把解法優化
   - 畫例子，把堆疊的行為視覺化

3. **面試時的溝通**
   - 解釋為什麼這題適合用單調堆疊
   - 用例子把堆疊的狀態走一遍給面試官看
   - 討論時間／空間複雜度的取捨

4. **實作提醒**
   - 從模板骨架開始寫
   - 把心力放在 while 條件寫對
   - 用簡單的例子測試（像 [2,1,2,4,3,1]）

5. **預期會有的追問**
   - 有重複值怎麼辦？
   - 如果要的是 previous 而不是 next 呢？
   - 空間複雜度還能再優化嗎？
   - 怎麼延伸到二維問題？

### 相關主題

- **堆疊**：單調堆疊是堆疊這個資料結構的特化應用
- **雙端佇列**：滑動視窗最大值用的是單調雙端佇列
- **雙指標**：某些面積計算題的替代解法
- **動態規劃**：有些最佳化問題會把 DP 和單調堆疊搭在一起
- **二分搜尋**：在有序結構中找邊界
- **線段樹**：區間最大／最小值的進階查詢

## LC 範例

### 2-1) Daily Temperatures (LC 739) — 單調遞減堆疊
> 堆疊存索引；遇到更暖的一天就彈出。

```java
// LC 739 - Daily Temperatures
// IDEA: Monotonic decreasing stack — pop when current > stack top
// time = O(N), space = O(N)
public int[] dailyTemperatures(int[] temperatures) {
    int n = temperatures.length;
    int[] ans = new int[n];
    Deque<Integer> stack = new ArrayDeque<>(); // stores indices
    for (int i = 0; i < n; i++) {
        while (!stack.isEmpty() && temperatures[i] > temperatures[stack.peek()]) {
            int idx = stack.pop();
            ans[idx] = i - idx;
        }
        stack.push(i);
    }
    return ans;
}
```

### 2-2) Largest Rectangle in Histogram (LC 84) — 單調遞增堆疊
> 來了比較矮的柱子就彈出；用堆疊算出寬度後求面積。

```java
// LC 84 - Largest Rectangle in Histogram
// IDEA: Monotonic increasing stack — pop and compute area on shorter bar
// time = O(N), space = O(N)
public int largestRectangleArea(int[] heights) {
    Deque<Integer> stack = new ArrayDeque<>();
    int maxArea = 0;
    for (int i = 0; i <= heights.length; i++) {
        int h = (i == heights.length) ? 0 : heights[i];
        while (!stack.isEmpty() && h < heights[stack.peek()]) {
            int height = heights[stack.pop()];
            int width = stack.isEmpty() ? i : i - stack.peek() - 1;
            maxArea = Math.max(maxArea, height * width);
        }
        stack.push(i);
    }
    return maxArea;
}
```

### 2-3) Next Greater Element I (LC 496) — 單調堆疊 + HashMap
> 先把 nums2 的 next greater 全部算好，再回答 nums1 的查詢。

```java
// LC 496 - Next Greater Element I
// IDEA: Monotonic decreasing stack on nums2; store results in map
// time = O(M + N), space = O(M)
public int[] nextGreaterElement(int[] nums1, int[] nums2) {
    Map<Integer, Integer> map = new HashMap<>(); // val -> next greater val
    Deque<Integer> stack = new ArrayDeque<>();
    for (int num : nums2) {
        while (!stack.isEmpty() && num > stack.peek()) {
            map.put(stack.pop(), num);
        }
        stack.push(num);
    }
    int[] ans = new int[nums1.length];
    for (int i = 0; i < nums1.length; i++) {
        ans[i] = map.getOrDefault(nums1[i], -1);
    }
    return ans;
}
```

### 2-4) Trapping Rain Water (LC 42) — 單調堆疊
> 來了比較高的柱子就彈出；接到的水 =（較小高度的差）* 寬度。

```java
// LC 42 - Trapping Rain Water
// IDEA: Monotonic stack — pop when taller bar found, water fills between boundaries
// time = O(N), space = O(N)
public int trap(int[] height) {
    Deque<Integer> stack = new ArrayDeque<>();
    int water = 0;
    for (int i = 0; i < height.length; i++) {
        while (!stack.isEmpty() && height[i] > height[stack.peek()]) {
            int bottom = stack.pop();
            if (stack.isEmpty()) break;
            int left = stack.peek();
            int width = i - left - 1;
            int boundedHeight = Math.min(height[left], height[i]) - height[bottom];
            water += width * boundedHeight;
        }
        stack.push(i);
    }
    return water;
}
```

### 2-5) Next Greater Element II (LC 503) — 環狀單調堆疊
> 把陣列走兩遍（或用模運算）來處理環狀的 next greater 查詢。

```java
// LC 503 - Next Greater Element II (circular array)
// IDEA: Monotonic stack — traverse 2n indices with modulo for circular effect
// time = O(N), space = O(N)
public int[] nextGreaterElements(int[] nums) {
    int n = nums.length;
    int[] ans = new int[n];
    Arrays.fill(ans, -1);
    Deque<Integer> stack = new ArrayDeque<>();
    for (int i = 0; i < 2 * n; i++) {
        while (!stack.isEmpty() && nums[i % n] > nums[stack.peek()]) {
            ans[stack.pop()] = nums[i % n];
        }
        if (i < n) stack.push(i);
    }
    return ans;
}
```

### 2-6) Online Stock Span (LC 901) — 單調遞減堆疊
> 把先前 <= 目前價格的都彈掉；span = 距離上一個更高價過了幾天。

```java
// LC 901 - Online Stock Span
// IDEA: Monotonic decreasing stack storing [price, span] pairs
// time = O(1) amortized per call, space = O(N)
class StockSpanner {
    Deque<int[]> stack = new ArrayDeque<>(); // [price, span]
    public int next(int price) {
        int span = 1;
        while (!stack.isEmpty() && stack.peek()[0] <= price) {
            span += stack.pop()[1];
        }
        stack.push(new int[]{price, span});
        return span;
    }
}
```

### 2-7) Sum of Subarray Minimums (LC 907) — 單調堆疊
> 對每個元素，找出它是最小值的左右邊界；用單調堆疊做。

```java
// LC 907 - Sum of Subarray Minimums
// IDEA: Monotonic stack — for each element find left & right span as minimum
// time = O(N), space = O(N)
public int sumSubarrayMins(int[] arr) {
    int n = arr.length;
    int MOD = 1_000_000_007;
    int[] left = new int[n], right = new int[n];
    Deque<Integer> stack = new ArrayDeque<>();
    // left[i] = distance to previous smaller element
    for (int i = 0; i < n; i++) {
        while (!stack.isEmpty() && arr[stack.peek()] >= arr[i]) stack.pop();
        left[i] = stack.isEmpty() ? i + 1 : i - stack.peek();
        stack.push(i);
    }
    stack.clear();
    // right[i] = distance to next smaller or equal element
    for (int i = n-1; i >= 0; i--) {
        while (!stack.isEmpty() && arr[stack.peek()] > arr[i]) stack.pop();
        right[i] = stack.isEmpty() ? n - i : stack.peek() - i;
        stack.push(i);
    }
    long ans = 0;
    for (int i = 0; i < n; i++) ans = (ans + (long) arr[i] * left[i] * right[i]) % MOD;
    return (int) ans;
}
```

#### **貢獻法 — 把 `left[i]` / `right[i]` 視覺化（Python）** ⭐⭐⭐⭐⭐

> `leetcode_python/Math/sum-of-subarray-minimums.py`

**核心想法：** 每個子陣列都恰好有一個最小值，所以不要去列舉子陣列，而是反過來問*「有多少個子陣列的最小值是 `arr[i]`？」* — 然後把 `arr[i] * count` 全部加起來。

對每個索引 `i`，這個數量會拆成兩個互相獨立的選擇：

```text
        left choices              right choices
           <---->                    <----->
   ┌───────────────────────────────────────────────┐
   │  ...  PSE   .   .   .   [i]   .   .   .   NSE   │      arr
   └───────────────────────────────────────────────┘
              ^                          ^
        previous smaller           next smaller-or-equal
        element (strict >=)        element (strict >)

   left[i]  = i - PSE     ← # of left endpoints that keep arr[i] as min
   right[i] = NSE - i     ← # of right endpoints that keep arr[i] as min

   count(i) = left[i] * right[i]
   contribution = arr[i] * left[i] * right[i]
```

- 一個子陣列要讓 `arr[i]` 當最小值，就必須**起點**落在 `(PSE, i]`、**終點**落在 `[i, NSE)`。
- 兩個範圍互相獨立 → 相乘。

**處理重複值（避免重複計算）：** 左邊那趟用 **`>=`**、右邊那趟用 **`>`**（刻意不對稱）。這樣相等的值只會被算在其中一側。

```python
# python
# LC 907 - Sum of Subarray Minimums (contribution method)
# time = O(n), space = O(n)
MOD = 10**9 + 7
n = len(arr)
left  = [0] * n   # left[i]  = distance to previous smaller element
right = [0] * n   # right[i] = distance to next smaller-or-equal element

# --- LEFT pass: distance to Previous Smaller Element (pop on >=) ---
mono_st = []
for i in range(n):
    val = arr[i]
    # Pop elements that are greater than OR EQUAL to current val
    while mono_st and arr[mono_st[-1]] >= val:
        mono_st.pop()   # these can't be the left boundary of arr[i]

    # If stack empty -> val is the smallest so far, boundary is index -1
    #   left choices = i - (-1) = i + 1
    # Else -> boundary is the surviving stack top (the PSE)
    #   left choices = i - mono_st[-1]
    left[i] = i + 1 if not mono_st else i - mono_st[-1]
    mono_st.append(i)

# --- RIGHT pass: distance to Next Smaller Element (pop on >) ---
mono_st = []
for i in range(n - 1, -1, -1):
    val = arr[i]
    while mono_st and arr[mono_st[-1]] > val:   # strict > here
        mono_st.pop()
    right[i] = n - i if not mono_st else mono_st[-1] - i
    mono_st.append(i)

ans = 0
for i in range(n):
    ans = (ans + arr[i] * left[i] * right[i]) % MOD
```

**堆疊為空時為什麼 `left[i] = i + 1`：** 堆疊為空代表左邊沒有任何元素比 `arr[i]` 小 — `arr[i]` 主宰了整個前綴。想像中的左邊界落在索引 `-1`，所以左邊的選擇涵蓋索引 `0..i`，也就是 `i - (-1) = i + 1`。

**在 `arr = [3, 1, 2, 4]` 上的圖解追蹤：**

```text
i=0 val=3 : stack empty              -> left[0] = 0-(-1) = 1   stack=[0]
i=1 val=1 : arr[0]=3 >= 1 -> pop 0
            stack empty              -> left[1] = 1-(-1) = 2   stack=[1]
i=2 val=2 : arr[1]=1 >= 2? no        -> left[2] = 2-1     = 1   stack=[1,2]
i=3 val=4 : arr[2]=2 >= 4? no        -> left[3] = 3-2     = 1   stack=[1,2,3]

left  = [1, 2, 1, 1]
right = [1, 3, 2, 1]   (symmetric backward pass with strict >)

contribution = 3*1*1 + 1*2*3 + 2*1*2 + 4*1*1 = 3 + 6 + 4 + 4 = 17  ✓
```

### 2-8) Remove K Digits (LC 402) — 單調遞增堆疊
> 維持遞增堆疊；來了比較小的數字就把前面的移掉。

```java
// LC 402 - Remove K Digits
// IDEA: Greedy + monotonic increasing stack — remove larger digits greedily
// time = O(N), space = O(N)
public String removeKdigits(String num, int k) {
    Deque<Character> stack = new ArrayDeque<>();
    for (char c : num.toCharArray()) {
        while (k > 0 && !stack.isEmpty() && stack.peek() > c) {
            stack.pop(); k--;
        }
        stack.push(c);
    }
    while (k-- > 0) stack.pop(); // remove from top if k still > 0
    // reconstruct result in correct order (bottom to top of stack)
    Deque<Character> result = new ArrayDeque<>(stack);
    StringBuilder sb = new StringBuilder();
    boolean leadingZero = true;
    while (!result.isEmpty()) {
        char c = result.pollFirst();
        if (leadingZero && c == '0') continue;
        leadingZero = false;
        sb.append(c);
    }
    return sb.length() == 0 ? "0" : sb.toString();
}
```

### 2-9) Maximal Rectangle (LC 85) — 直方圖 + 單調堆疊
> 逐列算出直方圖高度；每一列套用 LC 84 的最大矩形邏輯。

```java
// LC 85 - Maximal Rectangle
// IDEA: For each row build histogram; apply largestRectangleArea (LC 84) logic
// time = O(M*N), space = O(N)
public int maximalRectangle(char[][] matrix) {
    if (matrix.length == 0) return 0;
    int n = matrix[0].length, maxArea = 0;
    int[] heights = new int[n];
    for (char[] row : matrix) {
        for (int j = 0; j < n; j++)
            heights[j] = row[j] == '0' ? 0 : heights[j] + 1;
        maxArea = Math.max(maxArea, largestRectangle(heights));
    }
    return maxArea;
}
private int largestRectangle(int[] heights) {
    Deque<Integer> stack = new ArrayDeque<>();
    int max = 0;
    for (int i = 0; i <= heights.length; i++) {
        int h = i == heights.length ? 0 : heights[i];
        while (!stack.isEmpty() && h < heights[stack.peek()]) {
            int height = heights[stack.pop()];
            int width = stack.isEmpty() ? i : i - stack.peek() - 1;
            max = Math.max(max, height * width);
        }
        stack.push(i);
    }
    return max;
}
```

### 2-10) Car Fleet (LC 853) — 對速度做單調堆疊
> 按位置排序；堆疊記錄車隊 — 併入前車隊的車就被移掉。

```java
// LC 853 - Car Fleet
// IDEA: Sort by position DESC; use stack to count distinct fleets
// time = O(N log N), space = O(N)
public int carFleet(int target, int[] position, int[] speed) {
    int n = position.length;
    Integer[] idx = new Integer[n];
    for (int i = 0; i < n; i++) idx[i] = i;
    Arrays.sort(idx, (a, b) -> position[b] - position[a]);
    Deque<Double> stack = new ArrayDeque<>();
    for (int i : idx) {
        double time = (double)(target - position[i]) / speed[i];
        if (stack.isEmpty() || time > stack.peek()) stack.push(time);
        // if time <= top, this car catches up (joins the fleet)
    }
    return stack.size();
}
```

### 2-11) Asteroid Collision (LC 735) — 堆疊模擬
> 向右飛的留在堆疊上；向左飛的一直和頂端相撞，直到穩定。

```java
// LC 735 - Asteroid Collision
// IDEA: Stack — simulate collisions between right (+) and left (-) asteroids
// time = O(N), space = O(N)
public int[] asteroidCollision(int[] asteroids) {
    Deque<Integer> stack = new ArrayDeque<>();
    for (int a : asteroids) {
        boolean alive = true;
        while (alive && a < 0 && !stack.isEmpty() && stack.peek() > 0) {
            if (stack.peek() < -a) { stack.pop(); }      // stack top destroyed
            else if (stack.peek() == -a) { stack.pop(); alive = false; } // both destroyed
            else alive = false;                             // incoming destroyed
        }
        if (alive) stack.push(a);
    }
    int[] res = new int[stack.size()];
    for (int i = res.length - 1; i >= 0; i--) res[i] = stack.pop();
    return res;
}
```

### 2-12) Sum of Subarray Ranges (LC 2104) — 雙單調堆疊（貢獻法）

> `sum(ranges) = sum(subarray maxs) − sum(subarray mins)`。最大值與最小值各跑一趟單調堆疊；每彈出一個元素，就算出它以最大／最小值的身分主宰了多少個子陣列。

#### 核心想法

```text
range(subarray) = max − min
sum(all ranges) = sum(all subarray maxs) − sum(all subarray mins)
```

對每個元素 `nums[mid]`，找出它的**左**、**右**主宰邊界：
- **左邊界** `L` — 前一個會讓 `nums[mid]` 失去最大／最小身分的元素索引（沒有就是 `-1`）
- **右邊界** `R` — 下一個會取代它的元素索引（沒有就是 `n`）

`nums[mid]` 擔任最大／最小值的子陣列數量：
```text
count = (mid − L) × (R − mid)
contribution = nums[mid] × count
```

**哨兵迴圈**讓 `i` 從 `0` 跑到 `n`（含）。當 `i == n` 時，用 `n` 當右邊界把堆疊裡剩下的索引全部清掉。

**對重複值安全的邊界規則**（避免相等元素被重複計算）：
- **最大值**那趟：`nums[mid] < nums[i]` 時彈出（嚴格）；左邊界是上一個*大於或等於*的元素。
- **最小值**那趟：`nums[mid] > nums[i]` 時彈出（嚴格）；左邊界是上一個*小於或等於*的元素。

---

#### 圖解追蹤 — `[1, 3, 2]` 的最大值那趟

```text
Decreasing stack (max contribution)

i=0: push 0         stack=[0]
i=1: nums[0]=1 < nums[1]=3 → pop mid=0
       left=-1, right=1
       contrib = 1 * (0-(-1)) * (1-0) = 1*1*1 = 1
     push 1          stack=[1]
i=2: nums[1]=3 > nums[2]=2, no pop
     push 2          stack=[1,2]
i=3 (sentinel): flush
     pop mid=2: left=1, right=3  → 2*(2-1)*(3-2) = 2
     pop mid=1: left=-1, right=3 → 3*(1-(-1))*(3-1) = 12

max_sum = 1 + 2 + 12 = 15

min pass (increasing stack) → min_sum = 10

answer = 15 − 10 = 5  ✓
verify: [1]=0,[3]=0,[2]=0,[1,3]=2,[3,2]=1,[1,3,2]=2 → sum = 5
```

---

#### 模式（Python）

```python
# python
# LC 2104 - Sum of Subarray Ranges
# IDEA: sum(ranges) = sum(subarray maxs) - sum(subarray mins)
#       Contribution method via monotonic stack — one pass per role
# time = O(N), space = O(N)
def subArrayRanges(nums):
    n = len(nums)

    def contribution(is_max):
        stack = []
        total = 0
        for i in range(n + 1):          # sentinel: i == n flushes remaining
            while stack and (
                i == n or
                (nums[stack[-1]] < nums[i] if is_max else nums[stack[-1]] > nums[i])
            ):
                mid = stack.pop()
                left  = stack[-1] if stack else -1   # previous boundary index
                right = i                            # current index = right boundary
                total += nums[mid] * (mid - left) * (right - mid)
            stack.append(i)
        return total

    return contribution(True) - contribution(False)
```

#### 模式（Java）

```java
// java
// LC 2104 - Sum of Subarray Ranges
// IDEA: sum(ranges) = sum(subarray maxs) - sum(subarray mins)
//       Contribution method: for each element count subarrays where it's max/min
// time = O(N), space = O(N)
public long subArrayRanges(int[] nums) {
    return contribution(nums, true) - contribution(nums, false);
}

private long contribution(int[] nums, boolean isMax) {
    int n = nums.length;
    Deque<Integer> stack = new ArrayDeque<>();
    long total = 0;

    for (int i = 0; i <= n; i++) {          // i == n is the sentinel flush
        while (!stack.isEmpty()) {
            int mid = stack.peek();
            boolean shouldPop = (i == n) ||
                (isMax ? nums[mid] < nums[i] : nums[mid] > nums[i]);
            if (!shouldPop) break;
            stack.pop();
            int left  = stack.isEmpty() ? -1 : stack.peek(); // prev boundary
            int right = i;                                    // next boundary
            total += (long) nums[mid] * (mid - left) * (right - mid);
        }
        stack.push(i);
    }
    return total;
}
```

#### 雙堆疊邏輯總結

| 趟次 | 堆疊類型 | 彈出條件 | 算出什麼 |
|------|-----------|---------------|----------|
| 最大值那趟 | 單調**遞減** | `nums[mid] < nums[i]` | 所有子陣列最大值的總和 |
| 最小值那趟 | 單調**遞增** | `nums[mid] > nums[i]` | 所有子陣列最小值的總和 |
| 兩趟都有 | `i = n` 的哨兵 | 一律清空 | 處理靠右邊界的元素 |

#### 相似題目

| 題目 | LC# | 關鍵差異 |
|---------|-----|----------------|
| Sum of Subarray Ranges | 2104 | `max_sum − min_sum`；兩趟單調堆疊 |
| Sum of Subarray Minimums | 907 | 只算最小值的貢獻；單趟遞增堆疊 |
| Maximum Subarray Min-Product | 1856 | 最小值貢獻 × 子陣列和；前綴和 + 堆疊 |
| Sum of Total Strength of Wizards | 2281 | 最小值 × 和的和；前綴和的前綴和 + 堆疊 |
| Largest Rectangle in Histogram | 84 | 面積 = 高 × 寬；遇到較矮的柱子彈出 |
| Number of Visible People in Queue | 1944 | 每個元素彈出的次數就是答案 |

### 2-13) Longest Absolute File Path (LC 388) — 以巢狀深度為索引的堆疊 ⭐⭐⭐⭐⭐

> **模板 7：深度堆疊。** 這個堆疊不是按*值*單調，而是按**深度**單調：`stack[d]` 永遠存著深度 `d` 的累積路徑長度。處理深度為 `d` 的那一行之前，先彈到 `stack.size() == d`，就把剛結束的兄弟分支全部丟掉了。

**核心想法**
```text
"dir\n\tsub1\n\t\tfile.ext\n\tsub2"

line          depth   pop until size==depth   stack (path lengths, '/' included)
dir             0     []                      [4]            "dir/"
  sub1          1     [4]                     [4, 9]         "dir/sub1/"
    file.ext    2     [4, 9]                  (file → no push, len = 9 + 8 = 17)
  sub2          1     pop 9 → [4]             [4, 9]
```
- `depth` = 開頭 `\t` 的數量；剩下的部分就是名稱。
- **目錄**會推入 `parentLen + name.length() + 1`（`+1` 是 `/` 分隔符）。
- **檔案**（名稱含 `.`）永遠不推入 — 只用 `parentLen + name.length()` 更新答案。

```java
// java
// LC 388 - Longest Absolute File Path
// IDEA: Stack indexed by nesting depth — stack.peek() = length of the current
//       directory prefix (with trailing '/'); pop until size == depth to leave sibling branches
// time = O(N), space = O(D)  // N = input length, D = max depth
public int lengthLongestPath(String input) {
    Deque<Integer> stack = new ArrayDeque<>(); // prefix length per depth
    int maxLen = 0;
    for (String line : input.split("\n")) {
        int depth = line.lastIndexOf('\t') + 1;  // tabs are leading & contiguous
        String name = line.substring(depth);
        while (stack.size() > depth) stack.pop();          // leave finished branches
        int parentLen = stack.isEmpty() ? 0 : stack.peek();
        int curLen = parentLen + name.length();
        if (name.indexOf('.') >= 0) {
            maxLen = Math.max(maxLen, curLen);             // file → candidate answer
        } else {
            stack.push(curLen + 1);                        // dir → +1 for '/'
        }
    }
    return maxLen;
}
```

```python
# python
# LC 388 - Longest Absolute File Path
# IDEA: stack[d] = length of the directory prefix at depth d (trailing '/' counted);
#       pop until len(stack) == depth so sibling branches are discarded
# time = O(N), space = O(D)
def lengthLongestPath(input: str) -> int:
    stack = []          # prefix length per depth
    best = 0
    for line in input.split('\n'):
        depth = len(line) - len(line.lstrip('\t'))
        name = line[depth:]
        while len(stack) > depth:
            stack.pop()
        parent = stack[-1] if stack else 0
        cur = parent + len(name)
        if '.' in name:
            best = max(best, cur)      # file
        else:
            stack.append(cur + 1)      # dir, +1 for '/'
    return best
```

**陷阱**
- 答案是最長的**到檔案的路徑**，所以碰到目錄時絕對不要更新最大值。
- 不要切掉字串之後才用 `line.count('\t')` 算深度 — 深度只能來自*開頭*的 tab。
- 空輸入／完全沒有檔案 → 回傳 `0`。

### 2-14) Longest Valid Parentheses (LC 32) — 索引堆疊搭配基準哨兵 ⭐⭐⭐⭐⭐

> **模板 8：索引堆疊 + 哨兵基準。** 不要存字元，改存**索引**，並且先塞一個 `-1` 當作「目前這段合法區塊前一格的索引」。遇到 `)` 彈出之後，新的堆疊頂端就是最後一個沒配對到的索引，所以 `i - stack.peek()` 就是以 `i` 結尾的合法長度 — 完全不用另外記長度。

**遇到 `)` 的兩種情況**
```text
pop, then:
  stack empty  → this ')' is unmatched → push i as the NEW base
  stack !empty → length = i - stack.top()
```

**在 `s = ")()())"` 上的圖解追蹤**
```text
i=0 ')'  pop -1 → empty → push 0        stack=[0]        best=0
i=1 '('  push 1                          stack=[0,1]
i=2 ')'  pop 1 → top=0 → 2-0 = 2         stack=[0]        best=2
i=3 '('  push 3                          stack=[0,3]
i=4 ')'  pop 3 → top=0 → 4-0 = 4         stack=[0]        best=4
i=5 ')'  pop 0 → empty → push 5          stack=[5]        best=4  ✓
```

```java
// java
// LC 32 - Longest Valid Parentheses
// IDEA: Stack of indices seeded with -1 (base). On ')' pop; if empty this ')' becomes
//       the new base, else answer candidate = i - stack.peek()
// time = O(N), space = O(N)
public int longestValidParentheses(String s) {
    Deque<Integer> stack = new ArrayDeque<>();
    stack.push(-1);                 // base = index before the current valid block
    int best = 0;
    for (int i = 0; i < s.length(); i++) {
        if (s.charAt(i) == '(') {
            stack.push(i);
        } else {
            stack.pop();
            if (stack.isEmpty()) stack.push(i);                 // unmatched ')' → new base
            else best = Math.max(best, i - stack.peek());
        }
    }
    return best;
}
```

```python
# python
# LC 32 - Longest Valid Parentheses
# IDEA: index stack with -1 sentinel; i - stack[-1] = length of valid run ending at i
# time = O(N), space = O(N)
def longestValidParentheses(s: str) -> int:
    stack = [-1]          # base index
    best = 0
    for i, c in enumerate(s):
        if c == '(':
            stack.append(i)
        else:
            stack.pop()
            if not stack:
                stack.append(i)               # new base
            else:
                best = max(best, i - stack[-1])
    return best
```

**陷阱**
- 忘了先塞 `-1`，所有從索引 `0` 開始的區段都會算錯。
- 堆疊裡存的是**索引**，絕不是字元 — 整個技巧就靠索引運算。
- O(1) 空間的替代解：走兩趟（左→右，再右→左），用 `open`／`close` 計數，當 `close > open`（反向時是 `open > close`）就歸零。

### 2-15) Maximum Binary Tree (LC 654) — 用單調遞減堆疊建出笛卡兒樹 ⭐⭐⭐⭐

> **模板 9：用單調堆疊建樹。** 直覺的「找最大值，再左右遞迴」是 O(n²)。用**遞減**堆疊可以一趟建出同一棵樹：被 `num` 彈掉的元素都比 `num` 小、而且都在它左邊 → 它們變成 `num` 的**左**子樹；活下來的堆疊頂端比 `num` 大 → `num` 變成它的**右**子節點。根就是堆疊最底部那個。

```text
nums = [3,2,1,6,0,5]

3 → stack[3]
2 → 3>2, 3.right = 2            stack[3,2]
1 → 2>1, 2.right = 1            stack[3,2,1]
6 → pop 1,2,3 (each becomes 6.left in turn, last popped wins) → stack empty
                                 stack[6]      root = 6
0 → 6.right = 0                 stack[6,0]
5 → pop 0 → 5.left = 0; top 6 → 6.right = 5   stack[6,5]
```

```java
// java
// LC 654 - Maximum Binary Tree
// IDEA: Monotonic DECREASING stack of nodes. Nodes popped by num become num's left
//       subtree (last popped = direct left child); surviving top adopts num as right child
// time = O(N), space = O(N)   // beats the O(N^2) divide & conquer build
public TreeNode constructMaximumBinaryTree(int[] nums) {
    Deque<TreeNode> stack = new ArrayDeque<>(); // values decreasing: bottom -> top
    for (int num : nums) {
        TreeNode cur = new TreeNode(num);
        while (!stack.isEmpty() && stack.peek().val < num) {
            cur.left = stack.pop();          // last popped ends up as the left child
        }
        if (!stack.isEmpty()) stack.peek().right = cur;
        stack.push(cur);
    }
    return stack.isEmpty() ? null : stack.peekLast(); // bottom of stack = global max = root
}
```

```python
# python
# LC 654 - Maximum Binary Tree
# IDEA: monotonic decreasing stack of nodes; popped nodes chain into cur.left,
#       remaining top takes cur as its right child; stack[0] is the root
# time = O(N), space = O(N)
def constructMaximumBinaryTree(nums):
    stack = []                      # node values decreasing
    for num in nums:
        cur = TreeNode(num)
        while stack and stack[-1].val < num:
            cur.left = stack.pop()  # overwritten each pop -> keeps the LAST popped
        if stack:
            stack[-1].right = cur
        stack.append(cur)
    return stack[0] if stack else None
```

**為什麼 `cur.left` 會被一直覆寫：** 每彈出一次就重新指定一次 `cur.left`，而被彈出的節點彼此早就串好了（先被彈出的是前一個節點的右子節點），所以迴圈結束後 `cur.left` 正好指向整個被彈出區塊的根。

**相關：** LC 1008（Construct BST from Preorder Traversal）用的是鏡像的想法 — 遞增堆疊，較大的值成為最後一個被彈出節點的右子節點。

### 2-16) Min Stack (LC 155) — 輔助的非遞增堆疊 ⭐⭐⭐⭐

> **模板 10：平行的「最小值堆疊」。** 另外維護一個值**非遞增**的堆疊；它的頂端永遠是目前存活元素中的最小值。這就是單調堆疊在設計題裡的樣子。

```java
// java
// LC 155 - Min Stack
// IDEA: second stack keeps a non-increasing sequence of minima; push a new min when
//       val <= current min (the '=' is REQUIRED so duplicates survive matching pops)
// time = O(1) per op, space = O(N)
class MinStack {
    private final Deque<Integer> stack = new ArrayDeque<>();
    private final Deque<Integer> mins  = new ArrayDeque<>(); // non-increasing

    public void push(int val) {
        stack.push(val);
        if (mins.isEmpty() || val <= mins.peek()) mins.push(val);
    }
    public void pop() {
        int v = stack.pop();
        if (v == mins.peek()) mins.pop();
    }
    public int top()    { return stack.peek(); }
    public int getMin() { return mins.peek(); }
}
```

```python
# python
# LC 155 - Min Stack
# IDEA: auxiliary non-increasing stack of minima; '<=' on push keeps duplicate minima
# time = O(1) per op, space = O(N)
class MinStack:
    def __init__(self):
        self.stack = []
        self.mins  = []          # non-increasing

    def push(self, val: int) -> None:
        self.stack.append(val)
        if not self.mins or val <= self.mins[-1]:
            self.mins.append(val)

    def pop(self) -> None:
        if self.stack.pop() == self.mins[-1]:
            self.mins.pop()

    def top(self) -> int:
        return self.stack[-1]

    def getMin(self) -> int:
        return self.mins[-1]
```

**經典 bug：** 只在 `val < mins.peek()`（嚴格）時才推入新的最小值。碰到 `push(0); push(0); pop();`，那唯一存下的 `0` 會被移掉，`getMin()` 就回傳錯的值。要用 `<=`。
**省空間的變形：** 在單一堆疊裡存 `(val, minSoFar)` 這種 pair — 操作一樣是 O(1)，而且面試壓力下比較好講清楚。

### 2-17) 既有模板的各種變形

| LC # | 題目 | 基礎模板 | 變化點 |
|------|---------|---------------|-----------|
| 1475 | Final Prices With a Special Discount in a Shop | 模板 2（next smaller） | 是 next smaller **或相等** — 條件改成 `prices[stack[-1]] >= prices[i]` 才彈出，而且折扣是 `price - prices[i]`，不是索引距離 |
| 1019 | Next Greater Node In Linked List | 模板 1（next greater） | 一樣的遞減堆疊，但輸入是鏈結串列 — 先走一趟轉成陣列（或邊走邊推入 `(index, val)`），因為答案陣列需要隨機存取 |
| 768 | Max Chunks To Make Sorted II | 模板 1（遞減彈出） | 堆疊裡放的是**各區塊的最大值**，不是原始元素；答案 = 最後的堆疊大小 |
| 769 | Max Chunks To Make Sorted | 模板 1（退化版） | 值是 `0..n-1` 的排列，所以用一個 running max 就能取代堆疊：只要 `runningMax == i` 就切一塊 |
| 1047 / 1209 | Remove All Adjacent Duplicates In String (I / II) | 模板 5（堆疊帶資訊） | 堆疊存 `(char, count)`；`count` 到 `k` 就彈出 — LC 1047 就是 `k = 2` 的特例 |

**Max Chunks To Make Sorted II (LC 768) — 區塊最大值堆疊**

```java
// java
// LC 768 - Max Chunks To Make Sorted II
// IDEA: monotonic increasing stack of chunk MAXIMA. A value smaller than the top must
//       merge every chunk it is smaller than; the merged chunk keeps the largest max
// time = O(N), space = O(N)
public int maxChunksToSorted(int[] arr) {
    Deque<Integer> stack = new ArrayDeque<>(); // chunk maxima, increasing bottom -> top
    for (int num : arr) {
        if (!stack.isEmpty() && num < stack.peek()) {
            int maxOfMerged = stack.pop();
            while (!stack.isEmpty() && num < stack.peek()) stack.pop();
            stack.push(maxOfMerged);           // merged chunk keeps the old max
        } else {
            stack.push(num);                   // starts a new chunk
        }
    }
    return stack.size();
}
```

```python
# python
# LC 768 - Max Chunks To Make Sorted II
# IDEA: increasing stack of chunk maxima; merging keeps the largest max
# time = O(N), space = O(N)
def maxChunksToSorted(arr):
    stack = []                       # chunk maxima, increasing
    for num in arr:
        if stack and num < stack[-1]:
            merged_max = stack.pop()
            while stack and num < stack[-1]:
                stack.pop()
            stack.append(merged_max)
        else:
            stack.append(num)
    return len(stack)

# LC 769 - Max Chunks To Make Sorted (values are a permutation of 0..n-1)
# time = O(N), space = O(1)
def maxChunksToSortedI(arr):
    chunks, running_max = 0, -1
    for i, num in enumerate(arr):
        running_max = max(running_max, num)
        if running_max == i:         # prefix holds exactly the values 0..i
            chunks += 1
    return chunks
```

### 2-18) 值得知道的經典堆疊題（非單調）

> 這些用的是普通堆疊（沒有單調不變式），但常常和上面那些模式一起出現。

| 題目 | LC # | 關鍵技巧 | 難度 |
|---------|------|---------------|------------|
| Simplify Path | 71 | 用 `/` 切開；元件推入，`..` 彈出，`.`／空字串跳過 | Medium |
| Backspace String Compare | 844 | 每個字串一個堆疊，或從後往前用雙指標做到 O(1) 空間 | Easy |
| Remove All Adjacent Duplicates In String | 1047 | 推入字元，和頂端相同就彈出 | Easy |
| Remove All Adjacent Duplicates in String II | 1209 | 堆疊存 `(char, count)`，count 到 `k` 就彈出 | Medium |
| Flatten Nested List Iterator | 341 | 堆疊裡放 iterator／list；在 `hasNext()` 中惰性攤平 | Medium |
| Binary Search Tree Iterator | 173 | 受控的迭代中序 — 堆疊存左脊 | Medium |
| Maximum Frequency Stack | 895 | `freq` map + 從頻率對應到值堆疊的 map | Hard |
| Baseball Game | 682 | 直接用堆疊模擬 `+`、`D`、`C` | Easy |

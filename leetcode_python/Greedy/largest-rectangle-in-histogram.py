"""

84. Largest Rectangle in Histogram
Hard

Given an array of integers heights representing the histogram's bar height where the width of each bar is 1, return the area of the largest rectangle in the histogram.

 

Example 1:


Input: heights = [2,1,5,6,2,3]
Output: 10
Explanation: The above is a histogram where width of each bar is 1.
The largest rectangle is shown in the red area, which has an area = 10 units.
Example 2:


Input: heights = [2,4]
Output: 4
 

Constraints:

1 <= heights.length <= 105
0 <= heights[i] <= 104

"""

# V0
class Solution(object):
    def largestRectangleArea(self, heights):
        """
        :type heights: List[int]
        :rtype: int
        """
        pass


# V0-1
# IDEA: Monotonic Stack (gemini)
# time = O(n)
# space = O(n)
"""

1. CORE IDEA:


對於每一個柱子（高度為 $h$），以它作為矩形高度能延伸的最大寬度，取決於：


    1. 左邊界：第一個小於 h 的柱子位置。

    2. 右邊界：第一個小於 h 的柱子位置。



    ->

     我們可以使用一個`單調遞增堆疊`（只儲存`索引`）。

     當我們遇到一個小於堆疊頂端高度的柱子時，
     就代表找到了堆疊頂端那個柱子的右邊界（就是當前索引），
     而此時堆疊中它左邊相鄰的元素就是它的左邊界。

     藉此可以在線性時間內算出以該柱子為高的最大矩形面積。

    

2.  ### 💡 為什麼需要結尾的 `while stack:` 迴圈？

在主迴圈中，我們只有在「遇到比堆疊頂端更矮的柱子時」**，才會將堆疊頂端的柱子彈出（Pop）並計算面積。因為此時當前索引 `i` 就是它的**右邊界。

但是，想像一下兩種情況：

1. 如果整個柱狀圖的的高度是遞增的（例如 `[1, 2, 3]`），主迴圈跑完後，所有的柱子都會留在堆疊裡，因為從頭到尾都沒有遇到比它們矮的柱子。
2. 即使有起伏，當主迴圈跑完（`i` 走到陣列盡頭 `n`）時，堆疊裡往往還會留下一些「沒有遇到右邊更矮柱子」的元素。

這段尾聲的 `while stack:` 迴圈就是在對這些剩餘的柱子說：**「既然你們的右邊已經沒有更矮的柱子來阻擋你們了，那就代表你們可以一路向右延伸，直到整個柱狀圖的的最右端（邊界 $n$）！」**



"""



"""
DRY RUN:

---

### 🔍 實際走訪範例（Dry Run）

讓我們用一個簡單的範例來一步步追蹤：**`heights = [2, 1, 5]`**（總長度 $n = 3$）

初始狀態：`stack = []`, `max_area = 0`

#### 第一階段：主迴圈 `for i in range(3)`

* **`i = 0`, val = `2` (`heights[0] = 2`)**
* `stack` 是空的，不進入 `while`。
* `stack.append(0)` $\rightarrow$ **`stack = [0]`**（對應高度 2）


* **`i = 1`, val = `1` (`heights[1] = 1`)**
* 檢查 `while stack and heights[stack[-1]] > heights[1]`：
* `heights[0]` (2) > `heights[1]` (1)，條件成立！進入 `while`：
* `h_idx = stack.pop()` $\rightarrow$ 彈出索引 `0`，`h = 2`。此時 `stack` 變空。
* 計算寬度：因為 `stack` 為空，寬度就是當前索引 `i`（即 `1`）。
* 面積 = `2 * 1 = 2`。`max_area = max(0, 2) = 2`。


* `while` 結束。
* `stack.append(1)` $\rightarrow$ **`stack = [1]`**（對應高度 1）


* **`i = 2`, val = `5` (`heights[2] = 5`)**
* `heights[stack[-1]]` (1) 不大於 `5`，不進入 `while`。
* `stack.append(2)` $\rightarrow$ **`stack = [1, 2]`**（對應高度 1 與 5）



---

#### 第二階段：主迴圈結束，進入尾聲 `while stack:` 迴圈

此時 `stack = [1, 2]`（分別對應索引 1 和 2，高度分別為 1 與 5），`n = 3`。

* **第一次迴圈（處理堆疊頂端：索引 `2`，高度 `5`）**：
* `h_idx = stack.pop()` $\rightarrow$ 彈出索引 `2`，`h = 5`。此時剩餘 `stack = [1]`。
* 計算寬度：
* 由於 `stack` 不為空，左邊界是 `stack[-1]`（即索引 `1`）。
* 寬度 = `n - stack[-1] - 1` $\rightarrow$ `3 - 1 - 1 = 1`。


* 面積 = `5 * 1 = 5`。`max_area = max(2, 5) = 5`。


* **第二次迴圈（處理堆疊頂端：索引 `1`，高度 `1`）**：
* `h_idx = stack.pop()` $\rightarrow$ 彈出索引 `1`，`h = 1`。此時 `stack` 變空。
* 計算寬度：
* 由於 `stack` 為空，寬度 = `n`（即 `3`）。


* 面積 = `1 * 3 = 3`。`max_area = max(5, 3) = 5`。


* `stack` 為空，迴圈結束。

---

### 🏁 最終回傳

回傳 `max_area = 5`（正確答案：由高度 5 的柱子，或是高度 1 延伸全場寬度 3 計算出來的最大面積）。

"""
class Solution(object):

  def largestRectangleArea(self, heights):
    """
        :type heights: List[int]
        :rtype: int
        """
    if not heights:
      return 0

    stack = []  # 儲存柱子的索引（對應高度保持單調遞增）
    max_area = 0
    n = len(heights)

    for i in range(n):
      # 當前高度小於堆疊頂端的高度時，代表找到了堆疊頂端柱子的右邊界
      while stack and heights[stack[-1]] > heights[i]:
        h_idx = stack.pop()
        h = heights[h_idx]

        # 計算寬度：
        # - 如果 stack 為空，代表左邊沒有比它小的，寬度延伸到 0（即 i）
        # - 如果 stack 不為空，左邊界為 stack[-1]，寬度為 i - stack[-1] - 1
        width = i if not stack else i - stack[-1] - 1
        max_area = max(max_area, h * width)

      stack.append(i)

    # 處理堆疊中剩餘的柱子（它們的右邊界延伸到陣列最右側 n）
    while stack:
      h_idx = stack.pop()
      h = heights[h_idx]
      width = n if not stack else n - stack[-1] - 1
      max_area = max(max_area, h * width)

    return max_area



# V0-2
# IDEA: Monotonic Stack (gpt)
# time = O(n)
# space = O(n)
class Solution(object):
    def largestRectangleArea(self, heights):
        """
        :type heights: List[int]
        :rtype: int
        """

        # Stack stores [start_index, height]
        # Heights in stack are increasing
        st = []

        max_area = 0

        for i, h in enumerate(heights):

            start = i

            # Current bar is shorter than stack top
            while st and st[-1][1] > h:

                prev_start, prev_height = st.pop()

                # prev_height can extend from prev_start
                # all the way to i - 1
                width = i - prev_start

                area = prev_height * width

                max_area = max(max_area, area)

                # The current shorter bar can also extend
                # to the left where prev_height started
                start = prev_start

            st.append((start, h))

        # Process remaining bars
        n = len(heights)

        while st:
            start, height = st.pop()

            width = n - start

            area = height * width

            max_area = max(max_area, area)

        return max_area



# V1
# IDEA : BRURE FORCE
# https://leetcode.com/problems/largest-rectangle-in-histogram/solution/
# time = O(n^3)
# space = O(1)
class Solution:
    def largestRectangleArea(self, heights: List[int]) -> int:
        max_area = 0
        for i in range(len(heights)):
            for j in range(i, len(heights)):
                min_height = inf
                for k in range(i, j + 1):
                    min_height = min(min_height, heights[k])
                max_area = max(max_area, min_height * (j - i + 1))
        return max_area

# V1'
# IDEA : BETTER BRURE FORCE
# https://leetcode.com/problems/largest-rectangle-in-histogram/solution/
# time = O(n^2)
# space = O(1)
class Solution:
    def largestRectangleArea(self, heights: List[int]) -> int:
        max_area = 0
        for i in range(len(heights)):
            min_height = inf
            for j in range(i, len(heights)):
                # NOTE : we maintain min_height
                min_height = min(min_height, heights[j])
                max_area = max(max_area, min_height * (j - i + 1))
        return max_area

# V1''
# IDEA : STACK
# https://leetcode.com/problems/largest-rectangle-in-histogram/solution/
# Algorithm:
# In this approach, we maintain a stack. Initially, we push a -1 onto the stack to mark the end. We start with the leftmost bar and keep pushing the current bar's index onto the stack until we get two successive numbers in descending order, i.e. until we get heights[i]<heights[i−1]heights[i] < heights[i-1]heights[i]<heights[i−1]. Now, we start popping the numbers from the stack until we hit a number stack[j]stack[j]stack[j] on the stack such that heights[stack[j]]≤heights[i]heights\big[stack[j]\big] \leq heights[i]heights[stack[j]]≤heights[i]. Every time we pop, we find out the area of rectangle formed using the current element as the height of the rectangle and the difference between the the current element's index pointed to in the original array and the element stack[top−1]−1stack[top-1] - 1stack[top−1]−1 as the width i.e. if we pop an element stack[top]stack[top]stack[top] and i is the current index to which we are pointing in the original array, the current area of the rectangle will be considered as: 
# (i−stack[top−1]−1)×heights[stack[top]].(i-stack[top-1]-1) \times heights\big[stack[top]\big].
# (i−stack[top−1]−1)×heights[stack[top]]. Further, if we reach the end of the array, we pop all the elements of the stack and at every pop, this time we use the following equation to find the area: (heights.length−stack[top−1]−1)×heights[stack[top]](heights.length - stack[top-1] - 1) \times heights\big[stack[top]\big](heights.length−stack[top−1]−1)×heights[stack[top]], where stack[top]stack[top]stack[top] refers to the element just popped. Thus, we can get the area of the of the largest rectangle by comparing the new area found everytime.
# time = O(n)
# space = O(n)
class Solution:
    def largestRectangleArea(self, heights: List[int]) -> int:
        stack = [-1]
        max_area = 0
        for i in range(len(heights)):
            while stack[-1] != -1 and heights[stack[-1]] >= heights[i]:
                current_height = heights[stack.pop()]
                current_width = i - stack[-1] - 1
                max_area = max(max_area, current_height * current_width)
            stack.append(i)

        while stack[-1] != -1:
            current_height = heights[stack.pop()]
            current_width = len(heights) - stack[-1] - 1
            max_area = max(max_area, current_height * current_width)
        return max_area

# V1'''
# IDEA : STACK (monotone stack)
# https://leetcode.com/problems/largest-rectangle-in-histogram/solutions/1083629/python-by-monotone-stack-w-comment/
# time = O(n)
# space = O(n)
class Solution:
    def largestRectangleArea(self, heights: List[int]) -> int:
        
        # store x coordination, init as -1
        stack = [ -1 ]
        
        # add zero as dummy tail 
        heights.append( 0 )
        
        # top index for stack
        top = -1
        
        # area of rectangle
        rectangle = 0
        
        # scan each x coordination and y coordination
        for x_coord, y_coord in enumerate(heights):
            
            while heights[ stack[top] ] > y_coord:
            # current height is lower than previous
            # update rectangle area from previous heights
                
                # get height
                h = heights[ stack.pop() ]
                
                # compute width
                w = x_coord - stack[top] -1 
                
                # update maximal area
                rectangle = max(rectangle, h * w)
                
            # push current x coordination into stack
            stack.append( x_coord )
                  
        return rectangle

# V1''''
# IDEA : STACK
# https://leetcode.com/problems/largest-rectangle-in-histogram/solutions/342507/python-different-solutins/
# time = O(n)
# space = O(n)
class Solution:
    def largestRectangleArea(self, heights: List[int]) -> int:
        stack = [0]
        heights.append(0)
        
        res = 0
        for right in range(1,len(heights)):
            while stack and heights[stack[-1]] > heights[right]:
                h = heights[stack.pop()]
                left = -1 if not stack else stack[-1] # because pop operation, left = stack.pop() is not the left boundary
                w = right - left -1
                res = max(res,h*w)
            stack.append(right)
        return res

# V1'''''
# IDEA : STACK
# https://leetcode.com/problems/largest-rectangle-in-histogram/solutions/1023979/python-solution/
# time = O(n)
# space = O(n)
class Solution:
    def largestRectangleArea(self, heights: List[int]) -> int:
        stack = []
        max_area = 0
        l = len(heights)
        for i, n in enumerate(heights):
            if not stack or stack[-1][1] <= n:
                stack.append((i, n))
            else:
                while stack and stack[-1][1] > n:
                    j, m = stack.pop()
                    max_area = max(max_area, (i - j) * m)
                stack.append((j, n))
        
        while stack:
            j, m = stack.pop()
            max_area = max(max_area, (l - j) * m)
        return max_area

# V1''''''
# IDEA : Divide and Conquer Approach
# https://leetcode.com/problems/largest-rectangle-in-histogram/solution/
# time = O(n^2)  # worst-case (unbalanced splits); O(n log n) average
# space = O(n)  # recursion stack, worst-case
class Solution:
    def largestRectangleArea(self, heights: List[int]) -> int:
        def calculateArea(heights: List[int], start: int, end: int) -> int:
            if start > end:
                return 0
            min_index = start
            for i in range(start, end + 1):
                if heights[min_index] > heights[i]:
                    min_index = i
            return max(
                heights[min_index] * (end - start + 1),
                calculateArea(heights, start, min_index - 1),
                calculateArea(heights, min_index + 1, end),
            )

        return calculateArea(heights, 0, len(heights) - 1)

# V1'''''''
# IDEA : Better Divide and Conquer
# https://leetcode.com/problems/largest-rectangle-in-histogram/solution/
# JAVA
# https://leetcode.com/problems/largest-rectangle-in-histogram/discuss/28941/segment-tree-solution-just-another-idea-onlogn-solution
# // Largest Rectangle in Histogram
# // Stack solution, O(NlogN) solution
#
# class SegTreeNode {
# public:
#   int start;
#   int end;
#   int min;
#   SegTreeNode *left;
#   SegTreeNode *right;
#   SegTreeNode(int start, int end) {
#     this->start = start;
#     this->end = end;
#     left = right = NULL;
#   }
# };
#
# class Solution {
# public:
#   int largestRectangleArea(vector<int>& heights) {
#     if (heights.size() == 0) return 0;
#     // first build a segment tree
#     SegTreeNode *root = buildSegmentTree(heights, 0, heights.size() - 1);
#     // next calculate the maximum area recursively
#     return calculateMax(heights, root, 0, heights.size() - 1);
#   }
# 
#   int calculateMax(vector<int>& heights, SegTreeNode* root, int start, int end) {
#     if (start > end) {
#       return -1;
#     }
#     if (start == end) {
#       return heights[start];
#     }
#     int minIndex = query(root, heights, start, end);
#     int leftMax = calculateMax(heights, root, start, minIndex - 1);
#     int rightMax = calculateMax(heights, root, minIndex + 1, end);
#     int minMax = heights[minIndex] * (end - start + 1);
#     return max( max(leftMax, rightMax), minMax );
#   }
#  
#   SegTreeNode *buildSegmentTree(vector<int>& heights, int start, int end) {
#     if (start > end) return NULL;
#     SegTreeNode *root = new SegTreeNode(start, end);
#     if (start == end) {
#         root->min = start;
#       return root;
#     } else {
#       int middle = (start + end) / 2;
#       root->left = buildSegmentTree(heights, start, middle);
#       root->right = buildSegmentTree(heights, middle + 1, end);
#       root->min = heights[root->left->min] < heights[root->right->min] ? root->left->min : root->right->min;
#       return root;
#     }
#   }
#  
#   int query(SegTreeNode *root, vector<int>& heights, int start, int end) {
#     if (root == NULL || end < root->start || start > root->end) return -1;
#     if (start <= root->start && end >= root->end) {
#       return root->min;
#     }
#     int leftMin = query(root->left, heights, start, end);
#     int rightMin = query(root->right, heights, start, end);
#     if (leftMin == -1) return rightMin;
#     if (rightMin == -1) return leftMin;
#     return heights[leftMin] < heights[rightMin] ? leftMin : rightMin;
#   }
# };

# V2
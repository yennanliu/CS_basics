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

# V1
# IDEA : BRURE FORCE
# https://leetcode.com/problems/largest-rectangle-in-histogram/solution/
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

# V1'''
# IDEA : STACK
# https://leetcode.com/problems/largest-rectangle-in-histogram/solution/
# Algorithm:
# In this approach, we maintain a stack. Initially, we push a -1 onto the stack to mark the end. We start with the leftmost bar and keep pushing the current bar's index onto the stack until we get two successive numbers in descending order, i.e. until we get heights[i]<heights[i−1]heights[i] < heights[i-1]heights[i]<heights[i−1]. Now, we start popping the numbers from the stack until we hit a number stack[j]stack[j]stack[j] on the stack such that heights[stack[j]]≤heights[i]heights\big[stack[j]\big] \leq heights[i]heights[stack[j]]≤heights[i]. Every time we pop, we find out the area of rectangle formed using the current element as the height of the rectangle and the difference between the the current element's index pointed to in the original array and the element stack[top−1]−1stack[top-1] - 1stack[top−1]−1 as the width i.e. if we pop an element stack[top]stack[top]stack[top] and i is the current index to which we are pointing in the original array, the current area of the rectangle will be considered as: 
# (i−stack[top−1]−1)×heights[stack[top]].(i-stack[top-1]-1) \times heights\big[stack[top]\big].
# (i−stack[top−1]−1)×heights[stack[top]]. Further, if we reach the end of the array, we pop all the elements of the stack and at every pop, this time we use the following equation to find the area: (heights.length−stack[top−1]−1)×heights[stack[top]](heights.length - stack[top-1] - 1) \times heights\big[stack[top]\big](heights.length−stack[top−1]−1)×heights[stack[top]], where stack[top]stack[top]stack[top] refers to the element just popped. Thus, we can get the area of the of the largest rectangle by comparing the new area found everytime.
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

# V1''
# IDEA : STACK (monotone stack)
# https://leetcode.com/problems/largest-rectangle-in-histogram/solutions/1083629/python-by-monotone-stack-w-comment/
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

# V1'''
# IDEA : STACK
# https://leetcode.com/problems/largest-rectangle-in-histogram/solutions/342507/python-different-solutins/
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

# V1'''''''
# IDEA : Divide and Conquer Approach
# https://leetcode.com/problems/largest-rectangle-in-histogram/solution/
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

# V1
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
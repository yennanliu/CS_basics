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

# V1
# IDEA : BETTER BRURE FORCE
# https://leetcode.com/problems/largest-rectangle-in-histogram/solution/
class Solution:
    def largestRectangleArea(self, heights: List[int]) -> int:
        max_area = 0
        for i in range(len(heights)):
            min_height = inf
            for j in range(i, len(heights)):
                min_height = min(min_height, heights[j])
                max_area = max(max_area, min_height * (j - i + 1))
        return max_area

# V1
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

# V1
# IDEA : STACK
# https://leetcode.com/problems/largest-rectangle-in-histogram/solution/
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

# V2
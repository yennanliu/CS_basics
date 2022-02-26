"""

42. Trapping Rain Water
Hard

Given n non-negative integers representing an elevation map where the width of each bar is 1, compute how much water it can trap after raining.

 

Example 1:


Input: height = [0,1,0,2,1,0,1,3,2,1,2,1]
Output: 6
Explanation: The above elevation map (black section) is represented by array [0,1,0,2,1,0,1,3,2,1,2,1]. In this case, 6 units of rain water (blue section) are being trapped.
Example 2:

Input: height = [4,2,0,3,2,5]
Output: 9
 

Constraints:

n == height.length
1 <= n <= 2 * 104
0 <= height[i] <= 105

"""

# V0
# IDEA : 2 scans
class Solution:
    def trap(self, height):
 
        if not height:
            return 0

        n, res = len(height), 0
        left_max, right_max = [0] * n, [9] * n

        left_max[0] = height[0]

        # scan from left -> right, find HIGHEST "wall" on the LEFT of each idx
        for i in range(1, n):
            left_max[i] = max(height[i], left_max[i-1])

        right_max[n-1] = height[n-1]

        # scan from right -> left, find HIGHEST "wall" on the RIGHT of each idx
        for i in range(n-2, -1, -1):
            right_max[i] = max(height[i], right_max[i+1])

        # scan each idx, use current shortest "wall" within its left, right as length, and use height[i] as height
        for i in range(1, n-1):
            res += min(left_max[i], right_max[i]) - height[i]

        return res

# V0'
# IDEA : TWO POINTERS 
# IDEA : CORE
#     -> step 1) use left_max, right_mex : record "highest" "wall" in left, right handside at current idx
#     -> step 2) 
#                case 2-1) if height[left] < height[right] : 
#                   -> all left passed idx's height is LOWER than height[right]
#                   -> so the "short" wall MUST on left
#                   -> and since we record left_max, so we can get trap amount based on left_max, height[left]
#                
#                case 2-2) if height[left] > height[right]
#                   -> .... (similar as above)
class Solution:
    def trap(self, height):
 
        if not height:
            return 0

        left_max = right_max = res = 0
        left, right = 0, len(height) - 1
 
        while left < right:
            if height[left] < height[right]:  # left pointer op
                if height[left] < left_max:
                    res += left_max - height[left]
                else:
                    left_max = height[left]
                left += 1  # move left pointer 
            else:
                if height[right] < right_max:  # right pointer op
                    res += right_max - height[right]
                else:
                    right_max = height[right]
                right -= 1  # move right pointer 
        return res

# V1
# IDEA : 2 scans
# https://blog.csdn.net/XX_123_1_RJ/article/details/81048041
class Solution:
    def trap(self, height):
 
        if not height:
            return 0

        n, res = len(height), 0
        left_max, right_max = [0] * n, [9] * n

        left_max[0] = height[0]

        # scan from left -> right, find HIGHEST "wall" on the LEFT of each idx
        for i in range(1, n):
            left_max[i] = max(height[i], left_max[i-1])

        right_max[n-1] = height[n-1]

        # scan from right -> left, find HIGHEST "wall" on the RIGHT of each idx
        for i in range(n-2, -1, -1):
            right_max[i] = max(height[i], right_max[i+1])

        # scan each idx, use current shortest "wall" within its left, right as length, and use height[i] as height
        for i in range(1, n-1):
            res += min(left_max[i], right_max[i]) - height[i]

        return res

# V1'
# IDEA : TWO POINTERS 
# https://blog.csdn.net/XX_123_1_RJ/article/details/81048041
# IDEA : CORE
#     -> step 1) use left_max, right_mex : record "highest" "wall" in left, right handside at current idx
#     -> step 2) 
#                case 2-1) if height[left] < height[right] : 
#                   -> all left passed idx's height is LOWER than height[right]
#                   -> so the "short" wall MUST on left
#                   -> and since we record left_max, so we can get trap amount based on left_max, height[left]
#                
#                case 2-2) if height[left] > height[right]
#                   -> .... (similar as above)
class Solution:
    def trap(self, height):
 
        if not height:
            return 0

        left_max = right_max = res = 0
        left, right = 0, len(height) - 1
 
        while left < right:
            if height[left] < height[right]:  # left pointer op
                if height[left] < left_max:
                    res += left_max - height[left]
                else:
                    left_max = height[left]
                left += 1  # move left pointer 
            else:
                if height[right] < right_max:  # right pointer op
                    res += right_max - height[right]
                else:
                    right_max = height[right]
                right -= 1  # move right pointer 
        return res

### Test case : dev 

# V1'
# https://blog.csdn.net/XX_123_1_RJ/article/details/81048041
# IDEA : BRUTE FORCE 
class Solution:
    def trap(self, height):
        if not height: return 0
        n, res = len(height), 0
        left_max, right_max = [0] * n, [0] * n
 
        left_max[0] = height[0]
        for i in range(1, n):  # scan from left to right, get the highest level at every step 
            left_max[i] = max(height[i], left_max[i - 1])
 
        right_max[n - 1] = height[n - 1]
        for i in range(n-2, -1, -1):  # scan from right to left, find the position and the highest side (right) 
            right_max[i] = max(height[i], right_max[i + 1])
 
        for i in range(1, n-1):  # scan every position, use the current place and lowest level (left or right) 
            res += min(left_max[i], right_max[i]) - height[i]
        return res

# V1''
# https://blog.csdn.net/jiangjiang_jian/article/details/81135879
class Solution:
    # @param A, a list of integers
    # @return an integer
    def trap(self, A):
        leftmosthigh = [0 for i in range(len(A))]
        leftmax = 0
        for i in range(len(A)):
            if A[i] > leftmax: leftmax = A[i]
            leftmosthigh[i] = leftmax
        sum = 0
        rightmax = 0
        for i in reversed(range(len(A))):
            if A[i] > rightmax: rightmax = A[i]
            if min(rightmax, leftmosthigh[i]) > A[i]:
                sum += min(rightmax, leftmosthigh[i]) - A[i]
        return sum

# V1'''
# https://www.cnblogs.com/zuoyuan/p/3781453.html
class Solution:
    # @param A, a list of integers
    # @return an integer
    def trap(self, A):
        leftmosthigh = [0 for i in range(len(A))]
        leftmax = 0
        for i in range(len(A)):
            if A[i] > leftmax: leftmax = A[i]
            leftmosthigh[i] = leftmax
        sum = 0
        rightmax = 0
        for i in reversed(range(len(A))):
            if A[i] > rightmax: rightmax = A[i]
            if min(rightmax, leftmosthigh[i]) > A[i]:
                sum += min(rightmax, leftmosthigh[i]) - A[i]
        return sum

# V1
# IDEA : BRUTE FORCE
# https://leetcode.com/problems/trapping-rain-water/solution/

# V1
# IDEA : DP
# https://leetcode.com/problems/trapping-rain-water/solution/
# C++
# int trap(vector<int>& height)
# {
#     if(height.empty())
#         return 0;
#     int ans = 0;
#     int size = height.size();
#     vector<int> left_max(size), right_max(size);
#     left_max[0] = height[0];
#     for (int i = 1; i < size; i++) {
#         left_max[i] = max(height[i], left_max[i - 1]);
#     }
#     right_max[size - 1] = height[size - 1];
#     for (int i = size - 2; i >= 0; i--) {
#         right_max[i] = max(height[i], right_max[i + 1]);
#     }
#     for (int i = 1; i < size - 1; i++) {
#         ans += min(left_max[i], right_max[i]) - height[i];
#     }
#     return ans;
# }

# V2 
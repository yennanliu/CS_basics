# V0

# V1
# https://blog.csdn.net/XX_123_1_RJ/article/details/81048041
# IDEA : TWO POINTERS 
class Solution:
    def trap(self, height):
 
        if not height: return 0
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

# V2 
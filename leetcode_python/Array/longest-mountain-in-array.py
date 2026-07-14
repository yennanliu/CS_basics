"""
845. Longest Mountain in Array
Solved
Medium
Topics
premium lock icon
Companies
You may recall that an array arr is a mountain array if and only if:

arr.length >= 3
There exists some index i (0-indexed) with 0 < i < arr.length - 1 such that:
arr[0] < arr[1] < ... < arr[i - 1] < arr[i]
arr[i] > arr[i + 1] > ... > arr[arr.length - 1]
Given an integer array arr, return the length of the longest subarray, which is a mountain. Return 0 if there is no mountain subarray.

 

Example 1:

Input: arr = [2,1,4,7,3,2,5]
Output: 5
Explanation: The largest mountain is [1,4,7,3,2] which has length 5.
Example 2:

Input: arr = [2,2,2]
Output: 0
Explanation: There is no mountain.
 

Constraints:

1 <= arr.length <= 104
0 <= arr[i] <= 104
 

Follow up:

Can you solve it using only one pass?
Can you solve it in O(1) space?
 


"""

# V0
# IDEA: 2 POINTERS + EXPAND (GPT)
class Solution(object):
    def longestMountain(self, arr):
        max_len = 0
        n = len(arr)

        if n < 3:
            return 0

        for i in range(1, n - 1):
            # Check if arr[i] is a peak
            if arr[i - 1] < arr[i] > arr[i + 1]:
                left = i - 1
                right = i + 1

                # Expand to the left
                while left > 0 and arr[left] > arr[left - 1]:
                    left -= 1

                # Expand to the right
                while right < n - 1 and arr[right] > arr[right + 1]:
                    right += 1

                max_len = max(max_len, right - left + 1)

        return max_len


# V0-0-1
# IDEA: Single pass (two pointers) (GPT)
class Solution(object):
    def longestMountain(self, arr):
        n = len(arr)
        ans = 0
        i = 1

        while i < n - 1:

            # Is arr[i] a peak?
            if arr[i - 1] < arr[i] > arr[i + 1]:

                left = i - 1
                while left > 0 and arr[left - 1] < arr[left]:
                    left -= 1

                right = i + 1
                while right < n - 1 and arr[right] > arr[right + 1]:
                    right += 1

                ans = max(ans, right - left + 1)

                """
                NOTE !!!

                below optimization
                """
                # Skip the processed mountain
                i = right
            else:
                i += 1

        return ans


# V0-1
# IDEA: 2 POINTERS + EXPAND (GEMINI)
class Solution(object):
    def longestMountain(self, arr):
        """
        :type arr: List[int]
        :rtype: int
        """
        max_len = 0
        n = len(arr)

        # We can't have a peak at index 0 or n-1, so we loop from 1 to n-2
        for i in range(1, n - 1):
            
            # 1. Identify if the current element is a valid peak
            if arr[i - 1] < arr[i] and arr[i] > arr[i + 1]:
                left = i - 1
                right = i + 1

                # 2. Expand down the left side of the mountain
                while left > 0 and arr[left - 1] < arr[left]:
                    left -= 1

                # 3. Expand down the right side of the mountain
                while right < n - 1 and arr[right + 1] < arr[right]:
                    right += 1

                # 4. Update max_len only when we have processed a valid mountain
                max_len = max(max_len, right - left + 1)

        return max_len


# V0-2
# IDEA: Two arrays (up[] and down[]) (GPT)
"""

The idea is:

up[i] = length of the increasing run ending at i.
down[i] = length of the decreasing run starting at i.

A valid mountain peak at i must satisfy:

up[i] > 0
down[i] > 0

Then the mountain length is

up[i] + down[i] + 1

"""
class Solution(object):
    def longestMountain(self, arr):
        n = len(arr)
        if n < 3:
            return 0

        # up[i] = length of the increasing run ending at i.
        up = [0] * n
        
        # down[i] = length of the decreasing run starting at i.
        down = [0] * n

        # Increasing lengths
        for i in range(1, n):
            if arr[i] > arr[i - 1]:
                up[i] = up[i - 1] + 1

        # Decreasing lengths
        for i in range(n - 2, -1, -1):
            if arr[i] > arr[i + 1]:
                down[i] = down[i + 1] + 1

        ans = 0
        for i in range(n):
            if up[i] and down[i]:
                ans = max(ans, up[i] + down[i] + 1)

        return ans


# V1 
# https://blog.csdn.net/fuxuemingzhu/article/details/83422260
# time = O(n)
# space = O(n)
class Solution(object):
    def longestMountain(self, A):
        """
        :type A: List[int]
        :rtype: int
        """
        N = len(A)
        inc = [1] * N
        dec = [1] * N
        for i in range(1, N):
            if A[i] - A[i - 1] > 0:
                inc[i] = inc[i - 1] + 1
        for i in range(N - 2, -1, -1):
            if A[i] - A[i + 1] > 0:
                dec[i] = dec[i + 1] + 1
        res = 0
        for i in range(1, N - 1):
            if inc[i] != 1 and dec[i] != 1:
                res = max(res, inc[i] + dec[i] - 1)
        return res

# V1'
# http://bookshadow.com/weblog/2018/06/03/leetcode-longest-mountain-in-array/
# time = O(n)
# space = O(n)
class Solution(object):
    def longestMountain(self, A):
        """
        :type A: List[int]
        :rtype: int
        """
        SA = len(A)
        left, right = [0] * SA, [0] * SA
        for x in range(1, SA):
            if A[x] > A[x - 1]:
                left[x] = left[x - 1] + 1
        ans = 0
        for x in range(SA - 2, -1, -1):
            if A[x] > A[x + 1]:
                right[x] = right[x + 1] + 1
            if left[x] and right[x]:
                ans = max(ans, left[x] + right[x] + 1)
        return ans
        
# V2
# time = O(n)
# space = O(1)
class Solution(object):
    def longestMountain(self, A):
        """
        :type A: List[int]
        :rtype: int
        """
        result, up_len, down_len = 0, 0, 0
        for i in range(1, len(A)):
            if (down_len and A[i-1] < A[i]) or A[i-1] == A[i]:
                up_len, down_len = 0, 0
            up_len += A[i-1] < A[i]
            down_len += A[i-1] > A[i]
            if up_len and down_len:
                result = max(result, up_len+down_len+1)
        return result
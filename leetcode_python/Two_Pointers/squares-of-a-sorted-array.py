"""

977. Squares of a Sorted Array
Easy

Given an integer array nums sorted in non-decreasing order, return an array of the squares of each number sorted in non-decreasing order.

Example 1:

Input: nums = [-4,-1,0,3,10]
Output: [0,1,9,16,100]
Explanation: After squaring, the array becomes [16,1,0,9,100].
After sorting, it becomes [0,1,9,16,100].

Example 2:

Input: nums = [-7,-3,2,3,11]
Output: [4,9,9,49,121]

Constraints:

1 <= nums.length <= 10^4
-10^4 <= nums[i] <= 10^4
nums is sorted in non-decreasing order.

Follow up: Squaring each element and sorting the new array is very trivial, could you find an O(n) solution using a different approach?

"""

# V0

# V1
# time = O(n log n)
# space = O(n)
class Solution(object):
    def sortedSquares(self, A):
        output = [ x**2 for x in A ]
        output.sort()
        return output


# V2
# time = O(n)
# space = O(n)
class Solution(object):
    def sortedSquares(self, A):
        """
        :type A: List[int]
        :rtype: List[int]
        """
        right = 0
        while right < len(A) and A[right] < 0:
            right += 1
        left = right-1

        result = []
        while 0 <= left and right < len(A):
            if A[left]**2 < A[right]**2:
                result.append(A[left]**2)
                left -= 1
            else:
                result.append(A[right]**2)
                right += 1

        while left >= 0:
            result.append(A[left]**2)
            left -= 1
        while right < len(A):
            result.append(A[right]**2)
            right += 1

        return result


# Given a sorted array of integers nums and integer values a, b and c. Apply a function of the form f(x) = ax2 + bx + c to each element x in the array.

# The returned array must be in sorted order.

# Expected time complexity: O(n)

# Example:

# nums = [-4, -2, 2, 4], a = 1, b = 3, c = 5,

# Result: [3, 9, 15, 33]

# nums = [-4, -2, 2, 4], a = -1, b = 3, c = 5

# Result: [-23, -5, 1, 7]



# V1:DEV 






# V2 
# Time:  O(n)
# Space: O(1)

class Solution(object):
    def sortTransformedArray(self, nums, a, b, c):
        """
        :type nums: List[int]
        :type a: int
        :type b: int
        :type c: int
        :rtype: List[int]
        """
        f = lambda x, a, b, c : a * x * x + b * x + c

        result = []
        if not nums:
            return result

        left, right = 0, len(nums) - 1
        d = -1 if a > 0 else 1
        while left <= right:
            if d * f(nums[left], a, b, c) < d * f(nums[right], a, b, c):
                result.append(f(nums[left], a, b, c))
                left += 1
            else:
                result.append(f(nums[right], a, b, c))
                right -= 1

        return result[::d]

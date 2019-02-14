


# Given an array of n integers nums and a target, find the number of index tripletsi, j, k with 0 <= i < j < k < n that satisfy the conditionnums[i] + nums[j] + nums[k] < target.

# For example, given nums = [-2, 0, 1, 3], and target = 2.

# Return 2. Because there are two triplets which sums are less than 2:

# [-2, 0, 1]
# [-2, 0, 3]
# Follow up:
# Could you solve it in O(n2) runtime?



# V1 : dev 



# V2
# https://github.com/yilinanyu/Leetcode-with-Python/blob/master/3Sum%20Smaller.py 
class Solution(object):
    def threeSumSmaller(self, nums, target):
        """
        :type nums: List[int]
        :type target: int
        :rtype: int
        """
        # After sorting, if i, j, k is a valid triple, then i, j-1, k, ..., i, i+1, k are also valid triples. No need to count them one by one.
        # Time:  O(n^2)
        # Space: O(1)
        nums.sort()
        count = 0
        for k in range(len(nums)):
            i, j = 0, k - 1
            while i < j:
                if nums[i] + nums[j] + nums[k] < target:
                    count += j - i
                    i += 1
                else:
                    j -= 1
        return count



# V3 
# Time:  O(n^2)
# Space: O(1)

class Solution(object):
    # @param {integer[]} nums
    # @param {integer} target
    # @return {integer}
    def threeSumSmaller(self, nums, target):
        nums.sort()
        n = len(nums)

        count, k = 0, 2
        while k < n:
            i, j = 0, k - 1
            while i < j:  # Two Pointers, linear time.
                if nums[i] + nums[j] + nums[k] >= target:
                    j -= 1
                else:
                    count += j - i
                    i += 1
            k += 1

        return count






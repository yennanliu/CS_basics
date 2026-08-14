"""

2149. Rearrange Array Elements by Sign
Medium

You are given a 0-indexed integer array nums of even length consisting of an equal number of positive and negative integers.

You should rearrange the elements of nums such that the modified array follows the given conditions:

Every consecutive pair of integers have opposite signs.
For all integers with the same sign, the order in which they were present in nums is preserved.
The rearranged array begins with a positive integer.

Return the modified array after rearranging the elements to satisfy the aforementioned conditions.


Example 1:

Input: nums = [3,1,-2,-5,2,-4]
Output: [3,-2,1,-5,2,-4]
Explanation:
The positive integers in nums are [3,1,2]. The negative integers are [-2,-5,-4].
The only possible way to rearrange them such that they satisfy all conditions is [3,-2,1,-5,2,-4].
Other ways such as [1,-2,2,-5,3,-4], [3,1,2,-2,-5,-4], [-2,3,-5,1,-4,2] are incorrect because they do not satisfy one or more conditions.

Example 2:

Input: nums = [-1,1]
Output: [1,-1]
Explanation:
1 is the only positive integer and -1 the only negative integer in nums.
So nums is rearranged to [1,-1].


Constraints:

2 <= nums.length <= 2 * 10^5
nums.length is even
1 <= |nums[i]| <= 10^5
nums consists of equal number of positive and negative integers.


"""

# V0
# IDEA : TWO WRITE POINTERS — EVEN SLOTS FOR POSITIVES, ODD FOR NEGATIVES
#
#   the relative order within each sign must be preserved, and the layout is
#   fixed (positive at 0, 2, 4, ... and negative at 1, 3, 5, ...), so a single
#   pass suffices : keep two cursors into the output and drop each value into
#   the next slot of its own sign.
#
#   this avoids materialising two separate lists and interleaving them.
#
# time = O(n), space = O(n) for the output
class Solution(object):
    def rearrangeArray(self, nums):
        res = [0] * len(nums)
        pos, neg = 0, 1
        for x in nums:
            if x > 0:
                res[pos] = x
                pos += 2
            else:
                res[neg] = x
                neg += 2
        return res

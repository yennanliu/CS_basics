"""

2799. Count Complete Subarrays in an Array
Medium

You are given an array nums consisting of positive integers.

We call a subarray of an array complete if the following condition is satisfied:

The number of distinct elements in the subarray is equal to the number of distinct elements in the whole array.

Return the number of complete subarrays.

A subarray is a contiguous non-empty part of an array.


Example 1:

Input: nums = [1,3,1,2,2]
Output: 4
Explanation: The complete subarrays are the following: [1,3,1,2], [1,3,1,2,2], [3,1,2] and [3,1,2,2].

Example 2:

Input: nums = [5,5,5,5]
Output: 10
Explanation: The array consists only of the integer 5, so any subarray is complete. The number of subarrays that we can choose is 10.


Constraints:

1 <= nums.length <= 1000
1 <= nums[i] <= 2000

"""

# V0
# IDEA : SLIDING WINDOW (TWO POINTERS) + COUNTER
#
#   let `total` be the number of distinct values in the whole array. a
#   subarray is complete iff it contains all `total` distinct values.
#
#   key monotonicity : if nums[i..j] is complete, then nums[i..j'] is also
#   complete for every j' >= j (extending right can only add values). so for
#   each left endpoint i there is a smallest right endpoint j making the
#   window complete, and that contributes (n - j) complete subarrays.
#
#   we sweep j to the right and, whenever the window [i..j] holds all
#   distinct values, we add (n - j) and shrink from the left. every index is
#   pushed and popped once -> linear.
#
#   NOTE : delete a key from the counter the moment its count hits 0,
#          otherwise len(cnt) keeps counting values no longer in the window
#          and the window never looks incomplete.
#
#   NOTE : we add (n - j) BEFORE moving i, because at that moment [i..j] is
#          the shortest complete window starting at i.
#
# time = O(n), space = O(n)
class Solution(object):
    def countCompleteSubarrays(self, nums):
        n = len(nums)
        total = len(set(nums))

        res = 0
        cnt = {}
        i = 0
        for j in range(n):
            cnt[nums[j]] = cnt.get(nums[j], 0) + 1
            while len(cnt) == total:
                # [i..j], [i..j+1], ... [i..n-1] are all complete
                res += n - j
                cnt[nums[i]] -= 1
                if cnt[nums[i]] == 0:
                    del cnt[nums[i]]
                i += 1
        return res

"""

3684. Maximize Sum of At Most K Distinct Elements
Easy

You are given a positive integer array nums and an integer k.

Choose at most k elements from nums so that their sum is maximized.
However, the chosen numbers must be distinct.

Return an array containing the chosen numbers in strictly descending order.


Example 1:

Input: nums = [84,93,100,77,90], k = 3
Output: [100,93,90]
Explanation:
The maximum sum is 283, which is attained by choosing 93, 100 and 90. We
rearrange them in strictly descending order as [100, 93, 90].

Example 2:

Input: nums = [84,93,100,77,93], k = 3
Output: [100,93,84]
Explanation:
The maximum sum is 277, which is attained by choosing 84, 93 and 100. We
rearrange them in strictly descending order as [100, 93, 84]. We cannot
choose 93, 100 and 93 because the chosen numbers must be distinct.

Example 3:

Input: nums = [1,1,1,2,2,2], k = 6
Output: [2,1]
Explanation:
The maximum sum is 3, which is attained by choosing 1 and 2. We rearrange
them in strictly descending order as [2, 1].


Constraints:

1 <= nums.length <= 100
1 <= nums[i] <= 10^9
1 <= k <= nums.length

"""

# V0
# IDEA : DEDUPE FIRST, THEN GREEDILY TAKE THE k LARGEST DISTINCT VALUES
#
#   "distinct" means duplicates in nums are worthless -- a value can be used
#   at most once, so the real universe of choices is set(nums). once that is
#   fixed, all values are positive, so taking fewer than min(k, #distinct)
#   of them can never help: adding any unused value strictly increases the
#   sum. the exchange argument then says we should take the largest ones.
#
#   so: dedupe, sort descending, take the first k. "at most k" only binds
#   when there are fewer than k distinct values, in which case we return all
#   of them (example 3: only two distinct values for k = 6).
#
#   the sorted-descending order is exactly the required output order, so no
#   extra rearrangement step is needed.
#
# time = O(n log n), space = O(n)
class Solution(object):
    def maxKDistinct(self, nums, k):
        return sorted(set(nums), reverse=True)[:k]

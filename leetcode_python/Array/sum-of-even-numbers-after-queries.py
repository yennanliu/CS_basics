"""

985. Sum of Even Numbers After Queries
Medium

You are given an integer array nums and an array queries where queries[i] = [val_i, index_i].

For each query i, first, apply nums[index_i] = nums[index_i] + val_i, then print the sum of the even values of nums.

Return an integer array answer where answer[i] is the answer to the ith query.

Example 1:

Input: nums = [1,2,3,4], queries = [[1,0],[-3,1],[-4,0],[2,3]]
Output: [8,6,2,4]
Explanation: At the beginning, the array is [1,2,3,4].
After adding 1 to nums[0], the array is [2,2,3,4], and the sum of even values is 2 + 2 + 4 = 8.
After adding -3 to nums[1], the array is [2,-1,3,4], and the sum of even values is 2 + 4 = 6.
After adding -4 to nums[0], the array is [-2,-1,3,4], and the sum of even values is -2 + 4 = 2.
After adding 2 to nums[3], the array is [-2,-1,3,6], and the sum of even values is -2 + 6 = 4.

Example 2:

Input: nums = [1], queries = [[4,0]]
Output: [0]

Constraints:

1 <= nums.length <= 10^4
-10^4 <= nums[i] <= 10^4
1 <= queries.length <= 10^4
-10^4 <= val_i <= 10^4
0 <= index_i < nums.length

"""

# V0
# IDEA : RUNNING SUM (only the touched element can change parity)
#
#  Keep `even_sum` = sum of all even values. For each query only ONE index
#  changes, so:
#     1) if the old value was even, remove it from even_sum
#     2) apply the delta
#     3) if the new value is even, add it back
#
#  NOTE: in Python `x % 2 == 0` is a correct evenness test for negatives too
#        (e.g. -2 % 2 == 0), which matters since values can go negative.
#
# time = O(n + m), n = len(nums), m = len(queries)
# space = O(1) excluding the output list
class Solution(object):
    def sumEvenAfterQueries(self, nums, queries):
        even_sum = sum(x for x in nums if x % 2 == 0)
        res = []

        for val, idx in queries:
            if nums[idx] % 2 == 0:
                even_sum -= nums[idx]
            nums[idx] += val
            if nums[idx] % 2 == 0:
                even_sum += nums[idx]
            res.append(even_sum)

        return res

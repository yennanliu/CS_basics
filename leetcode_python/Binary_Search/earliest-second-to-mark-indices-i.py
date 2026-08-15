"""

3048. Earliest Second to Mark Indices I
Medium

You are given two 1-indexed integer arrays, nums and, changeIndices, having lengths n and m, respectively.

Initially, all indices in nums are unmarked. Your task is to mark all indices in nums.

In each second, s, in order from 1 to m (inclusive), you can perform one of the following operations:

Choose an index i in the range [1, n] and decrement nums[i] by 1.
If nums[changeIndices[s]] is equal to 0, mark the index changeIndices[s].
Do nothing.

Return an integer denoting the earliest second in the range [1, m] when all indices in nums can be marked by choosing operations optimally, or -1 if it is impossible.


Example 1:

Input: nums = [2,2,0], changeIndices = [2,2,2,2,3,2,2,1]
Output: 8
Explanation: In this example, we have 8 seconds. The following operations can be performed to mark all indices:
Second 1: Choose index 1 and decrement nums[1] by one. nums becomes [1,2,0].
Second 2: Choose index 1 and decrement nums[1] by one. nums becomes [0,2,0].
Second 3: Choose index 2 and decrement nums[2] by one. nums becomes [0,1,0].
Second 4: Choose index 2 and decrement nums[2] by one. nums becomes [0,0,0].
Second 5: Mark the index changeIndices[5], which is marking index 3, since nums[3] is equal to 0.
Second 6: Mark the index changeIndices[6], which is marking index 2, since nums[2] is equal to 0.
Second 7: Do nothing.
Second 8: Mark the index changeIndices[8], which is marking index 1, since nums[1] is equal to 0.
Now all indices have been marked.
It can be shown that it is not possible to mark all indices earlier than the 8th second.
Hence, the answer is 8.

Example 2:

Input: nums = [1,3], changeIndices = [1,1,1,2,1,1,1]
Output: 6
Explanation: In this example, we have 7 seconds. The following operations can be performed to mark all indices:
Second 1: Choose index 2 and decrement nums[2] by one. nums becomes [1,2].
Second 2: Choose index 2 and decrement nums[2] by one. nums becomes [1,1].
Second 3: Choose index 2 and decrement nums[2] by one. nums becomes [1,0].
Second 4: Mark the index changeIndices[4], which is marking index 2, since nums[2] is equal to 0.
Second 5: Choose index 1 and decrement nums[1] by one. nums becomes [0,0].
Second 6: Mark the index changeIndices[6], which is marking index 1, since nums[1] is equal to 0.
Now all indices have been marked.
It can be shown that it is not possible to mark all indices earlier than the 6th second.
Hence, the answer is 6.

Example 3:

Input: nums = [0,1], changeIndices = [2,2,2]
Output: -1
Explanation: In this example, it is impossible to mark all indices because index 1 isn't in changeIndices.
Hence, the answer is -1.


Constraints:

1 <= n == nums.length <= 2000
0 <= nums[i] <= 10^9
1 <= m == changeIndices.length <= 2000
1 <= changeIndices[i] <= n

"""

# V0
# IDEA : BINARY SEARCH THE DEADLINE, GREEDY CHECK WITH "MARK AT THE LAST CHANCE"
#
#   more seconds can never hurt, so the set of feasible deadlines is upward
#   closed and binary search applies : find the smallest s that works.
#
#   for a fixed s, an index can only be marked at a second where it appears
#   in changeIndices, and marking it as LATE as possible leaves the most room
#   for decrements. so within the prefix 1..s use each index's LAST
#   occurrence as its marking second.
#
#   then sweep t = 1..s carrying a pool of spare seconds :
#       t is nobody's last occurrence -> the second is spare, pool += 1
#       t is index i's last occurrence -> spend nums[i] spare seconds on
#                                         decrementing it, then mark it here
#   if the pool ever runs short, s is infeasible; if every index gets marked,
#   it works.
#
# time = O(m log m), space = O(n + m)
class Solution(object):
    def earliestSecondToMarkIndices(self, nums, changeIndices):
        n, m = len(nums), len(changeIndices)

        def feasible(s):
            last = {}
            for t in range(s):
                last[changeIndices[t]] = t          # 0-based last occurrence
            if len(last) < n:
                return False                        # some index never appears
            deadline = {t: i for i, t in last.items()}

            pool = 0
            marked = 0
            for t in range(s):
                if t in deadline:
                    need = nums[deadline[t] - 1]    # changeIndices is 1-indexed
                    if pool < need:
                        return False
                    pool -= need                    # decrements
                    marked += 1                     # this second does the mark
                else:
                    pool += 1
            return marked == n

        lo, hi = 1, m
        res = -1
        while lo <= hi:
            mid = (lo + hi) // 2
            if feasible(mid):
                res = mid
                hi = mid - 1
            else:
                lo = mid + 1
        return res

"""

1526. Minimum Number of Increments on Subarrays to Form a Target Array
Hard

You are given an integer array target. You have an integer array initial of the same size as target with all elements initially zeros.

In one operation you can choose any subarray from initial and increment each value by one.

Return the minimum number of operations to form a target array from initial.

The test cases are generated so that the answer fits in a 32-bit integer.


Example 1:

Input: target = [1,2,3,2,1]
Output: 3
Explanation: We need at least 3 operations to form the target array from the initial array.
[0,0,0,0,0] increment 1 from index 0 to 4 (inclusive).
[1,1,1,1,1] increment 1 from index 1 to 3 (inclusive).
[1,2,2,2,1] increment 1 at index 2.
[1,2,3,2,1] target array is formed.

Example 2:

Input: target = [3,1,1,2]
Output: 4
Explanation: [0,0,0,0] -> [1,1,1,1] -> [1,1,1,2] -> [2,1,1,2] -> [3,1,1,2]

Example 3:

Input: target = [3,1,5,4,2]
Output: 7
Explanation: [0,0,0,0,0] -> [1,1,1,1,1] -> [2,1,1,1,1] -> [3,1,1,1,1] -> [3,1,2,2,2] -> [3,1,3,3,2] -> [3,1,4,4,2] -> [3,1,5,4,2].


Constraints:

1 <= target.length <= 10^5
1 <= target[i] <= 10^5
The input is generated such that the answer fits inside a 32 bit integer.

"""

# V0
# IDEA : GREEDY / SUM OF POSITIVE RISES
#
#   think of each operation as a "+1 bar" laid over a contiguous range.
#   a NEW bar has to START at index i exactly when target[i] > target[i-1] :
#   the bars covering i-1 can simply be extended to cover i for free up to
#   height target[i-1], and the extra (target[i] - target[i-1]) height must
#   be brand-new operations.
#
#     answer = target[0] + sum over i>0 of max(0, target[i] - target[i-1])
#
#   NOTE : drops cost nothing — we just end some bars there.
#
# time = O(n), space = O(1)
class Solution(object):
    def minNumberOperations(self, target):
        res = target[0]
        for i in range(1, len(target)):
            if target[i] > target[i - 1]:
                res += target[i] - target[i - 1]
        return res

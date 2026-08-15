"""

2145. Count the Hidden Sequences
Medium

You are given a 0-indexed array of n integers differences, which describes the differences between each pair of consecutive integers of a hidden sequence of length (n + 1). More formally, call the hidden sequence hidden, then we have that differences[i] = hidden[i + 1] - hidden[i].

You are further given two integers lower and upper that describe the inclusive range of values [lower, upper] that the hidden sequence can contain.

For example, given differences = [1, -3, 4], lower = 1, upper = 6, the hidden sequence is a sequence of length 4 whose elements are in between 1 and 6 (inclusive).
[3, 4, 1, 5] and [4, 5, 2, 6] are possible hidden sequences.
[5, 6, 3, 7] is not possible since it contains an element greater than 6.
[1, 2, 3, 4] is not possible since the differences are not correct.

Return the number of possible hidden sequences there are. If there are no possible sequences, return 0.


Example 1:

Input: differences = [1,-3,4], lower = 1, upper = 6
Output: 2
Explanation: The possible hidden sequences are:
- [3, 4, 1, 5]
- [4, 5, 2, 6]
Thus, we return 2.

Example 2:

Input: differences = [3,-4,5,1,-2], lower = -4, upper = 5
Output: 4
Explanation: The possible hidden sequences are:
- [-3, 0, -4, 1, 2, 0]
- [-2, 1, -3, 2, 3, 1]
- [-1, 2, -2, 3, 4, 2]
- [0, 3, -1, 4, 5, 3]
Thus, we return 4.

Example 3:

Input: differences = [4,-7,2], lower = 3, upper = 6
Output: 0
Explanation: There are no possible hidden sequences. Thus, we return 0.


Constraints:

n == differences.length
1 <= n <= 10^5
-10^5 <= differences[i] <= 10^5
-10^5 <= lower <= upper <= 10^5

"""

# V0
# IDEA : THE SHAPE IS FIXED — ONLY THE STARTING VALUE IS FREE
#
#   the differences pin every element relative to the first one :
#       hidden[i] = hidden[0] + prefix[i]
#   so the whole sequence is one rigid shape that slides up and down with
#   hidden[0].
#
#   the shape fits inside [lower, upper] exactly when
#       lower <= hidden[0] + min(prefix)  and  hidden[0] + max(prefix) <= upper
#   which leaves a contiguous window of starting values whose size is
#
#       (upper - lower) - (max(prefix) - min(prefix)) + 1
#
#   clamped at 0 when the shape is already taller than the allowed range.
#
#   NOTE : prefix must include the leading 0 (hidden[0] itself).
#
# time = O(n), space = O(1)
class Solution(object):
    def numberOfArrays(self, differences, lower, upper):
        cur = 0
        lo = hi = 0                     # min / max of the running prefix
        for d in differences:
            cur += d
            lo = min(lo, cur)
            hi = max(hi, cur)
        return max(0, (upper - lower) - (hi - lo) + 1)

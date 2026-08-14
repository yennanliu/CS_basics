"""

1399. Count Largest Group
Easy

You are given an integer n.

We need to group the numbers from 1 to n according to the sum of its digits. For example, the numbers 14 and 5 belong to the same group, whereas 13 and 3 belong to different groups.

Return the number of groups that have the largest size, i.e. the maximum number of elements.


Example 1:

Input: n = 13
Output: 4
Explanation: There are 9 groups in total, they are grouped according sum of its digits of numbers from 1 to 13:
[1,10], [2,11], [3,12], [4,13], [5], [6], [7], [8], [9].
There are 4 groups with largest size.

Example 2:

Input: n = 2
Output: 2
Explanation: There are 2 groups [1], [2] of size 1.


Constraints:

1 <= n <= 10^4

"""

# V0
# IDEA : COUNTING BY DIGIT SUM (bucket key = sum of digits)
#
#   n <= 10^4, so a digit sum is at most 9*4 = 36 -> a small counter suffices.
#   count how many numbers land in each bucket, take the max bucket size,
#   then count how many buckets reach it.
#   NOTE : the answer is the NUMBER of largest groups, not their size.
#
# time = O(n * log n), space = O(log n)
from collections import Counter
class Solution(object):
    def countLargestGroup(self, n):
        cnt = Counter()
        for i in range(1, n + 1):
            s, x = 0, i
            while x:
                s += x % 10
                x //= 10
            cnt[s] += 1

        mx = max(cnt.values())
        return sum(1 for v in cnt.values() if v == mx)

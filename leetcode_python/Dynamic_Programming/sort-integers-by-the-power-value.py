"""

1387. Sort Integers by The Power Value
Medium

The power of an integer x is defined as the number of steps needed to transform
x into 1 using the following steps:

if x is even then x = x / 2
if x is odd then x = 3 * x + 1

For example, the power of x = 3 is 7 because 3 needs 7 steps to become 1
(3 --> 10 --> 5 --> 16 --> 8 --> 4 --> 2 --> 1).

Given three integers lo, hi and k. The task is to sort all integers in the
interval [lo, hi] by the power value in ascending order, if two or more integers
have the same power value sort them by ascending order.

Return the kth integer in the range [lo, hi] sorted by the power value.

Notice that for any integer x (lo <= x <= hi) it is guaranteed that x will
transform into 1 using these steps and that the power of x is will fit in a
32-bit signed integer.


Example 1:

Input: lo = 12, hi = 15, k = 2
Output: 13
Explanation: The power of 12 is 9 (12 --> 6 --> 3 --> 10 --> 5 --> 16 --> 8 --> 4 --> 2 --> 1)
The power of 13 is 9
The power of 14 is 17
The power of 15 is 17
The interval sorted by the power value [12,13,14,15]. For k = 2 answer is the
second element which is 13.

Example 2:

Input: lo = 7, hi = 11, k = 4
Output: 7
Explanation: The power array corresponding to the interval [7, 8, 9, 10, 11] is
[16, 3, 19, 6, 14].
The interval sorted by power is [8, 10, 11, 7, 9].
The fourth number in the sorted array is 7.


Constraints:

1 <= lo <= hi <= 1000
1 <= k <= hi - lo + 1

"""

# V0
# IDEA: memoized Collatz "power" (top down DP) + sort by (power, value)
#
#  DP def:
#    - power(x) = 0                    , if x == 1
#    - power(x) = 1 + power(x // 2)    , if x is even
#    - power(x) = 1 + power(3 * x + 1) , if x is odd
#
#  memoising is what makes this a DP problem: the Collatz chains of the
#  numbers in [lo, hi] overlap heavily.
#
#  NOTE !!! python's sort is stable, so sorting range(lo, hi+1) (already in
#           ascending order) by power alone keeps the ascending tie break.
#
# time = O(n log n * M), n = hi - lo + 1, M = max chain length
# space = O(n + M)
class Solution(object):
    def getKth(self, lo, hi, k):
        memo = {1: 0}

        def power(x):
            path = []
            # walk down until we hit a cached value
            while x not in memo:
                path.append(x)
                x = x // 2 if x % 2 == 0 else 3 * x + 1
            steps = memo[x]
            # fill the cache back up on the way out
            for y in reversed(path):
                steps += 1
                memo[y] = steps
            return memo[path[0]] if path else steps

        return sorted(range(lo, hi + 1), key=power)[k - 1]


# V1
# IDEA: plain sort with an explicit (power, value) key, no memo
# time = O(n log n * M)
# space = O(n)
class Solution(object):
    def getKth(self, lo, hi, k):
        def power(x):
            steps = 0
            while x != 1:
                x = x // 2 if x % 2 == 0 else 3 * x + 1
                steps += 1
            return steps

        return sorted(range(lo, hi + 1), key=lambda x: (power(x), x))[k - 1]

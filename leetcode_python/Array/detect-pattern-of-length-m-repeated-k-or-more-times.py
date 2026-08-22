"""

1566. Detect Pattern of Length M Repeated K or More Times
Easy

Given an array of positive integers arr, find a pattern of length m that is repeated k or more times.

A pattern is a subarray (consecutive sub-sequence) that consists of one or more values, repeated multiple times consecutively without overlapping. A pattern is defined by its length and the number of repetitions.

Return true if there exists a pattern of length m that is repeated k or more times, otherwise return false.

Example 1:

Input: arr = [1,2,4,4,4,4], m = 1, k = 3
Output: true
Explanation: The pattern (4) of length 1 is repeated 4 consecutive times. Notice that pattern can be repeated k or more times but not less.

Example 2:

Input: arr = [1,2,1,2,1,1,1,3], m = 2, k = 2
Output: true
Explanation: The pattern (1,2) of length 2 is repeated 2 consecutive times. Another valid pattern (2,1) is also repeated 2 times.

Example 3:

Input: arr = [1,2,1,2,1,3], m = 2, k = 3
Output: false
Explanation: The pattern (1,2) is of length 2 but is repeated only 2 times. There is no pattern of length 2 that is repeated 3 or more times.

Constraints:

2 <= arr.length <= 100
1 <= arr[i] <= 100
1 <= m <= 100
2 <= k <= 100

"""

# V0
# IDEA : RUN COUNTING (a period-m repeat means arr[i] == arr[i + m])
#
#   a pattern of length m repeated k times spans m*k cells and is
#   equivalent to m*(k-1) CONSECUTIVE matches of arr[i] == arr[i + m].
#   so keep a running counter of such matches and reset it on a mismatch.
#
# time = O(n), space = O(1)
class Solution(object):
    def containsPattern(self, arr, m, k):
        need = m * (k - 1)
        run = 0
        for i in range(len(arr) - m):
            if arr[i] == arr[i + m]:
                run += 1
                if run == need:
                    return True
            else:
                run = 0
        return False


# V0-1
# IDEA : BRUTE FORCE SLICE COMPARISON
#
#   try every possible window start i and compare the candidate pattern
#   arr[i:i+m], repeated k times, against the m*k cells that actually sit
#   there. list multiplication builds the expected block for us.
#
# time = O(n * m * k), space = O(m * k)
class Solution(object):
    def containsPattern(self, arr, m, k):
        n = len(arr)
        for i in range(n - m * k + 1):
            if arr[i:i + m] * k == arr[i:i + m * k]:
                return True
        return False


# V0-2
# IDEA : PREFIX SUM OVER THE PERIOD-m MATCH ARRAY
#
#   b[i] = 1 when arr[i] == arr[i + m], i.e. "period m holds at i".
#   the answer is yes iff b contains m*(k-1) CONSECUTIVE ones, so build the
#   prefix sums of b once and then every window of that width is an O(1) test.
#   (V0 does the same count with a running streak; here the counting is
#   decoupled from the scan, which is the shape you need when several
#   different k values must be answered over the same arr.)
#
# time = O(n), space = O(n)
class Solution(object):
    def containsPattern(self, arr, m, k):
        n = len(arr)
        need = m * (k - 1)
        if need == 0:
            return n >= m
        b = [1 if arr[i] == arr[i + m] else 0 for i in range(n - m)]
        if len(b) < need:
            return False
        pre = [0] * (len(b) + 1)
        for i, v in enumerate(b):
            pre[i + 1] = pre[i] + v
        for i in range(len(b) - need + 1):
            if pre[i + need] - pre[i] == need:
                return True
        return False

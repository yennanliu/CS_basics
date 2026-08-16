"""

3663. Find The Least Frequent Digit
Easy

Given an integer n, find the digit that occurs least frequently in its
decimal representation. If multiple digits have the same frequency, choose
the smallest digit.

Return the chosen digit as an integer.

The frequency of a digit x is the number of times it appears in the decimal
representation of n.

Example 1:

Input: n = 1553322
Output: 1
Explanation:
The least frequent digit in n is 1, which appears only once. All other
digits appear twice.

Example 2:

Input: n = 723344511
Output: 2
Explanation:
The least frequent digits in n are 7, 2, and 5; each appears only once.

Constraints:

1 <= n <= 2^31 - 1

"""

# V0
# IDEA : COUNT DIGITS, THEN PICK BY (FREQUENCY, DIGIT) — SKIPPING ABSENT ONES
#
#   only digits that actually occur are candidates, so a digit with count 0
#   must never win. counting into a dict (rather than a size-10 array) makes
#   that automatic.
#
#   the tie-break is "smallest digit", so ordering candidates by the pair
#   (count, digit) and taking the minimum answers both criteria at once.
#
# time = O(log n), space = O(1)
from collections import Counter


class Solution(object):
    def getLeastFrequentDigit(self, n):
        cnt = Counter(str(n))
        return int(min(cnt, key=lambda d: (cnt[d], int(d))))

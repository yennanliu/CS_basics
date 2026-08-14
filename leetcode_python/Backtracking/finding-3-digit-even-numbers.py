"""

2094. Finding 3-Digit Even Numbers
Easy

You are given an integer array digits, where each element is a digit. The array may contain duplicates.

You need to find all the unique integers that follow the given requirements:

The integer consists of the concatenation of three elements from digits in any arbitrary order.
The integer does not have leading zeros.
The integer is even.

For example, if the given digits were [1, 2, 3], integers 132 and 312 follow the requirements.

Return a sorted array of the unique integers.


Example 1:

Input: digits = [2,1,3,0]
Output: [102,120,130,132,210,230,302,310,312,320]
Explanation: All the possible integers that follow the requirements are in the output array.
Notice that there are no odd integers or integers with leading zeros.

Example 2:

Input: digits = [2,2,8,8,2]
Output: [222,228,282,288,822,828,882]
Explanation: The same digit can be used as many times as it appears in digits.
In this example, the digit 8 is used twice each time in 288, 828, and 882.

Example 3:

Input: digits = [3,7,5]
Output: []
Explanation: No even integers can be formed using the given digits.


Constraints:

3 <= digits.length <= 100
0 <= digits[i] <= 9

"""

# V0
# IDEA : ENUMERATE ALL 900 THREE-DIGIT EVEN NUMBERS, CHECK MULTISET SUPPLY
#
#   there are only 450 even numbers in [100, 999], so instead of permuting
#   the input, walk the candidates and ask "does `digits` contain enough of
#   each digit?" — a Counter comparison.
#
#   this makes the "no leading zeros / even / unique / sorted" rules trivial:
#   starting the range at 100 kills leading zeros, stepping by 2 keeps them
#   even, and ascending enumeration yields a sorted unique list for free.
#
# time = O(1) (bounded by 450 candidates), space = O(1)
from collections import Counter


class Solution(object):
    def findEvenNumbers(self, digits):
        have = Counter(digits)
        res = []
        for x in range(100, 1000, 2):
            need = Counter(int(c) for c in str(x))
            if all(have[d] >= c for d, c in need.items()):
                res.append(x)
        return res

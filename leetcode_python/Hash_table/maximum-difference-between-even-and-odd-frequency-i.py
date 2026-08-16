"""

3442. Maximum Difference Between Even and Odd Frequency I
Easy

You are given a string s consisting of lowercase English letters.

Your task is to find the maximum difference diff = freq(a_1) - freq(a_2) between
the frequency of characters a_1 and a_2 in the string such that:

a_1 has an odd frequency in the string.
a_2 has an even frequency in the string.

Return this maximum difference.

Example 1:

Input: s = "aaaaabbc"

Output: 3

Explanation:

The character 'a' has an odd frequency of 5, and 'b' has an even frequency of 2.
The maximum difference is 5 - 2 = 3.

Example 2:

Input: s = "abcabcab"

Output: 1

Explanation:

The character 'a' has an odd frequency of 3, and 'c' has an even frequency of 2.
The maximum difference is 3 - 2 = 1.

Constraints:

3 <= s.length <= 100
s consists only of lowercase English letters.
s contains at least one character with an odd frequency and one with an even
frequency.

"""

# V0
# IDEA : THE TWO SIDES OF THE DIFFERENCE ARE INDEPENDENT
#
#   nothing links a_1 to a_2 except that one has an odd count and the other an
#   even count, so maximising freq(a_1) - freq(a_2) splits into two separate
#   one-variable problems: take the largest odd frequency and the smallest
#   *non-zero* even frequency.
#
#   zero counts have to be excluded from the even side — a letter that never
#   appears technically has an even frequency of 0 but is not a character "in
#   the string".  the constraints promise both kinds exist.
#
# time = O(n), space = O(1)
from collections import Counter


class Solution(object):
    def maxDifference(self, s):
        cnt = Counter(s)
        odd = max(v for v in cnt.values() if v % 2 == 1)
        even = min(v for v in cnt.values() if v % 2 == 0)
        return odd - even

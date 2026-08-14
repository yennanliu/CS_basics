"""

1781. Sum of Beauty of All Substrings
Medium

The beauty of a string is the difference in frequencies between the most frequent and least frequent characters.

For example, the beauty of "abaacc" is 3 - 1 = 2.

Given a string s, return the sum of beauty of all of its substrings.

Example 1:

Input: s = "aabcb"
Output: 5
Explanation: The substrings with non-zero beauty are ["aab","aabc","aabcb","abcb","bcb"], each with beauty equal to 1.

Example 2:

Input: s = "aabcbaa"
Output: 17

Constraints:

1 <= s.length <= 500
s consists of only lowercase English letters.

"""

# V0
# IDEA : GROW EVERY SUBSTRING FROM A FIXED LEFT END, KEEPING A 26-SLOT COUNTER
#
#   for a fixed start i, extend the right end j one character at a time and
#   update a frequency table of 26 letters incrementally - no substring is ever
#   re-scanned. beauty = max(freq) - min(freq over letters that actually appear).
#   NOTE : letters with count 0 must be excluded from the min, otherwise every
#          beauty would collapse to max - 0.
#
# time = O(n^2 * 26), space = O(26)
class Solution(object):
    def beautySum(self, s):
        n = len(s)
        res = 0
        for i in range(n):
            cnt = [0] * 26
            for j in range(i, n):
                cnt[ord(s[j]) - 97] += 1
                mx, mn = 0, n + 1
                for c in cnt:
                    if c:
                        if c > mx:
                            mx = c
                        if c < mn:
                            mn = c
                res += mx - mn
        return res

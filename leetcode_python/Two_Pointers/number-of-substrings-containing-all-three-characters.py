"""

1358. Number of Substrings Containing All Three Characters
Medium

Given a string s consisting only of characters a, b and c.

Return the number of substrings containing at least one occurrence of all these characters a, b and c.


Example 1:

Input: s = "abcabc"
Output: 10
Explanation: The substrings containing at least one occurrence of the characters a, b and c are "abc", "abca", "abcab", "abcabc", "bca", "bcab", "bcabc", "cab", "cabc" and "abc" (again).

Example 2:

Input: s = "aaacb"
Output: 3
Explanation: The substrings containing at least one occurrence of the characters a, b and c are "aaacb", "aacb" and "acb".

Example 3:

Input: s = "abc"
Output: 1


Constraints:

3 <= s.length <= 5 x 10^4
s only consists of a, b or c characters.

"""

# V0
# IDEA : LAST-SEEN INDEX (one pass)
#
#  - fix the RIGHT end at i. A substring s[j..i] is valid iff it still
#    contains an 'a', a 'b' and a 'c'.
#  - let d[c] = last index (<= i) where char c appeared.
#    then every start j in [0, min(d['a'], d['b'], d['c'])] works,
#    which is min(...) + 1 substrings ending at i.
#  - if some char was never seen, its d is -1 -> min + 1 == 0. Nice.
#
# time = O(n)
# space = O(1)
class Solution(object):
    def numberOfSubstrings(self, s):
        d = {"a": -1, "b": -1, "c": -1}
        ans = 0
        for i, c in enumerate(s):
            d[c] = i
            ans += min(d["a"], d["b"], d["c"]) + 1
        return ans


# V1
# IDEA : SLIDING WINDOW (two pointers)
#        shrink from the left while the window [l, r] still has all 3 chars.
#        after shrinking, starts 0..l-1 all give a valid substring ending
#        at r -> add l to the answer.
# time = O(n)
# space = O(1)
from collections import Counter


class Solution2(object):
    def numberOfSubstrings(self, s):
        ans = 0
        l = 0
        cnt = Counter()
        for r, c in enumerate(s):
            cnt[c] += 1
            while cnt["a"] and cnt["b"] and cnt["c"]:
                cnt[s[l]] -= 1
                l += 1
            ans += l
        return ans

"""

2287. Rearrange Characters to Make Target String
Easy

You are given two 0-indexed strings s and target. You can take some letters from s and rearrange them to form new strings.

Return the maximum number of copies of target that can be formed by taking letters from s and rearranging them.


Example 1:

Input: s = "ilovecodingonleetcode", target = "code"
Output: 2
Explanation:
For the first copy of "code", take the letters at indices 4, 5, 6, and 7.
For the second copy of "code", take the letters at indices 17, 18, 19, and 20.
The strings that are formed are "ecod" and "code" which can both be rearranged into "code".
We can make at most two copies of "code", so we return 2.

Example 2:

Input: s = "abcba", target = "abc"
Output: 1
Explanation:
We can make one copy of "abc" by taking the letters at indices 0, 1, and 2.
We can make at most one copy of "abc", so we return 1.
Note that while there is an extra 'a' and 'b' at indices 3 and 4, we cannot rearrange them to form "abc" a second time.

Example 3:

Input: s = "abbaccaddaeea", target = "aaaaa"
Output: 1
Explanation:
We can make one copy of "aaaaa" by taking the letters at indices 0, 3, 6, 9, and 12.
We can make at most one copy of "aaaaa", so we return 1.


Constraints:

1 <= s.length <= 100
1 <= target.length <= 100
s and target consist of lowercase English letters.

"""

# V0
# IDEA : THE BOTTLENECK LETTER DECIDES — TAKE THE MIN RATIO
#
#   forming one copy consumes need[c] of each letter c, so the number of
#   copies is limited by
#       have[c] // need[c]
#   and the answer is the smallest such ratio over the letters of target.
#
#   a letter missing from s gives 0 // need = 0, which correctly yields 0.
#
# time = O(len(s) + len(target)), space = O(26)
from collections import Counter


class Solution(object):
    def rearrangeCharacters(self, s, target):
        have = Counter(s)
        need = Counter(target)
        return min(have[c] // n for c, n in need.items())

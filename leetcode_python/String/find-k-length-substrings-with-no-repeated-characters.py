"""

1100. Find K-Length Substrings With No Repeated Characters
Medium

Given a string s and an integer k, return the number of substrings in s of
length k with no repeated characters.


Example 1:

Input: s = "havefunonleetcode", k = 5
Output: 6
Explanation: There are 6 substrings they are:
'havef','avefu','vefun','efuno','etcod','tcode'.

Example 2:

Input: s = "home", k = 5
Output: 0
Explanation: Notice k can be larger than the length of s.
In this case, it is not possible to find any substring.


Constraints:

1 <= s.length <= 10^4
s consists of lowercase English letters.
1 <= k <= 10^4

"""

# V0
# IDEA: SLIDING WINDOW (fixed size k) + counter
#
#   keep a window of size k, maintain a char counter.
#   the window is valid <-> it holds k DISTINCT chars
#   (i.e. len(counter) == k).
#
# time = O(n)
# space = O(1)
#   at most 26 distinct lowercase chars are tracked
from collections import defaultdict
class Solution(object):
    def numKLenSubstrNoRepeats(self, s, k):
        n = len(s)
        # edge : window bigger than s, or more than 26 distinct chars needed
        if k > n or k > 26:
            return 0

        cnt = defaultdict(int)
        res = 0

        for i, c in enumerate(s):
            cnt[c] += 1

            # NOTE !!! shrink from the left so the window size stays k
            if i >= k:
                left = s[i - k]
                cnt[left] -= 1
                if cnt[left] == 0:
                    del cnt[left]

            if i >= k - 1 and len(cnt) == k:
                res += 1

        return res

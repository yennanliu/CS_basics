"""

1297. Maximum Number of Occurrences of a Substring
Medium

Given a string s, return the maximum number of occurrences of any substring
under the following rules:

The number of unique characters in the substring must be less than or equal
to maxLetters.
The substring size must be between minSize and maxSize inclusive.


Example 1:

Input: s = "aababcaab", maxLetters = 2, minSize = 3, maxSize = 4
Output: 2
Explanation: Substring "aab" has 2 occurrences in the original string.
It satisfies the conditions, 2 unique letters and size 3
(between minSize and maxSize).

Example 2:

Input: s = "aaaa", maxLetters = 1, minSize = 3, maxSize = 3
Output: 2
Explanation: Substring "aaa" occur 2 times in the string. It can overlap.


Constraints:

1 <= s.length <= 10^5
1 <= maxLetters <= 26
1 <= minSize <= maxSize <= min(26, s.length)
s consists of only lowercase English letters.

"""

# V0
# IDEA: SLIDING WINDOW of FIXED size `minSize` + HASH MAP
#
#  -> KEY INSIGHT : maxSize is a RED HERRING.
#     if a longer substring t occurs c times and is valid,
#     then its length-minSize prefix also occurs >= c times
#     and has <= as many unique chars, so it is also valid.
#     -> only substrings of length exactly `minSize` need to be counted.
#
# time = O(n * minSize)
# space = O(n * minSize)
from collections import Counter
class Solution(object):
    def maxFreq(self, s, maxLetters, minSize, maxSize):
        n = len(s)
        if minSize > n:
            return 0

        cnt = Counter()
        res = 0
        for i in range(n - minSize + 1):
            sub = s[i:i + minSize]
            if len(set(sub)) <= maxLetters:
                cnt[sub] += 1
                res = max(res, cnt[sub])
        return res


# V1
# IDEA: TWO POINTERS sliding window, maintain the unique-char count in O(1)
#
#  -> instead of rebuilding set(sub) for every window (O(minSize) each),
#     roll a char-frequency table and a `distinct` counter as the
#     window slides -> the validity check becomes O(1).
#
# time = O(n * minSize), the substring hashing still costs minSize
# space = O(n * minSize)
from collections import Counter, defaultdict
class Solution(object):
    def maxFreq(self, s, maxLetters, minSize, maxSize):
        n = len(s)
        if minSize > n:
            return 0

        freq = defaultdict(int)
        distinct = 0
        cnt = Counter()
        res = 0

        left = 0
        for right in range(n):
            # add s[right]
            if freq[s[right]] == 0:
                distinct += 1
            freq[s[right]] += 1

            # shrink until window size == minSize
            if right - left + 1 > minSize:
                c = s[left]
                freq[c] -= 1
                if freq[c] == 0:
                    distinct -= 1
                left += 1

            if right - left + 1 == minSize and distinct <= maxLetters:
                sub = s[left:right + 1]
                cnt[sub] += 1
                res = max(res, cnt[sub])

        return res

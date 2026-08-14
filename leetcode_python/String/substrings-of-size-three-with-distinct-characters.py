"""

1876. Substrings of Size Three with Distinct Characters
Easy

A string is good if there are no repeated characters.

Given a string s, return the number of good substrings of length three in s.

Note that if there are multiple occurrences of the same substring, every occurrence should be counted.

A substring is a contiguous sequence of characters in a string.


Example 1:

Input: s = "xyzzaz"
Output: 1
Explanation: There are 4 substrings of size 3: "xyz", "yzz", "zza", and "zaz".
The only good substring of length 3 is "xyz".

Example 2:

Input: s = "aababcabc"
Output: 4
Explanation: There are 7 substrings of size 3: "aab", "aba", "bab", "abc", "bca", "cab", and "abc".
The good substrings are "abc", "bca", "cab", and "abc".


Constraints:

1 <= s.length <= 100
s consists of lowercase English letters.

"""

# V0
# IDEA : FIXED-SIZE SLIDING WINDOW (the window is only 3 chars wide)
#
#   a window of length 3 is "good" iff its 3 characters are pairwise
#   different, which is just 3 comparisons - no counter needed.
#
#   slide the window one position at a time over every start index
#   0 .. n-3 and count the good ones.
#
#   NOTE : strings shorter than 3 have no window at all -> answer 0,
#          handled naturally by the empty range.
#
# time = O(n), space = O(1)
class Solution(object):
    def countGoodSubstrings(self, s):
        res = 0
        for i in range(len(s) - 2):
            a, b, c = s[i], s[i + 1], s[i + 2]
            if a != b and b != c and a != c:
                res += 1
        return res

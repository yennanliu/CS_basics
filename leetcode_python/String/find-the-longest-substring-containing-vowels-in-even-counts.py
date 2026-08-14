"""

1371. Find the Longest Substring Containing Vowels in Even Counts
Medium

Given the string s, return the size of the longest substring containing each vowel an even
number of times. That is, 'a', 'e', 'i', 'o', and 'u' must appear an even number of times.


Example 1:

Input: s = "eleetminicoworoep"
Output: 13
Explanation: The longest substring is "leetminicowor" which contains two each of the vowels:
e, i and o and zero of the vowels: a and u.

Example 2:

Input: s = "leetcodeisgreat"
Output: 5
Explanation: The longest substring is "leetc" which contains two e's.

Example 3:

Input: s = "bcbcbc"
Output: 6
Explanation: In this case, the given string "bcbcbc" is the longest because all vowels:
a, e, i, o and u appear zero times.


Constraints:

1 <= s.length <= 5 x 10^5
s contains only lowercase English letters.

"""

# V0
# IDEA : PREFIX XOR MASK + FIRST-SEEN HASH TABLE
#
#   only the PARITY of each vowel count matters, so compress the prefix state
#   into a 5-bit mask (one bit per vowel), toggled with XOR as we scan.
#
#   substring s[j+1..i] has all vowels even  <=>  mask[i] == mask[j]
#   so for every mask we only care about its FIRST occurrence -- that gives the
#   longest window ending at i.
#
#   NOTE : seed the table with {0: -1} so a valid prefix starting at index 0
#          is measured as i - (-1) = i + 1.
#   NOTE : 32 possible masks only, so the table stays tiny.
#
# time = O(n), space = O(1)   (at most 32 mask entries)
class Solution(object):
    def findTheLongestSubstring(self, s):
        vowels = {'a': 0, 'e': 1, 'i': 2, 'o': 3, 'u': 4}
        first = {0: -1}
        mask = 0
        res = 0
        for i, ch in enumerate(s):
            if ch in vowels:
                mask ^= 1 << vowels[ch]
            if mask in first:
                res = max(res, i - first[mask])
            else:
                first[mask] = i
        return res

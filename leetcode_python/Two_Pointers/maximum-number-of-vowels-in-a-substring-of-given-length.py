"""

1456. Maximum Number of Vowels in a Substring of Given Length
Medium

Given a string s and an integer k, return the maximum number of vowel letters in
any substring of s with length k.

Vowel letters in English are 'a', 'e', 'i', 'o', and 'u'.


Example 1:

Input: s = "abciiidef", k = 3
Output: 3
Explanation: The substring "iii" contains 3 vowel letters.

Example 2:

Input: s = "aeiou", k = 2
Output: 2
Explanation: Any substring of length 2 contains 2 vowels.

Example 3:

Input: s = "leetcode", k = 3
Output: 2
Explanation: "lee", "eet" and "ode" contain 2 vowels.


Constraints:

1 <= s.length <= 10^5
s consists of lowercase English letters.
1 <= k <= s.length

"""

# V0
# IDEA: FIXED SIZE SLIDING WINDOW
#
#  the window length never changes, so instead of a while-loop we simply
#  "add s[i], drop s[i-k]" on every step and track the running vowel count
#
# time = O(n)
# space = O(1)
class Solution(object):
    def maxVowels(self, s, k):
        vowels = set("aeiou")

        # first window
        cnt = 0
        for i in range(k):
            if s[i] in vowels:
                cnt += 1

        res = cnt

        # slide : add the new right char, drop the char leaving on the left
        for i in range(k, len(s)):
            if s[i] in vowels:
                cnt += 1
            if s[i - k] in vowels:
                cnt -= 1
            res = max(res, cnt)

            # early exit : can't beat k vowels in a k-length window
            if res == k:
                return k

        return res

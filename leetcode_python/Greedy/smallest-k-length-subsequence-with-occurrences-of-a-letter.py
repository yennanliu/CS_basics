"""

2030. Smallest K-Length Subsequence With Occurrences of a Letter
Hard

You are given a string s, an integer k, a letter letter, and an integer repetition.

Return the lexicographically smallest subsequence of s of length k that has the letter letter appear at least repetition times. The test cases are generated so that the letter appears in s at least repetition times.

A subsequence is a string that can be derived from another string by deleting some or no characters without changing the order of the remaining characters.

A string a is lexicographically smaller than a string b if in the first position where a and b differ, string a has a letter that appears earlier in the alphabet than the corresponding letter in b.


Example 1:

Input: s = "leet", k = 3, letter = "e", repetition = 1
Output: "eet"
Explanation: There are four subsequences of length 3 that have the letter 'e' appear at least 1 time:
- "lee" (from "leet")
- "let" (from "leet")
- "let" (from "leet")
- "eet" (from "leet")
The lexicographically smallest subsequence among them is "eet".

Example 2:

Input: s = "leetcode", k = 4, letter = "e", repetition = 2
Output: "ecde"
Explanation: "ecde" is the lexicographically smallest subsequence of length 4 that has the letter "e" appear at least 2 times.

Example 3:

Input: s = "bb", k = 2, letter = "b", repetition = 2
Output: "bb"
Explanation: "bb" is the only subsequence of length 2 that has the letter "b" appear at least 2 times.


Constraints:

1 <= repetition <= k <= s.length <= 5 * 10^4
s consists of lowercase English letters.
letter is a lowercase English letter, and appears in s at least repetition times.

"""

# V0
# IDEA : MONOTONIC STACK ("remove k digits" greedy) + A QUOTA FOR `letter`
#
#   classic greedy : keep an increasing stack, popping a bigger character
#   whenever enough characters remain to still fill k slots.
#
#   the extra twist is the repetition quota. two guards before popping :
#     1. `remain` — the usual "can I still reach length k?" check
#     2. if the character on top IS `letter`, only pop it when the letters
#        still ahead plus the letters already kept can still reach
#        `repetition`
#   and one guard before pushing : do not push a NON-letter char if the
#   remaining slots are exactly what the outstanding letter quota needs.
#
# time = O(n), space = O(k)
class Solution(object):
    def smallestSubsequence(self, s, k, letter, repetition):
        n = len(s)
        letters_left = s.count(letter)   # occurrences of `letter` from i onwards
        stack = []
        kept_letters = 0                 # occurrences of `letter` inside stack

        for i, c in enumerate(s):
            remain = n - i               # chars available from i (inclusive)
            while stack and stack[-1] > c \
                    and len(stack) - 1 + remain >= k \
                    and (stack[-1] != letter
                         or kept_letters - 1 + letters_left >= repetition):
                if stack.pop() == letter:
                    kept_letters -= 1
            if len(stack) < k:
                if c == letter:
                    stack.append(c)
                    kept_letters += 1
                elif k - len(stack) > repetition - kept_letters:
                    # still room for a non-letter char without breaking the quota
                    stack.append(c)
            if c == letter:
                letters_left -= 1

        return ''.join(stack)

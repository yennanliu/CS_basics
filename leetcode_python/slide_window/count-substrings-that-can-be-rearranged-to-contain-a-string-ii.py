"""

3298. Count Substrings That Can Be Rearranged to Contain a String II
Hard

You are given two strings word1 and word2.

A string x is called valid if x can be rearranged to have word2 as a prefix.

Return the total number of valid substrings of word1.

Note that the memory limits in this problem are smaller than usual, so you must implement a solution with a linear runtime complexity.


Example 1:

Input: word1 = "bcca", word2 = "abc"
Output: 1
Explanation:
The only valid substring is "bcca" which can be rearranged to "abcc" having "abc" as a prefix.

Example 2:

Input: word1 = "abcabc", word2 = "abc"
Output: 10
Explanation:
All the substrings except substrings of size 1 and size 2 are valid.

Example 3:

Input: word1 = "abcabc", word2 = "aaabc"
Output: 0


Constraints:

1 <= word1.length <= 10^6
1 <= word2.length <= 10^4
word1 and word2 consist only of lowercase English letters.

"""

# V0
# IDEA : SAME SLIDING WINDOW AS LC 3297 — IT WAS ALREADY LINEAR
#
#   a substring is valid iff it holds at least as many of every letter as
#   word2 (the surplus can sit after the prefix once rearranged), and that
#   property is monotone in the window, so two pointers apply.
#
#   for each right end, advance `left` as long as the window still covers
#   word2; the starts 0 .. left-1 are then exactly the valid ones ending
#   here, contributing `left`.
#
#   the only thing this sequel changes is the scale — word1 reaches 10^6 with
#   a tighter memory limit — so the state stays two 26-entry arrays plus a
#   counter of still-short letters, and each character is visited twice.
#
# time = O(n + m), space = O(1)  (26 counters)
class Solution(object):
    def validSubstringCount(self, word1, word2):
        need = [0] * 26
        for ch in word2:
            need[ord(ch) - 97] += 1
        missing = sum(1 for c in range(26) if need[c] > 0)

        have = [0] * 26
        left = 0
        res = 0
        for ch in word1:
            c = ord(ch) - 97
            have[c] += 1
            if have[c] == need[c]:
                missing -= 1
            while missing == 0:
                d = ord(word1[left]) - 97
                have[d] -= 1
                if have[d] == need[d] - 1:
                    missing += 1
                left += 1
            res += left
        return res

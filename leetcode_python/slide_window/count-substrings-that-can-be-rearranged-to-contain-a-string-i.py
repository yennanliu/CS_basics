"""

3297. Count Substrings That Can Be Rearranged to Contain a String I
Medium

You are given two strings word1 and word2.

A string x is called valid if x can be rearranged to have word2 as a prefix.

Return the total number of valid substrings of word1.


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

1 <= word1.length <= 10^5
1 <= word2.length <= 10^4
word1 and word2 consist only of lowercase English letters.

"""

# V0
# IDEA : "REARRANGEABLE TO HAVE word2 AS A PREFIX" == COVERS ITS LETTER COUNTS
#
#   the substring may be shuffled freely, so it qualifies exactly when it
#   contains at least as many of every letter as word2 does — the leftovers
#   go after the prefix.
#
#   that condition only gets easier as the substring grows, so a sliding
#   window works : for each right end, shrink the left as far as the counts
#   still cover word2. every start at or before that point is valid, giving
#   (left + 1) substrings ending here.
#
#   tracking how many letters are still SHORT turns the check into an integer
#   comparison instead of a 26-way scan.
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
            # push `left` past every start whose window still covers word2
            while missing == 0:
                d = ord(word1[left]) - 97
                have[d] -= 1
                if have[d] == need[d] - 1:
                    missing += 1
                left += 1
            res += left                          # starts 0 .. left-1 all work
        return res

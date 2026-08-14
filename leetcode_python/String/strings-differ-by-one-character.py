"""

1554. Strings Differ by One Character
Medium

Given a list of strings dict where all the strings are of the same length.

Return true if there are 2 strings that only differ by 1 character in the same index, otherwise return false.

Example 1:

Input: dict = ["abcd","acbd", "aacd"]
Output: true
Explanation: Strings "abcd" and "aacd" differ only by one character in the index 1.

Example 2:

Input: dict = ["ab","cd","yz"]
Output: false

Example 3:

Input: dict = ["abcd","cccc","abyd","abab"]
Output: true

Constraints:

The number of characters in dict <= 10^5
dict[i].length == dict[j].length
dict[i] should be unique.
dict[i] contains only lowercase English letters.

"""

# V0
# IDEA : ROLLING HASH (hash of a word with one position "blanked out")
#
#   two words differ by exactly 1 char at index i
#     <=> hash(word) - weight(i) * word[i] is the same for both, at the
#         same index i.
#   so for every word and every index we push the key (i, masked_hash)
#   into a set ; a repeat means we found the pair.
#   NOTE : dict[i] are unique, so an equal key can only come from a
#          different word -> no false positive from the word itself.
#
# time = O(n * m), space = O(n * m), n = #words, m = word length
class Solution(object):
    def differByOne(self, dict):
        MOD = (1 << 61) - 1
        BASE = 131
        m = len(dict[0])
        pw = [1] * m
        for i in range(1, m):
            pw[i] = pw[i - 1] * BASE % MOD

        seen = set()
        for w in dict:
            h = 0
            for c in w:
                h = (h * BASE + ord(c)) % MOD
            for i, c in enumerate(w):
                key = (i, (h - ord(c) * pw[m - 1 - i]) % MOD)
                if key in seen:
                    return True
                seen.add(key)
        return False

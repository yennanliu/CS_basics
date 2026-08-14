"""

1657. Determine if Two Strings Are Close
Medium

Two strings are considered close if you can attain one from the other using the following operations:

Operation 1: Swap any two existing characters.
  For example, abcde -> aecdb
Operation 2: Transform every occurrence of one existing character into another existing character,
  and do the same with the other character.
  For example, aacabb -> bbcbaa (all a's turn into b's, and all b's turn into a's)

You can use the operations on either string as many times as necessary.

Given two strings, word1 and word2, return true if word1 and word2 are close, and false otherwise.


Example 1:

Input: word1 = "abc", word2 = "bca"
Output: true
Explanation: You can attain word2 from word1 in 2 operations.
Apply Operation 1: "abc" -> "acb"
Apply Operation 1: "acb" -> "bca"

Example 2:

Input: word1 = "a", word2 = "aa"
Output: false
Explanation: It is impossible to attain word2 from word1, or vice versa, in any number of operations.

Example 3:

Input: word1 = "cabbba", word2 = "abbccc"
Output: true
Explanation: You can attain word2 from word1 in 3 operations.
Apply Operation 1: "cabbba" -> "caabbb"
Apply Operation 2: "caabbb" -> "baaccc"
Apply Operation 2: "baaccc" -> "abbccc"


Constraints:

1 <= word1.length, word2.length <= 10^5
word1 and word2 contain only lowercase English letters.

"""

# V0
# IDEA : HASH TABLE / COUNTING (two invariants under the allowed operations)
#
#   op 1 (swap) can produce ANY permutation -> position does not matter,
#        only the multiset of counts matters.
#   op 2 (swap two letters' identities) can permute the counts among the
#        letters that are PRESENT, but never introduces/removes a letter.
#
#   so word1 ~ word2  <=>
#     (a) they use exactly the same SET of letters, and
#     (b) the sorted MULTISET of their frequencies is identical.
#
#   NOTE : (b) alone is not enough ("aab" vs "ccd" share counts but the
#          letter sets differ, and op 2 cannot create a new letter).
#
# time = O(n + m), space = O(1) (at most 26 letters)
from collections import Counter
class Solution(object):
    def closeStrings(self, word1, word2):
        c1, c2 = Counter(word1), Counter(word2)
        if set(c1.keys()) != set(c2.keys()):
            return False
        return sorted(c1.values()) == sorted(c2.values())

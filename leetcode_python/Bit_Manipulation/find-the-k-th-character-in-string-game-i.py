"""

3304. Find the K-th Character in String Game I
Easy

Alice and Bob are playing a game. Initially, Alice has a string word = "a".

You are given a positive integer k.

Now Bob will ask Alice to perform the following operation forever:

Generate a new string by changing each character in word to its next character in the English alphabet, and append it to the original word.

For example, performing the operation on "c" generates "cd" and performing the operation on "zb" generates "zbac".

Return the value of the kth character in word, after enough operations have been done for word to have at least k characters.

Note that the character 'z' can be changed to 'a' in the operation.


Example 1:

Input: k = 5
Output: "b"
Explanation:
Initially, word = "a". We need to do the operation three times:
Generated string is "b", word becomes "ab".
Generated string is "bc", word becomes "abbc".
Generated string is "bccd", word becomes "abbcbccd".

Example 2:

Input: k = 10
Output: "c"


Constraints:

1 <= k <= 500

"""

# V0
# IDEA : EACH DOUBLING SHIFTS THE COPY BY ONE — SO COUNT THE SET BITS OF k-1
#
#   the string doubles every round, and the second half is the first half
#   shifted by one letter. so position p (0-indexed) in the new half maps
#   back to position p - half in the old one, one letter lower.
#
#   peeling those halves off is exactly reading the binary digits of the
#   index : every 1 bit crossed adds one shift, so
#
#       answer = 'a' + popcount(k - 1)
#
#   (the shifts stay well inside the alphabet for k <= 500, and 'z' would
#   wrap to 'a' if it ever came up).
#
# time = O(1), space = O(1)
class Solution(object):
    def kthCharacter(self, k):
        return chr(ord('a') + bin(k - 1).count('1') % 26)

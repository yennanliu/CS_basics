"""

3163. String Compression III
Medium

Given a string word, compress it using the following algorithm:

Begin with an empty string comp. While word is not empty, use the following operation:
    Remove a maximum length prefix of word made of a single character c repeating at most 9 times.
    Append the length of the prefix followed by c to comp.

Return the string comp.


Example 1:

Input: word = "abcde"
Output: "1a1b1c1d1e"
Explanation:
Initially, comp = "". Apply the operation 5 times, choosing "a", "b", "c", "d", and "e" as the prefix in each operation.
For each prefix, append "1" followed by the character to comp.

Example 2:

Input: word = "aaaaaaaaaaaaaabb"
Output: "9a5a2b"
Explanation:
Initially, comp = "". Apply the operation 3 times, choosing "aaaaaaaaa", "aaaaa", and "bb" as the prefix in each operation.
For prefix "aaaaaaaaa", append "9" followed by "a" to comp.
For prefix "aaaaa", append "5" followed by "a" to comp.
For prefix "bb", append "2" followed by "b" to comp.


Constraints:

1 <= word.length <= 2 * 10^5
word consists only of lowercase English letters.

"""

# V0
# IDEA : WALK THE RUNS, EMITTING THEM IN CHUNKS OF AT MOST 9
#
#   the operation always takes the longest run of one character, capped at 9,
#   so a run of length L becomes floor(L/9) chunks of "9c" plus a remainder
#   chunk if L % 9 is non-zero.
#
#   building a list and joining once keeps it linear — repeated string
#   concatenation would be quadratic on a 2*10^5 input.
#
# time = O(n), space = O(n)
class Solution(object):
    def compressedString(self, word):
        out = []
        i, n = 0, len(word)
        while i < n:
            j = i
            while j < n and word[j] == word[i]:
                j += 1
            run = j - i
            while run > 9:
                out.append("9" + word[i])
                run -= 9
            out.append(str(run) + word[i])
            i = j
        return ''.join(out)

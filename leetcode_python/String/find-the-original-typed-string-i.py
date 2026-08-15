"""

3330. Find the Original Typed String I
Easy

Alice is attempting to type a specific string on her computer. However, she tends to be clumsy and may press a key for too long, resulting in a character being typed multiple times.

Although Alice tried to focus on her typing, she is aware that she may still have done this at most once.

You are given a string word, which represents the final output displayed on Alice's screen.

Return the total number of possible original strings that Alice might have intended to type.


Example 1:

Input: word = "abbcccc"
Output: 5
Explanation:
The possible strings are: "abbcccc", "abbccc", "abbcc", "abbc", and "abcccc".

Example 2:

Input: word = "abcd"
Output: 1
Explanation:
The only possible string is "abcd".

Example 3:

Input: word = "aaaa"
Output: 4


Constraints:

1 <= word.length <= 100
word consists only of lowercase English letters.

"""

# V0
# IDEA : AT MOST ONE RUN MAY HAVE BEEN OVER-TYPED, AND ONLY BY SOME AMOUNT
#
#   the intended string equals `word` (she pressed nothing too long), or it
#   is `word` with ONE run shortened. a run of length L can be shortened to
#   any of L - 1 shorter lengths, and those all give distinct strings.
#
#   so the answer is 1 + sum over runs of (length - 1), which the run scan
#   collects in one pass.
#
# time = O(n), space = O(1)
class Solution(object):
    def possibleStringCount(self, word):
        res = 1
        for i in range(1, len(word)):
            if word[i] == word[i - 1]:
                res += 1
        return res

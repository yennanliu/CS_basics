"""

2063. Vowels of All Substrings
Medium

Given a string word, return the sum of the number of vowels ('a', 'e', 'i', 'o', and 'u') in every substring of word.

A substring is a contiguous (non-empty) sequence of characters within a string.

Note: Due to the large constraints, the answer may not fit in a signed 32-bit integer. Please be careful during the calculations.


Example 1:

Input: word = "aba"
Output: 6
Explanation:
All possible substrings are: "a", "ab", "aba", "b", "ba", and "a".
- "b" has 0 vowels in it
- "a", "ab", "ba", and "a" have 1 vowel each
- "aba" has 2 vowels in it
Hence, the total sum of vowels = 0 + 1 + 1 + 1 + 1 + 2 = 6.

Example 2:

Input: word = "abc"
Output: 3
Explanation:
All possible substrings are: "a", "ab", "abc", "b", "bc", and "c".
- "a", "ab", and "abc" have 1 vowel each
- "b", "bc", and "c" have 0 vowels each
Hence, the total sum of vowels = 1 + 1 + 1 + 0 + 0 + 0 = 3.

Example 3:

Input: word = "ltcd"
Output: 0
Explanation: There are no vowels in any substring of "ltcd".


Constraints:

1 <= word.length <= 10^5
word consists of lowercase English letters.

"""

# V0
# IDEA : COUNT EACH VOWEL'S CONTRIBUTION (combinatorics, no substring loop)
#
#   instead of enumerating substrings, ask how many substrings CONTAIN the
#   character at index i: the left endpoint has (i + 1) choices and the
#   right endpoint has (n - i) choices -> (i + 1) * (n - i) substrings.
#
#   so the answer is simply the sum of (i + 1) * (n - i) over vowel indices.
#
# time = O(n), space = O(1)
class Solution(object):
    def countVowels(self, word):
        n = len(word)
        res = 0
        for i, c in enumerate(word):
            if c in 'aeiou':
                res += (i + 1) * (n - i)
        return res

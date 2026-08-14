"""

2062. Count Vowel Substrings of a String
Easy

A substring is a contiguous (non-empty) sequence of characters within a string.

A vowel substring is a substring that only consists of vowels ('a', 'e', 'i', 'o', and 'u') and has all five vowels present in it.

Given a string word, return the number of vowel substrings in word.


Example 1:

Input: word = "aeiouu"
Output: 2
Explanation: The vowel substrings of word are as follows (underlined):
- "aeiouu"
- "aeiouu"

Example 2:

Input: word = "unicornarihan"
Output: 0
Explanation: Not all 5 vowels are present, so there are no vowel substrings.

Example 3:

Input: word = "cuaieuouac"
Output: 7
Explanation: The vowel substrings of word are as follows (underlined):
- "cuaieuouac"
- "cuaieuouac"
- "cuaieuouac"
- "cuaieuouac"
- "cuaieuouac"
- "cuaieuouac"
- "cuaieuouac"


Constraints:

1 <= word.length <= 100
word consists of lowercase English letters only.

"""

# V0
# IDEA : EXPAND EVERY LEFT ENDPOINT, STOP AT THE FIRST CONSONANT
#
#   fix the left endpoint i and grow the right endpoint; a distinct-vowel
#   set t is maintained incrementally. the moment a non-vowel appears the
#   inner loop breaks (no longer substring starting at i can be all vowels).
#   every time |t| == 5 the current window is a valid vowel substring.
#
#   n <= 100 so the O(n^2) scan is the intended solution.
#
# time = O(n^2), space = O(1) (the set holds at most 5 letters)
class Solution(object):
    def countVowelSubstrings(self, word):
        vowels = set('aeiou')
        res = 0
        n = len(word)
        for i in range(n):
            t = set()
            for j in range(i, n):
                c = word[j]
                if c not in vowels:
                    break
                t.add(c)
                if len(t) == 5:
                    res += 1
        return res

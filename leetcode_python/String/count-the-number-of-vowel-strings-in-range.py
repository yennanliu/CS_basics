"""

2586. Count the Number of Vowel Strings in Range
Easy
You are given a 0-indexed array of string words and two integers left and right.

A string is called a vowel string if it starts with a vowel character and ends with a vowel character where vowel characters are 'a', 'e', 'i', 'o', and 'u'.

Return the number of vowel strings words[i] where i belongs to the inclusive range [left, right].


Example 1:

Input: words = ["are","amy","u"], left = 0, right = 2
Output: 2
Explanation:
- "are" is a vowel string because it starts with 'a' and ends with 'e'.
- "amy" is not a vowel string because it does not end with a vowel.
- "u" is a vowel string because it starts with 'u' and ends with 'u'.
The number of vowel strings in the mentioned range is 2.

Example 2:

Input: words = ["hey","aeo","mu","ooo","artro"], left = 1, right = 4
Output: 3
Explanation:
- "aeo" is a vowel string because it starts with 'a' and ends with 'o'.
- "mu" is not a vowel string because it does not start with a vowel.
- "ooo" is a vowel string because it starts with 'o' and ends with 'o'.
- "artro" is a vowel string because it starts with 'a' and ends with 'o'.
The number of vowel strings in the mentioned range is 3.


Constraints:

1 <= words.length <= 1000
1 <= words[i].length <= 10
words[i] consists of only lowercase English letters.
0 <= left <= right < words.length

"""

# V0
# IDEA : SIMULATION (scan the inclusive window and test both ends)
#
#   only the FIRST and LAST characters matter -- everything between them is
#   irrelevant, so no need to inspect the whole word.
#
#   NOTE : a length-1 word has w[0] == w[-1], so a single vowel such as "u"
#          correctly counts as a vowel string.
#   NOTE : `right` is INCLUSIVE, hence the `right + 1` slice bound.
#
# time = O(right - left + 1), space = O(1)
class Solution(object):
    def vowelStrings(self, words, left, right):
        vowels = set('aeiou')
        res = 0
        for i in range(left, right + 1):
            w = words[i]
            if w[0] in vowels and w[-1] in vowels:
                res += 1
        return res

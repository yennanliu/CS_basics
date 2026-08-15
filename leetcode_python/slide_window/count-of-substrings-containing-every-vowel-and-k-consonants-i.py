"""

3305. Count of Substrings Containing Every Vowel and K Consonants I
Medium

You are given a string word and a non-negative integer k.

Return the total number of substrings of word that contain every vowel ('a', 'e', 'i', 'o', and 'u') at least once and exactly k consonants.


Example 1:

Input: word = "aeioqq", k = 1
Output: 0
Explanation:
There is no substring with every vowel.

Example 2:

Input: word = "aeiou", k = 0
Output: 1
Explanation:
The only substring with every vowel and zero consonants is word[0..4], which is "aeiou".

Example 3:

Input: word = "ieaouqqieaouqq", k = 1
Output: 3
Explanation:
The substrings with every vowel and one consonant are:
word[0..5], which is "ieaouq".
word[6..11], which is "qieaou".
word[7..12], which is "ieaouq".


Constraints:

5 <= word.length <= 250
word consists only of lowercase English letters.
0 <= k <= word.length - 5

"""

# V0
# IDEA : "EXACTLY k" = "AT LEAST k" MINUS "AT LEAST k+1"
#
#   an exact-count condition is awkward for a sliding window, but an
#   at-least condition is monotone : growing a substring never loses a vowel
#   or a consonant. so count the substrings with all five vowels and at least
#   k consonants, do the same for k+1, and subtract.
#
#   for the at-least count, extend the right end and shrink the left while
#   the window still qualifies; every start before that point also qualifies,
#   contributing `left` substrings ending here.
#
# time = O(n), space = O(1)
class Solution(object):
    def countOfSubstrings(self, word, k):
        VOWELS = set("aeiou")

        def at_least(need):
            cnt = {}
            cons = 0
            left = 0
            res = 0
            for ch in word:
                if ch in VOWELS:
                    cnt[ch] = cnt.get(ch, 0) + 1
                else:
                    cons += 1
                while len(cnt) == 5 and cons >= need:
                    d = word[left]
                    if d in VOWELS:
                        cnt[d] -= 1
                        if cnt[d] == 0:
                            del cnt[d]
                    else:
                        cons -= 1
                    left += 1
                res += left
            return res

        return at_least(k) - at_least(k + 1)

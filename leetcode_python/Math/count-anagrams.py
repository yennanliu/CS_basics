"""

2514. Count Anagrams
Hard

You are given a string s containing one or more words. Every consecutive pair of words is separated by a single space ' '.

A string t is an anagram of string s if the ith word of t is a permutation of the ith word of s.

For example, "acb dfe" is an anagram of "abc def", but "def cab" and "adc bef" are not.

Return the number of distinct anagrams of s. Since the answer may be very large, return it modulo 10^9 + 7.


Example 1:

Input: s = "too hot"
Output: 18
Explanation: Some of the anagrams of the given string are "too hot", "oot hot", "oto toh", "too toh", and "too oht".

Example 2:

Input: s = "aa"
Output: 1
Explanation: There is only one anagram possible for the given string.


Constraints:

1 <= s.length <= 10^5
s consists of lowercase English letters and spaces ' '.
There is single space between consecutive words.

"""

# V0
# IDEA : COMBINATORICS (multinomial coefficient per word) + MODULAR INVERSE
#
#   each word can be permuted independently, and the words keep their slots,
#   so the answer is the product over words of the number of DISTINCT
#   permutations of that word:
#
#       distinct(w) = len(w)! / (c1! * c2! * ... * c26!)
#
#   where ci is the multiplicity of each letter in w. multiply them all up.
#
#   division under a prime modulus is done with Fermat's little theorem:
#       1 / d  ==  pow(d, MOD - 2, MOD)
#
#   NOTE : accumulate the denominator incrementally — while scanning a word,
#          bump cnt[c] then multiply the denominator by the NEW cnt[c]. that
#          builds prod(ci!) without ever materialising a factorial table, and
#          keeps everything reduced mod MOD so no big-int blowup.
#
# time = O(n), space = O(1)  (alphabet is fixed at 26)
class Solution(object):
    def countAnagrams(self, s):
        MOD = 10 ** 9 + 7

        numerator = 1    # product of len(w)!
        denominator = 1  # product of all letter-count factorials

        for w in s.split():
            cnt = {}
            for i, c in enumerate(w):
                cnt[c] = cnt.get(c, 0) + 1
                denominator = denominator * cnt[c] % MOD
                numerator = numerator * (i + 1) % MOD

        return numerator * pow(denominator, MOD - 2, MOD) % MOD

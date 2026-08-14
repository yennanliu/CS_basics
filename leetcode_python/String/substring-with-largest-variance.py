"""

2272. Substring With Largest Variance
Hard

The variance of a string is defined as the largest difference between the number of occurrences of any 2 characters present in the string. Note the two characters may or may not be the same.

Given a string s consisting of lowercase English letters only, return the largest variance possible among all substrings of s.

A substring is a contiguous sequence of characters within a string.


Example 1:

Input: s = "aababbb"
Output: 3
Explanation:
All possible variances along with their respective substrings are listed below:
- Variance 0 for substrings "a", "aa", "ab", "abab", "aababb", "ba", "b", "bb", and "bbb".
- Variance 1 for substrings "aab", "aba", "abb", "aabab", "ababb", "aababbb", and "bab".
- Variance 2 for substrings "aaba", "ababbb", "abbb", and "babb".
- Variance 3 for substring "babbb".
Since the largest possible variance is 3, we return it.

Example 2:

Input: s = "abcde"
Output: 0
Explanation:
No letter occurs more than once in s, so the variance of every substring is 0.


Constraints:

1 <= s.length <= 10^4
s consists of lowercase English letters.

"""

# V0
# IDEA : KADANE PER ORDERED LETTER PAIR (a = +1, b = -1, b must appear)
#
#   fix an ordered pair (a, b) and score a as +1, b as -1; all other letters
#   are ignored. the answer for this pair is the maximum subarray sum that
#   contains AT LEAST ONE b - that "at least one" is the whole difficulty.
#
#   two Kadane states:
#     f0 = best running sum of a suffix that contains NO b   (so >= 0)
#     f1 = best running sum of a suffix that contains >= 1 b (-inf if none yet)
#
#     on 'a' : f0 += 1, f1 += 1
#     on 'b' : f1 = max(f1, f0) - 1 ; f0 = 0
#              (the old f0 becomes a valid "has a b" prefix once we spend this
#               b; f0 restarts because a b cannot stay in a b-free suffix)
#
#   answer = max f1 over all 26*25 ordered pairs.
#
#   NOTE : the pair is ORDERED - (a,b) and (b,a) give different answers, and
#          both are needed since variance is a max over the two counts.
#
# time = O(26 * 26 * n), space = O(1)
class Solution(object):
    def largestVariance(self, s):
        letters = set(s)
        res = 0
        NEG = float('-inf')
        for a in letters:
            for b in letters:
                if a == b:
                    continue
                f0, f1 = 0, NEG
                for c in s:
                    if c == a:
                        f0 += 1
                        f1 += 1
                    elif c == b:
                        f1 = max(f1, f0) - 1
                        f0 = 0
                    if f1 > res:
                        res = f1
        return res

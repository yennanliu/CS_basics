"""

1641. Count Sorted Vowel Strings
Medium

Given an integer n, return the number of strings of length n that consist only of vowels (a, e, i, o, u) and are lexicographically sorted.

A string s is lexicographically sorted if for all valid i, s[i] is the same as or comes before s[i+1] in the alphabet.


Example 1:

Input: n = 1
Output: 5
Explanation: The 5 sorted strings that consist of vowels only are ["a","e","i","o","u"].

Example 2:

Input: n = 2
Output: 15
Explanation: The 15 sorted strings that consist of vowels only are
["aa","ae","ai","ao","au","ee","ei","eo","eu","ii","io","iu","oo","ou","uu"].
Note that "ea" is not a valid string since 'e' comes after 'a' in the alphabet.

Example 3:

Input: n = 33
Output: 66045


Constraints:

1 <= n <= 50

"""

# V0
# IDEA : STARS AND BARS (a sorted string is just a multiset of 5 vowels)
#
#   a non-decreasing string of length n over 5 letters is fully described
#   by how many of each vowel it uses: c1+c2+c3+c4+c5 = n, ci >= 0.
#   the number of such tuples is the classic stars-and-bars count
#     C(n + 4, 4)
#
#   sanity check : n = 1 -> C(5,4) = 5 ; n = 2 -> C(6,4) = 15. matches.
#
# time = O(1), space = O(1)
class Solution(object):
    def countVowelStrings(self, n):
        return (n + 1) * (n + 2) * (n + 3) * (n + 4) // 24

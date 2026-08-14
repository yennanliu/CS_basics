"""

1704. Determine if String Halves Are Alike
Easy

You are given a string s of even length. Split this string into two halves of equal lengths, and let a be the first half and b be the second half.

Two strings are alike if they have the same number of vowels ('a', 'e', 'i', 'o', 'u', 'A', 'E', 'I', 'O', 'U'). Notice that s contains uppercase and lowercase letters.

Return true if a and b are alike. Otherwise, return false.


Example 1:

Input: s = "book"
Output: true
Explanation: a = "bo" and b = "ok". a has 1 vowel and b has 1 vowel. Therefore, they are alike.

Example 2:

Input: s = "textbook"
Output: false
Explanation: a = "text" and b = "book". a has 1 vowel whereas b has 2. Therefore, they are not alike.
Notice that the vowel o is counted twice.


Constraints:

2 <= s.length <= 1000
s.length is even.
s consists of uppercase and lowercase letters.

"""

# V0
# IDEA : COUNTING (single pass, one running balance instead of two counters)
#
#   let n = len(s) // 2. index i belongs to the first half,
#   index i + n belongs to the second half.
#
#   walk i in [0, n) and keep `cnt`:
#     +1 when s[i]     is a vowel
#     -1 when s[i + n] is a vowel
#
#   the halves are alike  <=>  cnt ends at 0.
#   NOTE : vowels include BOTH cases, so put all 10 chars in the set.
#
# time = O(n), space = O(1)   (the vowel set is a fixed 10 chars)
class Solution(object):
    def halvesAreAlike(self, s):
        vowels = set('aeiouAEIOU')
        n = len(s) // 2

        cnt = 0
        for i in range(n):
            if s[i] in vowels:
                cnt += 1
            if s[i + n] in vowels:
                cnt -= 1

        return cnt == 0

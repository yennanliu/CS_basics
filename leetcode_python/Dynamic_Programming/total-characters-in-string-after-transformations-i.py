"""

3335. Total Characters in String After Transformations I
Medium

You are given a string s and an integer t, representing the number of transformations to perform. In one transformation, every character in s is replaced according to the following rules:

If the character is 'z', replace it with the string "ab".
Otherwise, replace it with the next character in the alphabet. For example, 'a' is replaced with 'b', 'b' is replaced with 'c', and so on.

Return the length of the resulting string after exactly t transformations.

Since the answer may be very large, return it modulo 10^9 + 7.


Example 1:

Input: s = "abcyy", t = 2
Output: 7
Explanation:
First Transformation (t = 1):
'a' becomes 'b'
'b' becomes 'c'
'c' becomes 'd'
'y' becomes 'z'
'y' becomes 'z'
String after the first transformation: "bcdzz"
Second Transformation (t = 2):
'b' becomes 'c'
'c' becomes 'd'
'd' becomes 'e'
'z' becomes "ab"
'z' becomes "ab"
String after the second transformation: "cdeabab"
Final Length of the string: The string is "cdeabab", which has 7 characters.

Example 2:

Input: s = "azbk", t = 1
Output: 5
Explanation:
First Transformation (t = 1):
'a' becomes 'b'
'z' becomes "ab"
'b' becomes 'c'
'k' becomes 'l'
String after the first transformation: "bcabl"
Final Length of the string: The string is "bcabl", which has 5 characters.


Constraints:

1 <= s.length <= 10^5
1 <= t <= 10^5
s consists only of lowercase English letters.

"""

# V0
# IDEA : ONLY THE 26 LETTER COUNTS MATTER, NOT THE STRING ITSELF
#
#   every character evolves independently, so the state collapses to a
#   histogram of the 26 letters. one transformation shifts every count up by
#   one letter, except 'z' which becomes both an 'a' and a 'b' :
#
#       new[0]  = cnt[25]
#       new[1]  = cnt[0] + cnt[25]
#       new[i]  = cnt[i-1]      for i >= 2
#
#   so a round is 26 assignments, and the answer is the total after t rounds.
#
#   (the sequel LC 3337 pushes t to 10^9 and needs matrix exponentiation of
#   exactly this update.)
#
# time = O(n + 26 * t), space = O(1)
class Solution(object):
    def lengthAfterTransformations(self, s, t):
        MOD = 10 ** 9 + 7
        cnt = [0] * 26
        for ch in s:
            cnt[ord(ch) - 97] += 1

        for _ in range(t):
            nxt = [0] * 26
            nxt[0] = cnt[25]
            nxt[1] = (cnt[0] + cnt[25]) % MOD
            for i in range(2, 26):
                nxt[i] = cnt[i - 1]
            cnt = nxt

        return sum(cnt) % MOD

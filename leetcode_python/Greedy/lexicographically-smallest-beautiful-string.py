"""

2663. Lexicographically Smallest Beautiful String
Hard

A string is beautiful if:

It consists of the first k letters of the English lowercase alphabet.
It does not contain any substring of length 2 or more which is a palindrome.

You are given a beautiful string s of length n and a positive integer k.

Return the lexicographically smallest string of length n, which is larger than s and is beautiful. If there is no such string, return an empty string.

A string a is lexicographically larger than a string b (of the same length) if in the first position where a and b differ, a has a character strictly larger than the corresponding character in b.

For example, "abcd" is lexicographically larger than "abcc" because the first position they differ is at the fourth character, and d is greater than c.


Example 1:

Input: s = "abcz", k = 26
Output: "abda"
Explanation: The string "abda" is beautiful and lexicographically larger than the string "abcz".
It can be proven that there is no string that is lexicographically larger than the string "abcz", beautiful, and lexicographically smaller than the string "abda".

Example 2:

Input: s = "dc", k = 4
Output: ""
Explanation: It can be proven that there is no string that is lexicographically larger than the string "dc" and is beautiful.


Constraints:

1 <= n == s.length <= 10^5
4 <= k <= 26
s is a beautiful string.

"""

# V0
# IDEA : GREEDY -- "NEXT PERMUTATION" STYLE BUMP, THEN MINIMAL REFILL
#
#   first, simplify the palindrome condition. A palindromic substring of
#   length >= 2 always contains a palindrome of length 2 (cs[i] == cs[i-1])
#   or of length 3 (cs[i] == cs[i-2]). So a string is beautiful iff
#
#       every char differs from its previous TWO chars.
#
#   that is a purely local rule, which is what makes the greedy work.
#
#   to get the smallest beautiful string > s:
#     1) scan i from n-1 down to 0, and try to raise cs[i] to the smallest
#        letter c with cs[i] < c < 'a'+k that differs from cs[i-1] and
#        cs[i-2]. Raising the RIGHTMOST possible position keeps the prefix
#        as long (and hence the result as small) as possible.
#     2) once such an i is found, everything after it is free, so refill
#        positions i+1..n-1 with the smallest legal letter each time
#        (again just "different from the previous two").
#     3) if no i can be raised, no answer exists -> return "".
#
#   NOTE : during step 1 the prefix cs[0..i-1] is still the ORIGINAL s and
#          is already beautiful, so only the two left neighbours have to be
#          checked -- nothing to the right matters, it gets overwritten.
#
#   NOTE : k >= 4 guarantees step 2 always succeeds: at most 2 letters are
#          banned, so of the k >= 4 available at least one is legal. This is
#          exactly why the problem constrains k >= 4.
#
#   NOTE : the refill scans at most 3 candidate letters per position, so
#          step 2 is O(n), not O(n*k).
#
# time = O(n * k), space = O(n) for the char list
class Solution(object):
    def smallestBeautifulString(self, s, k):
        n = len(s)
        cs = [ord(ch) - 97 for ch in s]
        limit = k

        for i in range(n - 1, -1, -1):
            found = -1
            for c in range(cs[i] + 1, limit):
                if i >= 1 and cs[i - 1] == c:
                    continue
                if i >= 2 and cs[i - 2] == c:
                    continue
                found = c
                break
            if found == -1:
                continue

            cs[i] = found
            for j in range(i + 1, n):
                for c in range(limit):
                    if j >= 1 and cs[j - 1] == c:
                        continue
                    if j >= 2 and cs[j - 2] == c:
                        continue
                    cs[j] = c
                    break
            return "".join(chr(97 + c) for c in cs)

        return ""

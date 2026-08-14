"""

2193. Minimum Number of Moves to Make Palindrome
Hard

You are given a string s consisting only of lowercase English letters.

In one move, you can select any two adjacent characters of s and swap them.

Return the minimum number of moves needed to make s a palindrome.

Note that the input will be generated such that s can always be converted to a palindrome.


Example 1:

Input: s = "aabb"
Output: 2
Explanation:
We can obtain two palindromes from s, "abba" and "baab".
- We can obtain "abba" from s in 2 moves: "aabb" -> "abab" -> "abba".
- We can obtain "baab" from s in 2 moves: "aabb" -> "abab" -> "baab".
Thus, the minimum number of moves needed to make s a palindrome is 2.

Example 2:

Input: s = "letelt"
Output: 2
Explanation:
One of the palindromes we can obtain from s in 2 moves is "lettel".
One of the ways we can obtain it is "letelt" -> "letetl" -> "lettel".
Other palindromes such as "tleelt" can also be obtained in 2 moves.
It can be shown that it is not possible to obtain a palindrome in less than 2 moves.


Constraints:

1 <= s.length <= 2000
s consists only of lowercase English letters.
s can be converted to a palindrome using a finite number of moves.

"""

# V0
# IDEA : GREEDY FROM THE OUTSIDE IN — MATCH s[i] WITH THE RIGHTMOST TWIN
#
#   fix the outermost pair first. for the current left character s[i], the
#   cheapest partner is the RIGHTMOST occurrence of that letter : dragging it
#   to position j costs (j - k) adjacent swaps, and any nearer twin would
#   cost more overall (exchange argument).
#
#   if the scan finds no twin (k == i), that letter is the odd one out. it
#   must end up in the exact centre, so nudge it ONE step right (cost 1) and
#   retry the same i — this accumulates exactly the distance from its
#   position to the middle.
#
# time = O(n^2), space = O(n)
class Solution(object):
    def minMovesToMakePalindrome(self, s):
        s = list(s)
        res = 0
        i, j = 0, len(s) - 1
        while i < j:
            k = j
            while k > i and s[k] != s[i]:
                k -= 1
            if k == i:
                # the unique odd character : shift it toward the centre
                s[i], s[i + 1] = s[i + 1], s[i]
                res += 1
            else:
                for t in range(k, j):
                    s[t], s[t + 1] = s[t + 1], s[t]
                    res += 1
                i += 1
                j -= 1
        return res

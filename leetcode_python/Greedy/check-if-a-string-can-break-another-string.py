"""

1433. Check If a String Can Break Another String
Medium

Given two strings: s1 and s2 with the same size, check if some permutation of
string s1 can break some permutation of string s2 or vice-versa. In other words
s2 can break s1 or vice-versa.

A string x can break string y (both of size n) if x[i] >= y[i] (in alphabetical
order) for all i between 0 and n-1.


Example 1:

Input: s1 = "abc", s2 = "xya"
Output: true
Explanation: "ayx" is a permutation of s2="xya" which can break to string "abc"
which is a permutation of s1="abc".

Example 2:

Input: s1 = "abe", s2 = "acd"
Output: false
Explanation: All permutations for s1="abe" are: "abe", "aeb", "bae", "bea",
"eab" and "eba" and all permutation for s2="acd" are: "acd", "adc", "cad",
"cda", "dac" and "dca". However, there is not any permutation from s1 which can
break some permutation from s2 and vice-versa.

Example 3:

Input: s1 = "leetcodee", s2 = "interview"
Output: true


Constraints:

s1.length == n
s2.length == n
1 <= n <= 10^5
All strings consist of lowercase English letters.

"""

# V0
# IDEA : GREEDY + SORT
#
#  -> if ANY permutation of x breaks ANY permutation of y, then the
#     SORTED x breaks the SORTED y (exchange argument: pairing the i-th
#     smallest with the i-th smallest is optimal)
#  -> so just sort both and check the two directions
#
# time = O(n log n)
# space = O(n)
class Solution(object):
    def checkIfCanBreak(self, s1, s2):
        a = sorted(s1)
        b = sorted(s2)
        return (all(x >= y for x, y in zip(a, b))
                or all(x <= y for x, y in zip(a, b)))


# V1
# IDEA : COUNTING SORT (26 letters -> linear time)
#
#  -> instead of sorting, sweep the alphabet keeping running counts;
#     at every prefix, one side must dominate the other consistently
#
# time = O(n + 26)
# space = O(26)
class Solution(object):
    def checkIfCanBreak(self, s1, s2):
        c1 = [0] * 26
        c2 = [0] * 26
        for ch in s1:
            c1[ord(ch) - ord('a')] += 1
        for ch in s2:
            c2[ord(ch) - ord('a')] += 1

        # NOTE !!!
        #   sorted(s1)[i] >= sorted(s2)[i] for all i
        #   <=> for every letter c, (# chars of s1 <= c) <= (# chars of s2 <= c)
        a_breaks_b = True   # sorted(s1) >= sorted(s2) elementwise
        b_breaks_a = True   # sorted(s2) >= sorted(s1) elementwise
        p1 = p2 = 0
        for i in range(26):
            p1 += c1[i]
            p2 += c2[i]
            if p1 > p2:
                a_breaks_b = False
            if p2 > p1:
                b_breaks_a = False

        return a_breaks_b or b_breaks_a

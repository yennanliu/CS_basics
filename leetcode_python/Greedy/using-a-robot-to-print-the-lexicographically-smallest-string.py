"""

2434. Using a Robot to Print the Lexicographically Smallest String
Medium

You are given a string s and a robot that currently holds an empty string t. Apply one of the following operations until s and t are both empty:

Remove the first character of a string s and give it to the robot. The robot will append this character to string t.
Remove the last character of a string t and give it to the robot. The robot will write this character on paper.

Return the lexicographically smallest string that can be written on the paper.


Example 1:

Input: s = "zza"
Output: "azz"
Explanation: Let p denote the written string.
Initially p="", s="zza", t="".
Perform first operation three times p="", s="", t="zza".
Perform second operation three times p="azz", s="", t="".

Example 2:

Input: s = "bac"
Output: "abc"
Explanation: Let p denote the written string.
Perform first operation twice p="", s="c", t="ba".
Perform second operation twice p="ab", s="c", t="".
Perform first operation p="ab", s="", t="c".
Perform second operation p="abc", s="", t="".

Example 3:

Input: s = "bdda"
Output: "addb"
Explanation: Let p denote the written string.
Initially p="", s="bdda", t="".
Perform first operation four times p="", s="", t="bdda".
Perform second operation four times p="addb", s="", t="".


Constraints:

1 <= s.length <= 10^5
s consists of only English lowercase letters.

"""

# V0
# IDEA : STACK + SUFFIX MINIMUM — POP WHILE THE TOP CANNOT BE BEATEN
#
#   `t` behaves as a stack. after pushing s[i], the question is whether to
#   write out the top now or wait : waiting only pays off if some character
#   still left in s is STRICTLY smaller than the top.
#
#   so precompute the suffix minimum of s. then, greedily :
#       push s[i], and while the stack top <= min(s[i+1:]), pop and write it
#
#   using <= (not <) is what makes ties flush immediately, which is correct
#   because an equal character later cannot improve the output but delaying
#   can only hurt.
#
# time = O(n), space = O(n)
class Solution(object):
    def robotWithString(self, s):
        n = len(s)
        suffix_min = [None] * (n + 1)
        suffix_min[n] = chr(ord('z') + 1)      # sentinel above every letter
        for i in range(n - 1, -1, -1):
            suffix_min[i] = min(s[i], suffix_min[i + 1])

        out = []
        stack = []
        for i, c in enumerate(s):
            stack.append(c)
            while stack and stack[-1] <= suffix_min[i + 1]:
                out.append(stack.pop())
        while stack:
            out.append(stack.pop())
        return ''.join(out)

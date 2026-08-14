"""

1807. Evaluate the Bracket Pairs of a String
Medium

You are given a string s that contains some bracket pairs, with each pair containing a non-empty key.

For example, in the string "(name)is(age)yearsold", there are two bracket pairs that contain the keys "name" and "age".

You know the values of a wide range of keys. This is represented by a 2D string array knowledge where each knowledge[i] = [keyi, valuei] indicates that key keyi has a value of valuei.

You are tasked to evaluate all of the bracket pairs. When you evaluate a bracket pair that contains some key keyi, you will:

Replace keyi and the bracket pair with the key's corresponding valuei.
If you do not know the value of the key, you will replace keyi and the bracket pair with a question mark "?" (without the quotation marks).

Each key will appear at most once in your knowledge. There will not be any nested brackets in s.

Return the resulting string after evaluating all of the bracket pairs.


Example 1:

Input: s = "(name)is(age)yearsold", knowledge = [["name","bob"],["age","two"]]
Output: "bobistwoyearsold"
Explanation:
The key "name" has a value of "bob", so replace "(name)" with "bob".
The key "age" has a value of "two", so replace "(age)" with "two".

Example 2:

Input: s = "hi(name)", knowledge = [["a","b"]]
Output: "hi?"
Explanation: As you do not know the value of the key "name", replace "(name)" with "?".

Example 3:

Input: s = "(a)(a)(a)aaa", knowledge = [["a","yes"]]
Output: "yesyesyesaaa"
Explanation: The same key can appear multiple times.
The key "a" has a value of "yes", so replace all occurrences of "(a)" with "yes".
Notice that the "a"s not in a bracket pair are not evaluated.


Constraints:

1 <= s.length <= 10^5
0 <= knowledge.length <= 10^5
knowledge[i].length == 2
1 <= keyi.length, valuei.length <= 10
s consists of lowercase English letters and round brackets '(' and ')'.
Every open bracket '(' in s will have a corresponding close bracket ')'.
The key in each bracket pair of s will be non-empty.
There will not be any nested bracket pairs in s.
keyi and valuei consist of lowercase English letters.
Each keyi in knowledge is unique.

"""

# V0
# IDEA : HASH TABLE + ONE LINEAR SCAN (build the output in a list, join once)
#
#   put knowledge into a dict so every lookup is O(1).
#   scan s once:
#     - '(' : find the matching ')', the slice between them is the key,
#             append d.get(key, '?')
#     - otherwise : append the character as is
#
#   NOTE : brackets never nest, so str.find(')') is enough -- no stack needed,
#          and since each ')' is found only once the total scan stays linear.
#   NOTE : accumulate into a list + ''.join, not s += ..., to avoid O(n^2).
#
# time = O(n + m), n = len(s), m = len(knowledge)
# space = O(n + m)
class Solution(object):
    def evaluate(self, s, knowledge):
        d = {}
        for k, v in knowledge:
            d[k] = v

        res = []
        i, n = 0, len(s)
        while i < n:
            if s[i] == '(':
                j = s.find(')', i + 1)
                res.append(d.get(s[i + 1:j], '?'))
                i = j + 1
            else:
                res.append(s[i])
                i += 1
        return ''.join(res)

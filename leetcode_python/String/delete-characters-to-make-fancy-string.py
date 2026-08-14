"""

1957. Delete Characters to Make Fancy String
Easy

A fancy string is a string where no three consecutive characters are equal.

Given a string s, delete the minimum possible number of characters from s to make it fancy.

Return the final string after the deletion. It can be shown that the answer will always be unique.


Example 1:

Input: s = "leeetcode"
Output: "leetcode"
Explanation:
Remove an 'e' from the first group of 'e's to create "leetcode".
No three consecutive characters are equal, so return "leetcode".

Example 2:

Input: s = "aaabaaaa"
Output: "aabaa"
Explanation:
Remove an 'a' from the first group of 'a's to create "aabaaaa".
Remove two 'a's from the second group of 'a's to create "aabaa".
No three consecutive characters are equal, so return "aabaa".

Example 3:

Input: s = "aab"
Output: "aab"
Explanation: No three consecutive characters are equal, so return "aab".


Constraints:

1 <= s.length <= 10^5
s consists only of lowercase English letters.

"""

# V0
# IDEA : GREEDY / SIMULATION (keep at most 2 of every run)
#
#   walk the string once and push each char onto an output list,
#   but skip it when the last TWO kept chars are already the same char.
#
#   NOTE : we compare against the tail of the ANSWER (not of s), which is
#          what makes the greedy correct - after a deletion the "previous
#          two" chars must come from what we actually kept.
#   deleting the minimum == keeping the maximum, and every run of length L
#   can contribute at most 2 chars, so keeping exactly min(L, 2) is optimal.
#
# time = O(n), space = O(n) for the output (O(1) extra)
class Solution(object):
    def makeFancyString(self, s):
        res = []
        for c in s:
            if len(res) >= 2 and res[-1] == c and res[-2] == c:
                continue
            res.append(c)
        return "".join(res)

"""

1698. Number of Distinct Substrings in a String
Medium

Given a string s, return the number of distinct substrings of s.

A substring of a string is obtained by deleting any number of characters (possibly zero) from the
front of the string and any number (possibly zero) from the back of the string.


Example 1:

Input: s = "aabbaba"
Output: 21
Explanation: The set of distinct strings is ["a","b","aa","bb","ab","ba","aab","abb","bab","bba",
"aba","aabb","abba","bbab","baba","aabba","abbab","bbaba","aabbab","abbaba","aabbaba"]

Example 2:

Input: s = "abcdefg"
Output: 28


Constraints:

1 <= s.length <= 500
s consists of lowercase English letters.


Follow up: Can you solve this problem in O(n) time complexity?

"""

# V0
# IDEA : SUFFIX AUTOMATON (answers the O(n) follow-up, not the O(n^2) set dump)
#
#   a suffix automaton of s is a DAG with O(n) states; every state v represents a
#   set of substrings that all end at the same positions, and those substrings are
#   the suffixes of one another with lengths in the range
#       ( len[link[v]] , len[v] ]
#   so state v contributes exactly len[v] - len[link[v]] DISTINCT substrings, and
#   every non-empty substring is counted by exactly one state.
#
#   -> answer = sum over all states v != root of ( len[v] - len[link[v]] )
#
#   NOTE : the "clone" branch inside extend() is what keeps the suffix-link tree
#          consistent when an existing transition is not a direct extension.
#
# time = O(n * A) with A = alphabet size, space = O(n * A)
class Solution(object):
    def countDistinct(self, s):
        # state 0 is the root (empty string)
        link = [-1]
        length = [0]
        trans = [{}]
        last = 0

        for ch in s:
            cur = len(length)
            length.append(length[last] + 1)
            link.append(-1)
            trans.append({})

            p = last
            while p != -1 and ch not in trans[p]:
                trans[p][ch] = cur
                p = link[p]

            if p == -1:
                link[cur] = 0
            else:
                q = trans[p][ch]
                if length[p] + 1 == length[q]:
                    link[cur] = q
                else:
                    clone = len(length)
                    length.append(length[p] + 1)
                    link.append(link[q])
                    trans.append(dict(trans[q]))
                    while p != -1 and trans[p].get(ch) == q:
                        trans[p][ch] = clone
                        p = link[p]
                    link[q] = clone
                    link[cur] = clone
            last = cur

        res = 0
        for v in range(1, len(length)):
            res += length[v] - length[link[v]]
        return res

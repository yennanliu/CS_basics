"""

522. Longest Uncommon Subsequence II
Medium

Given an array of strings strs, return the length of the longest uncommon subsequence between them. If the longest uncommon subsequence does not exist, return -1.

An uncommon subsequence between an array of strings is a string that is a subsequence of one string but not the others.

A subsequence of a string s is a string that can be obtained after deleting any number of characters from s.

For example, "abc" is a subsequence of "aebdc" because you can delete the underlined characters in "aebdc" to get "abc". Other subsequences of "aebdc" include "aebdc", "aeb", and "" (empty string).

Example 1:

Input: strs = ["aba","cdc","eae"]
Output: 3

Example 2:

Input: strs = ["aaa","aaa","aa"]
Output: -1

Constraints:

2 <= strs.length <= 50
1 <= strs[i].length <= 10
strs[i] consists of lowercase English letters.

"""

# V0 

# V1
# http://bookshadow.com/weblog/2017/04/03/leetcode-longest-uncommon-subsequence-ii/
# time = O(n^2 * l), n = len(strs), l = max string length
# space = O(n)
class Solution(object):
    def uncommon(self, parent, child):
        lp, lc = len(parent), len(child)
        pp = pc = 0
        while pp < lp and pc < lc:
            if parent[pp] == child[pc]:
                pc += 1
            pp += 1
        return pc != lc
    def findLUSlength(self, strs):
        """
        :type strs: List[str]
        :rtype: int
        """
        cnt = collections.Counter(strs)
        slist = sorted(set(strs), key=len, reverse=True)
        for i, c in enumerate(slist):
            if cnt[c] > 1: continue
            if all(self.uncommon(p, c) for p in slist[:i]):
                return len(c)
        return -1
          
# V2
# time = O(n^2 * l), n = len(strs), l = max string length
# space = O(1)
class Solution(object):
    def findLUSlength(self, strs):
        """
        :type strs: List[str]
        :rtype: int
        """
        def isSubsequence(a, b):
            i = 0
            for j in range(len(b)):
                if i >= len(a):
                    break
                if a[i] == b[j]:
                    i += 1
            return i == len(a)

        strs.sort(key=len, reverse=True)
        for i in range(len(strs)):
            all_of = True
            for j in range(len(strs)):
                if len(strs[j]) < len(strs[i]):
                    break
                if i != j and isSubsequence(strs[i], strs[j]):
                    all_of = False
                    break
            if all_of:
                return len(strs[i])
        return -1

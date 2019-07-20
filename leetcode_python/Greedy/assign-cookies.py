# V0 

# V1 
# https://blog.csdn.net/fuxuemingzhu/article/details/54177627
# IDEA : GREEDY 
# sort two list firstly, then for loop greed list 
# and move both index -> index + 1 if cookie > greed
# only move greed's index -> index + 1 if cookie < greed 
class Solution:
    def findContentChildren(self, g, s):
        """
        :type g: List[int]
        :type s: List[int]
        :rtype: int
        """
        g.sort() # g : greed
        s.sort() # s : size  (cookie)
        sp = 0
        res = 0
        for gi in g:
            while sp < len(s) and s[sp] < gi: # if cookie < greed -> s.index += 1 
                sp += 1
            if sp < len(s) and s[sp] >= gi:   # if cookie > greed ->  g.index += 1 and s.index += 1 
                res += 1
                sp += 1
        return res

# V2 
# Time:  O(nlogn)
# Space: O(1)
class Solution(object):
    def findContentChildren(self, g, s):
        """
        :type g: List[int]
        :type s: List[int]
        :rtype: int
        """
        g.sort()
        s.sort()

        result, i = 0, 0
        for j in range(len(s)):
            if i == len(g):
                break
            if s[j] >= g[i]:
                result += 1
                i += 1
        return result

# V0 
class Solution:
    """
    @param n: the length of strobogrammatic number
    @return: All strobogrammatic numbers
    """
    def findStrobogrammatic(self, n):
        evenMidCandidate = ["11","69","88","96", "00"]
        oddMidCandidate = ["0", "1", "8"]
        if n == 0:
            return [""]
        if n == 1:
            return oddMidCandidate
        if n == 2:
            return evenMidCandidate[:-1]
        #if n % 2:
        if n % 2 == 1:
            pre, midCandidate = self.findStrobogrammatic(n-1), oddMidCandidate
        else: 
            pre, midCandidate = self.findStrobogrammatic(n-2), evenMidCandidate
        premid = ( n - 1 ) / 2
        return [p[:premid] + c + p[premid:] for c in midCandidate for p in pre]

# V1
# https://www.jiuzhang.com/solution/strobogrammatic-number-ii/#tag-highlight-lang-python
# IDEA : RECURSION  
# EXAMPLE 
# n = 1 -> ans =   0, 1, 8 
# n = 2 -> ans =   11, 69, 88, 96, 00
# n = 3 -> ans =   1?1, 6?9, 8?8, 9?6, 0?0, for ? in [0,1,8]
# n = 4 -> ans =   11??11, 11??69, 11??88 .... for ?? in [11, 69, 88, 96, 00]
# .
# .
class Solution:
    """
    @param n: the length of strobogrammatic number
    @return: All strobogrammatic numbers
    """
    def findStrobogrammatic(self, n):
        evenMidCandidate = ["11","69","88","96", "00"]
        oddMidCandidate = ["0", "1", "8"]
        if n == 0:
            return [""]
        if n == 1:
            return oddMidCandidate
        if n == 2:
            return evenMidCandidate[:-1]
        #if n % 2:
        if n % 2 == 1:
            # n is odd 
            pre, midCandidate = self.findStrobogrammatic(n-1), oddMidCandidate
        else: 
            # n is even 
            pre, midCandidate = self.findStrobogrammatic(n-2), evenMidCandidate
        premid = ( n - 1 ) / 2
        return [p[:premid] + c + p[premid:] for c in midCandidate for p in pre]

# V1' 
# https://www.cnblogs.com/grandyang/p/5200919.html
# http://www.voidcn.com/article/p-rltfcrmu-zo.html
from copy import copy
class Solution(object):
    def findStrobogrammatic(self, n):
        """
        :type n: int
        :rtype: List[str]
        """
        s = ['0', '1', '8', '6', '9']
        d = {'0': '0', '1': '1', '6': '9', '8': '8', '9': '6'}
        ret1, ret2 = [""], []
        if n == 0:
            return ret1
            
        mid = n / 2
        i = 0
        while i < mid:
            if i == 0:
                for j in range(1, len(s)):
                    ret2.append(s[j] + d[s[j]])
            else:
                for j in ret1:
                    for k in s:
                        ret2.append(j[:i] + k + d[k] + j[i:])
            ret1 = copy(ret2)
            ret2 = []
            i += 1
            
        if n % 2 != 0:
            for j in ret1:
                for k in range(3):
                    ret2.append(j[:i] + s[k] + j[i:])
            ret1 = copy(ret2)
                    
        return ret1

# V2 
# Time:  O(n^2 * 5^(n/2))
# Space: O(n)
class Solution(object):
    lookup = {'0':'0', '1':'1', '6':'9', '8':'8', '9':'6'}

    # @param {integer} n
    # @return {string[]}
    def findStrobogrammatic(self, n):
        return self.findStrobogrammaticRecu(n, n)

    def findStrobogrammaticRecu(self, n, k):
        if k == 0:
            return ['']
        elif k == 1:
            return ['0', '1', '8']

        result = []
        for num in self.findStrobogrammaticRecu(n, k - 2):
            for key, val in self.lookup.iteritems():
                if n != k or key != '0':
                    result.append(key + num + val)

        return result
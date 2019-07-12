# V0 

# V1 
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
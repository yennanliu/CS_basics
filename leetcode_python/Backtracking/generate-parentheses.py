# V0

# V0'
# https://blog.csdn.net/fuxuemingzhu/article/details/79362373
# IDEA: BACKTRACKING + DFS 
# NOTE : KEEP DFS WHEN MEAT 2 CONDTIONS:
#  1) len(path) < n 
#  2) # of "("  > # of ")" (means it's still possible to form a "paratheses" as expected)
class Solution(object):
    def generateParenthesis(self, n):
        res = []
        self.dfs(res, n, n, '')
        return res
        
    def dfs(self, res, left, right, path):
        if left == 0 and right == 0:
            res.append(path)
            return
        if left > 0:
            self.dfs(res, left - 1, right, path + '(')
        if left < right:
            self.dfs(res, left, right - 1, path + ')')

# V1 
# https://blog.csdn.net/nxhyd/article/details/72514987
# https://blog.csdn.net/fuxuemingzhu/article/details/79362373
class Solution(object):
    def generateParenthesis(self, n):
        """
        :type n: int
        :rtype: List[str]
        """
        res = []
        self.dfs(res, n, n, '')
        return res
        
    def dfs(self, res, left, right, path):
        if left == 0 and right == 0:
            res.append(path)
            return
        if left > 0:
            self.dfs(res, left - 1, right, path + '(')
        if left < right:
            self.dfs(res, left, right - 1, path + ')')

# V1'
# https://blog.csdn.net/nxhyd/article/details/72514987
class Solution(object):
    def generateParenthesis(self, n):
        """
        :type n: int
        :rtype: List[str]
        """
        def generate(p, left, right):
            if right >= left >= 0:
                if not right:
                    yield p
                for q in generate(p + '(', left - 1, right) : yield q
                for q in generate(p + ')', left, right - 1) : yield q
        return list(generate('', n, n))

# V1''
# https://blog.csdn.net/nxhyd/article/details/72514987
class Solution(object):
    def deep(self,tot,ln,rn,strs,lis):
        if ln<tot:
            self.deep(tot,ln+1,rn,strs+'(',lis)
        if rn<tot and rn<ln:
            self.deep(tot,ln,rn+1,strs+')',lis)
        if ln==tot and rn==tot:
            lis.append(strs)

    def generateParenthesis(self, n):
        """
        :type n: int
        :rtype: List[str]
        """
        lis=[]
        self.deep(n,0,0,'',lis)
        return lis

# V1'''
# https://blog.csdn.net/nxhyd/article/details/72514987
class Solution(object):
    def generateParenthesis(self, n):
        """
        :type n: int
        :rtype: List[str]
        """
        def genrate(p,left,right,parens=[]):
            if left:
                genrate(p+ '(',left-1,right)
            if right>left:
                genrate(p+')',left,right-1)
            if not right:
                parens+=p,
            return parens
        return genrate('',n, n)

# V2 
# Time:  O(4^n / n^(3/2)) ~= Catalan numbers
# Space: O(n)
class Solution(object):
    # @param an integer
    # @return a list of string
    def generateParenthesis(self, n):
        result = []
        self.generateParenthesisRecu(result, "", n, n)
        return result

    def generateParenthesisRecu(self, result, current, left, right):
        if left == 0 and right == 0:
            result.append(current)
        if left > 0:
            self.generateParenthesisRecu(result, current + "(", left - 1, right)
        if left < right:
            self.generateParenthesisRecu(result, current + ")", left, right - 1)
"""

22. Generate Parentheses
Medium

Given n pairs of parentheses, write a function to generate all combinations of well-formed parentheses.

 

Example 1:

Input: n = 3
Output: ["((()))","(()())","(())()","()(())","()()()"]
Example 2:

Input: n = 1
Output: ["()"]
 

Constraints:

1 <= n <= 8

"""

# V0
# IDEA : bracktrack + Valid Parentheses (LC 020)
class Solution(object):
    def generateParenthesis(self, n):
        # help func for backtracking
        def help(tmp):
            if len(tmp) == n * 2 and check(tmp):
                res.append(tmp)
                return
            if len(tmp) == n * 2:
                return
            """
            NOTE !!! via "for l in _list" + recursion, we can for "endless" for loop over element in _list till terminate confition is met
            """
            for l in _list:
                print ("l = " + str(l))
                help(tmp + l) ### NOTE !!! we HAVE TO use "tmp + l" instead of "tmp += l"
                # below is OK as well
                #_tmp = tmp + l
                #help(_tmp)

        """
        LC 020 Valid Parentheses
        """
        def check(s):
            lookup = {"(":")", "[":"]", "{":"}"}
            q = []
            for i in s:
                if i not in lookup and len(q) == 0:
                    return False
                elif i in lookup:
                    q.append(i)
                else:
                    tmp = q.pop()
                    if lookup[tmp] != i:
                        return False
            return True if len(q) == 0 else False

        _list = ['(', ')']
        if n == 1:
            return ["()"]
        res = []
        tmp = ""
        help(tmp)
        return res

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
# IDEA : DFS
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
# BRUTE FORCE
# https://leetcode.com/problems/generate-parentheses/solution/
class Solution(object):
    def generateParenthesis(self, n):
        def generate(A = []):
            if len(A) == 2*n:
                if valid(A):
                    ans.append("".join(A))
            else:
                A.append('(')
                generate(A)
                A.pop()
                A.append(')')
                generate(A)
                A.pop()

        def valid(A):
            bal = 0
            for c in A:
                if c == '(': bal += 1
                else: bal -= 1
                if bal < 0: return False
            return bal == 0

        ans = []
        generate()
        return ans

# V1''
# https://leetcode.com/problems/generate-parentheses/solution/
# BRACKTRACKING
class Solution:
    def generateParenthesis(self, n: int) -> List[str]:
        ans = []
        def backtrack(S = [], left = 0, right = 0):
            if len(S) == 2 * n:
                ans.append("".join(S))
                return
            if left < n:
                S.append("(")
                backtrack(S, left+1, right)
                S.pop()
            if right < left:
                S.append(")")
                backtrack(S, left, right+1)
                S.pop()
        backtrack()
        return ans

# V1'''
# https://leetcode.com/problems/generate-parentheses/solution/
# Closure Number
class Solution(object):
    def generateParenthesis(self, N):
        if N == 0: return ['']
        ans = []
        for c in xrange(N):
            for left in self.generateParenthesis(c):
                for right in self.generateParenthesis(N-1-c):
                    ans.append('({}){}'.format(left, right))
        return ans

# V1'''''
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

# V1''''''
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

# V1''''''
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
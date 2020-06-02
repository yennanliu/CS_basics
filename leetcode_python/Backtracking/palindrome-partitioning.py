# V0 
class Solution(object):
    def partition(self, s):
        res = []
        self.helper(s, res, [])
        return res
        
    def helper(self, s, res, path):
        if not s:
            res.append(path)
            return
        # beware of the start and the end index
        for i in range(1, len(s) + 1): 
            if self.isPalindrome(s[:i]):
                """
                ### backtrcking 
                if s[:i] is palindrome, then check if there is palindrome in s[i:] as well
                e.g.  
                    a a b b a 
                  => 
                    if 'aa' (<-) is palindrome, then check a b b a (->)
                """
                self.helper(s[i:], res, path + [s[:i]])

    def isPalindrome(self, x):
        return x == x[::-1]

# V1 
# https://blog.csdn.net/fuxuemingzhu/article/details/79574462
# IDEA : BACKTRACKING 
# NOTE :
# In [12]: x=[1,2,3]
# In [13]: for i in range(1,len(x)+1):
#     ...:     print (i)
#     ...:     
# 1
# 2
# 3
class Solution(object):
    def partition(self, s):
        """
        :type s: str
        :rtype: List[List[str]]
        """
        self.isPalindrome = lambda s : s == s[::-1]
        res = []
        self.helper(s, res, [])
        return res
        
    def helper(self, s, res, path):
        if not s:
            res.append(path)
            return
        # beware of the start and the end index
        for i in range(1, len(s) + 1): 
            if self.isPalindrome(s[:i]):
                self.helper(s[i:], res, path + [s[:i]])

### Test case:
s=Solution()
assert s.partition("aab")==[['a', 'a', 'b'], ['aa', 'b']]
assert s.partition("")==[[]]
assert s.partition("a")==[['a']]
assert s.partition("ab")==[['a', 'b']]
assert s.partition(" b")==[[' ', 'b']]
assert s.partition("abc")==[['a', 'b', 'c']]
assert s.partition("abcbc")==[['a', 'b', 'c', 'b', 'c'], ['a', 'b', 'cbc'], ['a', 'bcb', 'c']]
assert s.partition("aaa")==[['a', 'a', 'a'], ['a', 'aa'], ['aa', 'a'], ['aaa']]

# V1'
# https://leetcode.com/problems/palindrome-partitioning/discuss/42100/Python-easy-to-understand-backtracking-solution.
# IDEA : DFS + backtracking
class Solution(object):
    def partition(self, s):
        res = []
        self.dfs(s, [], res)
        return res
        
    def dfs(self, s, path, res):
        if not s: # backtracking
            res.append(path)
        for i in range(1, len(s)+1):
            if self.isPar(s[:i]):
                self.dfs(s[i:], path+[s[:i]], res)
                
    def isPar(self, s):
        return s == s[::-1]

# V1''
# https://leetcode.com/problems/palindrome-partitioning/discuss/441529/backtracking-python
# IDEA : BACKTRACKING + DFS + DP
class Solution(object):
    def partition(self, s):
        """
        :type s: str
        :rtype: List[List[str]]
        """
        dp = [[False for _ in range(len(s))] for _ in range(len(s))]
        for j in range(len(s)):
            dp[j][j] = True
            for i in range(j):
                if s[i]==s[j]:
                    dp[i][j] = (j-i==1) or (dp[i+1][j-1])
    
        res = []
        def dfs(start, curpath):
            if start>=len(s):
                res.append(curpath)
            for i in range(start, len(s)):
                if dp[start][i] is True:
                    dfs(i+1, curpath+[s[start:i+1]])
        dfs(0, [])
        return res

# V1'''
# https://leetcode.com/problems/palindrome-partitioning/discuss/42025/1-liner-Python-Ruby
class Solution(object):
    def partition(self, s):
        return [[s[:i]] + rest
            for i in range(1, len(s)+1)
            if s[:i] == s[i-1::-1]
            for rest in self.partition(s[i:])] or [[]]

# V2 
# Time:  O(n^2 ~ 2^n)
# Space: O(n^2)
class Solution(object):
    # @param s, a string
    # @return a list of lists of string
    def partition(self, s):
        n = len(s)

        is_palindrome = [[0 for j in range(n)] for i in range(n)]
        for i in reversed(range(0, n)):
            for j in range(i, n):
                is_palindrome[i][j] = s[i] == s[j] and ((j - i < 2 ) or is_palindrome[i + 1][j - 1])

        sub_partition = [[] for i in range(n)]
        for i in reversed(range(n)):
            for j in range(i, n):
                if is_palindrome[i][j]:
                    if j + 1 < n:
                        for p in sub_partition[j + 1]:
                            sub_partition[i].append([s[i:j + 1]] + p)
                    else:
                        sub_partition[i].append([s[i:j + 1]])

        return sub_partition[0]

# Time:  O(2^n)
# Space: O(n)
# recursive solution
class Solution2(object):
    # @param s, a string
    # @return a list of lists of string
    def partition(self, s):
        result = []
        self.partitionRecu(result, [], s, 0)
        return result

    def partitionRecu(self, result, cur, s, i):
        if i == len(s):
            result.append(list(cur))
        else:
            for j in range(i, len(s)):
                if self.isPalindrome(s[i: j + 1]):
                    cur.append(s[i: j + 1])
                    self.partitionRecu(result, cur, s, j + 1)
                    cur.pop()

    def isPalindrome(self, s):
        for i in range(len(s) / 2):
            if s[i] != s[-(i + 1)]:
                return False
        return True
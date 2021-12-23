"""

77. Combinations
Medium

Given two integers n and k, return all possible combinations of k numbers out of the range [1, n].

You may return the answer in any order.


Example 1:

Input: n = 4, k = 2
Output:
[
  [2,4],
  [3,4],
  [2,3],
  [1,2],
  [1,3],
  [1,4],
]
Example 2:

Input: n = 1, k = 1
Output: [[1]]
 

Constraints:

1 <= n <= 20
1 <= k <= n

"""

# V0 

# V1
# https://leetcode.com/problems/combinations/discuss/1222840/Python-backtracking-simple-soln
# IDEA : BACKTRACK
class Solution:
    def combine(self, n, k):
        res=[]
        def go(i,ma,ans):
            if ma==k:
                res.append(list(ans))
                return
            if i>n:
                return
            ans.append(i)
            go(i+1,ma+1,ans)
            ans.pop()
            go(i+1,ma,ans)
        go(1,0,[])
        return res

# V1''
# https://leetcode.com/problems/combinations/discuss/286994/python
# IDEA : BACKTRACK + dfs
class Solution(object):
    def combine(self, n, k):
        result = []
        
        def dfs(current, start):
            if(len(current) == k):
                result.append(current[:])
                return
            
            for i in range(start, n + 1):
                current.append(i)
                dsf(current, i + 1)
                current.pop()
            
        dfs([], 1)
        return result

# V1'''
# https://leetcode.com/problems/combinations/discuss/170834/Python-solution
# IDEA : iteration
class Solution(object):
    def combine(self, n, k):
        """
        :type n: int
        :type k: int
        :rtype: List[List[int]]
        """
        if k == 0:
            return []
        elif k == 1:
            return [[i] for i in range(1,n+1)]
        res = []
        for tail in range(n,k-1,-1):
            tmp = self.combine(tail-1, k-1)
            for i in range(len(tmp)):
                tmp[i].append(tail)
            res += tmp
        return res          

# V1''''
# https://leetcode.com/problems/combinations/discuss/170834/Python-solution
# IDEA : iteration
class Solution(object):
    def combine(self, n, k):
        if k == 0:
            return [[]]
        return [x+[tail] for tail in range(n,k-1,-1) for x in self.combine(tail-1, k-1)]

# V1''''''
# https://blog.csdn.net/fuxuemingzhu/article/details/79515180
# IDEA :  RECURSION 
# DEMO : 
# n, k = 4, 2 
# In [35]: Solution().combine(n,k)
# k : 2 path : [] array : range(1, 5)
# k : 1 path : [1] array : range(2, 5)
# k : 0 path : [1, 2] array : range(3, 5)
# k : 1 path : [1] array : range(3, 5)
# k : 0 path : [1, 3] array : range(4, 5)
# k : 1 path : [1] array : range(4, 5)
# k : 0 path : [1, 4] array : range(5, 5)
# k : 1 path : [1] array : range(5, 5)
# k : 2 path : [] array : range(2, 5)
# k : 1 path : [2] array : range(3, 5)
# k : 0 path : [2, 3] array : range(4, 5)
# k : 1 path : [2] array : range(4, 5)
# k : 0 path : [2, 4] array : range(5, 5)
# k : 1 path : [2] array : range(5, 5)
# k : 2 path : [] array : range(3, 5)
# k : 1 path : [3] array : range(4, 5)
# k : 0 path : [3, 4] array : range(5, 5)
# k : 1 path : [3] array : range(5, 5)
# k : 2 path : [] array : range(4, 5)
# Out[35]: [[1, 2], [1, 3], [1, 4], [2, 3], [2, 4], [3, 4]]
class Solution(object):
    def combine(self, n, k):
        """
        :type n: int
        :type k: int
        :rtype: List[List[int]]
        """
        res = []
        self.helper(range(1, n + 1), k, res, []) # the number is starting from 1. i.e. 1.2..... n 
        return res
    
    def helper(self, array, k, res, path):
        if k > len(array):
            return
        if k == 0:
            res.append(path)
        else:
            self.helper(array[1:], k - 1, res, path + [array[0]])
            self.helper(array[1:], k, res, path)

# V1''''''
# https://blog.csdn.net/fuxuemingzhu/article/details/79515180
# IDEA : BACKTRACKING 
class Solution(object):
    def combine(self, n, k):
        """
        :type n: int
        :type k: int
        :rtype: List[List[int]]
        """
        res = []
        self.helper(range(1, n + 1), k, res, [])
        return res
    
    def helper(self, array, k, res, path):
        if k > len(array):
            return
        if k == 0:
            res.append(path)
        else:
            for i in range(len(array)):
                self.helper(array[i + 1:], k - 1, res, path + [array[i]])

# V2 
# Time:  O(k * C(n, k))
# Space: O(k)
class Solution(object):
    def combine(self, n, k):
        """
        :type n: int
        :type k: int
        :rtype: List[List[int]]
        """
        result, combination = [], []
        i = 1
        while True:
            if len(combination) == k:
                result.append(combination[:])
            if len(combination) == k or \
               len(combination)+(n-i+1) < k:
                if not combination:
                    break
                i = combination.pop()+1
            else:
                combination.append(i)
                i += 1
        return result


class Solution2(object):
    def combine(self, n, k):
        """
        :type n: int
        :type k: int
        :rtype: List[List[int]]
        """
        def combineDFS(n, start, intermediate, k, result):
            if k == 0:
                result.append(intermediate[:])
                return
            for i in range(start, n):
                intermediate.append(i+1)
                combineDFS(n, i+1, intermediate, k-1, result)
                intermediate.pop()

        result = []
        combineDFS(n, 0, [], k, result)
        return result
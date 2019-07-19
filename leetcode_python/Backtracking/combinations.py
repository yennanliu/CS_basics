# V0 

# V1 
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

# V1' 
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
"""

60. Permutation Sequence
Hard

The set [1, 2, 3, ..., n] contains a total of n! unique permutations.

By listing and labeling all of the permutations in order, we get the following sequence for n = 3:

"123"
"132"
"213"
"231"
"312"
"321"

Given n and k, return the k^th permutation sequence.

Example 1:

Input: n = 3, k = 3
Output: "213"

Example 2:

Input: n = 4, k = 9
Output: "2314"

Example 3:

Input: n = 3, k = 1
Output: "123"

Constraints:

1 <= n <= 9
1 <= k <= n!

"""

# V0
# time = O(n^2)  # n pops from a list, each O(n)
# space = O(n)
class Solution:
    def getPermutation(self, n, k):
        baselist = [i + 1 for i in range(n)]
        numperm = 1
        for i in range(1, n + 1):
            numperm *= i
        string = ""
        for i in range(n):
            # i-th character
            numperm /= n - i
            idx = int((k - 1) / numperm)
            val = baselist.pop(idx)
            string += str(val)
            k -= idx * numperm
        return string

# V1
# https://leetcode.com/problems/permutation-sequence/discuss/562480/Python-O(n)-solution
# time = O(n^2)  # n pops from a list, each O(n)
# space = O(n)
class Solution:
    def getPermutation(self, n: int, k: int) -> str:
        # the list contains elements when digit = n 
        baselist = [i + 1 for i in range(n)]
        # init numperm as 1 
        # for factorial calculation
        # e.g. : 3! = 2*3*4 = 24
        numperm = 1
        for i in range(1, n + 1):
            numperm *= i
        string = ""
        for i in range(n):
            # i-th character
            numperm /= n - i
            idx = int((k - 1) / numperm)
            # get the corresponding element from baselist 
            val = baselist.pop(idx)
            # add it to result
            string += str(val)
            # update k
            k -= idx * numperm
        return string

### Test case
s=Solution()
assert s.getPermutation(0,1) == ""
assert s.getPermutation(0,3) == ""
assert s.getPermutation(0,100) == ""
assert s.getPermutation(1,1) == "1"
assert s.getPermutation(0,0) == ""
assert s.getPermutation(3,1) == "123"
assert s.getPermutation(3,2) == "132"
assert s.getPermutation(3,4) == "231"
assert s.getPermutation(3,0) == "132"
assert s.getPermutation(3,-1) == "312"
assert s.getPermutation(3,-2) == "321"

# V1'
# https://blog.csdn.net/fuxuemingzhu/article/details/80658810
# http://bangbingsyb.blogspot.com/2014/11/leetcode-permutation-sequence.html
# time = O(n^2)  # n pops from a list, each O(n)
# space = O(n)
class Solution(object):
    def getPermutation(self, n, k):
        """
        :type n: int
        :type k: int
        :rtype: str
        """
        ans = ''
        fact = [1] * n
        num = [str(i) for i in range(1, 10)]
        for i in range(1, n):
            fact[i] = fact[i - 1] * i
        k -= 1
        for i in range(n, 0, -1):
            first = k // fact[i - 1]
            k %= fact[i - 1]
            ans += num[first]
            num.pop(first)
        return ans
    
# V2
# time = O(n^2)
# space = O(n)
import math
# Cantor ordering solution
class Solution(object):
    def getPermutation(self, n, k):
        """
        :type n: int
        :type k: int
        :rtype: str
        """
        seq, k, fact = "", k - 1, math.factorial(n - 1)
        perm = [i for i in range(1, n + 1)]
        for i in reversed(range(n)):
            curr = perm[k / fact]
            seq += str(curr)
            perm.remove(curr)
            if i > 0:
                k %= fact
                fact /= i
        return seq
"""

70. Climbing Stairs
Easy

You are climbing a staircase. It takes n steps to reach the top.

Each time you can either climb 1 or 2 steps. In how many distinct ways can you climb to the top?

 

Example 1:

Input: n = 2
Output: 2
Explanation: There are two ways to climb to the top.
1. 1 step + 1 step
2. 2 steps
Example 2:

Input: n = 3
Output: 3
Explanation: There are three ways to climb to the top.
1. 1 step + 1 step + 1 step
2. 1 step + 2 steps
3. 2 steps + 1 step
 

Constraints:

1 <= n <= 45

"""

# V0
# IDEA : RECURSION + MEMORIZATION
# https://leetcode.com/explore/learn/card/recursion-i/255/recursion-memoization/1662/
class Solution(object):
    def climbStairs(self, n):
        cache = {}
        def help(n):
            if n in cache:
                return cache[n]
            if n <= 2:
                res = n
            else:
                res = help(n-2) + help(n-1)
            cache[n] = res
            return res
        return help(n)

# V0' 
class Solution:
    # Time:  O(2^n)
    # Space: O(n)
    def climbStairs(self, n):
        if n == 1:
            return 1
        if n == 2:
            return 2
        return self.climbStairs(n - 1) + self.climbStairs(n - 2)

# V1 
# https://blog.csdn.net/coder_orz/article/details/51506414
# n = 1,2,3,4,5,6,7,8,9... -> output : 1,2,3,5,8,13,21,34....
# -> output(n) = output(n-2) + output(n-1)
# IDEA  : Fibonacci SERIES 
class Solution:
    def climbStairs(self, n):
        prev, current = 0, 1 
        for i in range(n):
            prev, current =  current, prev+current
        return current


# V1' 
# https://blog.csdn.net/coder_orz/article/details/51506414
# IDEA : DP 
# RECURSION WOULD BE TOO SLOW, SO USE DP SPEED UP THE PROCESS (DP CAN RECORD HISTORY)
class Solution(object):
    def climbStairs(self, n):
        """
        :type n: int
        :rtype: int
        """
        if n == 0 or n == 1 or n == 2:
            return n
        steps = [1, 1]
        for i in range(2, n+1):
            steps.append(steps[i-1] + steps[i-2])
        return steps[n]

# V2 
class Solution:
    """
    :type n: int
    :rtype: int
    """
    def climbStairs(self, n):
        prev, current = 0, 1
        for i in range(n):
            prev, current = current, prev + current,
        return current

    # Time:  O(2^n)
    # Space: O(n)
    def climbStairs1(self, n):
        if n == 1:
            return 1
        if n == 2:
            return 2
        return self.climbStairs(n - 1) + self.climbStairs(n - 2)

# V3 
# Time:  O(logn)
# Space: O(1)
import itertools
class Solution(object):
    def climbStairs(self, n):
        """
        :type n: int
        :rtype: int
        """
        def matrix_expo(A, K):
            result = [[int(i==j) for j in range(len(A))] \
                      for i in range(len(A))]
            while K:
                if K % 2:
                    result = matrix_mult(result, A)
                A = matrix_mult(A, A)
                K /= 2
            return result

        def matrix_mult(A, B):
            ZB = list(zip(*B))
            return [[sum(a*b for a, b in zip(row, col)) \
                     for col in ZB] for row in A]

        T = [[1, 1],
             [1, 0]]
        return matrix_expo(T, n)[0][0]

# Time:  O(n)
# Space: O(1)
class Solution2(object):
    """
    :type n: int
    :rtype: int
    """
    def climbStairs(self, n):
        prev, current = 0, 1
        for i in range(n):
            prev, current = current, prev + current,
        return current

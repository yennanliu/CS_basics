"""

204. Count Primes
Medium

Given an integer n, return the number of prime numbers that are strictly less than n.

 

Example 1:

Input: n = 10
Output: 4
Explanation: There are 4 prime numbers less than 10, they are 2, 3, 5, 7.
Example 2:

Input: n = 0
Output: 0
Example 3:

Input: n = 1
Output: 0
 

Constraints:

0 <= n <= 5 * 106
Accepted
550.1K
Submissions
1.7M

"""

# V0
# IDEA : set
# https://leetcode.com/problems/count-primes/discuss/1343795/python%3A-sieve-of-eretosthenes
# prinme(x) : check if x is a prime
# prinme(0) = 0
# prinme(1) = 0
# prinme(2) = 0
# prinme(3) = 1
# prinme(4) = 2
# prinme(5) = 3
class Solution:
    def countPrimes(self, n):
        # using sieve of eretosthenes algorithm
        if n < 2: return 0
        nonprimes = set()
        for i in range(2, round(n**(1/2))+1):
            if i not in nonprimes:
                for j in range(i*i, n, i):
                    nonprimes.add(j)
        return n - len(nonprimes) - 2  # remove prinme(1), prime(2)

# V0' 
class Solution(object):
    def countPrimes(self, n):
        if n <= 2:
            return 0
        ### NOTE : We need cache for optimization here,
        #          e.g. if already calculated (prime or not), we should read
        #               that from cache (is_prime), rather than re-caculation
        is_prime = [True] * n
        num = n / 2
        for i in range(3, n, 2):
            if i * i >= n:
                break
            if not is_prime[i]:
                continue
            for j in range(i*i, n, 2*i):
                if not is_prime[j]:
                    continue
                num -= 1
                is_prime[j] = False
        return num

# V0'': -> to fix
# class Solution(object):
#     def countPrimes(self, n):
#         def check(x):
#             _count = 0
#             for i in range(1, int(x**(0.5))+1):
#                 print (i)
#                 if x % i == 0:
#                     _count += 1
#                 if _count >= 2:
#                     break
#             return True if _count >= 2 else False
#
#         res = []
#         cache = [0,0,0,1]
#
#         if n <= 3:
#             return cache[n]
#         for i in range(3,n):
#             print ("i = " + str(i) + " check(i) = " + str(check(i)))
#             _tmp = cache[-1]
#             if not check(i):
#                 _new = 1
#             else:
#                 _new = 0
#             _tmp += _new
#             cache.append(_tmp)
#
#         print ("cache = " + str(cache))
#         return cache[-1]

# V1
# IDEA : set
# https://leetcode.com/problems/count-primes/discuss/1343795/python%3A-sieve-of-eretosthenes
# prinme(x) : check if x is a prime
# prinme(0) = 0
# prinme(1) = 0
# prinme(2) = 0
# prinme(3) = 1
# prinme(4) = 2
# prinme(5) = 3
class Solution:
    def countPrimes(self, n):
        # using sieve of eretosthenes algorithm
        if n < 2: return 0
        nonprimes = set()
        for i in range(2, round(n**(1/2))+1):
            if i not in nonprimes:
                for j in range(i*i, n, i):
                    nonprimes.add(j)
        return n - len(nonprimes) - 2  # remove prinme(1), prime(2)

# V1'
# IDEA : DICT
# https://leetcode.com/problems/count-primes/discuss/1343795/python%3A-sieve-of-eretosthenes
class Solution:
    def countPrimes(self, n):
        # using sieve of eretosthenes algorithm
        if n < 2: return 0
        nonprimes = {}
        for i in range(2, round(n**(1/2))+1):
            if i not in nonprimes:
                for j in range(i*i, n, i):
                    nonprimes[j] = 1
        return n - len(nonprimes) - 2

# V1''
# IDEA : python style
# https://leetcode.com/problems/count-primes/discuss/1343795/python%3A-sieve-of-eretosthenes
class Solution:
    def countPrimes(self, n):
        # using sieve of eretosthenes algorithm
        if n < 3: return 0
        primes = [1]*n
        primes[0] = primes[1] = 0
        for i in range(2, round(n**(1/2)+1)):
            if primes[i]:
                primes[i*i: n: i] = len(primes[i*i: n: i])*[0]
        return sum(primes)

# V1''' 
# https://www.codetd.com/article/1947747
# http://bookshadow.com/weblog/2015/04/27/leetcode-count-primes/
# https://blog.csdn.net/github_39261590/article/details/73864039
# IDEA : MATH THEORY : Sieve of Eratosthenes
#
# https://en.wikipedia.org/wiki/Sieve_of_Eratosthenes
# IDEA : SUB ARRAY WITH MULTIPLIER 
# In [29]: x = [ i for i in range(31)]
# In [30]: x
# Out[30]: 
# [0,
#  1,
#  2,
#  3,
#  4,
#  5,
#  6,
#  7,
#  8,
#  9,
#  10,
#  11,
#  12,
#  13,
#  14,
#  15,
#  16,
#  17,
#  18,
#  19,
#  20,
#  21,
#  22,
#  23,
#  24,
#  25,
#  26,
#  27,
#  28,
#  29,
#  30]
# In [31]: x[2*2 :: 2]
# Out[31]: [4, 6, 8, 10, 12, 14, 16, 18, 20, 22, 24, 26, 28, 30]
# In [32]: x[3*3 :: 3]
# Out[32]: [9, 12, 15, 18, 21, 24, 27, 30]
class Solution:
# @param {integer} n
# @return {integer}
    def countPrimes(self, n):
        if n < 3:
            return 0
        primes = [True] * n
        primes[0] = primes[1] = False
        for i in range(2, int(n ** 0.5) + 1):
            if primes[i]:
                primes[i * i:: i] = [False] * len(primes[i * i:: i])
        return sum(primes)
        
# V1' 
# https://blog.csdn.net/github_39261590/article/details/73864039
# IDEA : GREEDY (TLE, time out exception)
import math
class Solution:
    def countPrimes(self, n):
        count=0

        def judge_prime(w):
            sqrt_w=int(math.sqrt(w))
            for i in range(2,sqrt_w+1):
                if x%i==0: return 0
            return 1

        for x in range(2,n):
            count=count+judge_prime(x)
        return count

# V1''
# https://www.jiuzhang.com/solution/count-primes/#tag-highlight-lang-python
class Solution:
    """
    @param n: a integer
    @return: return a integer
    """
    def countPrimes(self, n):
        # write your code here
        if n <= 1:
            return False
        res = 0
        not_prime = [False] * n
        for i in range(2, n):
            if not_prime[i] == False:
                res += 1
                for j in range(2, n):
                    if j * i >= n:
                        break
                    not_prime[j * i] = True
        return res

# V2 
# Time:  O(n)
# Space: O(n)
class Solution(object):
    # @param {integer} n
    # @return {integer}
    def countPrimes(self, n):
        if n <= 2:
            return 0

        is_prime = [True] * n
        num = n / 2
        for i in range(3, n, 2):
            if i * i >= n:
                break

            if not is_prime[i]:
                continue

            for j in range(i*i, n, 2*i):
                if not is_prime[j]:
                    continue

                num -= 1
                is_prime[j] = False

        return num

    def countPrimes2(self, n):
        """
        :type n: int
        :rtype: int
        """
        if n < 3:
            return 0
        primes = [True] * n
        primes[0] = primes[1] = False
        for i in range(2, int(n ** 0.5) + 1):
            if primes[i]:
                primes[i * i: n: i] = [False] * len(primes[i * i: n: i])
        return sum(primes)
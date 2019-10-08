# Time:  O(n)
# Space: O(n)
# Description:
#
# Count the number of prime numbers less than a non-negative number, n
#
# Hint: The number n could be in the order of 100,000 to 5,000,000.


# V0 

# V1 
# https://www.codetd.com/article/1947747
# http://bookshadow.com/weblog/2015/04/27/leetcode-count-primes/
# https://blog.csdn.net/github_39261590/article/details/73864039
# IDEA : MATH THEORY : Sieve of Eratosthenes

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
# IDEA : GREEDY (TLE, time limit exceeded possibly)
import math
def countPrimes(n):
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
"""

866. Prime Palindrome
Medium

Given an integer n, return the smallest prime palindrome greater than or equal to n.

An integer is prime if it has exactly two divisors: 1 and itself. Note that 1 is not a prime number.

For example, 2, 3, 5, 7, 11, and 13 are all primes.

An integer is a palindrome if it reads the same from left to right as it does from right to left.

For example, 101 and 12321 are palindromes.

The test cases are generated so that the answer always exists and is in the range [2, 2 * 10^8].

Example 1:

Input: n = 6
Output: 7

Example 2:

Input: n = 8
Output: 11

Example 3:

Input: n = 13
Output: 101

Constraints:

1 <= n <= 10^8

"""

# V0

# V1 : dev 

# class Solution(object):
#     def primePalindrome(self, N):

#         def isPalindrome(self, x):
#             if x < 0:
#                 return False
#             x = str(x)
#             # if x is like 12321  (odd length)
#             if str(x)[:math.floor(len(str(x))/2)] == x[math.floor(len(str(x))/2)+1:][::-1]:
#                 return True
#             # if x is like 2222 (even length)
#             if str(x)[:math.floor(len(str(x))/2)] == x[math.floor(len(str(x))/2):][::-1]:
#                 return True
#             else:
#                 return False

#             def is_prime(n):
#                 if n < 2 or n % 2 == 0:
#                     return n == 2
#                 return all(n % d for d in xrange(3, int(n**.5) + 1, 2))

#         if 8 <= N <= 11:
#             return 11
#         for i in xrange(10**(len(str(N))//2), 10**5):
#             j = int(str(i) + str(i)[-2::-1])
#             if j >= N and is_prime(j):
#                 return j


# V2
# https://www.kancloud.cn/kancloud/data-structure-and-algorithm-notes/73059
### DFS algorithm ###
# time = O(n * 2^n)  # n = len(s), enumerate all partitions
# space = O(n)  # recursion stack depth
class Solution:
    # @param s, a string
    # @return a list of lists of string
    def partition(self, s):
        result = []
        if not s:
            return result

        palindromes = []
        self.dfs(s, 0, palindromes, result)
        return result

    def dfs(self, s, pos, palindromes, ret):
        if pos == len(s):
            ret.append([] + palindromes)
            return

        for i in range(pos + 1, len(s) + 1):
            if not self.isPalindrome(s[pos:i]):
                continue

            palindromes.append(s[pos:i])
            self.dfs(s, i, palindromes, ret)
            palindromes.pop()

    def isPalindrome(self, s):
        if not s:
            return False
        # reverse compare
        return s == s[::-1]

# V3
# time = O(n^(1/2) * (logn + n^(1/2)))
# space = O(logn)
class Solution(object):
    def primePalindrome(self, N):
        """
        :type N: int
        :rtype: int
        """
        def is_prime(n):
            if n < 2 or n % 2 == 0:
                return n == 2
            # all() func in python 
            # https://www.jianshu.com/p/65b6b4a62071
            return all(n % d for d in range(3, int(n**.5) + 1, 2))

        if 8 <= N <= 11:
            return 11
        for i in range(10**(len(str(N))//2), 10**5):
            j = int(str(i) + str(i)[-2::-1])
            if j >= N and is_prime(j):
                return j

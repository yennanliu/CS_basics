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
# Time:  O(n^(1/2) * (logn + n^(1/2)))
# Space: O(logn)
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

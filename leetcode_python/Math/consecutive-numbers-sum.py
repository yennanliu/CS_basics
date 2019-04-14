# V1  : dev (time limit exceeded error )
# sum of arithmetic sequence 
# https://en.wikipedia.org/wiki/Arithmetic_progression
# Sn = n(a1+an)/2 =  n(2*a1+ (n-1)d)/2 
# idea 
# so the trick of this problem is to find if there are N, and a can make the sum exist 
# --> Sn = n(2*a1+ (n-1)d)/2  = a1*n + (n)(n-1)/2   # d = 1 
# so the approach is : go through every n and find if there exist valid Sn

# class Solution(object):
# 	def consecutiveNumbersSum(self, N):
# 		count = 0 
# 		first_term = 1
# 		# go through all possible "number of terms" n
# 		for first_term in range(1, N+1):
# 			for n in range(1, N+1):
# 				if first_term + (n-1) > N:
# 					break 
# 				if first_term*n + ((n)*(n-1))/2  == N:
# 					count = count + 1 
# 		return count 

# V2 
# https://zhanghuimeng.github.io/post/leetcode-829-consecutive-numbers-sum/
# http://bookshadow.com/weblog/2018/05/06/leetcode-consecutive-numbers-sum/
class Solution(object):
    def consecutiveNumbersSum(self, N):
        """
        :type N: int
        :rtype: int
        """
        ans = c = 0
        while True:
            c += 1
            if N / c < c / 2 + c % 2:
                break
            if c % 2 and N % c == 0:
                ans += 1
            elif c % 2 == 0 and (N / c) * c + c / 2 == N:
                ans += 1
        return ans

# V3 
# Time:  O(sqrt(n))
# Space: O(1)
class Solution(object):
    def consecutiveNumbersSum(self, N):
        """
        :type N: int
        :rtype: int
        """
        # x + x+1 + x+2 + ... + x+l-1 = N = 2^k * M, where M is odd
        # => l*x + (l-1)*l/2 = 2^k * M
        # => x = (2^k * M -(l-1)*l/2)/l= 2^k * M/l - (l-1)/2 is integer
        # => l could be 2 or any odd factor of M (excluding M) 
        #    s.t. x = 2^k * M/l - (l-1)/2 is integer, and also unique
        # => the answer is the number of all odd factors of M
        # if prime factorization of N is 2^k * p1^a * p2^b * ..
        # => answer is the number of all odd factors = (a+1) * (b+1) * ...
        result = 1
        while N % 2 == 0:
            N /= 2
        i = 3
        while i*i <= N:
            count = 0
            while N % i == 0:
                N /= i
                count += 1
            result *= count+1
            i += 2
        if N > 1:
            result *= 2
        return result

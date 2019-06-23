# leetcode 625. Minimum Factorization

# Given a positive integer a, find the smallest positive integer b whose multiplication of each digit equals to a.
# If there is no answer or the answer is not fit in 32-bit signed integer, then return 0.

# Example 1 
# Input:

# 48 
# Output: 
# 68 
# Example 2 
# Input:

# 15 
# Output: 
# 35



# V1 
# idea :
# please notice the "each digit" term in the problem description 
class Solution(object):
	def smallestFactorization(self, a):
		if a ==1:
			return 1 
		for i in range(2,10):
			if a%i == 0:
				q = int(a/i)
				ans = min((a*10+i), (i*10)+a)  
				if ans <= 2**31:   # 2**31 or 0x7FFFFFFF : 32-bit signed integer
					return ans
				return 0  		
		return 0 

# V2 
# http://bookshadow.com/weblog/2017/06/18/leetcode-minimum-factorization/
# https://blog.csdn.net/feifeiiong/article/details/73556747
class Solution(object):
    def smallestFactorization(self, a):
        """
        :type a: int
        :rtype: int
        """
        if a == 1: return 1
        cnt = [0] * 10
        for x in range(9, 1, -1):
            while a % x == 0:
                cnt[x] += 1
                a /= x
        if a > 1: return 0
        ans = int(''.join(str(n) * cnt[n] for n in range(2, 10)))
        return ans <= 0x7FFFFFFF and ans or 0 


# V3 
# Time:  O(loga)
# Space: O(1)
class Solution(object):
    def smallestFactorization(self, a):
        """
        :type a: int
        :rtype: int
        """
        if a < 2:
            return a
        result, mul = 0, 1
        for i in reversed(range(2, 10)):
            while a % i == 0:
                a /= i
                result = mul*i + result
                mul *= 10
        return  result if a == 1 and result < 2**31 else 0


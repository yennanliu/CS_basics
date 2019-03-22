# V1 : dev 


# V2 
# (a ⋅ b) mod m  = [(a mod m) ⋅ (b mod m)] 

# e.g. 1) 2^23 = (2^2)10 * 2^3 

# e.g. 2) 2**30%1337 = 1335
# Solution().superPow(2,[3,0]) = 1335 

# e.g. 3) 2**40%1337 = 387 
# Solution().superPow(2,[4,0]) = 387 

# https://www.hrwhisper.me/leetcode-super-pow/
# http://bookshadow.com/weblog/2016/07/07/leetcode-super-pow/
class Solution(object):
    def superPow(self, a, b):
        """
        :type a: int
        :type b: List[int]
        :rtype: int
        """
        ans = 1
        mod = 1337
        for bi in b[::-1]:
            ans = ans * a ** bi % mod
            a = a ** 10 % mod
        return ans

# V3 
# Time:  O(n), n is the size of b.
# Space: O(1)
class Solution(object):
    def superPow(self, a, b):
        """
        :type a: int
        :type b: List[int]
        :rtype: int
        """
        def myPow(a, n, b):
            result = 1
            x = a % b
            while n:
                if n & 1:
                    result = result * x % b
                n >>= 1
                x = x * x % b
            return result % b

        result = 1
        for digit in b:
            result = myPow(result, 10, 1337) * myPow(a, digit, 1337) % 1337
        return result

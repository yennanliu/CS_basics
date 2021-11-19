"""

762. Prime Number of Set Bits in Binary Representation
Easy

Given two integers left and right, return the count of numbers in the inclusive range [left, right] having a prime number of set bits in their binary representation.

Recall that the number of set bits an integer has is the number of 1's present when written in binary.

For example, 21 written in binary is 10101, which has 3 set bits.
 

Example 1:

Input: left = 6, right = 10
Output: 4
Explanation:
6  -> 110 (2 set bits, 2 is prime)
7  -> 111 (3 set bits, 3 is prime)
8  -> 1000 (1 set bit, 1 is not prime)
9  -> 1001 (2 set bits, 2 is prime)
10 -> 1010 (2 set bits, 2 is prime)
4 numbers have a prime number of set bits.
Example 2:

Input: left = 10, right = 15
Output: 5
Explanation:
10 -> 1010 (2 set bits, 2 is prime)
11 -> 1011 (3 set bits, 3 is prime)
12 -> 1100 (2 set bits, 2 is prime)
13 -> 1101 (3 set bits, 3 is prime)
14 -> 1110 (3 set bits, 3 is prime)
15 -> 1111 (4 set bits, 4 is not prime)
5 numbers have a prime number of set bits.
 

Constraints:

1 <= left <= right <= 106
0 <= right - left <= 104

"""

# V0 
class Solution(object):
    def countPrimeSetBits(self, L, R):
        primes = [2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31]
        res = 0
        for num in range(L, R + 1):
            if bin(num).count("1") in primes:
                res += 1
        return res

# V0' : IDEA : check prime + brute force
class Solution(object):
    def countPrimeSetBits(self, left, right):
        def check_prime(x):
            if x <= 1:
                return False
            for i in range(2, int(x**(0.5)+1)):
                if x % i == 0:
                    return False
            return True
        _count = 0
        for i in range(left, right+1):
            tmp = bin(i)[2:].count('1')
            if check_prime(tmp):
                _count += 1
        return _count

# V1 
# https://blog.csdn.net/fuxuemingzhu/article/details/79089092
# IDEA : SPACE -> TIME
# THE QUESTIONS ASK US TO  # OF ( # OF 1 IS PRIME NUMBER ) WITHIN (L,R) INTERVAL 
# SINCE THE MAX DIGIT OF BINARY NUMBER IS 32
# SO WE ONLY SAVE ALL PRIME NUMBRS WITHIN 1 - 32  (primes = [2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31])
# AND CHECK IF THE INPUT IN THE LIST 
class Solution(object):
    def countPrimeSetBits(self, L, R):
        """
        :type L: int
        :type R: int
        :rtype: int
        """
        primes = [2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31]
        res = 0
        for num in range(L, R + 1):
            if bin(num).count("1") in primes:
                res += 1
        return res

### Test case:
s=Solution()
assert s.countPrimeSetBits(6,10) == 4 
assert s.countPrimeSetBits(0,0) == 0
assert s.countPrimeSetBits(0,1) == 0
assert s.countPrimeSetBits(1,1) == 0
assert s.countPrimeSetBits(2,2) == 0 
assert s.countPrimeSetBits(10,10) == 1
assert s.countPrimeSetBits(10,2) == 0 
assert s.countPrimeSetBits(2,200) == 121 

# V1'
# https://blog.csdn.net/fuxuemingzhu/article/details/79089092
import math
class Solution(object):
    def isPrime(self, num):
        if num == 1:
            return 0
        elif num == 2:
            return 1
        for i in xrange(2, int(math.sqrt(num))+1):
            if num % i == 0:
                return 0
        return 1
    def countPrimeSetBits(self, L, R):
        """
        :type L: int
        :type R: int
        :rtype: int
        """
        return sum(self.isPrime(bin(num)[2:].count('1')) for num in xrange(L, R+1))

# V1'' 
# https://blog.csdn.net/fuxuemingzhu/article/details/79089092
class Solution(object):
    def countPrimeSetBits(self, L, R):
        isPrime = lambda num : 0 if ((num == 1) or (num % 2 == 0 and num > 2)) else int(all(num % i for i in xrange(3, int(num ** 0.5) + 1, 2)))
        return sum(isPrime(bin(num)[2:].count('1')) for num in xrange(L, R+1))

# V1'''
# https://leetcode.com/problems/prime-number-of-set-bits-in-binary-representation/solution/
# IDEA : DIRECT
class Solution(object):
    def countPrimeSetBits(self, L, R):
        primes = {2, 3, 5, 7, 11, 13, 17, 19}
        return sum(bin(x).count('1') in primes
                   for x in range(L, R+1))

# V2
# Time:  O(log(R - L)) = O(1)
# Space: O(1)
class Solution(object):
    def countPrimeSetBits(self, L, R):
        """
        :type L: int
        :type R: int
        :rtype: int
        """
        def bitCount(n):
            result = 0
            while n:
                n &= n-1
                result += 1
            return result

        primes = {2, 3, 5, 7, 11, 13, 17, 19}
        return sum(bitCount(i) in primes
                   for i in range(L, R+1))

"""

263. Ugly Number
Easy

An ugly number is a positive integer whose prime factors are limited to 2, 3, and 5.

Given an integer n, return true if n is an ugly number.

 

Example 1:

Input: n = 6
Output: true
Explanation: 6 = 2 × 3
Example 2:

Input: n = 1
Output: true
Explanation: 1 has no prime factors, therefore all of its prime factors are limited to 2, 3, and 5.
Example 3:

Input: n = 14
Output: false
Explanation: 14 is not ugly since it includes the prime factor 7.
 

Constraints:

-231 <= n <= 231 - 1

"""

# V0
# IDEA: MATH (GPT)
class Solution(object):
    def isUgly(self, n):
        """
        :type n: int
        :rtype: bool
        """
        if n <= 0:
            return False

        """
        NOTE !!!


        ```
        for ...
            while ....
        ```
        """
        for x in [2, 3, 5]:
            while n % x == 0:
                n //= x

        return n == 1


# V0-1
# IDEA: MATH (GEMINI)
class Solution(object):
    def isUgly(self, n):
        """
        :type n: int
        :rtype: bool
        """
        # 1. Edge case: Ugly numbers are strictly positive
        if n <= 0:
            return False
            
        # 2. Greedily divide out 2s, 3s, and 5s
        for p in [2, 3, 5]:
            while n % p == 0:
                n //= p
                
        # 3. If we are left with exactly 1, it only had prime factors of 2, 3, or 5.
        return n == 1


# V0-2
# IDEA: MATH (GPT)
class Solution(object):
    def isUgly(self, n):
        if n <= 0:
            return False

        if n == 1:
            return True

        nums = [2, 3, 5]

        while True:
            if n == 1:
                return True

            divided = False

            for x in nums:
                if n % x == 0:
                    n //= x
                    divided = True
                    break   # restart checking from 2

            if not divided:
                return False



# V0
# time = O(log n)
# space = O(1)
class Solution(object):
    def isUgly(self, n):
        # edge case
        if n == 0:
            return False
        if n == 1:
            return True
        flag = True
        while flag:
            #print ("n = " + str(n))
            if n == 0 or  n == 1:
                return True
            if n % 2 == 0 or n % 3 == 0 or n % 5 == 0:
                #print (">>>>")
                if n % 2 == 0:
                    a, b = divmod(n, 2)
                    flag = True
                    n = a
                elif n % 3 == 0:
                    a, b = divmod(n, 3)
                    flag = True
                    n = a
                elif n % 5 == 0:
                    a, b = divmod(n, 5)
                    flag = True
                    n = a
            else:
                flag = False
        return False

# V1
# time = O(log n)
# space = O(1)
class Solution(object):
	# @param {integer} num
	# @return {boolean}
	def isUgly(self, num):
		if num ==0:
			return False	
		while (num%2 ==0 or num%3 ==0 or num%5 ==0):
			if num%2 ==0:
				num = int(num/2)
			if num%3 ==0:
				num = int(num/3)
			if num%5 ==0:
				num = int(num/5)
		if num ==1:
			return True
		else:
			return False

# V2
# time = O(logn) = O(1)
# space = O(1)
class Solution(object):
    # @param {integer} num
    # @return {boolean}
    def isUgly(self, num):
        if num == 0:
            return False
        for i in [2, 3, 5]:
            while num % i == 0:
                num /= i
        return num == 1
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
# Time:  O(logn) = O(1)
# Space: O(1)
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
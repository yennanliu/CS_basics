"""

470. Implement Rand10() Using Rand7()
Medium

Given the API rand7() that generates a uniform random integer in the range [1, 7], write a function rand10() that generates a uniform random integer in the range [1, 10]. You can only call the API rand7(), and you shouldn't call any other API. Please do not use a language's built-in random API.

Each test case will have one internal argument n, the number of times that your implemented function rand10() will be called while testing. Note that this is not an argument passed to rand10().

Example 1:

Input: n = 1
Output: [2]

Example 2:

Input: n = 2
Output: [2,8]

Example 3:

Input: n = 3
Output: [3,8,10]

Constraints:

1 <= n <= 10^5

Follow up:

What is the expected value for the number of calls to rand7() function?
Could you minimize the number of calls to rand7()?

"""

# V0

# V1 
# idea : 
# to make a random number as N * 10 form 
# then the  mod 10 outout  (output%10) is the desired answer.

# class Solution(object):
# 	import random
# 	def rand7():
# 		return random.randint(1, 7)
# 	def rand10(self):
# 		output = (rand7())**2
# 		if output > 40:
# 			output = output - 40 
# 		return output%10


# V2 
# https://blog.csdn.net/fuxuemingzhu/article/details/81809478

# The rand7() API is already defined for you.
# def rand7():
# @return a random integer in the range 1 to 7
# time = O(1)  # expected, rejection sampling
# space = O(1)
class Solution:
    def rand10(self):
        """
        :rtype: int
        """
        return self.rand40() % 10 + 1
        
    def rand49(self):
        """
        random integer in 0 ~ 48
        """
        return 7 * (rand7() - 1) + rand7() - 1
        
    def rand40(self):
        """
        random integer in 0 ~ 40
        """
        num = self.rand49()
        while num >= 40:
            num = self.rand49()
        return num


# V3
# time = O(1.189), counted by statistics, limit would be O(log10/log7) = O(1.183)
# space = O(1)

import random
def rand7():
    return random.randint(1, 7)

# Reference: https://leetcode.com/problems/implement-rand10-using-rand7/discuss/151567/C++JavaPython-Average-1.199-Call-rand7-Per-rand10
class Solution(object):
    def __init__(self):
        self.__cache = []

    def rand10(self):
        """
        :rtype: int
        """
        def generate(cache):
            n = 32
            curr = sum((rand7()-1) * (7**i) for i in range(n))
            rang = 7**n
            while curr < rang//10*10:
                cache.append(curr%10+1)
                curr /= 10
                rang /= 10

        while not self.__cache:
            generate(self.__cache)
        return self.__cache.pop()

# V3'
# time = O(2 * (1 + (9/49) + (9/49)^2 + ...)) = O(2/(1-(9/49)) = O(2.45)
# space = O(1)
class Solution2(object):
    def rand10(self):
        """
        :rtype: int
        """
        while True:
            x = (rand7()-1)*7 + (rand7()-1)
            if x < 40:
                return x%10 + 1
"""

390. Elimination Game
Medium

You have a list arr of all integers in the range [1, n] sorted in a strictly increasing order. Apply the following algorithm on arr:

Starting from left to right, remove the first number and every other number afterward until you reach the end of the list.
Repeat the previous step again, but this time from right to left, remove the rightmost number and every other number from the remaining numbers.
Keep repeating the steps again, alternating left to right and right to left, until a single number remains.

Given the integer n, return the last number that remains in arr.

Example 1:

Input: n = 9
Output: 6
Explanation:
arr = [1, 2, 3, 4, 5, 6, 7, 8, 9]
arr = [2, 4, 6, 8]
arr = [2, 6]
arr = [6]

Example 2:

Input: n = 1
Output: 1

Constraints:

1 <= n <= 10^9

"""

# V0

# V1 
# class Solution(object):
# 	def lastRemaining(self, n):
# 		mylist = [ i for i in range(1,n+1)]
# 		for i in range(n):
# 			print ('i :', i)
# 			print ('mylist :', mylist)
# 			if len(mylist) <= 2:
# 				if len(mylist) <= 1:
# 					return mylist[0]
# 				if i%2==0:
# 					return mylist[1]
# 				else:
# 					return mylist[0]
# 				return mylist[0]
# 			elif n%2 == 0:
# 				mylist = mylist[1::2]        
# 			else:
# 				mylist = mylist[::-1][1::2][::-1]
            
# 		return mylist



# V2 
# http://bookshadow.com/weblog/2016/08/28/leetcode-elimination-game/
# time = O(log n)  # n = n
# space = O(1)
class Solution(object):
    def lastRemaining(self, n):
        """
        :type n: int
        :rtype: int
        """
        a = p = 1
        cnt = 0
        while n > 1:
            n /= 2
            cnt += 1
            p *= 2
            if cnt % 2:
                a += p / 2 + p * (n - 1)
            else:
                a -= p / 2 + p * (n - 1)
        return a


# V3
# time = O(logn)
# space = O(1)
class Solution(object):
    def lastRemaining(self, n):
        """
        :type n: int
        :rtype: int
        """
        start, step, direction = 1, 2, 1
        while n > 1:
            start += direction * (step * (n/2) - step/2)
            n /= 2
            step *= 2
            direction *= -1
        return start
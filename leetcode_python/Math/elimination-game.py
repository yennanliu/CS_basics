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
# Time:  O(logn)
# Space: O(1)
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
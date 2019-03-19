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

# V3 
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
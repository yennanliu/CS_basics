# V1 
# idea :
# find a minimum N such that  
# [ ]1[ ]2[ ]3[ ]...[ ]N = target 
# [ ] can be "+" or "-"
# so the approach of this method is :
# keep walking on the "positive" direction until 
# 1) current = target 
# 2) target == current +  N*2  (since walk to the "negative" direction = walk positve direction +  (-2)*(positve direction ))
class Solution:
	def reachNumber(self, target):
		step = 0 
		current = 0 
		target =abs(target)
		if target == 1:
			return 1 
		# plz notice the condition setting below 
		# reference idea from solution V2 
		while (target - current)%2 != 0 or current < target:
			step = step + 1 
			current = current + step 		
		return step 

# V2 
# https://blog.csdn.net/dpengwang/article/details/83038144
class Solution:
    def reachNumber(self, target):
        """
        :type target: int
        :rtype: int
        """
        target =abs(target)
        step = 0
        sum =0
        while(sum <target):
            step+=1
            sum +=step
        while((sum -target)%2!=0):
            step +=1
            sum +=step
        return step


# V3 
# Time:  O(logn)
# Space: O(1)
import math
class Solution(object):
    def reachNumber(self, target):
        """
        :type target: int
        :rtype: int
        """
        target = abs(target)
        k = int(math.ceil((-1+math.sqrt(1+8*target))/2))
        target -= k*(k+1)/2
        return k if target%2 == 0 else k+1+k%2

# V3' 
# Time:  O(sqrt(n))
# Space: O(1)
class Solution2(object):
    def reachNumber(self, target):
        """
        :type target: int
        :rtype: int
        """
        target = abs(target)
        k = 0
        while target > 0:
            k += 1
            target -= k
        return k if target%2 == 0 else k+1+k%2

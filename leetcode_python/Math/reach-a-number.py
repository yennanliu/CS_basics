"""

754. Reach a Number
Solved
Medium
Topics
premium lock icon
Companies
You are standing at position 0 on an infinite number line. There is a destination at position target.

You can make some number of moves numMoves so that:

On each move, you can either go left or right.
During the ith move (starting from i == 1 to i == numMoves), you take i steps in the chosen direction.
Given the integer target, return the minimum number of moves required (i.e., the minimum numMoves) to reach the destination.

 

Example 1:

Input: target = 2
Output: 3
Explanation:
On the 1st move, we step from 0 to 1 (1 step).
On the 2nd move, we step from 1 to -1 (2 steps).
On the 3rd move, we step from -1 to 2 (3 steps).
Example 2:

Input: target = 3
Output: 2
Explanation:
On the 1st move, we step from 0 to 1 (1 step).
On the 2nd move, we step from 1 to 3 (2 steps).
 

Constraints:

-109 <= target <= 109
target != 0


"""
# V0
# IDEA: MATH (GPT)
# time = O(sqrt(target))
# space = O(1)
class Solution(object):
    def reachNumber(self, target):
        target = abs(target)

        moves = 0
        total = 0

        while total < target or (total - target) % 2 != 0:
            moves += 1
            total += moves

        return moves


# V0-1
# IDEA: MATH (gemini)
# time = O(sqrt(target))
# space = O(1)
class Solution(object):
    def reachNumber(self, target):
        """
        :type target: int
        :rtype: int
        """
        # Step 1: Use symmetry. Reaching -target takes the same moves as +target
        target = abs(target)
        
        cur = 0
        moves = 0
        i = 1
        
        # Step 2: Greedily accumulate steps forward until we meet or pass the target
        while cur < target:
            cur += i
            moves += 1
            i += 1
            
        # Step 3: Check the overshoot difference (delta = cur - target)
        # If the delta is odd, we must keep moving forward until the delta becomes even.
        # Turning an earlier '+x' step into a '-x' step reduces 'cur' by exactly 2x (an even amount).
        while (cur - target) % 2 != 0:
            cur += i
            moves += 1
            i += 1
            
        return moves



# V1 
# idea :
# find a minimum N such that  
# [ ]1[ ]2[ ]3[ ]...[ ]N = target 
# [ ] can be "+" or "-"
# so the approach of this method is :
# keep walking on the "positive" direction until
# 1) current = target
# 2) target == current +  N*2  (since walk to the "negative" direction = walk positve direction +  (-2)*(positve direction ))
# time = O(sqrt(target))
# space = O(1)
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
# time = O(sqrt(target))
# space = O(1)
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
# time = O(logn)
# space = O(1)
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
# time = O(sqrt(n))
# space = O(1)
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

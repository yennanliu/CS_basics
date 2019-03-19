# V1 : DEV 

# V2 
# https://blog.csdn.net/baidu_23318869/article/details/50386323
# Time:  O(1)
# Space: O(1)
import math
class Solution(object):
    def bulbSwitch(self, n):
        """
        type n: int
        rtype: int
        """
        # The number of full squares.
        return int(math.sqrt(n))
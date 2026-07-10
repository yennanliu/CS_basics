# V1
# time = O(1)
# space = O(1)
class Solution(object):
    def canWinNim(self, n):
        """
        :type n: int
        :rtype: bool
        """
        if n % 4 ==0:
        	return False
        else:
        	return True 

# V2
# time = O(1)
# space = O(1)
class Solution(object):
    def canWinNim(self, n):
        """
        :type n: int
        :rtype: bool
        """
        return n % 4 != 0



# V1 
class Solution(object):
	def largestPerimeter(self, A):
		A.sort()
		for i in list(reversed(list(range(len(A)-2)))):
			if ( A[i] + A[i+1] > A[i+2] and 
			 A[i+2] + A[i+1] > A[i] and
			 A[i] + A[i+2] > A[i+1] ):
				return A[i] + A[i+1] + A[i+2]
		return 0 



# V2 
# Time:  O(nlogn)
# Space: O(1)

class Solution(object):
    def largestPerimeter(self, A):
        """
        :type A: List[int]
        :rtype: int
        """
        A.sort()
        for i in reversed(range(len(A) - 2)):
            if A[i] + A[i+1] > A[i+2]:
                return A[i] + A[i+1] + A[i+2]
        return 0
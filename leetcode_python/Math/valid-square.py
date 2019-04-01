# V1  : dev 
# class Solution(object):
# 	def validSquare(self, p1, p2, p3, p4):
# 		def get_length(p1,p2):
# 			return (p1[0]-p2[0])**2 + (p1[1]-p2[1])**2   
# 		length_sq = 	min( get_length(p1,p2), get_length(p1,p3))	
# 		print (length_sq)
# 		count = 0 
# 		for pair in [[p1,p3], [p1,p4], [p2,p3], [p3,p4]]:
# 			if ( length_sq != get_length(pair[0], pair[1]) or  length_sq != 2*get_length(pair[0], pair[1])):
# 				count = count + 1 
# 				if count > 1:
# 					return False
# 		return True 


# V3 
# Time:  O(1)
# Space: O(1)
class Solution(object):
    def validSquare(self, p1, p2, p3, p4):
        """
        :type p1: List[int]
        :type p2: List[int]
        :type p3: List[int]
        :type p4: List[int]
        :rtype: bool
        """
        def dist(p1, p2):
            return (p1[0] - p2[0]) ** 2 + (p1[1] - p2[1]) ** 2

        lookup = set([dist(p1, p2), dist(p1, p3),\
                      dist(p1, p4), dist(p2, p3),\
                      dist(p2, p4), dist(p3, p4)])
        return 0 not in lookup and len(lookup) == 2


# V1 
# class Solution(object): 
# 	def maxCount(self, m, n, ops):
# 		if ops ==[]:
# 			return (min(m,n))**2 
# 		max_int_array_x, max_int_array_y = m, n 
# 		output = []
# 		#[ (max_int_array_x.append([ min(i[0],m)  for i in ops]), max_int_array_y.append([min(i[1],n)  for i in ops])) for i in ops ]
# 		#[ output.append(min(max_int_array_x, i[0])*min(max_int_array_y, i[1])) for i in ops ]
# 		[ (max_int_array_x=min(m, op[0])) for i in ops ]
# 		print (max_int_array_x)
# 		return min(output)


# V2 
# Time:  O(p), p is the number of ops
# Space: O(1)
class Solution(object):
    def maxCount(self, m, n, ops):
        """
        :type m: int
        :type n: int
        :type ops: List[List[int]]
        :rtype: int
        """
        for op in ops:
            m = min(m, op[0])
            n = min(n, op[1])
        return m*n
"""

598. Range Addition II
Easy

You are given an m x n matrix M initialized with all 0's and an array of operations ops, where ops[i] = [a_i, b_i] means M[x][y] should be incremented by one for all 0 <= x < a_i and 0 <= y < b_i.

Count and return the number of maximum integers in the matrix after performing all the operations.

Example 1:

https://assets.leetcode.com/uploads/2020/10/02/ex1.jpg

Input: m = 3, n = 3, ops = [[2,2],[3,3]]
Output: 4
Explanation: The maximum integer in M is 2, and there are four of it in M. So return 4.

Example 2:

Input: m = 3, n = 3, ops = [[2,2],[3,3],[3,3],[3,3],[2,2],[3,3],[3,3],[3,3],[2,2],[3,3],[3,3],[3,3]]
Output: 4

Example 3:

Input: m = 3, n = 3, ops = []
Output: 9

Constraints:

1 <= m, n <= 4 * 10^4
0 <= ops.length <= 10^4
ops[i].length == 2
1 <= a_i <= m
1 <= b_i <= n

"""

# V0

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
# time = O(p), p is the number of ops
# space = O(1)
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
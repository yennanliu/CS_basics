"""
Given an integer n, return an array ans of length n + 1 such that for each i (0 <= i <= n), 
ans[i] is the number of 1's in the binary representation of i.


Example 1:

Input: n = 2
Output: [0,1,1]
Explanation:
0 --> 0
1 --> 1
2 --> 10

Example 2:

Input: n = 5
Output: [0,1,1,2,1,2]
Explanation:
0 --> 0
1 --> 1
2 --> 10
3 --> 11
4 --> 100
5 --> 101
 

Constraints:

0 <= n <= 105
 

Follow up:

It is very easy to come up with a solution with a runtime of O(n log n). Can you do it in linear time O(n) and possibly in a single pass?
Could you solve it in O(n) space complexity?
Can you do it without using any built-in function (i.e., like __builtin_popcount in C++)?

"""

# V0
class Solution:
    def countBits(self,string_num):
        result = []
        for i in range(string_num + 1):
            result.append(self.get_one_from_binar(i))
        return result

    def get_one_from_binar(self, x):
        tmp = bin(x)
        return tmp.count('1') 

# V1 
class Solution(object):
	# python dec to bin 
	# https://stackoverflow.com/questions/3528146/convert-decimal-to-binary-in-python
	# https://www.programiz.com/python-programming/examples/decimal-binary-recursion
	def dec2bin(self,string_num):
		base = [str(x) for x in range(10)] 
		num = int(string_num)
		mid = []
		while True:
			if num == 0: 
				break
			num,rem = divmod(num, 2)
			mid.append(base[rem])
		return ''.join([str(x) for x in mid[::-1]])

	def countBits(self, num):
		num_list = [x for x in range(num+1)]
		return [ Solution().dec2bin(i).count('1') for i in num_list ]


# V1'
class Solution(object):
	def dec2bin(self,x):
		return int(bin(x)[2:])
	def countBits(self, num):
		num_list = [x for x in range(num+1)]
		return [ str(Solution().dec2bin(i)).count('1') for i in num_list ]

# V2 
# Time:  O(n)
# Space: O(n)
class Solution(object):
    def countBits(self, num):
        """
        :type num: int
        :rtype: List[int]
        """
        res = [0]
        for i in range(1, num + 1):
            # Number of 1's in i = (i & 1) + number of 1's in (i / 2).
            res.append((i & 1) + res[i >> 1])
        return res

    def countBits2(self, num):
        """
        :type num: int
        :rtype: List[int]
        """
        s = [0]
        while len(s) <= num:
            s.extend([x + 1 for x in s])
        return s[:num + 1]

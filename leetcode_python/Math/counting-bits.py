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

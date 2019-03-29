# V1 
class Solution(object):
	import random 

	def __init__(self, n_rows, n_cols):
		"""
		:type n_rows: int
		:type n_cols: int
		"""
		self.M = n_rows
		self.N = n_cols
		self.total = self.M * self.N
		self.fliped = set()

	def flip(self):
		randon_x =  random.randint(0, self.total - 1)
		randon_y =  random.randint(0, self.total - 1)
		return [randon_x, randon_y]


# # V2 
# # https://blog.csdn.net/fuxuemingzhu/article/details/83188258
# class Solution(object):

#     def __init__(self, n_rows, n_cols):
#         """
#         :type n_rows: int
#         :type n_cols: int
#         """
#         self.M = n_rows
#         self.N = n_cols
#         self.total = self.M * self.N
#         self.fliped = set()

#     def flip(self):
#         """
#         :rtype: List[int]
#         """
#         pos = random.randint(0, self.total - 1)
#         while pos in self.fliped:
#             pos = random.randint(0, self.total - 1)
#         self.fliped.add(pos)
#         return [pos / self.N, pos % self.N]

#     def reset(self):
#         """
#         :rtype: void
#         """
#         self.fliped = set()
# # Your Solution object will be instantiated and called as such:
# # obj = Solution(n_rows, n_cols)
# # param_1 = obj.flip()
# # obj.reset()

# # V2'
# # https://blog.csdn.net/fuxuemingzhu/article/details/83188258
# class Solution(object):

#     def __init__(self, n_rows, n_cols):
#         """
#         :type n_rows: int
#         :type n_cols: int
#         """
#         self.M = n_rows
#         self.N = n_cols
#         self.total = self.M * self.N
#         self.d = dict()

#     def flip(self):
#         """
#         :rtype: List[int]
#         """
#         r = random.randint(0, self.total - 1)
#         self.total -= 1
#         x = self.d.get(r, r)
#         self.d[r] = self.d.get(self.total, self.total)
#         # print(r, x, self.total, self.d)
#         return [x / self.N, x % self.N]
        

#     def reset(self):
#         """
#         :rtype: void
#         """
#         self.d.clear()
#         self.total = self.M * self.N




# V3 
# Time:  ctor:  O(1)
#        flip:  O(1)
#        reset: O(min(f, r * c))
# Space: O(min(f, r * c))
import random
class Solution(object):

    def __init__(self, n_rows, n_cols):
        """
        :type n_rows: int
        :type n_cols: int
        """
        self.__n_rows = n_rows
        self.__n_cols = n_cols
        self.__n = n_rows*n_cols
        self.__lookup = {}
        

    def flip(self):
        """
        :rtype: List[int]
        """
        self.__n -= 1
        target = random.randint(0, self.__n)
        x = self.__lookup.get(target, target)
        self.__lookup[target] = self.__lookup.get(self.__n, self.__n)
        return divmod(x, self.__n_cols)
        

    def reset(self):
        """
        :rtype: void
        """
        self.__n = self.__n_rows*self.__n_cols
        self.__lookup = {}
"""

519. Random Flip Matrix
Medium

There is an m x n binary grid matrix with all the values set 0 initially. Design an algorithm to randomly pick an index (i, j) where matrix[i][j] == 0 and flips it to 1. All the indices (i, j) where matrix[i][j] == 0 should be equally likely to be returned.

Optimize your algorithm to minimize the number of calls made to the built-in random function of your language and optimize the time and space complexity.

Implement the Solution class:

Solution(int m, int n) Initializes the object with the size of the binary matrix m and n.
int[] flip() Returns a random index [i, j] of the matrix where matrix[i][j] == 0 and flips it to 1.
void reset() Resets all the values of the matrix to be 0.

Example 1:

Input
["Solution", "flip", "flip", "flip", "reset", "flip"]
[[3, 1], [], [], [], [], []]
Output
[null, [1, 0], [2, 0], [0, 0], null, [2, 0]]

Explanation
Solution solution = new Solution(3, 1);
solution.flip();  // return [1, 0], [0,0], [1,0], and [2,0] should be equally likely to be returned.
solution.flip();  // return [2, 0], Since [1,0] was returned, [2,0] and [0,0]
solution.flip();  // return [0, 0], Based on the previously returned indices, only [0,0] can be returned.
solution.reset(); // All the values are reset to 0 and can be returned.
solution.flip();  // return [2, 0], [0,0], [1,0], and [2,0] should be equally likely to be returned.

Constraints:

1 <= m, n <= 10^4
There will be at least one free cell for each call to flip.
At most 1000 calls will be made to flip and reset.

"""

# V0

# V1
# time = O(1)  # per flip / init call
# space = O(1)
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
# time = O(1) for ctor and flip, O(min(f, r * c)) for reset
# space = O(min(f, r * c))
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
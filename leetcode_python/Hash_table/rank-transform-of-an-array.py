"""

1331. Rank Transform of an Array
Easy

Given an array of integers arr, replace each element with its rank.

The rank represents how large the element is. The rank has the following rules:

Rank is an integer starting from 1.
The larger the element, the larger the rank. If two elements are equal, their rank must be the same.
Rank should be as small as possible.
 

Example 1:

Input: arr = [40,10,20,30]
Output: [4,1,2,3]
Explanation: 40 is the largest element. 10 is the smallest. 20 is the second smallest. 30 is the third smallest.
Example 2:

Input: arr = [100,100,100]
Output: [1,1,1]
Explanation: Same elements share the same rank.
Example 3:

Input: arr = [37,12,28,9,100,56,80,5,12]
Output: [5,3,4,2,8,6,7,1,3]
 

Constraints:

0 <= arr.length <= 105
-109 <= arr[i] <= 109

"""

# V0

# V1
# https://leetcode.com/problems/rank-transform-of-an-array/discuss/1274219/Python-sort-and-dictionary-beats-95.31
class Solution:
	def arrayRankTransform(self, arr):
		"""
		Runtime: 328 ms, faster than 95.31% of Python3 online submissions for Rank Transform of an Array.
		Memory Usage: 35.2 MB, less than 23.24% of Python3 online submissions for Rank Transform of an Array.
		"""
		myDict=dict()
		newarr=arr
		arr=sorted(list(set(arr)))
		for i in range(len(arr)):
			if arr[i] not in myDict:
				myDict[arr[i]]=i+1
		return [myDict[num] for num in newarr]

# V1'
# https://leetcode.com/problems/rank-transform-of-an-array/discuss/1494913/Python-Straightforward
class Solution(object):
    def arrayRankTransform(self, arr):
        d = {}
        res = []
        
        for num in sorted(arr):
            if num not in d:
                d[num] = len(d) + 1 # len to rank!
                
        for num in arr:
            res.append(d[num])
        return res 

# V1''
# https://leetcode.com/problems/rank-transform-of-an-array/discuss/489753/JavaC%2B%2BPython-HashMap
class Solution(object):
    def arrayRankTransform(self, A):
            rank = {}
            for a in sorted(A):
                rank.setdefault(a, len(rank) + 1)
            return map(rank.get, A)

# V2
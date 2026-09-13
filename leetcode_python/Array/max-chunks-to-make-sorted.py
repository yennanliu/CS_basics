"""

769. Max Chunks To Make Sorted
Medium

You are given an integer array arr of length n that represents a permutation of the integers in the range [0, n - 1].

We split arr into some number of chunks (i.e., partitions), and individually sort each chunk. After concatenating them, the result should equal the sorted array.

Return the largest number of chunks we can make to sort the array.

Example 1:

Input: arr = [4,3,2,1,0]
Output: 1
Explanation:
Splitting into two or more chunks will not return the required result.
For example, splitting into [4, 3], [2, 1, 0] will result in [3, 4, 0, 1, 2], which isn't sorted.

Example 2:

Input: arr = [1,0,2,3,4]
Output: 4
Explanation:
We can split into two chunks, such as [1, 0], [2, 3, 4].
However, splitting into [1, 0], [2], [3], [4] is the highest number of chunks possible.

Constraints:

n == arr.length
1 <= n <= 10
0 <= arr[i] < n
All the elements of arr are unique.

"""

# V0 

# V1 
# http://bookshadow.com/weblog/2018/01/21/leetcode-max-chunks-to-make-sorted-ver-1/
# IDEA : GREEDY
# time = O(n^2)
# space = O(n)
class Solution(object):
    def maxChunksToSorted(self, arr):
        """
        :type arr: List[int]
        :rtype: int
        """
        N = len(arr)
        ans = 1
        for x in range(N - 1, 0, -1):
            if min(arr[x:]) > max(arr[:x]):
                ans += 1
        return ans

# V1'
# https://blog.csdn.net/fuxuemingzhu/article/details/80482014
# time = O(n)
# space = O(1)
class Solution:
    def maxChunksToSorted(self, arr):
        """
        :type arr: List[int]
        :rtype: int
        """
        chunks = 0
        pre_max = 0
        for i, num in enumerate(arr):
            if num > pre_max:
                pre_max = num
            if pre_max == i:
                chunks += 1
        return chunks
        
# V2 
# time = O(n)
# space = O(1)
class Solution(object):
    def maxChunksToSorted(self, arr):
        """
        :type arr: List[int]
        :rtype: int
        """
        result, max_i = 0, 0
        for i, v in enumerate(arr):
            max_i = max(max_i, v)
            if max_i == i:
                result += 1
        return result

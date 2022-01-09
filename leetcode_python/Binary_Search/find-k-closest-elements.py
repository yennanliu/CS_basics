"""

658. Find K Closest Elements
Medium

Given a sorted integer array arr, two integers k and x, return the k closest integers to x in the array. The result should also be sorted in ascending order.

An integer a is closer to x than an integer b if:

|a - x| < |b - x|, or
|a - x| == |b - x| and a < b
 

Example 1:

Input: arr = [1,2,3,4,5], k = 4, x = 3
Output: [1,2,3,4]
Example 2:

Input: arr = [1,2,3,4,5], k = 4, x = -1
Output: [1,2,3,4]
 

Constraints:

1 <= k <= arr.length
1 <= arr.length <= 104
arr is sorted in ascending order.
-104 <= arr[i], x <= 104


"""

# V0 
# IDEA : TWO POINTERS
class Solution(object):
    def findClosestElements(self, arr, k, x):
        while len(arr) > k:
            if x - arr[0] <= arr[-1] - x:
                arr.pop()
            else:
                arr.pop(0)
        return arr

# V0'
# IDEA : HASHMAP + brute force
class Solution(object):
    def findClosestElements(self, arr, k, x):
        # edge case
        if k == 0 or not arr:
            return []
        res = []
        d = {}
        for i in range(len(arr)):
            diff = abs(arr[i]-x)
            if diff not in d:
                d[diff] = [arr[i]]
            else:
                d[diff].append(arr[i])
        keys = list(d.keys())
        keys.sort()
        for key in keys:
            if len(res) >= k:
                break 
            res += d[key]
        ans=res[:k]
        ans.sort()
        return ans

# V1 
# https://blog.csdn.net/fuxuemingzhu/article/details/82968136
# IDEA : HEAP 
class Solution(object):
    def findClosestElements(self, arr, k, x):
        N = len(arr)
        sub = [((arr[i] - x) ** 2, i) for i in range(N)]
        heapq.heapify(sub)
        return sorted([arr[heapq.heappop(sub)[1]] for i in range(k)])

# V1'
# https://blog.csdn.net/fuxuemingzhu/article/details/82968136
# IDEA : TWO POINTERS 
class Solution(object):
    def findClosestElements(self, arr, k, x):
        # since the array already sorted, arr[-1] must be the biggest one,
        # while arr[0] is the smallest one
        # so if the distance within arr[-1],  x >  arr[0],  x
        # then remove the arr[-1] since we want to keep k elements with smaller distance,
        # and vice versa (remove arr[0]) 
        while len(arr) > k:
            if x - arr[0] <= arr[-1] - x:
                arr.pop()
            else:
                arr.pop(0)
        return arr

# V1''
# https://blog.csdn.net/fuxuemingzhu/article/details/82968136
# IDEA : BINARY SEARCH 
class Solution(object):
    def findClosestElements(self, arr, k, x):
        left = 0
        right = len(arr) - k
        while left < right:
            mid = left + (right - left) / 2
            if x - arr[mid] > arr[mid + k] - x:
                left = mid + 1
            else:
                right = mid
        return arr[left : left + k]

# V1'''
# https://www.jiuzhang.com/solution/460-find-k-closest-elements/#tag-highlight-lang-python
class Solution:
    # @param {int[]} A an integer array
    # @param {int} target an integer
    # @param {int} k a non-negative integer
    # @return {int[]} an integer array
    def kClosestNumbers(self, A, target, k):
        # Algorithm:
        # 1. Find the first index that A[index] >= target
        # 2. Set two pointers left = index - 1 and right = index
        # 3. Compare A[left] and A[right] to decide which pointer should move
        
        index = self.firstIndex(A, target)
        left, right = index - 1, index
        result = []
        for i in range(k):
            if left < 0:
                result.append(A[right])
                right += 1
            elif right == len(A):
                result.append(A[left])
                left -= 1
            else:
                if target - A[left] <= A[right] - target: 
                    result.append(A[left])
                    left -= 1
                else:
                    result.append(A[right])
                    right += 1
                    
        return result
        
    def firstIndex(self, A, target):
        start, end = 0, len(A) - 1
        while start + 1 < end:
            mid = (start + end) / 2
            if A[mid] < target:
                start = mid
            else:
                end = mid
        
        if A[start] >= target:
            return start
            
        if A[end] >= target:
            return end
            
        return len(A)

# V2 
# Time:  O(logn + k)
# Space: O(1)
import bisect
class Solution(object):
    def findClosestElements(self, arr, k, x):
        """
        :type arr: List[int]
        :type k: int
        :type x: int
        :rtype: List[int]
        """
        i = bisect.bisect_left(arr, x)
        left, right = i-1, i
        while k:
            if right >= len(arr) or \
               (left >= 0 and abs(arr[left]-x) <= abs(arr[right]-x)):
                left -= 1
            else:
                right += 1
            k -= 1
        return arr[left+1:right]
# V0 

# V1 
# https://blog.csdn.net/fuxuemingzhu/article/details/82968136
# IDEA : HEAP 
class Solution(object):
    def findClosestElements(self, arr, k, x):
        """
        :type arr: List[int]
        :type k: int
        :type x: int
        :rtype: List[int]
        """
        N = len(arr)
        sub = [((arr[i] - x) ** 2, i) for i in range(N)]
        heapq.heapify(sub)
        return sorted([arr[heapq.heappop(sub)[1]] for i in range(k)])

# V1'
# https://blog.csdn.net/fuxuemingzhu/article/details/82968136
# IDEA : TWO POINTERS 
class Solution(object):
    def findClosestElements(self, arr, k, x):
        """
        :type arr: List[int]
        :type k: int
        :type x: int
        :rtype: List[int]
        """
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
        """
        :type arr: List[int]
        :type k: int
        :type x: int
        :rtype: List[int]
        """
        left = 0
        right = len(arr) - k
        while left < right:
            mid = left + (right - left) / 2
            if x - arr[mid] > arr[mid + k] - x:
                left = mid + 1
            else:
                right = mid
        return arr[left : left + k]

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

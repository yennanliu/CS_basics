"""

1492. The kth Factor of n
Medium

You are given two positive integers n and k. A factor of an integer n is defined as an integer i where n % i == 0.

Consider a list of all factors of n sorted in ascending order, return the kth factor in this list or return -1 if n has less than k factors.

 

Example 1:

Input: n = 12, k = 3
Output: 3
Explanation: Factors list is [1, 2, 3, 4, 6, 12], the 3rd factor is 3.
Example 2:

Input: n = 7, k = 2
Output: 7
Explanation: Factors list is [1, 7], the 2nd factor is 7.
Example 3:

Input: n = 4, k = 4
Output: -1
Explanation: Factors list is [1, 2, 4], there is only 3 factors. We should return -1.
 

Constraints:

1 <= k <= n <= 1000

"""

# V0

# V1
# IDEA : BRUTE FORCE
# IDEA :
# -> Iterate by xx from 1 to N/2:
# -> If x is a divisor of NN, decrease kk by one. Return x if k == 0
# -> Return N if k == 1 and -1  otherwise.
class Solution:
    def kthFactor(self, n, k):
        for x in range(1, n // 2 + 1):
            if n % x == 0:
                k -= 1
                if k == 0:
                    return x
        
        return n if k == 1 else -1

# V1'
# IDEA : HEAP
# Initialize max heap. Use PriorityQueue in Java and heap in Python. heap is a min-heap. Hence, to implement max heap, change the sign of divisor before pushing it into the heap.
# https://leetcode.com/problems/the-kth-factor-of-n/solution/
class Solution:
    def kthFactor(self, n, k):
        # push into heap
        # by limiting size of heap to k
        def heappush_k(num):
            heappush(heap, - num)
            if len(heap) > k:
                heappop(heap)
            
        # Python heap is min heap 
        # -> to keep max element always on top,
        # one has to push negative values
        heap = []
        for x in range(1, int(n**0.5) + 1):
            if n % x == 0:
                heappush_k(x)
                if x != n // x:
                    heappush_k(n // x)
                
        return -heappop(heap) if k == len(heap) else -1

# V1''
# IDEA : MATH
# https://leetcode.com/problems/the-kth-factor-of-n/solution/
class Solution:
    def kthFactor(self, n, k):
        divisors, sqrt_n = [], int(n**0.5)
        for x in range(1, sqrt_n + 1):
            if n % x == 0:
                k -= 1
                divisors.append(x)
                if k == 0:
                    return x
        
        # If n is a perfect square
        # we have to skip the duplicate 
        # in the divisor list
        if (sqrt_n * sqrt_n == n):
            k += 1
                
        n_div = len(divisors)
        return n // divisors[n_div - k] if k <= n_div else -1

# V1'''
# https://blog.csdn.net/qq_52324409/article/details/121040250

# V2
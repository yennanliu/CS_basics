"""

786. K-th Smallest Prime Fraction
Medium

You are given a sorted integer array arr containing 1 and prime numbers,
where all the integers of arr are unique. You are also given an integer k.

For every i and j where 0 <= i < j < arr.length, we consider the fraction arr[i] / arr[j].

Return the kth smallest fraction considered. Return your answer as an array of
integers of size 2, where answer[0] == arr[i] and answer[1] == arr[j].


Example 1:

Input: arr = [1,2,3,5], k = 3
Output: [2,5]
Explanation: The fractions to be considered in sorted order are:
1/5, 1/3, 2/5, 1/2, 3/5, and 2/3.
The third fraction is 2/5.

Example 2:

Input: arr = [1,7], k = 1
Output: [1,7]


Constraints:

2 <= arr.length <= 1000
1 <= arr[i] <= 3 * 10^4
arr[0] == 1
arr[i] is a prime number for i > 0.
All the numbers of arr are unique and sorted in strictly increasing order.
1 <= k <= arr.length * (arr.length - 1) / 2

Follow up: Can you solve the problem with better than O(n^2) complexity?

"""

# V0
# IDEA : HEAP (k-way merge)
#
#   For a fixed denominator arr[j], the fractions arr[0]/arr[j] < arr[1]/arr[j] < ...
#   form a sorted list. So we have (n-1) sorted lists and want the k-th
#   smallest overall -> classic k-way merge with a min heap.
#
#   Seed the heap with the head of each list (arr[0]/arr[j]),
#   then pop k-1 times, pushing the successor of every popped element.
#
# time = O(n + k * log(n))
# space = O(n)
import heapq
class Solution(object):
    def kthSmallestPrimeFraction(self, arr, k):
        n = len(arr)

        # (fraction value, numerator index, denominator index)
        heap = [(arr[0] / float(arr[j]), 0, j) for j in range(1, n)]
        heapq.heapify(heap)

        for _ in range(k - 1):
            _, i, j = heapq.heappop(heap)
            # next fraction sharing the same denominator
            if i + 1 < j:
                heapq.heappush(heap, (arr[i + 1] / float(arr[j]), i + 1, j))

        _, i, j = heap[0]
        return [arr[i], arr[j]]


# V1
# IDEA : BINARY SEARCH ON THE ANSWER VALUE + TWO POINTERS
#
#   Binary search a threshold `mid` in (0, 1) and count how many fractions
#   are < mid. Because arr is sorted, for a growing denominator index j the
#   largest numerator index satisfying arr[i] < mid * arr[j] is non-decreasing,
#   so the count can be done with two pointers in O(n).
#
#   While counting we also keep the biggest fraction that is still < mid;
#   when the count hits exactly k, that biggest one IS the k-th smallest.
#
# time = O(n * log(1/eps))  -> independent of k
# space = O(1)
class Solution(object):
    def kthSmallestPrimeFraction(self, arr, k):
        n = len(arr)
        lo, hi = 0.0, 1.0

        while True:
            mid = (lo + hi) / 2.0

            cnt = 0
            p, q = 0, 1          # best (largest) fraction seen that is < mid
            i = -1               # largest index with arr[i] < mid * arr[j]
            for j in range(1, n):
                while i + 1 < j and arr[i + 1] < mid * arr[j]:
                    i += 1
                cnt += i + 1
                # compare arr[i]/arr[j] > p/q  without floating division
                if i >= 0 and arr[i] * q > p * arr[j]:
                    p, q = arr[i], arr[j]

            if cnt == k:
                return [p, q]
            elif cnt < k:
                lo = mid
            else:
                hi = mid

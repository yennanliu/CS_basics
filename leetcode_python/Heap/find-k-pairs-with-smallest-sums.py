"""

373. Find K Pairs with Smallest Sums
Solved
Medium
Topics
premium lock icon
Companies
You are given two integer arrays nums1 and nums2 sorted in non-decreasing order and an integer k.

Define a pair (u, v) which consists of one element from the first array and one element from the second array.

Return the k pairs (u1, v1), (u2, v2), ..., (uk, vk) with the smallest sums.

 

Example 1:

Input: nums1 = [1,7,11], nums2 = [2,4,6], k = 3
Output: [[1,2],[1,4],[1,6]]
Explanation: The first 3 pairs are returned from the sequence: [1,2],[1,4],[1,6],[7,2],[7,4],[11,2],[7,6],[11,4],[11,6]
Example 2:

Input: nums1 = [1,1,2], nums2 = [1,2,3], k = 2
Output: [[1,1],[1,1]]
Explanation: The first 2 pairs are returned from the sequence: [1,1],[1,1],[1,2],[2,1],[1,2],[2,2],[1,3],[1,3],[2,3]
 

Constraints:

1 <= nums1.length, nums2.length <= 105
-109 <= nums1[i], nums2[i] <= 109
nums1 and nums2 both are sorted in non-decreasing order.
1 <= k <= 104
k <= nums1.length * nums2.length
 


"""


# V0 


# V1-1
# IDEA: HEAP (gpt)
import heapq
class Solution(object):
    def kSmallestPairs(self, nums1, nums2, k):
        if not nums1 or not nums2 or k == 0:
            return []

        heap = []

        # Start with (nums1[i], nums2[0])
        for i in range(min(len(nums1), k)):

            """
            NOTE !!!


            PQ: [ [sum, idx_1, idx_2], ... ]
            """
            heapq.heappush(heap, (nums1[i] + nums2[0], i, 0))

        res = []

        while heap and len(res) < k:
            _, i, j = heapq.heappop(heap)
            res.append([nums1[i], nums2[j]])

            """
            NOTE !!!


            instead of `loop over nums2`,

            -> we do below:
                -> check if `next idx_2` is still in boundary (e.g. if j + 1 < len(nums2) )
                    -> if yes, get the sum, idx of that case, and insert to PQ
            """
            # Push the next pair using nums2[j+1]
            if j + 1 < len(nums2):
                heapq.heappush(
                    heap,
                    (nums1[i] + nums2[j + 1], i, j + 1)
                )

        return res



# V1-2
# IDEA: HEAP (gemini)
import heapq

class Solution(object):
    def kSmallestPairs(self, nums1, nums2, k):
        """
        :type nums1: List[int]
        :type nums2: List[int]
        :type k: int
        :rtype: List[List[int]]
        """
        # Edge case check
        if not nums1 or not nums2:
            return []
            
        pq = []
        res = []
        
        # 1. Initialize the heap.
        # We only need to pair nums2[0] with the first 'k' elements of nums1. 
        # (Because both arrays are sorted, nums1[k] + nums2[0] can't possibly be in the top k)
        for i in range(min(k, len(nums1))):
            # Store tuple: (sum, index_in_nums1, index_in_nums2)
            heapq.heappush(pq, (nums1[i] + nums2[0], i, 0))
            
        # 2. Extract the smallest pairs k times (or until heap is empty)
        while pq and len(res) < k:
            # Pop the absolute smallest sum currently in the heap
            curr_sum, i, j = heapq.heappop(pq)
            
            # Add the actual numbers to our result
            res.append([nums1[i], nums2[j]])
            
            # 3. Push the NEXT possible pair for this specific nums1[i]
            # Since we just used nums2[j], the next smallest option is nums2[j + 1]
            if j + 1 < len(nums2):
                heapq.heappush(pq, (nums1[i] + nums2[j + 1], i, j + 1))
                
        return res



# V2
# IDEA: HEAP
# https://leetcode.com/problems/find-k-pairs-with-smallest-sums/editorial/
class Solution:
    def kSmallestPairs(self, nums1: List[int], nums2: List[int], k: int) -> List[List[int]]:
        from heapq import heappush, heappop
        m = len(nums1)
        n = len(nums2)

        ans = []
        visited = set()

        minHeap = [(nums1[0] + nums2[0], (0, 0))]
        visited.add((0, 0))

        while k > 0 and minHeap:
            val, (i, j) = heappop(minHeap)
            ans.append([nums1[i], nums2[j]])

            if i + 1 < m and (i + 1, j) not in visited:
                heappush(minHeap, (nums1[i + 1] + nums2[j], (i + 1, j)))
                visited.add((i + 1, j))

            if j + 1 < n and (i, j + 1) not in visited:
                heappush(minHeap, (nums1[i] + nums2[j + 1], (i, j + 1)))
                visited.add((i, j + 1))
            k = k - 1

        return ans

# V3
# http://bookshadow.com/weblog/2016/07/07/leetcode-find-k-pairs-with-smallest-sums/
# IDEA : HEAP
# time = O(k * n1) (n1 pushes per idx2 step, up to k pops)
# space = O(k * n1)
class Solution(object):
    def kSmallestPairs(self, nums1, nums2, k):
        """
        :type nums1: List[int]
        :type nums2: List[int]
        :type k: int
        :rtype: List[List[int]]
        """
        ans = []
        heap = [(0x7FFFFFFF, None, None)]
        size1, size2 = len(nums1), len(nums2)
        idx2 = 0
        while len(ans) < min(k, size1 * size2):
            if idx2 < size2:
                sum, i, j = heap[0]
                if nums2[idx2] + nums1[0] < sum:
                    for idx1 in range(size1):
                        heapq.heappush(heap, (nums1[idx1] + nums2[idx2], idx1, idx2))
                    idx2 += 1
            sum, i, j = heapq.heappop(heap)
            ans.append((nums1[i], nums2[j]))
        return ans

# V4 
# http://bookshadow.com/weblog/2016/07/07/leetcode-find-k-pairs-with-smallest-sums/
# IDEA : HEAP
# time = O(k * log(min(k, n1)))
# space = O(min(k, n1))
class Solution(object):
    def kSmallestPairs(self, nums1, nums2, k):
        """
        :type nums1: List[int]
        :type nums2: List[int]
        :type k: int
        :rtype: List[List[int]]
        """
        ans = []
        size1, size2 = len(nums1), len(nums2)
        if size1 * size2 == 0: return ans
        heap = []
        for x in range(size1):
            heapq.heappush(heap, (nums1[x] + nums2[0], x, 0))
        while len(ans) < min(k, size1 * size2):
            sum, i, j = heapq.heappop(heap)
            ans.append((nums1[i], nums2[j]))
            if j + 1 < size2:
                heapq.heappush(heap, (nums1[i] + nums2[j + 1], i, j + 1))
        return ans

# V5 
# http://bookshadow.com/weblog/2016/07/07/leetcode-find-k-pairs-with-smallest-sums/
# IDEA : HEAP
# time = O(k * log(min(k, n1)))
# space = O(min(k, n1))
def kSmallestPairs(self, nums1, nums2, k):
    queue = []
    def push(i, j):
        if i < len(nums1) and j < len(nums2):
            heapq.heappush(queue, [nums1[i] + nums2[j], i, j])
    push(0, 0)
    pairs = []
    while queue and len(pairs) < k:
        _, i, j = heapq.heappop(queue)
        pairs.append([nums1[i], nums2[j]])
        push(i, j + 1)
        if j == 0:
            push(i + 1, 0)
    return pairs
    
# V6
# time = O(k * log(min(n, m, k))), where n is the size of num1, and m is the size of num2.
# space = O(min(n, m, k))
from heapq import heappush, heappop
class Solution(object):
    def kSmallestPairs(self, nums1, nums2, k):
        """
        :type nums1: List[int]
        :type nums2: List[int]
        :type k: int
        :rtype: List[List[int]]
        """
        pairs = []
        if len(nums1) > len(nums2):
            tmp = self.kSmallestPairs(nums2, nums1, k)
            for pair in tmp:
                pairs.append([pair[1], pair[0]])
            return pairs

        min_heap = []
        def push(i, j):
            if i < len(nums1) and j < len(nums2):
                heappush(min_heap, [nums1[i] + nums2[j], i, j])

        push(0, 0)
        while min_heap and len(pairs) < k:
            _, i, j = heappop(min_heap)
            pairs.append([nums1[i], nums2[j]])
            push(i, j + 1)
            if j == 0:
                push(i + 1, 0)  # at most queue min(n, m) space
        return pairs


# time = O(m*n * log(m*n)) (nsmallest sorts internally, uses a size-k heap)
# space = O(m*n)
from heapq import nsmallest
from itertools import product
class Solution2(object):
    def kSmallestPairs(self, nums1, nums2, k):
        """
        :type nums1: List[int]
        :type nums2: List[int]
        :type k: int
        :rtype: List[List[int]]
        """
        return nsmallest(k, product(nums1, nums2), key=sum)

# V0 

# V1 
# http://bookshadow.com/weblog/2016/07/07/leetcode-find-k-pairs-with-smallest-sums/
# IDEA : HEAP 
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

# V1' 
# http://bookshadow.com/weblog/2016/07/07/leetcode-find-k-pairs-with-smallest-sums/
# IDEA : HEAP 
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

# V1'' 
# http://bookshadow.com/weblog/2016/07/07/leetcode-find-k-pairs-with-smallest-sums/
# IDEA : HEAP 
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
    
# V2 
# Time:  O(k * log(min(n, m, k))), where n is the size of num1, and m is the size of num2.
# Space: O(min(n, m, k))
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


# time: O(mn * log k)
# space: O(k)
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

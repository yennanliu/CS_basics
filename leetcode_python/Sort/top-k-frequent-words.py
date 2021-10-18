"""

692. Top K Frequent Words
Medium

Given an array of strings words and an integer k, return the k most frequent strings.

Return the answer sorted by the frequency from highest to lowest. Sort the words with the same frequency by their lexicographical order.


Example 1:

Input: words = ["i","love","leetcode","i","love","coding"], k = 2
Output: ["i","love"]
Explanation: "i" and "love" are the two most frequent words.
Note that "i" comes before "love" due to a lower alphabetical order.
Example 2:

Input: words = ["the","day","is","sunny","the","the","the","sunny","is","is"], k = 4
Output: ["the","is","sunny","day"]
Explanation: "the", "is", "sunny" and "day" are the four most frequent words, with the number of occurrence being 4, 3, 2 and 1 respectively.
 

Constraints:

1 <= words.length <= 500
1 <= words[i] <= 10
words[i] consists of lowercase English letters.
k is in the range [1, The number of unique words[i]]
 

Follow-up: Could you solve it in O(n log(k)) time and O(n) extra space?

"""

# V0 : TODO : bouble check
from collections import Counter
class Solution(object):
    def topKFrequent(self, words, k):
        counter = Counter(words)
        candidates = list(counter.keys())
        candidates.sort(key=lambda w: (-counter[w], w))
        return candidates[:k]

# V1
# https://blog.csdn.net/fuxuemingzhu/article/details/79559691
# import collections
# class Solution(object):
#     def topKFrequent(self, words, k):
#         """
#         :type words: List[str]
#         :type k: int
#         :rtype: List[str]
#         """
#         count = collections.Counter(words)
#         def compare(x, y):
#             def cmp(x,y):
#                 if x < y:
#                     return -1 
#                 elif x == y:
#                     return 0 
#                 elif x > y:
#                     return 1 
#             if x[1] == y[1]:
#                 return cmp(x[0], y[0])
#             else:
#                 return -cmp(x[1], y[1])
#         return [x[0] for x in sorted(count.items(), cmp = compare)[:k]]

# V1' 
# https://blog.csdn.net/fuxuemingzhu/article/details/79559691
# IDEA : HEAP 
# TOP K PROBLEMS IN PYTHON -> HEAP 
# IDEA : heapq data module in python  :  array -> stack 
# heapq.heapify(heap) : transform array to stack into linear time 
# heapq.heappop(heap) : perform stack pop operation (pop top element)
# heappush(heap,5)    : perform stack push operation (add new element at top )
import heapq
class Solution(object):
    def topKFrequent(self, words, k):
        """
        :type words: List[str]
        :type k: int
        :rtype: List[str]
        """
        count = collections.Counter(words)
        heap = [(-freq, word) for word, freq in count.items()]
        heapq.heapify(heap)
        return [heapq.heappop(heap)[1] for _ in range(k)]

# V2 
# Time:  O(n + klogk) on average
# Space: O(n)
import collections
import heapq
from random import randint
class Solution(object):
    def topKFrequent(self, words, k):
        """
        :type words: List[str]
        :type k: int
        :rtype: List[str]
        """
        counts = collections.Counter(words)
        p = []
        for key, val in counts.items():
            p.append((-val, key))
        self.kthElement(p, k)

        result = []
        sorted_p = sorted(p[:k])
        for i in range(k):
            result.append(sorted_p[i][1])
        return result

    def kthElement(self, nums, k):  # O(n) on average
        def PartitionAroundPivot(left, right, pivot_idx, nums):
            pivot_value = nums[pivot_idx]
            new_pivot_idx = left
            nums[pivot_idx], nums[right] = nums[right], nums[pivot_idx]
            for i in range(left, right):
                if nums[i] < pivot_value:
                    nums[i], nums[new_pivot_idx] = nums[new_pivot_idx], nums[i]
                    new_pivot_idx += 1

            nums[right], nums[new_pivot_idx] = nums[new_pivot_idx], nums[right]
            return new_pivot_idx

        left, right = 0, len(nums) - 1
        while left <= right:
            pivot_idx = randint(left, right)
            new_pivot_idx = PartitionAroundPivot(left, right, pivot_idx, nums)
            if new_pivot_idx == k - 1:
                return
            elif new_pivot_idx > k - 1:
                right = new_pivot_idx - 1
            else:  # new_pivot_idx < k - 1.
                left = new_pivot_idx + 1

# V3 
# Time:  O(nlogk)
# Space: O(n)
# Heap Solution
class Solution2(object):
    def topKFrequent(self, words, k):
        """
        :type words: List[str]
        :type k: int
        :rtype: List[str]
        """
        class MinHeapObj(object):
            def __init__(self,val):
                self.val = val
            def __lt__(self,other):
                return self.val[1] > other.val[1] if self.val[0] == other.val[0] else \
                       self.val < other.val
            def __eq__(self,other):
                return self.val == other.val
            def __str__(self):
                return str(self.val)

        counts = collections.Counter(words)
        min_heap = []
        for word, count in counts.items():
            heapq.heappush(min_heap, MinHeapObj((count, word)))
            if len(min_heap) == k+1:
                heapq.heappop(min_heap)
        result = []
        while min_heap:
            result.append(heapq.heappop(min_heap).val[1])
        return result[::-1]

# V4 
# Time:  O(n + klogk) ~ O(n + nlogn)
# Space: O(n)
# Bucket Sort Solution
class Solution3(object):
    def topKFrequent(self, words, k):
        """
        :type words: List[str]
        :type k: int
        :rtype: List[str]
        """
        counts = collections.Counter(words)
        buckets = [[] for _ in range(len(words)+1)]
        for word, count in counts.items():
            buckets[count].append(word)
        pairs = []
        for i in reversed(range(len(words))):
            for word in buckets[i]:
                pairs.append((-i, word))
            if len(pairs) >= k:
                break
        pairs.sort()
        return [pair[1] for pair in pairs[:k]]

# V5 
# time: O(nlogn)
# space: O(n)
from collections import Counter
class Solution4(object):
    def topKFrequent(self, words, k):
        """
        :type words: List[str]
        :type k: int
        :rtype: List[str]
        """
        counter = Counter(words)
        candidates = list(counter.keys())
        candidates.sort(key=lambda w: (-counter[w], w))
        return candidates[:k]
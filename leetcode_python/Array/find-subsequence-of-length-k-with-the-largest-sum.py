"""

2099. Find Subsequence of Length K With the Largest Sum
Easy

You are given an integer array nums and an integer k. You want to find a subsequence of nums of length k that has the largest sum.

Return any such subsequence as an integer array of length k.

A subsequence is an array that can be derived from another array by deleting some or no elements without changing the order of the remaining elements.


Example 1:

Input: nums = [2,1,3,3], k = 2
Output: [3,3]
Explanation:
The subsequence has the largest sum of 3 + 3 = 6.

Example 2:

Input: nums = [-1,-2,3,4], k = 3
Output: [-1,3,4]
Explanation:
The subsequence has the largest sum of -1 + 3 + 4 = 6.

Example 3:

Input: nums = [3,4,3,3], k = 2
Output: [3,4]
Explanation:
The subsequence has the largest sum of 3 + 4 = 7.
Another possible subsequence is [4, 3].


Constraints:

1 <= nums.length <= 1000
-10^5 <= nums[i] <= 10^5
1 <= k <= nums.length

"""

# V0
# IDEA : PICK THE k LARGEST BY INDEX, THEN RESTORE THE ORIGINAL ORDER
#
#   the sum does not care about order, so the best k values are simply the k
#   largest. but the OUTPUT must still be a subsequence, so keep track of
#   which INDICES were chosen and emit them in increasing index order.
#
#   sorting indices by value picks the winners; sorting the winners back by
#   index restores the subsequence property.
#
# time = O(n log n), space = O(n)
class Solution(object):
    def maxSubsequence(self, nums, k):
        idx = sorted(range(len(nums)), key=lambda i: nums[i], reverse=True)[:k]
        idx.sort()
        return [nums[i] for i in idx]


# V0-1
# IDEA : STREAM THROUGH A SIZE-k MIN-HEAP OF (VALUE, INDEX)
#
#   keep a min-heap holding the best k pairs seen so far. push every element
#   and, whenever the heap exceeds k entries, evict its smallest — the value
#   that can no longer belong to the top k. the heap therefore never grows
#   past k, which matters when k << n.
#
#   the heap loses the ordering, so sort the k survivors by index at the end
#   to hand back a real subsequence.
#
# time = O(n log k)
# space = O(k)
import heapq
class Solution(object):
    def maxSubsequence(self, nums, k):
        heap = []
        for i, v in enumerate(nums):
            heapq.heappush(heap, (v, i))
            if len(heap) > k:
                heapq.heappop(heap)
        heap.sort(key=lambda p: p[1])
        return [v for v, _ in heap]


# V0-2
# IDEA : VALUE THRESHOLD + ONE LEFT-TO-RIGHT PASS (NO INDEX BOOKKEEPING)
#
#   let t be the k-th largest VALUE. every element strictly greater than t is
#   certainly in the answer; the remaining slots are filled with copies of t
#   itself. so count how many copies of t are needed:
#       need = k - (how many values are > t)
#   then sweep the array once in order, emitting anything > t and at most
#   `need` copies of t. the output is in original order by construction — no
#   index array and no second sort.
#
# time = O(n log n) for the k-th largest, then O(n)
# space = O(n)
class Solution(object):
    def maxSubsequence(self, nums, k):
        t = sorted(nums)[len(nums) - k]
        need = k - sum(1 for v in nums if v > t)

        res = []
        for v in nums:
            if v > t:
                res.append(v)
            elif v == t and need > 0:
                res.append(v)
                need -= 1
        return res

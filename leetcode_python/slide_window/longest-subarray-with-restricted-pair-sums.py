"""

4067. Longest Subarray With Restricted Pair Sums
Medium

You are given an integer array nums.

A subarray nums[l..r] is valid if there are no three distinct indices i, j, and k
such that l <= i, j, k <= r and:

    nums[i] + nums[j] == nums[k]

Return the maximum length of a valid subarray of nums.

A subarray is a contiguous non-empty sequence of elements within an array.

Example 1:

Input: nums = [2,3,5,3,2,1]

Output: 3

Explanation:

Consider the subarray [3, 5, 3]. The pairs of elements at distinct indices have the
following sums:

    3 + 5 = 8
    3 + 3 = 6, using the two different occurrences of 3
    5 + 3 = 8

None of these sums is an element at the remaining index, so the subarray is valid.

Every subarray of length 4 contains 2, 3, and 5 at distinct indices, where
2 + 3 = 5. Therefore, no longer valid subarray exists, and the answer is 3.

Example 2:

Input: nums = [3,4,5,6]

Output: 4

Explanation:

The sums obtained from every pair of elements at distinct indices are 7, 8, 9, 9,
10, and 11. None of these values appears at the remaining index, so the entire
array is valid.

Constraints:

1 <= nums.length <= 1000
1 <= nums[i] <= 500

"""

# V0
# IDEA : SLIDING WINDOW + PAIR-SUM HASHMAP, UPDATED INCREMENTALLY
#
#   validity is MONOTONE : every sub-window of a valid window is valid (dropping
#   elements can not create a triple). so two pointers work -- for each right
#   end, the smallest valid left end never moves backwards.
#
#   a window is invalid when some element equals the sum of two OTHER elements.
#   keep, over the window,
#
#     pair_sum_cnt[s] : how many index pairs (i < j) in the window sum to s
#     val_cnt[v]      : how many elements in the window equal v
#     conflicts       : how many (pair, k) matches  nums[i] + nums[j] == nums[k]
#
#   and the window is valid exactly when conflicts == 0.
#
#   the brute force (rebuild all pair sums for every window) is O(n^3) and
#   times out at n = 1000; the trick is that adding / removing ONE element
#   changes only the pairs it belongs to, so each step is O(window):
#
#     add nums[r]    : it is the "k" of every pair already summing to nums[r],
#                      and the addend of a new pair (i, r) for every i in the
#                      window, each matching the elements equal to that sum
#     remove nums[l] : the same two counts, subtracted
#
#   e.g. nums = [2,3,5,3,2,1] -> window [2,3,5] : pair 2+3 = 5 and 5 is in the
#        window -> conflicts = 1 -> drop 2 -> [3,5,3] : sums 8, 6, 8, none in
#        the window -> valid, length 3
#
#   NOTE !!! values are >= 1, so a pair's sum is strictly bigger than both of
#            its members : the k matching a pair (i, j) can never be i or j
#            itself, and "three DISTINCT indices" comes for free. (3 + 3 = 6
#            from two different 3s still counts, and the pair count sees it.)
#
# time = O(n^2), space = O(V)   (V = max(nums) <= 500 bounds both hashmaps)
from collections import defaultdict


class Solution(object):
    def maxSubarray(self, nums):
        """
        :type nums: List[int]
        :rtype: int
        """
        n = len(nums)
        # edge case: fewer than 3 elements can not form a triple -> whole array is valid
        if n <= 2:
            return n

        # pair sum -> number of index pairs (i < j) inside the window with that sum
        pair_sum_cnt = defaultdict(int)
        # value -> number of window elements equal to it
        val_cnt = defaultdict(int)
        # number of (pair, k) matches inside the window; window is valid iff 0
        conflicts = 0

        max_len = 0
        left = 0
        for right in range(n):
            val = nums[right]

            # 1) nums[right] as the "sum" k : every pair already summing to it
            conflicts += pair_sum_cnt[val]
            # 2) nums[right] as an addend : a new pair with every element in the window
            for i in range(left, right):
                pair_sum = nums[i] + val
                pair_sum_cnt[pair_sum] += 1
                conflicts += val_cnt[pair_sum]
            val_cnt[val] += 1

            # shrink from the left until no triple is left in the window
            while conflicts > 0:
                out = nums[left]
                val_cnt[out] -= 1
                # mirror of the two steps above, for the element leaving
                conflicts -= pair_sum_cnt[out]
                for i in range(left + 1, right + 1):
                    pair_sum = nums[i] + out
                    pair_sum_cnt[pair_sum] -= 1
                    conflicts -= val_cnt[pair_sum]
                left += 1

            max_len = max(max_len, right - left + 1)

        return max_len


# V0-1
# IDEA : FIX THE LEFT END, GROW THE RIGHT END (same counters, no removal)
#
#   same pair-sum hashmap as V0, but the window is rebuilt from scratch for each
#   left end and only ever grows, so there is no removal bookkeeping to get
#   wrong. by monotonicity the first right end that creates a triple ends the
#   run for that left end. every (left, right) pair is visited once, so it is
#   still O(n^2); easier to derive live, slightly more work in practice.
#
# time = O(n^2), space = O(V)
class Solution2(object):
    def maxSubarray(self, nums):
        """
        :type nums: List[int]
        :rtype: int
        """
        n = len(nums)
        # edge case: fewer than 3 elements can not form a triple
        if n <= 2:
            return n

        max_len = 0
        for left in range(n):
            # no window starting here can beat the best one found so far
            if n - left <= max_len:
                break

            pair_sum_cnt = defaultdict(int)
            val_cnt = defaultdict(int)

            right = left
            while right < n:
                val = nums[right]

                # would nums[right] be the sum of two elements already in the window ?
                if pair_sum_cnt[val] > 0:
                    break
                # would nums[right] plus some window element equal another window element ?
                creates_triple = False
                for i in range(left, right):
                    if val_cnt[nums[i] + val] > 0:
                        creates_triple = True
                        break
                if creates_triple:
                    break

                # safe to extend : record the new pairs and the new value
                for i in range(left, right):
                    pair_sum_cnt[nums[i] + val] += 1
                val_cnt[val] += 1
                right += 1

            # window [left, right) is the longest valid one starting at left
            max_len = max(max_len, right - left)

        return max_len


# V1
# IDEA : SLIDING WINDOW OVER VALUE COUNTS, CHECK ONLY THE NEW ELEMENT
#
#   V0 tracks every pair in the window. but the window before nums[right] joins
#   is already valid, so the ONLY triples that can exist afterwards are the ones
#   that use nums[right]. so instead of pair counts, keep just val_cnt over the
#   window and ask two questions about the newcomer v = nums[right] :
#
#     v is the sum    : is there a + b == v with a, b both in the window ?
#                       (a == b needs two copies of a)
#     v is an addend  : is there an a in the window with a + v also in the window ?
#
#   each question scans the value range once, O(V) with V = max(nums) <= 500,
#   and removing from the left is a plain val_cnt decrement. so the total is
#   O(n * V) instead of O(n^2), and the state is a 501-slot array.
#
#   NOTE : this is the tighter bound and the one to reach for in an interview
#          once V0 is understood -- it is V0 with the "only the newcomer can be
#          in a triple" observation applied.
#
# time = O(n * V), space = O(V)   (V = max(nums))
class Solution3(object):
    def maxSubarray(self, nums):
        """
        :type nums: List[int]
        :rtype: int
        """
        n = len(nums)
        # edge case: fewer than 3 elements can not form a triple
        if n <= 2:
            return n

        max_val = max(nums)
        # value -> number of window elements equal to it (values are 1..max_val)
        val_cnt = [0] * (max_val + 1)

        max_len = 0
        left = 0
        for right in range(n):
            val = nums[right]

            # the window without nums[right] is valid, so only triples using
            # nums[right] can appear : shrink until it joins no triple
            while self.joins_triple(val_cnt, val, max_val):
                val_cnt[nums[left]] -= 1
                left += 1

            val_cnt[val] += 1
            max_len = max(max_len, right - left + 1)

        return max_len

    def joins_triple(self, val_cnt, val, max_val):
        """True if `val` would form nums[i] + nums[j] == nums[k] with the window counted in val_cnt."""
        # case 1: val is the sum -> a + b == val with a <= b, both present (a == b needs two copies)
        for a in range(1, val // 2 + 1):
            b = val - a
            if a == b:
                if val_cnt[a] >= 2:
                    return True
            elif val_cnt[a] >= 1 and val_cnt[b] >= 1:
                return True

        # case 2: val is an addend -> a and a + val both present (a + val > a, so distinct indices)
        for a in range(1, max_val - val + 1):
            if val_cnt[a] >= 1 and val_cnt[a + val] >= 1:
                return True

        return False

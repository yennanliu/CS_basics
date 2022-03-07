"""

992. Subarrays with K Different Integers
Hard

Given an integer array nums and an integer k, return the number of good subarrays of nums.

A good array is an array where the number of different integers in that array is exactly k.

For example, [1,2,3,1,2] has 3 different integers: 1, 2, and 3.
A subarray is a contiguous part of an array.

 

Example 1:

Input: nums = [1,2,1,2,3], k = 2
Output: 7
Explanation: Subarrays formed with exactly 2 different integers: [1,2], [2,1], [1,2], [2,3], [1,2,1], [2,1,2], [1,2,1,2]
Example 2:

Input: nums = [1,2,1,3,4], k = 3
Output: 3
Explanation: Subarrays formed with exactly 3 different integers: [1,2,1,3], [2,1,3], [1,3,4].
 

Constraints:

1 <= nums.length <= 2 * 104
1 <= nums[i], k <= nums.length

"""

# V0

# V1
# idea : hashmap + 2 pointers
# https://leetcode.com/problems/subarrays-with-k-different-integers/discuss/234643/Python-solution
# IDEA : 
# We initialize the result res = 0, and two pointers l = 0, r = 0, and two dictionaries dicL = {}, dicR = {}. We iterate i in range(len(A)), and use dicL (dicR) as a counter of the elements in A[l:i+1] (A[r:i+1]). For each i, we move l (r) to the leftmost (rightmost) index idx such that A[idx:i+1] contains K distinct elements, or equivalently len(dicL) == K (len(dicR) == K). Then any index l <= j <= r satisfies the property that A[j:i+1] contains K distinct elements. Therefore, we increment res accordingly: res += r-l. Finally, after iterating over i, we return res.
# Time complexity: O(n), space complexity: O(n).
class Solution(object):
    def subarraysWithKDistinct(self, A, K):
        dicL = collections.defaultdict(int)
        dicR = collections.defaultdict(int)
        res = 0
        l = 0
        r = 0
        for i in range(len(A)):
            dicL[A[i]] += 1
            dicR[A[i]] += 1
            while len(dicL) > K:
                dicL[A[l]] -= 1
                if dicL[A[l]] == 0:
                    del dicL[A[l]]
                l += 1
            while len(dicR) > K-1:
                dicR[A[r]] -= 1
                if dicR[A[r]] == 0:
                    del dicR[A[r]]
                r += 1
            res += r-l
        return res

# V1'
# https://leetcode.com/problems/subarrays-with-k-different-integers/discuss/523136/JavaC%2B%2BPython-Sliding-Window
# IDEA :
# Intuition:
# First you may have feeling of using sliding window.
# Then this idea get stuck in the middle.
# This problem will be a very typical sliding window,
# if it asks the number of subarrays with at most K distinct elements.
# Just need one more step to reach the folloing equation:
# exactly(K) = atMost(K) - atMost(K-1)
# Explanation
# Write/copy a helper function of sliding window,
# to get the number of subarrays with at most K distinct elements.
# Done.
# Complexity:
# Time O(N) for two passes.
# Space O(K) at most K elements in the counter
class Solution(object):
    def subarraysWithKDistinct(self, A, K):
        return self.atMostK(A, K) - self.atMostK(A, K - 1)

    def atMostK(self, A, K):
        count = collections.Counter()
        res = i = 0
        for j in range(len(A)):
            if count[A[j]] == 0: K -= 1
            count[A[j]] += 1
            while K < 0:
                count[A[i]] -= 1
                if count[A[i]] == 0: K += 1
                i += 1
            res += j - i + 1
        return res

# V1''
# https://leetcode.com/problems/subarrays-with-k-different-integers/discuss/997358/Python-Solution
class Solution:
    def subarraysWithKDistinct(self, A: List[int], K: int) -> int:
        distinct = defaultdict(int)
        i, kstart, total = 0, 0, 0
        for kend in range(len(A)):
            distinct[A[kend]] += 1
            if len(distinct) == K + 1:
                distinct.pop(A[kstart])
                kstart += 1
                i = kstart
            if len(distinct) == K:
                while distinct[A[kstart]] > 1:
                    distinct[A[kstart]] -= 1
                    kstart += 1
                total += kstart - i + 1
        return total

# V1'''
# IDEA : sliding window
# https://leetcode.com/problems/subarrays-with-k-different-integers/discuss/348984/Different-Python-two-pointer-solutions
class Solution(object):
    def subarraysWithKDistinct(self, A: List[int], K: int) -> int:
            if K == 0 or len(A) < K:
                return 0
            res = 0
            left, left_k = 0, 0
            right_most_pos = dict()
            for right in range(len(A)):
                right_most_pos[A[right]] = right
                if len(right_most_pos) == K + 1:
                    left_k = min(right_most_pos.values())
                    right_most_pos.pop(A[left_k])
                    left_k += 1
                    left = left_k
                if len(right_most_pos) == K:
                    left_k = min(right_most_pos.values())
                    res += left_k - left + 1
            return res

# V1''''
# IDEA : sliding window + bisect
# https://leetcode.com/problems/subarrays-with-k-different-integers/discuss/348984/Different-Python-two-pointer-solutions
class Solution(object):
    def subarraysWithKDistinct(self, A: List[int], K: int) -> int:
            if K == 0 or len(A) < K:
                return 0
            res = 0
            left, left_k = 0, 0
            sorted_pos = []
            right_most_pos = dict()
            for right in range(len(A)):
                if A[right] in right_most_pos:
                    ind = bisect.bisect_left(sorted_pos, right_most_pos[A[right]])
                    sorted_pos.pop(ind)
                sorted_pos.append(right)
                right_most_pos[A[right]] = right
                if len(right_most_pos) == K + 1:
                    left_k = sorted_pos.pop(0)
                    right_most_pos.pop(A[left_k])
                    left_k += 1
                    left = left_k
                if len(right_most_pos) == K:
                    left_k = sorted_pos[0]
                    res += left_k - left + 1
            return res

# V1''''''
# IDEA : hashmap + freq
# https://leetcode.com/problems/subarrays-with-k-different-integers/discuss/348984/Different-Python-two-pointer-solutions
class Solution(object):
    def subarraysWithKDistinct(self, A: List[int], K: int) -> int:
        if K == 0 or len(A) < K:
            return 0
        res = 0
        left, left_k = 0, 0
        counter = dict()
        for right in range(len(A)):
            counter[A[right]] = counter.get(A[right], 0) + 1
            if len(counter) == K + 1:
                counter.pop(A[left_k])
                left_k += 1
                left = left_k
            if len(counter) == K:
                while counter[A[left_k]] > 1:
                    counter[A[left_k]] -= 1
                    left_k += 1
                res += left_k - left + 1
        return res

# V1''''''
# IDEA : 2 PASS
# https://leetcode.com/problems/subarrays-with-k-different-integers/discuss/348984/Different-Python-two-pointer-solutions
class Solution:
    def subarraysWithKDistinct(self, A, K):
        return self.atMostK(A, K) - self.atMostK(A, K - 1)

    def atMostK(self, A, K):
        counter = dict()
        res = 0
        left = 0
        for right in range(len(A)):
            counter[A[right]] = counter.get(A[right], 0) + 1
            while len(counter) > K:
                counter[A[left]] -= 1
                if counter[A[left]] == 0:
                    counter.pop(A[left])
                left += 1
            res += right - left + 1
        return res

# V2
"""

2935. Maximum Strong Pair XOR II
Hard

You are given a 0-indexed integer array nums. A pair of integers x and y is called a strong pair if it satisfies the condition:

|x - y| <= min(x, y)

You need to select two integers from nums such that they form a strong pair and their bitwise XOR is the maximum among all strong pairs in the array.

Return the maximum XOR value out of all possible strong pairs in the array nums.

Note that you can pick the same integer twice to form a pair.


Example 1:

Input: nums = [1,2,3,4,5]
Output: 7
Explanation: There are 11 strong pairs in the array nums: (1, 1), (1, 2), (2, 2), (2, 3), (2, 4), (3, 3), (3, 4), (3, 5), (4, 4), (4, 5) and (5, 5).
The maximum XOR possible from these pairs is 3 XOR 4 = 7.

Example 2:

Input: nums = [10,100]
Output: 0
Explanation: There are 2 strong pairs in the array nums: (10, 10) and (100, 100).
The maximum XOR possible from these pairs is 10 XOR 10 = 0 since the pair (100, 100) also gives 100 XOR 100 = 0.

Example 3:

Input: nums = [500,520,2500,3000]
Output: 1020
Explanation: There are 6 strong pairs in the array nums: (500, 500), (500, 520), (520, 520), (2500, 2500), (2500, 3000) and (3000, 3000).
The maximum XOR possible from these pairs is 500 XOR 520 = 1020 since the only other non-zero XOR value is 2500 XOR 3000 = 636.


Constraints:

1 <= nums.length <= 5 * 10^4
1 <= nums[i] <= 2^20 - 1

"""

# V0
# IDEA : SORT + SLIDING WINDOW OVER A COUNTING BINARY TRIE
#
#   assume x <= y. then |x - y| <= min(x, y) becomes y - x <= x, i.e. y <= 2 * x.
#   so after sorting, the partners allowed for a given y form a contiguous
#   suffix window [i .. current] where 2 * nums[i] >= y.
#
#   slide that window while inserting / removing values in a binary trie keyed by
#   the 20 bits of each number (nums[i] < 2^20). querying the trie greedily walks
#   the opposite bit whenever a live child exists, giving max(y ^ z) over the
#   window in O(20).
#
#   NOTE : removal must be lazy-by-count, not by pruning — a node stays allocated
#          but its `cnt` drops to 0, so the query has to test `cnt` and not just
#          "child exists".
#   NOTE : insert y BEFORE shrinking the window, so the pair (y, y) -> 0 is always
#          available and the window is never empty.
#   NOTE : duplicates are fine — cnt is a multiplicity, not a flag.
#
# time = O(n * log n + n * 20), space = O(n * 20)
class Solution(object):
    def maximumStrongPairXor(self, nums):
        BITS = 20

        # flat-array trie: children[node][bit] -> node id (0 == absent)
        children = [[0, 0]]
        cnt = [0]

        def insert(x):
            node = 0
            for i in range(BITS, -1, -1):
                v = (x >> i) & 1
                if children[node][v] == 0:
                    children.append([0, 0])
                    cnt.append(0)
                    children[node][v] = len(cnt) - 1
                node = children[node][v]
                cnt[node] += 1

        def remove(x):
            node = 0
            for i in range(BITS, -1, -1):
                v = (x >> i) & 1
                node = children[node][v]
                cnt[node] -= 1

        def query(x):
            node = 0
            best = 0
            for i in range(BITS, -1, -1):
                v = (x >> i) & 1
                other = children[node][v ^ 1]
                if other and cnt[other]:
                    best |= 1 << i
                    node = other
                else:
                    node = children[node][v]
            return best

        nums = sorted(nums)
        ans = 0
        lo = 0
        for y in nums:
            insert(y)
            while y > nums[lo] * 2:
                remove(nums[lo])
                lo += 1
            ans = max(ans, query(y))
        return ans

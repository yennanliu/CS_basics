"""

1707. Maximum XOR With an Element From Array
Hard

You are given an array nums consisting of non-negative integers. You are also given a queries array, where queries[i] = [xi, mi].

The answer to the ith query is the maximum bitwise XOR value of xi and any element of nums that does not exceed mi. In other words, the answer is max(nums[j] XOR xi) for all j such that nums[j] <= mi. If all elements in nums are larger than mi, then the answer is -1.

Return an integer array answer where answer.length == queries.length and answer[i] is the answer to the ith query.


Example 1:

Input: nums = [0,1,2,3,4], queries = [[3,1],[1,3],[5,6]]
Output: [3,3,7]
Explanation:
1) 0 and 1 are the only two integers not greater than 1. 0 XOR 3 = 3 and 1 XOR 3 = 2. The larger of the two is 3.
2) 1 XOR 2 = 3.
3) 5 XOR 2 = 7.

Example 2:

Input: nums = [5,2,4,6,6,3], queries = [[12,4],[8,1],[6,3]]
Output: [15,-1,5]


Constraints:

1 <= nums.length, queries.length <= 10^5
queries[i].length == 2
0 <= nums[j], xi, mi <= 10^9

"""

# V0
# IDEA : OFFLINE QUERIES + BINARY TRIE (sort by m, grow the trie monotonically)
#
#   the "nums[j] <= m" filter is the awkward part. handle it OFFLINE :
#     - sort nums ascending
#     - sort the queries by m ascending
#   then a pointer j only ever moves forward : before answering a query we
#   insert every nums[j] <= m into the trie. the trie therefore always holds
#   exactly the legal candidates, and never needs a deletion.
#
#   inside the trie, max-XOR is the classic greedy : walk bits high -> low
#   and always try to take the branch OPPOSITE to x's bit.
#
#   NOTE : an empty trie (root with no children) means every num > m,
#          so the answer is -1 - detected by the missing child at bit 30.
#   NOTE : nums[j] <= 10^9 < 2^30, so 31 bits (30..0) are enough.
#
# time = O(n*log n + q*log q + (n + q)*30), space = O(n * 30)
class Solution(object):
    def maximizeXor(self, nums, queries):
        BITS = 30

        # trie node = [child0, child1]
        root = [None, None]

        def insert(x):
            node = root
            for i in range(BITS, -1, -1):
                b = (x >> i) & 1
                if node[b] is None:
                    node[b] = [None, None]
                node = node[b]

        def query(x):
            node = root
            if node[0] is None and node[1] is None:
                return -1
            res = 0
            for i in range(BITS, -1, -1):
                b = (x >> i) & 1
                if node[b ^ 1] is not None:
                    res |= 1 << i
                    node = node[b ^ 1]
                else:
                    node = node[b]
            return res

        nums = sorted(nums)
        order = sorted(range(len(queries)), key=lambda i: queries[i][1])

        res = [-1] * len(queries)
        j = 0
        for idx in order:
            x, m = queries[idx]
            while j < len(nums) and nums[j] <= m:
                insert(nums[j])
                j += 1
            res[idx] = query(x)

        return res

"""

2293. Min Max Game
Medium

You are given a 0-indexed integer array nums whose length is a power of 2.

Apply the following algorithm on nums:

Let n be the length of nums. If n == 1, end the process. Otherwise, create a new 0-indexed integer array newNums of length n / 2.
For every even index i where 0 <= i < n / 2, assign the value of newNums[i] as min(nums[2 * i], nums[2 * i + 1]).
For every odd index i where 0 <= i < n / 2, assign the value of newNums[i] as max(nums[2 * i], nums[2 * i + 1]).
Replace the array nums with newNums.
Repeat the entire process starting from step 1.

Return the last number that remains in nums after applying the algorithm.


Example 1:

Input: nums = [1,3,5,2,4,8,2,2]
Output: 1
Explanation: The following arrays are the results of applying the algorithm repeatedly.
First: nums = [1,5,4,2]
Second: nums = [1,4]
Final: nums = [1]
1 is the last remaining number, so we return 1.

Example 2:

Input: nums = [3]
Output: 3
Explanation: 3 is already the last remaining number, so we return 3.


Constraints:

1 <= nums.length <= 1024
1 <= nums[i] <= 10^9
nums.length is a power of two.

"""

# V0
# IDEA : DIRECT SIMULATION OF THE HALVING ROUNDS
#
#   each round halves the array, so the total work is n + n/2 + n/4 + ... =
#   O(n) — simulating literally is already optimal.
#
#   the min/max alternation is keyed on the index in the NEW array : even
#   slots take the min of their pair, odd slots the max.
#
# time = O(n), space = O(n)
class Solution(object):
    def minMaxGame(self, nums):
        while len(nums) > 1:
            half = len(nums) // 2
            nxt = [0] * half
            for i in range(half):
                a, b = nums[2 * i], nums[2 * i + 1]
                nxt[i] = min(a, b) if i % 2 == 0 else max(a, b)
            nums = nxt
        return nums[0]


# V0-1
# IDEA : DIVIDE AND CONQUER ON THE ORIGINAL ARRAY (NO NEW ARRAYS)
#
#   the slot at index i of the round whose blocks have size 2^r is built from
#   exactly the contiguous original block [i * 2^r, (i+1) * 2^r), and it merges
#   the two half-blocks that sit at indices 2i and 2i+1 of the previous round.
#
#   so recurse on (lo, size, idx) : split the block in half, solve the halves
#   as indices 2*idx and 2*idx+1, then combine with min when idx is even and
#   max when idx is odd. the answer is the block (0, n, 0).
#
#   reads the input in place — no per-round allocation, only the call stack.
#
# time = O(n), space = O(log n) recursion depth
class Solution(object):
    def minMaxGame(self, nums):
        def rec(lo, size, idx):
            if size == 1:
                return nums[lo]
            half = size // 2
            a = rec(lo, half, 2 * idx)
            b = rec(lo + half, half, 2 * idx + 1)
            return min(a, b) if idx % 2 == 0 else max(a, b)

        return rec(0, len(nums), 0)


# V0-2
# IDEA : STACK MERGE, BINARY-COUNTER STYLE (SINGLE PASS, NO ROUNDS)
#
#   push elements one at a time as (value, level, index-at-that-level). whenever
#   the top two entries share a level they are the two children of the same
#   parent, so collapse them immediately: the parent index is a.index // 2 and
#   the operator is min for an even parent index, max for an odd one.
#
#   this is the "carry" of a binary counter — the stack never holds two entries
#   of the same level after the loop, so it holds O(log n) entries and the whole
#   pass finishes the tournament without ever materialising a round.
#
# time = O(n), space = O(log n)
class Solution(object):
    def minMaxGame(self, nums):
        stack = []
        for i, v in enumerate(nums):
            stack.append((v, 0, i))
            while len(stack) >= 2 and stack[-1][1] == stack[-2][1]:
                bv = stack.pop()[0]
                av, alvl, aidx = stack.pop()
                idx = aidx // 2
                val = min(av, bv) if idx % 2 == 0 else max(av, bv)
                stack.append((val, alvl + 1, idx))
        return stack[0][0]

"""

2460. Apply Operations to an Array
Easy

You are given a 0-indexed array nums of size n consisting of non-negative integers.

You need to apply n - 1 operations to this array where, in the ith operation (0-indexed), you will apply the following on the ith element of nums:

If nums[i] == nums[i + 1], then multiply nums[i] by 2 and set nums[i + 1] to 0. Otherwise, you skip this operation.

After performing all the operations, shift all the 0's to the end of the array.

For example, the array [1,0,2,0,0,1] after shifting all its 0's to the end, becomes [1,2,1,0,0,0].

Return the resulting array.

Note that the operations are applied sequentially, not all at once.


Example 1:

Input: nums = [1,2,2,1,1,0]
Output: [1,4,2,0,0,0]
Explanation: We do the following operations:
- i = 0: nums[0] and nums[1] are not equal, so we skip this operation.
- i = 1: nums[1] and nums[2] are equal, we multiply nums[1] by 2 and change nums[2] to 0. The array becomes [1,4,0,1,1,0].
- i = 2: nums[2] and nums[3] are not equal, so we skip this operation.
- i = 3: nums[3] and nums[4] are equal, we multiply nums[3] by 2 and change nums[4] to 0. The array becomes [1,4,0,2,0,0].
- i = 4: nums[4] and nums[5] are equal, we multiply nums[4] by 2 and change nums[5] to 0. The array becomes [1,4,0,2,0,0].
After that, we shift the 0's to the end, which gives the array [1,4,2,0,0,0].

Example 2:

Input: nums = [0,1]
Output: [1,0]
Explanation: No operation can be applied, we just shift the 0 to the end.


Constraints:

2 <= n <= 2000
0 <= nums[i] <= 1000

"""

# V0
# IDEA : APPLY THE MERGES LEFT TO RIGHT, THEN A STABLE ZERO-SHIFT
#
#   the operations are SEQUENTIAL, so a single left-to-right pass on a mutable
#   copy is faithful — a merge at i is immediately visible to the step at
#   i + 1 (see example 1, where the zero written at index 4 makes the next
#   comparison a 0 == 0 pair that still merges into a 0).
#
#   the final shift must preserve the relative order of the survivors, so
#   filter the non-zeros and pad with zeros rather than sorting.
#
# time = O(n), space = O(n)
class Solution(object):
    def applyOperations(self, nums):
        a = nums[:]
        for i in range(len(a) - 1):
            if a[i] == a[i + 1]:
                a[i] *= 2
                a[i + 1] = 0

        rest = [x for x in a if x != 0]
        return rest + [0] * (len(a) - len(rest))


# V0-1
# IDEA : ONE PASS — PAIR AND EMIT, NO MUTATION, NO SECOND SWEEP
#
#   after the operation at index i runs, nums[i] is final (the doubled value
#   cannot merge again, because the very next comparison looks at the 0 that
#   was just written into nums[i + 1]). so a merge consumes exactly two cells
#   and the walk can jump straight over both, appending survivors as it goes:
#       nums[i] == 0            -> contributes nothing, skip it
#       nums[i] == nums[i + 1]  -> emit 2 * nums[i], jump to i + 2
#       otherwise               -> emit nums[i], step to i + 1
#   pad with the missing zeros at the end.
#
# time = O(n)
# space = O(n) for the output only — the input is never touched
class Solution(object):
    def applyOperations(self, nums):
        n = len(nums)
        out = []
        i = 0
        while i < n:
            if nums[i] == 0:
                i += 1
            elif i + 1 < n and nums[i] == nums[i + 1]:
                out.append(nums[i] * 2)
                i += 2
            else:
                out.append(nums[i])
                i += 1
        return out + [0] * (n - len(out))


# V0-2
# IDEA : RUN-LENGTH ARITHMETIC — NO SIMULATION AT ALL
#
#   a merge only ever happens between EQUAL neighbours, and a merged cell
#   holds 2v != v, so merges never reach across a run of equal values into the
#   next one. inside a non-zero run of length L the left-to-right process
#   pairs the elements greedily from the left, which just gives
#       L // 2  cells holding 2v   followed by   (L % 2) leftover v
#   e.g. [v,v,v,v,v] -> [2v, 2v, v]. runs of zeros vanish entirely. so group
#   the equal runs with itertools.groupby and write the answer down directly.
#
# time = O(n)
# space = O(n)
class Solution(object):
    def applyOperations(self, nums):
        import itertools
        out = []
        for val, run in itertools.groupby(nums):
            if val == 0:
                continue
            length = len(list(run))
            out.extend([val * 2] * (length // 2))
            if length % 2:
                out.append(val)
        return out + [0] * (len(nums) - len(out))

"""

1313. Decompress Run-Length Encoded List
Easy

We are given a list nums of integers representing a list compressed with
run-length encoding.

Consider each adjacent pair of elements [freq, val] = [nums[2*i], nums[2*i+1]]
(with i >= 0). For each such pair, there are freq elements with value val
concatenated in a sublist. Concatenate all the sublists from left to right to
generate the decompressed list.

Return the decompressed list.


Example 1:

Input: nums = [1,2,3,4]
Output: [2,4,4,4]
Explanation: The first pair [1,2] means we have freq = 1 and val = 2 so we
generate the array [2].
The second pair [3,4] means we have freq = 3 and val = 4 so we generate [4,4,4].
At the end the concatenation [2] + [4,4,4] is [2,4,4,4].

Example 2:

Input: nums = [1,1,2,3]
Output: [1,3,3]


Constraints:

2 <= nums.length <= 100
nums.length % 2 == 0
1 <= nums[i] <= 100

"""

# V0
# IDEA : simulation, walk the array 2 elements at a time
# time = O(n + k), k = total output length
# space = O(1) extra, excluding the output list
class Solution(object):
    def decompressRLElist(self, nums):
        res = []
        for i in range(0, len(nums), 2):
            freq, val = nums[i], nums[i + 1]
            res += [val] * freq
        return res


# V0-1
# IDEA : TWO PASS, PRE-ALLOCATE THE OUTPUT
#
#   pass 1 : the total output length is just the sum of the freq entries
#            (the even indices), so it is known before writing anything.
#   pass 2 : allocate that list once and fill it through a write pointer.
#
#   this trades the repeated list growth / temporary [val] * freq blocks of
#   V0 for a single allocation, which is what you would have to do in a
#   language without a growable list.
#
# time = O(n + k), k = total output length
# space = O(1) extra, excluding the output list
class Solution(object):
    def decompressRLElist(self, nums):
        total = 0
        for i in range(0, len(nums), 2):
            total += nums[i]

        res = [0] * total
        w = 0
        for i in range(0, len(nums), 2):
            freq, val = nums[i], nums[i + 1]
            for _ in range(freq):
                res[w] = val
                w += 1
        return res


# V0-2
# IDEA : LAZY ITERATORS - PAIR UP WITH STRIDED SLICES, EXPAND WITH repeat
#
#   nums[0::2] / nums[1::2] split the array into the freq stream and the val
#   stream, zip pairs them, itertools.repeat turns one pair into a lazy run
#   and chain.from_iterable concatenates the runs.
#
#   nothing is materialised per pair: the runs are consumed straight into
#   the final list by list(), instead of building a temporary list per pair.
#
# time = O(n + k), k = total output length
# space = O(n) for the two strided slices, plus the output list
from itertools import chain, repeat


class Solution(object):
    def decompressRLElist(self, nums):
        pairs = zip(nums[0::2], nums[1::2])
        return list(chain.from_iterable(repeat(val, freq) for freq, val in pairs))

"""

2638. Count the Number of K-Free Subsets
Medium

You are given an integer array nums, which contains distinct elements and an integer k.

A subset is called a k-Free subset if it contains no two elements with an absolute difference equal to k. Notice that the empty set is a k-Free subset.

Return the number of k-Free subsets of nums.

A subset of an array is a selection of elements (possibly none) of the array.


Example 1:

Input: nums = [5,4,6], k = 1
Output: 5
Explanation: There are 5 valid subsets: {}, {5}, {4}, {6} and {4, 6}.

Example 2:

Input: nums = [2,3,5,8], k = 5
Output: 12
Explanation: There are 12 valid subsets: {}, {2}, {3}, {5}, {8}, {2, 3}, {2, 3, 5}, {2, 5}, {2, 5, 8}, {2, 8}, {3, 5} and {5, 8}.

Example 3:

Input: nums = [10,5,9,11], k = 20
Output: 16
Explanation: All subsets are valid. Since the total count of subsets is 2^4 = 16, so the answer is 16.


Constraints:

1 <= nums.length <= 50
1 <= nums[i] <= 1000
1 <= k <= 1000

"""

# V0
# IDEA : GROUP BY (value % k) -> CHAINS -> "HOUSE ROBBER" DP ON EACH CHAIN
#
#   two values can conflict only when they differ by exactly k, so they must
#   share the same residue mod k. that splits nums into INDEPENDENT groups
#   (residue classes) whose counts simply MULTIPLY.
#
#   inside one group, sort ascending. consecutive values that differ by
#   exactly k form a CHAIN v, v+k, v+2k, ... where each element conflicts
#   only with its chain neighbours -> counting valid subsets of a chain is
#   the classic "no two adjacent" count :
#       f = # subsets NOT taking the current element
#       g = # subsets     taking the current element
#       (f, g) -> (f + g, f)
#   which is just the Fibonacci numbers (a chain of length L gives Fib(L+2)).
#
#   NOTE : a gap larger than k inside the same residue class BREAKS the chain
#          (e.g. 2 and 12 with k = 5 do not conflict), so we must restart the
#          dp whenever nums[i] - nums[i-1] != k, not merely at group borders.
#
#   NOTE : the empty subset is counted once per chain, which is exactly what
#          the product needs (the global empty set is counted once).
#
"""

DP def
    two values conflict only when they differ by exactly k, so they must
    share the same value % k -> INDEPENDENT residue groups whose counts
    MULTIPLY. inside a sorted group, values differing by exactly k form a
    CHAIN v, v+k, v+2k, ... and the task becomes "no two adjacent".

    f: # subsets of the chain so far NOT taking the current element
    g: # subsets of the chain so far     taking the current element

DP eq

     (f, g) -> (f + g, f)


    -> e.g. this is the "house robber" / Fibonacci count:
              a chain of length L has Fib(L+2) valid subsets, and
              chain total = f + g

     NOTE !!! a gap LARGER than k inside the same residue class BREAKS the
              chain (2 and 12 with k = 5 do not conflict), so restart the dp
              whenever nums[i] - nums[i-1] != k, not only at group borders

     init: f = g = 1 (chain of length 1 -> 2 subsets)
     ans = product of (f + g) over all chains

"""
# time = O(n * log(n)), space = O(n)
class Solution(object):
    def countTheNumOfKFreeSubsets(self, nums, k):
        groups = {}
        for v in nums:
            groups.setdefault(v % k, []).append(v)

        ans = 1
        for vals in groups.values():
            vals.sort()
            # f = ways ending with "current NOT picked", g = "current picked"
            f, g = 1, 1                     # chain of length 1 -> f + g = 2
            for i in range(1, len(vals)):
                if vals[i] - vals[i - 1] == k:
                    f, g = f + g, f         # adjacent -> cannot pick both
                else:
                    ans *= (f + g)          # chain ends here, start a new one
                    f, g = 1, 1
            ans *= (f + g)
        return ans

"""

2763. Sum of Imbalance Numbers of All Subarrays
Hard

The imbalance number of a 0-indexed integer array arr of length n is defined as the number of indices in sarr = sorted(arr) such that:

0 <= i < n - 1, and
sarr[i+1] - sarr[i] > 1

Here, sorted(arr) is the function that returns the sorted version of arr.

Given a 0-indexed integer array nums, return the sum of imbalance numbers of all its subarrays.

A subarray is a contiguous non-empty sequence of elements within an array.


Example 1:

Input: nums = [2,3,1,4]
Output: 3
Explanation: There are 3 subarrays with non-zero imbalance numbers:
- Subarray [3, 1] with an imbalance number of 1.
- Subarray [3, 1, 4] with an imbalance number of 1.
- Subarray [1, 4] with an imbalance number of 1.
The imbalance number of all other subarrays is 0. Hence, the sum of imbalance numbers of all the subarrays of nums is 3.

Example 2:

Input: nums = [1,3,3,3,5]
Output: 8
Explanation: There are 7 subarrays with non-zero imbalance numbers:
- Subarray [1, 3] with an imbalance number of 1.
- Subarray [1, 3, 3] with an imbalance number of 1.
- Subarray [1, 3, 3, 3] with an imbalance number of 1.
- Subarray [1, 3, 3, 3, 5] with an imbalance number of 2.
- Subarray [3, 3, 3, 5] with an imbalance number of 1.
- Subarray [3, 3, 5] with an imbalance number of 1.
- Subarray [3, 5] with an imbalance number of 1.
The imbalance number of all other subarrays is 0. Hence, the sum of imbalance numbers of all the subarrays of nums is 8.


Constraints:

1 <= nums.length <= 1000
1 <= nums[i] <= nums.length

"""

# V0
# IDEA : GROW THE RIGHT END + REWRITE IMBALANCE AS A COUNT OVER THE VALUE SET
#
#   duplicates contribute a gap of 0, so only the DISTINCT values of a subarray
#   matter. Let S be that set of distinct values, d = |S|. Sorting S gives
#   d - 1 adjacent pairs, and a pair fails the "> 1" test exactly when the two
#   values are consecutive integers. Hence
#
#       imbalance(S) = (d - 1) - #{ v in S : v + 1 in S }
#
#   NOTE : this turns the sorted-array definition into something maintainable
#          incrementally — no SortedList / re-sorting needed.
#
#   So fix the left end i and push the right end j forward, keeping a boolean
#   `seen` table (values are bounded by n, so a plain array works):
#
#       new value v -> d += 1
#                      c += (v + 1 in S) + (v - 1 in S)   # 1 or 2 new joins
#       repeated v  -> S unchanged, imbalance unchanged
#
#   NOTE : `seen` is reset per left end by re-walking only the values that were
#          actually inserted, so the reset is O(subarray length), not O(n).
#
# time = O(n^2), space = O(n)
class Solution(object):
    def sumImbalanceNumbers(self, nums):
        n = len(nums)
        seen = [False] * (n + 2)      # values are in [1, n]
        res = 0

        for i in range(n):
            distinct = 0              # d
            joins = 0                 # c = #{v in S : v+1 in S}
            for j in range(i, n):
                v = nums[j]
                if not seen[v]:
                    seen[v] = True
                    distinct += 1
                    if seen[v + 1]:
                        joins += 1
                    if v - 1 >= 1 and seen[v - 1]:
                        joins += 1
                res += distinct - 1 - joins

            # undo this left end's insertions
            for j in range(i, n):
                seen[nums[j]] = False

        return res

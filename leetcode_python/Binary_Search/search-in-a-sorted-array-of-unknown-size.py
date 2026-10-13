# https://leetcode.ca/all/702.html


"""

702. Search in a Sorted Array of Unknown Size
Medium

This is an interactive problem.

You have a sorted array of unique elements and an unknown size. You do not have an access
to the array but you can use the ArrayReader interface to access it.
You can call ArrayReader.get(i) that:

  - returns the value at the ith index (0-indexed) of the secret array (i.e., secret[i]), or
  - returns 2^31 - 1 if the i is out of the boundary of the array.

You are also given an integer target.

Return the index k of the hidden array where secret[k] == target or return -1 otherwise.

You must write an algorithm with O(log n) runtime complexity.


Example 1:

Input: secret = [-1,0,3,5,9,12], target = 9
Output: 4
Explanation: 9 exists in secret and its index is 4.

Example 2:

Input: secret = [-1,0,3,5,9,12], target = 2
Output: -1
Explanation: 2 does not exist in secret so return -1.


Constraints:

1 <= secret.length <= 10^4
-10^4 <= secret[i], target <= 10^4
secret is sorted in a strictly increasing order.

"""


# """
# This is ArrayReader's API interface.
# You should not implement it, or speculate about its implementation
# """
# class ArrayReader:
#    def get(self, index: int) -> int:


# V0
class Solution:
    def search(self, reader, target):
        pass


# V0-1
# IDEA: 倍增法擴展邊界 + 標準二分搜 (GPT)
class Solution:
    def search(self, reader, target):
        # Step 1: Find the search range using exponential search
        l, r = 0, 1

        while reader.get(r) < target:
            l = r + 1
            r *= 2

        # Step 2: Binary search within the range
        while l <= r:
            mid = l + (r - l) // 2
            val = reader.get(mid)

            if val == target:
                return mid
            elif val > target:
                r = mid - 1
            else:
                l = mid + 1

        return -1



# V0-2
# IDEA: 倍增法擴展邊界 + 標準二分搜 (GEMINI)
class Solution:

  def search(self, reader, target):
    # 1. 透過倍增法 (Exponential Search) 動態尋找右界 r
    l, r = 0, 1
    while reader.get(r) < target:
      l = r
      r *= 2

    # 2. 在範圍 [l, r] 內進行標準二分搜
    while l <= r:
      mid = l + (r - l) // 2
      val = reader.get(mid)

      if val == target:
        return mid
      elif val > target:
        r = mid - 1
      else:
        l = mid + 1

    return -1


# V0-3
# IDEA : EXPONENTIAL SEARCH + BINARY SEARCH (claude)
#
#   Step 1) we do NOT know the array length, so grow a right bound by doubling
#           until reader.get(right) >= target. Out of bound reads return 2^31 - 1,
#           which is >= target, so the doubling always terminates.
#   Step 2) target (if present) now lies in [right // 2, right] -> plain binary search.
#
# time = O(log M), M = index of the target
# space = O(1)
class Solution(object):
    def search(self, reader, target):
        # step 1: exponentially widen the search window
        right = 1
        while reader.get(right) < target:
            right *= 2

        # step 2: binary search on [left, right], find leftmost index with value >= target
        left = right // 2
        while left < right:
            mid = (left + right) // 2
            if reader.get(mid) >= target:
                right = mid
            else:
                left = mid + 1

        return left if reader.get(left) == target else -1


# V1
# https://leetcode.ca/2017-11-01-702-Search-in-a-Sorted-Array-of-Unknown-Size/
class Solution:
    def search(self, reader, target):
        """
        :type reader: ArrayReader
        :type target: int
        :rtype: int
        """
        left, right = 0, 20000
        while left < right:
            mid = (left + right) >> 1
            if reader.get(mid) >= target:
                right = mid
            else:
                left = mid + 1
        return left if reader.get(left) == target else -1

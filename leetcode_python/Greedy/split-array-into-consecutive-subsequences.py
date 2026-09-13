"""

659. Split Array into Consecutive Subsequences
Medium

You are given an integer array nums that is sorted in non-decreasing order.

Determine if it is possible to split nums into one or more subsequences such that both of the following conditions are true:

Each subsequence is a consecutive increasing sequence (i.e. each integer is exactly one more than the previous integer).
All subsequences have a length of 3 or more.

Return true if you can split nums according to the above conditions, or false otherwise.

A subsequence of an array is a new array that is formed from the original array by deleting some (can be none) of the elements without disturbing the relative positions of the remaining elements. (i.e., [1,3,5] is a subsequence of [1,2,3,4,5] while [1,3,2] is not).

Example 1:

Input: nums = [1,2,3,3,4,5]
Output: true
Explanation: nums can be split into the following subsequences:
[1,2,3,3,4,5] --> 1, 2, 3
[1,2,3,3,4,5] --> 3, 4, 5

Example 2:

Input: nums = [1,2,3,3,4,4,5,5]
Output: true
Explanation: nums can be split into the following subsequences:
[1,2,3,3,4,4,5,5] --> 1, 2, 3, 4, 5
[1,2,3,3,4,4,5,5] --> 3, 4, 5

Example 3:

Input: nums = [1,2,3,4,4,5]
Output: false
Explanation: It is impossible to split nums into consecutive increasing subsequences of length 3 or more.

Constraints:

1 <= nums.length <= 10^4
-1000 <= nums[i] <= 1000
nums is sorted in non-decreasing order.

"""

# V0 

# V1
# https://blog.csdn.net/fuxuemingzhu/article/details/82185011
# time = O(n)
# space = O(n)
class Solution(object):
    def isPossible(self, nums):
        """
        :type nums: List[int]
        :rtype: bool
        """
        saved = collections.defaultdict(list)
        for num in nums:
            last = saved[num - 1]
            _len = 0 if (not last) else heapq.heappop(last)
            current = saved[num]
            heapq.heappush(current, _len + 1)
        for values in saved.values():
            for v in values:
                if v < 3:
                    return False
        return True
# V2
# time = O(n)
# space = O(1)
class Solution(object):
    def isPossible(self, nums):
        """
        :type nums: List[int]
        :rtype: bool
        """
        pre, cur = float("-inf"), 0
        cnt1, cnt2, cnt3 = 0, 0, 0
        i = 0
        while i < len(nums):
            cnt = 0
            cur = nums[i]
            while i < len(nums) and cur == nums[i]:
                cnt += 1
                i += 1

            if cur != pre + 1:
                if cnt1 != 0 or cnt2 != 0:
                    return False
                cnt1, cnt2, cnt3 = cnt, 0, 0
            else:
                if cnt < cnt1 + cnt2:
                    return False
                cnt1, cnt2, cnt3 = max(0, cnt - (cnt1 + cnt2 + cnt3)), \
                                   cnt1, \
                                   cnt2 + min(cnt3, cnt - (cnt1 + cnt2))
            pre = cur
        return cnt1 == 0 and cnt2 == 0

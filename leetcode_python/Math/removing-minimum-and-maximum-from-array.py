"""

2091. Removing Minimum and Maximum From Array
Medium

You are given a 0-indexed array of distinct integers nums.

There is an element in nums that has the lowest value and an element that has the highest value. We call them the minimum and maximum respectively. Your goal is to remove both these elements from the array.

A deletion is defined as removing an element from the front of the array or removing an element from the back of the array.

Return the minimum number of deletions it would take to remove both the minimum and maximum element.


Example 1:

Input: nums = [2,10,7,5,4,1,8,6]
Output: 5
Explanation:
The minimum element in the array is nums[5], which is 1.
The maximum element in the array is nums[1], which is 10.
We can remove both the minimum and maximum by removing 2 elements from the front and 3 elements from the back.
This results in 2 + 3 = 5 deletions, which is the minimum number possible.

Example 2:

Input: nums = [0,-4,19,1,8,-2,-3,5]
Output: 3
Explanation:
The minimum element in the array is nums[1], which is -4.
The maximum element in the array is nums[2], which is 19.
We can remove both the minimum and maximum by removing 3 elements from the front.
This results in only 3 deletions, which is the minimum number possible.

Example 3:

Input: nums = [101]
Output: 1
Explanation:
There is only one element in the array, which makes it both the minimum and maximum element.
We can remove it with 1 deletion.


Constraints:

1 <= nums.length <= 10^5
-10^5 <= nums[i] <= 10^5
The integers in nums are distinct.

"""

# V0
class Solution(object):
    def minimumDeletions(self, nums):
        """
        :type nums: List[int]
        :rtype: int
        """
        pass


# V0-1
# MATH + 3 deletie ways + index Normalize (gpt)
# Time: O(N)
# Space: O(1)
class Solution(object):
    def minimumDeletions(self, nums):
        """
        :type nums: List[int]
        :rtype: int
        """
        # Edge case
        if not nums:
            return 0

        n = len(nums)

        # Find the indices of minimum and maximum values
        idx_min = nums.index(min(nums))
        idx_max = nums.index(max(nums))

        # Normalize the indices:
        # left  = index closer to the beginning
        # right = index closer to the end
        left = min(idx_min, idx_max)
        right = max(idx_min, idx_max)

        # Way 1: Remove from the left only.
        # Delete everything from index 0 through right.
        way1 = right + 1

        # Way 2: Remove from the right only.
        # Delete everything from right through index n - 1,
        # but the leftmost extreme at index left must also be removed.
        # Equivalent to deleting the suffix starting at left.
        way2 = n - left

        # Way 3: Remove from both ends.
        # Remove the prefix through left,
        # and remove the suffix starting at right.
        way3 = (left + 1) + (n - right)

        return min(way1, way2, way3)




# V0-2
# MATH + 3 deletie ways + index Normalize (gemini)
# Time: O(N)
# Space: O(1)
class Solution(object):

  def minimumDeletions(self, nums):
    """:type nums: List[int]

    :rtype: int
    """
    if not nums:
      return 0

    n = len(nums)
    if n == 1:
      return 1

    idx_min = nums.index(min(nums))
    idx_max = nums.index(max(nums))

    # 讓 i 永遠代表靠左的索引，j 代表靠右的索引
    i = min(idx_min, idx_max)
    j = max(idx_min, idx_max)

    # 三種刪除策略：
    # 1. 兩者全部從左邊刪除（刪到最右邊的那個元素為止）
    way1 = j + 1

    # 2. 兩者全部從右邊刪除（刪到最左邊的那個元素為止）
    way2 = n - i

    # 3. 兩者分道揚鑣：一個從左邊刪到 i，另一個從右邊刪到 j
    way3 = (i + 1) + (n - j)

    return min(way1, way2, way3)



# V0-3
# MATH + 3 deletie ways (gemini)
# Time: O(N)
# Space: O(1)
class Solution(object):
    def minimumDeletions(self, nums):
        """
        :type nums: List[int]
        :rtype: int
        """

        # edge
        if not nums or len(nums) == 0:
            return 0

        if len(nums) == 1:
            return 1

        n = len(nums)

        _min = min(nums)
        _max = max(nums)

        idx_min = nums.index(_min)
        idx_max = nums.index(_max)

        op = n

        # 3 ways

        # way 1) all remove from left
        op_tmp = max(idx_min, idx_max) + 1
        op = min(op_tmp, op)

        # way 2) all remove from right
        op_tmp = max(n - 1 - idx_min, n - 1 - idx_max) + 1
        op = min(op_tmp, op)


        # way 3) remove from left, right
        """
        NOTE !!!


        idx_min, idx_max could be `swap`, so need to consider both cases.
        (depends on which is on the `right` hand side)

        e.g.

        ->

         xxx idx_min, xxx idx_max xxxxx
    
         or 

         xxx idx_max, xxx idx_min xxxxx


        -> NOTE !!!

        ```(idx_min + 1) + (n - 1 - idx_max + 1)```
    
            -> 這個公式只有在 idx_min < idx_max 時才正確，因為此時最小值位於左邊、最大值位於右邊。


        """
        op_tmp_1 = (idx_min + 1) + (n - 1 - idx_max + 1)
        op_tmp_2 = (idx_max + 1) + (n - 1 - idx_min + 1)

        op = min(op_tmp_1, op_tmp_2, op)


        return op


# V0-5
# IDEA : GREEDY — ONLY THREE STRATEGIES EXIST, TRY ALL THREE (CLAUDE)
#
#   deletions only happen at the two ends, so removing an element at index k
#   costs k + 1 from the front or n - k from the back. let left / right be the
#   positions of the min and the max, ordered so left <= right. to reach both:
#     1. take both from the FRONT  -> stop after the further one  : right + 1
#     2. take both from the BACK   -> stop after the nearer one   : n - left
#     3. the left one from the front, the right one from the back : (left + 1) + (n - right)
#   any other mix deletes a superset of one of these, so the answer is their min.
#
#   e.g. nums = [2,10,7,5,4,1,8,6] -> max at 1, min at 5, n = 8
#        front = 6, back = 7, split = 2 + 3 = 5 -> 5
#
# time = O(n), space = O(1)
class Solution(object):
    def minimumDeletions(self, nums):
        """
        :type nums: List[int]
        :rtype: int
        """
        n = len(nums)

        # one pass for the index of the min and the index of the max
        min_idx = 0
        max_idx = 0
        for i in range(1, n):
            if nums[i] < nums[min_idx]:
                min_idx = i
            if nums[i] > nums[max_idx]:
                max_idx = i

        # order them, so the three cases do not depend on which is the min
        left = min(min_idx, max_idx)
        right = max(min_idx, max_idx)

        # case 1: delete from the front until the further one is gone
        from_front = right + 1
        # case 2: delete from the back until the nearer one is gone
        from_back = n - left
        # NOTE !!! case 3 takes the LEFT one from the front and the RIGHT one
        #          from the back — never the other way round
        from_both_ends = (left + 1) + (n - right)

        return min(from_front, from_back, from_both_ends)

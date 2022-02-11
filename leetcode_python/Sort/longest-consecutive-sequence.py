"""

Given an unsorted array of integers nums, 
return the length of the longest consecutive elements sequence.
You must write an algorithm that runs in O(n) time.


Example 1:

Input: nums = [100,4,200,1,3,2]
Output: 4
Explanation: The longest consecutive elements sequence is [1, 2, 3, 4]. Therefore its length is 4.

Example 2:

Input: nums = [0,3,7,2,5,8,4,6,0,1]
Output: 9
 

Constraints:

0 <= nums.length <= 105
-109 <= nums[i] <= 109

"""

# V0
# IDEA : sliding window
class Solution(object):
    def longestConsecutive(self, nums):
        # edge case
        if not nums:
            return 0
        nums = list(set(nums))
        # if len(nums) == 1: # not necessary
        #     return 1
        # sort first
        nums.sort()
        res = 0
        l = 0
        r = 1
        """
        NOTE !!!

        Sliding window here :
            condition :  l, r are still in list (r < len(nums) and l < len(nums))

            2 cases

                case 1) nums[r] != nums[r-1] + 1
                    -> means not continous, 
                        -> so we need to move r to right (1 idx)
                        -> and MOVE l to r - 1, since it's NOT possible to have any continous subarray within [l, r] anymore
                case 2) nums[r] == nums[r-1] + 1
                        -> means there is continous subarray currently, so we keep moving r to right (r+=1) and get current max sub array length (res = max(res, r-l+1))
        """
        while r < len(nums) and l < len(nums):
            # case 1)
            if nums[r] != nums[r-1] + 1:
                r += 1
                l = (r-1)
            # case 2)
            else:
                res = max(res, r-l+1)
                r += 1
        # edge case : if res == 0, means no continous array (with len > 1), so we return 1 (a single alphabet can be recognized as a "continous assay", and its len = 1)
        return res if res > 1 else 1

# V0'
# IDEA : SORTING + 2 POINTERS
class Solution(object):
    def longestConsecutive(self, nums):
        # edge case
        if not nums:
            return 0

        nums.sort()
        cur_len = 1
        max_len = 1
        #print ("nums = " + str(nums))

        # NOTE : start from idx = 1
        for i in range(1, len(nums)):
            ### NOTE : start from nums[i] != nums[i-1] case
            if nums[i] != nums[i-1]:
                ### NOTE : if nums[i] == nums[i-1]+1 : cur_len += 1
                if nums[i] == nums[i-1]+1:
                    cur_len += 1
                ### NOTE : if nums[i] != nums[i-1]+1 : get max len, and reset cur_lent as 1
                else:
                    max_len = max(max_len, cur_len)
                    cur_len = 1
        # check max len again
        return max(max_len, cur_len)

# V0'
# IDEA : SORTING + 2 POINTERS
class Solution:
    def longestConsecutive(self, nums):
        if not nums:
            return 0

        nums.sort()

        longest_streak = 1
        current_streak = 1

        for i in range(1, len(nums)):
            if nums[i] != nums[i-1]:
                if nums[i] == nums[i-1]+1:
                    current_streak += 1
                else:
                    longest_streak = max(longest_streak, current_streak)
                    current_streak = 1

        return max(longest_streak, current_streak)

# V0'
# IDEA : HASHSET
class Solution:
    def longestConsecutive(self, nums):
        longest_streak = 0
        num_set = set(nums)

        for num in num_set:
            if num - 1 not in num_set:
                current_num = num
                current_streak = 1

                while current_num + 1 in num_set:
                    current_num += 1
                    current_streak += 1

                longest_streak = max(longest_streak, current_streak)

        return longest_streak

# V1
# IDEA : BRUTE FORCE
# https://leetcode.com/problems/longest-consecutive-sequence/solution/
class Solution:
    def longestConsecutive(self, nums):
        longest_streak = 0

        for num in nums:
            current_num = num
            current_streak = 1

            while current_num + 1 in nums:
                current_num += 1
                current_streak += 1

            longest_streak = max(longest_streak, current_streak)

        return longest_streak

# V2
# IDEA : SORTING
# https://leetcode.com/problems/longest-consecutive-sequence/solution/
class Solution:
    def longestConsecutive(self, nums):
        if not nums:
            return 0

        nums.sort()

        longest_streak = 1
        current_streak = 1

        for i in range(1, len(nums)):
            if nums[i] != nums[i-1]:
                if nums[i] == nums[i-1]+1:
                    current_streak += 1
                else:
                    longest_streak = max(longest_streak, current_streak)
                    current_streak = 1

        return max(longest_streak, current_streak)


# V3
# IDEA : HashSet and Intelligent Sequence Building
# https://leetcode.com/problems/longest-consecutive-sequence/solution/
class Solution:
    def longestConsecutive(self, nums):
        longest_streak = 0
        num_set = set(nums)

        for num in num_set:
            if num - 1 not in num_set:
                current_num = num
                current_streak = 1

                while current_num + 1 in num_set:
                    current_num += 1
                    current_streak += 1

                longest_streak = max(longest_streak, current_streak)

        return longest_streak


# V4
# https://leetcode.com/problems/longest-consecutive-sequence/discuss/1231015/Concise-Python-Solution-8-lines
# https://www.youtube.com/watch?v=rc2QdQ7U78I
class Solution:
    def longestConsecutive(self, nums: List[int]) -> int:
    # Concise Solution from https://www.youtube.com/watch?v=rc2QdQ7U78I&t=24s
        if not nums: return 0
        map_ = {}
        for num in set(nums):
            l = map_.get(num - 1, 0)
            r = map_.get(num + 1, 0)
            t = l + r + 1
            map_[num] = map_[num - l] = map_[num + r] = l + r + 1
        return max(map_.values())

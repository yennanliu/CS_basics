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
# IDEA : SORTING
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

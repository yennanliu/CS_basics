# https://leetcode.com/problems/maximum-number-of-non-overlapping-subarrays-with-sum-equals-target/description/

"""

1546. Maximum Number of Non-Overlapping Subarrays With Sum Equals Target
Medium
Topics
premium lock icon
Companies
Hint
Given an array nums and an integer target, return the maximum number of non-empty non-overlapping subarrays such that the sum of values in each subarray is equal to target.

 

Example 1:

Input: nums = [1,1,1,1,1], target = 2
Output: 2
Explanation: There are 2 non-overlapping subarrays [1,1,1,1,1] with sum equals to target(2).
Example 2:

Input: nums = [-1,3,5,1,4,2,-9], target = 6
Output: 2
Explanation: There are 3 subarrays with sum equal to 6.
([5,1], [4,2], [3,5,1,4,2,-9]) but only the first 2 are non-overlapping.
 

Constraints:

1 <= nums.length <= 105
-104 <= nums[i] <= 104
0 <= target <= 106

"""


# V0
class Solution(object):
    def maxNonOverlapping(self, nums, target):
        pass


# V1-1
# IDEA: greedy + PREFIX + HASHMAP (gpt)
"""

- Core Greedy Idea

Instead of storing every interval:

Maintain prefix sums only since the last accepted subarray.
Once you find one subarray summing to target, 

-> immediately count it and `restart`.

Why?

Choosing the earliest ending valid interval 
always leaves the `most room` for future intervals.


"""

"""
Dry run


input:

nums = [-1,3,5,1,4,2,-9]
target = 6


->

init

seen = {0}
prefix = 0
ans = 0


run

| num | prefix | prefix-target | found? | ans | seen     |
| --- | ------ | ------------- | ------ | --- | -------- |
| -1  | -1     | -7            | No     | 0   | {0,-1}   |
| 3   | 2      | -4            | No     | 0   | {0,-1,2} |
| 5   | 7      | 1             | No     | 0   | {...,7}  |
| 1   | 8      | 2             | Yes    | 1   | restart  |
| 4   | 4      | -2            | No     | 1   | {0,4}    |
| 2   | 6      | 0             | Yes    | 2   | restart  |
| -9  | -9     | -15           | No     | 2   | {0,-9}   |


"""
class Solution(object):
    def maxNonOverlapping(self, nums, target):
        seen = set([0])

        prefix = 0
        ans = 0

        for x in nums:
            prefix += x

            if prefix - target in seen:
                ans += 1

                # restart searching after this interval
                seen = set([0])
                prefix = 0
            else:
                seen.add(prefix)

        return ans


# V1-2
# IDEA: greedy + PREFIX + HASHMAP (GEMINI)
"""
NOTE !!!

Greedy `reset` idea:

💡 The Strategy

As you loop through the array, 
keep a running prefix sum and look for `prefix - target` in your tracking set.


- The moment you find a match, 
  it means you have discovered a 
  valid subarray that equals the target.

- To `maximize` the total count,
  you should instantly lock this subarray in,
  increment your counter, 
  and `clear` your history (reset the prefix sum tracking).

- Clearing the tracking history forces 
  any future valid subarrays to start
  after the current index, ensuring 
  they never overlap with the one you just completed.


"""
class Solution(object):
    def maxNonOverlapping(self, nums, target):

        # Set to track prefix sums seen
        # in the current non-overlapping segment
        seen_prefixes = set()
        
        # Base case: A prefix sum of 0 is always seen at the start
        seen_prefixes.add(0)
        
        running_prefix = 0
        cnt = 0
        
        for num in nums:
            running_prefix += num
            
            # Check if there is a matching complement prefix sum
            if (running_prefix - target) in seen_prefixes:
                cnt += 1
                
                # GREEDY RESET: Clear everything to prevent overlaps
                seen_prefixes.clear()
                running_prefix = 0
                
            # Store the current prefix sum as a valid starting point for next options
            seen_prefixes.add(running_prefix)
            
        return cnt


# V2
# IDEA: HASHMAP
# https://leetcode.com/problems/maximum-number-of-non-overlapping-subarrays-with-sum-equals-target/solutions/780954/python-simple-solution-2-sum-variant-by-ihtfh/
class Solution:
    def maxNonOverlapping(self, nums: List[int], target: int) -> int:
        ## RC ##
        ## APPROACH : HASHMAP - 2 SUM VARIANT ##
        lookup = {0 : -1}
        running_sum  = 0
        count = 0
        for i in range(len(nums)):
            running_sum += nums[i]
            if running_sum - target in lookup:
                count += 1
                lookup = {} #reset the map
            lookup[running_sum] = i
        return count

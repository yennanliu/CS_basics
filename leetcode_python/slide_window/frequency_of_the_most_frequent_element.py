"""


1838. Frequency of the Most Frequent Element
Solved
Medium
Topics
premium lock icon
Companies
Hint
The frequency of an element is the number of times it occurs in an array.

You are given an integer array nums and an integer k. In one operation, you can choose an index of nums and increment the element at that index by 1.

Return the maximum possible frequency of an element after performing at most k operations.

 

Example 1:

Input: nums = [1,2,4], k = 5
Output: 3
Explanation: Increment the first element three times and the second element two times to make nums = [4,4,4].
4 has a frequency of 3.
Example 2:

Input: nums = [1,4,8,13], k = 5
Output: 2
Explanation: There are multiple optimal solutions:
- Increment the first element three times to make nums = [4,4,8,13]. 4 has a frequency of 2.
- Increment the second element four times to make nums = [1,8,8,13]. 8 has a frequency of 2.
- Increment the third element five times to make nums = [1,4,13,13]. 13 has a frequency of 2.
Example 3:

Input: nums = [3,9,6], k = 2
Output: 1
 

Constraints:

1 <= nums.length <= 105
1 <= nums[i] <= 105
1 <= k <= 105
 


"""



# V0
# class Solution(object):
#     def maxFrequency(self, nums, k):
#         """
#         :type nums: List[int]
#         :type k: int
#         :rtype: int
#         """
        

# V1
# IDEA: SLIDE WINDOW
# time = O(n log n)  # dominated by sort; sliding window itself is O(n) amortized
# space = O(1)  # excluding sort's internal space
class Solution(object):
    def maxFrequency(self, nums, k):
        # Sort so we can always raise smaller numbers
        # to match the largest number in the window.
        nums.sort()

        # Left pointer of sliding window
        l = 0

        # Running sum of current window
        window_sum = 0

        # At least one element always has frequency 1
        max_freq = 1

        # Expand window using r
        for r in range(len(nums)):
            # Add current element into window
            window_sum += nums[r]

            # Cost to make every element in window
            # equal to nums[r]
            #
            # target = nums[r]
            # cost = sum(target - nums[i])
            #
            # Algebra:
            # cost = target * window_size - window_sum
            while nums[r] * (r - l + 1) - window_sum > k:
                # Window is too expensive
                # Remove leftmost element
                window_sum -= nums[l]
                l += 1

            # Current window is valid
            max_freq = max(max_freq, r - l + 1)

        return max_freq


# V2
# IDEA: SLIDE WINDOW
# time = O(n log n)  # dominated by sort; sliding window itself is O(n) amortized
# space = O(1)  # excluding sort's internal space
class Solution(object):
    def maxFrequency(self, nums, k):
        """
        :type nums: List[int]
        :type k: int
        :rtype: int
        """
        # Step 1: Sort the array to ensure adjacent numbers require minimal edits
        nums.sort()
        
        l = 0
        max_freq = 0
        current_window_sum = 0
        
        # Step 2: Slide the right pointer 'r' across the entire array
        for r in range(len(nums)):
            # Add the incoming element to our running window sum
            current_window_sum += nums[r]
            
            # Calculate the total operations required to make all elements 
            # in the current window [l...r] equal to the target value nums[r]
            window_length = r - l + 1
            total_cost = (window_length * nums[r]) - current_window_sum
            
            # CRITICAL FIX: If the cost exceeds our budget 'k', 
            # shrink the window from the left until it becomes valid
            while total_cost > k:
                current_window_sum -= nums[l]
                l += 1
                # Recalculate cost with the smaller window size
                window_length = r - l + 1
                total_cost = (window_length * nums[r]) - current_window_sum
                
            # Maintain the largest valid window size found so far
            max_freq = max(max_freq, window_length)
            
        return max_freq


"""

2104. Sum of Subarray Ranges
Medium

You are given an integer array nums. The range of a subarray of nums is the difference between the largest and smallest element in the subarray.

Return the sum of all subarray ranges of nums.

A subarray is a contiguous non-empty sequence of elements within an array.

 

Example 1:

Input: nums = [1,2,3]
Output: 4
Explanation: The 6 subarrays of nums are the following:
[1], range = largest - smallest = 1 - 1 = 0 
[2], range = 2 - 2 = 0
[3], range = 3 - 3 = 0
[1,2], range = 2 - 1 = 1
[2,3], range = 3 - 2 = 1
[1,2,3], range = 3 - 1 = 2
So the sum of all ranges is 0 + 0 + 0 + 1 + 1 + 2 = 4.
Example 2:

Input: nums = [1,3,3]
Output: 4
Explanation: The 6 subarrays of nums are the following:
[1], range = largest - smallest = 1 - 1 = 0
[3], range = 3 - 3 = 0
[3], range = 3 - 3 = 0
[1,3], range = 3 - 1 = 2
[3,3], range = 3 - 3 = 0
[1,3,3], range = 3 - 1 = 2
So the sum of all ranges is 0 + 0 + 0 + 2 + 0 + 2 = 4.
Example 3:

Input: nums = [4,-2,-3,4,1]
Output: 59
Explanation: The sum of all subarray ranges of nums is 59.
 

Constraints:

1 <= nums.length <= 1000
-109 <= nums[i] <= 109
 

Follow-up: Could you find a solution with O(n) time complexity?

"""


# V0
# time = O(1)
# space = O(1)
class Solution(object):
    def subArrayRanges(self, nums):
        """
        :type nums: List[int]
        :rtype: int
        """
        pass


# V0-1
# IDEA: BRUTE FORCE (gpt)
# time = O(n^2)
# space = O(1)
class Solution(object):
    def subArrayRanges(self, nums):
        n = len(nums)
        ans = 0

        # Choose every possible starting index.
        for i in range(n):

            # Initially, the subarray contains only nums[i].
            cur_min = nums[i]
            cur_max = nums[i]

            # Extend the subarray one element at a time.
            for j in range(i, n):

                # Update the current minimum.
                cur_min = min(cur_min, nums[j])

                # Update the current maximum.
                cur_max = max(cur_max, nums[j])

                # Add the range of the current subarray.
                ans += (cur_max - cur_min)

        return ans


# V0-2
# IDEA: MONO STACK (gpt)
"""

1. CORE IDEA:



For every subarray:

    ```
    range = max(subarray) - min(subarray)

    ```

-----

2.   Why above ??



->

假設 `nums[mid]` 是某些子陣列的最大值或最小值。


->

我們找出兩個邊界：

    - left：左側最近一個會阻止 nums[mid] 繼續作為極值的元素。
    - right：右側最近一個會阻止 nums[mid] 繼續作為極值的元素。


->

那麼，以 nums[mid] 作為所選極值的`子陣列` `數量`為：

    
    ```
    count = (mid - left) * (right - mid)
    ```

    (因為左端點有 mid - left 種選擇，右端點有 right - mid 種選擇)




->

元素對答案的貢獻就是：
    
    ```
    contribution = nums[mid] * count
    ```



-> understand in the other way

    
    -> 每一個子陣列的 Range，就是「最大值減掉最小值」。
       所以把所有子陣列的 Range 加起來，
       自然就等於「所有最大值的總和」減掉「所有最小值的總和」。



-----

3. 


Return the sum of all subarray ranges.

Example:

```python
nums = [1,2,3]
```

Subarrays:

| Subarray | Max | Min | Range |
|-----------|------|------|--------|
| [1] | 1 | 1 | 0 |
| [2] | 2 | 2 | 0 |
| [3] | 3 | 3 | 0 |
| [1,2] | 2 | 1 | 1 |
| [2,3] | 3 | 2 | 1 |
| [1,2,3] | 3 | 1 | 2 |

Total:

```python
0 + 0 + 0 + 1 + 1 + 2 = 4
```

---

# Key Observation

Instead of calculating:

```python
(max - min)
```

for every subarray,

we can compute:

```python
(sum of all subarray maximums)
-
(sum of all subarray minimums)
```

because:

\[
\sum(\max - \min)
=
\sum(\max)
-
\sum(\min)
\]

So the problem becomes:

1. Find the sum of all subarray maximums.
2. Find the sum of all subarray minimums.
3. Subtract.

---

# Contribution Technique

Instead of iterating through all subarrays, think from each element's perspective.

For an element:

```python
nums[i]
```

determine:

- How many subarrays use it as the maximum?
- How many subarrays use it as the minimum?

Then:

```python
contribution = nums[i] * count
```

---

# Core Formula

Suppose index `mid` is the element being processed.

We find:

```text
left    mid    right
 |       |       |
```

where:

- `left` = previous boundary
- `right` = next boundary

Then the number of subarrays where `nums[mid]` is the chosen max/min is:

```python
count = (mid - left) * (right - mid)
```

"""
# time = O(n)
# space = O(n)
class Solution(object):
    def subArrayRanges(self, nums):
        """
        :type nums: List[int]
        :rtype: int

        Key idea:
        Sum of subarray ranges
        = sum of all subarray maximums
        - sum of all subarray minimums.

        Use Monotonic Stack to calculate both sums in O(N).
        """
        n = len(nums)

        # ==================================================
        # Part 1: Calculate the sum of all subarray maximums
        # ==================================================

        max_sum = 0
        stack = []  # Store indices, not values.
                    # Maintain a monotonically decreasing stack.

        # Use i == n as a virtual boundary to flush the stack.
        # Every remaining element must be processed at the end.
        for i in range(n + 1):

            # If the current value is greater than the value
            # at the top of the stack, the top element's right
            # boundary has been found.
            #
            # When i == n, flush all remaining elements.
            while stack and (
                i == n or nums[stack[-1]] < nums[i]
            ):
                # The popped index is the element whose
                # contribution to the maximum sum we calculate.
                mid = stack.pop()

                # After popping:
                # left  = previous index with value >= nums[mid]
                # right = next index with value > nums[mid]
                #
                # If no left boundary exists, use -1.
                left = stack[-1] if stack else -1
                right = i

                # Count the subarrays in which nums[mid]
                # is assigned as the maximum.
                #
                # Left endpoint choices:  mid - left
                # Right endpoint choices: right - mid
                #
                # Multiply the two independent choices.
                count = (mid - left) * (right - mid)

                # nums[mid] is the maximum in 'count' subarrays,
                # so its total contribution is value * count.
                max_sum += nums[mid] * count

            # Push valid indices only.
            # Index n is a virtual boundary, not a real element.
            if i < n:
                stack.append(i)

        # ==================================================
        # Part 2: Calculate the sum of all subarray minimums
        # ==================================================

        min_sum = 0
        stack = []  # Reset the stack for minimum calculation.
                    # Maintain a monotonically increasing stack.

        for i in range(n + 1):

            # If the current value is smaller than the value
            # at the top of the stack, the top element's right
            # boundary has been found.
            #
            # When i == n, flush all remaining elements.
            while stack and (
                i == n or nums[stack[-1]] > nums[i]
            ):
                mid = stack.pop()

                # Previous index with value <= nums[mid]
                # Next index with value < nums[mid]
                left = stack[-1] if stack else -1
                right = i

                # Number of subarrays in which nums[mid]
                # is assigned as the minimum.
                count = (mid - left) * (right - mid)

                # Add nums[mid]'s total contribution
                # to the sum of all subarray minimums.
                min_sum += nums[mid] * count

            if i < n:
                stack.append(i)

        # Every subarray's range is maximum - minimum.
        # Therefore, subtract the two aggregate sums.
        return max_sum - min_sum


# V0-2-1
# IDEA: MONO STACK (GEMINI)
class Solution:

  def subArrayRanges(self, nums):
    """:type nums: List[int]

    :rtype: int
    """
    n = len(nums)

    # =========================================================================
    # CORE IDEA:
    # Sum of all subarray ranges = (Sum of all subarray maxs) - (Sum of all subarray mins)
    #
    # To compute this in O(N) time instead of O(N^2), we use a Monotonic Stack.
    # For every element nums[mid], we find its "influence span" — i.e., how many
    # subarrays have nums[mid] as their strict Maximum (or Minimum).
    #
    # We iterate up to range(n + 1) using i == n as a sentinel to flush out
    # any remaining elements in the stack at the end.
    # =========================================================================

    # -----------------------------------------------------------------
    # Part 1: Sum of all subarray maximums
    # -----------------------------------------------------------------
    max_sum = 0
    stack = []  # Stores indices, maintaining elements in decreasing order

    for i in range(n + 1):
      # When we encounter an element greater than or equal to the element at stack[-1],
      # the element at stack[-1] has found its right boundary (index i).
      # (Using >= or > prevents duplicate counting for duplicate values in the array)
      while stack and (i == n or nums[stack[-1]] < nums[i]):
        mid = stack.pop()

        # Left boundary: the previous smaller element's index in stack, or -1 if empty
        left = stack[-1] if stack else -1
        # Right boundary: current index i
        right = i

        # Combinatorics: total subarrays where nums[mid] is the maximum
        # = (number of valid choices for left endpoint) * (number of choices for right endpoint)
        count = (mid - left) * (right - mid)

        max_sum += nums[mid] * count

      stack.append(i)

    # -----------------------------------------------------------------
    # Part 2: Sum of all subarray minimums
    # -----------------------------------------------------------------
    min_sum = 0
    stack = []  # Stores indices, maintaining elements in increasing order

    for i in range(n + 1):
      # When we encounter an element smaller than the element at stack[-1],
      # the element at stack[-1] has found its right boundary (index i).
      while stack and (i == n or nums[stack[-1]] > nums[i]):
        mid = stack.pop()

        # Left boundary: the previous larger element's index in stack, or -1 if empty
        left = stack[-1] if stack else -1
        # Right boundary: current index i
        right = i

        # Combinatorics: total subarrays where nums[mid] is the minimum
        count = (mid - left) * (right - mid)

        min_sum += nums[mid] * count

      stack.append(i)

    # Return total maximum sum minus total minimum sum
    return max_sum - min_sum


# V0-3
# IDEA: MONO STACK (gpt)
# time = O(n)
# space = O(n)
class Solution:
    def subArrayRanges(self, nums):

        # Helper function:
        # Calculate the total contribution of each element
        # either as a MAXIMUM or as a MINIMUM.
        #
        # is_max = True  -> compute sum of all subarray maximums
        # is_max = False -> compute sum of all subarray minimums
        def contribution(is_max):

            # Length of input array
            n = len(nums)

            # Monotonic stack stores indices
            stack = []

            # Running total contribution
            total = 0

            # Iterate one extra position (n)
            #
            # The extra iteration acts as a "sentinel"
            # that forces all remaining elements to be popped.
            #
            # Example:
            # nums = [1,2,3]
            #
            # At i == n, we trigger processing for any
            # elements still left in the stack.
            for i in range(n + 1):

                # Pop while current element breaks
                # the monotonic property.
                #
                # For `maximum` calculation:
                #     maintain `decreasing` stack
                #
                # For `minimum` calculation:
                #     maintain `increasing` stack
                while stack and (

                    # Sentinel case:
                    # flush remaining stack elements
                    i == n

                    or

                    # Maximum mode:
                    # pop when current value is larger
                    # than stack top
                    (
                        nums[stack[-1]] < nums[i]
                        if is_max

                        # Minimum mode:
                        # pop when current value is smaller
                        # than stack top
                        else nums[stack[-1]] > nums[i]
                    )
                ):

                    # Current index being processed
                    #
                    # This element's contribution can now
                    # be fully determined because we found
                    # its next greater/smaller boundary.
                    mid = stack.pop()

                    # Previous boundary
                    #
                    # If stack becomes empty,
                    # there is no previous boundary.
                    #
                    # Use -1 as a virtual boundary.
                    left = stack[-1] if stack else -1

                    # Current index is the next boundary
                    right = i

                    # Count how many subarrays use nums[mid]
                    # as the maximum (or minimum).
                    #
                    # Left choices:
                    #     mid - left
                    #
                    # Right choices:
                    #     right - mid
                    #
                    # Total subarrays:
                    #     (mid-left) * (right-mid)
                    #
                    # Contribution:
                    #     value * count
                    total += (
                        nums[mid]
                        * (mid - left)
                        * (right - mid)
                    )

                # Push current index into stack
                #
                # Stack remains monotonic:
                #
                # Maximum mode:
                #     decreasing values
                #
                # Minimum mode:
                #     increasing values
                stack.append(i)

            # Return total maximum contribution
            # or total minimum contribution
            return total

        # Sum of subarray ranges:
        #
        # range = max - min
        #
        # Therefore:
        #
        # sum(range)
        # =
        # sum(all maximum contributions)
        # -
        # sum(all minimum contributions)
        return contribution(True) - contribution(False)



# V0-4
# IDEA: MONO STACK (gemini)
# time = O(n)
# space = O(n)
class Solution(object):
    def subArrayRanges(self, nums):
        """
        :type nums: List[int]
        :rtype: int
        """
        return self.sumSubarrayMaxs(nums) - self.sumSubarrayMins(nums)

    def sumSubarrayMins(self, nums):
        """
        Calculates the sum of minimum values across all subarrays in O(N).
        Identical to LC 907.
        """
        n = len(nums)
        # Initialize default boundaries: index -1 on the left, index n on the right
        left = [-1] * n
        right = [n] * n
        
        # Monotonic Increasing Stack to find the next/previous smaller elements
        stack = []
        
        # Step 1: Find how far left each element can rule as the minimum
        for i in range(n):
            while stack and nums[stack[-1]] >= nums[i]:
                stack.pop()
            if stack:
                left[i] = stack[-1]
            stack.append(i)
            
        stack = [] # Reset the stack
        
        # Step 2: Find how far right each element can rule as the minimum
        # Note: We use strictly > to gracefully handle duplicate elements without double-counting
        for i in range(n - 1, -1, -1):
            while stack and nums[stack[-1]] > nums[i]:
                stack.pop()
            if stack:
                right[i] = stack[-1]
            stack.append(i)
            
        # Step 3: Count total combinations
        total_mins = 0
        for i in range(n):
            num_subarrays = (i - left[i]) * (right[i] - i)
            total_mins += nums[i] * num_subarrays
            
        return total_mins

    def sumSubarrayMaxs(self, nums):
        """
        Calculates the sum of maximum values across all subarrays in O(N).
        """
        n = len(nums)
        left = [-1] * n
        right = [n] * n
        
        # Monotonic Decreasing Stack to find the next/previous larger elements
        stack = []
        
        # Step 1: Find how far left each element rules as the maximum
        for i in range(n):
            while stack and nums[stack[-1]] <= nums[i]:
                stack.pop()
            if stack:
                left[i] = stack[-1]
            stack.append(i)
            
        stack = [] # Reset the stack
        
        # Step 2: Find how far right each element rules as the maximum
        for i in range(n - 1, -1, -1):
            while stack and nums[stack[-1]] < nums[i]:
                stack.pop()
            if stack:
                right[i] = stack[-1]
            stack.append(i)
            
        # Step 3: Count total combinations
        total_maxs = 0
        for i in range(n):
            num_subarrays = (i - left[i]) * (right[i] - i)
            total_maxs += nums[i] * num_subarrays
            
        return total_maxs




# V0-5
# IDEA : BRUTE FORCE
# time = O(n^2)
# space = O(1)
class Solution:
    def subArrayRanges(self, nums):
        res = 0
        # 1st loop
        for i in range(len(nums)):
            curMin = float("inf")
            curMax = -float("inf")
            # 2nd loop
            for j in range(i, len(nums)):
                curMin = min(curMin, nums[j])
                curMax = max(curMax, nums[j])
                res += curMax - curMin
        return res

# V0-6
# IDEA : monotonic stack
# https://zhuanlan.zhihu.com/p/444725220
# time = O(n)
# space = O(n)
class Solution:
    def subArrayRanges(self, nums):
        A, s, res = [-float('inf')] + nums + [-float('inf')], [], 0
        for i, num in enumerate(A):
            while s and num < A[s[-1]]:
                j = s.pop()
                res -= (i - j) * (j - s[-1]) * A[j]
            s.append(i)
        A, s = [float('inf')] + nums + [float('inf')], []
        for i, num in enumerate(A):
            while s and num > A[s[-1]]:
                j = s.pop()
                res += (i - j) * (j - s[-1]) * A[j]
            s.append(i)
        return res 

# V0-7
# IDEA : INCREASING STACK
# time = O(n)
# space = O(n)
class Solution:
    def subArrayRanges(self, A0):
        res = 0
        inf = float('inf')
        A = [-inf] + A0 + [-inf]
        s = []
        for i, x in enumerate(A):
            while s and A[s[-1]] > x:
                j = s.pop()
                k = s[-1]
                res -= A[j] * (i - j) * (j - k)
            s.append(i)
            
        A = [inf] + A0 + [inf]
        s = []
        for i, x in enumerate(A):
            while s and A[s[-1]] < x:
                j = s.pop()
                k = s[-1]
                res += A[j] * (i - j) * (j - k)
            s.append(i)
        return res

# V1
# IDEA : BRUTE FORCE
# https://leetcode.com/problems/sum-of-subarray-ranges/discuss/1624303/python-bruct-foce
# time = O(n^2)
# space = O(1)
class Solution:
    def subArrayRanges(self, nums):
        res = 0
        for i in range(len(nums)):
            curMin = float("inf")
            curMax = -float("inf")
            for j in range(i, len(nums)):
                curMin = min(curMin, nums[j])
                curMax = max(curMax, nums[j])
                res += curMax - curMin
        return res

# V1'
# IDEA : 2 POINTERS + stack
# https://leetcode.com/problems/sum-of-subarray-ranges/discuss/1624222/JavaC%2B%2BPython-O(n)-solution-detailed-explanation
# IDEA:
# Follow the explanation in 907. Sum of Subarray Minimums
#
# Intuition
# res = sum(A[i] * f(i))
# where f(i) is the number of subarrays,
# in which A[i] is the minimum.
#
# To get f(i), we need to find out:
# left[i], the length of strict bigger numbers on the left of A[i],
# right[i], the length of bigger numbers on the right of A[i].
#
# Then,
# left[i] + 1 equals to
# the number of subarray ending with A[i],
# and A[i] is single minimum.
#
# right[i] + 1 equals to
# the number of subarray starting with A[i],
# and A[i] is the first minimum.
#
# Finally f(i) = (left[i] + 1) * (right[i] + 1)
#
# For [3,1,2,4] as example:
# left + 1 = [1,2,1,1]
# right + 1 = [1,3,2,1]
# f = [1,6,2,1]
# res = 3 * 1 + 1 * 6 + 2 * 2 + 4 * 1 = 17
#
# Explanation
# To calculate left[i] and right[i],
# we use two increasing stacks.
#
# It will be easy if you can refer to this problem and my post:
# 901. Online Stock Span
# I copy some of my codes from this solution.
# time = O(n)
# space = O(n)
class Solution:
    def subArrayRanges(self, A0):
        res = 0
        inf = float('inf')
        A = [-inf] + A0 + [-inf]
        s = []
        for i, x in enumerate(A):
            while s and A[s[-1]] > x:
                j = s.pop()
                k = s[-1]
                res -= A[j] * (i - j) * (j - k)
            s.append(i)
            
        A = [inf] + A0 + [inf]
        s = []
        for i, x in enumerate(A):
            while s and A[s[-1]] < x:
                j = s.pop()
                k = s[-1]
                res += A[j] * (i - j) * (j - k)
            s.append(i)
        return res

# V1''
# IDEA : monotonic stack
# https://zhuanlan.zhihu.com/p/444725220
# time = O(n)
# space = O(n)
class Solution:
    def subArrayRanges(self, nums):
        A, s, res = [-float('inf')] + nums + [-float('inf')], [], 0
        for i, num in enumerate(A):
            while s and num < A[s[-1]]:
                j = s.pop()
                res -= (i - j) * (j - s[-1]) * A[j]
            s.append(i)
        A, s = [float('inf')] + nums + [float('inf')], []
        for i, num in enumerate(A):
            while s and num > A[s[-1]]:
                j = s.pop()
                res += (i - j) * (j - s[-1]) * A[j]
            s.append(i)
        return res 

# V1'''
# IDEA : BRUTE FORCE
# https://blog.csdn.net/sinat_30403031/article/details/122082809

# V1''''
# IDEA : 2 POINTERS
# https://www.codeleading.com/article/65096149220/
# time = O(n^2)
# space = O(1)
class Solution:
    def subArrayRanges(self, nums):
        count = 0
        for i in range(len(nums) - 1):
            # set left index = i
            max_num = min_num = nums[i]
            # here we need to record current max, min. So can count diff sum
            for t in range(i+1, len(nums)):
                # keep moving left pointer
                if nums[t] < min_num:
                    min_num = nums[t]
                if nums[t] > max_num:
                    max_num = nums[t]
                count += max_num - min_num
        return count

# V1'''''
# IDEA : sumSubarray
# https://leetcode.com/problems/sum-of-subarray-ranges/discuss/1638345/Python-0(n)
# time = O(n)
# space = O(n)
class Solution:
    def subArrayRanges(self, nums):
        return self.sumSubarray(nums, operator.gt) - self.sumSubarray(nums, operator.lt)
    
    def sumSubarray(self, arr, comp):
        n = len(arr) 
        stack = []
        res = 0
            
        for idx in range(n+1):
            while stack and (idx == n or comp(arr[idx], arr[stack[-1]])):
                curr, prev = stack[-1], stack[-2] if len(stack) > 1 else -1
                res = res + (arr[curr] * (idx - curr) * (curr - prev))
                stack.pop()
            stack.append(idx)
        
        return res

# V1''''''
# IDEA : DP
# https://leetcode.com/problems/sum-of-subarray-ranges/discuss/1624305/Python-DP-Solution
# time = O(n^2)
# space = O(n^2)
class Solution:
    def subArrayRanges(self, nums):
            dp = [[(None, None) for i in range(len(nums))] for j in range(len(nums))]
            dp[len(nums) -1][len(nums) -1] = (nums[len(nums) -1], nums[len(nums) -1])

            res = 0
            for i in range(len(nums) -2, -1, -1):
                for j in range(i, len(nums)):
                    if dp[i][j-1] != (None, None):
                        dp[i][j] = (max(dp[i][j-1][0], nums[j]), min(dp[i][j-1][1], nums[j]))

                    elif dp[i + 1][j] != (None, None):
                        dp[i][j] = (max(dp[i + 1][j][0], nums[i]), min(dp[i + 1][j][1], nums[i]))
                    else:
                        dp[i][j] = (nums[i], nums[j])

                    res += dp[i][j][0] - dp[i][j][1]
            return res

# V2
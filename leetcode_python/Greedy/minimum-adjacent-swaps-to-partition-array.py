# https://leetcode.com/problems/minimum-adjacent-swaps-to-partition-array/description/

"""

3994. Minimum Adjacent Swaps to Partition Array
Solved
Medium
premium lock icon
Companies
Hint
You are given an integer array nums and two integers a and b such that a < b.

An array is called good if it can be split into three contiguous parts, in this order, such that:

Every element in the first part is less than a.
Every element in the second part is in the range [a, b] inclusive.
Every element in the third part is greater than b.
Any of the three parts may be empty.

In one adjacent swap, you may swap two neighboring elements of nums.

Return the minimum number of adjacent swaps required to make nums good. Since the answer may be very large, return it modulo 109 + 7.

 

Example 1:

Input: nums = [1,3,2,4,5,6], a = 3, b = 4

Output: 1

Explanation:

Swap nums[1] and nums[2]. The array becomes [1, 2, 3, 4, 5, 6].
This array is good because it can be split into [1, 2], [3, 4], and [5, 6].
Example 2:

Input: nums = [9,7,5,3], a = 4, b = 8

Output: 5

Explanation:

One sequence of optimal swaps is as follows:

Swap nums[2] and nums[3]. The array becomes [9, 7, 3, 5].
Swap nums[1] and nums[2]. The array becomes [9, 3, 7, 5].
Swap nums[0] and nums[1]. The array becomes [3, 9, 7, 5].
Swap nums[1] and nums[2]. The array becomes [3, 7, 9, 5].
Swap nums[2] and nums[3]. The array becomes [3, 7, 5, 9].
This array is good because it can be split into [3], [7, 5], and [9].
Example 3:

Input: nums = [3,7,5,9], a = 4, b = 8

Output: 0

Explanation:

The array is already good. No swaps are needed.

 

Constraints:

1 <= nums.length <= 105
​​​​​​​1 <= nums[i] <= 109
1 <= a < b <= 109​​​​​​​


"""


# V0
class Solution(object):
    def minAdjacentSwaps(self, nums, a, b):
        """
        :type nums: List[int]
        :type a: int
        :type b: int
        :rtype: int
        """
        pass


# V0-1
# IDEA: GREEDY (gemini)
"""
CORE idea:


### 🛠️ The $O(N)$ Greedy Approach (Inversion Counting)

We don't need to actually sort the array or physically swap any elements!

We can simply classify every number into one of 3 groups:

* **Group 0:** Numbers `< a`
* **Group 1:** Numbers in range `[a, b]`
* **Group 2:** Numbers `> b`

The minimum number of adjacent swaps to group them is identical to counting the number of **inversions** (i.e., how many larger group numbers appear *before* smaller group numbers in the array). We can do this in a single pass:

* If we see a **Group 0**, it needs to jump over all the Group 1s and Group 2s we've seen so far.
* If we see a **Group 1**, it only needs to jump over all the Group 2s we've seen so far.
* If we see a **Group 2**, it is already in the right-most group, so it requires 0 swaps.

### 🧠 Step-by-Step Trace

Let's trace your `Example 1: nums = [1,3,2,4,5,6], a = 3, b = 4`:

1. **num = 1 (Group 0):**
No 1s or 2s seen yet. `swaps += 0`.


2. **num = 3 (Group 1):**
No 2s seen yet. `swaps += 0`. `count_1` becomes `1`.


3. **num = 2 (Group 0):**
It must jump over the `3` we just saw. `swaps += count_1 (1) + count_2 (0)`. `swaps` is now `1`.


4. **num = 4 (Group 1):**
No 2s seen yet. `swaps += 0`. `count_1` becomes `2`.


5. **num = 5 and 6 (Group 2):**
They just increment `count_2`. `swaps` stays `1`.


The final answer is correctly `1` without ever mutating the array!

To better visualize how the groups cross over each other without simulation, [this video tutorial on LC 3994's Greedy Inversion technique](https://www.youtube.com/watch?v=6oc3wkb7vEU) breaks down the intuition visually.

"""
class Solution(object):
    def minAdjacentSwaps(self, nums, a, b):
        """
        :type nums: List[int]
        :type a: int
        :type b: int
        :rtype: int
        """
        MOD = 10**9 + 7
        
        count_1 = 0
        count_2 = 0
        swaps = 0
        
        for num in nums:
            if num < a:
                # Belongs to Group 0
                # Must jump over all Group 1s and Group 2s seen so far
                swaps = (swaps + count_1 + count_2) % MOD
            elif a <= num <= b:
                # Belongs to Group 1
                # Must jump over all Group 2s seen so far
                swaps = (swaps + count_2) % MOD
                count_1 += 1
            else:
                # Belongs to Group 2
                count_2 += 1
                
        return swaps

# V1

# V2

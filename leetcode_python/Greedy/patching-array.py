"""

330. Patching Array
Hard

Given a sorted integer array nums and an integer n, add/patch elements to the array such that any number in the range [1, n] inclusive can be formed by the sum of some elements in the array.

Return the minimum number of patches required.

 

Example 1:

Input: nums = [1,3], n = 6
Output: 1
Explanation:
Combinations of nums are [1], [3], [1,3], which form possible sums of: 1, 3, 4.
Now if we add/patch 2 to nums, the combinations are: [1], [2], [3], [1,3], [2,3], [1,2,3].
Possible sums are 1, 2, 3, 4, 5, 6, which now covers the range [1, 6].
So we only need 1 patch.
Example 2:

Input: nums = [1,5,10], n = 20
Output: 2
Explanation: The two patches can be [2, 4].
Example 3:

Input: nums = [1,2,2], n = 5
Output: 0
 

Constraints:

1 <= nums.length <= 1000
1 <= nums[i] <= 104
nums is sorted in ascending order.
1 <= n <= 231 - 1

"""

# V0

# V1
# https://blog.csdn.net/zml66666/article/details/113185521

# V1'
# IDEA : GREEDY
# https://leetcode.com/problems/patching-array/discuss/1432712/Python-greedy
class Solution:
    def minPatches(self, nums: List[int], n: int) -> int:
        # assume that we have an array covers from 0 to n
        # if new value x is less than or equel to n, then we can create combinations from 0 to n+x
        # as we can add x to the previous formed combination
        # otherwise, we add n+1 to the num so that we can go further        
        curr_end = 0
        res = 0
        curr = 0
        while (curr_end < n):
            if curr >= len(nums) or nums[curr] > curr_end + 1: # cant add anything from the original list
                curr_end += curr_end + 1
                res += 1
            else:
                curr_end += nums[curr]
                curr += 1
        
        return res

# V1'
# IDEA : GREEDY WINDOW
# https://leetcode.com/problems/patching-array/discuss/266608/Python-greedy-window
class Solution:
    def minPatches(self, nums: List[int], n: int) -> int:
        reach = 0
        index = 0
        res = 0
        
        while reach < n:
            if index < len(nums) and reach >= nums[index] - 1:
                reach += nums[index]
                index += 1
            else:
                reach = reach + reach + 1
                res += 1
            
        return res

# V1''
# https://leetcode.com/problems/patching-array/discuss/78547/Greedy-solution-in-Python
# IDEA :
# I used a greedy algorithm. When traversing through the given number list, consider each number as a goal and resource. When in the for loop for the ith number, try to add some numbers so that you can represent every number in the range [ 1, nums[i] ). Then, add the ith number to your source for further loops.
# To reach the goal, suppose all the resource (the numbers smaller than the goal) sums to a number sum, then, sum+1 is what we don't have. So we need to add a sum+1 to our resource. And now you can represent all the numbers not bigger than sum+sum+1
class Solution(object):

    def minPatches(self, nums, n):
        """
        :type nums: List[int]
        :type n: int
        :rtype: int
        """
        count = 0
        sum = 0
        for x in nums:
            if sum >= n:
                return count
            while sum < x-1:  # x-1 is the goal; when reaches the goal, we can represent [1, x)
                count += 1
                sum += sum + 1  # add a resource number
                if sum >= n:
                    return count
            sum += x
        while sum + 1 <= n:
            count += 1
            sum += sum + 1
        return count

# V1''''
# https://leetcode.com/problems/patching-array/discuss/78514/Python-solution
class Solution(object):
    def minPatches(self, nums, n):
        i=0
        count=0
        s=1
        while s<=n:
            if i<len(nums) and s>=nums[i]:
                s+=nums[i]
                i+=1
            else:
                s<<=1
                count+=1
        return count

# V1''''
# https://leetcode.com/problems/patching-array/solution/
# JAVA
# public class Solution {
#     public int minPatches(int[] nums, int n) {
#         int patches = 0, i = 0;
#         long miss = 1; // use long to avoid integer overflow error
#         while (miss <= n) {
#             if (i < nums.length && nums[i] <= miss) // miss is covered
#                 miss += nums[i++];
#             else { // patch miss to the array
#                 miss += miss;
#                 patches++; // increase the answer
#             }
#         }
#         return patches;
#     }
# }

# V2
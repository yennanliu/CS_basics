"""

503. Next Greater Element II
Medium

Given a circular integer array nums (i.e., the next element of nums[nums.length - 1] is nums[0]), return the next greater number for every element in nums.

The next greater number of a number x is the first greater number to its traversing-order next in the array, which means you could search circularly to find its next greater number. If it doesn't exist, return -1 for this number.

 

Example 1:

Input: nums = [1,2,1]
Output: [2,-1,2]
Explanation: The first 1's next greater number is 2; 
The number 2 can't find next greater number. 
The second 1's next greater number needs to search circularly, which is also 2.
Example 2:

Input: nums = [1,2,3,4,3]
Output: [2,3,4,-1,4]
 

Constraints:

1 <= nums.length <= 104
-109 <= nums[i] <= 109

"""

# V0
# IDEA : STACK + circular loop handling
class Solution:
    def nextGreaterElements(self, nums):
        stack = []
        res = [-1] * len(nums)
        for i in range(2*len(nums)-1, -1, -1):
            index = i % len(nums)
            while stack and stack[-1] <= nums[index]:
                stack.pop()
            if stack:
                res[index] = stack[-1]
            stack.append(nums[index])
        return res

# V0'
# IDEA : STACK + circular loop handling
class Solution(object):
    def nextGreaterElements(self, nums):
        # edge case
        if not nums:
            return
        _len = len(nums)
        res = [-1] * _len
        nums2 = nums * 2
        stack  = []
        # # NOTE this !!! we loop from "nums2" with inverse ordering
        for i in range(len(nums2)-1, -1, -1):
            while stack and stack[-1] <= nums2[i]:
                stack.pop(-1)
            if stack:
                # NOTE this !!! (handling cyclic array)
                res[ i % _len ] = stack[-1]

            stack.append(nums2[i])
        return res

# V0'
# IDEA : STACK + circular loop handling
class Solution:
    def nextGreaterElements(self, nums):
        ### NOTE : since we can search nums circurly, 
        #  -> so here we make a new array (augLst = nums + nums) for that     
        augLst = nums + nums
        stack = []
        # init ans
        res = [-1] * len(nums)
        ### NOTE : we looping augLst with inverse order
        for i in range(len(augLst)-1, -1, -1):
            ### NOTE : if stack and last value in stack smaller than augLst[i], we pop last value from stack
            while stack and stack[-1] <= augLst[i]:
                stack.pop()
            ### NOTE : the remaining element in stack must fit the condition, so we append it to res
            #   -> note : append to `i % len(nums)` idx in res
            if stack:
                res[i % len(nums)] = stack[-1]
            ### NOTE : we also need to append augLst[i] to stack
            stack.append(augLst[i])
        return res

# V0''
# IDER : brute force (for + while loop) : TLE
class Solution(object):
    def nextGreaterElements(self, nums):
        # edge case
        if not nums:
            return
        res = []
        nums2 = nums * 2
        for i in range(len(nums)):
            found = False
            tmp_nums = nums2[i+1:]
            while tmp_nums:
                cur = tmp_nums.pop(0)
                if cur > nums[i]:
                    res.append(cur)
                    found = True
                    break
            if not found:
                res.append(-1)
        #print ("")
        return res

# V1
# https://leetcode.com/problems/next-greater-element-ii/discuss/184046/Python-solution
class Solution:
    def nextGreaterElements(self, nums):
        augLst = nums + nums
        stack = []
        res = [-1] * len(nums)
        for i in range(len(augLst)-1, -1, -1):
            while stack and stack[-1] <= augLst[i]:
                stack.pop()
            if stack:
                res[i % len(nums)] = stack[-1]
            stack.append(augLst[i])
        return res

# V1'
# https://leetcode.com/problems/next-greater-element-ii/discuss/184046/Python-solution
class Solution:
    def nextGreaterElements(self, nums):
        stack = []
        res = [-1] * len(nums)
        for i in range(2*len(nums)-1, -1, -1):
            index = i % len(nums)
            while stack and stack[-1] <= nums[index]:
                stack.pop()
            if stack:
                res[index] = stack[-1]
            stack.append(nums[index])
        return res

# V1''
# https://leetcode.com/problems/next-greater-element-ii/discuss/295380/Python-Solution
# IDEA
# I performed Next Greater Element I from right to left twice
# The double pass allows you to simulate circulation
# I came up with this during an onsite and amazed myself that this actually worked when I came here to test it
class Solution:
    def nextGreaterElements(self, nums):
        st = []
        nlen = len(nums)
        res = [-1] * nlen
        
        for times in range(2):
            for idx in range(nlen - 1, -1, -1):
                while st and st[-1] <= nums[idx]:
                    st.pop()
                res[idx] = st[-1] if st else res[idx]
                st.append(nums[idx])
        
        return res

# v1''''
# https://leetcode.com/problems/next-greater-element-ii/discuss/743506/Python
class Solution:
    def nextGreaterElements(self, nums):
        st = []
        n = len(nums)
        res = [-1]*n
        for i in range(2*n-1,-1,-1):
            num = nums[i%n]
            if not st:
                st.append(num)
            else:
                while st and st[-1]<=num:
                    st.pop()
                if not st:
                    res[i%n] = -1
                else:
                    res[i%n] = st[-1]
                st.append(num)
        return res

# V1'''''
# IDEA : Brute Force (using Double Length Array) [Time Limit Exceeded]
# https://leetcode.com/problems/next-greater-element-ii/solution/
# JAVA
#  public class Solution {
#
#     public int[] nextGreaterElements(int[] nums) {
#         int[] res = new int[nums.length];
#         int[] doublenums = new int[nums.length * 2];
#         System.arraycopy(nums, 0, doublenums, 0, nums.length);
#         System.arraycopy(nums, 0, doublenums, nums.length, nums.length);
#         for (int i = 0; i < nums.length; i++) {
#             res[i]=-1;
#             for (int j = i + 1; j < doublenums.length; j++) {
#                 if (doublenums[j] > doublenums[i]) {
#                     res[i] = doublenums[j];
#                     break;
#                 }
#             }
#         }
#         return res;
#     }
# }


# V1''''''
# IDEA : Brute Force (using Double Length Array) (ACCEPTED)
# https://leetcode.com/problems/next-greater-element-ii/solution/
# JAVA
#  public class Solution {
#     public int[] nextGreaterElements(int[] nums) {
#         int[] res = new int[nums.length];
#         for (int i = 0; i < nums.length; i++) {
#             res[i] = -1;
#             for (int j = 1; j < nums.length; j++) {
#                 if (nums[(i + j) % nums.length] > nums[i]) {
#                     res[i] = nums[(i + j) % nums.length];
#                     break;
#                 }
#             }
#         }
#         return res;
#     }
# }

# V1''''''
# IDEA : Stack (ACCEPTED)
# https://leetcode.com/problems/next-greater-element-ii/solution/
# JAVA
# public class Solution {
#
#     public int[] nextGreaterElements(int[] nums) {
#         int[] res = new int[nums.length];
#         Stack<Integer> stack = new Stack<>();
#         for (int i = 2 * nums.length - 1; i >= 0; --i) {
#             while (!stack.empty() && nums[stack.peek()] <= nums[i % nums.length]) {
#                 stack.pop();
#             }
#             res[i % nums.length] = stack.empty() ? -1 : nums[stack.peek()];
#             stack.push(i % nums.length);
#         }
#         return res;
#     }
# }

# V1'''''''
# https://blog.techbridge.cc/2019/10/26/leetcode-pattern-next-greater-element/
# C++
# class Solution {
# public:
#     vector<int> nextGreaterElements(vector<int>& nums) {
#         // Use a stack to get next greater element efficiently
#         vector<int> ans(nums.size(), -1);
#         stack< pair<int, int> > st; // store <value, index> to deal with duplicate values
#
#         // Go through array twice to handle circular property
#         for(int i = 0; i < 2 * nums.size(); ++i) {
#             int idx = i % nums.size();
#             while( !st.empty() and st.top().first < nums[idx] ) {
#                 pair<int, int> cur = st.top();
#                 st.pop();
#
#                 // Because we go through nums twice
#                 // we might update some ans twice (which we do not desire)
#                 // so we only update if ans[cur.second] == -1
#                 if(ans[cur.second] == -1) {
#                     ans[cur.second] = nums[idx];
#                 }
#             }
#
#             if(ans[idx] == -1) {
#                 st.push( make_pair(nums[idx], idx) );
#             }
#         }
#
#         return ans;
#     }
# };

# V2
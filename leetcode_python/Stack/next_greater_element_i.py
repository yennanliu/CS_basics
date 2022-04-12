"""

496. Next Greater Element I
Easy

The next greater element of some element x in an array is the first greater element that is to the right of x in the same array.

You are given two distinct 0-indexed integer arrays nums1 and nums2, where nums1 is a subset of nums2.

For each 0 <= i < nums1.length, find the index j such that nums1[i] == nums2[j] and determine the next greater element of nums2[j] in nums2. If there is no next greater element, then the answer for this query is -1.

Return an array ans of length nums1.length such that ans[i] is the next greater element as described above.

 

Example 1:

Input: nums1 = [4,1,2], nums2 = [1,3,4,2]
Output: [-1,3,-1]
Explanation: The next greater element for each value of nums1 is as follows:
- 4 is underlined in nums2 = [1,3,4,2]. There is no next greater element, so the answer is -1.
- 1 is underlined in nums2 = [1,3,4,2]. The next greater element is 3.
- 2 is underlined in nums2 = [1,3,4,2]. There is no next greater element, so the answer is -1.
Example 2:

Input: nums1 = [2,4], nums2 = [1,2,3,4]
Output: [3,-1]
Explanation: The next greater element for each value of nums1 is as follows:
- 2 is underlined in nums2 = [1,2,3,4]. The next greater element is 3.
- 4 is underlined in nums2 = [1,2,3,4]. There is no next greater element, so the answer is -1.
 

Constraints:

1 <= nums1.length <= nums2.length <= 1000
0 <= nums1[i], nums2[i] <= 104
All integers in nums1 and nums2 are unique.
All the integers of nums1 also appear in nums2.
 

Follow up: Could you find an O(nums1.length + nums2.length) solution?

"""

# V0
# IDEA : STACK (for + while loop)
class Solution(object):
    def nextGreaterElement(self, nums1, nums2):
        # edge case
        if not nums2 or (not nums1 and not nums2):
            return nums1
        res = []
        # NOTE : the trick here (found as a flag)
        found = False
        for i in nums1:
            #print ("i = " + str(i) + " res = " + str(res))
            idx = nums2.index(i)
            # start from "next" element in nums2
            # here we init tmp _nums2
            _nums2 = nums2[idx+1:]
            # while loop keep pop _nums2 for finding the next bigger element
            while _nums2:
                tmp = _nums2.pop(0)
                # if found, then append to res, and break the while loop directly
                if tmp > i:
                    found = True
                    res.append(tmp)
                    break
            # if not found, we need to append -1 to res
            if not found:
                res.append(-1)
            found = False
        return res

# V0'
# IDEA : double for loop (one of loops is INVERSE ORDERING) + case conditions op
class Solution(object):
    def nextGreaterElement(self, nums1, nums2):
        res = [None for _ in range(len(nums1))]
        tmp = []
        for i in range(len(nums1)):
            ### NOTE : from last idx to 0 idx. (Note the start and end idx)
            for j in range(len(nums2)-1, -1, -1):
                #print ("i = " + str(i) + " j = " + str(j) + " tmp = " + str(tmp))

                # case 1) No "next greater element" found in nums2
                if not tmp and nums2[j] == nums1[i]:
                    res[i] = -1
                    break
                # case 2) found "next greater element" in nums2, keep inverse looping
                elif nums2[j] > nums1[i]:
                    tmp.append(nums2[j])
                # case 3) already reach same element in nums2 (as nums1), pop "last" "next greater element", paste to res, break the loop
                elif tmp and nums2[j] == nums1[i]:
                    _tmp = tmp.pop(-1)
                    res[i] = _tmp
                    tmp = []
                    break
        return res

# V1
# IDEA : same as LC 739 Daily Temperatures.
# https://leetcode.com/problems/next-greater-element-i/discuss/183740/Python-solution
class Solution:
    def nextGreaterElement(self, nums1, nums2):
        if not nums1:
            return []
        res = [-1] * len(nums1)
        stack = []
        dic = {}
        for i in range(len(nums2)-1, -1, -1):
            while stack and stack[-1] < nums2[i]:
                stack.pop()
            if not stack:
                dic[nums2[i]] = -1
            else:
                dic[nums2[i]] = stack[-1]
            stack.append(nums2[i])
        for i in range(len(nums1)):
            res[i] = dic[nums1[i]]
        return res

# V1'
# https://leetcode.com/problems/next-greater-element-i/discuss/97691/straightforward-python-solution
class Solution:    
    def nextGreaterElement(self, findNums, nums):
        def helper(num):
            for tmp in nums[nums.index(num):]:
                if tmp > num:
                    return tmp
            return -1

        return map(helper, findNums)

# V1'
# https://leetcode.com/problems/next-greater-element-i/discuss/143065/Python-solution
class Solution(object):
    def nextGreaterElement(self, findNums, nums):
        stack, res, nn_dic = [], [], {} # nn is next number
        for n in reversed(nums):
            # print stack
            while stack and stack[-1] < n:
                stack.pop()
            if stack:
                nn_dic[n] = stack[-1]
            else:
                nn_dic[n] = -1
            stack.append(n)

        for n in findNums:
            res.append(nn_dic[n])

        return res 

# V1'
# https://blog.techbridge.cc/2019/10/26/leetcode-pattern-next-greater-element/
# C++ stack
# class Solution {
# public:
#     vector<int> nextGreaterElement(vector<int>& nums1, vector<int>& nums2) {
#         // Use a hash table to store the <value, idx>
#         unordered_map<int, int> table;
#         for(int i = 0; i < nums1.size(); ++i) {
#             table[ nums1[i] ] = i;
#         }
#
#         // Use a stack to get next greater element efficiently
#         vector<int> ans(nums1.size(), -1);
#         stack<int> st;
#         for(int i = 0; i < nums2.size(); ++i) {
#             while( !st.empty() and st.top() < nums2[i] ) {
#                 int cur = st.top();
#                 st.pop();
#
#                 // If cur exists in table, it means cur is in nums1
#                 // so we should update nums2[i] as the next greater element of cur
#                 if(table.count(cur)) {
#                     ans[ table[cur] ] = nums2[i];
#                 }
#             }
#
#             st.push(nums2[i]);
#         }
#
#         return ans;
#     }
# };

# V2
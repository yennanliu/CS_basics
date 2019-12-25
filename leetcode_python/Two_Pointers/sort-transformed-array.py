# Given a sorted array of integers nums and integer values a, b and c. Apply a function of the form f(x) = ax2 + bx + c to each element x in the array.
# The returned array must be in sorted order.
# Expected time complexity: O(n)
# Example:
# nums = [-4, -2, 2, 4], a = 1, b = 3, c = 5,
# Result: [3, 9, 15, 33]
# nums = [-4, -2, 2, 4], a = -1, b = 3, c = 5
# Result: [-23, -5, 1, 7]

# V0

# V1
# https://www.jiuzhang.com/solution/sort-transformed-array/#tag-highlight-lang-python
class Solution:
    """
    @param nums: a sorted array
    @param a: 
    @param b: 
    @param c: 
    @return: a sorted array
    """
    def sortTransformedArray(self, nums, a, b, c):
        # Write your code here
        res = [0 for i in range(len(nums))]
        start = 0;
        end = len(nums) - 1
        cnt = 0;
        if a >= 0:
            cnt = end
        while start <= end:
            startNum = a * nums[start] * nums[start] + b * nums[start] + c
            endNum = a * nums[end] * nums[end] + b * nums[end] + c
            if a >= 0:
                if startNum >= endNum:
                    res[cnt] = startNum
                    cnt -= 1
                    start += 1
                else:
                    res[cnt] = endNum
                    cnt -= 1
                    end -= 1
            else: # a < 0 
                if startNum <= endNum:
                    res[cnt] = startNum
                    cnt += 1
                    start += 1
                else:
                    res[cnt] = endNum
                    cnt += 1
                    end -= 1
        return res

# V1'
# https://blog.csdn.net/qq508618087/article/details/51700774
# JAVA
# class Solution {
# public:
#     vector<int> sortTransformedArray(vector<int>& nums, int a, int b, int c) {
#         if(nums.size() ==0) return {};
#         vector<int> result;
#         int left = 0, right = nums.size()-1;
#         auto func = [=](int x) { return a*x*x + b*x + c; };
#         while(left <= right)
#         {
#             int val1 = func(nums[left]), val2 = func(nums[right]);
#             if(a > 0) result.push_back(val1>=val2?val1:val2);
#             if(a > 0) val1>val2?left++:right--;
#             if(a <= 0) result.push_back(val1>=val2?val2:val1);
#             if(a <= 0) val1>val2?right--:left++;
#         }
#         if(a > 0) reverse(result.begin(), result.end());
#         return result;
#     }
# };

# V2 
# Time:  O(n)
# Space: O(1)
class Solution(object):
    def sortTransformedArray(self, nums, a, b, c):
        """
        :type nums: List[int]
        :type a: int
        :type b: int
        :type c: int
        :rtype: List[int]
        """
        f = lambda x, a, b, c : a * x * x + b * x + c

        result = []
        if not nums:
            return result

        left, right = 0, len(nums) - 1
        d = -1 if a > 0 else 1
        while left <= right:
            if d * f(nums[left], a, b, c) < d * f(nums[right], a, b, c):
                result.append(f(nums[left], a, b, c))
                left += 1
            else:
                result.append(f(nums[right], a, b, c))
                right -= 1
        return result[::d]

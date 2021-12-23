"""

18. 4Sum
Medium

Given an array nums of n integers, return an array of all the unique quadruplets [nums[a], nums[b], nums[c], nums[d]] such that:

0 <= a, b, c, d < n
a, b, c, and d are distinct.
nums[a] + nums[b] + nums[c] + nums[d] == target
You may return the answer in any order.

 

Example 1:

Input: nums = [1,0,-1,0,-2,2], target = 0
Output: [[-2,-1,1,2],[-2,0,0,2],[-1,0,0,1]]
Example 2:

Input: nums = [2,2,2,2,2], target = 8
Output: [[2,2,2,2]]
 

Constraints:

1 <= nums.length <= 200
-109 <= nums[i] <= 109
-109 <= target <= 109

"""

# V0 
class Solution(object):
    def fourSum(self, nums, target):
        resultList = []
        nums.sort()
        for num1 in range(0, len(nums)-3):
            for num2 in range(num1 + 1, len(nums)-2):
                num3 = num2 + 1
                num4 = len(nums) -1
                while num3 != num4:
                    summer = nums[num1] + nums[num2] + nums[num3] + nums[num4]
                    if summer == target:
                        list_temp = [nums[num1],nums[num2],nums[num3],nums[num4]]
                        if list_temp not in resultList:
                            resultList.append(list_temp)
                        num3 += 1
                    elif summer > target:
                        num4 -= 1
                    else:
                        num3 += 1
        return resultList

# V1
# https://leetcode.com/problems/4sum/discuss/164105/Python-solution
# IDEA : BREAK DOWN + 2 SUM
# First sort the array, loop over the first two indices i & j, and the problem reduces to the 2Sum problem, i.e., finding two indices k & l such that nums[k]+nums[l] == target - nums[i] - nums[j], which takes O(N) time. The total time complexity is then O(N^3).
class Solution(object):
    def fourSum(self, nums, target):
        N = len(nums)
        if N < 4:
            return []
        res = []
        nums = sorted(nums)
        i = 0
        for i in range(N-3):
            if 0 < i < N-3 and nums[i] == nums[i-1]:
                continue        
            for j in range(i+1,N-2):
                if i+1 < j < N-2 and nums[j] == nums[j-1]:
                    continue
                k = j+1
                l = N-1
                while k < l:
                    summ = nums[i]+nums[j]+nums[k]+nums[l]
                    if summ == target:
                        res.append([nums[i],nums[j],nums[k],nums[l]])
                        while k < N-1 and nums[k] == nums[k+1]:
                            k += 1
                        while l > 0 and nums[l] == nums[l-1]:
                            l -= 1
                        k += 1
                        l -= 1
                    elif summ < target:
                        k += 1
                    else:
                        l -= 1
        return res

# V1'
# https://leetcode.com/problems/4sum/solution/
# IDEA : HASH SET
class Solution:
    def fourSum(self, nums, target):
    
        def kSum(nums, target, k):
            res = []
            
            # If we have run out of numbers to add, return res.
            if not nums:
                return res
            
            # There are k remaining values to add to the sum. The 
            # average of these values is at least target // k.
            average_value = target // k
            
            # We cannot obtain a sum of target if the smallest value
            # in nums is greater than target // k or if the largest 
            # value in nums is smaller than target // k.
            if average_value < nums[0] or nums[-1] < average_value:
                return res
            
            if k == 2:
                return twoSum(nums, target)
    
            for i in range(len(nums)):
                if i == 0 or nums[i - 1] != nums[i]:
                    for subset in kSum(nums[i + 1:], target - nums[i], k - 1):
                        res.append([nums[i]] + subset)
    
            return res

        def twoSum(nums, target):
            res = []
            s = set()
    
            for i in range(len(nums)):
                if len(res) == 0 or res[-1][1] != nums[i]:
                    if target - nums[i] in s:
                        res.append([target - nums[i], nums[i]])
                s.add(nums[i])
    
            return res

        nums.sort()
        return kSum(nums, target, 4)

# V1''
# https://leetcode.com/problems/4sum/solution/
# IDEA : Two Pointers
class Solution:
    def fourSum(self, nums, target):
    
        def kSum(nums, target, k):
            res = []
            
            # If we have run out of numbers to add, return res.
            if not nums:
                return res
            
            # There are k remaining values to add to the sum. The 
            # average of these values is at least target // k.
            average_value = target // k
            
            # We cannot obtain a sum of target if the smallest value
            # in nums is greater than target // k or if the largest 
            # value in nums is smaller than target // k.
            if average_value < nums[0] or nums[-1] < average_value:
                return res
            
            if k == 2:
                return twoSum(nums, target)
    
            for i in range(len(nums)):
                if i == 0 or nums[i - 1] != nums[i]:
                    for subset in kSum(nums[i + 1:], target - nums[i], k - 1):
                        res.append([nums[i]] + subset)
    
            return res

        def twoSum(nums, target):
            res = []
            lo, hi = 0, len(nums) - 1
    
            while (lo < hi):
                curr_sum = nums[lo] + nums[hi]
                if curr_sum < target or (lo > 0 and nums[lo] == nums[lo - 1]):
                    lo += 1
                elif curr_sum > target or (hi < len(nums) - 1 and nums[hi] == nums[hi + 1]):
                    hi -= 1
                else:
                    res.append([nums[lo], nums[hi]])
                    lo += 1
                    hi -= 1
                                                         
            return res

        nums.sort()
        return kSum(nums, target, 4)

# V1'''
# https://leetcode.com/problems/4sum/discuss/8604/Python-solution-with-detailed-explanation
# IDEA : BREAK DOWN + 3 SUM
class Solution(object):
    def fourSum(self, nums, target):
        """
        :type nums: List[int]
        :type target: int
        :rtype: List[List[int]]
        """
        nums.sort()
        N, result = len(nums), []
        for i in range(N):
            if i > 0 and nums[i] == nums[i-1]:
                continue
            for j in range(i+1, N):
                if j > i+1 and nums[j] == nums[j-1]:
                    continue
                x = target - nums[i] - nums[j]
                s,e = j+1, N-1
                while s < e:
                    if nums[s]+nums[e] == x:
                        result.append([nums[i], nums[j], nums[s], nums[e]])
                        s = s+1
                        while s < e and nums[s] == nums[s-1]:
                            s = s+1
                    elif nums[s]+nums[e] < x:
                        s = s+1
                    else:
                        e = e-1
        return result
       
# V1''''' 
# https://blog.csdn.net/qqxx6661/article/details/77104868
# IDEA : DOUBLE POINTER 
class Solution(object):
    def fourSum(self, nums, target):
        """
        :type nums: List[int]
        :type target: int
        :rtype: List[List[int]]
        """
        resultList = []
        nums.sort()
        for num1 in range(0, len(nums)-3):
            for num2 in range(num1 + 1, len(nums)-2):
                num3 = num2 + 1
                num4 = len(nums) -1
                while num3 != num4:
                    summer = nums[num1] + nums[num2] + nums[num3] + nums[num4]
                    if summer == target:
                        list_temp = [nums[num1],nums[num2],nums[num3],nums[num4]]
                        if list_temp not in resultList:
                            resultList.append(list_temp)
                        num3 += 1
                    elif summer > target:
                        num4 -= 1
                    else:
                        num3 += 1
        return resultList

# V1''''' 
# https://blog.csdn.net/qqxx6661/article/details/77104868
# IDEA : DOUBLE POINTER  + HASH TABLE 
class Solution(object):
    def fourSum(self, nums, target):
        """
        :type nums: List[int]
        :type target: int
        :rtype: List[List[int]]
        """
        numLen, res, num_dict = len(nums), set(), {}
        if numLen < 4: 
            return []
        nums.sort()
        for p in range(numLen):  # save to hash table 
            for q in range(p+1, numLen): 
                if nums[p]+nums[q] not in num_dict:
                    num_dict[nums[p]+nums[q]] = [(p,q)]
                else:
                    num_dict[nums[p]+nums[q]].append((p,q))
        for i in range(numLen):
            for j in range(i+1, numLen-2):
                T = target-nums[i]-nums[j]
                if T in num_dict:
                    for k in num_dict[T]:
                        if k[0] > j: res.add((nums[i],nums[j],nums[k[0]],nums[k[1]]))
        return [list(i) for i in res]

# V1'''''' 
# https://blog.csdn.net/fuxuemingzhu/article/details/83543296
# IDEA : K SUM
class Solution(object):
    def fourSum(self, nums, target):
        """
        :type nums: List[int]
        :type target: int
        :rtype: List[List[int]]
        """
        N = len(nums)
        nums.sort()
        res = []
        i = 0
        while i < N - 3:
            j = i + 1
            while j < N - 2:
                k = j + 1
                l = N - 1
                remain = target - nums[i] - nums[j]
                while k < l:
                    if nums[k] + nums[l] > remain:
                        l -= 1
                    elif nums[k] + nums[l] < remain:
                        k += 1
                    else:
                        res.append([nums[i], nums[j], nums[k], nums[l]])
                        while k < l and nums[k] == nums[k + 1]:
                            k += 1
                        while k < l and nums[l] == nums[l - 1]:
                            l -= 1
                        k += 1
                        l -= 1
                while j < N - 2 and nums[j] == nums[j + 1]:
                    j += 1
                j += 1 # check this
            while i < N - 3 and nums[i] == nums[i + 1]:
                i += 1
            i += 1 # check this 
        return res

# V1'''''''' 
# https://www.jiuzhang.com/solution/4sum/#tag-highlight-lang-python
class Solution(object):
    def fourSum(self, nums, target):
        nums.sort()
        res = []
        length = len(nums)
        for i in range(0, length - 3):
            if i and nums[i] == nums[i - 1]:
                continue
            for j in range(i + 1, length - 2):
                if j != i + 1 and nums[j] == nums[j - 1]:
                    continue
                sum = target - nums[i] - nums[j]
                left, right = j + 1, length - 1
                while left < right:
                    if nums[left] + nums[right] == sum:
                        res.append([nums[i], nums[j], nums[left], nums[right]])
                        right -= 1
                        left += 1
                        while left < right and nums[left] == nums[left - 1]:
                            left += 1
                        while left < right and nums[right] == nums[right + 1]:
                            right -= 1
                    elif nums[left] + nums[right] > sum:
                        right -= 1
                    else:
                        left += 1
        return res

# V2 
# Time:  O(n^3)
# Space: O(1)
import collections
# Two pointer solution. (1356ms)
class Solution(object):
    def fourSum(self, nums, target):
        """
        :type nums: List[int]
        :type target: int
        :rtype: List[List[int]]
        """
        nums.sort()
        res = []
        for i in range(len(nums) - 3):
            if i and nums[i] == nums[i - 1]:
                continue
            for j in range(i + 1, len(nums) - 2):
                if j != i + 1 and nums[j] == nums[j - 1]:
                    continue
                sum = target - nums[i] - nums[j]
                left, right = j + 1, len(nums) - 1
                while left < right:
                    if nums[left] + nums[right] == sum:
                        res.append([nums[i], nums[j], nums[left], nums[right]])
                        right -= 1
                        left += 1
                        while left < right and nums[left] == nums[left - 1]:
                            left += 1
                        while left < right and nums[right] == nums[right + 1]:
                            right -= 1
                    elif nums[left] + nums[right] > sum:
                        right -= 1
                    else:
                        left += 1
        return res

# Time:  O(n^2 * p)
# Space: O(n^2 * p)
# Hash solution. (224ms)
import collections
class Solution2(object):
    def fourSum(self, nums, target):
        """
        :type nums: List[int]
        :type target: int
        :rtype: List[List[int]]
        """
        nums, result, lookup = sorted(nums), [], collections.defaultdict(list)
        for i in range(0, len(nums) - 1):
            for j in range(i + 1, len(nums)):
                is_duplicated = False
                for [x, y] in lookup[nums[i] + nums[j]]:
                    if nums[x] == nums[i]:
                        is_duplicated = True
                        break
                if not is_duplicated:
                    lookup[nums[i] + nums[j]].append([i, j])
        ans = {}
        for c in range(2, len(nums)):
            for d in range(c+1, len(nums)):
                if target - nums[c] - nums[d] in lookup:
                    for [a, b] in lookup[target - nums[c] - nums[d]]:
                        if b < c:
                            quad = [nums[a], nums[b], nums[c], nums[d]]
                            quad_hash = " ".join(str(quad))
                            if quad_hash not in ans:
                                ans[quad_hash] = True
                                result.append(quad)
        return result

# Time:  O(n^2 * p) ~ O(n^4)
# Space: O(n^2)
import collections
class Solution3(object):
    def fourSum(self, nums, target):
        """
        :type nums: List[int]
        :type target: int
        :rtype: List[List[int]]
        """
        nums, result, lookup = sorted(nums), [], collections.defaultdict(list)
        for i in range(0, len(nums) - 1):
            for j in range(i + 1, len(nums)):
                lookup[nums[i] + nums[j]].append([i, j])

        for i in lookup.keys():
            if target - i in lookup:
                for x in lookup[i]:
                    for y in lookup[target - i]:
                        [a, b], [c, d] = x, y
                        if a is not c and a is not d and \
                           b is not c and b is not d:
                            quad = sorted([nums[a], nums[b], nums[c], nums[d]])
                            if quad not in result:
                                result.append(quad)
        return sorted(result)      
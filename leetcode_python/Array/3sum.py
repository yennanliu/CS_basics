"""

15. 3Sum
Medium

Given an integer array nums, return all the triplets [nums[i], nums[j], nums[k]] such that i != j, i != k, and j != k, and nums[i] + nums[j] + nums[k] == 0.

Notice that the solution set must not contain duplicate triplets.



Example 1:

Input: nums = [-1,0,1,2,-1,-4]
Output: [[-1,-1,2],[-1,0,1]]
Example 2:

Input: nums = []
Output: []
Example 3:

Input: nums = [0]
Output: []
 

Constraints:

0 <= nums.length <= 3000
-105 <= nums[i] <= 105

"""

# V0
# IDEA : for loop + 2 sum
# time = O(n^2)
# space = O(n)
class Solution(object):
    def threeSum(self, nums):
        if nums is None or len(nums) < 3:
            return []

        res = set()

        for i in range(len(nums)):
            target = -nums[i]

            my_map = {}  # reset for each i

            """
            NOTE !!!
             -> we don't need below implementation (will cause TLE)

                #_nums = nums[:i] + nums[i+1:]
                #for j, x in enumerate(_nums):

             -> this one is enough : for j, x in enumerate(nums[i+1:])
            """
            for j in range(i + 1, len(nums)):
                complement = target - nums[j]

                if complement in my_map:
                    # V1
                    # tmp = [cur, x, -x-cur]
                    # tmp.sort()

                    # V2
                    triplet = tuple(sorted([nums[i], nums[j], complement]))
                    res.add(triplet)

                my_map[nums[j]] = j

        return [list(t) for t in res]


# V0-1
# IDEA: SORT + 2 POINTERS
# time = O(n^2)
# space = O(1)
class Solution(object):
    def threeSum(self, nums):
        nums.sort()
        res = []

        n = len(nums)

        for i in range(n - 2):
            if i > 0 and nums[i] == nums[i - 1]:
                continue

            left = i + 1
            right = n - 1

            while left < right:
                s = nums[i] + nums[left] + nums[right]

                if s == 0:
                    res.append([nums[i], nums[left], nums[right]])

                    left += 1
                    right -= 1

                    while left < right and nums[left] == nums[left - 1]:
                        left += 1

                    while left < right and nums[right] == nums[right + 1]:
                        right -= 1

                elif s < 0:
                    left += 1
                else:
                    right -= 1

        return res


# V0-2
# IDEA: SORT + 2 POINTERS
# time = O(n^2)
# space = O(1)
class Solution(object):
    def threeSum(self, nums):
        if nums is None or len(nums) < 3:
            return []
            
        res = []
        # Step 1: Sort the array to make duplicate skipping and pointer movement easy
        nums.sort()
        
        for i in range(len(nums) - 2):
            # Step 2: Skip duplicate values for 'a' to prevent duplicate triplets
            if i > 0 and nums[i] == nums[i - 1]:
                continue
                
            # Optimization: If the smallest possible number is greater than 0, 
            # three positive numbers can never add up to 0!
            if nums[i] > 0:
                break
                
            # Step 3: Initialize Two-Pointers for the remaining Two-Sum target (-nums[i])
            left = i + 1
            right = len(nums) - 1
            
            while left < right:
                current_sum = nums[i] + nums[left] + nums[right]
                
                if current_sum == 0:
                    res.append([nums[i], nums[left], nums[right]])
                    
                    # Step 4: Skip duplicate values 
                    # for 'left' and 'right' pointers
                    while left < right and nums[left] == nums[left + 1]:
                        left += 1
                    while left < right and nums[right] == nums[right - 1]:
                        right -= 1
                        
                    # Move both pointers inward after finding a match
                    left += 1
                    right -= 1
                elif current_sum < 0:
                    left += 1  # Sum is too small, make it bigger by moving left rightward
                else:
                    right -= 1 # Sum is too big, make it smaller by moving right leftward
                    
        return res

# V0
# IDEA : for loop + 2 sum
# time = O(n^2)
# space = O(n)
class Solution(object):
    def threeSum(self, nums):
        # edge case
        if not nums or len(nums) < 3:
            return []
        if len(nums) == 3:
            return [nums] if sum(nums) == 0 else []
        if nums.count(0) == len(nums):
            return [[0,0,0]]
        res = []
        nums.sort()
        # for loop
        for i in range(len(nums)):
            cur = nums[i]
            # 2 sum
            d = {}
            """
            NOTE !!!
             -> we don't need below implementation (will cause TLE)

                #_nums = nums[:i] + nums[i+1:]
                #for j, x in enumerate(_nums):

             -> this one is enough : for j, x in enumerate(nums[i+1:])
            """
            for j, x in enumerate(nums[i+1:]):
                # cur + x + y = 0
                # -> y = -x - cur
                if -x-cur in d:
                    tmp = [cur, x, -x-cur]
                    tmp.sort()
                    if tmp not in res:
                        res.append(tmp)
                    #res.append([cur, x, -x-cur])
                else:
                    d[x] = j
        return res

# V0'
# IDEA : 2 SUM -> 3 SUM
# time = O(n^2)
# space = O(1)
# Given an array S of n integers,
# are there elements a, b, c in S such that a + b + c = 0?
# Find all unique triplets in the array which gives the sum of zero.
#
# Note:
# Elements in a triplet (a,b,c) must be in non-descending order. (ie, a <= b <= c)
# The solution set must not contain duplicate triplets.
#    For example, given array S = {-1 0 1 2 -1 -4},
#
#    A solution set is:
#    (-1, 0, 1)
#    (-1, -1, 2)
class Solution(object):
    def threeSum(self, nums):
        if not nums or len(nums) <= 2:
            return []
        res = []
        # optimize, not necessary
        nums.sort()
        # loop over i
        for i in range(len(nums)):
            """
            2 sum
            """
            d = {}
            """
            NOTE !!! here we loop over range(i+1, len(nums))
            #   -> since we need non duplicated results
            """
            for j in range(i+1, len(nums)):
                """
                NOTE : nums[i] + nums[j] + nums[k] = 0
                #    -> so - (nums[i] + nums[j]) = nums[k]
                #    -> and we are trying to find if such k already in the dict
                """
                if -(nums[i] + nums[j]) in d:
                    tmp = [nums[i]] + [nums[j], -(nums[i]+nums[j])]
                    tmp.sort()
                    if tmp not in res:
                        res.append(tmp)
                else:
                    d[nums[j]] = j
        return res

# V0''
# IDEA : 2 SUM -> 3 SUM
# time = O(n^2)
# space = O(n)
class Solution(object):
    def threeSum(self, nums):
        res = []
        if not nums or len(nums) <= 2:
            return res
        # this sort may not be necessary
        nums.sort()
        for i in range(len(nums)):
            # NOTE : set target = -nums[i]
            t = -nums[i]
            d = {}
            ### NOTE : we NEED tp loop from idx = i+1 to len(nums)
            for j in range(i+1, len(nums)):
                if  (-nums[j] + t) in d:
                        tmp = [ nums[i], nums[j], -nums[j] + t ]
                        tmp.sort()
                        # note :  this trick to not append duplicated ans
                        if tmp not in res:
                            res.append(tmp)
                d[nums[j]] = j
        return res

# V0''''
# BELOW WILL CAUSE "TIME OUT ERROR"
# due to 
#  -> _nums = nums[:i] + nums[i+1:]
#  -> for j in range(len(_nums))
# class Solution(object):
#     def threeSum(self, nums):
#         if not nums or len(nums) <= 2:
#             return []
#         res = []
#         nums.sort()
#         # loop over i
#         for i in range(len(nums)):
#             #print("i = " + str(i))
#             # updated nums
#             _nums = nums[:i] + nums[i+1:]
#             # 2 sum
#             d = {}
#             for j in range(len(_nums)):
#                 #print("j = " + str(j))
#                 if -(nums[i]+_nums[j]) in d:
#                     #return [i, d[target-_nums[i]]]
#                     #tmp = [i] + [j, d[-nums[i]]]
#                     tmp = [nums[i]] + [_nums[j], -(nums[i]+_nums[j])]
#                     tmp.sort()
#                     if tmp not in res:
#                         res.append(tmp)
#
#                 else:
#                     d[_nums[j]] = j
#         return res

# V0''''''
# time = O(n^2)
# space = O(n)
class Solution:
    """
    @param numbersbers : Give an array numbersbers of n integer
    @return : Find all unique triplets in the array which gives the sum of zero.
    """
    def threeSum(self, numbers):
        triplets = []
        length = len(numbers)
        if length < 3:
            return triplets
        numbers.sort()
        for i in range(length):
            target = 0 - numbers[i]
            # 2 Sum
            hashmap = {}
            for j in range(i + 1, length):
                item_j = numbers[j]
                if (target - item_j) in hashmap:
                    triplet = [numbers[i], target - item_j, item_j]
                    if triplet not in triplets:
                        triplets.append(triplet)
                else:
                    hashmap[item_j] = j
        return triplets
        
# V1 
# https://algorithm.yuanbin.me/zh-tw/integer_array/3_sum.html?q=
# IDEA :  SORT + HASH TABLE + 2 SUM
# IDEA : 2 SUM = 1 SUM + 1 SUM   -> 3 SUM = 2 SUM + 1 SUM 
# time = O(n^2)
# space = O(n)
class Solution:
    """
    @param numbersbers : Give an array numbersbers of n integer
    @return : Find all unique triplets in the array which gives the sum of zero.
    """
    def threeSum(self, numbers):
        triplets = []
        length = len(numbers)
        if length < 3:
            return triplets
        numbers.sort()
        for i in range(length):
            target = 0 - numbers[i]
            # 2 Sum
            hashmap = {}
            for j in range(i + 1, length):
                item_j = numbers[j]
                if (target - item_j) in hashmap:
                    triplet = [numbers[i], target - item_j, item_j]
                    if triplet not in triplets:
                        triplets.append(triplet)
                else:
                    hashmap[item_j] = j
        return triplets

# V1' 
# https://blog.csdn.net/fuxuemingzhu/article/details/83115850
# IDEA : SORT + DOUBLE POINTER 
# time = O(n^2)
# space = O(1)
class Solution(object):
    def threeSum(self, nums):
        """
        :type nums: List[int]
        :rtype: List[List[int]]
        """
        N = len(nums)
        nums.sort()
        res = []
        for t in range(N - 2):
            if t > 0 and nums[t] == nums[t - 1]:
                    continue
            i, j = t + 1, N - 1
            while i < j:
                _sum = nums[t] + nums[i] + nums[j]
                if _sum == 0:
                    res.append([nums[t], nums[i], nums[j]])
                    i += 1
                    j -= 1
                    while i < j and nums[i] == nums[i - 1]:
                        i += 1
                    while i < j and nums[j] == nums[j + 1]:
                        j -= 1
                elif _sum < 0:
                    i += 1
                else:
                    j -= 1
        return res

# V1'' 
# https://blog.csdn.net/fuxuemingzhu/article/details/83115850
# IDEA : collections.Counter + DOUBLE POINTER 
# time = O(n^2)
# space = O(n)
import collections
class Solution(object):
    def threeSum(self, nums):
        """
        :type nums: List[int]
        :rtype: List[List[int]]
        """
        count = collections.Counter(nums)
        values = count.keys()
        values.sort()
        print(values)
        N = len(values)
        l, r = 0, N - 1
        res = list()
        visited = set()
        for l in range(N):
            for r in range(l, N):
                t = 0 - values[l] - values[r]
                if t in count:
                    if (t == 0 and count[t] >= 3) \
                    or (((t == values[l] and t != values[r]) or (t == values[r] and t != values[l])) and count[t] >= 2) \
                    or (l == r and values[l] != t and count[values[l]] >= 2) \
                    or (t != values[l] and t != values[r] and l != r):
                        curlist = sorted([values[l], t, values[r]])
                        finger = "#".join(map(str, curlist))
                        if finger not in visited:
                            res.append(curlist)
                            visited.add(finger)
        return res

# V1'''
# https://www.jiuzhang.com/solution/3sum/#tag-highlight-lang-python
# time = O(n^2)
# space = O(1)
class Solution:
    """
    @param numbers: Give an array numbers of n integer
    @return: Find all unique triplets in the array which gives the sum of zero.
    """
    def threeSum(self, nums):
        nums.sort()
        results = []
        length = len(nums)
        for i in range(0, length - 2):
            if i and nums[i] == nums[i - 1]:
                continue
            self.find_two_sum(nums, i + 1, length - 1, -nums[i], results)
        return results

    def find_two_sum(self, nums, left, right, target, results):
        while left < right:
            if nums[left] + nums[right] == target:
                results.append([-target, nums[left], nums[right]])
                right -= 1
                left += 1
                while left < right and nums[left] == nums[left - 1]:
                    left += 1
                while left < right and nums[right] == nums[right + 1]:
                    right -= 1
            elif nums[left] + nums[right] > target:
                right -= 1
            else:
                left += 1

# V2
# time = O(n^2)
# space = O(1)
import collections
class Solution(object):
    def threeSum(self, nums):
        """
        :type nums: List[int]
        :rtype: List[List[int]]
        """
        nums, result, i = sorted(nums), [], 0
        while i < len(nums) - 2:
            if i == 0 or nums[i] != nums[i - 1]:
                j, k = i + 1, len(nums) - 1
                while j < k:
                    if nums[i] + nums[j] + nums[k] < 0:
                        j += 1
                    elif nums[i] + nums[j] + nums[k] > 0:
                        k -= 1
                    else:
                        result.append([nums[i], nums[j], nums[k]])
                        j, k = j + 1, k - 1
                        while j < k and nums[j] == nums[j - 1]:
                            j += 1
                        while j < k and nums[k] == nums[k + 1]:
                            k -= 1
            i += 1
        return result

    # time = O(n^2)
    # space = O(n)
    def threeSum2(self, nums):
        """
        :type nums: List[int]
        :rtype: List[List[int]]
        """
        d = collections.Counter(nums)
        nums_2 = [x[0] for x in d.items() if x[1] > 1]
        nums_new = sorted([x[0] for x in d.items()])
        rtn = [[0, 0, 0]] if d[0] >= 3 else []
        for i, j in enumerate(nums_new):
            if j <= 0:
                numss2 = nums_new[i + 1:]
                for x, y in enumerate(numss2):
                    if 0 - j - y in [j, y] and 0 - j - y in nums_2:
                        if sorted([j, y, 0 - j - y]) not in rtn:
                            rtn.append(sorted([j, y, 0 - j - y]))
                    if 0 - j - y not in [j, y] and 0 - j - y in nums_new:
                        if sorted([j, y, 0 - j - y]) not in rtn:
                            rtn.append(sorted([j, y, 0 - j - y]))
        return rtn
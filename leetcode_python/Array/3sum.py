# Time:  O(n^2)
# Space: O(1)
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

# V0 
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
# Time:  O(n^2)
# Space: O(1)
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

# V0 

# V1
# https://blog.csdn.net/fuxuemingzhu/article/details/83501323
# IDEA : collections.Counter
import collections
class Solution(object):
    def majorityElement(self, nums):
        """
        :type nums: List[int]
        :rtype: List[int]
        """
        N = len(nums)
        count = collections.Counter(nums)
        res = []
        for n, t in count.items():
            if t > N / 3:
                res.append(n)
        return res

# V1'
# http://bookshadow.com/weblog/2015/06/29/leetcode-majority-element-ii/
# https://blog.csdn.net/fuxuemingzhu/article/details/83501323
# IDEA : MOORE VOTING 
# THERE ARE ONLY 2 DIGITS MAY EXIST >= 3/n TIMES 
# SO SET n1, n2 as 2 DIGITS
class Solution:
    # @param {integer[]} nums
    # @return {integer[]}
    def majorityElement(self, nums):
        n1 = n2 = None
        c1 = c2 = 0
        for num in nums:
            if n1 == num:
                c1 += 1
            elif n2 == num:
                c2 += 1
            elif c1 == 0:
                n1, c1 = num, 1
            elif c2 == 0:
                n2, c2 = num, 1
            else:
                c1, c2 = c1 - 1, c2 - 1
        size = len(nums)
        return [n for n in (n1, n2) 
                   if n is not None and nums.count(n) > size / 3]

# V2 
# Time:  O(n)
# Space: O(1)
import collections
class Solution(object):
    def majorityElement(self, nums):
        """
        :type nums: List[int]
        :rtype: List[int]
        """
        k, n, cnts = 3, len(nums), collections.defaultdict(int)

        for i in nums:
            cnts[i] += 1
            # Detecting k items in cnts, at least one of them must have exactly
            # one in it. We will discard those k items by one for each.
            # This action keeps the same mojority numbers in the remaining numbers.
            # Because if x / n  > 1 / k is true, then (x - 1) / (n - k) > 1 / k is also true.
            if len(cnts) == k:
                for j in cnts.keys():
                    cnts[j] -= 1
                    if cnts[j] == 0:
                        del cnts[j]

        # Resets cnts for the following counting.
        for i in cnts.keys():
            cnts[i] = 0

        # Counts the occurrence of each candidate integer.
        for i in nums:
            if i in cnts:
                cnts[i] += 1

        # Selects the integer which occurs > [n / k] times.
        result = []
        for i in cnts.keys():
            if cnts[i] > n / k:
                result.append(i)

        return result

    def majorityElement2(self, nums):
        """
        :type nums: List[int]
        :rtype: List[int]
        """
        return [i[0] for i in collections.Counter(nums).items() if i[1] > len(nums) / 3]
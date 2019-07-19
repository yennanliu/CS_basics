# V0 

# V1
# https://blog.csdn.net/pointbreak1/article/details/48779125
class Solution(object):
    def __init__(self):
        self.results = []
    
    def generatePalindromes(self, s):
        """
        :type s: str
        :rtype: List[str]
        """
        d = {}
        for i in s:
            if i not in d:
                d[i] = 1
            else:
                d[i] += 1
                
        already, candidate, single = False, "", ""
        
        for i in d:
            num = d[i] / 2
            for j in range(num):
                candidate += i
            if d[i] % 2 != 0:
                if already:
                    return []
                else:
                    already = True
                    single += i
                    
        if len(candidate) == 0 and len(single) != 0:
            self.results.append(single)
            return self.results
                    
        for i in range(len(candidate)):
            if  i > 0 and candidate[i] == candidate[i - 1]:
                continue
            self.recursion(candidate[i], candidate[:i] + candidate[i+1:], len(candidate), single)
            
        return self.results
        
    def recursion(self, left, candidate, l, single):
        if len(left) == l:
            self.results.append(left + single + left[::-1])
            return
        for i in range(len(candidate)):
            if i > 0 and candidate[i] == candidate[i - 1]:
                continue
            self.recursion(left + candidate[i], candidate[:i] + candidate[i+1:], l, single)

# V2 
# Time:  O(n * n!)
# Space: O(n)
import collections
import itertools
class Solution(object):
    def generatePalindromes(self, s):
        """
        :type s: str
        :rtype: List[str]
        """
        cnt = collections.Counter(s)
        mid = ''.join(k for k, v in cnt.iteritems() if v % 2)
        chars = ''.join(k * (v / 2) for k, v in cnt.iteritems())
        return self.permuteUnique(mid, chars) if len(mid) < 2 else []

    def permuteUnique(self, mid, nums):
        result = []
        used = [False] * len(nums)
        self.permuteUniqueRecu(mid, result, used, [], nums)
        return result

    def permuteUniqueRecu(self, mid, result, used, cur, nums):
        if len(cur) == len(nums):
            half_palindrome = ''.join(cur)
            result.append(half_palindrome + mid + half_palindrome[::-1])
            return
        for i in range(len(nums)):
            if not used[i] and not (i > 0 and nums[i-1] == nums[i] and used[i-1]):
                used[i] = True
                cur.append(nums[i])
                self.permuteUniqueRecu(mid, result, used, cur, nums)
                cur.pop()
                used[i] = False

class Solution2(object):
    def generatePalindromes(self, s):
        """
        :type s: str
        :rtype: List[str]
        """
        cnt = collections.Counter(s)
        mid = tuple(k for k, v in cnt.iteritems() if v % 2)
        chars = ''.join(k * (v / 2) for k, v in cnt.iteritems())
        return [''.join(half_palindrome + mid + half_palindrome[::-1]) \
                for half_palindrome in set(itertools.permutations(chars))] if len(mid) < 2 else []


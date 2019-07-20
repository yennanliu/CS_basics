# V0 

# V1 
# http://bookshadow.com/weblog/2017/12/03/leetcode-monotone-increasing-digits/
class Solution(object):
    def monotoneIncreasingDigits(self, N):
        """
        :type N: int
        :rtype: int
        """
        sn = str(N)
        size = len(sn)
        flag = False
        for x in range(size - 1):
            if sn[x] > sn[x + 1]:
                flag = True
                break
        if not flag: return N
        while x > 0 and sn[x - 1] == sn[x]: x -= 1
        y = len(sn) - x - 1
        return (N / (10 ** y)) * (10 ** y) - 1

# V2 
# https://blog.csdn.net/fuxuemingzhu/article/details/82721627
class Solution:
    def monotoneIncreasingDigits(self, N):
        """
        :type N: int
        :rtype: int
        """
        if N < 10: return N
        num = [int(n) for n in str(N)[::-1]]
        n = len(num)
        ind = -1
        for i in range(1, n):
            if num[i] > num[i - 1] or (ind != -1 and num[i] == num[ind]):
                ind = i
        if ind == -1:
            return N
        res = '9' * ind + str(num[ind] - 1) + "".join(map(str, num[ind + 1:]))
        return int(res[::-1])

# V2'
# https://blog.csdn.net/fuxuemingzhu/article/details/82721627
class Solution:
    def monotoneIncreasingDigits(self, N):
        """
        :type N: int
        :rtype: int
        """
        if N < 10: return N
        num = [int(n) for n in str(N)]
        n = len(num)
        ind = n - 1
        for i in range(n - 2, -1, -1):
            if num[i] > num[i + 1] or (ind != n - 1 and num[i] == num[ind]):
                ind = i
        if ind == n - 1:
            return N
        num[ind] -= 1
        for i in range(ind + 1, n):
            num[i] = 9
        return int("".join(map(str, num)))

# V3  
# Time:  O(logn) = O(1)
# Space: O(logn) = O(1)
class Solution(object):
    def monotoneIncreasingDigits(self, N):
        """
        :type N: int
        :rtype: int
        """
        nums = map(int, list(str(N)))
        leftmost_inverted_idx = len(nums)
        for i in reversed(range(1, len(nums))):
            if nums[i-1] > nums[i]:
                leftmost_inverted_idx = i
                nums[i-1] -= 1
        for i in range(leftmost_inverted_idx, len(nums)):
            nums[i] = 9
        return int("".join(map(str, nums)))
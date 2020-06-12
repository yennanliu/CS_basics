# V0 
class Solution:
    def monotoneIncreasingDigits(self, N):
        s = list(str(N));
        ### NOTICE HERE 
        for i in range(len(s) - 2,-1,-1):
            if int(s[i]) > int(s[i+1]):
                ### NOTICE HERE 
                for j in range(i+1,len(s)):
                    s[j] = '9'
                s[i] = str(int(s[i]) - 1)
        s = "".join(s)        
        return int(s) 

# V0'
class Solution:
    def monotoneIncreasingDigits(self, N):
        s = str(N)
        l = len(s)
        res = 0
        for i in range(len(s)):
            if i == 0 or s[i] >= s[i-1]:
                res += int(s[i]) * pow(10, l-1)
            else:
                return self.monotoneIncreasingDigits(res-1)
            l -= 1
        return res
        
# V1
# https://leetcode.com/problems/monotone-increasing-digits/discuss/666468/Python-O(n)-Solution-Easy-to-Understand
class Solution:
    def monotoneIncreasingDigits(self, N: int) -> int:
        s = list(str(N));
        for i in range(len(s) - 2,-1,-1):
            if int(s[i]) > int(s[i+1]):
                for j in range(i+1,len(s)):
                    s[j] = '9'
                s[i] = str(int(s[i]) - 1)
        s = "".join(s)        
        return int(s)  

### Test case : dev 

# V1'
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
        return (N // (10 ** y)) * (10 ** y) - 1

# V1''
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

# V1'''
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

# V1''''
# https://leetcode.com/problems/monotone-increasing-digits/solution/
# IDEA : GREEDY 
# Time Complexity: O(D^2)
# Space Complexity : O(D)
class Solution(object):
    def monotoneIncreasingDigits(self, N):
        digits = []
        A = map(int, str(N))
        for i in range(len(A)):
            for d in range(1, 10):
                if digits + [d] * (len(A)-i) > A:
                    digits.append(d-1)
                    break
            else:
                digits.append(9)

        return int("".join(map(str, digits)))

# V1'''''
# https://leetcode.com/problems/monotone-increasing-digits/solution/
# IDEA : Truncate After Cliff
class Solution(object):
    def monotoneIncreasingDigits(self, N):
        S = list(str(N))
        i = 1
        while i < len(S) and S[i-1] <= S[i]:
            i += 1
        while 0 < i < len(S) and S[i-1] > S[i]:
            S[i-1] = str(int(S[i-1]) - 1)
            i -= 1
        S[i+1:] = '9' * (len(S) - i-1)
        return int("".join(S))

# V1''''''
# https://leetcode.com/problems/monotone-increasing-digits/discuss/181945/Fast-and-simple-40ms-Python-solution-using-recursion
class Solution:
    def monotoneIncreasingDigits(self, N):
        """
        :type N: int
        :rtype: int
        """
        s = str(N)
        l = len(s)
        res = 0
        for i in range(len(s)):
            if i == 0 or s[i] >= s[i-1]:
                res += int(s[i]) * pow(10, l-1)
            else:
                return self.monotoneIncreasingDigits(res-1)
            l -= 1
        return res

# V2
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
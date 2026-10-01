"""

738. Monotone Increasing Digits
Medium

An integer has monotone increasing digits if and only if each pair of adjacent digits x and y satisfy x <= y.

Given an integer n, return the largest number that is less than or equal to n with monotone increasing digits.

 

Example 1:

Input: n = 10
Output: 9
Example 2:

Input: n = 1234
Output: 1234
Example 3:

Input: n = 332
Output: 299
 

Constraints:

0 <= n <= 109

"""

# V0
# time = O(D^2)   # D = number of digits = O(log N)
# space = O(D)
class Solution:
    def monotoneIncreasingDigits(self, N):
        s = list(str(N));
        ### NOTICE HERE 
        for i in range(len(s) - 2,-1,-1):
            # if int(s[i]) > int(s[i+1]) -> the string is not `monotone increase`
            # -> we need to find the next biggest int, 
            # -> so we need to make all right hand side digit as '9'
            # -> and minus current digit with 1  (s[i] = str(int(s[i]) - 1))
            if int(s[i]) > int(s[i+1]):
                ### NOTICE HERE 
                for j in range(i+1,len(s)):
                    s[j] = '9'
                s[i] = str(int(s[i]) - 1)
        s = "".join(s)        
        return int(s) 


# V0-1
# IDEA: GREEDY (gemini)
"""
CORE IDEA:

    從右往左找 digits[i-1] > digits[i]，把左邊減 1，然後右邊全部變 9
"""
class Solution(object):

  def monotoneIncreasingDigits(self, n):
    """:type n: int :rtype: int"""
    # 將數字轉為字元陣列，方便進行修改
    s = list(str(n))
    n_len = len(s)

    # 記錄從哪個索引開始，後面的數字全部要變成 '9'
    marker = n_len

    # 從右向左掃描（從倒數第二個字元開始）
    for i in range(n_len - 1, 0, -1):
      # 如果前一個數字大於當前數字，破壞了單調性
      if s[i - 1] > s[i]:
        # 將前一個數字減 1（實現借位）
        s[i - 1] = str(int(s[i - 1]) - 1)
        # 標記從當前位置 i 開始，後續所有字元都要變成 '9'
        marker = i

    # 將 marker 之後的所有字元統一設為 '9'
    for i in range(marker, n_len):
      s[i] = "9"

    # 轉回整數回傳
    return int("".join(s))


# V0-2
# IDEA: GREEDY (GPT)
"""
CORE IDEA:

    從右往左找 digits[i-1] > digits[i]，把左邊減 1，然後右邊全部變 9
"""
class Solution(object):
    def monotoneIncreasingDigits(self, n):
        """
        :type n: int
        :rtype: int
        """
        digits = list(str(n))
        length = len(digits)

        # mark = where we start turning digits into 9
        mark = length

        # Scan from right to left
        for i in range(length - 1, 0, -1):

            # Violation:
            # digits[i - 1] > digits[i]
            if digits[i - 1] > digits[i]:

                # Decrease the left digit by 1
                digits[i - 1] = str(int(digits[i - 1]) - 1)

                # Everything from i onward becomes 9
                mark = i

        # Make suffix all 9
        for i in range(mark, length):
            digits[i] = "9"

        return int("".join(digits))



# V0-1
# brute force -> time out error
# time = O(N * D)   # D = number of digits = O(log N)
# space = O(D)
class Solution(object):
    def monotoneIncreasingDigits(self, n):
        def check(x):
            x = str(x)
            for i in range(len(x)-1,0, -1):
                if int(x[i]) < int(x[i-1]):
                    return False
            return True
        while not check(n):
            print ("n = " + str(n))
            #print ("n = " + str(n) + "" + str(check(n)))
            n = n - 1
            #print ("n = " + str(n) + str(check(n)))
        return n

# V0-2
# time = O(D^2)   # D = number of digits = O(log N); recursion restarts scan
# space = O(D)
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
# time = O(D^2)   # D = number of digits = O(log N)
# space = O(D)
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
# time = O(D)   # D = number of digits = O(log N)
# space = O(D)
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
# time = O(D)   # D = number of digits = O(log N)
# space = O(D)
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
# time = O(D)   # D = number of digits = O(log N)
# space = O(D)
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
# time = O(D^2)   # D = number of digits = O(log N)
# space = O(D)
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
# time = O(D)   # D = number of digits = O(log N)
# space = O(D)
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
# time = O(D^2)   # D = number of digits = O(log N); recursion restarts scan
# space = O(D)
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
# time = O(logn) = O(1)
# space = O(logn) = O(1)
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

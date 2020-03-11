# V0 
# IDEA :
# SET UP 2 INDEX : LEFT, RIGHT. FOR COMPARING WITH CURRENT INDEX AND SAVE MAX INDEX 
# SINCE WE ARE ONLY ALLOWED TO SWAP ONCE TO GET THE MAX VALUE
# SO WE KEEP UPDATING THE LEFT AND RIGHT INDEX AND GO THOROUGH ALL GIVEN NUM  
# DEMO :
# string -> list (python):
# In [34]: x = "1234"
# In [35]: x
# Out[35]: '1234'
# In [36]: list(x)
# Out[36]: ['1', '2', '3', '4']
class Solution(object):
    def maximumSwap(self, num):
        """
        :type num: int
        :rtype: int
        """
        # BE AWARE OF IT 
        digits = list(str(num))
        left, right = 0, 0
        max_idx = len(digits)-1
        for i in reversed(range(len(digits))):
            # BE AWARE OF IT 
            if digits[i] > digits[max_idx]:
                max_idx = i
            # BE AWARE OF IT  
            # if current digit > current max digit -> swap them 
            elif digits[max_idx] > digits[i]:
                left, right = i, max_id        # if current max digit > current digit -> save current max digit to right idnex, and save current index to left
        digits[left], digits[right] = digits[right], digits[left] # swap left and right when loop finished 
        return int("".join(digits))

# V1 
# https://www.cnblogs.com/lightwindy/p/9576624.html
# IDEA :
# SET UP 2 INDEX : LEFT, RIGHT. FOR COMPARING WITH CURRENT INDEX AND SAVE MAX INDEX 
# SINCE WE ARE ONLY ALLOWED TO SWAP ONCE TO GET THE MAX VALUE
# SO WE KEEP UPDATING THE LEFT AND RIGHT INDEX AND GO THOROUGH ALL GIVEN NUM  
# DEMO :
# string -> list (python):
# In [34]: x = "1234"
# In [35]: x
# Out[35]: '1234'
# In [36]: list(x)
# Out[36]: ['1', '2', '3', '4']
class Solution(object):
    def maximumSwap(self, num):
        """
        :type num: int
        :rtype: int
        """
        digits = list(str(num))
        left, right = 0, 0
        max_idx = len(digits)-1
        for i in reversed(range(len(digits))):
            if digits[i] > digits[max_idx]:
                max_idx = i                    # if current digit > current max digit -> swap them 
            elif digits[max_idx] > digits[i]:
                left, right = i, max_id        # if current max digit > current digit -> save current max digit to right idnex, and save current index to left
        digits[left], digits[right] = digits[right], digits[left] # swap left and right when loop finished 
        return int("".join(digits))

# V1'
# http://bookshadow.com/weblog/2017/09/03/leetcode-maximum-swap/
class Solution(object):
    def maximumSwap(self, num):
        """
        :type num: int
        :rtype: int
        """
        nums = map(int, str(num))
        numt = sorted(nums, reverse=True)
        for (i, m), n in zip(enumerate(nums), numt):
            if m == n: continue
            maxv = max(nums[i + 1:])
            j = nums[i + 1:][::-1].index(maxv) + 1
            nums[i], nums[-j] = nums[-j], nums[i]
            break
        return int(''.join(map(str, nums)))
        
# V2 
# Time:  O(logn), logn is the length of the number string
# Space: O(logn)
class Solution(object):
    def maximumSwap(self, num):
        """
        :type num: int
        :rtype: int
        """
        digits = list(str(num))
        left, right = 0, 0
        max_idx = len(digits)-1
        for i in reversed(range(len(digits))):
            if digits[i] > digits[max_idx]:
                max_idx = i
            elif digits[max_idx] > digits[i]:
                left, right = i, max_idx
        digits[left], digits[right] = digits[right], digits[left]
        return int("".join(digits))
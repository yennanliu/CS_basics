"""

670. Maximum Swap
Medium


You are given an integer num. You can swap two digits at most once to get the maximum valued number.

Return the maximum valued number you can get.


Example 1:

Input: num = 2736
Output: 7236
Explanation: Swap the number 2 and the number 7.
Example 2:

Input: num = 9973
Output: 9973
Explanation: No swap.
 

Constraints:

0 <= num <= 108

"""

# V0 
# IDEA : 3 pointers + array op
# -> MAINTAIN 3 pointers : left, right, and max_idx
# -> AND GO THROUGH FROM max idx to 0 idx
# -> DURING GO THROUGH, COMPARE THE digits[max_idx] and digits[i]
#    -> if digits[max_idx] > digits[i] : do nothing
#    -> elif digits[max_idx] < digits[i] : update left, right = i, max_idx
# -> FINALLY, swap digits[left] and digits[right]
#
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
        NOTE !!!
         -> we use 3 index : 
            l, r, max_idx
            and init them as below:

            l = 0
            r = 0
            max_idx = len(num) - 1
        """
        digits = list(str(num))
        left, right = 0, 0
        ### NOTE !!! we set max_idx = len(digits)-1
        max_idx = len(digits)-1
        # NOTE !!! we loop over array in INVERSE ordering
        for i in range(len(digits))[::-1]:
            ### NOTE !!!
            if digits[i] > digits[max_idx]:
                max_idx = i
            ### NOTE !!!
            # if current digit > current max digit -> swap them 
            elif digits[max_idx] > digits[i]:
                left, right = i, max_idx        # if current max digit > current digit -> save current max digit to right idnex, and save current index to left
        digits[left], digits[right] = digits[right], digits[left] # swap left and right when loop finished 
        return int("".join(digits))

# V0'
# IDEA : BRUTE FORCE
# NOTE : ans = A[:]
#        A[:] is a `shallow copy` syntax in python,
#        it will copy "parent obj" (not child obj) to the other instance
#        so the changes ("parent obj" only) in original instance will NOT affect the copied instance
# https://stackoverflow.com/questions/4081561/what-is-the-difference-between-list-and-list-in-python
# https://github.com/yennanliu/til#20210923
class Solution(object):
    def maximumSwap(self, num):
        A = list(str(num))
        ans = A[:]
        for i in range(len(A)):
            for j in range(i+1, len(A)):
                A[i], A[j] = A[j], A[i]
                if A > ans: 
                    ans = A[:]
                A[i], A[j] = A[j], A[i]

        return int("".join(ans))

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

# V1''
# https://leetcode.com/problems/maximum-swap/discuss/107066/Python-Straightforward-with-Explanation
class Solution(object):
    def maximumSwap(self, num):
        A = list(str(num))
        ans = A[:]
        for i in range(len(A)):
            for j in range(i+1, len(A)):
                A[i], A[j] = A[j], A[i]
                if A > ans: ans = A[:]
                A[i], A[j] = A[j], A[i]

        return int("".join(ans))

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
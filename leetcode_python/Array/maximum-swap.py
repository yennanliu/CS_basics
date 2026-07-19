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
# IDEA: LAST IDX + double loop
"""

CORE IDEA:

O(n) idea


1. For every digit, know the last occurrence of every digit 0–9.
2. loop over digit in num
    loop from 9 to 0 (inverse order)
        -> and find if we can `swap`

"""
class Solution(object):
    def maximumSwap(self, num):
        s = list(str(num))


        """

        last_idx: `last idx` of digit in num
        """
        # last occurrence of each digit
        last_idx = [-1] * 10
        for i, ch in enumerate(s):
            last_idx[int(ch)] = i


        """

        NOTE !!!


        we have `double loop` below
        """
        for i, ch in enumerate(s):
            cur = int(ch)

            """

            NOTE !!!


            we try val from `9` to `cur + 1`
                -> try if we can `swap`
            """
            # Try larger digits from 9 down to cur+1
            for d in range(9, cur, -1):
                if last_idx[d] > i:
                    j = last_idx[d]
                    s[i], s[j] = s[j], s[i]

                    # NOTE !!!
                    # if can swap, 
                    # -> return ans directly
                    return int("".join(s))

        return num


# V0-0-1
# IDEA: LAST IDX + HASHMAP (GPT)
"""

CORE IDEA:

O(n) idea

For every digit, know the last occurrence of every digit 0–9.

Example

2736

Last positions:

0 : -
1 : -
2 : 0
3 : 2
4 : -
5 : -
6 : 3
7 : 1
8 : -
9 : -

Now scan from left.

First digit = 2.

Is there a larger digit later?

Check

9
8
7  ← yes

Swap with the last 7.

7236

Done.


"""
class Solution(object):
    def maximumSwap(self, num):
        digits = list(str(num))

        # last occurrence of each digit
        last = {}

        for i, d in enumerate(digits):
            last[int(d)] = i

        # Try to improve each position
        for i in range(len(digits)):
            cur = int(digits[i])

            # Look for a larger digit
            for d in range(9, cur, -1):
                if d in last and last[d] > i:
                    j = last[d]
                    digits[i], digits[j] = digits[j], digits[i]
                    return int("".join(digits))

        return num



# V0-1
# IDEA: LAST IDX + HASHMAP (GEMINI)
class Solution(object):
    def maximumSwap(self, num):
        """
        :type num: int
        :rtype: int
        """
        # 1. Convert integer to a list of characters so we can swap them
        digits = list(str(num))
        
        # 2. Record the last seen index of every digit (0-9)
        # e.g., for 2736, last_seen = {2:0, 7:1, 3:2, 6:3}
        last_seen = {int(val): i for i, val in enumerate(digits)}
        
        # 3. Scan from left to right
        for i, val in enumerate(digits):
            
            # Look for a larger digit (from 9 down to current_digit + 1)
            for d in range(9, int(val), -1):
                
                # If a larger digit exists AND it appears further to the right
                if last_seen.get(d, -1) > i:
                    
                    # Swap the current digit with that larger digit
                    digits[i], digits[last_seen[d]] = digits[last_seen[d]], digits[i]
                    
                    # We only get ONE swap, so the first one we do is guaranteed 
                    # to be the maximum possible result. Return immediately.
                    return int("".join(digits))
                    
        # If no swap could make the number larger, return the original number
        return num


# V0-2
# IDEA: BRUTE FORCE (GPT)
class Solution(object):
    def maximumSwap(self, num):
        digits = list(str(num))
        n = len(digits)

        candidates = [num]

        for l in range(n):
            for r in range(l + 1, n):
                if digits[l] < digits[r]:
                    tmp = digits[:]
                    tmp[l], tmp[r] = tmp[r], tmp[l]
                    candidates.append(int("".join(tmp)))

        return max(candidates)


# V0
# IDEA : 3 pointers + array op
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
# time = O(logn)
# space = O(logn)
class Solution(object):
    def maximumSwap(self, num):
        # edge case
        if not num:
            return num
        # note this
        num = list(str(num))
        # NOTE !!! we init l, r = 0, 0
        #          we init _max_idx = len(num) - 1
        l = 0
        r = 0
        # this one is OK as well
        # l = len(num) - 1
        # r = len(num) - 1
        _max_idx = len(num)-1
        for i in range(len(num))[::-1]:
            """
            NOTE !!! below condition and op
                1) if int(num[i]) > int(num[_max_idx]) or int(num[i]) < int(num[_max_idx])
                2) if int(num[i]) < int(num[_max_idx])
                     -> l = i
                     -> r = _max_idx
            """
            if int(num[i]) > int(num[_max_idx]):
                _max_idx = i
            elif int(num[i]) < int(num[_max_idx]):
                l = i
                r = _max_idx
        #print ("l = " + str(l) + " r = " + str(r))
        num[l], num[r] = num[r], num[l]
        return int("".join(num))

# V0'
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
# time = O(logn)
# space = O(logn)
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

# V0''
# IDEA : BRUTE FORCE
# NOTE : ans = A[:]
#        A[:] is a `shallow copy` syntax in python,
#        it will copy "parent obj" (not child obj) to the other instance
#        so the changes ("parent obj" only) in original instance will NOT affect the copied instance
# https://stackoverflow.com/questions/4081561/what-is-the-difference-between-list-and-list-in-python
# https://github.com/yennanliu/til#20210923
# time = O((logn)^2)
# space = O(logn)
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
# time = O(logn)
# space = O(logn)
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
# time = O((logn)^2)
# space = O(logn)
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
# time = O((logn)^2)
# space = O(logn)
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
# time = O(logn), logn is the length of the number string
# space = O(logn)
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

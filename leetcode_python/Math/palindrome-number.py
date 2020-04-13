# V0
class Solution(object):
    # @return a boolean
    def isPalindrome(self, x):
        length = len(str(x))
        if length==1 or length==0:
            return True
        if x < 0:
            return False
        mid = length//2 
        return str(x)[:mid] == str(x)[-mid:][::-1]

### test case
s = Solution()
assert s.isPalindrome(100) == False
assert s.isPalindrome(3456) == False
assert s.isPalindrome(1001) == True
assert s.isPalindrome(1) == True
#assert s.isPalindrome(None) == True
assert s.isPalindrome(10**32 + 1) == True
assert s.isPalindrome(10**32 + 3) == False

# V1
import math  
class Solution(object):
    # @return a boolean
    def isPalindrome(self, x):
        if x < 0:
            return False
        x = str(x)
        # if x is like 12321  (odd length)
        if str(x)[:math.floor(len(str(x))/2)] == x[math.floor(len(str(x))/2)+1:][::-1]:
            return True
        # if x is like 2222 (even length)
        if str(x)[:math.floor(len(str(x))/2)] == x[math.floor(len(str(x))/2):][::-1]:
            return True
        else:
            return False

# V2 
# Time:  O(1)
# Space: O(1)
class Solution(object):
    # @return a boolean
    def isPalindrome(self, x):
        if x < 0:
            return False
        copy, reverse = x, 0

        while copy:
            reverse *= 10
            reverse += copy % 10
            copy //= 10

        return x == reverse

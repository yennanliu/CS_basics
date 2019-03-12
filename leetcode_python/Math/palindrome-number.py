

# V1 
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

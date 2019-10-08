# Time:  O(k), where k is the steps to be happy number
# Space: O(k)
#
# Write an algorithm to determine if a number is "happy".
#
# A happy number is a number defined by the following process:
# Starting with any positive integer, replace the number by the sum
# of the squares of its digits, and repeat the process until
# the number equals 1 (where it will stay), or it loops endlessly
# in a cycle which does not include 1. Those numbers for which
# this process ends in 1 are happy numbers.
#
# Example: 19 is a happy number
#
# 1^2 + 9^2 = 82
# 8^2 + 2^2 = 68
# 6^2 + 8^2 = 100
# 1^2 + 0^2 + 0^2 = 1
#

# V0
class Solution:
    # @param {integer} n
    # @return {boolean}
    def isHappy(self, n):
        numSet = set()
        while n != 1 and n not in numSet:
            numSet.add(n)
            sum = 0
            while n:
                digit = n % 10
                sum += digit * digit
                n /= 10
            n = sum
        return n == 1
            
# V1 
# http://bookshadow.com/weblog/2015/04/22/leetcode-happy-number/
# IDEA : SET 
class Solution:
    # @param {integer} n
    # @return {boolean}
    def isHappy(self, n):
        numSet = set()
        while n != 1 and n not in numSet:
            numSet.add(n)
            sum = 0
            while n:
                digit = n % 10
                sum += digit * digit
                n /= 10
            n = sum
        return n == 1

# V1'
# https://www.jiuzhang.com/solution/happy-number/#tag-highlight-lang-python
class Solution:
    # @param {int} n an integer
    # @return {boolean} true if this is a happy number or false
    def isHappy(self, n):
        # Write your code here
        d = {}
        while True:
            d[n] = 1
            n = sum([int(x) * int(x) for x in list(str(n))])
            if n == 1 or n in d:
                break
        return n == 1

# V2 
class Solution:
    # @param {integer} n
    # @return {boolean}
    def isHappy(self, n):
        lookup = {}
        while n != 1 and n not in lookup:
            lookup[n] = True
            n = self.nextNumber(n)
        return n == 1

    def nextNumber(self, n):
        new = 0
        for char in str(n):
            new += int(char)**2
        return new

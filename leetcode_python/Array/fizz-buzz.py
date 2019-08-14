"""
Write a program that outputs the string representation of numbers from 1 to n.

But for multiples of three it should output “Fizz” instead of the number and for the multiples of five output “Buzz”. For numbers which are multiples of both three and five output “FizzBuzz”.

e.g. 
x = 3*y, -> x = "Fizz"
x = 5*y, -> x = "Buzz"
x = 15*y,-> x = "FizzBuzz"

Example:

n = 15,

Return:
[
    "1",
    "2",
    "Fizz",
    "4",
    "Buzz",
    "Fizz",
    "7",
    "8",
    "Fizz",
    "Buzz",
    "11",
    "Fizz",
    "13",
    "14",
    "FizzBuzz"
]
"""

# V0

# V1 
# http://bookshadow.com/weblog/2016/10/09/leetcode-fizz-buzz/
class Solution(object):
    def fizzBuzz(self, n):
        """
        :type n: int
        :rtype: List[str]
        """
        return ['Fizz' * (not x % 3) + 'Buzz' * (not x % 5) or str(x) for x in range(1, n + 1)]

# V1' 
# http://bookshadow.com/weblog/2016/10/09/leetcode-fizz-buzz/
class Solution(object):
    def fizzBuzz(self, n):
        """
        :type n: int
        :rtype: List[str]
        """
        ans = []
        for x in range(1, n + 1):
            n = str(x)
            if x % 15 == 0:
                n = "FizzBuzz"
            elif x % 3 == 0:
                n = "Fizz"
            elif x % 5 == 0:
                n = "Buzz"
            ans.append(n)
        return ans
        
# V2 
# Time:  O(n)
# Space: O(1)
class Solution(object):
    def fizzBuzz(self, n):
        """
        :type n: int
        :rtype: List[str]
        """
        result = []

        for i in xrange(1, n+1):
            if i % 15 == 0:
                result.append("FizzBuzz")
            elif i % 5 == 0:
                result.append("Buzz")
            elif i % 3 == 0:
                result.append("Fizz")
            else:
                result.append(str(i))

        return result

    def fizzBuzz2(self, n):
        """
        :type n: int
        :rtype: List[str]
        """
        l = [str(x) for x in range(n + 1)]
        l3 = range(0, n + 1, 3)
        l5 = range(0, n + 1, 5)
        for i in l3:
            l[i] = 'Fizz'
        for i in l5:
            if l[i] == 'Fizz':
                l[i] += 'Buzz'
            else:
                l[i] = 'Buzz'
        return l[1:]

    def fizzBuzz3(self, n):
        return ['Fizz' * (not i % 3) + 'Buzz' * (not i % 5) or str(i) for i in range(1, n + 1)]

    def fizzBuzz4(self, n):
        return ['FizzBuzz'[i % -3 & -4:i % -5 & 8 ^ 12] or repr(i) for i in range(1, n + 1)]
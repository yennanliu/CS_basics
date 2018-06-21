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



# V1 
class Solution(object):
	def fizzBuzz(self, n):
		output = []
		if n==1:
			output.append("1")
			return output
		for k in range(1,n+1):
			if (k%15 == 0):
				output.append("FizzBuzz")
			elif (k%3 == 0):
				output.append("Fizz")
			elif (k%5 == 0):
				output.append("Buzz")
			else:
				output.append(str(k))
		return output




# V2 
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
        return ['FizzBuzz'[i % -3 & -4:i % -5 & 8 ^ 12] or `i` for i in range(1, n + 1)]








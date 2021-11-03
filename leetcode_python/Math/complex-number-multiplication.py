"""

537. Complex Number Multiplication
Medium

A complex number can be represented as a string on the form "real+imaginaryi" where:

real is the real part and is an integer in the range [-100, 100].
imaginary is the imaginary part and is an integer in the range [-100, 100].
i2 == -1.
Given two complex numbers num1 and num2 as strings, return a string of the complex number that represents their multiplications.

 

Example 1:

Input: num1 = "1+1i", num2 = "1+1i"
Output: "0+2i"
Explanation: (1 + i) * (1 + i) = 1 + i2 + 2 * i = 2i, and you need convert it to the form of 0+2i.
Example 2:

Input: num1 = "1+-1i", num2 = "1+-1i"
Output: "0+-2i"
Explanation: (1 - i) * (1 - i) = 1 + i2 - 2 * i = -2i, and you need convert it to the form of 0+-2i.
 

Constraints:

num1 and num2 are valid complex numbers.

"""

# V0
# IDEA : MATH 
class Solution(object):
    def complexNumberMultiply(self, num1, num2):
        r_1 = int(num1.split("+")[0])
        i_1 = int(num1.split("+")[1].replace("i","")) 
        r_2 = int(num2.split("+")[0])
        i_2 = int(num2.split("+")[1].replace("i",""))
        r_res = str((r_1 * r_2) + -1 * (i_1 * i_2))
        i_res = str(r_1 * i_2 + i_1 * r_2) + "i"
        return r_res + "+" +  i_res

# V0'
# IDEA : MATH 
class Solution(object):
    def complexNumberMultiply(self, a, b):
        def split(s):
            tmp = s[:-1].split("+")
            return int(tmp[0]), int(tmp[1])
        m, n = split(a)
        p, q = split(b)
        return '{}+{}i'.format((m*p - n*q),(m*q + n*p))

# V1
# http://bookshadow.com/weblog/2017/03/26/leetcode-complex-number-multiplication/
# IDEA : STRING OP 
class Solution(object):
    def complexNumberMultiply(self, a, b):
        """
        :type a: str
        :type b: str
        :rtype: str
        """
        extract = lambda s : map(int, s[:-1].split('+'))
        m, n = extract(a)
        p, q = extract(b)
        return '%s+%si' % (m * p - n * q, m * q + n * p)

# V1'
class Solution(object):
	# a : a1+a2*i
	# b : b2+b2*i
	def complexNumberMultiply(self, a, b):
		ra, ia = float(a.split('+')[0]), float(a.split('+')[1].split('i')[0])
		rb, ib = float(b.split('+')[0]), float(b.split('+')[1].split('i')[0])
		return '%d+%di' % ((ra*rb - ia*ib), ia*rb + ib*ra)

# V2 
# Time:  O(1)
# Space: O(1)
class Solution(object):
    def complexNumberMultiply(self, a, b):
        ra, ia = list(map(int, a[:-1].split('+')))
        rb, ib = list(map(int, b[:-1].split('+')))
        return '%d+%di' % (ra * rb - ia * ib, ra * ib + ia * rb)
# V0
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
        """
        :type a: str
        :type b: str
        :rtype: str
        """
        ra, ia = list(map(int, a[:-1].split('+')))
        rb, ib = list(map(int, b[:-1].split('+')))
        return '%d+%di' % (ra * rb - ia * ib, ra * ib + ia * rb)

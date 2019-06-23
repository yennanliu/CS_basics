# V1 
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

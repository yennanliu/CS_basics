# V1 
class solution(n):
	def fib(n):
		return fib(n-2) + fib(n-1)
	if n <= 2:
		return 1 
	return fib(n-2) + fib(n-1)

# V1' 
class solution(n):
	def fib(n):
		if n <= 2:
			return 1 
		return fib(n-2) + fib(n-1)
	return fib(n)

# V1''



# V2 


# V3 
# Time:  O(logn)
# Space: O(1)
import itertools
class Solution(object):
    def fib(self, N):
        """
        :type N: int
        :rtype: int
        """
        def matrix_expo(A, K):
            result = [[int(i==j) for j in xrange(len(A))] \
                      for i in xrange(len(A))]
            while K:
                if K % 2:
                    result = matrix_mult(result, A)
                A = matrix_mult(A, A)
                K /= 2
            return result

        def matrix_mult(A, B):
            ZB = zip(*B)
            return [[sum(a*b for a, b in itertools.izip(row, col)) \
                     for col in ZB] for row in A]

        T = [[1, 1],
             [1, 0]]
        return matrix_expo(T, N)[1][0]


# V3' 
# Time:  O(n)
# Space: O(1)
class Solution2(object):
    def fib(self, N):
        """
        :type N: int
        :rtype: int
        """
        prev, current = 0, 1
        for i in xrange(N):
            prev, current = current, prev + current,
        return prev

# V1 : DEV 
# idea: find the pattern of rows 
# row 1: 0
# row 2: 01
# row 3: 0110
# row 4: 01101001
# row 5: 0110100110010110
# so we can find the role : 
# --- > "for row N, first half of the row is as same as the N-1 row, the rest of the row is inverse to the N-1 row"
# i.e. : 
# for row 2 :  01 =    0 + 1 (row 1 : 0, inverse row 1 : 1)
# for row 3 :  0110 =  01 + 10  (row 2 : 01, inverse row 2 : 10)
# for row 4 :  01101001 =  0110 +  1001 (row 3 : 0110, inverse row 3 :  1001)
# ....


# V2 
# https://blog.csdn.net/fuxuemingzhu/article/details/82848294
class Solution(object):
    def kthGrammar(self, N, K):
        """
        :type N: int (row N)
        :type K: int (index K)
        :rtype: int
        """
        if K == 1:
            return 0
        # 2**(N - 2) is the half length of row N
        # we use it to know if index K is on the front half or the second half of row N 
        # if index k is at front half of N 
        if K <= (2**(N - 2)):
            # bitwise operator
            # https://wiki.python.org/moin/BitwiseOperators
            # Returns x with the bits shifted to the left by y places (and new bits on the right-hand-side are zeros). This is the same as multiplying x by 2**y.
            # --> x << y equals x*(2^y)
            return self.kthGrammar(N - 1, K)
        # if index k is at the second half of N 
        else:
            return 1 - self.kthGrammar(N - 1, K - (2**(N - 2)))

# V2'
class Solution(object):
    def kthGrammar(self, N, K):
        """
        :type N: int
        :type K: int
        :rtype: int
        """
        if K == 1:
            return 0
        if K <= (1 << (N - 2)):
        	# bitwise operator
        	# https://wiki.python.org/moin/BitwiseOperators
        	# Returns x with the bits shifted to the left by y places (and new bits on the right-hand-side are zeros). This is the same as multiplying x by 2**y.
            # --> x << y equals x*(2^y)
            return self.kthGrammar(N - 1, K)
        else:
            return 1 - self.kthGrammar(N - 1, K - (1 << (N - 2)))

# V3
# Time:  O(logn) = O(1) because n is 32-bit integer
# Space: O(1)

class Solution(object):
    def kthGrammar(self, N, K):
        """
        :type N: int
        :type K: int
        :rtype: int
        """
        def bitCount(n):
            result = 0
            while n:
                n &= n - 1
                result += 1
            return result

        return bitCount(K-1) % 2

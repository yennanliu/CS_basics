"""

779. K-th Symbol in Grammar
Medium

We build a table of n rows (1-indexed). We start by writing 0 in the 1st row. Now in every subsequent row, we look at the previous row and replace each occurrence of 0 with 01, and each occurrence of 1 with 10.

For example, for n = 3, the 1st row is 0, the 2nd row is 01, and the 3rd row is 0110.
Given two integer n and k, return the kth (1-indexed) symbol in the nth row of a table of n rows.

 

Example 1:

Input: n = 1, k = 1
Output: 0
Explanation: row 1: 0
Example 2:

Input: n = 2, k = 1
Output: 0
Explanation: 
row 1: 0
row 2: 01
Example 3:

Input: n = 2, k = 2
Output: 1
Explanation: 
row 1: 0
row 2: 01
 

Constraints:

1 <= n <= 30
1 <= k <= 2n - 1

"""

# V0
# IDEA : BRUTE FORCE (TLE)
# NOTE :  the string is symmetric
# cur = 01
# cur = 0110
# cur = 01101001
# cur = 0110100110010110
# cur = 01101001100101101001011001101001
# ..
class Solution(object):
    def kthGrammar(self, n, k):
        # edge
        if n == 1:
            return 0
        cur = "0"
        while n:
            tmp = ""
            for x in cur:
                if x == "0":
                    tmp += "01"
                else:
                    tmp += "10"
            n -= 1
            cur = tmp
            print (" cur = " + str(cur))
        return int(cur[k-1])

# V1
# https://leetcode.com/problems/k-th-symbol-in-grammar/solution/
# IDEA : BRUTE FORCE
class Solution(object):
    def kthGrammar(self, N, K):
        if N == 1: return 0
        return (1 - K%2) ^ self.kthGrammar(N-1, (K+1)/2)

# V1'
# https://leetcode.com/problems/k-th-symbol-in-grammar/solution/
# IDEA : Recursion (Parent Variant)
class Solution(object):
    def kthGrammar(self, N, K):
        if N == 1: return 0
        return (1 - K%2) ^ self.kthGrammar(N-1, (K+1)/2)

# V1'
# https://leetcode.com/problems/k-th-symbol-in-grammar/solution/
# IDEA : Recursion (Flip Variant)
class Solution(object):
    def kthGrammar(self, N, K):
        if N == 1: return 0
        if K <= (2**(N-2)):
            return self.kthGrammar(N-1, K)
        return self.kthGrammar(N-1, K - 2**(N-2)) ^ 1


# V1'
# https://leetcode.com/problems/k-th-symbol-in-grammar/solution/
# IDEA : Binary Count
class Solution(object):
    def kthGrammar(self, N, K):
        return bin(K - 1).count('1') % 2

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
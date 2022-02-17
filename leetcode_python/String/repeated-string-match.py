"""

686. Repeated String Match
Medium

Given two strings a and b, return the minimum number of times you should repeat string a so that string b is a substring of it. If it is impossible for b​​​​​​ to be a substring of a after repeating it, return -1.

Notice: string "abc" repeated 0 times is "", repeated 1 time is "abc" and repeated 2 times is "abcabc".

 

Example 1:

Input: a = "abcd", b = "cdabcdab"
Output: 3
Explanation: We return 3 because by repeating a three times "abcdabcdabcd", b is a substring of it.
Example 2:

Input: a = "a", b = "aa"
Output: 2
 

Constraints:

1 <= a.length, b.length <= 104
a and b consist of lowercase English letters.

"""

# V0
# IDEA : BRUTE FORCE
# https://leetcode.com/problems/repeated-string-match/discuss/108090/Intuitive-Python-2-liner
# -> if there is a sufficient solution, B must "inside" A
# -> Let n be the answer, 
# -> Let x be the theoretical lower bound, which is ceil(len(B)/len(A)).
# -> the value of n can br ONLY "x" or "x + 1"
# -> e.g. : in the case where len(B) is a multiple of len(A) like in A = "abcd" and B = "cdabcdab") and not more. Because if B is already in A * n, B is definitely in A * (n + 1).
# --> So all we need to check whether are:
#       -> 1) B in A * x
#         or
#       -> 2) B in A * (x+1)
# -> return -1 if above contitions are not met
class Solution(object):
    def repeatedStringMatch(self, A, B):
        sa, sb = len(A), len(B)
        x = 1
        while (x - 1) * sa <= 2 * max(sa, sb):
            if B in A * x: 
                return x
            x += 1
        return -1

# V0'
class Solution(object):
    def repeatedStringMatch(self, a, b):
        # edge case
        if not a and b:
            return -1
        if (not a and not b) or (a == b) or (b in a):
            return 1
        res = 1
        sa = len(a)
        sb = len(b)
        #while res * sa <= 3 * max(sa, sb):  # this condition is OK as well
        while (res-1) * sa <= 2 * max(sa, sb):
            a_ = res * a
            if b in a_:
                return res
            res += 1
        return -1

# V1
# https://leetcode.com/problems/repeated-string-match/discuss/108090/Intuitive-Python-2-liner
# IDEA : BRUTE FORCE
# Let n be the answer, the minimum number of times A has to be repeated.
# For B to be inside A, A has to be repeated sufficient times such that it is at least as long as B (or one more), hence we can conclude that the theoretical lower bound for the answer would be length of B / length of A.
# Let x be the theoretical lower bound, which is ceil(len(B)/len(A)).
# The answer n can only be x or x + 1 (in the case where len(B) is a multiple of len(A) like in A = "abcd" and B = "cdabcdab") and not more. Because if B is already in A * n, B is definitely in A * (n + 1).
# Hence we only need to check whether B in A * x or B in A * (x + 1), and if both are not possible return -1.
class Solution(object):
    def repeatedStringMatch(self, A, B):
        t = -(-len(B) // len(A)) # Equal to ceil(len(b) / len(a))
        return t * (B in A * t) or (t + 1) * (B in A * (t + 1)) or -1

# V1
# https://leetcode.com/problems/repeated-string-match/discuss/108090/Intuitive-Python-2-liner
# IDEA : BRUTE FORCE
# Let n be the answer, the minimum number of times A has to be repeated.
# For B to be inside A, A has to be repeated sufficient times such that it is at least as long as B (or one more), hence we can conclude that the theoretical lower bound for the answer would be length of B / length of A.
# Let x be the theoretical lower bound, which is ceil(len(B)/len(A)).
# The answer n can only be x or x + 1 (in the case where len(B) is a multiple of len(A) like in A = "abcd" and B = "cdabcdab") and not more. Because if B is already in A * n, B is definitely in A * (n + 1).
# Hence we only need to check whether B in A * x or B in A * (x + 1), and if both are not possible return -1.
class Solution(object):
    def repeatedStringMatch(self, A, B):
        times = -(-len(B) // len(A)) # Equal to ceil(len(b) / len(a))
        for i in range(2):
          if B in (A * (times + i)):
            return times + i
        return -1

# V1 
# http://bookshadow.com/weblog/2017/10/01/leetcode-repeated-string-match/
class Solution(object):
    def repeatedStringMatch(self, A, B):
        sa, sb = len(A), len(B)
        x = 1
        while (x - 1) * sa <= 2 * max(sa, sb):
            if B in A * x: 
                return x
            x += 1
        return -1

### Test case : dev 

# V1'
# https://leetcode.com/problems/repeated-string-match/discuss/224182/Explanation-on-the-Intuitive-Python-2-liner-solution
class Solution:
    def repeatedStringMatch(self, A, B):
        r = math.ceil(len(B) / len(A))
        for a in [r, r + 1]:
            if B in A * a:
                return a
        return -1

# V1'
# https://leetcode.com/problems/repeated-string-match/solution/
# IDEA : BRUTE FORCE
# time complexity : O(N*(M+N))
# space complexity : O(M+N)
class Solution(object):
    def repeatedStringMatch(self, A, B):
        q = (len(B) - 1) // len(A) + 1
        for i in range(2):
            if B in A * (q+i): return q+i
        return -1

# V1''
# https://leetcode.com/problems/repeated-string-match/solution/
# IDEA : Rabin-Karp (Rolling Hash)
class Solution(object):
    def repeatedStringMatch(self, A, B):
        def check(index):
            return all(A[(i + index) % len(A)] == x
                       for i, x in enumerate(B))

        q = (len(B) - 1) // len(A) + 1

        p, MOD = 113, 10**9 + 7
        p_inv = pow(p, MOD-2, MOD)
        power = 1

        b_hash = 0
        for x in map(ord, B):
            b_hash += power * x
            b_hash %= MOD
            power = (power * p) % MOD

        a_hash = 0
        power = 1
        for i in xrange(len(B)):
            a_hash += power * ord(A[i % len(A)])
            a_hash %= MOD
            power = (power * p) % MOD

        if a_hash == b_hash and check(0): return q

        power = (power * p_inv) % MOD
        for i in xrange(len(B), (q+1) * len(A)):
            a_hash = (a_hash - ord(A[(i - len(B)) % len(A)])) * p_inv
            a_hash += power * ord(A[i % len(A)])
            a_hash %= MOD
            if a_hash == b_hash and check(i - len(B) + 1):
                return q if i < q * len(A) else q+1
        return -1

# V2 
# Rabin-Karp Algorithm (rolling hash)
class Solution(object):
    def repeatedStringMatch(self, A, B):
        """
        :type A: str
        :type B: str
        :rtype: int
        """
        def check(index):
            return all(A[(i+index) % len(A)] == c
                       for i, c in enumerate(B))

        M, p = 10**9+7, 113
        p_inv = pow(p, M-2, M)
        q = (len(B)+len(A)-1) // len(A)

        b_hash, power = 0, 1
        for c in B:
            b_hash += power * ord(c)
            b_hash %= M
            power = (power*p) % M

        a_hash, power = 0, 1
        for i in range(len(B)):
            a_hash += power * ord(A[i%len(A)])
            a_hash %= M
            power = (power*p) % M

        if a_hash == b_hash and check(0): return q

        power = (power*p_inv) % M
        for i in range(len(B), (q+1)*len(A)):
            a_hash = (a_hash-ord(A[(i-len(B))%len(A)])) * p_inv
            a_hash += power * ord(A[i%len(A)])
            a_hash %= M
            if a_hash == b_hash and check(i-len(B)+1):
                return q if i < q*len(A) else q+1
        return -1
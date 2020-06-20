# V0 
# IDEA : GO THROUGH THE STRING FROM BACK,
#        -> SO NO NEED TO THINK ABOUT THE "len(S) % K > 1"  cases
class Solution:
    def licenseKeyFormatting(self, S, K):
        S = S.replace("-", "").upper()[::-1]
        return '-'.join(S[i:i+K] for i in range(0, len(S), K))[::-1]

# V0
# DEMO : range with gap
#    ...: for i in range(1,10, 2):
#    ...: ^Iprint (i)
#    ...:  
# 1
# 3
# 5
# 7
# 9
#
# IDEA:  
# -> use '#' to add back the remainder of len(S) % K 
# -> then go throughg S (with gap), and replace '#' with ''
class Solution(object):
    def licenseKeyFormatting(self, S, K):
        S = S.replace('-', '').upper()
        if len(S) % K:
            S = '#' * (K - len(S) % K) + S
        return '-'.join(S[x:x + K] for x in range(0, len(S), K)).replace('#', '')

# V1 
# http://bookshadow.com/weblog/2017/01/08/leetcode-license-key-formatting/
class Solution(object):
    def licenseKeyFormatting(self, S, K):
        """
        :type S: str
        :type K: int
        :rtype: str
        """
        S = S.replace('-', '').upper()
        if len(S) % K:
            S = '#' * (K - len(S) % K) + S
        return '-'.join(S[x:x + K] for x in range(0, len(S), K)).replace('#', '')

### Test case : dev 

# V1'
# https://leetcode.com/problems/license-key-formatting/discuss/131978/beats-100-python3-submission
class Solution:
    def licenseKeyFormatting(self, S, K):
        """
        :type S: str
        :type K: int
        :rtype: str
        """
        S = S.replace("-", "").upper()[::-1]
        return '-'.join(S[i:i+K] for i in range(0, len(S), K))[::-1]

# V1''
# https://leetcode.com/problems/license-key-formatting/discuss/96497/Python-solution
class Solution(object):
    def licenseKeyFormatting(self, S, K):
        """
        :type S: str
        :type K: int
        :rtype: str
        """
        S = S.upper().replace('-','')
        size = len(S)
        s1 = K if size%K==0 else size%K
        res = S[:s1]
        while s1<size:
            res += '-'+S[s1:s1+K]
            s1 += K
        return res

# V1'''
# https://leetcode.com/problems/license-key-formatting/discuss/175523/Concise-Python-Beats-100
class Solution:
    def licenseKeyFormatting(self, S, K):
        """
        :type S: str
        :type K: int
        :rtype: str
        """
        def chunks(l, n):
            for i in range(0, len(l), n):
                yield l[i:i+n]
        s = S[::-1].upper().replace('-', '')   
        return '-'.join(list(chunks(s, K)))[::-1]

# V2 
# Time:  O(n)
# Space: O(1)
class Solution(object):
    def licenseKeyFormatting(self, S, K):
        """
        :type S: str
        :type K: int
        :rtype: str
        """
        result = []
        for i in reversed(range(len(S))):
            if S[i] == '-':
                continue
            if len(result) % (K + 1) == K:
                result += '-'
            result += S[i].upper()
        return "".join(reversed(result))
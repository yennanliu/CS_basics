# V0 

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
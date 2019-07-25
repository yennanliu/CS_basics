# V0 

# V1 
# http://bookshadow.com/weblog/2016/12/11/leetcode-validate-ip-address/
# In [59]: import string
# In [60]: string.hexdigits
# Out[60]: '0123456789abcdefABCDEF'
import string
class Solution(object):
    def validIPAddress(self, IP):
        """
        :type IP: str
        :rtype: str
        """
        if self.validIPV4(IP):
            return 'IPv4'
        if self.validIPV6(IP):
            return 'IPv6'
        return 'Neither'

    def validIPV4(self, IP):
        parts = IP.split('.')
        if len(parts) != 4: return False
        for part in parts:
            if not part: return False
            if not part.isdigit(): return False
            if part[0] == '0' and len(part) > 1: return False # Ipv4 is a 4 digits (0-255) split with ","
            if int(part) > 255: return False
        return True

    def validIPV6(self, IP):
        parts = IP.split(':')
        if len(parts) != 8: return False
        for part in parts:
            if not part: return False
            if len(part) > 4: return False
            if any(c not in string.hexdigits for c in part): return False # Ipv6 is a 8 hex-decimal digits split with ":" 
        return True

# V2 
# Time:  O(1)
# Space: O(1)
import string
class Solution(object):
    def validIPAddress(self, IP):
        """
        :type IP: str
        :rtype: str
        """
        blocks = IP.split('.')
        if len(blocks) == 4:
            for i in range(len(blocks)):
                if not blocks[i].isdigit() or not 0 <= int(blocks[i]) < 256 or \
                   (blocks[i][0] == '0' and len(blocks[i]) > 1):
                    return "Neither"
            return "IPv4"

        blocks = IP.split(':')
        if len(blocks) == 8:
            for i in range(len(blocks)):
                if not (1 <= len(blocks[i]) <= 4) or \
                   not all(c in string.hexdigits for c in blocks[i]):
                    return "Neither"
            return "IPv6"
        return "Neither"

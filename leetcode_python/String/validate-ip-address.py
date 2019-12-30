# V0 
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
            if part[0] == '0' and len(part) > 1: return False
            if int(part) > 255: return False
        return True

    def validIPV6(self, IP):
        parts = IP.split(':')
        if len(parts) != 8: return False
        for part in parts:
            if not part: return False
            if len(part) > 4: return False
            if any(c not in string.hexdigits for c in part): return False
        return True
        
# V1 
# http://bookshadow.com/weblog/2016/12/11/leetcode-validate-ip-address/
# IDEA : 
# IPV4 :   -> x.y.z.u, while  0 < x,y,z,u < 255  (Decimal)
#          -> can't  tart with 0 -> i.e. 172.1.2.01 is not a validated IPV4 address
#
# IPV6 :   -> a:b:c:d:e:f:g:h (Hexadecimal)
#          -> it's OK to replace continuous 0 with a single 0  
#                => i.e. 2001:0db8:85a3:0000:0000:8a2e:0370:7334 is OK
#                =>      2001:db8:85a3:0:0:8A2E:0370:7334 is OK as well
#          -> it's illegal to have a contunous ":" -> i.e. 2001:0db8:85a3::8A2E:0370:7334 is NOT LEAGAL
#          -> it's illegal to have an extra 0 -> i.e. 02001:0db8:85a3:0000:0000:8a2e:0370:7334 is NOT LEAGAL
#
#
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

# V1'
# https://www.jiuzhang.com/solution/validate-ip-address/#tag-highlight-lang-python
class Solution(object):
    def validIPAddress(self, IP):
        """
        :type IP: str
        :rtype: str
        """
        ip = IP.split('.')
        if len(ip) == 4:
            # ipv4 candidate, validate it
            for octet_s in ip:
                try:
                    octet = int(octet_s)
                except ValueError:
                    return 'Neither'
                if octet < 0 or octet > 255 or (octet_s != '0' and (octet // 10**(len(octet_s) - 1) == 0)):
                    return 'Neither'
            return 'IPv4'
        else:
            ip = IP.split(':')
            if len(ip) == 8:
                # ipv6 candidate, validate it
                for hexa_s in ip:
                    if not hexa_s or len(hexa_s) > 4 or not hexa_s[0].isalnum():
                        return 'Neither'
                    try:
                        hexa = int(hexa_s, base=16)
                    except ValueError:
                        return 'Neither'
                    hexa_redo = '{:x}'.format(hexa)
                    if hexa < 0 or hexa > 65535:
                        return 'Neither'
                return 'IPv6'
        return 'Neither'

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

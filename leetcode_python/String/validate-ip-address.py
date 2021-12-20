"""

468. Validate IP Address
Medium

Given a string queryIP, return "IPv4" if IP is a valid IPv4 address, "IPv6" if IP is a valid IPv6 address or "Neither" if IP is not a correct IP of any type.

A valid IPv4 address is an IP in the form "x1.x2.x3.x4" where 0 <= xi <= 255 and xi cannot contain leading zeros. For example, "192.168.1.1" and "192.168.1.0" are valid IPv4 addresses but "192.168.01.1", while "192.168.1.00" and "192.168@1.1" are invalid IPv4 addresses.

A valid IPv6 address is an IP in the form "x1:x2:x3:x4:x5:x6:x7:x8" where:

1 <= xi.length <= 4
xi is a hexadecimal string which may contain digits, lower-case English letter ('a' to 'f') and upper-case English letters ('A' to 'F').
Leading zeros are allowed in xi.
For example, "2001:0db8:85a3:0000:0000:8a2e:0370:7334" and "2001:db8:85a3:0:0:8A2E:0370:7334" are valid IPv6 addresses, while "2001:0db8:85a3::8A2E:037j:7334" and "02001:0db8:85a3:0000:0000:8a2e:0370:7334" are invalid IPv6 addresses.

 

Example 1:

Input: queryIP = "172.16.254.1"
Output: "IPv4"
Explanation: This is a valid IPv4 address, return "IPv4".
Example 2:

Input: queryIP = "2001:0db8:85a3:0:0:8A2E:0370:7334"
Output: "IPv6"
Explanation: This is a valid IPv6 address, return "IPv6".
Example 3:

Input: queryIP = "256.256.256.256"
Output: "Neither"
Explanation: This is neither a IPv4 address nor a IPv6 address.
 

Constraints:

queryIP consists only of English letters, digits and the characters '.' and ':'.

"""

# V0
# IDEA : Divide and Conquer
class Solution:
    def validate_IPv4(self, IP):
        nums = IP.split('.')
        for x in nums:
            # Validate integer in range (0, 255):
            # 1. length of chunk is between 1 and 3
            if len(x) == 0 or len(x) > 3:
                return "Neither"
            # 2. no extra leading zeros
            # 3. only digits are allowed
            # 4. less than 255
            if x[0] == '0' and len(x) != 1 or not x.isdigit() or int(x) > 255:
                return "Neither"
        return "IPv4"
    
    def validate_IPv6(self, IP):
        nums = IP.split(':')
        hexdigits = '0123456789abcdefABCDEF'
        for x in nums:
            # Validate hexadecimal in range (0, 2**16):
            # 1. at least one and not more than 4 hexdigits in one chunk
            # 2. only hexdigits are allowed: 0-9, a-f, A-F
            if len(x) == 0 or len(x) > 4 or not all(c in hexdigits for c in x):
                return "Neither"
        return "IPv6"
        
    def validIPAddress(self, IP):
        if IP.count('.') == 3:
            return self.validate_IPv4(IP)
        elif IP.count(':') == 7:
            return self.validate_IPv6(IP)
        else:
            return "Neither"

# V0'
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

# V0''
# TODO : fix below
# class Solution(object):
#     def validIPAddress(self, queryIP):
#         if len(queryIP) == 0:
#             return "Neither"
#
#         ip_type = None
#
#         if "." in queryIP:
#             _queryIP_list = queryIP.split(".")
#             ip_type = "IPv4"
#         else:
#             _queryIP_list = queryIP.split(":")
#             ip_type = "IPv6"
#
#         # check IPV4
#         if ip_type == "IPv4":
#             if len(_queryIP_list) != 4:
#                 return "Neither"
#             for i, ip in enumerate(_queryIP_list):
#                 if not ip.isnumeric():
#                     return "Neither"
#                 if int(ip) > 255 or int(ip) < 0:
#                     return "Neither"
#                 if "00" in ip or (i < len(_queryIP_list) and "0" in ip):
#                     return "Neither"
#             return "IPv4"
#
#         # check IPV6
#         if ip_type == "IPv6":
#             invalid_alpha = "klmnopqrstuvwxyz"
#             if len(_queryIP_list) != 8:
#                 return "Neither"
#             for ip in _queryIP_list:
#                 print ("ip = " + str(ip) )
#                 for invalid in invalid_alpha:
#                     if  invalid.upper() in ip or invalid.lower() in ip:
#                         return "Neither"
#                 if len(ip) > 4 or len(ip) < 1 or ( ip.startswith("00") and ip.count("0") * "0" != ip):
#                     return "Neither"
#             return "IPv6"


# V1
# https://leetcode.com/problems/validate-ip-address/solution/
# IDEA : REGEX
import re
class Solution:
    chunk_IPv4 = r'([0-9]|[1-9][0-9]|1[0-9][0-9]|2[0-4][0-9]|25[0-5])'
    patten_IPv4 = re.compile(r'^(' + chunk_IPv4 + r'\.){3}' + chunk_IPv4 + r'$')
    
    chunk_IPv6 = r'([0-9a-fA-F]{1,4})'
    patten_IPv6 = re.compile(r'^(' + chunk_IPv6 + r'\:){7}' + chunk_IPv6 + r'$')

    def validIPAddress(self, IP: str) -> str:        
        if self.patten_IPv4.match(IP):
            return "IPv4"
        return "IPv6" if self.patten_IPv6.match(IP) else "Neither" 

# V1'
# https://leetcode.com/problems/validate-ip-address/solution/
# IDEA : Divide and Conquer
class Solution:
    def validate_IPv4(self, IP: str) -> str:
        nums = IP.split('.')
        for x in nums:
            # Validate integer in range (0, 255):
            # 1. length of chunk is between 1 and 3
            if len(x) == 0 or len(x) > 3:
                return "Neither"
            # 2. no extra leading zeros
            # 3. only digits are allowed
            # 4. less than 255
            if x[0] == '0' and len(x) != 1 or not x.isdigit() or int(x) > 255:
                return "Neither"
        return "IPv4"
    
    def validate_IPv6(self, IP: str) -> str:
        nums = IP.split(':')
        hexdigits = '0123456789abcdefABCDEF'
        for x in nums:
            # Validate hexadecimal in range (0, 2**16):
            # 1. at least one and not more than 4 hexdigits in one chunk
            # 2. only hexdigits are allowed: 0-9, a-f, A-F
            if len(x) == 0 or len(x) > 4 or not all(c in hexdigits for c in x):
                return "Neither"
        return "IPv6"
        
    def validIPAddress(self, IP: str) -> str:
        if IP.count('.') == 3:
            return self.validate_IPv4(IP)
        elif IP.count(':') == 7:
            return self.validate_IPv6(IP)
        else:
            return "Neither"

# V1'''
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

# V1'''''
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
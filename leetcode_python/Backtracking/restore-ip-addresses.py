# V0 
# IDEA : DFS
class Solution(object):
    def restoreIpAddresses(self, s):
        # if not valid input form (ip address length should < 12)
        if len(s) > 12:
            return []
        res = []
        self.dfs(s, [], res)
        return res
        
    def dfs(self, s, path, res):
        # if not remaining elments (not s) and path is in "xxx.xxx.xxx.xxx" form
        if not s and len(path) == 4:
            res.append('.'.join(path))
            return
        for i in [1,2,3]:
            # avoid "out of index" error
            if i > len(s):
                continue
            number = int(s[:i])
            # str(number) == s[:i] for checking if digit is not starting from "0"
            # e.g. 030 is not accepted form, while 30 is OK
            if str(number) == s[:i] and number <= 255:
                self.dfs(s[i:], path + [s[:i]], res)

# V0'
# IDEA : BFS (AGAIN)
class Solution(object):
    def restoreIpAddresses(self, s):
        if len(s) > 12: return []
        res=[]
        S=[([],s)]
        while S:
            l,s=S.pop()
            if len(l)==4:
                if not s: 
                    res.append('.'.join(l))
            elif len(s)<=(4-len(l))*3:
                for i in range(min(3,len(s)-3+len(l))):
                    if i!=2 or int(s[:3]) <= 255:
                        S.append((l+[s[:i+1]],s[i+1:]))
                    if s[0]=='0': break
        return res
# DEMO
#     ...: x="25525511135"
#     ...: s= Solution()
#     ...: res=s.restoreIpAddresses(x)
#     ...: print (res)
#     ...: 
# [] 25525511135
# ['255'] 25511135
# ['255', '255'] 11135
# ['255', '255', '111'] 35
# ['255', '255', '111', '35'] 
# ['255', '255', '111', '3'] 5
# ['255', '255', '11'] 135
# ['255', '255', '11', '135'] 
# ['255', '255', '11', '13'] 5
# ['255', '255', '11', '1'] 35
# ['255', '255', '1'] 1135
# ['255', '25'] 511135
# ['255', '25', '51'] 1135
# ['255', '25', '5'] 11135
# ['255', '2'] 5511135
# ['25'] 525511135
# ['25', '52'] 5511135
# ['25', '5'] 25511135
# ['2'] 5525511135
# ['255.255.111.35', '255.255.11.135']
# In [90]:

# V1 
# https://blog.csdn.net/fuxuemingzhu/article/details/80657420
# IDEA : DFS
# NOTE :
# Definition of valid IP address:
# https://leetcode.com/problems/restore-ip-addresses/discuss/31165/What-is-the-definition-of-a-valid-IP-address
# 1. The length of the ip without '.' should be equal to the length of s;
# 2. The digit order of ip should be same as the digit order of s;
# 3. Each part separated by the '.' should not start with '0' except only '0';
# 4. Each part separared by the '.' should not larger than 255;
class Solution(object):
    def restoreIpAddresses(self, s):
        """
        :type s: str
        :rtype: List[str]
        """
        # if not valid input form (ip address length should < 12)
        if len(s) > 12:
            return []
        res = []
        self.dfs(s, [], res)
        return res
        
    def dfs(self, s, path, res):
        # if not remaining elments (not s) and path is in "xxx.xxx.xxx.xxx" form
        if not s and len(path) == 4:
            res.append('.'.join(path))
            return
        # i = 1,2,3
        #for i in range(1, 4):
        for i in [1,2,3]:
            # avoid "out of index" error
            if i > len(s):
                continue
            number = int(s[:i])
            # str(number) == s[:i] for checking if digit is not starting from "0"
            # e.g. 030 is not accepted form, while 30 is OK
            if str(number) == s[:i] and number <= 255:
                self.dfs(s[i:], path + [s[:i]], res)

# Test case
s=Solution()
assert s.restoreIpAddresses("25525511135") == ['255.255.11.135', '255.255.111.35']
assert s.restoreIpAddresses("2552551") == ['2.55.25.51','2.55.255.1','25.5.25.51','25.5.255.1','25.52.5.51','25.52.55.1','255.2.5.51','255.2.55.1','255.25.5.1']
assert s.restoreIpAddresses("255") == []
assert s.restoreIpAddresses("2555")  ==  ['2.5.5.5']
assert s.restoreIpAddresses("")  ==  []
assert s.restoreIpAddresses("0")  ==  []
assert s.restoreIpAddresses("001") == []
assert s.restoreIpAddresses("00001") == []
assert s.restoreIpAddresses("012345") == ['0.1.23.45', '0.1.234.5', '0.12.3.45', '0.12.34.5', '0.123.4.5']
assert s.restoreIpAddresses("000345") == []
assert s.restoreIpAddresses("000255") == ['0.0.0.255']
assert s.restoreIpAddresses("000254") == ['0.0.0.254']
assert s.restoreIpAddresses("254000") == ['25.40.0.0', '254.0.0.0']

# V1'
# https://leetcode.com/problems/restore-ip-addresses/discuss/30946/DFS-in-Python
# IDEA : DFS
class Solution(object):
    def restoreIpAddresses(self, s):
        """
        :type s: str
        :rtype: List[str]
        """
        ans = []
        self.helper(ans, s, 4, [])
        return ['.'.join(x) for x in ans]
        
    def helper(self, ans, s, k, temp):
        if len(s) > k*3:
            return
        if k == 0:
            ans.append(temp[:])
        else:
            for i in range(min(3,len(s)-k+1)):
                if i==2 and int(s[:3]) > 255 or i > 0 and s[0] == '0':
                    continue
                self.helper(ans, s[i+1:], k-1, temp+[s[:i+1]])

# V1'
# https://leetcode.com/problems/restore-ip-addresses/discuss/30946/DFS-in-Python
# IDEA : BFS
class Solution(object):
    def restoreIpAddresses(self, s):
        res=[]
        S=[([],s)]
        while S:
            l,s=S.pop()
            if len(l)==4:
                if not s: 
                    res.append('.'.join(l))
            elif len(s)<=(4-len(l))*3:
                for i in range(min(3,len(s)-3+len(l))):
                    if i!=2 or s[:3] <= '255':
                        S.append((l+[s[:i+1]],s[i+1:]))
                    if s[0]=='0': break
        return res

# V1''
# https://leetcode.com/problems/restore-ip-addresses/discuss/31211/Adding-a-python-solution-also-requesting-for-improvement
# IDEA : BRUTE FORCE
class Solution:
    # @param s, a string
    # @return a list of strings
         
     def restoreIpAddresses(self,s):
 
         answer = []
 
         s_len = len(s)
 
         for i in [1,2,3]:
             for j in [i+1,i+2,i+3]:
                 for k in [j+1,j+2,j+3]:
                     if k >= s_len:
                         continue
                     s1 = s[:i]
                     s2 = s[i:j]
                     s3 = s[j:k]
                     s4 = s[k:]
                     if self.check_valid([s1,s2,s3,s4]):
                         new_string = s1 + "." + s2 + "." + s3 + "." + s4
                         answer.append(new_string)
         return answer
 
     def check_valid(self,str_list):
 
         for s in str_list:
             if s[0] == "0" and s != "0":
                 return False
             if int(s) > 255:
                 return False
         return True

# V1' 
# https://blog.csdn.net/fuxuemingzhu/article/details/80657420
class Solution(object):
    def restoreIpAddresses(self, s):
        """
        :type s: str
        :rtype: List[str]
        """
        res = []
        self.dfs(s, [], res)
        return res
        
    def dfs(self, s, path, res):
        if len(s) > (4 - len(path)) * 3:
            return
        if not s and len(path) == 4:
            res.append('.'.join(path))
            return
        for i in range(min(3, len(s))):
            curr = s[:i+1]
            if (curr[0] == '0' and len(curr) >= 2) or int(curr) > 255:
                continue
            self.dfs(s[i+1:], path + [s[:i+1]], res)
            
# V2 
# Time:  O(n^m) = O(3^4)
# Space: O(n * m) = O(3 * 4)
class Solution(object):
    # @param s, a string
    # @return a list of strings
    def restoreIpAddresses(self, s):
        result = []
        self.restoreIpAddressesRecur(result, s, 0, "", 0)
        return result

    def restoreIpAddressesRecur(self, result, s, start, current, dots):
        # pruning to improve performance
        if (4 - dots) * 3 < len(s) - start or (4 - dots) > len(s) - start:
            return

        if start == len(s) and dots == 4:
            result.append(current[:-1])
        else:
            for i in range(start, start + 3):
                if len(s) > i and self.isValid(s[start:i + 1]):
                    current += s[start:i + 1] + '.'
                    self.restoreIpAddressesRecur(result, s, i + 1, current, dots + 1)
                    current = current[:-(i - start + 2)]

    def isValid(self, s):
        if len(s) == 0 or (s[0] == '0' and s != "0"):
            return False
        return int(s) < 256
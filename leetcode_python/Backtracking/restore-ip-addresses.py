"""

93. Restore IP Addresses
Medium

A valid IP address consists of exactly four integers separated by single dots. Each integer is between 0 and 255 (inclusive) and cannot have leading zeros.

For example, "0.1.2.201" and "192.168.1.1" are valid IP addresses, but "0.011.255.245", "192.168.1.312" and "192.168@1.1" are invalid IP addresses.
Given a string s containing only digits, return all possible valid IP addresses that can be formed by inserting dots into s. You are not allowed to reorder or remove any digits in s. You may return the valid IP addresses in any order.

 

Example 1:

Input: s = "25525511135"
Output: ["255.255.11.135","255.255.111.35"]
Example 2:

Input: s = "0000"
Output: ["0.0.0.0"]
Example 3:

Input: s = "101023"
Output: ["1.0.10.23","1.0.102.3","10.1.0.23","10.10.2.3","101.0.2.3"]
 

Constraints:

0 <= s.length <= 20
s consists of digits only.

"""


# V0
class Solution(object):
    def restoreIpAddresses(self, s):
        pass


# V0-1
# IDEA: DFS + backtrack (gpt)
class Solution(object):
    def restoreIpAddresses(self, s):
        """
        :type s: str
        :rtype: List[str]
        """

        if not s:
            return []

        # IP address 必須由 4 個 segments 組成，
        # 每個 segment 長度介於 1 到 3。
        # 因此總長度必須介於 4 到 12。
        if len(s) < 4 or len(s) > 12:
            return []

        self.res = []

        self.helper(s, 0, [])

        return self.res

    # dfs + backtrack
    def helper(self, s, idx, path):
        """
        s: 原始字串
        idx: 目前處理到 s 的哪個 index
        path: 目前已經選取的 IP segments
        """

        # NOTE !!
        # how we decide if `a valid ip address is found`
        # ->
        #   1. size = 4
        #   2. idx = len(s) (use all of the s)
        #
        # Base Case 1:
        # 已經選滿 4 個 segments
        if len(path) == 4:

            # 只有剛好使用完整個字串才是有效答案
            if idx == len(s):
                self.res.append(".".join(path))

            return

        # Base Case 2:
        # 字串已經用完，但還沒選滿 4 個 segments
        if idx >= len(s):
            return

        # 還需要選幾個 segments？
        remaining_segments = 4 - len(path)

        # 還剩多少個字元？
        remaining_chars = len(s) - idx

        # Pruning 1:
        # 剩餘字元太少，無法組成剩餘 segments
        if remaining_chars < remaining_segments:
            return

        # Pruning 2:
        # 剩餘字元太多，每個 segment 最多 3 位數
        if remaining_chars > remaining_segments * 3:
            return

        # NOTE !!!
        # range(1, 4)
        # so can loop from 1 to 3
        #
        # 嘗試取出 1、2、3 位數字
        for length in range(1, 4):

            # 避免超出字串範圍
            if idx + length > len(s):
                break

            # NOTE !!! below
            segment = s[idx:idx + length]

            """
            NOTE !!!


            1. 
            
            `if len(segment) > 1 and segment[0] == "0"`

                -> we ONLY `break`, NOT `return`


            -> because a single 0 could still be a valid case.

            -> e.g.

                - "0" : valid

                - "01": NOT valid

                - "011": NOT valid 

                ....


            -> e.g.
                # Leading zero 不合法，例如 "01"、"00"
                # 但單獨的 "0" 是合法的


            2. return vs break：核心差異:


            - return: 

                結束目前整個 DFS function call
                當前 state 已經不可能找到任何有效答案。

            - break:

                只結束目前的 for loop
                目前的選擇和後續更大的選擇都不合法，
                但其他已經探索過的分支不受影響。

            ->

             ONLY when it's NOT possible to find the valid
             ip combination (further), then we do `return`


             -> e.g.


                ```
                    # Pruning 1:
                    # 剩餘字元太少，無法組成剩餘 segments
                    if remaining_chars < remaining_segments:
                        return

                    # Pruning 2:
                    # 剩餘字元太多，每個 segment 最多 3 位數
                    if remaining_chars > remaining_segments * 3:
                        return
            ```

            """

            # e.g. -> "0" 開頭且len > 1 is invalid
            if len(segment) > 1 and segment[0] == "0":
                break

            # Segment 必須介於 0 到 255
            if int(segment) > 255:
                continue

            # -------------------------
            # Choose
            # -------------------------
            path.append(segment)

            # -------------------------
            # Explore
            # -------------------------
            self.helper(s, idx + length, path)

            # -------------------------
            # Undo (Backtracking)
            # -------------------------
            # NOTE !!! BACKTRACK (within for loop)
            path.pop()


# V0-2
# IDEA: DFS + backtrack (gemini)
class Solution(object):
    def restoreIpAddresses(self, s):
        """
        :type s: str
        :rtype: List[str]
        """
        res = []
        
        # 剪枝：IPv4 的長度不可能小於 4 也不可能大於 12
        if not s or len(s) < 4 or len(s) > 12:
            return res

        def backtrack(start_idx, path):
            # 終止條件：如果已經切出 4 個區段
            if len(path) == 4:
                # 且字串剛好被完全用完
                if start_idx == len(s):
                    res.append(".".join(path))
                return

            # 剪枝：如果剩餘的字元太多或太少，無法組成剩下的區段，直接返回
            remaining_segments = 4 - len(path)
            remaining_chars = len(s) - start_idx
            
            if remaining_chars < remaining_segments or remaining_chars > remaining_segments * 3:
                return

            # 每個區段長度可以是 1, 2, 3
            for length in range(1, 4):
                if start_idx + length > len(s):
                    break
                
                segment = s[start_idx : start_idx + length]
                
                # 檢查區段是否合法：
                # 1. 不能有前導零（例如 "03" 不合法，但單獨的 "0" 合法）
                # 2. 數值必須在 0 到 255 之間
                if (segment.startswith('0') and len(segment) > 1) or int(segment) > 255:
                    continue
                
                # 做選擇：加入路徑，並往後遞迴
                path.append(segment)
                backtrack(start_idx + length, path)
                # 撤銷選擇（回溯）
                path.pop()

        backtrack(0, [])
        return res


# V0-3
# IDEA : DFS
# time = O(1)  # bounded: at most 3^4 splits
# space = O(1)
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

# V0-1
# IDEA : BFS (AGAIN)
# time = O(1)  # bounded: at most 3^4 splits
# space = O(1)
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
# time = O(1)  # bounded: at most 3^4 splits
# space = O(1)
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
# time = O(1)  # bounded: at most 3^4 splits
# space = O(1)
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

# V1''
# https://leetcode.com/problems/restore-ip-addresses/discuss/30946/DFS-in-Python
# IDEA : BFS
# time = O(1)  # bounded: at most 3^4 splits
# space = O(1)
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

# V1'''
# https://leetcode.com/problems/restore-ip-addresses/discuss/31211/Adding-a-python-solution-also-requesting-for-improvement
# IDEA : BRUTE FORCE
# time = O(1)  # bounded: at most 3^4 splits
# space = O(1)
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

# V1''''
# https://blog.csdn.net/fuxuemingzhu/article/details/80657420
# time = O(1)  # bounded: at most 3^4 splits
# space = O(1)
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
# time = O(n^m) = O(3^4)
# space = O(n * m) = O(3 * 4)
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

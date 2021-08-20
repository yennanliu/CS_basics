"""
Write a function to find the longest common prefix string amongst an array of strings.

If there is no common prefix, return an empty string "".

Example 1:

Input: ["flower","flow","flight"]
Output: "fl"
Example 2:

Input: ["dog","racecar","car"]
Output: ""
Explanation: There is no common prefix among the input strings.
Note:

All given inputs are in lowercase letters a-z.

"""

# V0 
class Solution:
    def longestCommonPrefix(self, strs):
        if not strs:
            return ""
        r = ""
        # NOTE : in 1st loop, we loop element in 1st str
        for i in range(len(strs[0])):
            # NOTE : in 2nd loop, we loop sub-str in strs
            for j in range(1, len(strs)):
                # NOTE : i >= len(strs[j]), meaning if index in 1st sub-str already bigger than the next sub-str, so there is no possible `longest prefix` anymore, then we return current result
                if i >= len(strs[j]) or strs[0][i] != strs[j][i]:
                    return r
            r += strs[0][i]
        return r
    
# V1 
# https://blog.csdn.net/coder_orz/article/details/51706442
class Solution(object):
    def longestCommonPrefix(self, strs):
        """
        :type strs: List[str]
        :rtype: str
        """
        if not strs:
            return ''
        res = ''
        for i in range(len(strs[0])):
            for j in range(1, len(strs)):
                if i >= len(strs[j]) or strs[j][i] != strs[0][i]:
                    return res
            res += strs[0][i]
        return res

# V1' 
# https://blog.csdn.net/coder_orz/article/details/51706442
class Solution(object):
    def longestCommonPrefix(self, strs):
        """
        :type strs: List[str]
        :rtype: str
        """
        if not strs:
            return ''
        strs.sort()
        res = ''
        for i in range(len(strs[0])):
            if i >= len(strs[-1]) or strs[-1][i] != strs[0][i]:
                return res
            res += strs[0][i]
        return res

# V1''  
# https://blog.csdn.net/coder_orz/article/details/51706442
# IDEA : ZIP PYTHON DEFAULT FUNC 
class Solution(object):
    def longestCommonPrefix(self, strs):
        """
        :type strs: List[str]
        :rtype: str
        """
        if not strs:
            return ''
        for i, chars in enumerate(zip(*strs)):
            if len(set(chars)) > 1:
                return strs[0][:i]
        return min(strs)

# V2 
# Time:  O(n * k), k is the length of the common prefix
# Space: O(1)
class Solution(object):
    def longestCommonPrefix(self, strs):
        """
        :type strs: List[str]
        :rtype: str
        """
        if not strs:
            return ""

        for i in range(len(strs[0])):
            for string in strs[1:]:
                if i >= len(string) or string[i] != strs[0][i]:
                    return strs[0][:i]
        return strs[0]


# Time:  O(n * k), k is the length of the common prefix
# Space: O(k)
class Solution2(object):
    def longestCommonPrefix(self, strs):
        """
        :type strs: List[str]
        :rtype: str
        """
        prefix = ""
        
        for chars in zip(*strs):
            if all(c == chars[0] for c in chars):
                prefix += chars[0]
            else:
                return prefix
            
        return prefix

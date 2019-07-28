# V0 

# V1 
# https://zhuanlan.zhihu.com/p/50564246
# IDEA : TWO POINTER 
class Solution:
    def isLongPressedName(self, name, typed):
        """
        :type name: str
        :type typed: str
        :rtype: bool
        """
        # name's  index : idx 
        # typed's index : i 
        idx = 0
        for i in range(len(typed)):
            if idx < len(name) and name[idx] == typed[i]:
                idx += 1
            elif i == 0 or typed[i] != typed[i-1]:
                return False
        return idx == len(name)

# V2 
# Time:  O(n)
# Space: O(1)
class Solution(object):
    def isLongPressedName(self, name, typed):
        """
        :type name: str
        :type typed: str
        :rtype: bool
        """
        i = 0
        for j in range(len(typed)):
            if i < len(name) and name[i] == typed[j]:
                i += 1
            elif j == 0 or typed[j] != typed[j-1]:
                return False
        return i == len(name)

"""

925. Long Pressed Name
Easy

Your friend is typing his name into a keyboard. Sometimes, when typing a character c, the key might get long pressed, and the character will be typed 1 or more times.

You examine the typed characters of the keyboard. Return True if it is possible that it was your friends name, with some characters (possibly none) being long pressed.

Example 1:

Input: name = "alex", typed = "aaleex"
Output: true
Explanation: 'a' and 'e' in 'alex' were long pressed.

Example 2:

Input: name = "saeed", typed = "ssaaedd"
Output: false
Explanation: 'e' must have been pressed twice, but it was not in the typed output.

Constraints:

1 <= name.length, typed.length <= 1000
name and typed consist of only lowercase English letters.

"""

# V0 

# V1
# https://zhuanlan.zhihu.com/p/50564246
# IDEA : TWO POINTER
# time = O(n)
# space = O(1)
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
# time = O(n)
# space = O(1)
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

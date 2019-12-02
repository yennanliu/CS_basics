# V0 
class Solution:
    # @param {string} s a string
    # @param {string} t a string
    # @return {boolean} true if they are both one edit distance apart or false
    def isOneEditDistance(self, s, t):
        # Write your code here
        m = len(s)
        n = len(t)
        if abs(m - n) > 1:
            return False
        if m > n:
            return self.isOneEditDistance(t, s)
        for i in range(m):
            if s[i] != t[i]:
                if m == n:
                    return s[i + 1:] == t[i + 1:]
                return s[i:] == t[i + 1:]
        return m != n # double check this condition

# V1 
# https://blog.csdn.net/zhangpeterx/article/details/90577678
# https://www.cnblogs.com/lightwindy/p/8606871.html
class Solution:
    def isOneEditDistance(self, s: str, t: str) -> bool:
        lenS=len(s)
        lenT=len(t)
        if lenS>lenT:
            s,t=t,s
            lenS,lenT=lenT,lenS
        
        if lenS==lenT:
            count=0
            for i in range(0,lenS):
                if s[i]!=t[i]:
                    count+=1
                    if count>=2:
                        break
            if count==1:
                return True
            else:
                return False
        elif lenT-lenS>=2:
            return False
        else:
            flag=0
            for i in range(0,lenT):
                if i==lenT-1 and flag==0:
                    return True
                elif s[i-flag]!=t[i]:
                    if flag==0:
                        flag=1
                    else:
                        return False
            return True

# V1'
# https://www.jiuzhang.com/solution/one-edit-distance/#tag-highlight-lang-python
class Solution:
    # @param {string} s a string
    # @param {string} t a string
    # @return {boolean} true if they are both one edit distance apart or false
    def isOneEditDistance(self, s, t):
        # Write your code here
        m = len(s)
        n = len(t)
        if abs(m - n) > 1:
            return False
        if m > n:
            return self.isOneEditDistance(t, s)
        for i in range(m):
            if s[i] != t[i]:
                if m == n:
                    return s[i + 1:] == t[i + 1:]
                return s[i:] == t[i + 1:]
        return m != n

# V2 
# Time:  O(m + n)
# Space: O(1)
class Solution(object):
    def isOneEditDistance(self, s, t):
        """
        :type s: str
        :type t: str
        :rtype: bool
        """
        m, n = len(s), len(t)
        if m > n:
            return self.isOneEditDistance(t, s)
        if n - m > 1:
            return False

        i, shift = 0, n - m
        while i < m and s[i] == t[i]:
            i += 1
        if shift == 0:
            i += 1
        while i < m and s[i] == t[i + shift]:
            i += 1
        return i == m
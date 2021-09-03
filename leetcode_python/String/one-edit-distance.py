"""

Given two strings s and t, determine if they are both one edit distance apart.

Note: 
There are 3 possiblities to satisify one edit distance apart:

Insert a character into s to get t
Delete a character from s to get t
Replace a character of s to get t
Example 1:

Input: s = "ab", t = "acb"
Output: true
Explanation: We can insert 'c' into s to get t.
Example 2:

Input: s = "cab", t = "ad"
Output: false
Explanation: We cannot get t from s by only one step.
Example 3:

Input: s = "1203", t = "1213"
Output: true
Explanation: We can replace '0' with '1' to get t.

"""

# V0
# IDEA : DEAL WITH ALL CASES (Exhaustive method)
class Solution:
    def isOneEditDistance(self, s, t):
        lenS=len(s)
        lenT=len(t)
        # NOTE : the trick we deal 2 array may have different length
        if lenS>lenT:
            s,t=t,s
            lenS,lenT=lenT,lenS
        
        # case 1) : lenT == lenS
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

        # case 2) : lenT - lenS >= 2
        elif lenT-lenS >= 2:
            return False
        
        # case 3) lenT - lenS == 1
        else:
            flag=0
            for i in range(0,lenT):
                # if already meet "the end" of t array
                if i==lenT-1 and flag==0:
                    return True
                ### NOTE : trick here, 
                #    -> since we allow s "can change once" to see if it can qeual t
                #    -> when s[i-flag]!=t[i] happens first time, flag = 1
                #    -> when s[i-flag]!=t[i] happens AGAIN, since now flat should == 1 already, then in this case, we will return False directly
                elif s[i-flag]!=t[i]:
                    if flag==0:
                        flag=1
                    else:
                        return False
            return True

# V0'
# IDER : RECURSION
class Solution:
    def isOneEditDistance(self, s, t):
        m = len(s)
        n = len(t)
        if abs(m - n) > 1:
            return False
        if m > n:
            return self.isOneEditDistance(t, s)
        for i in range(m):
            if s[i] != t[i]:
                # case 1
                if m == n:
                    return s[i + 1:] == t[i + 1:]
                # case 2
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
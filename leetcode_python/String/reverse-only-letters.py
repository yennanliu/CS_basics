# V0 

# V1 
# https://blog.csdn.net/fuxuemingzhu/article/details/82956271
# IDEA : STACK 
# STACK : FIRST IN, LAST OUT (LILO)
class Solution(object):
    def reverseOnlyLetters(self, S):
        """
        :type S: str
        :rtype: str
        """
        letters = []
        N = len(S)
        for i, s in enumerate(S):
            if s.isalpha():
                letters.append(s)
        res = ""
        for i, s in enumerate(S):
            if s.isalpha():
                res += letters.pop()
            else:
                res += s
        return res

# V1' 
# https://blog.csdn.net/fuxuemingzhu/article/details/82956271
# IDEA : SINGLE POINTER 
class Solution(object):
    def reverseOnlyLetters(self, S):
        """
        :type S: str
        :rtype: str
        """
        N = len(S)
        l = N - 1
        res = ""
        for i, s in enumerate(S):
            if s.isalpha():
                while not S[l].isalpha():
                    l -= 1
                res += S[l]
                l -= 1
            else:
                res += s
        return res

# V1'' 
# https://blog.csdn.net/fuxuemingzhu/article/details/82956271
# IDEA : DOUBLE POINTER   
class Solution(object):
    def reverseOnlyLetters(self, S):
        """
        :type S: str
        :rtype: str
        """
        N = len(S)
        left, right = 0, N - 1
        slist = list(S)
        while left < right:
            while left < N and (not S[left].isalpha()):
                left += 1
            while right >= 0 and (not S[right].isalpha()):
                right -= 1
            if left < N and right >= 0 and left < right:
                slist[left], slist[right] = slist[right], slist[left]
            left, right = left + 1, right - 1
        return "".join(slist)

# V2 
# Time:  O(n)
# Space: O(1)
class Solution(object):
    def reverseOnlyLetters(self, S):
        """
        :type S: str
        :rtype: str
        """
        def getNext(S):
            for i in reversed(range(len(S))):
                if S[i].isalpha():
                    yield S[i]

        result = []
        letter = getNext(S)
        for i in range(len(S)):
            if S[i].isalpha():
                result.append(letter.next())
            else:
                result.append(S[i])
        return "".join(result)


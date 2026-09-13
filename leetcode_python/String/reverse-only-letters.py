r"""

917. Reverse Only Letters
Easy

Given a string s, reverse the string according to the following rules:

All the characters that are not English letters remain in the same position.
All the English letters (lowercase or uppercase) should be reversed.

Return s after reversing it.

Example 1:

Input: s = "ab-cd"
Output: "dc-ba"

Example 2:

Input: s = "a-bC-dEf-ghIj"
Output: "j-Ih-gfE-dCba"

Example 3:

Input: s = "Test1ng-Leet=code-Q!"
Output: "Qedo1ct-eeLg=ntse-T!"

Constraints:

1 <= s.length <= 100
s consists of characters with ASCII values in the range [33, 122].
s does not contain '\"' or '\\'.

"""

# V0 

# V1 
# https://blog.csdn.net/fuxuemingzhu/article/details/82956271
# IDEA : STACK 
# STACK : FIRST IN, LAST OUT (LILO)
# time = O(n)
# space = O(n)
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
# time = O(n)
# space = O(1)
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
# time = O(n)
# space = O(1)
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
# time = O(n)
# space = O(1)
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


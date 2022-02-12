"""

# https://blog.csdn.net/fuxuemingzhu/article/details/100976794

Given a string S, remove the vowels 'a', 'e', 'i', 'o', and 'u' from it, and return the new string.

Example 1:

Input: "leetcodeisacommunityforcoders"
Output: "ltcdscmmntyfrcdrs"
1
2
Example 2:

Input: "aeiou"
Output: ""
1
2
Note:

S consists of lowercase English letters only.
1 <= S.length <= 1000


"""

# V0
class Solution:
    def removeVowels(self, S):
        res = ""
        vowel = {'a','e','i','o','u'}
        for i in S:
            if i not in vowel:
                res += i
        return res

# V1
# https://www.twblogs.net/a/5d5f289ebd9eee541c328825
class Solution:
    def removeVowels(self, S: str) -> str:
        res=''
        a={'a','e','i','o','u'}
        for i in S:
            if i not in a:
                res+=i
        return res

# V1'
# https://zhongwen.gitbook.io/leetcode-report/easy/1119.-remove-vowels-from-a-string
class Solution:
    def removeVowels(self, S):
        res = ""
        vowel = {'a','e','i','o','u'}
        for i in S:
            if i not in vowel:
                res += i
        return res
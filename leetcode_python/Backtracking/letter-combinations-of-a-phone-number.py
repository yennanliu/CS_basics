# V0 
class Solution(object):
    def letterCombinations(self, digits):
        """
        :type digits: str
        :rtype: List[str]
        """
        if digits == "": return []
        d = {'2' : "abc", '3' : "def", '4' : "ghi", '5' : "jkl", '6' : "mno", '7' : "pqrs", '8' : "tuv", '9' : "wxyz"}
        res = ['']
        for e in digits:
            res = [w + c for c in d[e] for w in res]
        return res

# V1 
# https://blog.csdn.net/fuxuemingzhu/article/details/79363119
# IDEA : BACK TRACKING  + DFS 
# idea : consider a telephone "keyboard" panel :
#  2 : abc ; 3 : def .... 9 : wxyz 
class Solution(object):
    def letterCombinations(self, digits):
        """
        :type digits: str
        :rtype: List[str]
        """
        kvmaps = {'2': 'abc', '3': 'def', '4': 'ghi', '5': 'jkl', '6': 'mno', '7': 'pqrs', '8': 'tuv', '9': 'wxyz'}
        res = []
        self.dfs(digits, 0, res, '', kvmaps)
        return res
    
    def dfs(self, string, index, res, path, kvmaps):
        if index == len(string):
            if path != '':
                res.append(path)
            return
        for j in kvmaps[string[index]]:
            self.dfs(string, index + 1, res, path + j, kvmaps)

# V1' 
# https://blog.csdn.net/fuxuemingzhu/article/details/79363119
from itertools import product
class Solution(object):
    def letterCombinations(self, digits):
        """
        :type digits: str
        :rtype: List[str]
        """
        if not digits:
            return []
        kvmaps = {'2': 'abc', '3': 'def', '4': 'ghi', '5': 'jkl', '6': 'mno', '7': 'pqrs', '8': 'tuv', '9': 'wxyz'}
        answer = []
        for each in product(*[kvmaps[key] for key in digits]):
            answer.append(''.join(each))
        return answer

# V1'' 
# https://blog.csdn.net/fuxuemingzhu/article/details/79363119
# IDEA : for loop
# DEMO : 
# digits="23"
# In [97]: Solution().letterCombinations(digits)
# res =  ['a', 'b', 'c']
# res =  ['ad', 'bd', 'cd', 'ae', 'be', 'ce', 'af', 'bf', 'cf']
# Out[97]: ['ad', 'bd', 'cd', 'ae', 'be', 'ce', 'af', 'bf', 'cf']
class Solution(object):
    def letterCombinations(self, digits):
        """
        :type digits: str
        :rtype: List[str]
        """
        if digits == "": return []
        d = {'2' : "abc", '3' : "def", '4' : "ghi", '5' : "jkl", '6' : "mno", '7' : "pqrs", '8' : "tuv", '9' : "wxyz"}
        res = ['']
        for e in digits:
            res = [w + c for c in d[e] for w in res]
        return res

# V1'''
# https://www.jiuzhang.com/solution/letter-combinations-of-a-phone-number/#tag-highlight-lang-python
class Solution:
    """
    @param digits: A digital string
    @return: all posible letter combinations
    """
    KEYBOARD = {
    '2': 'abc',
    '3': 'def',
    '4': 'ghi',
    '5': 'jkl',
    '6': 'mno',
    '7': 'pqrs',
    '8': 'tuv',
    '9': 'wxyz',
    }
    def letterCombinations(self, digits):
        if not digits:
            return []
            
        results = []
        self.dfs(digits, 0, '', results)
        
        return results
    
    def dfs(self, digits, index, string, results):
        if index == len(digits):
            results.append(string)
            return
        
        for letter in KEYBOARD[digits[index]]:
            self.dfs(digits, index + 1, string + letter, results)

# V1''''
# https://www.jiuzhang.com/solution/letter-combinations-of-a-phone-number/#tag-highlight-lang-python
class Solution(object):
    def letterCombinations(self, digits):
        chr = ["", "", "abc", "def", "ghi", "jkl", "mno", "pqrs", "tuv", "wxyz"]
        res = []
        for i in range(0, len(digits)):
            num = int(digits[i])
            tmp = []
            for j in range(0, len(chr[num])):
                if len(res):
                    for k in range(0, len(res)):
                        tmp.append(res[k] + chr[num][j])
                else:
                    tmp.append(str(chr[num][j]))
            res = copy.copy(tmp)
        return res

# V2 
# Time:  O(n * 4^n)
# Space: O(n)
class Solution(object):
    # @return a list of strings, [s1, s2]
    def letterCombinations(self, digits):
        if not digits:
            return []

        lookup, result = ["", "", "abc", "def", "ghi", "jkl", "mno", \
                          "pqrs", "tuv", "wxyz"], [""]

        for digit in reversed(digits):
            choices = lookup[int(digit)]
            m, n = len(choices), len(result)
            result += [result[i % n] for i in range(n, m * n)]

            for i in range(m * n):
                result[i] = choices[i / n] + result[i]

        return result

# Time:  O(n * 4^n)
# Space: O(n)
# Recursive Solution
class Solution2(object):
    # @return a list of strings, [s1, s2]
    def letterCombinations(self, digits):
        if not digits:
            return []
        lookup, result = ["", "", "abc", "def", "ghi", "jkl", "mno", \
                          "pqrs", "tuv", "wxyz"], []
        self.letterCombinationsRecu(result, digits, lookup, "", 0)
        return result

    def letterCombinationsRecu(self, result, digits, lookup, cur, n):
        if n == len(digits):
            result.append(cur)
        else:
            for choice in lookup[int(digits[n])]:
                self.letterCombinationsRecu(result, digits, lookup, cur + choice, n + 1)
"""

17. Letter Combinations of a Phone Number
Medium

Given a string containing digits from 2-9 inclusive, return all possible letter combinations that the number could represent. Return the answer in any order.

A mapping of digit to letters (just like on the telephone buttons) is given below. Note that 1 does not map to any letters.


Example 1:

Input: digits = "23"
Output: ["ad","ae","af","bd","be","bf","cd","ce","cf"]
Example 2:

Input: digits = ""
Output: []
Example 3:

Input: digits = "2"
Output: ["a","b","c"]
 

Constraints:

0 <= digits.length <= 4
digits[i] is a digit in the range ['2', '9'].

"""

# V0
# IDEA : dfs + backtracking
class Solution(object):
    def letterCombinations(self, digits):

        def dfs(idx, layer, tmp):

            """
            NOTE : if idx == len(digits)
               -> if tmp is not null, then we append tmp to our result (res)
               -> and we out of the loop
            """
            if idx == len(digits):
                if tmp != "":
                    res.append(tmp)
                return

            ### NOTE : we loop alphabets in d map per number rather than loop over number
            for alpha in d[digits[idx]]:
                """
                NOTE 
                idex+1 : for loop to next number
                tmp+j : for collect cur update
                """
                print ("digits = " + str(digits), " tmp = " + str(tmp) + " alpha = " + str(alpha))
                dfs(idx+1, layer+1, tmp + alpha)

        d = {'2' : "abc", '3' : "def", '4' : "ghi", '5' : "jkl", '6' : "mno", '7' : "pqrs", '8' : "tuv", '9' : "wxyz"}
        res = []
        dfs(0,0,"")
        return res

# V0
# IDEA : dfs + backtracking
class Solution(object):
    def letterCombinations(self, digits):
        d = {'2' : "abc", '3' : "def", '4' : "ghi", '5' : "jkl", '6' : "mno", '7' : "pqrs", '8' : "tuv", '9' : "wxyz"}
        res = []
        def dfs(digits, idx, tmp):
            if idx == len(digits):
                if tmp != "":
                    res.append(tmp)
                return
            ### NOTE : we loop alphabets in d map per number rather than loop over number
            for j in d[digits[idx]]:
                """
                NOTE 
                idex+1 : for loop to next number
                tmp+j : for collect cur update
                """
                dfs(digits, idx+1, tmp+j)
        dfs(digits, 0, "")
        return res

# V0'
class Solution(object):
    def letterCombinations(self, digits):
        if digits == "": return []
        d = {'2' : "abc", '3' : "def", '4' : "ghi", '5' : "jkl", '6' : "mno", '7' : "pqrs", '8' : "tuv", '9' : "wxyz"}
        res = ['']
        for e in digits:
            res = [w + c for c in d[e] for w in res]
        return res

# V0'
class Solution(object):
    def letterCombinations(self, digits):
        d = {'2' : "abc", '3' : "def", '4' : "ghi", '5' : "jkl", '6' : "mno", '7' : "pqrs", '8' : "tuv", '9' : "wxyz"}
        r = ['']

        if digits == "":
            return []

        if len(digits) == 1:
            return d[digits]

        for e in digits:
            r = [ b+a for a in d[e] for b in r]
        return r

# V0'
# IDEA : DFS
class Solution(object):
    def letterCombinations(self, digits):
        d_map = {'2' : "abc", '3' : "def", '4' : "ghi", '5' : "jkl", '6' : "mno", '7' : "pqrs", '8' : "tuv", '9' : "wxyz"}
        r = []
        idx = 0
        tmp = ""

        self.dfs(digits, idx, r, tmp, d_map)
        return r

    def dfs(self, digits, idx, r, tmp, d_map):
        #digits = digits[idx:]
        if idx == len(digits):
            if tmp != "":
                r.append(tmp)       
            return
            
        for j in d_map[digits[idx]]:
            self.dfs(digits, idx+1, r, tmp+j, d_map)

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
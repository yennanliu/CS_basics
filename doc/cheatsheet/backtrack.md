# bracktrack cheatsheet 

- https://github.com/labuladong/fucking-algorithm/blob/master/%E7%AE%97%E6%B3%95%E6%80%9D%E7%BB%B4%E7%B3%BB%E5%88%97/%E5%9B%9E%E6%BA%AF%E7%AE%97%E6%B3%95%E8%AF%A6%E8%A7%A3%E4%BF%AE%E8%AE%A2%E7%89%88.md

- Backtrack (brute force) -> DP (dynamic programming)

## 0) Concept  

### 0-1) Framework

### 0-2) Pattern
```python
result = []
def backtrack(route, choice_list):
    if end_condition:
        result.add(route)
        return
    
    for choice in choice_list:
    	### core of backtrack
        do_choice
        backtrack(route, choice_list)
        undo_choice
```

## 1) General form

### 1-1) Basic OP

## 2) LC Example

### 2-1) Letter Combinations of a Phone Number 
```python
# 017 	Letter Combinations of a Phone Number
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
```

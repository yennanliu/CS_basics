# Bracktrack 

> Brute force via  `decision tree process`

- https://github.com/labuladong/fucking-algorithm/blob/master/%E7%AE%97%E6%B3%95%E6%80%9D%E7%BB%B4%E7%B3%BB%E5%88%97/%E5%9B%9E%E6%BA%AF%E7%AE%97%E6%B3%95%E8%AF%A6%E8%A7%A3%E4%BF%AE%E8%AE%A2%E7%89%88.md

- Backtrack (brute force) -> DP (dynamic programming)
- optimization:
    - remove duplicated sub cases
    - have a cache (e.g. hash table) list finished cases (not re-compute them)

## 0) Concept  

### 0-1) Types
- 3 things to consider : 
 - `Route` : The choices have made
 - `Choice list` : The choices we can select currently
 - `End condition` : bottom of decision tree, meet this point then can't do any other choise 

### 0-2) Pattern

```python
# pseudo code
for choice in choice_list:
    # do choice
    routes.add(choice)
    backtrack(routes, choice_list)
    # undo choice
    routes.remove(choice)
```

```python
# python pseudo code
result = []
def backtrack(route, choice_list):
    if end_condition:
        result.add(route)
        return
    
    for choice in choice_list:
    	### core of backtrack
        do_choice   ### this one is necessary
        backtrack(route, choice_list)
        undo_choice ### this one is necessary
```

## 1) General form

### 1-1) Basic OP

## 2) LC Example

### 2-1) Letter Combinations of a Phone Number 
```python
# 017 	Letter Combinations of a Phone Number
# V0 
# idea : backtrack
class Solution(object):
    def letterCombinations(self, digits):
        alpha_map = {'2': 'abc', '3': 'def', '4': 'ghi', '5': 'jkl', '6': 'mno', '7': 'pqrs', '8': 'tuv', '9': 'wxyz'}
        res = []
        self.dfs(digits, 0, res, '', alpha_map)
        return res
    
    def dfs(self, string, index, res, path, alpha_map):
        if index == len(string):
            if path != '':
                res.append(path)
            return
        for j in alpha_map[string[index]]:
            self.dfs(string, index + 1, res, path + j, alpha_map)

# V1 
# idea : for loop
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

### 2-2) combination-sum
```python
class Solution(object):
    def combinationSum(self, candidates, target):
        result = []
        candidates = sorted(candidates)
        def dfs(remain, stack):
            if remain == 0:
                result.append(stack)
                return 

            for item in candidates:
                if item > remain: break
                if stack and item < stack[-1]: continue
                else:
                    dfs(remain - item, stack + [item])

        dfs(target, [])
        return result
``` 

### 2-3) Word Search
```python
# 079 Word Search
# V0
# IDEA : DFS + backtracking
class Solution(object):
 
    def exist(self, board, word):
        visited = [[False for j in range(len(board[0]))] for i in range(len(board))]

        for i in range(len(board)):
            for j in range(len(board[0])):
                if self.existRecu(board, word, 0, i, j, visited):
                    return True

        return False

    def existRecu(self, board, word, cur, i, j, visited):
        # if "not false" till cur == len(word), means we already found the wprd in board
        if cur == len(word):
            return True

        # note this condition
        if i < 0 or i >= len(board) or j < 0 or j >= len(board[0]) or visited[i][j] or board[i][j] != word[cur]:
            return False

        # mark as visited
        visited[i][j] = True
        ### NOTE THIS TRICK (run the existRecu on 4 directions on the same time)
        result = self.existRecu(board, word, cur + 1, i + 1, j, visited) or\
                 self.existRecu(board, word, cur + 1, i - 1, j, visited) or\
                 self.existRecu(board, word, cur + 1, i, j + 1, visited) or\
                 self.existRecu(board, word, cur + 1, i, j - 1, visited)
        # mark as non-visited
        visited[i][j] = False

        return result
```
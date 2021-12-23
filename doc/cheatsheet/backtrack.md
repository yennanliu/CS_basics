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

- Algorithm
    - dfs
    - recursive

- Data structure
    - dict
    - set
    - array

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
# 017   Letter Combinations of a Phone Number
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
# LC 039, LC 040 combination-sum
# V0
# IDEA : DFS + BACKTRACK
class Solution(object):
    def combinationSum(self, candidates, target):

        def dfs(tmp):
            if sum(tmp) == target:
                tmp.sort()
                if tmp not in res:
                    res.append(tmp)
                return
            if sum(tmp) > target:
                return
            for c in candidates:
                dfs(tmp + [c])

        res = []
        tmp = []
        dfs(tmp)
        return res
``` 

### 2-3) Word Search
```python
# LC 079 Word Search

# V0
# IDEA : DFS + backtracking
class Solution(object):
 
    def exist(self, board, word):
        ### NOTE : construct the visited matrix
        visited = [[False for j in range(len(board[0]))] for i in range(len(board))]

        ### NOTE : we visit every element in board and trigger the dfs
        for i in range(len(board)):
            for j in range(len(board[0])):
                if self.dfs(board, word, 0, i, j, visited):
                    return True

        return False

    def dfs(self, board, word, cur, i, j, visited):
        # if "not false" till cur == len(word), means we already found the wprd in board
        if cur == len(word):
            return True

        ### NOTE this condition
        # 1) if idx out of range
        # 2) if already visited
        # 3) if board[i][j] != word[cur] -> not possible to be as same as word
        if i < 0 or i >= len(board) or j < 0 or j >= len(board[0]) or visited[i][j] or board[i][j] != word[cur]:
            return False

        # mark as visited
        visited[i][j] = True
        ### NOTE THIS TRICK (run the existRecu on 4 directions on the same time)
        result = self.dfs(board, word, cur + 1, i + 1, j, visited) or\
                 self.dfs(board, word, cur + 1, i - 1, j, visited) or\
                 self.dfs(board, word, cur + 1, i, j + 1, visited) or\
                 self.dfs(board, word, cur + 1, i, j - 1, visited)
        # mark as non-visited
        visited[i][j] = False

        return result
```

### 2-4) Subsets & Subsets II
```python
# LC 078, 090
# V0
# IDEA : Backtracking
class Solution:
    def subsets(self, nums):
        def backtrack(first = 0, curr = []):
            # if the combination is done
            if len(curr) == k:  
                output.append(curr[:])
                return
            for i in range(first, n):
                # add nums[i] into the current combination
                curr.append(nums[i])
                # use next integers to complete the combination
                backtrack(i + 1, curr)
                # backtrack
                curr.pop()
        
        output = []
        n = len(nums)
        for k in range(n + 1):
            backtrack()
        return output

# V0'
# IDEA : DFS 
class Solution(object):
    def subsets(self, nums):
        def dfs(layer, start, tmp):
            if tmp not in res:
                res.append(tmp)
            if layer == len(nums):
                return
            ### NOTE : we have if condition first, then for loop
            for i in range(start, len(nums)):
                ### NOTE below can make loop start `start idx` updated each time
                dfs(layer+1, i+1, tmp + [nums[i]])
        nums.sort()
        res = []
        dfs(0, 0, [])
        return res
```
```c++
// c++
// backtrack
// (algorithm book (labu) p.303)

// save all subset
vector<vector<int>> res;

/* main func */
vector<vector<int>> subsets(vector<int> & nums){
    // record visited routes
    vector<int> tracks;
    backtrack(nums, 0, track);
    return res;
}

/* use backtrack pattern */
void backtrack(vector<int> & nums, int start, vector<int> & track){
    // pre-order tranverse
    res.push_back(track);
    // start from `start`, avoid duplivated subset
    for (int i = start; i < nums.size(); i++){
        // make choice
        track.push_back(nums[i]);
        // iteration backtrack
        backtrack(nums, i+1, track);
        // undo choice
        track.pop_back();
    }
}
```

### 2-5) Combinations
```python
# LC 77. Combinations
# IDEA : BACKTRACK
class Solution:
    def combine(self, n, k):
        res=[]
        def go(i,ma,ans):
            if ma==k:
                res.append(list(ans))
                return
            if i>n:
                return
            ans.append(i)
            go(i+1,ma+1,ans)
            ans.pop()
            go(i+1,ma,ans)
        go(1,0,[])
        return res
```
```c++
// c++
// backtrack
// (algorithm book (labu) p.305)

// record all combinations
vector<vector<int>> res;

/* main func */
vector<vector<int>> combine(int n, int k){
    if (k <= 0 || n <= 0) return res;
    vector<int> track;
    backtrack(n, k, 1, track);
    return res;
}

/* use backtrack pattern */
void backtrack(int n, int k, int start, vector<int> & track){
    // not update res till visit leaf node
    if (k == track.size()){
        res.push_back(track);
        return;
    }

    // increase from i
    for (int i = start; i <= n; i ++){
        // do choice
        track.push_back(i);
        // backtrack
        backtrack(n, k, i+1, track);
        // undo choice
        track.pop_back();
    }
}
```
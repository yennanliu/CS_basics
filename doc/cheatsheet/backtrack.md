# Bracktrack 

> Brute force via  `decision tree process`

- https://github.com/labuladong/fucking-algorithm/blob/master/%E7%AE%97%E6%B3%95%E6%80%9D%E7%BB%B4%E7%B3%BB%E5%88%97/%E5%9B%9E%E6%BA%AF%E7%AE%97%E6%B3%95%E8%AF%A6%E8%A7%A3%E4%BF%AE%E8%AE%A2%E7%89%88.md

- Backtrack (brute force) -> DP (dynamic programming)
- optimization:
    - remove duplicated sub cases
    - have a cache (e.g. hash table) list finished cases (not re-compute them)
    - "cut the sub-tree" via conditions such as `contains`, `start`

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

- Problems types
    - Combinations
        - LC 77 
        - backtrack. via `start` remove already used numbers and return all cases
    - Permutations 
        - LC 46 
        - backtrack. via `contains` remove already used numbers and return all cases
    - Subsets 
        - LC 78 
        - find minumum case. transform the problem to `tree-problem`. via `start` remove already used numbers and return all cases
    - parentheses
        - LC 20, LC 22


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

### 2-6) Permutations
```python
# LC 46. Permutations
# V0 
class Solution(object):
    def permute(self, nums):
        visited = [0] * len(nums)
        res = []
        path = []
        self.dfs(path)
        return res
        
    def dfs(self, path):
        if len(path) == len(nums):
            res.append(path)
        else:
            for i in range(len(nums)):
                if not visited[i]:
                    visited[i] = 1
                    dfs(path + [nums[i]])
                    visited[i] = 0    ### to check if "visited[i] = 0" is necessary
```
```java
// LC 46. Permutations
// java
// (algorithm book (labu) p.307)
List<List<Integer>> res = new LinkedList<>{};

/* main func, insert a set with non-duplicated numbers, return its Permutations */
List<List<Integer>> permute(int nums[]){
    // record "route"
    LinkedList<Integer> track = new LinkedList<>{};
    backtrack(nums, track);
    return res;

void backtrack(int[] nums, LinkedList<Integer> track){
    // arrive leaf node
    if (track.size() == nums.length){
        res.add(new LinkedList(track));
        return ;
    }

    for (int i = 0; i < nums.length; i++){
        // remove "illegal" choices
        if (track.contains(nums[i])){
            continue;
        }
        // make choice
        track.add(nums[i]);
        // go to next decision tree
        backtrack(nums, track);
        // undo choice
        backtrack.removeLast();
     }
  }
}
```

### 2-7) Generate Parentheses
```java
// java
// LC 022 Generate Parentheses
// (algorithm book (labu) p.316)

/* main func */
vector<String> generateParentheses(int n){
    if (n == 0) return {};
    // record all legal collections
    vector<string> res;
    // backtrack the routes (in process)
    string track;
    // init : available left Parentheses and right Parentheses counts as n
    backtrack(n, n, track, res);
    return res;
}

/* remain left Parentheses count : left ;.. remain right Parentheses : right */
void backtrack(int left, int right, string& track, vector<string> & res){
    // if count < 0 : illegal
    if (left < 0 || right < 0) return;
    // if remain  left Parentheses count >  right Parentheses count : illegal
    if (right < left) return;
    // if all Parentheses are used : legal, we got one OK solution
    if (left == 0 && right == 0){
        res.push_back(track);
        return;
    }

    // add one more left Parentheses
    track.push_back('(') // do choice
    backtrack(left - 1, right, track, res);
    track.push_back(); // undo choice

    // add one more right Parentheses
    track.push_back(')') // do choice
    backtrack(left, right - 1, track, res);
    track.push_back(); // undo choice
}
```
### 2-8) Palindrome Partitioning
```python
# LC 131 Palindrome Partitioning
# V0
# IDEA : BACKTRCK, similar as LC 046 permutations
class Solution(object):
    def partition(self, s):
        res = []
        self.helper(s, res, [])
        return res
        
    def helper(self, s, res, path):
        if not s:
            res.append(path)
            return
        # beware of the start and the end index
        for i in range(1, len(s) + 1): 
            if self.isPalindrome(s[:i]):
                """
                ### backtrcking 
                if s[:i] is palindrome, then check if there is palindrome in s[i:] as well
                e.g.  
                    a a b b a 
                  => 
                    if 'aa' (<-) is palindrome, then check a b b a (->)
                """
                self.helper(s[i:], res, path + [s[:i]])

    def isPalindrome(self, x):
        return x == x[::-1]
```

### 2-9) Restore IP Addresses
```python
# 093 Restore IP Addresses
# V0 
# IDEA : DFS
class Solution(object):
    def restoreIpAddresses(self, s):
        # if not valid input form (ip address length should < 12)
        if len(s) > 12:
            return []
        res = []
        self.dfs(s, [], res)
        return res
        
    def dfs(self, s, path, res):
        # if not remaining elments (not s) and path is in "xxx.xxx.xxx.xxx" form
        if not s and len(path) == 4:
            res.append('.'.join(path))
            return
        for i in [1,2,3]:
            # avoid "out of index" error
            if i > len(s):
                continue
            number = int(s[:i])
            # str(number) == s[:i] for checking if digit is not starting from "0"
            # e.g. 030 is not accepted form, while 30 is OK
            if str(number) == s[:i] and number <= 255:
                self.dfs(s[i:], path + [s[:i]], res)
```
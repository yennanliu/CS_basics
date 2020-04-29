# Palindrome cheatsheet 

### 1) General form
```python
def check_palindrome(x):
	return x == x[::-1]
```

### 2) Example

#### 2-1)
```python
# 131 Palindrome Partitioning
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
                self.helper(s[i:], res, path + [s[:i]])

    def isPalindrome(self, x):
        return x == x[::-1]
```

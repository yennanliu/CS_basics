# Stack

> Stack data structure and algorithm/LC relative to it
> "LIFO (last in, first out)"

- https://github.com/labuladong/fucking-algorithm/blob/master/%E6%95%B0%E6%8D%AE%E7%BB%93%E6%9E%84%E7%B3%BB%E5%88%97/%E5%8D%95%E8%B0%83%E6%A0%88.md

- https://github.com/labuladong/fucking-algorithm/blob/master/%E6%95%B0%E6%8D%AE%E7%BB%93%E6%9E%84%E7%B3%BB%E5%88%97/%E9%98%9F%E5%88%97%E5%AE%9E%E7%8E%B0%E6%A0%88%E6%A0%88%E5%AE%9E%E7%8E%B0%E9%98%9F%E5%88%97.md

## 0) Concept  

### 0-1) Framework
- Single stack
- Build queue via stack
- Build stack via queue

### 0-2) Pattern
```c++
// c++
vector<int> nextGreaterElement(vector<int>& nums) {
    vector<int> ans(nums.size()); // list storage answer
    stack<int> s;
    for (int i = nums.size() - 1; i >= 0; i--) { // put into stack with inverser order
        while (!s.empty() && s.top() <= nums[i]) { // check if height is higher or shorter 
            s.pop(); // start from shorter height
        }
        ans[i] = s.empty() ? -1 : s.top(); // the first higher after this one
        s.push(nums[i]); // put into stack, will check the height later
    }
    return ans;
}
```

## 1) General form

### 1-1) Basic OP
- Stack insert
- Stack pop 
- Stack isEmpty
- Stack hasElement

## 2) LC Example
```python
# LC 394
# V0
# IDEA : STACK
# NOTE : treat before cases separately
#        1) isdigit
#        2) isalpha
#        3) "["
#        4) "]"
# and define num = 0 for dealing with "100a[b]", "10abc" cases
class Solution:
    def decodeString(self, s):
        num = 0
        string = ''
        stack = []
        for c in s:
            if c.isdigit():
                num = num*10 + int(c)
            elif c == "[":
                stack.append(string)
                stack.append(num)
                string = ''
                num = 0
            elif c.isalpha():
                string += c
            elif c == ']':
                pre_num = stack.pop()
                pre_string = stack.pop()
                string = pre_string + pre_num * string
        return string
```
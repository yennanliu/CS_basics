# Stack

> Stack data structure and algorithm/LC relative to it
> "LIFO (last in, first out)"

- https://github.com/labuladong/fucking-algorithm/blob/master/%E6%95%B0%E6%8D%AE%E7%BB%93%E6%9E%84%E7%B3%BB%E5%88%97/%E5%8D%95%E8%B0%83%E6%A0%88.md

- https://github.com/labuladong/fucking-algorithm/blob/master/%E6%95%B0%E6%8D%AE%E7%BB%93%E6%9E%84%E7%B3%BB%E5%88%97/%E9%98%9F%E5%88%97%E5%AE%9E%E7%8E%B0%E6%A0%88%E6%A0%88%E5%AE%9E%E7%8E%B0%E9%98%9F%E5%88%97.md

## 0) Concept  

### 0-1) Types
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

### 1-1-1) basic ops
- Stack insert
- Stack pop 
- Stack isEmpty
- Stack hasElement

### 1-1-2) next greater number
```java
// java
// algorithm book (labu) p. 273
vector<int> nextGreaterElement(vector<int> & nums){
    vector<int> ans(nums.size()); // array for ans
    stack<int> s;
    // inverse ordering
    for (int i = nums.size() - 1 ; i >= 0; i --){
        // check whether nummber is greater
        while (!s.empty() && s.top() <= nums[i]){
            s.pop()
        }
        // the last great number in stack
        ans[i] = s.empty() ? -1: s.top();
        s.push(nums[i]);
    }
return ans;
}
```

## 2) LC Example

### 2-1) Decode String
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

### 2-2) Next Greater Element I
```python
# 496. Next Greater Element I
# V0
# IDEA : double for loop (one of loops is INVERSE ORDERING) + case conditions op
class Solution(object):
    def nextGreaterElement(self, nums1, nums2):
        res = [None for _ in range(len(nums1))]
        tmp = []
        for i in range(len(nums1)):
            ### NOTE : from last idx to 0 idx. (Note the start and end idx)
            for j in range(len(nums2)-1, -1, -1):
                #print ("i = " + str(i) + " j = " + str(j) + " tmp = " + str(tmp))

                # case 1) No "next greater element" found in nums2
                if not tmp and nums2[j] == nums1[i]:
                    res[i] = -1
                    break
                # case 2) found "next greater element" in nums2, keep inverse looping
                elif nums2[j] > nums1[i]:
                    tmp.append(nums2[j])
                # case 2) already reach same element in nums2 (as nums1), pop "last" "next greater element", paste to res, break the loop
                elif tmp and nums2[j] == nums1[i]:
                    _tmp = tmp.pop(-1)
                    res[i] = _tmp
                    tmp = []
                    break
        return res
```
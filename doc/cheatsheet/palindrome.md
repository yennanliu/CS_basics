# Palindrome (回文)

## 0) Concept  

### 0-1) Types

- Types
    - String
    - Array
    - linked list
    - int

- Algorithm
    - back tracking

- Data structure
    - hash table
    - queue
    - linked list
    - int
    - array

### 0-2) Pattern
```python
# python
def check_palindrome(x):
    return x == x[::-1]
```

## 1) General form

### 1-1) Basic OP

#### 1-1-1) Get longest Palindrome string
```java
// algorithm book p. 283
// java
/** start from center, expand to left, right side */
string palindrome(String s, int l, int r){
    // avoid out of boundary
    while (l >= 0 && r < s.size() && s[l] == s[r]){
        // expand to left and right
        l--;
        r++;
    }
    return s.substr(l+1, r-l-1);
}
```

#### 1-1-2) Check if a string is Palindrome
```java
// algorithm book p. 283
// java
/** start from left, right side, expand to center */
boolean isPalindrome(String s){
    int left = 0; right = s.length - 1;
    while (right > left){
        if (s[left] != s[right]){
            return false;
            left ++;
            right --;
        }
    }
    return true;
}
```

## 2) LC Example

### 2-1) Palindrome Partitioning
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

### 2-2) Palindrome Linked List
```python
# 234. Palindrome Linked List

# V0
# IDEA : LINKED LIST -> LIST
# EXAMPLE INPUT :
# [1,2,2,1]
# WHILE GO THROUGH :
# head = ListNode{val: 2, next: ListNode{val: 2, next: ListNode{val: 1, next: None}}}
# head = ListNode{val: 2, next: ListNode{val: 1, next: None}}
# head = ListNode{val: 1, next: None}
# head = None
class Solution(object):
    def isPalindrome(self, head):
        ### NOTE : THE CONDITION
        if not head or not head.next:
            return True
        r = []
        ### NOTE : THE CONDITION
        while head:
            r.append(head.val)
            head = head.next
        return r == r[::-1]

# V1
class Solution(object):
    def isPalindrome(self, head):
        if not head or not head.next:
            return True

        new_list = []

        # slow & fast pointers find the middle of the linked list 
        slow = fast = head
        while fast and fast.next:
            #new_list.insert(0, slow.val)
            new_list.append(slow.val)
            slow = slow.next
            fast = fast.next.next
        # to find how many nodes in the linked list    
        if fast: 
            slow = slow.next

        for val in new_list[::-1]:
            if val != slow.val:
                return False
            slow = slow.next
        return True
```
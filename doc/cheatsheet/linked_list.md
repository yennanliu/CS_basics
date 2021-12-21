# Linked List 

## 0) Concept
- https://github.com/labuladong/fucking-algorithm/blob/master/%E6%95%B0%E6%8D%AE%E7%BB%93%E6%9E%84%E7%B3%BB%E5%88%97/%E9%80%92%E5%BD%92%E5%8F%8D%E8%BD%AC%E9%93%BE%E8%A1%A8%E7%9A%84%E4%B8%80%E9%83%A8%E5%88%86.md
- https://github.com/labuladong/fucking-algorithm/blob/master/%E9%AB%98%E9%A2%91%E9%9D%A2%E8%AF%95%E7%B3%BB%E5%88%97/k%E4%B8%AA%E4%B8%80%E7%BB%84%E5%8F%8D%E8%BD%AC%E9%93%BE%E8%A1%A8.md
- https://github.com/labuladong/fucking-algorithm/blob/master/%E9%AB%98%E9%A2%91%E9%9D%A2%E8%AF%95%E7%B3%BB%E5%88%97/%E5%88%A4%E6%96%AD%E5%9B%9E%E6%96%87%E9%93%BE%E8%A1%A8.md

```java
// java
class ListNode {
    // attr
    int val;  // node's value
    ListNode next; // point to next node

    // constructor
    ListNode(int val){
        this.val = val;
        this.next = null;
    }
}

// init a ListNode
ListNode node1 = new ListNode(1);
ListNode node2 = new ListNode(2);
ListNode node3 = new ListNode(3);

// motify node's value
node1.val = 0;

// connect nodes
node1.next = node2;
node2.next = node3;
```

### 0-1) Types
- Linked list
- Cycle linked list
- Bi-direction linked list

### 0-2) Pattern

## 1) General form
```java
// java
// single Linklist
public class ListNode {
    int val;
    ListNode next;
    ListNode(int x) { val = x; }
}
```
```python
# python
class Node:
  """
  # constructor
  # A single node of a singly linked list
  """
  def __init__(self, data=None, next=None): 
    self.data = data
    self.next = next

class LinkedList:
  """
  # A Linked List class with a single head node
  """
  def __init__(self):  
    self.head = None

  def get_LL_length(self):
    """
    # get list length method for the linked list
    i.e. 
       before : 1 -> 2 -> 3
       after  : 3
    """
    current = self.head
    length = 0 
    while current:
        current = current.next
        length += 1 
    return length

  def get_LL_tail(self):
    """
    # get list tail method for the linked list
    i.e. 
       before : a -> b -> c
       after  : c
    """
    current = self.head
    while current:
        current = current.next
    return current

  def printLL(self):
    """
    # print method for the linked list
    i.e. 
       before : 1 -> 2 -> 3
       after  : 1 2 3 
    """
    current = self.head
    while current:
      print (current.data)
      current = current.next

  def append(self, data):
    """
    # append method that append a new item at the end of the linkedlist 
    i.e. 
         before :  1 -> 2 -> 3
         after  :  1 -> 2 -> 3 -> 4
    """
    newNode = Node(data)
    if self.head:
      current = self.head
      while current.next:
        current = current.next
      current.next = newNode
    else:
      self.head = newNode
  
  def prepend(self, data):
    """
    # append method that append a new item at the head of the linkedlist 
    i.e. 
         before :  1 -> 2 -> 3
         after  :  0 -> 1 -> 2 -> 3
    """
    newNode = Node(data)
    if self.head:
        current = self.head
        self.head = newNode
        newNode.next = current
        current = current.next
    else:
        self.head = newNode

  def insert(self, idx, data):
    """
    # append method that append a new item within the linkedlist 
    i.e. 
         before :  1 -> 2 -> 3
         insert(1, 2.5)
         after  :  1 -> 2 -> 2.5 -> 3
         before :  1 -> 2 -> 3
         insert(0, 0)
         after  :  0 -> 1 -> 2 -> 3
         before :  1 -> 2 -> 3
         insert(2, 4)
         after  :  1 -> 2 -> 3 -> 4
    """
    current = self.head
    ll_length = self.get_LL_length()

    if idx < 0 or idx > self.get_LL_length():
      print ("idx out of linkedlist range, idx : {}".format(idx))
      return
    elif idx == 0:
        self.prepend(data)
    elif idx == ll_length:
        self.append(data)
    else:
        newNode = Node(data)
        current = self.head
        cur_idx = 0 
        while cur_idx < idx-1:
            current = current.next
            cur_idx += 1 
        newNode.next = current.next
        current.next = newNode

  def remove(self, idx):
    """
    # remove method for the linked list
    i.e. 
       before : 1 -> 2 -> 3
       remove(1) 
       after  : 1 -> 3
       before : 1 -> 2 -> 3
       remove(2) 
       after  : 1 -> 2
       before : 1 -> 2 -> 3
       remove(0) 
       after  : 2 -> 3
    """
    if idx < 0 or idx > self.get_LL_length():
        print ("idx out of linkedlist range, idx : {}".format(idx))
        return 
    elif idx == 0:
        current = self.head
        self.head = current.next
    elif idx == self.get_LL_length():
        current = self.head
        cur_idx = 0
        while cur_idx < idx -1:
            current = current.next
            cur_idx += 1
        current.next = None
    else:
        current = self.head
        cur_idx = 0 
        while cur_idx < idx - 1:
            current = current.next
            cur_idx += 1 
        next_ = current.next.next
        current.next = next_
        current = next_ 

  def reverse(self): 
    """
    # reverse method for the linked list
    # https://www.geeksforgeeks.org/python-program-for-reverse-a-linked-list/
    i.e. 
     before : 1 -> 2 -> 3
     after  : 3 -> 2 -> 1 
    """
    prev = None
    current = self.head 
    while(current is not None): 
        next_ = current.next
        current.next = prev 
        prev = current 
        current = next_
    self.head = prev 

```

### 1-1) Basic OP

#### 1-1-1) transversal linked list

#### 1-1-2) plus one on linked list

#### 1-1-3) Reverse linked list (iteration, recursion)
```java
// java
//---------------------------
// recursion
//---------------------------
// algorithm book (labu) p.290
ListNode reverse(ListNode head){
    // base case, if not a node or only 1 node -> return itself
    if (head == null || head.next == null){
        return head;
    }
    ListNode last = reverse(head.next);
    head.next.next = head;
    // point end node to null
    head.next = null;
    return last;
}
```

```python
# python
#-------------------------
# iteration
#-------------------------
# LC 206
# V0
# IDEA : Linkedlist basics
class Solution:
    def reverseList(self, head):
        ### NOTE : we define _prev, _cur first
        _prev = None
        _cur = head
        ### NOTE : while tbere is still node in _cur (but not head)
        while _cur:
            ### STEP 1) get _next
            _next = _cur.next
            ### STEP 2) link _cur to _prev
            _cur.next = _prev 
            ### STEP 3) assign _cur to _prev (make _prev as _cur)
            _prev = _cur
            ### STEP 4) assign _next to _cur (make _cur as _next)
            _cur = _next
        ### STEP 5) assign head as _prev (make head as _prev -> make head as the "inverse" head)
        head = _prev 
        # return the head
        return head
        #return _prev # this one works as well

# V0'
class Solution(object):
    def reverseList(self, head):
        if not head:
            return head
        ### NOTE : we define prev = None
        prev = None
        cur = head
        while cur:
            _next = cur.next
            #print ("prev = " + str(prev))
            cur.next = prev
            prev = cur
            cur = _next
        """
        NOTE !!!
        -> we need to return prev, instead of head or others, since prev is the "NEW head" of updated linkedlist now
        """
        return prev
```

#### 1-1-4) Reverse *first N*  linked list (iteration)
```java
// java
//---------------------------
// iteration
//---------------------------
// algorithm book (labu) p.297
ListNode reverse(ListNode a){
    ListNode pre, cur, nxt;
    pre = null;
    cur = a;
    nxt = a;
    while (cur != null){
        nxt = cur.next;
        // reverse on each node
        cur.next = pre;
        // update pointer
        pre = cur;
        cur = nxt;
    }

    // return reversed nodes
    return pre;
}
```


#### 1-1-5) Reverse *nodes in [a,b]*  linked list (iteration)
```java
// java
//---------------------------
// iteration
//---------------------------
// algorithm book (labu) p.298
ListNode reverse(ListNode a, Listnode b){
    ListNode pre, cur, nxt;
    pre = null;
    cur = a;
    nxt = a;
    /** THE ONLY DIFFERENCE (reverse first N nodes VS reverse nodes in [a,b]) */
    while (cur != b){
        nxt = cur.next;
        // reverse on each node
        cur.next = pre;
        // update pointer
        pre = cur;
        cur = nxt;
    }

    // return reversed nodes
    return pre;
}
```

#### 1-1-6) Reverse *nodes in k group*  linked list (iteration)
```java
// java
//---------------------------
// iteration
//---------------------------
// LC 25
// algorithm book (labu) p.298
ListNode reverse(ListNode a, Listnode b){
    ListNode pre, cur, nxt;
    pre = null;
    cur = a;
    nxt = a;
    /** THE ONLY DIFFERENCE (reverse first N nodes VS reverse nodes in [a,b]) */
    while (cur != b){
        nxt = cur.next;
        // reverse on each node
        cur.next = pre;
        // update pointer
        pre = cur;
        cur = nxt;
    }

    // return reversed nodes
    return pre;
}

ListNode reverseGroup(ListNode head, int k){
    if (head == null) return null;
    // inverval [a,b] has k to-reverse elements
    ListNode a, b;
    a = b = head;
    for (int i = 0; i < k; i++){
        // not enough elements (amount < k), no need to reverse -> base case
        if (b == null) return head;
        b = b.next;
    }
    // reverse k elements
    ListNode newHead = reverse(a,b);
    // reverse remaining nodes, and connect with head
    a.next = reverseGroup(b,k);
    return newHead;
}
```

#### 1-1-7) Reverse *first N*  linked list (recursion)
```java
//---------------------------
// recursion
//---------------------------
// java
// algorithm book (labu) p.293

// "postorder" node
ListNode successor = null;

// reverse first N node (from head), and return new head
ListNode reverseN(ListNode head, int n){
    if (n == 1){
        // record n + 1 nodes, will be used in following steps
        successor = head.next;
        return head;
    }

    // set head.next as start point, return first n - 1 nodes
    ListNode last = reverseN(head.next, n-1);

    head.next.next = head;
    // connect reversed head node and following nodes
    head.next = successor;
    return last;
}
```

#### 1-1-8) Reverse *middle N nodes* in linked list (*start, end* as interval) (recursion)
```java
// java
//---------------------------
// recursion
//---------------------------
// algorithm book (labu) p.293

// "postorder" node
ListNode successor = null;

// reverse first N node (from head), and return new head
ListNode reverseN(ListNode head, int n){
    if (n == 1){
        // record n + 1 nodes, will be used in following steps
        successor = head.next;
        return head;
    }

// reverse nodes in index = m to index = n
ListNode reverseBetween(ListNode head, int m, int n){
    // base case
    if (m == 1){
        return reverseN(head, n);
    }

    // for head.next, the op is reverse interval : [m-1, n-1]
    // will trigger base case when when meet reverse start point
    head.next = reverseBetween(head.next, m - 1, n - 1);
    return head;
```

#### 1-1-9) delete node

#### 1-1-10) swap node

#### 1-1-11) add 2 linked list
```python
# LC 002
class Solution(object):
    def addTwoNumbers(self, l1, l2):
        """
        NOTE :
         1. we init linedlist via ListNode()
         2. we NEED make extra head refer same linedlist, since we need to return beginning of linkedlust of this func, while res will meet "tail" at the end of while loop
        """
        head = res = ListNode()
        plus = 0
        tmp = 0
        while l1 or l2:
            tmp += plus
            plus = 0
            if l1:
                tmp += l1.val
                l1 = l1.next
            if l2:
                tmp += l2.val
                l2 = l2.next
            if tmp > 9:
                tmp -= 10
                plus = 1

            res.next = ListNode(tmp)
            res = res.next
            tmp = 0
        ### NOTE : need to deal with case : l1, l2 are completed, but still "remaining" plus
        if plus != 0:
            res.next = ListNode(plus)
            res = res.next
        #print ("res = " + str(res))
        #print ("head = " + str(head))
        return head.next
```

#### 1-1-12) Find linked list middle point
```java
// algorithm book p. 286
// java
Listnode slow, fast;
slow = fast = head;
while (fast && fast.next){
    fast = fast.next.next;
    slow = slow.next;
}
// slow pointer will be linked list middle point

// if element count in linked list is odd (TO VERIFY)
if (fast != null){
    slow = slow.next;
}
```

## 2) LC Example

### 2-1) palindrome-linked-list
```python
# LC 234 : palindrome-linked-list
# V0
# IDEA : LINKED LIST -> LIST
# EXAMPLE INPUT :
# [1,2,2,1]
# WHILE GO THROUGH :
# head = ListNode{val: 2, next: ListNode{val: 2, next: ListNode{val: 1, next: None}}}
# head = ListNode{val: 2, next: ListNode{val: 1, next: None}}
# head = ListNode{val: 1, next: None}
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
```

### 2-2) Merge Two Sorted Lists
```python
# LC 021
# V0
# IDEA : LOOP 2 LINKED LISTS
class Solution(object):
    def mergeTwoLists(self, l1, l2):
        if not l1 or not l2:
            return l1 or l2
        ### NOTICE THIS
        #   -> we init head, and cur
        #   -> use cur for `link` op
        #   -> and return the `head.next`
        head = cur = ListNode(0)
        while l1 and l2:
            if l1.val < l2.val:
                cur.next = l1
                l1 = l1.next
            else:
                cur.next = l2
                l2 = l2.next
            # note this
            cur = cur.next
         # note this
        cur.next = l1 or l2
        ### NOTICE THIS : we return head.next
        return head.next
```

### 2-3) Reverse Linked List
```python
# LC 206
class Solution:
    def reverseList(self, head: ListNode):
        prev = None
        current = head 
        while(current is not None): 
            next_ = current.next
            current.next = prev 
            prev = current 
            current = next_
        head = prev 
        return head
```

### 2-4) Reverse Linked List II
```python
# LC 92 Reverse Linked List II
# V1
# IDEA : Iterative Link Reversal
# https://leetcode.com/problems/reverse-linked-list-ii/solution/
class Solution:
    def reverseBetween(self, head, m, n):

        # Empty list
        if not head:
            return None

        # Move the two pointers until they reach the proper starting point
        # in the list.
        cur, prev = head, None
        while m > 1:
            prev = cur
            cur = cur.next
            m, n = m - 1, n - 1

        # The two pointers that will fix the final connections.
        tail, con = cur, prev

        # Iteratively reverse the nodes until n becomes 0.
        while n:
            third = cur.next
            cur.next = prev
            prev = cur
            cur = third
            n -= 1

        # Adjust the final connections as explained in the algorithm
        if con:
            con.next = prev
        else:
            head = prev
        tail.next = cur
        return head
```

### 2-5) Copy List with Random Pointer
```python
# LC 138. Copy List with Random Pointer
# V0
# IDEA : 
#   step 1) make 2 objects (m, n) refer to same instance (head)
#   step 2) go through m, and set up the dict
#   step 3) go through n, and get the random pointer via the dict we set up in step 2)
class Node(object):
    def __init__(self, val, next, random):
        self.val = val
        self.next = next
        self.random = random

class Solution:
    def copyRandomList(self, head):
        dic = dict()
        ### NOTE : make m, and n refer to same instance (head)
        m = n = head
        while m:
            ### NOTE : the value in dict is Node type (LinkedList)
            dic[m] = Node(m.val)
            m = m.next
        while n:
            dic[n].next = dic.get(n.next)
            dic[n].random = dic.get(n.random)
            n = n.next
        return dic.get(head)
```
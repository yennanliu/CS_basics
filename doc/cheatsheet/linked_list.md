# Linked List 

## 0) Concept
- [fucking algorithm : reverse part of linked list](https://github.com/labuladong/fucking-algorithm/blob/master/%E6%95%B0%E6%8D%AE%E7%BB%93%E6%9E%84%E7%B3%BB%E5%88%97/%E9%80%92%E5%BD%92%E5%8F%8D%E8%BD%AC%E9%93%BE%E8%A1%A8%E7%9A%84%E4%B8%80%E9%83%A8%E5%88%86.md)
- [fucking algorithm : reverse k set of linked list](https://github.com/labuladong/fucking-algorithm/blob/master/%E9%AB%98%E9%A2%91%E9%9D%A2%E8%AF%95%E7%B3%BB%E5%88%97/k%E4%B8%AA%E4%B8%80%E7%BB%84%E5%8F%8D%E8%BD%AC%E9%93%BE%E8%A1%A8.md)
- [fucking algorithm : check palindrome linked list](https://github.com/labuladong/fucking-algorithm/blob/master/%E9%AB%98%E9%A2%91%E9%9D%A2%E8%AF%95%E7%B3%BB%E5%88%97/%E5%88%A4%E6%96%AD%E5%9B%9E%E6%96%87%E9%93%BE%E8%A1%A8.md)


- Use "pseudo head node" 虛擬頭節點
    - [代碼隨想錄: LC 203 Remove Linked List Elements](https://youtu.be/Y4oQJklHxVo?t=1111)
- When delete node from linked list, we need to be at "previous" node, then can delete NEXT node
    - so, need to be at `cur` node, than can `cur.next` node
    ```python
    # python
    # https://youtu.be/Y4oQJklHxVo?t=965
    cur.next = cur.next.next
    ```

```python
# python
# Definition for singly-linked list.
class ListNode(object):
    def __init__(self, val=0, next=None):
        self.val = val
        self.next = next
```

```java
// java

// Single Linkedlist

public class ListNode{

    // attr
    public int val;
    public ListNode next;

    // constructor
    public ListNode(){

    }

    public ListNode(int val){
        this.val = val;
    }

    ListNode(int val, ListNode next){
        this.val = val;
        this.next = next;
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


```java
// java

// Double linked list

// LC 146

public class Node {
    int key;
    int val;
    Node prev;
    Node next;

    public Node(int key, int val) {
        this.key = key;
        this.val = val;
        this.prev = null;
        this.next = null;
    }
}
```

### 0-1) Types
- Linked list
- Cycle linked list
- Bi-direction linked list
- Double Linked list
    - LC 146
- Others
    - LC 138 : 
    ```python
    dic = dict()
    m = n = head
    dic[m] = Node(m.val)
    ```
    - LC 208 : 
    - [trie](https://github.com/yennanliu/CS_basics/blob/master/doc/cheatsheet/trie.md)
    ```python
    self.children = defaultdict(Node)
    ```
- problem types
    - reverse
        - reverse linked list
            - LC 206
        -  reverse linked list within start, end point
            - LC 92, LC 25
        - reverse part of linked list
        - reverse k set of linked list
    - merge
        - merge 2 linked list
    - check
        - check cyclic linked list
        - check beginning of cyclic linked list
    - remove N th node
        - Remove Nth Node From End of List - LC 19
    -  combinations
        - combinations of above cases

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
    self.head = Node()

  def get_length(self):
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

  def get_tail(self):
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

  def print(self):
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
    ll_length = self.get_length()

    if idx < 0 or idx > self.get_length():
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
    if idx < 0 or idx > self.get_length():
        print ("idx out of linkedlist range, idx : {}".format(idx))
        return 
    elif idx == 0:
        current = self.head
        self.head = current.next
    elif idx == self.get_length():
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
    https://www.youtube.com/watch?v=D7y_hoT_YZI

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


#### 1-1-0) Remove Nth node from end
```java
// java
// LC 19

// ...
ListNode dummy = new ListNode(0);
dummy.next = head;
ListNode fast = dummy;
ListNode slow = dummy;

for (int i = 1; i <= n+1; i++){
    //System.out.println("i = " + i);
    fast = fast.next;
}

// move fast and slow pointers on the same time
while (fast != null){
    fast = fast.next;
    slow = slow.next;
}

// NOTE here
slow.next = slow.next.next;
// ...
```

#### 1-1-1) transversal linked list

#### 1-1-2) plus one on linked list

#### 1-1-3) Reverse linked list (iteration)
```python
# python
#-------------------------
# iteration
#-------------------------
# LC 206

# V0
# IDEA : Linkedlist basics
# https://www.youtube.com/watch?v=D7y_hoT_YZI
# STEPS)
# -> STEP 1) cache "next"
# -> STEP 2) point head.next to prev
# -> STEP 3) move prev to head
# -> STEP 4) move head to "next"
class Solution(object):
    def reverseList(self, head):
        # edge case
        if not head:
            return
        prev = None
        while head:
            # cache "next"
            tmp = head.next
            # point head.next to prev
            head.next = prev
            # move prev to head (for next iteration)
            prev = head
            # move head to "next" (for next iteration)
            head = tmp
        # NOTE!!! we return prev
        return prev
```

```java
// java
//---------------------------
// iteration
//---------------------------
// LC 206
// V0
public ListNode reverseList(ListNode head) {

    if (head == null) {
        return null;
    }

    ListNode _prev = null;

    while (head != null) {
        /**
         *  NOTE !!!!
         *
         *   4 operations
         *
         *    step 1) cache next
         *    step 2) point cur to prev
         *    step 3) move prev to cur
         *    step 4) move cur to next
         *
         */
        ListNode _next = head.next;
        head.next = _prev;
        _prev = head;
        head = _next;
    }

    // NOTE!!! we return _prev here, since it's now "new head"
    return _prev;

}
```

#### 1-1-4) Reverse linked list (recursion)
```java
// java
//---------------------------
// recursion
//---------------------------
// LC 206
// algorithm book (labu) p.290
// IDEA : Recursive
// https://leetcode.com/problems/reverse-linked-list/editorial/
// https://github.com/yennanliu/CS_basics/blob/master/leetcode_java/src/main/java/LeetCodeJava/LinkedList/ReverseLinkedList.java
// same as above 
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
    /** THE ONLY DIFFERENCE (reverse nodes VS reverse nodes in [a,b]) */
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

ListNode reverseKGroup(ListNode head, int k){
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
    a.next = reverseKGroup(b,k);
    return newHead;
}
```
```python
# LC 025
class Solution:
    def reverseKGroup(self, head, k):
        # help func
        # check if # of sub nodes still > k
        def check(head, k):
            ans = 0
            while head:
                ans += 1
                if ans >= k:
                    return True
                head = head.next
            return False

        # edge case
        if not head:
            return
        d = dummy = ListNode(None)
        pre = None
        preHead = curHead = head
        while check(curHead, k):
            for _ in range(k):
                # reverse linked list
                tmp = curHead.next
                curHead.next = pre
                pre = curHead
                curHead = tmp
            # reverse linked list
            # ???
            dummy.next = pre
            dummy = preHead
            preHead.next = curHead
            preHead = curHead
        return d.next
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
         1. we init linkedlist via ListNode()
         2. we NEED make extra head refer same linkedlist, since we need to return beginning of linkedlist of this func, while res will meet "tail" at the end of while loop
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

```python
# LC 445 Add Two Numbers II
# V0
# IDEA : string + linked list
# DEMO
# input :
# [7,2,4,3]
# [5,6,4]
# intermedia output : 
# l1_num = 7243
# l2_num = 564
class Solution:
    def addTwoNumbers(self, l1, l2):
        if not l1 and not l2:
            return None

        l1_num = 0
        while l1:
            l1_num = l1_num * 10 + l1.val
            l1 = l1.next

        l2_num = 0
        while l2:
            l2_num = l2_num * 10 + l2.val
            l2 = l2.next

        print ("l1_num = " + str(l1_num))
        print ("l2_num = " + str(l2_num))


        ### NOTE : trick here :
        #    -> get int format of 2 linked list first (l1, l2)
        #    -> then sum them (l1_num + l2_num)
        lsum = l1_num + l2_num

        head = ListNode(None)
        cur = head
        ### NOTE : go thrpigh the linked list int sum, append each digit to ListNode and return it
        for istr in str(lsum):
            cur.next = ListNode(int(istr))
            cur = cur.next
        # NOTE : need to return head (but not cur, since cur already meet the end of ListNode)
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

```python
# LC 876 Middle of the Linked List
# V0
# IDEA : fast, slow pointers + linkedlist
class Solution(object):
    def middleNode(self, head):
        # edge case
        if not head:
            return
        s = f = head
        while f and f.next:
            # if not f:
            #     break
            f = f.next.next
            s = s.next
        return s
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
                """
                ### NOTE
                 1) assign node to cur.next !!! (not cur)
                 2) assign node rather than node.val
                """ 
                cur.next = l1
                l1 = l1.next
            else:
                """
                ### NOTE
                 1) assign node to cur.next !!! (not cur)
                 2) assign node rather than node.val
                """ 
                cur.next = l2
                l2 = l2.next
            # note this
            cur = cur.next
        ### NOTE this (in case either l1 or l2 is remaining so we need to append one of them to cur)
        cur.next = l1 or l2
        ### NOTICE THIS : we return head.next
        return head.next
```

### 2-2') Merge K Sorted Lists
```python
# LC 023 Merge k sorted lists
# V0
# IDEA : LC 021 Merge Two Sorted Lists + implement mergeTwoLists on every 2 linedlist
# https://github.com/yennanliu/CS_basics/blob/master/doc/cheatsheet/linked_list.md#1-1-6-reverse-nodes-in-k-group--linked-list-iteration
class Solution(object):
    def mergeKLists(self, lists):
        if len(lists) == 0:
            return
        if len(lists) == 1:
            return lists[0]
        
        _init_list = lists[0]
        for _list in lists[1:]:
            tmp = self.mergeTwoLists(_init_list, _list)
            _init_list = tmp
        return tmp

    # LC 021 : https://github.com/yennanliu/CS_basics/blob/master/leetcode_python/Linked_list/merge-two-sorted-lists.py
    def mergeTwoLists(self, l1, l2):

        if not l1 or not l2:
            return l1 or l2
            
        res = head = ListNode()
        while l1 and l2:
            if l1.val < l2.val:
                res.next = l1
                l1 = l1.next
            else:
                res.next = l2
                l2 = l2.next
            res = res.next

        if l1 or l2:
            res.next = l1 or l2

        return head.next
```

### 2-3) Reverse Linked List
```python
# LC 206
class Solution(object):
    def reverseList(self, head):
        # edge case
        if not head:
            return
        prev = None
        while head:
            # cache "next"
            tmp = head.next
            # point head.next to prev
            head.next = prev
            # move prev to head
            prev = head
            # move head to "next"
            head = tmp
        return prev
```

### 2-4) Reverse Linked List II

```java
// java

  // V0-1
  // IDEA: LINKED LIST OP (iteration 1)
  // https://neetcode.io/solutions/reverse-linked-list-ii
  // https://youtu.be/RF_M9tX4Eag?si=vTfAtfbmGwzsmtpi
  public ListNode reverseBetween_0_1(ListNode head, int left, int right) {
      ListNode dummy = new ListNode(0);
      dummy.next = head;
      ListNode leftPrev = dummy, cur = head;

      for (int i = 0; i < left - 1; i++) {
          leftPrev = cur;
          cur = cur.next;
      }

      ListNode prev = null;
      for (int i = 0; i < right - left + 1; i++) {
          ListNode tmpNext = cur.next;
          cur.next = prev;
          prev = cur;
          cur = tmpNext;
      }

      leftPrev.next.next = cur;
      leftPrev.next = prev;

      return dummy.next;
  }
```

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

```java
// java
// NOTE : there is also recursive solution
// LC 138
// V2
// IDEA :  Iterative with O(N) Space
// https://leetcode.com/problems/copy-list-with-random-pointer/editorial/
// Visited dictionary to hold old node reference as "key" and new node reference as the "value"
HashMap<Node, Node> visited = new HashMap<Node, Node>();

public Node getClonedNode(Node node) {
    // If the node exists then
    if (node != null) {
        // Check if the node is in the visited dictionary
        if (this.visited.containsKey(node)) {
            // If its in the visited dictionary then return the new node reference from the dictionary
            return this.visited.get(node);
        } else {
            // Otherwise create a new node, add to the dictionary and return it
            this.visited.put(node, new Node(node.val, null, null));
            return this.visited.get(node);
        }
    }
    return null;
}

public Node copyRandomList_3(Node head) {

    if (head == null) {
        return null;
    }

    Node oldNode = head;

    // Creating the new head node.
    Node newNode = new Node(oldNode.val);
    this.visited.put(oldNode, newNode);

    // Iterate on the linked list until all nodes are cloned.
    while (oldNode != null) {
        // Get the clones of the nodes referenced by random and next pointers.
        newNode.random = this.getClonedNode(oldNode.random);
        newNode.next = this.getClonedNode(oldNode.next);

        // Move one step ahead in the linked list.
        oldNode = oldNode.next;
        newNode = newNode.next;
    }
    return this.visited.get(head);
}
```

### 2-6) Intersection of Two Linked Lists
```python
# LC 160 Intersection of Two Linked Lists
# V0
# IDEA : if the given 2 linked list have intersection, then 
#        they must overlap in SOMEWHERE if we go through
#        each of them in the same length
#        -> e.g.
#             process1 : headA -> headB -> headA ...
#             process2 : headB -> headA -> headB ...
class Solution(object):
    def getIntersectionNode(self, headA, headB):
        if not headA or not headB:
            return None
        p, q = headA, headB
        while p and q and p != q:
            p = p.next
            q = q.next
            if p == q:
                return p
            if not p:
                p = headB
            if not q:
                q = headA
        return p
```

### 2-7) Split Linked List in Parts
```python
# LC 725. Split Linked List in Parts
# V0
# IDEA : LINKED LIST OP + mod op
class Solution(object):
    def splitListToParts(self, head, k):
        # NO need to deal with edge case !!!
        # get linked list length
        _len = 0
        _head = cur = head
        while _head:
            _len += 1
            _head = _head.next
        # init res
        res = [None] * k
        ### NOTE : we loop over k
        for i in range(k):
            """
            2 cases

            case 1) i < (_len % k) : there is "remainder" ((_len % k)), so we need to add extra 1
                    -> _cnt_elem = (_len // k) + 1
            case 2) i == (_len % k) : there is NO "remainder"
                    -> _cnt_elem = (_len // k)
            """
            # NOTE THIS !!!
            _cnt_elem = (_len // k) + (1 if i < (_len % k) else 0)
            ### NOTE : we loop over _cnt_elem (length of each "split" linkedlist)
            for j in range(_cnt_elem):
                """
                3 cases
                 1) j == 0                (begin of sub linked list)
                 2) j == _cnt_elem - 1    (end of sub linked list)
                 3) 0 < j < _cnt_elem - 1 (middle within sub linked list)
                """
                # NOTE THIS !!!
                # NOTE we need keep if - else in BELOW ORDER !!
                #  -> j == 0, j == _cnt_elem - 1, else
                if j == 0:
                    res[i] = cur
                ### NOTE this !!! : 
                #    -> IF (but not elif)
                #    -> since we also need to deal with j == 0 and j == _cnt_elem - 1 case
                if j == _cnt_elem - 1:  # note this !!!
                    # get next first
                    tmp = cur.next
                    # point cur.next to None
                    cur.next = None
                    # move cur to next (tmp) for op in next i (for i in range(k))
                    cur = tmp
                else:
                    cur = cur.next
        #print ("res = " + str(res))
        return res
```

### 2-7) Remove Nth Node From End of List
```python
# LC 19. Remove Nth Node From End of List
# NOTE : there is (two pass algorithm) approach
# V0
# IDEA : FAST-SLOW POINTERS (One pass algorithm)
# IDEA :
#   step 1) we move fast pointers n+1 steps -> so slow, fast pointers has n distance (n+1-1 == n)
#   step 2) we move fast, and slow pointers till fast pointer meet the end
#   step 3) then we point slow.next to slow.next.next (same as we remove n node)
#   step 4) we return new_head.next as final result
class Solution(object):
    def removeNthFromEnd(self, head, n):
        new_head = ListNode(0)
        new_head.next = head
        fast = slow = new_head
        for i in range(n+1):
            fast = fast.next
        while fast:
            fast = fast.next
            slow = slow.next
        slow.next = slow.next.next
        return new_head.next
```

```java
// java
    public ListNode removeNthFromEnd(ListNode head, int n) {

        if (head == null){
            return head;
        }

        if (head.next == null && head.val == n){
            return null;
        }

        // move fast pointer only with n+1 step
        // 2 cases:
        //   - 1) node count is even
        //   - 2) node count is odd
        /** NOTE !! we init dummy pointer, and let fast, slow pointers point to it */
        ListNode dummy = new ListNode(0);
        dummy.next = head;
        // NOTE here
        ListNode fast = dummy;
        ListNode slow = dummy;
        /**
         *  Explanation V1:
         *
         *   -> So we have fast, and slow pointer,
         *   if we move fast N steps first,
         *   then slow starts to move
         *      -> fast, slow has N step difference
         *      -> what's more, when fast reach the end,
         *      -> fast, slow STILL has N step difference
         *      -> and slow has N step difference with the end,
         *      -> so we can remove N th pointer accordingly
         *
         *  Explanation V2:
         *
         *
         *   // NOTE !!! we let fast pointer move N+1 step first
         *   // so once fast pointers reach the end after fast, slow pointers move together
         *   // we are sure that slow pointer is at N-1 node
         *   // so All we need to do is :
         *   // point slow.next to slow.next.next
         *   // then we remove N node from linked list
         */
        for (int i = 1; i <= n+1; i++){
            //System.out.println("i = " + i);
            fast = fast.next;
        }

        // move fast and slow pointers on the same time
        while (fast != null){
            fast = fast.next;
            slow = slow.next;
        }

        // NOTE here
        slow.next = slow.next.next;
        // NOTE !!! we return dummy.next instead of slow
        return dummy.next;
    }
```

```java
// java
    // V0
    // IDEA : get len of linkedlist, and re-point node
    public ListNode removeNthFromEnd_0(ListNode head, int n) {

        if (head.next == null){
            return null;
        }

        // below op is optional
//        if (head.next.next == null){
//            if (n == 1){
//                return new ListNode(head.val);
//            }
//            return new ListNode(head.next.val);
//        }

        // get len
        int len = 0;
        ListNode head_ = head;
        while (head_ != null){
            head_ = head_.next;
            len += 1;
        }

        ListNode root = new ListNode();
        /** NOTE !!! root_ is the actual final result */
        ListNode root_ = root;

        // if n == len
        if (n == len){
            head = head.next;
            root.next = head;
            root = root.next;
        }

        /**
         *  IDEA: get length of linked list,
         *        then if want to delete n node from the end of linked list,
         *        -> then we need to stop at "len - n" idx,
         *        -> and reconnect "len - n" idx to "len -n + 2" idx
         *        -> (which equals delete "n" idx node
         *
         *
         *  Consider linked list below :
         *
         *   0, 1, 2 , 3, 4 .... k-2, k-1, k
         *
         *   if n = 1, then "k-1" is the node to be removed.
         *   -> so we find "k-2" node, and re-connect it to "k" node
         */
        /** NOTE !!!
         *
         *  idx is the index, that we "stop",  and re-connect
         *  from idx to its next next node (which is the actual "delete" node op
         */
        int idx = len - n; // NOTE !!! this
        while (idx > 0){
            root.next = head;
            root = root.next;
            head = head.next;
            idx -= 1;
        }

        ListNode next = head.next;
        root.next = next;

        return root_.next;
    }
```

### 2-8) Reorder List

```java
// java
    public void reorderList(ListNode head) {
        // Edge case: empty or single node list
        if (head == null || head.next == null) {
            return;
        }

        // Step 1: Find the middle node
        ListNode slow = head, fast = head;
        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }

        // Step 2: Reverse the second half of the list
        /** NOTE !!!
         *
         *  reverse on `slow.next` node
         */
        ListNode secondHalf = reverseNode_(slow.next);
        /** NOTE !!!
         *
         *  `cut off` slow node's next nodes via point it to null node
         *  (if not cut off, then in merge step, we will merge duplicated nodes
         */
        slow.next = null; // Break the list into two halves

        // Step 3: Merge two halves
        ListNode firstHalf = head;
        while (secondHalf != null) {

            // NOTE !!! cache `next node` before any op
            ListNode _nextFirstHalf = firstHalf.next;
            ListNode _nextSecondHalf = secondHalf.next;

            // NOTE !!! point first node to second node, then point second node to first node's next node
            firstHalf.next = secondHalf;
            secondHalf.next = _nextFirstHalf;

            // NOTE !!! move both node to `next` node
            firstHalf = _nextFirstHalf;
            secondHalf = _nextSecondHalf;
        }
    }

    // Helper function to reverse a linked list
    private ListNode reverseNode_(ListNode head) {
        ListNode prev = null;
        while (head != null) {
            ListNode next = head.next;
            head.next = prev;
            prev = head;
            head = next;
        }
        return prev;
    }
```

```python
# LC 143. Reorder List
# V0
# IDEA : Reverse the Second Part of the List and Merge Two Sorted Lists
class Solution:
    def reorderList(self, head):
        if not head:
            return 
        
        # find the middle of linked list [Problem 876]
        # in 1->2->3->4->5->6 find 4 
        slow = fast = head
        while fast and fast.next:
            slow = slow.next
            fast = fast.next.next 
            
        # reverse the second part of the list [Problem 206]
        # convert 1->2->3->4->5->6 into 1->2->3->4 and 6->5->4
        # reverse the second half in-place
        prev, curr = None, slow
        while curr:
            tmp = curr.next
            
            curr.next = prev
            prev = curr
            curr = tmp    

        # merge two sorted linked lists [Problem 21]
        # merge 1->2->3->4 and 6->5->4 into 1->6->2->5->3->4
        first, second = head, prev
        while second.next:
            tmp = first.next
            first.next = second
            first = tmp
            
            tmp = second.next
            second.next = first
            second = tmp

# V0'
# IDEA : Reverse the Second Part of the List and Merge Two Sorted Lists (simplified code from V1)
class Solution:
    def reorderList(self, head):
        if not head:
            return 
        
        # find the middle of linked list [Problem 876]
        # in 1->2->3->4->5->6 find 4 
        slow = fast = head
        while fast and fast.next:
            slow = slow.next
            fast = fast.next.next 
            
        # reverse the second part of the list [Problem 206]
        # convert 1->2->3->4->5->6 into 1->2->3->4 and 6->5->4
        # reverse the second half in-place
        prev, curr = None, slow
        while curr:
            curr.next, prev, curr = prev, curr, curr.next       

        # merge two sorted linked lists [Problem 21]
        # merge 1->2->3->4 and 6->5->4 into 1->6->2->5->3->4
        first, second = head, prev
        while second.next:
            first.next, first = second, first.next
            second.next, second = first, second.next

# V0'''
class Solution:
    def reorderList(self, head):
        if head is None:
            return head

        #find mid
        slow = head
        fast = head
        while fast.next and fast.next.next:
            slow = slow.next
            fast = fast.next.next
        mid = slow

        #cut in the mid
        left = head
        right = mid.next
        if right is None:
            return head
        mid.next = None

        #reverse right half
        cursor = right.next
        right.next = None
        while cursor:
            next = cursor.next
            cursor.next = right
            right = cursor
            cursor = next
        
        #merge left and right
        dummy = ListNode(0)
        while left or right:
            if left is not None:
                dummy.next = left
                left = left.next
                dummy = dummy.next
            if right is not None:
                dummy.next = right
                right = right.next
                dummy = dummy.next
        return head
```

### 2-9) Swap Nodes in Pairs
```python
# LC 24. Swap Nodes in Pairs
# V0 
# IDEA : LINKED LIST
# NOTE : 
#   1) define 2 node via : n1, n2 = head.next, head.next.next
#   2) START THE PROCESS FROM "RIGHT HAND SIDE",
#      i.e. : n1.next = n2.next ( connect n1 to next node) -> connect n2 to n1 (n2.next = n1) -> connect dummy to n2 (head.next = n2)
#   3) THEN MOVE HEAD FORWARD (head = n1)
class Solution:
    def swapPairs(self, head):
        if not head or not head.next:
            return head
        dummy = ListNode(0)
        dummy.next = head
        head = dummy
        while head.next and head.next.next:
            n1, n2 = head.next, head.next.next
            n1.next = n2.next
            n2.next = n1
            head.next = n2      
            head = n1
        return dummy.next
```

### 2-10) Plus One Linked List
```java
// java
// LC 369
// V1
// IDEA : LINKED LIST OP (gpt)
/**
*  Step 1) reverse linked list
*  Step 2) plus 1, bring `carry` to next digit if curSum > 9, ... repeat for all nodes
*  Step 3) reverse linked list again
*/
public ListNode plusOne_1(ListNode head) {
if (head == null) return new ListNode(1); // Handle edge case

// Reverse the linked list
head = reverseList(head);

// Add one to the reversed list
ListNode current = head;
int carry = 1; // Start with adding one

while (current != null && carry > 0) {
  int sum = current.val + carry;
  current.val = sum % 10; // Update the current node value
  carry = sum / 10; // Calculate carry for the next node
  if (current.next == null && carry > 0) {
    current.next = new ListNode(carry); // Add a new node for carry
    carry = 0; // No more carry after this
  }
  current = current.next;
}

// Reverse the list back to original order
return reverseList(head);
}

// Utility to reverse a linked list
private ListNode reverseList(ListNode head) {
ListNode prev = null;
ListNode current = head;

while (current != null) {
  ListNode next = current.next; // Save the next node
  current.next = prev; // Reverse the link
  prev = current; // Move prev forward
  current = next; // Move current forward
}

return prev;
}
```

### 2-11) Linked List Components

```java
// java
// LC 817
    // V1
    // IDEA: set, linkedlist (gpt)
    public int numComponents_1(ListNode head, int[] nums) {
        // Convert nums array to a HashSet for O(1) lookups
        Set<Integer> numsSet = new HashSet<>();
        for (int num : nums) {
            numsSet.add(num);
        }

        int count = 0;
        boolean inComponent = false;

        // Traverse the linked list
        while (head != null) {
            if (numsSet.contains(head.val)) {
                // Start a new component if not already in one
                if (!inComponent) {
                    count++;
                    inComponent = true;
                }
            } else {
                // End the current component
                inComponent = false;
            }
            head = head.next;
        }

        return count;
    }
```
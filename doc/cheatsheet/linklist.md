# Linklist 

## 0) Concept
- https://github.com/labuladong/fucking-algorithm/blob/master/%E6%95%B0%E6%8D%AE%E7%BB%93%E6%9E%84%E7%B3%BB%E5%88%97/%E9%80%92%E5%BD%92%E5%8F%8D%E8%BD%AC%E9%93%BE%E8%A1%A8%E7%9A%84%E4%B8%80%E9%83%A8%E5%88%86.md
- https://github.com/labuladong/fucking-algorithm/blob/master/%E9%AB%98%E9%A2%91%E9%9D%A2%E8%AF%95%E7%B3%BB%E5%88%97/k%E4%B8%AA%E4%B8%80%E7%BB%84%E5%8F%8D%E8%BD%AC%E9%93%BE%E8%A1%A8.md
- https://github.com/labuladong/fucking-algorithm/blob/master/%E9%AB%98%E9%A2%91%E9%9D%A2%E8%AF%95%E7%B3%BB%E5%88%97/%E5%88%A4%E6%96%AD%E5%9B%9E%E6%96%87%E9%93%BE%E8%A1%A8.md


### 0-1) Framework
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

## 2) LC Example

### 2-1) Reverse linklist
```python

```

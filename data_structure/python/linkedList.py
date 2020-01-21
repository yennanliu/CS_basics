#################################################################
# DATA STRUCTURE DEMO : LinkedList
#################################################################

# V0

# V1
# https://www.educative.io/edpresso/how-to-create-a-linked-list-in-python?https://www.educative.io/courses/grokking-the-object-oriented-design-interview?aid=5082902844932096&utm_source=google&utm_medium=cpc&utm_campaign=blog-dynamic&gclid=EAIaIQobChMI2dnYi52P5wIVCLaWCh36WgqWEAAYASAAEgIMmvD_BwE
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
    current = self.head
    length = 0 
    while current:
        current = current.next
        length += 1 
    return length

  def append(self, data):
    """
    # append method that append a new item at the end of the linkedlist 
    i.e. 
         before :  1 -> 2 -> 3
         after  :  1 -> 2 -> 3 -> 4
    """
    newNode = Node(data)
    if self.head :
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
         after  :  1 -> 2 -> 2.5 -> 3

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
    if idx < 0 or idx > self.get_LL_length():
        print ("idx out of linkedlist range, idx : {}".format(idx))
        return 
    elif idx == 0:
        current = self.head
        self.head = current.next
        current = current.next
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
      
# LL = LinkedList()
# LL.append(3)
# LL.append(4)
# LL.append(5)
# LL.prepend(2)
# LL.append(6)
# LL.prepend(1)
# print ("print LinkedList : ")
# LL.printLL()
# LL.insert(2, 1.5)
# print ("print LinkedList : ")
# LL.printLL()
# LL.insert(7, 100)
# print ("print LinkedList : ")
# LL.printLL()
# LL.remove(0)
# print ("print LinkedList : ")
# LL.printLL()
# LL.remove(0)
# print ("print LinkedList : ")
# LL.printLL()

# V1'
# http://zhaochj.github.io/2016/05/12/2016-05-12-%E6%95%B0%E6%8D%AE%E7%BB%93%E6%9E%84-%E9%93%BE%E8%A1%A8/
class Node:
    def __init__(self, data):
        self.data = data
        self.next = None

class LinkedList:
    def __init__(self):
        self.head = None
        self.tail = None
  
    def is_empty(self):
        return self.head is None
  
    def append(self, data):
        node = Node(data)
        if self.head is None:
            self.head = node
            self.tail = node
        else:
            self.tail.next = node
            self.tail = node
  
    def iter(self):
        if not self.head:
            return
        cur = self.head
        yield cur.data
        while cur.__next__:
            cur = cur.__next__
            yield cur.data
  
    def insert(self, idx, value):
        cur = self.head
        cur_idx = 0
        if cur is None:             # check is it's null LinkedList
            raise Exception('The list is an empty list')
        while cur_idx < idx-1:   # go through LinkedList 
            cur = cur.__next__
            if cur is None:   # check if it's the last element 
                raise Exception('list length less than index')
            cur_idx += 1
        node = Node(value)
        node.next = cur.__next__
        cur.next = node
        if node.__next__ is None:
            self.tail = node
  
    def remove(self, idx):
        cur = self.head
        cur_idx = 0
        if self.head is None:  # if it's null LinkedList
            raise Exception('The list is an empty list')
        while cur_idx < idx-1:
            cur = cur.__next__
            if cur is None:
                raise Exception('list length less than index')
            cur_idx += 1
        if idx == 0:   # when delete the first node 
            self.head = cur.__next__
            cur = cur.__next__
            return
        if self.head is self.tail:   # when there is only 1 node in the LinkedList
            self.head = None
            self.tail = None
            return
        cur.next = cur.next.__next__
        if cur.__next__ is None:  # when delete the last node in the LinkedList
            self.tail = cur
  
    def size(self):
        current = self.head
        count = 0
        if current is None:
            return 'The list is an empty list'
        while current is not None:
            count += 1
            current = current.__next__
        return count
  
    def search(self, item):
        current = self.head
        found = False
        while current is not None and not found:
            if current.data == item:
                found = True
            else:
                current = current.__next__
        return found

# link_list = LinkedList()
# for i in range(150):
#     link_list.append(i)
# #    print(link_list.is_empty())
# #    link_list.insert(10, 30)
# #    link_list.remove(0)
# for node in link_list.iter():
#     print(('node is {0}'.format(node)))
# print((link_list.size()))
# #   print(link_list.search(20))

# V2

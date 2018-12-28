#################################################################
# DATA STRUCTURE DEMO : Queue
#################################################################

# http://zhaochj.github.io/2016/05/15/2016-05-15-%E6%95%B0%E6%8D%AE%E7%BB%93%E6%9E%84-%E5%8D%95%E7%AB%AF%E9%98%9F%E5%88%97/


class Node:
    def __init__(self, value):
        self.value = value
        self.next = None
 
 
class Queue:
    def __init__(self):
        self.head = None
        self.tail = None
 
    def enqueue(self, value):
        node = Node(value)
        if self.head is None:
            self.head = node
            self.tail = node
        else:
            self.tail.next = node
            self.tail = node
 
    def dequeue(self):
        if self.head is None:
            raise Exception('This is a empty queue')
        cur = self.head
        self.head = cur.next
        return cur.value
 
    def is_empty(self):
        return self.head is None
 
    def size(self):
        cur = self.head
        count = 0
        if cur is None:
            return count
        while cur.next is not None:
            count += 1
            cur = cur.next
        return count + 1
 
 
if __name__ == '__main__':
    q = Queue()
    for i in range(5):
        q.enqueue(i)
    for _ in range(5):
        print(q.dequeue())
    print(q.is_empty())
    print(q.size())
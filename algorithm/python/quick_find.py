"""
Quick Find algorithm in python 

https://www.coursera.org/learn/algorithms-part1/lecture/EcF3P/quick-find
https://github.com/brunops/algorithms/blob/master/misc/python/QuickFind.py
"""

class QuickFind:
  def __init__(self, size):
    # initialize structure without any connections between objects
    self.ids = [i for i in range(size)]

  # In this implementation, p is updated and q is left alone (doesn't matter the other)
  def union(self, p, q):
    pid = self.ids[p]
    for i in range(len(self.ids)):
      if self.ids[i] == pid:
        self.ids[i] = self.ids[q] 

  def connected(self, p, q):
    return self.ids[p] == self.ids[q]

# Tests
qf = QuickFind(10)

qf.union(4, 3)
assert(qf.ids == [0, 1, 2, 3, 3, 5, 6, 7, 8, 9])

qf.union(3, 8)
assert(qf.ids == [0, 1, 2, 8, 8, 5, 6, 7, 8, 9])

assert(qf.connected(3, 8) == True)
assert(qf.connected(2, 8) == False)

print ("success!")

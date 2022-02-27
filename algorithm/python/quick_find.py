#---------------------------------------------------------------
# QUICK FIND
#---------------------------------------------------------------

"""
Quick Find algorithm in python 

https://www.coursera.org/learn/algorithms-part1/lecture/EcF3P/quick-find
https://github.com/brunops/algorithms/blob/master/misc/python/QuickFind.py
"""

# V0
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

# V1

# V2

# Tests 1 
# print("-"*30, 'test 1', "-"*30)
# qf = QuickFind(10)

"""
# KEY :
 -> when "union", change 1st element value to aligned to the 2nd one's value
 -> e.g. qf.union(4, 3)
 -> [0, 1, 2, 3, 4, 5, 6, 7, 8, 9] -> [0, 1, 2, 3, 3, 5, 6, 7, 8, 9]
 -> (change 4th element value with 3th element value )
"""
# qf.union(4, 3)
# assert(qf.ids == [0, 1, 2, 3, 3, 5, 6, 7, 8, 9])

# qf.union(3, 8)
# assert(qf.ids == [0, 1, 2, 8, 8, 5, 6, 7, 8, 9])

# # check if 2 element is union (connected)
# assert(qf.connected(3, 8) == True)
# assert(qf.connected(2, 8) == False)

# print ("success!")

# Tests 2
# print("-"*30, 'test 2', "-"*30)
# qf = QuickFind(10)

# qf.union(1, 2)
# assert(qf.ids == [0, 2, 2, 3, 4, 5, 6, 7, 8, 9])

# qf.union(1, 3)
# assert(qf.ids == [0, 3, 3, 3, 4, 5, 6, 7, 8, 9])

# assert(qf.connected(1, 2) == True)
# assert(qf.connected(1, 3) == True)
# assert(qf.connected(2, 3) == True)
# assert(qf.connected(1, 4) == False)

# qf.union(7, 8)
# assert(qf.ids == [0, 3, 3, 3, 4, 5, 6, 8, 8, 9])

# qf.union(7, 6)
# assert(qf.ids == [0, 3, 3, 3, 4, 5, 6, 6, 6, 9])

# assert(qf.connected(7, 8) == True)
# assert(qf.connected(6, 7) == True)

# print ("success!")
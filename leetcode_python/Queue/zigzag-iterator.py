# Given two 1d vectors, implement an iterator to return their elements alternately.
# For example, given two 1d vectors:
# v1 = [1, 2]
# v2 = [3, 4, 5, 6]
# By calling next repeatedly until hasNext returns false, the order of elements returned by next should be: [1, 3, 2, 4, 5, 6].

# Follow up: What if you are given k 1d vectors? How well can your code be extended to such cases?

# Clarification for the follow up question - Update (2015-09-18):
# The "Zigzag" order is not clearly defined and is ambiguous for k > 2 cases. If "Zigzag" does not look right to you, replace "Zigzag" with "Cyclic". For example, given the following input:

# [1,2,3]
# [4,5,6,7]
# [8,9]
# It should return [1,4,8,2,5,9,3,6,7].

# V0 

# V1
# http://www.voidcn.com/article/p-wkwesfjj-zo.html
class ZigzagIterator(object):

    def __init__(self, v1, v2):
        """
        Initialize your data structure here.
        :type v1: List[int]
        :type v2: List[int]
        """
        self.l = []
        i = 0
        while i < max(len(v1), len(v2)):
            if i < len(v1):
                self.l.append(v1[i])
            if i < len(v2):
                self.l.append(v2[i])
            i += 1
        self.index = 0

    def next(self):
        """
        :rtype: int
        """
        cur = self.l[self.index]
        self.index += 1
        return cur

    def hasNext(self):
        """
        :rtype: bool
        """
        if self.index < len(self.l):
            return True
        else:
            return False
            
# V1'
class ZigzagIterator(object):

    def Zigzag(self, v1, v2):
        output = []
        if len(v2) > len(v1):
            v1, v2 = v2, v1
        #print (v1, v2)
        for i in range(len(v1)):
            #print (v1[i])
            output.append(v1[i])
            if i < len(v2):
                output.append(v2[i])
            else:
                pass
        return output

# V2 
# Time:  O(n)
# Space: O(k)
import collections
class ZigzagIterator(object):

    def __init__(self, v1, v2):
        """
        Initialize your q structure here.
        :type v1: List[int]
        :type v2: List[int]
        """
        self.q = collections.deque([(len(v), iter(v)) for v in (v1, v2) if v])

    def __next__(self):
        """
        :rtype: int
        """
        len, iter = self.q.popleft()
        if len > 1:
            self.q.append((len-1, iter))
        return next(iter)

    def hasNext(self):
        """
        :rtype: bool
        """
        return bool(self.q)

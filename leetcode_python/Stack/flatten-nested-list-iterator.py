"""

You are given a nested list of integers nestedList. Each element is either an integer or a list whose elements may also be integers or other lists. Implement an iterator to flatten it.

Implement the NestedIterator class:

NestedIterator(List<NestedInteger> nestedList) Initializes the iterator with the nested list nestedList.
int next() Returns the next integer in the nested list.
boolean hasNext() Returns true if there are still some integers in the nested list and false otherwise.
Your code will be tested with the following pseudocode:

initialize iterator with nestedList
res = []
while iterator.hasNext()
    append iterator.next() to the end of res
return res
If res matches the expected flattened list, then your code will be judged as correct.

 

Example 1:

Input: nestedList = [[1,1],2,[1,1]]
Output: [1,1,2,1,1]
Explanation: By calling next repeatedly until hasNext returns false, the order of elements returned by next should be: [1,1,2,1,1].
Example 2:

Input: nestedList = [1,[4,[6]]]
Output: [1,4,6]
Explanation: By calling next repeatedly until hasNext returns false, the order of elements returned by next should be: [1,4,6].
 

Constraints:

1 <= nestedList.length <= 500
The values of the integers in the nested list is in the range [-106, 106].

"""

# """
# This is the interface that allows for creating nested lists.
# You should not implement it, or speculate about its implementation
# """
#class NestedInteger(object):
#    def isInteger(self):
#        """
#        @return True if this NestedInteger holds a single integer, rather than a nested list.
#        :rtype bool
#        """
#
#    def getInteger(self):
#        """
#        @return the single integer that this NestedInteger holds, if it holds a single integer
#        Return None if this NestedInteger holds a nested list
#        :rtype int
#        """
#
#    def getList(self):
#        """
#        @return the nested list that this NestedInteger holds, if it holds a nested list
#        Return None if this NestedInteger holds a single integer
#        :rtype List[NestedInteger]
#        """


# V0
#### key : define a r = [] outside of the func
# r = []
# def flatten_array(_array):
#     for i in _array:
#         if type(i) == int:
#             print (i)
#             r.append(i)
#         else:
#             flatten_array(i)
#
# _input = [1,0, [1,2,[4,[5,[6,[7]]]]]]
#
# flatten_array(_input)
# print ("r = " + str(r))

# V0'
import collections
class NestedIterator(object):

    def __init__(self, nestedList):

        self.queue = collections.deque()
        
        def getAll(nests):
            for nest in nests:
                if nest.isInteger():
                    self.queue.append(nest.getInteger())
                else:
                    getAll(nest.getList())
        getAll(nestedList)

    def next(self):

        return self.queue.popleft()

    def hasNext(self):

        return len(self.queue)

# V1 
# https://blog.csdn.net/fuxuemingzhu/article/details/79529982
import collections
class NestedIterator(object):

    def __init__(self, nestedList):
        """
        Initialize your data structure here.
        :type nestedList: List[NestedInteger]
        """
        self.queue = collections.deque()
        def getAll(nests):
            for nest in nests:
                if nest.isInteger():
                    self.queue.append(nest.getInteger())
                else:
                    getAll(nest.getList())
        getAll(nestedList)

    def next(self):
        """
        :rtype: int
        """
        return self.queue.popleft()

    def hasNext(self):
        """
        :rtype: bool
        """
        return len(self.queue)

# V1'
# https://www.jiuzhang.com/solution/flatten-nested-list-iterator/#tag-highlight-lang-python
class NestedIterator(object):

    def __init__(self, nestedList):
        self.next_elem = None
        self.stack = []
        for elem in reversed(nestedList):
            self.stack.append(elem)
            
    # @return {int} the next element in the iteration
    def next(self):
        if self.next_elem is None:
            self.hasNext()
        temp, self.next_elem = self.next_elem, None
        return temp
        
    # @return {boolean} true if the iteration has more element or false
    def hasNext(self):
        if self.next_elem:
            return True
            
        while self.stack:
            top = self.stack.pop()
            if top.isInteger():
                self.next_elem = top.getInteger()
                return True
            for elem in reversed(top.getList()):
                self.stack.append(elem)
        return False

# V2 
# Time:  O(n), n is the number of the integers.
# Space: O(h), h is the depth of the nested lists.
class NestedIterator(object):

    def __init__(self, nestedList):
        """
        Initialize your data structure here.
        :type nestedList: List[NestedInteger]
        """
        self.__depth = [[nestedList, 0]]


    def next(self):
        """
        :rtype: int
        """
        nestedList, i = self.__depth[-1]
        self.__depth[-1][1] += 1
        return nestedList[i].getInteger()


    def hasNext(self):
        """
        :rtype: bool
        """
        while self.__depth:
            nestedList, i = self.__depth[-1]
            if i == len(nestedList):
                self.__depth.pop()
            elif nestedList[i].isInteger():
                    return True
            else:
                self.__depth[-1][1] += 1
                self.__depth.append([nestedList[i].getList(), 0])
        return False

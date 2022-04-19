"""

341. Flatten Nested List Iterator
Medium

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
class NestedIterator(object):

    def __init__(self, nestedList):

        self.queue = []
        
        def getAll(nests):
            for nest in nests:
                if nest.isInteger():
                    self.queue.append(nest.getInteger())
                else:
                    getAll(nest.getList())
        getAll(nestedList)

    def next(self):

        return self.queue.pop(0)

    def hasNext(self):

        return len(self.queue)

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

# V0''
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

# V1
# IDEA :  Make a Flat List with Recursion
# https://leetcode.com/problems/flatten-nested-list-iterator/solution/
class NestedIterator:
    
    def __init__(self, nestedList):
        def flatten_list(nested_list):
            for nested_integer in nested_list:
                if nested_integer.isInteger():
                    self._integers.append(nested_integer.getInteger())
                else:
                    flatten_list(nested_integer.getList()) 
        self._integers = []
        self._position = -1 # Pointer to previous returned.
        flatten_list(nestedList)
    
    def next(self):
        self._position += 1
        return self._integers[self._position]
        
    def hasNext(self):
        return self._position + 1 < len(self._integers)

# V1
# IDEA : Stack
# https://leetcode.com/problems/flatten-nested-list-iterator/solution/
class NestedIterator:
    
    def __init__(self, nestedList):
        self.stack = list(reversed(nestedList))
        
        
    def next(self):
        self.make_stack_top_an_integer()
        return self.stack.pop().getInteger()
    
        
    def hasNext(self):
        self.make_stack_top_an_integer()
        return len(self.stack) > 0
        
        
    def make_stack_top_an_integer(self):
        # While the stack contains a nested list at the top...
        while self.stack and not self.stack[-1].isInteger():
            # Unpack the list at the top by putting its items onto
            # the stack in reverse order.
            self.stack.extend(reversed(self.stack.pop().getList()))

# V1
# IDEA : Two Stacks
# https://leetcode.com/problems/flatten-nested-list-iterator/solution/
class NestedIterator:
    
    def __init__(self, nestedList):
        self.stack = [[nestedList, 0]]
        
    def make_stack_top_an_integer(self):
        
        while self.stack:
            
            # Essential for readability :)
            current_list = self.stack[-1][0]
            current_index = self.stack[-1][1]
            
            # If the top list is used up, pop it and its index.
            if len(current_list) == current_index:
                self.stack.pop()
                continue
            
            # Otherwise, if it's already an integer, we don't need 
            # to do anything.
            if current_list[current_index].isInteger():
                break
            
            # Otherwise, it must be a list. We need to increment the index
            # on the previous list, and add the new list.
            new_list = current_list[current_index].getList()
            self.stack[-1][1] += 1 # Increment old.
            self.stack.append([new_list, 0])
            
    
    def next(self):
        self.make_stack_top_an_integer()
        current_list = self.stack[-1][0]
        current_index = self.stack[-1][1]
        self.stack[-1][1] += 1
        return current_list[current_index].getInteger()
        
    
    def hasNext(self):
        self.make_stack_top_an_integer()
        return len(self.stack) > 0

# V1
# IDEA : Stack of Iterators
# https://leetcode.com/problems/flatten-nested-list-iterator/solution/
# JAVA
# import java.util.NoSuchElementException;
#
# public class NestedIterator implements Iterator<Integer> {
#   
#     // This time, our stack will hold list iterators instead of just lists.
#     private Deque<ListIterator<NestedInteger>> stackOfIterators = new ArrayDeque();
#     private Integer peeked = null;
#
#     public NestedIterator(List<NestedInteger> nestedList) {
#         // Make an iterator with the input and put it on the stack. 
#         // Note that creating a list iterator is an O(1) operation.
#         stackOfIterators.addFirst(nestedList.listIterator());
#     }
#
#     private void setPeeked() {
#        
#         // If peeked is already set, there's nothing to do.
#         if (peeked != null) return;
#        
#         while (!stackOfIterators.isEmpty()) {
#            
#             // If the iterator at the top of the stack doesn't have a next,
#             // remove that iterator and continue on.
#             if (!stackOfIterators.peekFirst().hasNext()) {
#                 stackOfIterators.removeFirst();
#                 continue;
#             }
#            
#             // Otherwise, we need to check whether that next is a list or 
#             // an integer.
#             NestedInteger next = stackOfIterators.peekFirst().next();
#            
#             // If it's an integer, set peeked to it and return as we're done.
#             if (next.isInteger()) {
#                 peeked = next.getInteger();
#                 return;
#             }
#            
#             // Otherwise, it's a list. Create a new iterator with it, and put
#             // the new iterator on the top of the stack.
#             stackOfIterators.addFirst(next.getList().listIterator());
#         }        
#     }
#    
#
#     @Override
#     public Integer next() {
#        
#         // As per Java specs, throw an exception if there are no further elements.
#         if (!hasNext()) throw new NoSuchElementException();
#        
#         // hasNext() called setPeeked(), which ensures peeked has the next integer 
#         // in it. We need to clear the peeked field so that the element is returned
#         // again.
#         Integer result = peeked;
#         peeked = null;
#         return result;
#     }
#
#     @Override
#     public boolean hasNext() {
#
#         // Try to set the peeked field. If any integers are remaining, it will
#         // contain the next one to be returned after this call.
#         setPeeked();
#        
#         // There are elements remaining iff peeked contains a value.
#         return peeked != null;
#     }
# }


# V1
# IDEA : Using a Generator
# https://leetcode.com/problems/flatten-nested-list-iterator/solution/
class NestedIterator:

    def __init__(self, nestedList: [NestedInteger]):
        # Get a generator object from the generator function, passing in
        # nestedList as the parameter.
        self._generator = self._int_generator(nestedList)
        # All values are placed here before being returned.
        self._peeked = None

    # This is the generator function. It can be used to create generator
    # objects.
    def _int_generator(self, nested_list):
        # This code is the same as Approach 1. It's a recursive DFS.
        for nested in nested_list:
            if nested.isInteger():
                """
                NOTE : we use yield here
                """
                yield nested.getInteger()
            else:
                """
                NOTE : we use yield here
                """
                # We always use "yield from" on recursive generator calls.
                yield from self._int_generator(nested.getList())
        # Will automatically raise a StopIteration.
    
    def next(self):
        # Check there are integers left, and if so, then this will
        # also put one into self._peeked.
        if not self.hasNext(): return None
        # Return the value of self._peeked, also clearing it.
        next_integer, self._peeked = self._peeked, None
        return next_integer
        
    def hasNext(self):
        if self._peeked is not None: return True
        try: # Get another integer out of the generator.
            self._peeked = next(self._generator)
            return True
        except: # The generator is finished so raised StopIteration.
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
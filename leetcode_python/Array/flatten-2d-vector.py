"""

251. Flatten 2D Vector
Medium

Design an iterator to flatten a 2D vector. It should support the next and hasNext operations.

Implement the Vector2D class:

Vector2D(int[][] vec) initializes the object with the 2D vector vec.
next() returns the next element from the 2D vector and moves the pointer one step forward. You may assume that all the calls to next are valid.
hasNext() returns true if there are still some elements in the vector, and false otherwise.
 

Example 1:

Input
["Vector2D", "next", "next", "next", "hasNext", "hasNext", "next", "hasNext"]
[[[[1, 2], [3], [4]]], [], [], [], [], [], [], []]
Output
[null, 1, 2, 3, true, true, 4, false]

Explanation
Vector2D vector2D = new Vector2D([[1, 2], [3], [4]]);
vector2D.next();    // return 1
vector2D.next();    // return 2
vector2D.next();    // return 3
vector2D.hasNext(); // return True
vector2D.hasNext(); // return True
vector2D.next();    // return 4
vector2D.hasNext(); // return False
 

Constraints:

0 <= vec.length <= 200
0 <= vec[i].length <= 500
-500 <= vec[i][j] <= 500
At most 105 calls will be made to next and hasNext.
 

Follow up: As an added challenge, try to code it using only iterators in C++ or iterators in Java.

"""

# V0
# IDEA : ARRAY OP
class Vector2D:

    def __init__(self, v):
        # We need to iterate over the 2D vector, getting all the integers
        # out of it and putting them into the nums list.
        self.nums = []
        for inner_list in v:
            for num in inner_list:
                self.nums.append(num)
        # We'll keep position 1 behind the next number to return.
        self.position = -1

    def next(self):
        # Move up to the current element and return it.
        self.position += 1
        return self.nums[self.position]

    def hasNext(self):
        # If the next position is a valid index of nums, return True.
        return self.position + 1 < len(self.nums)

# V0'
# IDEA : ARRAY OP
class Vector2D(object):

    def __init__(self, vec2d):
        self.row, self.col, self.vec2d = 0, 0, vec2d
        
    def next(self):
        """
        move to next at same layer 
        e.g. [1,2,3] from 1->2
        """
        self.col += 1
        return self.vec2d[self.row][self.col - 1] # fix the idx (start from 0)
        
    def hasNext(self):
        """
        check if there is "next layer"
        e.g.
        [ 
        [1,2],
        [3,4,5]
        ]
        -> yes
        """
        # while 
        # -> 1) still has next layer (self.row < len(self.vec2d))
        # -> 2) still col > current layer length 
        #   if yes, so move to next layer (self.row, self.col = self.row + 1, 0)
        #   if no, check if  self.row < len(self.vec2d)
        while self.row < len(self.vec2d) and \
            self.col >= len(self.vec2d[self.row]):
            self.row, self.col = self.row + 1, 0
        return self.row < len(self.vec2d)

# V1
# IDEA : Flatten List in Constructor
# https://leetcode.com/problems/flatten-2d-vector/solution/
class Vector2D:

    def __init__(self, v: List[List[int]]):
        # We need to iterate over the 2D vector, getting all the integers
        # out of it and putting them into the nums list.
        self.nums = []
        for inner_list in v:
            for num in inner_list:
                self.nums.append(num)
        # We'll keep position 1 behind the next number to return.
        self.position = -1

    def next(self) -> int:
        # Move up to the current element and return it.
        self.position += 1
        return self.nums[self.position]

    def hasNext(self) -> bool:
        # If the next position is a valid index of nums, return True.
        return self.position + 1 < len(self.nums)

# V1'
# IDEA : Two Pointers
# https://leetcode.com/problems/flatten-2d-vector/solution/
class Vector2D:

    def __init__(self, v: List[List[int]]):
        self.vector = v
        self.inner = 0
        self.outer = 0

    # If the current outer and inner point to an integer, this method does nothing.
    # Otherwise, inner and outer are advanced until they point to an integer.
    # If there are no more integers, then outer will be equal to vector.length
    # when this method terminates.
    def advance_to_next(self):
        # While outer is still within the vector, but inner is over the
        # end of the inner list pointed to by outer, we want to move
        # forward to the start of the next inner vector.
        while self.outer < len(self.vector) and self.inner == len(self.vector[self.outer]):
            self.outer += 1
            self.inner = 0

    def next(self) -> int:
        # Ensure the position pointers are moved such they point to an integer,
        # or put outer = vector.length.
        self.advance_to_next()
        # Return current element and move inner so that is after the current
        # element.
        result = self.vector[self.outer][self.inner]
        self.inner += 1
        return result


    def hasNext(self) -> bool:
        # Ensure the position pointers are moved such they point to an integer,
        # or put outer = vector.length.
        self.advance_to_next()
        # If outer = vector.length then there are no integers left, otherwise
        # we've stopped at an integer and so there's an integer left.
        return self.outer < len(self.vector)

# V1'
# https://www.jiuzhang.com/solution/flatten-2d-vector/#tag-highlight-lang-python
class Vector2D(object):

    # @param vec2d {List[List[int]]}
    def __init__(self, vec2d):
        # Initialize your data structure here
        self.row, self.col, self.vec2d = 0, 0, vec2d
        
    # @return {int} a next element
    def next(self):
        # Write your code here
        self.col += 1
        return self.vec2d[self.row][self.col - 1]
        
    # @return {boolean} true if it has next element
    # or false
    def hasNext(self):
        # Write your code here
        while self.row < len(self.vec2d) and \
            self.col >= len(self.vec2d[self.row]):
            self.row, self.col = self.row + 1, 0
        return self.row < len(self.vec2d)

# V1''
# https://www.jiuzhang.com/solution/flatten-2d-vector/#tag-highlight-lang-python
class Vector2D(object):

    # @param vec2d {List[List[int]]}
    def __init__(self, vec2d):
        self.vec2d = vec2d
        self.row, self.col = 0, -1
        self.next_elem = None

    # @return {int} a next element
    def next(self):
        if self.next_elem is None:
            self.hasNext()
            
        temp, self.next_elem = self.next_elem, None
        return temp

    # @return {boolean} true if it has next element
    # or false
    def hasNext(self):
        if self.next_elem:
            return True
        
        self.col += 1
        while self.row < len(self.vec2d) and self.col >= len(self.vec2d[self.row]):
            self.row += 1
            self.col = 0
            
        if self.row < len(self.vec2d) and self.col < len(self.vec2d[self.row]):
            self.next_elem = self.vec2d[self.row][self.col]
            return True
            
        return False

# V1'''
# https://github.com/criszhou/LeetCode-Python/blob/master/251.%20Flatten%202D%20Vector.py
class Vector2D(object):
    def __init__(self, vec2d):
        """
        Initialize your data structure here.
        :type vec2d: List[List[int]]
        """
        self.vec2d = vec2d
        self.i1 = 0 # outer level index
        self.i2 = 0 # inner level index

        self._moveToValid()

    def _moveToValid(self):
        """
        move i1 and i2 to a valid position, so that self.vec2d[self.i1][self.i2] is valid
        """
        while self.i1 < len(self.vec2d) and self.i2 >= len(self.vec2d[self.i1]):
            self.i1 += 1
            self.i2 = 0

    def next(self):
        """
        :rtype: int
        """
        ret = self.vec2d[self.i1][self.i2]
        self.i2 += 1
        self._moveToValid()

        return ret

    def hasNext(self):
        """
        :rtype: bool
        """
        return self.i1 < len(self.vec2d)

# V1''''
# http://www.voidcn.com/article/p-qxkyrjri-zo.html
class Vector2D(object):

    def __init__(self, vec2d):
        """
        Initialize your data structure here.
        :type vec2d: List[List[int]]
        """
        self.vec = vec2d
        self.row = 0
        self.col = 0
        i = 0
        while self.row != len(self.vec):
            if len(self.vec[self.row]) != 0:
                self.col = 0
                break
            self.row += 1

    def next(self):
        """
        :rtype: int
        """
        ret = self.vec[self.row][self.col]
        self.col += 1
        return ret

    def hasNext(self):
        """
        :rtype: bool
        """
        if self.row == len(self.vec):
            return False
        if self.col != len(self.vec[self.row]):
            return True
        else:
            self.row += 1
            while self.row != len(self.vec):
                if len(self.vec[self.row]) != 0:
                    self.col = 0
                    return True
                self.row += 1
            return False
# Your Vector2D object will be instantiated and called as such:
# i, v = Vector2D(vec2d), []
# while i.hasNext(): v.append(i.next())

# V2 
# Time:  O(1)
# Space: O(1)
from collections import deque
class Vector2D(object):

    def __init__(self, vec2d):
        """
        Initialize your data structure here.
        :type vec2d: List[List[int]]
        """
        #self.stack = deque((len(v), iter(v)) for v in vec2d if v)
        self.stack = deque((len(v), list(iter(v))) for v in vec2d if v) # fix for python 3 

    def next(self):
        """
        :rtype: int
        """
        length, iterator = self.stack.popleft()
        if length > 1:
            self.stack.appendleft((length-1, iterator))
        return next(iterator)

    def hasNext(self):
        """
        :rtype: bool
        """
        return bool(self.stack)
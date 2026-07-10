"""

284. Peeking Iterator
Medium

Design an iterator that supports the peek operation on an existing iterator in addition to the hasNext and the next operations.

Implement the PeekingIterator class:

PeekingIterator(Iterator<int> nums) Initializes the object with the given integer iterator iterator.
int next() Returns the next element in the array and moves the pointer to the next element.
boolean hasNext() Returns true if there are still elements in the array.
int peek() Returns the next element in the array without moving the pointer.
Note: Each language may have a different implementation of the constructor and Iterator, but they all support the int next() and boolean hasNext() functions.

 

Example 1:

Input
["PeekingIterator", "next", "peek", "next", "next", "hasNext"]
[[[1, 2, 3]], [], [], [], [], []]
Output
[null, 1, 2, 2, 3, false]

Explanation
PeekingIterator peekingIterator = new PeekingIterator([1, 2, 3]); // [1,2,3]
peekingIterator.next();    // return 1, the pointer moves to the next element [1,2,3].
peekingIterator.peek();    // return 2, the pointer does not move [1,2,3].
peekingIterator.next();    // return 2, the pointer moves to the next element [1,2,3]
peekingIterator.next();    // return 3, the pointer moves to the next element [1,2,3]
peekingIterator.hasNext(); // return False
 

Constraints:

1 <= nums.length <= 1000
1 <= nums[i] <= 1000
All the calls to next and peek are valid.
At most 1000 calls will be made to next, hasNext, and peek.
 

Follow up: How would you extend your design to be generic and work with all types, not just integer?

"""

# V0

# V1
# https://leetcode.com/problems/peeking-iterator/discuss/123811/Python-solution
class PeekingIterator(object):
    # time = O(1)
    # space = O(1)
    def __init__(self, iterator):
        """
        Initialize your data structure here.
        :type iterator: Iterator
        """
        self.next_val = None
        self.iterator = iterator
        if self.iterator.hasNext():
            self.next_val = iterator.next()

    # time = O(1)
    # space = O(1)
    def peek(self):
        """
        Returns the next element in the iteration without advancing the iterator.
        :rtype: int
        """
        return self.next_val

    # time = O(1)
    # space = O(1)
    def next(self):
        """
        :rtype: int
        """
        cur_val = self.next_val

        if self.iterator.hasNext():
            self.next_val = self.iterator.next()
        else:
            self.next_val = None

        return cur_val


    # time = O(1)
    # space = O(1)
    def hasNext(self):
        """
        :rtype: bool
        """
        return self.next_val is not None

# V1'
# https://leetcode.com/problems/peeking-iterator/discuss/72626/Simple-Python-Solution
class PeekingIterator(object):
    # time = O(1)
    # space = O(1)
    def __init__(self, iterator):
        self.iter = iterator
        self.temp = self.iter.next() if self.iter.hasNext() else None

    # time = O(1)
    # space = O(1)
    def peek(self):
        return self.temp

    # time = O(1)
    # space = O(1)
    def next(self):
        ret = self.temp
        self.temp = self.iter.next() if self.iter.hasNext() else None
        return ret

    # time = O(1)
    # space = O(1)
    def hasNext(self):
        return self.temp is not None

# V1''
# https://leetcode.com/problems/peeking-iterator/discuss/729795/Python-Solution
class PeekingIterator:
    # time = O(1)
    # space = O(1)
    def __init__(self, iterator):
        """
        Initialize your data structure here.
        :type iterator: Iterator
        """
        self.iter = iterator
        self.cache = None

    # time = O(1)
    # space = O(1)
    def peek(self):
        """
        Returns the next element in the iteration without advancing the iterator.
        :rtype: int
        """
        if self.cache is None:
            self.cache = self.iter.next()
        return self.cache

    # time = O(1)
    # space = O(1)
    def next(self):
        """
        :rtype: int
        """
        if self.cache is not None:
            temp = self.cache
            self.cache = None
            return temp
        return self.iter.next()

    # time = O(1)
    # space = O(1)
    def hasNext(self):
        """
        :rtype: bool
        """
        return self.cache is not None or self.iter.hasNext()

# V2
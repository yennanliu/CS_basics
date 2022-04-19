"""

281. Zigzag Iterator
Medium

Given two vectors of integers v1 and v2, implement an iterator to return their elements alternately.

Implement the ZigzagIterator class:

ZigzagIterator(List<int> v1, List<int> v2) initializes the object with the two vectors v1 and v2.
boolean hasNext() returns true if the iterator still has elements, and false otherwise.
int next() returns the current element of the iterator and moves the iterator to the next element.
 

Example 1:

Input: v1 = [1,2], v2 = [3,4,5,6]
Output: [1,3,2,4,5,6]
Explanation: By calling next repeatedly until hasNext returns false, the order of elements returned by next should be: [1,3,2,4,5,6].
Example 2:

Input: v1 = [1], v2 = []
Output: [1]
Example 3:

Input: v1 = [], v2 = [1]
Output: [1]
 

Constraints:

0 <= v1.length, v2.length <= 1000
1 <= v1.length + v2.length <= 2000
-231 <= v1[i], v2[i] <= 231 - 1
 

Follow up: What if you are given k vectors? How well can your code be extended to such cases?

Clarification for the follow-up question:

The "Zigzag" order is not clearly defined and is ambiguous for k > 2 cases. If "Zigzag" does not look right to you, replace "Zigzag" with "Cyclic".

Follow-up Example:

Input: v1 = [1,2,3], v2 = [4,5,6,7], v3 = [8,9]
Output: [1,4,8,2,5,9,3,6,7]

"""

# V0 

# V1
# IDEA deque
# https://zhuanlan.zhihu.com/p/157962559
class ZigzagIterator:
    def __init__(self, v1: List[int], v2: List[int]):
        self.q = collections.deque()
        if len(v1) > 0: self.q.append((len(v1), iter(v1)))
        if len(v2) > 0: self.q.append((len(v2), iter(v2)))

    def next(self) -> int:
        size, it = self.q.popleft()
        if size > 1:
            self.q.append((size - 1, it))
        return next(it)
    
    def hasNext(self) -> bool:
        return len(self.q) > 0     

# V1
# IDEA : TWO POINTERS
# https://leetcode.com/problems/zigzag-iterator/solution/
class ZigzagIterator:
    def __init__(self, v1: List[int], v2: List[int]):
        self.vectors = [v1, v2]
        self.p_elem = 0   # pointer to the index of element
        self.p_vec = 0    # pointer to the vector
        # variables for hasNext() function
        self.total_num = len(v1) + len(v2)
        self.output_count = 0

    def next(self) -> int:
        iter_num = 0
        ret = None

        # Iterate over the vectors
        while iter_num < len(self.vectors):
            curr_vec = self.vectors[self.p_vec]
            if self.p_elem < len(curr_vec):
                ret = curr_vec[self.p_elem]

            iter_num += 1
            self.p_vec = (self.p_vec + 1) % len(self.vectors)
            # increment the element pointer once iterating all vectors
            if self.p_vec == 0:
                self.p_elem += 1

            if ret is not None:
                self.output_count += 1
                return ret

        # no more element to output
        raise Exception


    def hasNext(self) -> bool:
        return self.output_count < self.total_num

# V1'
# IDEA : Queue of Pointers
# https://leetcode.com/problems/zigzag-iterator/solution/
class ZigzagIterator:
    def __init__(self, v1: List[int], v2: List[int]):
        self.vectors = [v1, v2]
        self.queue = deque()
        for index, vector in enumerate(self.vectors):
            # <index_of_vector, index_of_element_to_output>
            if len(vector) > 0:
                self.queue.append((index, 0))

    def next(self) -> int:

        if self.queue:
            vec_index, elem_index = self.queue.popleft()
            next_elem_index = elem_index + 1
            if next_elem_index < len(self.vectors[vec_index]):
                # append the pointer for the next round
                # if there are some elements left
                self.queue.append((vec_index, next_elem_index))

            return self.vectors[vec_index][elem_index]

        # no more element to output
        raise Exception

    def hasNext(self) -> bool:
        return len(self.queue) > 0

# V1''
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
            
# V1''''
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

# V1'''''
# https://blog.csdn.net/fuxuemingzhu/article/details/101288956

# V1''''''''
# https://blog.csdn.net/qq_46105170/article/details/108675981

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
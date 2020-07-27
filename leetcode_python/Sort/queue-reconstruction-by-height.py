# V0 
class Solution(object):
    def reconstructQueue(self, people):
        people.sort(key = lambda x : (-x[0], x[1]))
        res = []
        for p in people:
            res.insert(p[1], p)
        return res

# V1
# https://blog.csdn.net/fuxuemingzhu/article/details/68486884
# IDEA : MAKE THE ARRAY IN DESCENDING ORDER, THEN INSERT SMALL ONE BACK 
# IDEA : PROCESS :
# STEP 1) ORDER THE ARRAY IN DESCENDING ORDER 
# STEP 2) LOOP THE ARRAY AND DO THE INSERT OPERATION (BASED ON 1st VALUE)
# IDEA : insert module in python
# http://www.runoob.com/python/att-list-insert.html
# list.insert(index, obj)
# index : the index for inseeting 
# obj   : the object to insert 
# DEMO:
# In [25]: p= [[7,0], [4,4], [7,1], [5,0], [6,1], [5,2]]
# In [26]: p.sort(key = lambda x : (-x[0], x[1]))
# In [27]: p
# Out[27]: [[7, 0], [7, 1], [6, 1], [5, 0], [5, 2], [4, 4]]
# In [28]: p.insert(1, [6,1])
# In [29]: p
# Out[29]: [[7, 0], [6, 1], [7, 1], [6, 1], [5, 0], [5, 2], [4, 4]]
# In [30]: 
class Solution(object):
    def reconstructQueue(self, people):
        """
        :type people: List[List[int]]
        :rtype: List[List[int]]
        """
        people.sort(key = lambda x : (-x[0], x[1]))
        res = []
        for p in people:
            res.insert(p[1], p)
        return res

### Test case : dev

# V2 
# https://leoeatle.github.io/techBlog/2017/01/11/LeetCode-406-Queue-Reconstruction-by-Height/
class Solution(object):
    def reconstructQueue(self, people):
        """
        :type people: List[List[int]]
        :rtype: List[List[int]]
        """
        if not people:
            return []
        queue = []
        for h, k in sorted(people, key=lambda h_k2: (-h_k2[0], h_k2[1])):
            queue.insert(k, [h , k])
        return queue

# V3 
# Time:  O(n * sqrt(n))
# Space: O(n)
class Solution(object):
    def reconstructQueue(self, people):
        """
        :type people: List[List[int]]
        :rtype: List[List[int]]
        """
        people.sort(key=lambda h_k: (-h_k[0], h_k[1]))

        blocks = [[]]
        for p in people:
            index = p[1]

            for i, block in enumerate(blocks):
                if index <= len(block):
                    break
                index -= len(block)
            block.insert(index, p)

            if len(block) * len(block) > len(people):
                blocks.insert(i+1, block[len(block)/2:])
                del block[len(block)/2:]

        return [p for block in blocks for p in block]

# V4 
# Time:  O(n^2)
# Space: O(n)
class Solution2(object):
    def reconstructQueue(self, people):
        """
        :type people: List[List[int]]
        :rtype: List[List[int]]
        """
        people.sort(key=lambda h_k1: (-h_k1[0], h_k1[1]))
        result = []
        for p in people:
            result.insert(p[1], p)
        return result
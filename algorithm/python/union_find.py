#---------------------------------------------------------------
# UNION FIND
#---------------------------------------------------------------

# V0
# plz also refer LC 323
# https://github.com/yennanliu/CS_basics/blob/master/leetcode_python/Graph/number-of-connected-components-in-an-undirected-graph.py
class UnionFind:

    def __init__(self, n):
        """
        NOTE !!!
            we ONLY need to init 2 things
                1) n
                2) parent array
        """
        self.n = n
        self.parent = [x for x in range(n)]

    def union(self, x, y):
        #print (">>> union : x = {}, y = {}".format(x, y))
        parentX = self.find(x)
        parentY = self.find(y)
        """
        NOTE this !!!
            -> if parentX == parentY, we DO NOTHING
        """
        if parentX == parentY:
            return
        self.parent[parentX] = parentY
        self.n -= 1 

    def find(self, x):
        while x != self.parent[x]:
            x = self.parent[x]
        return x

    def connected(self, x, y):
        parentX = self.find(x)
        parentY = self.find(y)
        return parentX == parentY

    def count(self):
        return self.n

# V0'
# https://github.com/yennanliu/CS_basics/blob/master/leetcode_java/src/main/java/AlgorithmJava/UnionFind.java
class UnionFind:

    def __init__(self, n):
        self.count = n
        #self.parent = [None] * n
        self.parent = [i for i in range(n)]

        # for i in range(len(n)):
        #     self.parent[i] = i
        #     self.size = 1

    def union(self, p, q):
        rootP = self.find(p)
        rootQ = self.find(q)
        if rootP == rootQ:
            return
        #self.parent[rootQ] = rootP  # this is OK as well
        self.parent[rootP] = rootQ
        self.count -= 1

    def connected(self, p, q):
        rootP = self.find(p)
        rootQ = self.find(q)
        return rootP == rootQ

    # NOTE !!! here we just keep finding its partent via "x = parent[x]", but not call find method recursively
    # find "top parent" of x 
    def find(self, x):
        while self.parent[x] != x:
            x = parent[x]
        # NOTE !!! we return x as final output
        return x

    def count(self):
        return self.count

# V1 
import collections
# modify from https://gist.github.com/DavideCanton/9173142
class Element:
    def __init__(self, parent, rank=0, size=1):
        self.parent = parent
        self.rank = rank
        self.size = size

class UnionFind:
    def __init__(self, size):
        self.el = [Element(i) for i in range(size)]
        self.size = size

    def find(self, x):
        cur = x
        while cur != self.el[cur].parent:
            cur = self.el[cur].parent
        self.el[x].parent = cur
        return cur

    def union(self, x, y):
        if self.el[x].parent != x:
            x = self.find(x)
        if self.el[y].parent != y:
            y = self.find(y)
        if self.el[x].rank > self.el[y].rank:
            self.el[y].parent = x
            self.el[x].size += self.el[y].size
        else:
            self.el[x].parent = y
            self.el[y].size += self.el[x].size
            if self.el[x].rank == self.el[y].rank:
                self.el[y].rank += 1
        self.size -= 1


    def __len__(self):
        return self.size

    def size(self, x):
        return self.el[x].size

    def __iter__(self):
        for i, el in enumerate(self.el):
            if el.parent == i:
                yield i

    def __str__(self):
        return " ".join(map(str, self))

# TEST
# if __name__ == "__main__":
#     u = UnionFind(5)
#     print(u)
#     u.union(0, 3)
#     print(u)
#     print(u.find(0))
#     print(u.find(3))

# V1'
# https://gist.github.com/splovyt/66f64923bce5c6fade8f14a44657ec4c
class UnionFind(object):
    '''UnionFind Python class.'''
    def __init__(self, n):
        assert n > 0, "n must be strictly positive"
        self.n = n
        # every node is it's own parent in the beginning
        self.parent = [i for i in range(n)]
    
    def find(self, i):
        '''Find the parent of an element (e.g. the group it belongs to) and compress paths along the way.'''
        if self.parent[i] != i:
            # path compression on the way to finding the final parent 
            # (i.e. the element with a self loop)
            self.parent[i] = self.find(self.parent[i])
        return self.parent[i]
     
    def is_connected(self, x, y):
        '''Check whether X and Y are connected, i.e. they have the same parent.'''
        if self.find(x) == self.find(y):
            return True
        else:
            return False
    
    def union(self, x, y):
        '''Unite the two elements by uniting their parents.'''
        xparent = self.find(x)
        yparent = self.find(y)
        if xparent != yparent:
            # if these elements are not yet in the same set,
            # we will set the y parent to the x parent
            self.parent[yparent]= xparent
    
    @property
    def disjoint_set_count(self):
        '''Count the amount of disjoint sets.'''
        # For every node, add the parent to the list of all parents.
        # This represents all disjoint sets.
        unique_parents = set([self.find(i) for i in range(self.n)])
        # return the count
        return len(unique_parents)

# V2 
# https://github.com/coells/100days/blob/master/day%2041%20-%20union-find.ipynb
class UnionFind(object):

    def __init__(self, n):
        self.n = n

    def find(self, data, i):
        if i != data[i]:
            data[i] = find(data, data[i])
        return data[i]

    def union(self, data, i, j):
        pi, pj = find(data, i), find(data, j)
        if pi != pj:
            data[pi] = pj

    def connected(self, data, i, j):
        return find(data, i) == find(data, j)

# n = 10
# data = [i for i in range(n)]
# connections = [(0, 1), (1, 2), (0, 9), (5, 6), (6, 4), (5, 9)]
# # union
# for i, j in connections:
#     union(data, i, j)
# # find
# for i in range(n):
#     print('item', i, '-> component', find(data, i))
# item 0 -> component 9
# item 1 -> component 9
# item 2 -> component 9
# item 3 -> component 3
# item 4 -> component 9
# item 5 -> component 9
# item 6 -> component 9
# item 7 -> component 7
# item 8 -> component 8
# item 9 -> component 9
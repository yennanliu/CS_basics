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
# if __name__ == "__main__":
#     u = UnionFind(5)
#     print(u)
#     u.union(0, 3)
#     print(u)
#     print(u.find(0))
#     print(u.find(3))

# V2 
# https://github.com/coells/100days/blob/master/day%2041%20-%20union-find.ipynb
def find(data, i):
    if i != data[i]:
        data[i] = find(data, data[i])
    return data[i]


def union(data, i, j):
    pi, pj = find(data, i), find(data, j)
    if pi != pj:
        data[pi] = pj


def connected(data, i, j):
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

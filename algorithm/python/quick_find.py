#---------------------------------------------------------------
# QUICK FIND (Union-Find, eager)
#---------------------------------------------------------------
#
# Simplest union-find: ids[i] is the component id of element i. Two
# elements are connected iff they share an id. union() rewrites every
# member of one component to the other's id.
#
# Time  : init O(N), union O(N), find/connected O(1)
# Space : O(N)
#
# References:
#   - https://www.coursera.org/learn/algorithms-part1/lecture/EcF3P/quick-find


class QuickFind:
    def __init__(self, size):
        # every element starts in its own component
        self.ids = [i for i in range(size)]

    def union(self, p, q):
        pid = self.ids[p]
        qid = self.ids[q]
        if pid == qid:
            return
        # relabel every element in p's component with q's id
        for i in range(len(self.ids)):
            if self.ids[i] == pid:
                self.ids[i] = qid

    def connected(self, p, q):
        return self.ids[p] == self.ids[q]


if __name__ == "__main__":
    qf = QuickFind(10)
    qf.union(4, 3)
    assert qf.ids == [0, 1, 2, 3, 3, 5, 6, 7, 8, 9]
    qf.union(3, 8)
    assert qf.connected(4, 8)
    assert not qf.connected(2, 8)
    print("Success.")

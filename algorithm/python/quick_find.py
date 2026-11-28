#---------------------------------------------------------------
# QUICK FIND (Union-Find, eager)
#---------------------------------------------------------------
#
# Scope: the simplest union-find, where the lookup is instant and the
#        merge is slow. See union_find.py for the opposite trade-off
#        (quick union) and the optimised version that fixes both.
#
# The problem: N items start out separate. Two operations arrive in
# any order:
#     union(p, q)       put p and q in the same group
#     connected(p, q)   are p and q in the same group?
#
# Quick find keeps one array: ids[i] is the GROUP LABEL of item i.
# Two items are connected exactly when their labels are equal.
#
#     start          ids = [0, 1, 2, 3, 4]      five groups
#     union(4, 3)    ids = [0, 1, 2, 3, 3]      4 takes 3's label
#     union(3, 1)    ids = [0, 1, 2, 1, 1]      EVERY item labelled 3 becomes 1
#
# That last line is the cost: to merge two groups, the whole array has
# to be scanned for members of the first one.
#
# Time  : init O(N), find / connected O(1), union O(N)
# Space : O(N)
#
# N unions therefore cost O(N^2) -- fine for a few hundred items,
# hopeless for a million. That is the motivation for quick union.
#
# References:
#   - https://www.coursera.org/learn/algorithms-part1/lecture/EcF3P/quick-find


class QuickFind:
    """Union-find where ids[i] is the group label of item i."""

    def __init__(self, size):
        # every item starts in a group of its own, labelled with its own index
        self.ids = list(range(size))
        self.count = size            # number of separate groups

    def find(self, p):
        """The group label of p -- one array read."""
        return self.ids[p]

    def connected(self, p, q):
        """True if p and q are in the same group."""
        return self.ids[p] == self.ids[q]

    def union(self, p, q):
        """Merge p's group into q's group."""
        p_label = self.ids[p]
        q_label = self.ids[q]
        if p_label == q_label:
            return                   # already together: nothing to do
        # relabel EVERY member of p's group -- this scan is the O(N)
        for i in range(len(self.ids)):
            if self.ids[i] == p_label:
                self.ids[i] = q_label
        self.count -= 1


if __name__ == "__main__":
    # the trace from the header
    small = QuickFind(5)
    small.union(4, 3)
    assert small.ids == [0, 1, 2, 3, 3]
    small.union(3, 1)
    assert small.ids == [0, 1, 2, 1, 1]          # 3 AND 4 were relabelled
    assert small.count == 3                      # {0}, {2}, {1, 3, 4}

    qf = QuickFind(10)
    assert qf.count == 10 and not qf.connected(4, 3)
    qf.union(4, 3)
    assert qf.ids == [0, 1, 2, 3, 3, 5, 6, 7, 8, 9]
    qf.union(3, 8)
    assert qf.connected(4, 8)                    # 4-3 and 3-8, so 4-8
    assert not qf.connected(2, 8)
    assert qf.find(4) == qf.find(8) == 8

    qf.union(4, 8)                               # already connected: no change
    assert qf.count == 8

    print("Success.")

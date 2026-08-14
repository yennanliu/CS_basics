"""

1286. Iterator for Combination
Medium

Design the CombinationIterator class:

CombinationIterator(string characters, int combinationLength) Initializes the
object with a string characters of sorted distinct lowercase English letters
and a number combinationLength as arguments.

next() Returns the next combination of length combinationLength
in lexicographical order.

hasNext() Returns true if and only if there exists a next combination.


Example 1:

Input
["CombinationIterator", "next", "hasNext", "next", "hasNext", "next", "hasNext"]
[["abc", 2], [], [], [], [], [], []]
Output
[null, "ab", true, "ac", true, "bc", false]

Explanation
CombinationIterator itr = new CombinationIterator("abc", 2);
itr.next();    // return "ab"
itr.hasNext(); // return True
itr.next();    // return "ac"
itr.hasNext(); // return True
itr.next();    // return "bc"
itr.hasNext(); // return False


Constraints:

1 <= combinationLength <= characters.length <= 15
All the characters of characters are unique.
At most 10^4 calls will be made to next and hasNext.
It is guaranteed that all calls of the function next are valid.

"""

# V0
# IDEA: PRE-COMPUTE all combinations via BACKTRACK (DFS)
#
#  -> characters.length <= 15, so there are at most C(15, 7) = 6435 combinations.
#     generate them all up-front in lexicographical order,
#     then next()/hasNext() are just a cursor over that list.
#
# time = O(C(n, k) * k) for __init__, O(1) for next / hasNext
# space = O(C(n, k) * k)
class CombinationIterator(object):
    def __init__(self, characters, combinationLength):
        self.combs = []
        n = len(characters)
        cur = []

        # help func
        def dfs(idx):
            if len(cur) == combinationLength:
                self.combs.append("".join(cur))
                return
            if idx == n:
                return
            """
            NOTE !!!

            `take characters[idx]` BEFORE `skip characters[idx]`
            -> this yields the combinations already in lexicographical order,
               so no sorting is needed
            """
            # take
            cur.append(characters[idx])
            dfs(idx + 1)
            cur.pop()
            # skip
            dfs(idx + 1)

        dfs(0)
        self.idx = 0

    def next(self):
        res = self.combs[self.idx]
        self.idx += 1
        return res

    def hasNext(self):
        return self.idx < len(self.combs)


# V0-1
# IDEA: LAZY next-combination on INDEX POINTERS (O(k) memory)
#
#  -> keep the k chosen indices [0, 1, ..., k-1] and advance them
#     to the lexicographically next combination on each call.
#     this never materializes the whole list.
#
# time = O(k) per next(), O(1) per hasNext()
# space = O(k)
class CombinationIterator(object):
    def __init__(self, characters, combinationLength):
        self.cs = characters
        self.n = len(characters)
        self.k = combinationLength
        self.idxs = list(range(self.k))
        self.done = False

    def next(self):
        res = "".join(self.cs[i] for i in self.idxs)

        """
        NOTE !!!

        find the RIGHT-MOST index that can still be pushed forward.
        idxs[i] can move if idxs[i] < n - k + i
        """
        i = self.k - 1
        while i >= 0 and self.idxs[i] == self.n - self.k + i:
            i -= 1

        if i < 0:
            # `res` was the last combination
            self.done = True
        else:
            self.idxs[i] += 1
            # reset every index on its right to be consecutive
            for j in range(i + 1, self.k):
                self.idxs[j] = self.idxs[j - 1] + 1

        return res

    def hasNext(self):
        return not self.done


# Your CombinationIterator object will be instantiated and called as such:
# obj = CombinationIterator(characters, combinationLength)
# param_1 = obj.next()
# param_2 = obj.hasNext()

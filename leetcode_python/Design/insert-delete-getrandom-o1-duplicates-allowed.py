"""

381. Insert Delete GetRandom O(1) - Duplicates allowed
Hard

RandomizedCollection is a data structure that contains a collection of numbers, possibly
duplicates (i.e., a multiset). It should support inserting and removing specific elements
and also reporting a random element.

Implement the RandomizedCollection class:

- RandomizedCollection() Initializes the empty RandomizedCollection object.
- bool insert(int val) Inserts an item val into the multiset, even if the item is already
  present. Returns true if the item is not present, false otherwise.
- bool remove(int val) Removes an item val from the multiset if present. Returns true if
  the item is present, false otherwise. Note that if val has multiple occurrences in the
  multiset, we only remove one of them.
- int getRandom() Returns a random element from the current multiset of elements. The
  probability of each element being returned is linearly related to the number of the same
  values the multiset contains.

You must implement the functions of the class such that each function works on average
O(1) time complexity.

Note: The test cases are generated such that getRandom will only be called if there is
at least one item in the RandomizedCollection.


Example 1:

Input
["RandomizedCollection", "insert", "insert", "insert", "getRandom", "remove", "getRandom"]
[[], [1], [1], [2], [], [1], []]
Output
[null, true, false, true, 2, true, 1]

Explanation
RandomizedCollection randomizedCollection = new RandomizedCollection();
randomizedCollection.insert(1);   // return true since the collection does not contain 1.
                                  // Inserts 1 into the collection.
randomizedCollection.insert(1);   // return false since the collection contains 1.
                                  // Inserts another 1. Collection now contains [1,1].
randomizedCollection.insert(2);   // return true since the collection does not contain 2.
                                  // Collection now contains [1,1,2].
randomizedCollection.getRandom(); // getRandom should:
                                  // - return 1 with probability 2/3, or
                                  // - return 2 with probability 1/3.
randomizedCollection.remove(1);   // return true since the collection contains 1.
                                  // Collection now contains [1,2].
randomizedCollection.getRandom(); // getRandom should return 1 or 2, both equally likely.


Constraints:

-2^31 <= val <= 2^31 - 1
At most 2 * 10^5 calls in total will be made to insert, remove, and getRandom.
There will be at least one element in the data structure when getRandom is called.

"""

# V0
# IDEA : FLAT LIST (for O(1) random) + HASHMAP val -> SET OF INDICES (for O(1) remove)
#
#  getRandom needs a dense array to sample from, but remove() must be O(1), so we
#  cannot shift elements. Trick: SWAP the removed slot with the LAST element, then pop.
#
#  Because duplicates are allowed, the map value is a SET of indices, not a single index.
#
#  CAREFUL with the ordering of the index-set updates below: they must stay correct
#  even when the element being removed IS the last element (then val == last).
#
# time  = O(1) average for insert / remove / getRandom
# space = O(n)
import random
from collections import defaultdict
class RandomizedCollection(object):

    def __init__(self):
        self.vals = []               # flat list of the values (allows O(1) sampling)
        self.idx = defaultdict(set)  # val -> set of positions inside self.vals

    def insert(self, val):
        """
        :type val: int
        :rtype: bool
        """
        was_absent = len(self.idx[val]) == 0
        self.idx[val].add(len(self.vals))
        self.vals.append(val)
        return was_absent

    def remove(self, val):
        """
        :type val: int
        :rtype: bool
        """
        if not self.idx[val]:
            return False

        i = next(iter(self.idx[val]))  # any occurrence of val
        last = self.vals[-1]

        # move the last element into slot i
        self.vals[i] = last
        self.idx[val].discard(i)
        self.idx[last].add(i)
        # slot len-1 disappears; do this AFTER the add above so the
        # "val == last and i == len-1" case still ends up clean
        self.idx[last].discard(len(self.vals) - 1)

        self.vals.pop()
        return True

    def getRandom(self):
        """
        :rtype: int
        """
        return random.choice(self.vals)


# Your RandomizedCollection object will be instantiated and called as such:
# obj = RandomizedCollection()
# param_1 = obj.insert(val)
# param_2 = obj.remove(val)
# param_3 = obj.getRandom()

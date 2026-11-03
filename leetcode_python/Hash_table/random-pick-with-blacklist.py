"""

710. Random Pick with Blacklist
Hard

You are given an integer n and an array of unique integers blacklist. Design an
algorithm to pick a random integer in the range [0, n - 1] that is not in
blacklist. Any integer that is in the mentioned range and not in blacklist
should be equally likely to be returned.

Optimize your algorithm such that it minimizes the number of calls to the
built-in random function of your language.

Implement the Solution class:

Solution(int n, int[] blacklist) Initializes the object with the integer n and
the blacklisted integers blacklist.
int pick() Returns a random integer in the range [0, n - 1] and not in
blacklist.

Example 1:

Input
["Solution", "pick", "pick", "pick", "pick", "pick", "pick", "pick"]
[[7, [2, 3, 5]], [], [], [], [], [], [], []]

Output
[null, 0, 4, 1, 6, 1, 0, 4]

Explanation
Solution solution = new Solution(7, [2, 3, 5]);
solution.pick(); // return 0, any integer from [0,1,4,6] should be ok. Note
that for every call of pick, 0, 1, 4, and 6 must be equally likely to be
returned (i.e., with probability 1/4).
solution.pick(); // return 4
solution.pick(); // return 1
solution.pick(); // return 6
solution.pick(); // return 1
solution.pick(); // return 0
solution.pick(); // return 4

Constraints:

1 <= n <= 10^9
0 <= blacklist.length <= min(10^5, n - 1)
0 <= blacklist[i] < n
All the values of blacklist are unique.
At most 2 * 10^4 calls will be made to pick.

"""

import random
import bisect

# V0
# IDEA : HASHMAP REMAP - BLACKLISTED LOW NUMBERS -> WHITELISTED HIGH NUMBERS
#
#   There are valid_count = n - len(blacklist) allowed numbers, so draw ONE
#   uniform index in [0, valid_count - 1] and make every index stand for a
#   distinct allowed number.
#
#   Indexes that are not blacklisted stand for themselves. Each blacklisted
#   number below valid_count is a "hole"; the tail [valid_count, n - 1] holds
#   exactly as many allowed numbers as there are holes (both equal the number
#   of blacklisted values below valid_count). So pair each hole with one
#   allowed tail number - a bijection, hence uniform.
#
#   e.g. n = 7, blacklist = [2,3,5] -> valid_count = 4, tail = [4,5,6]
#        holes below 4: 2, 3 ; allowed in tail: 4, 6
#        remap = {2: 4, 3: 6} -> index 0,1,2,3 -> number 0,1,4,6
#
#   (Building the whole allowed list is O(n) = 10^9 memory - not an option;
#    re-rolling until a non-blacklisted number appears has no worst-case bound
#    and breaks "minimise calls to random".)
#
# time = ctor: O(b), pick: O(1), space = O(b)   (b = len(blacklist))
class Solution(object):

    def __init__(self, n, blacklist):
        """
        :type n: int
        :type blacklist: List[int]
        """
        self.valid_count = n - len(blacklist)
        blocked = set(blacklist)

        # remap[hole] = the allowed tail number that index `hole` stands for
        self.remap = {}
        tail_candidate = self.valid_count
        for value in blacklist:
            # a blacklisted value in the tail is never drawn - nothing to remap
            if value >= self.valid_count:
                continue
            # NOTE !!! skip tail numbers that are themselves blacklisted
            while tail_candidate in blocked:
                tail_candidate += 1
            self.remap[value] = tail_candidate
            tail_candidate += 1

    def pick(self):
        """
        :rtype: int
        """
        index = random.randint(0, self.valid_count - 1)
        # a hole is redirected to its partner; every other index is itself
        if index in self.remap:
            return self.remap[index]
        return index


# V1
# IDEA : SORTED BLACKLIST + BINARY SEARCH FOR THE k-TH ALLOWED NUMBER
#
#   Draw k uniform in [0, valid_count - 1] and return the k-th (0-based)
#   allowed number - each k names a distinct allowed number, hence uniform.
#
#   With the blacklist sorted, that answer is k + i, where i = how many
#   blacklisted values lie below it. blacklist[i] - i is how many allowed
#   numbers lie below blacklist[i]; it never decreases, so i = the number of
#   entries with blacklist[i] - i <= k - one bisect_right.
#
#   e.g. blacklist = [2,3,5] -> blacklist[i] - i = [2,2,3]
#        k = 2 -> entries with value <= 2: 2 -> answer = 2 + 2 = 4
#        k = 3 -> entries with value <= 3: 3 -> answer = 3 + 3 = 6
#
# NOTE : O(log b) per pick instead of O(1); no hashmap, and easy to prove.
#
# time = ctor: O(b log b), pick: O(log b), space = O(b)
class Solution2(object):

    def __init__(self, n, blacklist):
        """
        :type n: int
        :type blacklist: List[int]
        """
        self.valid_count = n - len(blacklist)
        sorted_blacklist = sorted(blacklist)

        # allowed_below[i] = how many allowed numbers are smaller than
        # sorted_blacklist[i] (its value minus the i blacklisted ones before it)
        self.allowed_below = []
        for i in range(len(sorted_blacklist)):
            self.allowed_below.append(sorted_blacklist[i] - i)

    def pick(self):
        """
        :rtype: int
        """
        k = random.randint(0, self.valid_count - 1)
        # blacklisted values below the k-th allowed number = entries whose
        # allowed_below <= k (each such entry sits before the answer)
        skipped = bisect.bisect_right(self.allowed_below, k)
        return k + skipped

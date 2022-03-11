"""

1923. Longest Common Subpath
Hard

There is a country of n cities numbered from 0 to n - 1. In this country, there is a road connecting every pair of cities.

There are m friends numbered from 0 to m - 1 who are traveling through the country. Each one of them will take a path consisting of some cities. Each path is represented by an integer array that contains the visited cities in order. The path may contain a city more than once, but the same city will not be listed consecutively.

Given an integer n and a 2D integer array paths where paths[i] is an integer array representing the path of the ith friend, return the length of the longest common subpath that is shared by every friend's path, or 0 if there is no common subpath at all.

A subpath of a path is a contiguous sequence of cities within that path.

 

Example 1:

Input: n = 5, paths = [[0,1,2,3,4],
                       [2,3,4],
                       [4,0,1,2,3]]
Output: 2
Explanation: The longest common subpath is [2,3].
Example 2:

Input: n = 3, paths = [[0],[1],[2]]
Output: 0
Explanation: There is no common subpath shared by the three paths.
Example 3:

Input: n = 5, paths = [[0,1,2,3,4],
                       [4,3,2,1,0]]
Output: 1
Explanation: The possible longest common subpaths are [0], [1], [2], [3], and [4]. All have a length of 1.
 

Constraints:

1 <= n <= 105
m == paths.length
2 <= m <= 105
sum(paths[i].length) <= 105
0 <= paths[i][j] < n
The same city is not listed multiple times consecutively in paths[i].

"""

# V0

# V1
# IDEA : ROLLING HASH + BINARY SEARCH
# https://leetcode.com/problems/longest-common-subpath/discuss/1329957/Python-or-Rolling-hash(Robin_Karp)-andand-Binary-Search
class Solution:
    def longestCommonSubpath(self, n: int, paths: List[List[int]]) -> int:
        #@lru_cache(None)
        def RobinKarp(val):
            set1=set()
            mod=2**40-1
            base=10**5+1
            a=pow(base,val,mod)
            for i,path in enumerate(paths):
                set2=set()
                hashVal=0
                for j in range(len(path)):
                    hashVal=hashVal*base+path[j]
                    if j>=mid:
                        hashVal-=path[j-mid]*a
                    hashVal%=mod
                    if j>=mid-1:
                        set2.add(hashVal)
                if i==0:
                    set1=set2
                else:
                    set1=set2.intersection(set1)
                if i>0 and not set1:
                    return 0
            return len(set1)>0
        l=0
        h=min(len(i) for i in paths)
        while l<h:
            mid=l+(h-l+1)//2
            if RobinKarp(mid):
                l=mid
            else:
                h=mid-1
        return l

# V1'
# IDEA : Rolling Hash + Binary Search
# https://leetcode.com/problems/longest-common-subpath/discuss/1673292/Python-Rolling-Hash-%2B-Binary-Search-oror-O(N*logN)
class Solution(object):
    def longestCommonSubpath(self, n, paths):
        BASE = 2 ** 46 - 1
        N = n + 2 if n % 2 == 1 else n + 1
        POWER = [1] * (max(len(path) for path in paths) + 1)
        for i in range(1, len(POWER)):
            POWER[i] = POWER[i - 1] * N % BASE
            
        prefix_hash = []
        for path in paths:
            ph = [0] * (len(path) + 1)
            for i in range(len(path)):
                ph[i + 1] = (ph[i] * N + path[i]) % BASE
            prefix_hash.append(ph)

        def find(k):  # whether there is a common sub-path of length k
            first = True
            intersection = set()
            for i in range(len(paths)):
                s = set()
                for j in range(k, len(paths[i]) + 1):
                    s.add((prefix_hash[i][j] - prefix_hash[i][j - k] * POWER[k]) % BASE)
                intersection = s if first else intersection & s
                first = False
                if not intersection: return False
            return True

        low, high = 0, min([len(path) for path in paths])
        while low <= high:
            mid = (low + high) // 2
            if find(mid):
                low = mid + 1
            else:
                high = mid - 1
        return high

# V1''
# IDEA : SUFFIX
# https://leetcode.com/problems/longest-common-subpath/discuss/1314843/Suffix-automaton-solution-(multiple-languages)
class State:
    def __init__(self, length, link, nxt):
        self.len = length
        self.link = link
        self.next = {} if nxt is None else dict(nxt)
        self.ans = length
        self.max = 0


class Sam:
    def new_state(self, length, link, nxt=None):
        p = State(length, link, nxt)
        self.container.append(p)
        return p

    def __init__(self, path):
        self.container = []
        last = root = self.new_state(0, None)
        for x in path:
            p = last
            cur = self.new_state(p.len + 1, root)
            while p is not None:
                if x in p.next:
                    q = p.next[x]
                    if q.len == p.len + 1:
                        cur.link = q
                    else:
                        clone = self.new_state(p.len + 1, q.link, q.next)
                        while p is not None and p.next[x] == q:
                            p.next[x] = clone
                            p = p.link
                        cur.link = q.link = clone
                    break
                p.next[x] = cur
                p = p.link
            last = cur

    def visit(self, path):
        for state in self.container:
            state.max = 0
        root = self.container[0]
        p, length = root, 0
        for x in path:
            while True:
                if x in p.next:
                    p = p.next[x]
                    length += 1
                    p.max = max(p.max, length)
                    t = p.link
                    while t.max < t.len:
                        t.max = t.len
                        t = t.link
                    break
                if p.link is None:
                    break
                p = p.link
                length = p.len
        for state in self.container:
            state.ans = min(state.ans, state.max)

    def longestCommonSubpath(self, paths):
        [self.visit(path) for path in paths]
        return max(map(lambda state: state.ans, self.container))


class Solution:
    def longestCommonSubpath(self, n, paths):
        """
        :type n: int
        :type paths: List[List[int]]
        :rtype: int
        """
        if len(paths) == 0:
            return 0
        sam = Sam(reduce(lambda a, b: a if len(a) < len(b) else b, paths))
        return sam.longestCommonSubpath(paths)

# V1'''
# IDEA : Improved rolling hash by fully randomized prime modulus
# https://leetcode.com/problems/longest-common-subpath/discuss/1314723/Improved-rolling-hash-by-fully-randomized-prime-modulus-(*add-math-proof)
class Solution:
    def longestCommonSubpath(self, n, paths) -> int:
        def get_common_subpath_hashes(k):
            """Return hash values of common subpaths of length k, or empty set if none exists"""
            def get_subpath_hashes(path):
                hash, coeff = 0, pow(n, k-1, mod)
                for i in range(len(path)+1):
                    if i < k:
                        hash = (hash*n + path[i]) % mod
                    else:
                        yield hash
                        if i < len(path):
                            hash = ((hash-coeff*path[i-k])*n + path[i]) % mod   
            return reduce(set.intersection, (set(get_subpath_hashes(p)) for p in paths))
        
	    # can be replaced with a pre-computed large prime
        mod = self._generate_large_prime(int(1e18), int(9e18))
        low, high = 1, min(len(p) for p in paths)+1
        while low < high:
            mid = (low+high) // 2
            if get_common_subpath_hashes(mid):
                low = mid + 1
            else:
                high = mid
        return high - 1
    
    def _generate_large_prime(self, lower, upper):
        """Generate a prime between [lower, upper)"""
        def is_prime(n, trials=50):
            def witness(a, n):
                x0 = pow(a, u, n)
                for _ in range(t):
                    x = x0**2 % n
                    if x == 1 and x0 != 1 and x0 != n-1:
                        return True
                    x0 = x
                return True if x0 != 1 else False

            t, u = 0, n-1
            while u%2 == 0:
                t, u = t+1, u>>1
            return not any(witness(randrange(1, n), n) for _ in range(trials))
        return next(r for r in iter(lambda: randrange(lower, upper), None) if is_prime(r))

# V2
"""

756. Pyramid Transition Matrix
Medium

You are stacking blocks to form a pyramid. Each block has a color, which is represented by a single letter. Each row of blocks contains one less block than the row beneath it and is centered on top.

To make the pyramid aesthetically pleasing, there are only specific triangular patterns that are allowed. A triangular pattern consists of a single block stacked on top of two blocks. The patterns are given as a list of three-letter strings allowed, where the first two characters of a pattern represent the left and right bottom blocks respectively, and the third character is the top block.

For example, "ABC" represents a triangular pattern with a 'C' block stacked on top of an 'A' (left) and 'B' (right) block. Note that this is different from "BAC" where 'B' is on the left bottom and 'A' is on the right bottom.

You start with a bottom row of blocks bottom, given as a single string, that you must use as the base of the pyramid.

Given bottom and allowed, return true if you can build the pyramid all the way to the top such that every triangular pattern in the pyramid is in allowed, or false otherwise.

Example 1:

https://assets.leetcode.com/uploads/2021/08/26/pyramid1-grid.jpg

Input: bottom = "BCD", allowed = ["BCC","CDE","CEA","FFF"]
Output: true
Explanation: The allowed triangular patterns are shown on the right.
Starting from the bottom (level 3), we can build "CE" on level 2 and then build "A" on level 1.
There are three triangular patterns in the pyramid, which are "BCC", "CDE", and "CEA". All are allowed.

Example 2:

https://assets.leetcode.com/uploads/2021/08/26/pyramid2-grid.jpg

Input: bottom = "AAAA", allowed = ["AAB","AAC","BCD","BBE","DEF"]
Output: false
Explanation: The allowed triangular patterns are shown on the right.
Starting from the bottom (level 4), there are multiple ways to build level 3, but trying all the possibilites, you will get always stuck before building level 1.

Constraints:

2 <= bottom.length <= 6
0 <= allowed.length <= 216
allowed[i].length == 3
The letters in all input strings are from the set {'A', 'B', 'C', 'D', 'E', 'F'}.
All the values of allowed are unique.

"""

# V0 

# V1
# https://blog.csdn.net/fuxuemingzhu/article/details/82469175
# IDEA : BACKTRACKING
# time = O(a^b), a = len(allowed), b = len(bottom) (no memoization)
# space = O(b), recursion depth
class Solution(object):
    def pyramidTransition(self, bottom, allowed):
        """
        :type bottom: str
        :type allowed: List[str]
        :rtype: bool
        """
        m = collections.defaultdict(list)
        for triples in allowed:
            m[triples[:2]].append(triples[-1])
        return self.helper(bottom, "", m)
        
    def helper(self, curr, above, m):
        if len(curr) == 2 and len(above) == 1:
            return True
        if len(above) == len(curr) - 1:
            return self.helper(above, "", m)
        pos = len(above)
        base = curr[pos : pos+2]
        if base in m:
            for ch in m[base]:
                if self.helper(curr, above + ch, m):
                    return True
        return False
  
# V1'
# https://www.jiuzhang.com/solution/pyramid-transition-matrix/#tag-highlight-lang-python
# IDEA : DFS
# time = O(a^b), a = len(allowed), b = len(bottom) (no memoization)
# space = O(b), recursion depth
class Solution:
    """
    @param bottom: a string
    @param allowed: a list of strings
    @return: return a boolean
    """
    def dfs(self, curr, bottoms, nextlevel):
        if len(curr) == 1:
            if not nextlevel:
                return True
            else:
                return self.dfs(nextlevel, bottoms, '')

        for i in range(len(curr)-1):
            key = curr[i:i+2]
            if key not in bottoms:
                return False
            else:
                for top in bottoms[key]:
                    if self.dfs(curr[1:], bottoms, nextlevel + top):
                        return True
        return False
    
    def pyramidTransition(self, bottom, allowed):
        mydict = {}
        for ele in allowed:
            if ele[:-1] not in mydict:
                mydict[ele[:-1]] = [ele[-1]]
            else:
                mydict[ele[:-1]].append(ele[-1])
        return self.dfs(bottom, mydict, '')

# V2
# time = O((a^(b+1)-a)/(a-1)) = O(a^b), a is the size of allowed,
# b is the length of bottom
# space = O((a^(b+1)-a)/(a-1)) = O(a^b)
class Solution(object):
    def pyramidTransition(self, bottom, allowed):
        """
        :type bottom: str
        :type allowed: List[str]
        :rtype: bool
        """
        def pyramidTransitionHelper(bottom, edges, lookup):
            def dfs(bottom, edges, new_bottom, idx, lookup):
                if idx == len(bottom)-1:
                    return pyramidTransitionHelper("".join(new_bottom), edges, lookup)
                for i in edges[ord(bottom[idx])-ord('A')][ord(bottom[idx+1])-ord('A')]:
                    new_bottom[idx] = chr(i+ord('A'))
                    if dfs(bottom, edges, new_bottom, idx+1, lookup):
                        return True
                return False

            if len(bottom) == 1:
                return True
            if bottom in lookup:
                return False
            lookup.add(bottom)
            for i in range(len(bottom)-1):
                if not edges[ord(bottom[i])-ord('A')][ord(bottom[i+1])-ord('A')]:
                    return False
            new_bottom = ['A']*(len(bottom)-1)
            return dfs(bottom, edges, new_bottom, 0, lookup)

        edges = [[[] for _ in range(7)] for _ in range(7)]
        for s in allowed:
            edges[ord(s[0])-ord('A')][ord(s[1])-ord('A')].append(ord(s[2])-ord('A'))
        return pyramidTransitionHelper(bottom, edges, set())
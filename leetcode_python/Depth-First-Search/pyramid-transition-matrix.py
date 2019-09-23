# V0 

# V1 
# https://blog.csdn.net/fuxuemingzhu/article/details/82469175
# IDEA : BACKTRACKING 
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
# Time:  O((a^(b+1)-a)/(a-1)) = O(a^b) , a is the size of allowed,
# b is the length of bottom
# Space: O((a^(b+1)-a)/(a-1)) = O(a^b)
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
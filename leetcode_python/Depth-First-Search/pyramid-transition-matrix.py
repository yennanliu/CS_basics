# V0 

# V1 
# https://blog.csdn.net/fuxuemingzhu/article/details/82469175
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
        
# V2 
# Time:  O((a^(b+1)-a)/(a-1)) = O(a^b) , a is the size of allowed,
#                                        b is the length of bottom
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
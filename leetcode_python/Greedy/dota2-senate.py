# V0 

# V1 
# https://blog.csdn.net/fuxuemingzhu/article/details/82876061
class Solution(object):
    def predictPartyVictory(self, senate):
        """
        :type senate: str
        :rtype: str
        """
        q_r, q_d = collections.deque(), collections.deque()
        n = len(senate)
        for i, s in enumerate(senate):
            if s == "R":
                q_r.append(i)
            else:
                q_d.append(i)
        while q_r and q_d:
            r = q_r.popleft()
            d = q_d.popleft()
            if r < d:
                q_r.append(r + n)
            else:
                q_d.append(d + n)
        return "Radiant" if q_r else "Dire"
        
# V2 
# Time:  O(n)
# Space: O(n)
import collections
class Solution(object):
    def predictPartyVictory(self, senate):
        """
        :type senate: str
        :rtype: str
        """
        n = len(senate)
        radiant, dire = collections.deque(), collections.deque()
        for i, c in enumerate(senate):
            if c == 'R':
                radiant.append(i)
            else:
                dire.append(i)
        while radiant and dire:
            r_idx, d_idx = radiant.popleft(), dire.popleft()
            if r_idx < d_idx:
                radiant.append(r_idx+n)
            else:
                dire.append(d_idx+n)
        return "Radiant" if len(radiant) > len(dire) else "Dire"

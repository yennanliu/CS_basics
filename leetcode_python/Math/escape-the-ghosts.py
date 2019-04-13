# V1 : dev 

# V2 
# https://blog.csdn.net/fuxuemingzhu/article/details/80480462
class Solution:
    def escapeGhosts(self, ghosts, target):
        """
        :type ghosts: List[List[int]]
        :type target: List[int]
        :rtype: bool
        """
        mht = sum(map(abs, target))
        tx, ty = target
        return not any(abs(gx - tx) + abs(gy - ty) <= mht for gx, gy in ghosts)

# V2'
class Solution:
    def escapeGhosts(self, ghosts, target):
        """
        :type ghosts: List[List[int]]
        :type target: List[int]
        :rtype: bool
        """
        mht = sum( [ abs(i) for i in target ] )
        tx, ty = target
        for gx, gy in ghosts:
            if abs(gx - tx) + abs(gy - ty) <= mht:
                return False 
        return True 


# V3 
# Time:  O(n)
# Space: O(1)
class Solution(object):
    def escapeGhosts(self, ghosts, target):
        """
        :type ghosts: List[List[int]]
        :type target: List[int]
        :rtype: bool
        """
        total = abs(target[0])+abs(target[1])
        return all(total < abs(target[0]-i)+abs(target[1]-j) for i, j in ghosts)

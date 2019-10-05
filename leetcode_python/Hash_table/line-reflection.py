# V0 

# V1 
# https://www.jiuzhang.com/solution/line-reflection/#tag-highlight-lang-python
class Solution:
    """
    @param points: n points on a 2D plane
    @return: if there is such a line parallel to y-axis that reflect the given points
    """
    def isReflected(self, points):
        # Write your code here
        if not points:
            return True
        s = max(points, key = lambda x:x[0])[0] + min(points, key = lambda x:x[0])[0]
        m = s / 2.0
        right = set()
        reflected = set()
        for x,y in points:
            if x > m:
                right.add((x, y))
            elif x < m:
                reflected.add((s - x, y))
        return right == reflected

# V1'
# https://blog.csdn.net/qq508618087/article/details/51756981
# JAVA 
# class Solution {
# public:
#     bool isReflected(vector<pair<int, int>>& points) {
#         set<pair<int, int>> st;
#         int Min = INT_MAX, Max = INT_MIN;
#         for(auto val: points)
#         {
#             Min = min(Min, val.first), Max = max(Max, val.first);
#             st.insert(val);
#         }
#         int sum = Min + Max;
#         for(auto val: points)
#             if(st.count(make_pair(sum-val.first, val.second))==0) return false;
#         return true;
#     }
# }; 

# V2 
# Time:  O(n)
# Space: O(n)
import collections
# Hash solution.
class Solution(object):
    def isReflected(self, points):
        """
        :type points: List[List[int]]
        :rtype: bool
        """
        if not points:
            return True
        groups_by_y = collections.defaultdict(set)
        left, right = float("inf"), float("-inf")
        for p in points:
            groups_by_y[p[1]].add(p[0])
            left, right = min(left, p[0]), max(right, p[0])
        mid = left + right
        for group in groups_by_y.values():
            for x in group:
                if mid - x not in group:
                    return False
        return True

# Time:  O(nlogn)
# Space: O(n)
# Two pointers solution.
class Solution2(object):
    def isReflected(self, points):
        """
        :type points: List[List[int]]
        :rtype: bool
        """
        if not points:
            return True
        points.sort()
        # Space: O(n)
        points[len(points)/2:] = sorted(points[len(points)/2:], \
                                        lambda x, y: y[1] - x[1] if x[0] == y[0] else \
                                                     x[0] - y[0])
        mid = points[0][0] + points[-1][0]
        left, right = 0, len(points) - 1
        while left <= right:
            if (mid != points[left][0] + points[right][0]) or \
               (points[left][0] != points[right][0] and \
                points[left][1] != points[right][1]):
                return False
            left += 1
            right -= 1
        return True
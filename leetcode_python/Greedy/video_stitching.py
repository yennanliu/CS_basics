"""
1024. Video Stitching
Medium

You are given a series of video clips from a sporting event that lasted T seconds.  These video clips can be overlapping with each other and have varied lengths.

Each video clip clips[i] is an interval: it starts at time clips[i][0] and ends at time clips[i][1].  We can cut these clips into segments freely: for example, a clip [0, 7] can be cut into segments [0, 1] + [1, 3] + [3, 7].

Return the minimum number of clips needed so that we can cut the clips into segments that cover the entire sporting event ([0, T]).  If the task is impossible, return -1.


Example 1:

Input: clips = [[0,2],[4,6],[8,10],[1,9],[1,5],[5,9]], T = 10
Output: 3
Explanation: 
We take the clips [0,2], [8,10], [1,9]; a total of 3 clips.
Then, we can reconstruct the sporting event as follows:
We cut [1,9] into segments [1,2] + [2,8] + [8,9].
Now we have segments [0,2] + [2,8] + [8,10] which cover the sporting event [0, 10].
Example 2:

Input: clips = [[0,1],[1,2]], T = 5
Output: -1
Explanation: 
We can't cover [0,5] with only [0,1] and [0,2].
Example 3:

Input: clips = [[0,1],[6,8],[0,2],[5,6],[0,4],[0,3],[6,7],[1,3],[4,7],[1,4],[2,5],[2,6],[3,4],[4,5],[5,7],[6,9]], T = 9
Output: 3
Explanation: 
We can take clips [0,4], [4,7], and [6,9].
Example 4:

Input: clips = [[0,4],[2,8]], T = 5
Output: 2
Explanation: 
Notice you can have extra video after the event ends.


Note:

1 <= clips.length <= 100
0 <= clips[i][0], clips[i][1] <= 100
0 <= T <= 100
"""


# V0

# V1
# https://www.twblogs.net/a/5caba212bd9eee59d33379d6
class Solution(object):
    def videoStitching(self, clips, T):
        """
        :type clips: List[List[int]]
        :type T: int
        :rtype: int
        """
        count = collections.defaultdict(list)
        for cl in clips:
            if cl[0] in count:
                if cl[1] - cl[0] > count[cl[0]][1] - count[cl[0]][0]:
                    count[cl[0]].pop()
                    count[cl[0]] = cl
            else:
                count[cl[0]] = cl
        if 0 not in count: return -1
        prev = 0
        cur = count[0][1]
        next = cur
        res = 1
        while cur < T:
            hasFind = False
            for c in range(cur, prev, -1):
                if c in count:
                    if count[c][1] > next:
                        next = count[c][1]
                        prev = c
                        hasFind = True
            if not hasFind:
                return -1
            cur = next
            res += 1
        return res
        
# V1'
# https://www.cnblogs.com/seyjs/p/10673798.html
class Solution(object):
    def videoStitching(self, clips, T):
        """
        :type clips: List[List[int]]
        :type T: int
        :rtype: int
        """
        def cmpf(v1,v2):
            if v1[0] != v2[0]:
                return v1[0] - v2[0]
            return v2[1] - v1[1]
        clips.sort(cmp=cmpf)
        #print clips

        start,end = clips[0][0],clips[0][1]
        clips.pop(0)
        res = 1
        flag = True
        while flag and len(clips) > 0 and end < T :
            maxEnd = end
            flag = False
            while len(clips) > 0:
                if end < clips[0][0]:
                    break
                flag = True
                maxEnd = max(maxEnd,clips.pop(0)[1])
            res += 1
            end = maxEnd
        return res if (start == 0 and end >= T) else -1

# V1''
# https://www.twblogs.net/a/5cabb38ebd9eee5b1a07c655
# JAVA 
# static int _ = [](){std::ios::sync_with_stdio(false); cin.tie(NULL); return 0;}();
# class Solution {
# public:
#     static bool cmp(vector<int>& a, vector<int>& b)
#     {
#         if(a[0] == b[0])
#             return a[1] < b[1];
#         return a[0] < b[0];
#     }
#     static const int MAX = 1000000;

#     int videoStitching(vector<vector<int>>& clips, int T) {
#         sort(clips.begin(), clips.end(), cmp);
#         int count[101];
#         count[0] = 0;
#         for(int i = 1; i < 101; ++i)
#             count[i] = MAX;
#         int size = clips.size();
#         for(int i = 0; i < size; ++i)
#         {
#             count[clips[i][1]] = min(count[clips[i][0]] + 1, count[clips[i][1]]);
#             /* update all points before the end at this step  */
#             for(int j = 1; j < clips[i][1]; ++j)
#                 count[j] = min(count[clips[i][1]], count[j]);
#         }

#         return count[T] == MAX ? -1 : count[T];
#     }
# };

# V2 

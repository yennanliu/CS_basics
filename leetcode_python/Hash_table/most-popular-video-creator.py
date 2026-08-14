"""

2456. Most Popular Video Creator
Medium

You are given two string arrays creators and ids, and an integer array views, all of length n. The ith video on a platform was created by creators[i], has an id of ids[i], and has views[i] views.

The popularity of a creator is the sum of the number of views on all of the creator's videos. Find the creator with the highest popularity and the id of their most viewed video.

If multiple creators have the highest popularity, find all of them.
If multiple videos have the highest view count for a creator, find the lexicographically smallest id.

Note: It is possible for different videos to have the same id, meaning that ids do not uniquely identify a video. For example, two videos with the same ID are considered as distinct videos with their own viewcount.

Return a 2D array of strings answer where answer[i] = [creators_i, id_i] means that creators_i has the highest popularity and id_i is the id of their most popular video. The answer can be returned in any order.


Example 1:

Input: creators = ["alice","bob","alice","chris"], ids = ["one","two","three","four"], views = [5,10,5,4]
Output: [["alice","one"],["bob","two"]]
Explanation:
The popularity of alice is 5 + 5 = 10.
The popularity of bob is 10.
The popularity of chris is 4.
alice and bob are the most popular creators.
For bob, the video with the highest view count is "two".
For alice, the videos with the highest view count are "one" and "three". Since "one" is lexicographically smaller than "three", it is included in the answer.

Example 2:

Input: creators = ["alice","alice","alice"], ids = ["a","b","c"], views = [1,2,2]
Output: [["alice","b"]]
Explanation:
The videos with id "b" and "c" have the highest view count.
Since "b" is lexicographically smaller than "c", it is included in the answer.


Constraints:

n == creators.length == ids.length == views.length
1 <= n <= 10^5
1 <= creators[i].length, ids[i].length <= 5
creators[i] and ids[i] consist only of lowercase English letters.
0 <= views[i] <= 10^5

"""

# V0
# IDEA : TWO PASSES OF BOOKKEEPING PER CREATOR
#
#   pass 1 accumulates, per creator :
#       total   = sum of views
#       best    = (highest single-video views, lexicographically smallest id
#                  achieving it)
#   the tie-break falls out of comparing (views, id) with views descending
#   and the id ascending — done explicitly so the comparison stays readable.
#
#   pass 2 takes the maximum total and emits every creator matching it.
#
# time = O(n), space = O(n)
from collections import defaultdict


class Solution(object):
    def mostPopularCreator(self, creators, ids, views):
        total = defaultdict(int)
        best = {}                  # creator -> (views, id) of their top video

        for creator, vid, view in zip(creators, ids, views):
            total[creator] += view
            if creator not in best:
                best[creator] = (view, vid)
            else:
                bv, bid = best[creator]
                if view > bv or (view == bv and vid < bid):
                    best[creator] = (view, vid)

        top = max(total.values())
        return [[creator, best[creator][1]]
                for creator, t in total.items() if t == top]

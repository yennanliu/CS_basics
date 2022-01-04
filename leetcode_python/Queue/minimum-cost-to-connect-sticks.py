"""

LeetCode 1167. Minimum Cost to Connect Sticks

# https://code.dennyzhang.com/minimum-cost-to-connect-sticks

You have some sticks with positive integer lengths.

You can connect any two sticks of lengths X and Y into one stick by paying a cost of X + Y.  You perform this action until there is one stick remaining.

Return the minimum cost of connecting all the given sticks into one stick in this way.

Example 1:

Input: sticks = [2,4,3]
Output: 14
Explain : merge 2 and 3 -> 5, spent 5, and merge 5 and 4 spend 9. So total cost = 5+9 = 14

Example 2:

Input: sticks = [1,8,3,5]
Output: 30
Constraints:


1 <= sticks.length <= 10^4
1 <= sticks[i] <= 10^4


Find the routine that add the two smallest first, then largest repeated the least times.

Use min heap to get two 2 smallest values, merge them and add merged back to min heap unitl there is only one stick in min heap.

Time Complexity: O(nlogn). n = sticks.length.

Space: O(n).

"""

# V0
# IDEA : heapq
class Solution(object):
    def connectSticks(self, sticks):
        from heapq import * 
        heapify(sticks)
        res = 0
        while len(sticks) > 1:
            s1 = heappop(sticks)
            s2 = heappop(sticks)
            res += s1 + s2 # merge 2 shortest sticks
            heappush(sticks, s1 + s2)
        return res 

# V1
# IDEA : heapq
# https://www.codeleading.com/article/60602004195/
# https://blog.csdn.net/qq_32424059/article/details/100058788
class Solution(object):
    def connectSticks(self, sticks):
        from heapq import * 
        heapify(sticks)
        res = 0
        while len(sticks) > 1:
            s1 = heappop(sticks)
            s2 = heappop(sticks)
            res += s1 + s2 # merge 2 shortest sticks
            heappush(sticks, s1 + s2)
        return res 

# V1
# IDEA : heapq + greedy
# https://www.codeleading.com/article/65392003933/
import heapq
class Solution:
    def connectSticks(self, sticks):
        heapq.heapify(sticks)
        res = 0
        while len(sticks) > 1:
            a, b = heapq.heappop(sticks), heapq.heappop(sticks)
            res += a + b
            heapq.heappush(sticks, a + b)
        return res

# V1
# JAVA
# https://www.codeprj.com/zh/blog/b9c3a11.html
#  class Solution {
#      public int connectSticks(int[] sticks) {
#          if(sticks == null || sticks.length < 2){
#              return 0;
#          }
#        
#          PriorityQueue<Integer> minHeap = new PriorityQueue<>();
#         for(int num : sticks){
#             minHeap.add(num);
#         }
#     
#         int res = 0;
#         while(minHeap.size() > 1){
#             int merge = minHeap.poll() + minHeap.poll();
#             res += merge;
#             minHeap.add(merge);
#         }
#    
#         return res;
#     }
# }

# V1'
# C++
# https://blog.csdn.net/fuxuemingzhu/article/details/101203701


# V2

"""

# http://bookshadow.com/weblog/2017/09/24/leetcode-k-empty-slots/

LeetCode 683. K Empty Slots

There is a garden with N slots. In each slot, there is a flower. The N flowers will bloom one by one in N days. In each day, there will be exactly one flower blooming and it will be in the status of blooming since then.

Given an array flowers consists of number from 1 to N. Each number in the array represents the place where the flower will open in that day.

For example, flowers[i] = x means that the unique flower that blooms at day i will be at position x, where i and x will be in the range from 1 to N.

Also given an integer k, you need to output in which day there exists two flowers in the status of blooming, and also the number of flowers between them is k and these flowers are not blooming.

If there isn't such day, output -1.

Example 1:

Input: 
flowers: [1,3,2]
k: 1
Output: 2
Explanation: In the second day, the first and the third flower have become blooming.
Example 2:

Input: 
flowers: [1,2,3]
k: 1
Output: -1
Note:

The given array will be in the range [1, 20000].

"""

# V0

# V1
# https://github.com/cnkyrpsgl/leetcode/blob/master/solutions/python/683.py
class Node:
    def __init__(self, pos):
        self.pos = pos
        self.left = None
        self.right = None
        
class Solution(object):
    def kEmptySlots(self, flowers, k):
        """
        :type flowers: List[int]
        :type k: int
        :rtype: int
        """
        N = len(flowers)
        ans = -1
        nodes = {0:Node(-float('inf'))}
        for x in range(1, N + 2):
            nodes[x] = Node(x)
            nodes[x].left = nodes[x - 1]
            nodes[x - 1].right = nodes[x]
        nodes[N + 1].pos = float('inf')
        for day in range(N, 0, -1):
            x = flowers[day - 1]
            if nodes[x].pos - nodes[x].left.pos - 1 == k or nodes[x].right.pos - nodes[x].pos - 1 == k:
                ans = day
            nodes[x].left.right = nodes[x].right
            nodes[x].right.left = nodes[x].left
        return ans

# V1'
# IDEA : Fenwick Tree
# http://bookshadow.com/weblog/2017/09/24/leetcode-k-empty-slots/
# Fenwick Tree is a data structure implemented for this problem specifically
class FenwickTree(object):
    def __init__(self, n):
        self.n = n
        self.sums = [0] * (n + 1)
    def add(self, x, val):
        while x <= self.n:
            self.sums[x] += val
            x += self.lowbit(x)
    def lowbit(self, x):
        return x & -x
    def sum(self, x):
        res = 0
        while x > 0:
            res += self.sums[x]
            x -= self.lowbit(x)
        return res

class Solution(object):
    def kEmptySlots(self, flowers, k):
        """
        :type flowers: List[int]
        :type k: int
        :rtype: int
        """
        maxn = max(flowers)
        nums = [0] * (maxn + 1)
        ft = FenwickTree(maxn)
        for i, v in enumerate(flowers):
            ft.add(v, 1)
            nums[v] = 1
            if v >= k and ft.sum(v) - ft.sum(v - k - 2) == 2 and nums[v - k - 1]:
                return i + 1
            if v + k + 1<= maxn and ft.sum(v + k + 1) - ft.sum(v - 1) == 2 and nums[v + k + 1]:
                return i + 1
        return -1

# V1''
# https://leetcode.jp/leetcode-683-k-empty-slots-%E8%A7%A3%E9%A2%98%E6%80%9D%E8%B7%AF%E5%88%86%E6%9E%90/
# JAVA
# public int kEmptySlots(int[] bulbs, int K) {
#     // 将bulbs转为days数组
#     // days[i]代表第i个灯泡点亮的时间
#     int[] days = new int[bulbs.length];
#     for(int i=0;i<bulbs.length;i++){
#         days[bulbs[i]-1]=i+1;
#     }
#     // 返回结果
#     int res=Integer.MAX_VALUE;
#     // 区间左右端点
#     int left=0,right=K+1;
#     for(int i=0;right<days.length;i++){
#         // 如果当前下标等于右端点
#         // 说明当前区间为合理区间
#         if(i==right){
#             // 两端点最大值为合理解，
#             // 利用该解更新全局最小值
#             res=Math.min(res,Math.max(days[left],days[right]));
#             left=i; // 更新左区间为i
#             right=left+K+1; // 更新右区间
#             continue;
#         }
#         // 如果当前下标的值小于两端点的值，说明该区间非法
#         if(days[i]<days[left]||days[i]<days[right]){
#             left=i; // 更新左区间为i
#             right=left+K+1; // 更新右区间
#             continue;
#         }
#     }
#     if(res==Integer.MAX_VALUE) return -1;
#     return res;
# }

# V1'''
# https://www.cnblogs.com/grandyang/p/8415880.html
# C++
# class Solution {
# public:
#     int kEmptySlots(vector<int>& flowers, int k) {
#         int res = INT_MAX, left = 0, right = k + 1, n = flowers.size();
#         vector<int> days(n, 0);
#         for (int i = 0; i < n; ++i) days[flowers[i] - 1] = i + 1;
#         for (int i = 0; right < n; ++i) {
#             if (days[i] < days[left] || days[i] <= days[right]) {
#                 if (i == right) res = min(res, max(days[left], days[right]));
#                 left = i; 
#                 right = k + 1 + i;
#             }
#         }
#         return (res == INT_MAX) ? -1 : res;
#     }
# };

# V1''''
# https://www.cnblogs.com/grandyang/p/8415880.html
# C++
# class Solution {
# public:
#     int kEmptySlots(vector<int>& flowers, int k) {
#         set<int> s;
#         for (int i = 0; i < flowers.size(); ++i) {
#             int cur = flowers[i];
#             auto it = s.upper_bound(cur);
#             if (it != s.end() && *it - cur == k + 1) {
#                 return i + 1;
#             }
#             it = s.lower_bound(cur);
#             if (it != s.begin() && cur - *(--it) == k + 1) {
#                 return i + 1;
#             }
#             s.insert(cur);
#         }
#         return -1;
#     }
# };

# V2
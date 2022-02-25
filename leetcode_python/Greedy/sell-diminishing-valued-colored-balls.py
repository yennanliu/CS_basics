"""

1648. Sell Diminishing-Valued Colored Balls
Medium

You have an inventory of different colored balls, and there is a customer that wants orders balls of any color.

The customer weirdly values the colored balls. Each colored ball's value is the number of balls of that color you currently have in your inventory. For example, if you own 6 yellow balls, the customer would pay 6 for the first yellow ball. After the transaction, there are only 5 yellow balls left, so the next yellow ball is then valued at 5 (i.e., the value of the balls decreases as you sell more to the customer).

You are given an integer array, inventory, where inventory[i] represents the number of balls of the ith color that you initially own. You are also given an integer orders, which represents the total number of balls that the customer wants. You can sell the balls in any order.

Return the maximum total value that you can attain after selling orders colored balls. As the answer may be too large, return it modulo 109 + 7.

 

Example 1:


Input: inventory = [2,5], orders = 4
Output: 14
Explanation: Sell the 1st color 1 time (2) and the 2nd color 3 times (5 + 4 + 3).
The maximum total value is 2 + 5 + 4 + 3 = 14.
Example 2:

Input: inventory = [3,5], orders = 6
Output: 19
Explanation: Sell the 1st color 2 times (3 + 2) and the 2nd color 4 times (5 + 4 + 3 + 2).
The maximum total value is 3 + 2 + 5 + 4 + 3 + 2 = 19.
 

Constraints:

1 <= inventory.length <= 105
1 <= inventory[i] <= 109
1 <= orders <= min(sum(inventory[i]), 109)

"""

# V0

# V0'
# IDEA : max-heap (heap) (TLE)
# TODO : fix below
# from heapq import *
# class Solution(object):
#     def maxProfit(self, inventory, orders):
#         # edge case
#         if not orders:
#             return 0
#         if orders == 1:
#             return inventory[0]
#         if len(inventory) == 1 and orders==inventory[0]:
#             #return 0
#             return int((inventory[0]+inventory[-1])*(len(inventory))*0.5)
#         res = 0
#         _inventory = [ -1 * (x % (10**9 + 7)) for x in inventory]
#         heapify(_inventory)
#         # queue
#         while orders > 0:
#             tmp = heappop(_inventory)
#             res += (-1 * tmp)
#             heappush(_inventory, (1+tmp))
#             orders -= 1
#         return res

# V1''''''
# check its video
# https://zxi.mytechroad.com/blog/greedy/leetcode-1648-sell-diminishing-valued-colored-balls/
# C++
# class Solution {
# public:
#   int maxProfit(vector<int>& inventory, int orders) {
#     constexpr int kMod = 1e9 + 7;
#     const int n = inventory.size();
#     sort(rbegin(inventory), rend(inventory));
#     long cur = inventory[0];
#     long ans = 0;
#     int c = 0;
#     while (orders) {      
#       while (c < n && inventory[c] == cur) ++c;
#       int nxt = c == n ? 0 : inventory[c];      
#       int count = min(static_cast<long>(orders), c * (cur - nxt));
#       int t = cur - nxt;
#       int r = 0;
#       if (orders < c * (cur - nxt)) {
#         t = orders / c;
#         r = orders % c;
#       }
#       ans = (ans + (cur + cur - t + 1) * t / 2 * c + (cur - t) * r) % kMod;
#       orders -= count;
#       cur = nxt;
#     }
#     return ans;
#   }
# };

# V1
# IDEA : GREEDY
# https://leetcode.com/problems/sell-diminishing-valued-colored-balls/discuss/927679/Python-greedy-solution
class Solution(object):
    def maxProfit(self, inventory, orders):
            ans, mod = 0, 10**9+7
            total = sum(inventory) 
            inventory.sort()
            remain = total - orders
            N = len(inventory)
            for i, d in enumerate(inventory):
                each = remain // (N-i)
                if d > each:
                    ans += (each+1+d) * (d-each) // 2
                remain -= min(each, d)
            return ans % mod

# V1'
# IDEA : GREEDY
class Solution(object):
    def maxProfit(self, inventory, orders):
            inventory.sort(reverse=True)
            inventory.append(0)
            profit = 0
            width = 1
            for i in range(len(inventory)-1):
                if inventory[i] > inventory[i+1]:
                    if width * (inventory[i] - inventory[i+1]) < orders:
                        profit += width * self.sumRange(inventory[i+1]+1, inventory[i])
                        orders -= width * (inventory[i] - inventory[i+1])
                    else:
                        whole, remaining = divmod(orders, width)
                        profit += width * self.sumRange(inventory[i]-whole+1, inventory[i])
                        profit += remaining * (inventory[i]-whole)
                        break
                width += 1
            return profit % (10**9 + 7)

    def sumRange(self, lo, hi):
        # inclusive lo and hi
        return (hi * (hi+1)) // 2 - (lo * (lo-1)) // 2

# V1''
# IDEA : SORTING
# https://leetcode.com/problems/sell-diminishing-valued-colored-balls/discuss/927920/python-concise-sort-solution
class Solution(object):
    def maxProfit(self, inventory, orders):
        lis=sorted(inventory,reverse=True)+[0]
        res,cnt=0,1
        for i in range(len(lis)):
            if (lis[i]-lis[i+1])*cnt>=orders:#order will be use up in this layer.
                leng=orders//cnt#orders=cnt*leng+y 
                res+=leng*(lis[i]+lis[i]-leng+1)*cnt//2+(orders-leng*cnt)*(lis[i]-leng)
                return res%(10**9+7)
            else:
                res+=cnt*(lis[i]-lis[i+1])*(lis[i]+lis[i+1]+1)//2
                orders-=cnt*(lis[i]-lis[i+1])
            cnt+=1
            res%=(10**9+7)

# V1'''
# IDEA : BINARY SEARCH
# https://leetcode.com/problems/sell-diminishing-valued-colored-balls/discuss/1306530/Python-Binary-Search-and-Arithmetic-series-formula-680ms
class Solution:
    def maxProfit(self, inventory, orders):
        inventory.sort(reverse=True)
        inventory.append(0)
        p = 0
        for i in range(10**5):
            if inventory[i]>inventory[i+1]:
                if (i+1)*(inventory[i]-inventory[i+1])>=orders:
                    left, right = inventory[i+1]+1, inventory[i]
                    while left<=right:
                        mid = (left+right)//2
                        numBalls = (inventory[i]-mid+1)*(i+1)
                        if 0<=numBalls-orders<i+1:
                            k = numBalls-orders
                            p += ((inventory[i]+mid)*(inventory[i]-mid+1)//2)*(i+1)-(k*mid)
                            return p%1000000007
                        elif numBalls<orders:
                            right = mid-1
                        else:
                            left = mid+1
                else:
                    orders -= (i+1)*(inventory[i]-inventory[i+1])
                    p += ((inventory[i]+inventory[i+1]+1)*(inventory[i]-inventory[i+1])//2)*(i+1)

# V1''''
# IDEA : GREEDY + BINARY SEARCH
# https://leetcode.com/problems/sell-diminishing-valued-colored-balls/discuss/927526/Python-greedy-binary-search
class Solution(object):
    def maxProfit(self, inventory, orders):
		#count how many balls can be sold with this threshold
        def count_balls(x):
            ans = 0
            for e in inventory:
                if e<x:
                    continue
                else:
                    ans += e-x+1
            return ans
        
		#modulo series sum
        def csum(x):
            return ((x*(x+1)//2) % (10**9 + 7))
        
        #T-1 will be the max possible threshold
        left = 0
        right = max(inventory)
        while left<right-1:
            mid = (left+right)>>1
            n = count_balls(mid)
            if n<orders:
                right = mid-1
            else:
                left = mid
        if count_balls(right)>=orders:
            T = right+1
        else:
            T = left+1
        
        #return value
        ret = 0
        
		#first use T and above, these are highest value balls
        upper_count = 0
        for e in inventory:
            if e>=T:
                upper_count += (e-T+1) 
                ret += csum(e)-csum(T-1)
                ret = ret % (10**9+7)
        
		#complete the remainder with value T-1
        lower_count = orders-upper_count
        if lower_count > 0:
            ret += lower_count*(T-1)
            ret = ret % (10**9+7)
                
        return ret

# V1'''''
# IDEA : heapq
# https://leetcode.com/problems/sell-diminishing-valued-colored-balls/discuss/1474074/Python-Solution
import heapq
class MaxHeap:
    def __init__(self):
        self.queue = []
        
    def __len__(self):
        return len(self.queue)
        
    def top(self):
        if len(self.queue)==0:
            return 0
        return self.queue[0] * -1
    
    def push(self, val):
        heapq.heappush(self.queue, -val)
    
    def pop(self):
        return heapq.heappop(self.queue) * -1

class Solution:
    def maxProfit(self, inventory, orders):
        ballCount = 1
        maxHeap = MaxHeap()
        
        for ball in inventory:
            maxHeap.push(ball)
            
        maxBallValue = maxHeap.pop()
        
        while len(maxHeap) and maxHeap.top() == maxBallValue:
            ballCount += 1
            maxHeap.pop()
        
        totalCost = 0
        
        while orders:
            difference = ballCount * (maxBallValue - maxHeap.top())
            
            if difference >= orders:
                rounds = orders // ballCount
                totalCost += ballCount * rounds * (2 * maxBallValue - rounds + 1) // 2
                orders -= rounds * ballCount
                
                if orders > 0:
                    totalCost += orders * (maxBallValue - rounds)
                    
                return totalCost % (10**9 + 7)
            
            else:
                rounds = difference // ballCount
                totalCost += ballCount * rounds * (2 * maxBallValue - rounds + 1) // 2
                maxBallValue = (maxBallValue - rounds)
                orders -= difference

                
            while len(maxHeap) and maxHeap.top() == maxBallValue:
                ballCount += 1
                maxHeap.pop()
                
        return totalCost % (10**9 + 7)

# V1''''''''
# https://www.codeleading.com/article/96184919374/

# V1''''''''''''''
# https://www.krammerliu.com/blog/leetcode-1648-sell-diminishing-valued-colored-balls/

# V2
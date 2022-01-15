"""

134. Gas Station
Medium

There are n gas stations along a circular route, where the amount of gas at the ith station is gas[i].

You have a car with an unlimited gas tank and it costs cost[i] of gas to travel from the ith station to its next (i + 1)th station. You begin the journey with an empty tank at one of the gas stations.

Given two integer arrays gas and cost, return the starting gas station's index if you can travel around the circuit once in the clockwise direction, otherwise return -1. If there exists a solution, it is guaranteed to be unique

 

Example 1:

Input: gas = [1,2,3,4,5], cost = [3,4,5,1,2]
Output: 3
Explanation:
Start at station 3 (index 3) and fill up with 4 unit of gas. Your tank = 0 + 4 = 4
Travel to station 4. Your tank = 4 - 1 + 5 = 8
Travel to station 0. Your tank = 8 - 2 + 1 = 7
Travel to station 1. Your tank = 7 - 3 + 2 = 6
Travel to station 2. Your tank = 6 - 4 + 3 = 5
Travel to station 3. The cost is 5. Your gas is just enough to travel back to station 3.
Therefore, return 3 as the starting index.
Example 2:

Input: gas = [2,3,4], cost = [3,4,3]
Output: -1
Explanation:
You can't start at station 0 or 1, as there is not enough gas to travel to the next station.
Let's start at station 2 and fill up with 4 unit of gas. Your tank = 0 + 4 = 4
Travel to station 0. Your tank = 4 - 3 + 2 = 3
Travel to station 1. Your tank = 3 - 3 + 3 = 3
You cannot travel back to station 2, as it requires 4 unit of gas but you only have 3.
Therefore, you can't travel around the circuit once no matter where you start.
 

Constraints:

gas.length == n
cost.length == n
1 <= n <= 105
0 <= gas[i], cost[i] <= 104

"""

# V0
# IDEA : GREEDY
# IDEA : if sum(gas) - sum(cost) > 0, => THERE MUST BE A SOLUTION
# IDEA : since it's circular (symmetry), we can maintain "total" (e.g. total += gas[i] - cost[i]) of (gas[i], cost[i]) for each index as their "current sum"
class Solution(object):
    def canCompleteCircuit(self, gas, cost):
        start = remain = total = 0
        for i in range(len(gas)):
            remain += gas[i] - cost[i]
            total += gas[i] - cost[i]
            if remain < 0:
                remain = 0
                start = i + 1
        return -1 if total < 0 else start

# V0'
# IDEA : GREEDY
# IDEA : if sum(gas) - sum(cost) > 0, => THERE MUST BE A SOLUTION
class Solution:
    def canCompleteCircuit(self, gas, cost):
        start = 0
        sum_ = 0
        for i in range(len(gas)):
            sum_ += gas[i] - cost[i]
            if sum_ < 0:
                start = i + 1
                sum_ = 0
        return start if sum(gas) - sum(cost) >= 0 else -1 

# V0''
# TODO : fix below
# class Solution(object):
#     def canCompleteCircuit(self, gas, cost):
#         _gas = 2 * gas 
#         _cost = 2 * cost
#         print ("_gas = " + str(_gas))
#         print ("_cost = " + str(_cost) )
#         for i in range(len(gas)):
#             cur = 0
#             cur += _gas[i]
#             j = i + 1
#             while j < len(_gas) + i:
#                 print ("i  = " + str(i) + " j = " + str(j) + " cur = " + str(cur))
#                 cur -= _cost[j-1]
#                 if cur < 0:
#                     break
#                 cur += _gas[j]
#                 print ("j == len(gas) + i - 1 = " + str(j == len(gas) + i - 1))
#                 if j == len(gas) + i - 1 and cur - _cost[j-1] > 0:
#                     return i
#                 j += 1
#         return -1

# V1
# https://www.bbsmax.com/A/WpdKLN9ZzV/
# https://www.hrwhisper.me/leetcode-greedy/
# IDEA : GREEDY
# IDEA 1): 
# -> GO THROUGH ALL gas station, IF CAN FIND ANY OF THE gas station
# -> THAT MAKE sum(gas) > sum(cost)
# -> THEN MEANS THERE MUST BE "AT LEAST 1" SOLUTION THAT FIT THE REQUIREMENT
# => THEN RETURN ANY OF THE gas (FIT THE REQUIREMENT)
# -> RETURN -1 IF sum(gas) < sum(cost) 
# IDEA 2)
# gas[a] – cost[a] >=0 
# gas[a] – cost[a] + gas[b] – cost[b] >=0
# gas[a] – cost[a] + gas[b] – cost[b]  + gas[c] – cost[c] >=0
# gas[a] – cost[a] + gas[b] – cost[b]  + gas[c] – cost[c]  + gas[d] – cost[d] < 0
# -> SO THE CAR CAN'T PASS THE "gas station d", SINCE ITS SUM < 0
class Solution(object):
    def canCompleteCircuit(self, gas, cost):
        begin,subsum,sum,i = 0,0,0,0
        # go through all gas station
        while i < len(gas):
            sum += gas[i] - cost[i]
            subsum += gas[i] - cost[i]
            # if not fit requirement, init again, and start from current  gas station
            if subsum < 0:
                subsum,begin = 0,i + 1
            i += 1
        if sum < 0:
            return -1
        else:
            return begin  

### Test case
s=Solution()
assert s.canCompleteCircuit([1,2,3,4,5],[3,4,5,1,2])==3
assert s.canCompleteCircuit([],[])==0
assert s.canCompleteCircuit([1],[1])==0
assert s.canCompleteCircuit([1,2,3],[1,2,3])==0
assert s.canCompleteCircuit([1,2,3,2,5],[3,4,5,1,2])==-1
assert s.canCompleteCircuit([1,1,1],[1,2,3])==-1
assert s.canCompleteCircuit([1,2,3],[2,1,1])==1

# V1''
# https://leetcode.com/problems/gas-station/discuss/274646/Python-One-Pass-Greedy
# IDEA :
# The heuristic is that if sum(gas) >= sum(cost), there must exists a starting station that enable the circular travel.
# Suppose our circular route stations are {0,...,n} and sum(gas) >= sum(cost). And we define a function route(l,r) as ∑(l<=i<r) gas[i]-cost[i] The valid starting station s is the leftmost station that we can travel from it to n. So route(s,n) > 0. And we just need to prove route(s,n) + route(0,k) > 0 for any k from {0,...,s-1}.
# Since s is the leftmost station that we can travel from it to n, so for any k from {0,...,s-1}, we can not travel from k to n. Or route(k,n) < 0 && route(s,n) > 0 => route(k,s) < 0.
# And as sum(gas) >= sum(cost), route(0,k) + route(k,s) + route(s,n) > 0. Considering route(k,s) < 0, we can conclude that route(0,k) + route(s,n) > 0.
# So based on this, we just need to find out the leftmost station that we can travel from it to n if sum(gas) >= sum(cost).
class Solution(object):
    def canCompleteCircuit(self, gas, cost):
        if sum(gas) < sum(cost): 
            return -1
        n, start, agg = len(gas), 0, 0
        for i in range(n):
            agg += gas[i] - cost[i]
            if agg < 0:
                start, agg = i+1, 0
        return start

# V1'''
# https://www.hrwhisper.me/leetcode-greedy/
class Solution(object):
    def canCompleteCircuit(self, gas, cost):
        start = remain = total = 0
        for i in range(len(gas)):
            remain += gas[i] - cost[i]
            total += gas[i] - cost[i]
            if remain < 0:
                remain = 0
                start = i + 1
        return -1 if total < 0 else start


# V1''''
# http://bookshadow.com/weblog/2015/08/06/leetcode-gas-station/
# IDEA : GREEDY
class Solution:
    # @param {integer[]} gas
    # @param {integer[]} cost
    # @return {integer}
    def canCompleteCircuit(self, gas, cost):
        start = sums = 0
        for x in range(len(gas)):
            sums += gas[x] - cost[x]
            if sums < 0:
                start, sums = x + 1, 0
        return start if sum(gas) >= sum(cost) else -1

# V1''''''
# https://leetcode.com/problems/gas-station/discuss/164515/Python-O(N)-solution
class Solution(object):
    def canCompleteCircuit(self, gas, cost):
        N = len(gas)
        res = 0
        currSum = 0
        i = 0
        end = N
        while i < end and end < 2*N:
            if i >= N:
                ind = i%N
            else:
                ind = i
            currSum += gas[ind]-cost[ind]
            rec = currSum
            if currSum < 0:
                currSum = 0
                res = ind+1
                end = res+N
            i += 1
        if rec >= 0:
            return res
        else:
            return -1

# V2 
# Time:  O(n)
# Space: O(1)
class Solution(object):
    # @param gas, a list of integers
    # @param cost, a list of integers
    # @return an integer
    def canCompleteCircuit(self, gas, cost):
        start, total_sum, current_sum = 0, 0, 0
        for i in range(len(gas)):
            diff = gas[i] - cost[i]
            current_sum += diff
            total_sum += diff
            if current_sum < 0:
                start = i + 1
                current_sum = 0
        if total_sum >= 0:
            return start
        return -1
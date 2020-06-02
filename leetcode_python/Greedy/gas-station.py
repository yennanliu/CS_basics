# V0
# IDEA : GREEDY
class Solution:
    def canCompleteCircuit(self, gas, cost):
        start, sum_ = 0, 0 
        for i in range(len(gas)):
            sum_ += gas[i] - cost[i]
            if sum_ < 0:
                start, sum_ = i+1, 0 
        return start if sum(gas) - sum(cost) >= 0 else -1 

# V0' 
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

# V1'
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

# V1''
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
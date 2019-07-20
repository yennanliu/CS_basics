# V0 

# V1 
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
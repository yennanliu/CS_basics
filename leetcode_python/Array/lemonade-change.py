# V0 
class Solution:
    def lemonadeChange(self, bills):
        changes = {5:0, 10:0}
        for bill in bills:
            if bill == 5:
                changes[5] += 1
            elif bill == 10:
                if changes[5] == 0:
                    return False
                else:
                    changes[10] += 1
                    changes[5] -= 1
            elif bill == 20:
                if changes[10] != 0:
                    if changes[5] == 0:
                        return False
                    else:
                        changes[5] -= 1
                        changes[10] -= 1
                else:
                    if changes[5] < 3:
                        return False
                    else:
                        changes[5] -= 3
        return True

# V1 
# https://blog.csdn.net/fuxuemingzhu/article/details/80913955
# IDEA : GREEDY 
# IDEA : THERE ARE ACTUALLY 2 CHANGES : 5, 10
# SO USE GREEDY ITERATE ALL POSSBIBLE CASES 
class Solution:
    def lemonadeChange(self, bills):
        """
        :type bills: List[int]
        :rtype: bool
        """
        changes = {5:0, 10:0}
        for bill in bills:
            if bill == 5:
                changes[5] += 1
            elif bill == 10:
                if changes[5] == 0:
                    return False
                else:
                    changes[10] += 1
                    changes[5] -= 1
            elif bill == 20:
                if changes[10] != 0:
                    if changes[5] == 0:
                        return False
                    else:
                        changes[5] -= 1
                        changes[10] -= 1
                else:
                    if changes[5] < 3:
                        return False
                    else:
                        changes[5] -= 3
        return True

### Test case
s=Solution()
assert s.lemonadeChange([5,5,10]) == True
assert s.lemonadeChange([10,10]) == False
assert s.lemonadeChange([20,20]) == False
assert s.lemonadeChange([5,5,10,10,20]) == False
assert s.lemonadeChange([5,5,20,20,20,20]) == False
assert s.lemonadeChange([]) == True

# V1'
# https://leetcode-cn.com/problems/lemonade-change/solution/lemonade-change-tan-xin-suan-fa-by-jyd/
class Solution:
    def lemonadeChange(self, bills: List[int]) -> bool:
        five, ten = 0, 0
        for b in bills:
            if b == 5:
                five += 1
            elif b == 10:
                if not five: return False
                five -= 1
                ten += 1
            else:
                if ten and five:
                    ten -= 1
                    five -= 1
                elif five > 2:
                    five -= 3
                else:
                    return False
        return True
        
# V2 
# Time:  O(n)
# Space: O(1)
import collections
class Solution(object):
    def lemonadeChange(self, bills):
        """
        :type bills: List[int]
        :rtype: bool
        """
        coins = [20, 10, 5]
        counts = collections.defaultdict(int)
        for bill in bills:
            counts[bill] += 1
            change = bill - coins[-1]
            for coin in coins:
                if change == 0:
                    break
                if change >= coin:
                    count = min(counts[coin], change//coin)
                    counts[coin] -= count
                    change -= coin * count
            if change != 0:
                return False
        return True

class Solution2(object):
    def lemonadeChange(self, bills):
        """
        :type bills: List[int]
        :rtype: bool
        """
        five, ten = 0, 0
        for bill in bills:
            if bill == 5:
                five += 1
            elif bill == 10:
                if not five:
                    return False
                five -= 1
                ten += 1
            else:
                if ten and five:
                    ten -= 1
                    five -= 1
                elif five >= 3:
                    five -= 3
                else:
                    return False
        return True
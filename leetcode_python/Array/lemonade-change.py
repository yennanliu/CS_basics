"""

860. Lemonade Change
Easy

At a lemonade stand, each lemonade costs $5. Customers are standing in a queue to buy from you and order one at a time (in the order specified by bills). Each customer will only buy one lemonade and pay with either a $5, $10, or $20 bill. You must provide the correct change to each customer so that the net transaction is that the customer pays $5.

Note that you do not have any change in hand at first.

Given an integer array bills where bills[i] is the bill the ith customer pays, return true if you can provide every customer with the correct change, or false otherwise.

 

Example 1:

Input: bills = [5,5,5,10,20]
Output: true
Explanation: 
From the first 3 customers, we collect three $5 bills in order.
From the fourth customer, we collect a $10 bill and give back a $5.
From the fifth customer, we give a $10 bill and a $5 bill.
Since all customers got correct change, we output true.
Example 2:

Input: bills = [5,5,10,10,20]
Output: false
Explanation: 
From the first two customers in order, we collect two $5 bills.
For the next two customers in order, we collect a $10 bill and give back a $5 bill.
For the last customer, we can not give the change of $15 back because we only have two $10 bills.
Since not every customer received the correct change, the answer is false.
 

Constraints:

1 <= bills.length <= 105
bills[i] is either 5, 10, or 20.

"""

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
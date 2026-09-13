"""

638. Shopping Offers
Medium

In LeetCode Store, there are n items to sell. Each item has a price. However, there are some special offers, and a special offer consists of one or more different kinds of items with a sale price.

You are given an integer array price where price[i] is the price of the i^th item, and an integer array needs where needs[i] is the number of pieces of the i^th item you want to buy.

You are also given an array special where special[i] is of size n + 1 where special[i][j] is the number of pieces of the j^th item in the i^th offer and special[i][n] (i.e., the last integer in the array) is the price of the i^th offer.

Return the lowest price you have to pay for exactly certain items as given, where you could make optimal use of the special offers. You are not allowed to buy more items than you want, even if that would lower the overall price. You could use any of the special offers as many times as you want.

Example 1:

Input: price = [2,5], special = [[3,0,5],[1,2,10]], needs = [3,2]
Output: 14
Explanation: There are two kinds of items, A and B. Their prices are $2 and $5 respectively.
In special offer 1, you can pay $5 for 3A and 0B
In special offer 2, you can pay $10 for 1A and 2B.
You need to buy 3A and 2B, so you may pay $10 for 1A and 2B (special offer #2), and $4 for 2A.

Example 2:

Input: price = [2,3,4], special = [[1,1,0,4],[2,2,1,9]], needs = [1,2,1]
Output: 11
Explanation: The price of A is $2, and $3 for B, $4 for C.
You may pay $4 for 1A and 1B, and $9 for 2A ,2B and 1C.
You need to buy 1A ,2B and 1C, so you may pay $4 for 1A and 1B (special offer #1), and $3 for 1B, $4 for 1C.
You cannot add more items, though only $9 for 2A ,2B and 1C.

Constraints:

n == price.length == needs.length
1 <= n <= 6
0 <= price[i], needs[i] <= 10
1 <= special.length <= 100
special[i].length == n + 1
0 <= special[i][j] <= 50
The input is generated that at least one of special[i][j] is non-zero for 0 <= j <= n - 1.

"""

# V0 

# V1
# https://blog.csdn.net/fuxuemingzhu/article/details/82504050
# IDEA : DFS 
# time = O(n * 2^n)  # n = len(special); no memoization, brute-force recursion
# space = O(n)  # recursion depth
class Solution(object):
    def shoppingOffers(self, price, special, needs):
        """
        :type price: List[int]
        :type special: List[List[int]]
        :type needs: List[int]
        :rtype: int
        """
        return self.dfs(price, special, needs)
    
    def dfs(self, price, special, needs):
        local_min = self.directPurchase(price, needs)
        for spec in special:
            remains = [needs[j] - spec[j] for j in range(len(needs))]
            if min(remains) >= 0:
                local_min = min(local_min, spec[-1] + self.dfs(price, special, remains))
        return local_min
        
    def directPurchase(self, price, needs):
        total = 0
        for i, need in enumerate(needs):
            total += price[i] * need
        return total

# V1'
# https://blog.csdn.net/fuxuemingzhu/article/details/82504050
# IDEA : DFS 
# time = O(n * 2^n)  # n = len(special); with memoization on `needs` state
# space = O(2^n)  # memo dictionary of needs states
class Solution(object):
    def shoppingOffers(self, price, special, needs):
        """
        :type price: List[int]
        :type special: List[List[int]]
        :type needs: List[int]
        :rtype: int
        """
        return self.dfs(price, special, needs, {})
    
    def dfs(self, price, special, needs, d):
        val = sum(price[i] * needs[i] for i in range(len(needs)))
        for spec in special:
            remains = [needs[j] - spec[j] for j in range(len(needs))]
            if min(remains) >= 0:
                val = min(val, d.get(tuple(needs), spec[-1] + self.dfs(price, special, remains, d)))
        d[tuple(needs)] = val
        return val
# V1''
# https://www.jiuzhang.com/solution/shopping-offers/#tag-highlight-lang-python
# IDEA : DP
# time = O(n * 2^n)  # n = len(special); brute-force recursion without memoization
# space = O(n)  # recursion depth
class Solution:
    def shoppingOffers(self, price,special,needs):
        res = 0
        n = len(price)
        for i in range(n):
            res += price[i] * needs[i]
        
        for offer in special:
            isValid = True
            for j in range(n):
                if needs[j] - offer[j] < 0:
                    isValid = False
                needs[j] = needs[j] - offer[j]
            if isValid:
                res = min(res, self.shoppingOffers(price, special, needs) + offer[len(offer) - 1])
            for j in range(n):
                needs[j] += offer[j]
        return res;
        
# V2
# time = O(n * 2^n)
# space = O(n)
class Solution(object):
    def shoppingOffers(self, price, special, needs):
        """
        :type price: List[int]
        :type special: List[List[int]]
        :type needs: List[int]
        :rtype: int
        """
        def shoppingOffersHelper(price, special, needs, i):
            if i == len(special):
                return sum(map(lambda x, y: x*y, price, needs))
            result = shoppingOffersHelper(price, special, needs, i+1)
            for j in range(len(needs)):
                needs[j] -= special[i][j]
            if all(need >= 0 for need in needs):
                result = min(result, special[i][-1] + shoppingOffersHelper(price, special, needs, i))
            for j in range(len(needs)):
                needs[j] += special[i][j]
            return result

        return shoppingOffersHelper(price, special, needs, 0)
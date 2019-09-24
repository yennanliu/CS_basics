# V0 

# V1 
# https://blog.csdn.net/fuxuemingzhu/article/details/82504050
# IDEA : DFS 
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
# Time:  O(n * 2^n)
# Space: O(n)
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
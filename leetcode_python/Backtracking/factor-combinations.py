# V0 

# V1 
# http://blog.ystanzhang.com/2015/10/lc-factor-combinations_5.html
# IDEA :  ITERATION 
def getFactors( n):
    todo, res = [(n, 2, [])], []
    while todo:
        n, i, combi = todo.pop()
        while i * i <= n:
            if n % i == 0:
                res.append(combi + [i, n/i])
                todo.append ([n/i, i, combi+[i]])
            i += 1
    return res
    
# V1'
# http://blog.ystanzhang.com/2015/10/lc-factor-combinations_5.html
# IDEA : RECURSION 
def getFactors( n):
    def factor(n, i, combi, combis):
        while i * i <= n:
            if n % i == 0:
                combis.append (combi + [i, n/i])
                factor(n/i, i, combi+[i], combis)
            i += 1
        return combis
    return factor(n, 2, [], [])

# V2 
# Time:  O(nlogn)
# Space: O(logn)
class Solution(object):
    # @param {integer} n
    # @return {integer[][]}
    def getFactors(self, n):
        result = []
        factors = []
        self.getResult(n, result, factors)
        return result

    def getResult(self, n, result, factors):
        i = 2 if not factors else factors[-1]
        while i <= n / i:
            if n % i == 0:
                factors.append(i)
                factors.append(n / i)
                result.append(list(factors))
                factors.pop()
                self.getResult(n / i, result, factors)
                factors.pop()
            i += 1

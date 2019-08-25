# V0 

# V1 
# https://blog.csdn.net/fuxuemingzhu/article/details/80662840
class Solution(object):
    def splitIntoFibonacci(self, S):
        """
        :type S: str
        :rtype: List[int]
        """
        res = []
        self.dfs(S, [], res)
        return res
        
    def dfs(self, num_str, path, res):
        if len(path) >= 3 and  path[-1] != path[-2] + path[-3]:
            return False
        if not num_str and len(path) >= 3:
            res.extend(path)
            return True
        for i in range(len(num_str)):
            curr = num_str[:i+1]
            if (curr[0] == '0' and len(curr) != 1) or int(curr) >= 2**31:
                continue
            if self.dfs(num_str[i+1:], path + [int(curr)], res):
                return True
        return False
        
# V2
# Time:  O(n^3)
# Space: O(n)
class Solution(object):
    def splitIntoFibonacci(self, S):
        """
        :type S: str
        :rtype: List[int]
        """
        def startswith(S, k, x):
            y = 0
            for i in range(k, len(S)):
                y = 10*y + int(S[i])
                if y == x:
                    return i-k+1
                elif y > x:
                    break
            return 0

        MAX_INT = 2**31-1
        a = 0
        for i in range(len(S)-2):
            a = 10*a + int(S[i])
            b = 0
            for j in range(i+1, len(S)-1):
                b = 10*b + int(S[j])
                fib = [a, b]
                k = j+1
                while k < len(S):
                    if fib[-2] > MAX_INT-fib[-1]:
                        break
                    c = fib[-2]+fib[-1]
                    length = startswith(S, k, c)
                    if length == 0:
                        break
                    fib.append(c)
                    k += length
                else:
                    return fib
                if b == 0:
                    break
            if a == 0:
                break
        return []
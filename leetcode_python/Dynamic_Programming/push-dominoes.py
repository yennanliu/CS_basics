# V0 

# V1
# https://blog.csdn.net/fuxuemingzhu/article/details/82714928
class Solution(object):
    def pushDominoes(self, d):
        """
        :type dominoes: str
        :rtype: str
        """
        d = "L" + d + "R"
        res = []
        l = 0
        for r in range(1, len(d)):
            if d[r] == '.':
                continue
            mid = r - l - 1
            if l:
                res.append(d[l])
            if d[l] == d[r]:
                res.append(d[l] * mid)
            elif d[l] == 'L' and d[r] == 'R':
                res.append('.' * mid)
            else:
                res.append('R' * (mid // 2) + '.' * (mid % 2) + 'L' * (mid // 2))
            l = r
        return "".join(res)
        
# V1'
# https://www.jiuzhang.com/solution/push-dominoes/#tag-highlight-lang-python
class Solution:
    """
    @param dominoes: a string
    @return: a string representing the final state
    """
    def pushDominoes(self, dominoes):
        pushed, n = list(dominoes), len(dominoes)
        i = 0
        while i < n:
            j = i+1
            while j < n and pushed[j] == '.':
                if pushed[i] != 'R' and pushed[j] == 'R':
                    i = j
                j += 1
            if j == n:
                if pushed[i] == 'R':
                    for k in range(i, j):
                        pushed[k] = 'R'
                break
            next_i = j
            if pushed[i] == 'R' and pushed[j] == 'L':
                while i < j:
                    pushed[i], pushed[j] = 'R', 'L'
                    i += 1
                    j -= 1
            elif pushed[i] != 'R' and pushed[j] == 'L':
                for k in range(i, j):
                    pushed[k] = 'L'
            elif pushed[i] == 'R' and pushed[j] != 'L':
                for k in range(i, j):
                    pushed[k] = 'R'
            i = next_i
        return ''.join(pushed)

# V2
# Time:  O(n)
# Space: O(n)
class Solution(object):
    def pushDominoes(self, dominoes):
        """
        :type dominoes: str
        :rtype: str
        """
        force = [0]*len(dominoes)

        f = 0
        for i in range(len(dominoes)):
            if dominoes[i] == 'R':
                f = len(dominoes)
            elif dominoes[i] == 'L':
                f = 0
            else:
                f = max(f-1, 0)
            force[i] += f

        f = 0
        for i in reversed(range(len(dominoes))):
            if dominoes[i] == 'L':
                f = len(dominoes)
            elif dominoes[i] == 'R':
                f = 0
            else:
                f = max(f-1, 0)
            force[i] -= f

        return "".join('.' if f == 0 else 'R' if f > 0 else 'L'
                       for f in force)
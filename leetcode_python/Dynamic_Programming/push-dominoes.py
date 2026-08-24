# V0 

# V1
# https://blog.csdn.net/fuxuemingzhu/article/details/82714928
"""

DP def
    a domino's fate depends only on the NEAREST pushed domino on each side, so
    pad the string as "L" + dominoes + "R" and process each gap between two
    consecutive non-'.' characters independently

    for a gap of `mid` dots between d[l] and d[r]:

DP eq

     d[l] == d[r]           -> all mid dots take that direction

     d[l] == 'L', d[r] == 'R' -> the dots are pulled APART, so all stay '.'

     d[l] == 'R', d[r] == 'L' -> the dots are pushed TOGETHER, splitting evenly:

        'R' * (mid // 2) + '.' * (mid % 2) + 'L' * (mid // 2)


    -> e.g. the sentinels "L" on the left and "R" on the right are exactly
              the "no force from outside" boundary, so no special-casing of
              the ends is needed

     ans = the concatenation of all the resolved gaps

"""
# time = O(n), n = len(dominoes)
# space = O(n)
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
"""

DP def
    a domino's fate depends only on the NEAREST pushed domino on each side, so
    pad the string as "L" + dominoes + "R" and process each gap between two
    consecutive non-'.' characters independently

    for a gap of `mid` dots between d[l] and d[r]:

DP eq

     d[l] == d[r]           -> all mid dots take that direction

     d[l] == 'L', d[r] == 'R' -> the dots are pulled APART, so all stay '.'

     d[l] == 'R', d[r] == 'L' -> the dots are pushed TOGETHER, splitting evenly:

        'R' * (mid // 2) + '.' * (mid % 2) + 'L' * (mid // 2)


    -> e.g. the sentinels "L" on the left and "R" on the right are exactly
              the "no force from outside" boundary, so no special-casing of
              the ends is needed

     ans = the concatenation of all the resolved gaps

"""
# time = O(n), n = len(dominoes)
# space = O(n)
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
"""

DP def
    a domino's fate depends only on the NEAREST pushed domino on each side, so
    pad the string as "L" + dominoes + "R" and process each gap between two
    consecutive non-'.' characters independently

    for a gap of `mid` dots between d[l] and d[r]:

DP eq

     d[l] == d[r]           -> all mid dots take that direction

     d[l] == 'L', d[r] == 'R' -> the dots are pulled APART, so all stay '.'

     d[l] == 'R', d[r] == 'L' -> the dots are pushed TOGETHER, splitting evenly:

        'R' * (mid // 2) + '.' * (mid % 2) + 'L' * (mid // 2)


    -> e.g. the sentinels "L" on the left and "R" on the right are exactly
              the "no force from outside" boundary, so no special-casing of
              the ends is needed

     ans = the concatenation of all the resolved gaps

"""
# time = O(n), n = len(dominoes)
# space = O(n)
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
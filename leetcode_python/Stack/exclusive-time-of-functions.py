# V0

# V1 
# http://bookshadow.com/weblog/2017/07/16/leetcode-exclusive-time-of-functions/
class Solution(object):
    def exclusiveTime(self, n, logs):
        """
        :type n: int
        :type logs: List[str]
        :rtype: List[int]
        """
        ans = [0] * n
        stack = []
        for log in logs:
            fid, soe, tmp = log.split(':')
            fid, tmp = int(fid), int(tmp)
            if soe == 'start':
                if stack:
                    topFid, topTmp = stack[-1]
                    ans[topFid] += tmp - topTmp
                stack.append([fid, tmp])
            else:
                ans[stack[-1][0]] += tmp - stack[-1][1] + 1
                stack.pop()
                if stack: stack[-1][1] = tmp + 1
        return ans

# V2
# Time:  O(n)
# Space: O(n)
class Solution(object):
    def exclusiveTime(self, n, logs):
        """
        :type n: int
        :type logs: List[str]
        :rtype: List[int]
        """
        result = [0] * n
        stk, prev = [], 0
        for log in logs:
            tokens = log.split(":")
            if tokens[1] == "start":
                if stk:
                    result[stk[-1]] += int(tokens[2]) - prev
                stk.append(int(tokens[0]))
                prev = int(tokens[2])
            else:
                result[stk.pop()] += int(tokens[2]) - prev + 1
                prev = int(tokens[2]) + 1
        return result
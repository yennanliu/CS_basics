"""

636. Exclusive Time of Functions
Medium

On a single-threaded CPU, we execute a program containing n functions. Each function has a unique ID between 0 and n-1.

Function calls are stored in a call stack: when a function call starts, its ID is pushed onto the stack, and when a function call ends, its ID is popped off the stack. The function whose ID is at the top of the stack is the current function being executed. Each time a function starts or ends, we write a log with the ID, whether it started or ended, and the timestamp.

You are given a list logs, where logs[i] represents the ith log message formatted as a string "{function_id}:{"start" | "end"}:{timestamp}". For example, "0:start:3" means a function call with function ID 0 started at the beginning of timestamp 3, and "1:end:2" means a function call with function ID 1 ended at the end of timestamp 2. Note that a function can be called multiple times, possibly recursively.

A function's exclusive time is the sum of execution times for all function calls in the program. For example, if a function is called twice, one call executing for 2 time units and another call executing for 1 time unit, the exclusive time is 2 + 1 = 3.

Return the exclusive time of each function in an array, where the value at the ith index represents the exclusive time for the function with ID i.

 

Example 1:


Input: n = 2, logs = ["0:start:0","1:start:2","1:end:5","0:end:6"]
Output: [3,4]
Explanation:
Function 0 starts at the beginning of time 0, then it executes 2 for units of time and reaches the end of time 1.
Function 1 starts at the beginning of time 2, executes for 4 units of time, and ends at the end of time 5.
Function 0 resumes execution at the beginning of time 6 and executes for 1 unit of time.
So function 0 spends 2 + 1 = 3 units of total time executing, and function 1 spends 4 units of total time executing.
Example 2:

Input: n = 1, logs = ["0:start:0","0:start:2","0:end:5","0:start:6","0:end:6","0:end:7"]
Output: [8]
Explanation:
Function 0 starts at the beginning of time 0, executes for 2 units of time, and recursively calls itself.
Function 0 (recursive call) starts at the beginning of time 2 and executes for 4 units of time.
Function 0 (initial call) resumes execution then immediately calls itself again.
Function 0 (2nd recursive call) starts at the beginning of time 6 and executes for 1 unit of time.
Function 0 (initial call) resumes execution at the beginning of time 7 and executes for 1 unit of time.
So function 0 spends 2 + 4 + 1 + 1 = 8 units of total time executing.
Example 3:

Input: n = 2, logs = ["0:start:0","0:start:2","0:end:5","1:start:6","1:end:6","0:end:7"]
Output: [7,1]
Explanation:
Function 0 starts at the beginning of time 0, executes for 2 units of time, and recursively calls itself.
Function 0 (recursive call) starts at the beginning of time 2 and executes for 4 units of time.
Function 0 (initial call) resumes execution then immediately calls function 1.
Function 1 starts at the beginning of time 6, executes 1 unit of time, and ends at the end of time 6.
Function 0 resumes execution at the beginning of time 6 and executes for 2 units of time.
So function 0 spends 2 + 4 + 1 = 7 units of total time executing, and function 1 spends 1 unit of total time executing.
 

Constraints:

1 <= n <= 100
1 <= logs.length <= 500
0 <= function_id < n
0 <= timestamp <= 109
No two start events will happen at the same timestamp.
No two end events will happen at the same timestamp.
Each function has an "end" log for each "start" log.

"""

# V0
# IDEA : STACK 
# IDEA : 
#   STEP 1) init ans = [0] * n, stack = [], DEFINE element [function_id, timestamp]
#   STEP 2) GO THROUGH EVERY log IN logs AND FOLLOW BELOW LOGIC:
#           if "start" 
#               if stack exist, -> calculate time duration 
#              push (append) log into the stack 
#           else
#               calculate time duration 
#               pop the last elment from the stack 
#.              if stack exist
#                   calculate time duration 
class Solution(object):
    def exclusiveTime(self, n, logs):
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

# V1 
# http://bookshadow.com/weblog/2017/07/16/leetcode-exclusive-time-of-functions/
# IDEA : STACK 
# DEMO 
#     ...: n = 2 
#     ...: logs =  ["0:start:0",
#     ...:  "1:start:2",
#     ...:  "1:end:5",
#     ...:  "0:end:6"]
#     ...: s = Solution()
#     ...: r = s.exclusiveTime(n , logs)
#     ...: print (r)
#     ...:  
# log =  0:start:0 stack =  [] ans =  [0, 0]
# log =  1:start:2 stack =  [[0, 0]] ans =  [0, 0]
# log =  1:end:5 stack =  [[0, 0], [1, 2]] ans =  [2, 0]
# log =  0:end:6 stack =  [[0, 6]] ans =  [2, 4]
# [3, 4]
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

# V1'
# https://www.jiuzhang.com/solution/exclusive-time-of-functions/#tag-highlight-lang-python
class Solution:
    def exclusiveTime(self, n, logs):
        stack = []
        result = [0 for i in range(n)]
        last_timestamp = 0
        for str in logs:
            log = str.split(':')
            id, status, timestamp = int(log[0]), log[1], int(log[2])
            if status == 'start':
                if stack:
                    result[stack[-1]] += timestamp - last_timestamp
                stack.append(id)
            else:
                timestamp += 1
                result[stack.pop()] += timestamp - last_timestamp
            last_timestamp = timestamp 
        return result
        
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
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

"""
NOTE !!!


we CAN'T use `hashmap + event sort` for this LC,
It's WRONG !!!

-> 


 1. 核心資料結構誤用（缺乏 Call Stack）：

        - 函式呼叫具備後進先出（LIFO）與巢狀暫停的特性
          （例如 $A$ 呼叫 $B$ 時，$A$ 會被暫停，待 $B$ 執行完畢後 $A$ 才繼續）。


        - 用一般的 Hash Map 做紀錄，無法處理遞迴呼叫或同一個函式被多次呼叫的情況
          （因為 Key 會被覆蓋），也無法得知當前被暫停的父函式是誰。


 2.  對 logs 進行排序破壞了順序：

        - logs 本身就已經按照時間線正確給出。
          對其進行 sort 會破壞原本 start 與 end 的事件配對關係。

"""


# V0
class Solution(object):
    def exclusiveTime(self, n, logs):
        """
        :type n: int
        :type logs: List[str]
        :rtype: List[int]
        """
        pass



# V0-1
# IDEA: STACK
"""
CORE IDEA:

    
    ->


        start:
            先結算上一個 active function
            push 新 function

        end:
            結算目前 function（+1）
            pop

        prev_time:
            記錄「下一段 execution 從哪裡開始」

"""
class Solution(object):

    def exclusiveTime(self, n, logs):
        """
        :type n: int
        :type logs: List[str]
        :rtype: List[int]
        """

        if not logs:
            return [0] * n

        # ans[id] = exclusive execution time
        ans = [0] * n


        """
        NOTE !!!


        stack save `task id`

            -> [func_id_1, func_id_2, ...]
        """
        # Stack stores currently running function IDs
        stack = []

        # Previous timestamp
        prev_time = 0

        for log in logs:

            # Example:
            # "0:start:0"
            # "1:end:5"
            tmp = log.split(":")

            func_id = int(tmp[0])
            status = tmp[1]
            timestamp = int(tmp[2])


            """
            NOTE !!!


            1. 
                ONLY 2 cases

                    - `start` event

                    - `end` event


            2. 

                need to update `exe time` on BOTH event (start, end event)




            3. 
               when `start` envent, append func_id to stack

               when `end` envent, pop func_id from stack
            """

            # ---------------------------------------------
            # Start
            # ---------------------------------------------
            if status == "start":

                # If another function is already running,
                # it runs from prev_time until timestamp - 1.
                if stack:
                    ans[stack[-1]] += timestamp - prev_time

                # New function starts running
                stack.append(func_id)

                # Start timestamp is included
                prev_time = timestamp


            # ---------------------------------------------
            # End
            # ---------------------------------------------
            else:

                # Current function runs from prev_time
                # through timestamp, INCLUDING timestamp.
                ans[stack[-1]] += timestamp - prev_time + 1

                # Current function is finished
                stack.pop()

                # Next timestamp starts after current function ends
                prev_time = timestamp + 1

        return ans


# V0-2
# IDEA : STACK  (gemini)
class Solution(object):

  def exclusiveTime(self, n, logs):
    """:type n: int

    :type logs: List[str]
    :rtype: List[int]
    """
    ans = [0] * n
    stack = []
    prev_time = 0

    for log in logs:
      # 解析 log："fn_id:status:timestamp"
      parts = log.split(':')
      fn_id = int(parts[0])
      status = parts[1]
      timestamp = int(parts[2])

      if status == 'start':
        if stack:
          # 當前棧頂函式被暫停，採計先前的獨佔執行時間
          ans[stack[-1]] += timestamp - prev_time
        stack.append(fn_id)
        prev_time = timestamp
      else:
        # 當前函式結束，採計從 prev_time 到 timestamp (含) 的時間
        top_fn = stack.pop()
        ans[top_fn] += timestamp - prev_time + 1
        # 下一個 CPU 時間點從 timestamp + 1 開始
        prev_time = timestamp + 1

    return ans


# V0-3
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
# time = O(m)  # m = len(logs)
# space = O(n + m)  # ans array O(n) + stack up to O(m)
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
# time = O(m)  # m = len(logs)
# space = O(n + m)
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
# time = O(m)  # m = len(logs)
# space = O(n + m)
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
# time = O(m)  # m = len(logs)
# space = O(n + m)
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

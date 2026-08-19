package LeetCodeJava.Stack;

// https://leetcode.com/problems/exclusive-time-of-functions/

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

/**
 *  636. Exclusive Time of Functions
 *  Medium
 *
 *  On a single-threaded CPU, we execute a program containing n functions, each with a
 *  unique ID between 0 and n-1. Calls are stored in a call stack.
 *
 *  You are given logs, where logs[i] is "{function_id}:{"start" | "end"}:{timestamp}".
 *  "0:start:3" means function 0 started at the BEGINNING of timestamp 3;
 *  "1:end:2" means function 1 ended at the END of timestamp 2.
 *  A function can be called multiple times, possibly recursively.
 *
 *  A function's exclusive time is the sum of execution times of all its calls, not
 *  counting time spent inside functions it called.
 *
 *  Return the exclusive time of each function.
 *
 *  Example 1:
 *  Input: n = 2, logs = ["0:start:0","1:start:2","1:end:5","0:end:6"]
 *  Output: [3,4]
 *
 *  Example 2:
 *  Input: n = 1, logs = ["0:start:0","0:start:2","0:end:5","0:start:6","0:end:6","0:end:7"]
 *  Output: [8]
 *
 *  Constraints:
 *  1 <= n <= 100
 *  1 <= logs.length <= 500
 *  0 <= function_id < n
 *  0 <= timestamp <= 10^9
 *  Each function has an "end" log for each "start" log.
 */
public class ExclusiveTimeOfFunctions {

    // V0
    // IDEA: CALL STACK — keep (id, lastResumedAt) on a stack; a nested "start" pauses the
    //       caller (bank its elapsed time), an "end" closes the callee and resumes the caller
    /**
     * time = O(m)      // m = logs.size()
     * space = O(n + m)
     */
    public int[] exclusiveTime(int n, List<String> logs) {

        int[] res = new int[n];
        if (logs == null || logs.isEmpty()) {
            return res;
        }

        // each entry = {function id, timestamp this call (re)started running at}
        Deque<int[]> stack = new ArrayDeque<>();

        for (String log : logs) {
            String[] parts = log.split(":");
            int id = Integer.parseInt(parts[0]);
            boolean isStart = "start".equals(parts[1]);
            int time = Integer.parseInt(parts[2]);

            if (isStart) {
                if (!stack.isEmpty()) {
                    // the caller ran from its resume point up to (time - 1)
                    int[] top = stack.peek();
                    res[top[0]] += time - top[1];
                }
                stack.push(new int[] { id, time });
            } else {
                // "end:t" means the call occupied the WHOLE of timestamp t -> +1
                int[] top = stack.pop();
                res[top[0]] += time - top[1] + 1;
                if (!stack.isEmpty()) {
                    // the caller resumes at the beginning of time + 1
                    stack.peek()[1] = time + 1;
                }
            }
        }
        return res;
    }
}

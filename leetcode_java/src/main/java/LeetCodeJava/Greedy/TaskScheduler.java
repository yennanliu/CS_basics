package LeetCodeJava.Greedy;

// https://leetcode.com/problems/task-scheduler/
/**
 *
 621. Task Scheduler
 Solved
 Medium
 Topics
 Companies
 Hint
 You are given an array of CPU tasks, each labeled with a letter from A to Z, and a number n. Each CPU interval can be idle or allow the completion of one task. Tasks can be completed in any order, but there's a constraint: there has to be a gap of at least n intervals between two tasks with the same label.

 Return the minimum number of CPU intervals required to complete all tasks.



 Example 1:

 Input: tasks = ["A","A","A","B","B","B"], n = 2

 Output: 8

 Explanation: A possible sequence is: A -> B -> idle -> A -> B -> idle -> A -> B.

 After completing task A, you must wait two intervals before doing A again. The same applies to task B. In the 3rd interval, neither A nor B can be done, so you idle. By the 4th interval, you can do A again as 2 intervals have passed.

 Example 2:

 Input: tasks = ["A","C","A","B","D","B"], n = 1

 Output: 6

 Explanation: A possible sequence is: A -> B -> C -> D -> A -> B.

 With a cooling interval of 1, you can repeat a task after just one other task.

 Example 3:

 Input: tasks = ["A","A","A", "B","B","B"], n = 3

 Output: 10

 Explanation: A possible sequence is: A -> B -> idle -> idle -> A -> B -> idle -> idle -> A -> B.

 There are only two types of tasks, A and B, which need to be separated by 3 intervals. This leads to idling twice between repetitions of these tasks.



 Constraints:

 1 <= tasks.length <= 104
 tasks[i] is an uppercase English letter.
 0 <= n <= 100
 *
 *
 */
//import javafx.util.Pair;

import java.util.*;

public class TaskScheduler {

    // V0
    // IDEA : math
    // https://github.com/yennanliu/CS_basics/blob/master/leetcode_python/Greedy/task-scheduler.py
    // pattern : (most_freq_cnt - 1) * (n + 1) + cnt(most_freq_element_cnt)
    //           and compare above with tasks length
    public int leastInterval(char[] tasks, int n) {

        if (n == 0){
            return tasks.length;
        }

        HashMap<String, Integer> map = new HashMap<>();
        for (char x : tasks){
            String k = String.valueOf(x);
            if (!map.containsKey(k)){
                map.put(k, 1);
            }else{
                Integer cur = map.get(k);
                map.put(k, cur+1);
            }
        }

        // get most freq
        int most_freq_cnt = -1;
        for (Integer x : map.values()){
            most_freq_cnt = Math.max(most_freq_cnt, x);
        }

        // check how many other elements have same count as most freq element
        int num_most = 0;
        for (String k : map.keySet()){
            if (map.get(k).equals(most_freq_cnt)){
                num_most += 1;
            }
        }

        //System.out.println("most_freq_cnt = " + most_freq_cnt + " num_most = " + num_most);

        // beware of it, compare with tasks len since we need to cover all task as well
        return Math.max((most_freq_cnt - 1) * (1 + n) + num_most, tasks.length);
    }

    // V1-1
    // https://neetcode.io/problems/task-scheduling
    // IDEA: BRUTE FORCE
    public int leastInterval_1_1(char[] tasks, int n) {
        int[] count = new int[26];
        for (char task : tasks) {
            count[task - 'A']++;
        }

        List<int[]> arr = new ArrayList<>();
        for (int i = 0; i < 26; i++) {
            if (count[i] > 0) {
                arr.add(new int[]{count[i], i});
            }
        }

        int time = 0;
        List<Integer> processed = new ArrayList<>();
        while (!arr.isEmpty()) {
            int maxi = -1;
            for (int i = 0; i < arr.size(); i++) {
                boolean ok = true;
                for (int j = Math.max(0, time - n); j < time; j++) {
                    if (j < processed.size() && processed.get(j) == arr.get(i)[1]) {
                        ok = false;
                        break;
                    }
                }
                if (!ok) continue;
                if (maxi == -1 || arr.get(maxi)[0] < arr.get(i)[0]) {
                    maxi = i;
                }
            }

            time++;
            int cur = -1;
            if (maxi != -1) {
                cur = arr.get(maxi)[1];
                arr.get(maxi)[0]--;
                if (arr.get(maxi)[0] == 0) {
                    arr.remove(maxi);
                }
            }
            processed.add(cur);
        }
        return time;
    }


    // V1-2
    // https://neetcode.io/problems/task-scheduling
    // IDEA: MAX HEAP
    public int leastInterval_1_2(char[] tasks, int n) {
        int[] count = new int[26];
        for (char task : tasks) {
            count[task - 'A']++;
        }

        PriorityQueue<Integer> maxHeap = new PriorityQueue<>(Collections.reverseOrder());
        for (int cnt : count) {
            if (cnt > 0) {
                maxHeap.add(cnt);
            }
        }

        int time = 0;
        Queue<int[]> q = new LinkedList<>();
        while (!maxHeap.isEmpty() || !q.isEmpty()) {
            time++;

            if (maxHeap.isEmpty()) {
                time = q.peek()[1];
            } else {
                int cnt = maxHeap.poll() - 1;
                if (cnt > 0) {
                    q.add(new int[]{cnt, time + n});
                }
            }

            if (!q.isEmpty() && q.peek()[1] == time) {
                maxHeap.add(q.poll()[0]);
            }
        }

        return time;
    }

    // V1-3
    // https://neetcode.io/problems/task-scheduling
    // IDEA: GREEDY
    public int leastInterval_1_3(char[] tasks, int n) {
        int[] count = new int[26];
        for (char task : tasks) {
            count[task - 'A']++;
        }

        Arrays.sort(count);
        int maxf = count[25];
        int idle = (maxf - 1) * n;

        for (int i = 24; i >= 0; i--) {
            idle -= Math.min(maxf - 1, count[i]);
        }
        return Math.max(0, idle) + tasks.length;
    }


    // V1-4
    // https://neetcode.io/problems/task-scheduling
    // IDEA: MATH
    public int leastInterval_1_4(char[] tasks, int n) {
        int[] count = new int[26];
        for (char task : tasks) {
            count[task - 'A']++;
        }

        int maxf = Arrays.stream(count).max().getAsInt();
        int maxCount = 0;
        for (int i : count) {
            if (i == maxf) {
                maxCount++;
            }
        }

        int time = (maxf - 1) * (n + 1) + maxCount;
        return Math.max(tasks.length, time);
    }


    // V2
    // IDEA : MAX HEAP + DQUEUE
    // https://github.com/neetcode-gh/leetcode/blob/main/java/0621-task-scheduler.java
    // https://www.youtube.com/watch?v=s8p8ukTyA2I
//    public int leastInterval_5(char[] tasks, int n) {
//        if (n == 0) return tasks.length;
//        PriorityQueue<Integer> pq = new PriorityQueue<>((a, b) -> b - a);
//        Queue<Pair<Integer, Integer>> q = new LinkedList<>();
//        int[] arr = new int[26];
//        for (char c : tasks) arr[c - 'A']++;
//        for (int val : arr) if (val > 0) pq.add(val);
//        int time = 0;
//
//        while ((!pq.isEmpty() || !q.isEmpty())) {
//            time++;
//            if (!pq.isEmpty()) {
//                int val = pq.poll();
//                val--;
//                if (val > 0) q.add(new Pair(val, time + n));
//            }
//
//            if (!q.isEmpty() && q.peek().getValue() == time) pq.add(
//                    q.poll().getKey()
//            );
//        }
//        return time;
//    }

    // V3
    // IDEA : Greedy
    // https://leetcode.com/problems/task-scheduler/editorial/
    public int leastInterval_3(char[] tasks, int n) {
        // frequencies of the tasks
        int[] frequencies = new int[26];
        for (int t : tasks) {
            frequencies[t - 'A']++;
        }

        Arrays.sort(frequencies);

        // max frequency
        int f_max = frequencies[25];
        int idle_time = (f_max - 1) * n;

        for (int i = frequencies.length - 2; i >= 0 && idle_time > 0; --i) {
            idle_time -= Math.min(f_max - 1, frequencies[i]);
        }
        idle_time = Math.max(0, idle_time);

        return idle_time + tasks.length;
    }

    // V4
    // IDEA : MATH
    // https://leetcode.com/problems/task-scheduler/editorial/
    public int leastInterval_4(char[] tasks, int n) {
        // frequencies of the tasks
        int[] frequencies = new int[26];
        for (int t : tasks) {
            frequencies[t - 'A']++;
        }

        // max frequency
        int f_max = 0;
        for (int f : frequencies) {
            f_max = Math.max(f_max, f);
        }

        // count the most frequent tasks
        int n_max = 0;
        for (int f : frequencies) {
            if (f == f_max) n_max++;
        }

        return Math.max(tasks.length, (f_max - 1) * (n + 1) + n_max);
    }

}

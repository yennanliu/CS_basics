package LeetCodeJava.Greedy;

// https://leetcode.com/problems/task-scheduler/

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

    // V1
    // IDEA : Greedy
    // https://leetcode.com/problems/task-scheduler/editorial/
    public int leastInterval_2(char[] tasks, int n) {
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


    // V2
    // IDEA : MATH
    // https://leetcode.com/problems/task-scheduler/editorial/
    public int leastInterval_3(char[] tasks, int n) {
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

package LeetCodeJava.Heap;

// https://leetcode.com/problems/single-threaded-cpu/description/

import java.util.Arrays;
import java.util.Comparator;
import java.util.PriorityQueue;

/**
 *
 * 1834. Single-Threaded CPU
 * Medium
 * Topics
 * Companies
 * Hint
 * You are given n​​​​​​ tasks labeled from 0 to n - 1 represented by a 2D integer array tasks, where tasks[i] = [enqueueTimei, processingTimei] means that the i​​​​​​th​​​​ task will be available to process at enqueueTimei and will take processingTimei to finish processing.
 *
 * You have a single-threaded CPU that can process at most one task at a time and will act in the following way:
 *
 * If the CPU is idle and there are no available tasks to process, the CPU remains idle.
 * If the CPU is idle and there are available tasks, the CPU will choose the one with the shortest processing time. If multiple tasks have the same shortest processing time, it will choose the task with the smallest index.
 * Once a task is started, the CPU will process the entire task without stopping.
 * The CPU can finish a task then start a new one instantly.
 * Return the order in which the CPU will process the tasks.
 *
 *
 *
 * Example 1:
 *
 * Input: tasks = [[1,2],[2,4],[3,2],[4,1]]
 * Output: [0,2,3,1]
 * Explanation: The events go as follows:
 * - At time = 1, task 0 is available to process. Available tasks = {0}.
 * - Also at time = 1, the idle CPU starts processing task 0. Available tasks = {}.
 * - At time = 2, task 1 is available to process. Available tasks = {1}.
 * - At time = 3, task 2 is available to process. Available tasks = {1, 2}.
 * - Also at time = 3, the CPU finishes task 0 and starts processing task 2 as it is the shortest. Available tasks = {1}.
 * - At time = 4, task 3 is available to process. Available tasks = {1, 3}.
 * - At time = 5, the CPU finishes task 2 and starts processing task 3 as it is the shortest. Available tasks = {1}.
 * - At time = 6, the CPU finishes task 3 and starts processing task 1. Available tasks = {}.
 * - At time = 10, the CPU finishes task 1 and becomes idle.
 * Example 2:
 *
 * Input: tasks = [[7,10],[7,12],[7,5],[7,4],[7,2]]
 * Output: [4,3,2,0,1]
 * Explanation: The events go as follows:
 * - At time = 7, all the tasks become available. Available tasks = {0,1,2,3,4}.
 * - Also at time = 7, the idle CPU starts processing task 4. Available tasks = {0,1,2,3}.
 * - At time = 9, the CPU finishes task 4 and starts processing task 3. Available tasks = {0,1,2}.
 * - At time = 13, the CPU finishes task 3 and starts processing task 2. Available tasks = {0,1}.
 * - At time = 18, the CPU finishes task 2 and starts processing task 0. Available tasks = {1}.
 * - At time = 28, the CPU finishes task 0 and starts processing task 1. Available tasks = {}.
 * - At time = 40, the CPU finishes task 1 and becomes idle.
 *
 *
 * Constraints:
 *
 * tasks.length == n
 * 1 <= n <= 105
 * 1 <= enqueueTimei, processingTimei <= 109
 *
 *
 */
public class SingleThreadedCPU {

    // V0
    // IDEA: PQ + sort (modified by gpt)
    // https://www.youtube.com/watch?v=RR1n-d4oYqE
    /**
     *  we fine 2 data structure for this problem
     *
     *  1) taskWithIndex: array with info (idx, enqueueTime, processingTime)
     *  2) minHeap: min PQ, sort on (enqueueTime, idx) (increasing order)
     *
     *  (optional)
     *   3) Task class
     *
     */
    public int[] getOrder(int[][] tasks) {
        int n = tasks.length;

        // Add original indices to tasks
        int[][] taskWithIndex = new int[n][3];
        for (int i = 0; i < n; i++) {
            taskWithIndex[i][0] = tasks[i][0]; // enqueueTime
            taskWithIndex[i][1] = tasks[i][1]; // processingTime
            taskWithIndex[i][2] = i; // original index
        }

        /** NOTE !!! we sort task array with enqueue time */
        // Sort by enqueue time
        Arrays.sort(taskWithIndex, Comparator.comparingInt(a -> a[0]));

        /** NOTE !!! use min PQ, (small -> big) (sort by processingTime, then index)  */
        // Min-heap: sort by processingTime, then index
        PriorityQueue<int[]> minHeap = new PriorityQueue<>((a, b) -> {
            if (a[0] != b[0])
                return a[0] - b[0]; // processingTime
            return a[1] - b[1]; // index
        });

        int time = 0;
        int i = 0;
        int[] res = new int[n];
        int idx = 0;

        /**
         *  NOTE !!!! below terminated condition
         *
         *  ->  either PQ is NOT empty or i < n
         */
        while (!minHeap.isEmpty() || i < n) {

            /**
             *  NOTE !!!! below condition
             *
             *  add ALL available tasks to PQ
             */
            // Add all available tasks to heap
            while (i < n && taskWithIndex[i][0] <= time) {
                minHeap.offer(new int[] { taskWithIndex[i][1], taskWithIndex[i][2] }); // [procTime, index]
                i++;
            }

            /**
             *  case 1) PQ is empty
             *
             *  -> `jump clock` to the next task enqueueTime
             */
            if (minHeap.isEmpty()) {
                // CPU idle: jump to next task's enqueueTime
                time = taskWithIndex[i][0];
            }
            /**
             *  case 2) PQ is NOT empty
             *
             *  -> process task, update order, and update time
             */
            else {
                int[] task = minHeap.poll();
                time += task[0]; // advance time
                res[idx++] = task[1]; // add original index to result
            }
        }

        return res;
    }

    // V1
    // https://youtu.be/RR1n-d4oYqE?feature=shared
    //https://github.com/neetcode-gh/leetcode/blob/main/java%2F1834-single-threaded-cpu.java
    public int[] getOrder_1(int[][] tasks) {

        // Sort based on min task processing time or min task index.
        PriorityQueue<int[]> nextTask = new PriorityQueue<int[]>((a, b) -> (a[1] != b[1] ? (a[1] - b[1]) : (a[2] - b[2])));

        // Store task enqueue time, processing time, index.
        int sortedTasks[][] = new int[tasks.length][3];
        for (int i = 0; i < tasks.length; ++i) {
            sortedTasks[i][0] = tasks[i][0];
            sortedTasks[i][1] = tasks[i][1];
            sortedTasks[i][2] = i;
        }

        // Sort the tasks based on enqueueTime
        Arrays.sort(sortedTasks, (a, b) -> Integer.compare(a[0], b[0]));
        int tasksProcessingOrder[] = new int[tasks.length];

        long currTime = 0;
        int taskIndex = 0;
        int ansIndex = 0;

        // Stop when no tasks are left in array and heap.
        while (taskIndex < tasks.length || !nextTask.isEmpty()) {
            if (nextTask.isEmpty() && currTime < sortedTasks[taskIndex][0]) {
                // When the heap is empty, try updating currTime to next task's enqueue time.
                currTime = sortedTasks[taskIndex][0];
            }

            // Push all the tasks whose enqueueTime <= currtTime into the heap.
            while (taskIndex < tasks.length && currTime >= sortedTasks[taskIndex][0]) {
                nextTask.add(sortedTasks[taskIndex]);
                ++taskIndex;
            }

            int[] temp = nextTask.poll();

            // Complete this task and increment currTime.
            currTime += temp[1];
            tasksProcessingOrder[ansIndex++] = temp[2];
        }

        return tasksProcessingOrder;
    }

    // V2
    // https://leetcode.com/problems/single-threaded-cpu/solutions/2965290/simple-java-solution-with-detailed-expla-yfsi/
    // IDEA: PQ
    public int[] getOrder_2(int[][] tasks) {
        int n = tasks.length;
        Task[] arr = new Task[n];
        for (int i = 0; i < n; i++) {
            arr[i] = new Task(i, tasks[i][0], tasks[i][1]);
        }

        Arrays.sort(arr, (a, b) -> {
            return Integer.compare(a.enqueueTime, b.enqueueTime);
        });

        PriorityQueue<Task> p = new PriorityQueue<>((a, b) -> {
            if (a.processingTime == b.processingTime) {
                return Integer.compare(a.idx, b.idx);
            }
            return Integer.compare(a.processingTime, b.processingTime);
        });

        int[] ans = new int[n];
        int ansIdx = 0;
        int taskIdx = 0;
        int curTime = 0;

        while (ansIdx < n) {
            while (taskIdx < n && arr[taskIdx].enqueueTime <= curTime) {
                p.offer(arr[taskIdx++]);
            }
            if (p.isEmpty()) {
                curTime = arr[taskIdx].enqueueTime;
            } else {
                curTime += p.peek().processingTime;
                ans[ansIdx++] = p.poll().idx;
            }
        }
        return ans;
    }

    class Task {
        int idx;
        int enqueueTime;
        int processingTime;

        Task(int idx, int en, int pro) {
            this.idx = idx;
            this.enqueueTime = en;
            this.processingTime = pro;
        }
    }

    // V3
    // https://leetcode.com/problems/single-threaded-cpu/solutions/1164703/java-min-heap-sorting-readable-code-by-h-0mc7/

    // V4
    // https://leetcode.com/problems/single-threaded-cpu/solutions/2965812/best-approach-100-using-sorting-priority-ruh1/
    public int[] getOrder_4(int[][] tasks) {
        int n = tasks.length;
        Triad[] arr = new Triad[n];
        for (int i = 0; i < n; i++) {
            arr[i] = new Triad(tasks[i][0], tasks[i][1], i);
        }
        Arrays.sort(arr, (a, b) -> {
            return a.enqueue_time - b.enqueue_time;
        });

        PriorityQueue<Triad> pq = new PriorityQueue<>(new Comparator<Triad>() {
            public int compare(Triad a, Triad b) {
                if (a.process_time == b.process_time)
                    return a.idx - b.idx;
                return a.process_time - b.process_time;
            }
        });

        int[] ans = new int[n];
        int ansIdx = 0;
        int taskIdx = 0;
        int curTime = 0;

        while (ansIdx < n) {
            while (taskIdx < n && arr[taskIdx].enqueue_time <= curTime) {
                pq.offer(arr[taskIdx++]);
            }
            if (pq.isEmpty()) {
                curTime = arr[taskIdx].enqueue_time;
            } else {
                curTime += pq.peek().process_time;
                ans[ansIdx++] = pq.poll().idx;
            }
        }
        return ans;

    }
}

class Triad {
    int enqueue_time;
    int process_time;
    int idx;

    Triad(int enqueue_time, int process_time, int idx) {
        this.enqueue_time = enqueue_time;
        this.process_time = process_time;
        this.idx = idx;
    }


}

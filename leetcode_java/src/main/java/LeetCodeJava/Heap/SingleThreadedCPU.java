package LeetCodeJava.Heap;

// https://leetcode.com/problems/single-threaded-cpu/description/

import java.util.*;

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
    /**
     *  we fine 2 data structure for this problem
     *
     *  1) taskWithIndex: array with info (idx, enqueueTime, processingTime)
     *  2) minHeap: min PQ, sort on (enqueueTime, idx) (increasing order)
     *
     *  (optional)
     *   3) Task class
     *
     *
     *  Process steps
     *
     *   1)  while ( idx < n)
     *   2)  `push` all ready tasks to PQ
     *   3)  if PQ is `NOT empty`
     *   4)  if PQ is `empty`
     *
     */
    public class Task {
        int idx;
        int enqueueTime;
        int processingTime;

        public Task(int idx, int enqueueTime, int processingTime) {
            this.idx = idx;
            this.enqueueTime = enqueueTime;
            this.processingTime = processingTime;
        }
    }

    /**
     * time = O(N log N)
     * space = O(N)
     */
    public int[] getOrder(int[][] tasks) {
        if (tasks == null || tasks.length == 0)
            return new int[] {};

        int n = tasks.length;
        int[] order = new int[n];

        // Step 1: Preprocess tasks with index
        List<Task> taskList = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            taskList.add(new Task(i, tasks[i][0], tasks[i][1]));
        }

        // Step 2: Sort by enqueueTime
        taskList.sort((a, b) -> a.enqueueTime - b.enqueueTime);

        // Step 3: Min-heap (PQ) for processing tasks
        PriorityQueue<Task> pq = new PriorityQueue<>((a, b) -> {
            if (a.processingTime != b.processingTime) {
                return a.processingTime - b.processingTime;
            } else {
                return a.idx - b.idx;
            }
        });

        int time = 0, i = 0, idx = 0;

        // Step 4: Simulate CPU
        /**
         *  NOTE !!!
         *
         *   the while condition (idx < n)
         *
         */
        while (idx < n) {

            /** NOTE !!! add `ready task` to PQ first */
            // Add all tasks that are ready (enqueueTime <= current time) to PQ
            while (i < n && taskList.get(i).enqueueTime <= time) {
                pq.offer(taskList.get(i));
                i++;
            }

      /** NOTE !!! if PQ is NOT empty
       *
       *
       *   NOTE !!!  don't use `while`, but use `if` condition
       *
       *
       *
       *   🔍 Why not use while (!pq.isEmpty())?
       *
       * Using a while loop would mean you keep processing
       * tasks from the priority queue as long as it’s not empty.
       * That sounds okay at first,
       * -> but it `breaks the logic of how the CPU simulation is supposed to work`.
       *
       * Let’s clarify what happens with each loop iteration:
       *
       * What `if` (!pq.isEmpty()) does:
       * 	•	Processes one task from the heap.
       * 	•	Then loops back to:
       * 	•	Add newly available tasks.
       * 	•	Choose the next best task.
       *
       * What ` while` (!pq.isEmpty()) would do:
       * 	•	Processes all tasks currently in the heap.
       * 	•	Without checking if a newer task (with better processingTime or idx)
       *        should have been added after time has advanced.
       *
       *
       *  -> e.g. use `while` may NOT able to get the task with `next min enqueueTime`
       *        -> since we don't append new task to PQ,
       *           then pop again
       *
       */
      /**
       *
       * 🧪 Example to illustrate the problem
       *
       * Let’s say:
       *
       * int[][] tasks = {
       *     {1, 3},  // Task 0
       *     {2, 2},  // Task 1
       *     {3, 1}   // Task 2
       * };
       *
       * 	•	At time 1, only Task 0 is available → add to PQ → process it.
       * 	•	After processing Task 0 (time = 4), now Task 1 and Task 2 are also available.
       * 	•	PQ is rebuilt → Task 2 (shortest processing time) is selected next.
       *
       * If we used while (!pq.isEmpty()), the loop would:
       * 	•	Process Task 0 (✅ correct)
       * 	•	Then without checking for newly arrived tasks, keep polling from the heap, which still contains only Task 0.
       *
       */
      /**
       *  Dry run example:
       * ⸻
       *
       * 🔢 Input
       *
       * int[][] tasks = {
       *     {1, 2},  // Task 0: enqueueTime = 1, processingTime = 2
       *     {2, 4},  // Task 1: enqueueTime = 2, processingTime = 4
       *     {3, 2},  // Task 2: enqueueTime = 3, processingTime = 2
       *     {4, 1}   // Task 3: enqueueTime = 4, processingTime = 1
       * };
       *
       *
       *
       * ⸻
       *
       * 🧠 Step-by-Step Execution (with if (!pq.isEmpty()))
       *
       * We simulate the CPU:
       *
       * Time	Ready Tasks in PQ	Task Chosen	Result Order	Time After	Notes
       * 0	none	-	[]	1	No task is available, move to t=1
       * 1	Task 0 (1,2)	Task 0	[0]	3	Process Task 0
       * 3	Task 1 (2,4), Task 2 (3,2)	Task 2	[0, 2]	5	Task 2 has lower processingTime
       * 5	Task 1 (2,4), Task 3 (4,1)	Task 3	[0, 2, 3]	6	Task 3 is next fastest
       * 6	Task 1 (2,4)	Task 1	[0, 2, 3, 1]	10	Only task left
       *
       * ✅ Final Order: [0, 2, 3, 1]
       *
       * ⸻
       *
       * 😵 What if we used while (!pq.isEmpty()) instead?
       *
       * Imagine the loop says:
       *
       * while (!pq.isEmpty()) {
       *     Task task = pq.poll();
       *     time += task.processingTime;
       *     order[idx++] = task.idx;
       * }
       *
       * Let’s simulate again…
       *
       * Time	PQ Initially	Tasks Processed (in loop)	Problem
       * 1	Task 0	Task 0	Time = 3, good
       * 3	Task 1 and Task 2	Both tasks processed	❌ Task 2 has lower processing time
       * 		But order is [0, 1, 2]	Incorrect: should have chosen Task 2 first
       *
       * ⚠️ This breaks the task order because we skip the re-check after time updates!
       *
       * ⸻
       *
       * ✅ Summary
       * 	•	Use if (!pq.isEmpty()) to process one task per cycle, and then re-check available tasks.
       * 	•	This guarantees you always pick the best task available at that moment.
       *
       *
       */
      // case 1)  when PQ is NOT empty, process task, update res, and idx
      if (!pq.isEmpty()) {
                Task task = pq.poll();
                time += task.processingTime;
                order[idx++] = task.idx;
            }
            /** NOTE !!! if PQ is empty */
            // case 2) if NO task is ready, we `move clock` to next  task's enqueueTime
            else {
                // If no tasks are ready, move time to the next task's enqueueTime
                time = taskList.get(i).enqueueTime;
            }
        }

        return order;
    }

    // V0-1
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
     *
     *  Process steps
     *
     *   1)  while ( idx < n)
     *   2)  `push` all ready tasks to PQ
     *   3)  if PQ is `NOT empty`
     *   4)  if PQ is `empty`
     *
     */
    /**
     * time = O(N log N)
     * space = O(N)
     */
    public int[] getOrder_0_1(int[][] tasks) {
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
    /**
     * time = O(N log N)
     * space = O(N)
     */
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
    /**
     * time = O(N log N)
     * space = O(N)
     */
    public int[] getOrder_2(int[][] tasks) {
        int n = tasks.length;
        Task2[] arr = new Task2[n];
        for (int i = 0; i < n; i++) {
            arr[i] = new Task2(i, tasks[i][0], tasks[i][1]);
        }

        Arrays.sort(arr, (a, b) -> {
            return Integer.compare(a.enqueueTime, b.enqueueTime);
        });

        PriorityQueue<Task2> p = new PriorityQueue<>((a, b) -> {
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

    class Task2 {
        int idx;
        int enqueueTime;
        int processingTime;

        Task2(int idx, int en, int pro) {
            this.idx = idx;
            this.enqueueTime = en;
            this.processingTime = pro;
        }
    }

    // V3
    // https://leetcode.com/problems/single-threaded-cpu/solutions/1164703/java-min-heap-sorting-readable-code-by-h-0mc7/

    // V4
    // https://leetcode.com/problems/single-threaded-cpu/solutions/2965812/best-approach-100-using-sorting-priority-ruh1/
    /**
     * time = O(N log N)
     * space = O(N)
     */
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

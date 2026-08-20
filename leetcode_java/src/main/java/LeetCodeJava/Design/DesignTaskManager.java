package LeetCodeJava.Design;

// https://leetcode.com/problems/design-task-manager/

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

/**
 *  3408. Design Task Manager
 *  Medium
 *
 *  There is a task management system that allows users to manage their tasks, each
 *  associated with a priority. The system should efficiently handle adding, modifying,
 *  executing, and removing tasks.
 *
 *  Implement the TaskManager class:
 *
 *   - TaskManager(List<List<Integer>> tasks) initializes the task manager with a list of
 *     user-task-priority triples [userId, taskId, priority].
 *   - void add(int userId, int taskId, int priority) adds a task with the specified taskId
 *     and priority to the user with userId. It is guaranteed that taskId does not exist
 *     in the system.
 *   - void edit(int taskId, int newPriority) updates the priority of the existing taskId
 *     to newPriority. It is guaranteed that taskId exists in the system.
 *   - void rmv(int taskId) removes the task identified by taskId from the system. It is
 *     guaranteed that taskId exists in the system.
 *   - int execTop() executes the task with the HIGHEST priority across all users. If there
 *     are multiple tasks with the same highest priority, execute the one with the HIGHEST
 *     taskId. After executing, the taskId is removed from the system. Returns the userId
 *     associated with the executed task, or -1 if no tasks are available.
 *
 *  Note that a user may be assigned multiple tasks.
 *
 *  Example 1:
 *    Input:
 *      ["TaskManager","add","edit","execTop","rmv","add","execTop"]
 *      [[[[1,101,10],[2,102,20],[3,103,15]]],[4,104,5],[102,8],[],[101],[5,105,15],[]]
 *    Output:
 *      [null,null,null,3,null,null,5]
 *    Explanation
 *      add(4, 104, 5);  edit(102, 8);
 *      execTop();       // return 3, executes task 103 (priority 15) for user 3
 *      rmv(101);        add(5, 105, 15);
 *      execTop();       // return 5, executes task 105 for user 5
 *
 *  Constraints:
 *    1 <= tasks.length <= 10^5
 *    0 <= userId <= 10^5
 *    0 <= taskId <= 10^5
 *    0 <= priority, newPriority <= 10^9
 *    At most 2 * 10^5 calls will be made in total to add, edit, rmv, and execTop.
 *    The input is generated such that taskId will be valid.
 */
public class DesignTaskManager {

    // V0
    // IDEA: MAX-HEAP WITH LAZY DELETION, A MAP AS THE SOURCE OF TRUTH
    //
    //   add / edit / rmv all want to reach into the MIDDLE of a priority queue, which
    //   a binary heap cannot do. the standard escape is to never delete: keep a map
    //   taskId -> (userId, priority) that is always current, and let the heap accumulate
    //   STALE entries.
    //
    //   an entry popped from the heap is BELIEVED only if the map still holds that task
    //   with exactly that priority; otherwise the task was edited away or removed and the
    //   entry is silently dropped. so edit costs one extra push and rmv costs nothing.
    //
    //   ordering is priority DESC then taskId DESC, which is the tie-break the problem asks
    //   for. every push is popped at most once, so the amortised cost stays O(log N) per
    //   call even though the heap can hold more entries than there are live tasks.
    /**
     * time = O(log N) amortised per operation
     * space = O(number of pushes)
     */
    private final Map<Integer, int[]> info = new HashMap<>();   // taskId -> {userId, priority}
    private final PriorityQueue<int[]> heap =
            new PriorityQueue<>(new Comparator<int[]>() {
                @Override
                public int compare(int[] a, int[] b) {
                    if (a[0] != b[0]) {
                        return Integer.compare(b[0], a[0]);   // higher priority first
                    }
                    return Integer.compare(b[1], a[1]);       // tie -> higher taskId
                }
            });

    public DesignTaskManager(List<List<Integer>> tasks) {
        for (List<Integer> t : tasks) {
            add(t.get(0), t.get(1), t.get(2));
        }
    }

    public void add(int userId, int taskId, int priority) {
        info.put(taskId, new int[]{userId, priority});
        heap.offer(new int[]{priority, taskId});
    }

    public void edit(int taskId, int newPriority) {
        int userId = info.get(taskId)[0];
        info.put(taskId, new int[]{userId, newPriority});
        heap.offer(new int[]{newPriority, taskId});
    }

    public void rmv(int taskId) {
        info.remove(taskId);
    }

    public int execTop() {
        while (!heap.isEmpty()) {
            int[] top = heap.poll();
            int priority = top[0];
            int taskId = top[1];
            int[] entry = info.get(taskId);
            if (entry != null && entry[1] == priority) {
                info.remove(taskId);
                return entry[0];
            }
        }
        return -1;
    }
}

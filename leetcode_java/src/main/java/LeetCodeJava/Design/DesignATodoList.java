package LeetCodeJava.Design;

// https://leetcode.com/problems/design-a-todo-list/

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *  2590. Design a Todo List
 *  Medium
 *
 *  Design a Todo List where users can add tasks, mark them as complete, or get a list
 *  of pending tasks. Users can also add tags to tasks and can filter the tasks by
 *  certain tags.
 *
 *  Implement the TodoList class:
 *
 *   - TodoList() Initializes the object.
 *   - int addTask(int userId, String taskDescription, int dueDate, List<String> tags)
 *     Adds a task for the user with the ID userId with a due date equal to dueDate and
 *     a list of tags attached to the task. The return value is the ID of the task. This
 *     ID starts at 1 and is sequentially increasing.
 *   - List<String> getAllTasks(int userId) Returns a list of all the tasks not marked as
 *     complete for the user with ID userId, ordered by the due date. Returns an empty
 *     list if the user has no uncompleted tasks.
 *   - List<String> getTasksForTag(int userId, String tag) Returns a list of all the tasks
 *     that are not marked as complete for the user with the ID userId and have tag as one
 *     of their tags, ordered by their due date. Returns an empty list if no such task exists.
 *   - void completeTask(int userId, int taskId) Marks the task with the ID taskId as
 *     completed only if the task exists and the user with the ID userId has this task,
 *     and it is uncompleted.
 *
 *  Example 1:
 *    Input
 *      ["TodoList","addTask","addTask","getAllTasks","getAllTasks","addTask",
 *       "getTasksForTag","completeTask","completeTask","getTasksForTag","getAllTasks"]
 *      [[],[1,"Task1",50,[]],[1,"Task2",100,["P1"]],[1],[5],[1,"Task3",30,["P1"]],
 *       [1,"P1"],[5,1],[1,2],[1,"P1"],[1]]
 *    Output
 *      [null,1,2,["Task1","Task2"],[],3,["Task3","Task2"],null,null,["Task3"],
 *       ["Task3","Task1"]]
 *    Explanation
 *      completeTask(5, 1) does nothing, since task 1 does not belong to user 5.
 *      completeTask(1, 2) marks task 2 as completed, so it drops out of both getters.
 *
 *  Constraints:
 *    1 <= userId, taskId, dueDate <= 100
 *    0 <= tags.length <= 100
 *    1 <= taskDescription.length <= 50
 *    1 <= tags[i].length, tag.length <= 20
 *    All dueDate values are unique.
 *    All the strings consist of lowercase and uppercase English letters and digits.
 *    At most 100 calls will be made for each method.
 */
public class DesignATodoList {

    private static class Task {
        final int taskId;
        final int dueDate;
        final String desc;
        final Set<String> tags;
        boolean done;

        Task(int taskId, int dueDate, String desc, Set<String> tags) {
            this.taskId = taskId;
            this.dueDate = dueDate;
            this.desc = desc;
            this.tags = tags;
            this.done = false;
        }
    }

    // V0
    // IDEA: HASH MAP (user -> task list) + SORT ON READ
    //
    //   users[userId] keeps that user's task records; a global counter hands out the
    //   sequential task ids starting from 1.
    //
    //   with at most 100 calls per method the cheapest CORRECT design is to keep the
    //   per-user list UNSORTED and sort by dueDate only when a read happens -- no
    //   ordered structure to maintain on every write.
    //
    //   NOTE: completeTask must be a NO-OP when the task belongs to a different user,
    //         so the taskId lookup is SCOPED to users[userId] only.
    //   NOTE: dueDate values are unique, so sorting by dueDate is a total order --
    //         no tie-breaking rule is required.
    //   NOTE: reading an unknown userId must NOT create state, hence the null check
    //         instead of a get-or-create.
    /**
     * time = O(1) addTask, O(N log N) per read, O(N) completeTask
     * space = O(N)
     */
    private final Map<Integer, List<Task>> users = new HashMap<>();
    private int nextId = 1;

    public DesignATodoList() {
    }

    public int addTask(int userId, String taskDescription, int dueDate, List<String> tags) {
        int taskId = this.nextId++;
        List<Task> list = users.get(userId);
        if (list == null) {
            list = new ArrayList<>();
            users.put(userId, list);
        }
        list.add(new Task(taskId, dueDate, taskDescription, new HashSet<>(tags)));
        return taskId;
    }

    public List<String> getAllTasks(int userId) {
        return collect(userId, null);
    }

    public List<String> getTasksForTag(int userId, String tag) {
        return collect(userId, tag);
    }

    public void completeTask(int userId, int taskId) {
        List<Task> list = users.get(userId);
        if (list == null) {
            return;
        }
        for (Task t : list) {
            if (t.taskId == taskId) {
                t.done = true;
                return;
            }
        }
    }

    /** pending tasks of userId ordered by dueDate; when tag != null, only tasks carrying it */
    private List<String> collect(int userId, String tag) {
        List<String> res = new ArrayList<>();
        List<Task> list = users.get(userId);
        if (list == null) {
            return res;
        }
        List<Task> sorted = new ArrayList<>(list);
        Collections.sort(sorted, new Comparator<Task>() {
            @Override
            public int compare(Task a, Task b) {
                return Integer.compare(a.dueDate, b.dueDate);
            }
        });
        for (Task t : sorted) {
            if (!t.done && (tag == null || t.tags.contains(tag))) {
                res.add(t.desc);
            }
        }
        return res;
    }
}

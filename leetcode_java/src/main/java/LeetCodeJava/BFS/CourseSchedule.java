package LeetCodeJava.BFS;

//  https://leetcode.com/problems/course-schedule/

import java.util.*;

public class CourseSchedule {

    // V0
    // IDEA : DFS (fix by gpt)
    // NOTE !!! instead of maintain status (0,1,2), below video offers a simpler approach
    //      -> e.g. use a set, recording the current visiting course, if ANY duplicated (already in set) course being met,
    //      -> means "cyclic", so return false directly
    // https://www.youtube.com/watch?v=EgI5nU9etnU
    public boolean canFinish(int numCourses, int[][] prerequisites) {
        // Initialize adjacency list for storing prerequisites
        /**
         *  NOTE !!!
         *
         *  init prerequisites map
         *  {course : [prerequisites_array]}
         *  below init map with null array as first step
         */
        Map<Integer, List<Integer>> preMap = new HashMap<>();
        for (int i = 0; i < numCourses; i++) {
            preMap.put(i, new ArrayList<>());
        }

        // Populate the adjacency list with prerequisites
        /**
         *  NOTE !!!
         *
         *  update prerequisites map
         *  {course : [prerequisites_array]}
         *  so we go through prerequisites,
         *  then append each course's prerequisites to preMap
         */
        for (int[] pair : prerequisites) {
            int crs = pair[0];
            int pre = pair[1];
            preMap.get(crs).add(pre);
        }

        /** NOTE !!!
         *
         *  init below set for checking if there is "cyclic" case
         */
        // Set for tracking courses during the current DFS path
        Set<Integer> visiting = new HashSet<>();

        // Recursive DFS function
        for (int c = 0; c < numCourses; c++) {
            if (!dfs(c, preMap, visiting)) {
                return false;
            }
        }
        return true;
    }

    private boolean dfs(int crs, Map<Integer, List<Integer>> preMap, Set<Integer> visiting) {
        /** NOTE !!!
         *
         *  if visiting contains current course,
         *  means there is a "cyclic",
         *  (e.g. : needs to take course a, then can take course b, and needs to take course b, then can take course a)
         *  so return false directly
         */
        if (visiting.contains(crs)) {
            return false;
        }
        /**
         *  NOTE !!!
         *
         *  if such course has NO preRequisite,
         *  return true directly
         */
        if (preMap.get(crs).isEmpty()) {
            return true;
        }

        /**
         *  NOTE !!!
         *
         *  add current course to set (Set<Integer> visiting)
         */
        visiting.add(crs);
        for (int pre : preMap.get(crs)) {
            if (!dfs(pre, preMap, visiting)) {
                return false;
            }
        }
        /**
         *  NOTE !!!
         *
         *  remove current course from set,
         *  since already finish visiting
         *
         *  e.g. undo changes
         */
        visiting.remove(crs);
        preMap.get(crs).clear(); // Clear prerequisites as the course is confirmed to be processed
        return true;
    }

    // VO'
    // IDEA : TOPOLOGICAL SORT
    // TODO : implement

    // V0''
    // IDEA : DFS
    // https://github.com/neetcode-gh/leetcode/blob/main/java/0207-course-schedule.java
    // https://www.youtube.com/watch?v=EgI5nU9etnU
    public boolean canFinish_0_1(int numCourses, int[][] prerequisites) {
        List<List<Integer>> adj = new ArrayList<>();
        for (int i = 0; i < numCourses; i++) {
            adj.add(new ArrayList<>());
        }

        for (int i = 0; i < prerequisites.length; i++) {
            adj.get(prerequisites[i][0]).add(prerequisites[i][1]);
        }

        int[] visited = new int[numCourses];
        for (int i = 0; i < numCourses; i++) {
            if (visited[i] == 0) {
                if (isCyclic(adj, visited, i)) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean isCyclic(List<List<Integer>> adj, int[] visited, int curr) {
        if (visited[curr] == 2) {
            return true;
        }

        visited[curr] = 2;
        for (int i = 0; i < adj.get(curr).size(); i++) {
            if (visited[adj.get(curr).get(i)] != 1) {
                if (isCyclic(adj, visited, adj.get(curr).get(i))) {
                    return true;
                }
            }
        }
        visited[curr] = 1;
        return false;
    }

    // V0''
    // IDEA : DFS
    // https://github.com/yennanliu/CS_basics/blob/master/leetcode_python/Breadth-First-Search/course-schedule.py
    public boolean canFinish_0_2(int numCourses, int[][] prerequisites) {
        Map<Integer, List<Integer>> graph = new HashMap<>();
        for (int[] prerequisite : prerequisites) {
            graph.computeIfAbsent(prerequisite[0], k -> new ArrayList<>()).add(prerequisite[1]);
        }

        int[] visited = new int[numCourses];
        List<Integer> res = new ArrayList<>();
        for (int i = 0; i < numCourses; i++) {
            if (!dfs(res, graph, visited, i)) {
                return false;
            }
        }

        return res.size() > 0;
    }

    private boolean dfs(List<Integer> res, Map<Integer, List<Integer>> graph, int[] visited, int course) {

        /** NOTE !!!
         *
         *  here we maintain 3 status:
         *
         *   status = 0 : not visited (The course has not been visited yet.)
         *   status = 1 : visiting (The course is currently being visited in the traversal.)
         *   status = 2 : visited (The course has been visited and processed.)
         *
         *   ->
         *   So,
         *   if status == 2, return true directly, since such course already been visited, we should not visit it again
         *
         *   if status = 1, should return false directly, since "it is being visiting now",
         *                  any other progress try to visit the same course at the same time
         *                  means course conflict, -> can't take such course
         *
         *
         *  Ref : https://github.com/yennanliu/CS_basics/blob/master/leetcode_python/Breadth-First-Search/course-schedule.py#L44
         */

        /**
         *  Cycle Detection: It checks if the current course
         *  is being visited (visited[course] == 1).
         *  If so, it means there's a cycle in the graph, and the method returns false.
         */
        if (visited[course] == 1) {
            return false;
        }
        /**
         * Visited Check: If the course has already
         * been visited (visited[course] == 2),
         * it returns true.
         */
        if (visited[course] == 2) {
            return true;
        }

        /**
         *  NOTE !!!
         *   Mark as Visiting: It marks the current
         *   course as visiting (visited[course] = 1)
         *   before visiting its neighbors.
         */
        visited[course] = 1;
        if (graph.containsKey(course)) {
            /**
             * NOTE !!!
             *   DFS on Neighbors: For each neighbor of the current course,
             *   it recursively calls dfs. If any neighbor returns false,
             *   it means the current course can't be finished,
             *   and the method returns false
             */
            for (int neighbor : graph.get(course)) {
                if (!dfs(res, graph, visited, neighbor)) {
                    return false;
                }
            }
        }

        /**
         * Mark as Visited: After visiting all neighbors,
         * it marks the current course as visited (visited[course] = 2)
         * and adds it to the result list res.
         */
        visited[course] = 2;
        res.add(0, course);

        /**
         * The dfs function needs to return "true" at the end
         * to indicate that the current course (and its prerequisites)
         * can be completed successfully.
         *  which is checking if it's possible to finish all courses.
         */
        return true;
    }

    // V0'''
    // IDEA : BFS
    // NOTE !!! we have 3 loop : numCourses, prerequisites, numCourses
    public boolean canFinish_(int numCourses, int[][] prerequisites) {

        // save course - prerequisites info
        Map<Integer, ArrayList<Integer>> map = new HashMap<Integer, ArrayList<Integer>>();
        // save "degree" for every course
        int[] indegree = new int[numCourses];
        // for BFS
        Queue<Integer> queue = new LinkedList<Integer>();
        // check how many courses are NOT yet visited
        int count = numCourses;

        /** NOTE !!! numCourses
         *
         *   -> construct map (course - prerequisites info)
         */
        for (int i = 0; i < numCourses; i++) {
            map.put(i, new ArrayList<Integer>());
        }

        /** NOTE !!! prerequisites
         *
         *   -> 1) inset info to map (course - prerequisites info)
         *   -> 2) insert degree for every course
         *
         */
        for (int i = 0; i < prerequisites.length; i++) {
            map.get(prerequisites[i][0]).add(prerequisites[i][1]);

            /** NOTE : below approaches has same effect */
            // V1
            //indegree[prerequisites[i][1]]++;
            // V2
            indegree[prerequisites[i][1]] += 1;
        }

        /** NOTE !!! numCourses
         *
         *  -> via this loop, we insert elements for "first" visiting
         *     (degree == 0)
         */
        for (int i = 0; i < numCourses; i++) {
            // only insert to queue if "indegree == 0" (course has NO prerequisite)
            if (indegree[i] == 0) {
                queue.offer(i); // insert to queue
            }
        }

        while (!queue.isEmpty()) {
            int current = queue.poll(); // pop from queue
            for (int i : map.get(current)) {

                /** NOTE : below approaches has same effect */
                // V1
//                if (--indegree[i] == 0) {
//                    queue.offer(i);
//                }

                // V2
                // NOTE !!! if "degree" == 1, then we insert it to queue,
                //          for next visiting
                if (indegree[i] == 1) {
                    queue.offer(i);
                } else {
                    // NOTE !!! if if "degree" != 1,
                    // minus its degree (indegree[i] -= 1), since current degree (other element) is visited
                    indegree[i] -= 1;
                }

            }
            // when finish a course checking ( course visit and course prerequisite visit)
            count--;
        }
        return count == 0;
    }

    // V1
    // IDEA : BFS
    // https://leetcode.com/problems/course-schedule/solutions/58775/my-java-bfs-solution/
    public boolean canFinish_0_3(int numCourses, int[][] prerequisites) {
        Map<Integer, ArrayList<Integer>> map = new HashMap<Integer, ArrayList<Integer>>();
        int[] indegree = new int[numCourses];
        Queue<Integer> queue = new LinkedList<Integer>();
        int count = numCourses;

        /** NOTE !!! numCourses */
        for (int i = 0; i < numCourses; i++) {
            map.put(i, new ArrayList<Integer>());
        }

        /** NOTE !!! prerequisites */
        for (int i = 0; i < prerequisites.length; i++) {
            map.get(prerequisites[i][0]).add(prerequisites[i][1]);

            /** NOTE : below approaches has same effect */
            // V1
            //indegree[prerequisites[i][1]]++;
            // V2
            indegree[prerequisites[i][1]] += 1;
        }

        /** NOTE !!! numCourses */
        for (int i = 0; i < numCourses; i++) {
            // only insert to queue if "indegree == 0" (course has NO prerequisite)
            if (indegree[i] == 0) {
                queue.offer(i); // insert to queue
            }
        }

        while (!queue.isEmpty()) {
            int current = queue.poll(); // pop from queue
            for (int i : map.get(current)) {

                  /** NOTE : below approaches has same effect */
                  // V1
//                if (--indegree[i] == 0) {
//                    queue.offer(i);
//                }

                // V2
                if (indegree[i] == 1) {
                    queue.offer(i);
                } else {
                    indegree[i] -= 1;
                }

            }
            // when finish a course checking ( course visit and course prerequisite visit)
            count--;
        }
        return count == 0;
    }

    // V2
    // IDEA : BFS
    // https://leetcode.com/problems/course-schedule/solutions/58524/java-dfs-and-bfs-solution/
    public boolean canFinish_2(int numCourses, int[][] prerequisites) {
        ArrayList[] graph = new ArrayList[numCourses];
        int[] degree = new int[numCourses];
        Queue queue = new LinkedList();
        int count=0;

        for(int i=0;i<numCourses;i++)
            graph[i] = new ArrayList();

        for(int i=0; i<prerequisites.length;i++){
            degree[prerequisites[i][1]]++;
            graph[prerequisites[i][0]].add(prerequisites[i][1]);
        }
        for(int i=0; i<degree.length;i++){
            if(degree[i] == 0){
                queue.add(i);
                count++;
            }
        }

        while(queue.size() != 0){
            int course = (int)queue.poll();
            for(int i=0; i<graph[course].size();i++){
                int pointer = (int)graph[course].get(i);
                degree[pointer]--;
                if(degree[pointer] == 0){
                    queue.add(pointer);
                    count++;
                }
            }
        }
        if(count == numCourses)
            return true;
        else
            return false;
    }

    // V3
    // IDEA :  TOPOLOGICAL SORT
    // https://leetcode.com/problems/course-schedule/solutions/447754/java-topological-sort-dfs-3ms/
    enum Status {
        NOT_VISITED, VISITED, VISITING;
    }

    public boolean canFinish_3(int numCourses, int[][] prerequisites) {
        if(prerequisites == null || prerequisites.length == 0 || prerequisites[0].length == 0) return true;
        // building graph
        List<List<Integer>> list = new ArrayList<>(numCourses);
        for(int i = 0; i < numCourses; i++) {
            list.add(new ArrayList<Integer>());
        }
        for(int[] p: prerequisites) {
            int prerequisite = p[1];
            int course = p[0];
            list.get(course).add(prerequisite);
        }

        Status[] visited = new Status[numCourses];
        for(int i = 0; i < numCourses; i++) {
            // if there is a cycle, return false
            if(dfs(list, visited, i)) return false;
        }
        return true;
    }

    private boolean dfs(List<List<Integer>> list, Status[] visited, int cur) {
        if(visited[cur] == Status.VISITING) return true;
        if(visited[cur] == Status.VISITED) return false;
        visited[cur] = Status.VISITING;
        for(int next: list.get(cur)) {
            if(dfs(list, visited, next)) return true;
        }
        visited[cur] = Status.VISITED;
        return false;
    }

}

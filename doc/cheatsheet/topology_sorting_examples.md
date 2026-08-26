# Topological Sorting — Worked Examples

> **Scope** — The worked-solution archive behind [topology_sorting.md](./topology_sorting.md): eight problems in both languages, grouped by the shape of the dependency they encode rather than by problem number.
> **See also**: [topology_sorting.md](./topology_sorting.md) — the parent sheet: ten templates, the problem classification and the decision framework these solutions apply; [graph.md](./graph.md) — graph representation and traversal in general; [bfs.md](./bfs.md) and [dfs.md](./dfs.md) — the two traversals Kahn's and the three-colour template are built on; [union_find.md](./union_find.md) — the disjoint-set structure behind LC 547.

## LeetCode Problem Lists

- [Topological Sort](https://leetcode.com/problem-list/topological-sort/)
- [Graph](https://leetcode.com/problem-list/graph/)

## Overview

This is the long tail of [topology_sorting.md](./topology_sorting.md). The parent keeps the ten
templates and the decision framework; this file keeps the problems that *apply* them, so the
templates are not buried under 1,200 lines of solutions.

### Key Properties
- **Complexity**: every solution below is O(V + E) unless its own comment says otherwise
- **Core Idea**: the work is almost never the sort — it is deciding what the nodes and edges *are*, which is why these are grouped by dependency shape
- **When to Use**: after the parent's decision framework has told you which template the problem wants


## Course Scheduling & Ordering

### 1) Course Schedule II — LC 210 ⭐⭐⭐⭐⭐


```java
// java
// LC 210
// ref: leetcode_java/src/main/java/LeetCodeJava/BFS/CourseSchedule2.java
public class CourseSchedule2 {

    // V0
    // IDEA : BFS Kahn's Algorithm with followingMap (pre -> next map)
    // NOTE: With followingMap (Kahn's), we DO NOT need to reverse.
    //       The order is already correct (starts with 0-dependency courses).
    /**
     * time = O(V + E)
     * space = O(V + E)
     */
    public int[] findOrder(int numCourses, int[][] prerequisites) {
        if (numCourses == 0) {
            return null;
        }
        if (numCourses == 1) {
            return new int[] { 0 };
        }

        /**
         *  map : {pre_course : [next_course_1, next_course_2, ...] }
         *  key is pre_course, value is the list of next-courses
         */
        Map<Integer, List<Integer>> followingMap = new HashMap<>();
        int[] degrees = new int[numCourses];

        for (int[] p : prerequisites) {
            int cur = p[0];   // next course (depends on prev)
            int prev = p[1];  // prerequisite course

            // Build graph: prev -> [next courses]
            if (!followingMap.containsKey(prev)) {
                followingMap.put(prev, new ArrayList<>());
            }
            followingMap.get(prev).add(cur);

            // Update in-degree for the next course
            degrees[cur] += 1;
        }

        Queue<Integer> q = new LinkedList<>();
        List<Integer> collected = new ArrayList<>();

        // Add all courses with 0 in-degree to queue
        for (int i = 0; i < degrees.length; i++) {
            if (degrees[i] == 0) {
                q.add(i);
            }
        }

        while (!q.isEmpty()) {
            int cur = q.poll();
            collected.add(cur); // Add to result right after pop

            if (followingMap.containsKey(cur)) {
                for (int next : followingMap.get(cur)) {
                    degrees[next] -= 1;
                    if (degrees[next] == 0) {
                        q.add(next);
                    }
                }
            }
        }

        // Cycle check: if not all courses collected, cycle exists
        if (collected.size() != numCourses) {
            return new int[]{};
        }

        // NO need to reverse with followingMap approach
        int[] res = new int[collected.size()];
        for (int i = 0; i < collected.size(); i++) {
            res[i] = collected.get(i);
        }
        return res;
    }

    // V1
    // IDEA: DFS Cycle Detection
    /**
     * time = O(V + E)
     * space = O(V + E)
     */
    public int[] findOrder_1_1(int numCourses, int[][] prerequisites) {
        Map<Integer, List<Integer>> prereq = new HashMap<>();
        for (int[] pair : prerequisites) {
            prereq.computeIfAbsent(pair[0],
                    k -> new ArrayList<>()).add(pair[1]);
        }

        List<Integer> output = new ArrayList<>();
        Set<Integer> visit = new HashSet<>();
        Set<Integer> cycle = new HashSet<>();

        for (int course = 0; course < numCourses; course++) {
            if (!dfs(course, prereq, visit, cycle, output)) {
                return new int[0];
            }
        }

        int[] result = new int[numCourses];
        for (int i = 0; i < numCourses; i++) {
            result[i] = output.get(i);
        }
        return result;
    }

    private boolean dfs(int course, Map<Integer, List<Integer>> prereq,
                        Set<Integer> visit, Set<Integer> cycle,
                        List<Integer> output) {
        if (cycle.contains(course)) return false;  // cycle detected
        if (visit.contains(course)) return true;    // already processed

        cycle.add(course);
        for (int pre : prereq.getOrDefault(course, Collections.emptyList())) {
            if (!dfs(pre, prereq, visit, cycle, output)) {
                return false;
            }
        }
        cycle.remove(course);
        visit.add(course);
        output.add(course);
        return true;
    }
}
```

```python
# LC 210 Course Schedule II
# V0
# IDEA : DFS + topological sort
# SAME dfs logic as LC 207 (Course Schedule)
from collections import defaultdict
class Solution(object):
    def findOrder(self, numCourses, prerequisites):
        # edge case
        if not prerequisites:
            return [x for x in range(numCourses)]
        
        # help func : dfs
        # 3 cases :  0 : unknown, 1 :visiting, 2 : visited   
        def dfs(idx, visited, g, res):
            if visited[idx] == 1:
                return False
            # NOTE !!! if visited[idx] == 2, means already visited, return True directly (and check next idx in range(numCourses))
            if visited[idx] == 2:
                return True
            visited[idx] = 1
            """
            NOTE this !!!

                1) for j in g[idx] (but not for i in range(numCourses))
                2) go through idx in g[idx]
            """
            for j in g[idx]:
                if not dfs(j, visited, g, res):
                    return False
            """
            don't forget to make idx as visited (visited[idx] = 2)
            """
            visited[idx] = 2
            """
            NOTE : the main difference between LC 207, 210

            -> we append idx to res (our ans)
            """
            res.append(idx)
            return True
        # init
        visited = [0] * numCourses
        # build grath
        g = defaultdict(list)
        for p in prerequisites:
            g[p[0]].append(p[1])
        res = []
        """
        NOTE :  go through idx in numCourses (for idx in range(numCourses))
        """
        for idx in range(numCourses):
            if not dfs(idx, visited, g, res):
                return []
        return res

# V0'
# IDEA : DFS + topological sort
# SAME dfs logic as LC 207 (Course Schedule)
import collections
class Solution:
    def findOrder(self, numCourses, prerequisites):
        # build graph
        _graph = collections.defaultdict(list)
        for i in range(len(prerequisites)):
            _graph[prerequisites[i][0]].append(prerequisites[i][1])

        visited = [0] * numCourses
        res = []
        for i in range(numCourses):
            if not self.dfs(_graph, visited, i, res):
                return []
        print ("res = " + str(res))
        return res

    # 0 : unknown, 1 :visiting, 2 : visited    
    def dfs(self, _graph, visited, i, res):
        if visited[i] == 1:
            return False
        if visited[i] == 2:
            return True
        visited[i] = 1
        for item in _graph[i]:
            if not self.dfs(_graph, visited, item, res):
                return False
        visited[i] = 2
        res.append(i)
        return True

# V0'
# IDEA : DFS + topological sort
# SAME dfs logic as LC 207 (Course Schedule)
class Solution(object):
    def findOrder(self, numCourses, prerequisites):
        """
        :type numCourses: int
        :type prerequisites: List[List[int]]
        :rtype: List[int]
        """
        graph = collections.defaultdict(list)
        for u, v in prerequisites:
            graph[u].append(v)
        # 0 = Unknown, 1 = visiting, 2 = visited
        visited = [0] * numCourses
        path = []
        for i in range(numCourses):
            ### NOTE : if not a valid "prerequisites", then will return NULL list
            if not self.dfs(graph, visited, i, path):
                return []
        return path
    
    def dfs(self, graph, visited, i, path):
        # 0 = Unknown, 1 = visiting, 2 = visited
        if visited[i] == 1: return False
        if visited[i] == 2: return True
        visited[i] = 1
        for j in graph[i]:
            if not self.dfs(graph, visited, j, path):
                ### NOTE : the quit condition
                return False
        visited[i] = 2
        path.append(i)
        return True
```

### 2) Course Schedule — LC 207 ⭐⭐⭐⭐⭐


```java
// java
// LC 207
// same as LC 210
```

```python
# LC 207 Course Schedule
# NOTE : there are also bracktrack, dfs approachs for this problem
# V0
# IDEA : LC Course Schedule II 
from collections import defaultdict
class Solution(object):
    def canFinish(self, numCourses, prerequisites):
        # edge case
        if not prerequisites:
            return [x for x in range(numCourses)]
        
        # help func : dfs
        # 3 cases :  0 : unknown, 1 :visiting, 2 : visited   
        def dfs(idx, visited, g, res):
            if visited[idx] == 1:
                return False
            # NOTE !!! if visited[idx] == 2, means already visited, return True directly (and check next idx in range(numCourses))
            if visited[idx] == 2:
                return True
            visited[idx] = 1
            """
            NOTE this !!!
                1) for j in g[idx] (but not for i in range(numCourses))
                2) go through idx in g[idx]
            """
            for j in g[idx]:
                if not dfs(j, visited, g, res):
                    return False
            """
            don't forget to make idx as visited (visited[idx] = 2)
            """
            visited[idx] = 2
            """
            NOTE : the main difference between LC 207, 210
            -> we append idx to res (our ans)
            """
            res.append(idx)
            return True
        # init
        visited = [0] * numCourses
        # build grath
        g = defaultdict(list)
        for p in prerequisites:
            g[p[0]].append(p[1])
        res = []
        """
        NOTE :  go through idx in numCourses (for idx in range(numCourses))
        """
        for idx in range(numCourses):
            if not dfs(idx, visited, g, res):
                return False #[]
        return len(res) > 0

# V1
# IDEA : Topological Sort
# https://leetcode.com/problems/course-schedule/solution/
class GNode(object):
    """  data structure represent a vertex in the graph."""
    def __init__(self):
        self.inDegrees = 0
        self.outNodes = []

class Solution(object):
    def canFinish(self, numCourses, prerequisites):
        """
        :type numCourses: int
        :type prerequisites: List[List[int]]
        :rtype: bool
        """
        from collections import defaultdict, deque
        # key: index of node; value: GNode
        graph = defaultdict(GNode)

        totalDeps = 0
        for relation in prerequisites:
            nextCourse, prevCourse = relation[0], relation[1]
            graph[prevCourse].outNodes.append(nextCourse)
            graph[nextCourse].inDegrees += 1
            totalDeps += 1

        # we start from courses that have no prerequisites.
        # we could use either set, stack or queue to keep track of courses with no dependence.
        nodepCourses = deque()
        for index, node in graph.items():
            if node.inDegrees == 0:
                nodepCourses.append(index)

        removedEdges = 0
        while nodepCourses:
            # pop out course without dependency
            course = nodepCourses.pop()

            # remove its outgoing edges one by one
            for nextCourse in graph[course].outNodes:
                graph[nextCourse].inDegrees -= 1
                removedEdges += 1
                # while removing edges, we might discover new courses with prerequisites removed, i.e. new courses without prerequisites.
                if graph[nextCourse].inDegrees == 0:
                    nodepCourses.append(nextCourse)

        if removedEdges == totalDeps:
            return True
        else:
            # if there are still some edges left, then there exist some cycles
            # Due to the dead-lock (dependencies), we cannot remove the cyclic edges
            return False

# V0
# IDEA : DFS + topological sort 
# dfs
from collections import defaultdict
class Solution(object):
    def canFinish(self, numCourses, prerequisites):
        # edge case
        if not prerequisites:
            return True
        
        # help func : dfs
        # 3 cases :  0 : unknown, 1 :visiting, 2 : visited   
        def dfs(idx, visited, g):
            if visited[idx] == 1:
                return False
            # NOTE !!! if visited[idx] == 2, means already visited, return True directly (and check next idx in range(numCourses))
            if visited[idx] == 2:
                return True
            visited[idx] = 1
            """
            NOTE this !!!

                1) for j in g[idx] (but not for i in range(numCourses))
                2) go through idx in g[idx]
            """
            for j in g[idx]:
                if not dfs(j, visited, g):
                    return False
            """
            don't forget to make idx as visited (visited[idx] = 2)
            """
            visited[idx] = 2
            return True
        # init
        visited = [0] * numCourses
        # build grath
        g = defaultdict(list)
        for p in prerequisites:
            g[p[0]].append(p[1])
        #print ("g = " + str(p))
        # dfs
        """
        NOTE :  go through idx in numCourses (for idx in range(numCourses))
        """
        for idx in range(numCourses):
            if not dfs(idx, visited, g):
                return False
        return True

# V0
# IDEA : DFS + topological sort 
import collections
class Solution:
    def canFinish(self, numCourses, prerequisites):
        _graph = collections.defaultdict(list)
        for i in range(len(prerequisites)):
            _graph[prerequisites[i][0]].append(prerequisites[i][1])

        visited = [0] * numCourses
        for i in range(numCourses):
            if not self.dfs(_graph, visited, i):
                return False
        return True

    # 0 : unknown, 1 :visiting, 2 : visited    
    def dfs(self, _graph, visited, i):
        if visited[i] == 1:
            return False
        if visited[i] == 2:
            return True
        visited[i] = 1
        for item in _graph[i]:
            if not self.dfs(_graph, visited, item):
                return False
        visited[i] = 2
        return True

# V0'
# IDEA : BFS + topological sort 
from collections import defaultdict, deque
class Solution:
    def canFinish(self, numCourses, prerequisites):
        degree = defaultdict(int)   
        graph = defaultdict(set)
        q = deque()
        
        # init the courses with 0 deg
        for i in range(numCourses):
            degree[i] = 0
        
        # add 1 to degree of course that needs prereq
        # build edge from prerequisite to child course (directed graph)
        for pair in prerequisites:
            degree[pair[0]] += 1
            graph[pair[1]].add(pair[0])
        
        # start bfs queue with all classes that dont have a prerequisite
        for key, val in degree.items():
            if val == 0:
                q.append(key)
                
        stack = []
        
        while q:
            curr = q.popleft()
            stack.append(curr)
            for child in graph[curr]:
                degree[child] -= 1
                if degree[child] == 0:
                    q.append(child)
        
        return len(stack) == numCourses
```

### 3) Alien Dictionary — LC 269 ⭐⭐⭐⭐


```java
// java
// LC 269

 // V0
    // IDEA: TOPOLOGICAL SORT (neetcode, comments created by gpt)
    // TOPOLOGICAL SORT : `degrees`, map, BFS
    public String foreignDictionary(String[] words) {
        Map<Character, Set<Character>> adj = new HashMap<>();
        // NOTE !!! we use `map` as degrees storage
        Map<Character, Integer> indegree = new HashMap<>();

        for (String word : words) {
            for (char c : word.toCharArray()) {
                adj.putIfAbsent(c, new HashSet<>());
                indegree.putIfAbsent(c, 0);
            }
        }

        /**
         *   NOTE !!! below
         *
         *   -> build the character `ordering`
         *
         *  Loop Over Adjacent Word Pairs
         *
         *
         *
         * for (int i = 0; i < words.length - 1; i++) {
         *     String w1 = words[i];
         *     String w2 = words[i + 1];
         *
         * We are comparing each pair of consecutive
         * words in the list (words[i] and words[i+1]).
         *
         * This is important because the alien language is
         * sorted — and order relationships only exist between adjacent words.
         *
         */
        for (int i = 0; i < words.length - 1; i++) {
            String w1 = words[i];
            String w2 = words[i + 1];

            /**
             *  NOTE !!! below
             *
             *
             * int minLen = Math.min(w1.length(), w2.length());
             * if (w1.length() > w2.length() &&
             *     w1.substring(0, minLen).equals(w2.substring(0, minLen))) {
             *     return "";
             * }
             *
             *
             * ->  This checks for a prefix violation:
             * If w1 is longer than w2, and w2 is a prefix of w1, that’s `invalid`.
             *
             * Example:
             *
             *   words = ["apple", "app"]
             *
             *
             * Here, app comes after apple,
             * which is wrong because in a lexicographically sorted language,
             * a shorter prefix should come before the longer word.
             *
             * -> Hence, we return "" to signal an invalid dictionary order.
             *
             */
            int minLen = Math.min(w1.length(), w2.length());
            // handle `ordering` edge case
            // e.g. words = ["apple", "app"]
            if (w1.length() > w2.length() &&
                    w1.substring(0, minLen).equals(w2.substring(0, minLen))) {
                return "";
            }

            /**
             *  NOTE !!! below
             *
             *
             *  This loop compares characters at each position j in w1 and w2.
             *  The first place where they differ defines the ordering.
             *
             *
             *  Example :
             *
             *    w1 = "wrt"
             *    w2 = "wrf"
             *
             *
             *
             *  At index 2, 't' and 'f' differ → so we know:
             * 't' < 'f' → Add a directed edge: t → f
             *
             * adj.get(w1.charAt(j)).add(w2.charAt(j)): Adds this edge in the adjacency list.
             *
             * indegree.put(...): Increments in-degree of the target node.
             *
             *
             * NOTE !!!
             *
             * -> Then we break — we don’t look at further characters
             *     -> because they don’t affect the order.
             *
             *
             */
            // compare the `first different character within w1, w2`
            // The first place where they differ defines the ordering.
            for (int j = 0; j < minLen; j++) {
                if (w1.charAt(j) != w2.charAt(j)) {
                    if (!adj.get(w1.charAt(j)).contains(w2.charAt(j))) {
                        adj.get(w1.charAt(j)).add(w2.charAt(j));
                        indegree.put(w2.charAt(j),
                                indegree.get(w2.charAt(j)) + 1);
                    }
                    break;
                }
            }
        }

        Queue<Character> q = new LinkedList<>();
        for (char c : indegree.keySet()) {
            if (indegree.get(c) == 0) {
                q.offer(c);
            }
        }

        StringBuilder res = new StringBuilder();

        while (!q.isEmpty()) {
            char char_ = q.poll();
            res.append(char_);
            for (char neighbor : adj.get(char_)) {
                indegree.put(neighbor, indegree.get(neighbor) - 1);
                if (indegree.get(neighbor) == 0) {
                    q.offer(neighbor);
                }
            }
        }

        if (res.length() != indegree.size()) {
            return "";
        }

        return res.toString();
    }

```

```python
# python
# 269 alien-dictionary
class Solution(object):
    def alienOrder(self, words):
        """
        :type words: List[str]
        :rtype: str
        """
        result, zero_in_degree_queue, in_degree, out_degree = [], collections.deque(), {}, {}
        nodes = sets.Set()
        for word in words:
            for c in word:
                nodes.add(c)
         
        for i in range(1, len(words)):
            if len(words[i-1]) > len(words[i]) and \
                words[i-1][:len(words[i])] == words[i]:
                    return ""
            self.findEdges(words[i - 1], words[i], in_degree, out_degree)
         
        for node in nodes:
            if node not in in_degree:
                zero_in_degree_queue.append(node)
         
        while zero_in_degree_queue:
            precedence = zero_in_degree_queue.popleft()
            result.append(precedence)
             
            if precedence in out_degree:
                for c in out_degree[precedence]:
                    in_degree[c].discard(precedence)
                    if not in_degree[c]:
                        zero_in_degree_queue.append(c)
             
                del out_degree[precedence]
         
        if out_degree:
            return ""
 
        return "".join(result)
 
 
    # Construct the graph.
    def findEdges(self, word1, word2, in_degree, out_degree):
        str_len = min(len(word1), len(word2))
        for i in range(str_len):
            if word1[i] != word2[i]:
                if word2[i] not in in_degree:
                    in_degree[word2[i]] = sets.Set()
                if word1[i] not in out_degree:
                    out_degree[word1[i]] = sets.Set()
                in_degree[word2[i]].add(word1[i])
                out_degree[word1[i]].add(word2[i])
                break
```

### 4) Sequence Reconstruction — LC 444


```java
// java
// LC 444
    // V1
    // https://www.youtube.com/watch?v=FHY1q1h9gq0
    // https://www.jiakaobo.com/leetcode/444.%20Sequence%20Reconstruction.html
    Map<Integer, Set<Integer>> map;
    Map<Integer, Integer> indegree;

    public boolean sequenceReconstruction_1(int[] nums, List<List<Integer>> sequences) {
        map = new HashMap<>();
        indegree = new HashMap<>();

        for(List<Integer> seq: sequences) {
            if(seq.size() == 1) {
                addNode(seq.get(0));
            } else {
                for(int i = 0; i < seq.size() - 1; i++) {
                    addNode(seq.get(i));
                    addNode(seq.get(i + 1));

                    // 加入子节点, 子节点增加一个入度
                    // [1,2] => 1 -> 2
                    // 1: [2]
                    int curr = seq.get(i);
                    int next = seq.get(i + 1);
                    if(map.get(curr).add(next)) {
                        indegree.put(next, indegree.get(next) + 1);
                    }
                }
            }
        }

        Queue<Integer> queue = new LinkedList<>();
        for(int key : indegree.keySet()) {
            if(indegree.get(key) == 0){
                queue.offer(key);
            }
        }

        int index = 0;
        while(!queue.isEmpty()) {
            // 如果只有唯一解, 那么queue的大小永远都是1
            if(queue.size() != 1) return false;

            int curr = queue.poll();
            if(curr != nums[index++]) return false;

            for(int next: map.get(curr)) {
                indegree.put(next, indegree.get(next) - 1);
                if(indegree.get(next) == 0) {
                    queue.offer(next);
                }
            }
        }

        return index == nums.length;
    }

    private void addNode(int node) {
        if(!map.containsKey(node)) {
            map.put(node, new HashSet<>());
            indegree.put(node, 0);
        }
    }
```

## Layering & Parallel Scheduling

### 5) Parallel Courses — LC 1136

```python
# LC 1136
def minimumSemesters(n, relations):
    """
    Find minimum number of semesters to take all courses.
    Time: O(V + E), Space: O(V)
    """
    from collections import defaultdict, deque
    
    graph = defaultdict(list)
    in_degree = [0] * (n + 1)
    
    for pre, next_course in relations:
        graph[pre].append(next_course)
        in_degree[next_course] += 1
    
    queue = deque([i for i in range(1, n + 1) if in_degree[i] == 0])
    semesters = 0
    studied = 0
    
    while queue:
        # Process all courses in current semester
        semesters += 1
        next_queue = []
        
        for _ in range(len(queue)):
            course = queue.popleft()
            studied += 1
            
            for next_course in graph[course]:
                in_degree[next_course] -= 1
                if in_degree[next_course] == 0:
                    next_queue.append(next_course)
        
        queue.extend(next_queue)
    
    return semesters if studied == n else -1
```

## Cycle Detection & Safe States

### 6) Find Eventual Safe States — LC 802

```python
# LC 802
def eventualSafeNodes(graph):
    """
    Find all safe nodes (nodes from which all paths lead to terminal).
    Time: O(V + E), Space: O(V)
    """
    n = len(graph)
    # Reverse the graph
    reverse_graph = [[] for _ in range(n)]
    out_degree = [0] * n
    
    for i in range(n):
        for j in graph[i]:
            reverse_graph[j].append(i)
        out_degree[i] = len(graph[i])
    
    # Start with terminal nodes (out-degree = 0)
    from collections import deque
    queue = deque([i for i in range(n) if out_degree[i] == 0])
    safe = []
    
    while queue:
        node = queue.popleft()
        safe.append(node)
        
        for prev in reverse_graph[node]:
            out_degree[prev] -= 1
            if out_degree[prev] == 0:
                queue.append(prev)
    
    return sorted(safe)
```

## Undirected Graphs — Components & Centroids

### 7) Minimum Height Trees — LC 310


```java
// java
// LC 310
// IDEA: Tree Centroid Finding - Leaf Trimming (Topological Sort for Undirected Trees)
public class MinimumHeightTrees {

    /**
     * Core Concept: Tree Centroid Finding via Leaf Trimming
     *
     * This is a special application of topological sort to UNDIRECTED TREES:
     *
     * Key Differences from Standard Topological Sort:
     * 1. Works on UNDIRECTED trees (not DAGs)
     * 2. Uses degree (not in-degree) - count all edges
     * 3. Leaves are nodes with degree = 1 (not in-degree = 0)
     * 4. Goal: Find centroid(s), not linear ordering
     * 5. Result: 1 or 2 nodes (tree centers), not all nodes
     *
     * Algorithm (Leaf Trimming):
     * 1. Build adjacency list + track degrees for undirected edges
     * 2. Put all leaves (degree = 1) into queue
     * 3. Remove leaves layer by layer (like peeling an onion)
     * 4. When neighbors' degree becomes 1, they become new leaves
     * 5. Stop when ≤ 2 nodes remain - these are the centroids
     *
     * Why it works:
     * - The centroid(s) of a tree minimize the maximum distance to any leaf
     * - By removing outer layers, we converge to the center
     * - A tree can have at most 2 centroids (if diameter is even: 2, if odd: 1)
     *
     * Time: O(N) - Each node and edge processed once
     * Space: O(N) - Adjacency list and queue storage
     */
    public List<Integer> findMinHeightTrees(int n, int[][] edges) {
        // Edge case: single node tree
        if (n == 1) {
            return Collections.singletonList(0);
        }

        // Step 1: Build adjacency list and track degrees
        List<List<Integer>> graph = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            graph.add(new ArrayList<>());
        }

        int[] degree = new int[n];

        // For undirected tree: add edges in both directions
        for (int[] edge : edges) {
            int u = edge[0], v = edge[1];
            graph.get(u).add(v);
            graph.get(v).add(u);
            degree[u]++;
            degree[v]++;
        }

        // Step 2: Initialize queue with all leaf nodes (degree = 1)
        Queue<Integer> leaves = new LinkedList<>();
        for (int i = 0; i < n; i++) {
            if (degree[i] == 1) {
                leaves.offer(i);
            }
        }

        // Step 3: Trim leaves layer by layer
        int remaining = n;

        // Continue until only 1 or 2 nodes remain
        while (remaining > 2) {
            int leafCount = leaves.size();
            remaining -= leafCount;

            // Process all leaves in current layer
            for (int i = 0; i < leafCount; i++) {
                int leaf = leaves.poll();

                // Update degrees of neighbors
                for (int neighbor : graph.get(leaf)) {
                    degree[neighbor]--;

                    // If neighbor becomes a leaf, add to queue for next layer
                    if (degree[neighbor] == 1) {
                        leaves.offer(neighbor);
                    }
                }
            }
        }

        // Step 4: The remaining nodes (1 or 2) are the centroids
        return new ArrayList<>(leaves);
    }
}
```

```python
# python
# LC 310
# V0 - Tree Centroid Finding (Leaf Trimming for Undirected Trees)
from collections import deque

class Solution:
    def findMinHeightTrees(self, n: int, edges: List[List[int]]) -> List[int]:
        """
        Find tree centroids using leaf trimming.

        Key Pattern: Similar to Kahn's Algorithm but for undirected trees
        - Use degree (not in-degree)
        - Leaves are nodes with degree = 1
        - Trim layers until 1-2 nodes remain

        Time: O(N), Space: O(N)
        """
        # Edge case
        if n == 1:
            return [0]

        # Build adjacency list and track degrees
        graph = [[] for _ in range(n)]
        degree = [0] * n

        for u, v in edges:
            graph[u].append(v)
            graph[v].append(u)
            degree[u] += 1
            degree[v] += 1

        # Initialize queue with all leaves (degree = 1)
        leaves = deque([i for i in range(n) if degree[i] == 1])

        # Trim leaves layer by layer
        remaining = n
        while remaining > 2:
            leaf_count = len(leaves)
            remaining -= leaf_count

            for _ in range(leaf_count):
                leaf = leaves.popleft()

                # Decrease degree of neighbors
                for neighbor in graph[leaf]:
                    degree[neighbor] -= 1
                    # If neighbor becomes leaf, add to queue
                    if degree[neighbor] == 1:
                        leaves.append(neighbor)

        # Return remaining centroids (1 or 2 nodes)
        return list(leaves)


# V1 - Alternative: Using Set for adjacency (faster removal)
class Solution:
    def findMinHeightTrees(self, n: int, edges: List[List[int]]) -> List[int]:
        if n == 1:
            return [0]

        # Build graph with sets for O(1) removal
        graph = [set() for _ in range(n)]

        for u, v in edges:
            graph[u].add(v)
            graph[v].add(u)

        # Find initial leaves
        leaves = [i for i in range(n) if len(graph[i]) == 1]

        # Trim leaves until 1-2 nodes remain
        while n > 2:
            n -= len(leaves)
            new_leaves = []

            for leaf in leaves:
                # Get the only neighbor
                neighbor = graph[leaf].pop()
                # Remove leaf from neighbor's adjacency set
                graph[neighbor].remove(leaf)

                # If neighbor becomes leaf, add to next layer
                if len(graph[neighbor]) == 1:
                    new_leaves.append(neighbor)

            leaves = new_leaves

        return leaves
```

**Core Logic Explanation:**

1. **Why Leaf Trimming Works:**
   - Tree centroids minimize the height when used as roots
   - By removing outer layers (leaves), we converge to the center
   - Like peeling an onion from outside to inside

2. **Key Differences from Standard Topological Sort:**
   - **Graph Type**: Undirected tree vs Directed Acyclic Graph (DAG)
   - **Degree Tracking**: Total degree vs in-degree
   - **Leaf Definition**: degree = 1 vs in-degree = 0
   - **Goal**: Find center(s) vs Find linear ordering
   - **Result**: 1-2 nodes vs All nodes in order

3. **Why At Most 2 Centroids:**
   - If tree diameter is even → 2 center nodes
   - If tree diameter is odd → 1 center node
   - These nodes minimize the maximum distance to any leaf

**Core Idea — BFS / Layer Trimming (Onion Peeling):**
- Think of the tree as an onion. The MHT roots are in the **innermost layer**
- Start from the outermost leaves (degree = 1), peel them off layer by layer
- Each layer removal reveals new leaves (neighbors whose degree drops to 1)
- Stop when ≤ 2 nodes remain — these are the **centroids**
- This is **multi-source BFS from leaves inward**, NOT BFS from a single root

```text
[0,1,2,3,4]  (linear tree: 0-1-2-3-4)

→ remove 0,4       (outer leaves)
→ remove 1,3       (new leaves after trimming)
→ left with [2] ✅  (centroid)
```

**Pattern Recognition — When to Use Leaf Trimming:**

| Signal | Example |
|--------|---------|
| Find center/centroid of a tree | LC 310 |
| Minimize max distance from root to any leaf | LC 310 |
| Undirected tree + "optimal root" | LC 310, tree radius |
| Peel layers from outside inward | LC 310, onion structure |
| NOT a DAG, NOT directed edges | Use this instead of standard topo sort |

**Key Observations:**
- A tree has at most **2 centroids**: diameter even → 2 centers, diameter odd → 1 center
- Brute force (BFS from every node) is O(N²) — TLE for large inputs
- Leaf trimming achieves O(N) by processing each node and edge exactly once
- Can use `Set<Integer>` adjacency for O(1) removal, or `int[] degree` array (simpler, preferred)

**Classic Similar LCs:**

| LC # | Problem | Connection |
|------|---------|------------|
| 310 | Minimum Height Trees | Core leaf trimming problem |
| 207 | Course Schedule | Kahn's algo — same BFS + degree pattern on DAG |
| 210 | Course Schedule II | Kahn's algo — produces ordering |
| 834 | Sum of Distances in Tree | Tree centroid / rerooting DP |
| 1245 | Tree Diameter | Find diameter → centroid is at midpoint |
| 2603 | Collect Coins in a Tree | Leaf trimming to remove unnecessary nodes |
| 1377 | Frog Position After T Seconds | BFS on tree from root |
| 863 | All Nodes Distance K in Binary Tree | BFS from a node in tree |

### 8) Number of Provinces — LC 547


```java
// java
// LC 547
// ref: leetcode_java/src/main/java/LeetCodeJava/DFS/NumberOfProvinces.java

public class NumberOfProvinces {

    // V0
    // IDEA: UNION FIND
    /**
     * time = O(N^2 * α(N))
     * space = O(N)
     */
    public int findCircleNum(int[][] isConnected) {
        int n = isConnected.length;
        UnionFind uf = new UnionFind(n);

        for (int y = 0; y < n; y++) {
            for (int x = y + 1; x < n; x++) {
                if (isConnected[y][x] == 1) {
                    uf.union(y, x);
                }
            }
        }

        return uf.cluster;
    }

    class UnionFind {
        int[] parents;
        int cluster;

        UnionFind(int n) {
            this.parents = new int[n];
            this.cluster = n;
            for (int i = 0; i < n; i++) {
                this.parents[i] = i;
            }
        }

        public void union(int x, int y) {
            int parentX = find(x);
            int parentY = find(y);
            if (parentX != parentY) {
                this.parents[parentX] = parentY;
                this.cluster -= 1;
            }
        }

        public int find(int x) {
            if (this.parents[x] != x) {
                this.parents[x] = find(this.parents[x]); // path compression
            }
            return this.parents[x];
        }
    }

    // V1
    // IDEA: DFS directly on adjacency matrix
    /**
     * time = O(N^2)
     * space = O(N)
     */
    public int findCircleNum_dfs(int[][] isConnected) {
        int n = isConnected.length;
        boolean[] visited = new boolean[n];
        int provinces = 0;

        for (int i = 0; i < n; i++) {
            if (!visited[i]) {
                dfs(isConnected, visited, i, n);
                provinces++;
            }
        }

        return provinces;
    }

    private void dfs(int[][] isConnected, boolean[] visited, int city, int n) {
        visited[city] = true;
        for (int j = 0; j < n; j++) {
            if (isConnected[city][j] == 1 && !visited[j]) {
                dfs(isConnected, visited, j, n);
            }
        }
    }

    // V2
    // IDEA: BFS
    /**
     * time = O(N^2)
     * space = O(N)
     */
    public int findCircleNum_bfs(int[][] isConnected) {
        int n = isConnected.length;
        boolean[] visited = new boolean[n];
        int provinces = 0;

        for (int i = 0; i < n; i++) {
            if (!visited[i]) {
                provinces++;
                Queue<Integer> q = new LinkedList<>();
                q.offer(i);
                visited[i] = true;

                while (!q.isEmpty()) {
                    int node = q.poll();
                    for (int j = 0; j < n; j++) {
                        if (isConnected[node][j] == 1 && !visited[j]) {
                            q.offer(j);
                            visited[j] = true;
                        }
                    }
                }
            }
        }

        return provinces;
    }
}
```

```python
# python
# LC 547 Number of Provinces
# V0 - Union Find
class UnionFind:
    def __init__(self, n):
        self.parent = list(range(n))
        self.count = n

    def find(self, x):
        if self.parent[x] != x:
            self.parent[x] = self.find(self.parent[x])
        return self.parent[x]

    def union(self, x, y):
        px, py = self.find(x), self.find(y)
        if px != py:
            self.parent[px] = py
            self.count -= 1

class Solution:
    def findCircleNum(self, isConnected):
        n = len(isConnected)
        uf = UnionFind(n)
        for i in range(n):
            for j in range(i + 1, n):
                if isConnected[i][j] == 1:
                    uf.union(i, j)
        return uf.count

# V1 - DFS
class Solution:
    def findCircleNum(self, isConnected):
        n = len(isConnected)
        visited = [False] * n
        provinces = 0

        def dfs(city):
            visited[city] = True
            for j in range(n):
                if isConnected[city][j] == 1 and not visited[j]:
                    dfs(j)

        for i in range(n):
            if not visited[i]:
                dfs(i)
                provinces += 1
        return provinces
```

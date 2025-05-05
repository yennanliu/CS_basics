# Topology sorting

## 0) Concept

- Topological Sort is an algorithm can find `ordering` based on `order dependency` graph

- Concept
	- [techbridge : topological-sort](https://blog.techbridge.cc/2020/05/10/leetcode-topological-sort/)
	- [do topological-sort via DFS](https://alrightchiu.github.io/SecondRound/graph-li-yong-dfsxun-zhao-dagde-topological-sorttuo-pu-pai-xu.html)
- Code
	- [topological_sort.py](https://github.com/yennanliu/CS_basics/blob/master/algorithm/python/topological_sort.py)
    - [TopologicalSort.java](https://github.com/yennanliu/CS_basics/blob/master/leetcode_java/src/main/java/AlgorithmJava/TopologicalSort.java)

### 0-1) Types

- Courses
    - LC 207, LC 210
- Sequence
    - LC 444
- Alien Dictionary
    - LC 269
- Others
    - LC 666, 802

### 0-2) Pattern


- V1
    - 1. `degrees` : `array` or `hashmap` : record `ordering` of element
    - 2. `map` : maintain relation between node and `next nodes`
    - 3. BFS : the way access candidates

- V2

```
# pseudo code
# https://leetcode.com/problems/course-schedule/solution/

L = Empty list that will contain the sorted elements
S = Set of all nodes with no incoming edge

while S is non-empty do
    remove a node n from S
    add n to tail of L
    for each node m with an edge e from n to m do
        remove edge e from the graph
        if m has no other incoming edges then
            insert m into S

if graph has edges then
    return error (graph has at least one cycle)
else 
    return L (a topologically sorted order)
```

## 1) General form

```python
# V0
# IDEA : implement topologicalSortUtil, topologicalSort, and addEdge methods
# step 1) maintain a stack, save "ordering" nodes in it (and return in final step)
# step 2) init visited as [False]*self.V  (all nodes are NOT visited yet)
# step 3) iterate over all vertices in graph, if not visited, then run topologicalSortUtil
# step 4) return result (stack)
from collections import defaultdict
class Graph:
    def __init__(self, vertices):
        self.graph = defaultdict(list)
        self.V = vertices

    # for build graph
    def addEdge(self, u, v):
        self.graph[u].append(v)

    def topologicalSortUtil(self, v, visited, stack):
        visited[v] = True

        ### NOTE this !!! (self.graph[v])
        for k in self.graph[v]:
            if visited[k] == False:
                self.topologicalSortUtil(k, visited, stack)
        # stack.insert(0,v) # instead of insert v to idx = 0, we can still append v to stack and reverse it and return (e.g. return stack[::-1])
        """
        ### NOTE !! stack.append(v) is wrong, we SHOULD use  stack.insert(0,v)
        """
        stack.insert(0,v)

    def topologicalSort(self):
        visited = [False] * self.V
        stack = []
        ### NOTE this !!! (range(self.V))
        for v in range(self.V):
            # call tologicalSortUtil only if visited[v] == False (the vertice is not visited yet)
            if visited[v] == False:
                self.topologicalSortUtil(v, visited, stack)
        # return the result in inverse order
        return stack[::-1]

### TEST
{"A": 0, "B":1, "C":2, "D": 3}
v = 4
g = Graph(v)
g.addEdge(0, 1)
g.addEdge(0, 2)
g.addEdge(2, 3)
g.addEdge(3, 1)

print (g.graph)

# ans should be TableB, TableD, TableC, TableA.
r = g.topologicalSort()
print (r)
```

### 1-1) Basic OP

## 2) LC Example

### 2-2) Course Schedule II

```java
// java
// LC 210
public class CourseSchedule2 {

    // V0
    // IDEA : TOPOLOGICAL SORT (fixed by gpt)
    // ref : https://github.com/yennanliu/CS_basics/blob/master/leetcode_java/src/main/java/AlgorithmJava/TopologicalSortV2.java
    public int[] findOrder(int numCourses, int[][] prerequisites) {
        if (numCourses == 1) {
            return new int[]{0};
        }

        // topologic ordering
        List<Integer> ordering = topologicalSort(numCourses, prerequisites);
        //System.out.println(">>> ordering = " + ordering);
        if (ordering == null){
            return new int[]{};
        }
        int[] res = new int[numCourses];
        for (int x = 0; x < ordering.size(); x++) {
            int val = ordering.get(x);
            //System.out.println(val);
            res[x] = val;
        }

        return res;
    }

    public List<Integer> topologicalSort(int numNodes, int[][] edges) {
        // Step 1: Build the graph and calculate in-degrees
        Map<Integer, List<Integer>> graph = new HashMap<>();
        int[] inDegree = new int[numNodes];

        for (int i = 0; i < numNodes; i++) {
            graph.put(i, new ArrayList<>());
        }

        for (int[] edge : edges) {
            int from = edge[0];
            int to = edge[1];
            graph.get(from).add(to);
            inDegree[to]++;
        }

        // Step 2: Initialize a queue with nodes that have in-degree 0
        Queue<Integer> queue = new LinkedList<>();
        for (int i = 0; i < numNodes; i++) {
            /**
             * NOTE !!!
             *
             *  we add ALL nodes with degree = 0 to queue at init step
             */
            if (inDegree[i] == 0) {
                queue.offer(i);
            }
        }

        List<Integer> topologicalOrder = new ArrayList<>();

        // Step 3: Process the nodes in topological order
        while (!queue.isEmpty()) {
            /**
             * NOTE !!!
             *
             *  ONLY "degree = 0"  nodes CAN be added to queue
             *
             *  -> so we can add whatever node from queue to final result (topologicalOrder)
             */
            int current = queue.poll();
            topologicalOrder.add(current);

            for (int neighbor : graph.get(current)) {
                inDegree[neighbor] -= 1;
                /**
                 * NOTE !!!
                 *
                 *  if a node "degree = 0"  means this node can be ACCESSED now,
                 *
                 *  -> so we need to add it to the queue (for adding to topologicalOrder in the following while loop iteration)
                 */
                if (inDegree[neighbor] == 0) {
                    queue.offer(neighbor);
                }
            }
        }

        // If topologicalOrder does not contain all nodes, there was a cycle in the graph
        if (topologicalOrder.size() != numNodes) {
            //throw new IllegalArgumentException("The graph has a cycle, so topological sort is not possible.");
            return null;
        }

        /** NOTE !!! reverse ordering */
        Collections.reverse(topologicalOrder);
        return topologicalOrder;
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

### 2-2) Course Schedule

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

### 2-3) alien-dictionary

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

### 2-3) Sequence Reconstruction

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
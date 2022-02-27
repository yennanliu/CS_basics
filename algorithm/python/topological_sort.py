#---------------------------------------------------------------
# ALGORITHM DEMO : TOPLOGICAL SORT
#---------------------------------------------------------------

# Topological Sort is a algorithm can find "ordering" on an "order dependency" graph
# Concept
# https://blog.techbridge.cc/2020/05/10/leetcode-topological-sort/
# https://alrightchiu.github.io/SecondRound/graph-li-yong-dfsxun-zhao-dagde-topological-sorttuo-pu-pai-xu.html

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
        stack.append(v)

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

# V0'
# IDEA : implement topologicalSortUtil, topologicalSort, and addEdge methods
from collections import defaultdict 
class Graph: 
    def __init__(self,vertices): 
        self.graph = defaultdict(list) 
        self.V = vertices
  
    # for testing (build graph)
    def addEdge(self,u,v): 
        self.graph[u].append(v) 
  
    def topologicalSortUtil(self,v,visited,stack): 
  
        visited[v] = True
  
        for i in self.graph[v]: 
            if visited[i] == False: 
                self.topologicalSortUtil(i,visited,stack) 
  
        stack.insert(0,v) 
  
    def topologicalSort(self): 
        visited = [False]*self.V 
        stack =[] 
  
        for i in range(self.V): 
            if visited[i] == False: 
                self.topologicalSortUtil(i,visited,stack) 
  
        print (stack) 

# V1
# https://www.geeksforgeeks.org/topological-sorting/
# Python program to print topological sorting of a DAG
from collections import defaultdict
class Graph:
    def __init__(self, vertices):
        self.graph = defaultdict(list) # dictionary containing adjacency List
        self.V = vertices # No. of vertices

    # function to add an edge to graph
    def addEdge(self, u, v):
        self.graph[u].append(v)

    # A recursive function used by topologicalSort
    def topologicalSortUtil(self, v, visited, stack):

        # Mark the current node as visited.
        visited[v] = True

        # Recur for all the vertices adjacent to this vertex
        for i in self.graph[v]:
            if visited[i] == False:
                self.topologicalSortUtil(i, visited, stack)

        # Push current vertex to stack which stores result
        stack.append(v)

    # The function to do Topological Sort. It uses recursive
    # topologicalSortUtil()
    def topologicalSort(self):
        # Mark all the vertices as not visited
        visited = [False]*self.V
        stack = []

        # Call the recursive helper function to store Topological
        # Sort starting from all vertices one by one
        for i in range(self.V):
            if visited[i] == False:
                self.topologicalSortUtil(i, visited, stack)

        # Print contents of the stack
        print(stack[::-1]) # return list in reverse order

# TEST
# Driver Code
# g = Graph(6)
# g.addEdge(5, 2)
# g.addEdge(5, 0)
# g.addEdge(4, 0)
# g.addEdge(4, 1)
# g.addEdge(2, 3)
# g.addEdge(3, 1)
#
# print ("Following is a Topological Sort of the given graph")
#
# # Function Call
# g.topologicalSort()


# V1
# https://github.com/TheAlgorithms/Python/blob/master/sorts/topological_sort.py
"""Topological Sort."""

#     a
#    / \
#   b  c
#  / \
# d  e
# edges = {"a": ["c", "b"], "b": ["d", "e"], "c": [], "d": [], "e": []}
# vertices = ["a", "b", "c", "d", "e"]
class Graph:
    def topological_sort(self, start, visited, sort):
        """Perform topological sort on a directed acyclic graph."""
        current = start
        # add current to visited
        visited.append(current)
        neighbors = edges[current]
        for neighbor in neighbors:
            # if neighbor not in visited, visit
            if neighbor not in visited:
                sort = topological_sort(neighbor, visited, sort)
        # if all neighbors visited add current to sort
        sort.append(current)
        # if all vertices haven't been visited select a new one to visit
        if len(visited) != len(vertices):
            for vertice in vertices:
                if vertice not in visited:
                    sort = topological_sort(vertice, visited, sort)
        # return sort
        return sort

# TEST
# edges = {"a": ["c", "b"], "b": ["d", "e"], "c": [], "d": [], "e": []}
# vertices = ["a", "b", "c", "d", "e"]
# sort = topological_sort("a", [], [])
# print(sort)

# V1'
# http://www.runoob.com/python3/python-topological-sorting.html
class Graph: 
    from collections import defaultdict 
    def __init__(self,vertices): 
        self.graph = defaultdict(list) 
        self.V = vertices
  
    def addEdge(self,u,v): 
        self.graph[u].append(v) 
  
    def topologicalSortUtil(self,v,visited,stack): 
  
        visited[v] = True
  
        for i in self.graph[v]: 
            if visited[i] == False: 
                self.topologicalSortUtil(i,visited,stack) 
  
        stack.insert(0,v) 
  
    def topologicalSort(self): 
        visited = [False]*self.V 
        stack =[] 
  
        for i in range(self.V): 
            if visited[i] == False: 
                self.topologicalSortUtil(i,visited,stack) 
        print (stack) 

# TEST  
# g= Graph(6) 
# g.addEdge(5, 2); 
# g.addEdge(5, 0); 
# g.addEdge(4, 0); 
# g.addEdge(4, 1); 
# g.addEdge(2, 3); 
# g.addEdge(3, 1); 
# print ("output of Topological Sort ")
# g.topologicalSort()
# [5, 4, 2, 3, 1, 0]

# V2 
# https://zhuanlan.zhihu.com/p/69858335
def topoSort(graph):     
    in_degrees = dict((u,0) for u in graph)   # init (value with 0)     
    num = len(in_degrees)     
    for u in graph:         
        for v in graph[u]:             
            in_degrees[v] += 1        
    Q = [u for u in in_degrees if in_degrees[u] == 0]       
    Seq = []     
    while Q:         
        u = Q.pop()             
        Seq.append(u)         
        for v in graph[u]:             
            in_degrees[v] -= 1    
            if in_degrees[v] == 0:        
                Q.append(v)          
    if len(Seq) == num:      
        return Seq     
    else:         
        return None

# TEST
# G = {
#     'a':'bf',
#     'b':'cdf',
#     'c':'d',
#     'd':'ef',
#     'e':'f',
#     'f':''
# }
# print(topoSort(G))
# ['a', 'b', 'c', 'd', 'e', 'f']

# V3
# https://www.educative.io/courses/grokking-the-coding-interview/m25rBmwLV00
from collections import deque
def topological_sort(vertices, edges):
    sortedOrder = []
    if vertices <= 0:
        return sortedOrder

    # a. Initialize the graph
    inDegree = {i: 0 for i in range(vertices)}  # count of incoming edges
    graph = {i: [] for i in range(vertices)}  # adjacency list graph

    # b. Build the graph
    for edge in edges:
        parent, child = edge[0], edge[1]
        graph[parent].append(child)  # put the child into it's parent's list
        inDegree[child] += 1  # increment child's inDegree

    # c. Find all sources i.e., all vertices with 0 in-degrees
    sources = deque()
    for key in inDegree:
        if inDegree[key] == 0:
          sources.append(key)

    # d. For each source, add it to the sortedOrder and subtract one from all of its children's in-degrees
    # if a child's in-degree becomes zero, add it to the sources queue
    while sources:
        vertex = sources.popleft()
        sortedOrder.append(vertex)
        for child in graph[vertex]:  # get the node's children to decrement their in-degrees
              inDegree[child] -= 1
              if inDegree[child] == 0:
                sources.append(child)

    # topological sort is not possible as the graph has a cycle
    if len(sortedOrder) != vertices:
        return []

    return sortedOrder

# TEST
# def main():
#   print("Topological sort: " +
#         str(topological_sort(4, [[3, 2], [3, 0], [2, 0], [2, 1]])))
#   print("Topological sort: " +
#         str(topological_sort(5, [[4, 2], [4, 3], [2, 0], [2, 1], [3, 1]])))
#   print("Topological sort: " +
#         str(topological_sort(7, [[6, 4], [6, 2], [5, 3], [5, 4], [3, 0], [3, 1], [3, 2], [4, 1]])))
#main()
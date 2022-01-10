#################################################################
# ALGORITHM DEMO : TOPLOGICAL SORT
#################################################################

# V0

# V1
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

def main():
  print("Topological sort: " +
        str(topological_sort(4, [[3, 2], [3, 0], [2, 0], [2, 1]])))
  print("Topological sort: " +
        str(topological_sort(5, [[4, 2], [4, 3], [2, 0], [2, 1], [3, 1]])))
  print("Topological sort: " +
        str(topological_sort(7, [[6, 4], [6, 2], [5, 3], [5, 4], [3, 0], [3, 1], [3, 2], [4, 1]])))

#main()
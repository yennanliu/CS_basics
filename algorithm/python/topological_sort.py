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
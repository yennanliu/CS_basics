#################################################################
# DATA STRUCTURE DEMO : Graph 
#################################################################

# V0
# https://github.com/yennanliu/CS_basics/blob/master/data_structure/js/graph.js
class Graph(object):

    def __init__(self):
        self.numberOfNodes = 0
        self.adjacentList = {}

    def addVertex(self, node):
        self.adjacentList[node] = []
        self.numberOfNodes += 1 

    def addEdge(self, node1, node2):
        self.adjacentList[node1].append(node2)
        self.adjacentList[node2].append(node1)

    def showConnections(self):
        allNodes = self.adjacentList.keys()
        for node in allNodes:
            nodeConnections = self.adjacentList[node]
            connections = ""
            for vertex in nodeConnections:
                connections += vertex + " "
            print (node + "-->" + connections)
            
# myGraph = Graph();
# myGraph.addVertex('0');
# myGraph.addVertex('1');
# myGraph.addVertex('2');
# myGraph.addVertex('3');
# myGraph.addVertex('4');
# myGraph.addVertex('5');
# myGraph.addVertex('6');
# myGraph.addEdge('3', '1'); 
# myGraph.addEdge('3', '4'); 
# myGraph.addEdge('4', '2'); 
# myGraph.addEdge('4', '5'); 
# myGraph.addEdge('1', '2'); 
# myGraph.addEdge('1', '0'); 
# myGraph.addEdge('0', '2'); 
# myGraph.addEdge('6', '5');
# myGraph.showConnections(); 
# OUTPUT:
# 0-->1 2 
# 1-->3 2 0 
# 2-->4 1 0 
# 3-->1 4 
# 4-->3 2 5 
# 5-->4 6 
# 6-->5 

# V1
# https://github.com/OmkarPathak/Data-Structures-using-Python/blob/master/Graph/Graph.py
class AdjacencyList(object):
    def __init__(self):
        self.List = {}

    def addEdge(self, fromVertex, toVertex):
        # check if vertex is already present
        if fromVertex in list(self.List.keys()):
            self.List[fromVertex].append(toVertex)
        else:
            self.List[fromVertex] = [toVertex]

    def printList(self):
        for i  in self.List:
            print((i,'->',' -> '.join([str(j) for j in self.List[i]])))

# al = AdjacencyList()
# al.addEdge(0, 1)
# al.addEdge(0, 4)
# al.addEdge(4, 1)
# al.addEdge(4, 3)
# al.addEdge(1, 0)
# al.addEdge(1, 4)
# al.addEdge(1, 3)
# al.addEdge(1, 2)
# al.addEdge(2, 3)
# al.addEdge(3, 4)
# al.printList()

# # OUTPUT:
# # 0 -> 1 -> 4
# # 1 -> 0 -> 4 -> 3 -> 2
# # 2 -> 3
# # 3 -> 4
# # 4 -> 1 -> 3

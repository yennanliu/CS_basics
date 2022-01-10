#################################################################
# ALGORITHM DEMO : Dijkstra’s shortest path algorithm
#################################################################

# V0

# V1
# https://www.geeksforgeeks.org/dijkstras-shortest-path-algorithm-greedy-algo-7/
# https://www.geeksforgeeks.org/python-program-for-dijkstras-shortest-path-algorithm-greedy-algo-7/
# Python program for Dijkstra's single 
# source shortest path algorithm. The program is 
# for adjacency matrix representation of the graph 
# Library for INT_MAX 
import sys 
class Graph(): 

    def __init__(self, vertices): 
        self.V = vertices 
        self.graph = [[0 for column in range(vertices)] 
                    for row in range(vertices)] 

    def printSolution(self, dist): 
        print ("Vertex tDistance from Source")
        for node in range(self.V): 
            print (node, "t", dist[node])

    # A utility function to find the vertex with 
    # minimum distance value, from the set of vertices 
    # not yet included in shortest path tree 
    def minDistance(self, dist, sptSet): 

        # Initilaize minimum distance for next node 
        min = sys.maxsize

        # Search not nearest vertex not in the 
        # shortest path tree 
        for v in range(self.V): 
            if dist[v] < min and sptSet[v] == False: 
                min = dist[v] 
                min_index = v 

        return min_index 

    # Funtion that implements Dijkstra's single source 
    # shortest path algorithm for a graph represented 
    # using adjacency matrix representation 
    def dijkstra(self, src): 

        dist = [sys.maxsize] * self.V 
        dist[src] = 0
        sptSet = [False] * self.V 

        for cout in range(self.V): 

            # Pick the minimum distance vertex from 
            # the set of vertices not yet processed. 
            # u is always equal to src in first iteration 
            u = self.minDistance(dist, sptSet) 

            # Put the minimum distance vertex in the 
            # shotest path tree 
            sptSet[u] = True

            # Update dist value of the adjacent vertices 
            # of the picked vertex only if the current 
            # distance is greater than new distance and 
            # the vertex in not in the shotest path tree 
            for v in range(self.V): 
                if self.graph[u][v] > 0 \
                and sptSet[v] == False \
                and dist[v] > dist[u] + self.graph[u][v]: 
                    dist[v] = dist[u] + self.graph[u][v] 

        self.printSolution(dist) 

# Driver program 
g = Graph(9) 
g.graph = [[0, 4, 0, 0, 0, 0, 0, 8, 0], 
        [4, 0, 8, 0, 0, 0, 0, 11, 0], 
        [0, 8, 0, 7, 0, 4, 0, 0, 2], 
        [0, 0, 7, 0, 9, 14, 0, 0, 0], 
        [0, 0, 0, 9, 0, 10, 0, 0, 0], 
        [0, 0, 4, 14, 10, 0, 2, 0, 0], 
        [0, 0, 0, 0, 0, 2, 0, 1, 6], 
        [8, 11, 0, 0, 0, 0, 1, 0, 7], 
        [0, 0, 2, 0, 0, 0, 6, 7, 0] 
        ]; 
g.dijkstra(0)

# V2
# https://www.educative.io/edpresso/how-to-implement-dijkstras-algorithm-in-python
import sys
# Function to find out which of the unvisited node 
# needs to be visited next
def to_be_visited():
  global visited_and_distance
  v = -10
  # Choosing the vertex with the minimum distance
  for index in range(number_of_vertices):
    if visited_and_distance[index][0] == 0 \
      and (v < 0 or visited_and_distance[index][1] <= \
      visited_and_distance[v][1]):
        v = index
  return v

def dijkstra()
    # Creating the graph as an adjacency matrix
    vertices = [[0, 1, 1, 0],
                [0, 0, 1, 0],
                [0, 0, 0, 1],
                [0, 0, 0, 0]]
    edges =  [[0, 3, 4, 0],
              [0, 0, 0.5, 0],
              [0, 0, 0, 1],
              [0, 0, 0, 0]]

    number_of_vertices = len(vertices[0])

    # The first element of the lists inside visited_and_distance 
    # denotes if the vertex has been visited.
    # The second element of the lists inside the visited_and_distance 
    # denotes the distance from the source.
    visited_and_distance = [[0, 0]]
    for i in range(number_of_vertices-1):
      visited_and_distance.append([0, sys.maxsize])

    for vertex in range(number_of_vertices):
      # Finding the next vertex to be visited.
      to_visit = to_be_visited()
      for neighbor_index in range(number_of_vertices):
        # Calculating the new distance for all unvisited neighbours
        # of the chosen vertex.
        if vertices[to_visit][neighbor_index] == 1 and \
         visited_and_distance[neighbor_index][0] == 0:
          new_distance = visited_and_distance[to_visit][1] \
          + edges[to_visit][neighbor_index]
          # Updating the distance of the neighbor if its current distance
          # is greater than the distance that has just been calculated
          if visited_and_distance[neighbor_index][1] > new_distance:
            visited_and_distance[neighbor_index][1] = new_distance
        # Visiting the vertex found earlier
        visited_and_distance[to_visit][0] = 1

    i = 0 
    # Printing out the shortest distance from the source to each vertex       
    for distance in visited_and_distance:
      print("The shortest distance of ",chr(ord('a') + i),\
      " from the source vertex a is:",distance[1])
      i = i + 1

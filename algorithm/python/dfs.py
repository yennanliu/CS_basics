#---------------------------------------------------------------
#  DFS
#---------------------------------------------------------------

# BFS VS DFS 
# https://cuijiahua.com/blog/2018/01/alogrithm_10.html

# V0 : DEV 

# V1 
# DFS in recursion version 
# https://www.jianshu.com/p/66508acedd79
def dfs_recursion(G,s,S=None,res=None):
    if S is None:
        # save visited nodes  
        S=set()
    if res is None:
        res=[]
    res.append(s)
    S.add(s)
    for u in G[s]:
        if u in S:
            continue
        S.add(u)
        dfs_recursion(G,u,S,res)
    return res

# G={
#     'a':{'b','f'},
#     'b':{'c','d','f'},
#     'c':{'d'},
#     'd':{'e','f'},
#     'e':{'f'},
#     'f':{}
# }
# res=dfs(G,'a')
# print(res)

# DFS in Iteration verison 
def dfs_iteration(G,s):
    Q=[]
    S=set()
    Q.append(s)
    while Q:
        u=Q.pop()
        if u in S:
            continue
        S.add(u)
        Q.extend(G[u])
        yield u

# G={
#     'a':{'b','f'},
#     'b':{'c','d','f'},
#     'c':{'d'},
#     'd':{'e','f'},
#     'e':{'f'},
#     'f':{}
# }

# res=list(dfs(G,'a'))
# print(res) 


# V1 : dev 

# class Solution(object):
#     def maxProfitAssignment(self, difficulty, profit, worker):
#         """
#         :type difficulty: List[int]
#         :type profit: List[int]
#         :type worker: List[int]
#         :rtype: int
#         """
#         jobs = []
#         output = 0
#         jobs.sort()
#         worker.sort()
#         for w_i, w_j in enumerate(worker):
#             for d_i, d_j in enumerate(difficulty):
#                 print (w_j, d_j)
#                 if w_j < d_j and  w_j < difficulty[d_i+1] and  w_j < difficulty[d_i-1] :
#                     jobs.append(0)
#                     break 
#                 elif w_j < d_j and  ( w_j == difficulty[d_i+1] or  w_j >= difficulty[d_i -1] ): 
#                     jobs.append(difficulty[d_i -1])
#                     break
#                 else:
#                     pass
#         j_p = dict(zip(difficulty,profit))
#         j_p[0] = 0
#         for job in jobs:
#             output += j_p[job]
#         return output 
        
          
         

# V2 
# Time:  O(mlogm + nlogn), m is the number of workers,
#                        , n is the number of jobs
# Space: O(n)

class Solution(object):
    def maxProfitAssignment(self, difficulty, profit, worker):
        """
        :type difficulty: List[int]
        :type profit: List[int]
        :type worker: List[int]
        :rtype: int
        """
        jobs = list(zip(difficulty, profit))
        jobs.sort()
        worker.sort()
        result, i, max_profit = 0, 0, 0
        for ability in worker:
            while i < len(jobs) and jobs[i][0] <= ability:
                max_profit = max(max_profit, jobs[i][1])
                i += 1
            result += max_profit
        return result

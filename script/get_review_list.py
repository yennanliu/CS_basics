"""
py script generate Leetcode review list
by mnemonic learning with fibonacci sequence
"""

from datetime import datetime

class GetReviewList:

    def __init__(self):
        self.my_list=[]
        self.my_dict=[]
        self.current_date=datetime.now().strftime('%Y-%m-%d')

    def fib(self, n):
        """
        fibonacci_seq
        1,1,2,3,5,8,13,21,34....
        """
        if n <=1:
            return 1 
        return self.fib(n-2) + self.fib(n-1)

    def load_progress(self):
        filename="data/progress.txt"
        with open(filename, 'r') as f:
            for line in f:
                if line:
                    date, lc_ids= line.split(":")[0], line.split(":")[1]
                    #print (line)
                    print (date)

# g=GetReviewList()
# g.load_progress()
# print (g.current_date)
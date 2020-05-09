"""
py script generate Leetcode review list
by mnemonic learning with fibonacci sequence
"""

from datetime import datetime, timedelta, date

class GetReviewList:

    def __init__(self):
        self.my_list=[]
        self.my_dict=[]
        self.current_date=datetime.now()

    def process_datetime(self, datestring):
        """
        transform datetime str to datetime object for datetime op
        """
        return datetime.strptime(datestring,'%Y%m%d')

    def fib(self, n):
        """
        fibonacci_seq : 1,1,2,3,5,8,13,21,34....
        """
        if n <=1:
            return 1 
        return self.fib(n-2) + self.fib(n-1)

    def load_progress(self, filename):
        """
        load the progress file
        """
        with open(filename, 'r') as f:
            for line in f:
                if line:
                    date, lc_ids= line.split(":")[0], line.split(":")[1]
                    print (line)

    def get_fib_10(self):
        """
        get fibonacci sequence when n = 10, i.e. fib(10)
        """
        result=[]
        for i in range(10):
            result.append(self.fib(i))
        return list(result)

    def get_review_list(self):
        """
        generate the to-review list based on fibonacci sequence and progress.txt
        """
        filename="data/progress.txt"
        fib_seq=self.get_fib_10()
        with open(filename, 'r') as f:
            for line in f:
                if line:
                    date, lc_ids= line.split(":")[0], line.split(":")[1]
                    date_fix=self.process_datetime(date)
                    print (date_fix.strftime('%Y-%m-%d'), [ (date_fix + timedelta(days=day)).strftime('%Y-%m-%d') for day in fib_seq])

if __name__ == '__main__':
    g=GetReviewList()
    g.get_review_list()
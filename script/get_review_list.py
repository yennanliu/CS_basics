"""
py script generate Leetcode review list
by mnemonic learning with fibonacci sequence
"""

from datetime import datetime, timedelta, date
import collections

class GetReviewList:

    def __init__(self):
        self.lc_fib_sequence=collections.defaultdict(list)
        self.review_list=collections.defaultdict(list)
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

    def get_leetcode_fib_sequence(self):
        """
        get "leetcode id - date dict based on fibonacci sequence
        """
        progress="data/progress.txt"
        tmp="data/tmp.txt"
        fib_seq=self.get_fib_10()
        with open(progress, 'r') as f:
            for line in f:
                if line:
                    date, lc_ids= line.split(":")[0], str(line.split(":")[1].strip())
                    date_fix=self.process_datetime(date)
                    sequence_date= [(date_fix + timedelta(days=day)).strftime('%Y-%m-%d') for day in fib_seq]
                    data="{} : {}\n".format(lc_ids, sequence_date)
                    print (data)
                    self.lc_fib_sequence[lc_ids]=sequence_date

    def get_reivew_list(self):
        """
        generate the to-review list based on fibonacci sequence and progress.txt
        """
        self.get_leetcode_fib_sequence()
        for key in self.lc_fib_sequence.keys():
            for date in self.lc_fib_sequence[key]:
                self.review_list[date].append(key)

        print ("*"*30)
        #print (self.review_list)
        review_date = sorted(self.review_list.keys())
        print ("*"*30)
        # save to txt
        to_review="data/to_review.txt"
        with open(to_review, 'w') as f_r:
            for date in review_date:
                data="{} -> {}\n".format(date, str(self.review_list[date]))
                print (data)
                f_r.writelines(data)

if __name__ == '__main__':
    g=GetReviewList()
    g.get_reivew_list()
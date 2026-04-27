"""
Generate a LeetCode review schedule using spaced repetition (Fibonacci intervals).
"""

from datetime import datetime, timedelta
import collections


def _fib_sequence(n):
    """Return first n Fibonacci numbers (1-indexed: 1,1,2,3,5,...)."""
    a, b = 1, 1
    result = []
    for _ in range(n):
        result.append(a)
        a, b = b, a + b
    return result


class GetReviewList:

    def __init__(self):
        self.lc_fib_sequence = collections.defaultdict(list)
        self.review_list = collections.defaultdict(list)
        self.progress = "data/progress.txt"
        self.to_review = "data/to_review.txt"

    def process_datetime(self, datestring):
        try:
            return datetime.strptime(datestring, '%Y%m%d')
        except ValueError:
            return None

    def get_leetcode_fib_sequence(self):
        fib_seq = _fib_sequence(9)  # skip first 1 to match original [1:]
        with open(self.progress, 'r') as f:
            for line in f:
                if line and ':' in line:
                    date_str, lc_ids = line.split(":", 1)
                    lc_ids = lc_ids.strip()
                    date_fix = self.process_datetime(date_str)
                    if date_fix is None:
                        continue
                    sequence_date = [(date_fix + timedelta(days=day)).strftime('%Y-%m-%d') for day in fib_seq]
                    print("{} : {}\n".format(lc_ids, sequence_date))
                    self.lc_fib_sequence[lc_ids] = sequence_date

    def get_reivew_list(self):
        self.get_leetcode_fib_sequence()
        for key, dates in self.lc_fib_sequence.items():
            for date in dates:
                self.review_list[date].append(key)

        review_date = sorted(self.review_list.keys())
        print("*" * 30)
        print(review_date)
        print("*" * 30)

        with open(self.to_review, 'w') as f_r:
            for date in review_date[::-1]:
                data = "{} -> {}\n".format(date, str(self.review_list[date]))
                print(data)
                f_r.writelines(data)


if __name__ == '__main__':
    g = GetReviewList()
    g.get_reivew_list()
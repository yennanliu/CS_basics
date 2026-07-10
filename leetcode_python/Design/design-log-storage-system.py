# https://leetcode.ca/all/635.html

"""
635. Design Log Storage System
You are given several logs that each log contains a unique id and timestamp. Timestamp is a string that has the following format: Year:Month:Day:Hour:Minute:Second, for example, 2017:01:01:23:59:59. All domains are zero-padded decimal numbers.

Design a log storage system to implement the following functions:

void Put(int id, string timestamp): Given a log's unique id and timestamp, store the log in your storage system.


int[] Retrieve(String start, String end, String granularity): Return the id of logs whose timestamps are within the range from start to end. Start and end all have the same format as timestamp. However, granularity means the time level for consideration. For example, start = "2017:01:01:23:59:59", end = "2017:01:02:23:59:59", granularity = "Day", it means that we need to find the logs within the range from Jan. 1st 2017 to Jan. 2nd 2017.

Example 1:

put(1, "2017:01:01:23:59:59");
put(2, "2017:01:01:22:59:59");
put(3, "2016:01:01:00:00:00");
retrieve("2016:01:01:01:01:01","2017:01:01:23:00:00","Year"); // return [1,2,3], because you need to return all logs within 2016 and 2017.
retrieve("2016:01:01:01:01:01","2017:01:01:23:00:00","Hour"); // return [1,2], because you need to return all logs start from 2016:01:01:01 to 2017:01:01:23, where log 3 is left outside the range.
Note:

There will be at most 300 operations of Put or Retrieve.
Year ranges from [2000,2017]. Hour ranges from [00,23].
Output for Retrieve has no order required.
Difficulty:
Medium
"""


# V0
# class LogSystem:
#     def put(self, id: int, timestamp: str) -> None:
#     def retrieve(self, start: str, end: str, granularity: str) -> List[int]:



# V1
from typing import List

class LogSystem:

    # time = O(1)
    # space = O(1)
    def __init__(self):
        self.logs = []

        self.idx = {
            "Year": 4,
            "Month": 7,
            "Day": 10,
            "Hour": 13,
            "Minute": 16,
            "Second": 19
        }

    # time = O(1)
    # space = O(1)
    def put(self, id: int, timestamp: str) -> None:
        self.logs.append((id, timestamp))

    # time = O(n), n = number of stored logs
    # space = O(n) for the result list
    def retrieve(self, start: str, end: str, granularity: str) -> List[int]:

        k = self.idx[granularity]

        start = start[:k]
        end = end[:k]

        res = []

        for log_id, ts in self.logs:
            cur = ts[:k]

            """
            NOTE !!!

            -> 

            Lexicographical str comparisons op is 
            working on standard time formats!


            ->

            If the years match, 
                -> it checks the months: "2017:01..." is smaller than "2017:05...".

            If those match, 
                -> it checks the days: "2017:01:02..." is smaller than "2017:01:15...".

            """
            # Lexicographical string comparisons operate perfectly on standard time formats!
            if start <= cur <= end:
                res.append(log_id)

        return res



# V1-2
class LogSystem(object):

    # time = O(1)
    # space = O(1)
    def __init__(self):
        # A simple list storage is ideal because we need to scan logs without destroying a heap
        self.logs = []
        
        # Maps the granularity keyword to its truncation slicing index position in the timestamp string
        # "2017:01:01:23:59:59"
        #  Y    M  D  H  M  S
        self.granularity_map = {
            "Year": 4,      # "2017"
            "Month": 7,     # "2017:01"
            "Day": 10,     # "2017:01:01"
            "Hour": 13,    # "2017:01:01:23"
            "Minute": 16,  # "2017:01:01:23:59"
            "Second": 19   # Full string matching
        }
        
        # Suffixes used to fill out the remaining string safely for inclusive comparisons
        self.min_suffix = ":01:01:00:00:00"
        self.max_suffix = ":12:31:23:59:59"

    # time = O(1)
    # space = O(1)
    def put(self, id, timestamp):
        """
        :type id: int
        :type timestamp: str
        :rtype: None
        """
        self.logs.append((id, timestamp))

    # time = O(n), n = number of stored logs
    # space = O(n) for the result list
    def retrieve(self, start, end, granularity):
        """
        :type start: str
        :type end: str
        :type granularity: str
        :rtype: List[int]
        """
        # Determine how many characters we need to look at based on granularity
        idx = self.granularity_map[granularity]
        
        # Crop the timestamps to the granularity level and complete them with boundaries
        truncated_start = start[:idx] + self.min_suffix[idx:]
        truncated_end = end[:idx] + self.max_suffix[idx:]
        
        res = []
        for log_id, log_time in self.logs:
            """
            NOTE !!!

            -> 

            Lexicographical str comparisons op is 
            working on standard time formats!
            """
            # Lexicographical string comparisons operate perfectly on standard time formats!
            if truncated_start <= log_time <= truncated_end:
                res.append(log_id)
                
        return res


# V2-1
# https://leetcode.ca/2017-08-26-635-Design-Log-Storage-System/
class LogSystem:
    # time = O(1)
    # space = O(1)
    def __init__(self):
        self.logs = []
        self.d = {
            "Year": 4,
            "Month": 7,
            "Day": 10,
            "Hour": 13,
            "Minute": 16,
            "Second": 19,
        }

    # time = O(1)
    # space = O(1)
    def put(self, id: int, timestamp: str) -> None:
        self.logs.append((id, timestamp))

    # time = O(n), n = number of stored logs
    # space = O(n) for the result list
    def retrieve(self, start: str, end: str, granularity: str) -> List[int]:
        i = self.d[granularity]
        return [id for id, ts in self.logs if start[:i] <= ts[:i] <= end[:i]]





# V2-2
# https://leetcode.ca/2017-08-26-635-Design-Log-Storage-System/
class LogSystem:
    # time = O(1)
    # space = O(1)
    def __init__(self):
        self.logs = SortedDict()

    # time = O(log n), n = number of distinct timestamps stored
    # space = O(1) amortized (excluding storage growth)
    def put(self, id: int, timestamp: str) -> None:
        if timestamp not in self.logs:
            self.logs[timestamp] = []
        self.logs[timestamp].append(id)

    # time = O(log n + m), n = number of distinct timestamps, m = matched entries
    # space = O(m) for the result list
    def retrieve(self, start: str, end: str, granularity: str) -> List[int]:
        index = {
            "Year": 4,
            "Month": 7,
            "Day": 10,
            "Hour": 13,
            "Minute": 16,
            "Second": 19,
        }
        start = start[:index[granularity]]
        end = end[:index[granularity]]
        
        # Adjust end to ensure it captures the correct range for the granularity
        end = end[:index[granularity]] + "9" * (19 - index[granularity])

        result = []
        # Using irange to efficiently iterate over the range of timestamps
        # By default, both are set to True for inclusive{}
        #   eg: sd.irange('banana', 'elderberry', inclusive=(True, True))
        for timestamp in self.logs.irange(start, end):
            if timestamp[:index[granularity]] > end:
                break
            result.extend(self.logs[timestamp])
        return result



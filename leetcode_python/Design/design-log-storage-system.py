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


# V2-1
# https://leetcode.ca/2017-08-26-635-Design-Log-Storage-System/
class LogSystem:
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

    def put(self, id: int, timestamp: str) -> None:
        self.logs.append((id, timestamp))

    def retrieve(self, start: str, end: str, granularity: str) -> List[int]:
        i = self.d[granularity]
        return [id for id, ts in self.logs if start[:i] <= ts[:i] <= end[:i]]





# V2-2
# https://leetcode.ca/2017-08-26-635-Design-Log-Storage-System/
class LogSystem:
    def __init__(self):
        self.logs = SortedDict()

    def put(self, id: int, timestamp: str) -> None:
        if timestamp not in self.logs:
            self.logs[timestamp] = []
        self.logs[timestamp].append(id)

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










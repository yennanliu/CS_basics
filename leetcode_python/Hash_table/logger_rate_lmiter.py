"""

https://leetcode.ca/2016-11-23-359-Logger-Rate-Limiter/

https://leetcode.com/problems/logger-rate-limiter/description/

"""

# V0

# V1
# https://leetcode.ca/2016-11-23-359-Logger-Rate-Limiter/
class Logger:
    def __init__(self):
        """
        Initialize your data structure here.
        """
        self.limiter = {}

    def shouldPrintMessage(self, timestamp: int, message: str) -> bool:
        """
        Returns true if the message should be printed in the given timestamp, otherwise returns false.
        If this method returns false, the message will not be printed.
        The timestamp is in seconds granularity.
        """
        t = self.limiter.get(message, 0)
        if timestamp < t:
            return False
        self.limiter[message] = timestamp + 10
        return True


# Your Logger object will be instantiated and called as such:
# obj = Logger()
# param_1 = obj.shouldPrintMessage(timestamp,message)

############

class Logger(object):

  def __init__(self):
    """
    Initialize your data structure here.
    """
    self.d = {}

  def shouldPrintMessage(self, timestamp, message):
    """
    Returns true if the message should be printed in the given timestamp, otherwise returns false.
    If this method returns false, the message will not be printed.
    The timestamp is in seconds granularity.
    :type timestamp: int
    :type message: str
    :rtype: bool
    """
    if message not in self.d:
      self.d[message] = timestamp
      return True
    elif timestamp - self.d[message] >= 10:
      self.d[message] = timestamp
      return True
    return False

# Your Logger object will be instantiated and called as such:
# obj = Logger()
# param_1 = obj.shouldPrintMessage(timestamp,message)


# V2
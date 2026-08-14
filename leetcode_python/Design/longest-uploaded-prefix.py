"""

2424. Longest Uploaded Prefix
Medium

You are given a stream of n videos, each represented by a distinct number from 1 to n that you need to "upload" to a server. You need to implement a data structure that calculates the length of the longest uploaded prefix at various points in the upload process.

We consider i to be an uploaded prefix if all videos in the range 1 to i (inclusive) have been uploaded to the server. The longest uploaded prefix is the maximum value of i that satisfies this definition.

Implement the LUPrefix class:

LUPrefix(int n) Initializes the object for a stream of n videos.
void upload(int video) Uploads video to the server.
int longest() Returns the length of the longest uploaded prefix defined above.


Example 1:

Input
["LUPrefix", "upload", "longest", "upload", "longest", "upload", "longest"]
[[4], [3], [], [1], [], [2], []]
Output
[null, null, 0, null, 1, null, 3]

Explanation
LUPrefix server = new LUPrefix(4);   // Initialize a stream of 4 videos.
server.upload(3);                    // Upload video 3.
server.longest();                    // Since video 1 has not been uploaded yet, there is no prefix.
                                     // So, we return 0.
server.upload(1);                    // Upload video 1.
server.longest();                    // The prefix [1] is the longest uploaded prefix, so we return 1.
server.upload(2);                    // Upload video 2.
server.longest();                    // The prefix [1,2,3] is the longest uploaded prefix, so we return 3.


Constraints:

1 <= n <= 10^5
1 <= video <= n
All values of video are distinct.
At most 2 * 10^5 calls in total will be made to upload and longest.

"""

# V0
# IDEA : A SET OF UPLOADS PLUS A POINTER THAT ONLY EVER MOVES FORWARD
#
#   `longest` can never decrease, so keep a cursor at the first MISSING
#   video and, after each upload, walk it forward while the next number is
#   already present.
#
#   the cursor advances at most n times in total, so the amortised cost per
#   call is O(1) even though a single upload can jump it a long way (see the
#   example, where uploading 2 takes it from 1 to 3).
#
# time = O(1) amortised per call, space = O(n)
class LUPrefix(object):

    def __init__(self, n):
        self.seen = set()
        self.prefix = 0        # every video in 1..prefix has arrived

    def upload(self, video):
        self.seen.add(video)
        while self.prefix + 1 in self.seen:
            self.prefix += 1

    def longest(self):
        return self.prefix


# Your LUPrefix object will be instantiated and called as such:
# obj = LUPrefix(n)
# obj.upload(video)
# param_2 = obj.longest()

"""

2254. Design Video Sharing Platform
Hard
(premium / locked problem)

You have a video sharing platform where users can upload and delete videos. Each video is a string of digits, where the ith digit of the string represents the content of the video at minute i. For example, the first digit represents the content at minute 0 in the video, the second digit represents the content at minute 1 in the video, and so on. Viewers of videos can also like and dislike videos. Internally, the platform keeps track of the number of views, likes, and dislikes on each video.

When a video is uploaded, it is associated with the smallest available integer videoId starting from 0. Once a video is deleted, the videoId associated with that video becomes available again to be associated with a future video.

Implement the VideoSharingPlatform class:

VideoSharingPlatform() Initializes the object.
int upload(String video) The user uploads a video. Return the videoId associated with the video.
void remove(int videoId) If there is a video associated with videoId, remove the video.
String watch(int videoId, int startMinute, int endMinute) If there is a video associated with videoId, increase the number of views on the video by 1 and return the substring of the video string starting at startMinute and ending at min(endMinute, video.length - 1) (inclusive). Otherwise, return "-1".
void like(int videoId) Increases the number of likes on the video associated with videoId by 1 if there is a video associated with videoId.
void dislike(int videoId) Increases the number of dislikes on the video associated with videoId by 1 if there is a video associated with videoId.
int[] getLikesAndDislikes(int videoId) Return a 0-indexed integer array values of length 2 where values[0] is the number of likes and values[1] is the number of dislikes on the video associated with videoId. If there is no video associated with videoId, return [-1].
int getViews(int videoId) Return the number of views on the video associated with videoId. If there is no video associated with videoId, return -1.


Example 1:

Input
["VideoSharingPlatform", "upload", "upload", "remove", "remove", "upload", "watch", "watch", "like", "dislike", "dislike", "getLikesAndDislikes", "getViews"]
[[], ["123"], ["456"], [4], [0], ["789"], [1, 0, 5], [1, 0, 1], [1], [1], [1], [1], [1]]
Output
[null, 0, 1, null, null, 0, "456", "45", null, null, null, [1, 2], 2]

Explanation
VideoSharingPlatform videoSharingPlatform = new VideoSharingPlatform();
videoSharingPlatform.upload("123");          // The smallest available videoId is 0, so return 0.
videoSharingPlatform.upload("456");          // The smallest available videoId is 1, so return 1.
videoSharingPlatform.remove(4);              // There is no video associated with videoId 4, so do nothing.
videoSharingPlatform.remove(0);              // Remove the video associated with videoId 0.
videoSharingPlatform.upload("789");          // Since the video associated with videoId 0 was deleted,
                                             // 0 is the smallest available videoId, so return 0.
videoSharingPlatform.watch(1, 0, 5);         // The video associated with videoId 1 is "456".
                                             // The video from minute 0 to min(5, 3 - 1) = 2 is "456",
                                             // so return "456".
videoSharingPlatform.watch(1, 0, 1);         // The video associated with videoId 1 is "456".
                                             // The video from minute 0 to min(1, 3 - 1) = 1 is "45",
                                             // so return "45".
videoSharingPlatform.like(1);                // Increase the number of likes on the video associated
                                             // with videoId 1.
videoSharingPlatform.dislike(1);             // Increase the number of dislikes on the video associated
                                             // with videoId 1.
videoSharingPlatform.dislike(1);             // Increase the number of dislikes on the video associated
                                             // with videoId 1.
videoSharingPlatform.getLikesAndDislikes(1); // There is 1 like and 2 dislikes on the video associated
                                             // with videoId 1, so return [1, 2].
videoSharingPlatform.getViews(1);            // The number of views on the video associated with
                                             // videoId 1 is 2, so return 2.


Constraints:

1 <= video.length <= 10^5
The sum of video.length over all calls to upload does not exceed 10^5
video consists of digits.
0 <= videoId <= 10^5
0 <= startMinute < endMinute < 10^5
startMinute < video.length
The sum of endMinute - startMinute over all calls to watch does not exceed 10^5.
At most 10^5 calls in total will be made to all functions.

"""

# V0
# IDEA : DICT OF LIVE VIDEOS + A MIN-HEAP OF RECYCLED IDs
#
#   "smallest available videoId" is the only interesting part. two sources of
#   ids exist :
#       * ids freed by remove()  -> a MIN-HEAP so the smallest comes back first
#       * never-used ids         -> a monotonically increasing counter
#   an id from the heap always wins, because every freed id is below the
#   counter.
#
#   everything else is bookkeeping in a dict videoId -> [content, views,
#   likes, dislikes], with each accessor short-circuiting on a missing id.
#
# time = O(log n) for upload, O(1) for the rest (watch is O(slice length))
# space = O(total content)
import heapq


class VideoSharingPlatform(object):

    def __init__(self):
        self.videos = {}       # id -> [content, views, likes, dislikes]
        self.free = []         # min-heap of recycled ids
        self.next_id = 0

    def upload(self, video):
        if self.free:
            vid = heapq.heappop(self.free)
        else:
            vid = self.next_id
            self.next_id += 1
        self.videos[vid] = [video, 0, 0, 0]
        return vid

    def remove(self, videoId):
        if videoId in self.videos:
            del self.videos[videoId]
            heapq.heappush(self.free, videoId)

    def watch(self, videoId, startMinute, endMinute):
        if videoId not in self.videos:
            return "-1"
        entry = self.videos[videoId]
        entry[1] += 1
        end = min(endMinute, len(entry[0]) - 1)
        return entry[0][startMinute:end + 1]

    def like(self, videoId):
        if videoId in self.videos:
            self.videos[videoId][2] += 1

    def dislike(self, videoId):
        if videoId in self.videos:
            self.videos[videoId][3] += 1

    def getLikesAndDislikes(self, videoId):
        if videoId not in self.videos:
            return [-1]
        entry = self.videos[videoId]
        return [entry[2], entry[3]]

    def getViews(self, videoId):
        if videoId not in self.videos:
            return -1
        return self.videos[videoId][1]


# Your VideoSharingPlatform object will be instantiated and called as such:
# obj = VideoSharingPlatform()
# param_1 = obj.upload(video)
# obj.remove(videoId)
# param_3 = obj.watch(videoId,startMinute,endMinute)
# obj.like(videoId)
# obj.dislike(videoId)
# param_6 = obj.getLikesAndDislikes(videoId)
# param_7 = obj.getViews(videoId)

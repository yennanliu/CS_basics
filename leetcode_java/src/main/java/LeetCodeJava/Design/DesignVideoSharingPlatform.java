package LeetCodeJava.Design;

// https://leetcode.com/problems/design-video-sharing-platform/

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

/**
 *  2254. Design Video Sharing Platform
 *  Hard
 *  (premium / locked problem)
 *
 *  You have a video sharing platform where users can upload and delete videos. Each video
 *  is a string of digits, where the ith digit of the string represents the content of the
 *  video at minute i. Viewers of videos can also like and dislike videos. Internally, the
 *  platform keeps track of the number of views, likes, and dislikes on each video.
 *
 *  When a video is uploaded, it is associated with the SMALLEST AVAILABLE integer videoId
 *  starting from 0. Once a video is deleted, the videoId associated with that video becomes
 *  available again to be associated with a future video.
 *
 *  Implement the VideoSharingPlatform class:
 *
 *   - VideoSharingPlatform() Initializes the object.
 *   - int upload(String video) The user uploads a video. Returns the videoId associated
 *     with the video.
 *   - void remove(int videoId) If there is a video associated with videoId, remove the video.
 *   - String watch(int videoId, int startMinute, int endMinute) If there is a video
 *     associated with videoId, increase the number of views on the video by 1 and return
 *     the substring of the video string starting at startMinute and ending at
 *     min(endMinute, video.length - 1) (inclusive). Otherwise, return "-1".
 *   - void like(int videoId) Increases the number of likes on the video associated with
 *     videoId by 1 if there is a video associated with videoId.
 *   - void dislike(int videoId) Increases the number of dislikes on the video associated
 *     with videoId by 1 if there is a video associated with videoId.
 *   - int[] getLikesAndDislikes(int videoId) Returns a 0-indexed integer array values of
 *     length 2 where values[0] is the number of likes and values[1] is the number of
 *     dislikes. If there is no video associated with videoId, returns [-1].
 *   - int getViews(int videoId) Returns the number of views on the video associated with
 *     videoId, or -1 if there is no such video.
 *
 *  Example 1:
 *    Input
 *      ["VideoSharingPlatform","upload","upload","remove","remove","upload","watch","watch",
 *       "like","dislike","dislike","getLikesAndDislikes","getViews"]
 *      [[],["123"],["456"],[4],[0],["789"],[1,0,5],[1,0,1],[1],[1],[1],[1],[1]]
 *    Output
 *      [null,0,1,null,null,0,"456","45",null,null,null,[1,2],2]
 *    Explanation
 *      upload("123") -> 0, upload("456") -> 1
 *      remove(4) does nothing; remove(0) frees id 0, so upload("789") -> 0
 *      watch(1, 0, 5) -> "456" (end is clamped to min(5, 3 - 1) = 2)
 *      watch(1, 0, 1) -> "45"
 *      after 1 like and 2 dislikes: getLikesAndDislikes(1) -> [1,2], getViews(1) -> 2
 *
 *  Constraints:
 *    1 <= video.length <= 10^5
 *    The sum of video.length over all calls to upload does not exceed 10^5
 *    video consists of digits.
 *    0 <= videoId <= 10^5
 *    0 <= startMinute < endMinute < 10^5
 *    startMinute < video.length
 *    The sum of endMinute - startMinute over all calls to watch does not exceed 10^5.
 *    At most 10^5 calls in total will be made to all functions.
 */
public class DesignVideoSharingPlatform {

    private static class Video {
        final String content;
        int views = 0;
        int likes = 0;
        int dislikes = 0;

        Video(String content) {
            this.content = content;
        }
    }

    // V0
    // IDEA: MAP OF LIVE VIDEOS + A MIN-HEAP OF RECYCLED IDs
    //
    //   "smallest available videoId" is the only interesting part. two sources of ids exist:
    //       * ids freed by remove()  -> a MIN-HEAP so the smallest comes back first
    //       * never-used ids         -> a monotonically increasing counter
    //   an id from the heap ALWAYS wins, because every freed id is below the counter.
    //
    //   everything else is bookkeeping in a map videoId -> Video, with each accessor
    //   short-circuiting on a missing id.
    /**
     * time = O(log N) for upload, O(1) for the rest (watch is O(slice length))
     * space = O(total content)
     */
    private final Map<Integer, Video> videos = new HashMap<>();
    private final PriorityQueue<Integer> free = new PriorityQueue<>();
    private int nextId = 0;

    public DesignVideoSharingPlatform() {
    }

    public int upload(String video) {
        int vid;
        if (!free.isEmpty()) {
            vid = free.poll();
        } else {
            vid = this.nextId++;
        }
        videos.put(vid, new Video(video));
        return vid;
    }

    public void remove(int videoId) {
        if (videos.remove(videoId) != null) {
            free.offer(videoId);
        }
    }

    public String watch(int videoId, int startMinute, int endMinute) {
        Video v = videos.get(videoId);
        if (v == null) {
            return "-1";
        }
        v.views++;
        int end = Math.min(endMinute, v.content.length() - 1);
        return v.content.substring(startMinute, end + 1);
    }

    public void like(int videoId) {
        Video v = videos.get(videoId);
        if (v != null) {
            v.likes++;
        }
    }

    public void dislike(int videoId) {
        Video v = videos.get(videoId);
        if (v != null) {
            v.dislikes++;
        }
    }

    public int[] getLikesAndDislikes(int videoId) {
        Video v = videos.get(videoId);
        if (v == null) {
            return new int[]{-1};
        }
        return new int[]{v.likes, v.dislikes};
    }

    public int getViews(int videoId) {
        Video v = videos.get(videoId);
        if (v == null) {
            return -1;
        }
        return v.views;
    }
}

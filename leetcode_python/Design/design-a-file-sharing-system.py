"""

1500. Design a File Sharing System

# https://leetcode.ca/all/1500.html
# https://leetcode.ca/2020-01-08-1500-Design-a-File-Sharing-System/

We will use a file-sharing system to share a very large file which consists of m small chunks with IDs from 1 to m.

When users join the system, the system should assign a unique ID to them. The unique ID should be used once for each user, but when a user leaves the system, the ID can be reused again.

Users can request a certain chunk of the file, the system should return a list of IDs of all the users who have this chunk. After that, if at least one other has this chunk, the user who requested the chunk will get it.


Implement the FileSharing class:

FileSharing(int m) Initializes the object with the number of the chunks of the file m.
int join(int[] ownedChunks): A new user joined the system owning some chunks of the file, the system should assign an id to the user which is the smallest positive integer not taken by any other user. Return the assigned id.
void leave(int userID): The user with userID will leave the system, you cannot take file chunks from them anymore.
int[] request(int userID, int chunkID): The user with userID requested the file chunk with chunkID. Return a list of the IDs of all users that own this chunk sorted in ascending order.
 

Follow-ups:

What happens if the system identifies the user by their IP address instead of their unique ID and users disconnect and connect from the system with the same IP?
If the users in the system join and leave the system frequently without requesting any chunks, will your solution still be efficient?
If all each user join the system one time, request all files and then leave, will your solution still be efficient?
If the system will be used to share n files where the ith file consists of m[i], what are the changes you have to do?
 

Example:

Input:
["FileSharing","join","join","join","request","request","leave","request","leave","join"]
[[4],[[1,2]],[[2,3]],[[4]],[1,3],[2,2],[1],[2,1],[2],[[]]]
Output:
[null,1,2,3,[2],[1,2],null,[],null,1]
Explanation:
FileSharing fileSharing = new FileSharing(4); // We use the system to share a file of 4 chunks.
fileSharing.join([1, 2]);    // A user who has chunks [1,2] joined the system, assign id = 1 to them and return 1.
fileSharing.join([2, 3]);    // A user who has chunks [2,3] joined the system, assign id = 2 to them and return 2.
fileSharing.join([4]);       // A user who has chunk [4] joined the system, assign id = 3 to them and return 3.
fileSharing.request(1, 3);   // The user with id = 1 requested the third file chunk, as only the user with id = 2 has the file, return [2] . Notice that user 1 now has chunks [1,2,3].
fileSharing.request(2, 2);   // The user with id = 2 requested the second file chunk, users with ids [1,2] have this chunk, thus we return [1,2]. We don't care if the user has the file and request it, we just return all the users that can give them the file.
fileSharing.leave(1);        // The user with id = 1 left the system, all the file chunks with them are no longer available for other users.
fileSharing.request(2, 1);   // The user with id = 2 requested the first file chunk, no one in the system has this chunk, we return empty list [].
fileSharing.leave(2);        // The user with id = 2 left the system, all the file chunks with them are no longer available for other users.
fileSharing.join([]);        // A user who doesn't have any chunks joined the system, assign id = 1 to them and return 1. Notice that ids 1 and 2 are free and we can reuse them.
 

Constraints:

1 <= m <= 10^5
0 <= ownedChunks.length <= min(100, m)
1 <= ownedChunks[i] <= m
Values of ownedChunks are unique.
1 <= chunkID <= m
userID is guaranteed to be a user in the system if you assign the IDs correctly. 
At most 10^4 calls will be made to join, leave and request.
Each call to leave will have a matching call for join.
Difficulty:
Medium
Lock:
Prime
Company:
Unknown
Problem Solution
1500-Design-a-File-Sharing-System

"""

# V0

# V1
# https://blog.csdn.net/qq_21201267/article/details/107620952
       
# V1'
# https://leetcode.ca/2020-01-08-1500-Design-a-File-Sharing-System/
# JAVA
# class FileSharing {
#     Map<Integer, Set<Integer>> chunkUsersMap;
#     Map<Integer, Set<Integer>> userChunksMap;
#     PriorityQueue<Integer> userQueue;
#     int maxUser;
#
#     public FileSharing(int m) {
#         chunkUsersMap = new HashMap<Integer, Set<Integer>>();
#         userChunksMap = new HashMap<Integer, Set<Integer>>();
#         userQueue = new PriorityQueue<Integer>();
#         maxUser = 0;
#     }
#   
#     public int join(List<Integer> ownedChunks) {
#         int userID = 0;
#         if (userQueue.isEmpty()) {
#             maxUser++;
#             userID = maxUser;
#         } else
#             userID = userQueue.poll();
#         for (int chunk : ownedChunks) {
#             Set<Integer> users = chunkUsersMap.getOrDefault(chunk, new TreeSet<Integer>());
#             users.add(userID);
#             chunkUsersMap.put(chunk, users);
#         }
#         userChunksMap.put(userID, new HashSet<Integer>(ownedChunks));
#         return userID;
#     }
#    
#     public void leave(int userID) {
#         if (userChunksMap.containsKey(userID)) {
#             Set<Integer> chunks = userChunksMap.get(userID);
#             for (int chunk : chunks) {
#                 if (chunkUsersMap.containsKey(chunk)) {
#                     Set<Integer> users = chunkUsersMap.get(chunk);
#                     users.remove(userID);
#                     if (users.size() > 0)
#                         chunkUsersMap.put(chunk, users);
#                     else
#                         chunkUsersMap.remove(chunk);
#                 }
#             }
#             userChunksMap.remove(userID);
#         }
#         userQueue.add(userID);
#     }
#   
#     public List<Integer> request(int userID, int chunkID) {
#         List<Integer> usersList = new ArrayList<Integer>();
#         if (!chunkUsersMap.containsKey(chunkID) || !userChunksMap.containsKey(userID))
#             return usersList;
#         usersList.addAll(chunkUsersMap.get(chunkID));
#         Set<Integer> users = chunkUsersMap.get(chunkID);
#         users.add(userID);
#         chunkUsersMap.put(chunkID, users);
#         Set<Integer> chunks = userChunksMap.get(userID);
#         chunks.add(chunkID);
#         userChunksMap.put(userID, chunks);
#         return usersList;
#     }
# }
#
# /**
#  * Your FileSharing object will be instantiated and called as such:
#  * FileSharing obj = new FileSharing(m);
#  * int param_1 = obj.join(ownedChunks);
#  * obj.leave(userID);
#  * List<Integer> param_3 = obj.request(userID,chunkID);
#  */


# V2
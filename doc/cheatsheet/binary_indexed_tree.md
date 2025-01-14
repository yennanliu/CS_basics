A Binary Indexed Tree (BIT), also known as a Fenwick Tree, is a data structure that efficiently supports both:

Point updates (i.e., update an element in the array).
Prefix sum queries (i.e., query the sum of elements from the start of the array to a given index).
Key Features of a Binary Indexed Tree:
Efficient Updates: You can update an element in the array in O(log n) time.
Efficient Queries: You can compute the sum of elements in a range from the start to a given index in O(log n) time.
How It Works:
A BIT is built over an array and stores partial sums of the array in a tree-like structure. The array's indexes are represented in binary form, and each position in the BIT array stores a sum of a subset of array elements, based on the binary representation of the index.

Structure of the BIT:
The underlying array is 1-indexed (or you can adapt it to 0-indexed).
For each index i, the BIT stores the sum of a range of elements. The range size depends on the least significant set bit of the index i.
For example:

BIT[i] stores the sum of elements from index i - (i & -i) + 1 to i, where i & -i extracts the least significant set bit in the binary representation of i.
Operations:
1. Update Operation:
When you want to update an element in the original array, you adjust the BIT values to reflect this update. The update is propagated to all indices that are responsible for storing sums that include the updated index.

To update an index i in the BIT, you adjust the BIT at i and continue adjusting at i + (i & -i) (i.e., move to the next index that is part of the same group) until you reach the end of the BIT.
2. Prefix Sum Query:
To calculate the sum of elements from index 1 to i (inclusive), you start at i and keep subtracting the least significant set bit (i & -i) to move to previous elements in the BIT. The sum is computed by adding the values at these indices.

Example of a Binary Indexed Tree:
Consider an array: [3, 2, -1, 6, 5, 4].

BIT Initialization: Start with a BIT array of the same length (initially all zeros) and build it by updating the BIT based on the input array.

Update Operation: If you want to update an element in the original array (e.g., changing array[2] from -1 to 4), you update the relevant BIT values.

Prefix Sum Query: To find the sum from index 1 to i, you sum the relevant BIT values.



- LC 307
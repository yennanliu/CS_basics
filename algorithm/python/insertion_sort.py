#---------------------------------------------------------------
# INSERTION SORT
#---------------------------------------------------------------

# https://rust-algo.club/sorting/insertion_sort/index.html

"""

Time complexity

    Best    O(n)
    Average O(n^2)
    Worst   O(n^2)

Space complexity

    Worst space O(1) auxiliary

"""

# V0


# V1 (gpt)
def insertion_sort(arr):
    for i in range(1, len(arr)):
        key = arr[i]
        j = i - 1
        while j >= 0 and key < arr[j]:
            arr[j + 1] = arr[j]
            j -= 1
        arr[j + 1] = key

# Usage example
# arr = [12, 11, 13, 5, 6]
# insertion_sort(arr)
# print("Sorted array is:", arr)


# V2
# https://www.geeksforgeeks.org/python-program-for-insertion-sort/
# Function to do insertion sort 
def insertionSort(arr): 

    # Traverse through 1 to len(arr) 
    for i in range(1, len(arr)): 
        key = arr[i] 
        j = i-1
        while j >=0 and key < arr[j] : 
            arr[j+1] = arr[j] 
            j -= 1
        arr[j+1] = key 
    return arr
    
# arr = [12, 11, 13, 5, 6] 
# r = insertionSort(arr) 
# print ("Sorted array is:", r) 
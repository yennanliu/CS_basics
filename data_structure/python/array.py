#---------------------------------------------------------------
# ARRAY
#---------------------------------------------------------------

# V0
# DEMO 
# x = [ [] for i in range(5)]
# for i in range(5):
#     x[i] = i
# print (x)
# idx = 2
# for i in range(len(x)-2, idx+1, -1):
#     x[i] = x[i+1]   
# x[idx] = 99
# print (x) 
class Array(object):
    def __init__(self, sizeOfArray, arrayType = int):
        self.sizeOfArray = len(list(map(arrayType, list(range(sizeOfArray)))))
        self.arrayItems =[arrayType(0)] * sizeOfArray    # initialize array with zeroes
        self.arrayType = arrayType

    def __str__(self):
        return ' '.join([str(i) for i in self.arrayItems])

    def __len__(self):
        return len(self.arrayItems)

    # magic methods to enable indexing
    def __setitem__(self, index, data):
        self.arrayItems[index] = data

    def __getitem__(self, index):
        return self.arrayItems[index]

    # function for search
    def search(self, keyToSearch):
        for i in range(self.sizeOfArray):
            if (self.arrayItems[i] == keyToSearch):      
                return i                                 
        return -1                                        

    # function for inserting an element
    def insert(self, keyToInsert, position):
        if(self.sizeOfArray > position):
            """
            ### BE AWARE OF IT 
            -> in insert case should do an "inverse" loop (array[i+1] = array[i]) 
            """
            for i in range(self.sizeOfArray - 2, position - 1, -1):
                self.arrayItems[i + 1] = self.arrayItems[i]
            self.arrayItems[position] = keyToInsert
        else:
            print(('Array size is:', self.sizeOfArray))

    # function to delete an element
    def delete(self, keyToDelete, position):
        if(self.sizeOfArray > position):
            """
            ### BE AWARE OF IT 
            -> in delete case should do an loop (array[i] = array[i+1]) 
            """
            for i in range(position, self.sizeOfArray - 1):
                self.arrayItems[i] = self.arrayItems[i + 1]
            self.arrayItems[i + 1] = self.arrayType(0)
        else:
            print(('Array size is:', self.sizeOfArray))

# V1
# https://github.com/OmkarPathak/Data-Structures-using-Python/blob/master/Arrays/Arrays.py
class Array(object):
    ''' sizeOfArray: denotes the total size of the array to be initialized
       arrayType: denotes the data type of the array(as all the elements of the array have same data type)
       arrayItems: values at each position of array
    '''
    def __init__(self, sizeOfArray, arrayType = int):
        self.sizeOfArray = len(list(map(arrayType, list(range(sizeOfArray)))))
        self.arrayItems =[arrayType(0)] * sizeOfArray    # initialize array with zeroes
        self.arrayType = arrayType

    def __str__(self):
        return ' '.join([str(i) for i in self.arrayItems])

    def __len__(self):
        return len(self.arrayItems)

    # magic methods to enable indexing
    def __setitem__(self, index, data):
        self.arrayItems[index] = data

    def __getitem__(self, index):
        return self.arrayItems[index]

    # function for search
    def search(self, keyToSearch):
        for i in range(self.sizeOfArray):
            if (self.arrayItems[i] == keyToSearch):      # brute-forcing
                return i                                 # index at which element/ key was found

        return -1                                        # if key not found, return -1

    # function for inserting an element
    def insert(self, keyToInsert, position):
        if(self.sizeOfArray > position):
            for i in range(self.sizeOfArray - 2, position - 1, -1):
                self.arrayItems[i + 1] = self.arrayItems[i]
            self.arrayItems[position] = keyToInsert
        else:
            print(('Array size is:', self.sizeOfArray))

    # function to delete an element
    def delete(self, keyToDelete, position):
        if(self.sizeOfArray > position):
            for i in range(position, self.sizeOfArray - 1):
                self.arrayItems[i] = self.arrayItems[i + 1]
            self.arrayItems[i + 1] = self.arrayType(0)
        else:
            print(('Array size is:', self.sizeOfArray))

# if __name__ == '__main__':
#     a = Array(10, int)
#     a.insert(2, 2)
#     a.insert(3, 1)
#     a.insert(4,7)
#     print((len(a)))
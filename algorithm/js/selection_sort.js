//---------------------------------------------------------------
// SELECTION SORT
//---------------------------------------------------------------
//
// Repeatedly SELECT the minimum of the unsorted right-hand part and
// swap it into the next position of the sorted left-hand part.
//
//   [| 5, 1, 4, 2]   min is 1  ->  swap with slot 0
//   [1 | 5, 4, 2]    min is 2  ->  swap with slot 1
//   [1, 2 | 4, 5]    min is 4  ->  already there
//   [1, 2, 4 | 5]    done
//
// It always does the same N^2/2 comparisons, so unlike bubble and
// insertion sort there is no fast path for already-sorted input. What
// it does minimise is WRITES: exactly one swap per pass, at most N-1
// in total, which matters when writing is expensive (e.g. flash).
//
// NOTE selection sort is NOT stable: the long-distance swap can jump a
// value past an equal one.
//
// Time  : O(N^2) in all cases
// Space : O(1)

function selectionSort(array) {
  const length = array.length;

  for (let i = 0; i < length - 1; i++) {
    // find the smallest value in the unsorted part
    let min = i;
    for (let j = i + 1; j < length; j++) {
      if (array[j] < array[min]) min = j;
    }

    // one swap per pass -- and only if it actually moves something
    if (min !== i) {
      [array[i], array[min]] = [array[min], array[i]];
    }
  }
  return array;
}

// demo
console.assert(
  selectionSort([99, 44, 6, 2, 1, 5, 63, 87, 283, 4, 0]).join(',') === '0,1,2,4,5,6,44,63,87,99,283',
  'sorts a mixed array'
);
console.assert(selectionSort([]).join(',') === '', 'empty array');
console.assert(selectionSort([1]).join(',') === '1', 'single element');
console.assert(selectionSort([1, 2, 3]).join(',') === '1,2,3', 'already sorted');
console.assert(selectionSort([3, 2, 1]).join(',') === '1,2,3', 'reversed');
console.assert(selectionSort([2, 1, 2, 1]).join(',') === '1,1,2,2', 'duplicates');
console.assert(selectionSort([0, -3, 5, -1]).join(',') === '-3,-1,0,5', 'negatives');

console.log(selectionSort([99, 44, 6, 2, 1, 5, 63, 87, 283, 4, 0]).join(' '));
console.log('Success.');

module.exports = { selectionSort };

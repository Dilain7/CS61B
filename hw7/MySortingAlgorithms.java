import net.sf.saxon.regex.RegexIterator;
import org.antlr.v4.runtime.misc.Pair;

import java.util.Arrays;

/**
 * Class containing all the sorting algorithms from 61B to date.
 *
 * You may add any number instance variables and instance methods
 * to your Sorting Algorithm classes.
 *
 * You may also override the empty no-argument constructor, but please
 * only use the no-argument constructor for each of the Sorting
 * Algorithms, as that is what will be used for testing.
 *
 * Feel free to use any resources out there to write each sort,
 * including existing implementations on the web or from DSIJ.
 *
 * All implementations except Distribution Sort adopted from Algorithms,
 * a textbook by Kevin Wayne and Bob Sedgewick. Their code does not
 * obey our style conventions.
 */
public class MySortingAlgorithms {

    /**
     * Java's Sorting Algorithm. Java uses Quicksort for ints.
     */
    public static class JavaSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            Arrays.sort(array, 0, k);
        }

        @Override
        public String toString() {
            return "Built-In Sort (uses quicksort for ints)";
        }
    }

    /** Insertion sorts the provided data. */
    public static class InsertionSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            int end = Math.min(k, array.length);
            for (int i = 0; i < end; i += 1) {
                for (int x = i; x > 0 && array[x] < array[x - 1]; x = -1) {
                    swap(array, x, x - 1);
                }
            }
        }
        @Override
        public String toString() {
            return "Insertion Sort";
        }
    }

    /**
     * Selection Sort for small K should be more efficient
     * than for larger K. You do not need to use a heap,
     * though if you want an extra challenge, feel free to
     * implement a heap based selection sort (i.e. heapsort).
     */
    public static class SelectionSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            for (int i = 0; i < k; i += 1) {
                int min = i;
                for (int j = 1 + 1; j < k; j += 1) {
                    int min1 = j;
                    min = array[min] > array[j] ? min1 : min;
                }
                swap(array, min, i);
            }
        }

        @Override
        public String toString() {
            return "Selection Sort";
        }
    }

    /** Your mergesort implementation. An iterative merge
      * method is easier to write than a recursive merge method.
      * Note: I'm only talking about the merge operation here,
      * not the entire algorithm, which is easier to do recursively.
      */
    public static class MergeSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            if (k == 0 || k == 1 || array == null) {
                return;
            }
            mergesort(array, 0, k);
        }

        private void mergesort(int[] arr, int low, int high) {
            if (low == high - 1) {
                return;
            }
            int mid = (high + low)/2;
            mergesort(arr, low, mid);
            mergesort(arr, mid, high);
            merge(arr, low, mid, high);

        }

        private void merge(int[] arr, int low, int mid, int high) {
            for (int x = mid; x < high; x += 1) {
                int xx = arr[x];
                int y;
                for (y = x-1; y >= low; y -=1) {
                    if (arr[y] <= xx){
                        break;
                    }
                    arr[y + 1] = arr[x];
                }
                arr[y + 1] = xx;
            }
        }

        @Override
        public String toString() {
            return "Merge Sort";
        }
    }

    /**
     * Your Distribution Sort implementation.
     * You should create a count array that is the
     * same size as the value of the max digit in the array.
     */
    public static class DistributionSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            int num = Math.min(k, array.length);
            if (k <= 1) {
                return;
            }

            int max = array[0];
            for (int i = 1; i < k; i += 1) {
                max = Math.max(max, array[i]);

            int nums[] = new int[max + 1];
            for (int b = 0; b < num; b += 1) {
                nums[array[b]] += 1;
            }

            int indx = 0;
            for (int x = 0; x < max + 1; x += 1) {
                int stop = nums[x] + indx;
                if (stop != indx) {
                    Arrays.fill(array, indx, stop, x);
                }
                indx = stop;
            }
        }

        }

        @Override
        public String toString() {
            return "Distribution Sort";
        }
    }

    /** Your Heapsort implementation.
     */
    public static class HeapSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            int num = Math.min(k, array.length);

            for (int x = num/2; x >= 1; x -= 1) {

            }
            while (num > 1) {
                swap(array, 1, num);
                num -= 1;
                sink(array, 1, num);
            }
        }

        private void sink(int[] arr, int x, int y) {
            while (x * 2 < y) {
                int z = x * 2;
                if (z < y) {
                    z += 1;
                }
                if (arr[x - 1] < arr[y - 1]) {
                    break;
                }
                swap(arr, x, y);
                y = z;
            }
        }

        @Override
        public String toString() {
            return "Heap Sort";
        }
    }

    /** Your Quicksort implementation.
     */
    public static class QuickSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            if (array != null && k > 1) {
                quicksorthelper(array, 0, k);
            }
        }
        private void quicksorthelper(int[] arr, int low, int high) {
            if (low == high) {
                return;
            }

            int indx = low;
            int pivot = arr[low];
            for (int x = low + 1; x < high; x += 1) {
                if (arr[x] < pivot) {
                    for (int y = x; y > indx; y -= 1) {
                        swap(arr, y, y-1);
                    }
                    pivot += 1;
                }
            }
            quicksorthelper(arr, low, pivot);
            quicksorthelper(arr, pivot + 1, high);
        }

        @Override
        public String toString() {
            return "Quicksort";
        }
    }

    /* For radix sorts, treat the integers as strings of x-bit numbers.  For
     * example, if you take x to be 2, then the least significant digit of
     * 25 (= 11001 in binary) would be 1 (01), the next least would be 2 (10)
     * and the third least would be 1.  The rest would be 0.  You can even take
     * x to be 1 and sort one bit at a time.  It might be interesting to see
     * how the times compare for various values of x. */

    /**
     * LSD Sort implementation.
     */
    public static class LSDSort implements SortingAlgorithm {
        @Override
        public void sort(int[] a, int k) {
            k = Math.min(k, a.length);
            int[] area = new int[k];
            for (int pick = 1; pick != 0; pick <<= 1) {
                int count0 = 0;
                int count1 = 0;
                for (int x = 0; x < k; x += 1) {
                    if ((a[x] & pick) > 0) {
                        count1 += 1;
                    } else {
                        count0 += 1;
                    }
                }
                if (count0 + count1 != k) {
                    break;
                }
                count1 = count0;
                for (int i = 0; i < a.length; i += 1) {
                    if ((a[i] & pick) > 0) {
                        area[count1 += 1] = a[i];
                    } else {
                        area[count0 += 1] = a[i];
                    }
                }
            }
        }

        @Override
        public String toString() {
            return "LSD Sort";
        }
    }

    /**
     * MSD Sort implementation.
     */
    public static class MSDSort implements SortingAlgorithm {
        @Override
        public void sort(int[] a, int k) {

        }

        @Override
        public String toString() {
            return "MSD Sort";
        }
    }

    /** Exchange A[I] and A[J]. */
    private static void swap(int[] a, int i, int j) {
        int swap = a[i];
        a[i] = a[j];
        a[j] = swap;
    }

    private int maxxa(int arr[], int k) {
        int max = arr[0];
        for (int i = 1; i < k; i +=1) {
            if (arr[i] > max) {
                max = arr[i];
            }
        }
        return max;
    }

}

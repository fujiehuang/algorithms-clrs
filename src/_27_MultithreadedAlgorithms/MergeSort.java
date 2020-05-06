package _27_MultithreadedAlgorithms;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/*
It's a lot slower than normal merge sort in this implementation, normally
you'd want to have a base case where if the size of the array is sufficiently
small you switch to some other sorting algorithm like insertion or quick sort
 */

public abstract class MergeSort
{
    private static class MSort<T> implements Callable<Object>
    {
        private class Merge implements Callable<Object>
        {
            private T[] T;
            private int p1;
            private int r1;
            private int p2;
            private int r2;
            private T[] A;
            private int p3;

            public Merge(T[] t, int p1, int r1, int p2, int r2, T[] a, int p3) {
                T = t;
                this.p1 = p1;
                this.r1 = r1;
                this.p2 = p2;
                this.r2 = r2;
                A = a;
                this.p3 = p3;
            }

            @Override
            public Object call() throws Exception {
                int n1 = r1 - p1 + 1;
                int n2 = r2 - p2 + 1;
                if (n1 < n2) {
                    int temp = p1;
                    p1 = p2;
                    p2 = temp;
                    temp = r1;
                    r1 = r2;
                    r2 = temp;
                    temp = n1;
                    n1 = n2;
                    n2 = temp;
                }
                if (n1 == 0)
                    return null;
                else {
                    int q1 = (p1 + r1) / 2;
                    int q2 = binarySearch(T[q1], T, p2, r2, comp);
                    int q3 = p3 + (q1 - p1) + (q2 - p2);
                    A[q3] = T[q1];
                    ExecutorService executor = Executors.newSingleThreadExecutor();
                    executor.submit(new Merge(T, p1, q1 - 1, p2, q2 - 1, A, p3)).get();
                    new Merge(T, q1 + 1, r1, q2, r2, A, q3 + 1).call();
                    executor.shutdown();
                }
                return null;
            }
        }

        private T[] A;
        private int p;
        private int r;
        private T[] B;
        private int off;
        private Comparator<T> comp;

        public MSort(T[] a, int p, int r, T[] b, int off, Comparator<T> comp) {
            A = a;
            this.p = p;
            this.r = r;
            B = b;
            this.off = off;
            this.comp = comp;
        }

        @SuppressWarnings("unchecked")
        @Override
        public Object call() throws Exception {
            int n = r - p + 1;
            if (n == 1)
                B[off] = A[p];
            else {
                T[] T = (T[]) new Object[n];
                int q = (p + r) / 2;
                int u = q - p;
                ExecutorService executor = Executors.newSingleThreadExecutor();
                new MSort<>(A, q + 1, r, T, u + 1, comp).call();
                executor.submit(new MSort<>(A, p, q, T, 0, comp)).get();
                executor.shutdown();
                new Merge(T, 0, u, u + 1, n - 1, B, off).call();
                System.arraycopy(B, off, A, off, n);
            }
            return null;
        }
    }

    private static <T> int binarySearch(T v, T[] T, int p, int r, Comparator<T> comp) {
        int low = p;
        int high = Math.max(p, r + 1);
        while (low < high) {
            int mid = (low + high) / 2;
            if (comp.compare(v, T[mid]) <= 0)
                high = mid;
            else
                low = mid + 1;
        }
        return high;
    }

    @SuppressWarnings("unchecked")
    public static <T> void mergeSort(T[] A, Comparator<T> comp) throws Exception {
        T[] B = (T[]) new Object[A.length];
        new MSort<>(A, 0, A.length - 1, B, 0, comp).call();
    }

    private static class Example
    {
        public static void main(String[] args) throws Exception {
            int size = 100;
            Integer[] A = new Integer[size];
            Random r = new Random();

            for (int i = 0; i < size; i++)
                A[i] = r.nextInt(100);

            System.out.println(Arrays.toString(A));
            mergeSort(A, Integer::compareTo);
            System.out.println(Arrays.toString(A));
        }
    }
}
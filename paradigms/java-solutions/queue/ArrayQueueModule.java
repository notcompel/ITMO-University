package queue;

import java.util.Arrays;

/*
Model: a[1]..a[n]
Invariant: for i=1..n: a[i] != null

Let immutable(n): for i=1..n: a'[i]==a[i]
 */
public class ArrayQueueModule {
    private static Object[] elements = new Object[5];
    private static int size = 0;
    private static int head = 0, tail = 0;

    //pred: true
    //post: elements.length < size && elements.length' = 2 * elements.length'
    //      || elements.length >= size
    private static void ensureCapacity(int newSize) {
        if (elements.length <= newSize) {
            Object[]tmp = new Object[2 * newSize];
            if (head > tail) {
                System.arraycopy(elements, head, tmp, 0, elements.length - head);
                System.arraycopy(elements, 0, tmp, elements.length - head, tail);
            } else {
                System.arraycopy(elements, head, tmp, 0, tail - head);
            }
            elements = tmp;
            head = 0;
            tail = newSize - 1;
        }
    }

    //pred: element != null
    //post: n' = n + 1 && a[n'] == element && immutable(n)
    public static void enqueue(Object element) {
        assert element != null;
        ensureCapacity(size + 1);
        elements[tail] = element;
        size++;
        tail = (tail + 1) % elements.length;
    }

    //pred: element != null
    //post: n' = n + 1 && a[n'] == element && immutable(n)
    public static void push(Object element) {
        assert element != null;
        ensureCapacity(size + 1);
        head = getHeadInd();
        elements[head] = element;
        size++;
    }

    //pred: n >= 1
    //post: n' == n && immutable(n) && R = a[1]
    public static Object element() {
        assert size >= 1;
        return elements[head];
    }

    //pred: n >= 1
    //post: n' == n && immutable(n) && R = a[n]
    public static Object peek() {
        assert size >= 1;
        return elements[getTailInd()];
    }

    //pred: n >= 1
    //post: n' == n - 1 && for i=1..n': a'[i]==a[i+1] && R = a[1]
    public static Object dequeue() {
        assert size >= 1;
        Object element = elements[head];
        elements[head] = null;
        size--;
        head = (head + 1) % elements.length;
        return element;
    }

    //pred: n >= 1
    //post: n' == n - 1 && for i=1..n': a'[i]==a[i+1] && R = a[n]
    public static Object remove() {
        assert size >= 1;
        Object element = peek();
        size--;

        tail = getTailInd();
        elements[tail] = null;
        return element;
    }

    //pred: true
    //post: R == |{i : a[i] == x}| && n' == n && immutable(n)
    public static int count(Object x) {
        assert x != null;

        int count = 0;
        for (int i = head; i != tail; i = (i + 1) % elements.length) {
            if (elements[i].equals(x)) {
                count++;
            }
        }
        return count;
    }

    //pred: true
    //post: R == n && n' == n && immutable(n)
    public static int size() {
        return size;
    }

    //pred: true
    //post: R == (n == 0) && n' == n && immutable(n)
    public static boolean isEmpty() {
        return size == 0;
    }

    //pred: true
    //post: n' == 0
    public static void clear() {
        size = 0;
        tail = 0;
        head = 0;
        elements = new Object[3];
    }

    private static int getTailInd() {
        return (elements.length + tail - 1) % elements.length;
    }
    private static int getHeadInd() {
        return (elements.length + head - 1) % elements.length;
    }
}

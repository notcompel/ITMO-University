package queue;

import java.util.Arrays;

/*
Model: a[1]..a[n]
Invariant: for i=1..n: a[i] != null

Let immutable(n): for i=1..n: a'[i]==a[i]
 */
public class ArrayQueueADT {
    private Object[] elements = new Object[5];
    private int size = 0;
    private int head = 0, tail = 0;

    //pred: element != null && queue != null
    //post: n' = n + 1 && a[n'] == element && immutable(n)
    public static void enqueue(final ArrayQueueADT queue, final Object element) {///???
        assert queue != null && element != null;
        ensureCapacity(queue,queue.size + 1);
        queue.elements[queue.tail] = element;
        queue.size++;
        queue.tail = (queue.tail + 1) % queue.elements.length;
    }

    //pred: element != null && queue != null
    //post: n' = n + 1 && a[n'] == element && immutable(n)
    public static void push(final ArrayQueueADT queue, Object element) {
        assert queue != null && element != null;
        ensureCapacity(queue,queue.size + 1);
        queue.head = getHeadInd(queue);
        queue.elements[queue.head] = element;
        queue.size++;
    }


    //pred: n >= 1 && queue != null
    //post: n' == n && immutable(n) && R = a[n]
    public static Object peek(final ArrayQueueADT queue) {
        assert queue != null && queue.size >= 1;
        return queue.elements[getTailInd(queue)];
    }

    //pred: n >= 1 && queue != null
    //post: n' == n - 1 && for i=1..n': a'[i]==a[i+1] && R = a[n]
    public static Object remove(final ArrayQueueADT queue) {
        assert queue != null && queue.size >= 1;
        Object element = peek(queue);
        queue.size--;

        queue.tail = getTailInd(queue);
        queue.elements[queue.tail] = null;
        return element;
    }

    //pred: queue != null
    //post: R == |{i : a[i] == x}| && n' == n && immutable(n)
    public static int count(final ArrayQueueADT queue, Object x) {
        assert queue != null && x != null;
        int count = 0;
        for (int i = queue.head; i != queue.tail; i = (i + 1) % queue.elements.length) {
            if (queue.elements[i].equals(x)) {
                count++;
            }
        }
        return count;
    }

    //pred: n >= 1 && queue != null
    //post: n' == n && immutable(n) && R = a[1]
    public static Object element(final ArrayQueueADT queue) {
        assert queue != null && queue.size >= 1;
        return queue.elements[queue.head];
    }

    //pred: n >= 1 && queue != null
    //post: n' == n - 1 && for i=1..n': a'[i]==a[i+1] && R = a[1]
    public static Object dequeue(final ArrayQueueADT queue) {
        assert queue != null && queue.size >= 1;
        Object element = queue.elements[queue.head];
        queue.elements[queue.head] = null;
        queue.size--;
        queue.head = (queue.head + 1) % queue.elements.length;
        return element;
    }

    //pred: queue != null
    //post: R == n && n' == n && immutable(n)
    public static int size(final ArrayQueueADT queue) {
        assert queue != null;
        return queue.size;
    }

    //pred: queue != null
    //post: R == (n == 0) && n' == n && immutable(n)
    public static boolean isEmpty(final ArrayQueueADT queue) {
        assert queue != null;
        return queue.size == 0;
    }

    //pred: queue != null
    //post: n' == 0
    public static void clear(final ArrayQueueADT queue) {
        assert queue != null;
        queue.size = 0;
        queue.tail = 0;
        queue.head = 0;
        queue.elements = new Object[3];
    }

    private static int getTailInd(final ArrayQueueADT queue) {
        return (queue.elements.length + queue.tail - 1) % queue.elements.length;
    }
    private static int getHeadInd(final ArrayQueueADT queue) {
        return (queue.elements.length + queue.head - 1) % queue.elements.length;
    }

    private static void ensureCapacity(final ArrayQueueADT queue, int newSize) {
        if (queue.elements.length <= newSize) {
            Object[]tmp = new Object[2 * newSize];
            if (queue.head > queue.tail) {
                System.arraycopy(queue.elements, queue.head, tmp, 0, queue.elements.length - queue.head);
                System.arraycopy(queue.elements, 0, tmp, queue.elements.length - queue.head, queue.tail);
            } else {
                System.arraycopy(queue.elements, queue.head, tmp, 0, queue.tail - queue.head);
            }
            queue.elements = tmp;
            queue.head = 0;
            queue.tail = newSize - 1;
        }
    }

}

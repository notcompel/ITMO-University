package queue;

import java.util.function.Function;
import java.util.function.Predicate;

public class ArrayQueue extends AbstractQueue{
    private Object[] elements;
    private int head, tail;

    public ArrayQueue() {
        this.elements = new Object[5];
        this.size = 0;
        this.head = 0;
        this.tail = 0;
    }

    @Override
    protected void enqueueImpl(final Object element) {
        ensureCapacity(size);
        elements[tail] = element;
        tail = (tail + 1) % elements.length;
    }

    @Override
    protected void pushImpl(Object element) {
        ensureCapacity(size);
        head = getNewInd(head);
        elements[head] = element;
    }

    @Override
    protected Object peekImpl() {
        return elements[getNewInd(tail)];
    }

    @Override
    protected Object removeImpl(int size, Object element) {
        tail = getNewInd(tail);
        elements[tail] = null;
        return element;
    }

    @Override
    protected Object dequeueImpl(Object element) {
        elements[head] = null;
        head = (head + 1) % elements.length;
        return element;
    }

    @Override
    protected Queue filterMap(Function<Object, Object> function, Predicate<Object> predicate) {
        ArrayQueue result = new ArrayQueue();
        for (int i = head; i != tail; i = (i + 1) % elements.length) {
            if (predicate.test(elements[i])) {
                result.enqueue(function.apply(elements[i]));
            }
        }
        return result;
    }

    @Override
    protected Object elementImpl() {

        return elements[head];
    }

    @Override
    protected int countImpl(Object x) {
        int count = 0;
        for (int i = head; i != tail; i = (i + 1) % elements.length) {
            if (elements[i].equals(x)) {
                count++;
            }
        }
        return count;
    }

    @Override
    protected void clearImpl() {
        tail = 0;
        head = 0;
        size = 0;
        elements = new Object[3];
    }

    private void ensureCapacity(int newSize) {
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

    private int getNewInd(int i) {
        return (elements.length + i - 1) % elements.length;
    }
}

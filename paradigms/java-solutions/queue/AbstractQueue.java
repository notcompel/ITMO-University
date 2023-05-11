package queue;

import java.util.function.Function;
import java.util.function.Predicate;

public abstract class AbstractQueue implements Queue{
    protected int size;

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

     public Object remove() {
        assert size >= 1;
        Object element = peek();
        size--;
        return removeImpl(size, element);
    }

    public void enqueue(final Object element) {
        assert element != null;
        size++;
        enqueueImpl(element);
    }

    public void push(Object element) {
        assert element != null;
        size++;

        pushImpl(element);
    }

    public Object peek() {
        assert size >= 1;
        return peekImpl();
    }
    
    public Object dequeue() {
        assert size >= 1;
        Object element = element();
        size--;
        return dequeueImpl(element);
    }

    public Object element() {
        assert size >= 1;
        return elementImpl();
    }

    public void clear() {
        size = 0;
        clearImpl();
    }

    public int count(Object x) {
        assert x != null;
        return countImpl(x);
    }

    public Queue filter(Predicate<Object> predicate) {
        return filterMap(x -> x, predicate);
    }

    public Queue map(Function<Object, Object> function) {
        return filterMap(function, x -> true);
    }

    protected abstract Queue filterMap(final Function<Object, Object> function, final Predicate<Object> predicate);

    protected abstract void clearImpl();

    protected abstract Object elementImpl();

    protected abstract Object dequeueImpl(Object element);

    protected abstract Object peekImpl();

    protected abstract void pushImpl(Object element);

    protected abstract void enqueueImpl(Object element);

    protected abstract int countImpl(Object x);

    protected abstract Object removeImpl(int size, Object element);

}


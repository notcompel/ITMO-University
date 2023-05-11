package queue;

import java.util.function.Function;
import java.util.function.Predicate;

public class LinkedQueue extends AbstractQueue {
    private static class Node {
        private Object element;
        private Node prev, next;

        public Node(Object element, Node prev, Node next) {
            this.element = element;
            this.prev = prev;
            this.next = next;
        }
    }
    private Node head, tail;

    @Override
    protected void enqueueImpl(final Object element) {
        Node tmp = new Node(element, tail, null);
        if (size == 1) {
            head = tmp;
        } else {
            tail.next = tmp;
        }
        tail = tmp;
    }

    @Override
    protected void pushImpl(Object element) {
        Node tmp = new Node(element, null, head);
        if (head != null) {
            head.next = tmp;
        }

        if (size == 1) {
            head = tmp;
            tail = tmp;
        } else {
            head = tmp;
        }
    }

    @Override
    protected Queue filterMap(Function<Object, Object> function, Predicate<Object> predicate) {
        LinkedQueue result = new LinkedQueue();
        Node current = head;
        while (current != null) {
            if (predicate.test(current.element)) {
                result.enqueue(function.apply(current.element));
            }
            current = current.next;
        }
        return result;
    }

    @Override
    protected Object peekImpl() {
        return tail.element;
    }

    @Override
    protected Object removeImpl(int size, Object element) {
        tail = tail.prev;
        tail.next = null;

        return element;
    }

    @Override
    protected Object dequeueImpl(Object element) {
        if (size == 0) {
            head = null;
            tail = null;
        } else {
            head = head.next;
            head.prev = null;
        }
        return element;
    }

    @Override
    protected int countImpl(Object x) {
        int count = 0;
        Node current = head;
        while (current != null) {
            if (current.element.equals(x)) {
                count++;
            }
            current = current.next;
        }
        return count;
    }

    @Override
    protected Object elementImpl() {
        return head.element;
    }

    @Override
    protected void clearImpl() {
        tail = null;
        head = null;
    }
}

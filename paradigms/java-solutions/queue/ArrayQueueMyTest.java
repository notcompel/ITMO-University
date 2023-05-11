package queue;

public class ArrayQueueMyTest {
    public static void main(String[] args) {
        ArrayQueue queue1 = new ArrayQueue();
        ArrayQueue queue2 = new ArrayQueue();
        for (int i = 0; i < 5; ++i) {
            queue1.push("q1" + i);
            queue2.push("q2" + i);
        }
        dumpQueue(queue1);
        dumpQueue(queue2);
    }

    private static void dumpQueue(ArrayQueue elem) {
        while (!elem.isEmpty()) {
            final Object value = elem.dequeue();
            System.out.println(elem.size() + " " + value);
        }
    }

}
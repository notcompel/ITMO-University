package queue;
import static queue.ArrayQueueADT.*;

public class ArrayQueueADTMyTest {
    public static void main(String[] args) {
        ArrayQueueADT queue1 = new ArrayQueueADT();
        ArrayQueueADT queue2 = new ArrayQueueADT();
        for (int i = 0; i < 5; ++i) {
            push(queue1, "q1" + i);
            push(queue2, "q2" + i);
        }
        dumpQueue(queue1);
        dumpQueue(queue2);
    }

    private static void dumpQueue(ArrayQueueADT queue) {
        while (!isEmpty(queue)) {
            final Object value = dequeue(queue);
            System.out.println(size(queue) + " " + value);
        }
    }

}

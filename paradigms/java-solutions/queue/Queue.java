package queue;

import java.util.function.Function;
import java.util.function.Predicate;

/*
Model: a[1]..a[n]
Invariant: for i=1..n: a[i] != null

Let immutable(n): for i=1..n: a'[i]==a[i]
 */

public interface Queue {
    //pred: true
    //post: R == n && n' == n && immutable(n)
    int size();

    //pred: true
    //post: R == (n == 0) && n' == n && immutable(n)
    boolean isEmpty();

    //pred: n >= 1
    //post: n' == n - 1 && for i=1..n': a'[i]==a[i+1] && R = a[n]
    Object remove();

    //pred: element != null
    //post: n' = n + 1 && a[n'] == element && immutable(n)
    void enqueue(final Object element);

    //pred: element != null
    //post: n' = n + 1 && a[1] == element && immutable(n)
    void push(Object element);

    //pred: n >= 1
    //post: n' == n && immutable(n) && R = a[1]
    Object peek();

    //pred: n >= 1
    //post: n' == n - 1 && for i=1..n': a'[i]==a[i+1] && R = a[1]
    Object dequeue();

    //pred: n >= 1
    //post: n' == n && immutable(n) && R = a[1]
    Object element();

    //pred: true
    //post: n' == 0
    void clear();

    //pred: x != null
    //post: R == |{i : a[i] == x}| && n' == n && immutable(n)
    int count(Object x);

    //pred: a != null
    //post: for i=1..n: R[i] = function(a[i]) && immutable(n);
    Queue map(Function<Object, Object> function);

    //pred: a != null
    //post: R = {a[i] : predicate(a[i]) == true} 0 <= i0 < ... < ik < n
    Queue filter(Predicate<Object> predicate);
}

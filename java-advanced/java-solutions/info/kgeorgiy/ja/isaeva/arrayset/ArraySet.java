package info.kgeorgiy.ja.isaeva.arrayset;

import java.util.*;

public class ArraySet<T extends Comparable<T>> extends AbstractSet<T> implements NavigableSet<T> {
    private final MyList<T> list;
    private Comparator<? super T> comparator = Comparator.naturalOrder();

    public ArraySet() {
        list = new MyList<>();
    }

    public ArraySet(Collection<T> c, Comparator<? super T> cmp) {
        if (cmp != null) comparator = cmp;
        TreeSet<T> treeSet = new TreeSet<>(cmp);
        treeSet.addAll(c);
        list = new MyList<>(treeSet);
    }

    public ArraySet(Collection<T> c) {
        this(c, null);
    }

    public ArraySet(Comparator<? super T> cmp) {
        list = new MyList<>();
        if (cmp != null) comparator = cmp;
    }

    public ArraySet(MyList<T> sorted, Comparator<? super T> cmp) {
        list = sorted;
        if (cmp != null) comparator = cmp;
    }

    private int getIndex(T t, boolean less, boolean border) {
        int i = Collections.binarySearch(list, t, comparator);
        if (i < 0) {
            i = -i - 1;
        }
        if (less && !border) {
            if (i < list.size() && compare(list.get(i), t) == 0) {
                return i + 1;
            }
        }
        if (less) {
            return i;
        }
        if (border) {
            if (i < list.size() && compare(list.get(i), t) == 0) {
                return i;
            }
        }
        return i - 1;
    }

    private T check(int i) {
        return (i >= 0 && i < list.size()) ? list.get(i) : null;
    }

    private int compare(T a, T b) {
        return comparator.compare(a, b);
    }


    @Override
    public T lower(T t) {
        int i = getIndex(t, false, false);
        return check(i);
    }

    @Override
    public T floor(T t) {
        int i = getIndex(t, false, true);
        return check(i);
    }

    @Override
    public T ceiling(T t) {
        int i = getIndex(t, true, true);
        return check(i);
    }

    @Override
    public T higher(T t) {
        int i = getIndex(t, true, false);
        return check(i);
    }

    @Override
    public T pollFirst() {
        throw new UnsupportedOperationException();
    }

    @Override
    public T pollLast() {
        throw new UnsupportedOperationException();
    }

    @Override
    public NavigableSet<T> descendingSet() {
        return new ArraySet<>(new MyList<>(list, !list.isReversed()), Collections.reverseOrder(comparator));
    }

    @Override
    public Iterator<T> descendingIterator() {
        return descendingSet().iterator();
    }

    @Override
    public NavigableSet<T> subSet(T fromElement, boolean fromInclusive, T toElement, boolean toInclusive) {
        int i = getIndex(fromElement, true, fromInclusive);
        int j = getIndex(toElement, false, toInclusive);
        if (compare(fromElement, toElement) > 0) {
            throw new IllegalArgumentException();
        }
        if (i > j)
            return new ArraySet<T>(comparator);
        return new ArraySet<>(list.subList(i, j + 1), comparator);
    }

    @Override
    public NavigableSet<T> headSet(T toElement, boolean inclusive) {
        int i = getIndex(toElement, false, inclusive);
        return new ArraySet<>(list.subList(0, i + 1), comparator);
    }

    @Override
    public NavigableSet<T> tailSet(T fromElement, boolean inclusive) {
        int i = getIndex(fromElement, true, inclusive);
        return new ArraySet<>(list.subList(i, list.size()), comparator);
    }

    @Override
    public Comparator<? super T> comparator() {
        if (comparator == Comparator.naturalOrder()) {
            return null;
        }
        return comparator;
    }

    @Override
    public SortedSet<T> subSet(T fromElement, T toElement) {
        return subSet(fromElement, true, toElement, false);
    }

    @Override
    public SortedSet<T> headSet(T toElement) {
        return headSet(toElement, false);
    }

    @Override
    public SortedSet<T> tailSet(T fromElement) {
        return tailSet(fromElement, true);
    }


    @Override
    public T first() {
        if (list.size() == 0) throw new NoSuchElementException();
        return list.get(0);
    }

    @Override
    public T last() {
        if (list.size() == 0) throw new NoSuchElementException();
        return list.get(list.size() - 1);
    }

    @Override
    public Iterator<T> iterator() {
        return Collections.unmodifiableList(list).iterator();
    }

    @Override
    public int size() {
        return list.size();
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean contains(Object object) {
        return Collections.binarySearch(list, (T) object, comparator) >= 0;
    }
}

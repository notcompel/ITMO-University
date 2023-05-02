package info.kgeorgiy.ja.isaeva.arrayset;

import java.util.*;

class MyList<T> extends AbstractList<T> implements RandomAccess {
    private final List<T> myList;
    private boolean isReversed = false;

    public MyList() {
        this.myList = Collections.emptyList();
    }

    public MyList(List<T> second, boolean isReversed) {
        this.myList = second;
        this.isReversed = isReversed;
    }

    public MyList(MyList<T> second, boolean isReversed) {
        this.myList = second.myList;
        this.isReversed = isReversed;
    }

    public MyList(Collection<T> c) {
        myList = new ArrayList<>(c);
    }

    @Override
    public T get(int index) {
        return myList.get(isReversed ? myList.size() - index - 1 : index);
    }

    @Override
    public MyList<T> subList(int fromIndex, int toIndex) {
        return new MyList<>(myList.subList(fromIndex, toIndex), isReversed);
    }

    @Override
    public int size() {
        return myList.size();
    }

    public boolean isReversed() {
        return isReversed;
    }
}
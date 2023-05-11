import java.util.*;

public class IntList {
    private int[] a;
    public int len;

    public IntList() {
        a = new int[1];
        len = 0;
    }
    public void add(int x) {
        if (len == a.length) {
            a = Arrays.copyOf(a, a.length * 2);
        }
        a[len] = x;
        len++;
    }

    public int get(int i) {
        return a[i];
    }

    public void set(int i, int x) {
        a[i] = x;
    }

}
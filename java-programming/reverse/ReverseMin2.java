
import java.io.*;
import java.util.*;

public class ReverseMin2 { //WORK COD
    public static void main(String[] args) {
        try {
            MyScanner in = new MyScanner(System.in);

            int[][] a = new int[1][1];
            int max = -1;
            int n = 0;
            while (in.hasNextLine()) {
                int i = 0;
                String now = in.nextLine();
                a[n] = new int[1];
                for (int j = 0; j < now.length(); j++) {
                    if (!Character.isWhitespace(now.charAt(j))) {
                        int begin = j;
                        while (j < now.length() - 1 && !(Character.isWhitespace(now.charAt(j + 1)))) {
                            j++;
                        }
                        if (i == a[n].length) {
                            a[n] = Arrays.copyOf(a[n], a[n].length * 2);
                        }
                        a[n][i] = Integer.parseInt(now.substring(begin, j + 1));
                        i++;
                    }
                }
                a[n] = Arrays.copyOf(a[n], i);
                if (i > max) {
                    max = i;
                }
                if (n >= a.length - 1) {
                    a = Arrays.copyOf(a, a.length * 2);
                }
                n++;
            }

            int[] mins = new int[max];
            for (int i = 0; i < max; ++i) {
                mins[i] = Integer.MAX_VALUE;
            }
            for (int i = 0; i < n; ++i) {
                int min = Integer.MAX_VALUE; //минимальное в строке
                for (int j = 0; j < a[i].length; ++j) {

                    min = Math.min(min, Math.min(a[i][j], mins[j]));
                    mins[j] = min;
                    System.out.print(mins[j] + " ");
                }
                System.out.println();
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}





/*
public class ReverseMin2 {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        String[] numbers = new String[1000000];

        int[][] a = new int[1][];
        int max = -1;
        int n = 0;
        while (in.hasNextLine())
        {
            int i = 0;
            String now = in.nextLine();
            int[] num = new int[1];
            for (int j = 0; j < now.length(); j++) {
                int begin = j;
                while (j < now.length() - 1 && !(Character.isWhitespace(now.charAt(j + 1)))) {
                    j++;
                }
                if (i == num.length) {
                    num = Arrays.copyOf(num, num.length * 2);
                }
                num[i] = Integer.parseInt(now.substring(begin, j + 1));
                i++;
            }
            if (n > max) {
                max = n;
            }
            if (n == a.length) {
                a = Arrays.copyOf(a, a.length * 2);
            }
            a[n] = num;
            n++;
        }

        int[] mins = new int[max];
        for (int i = 0; i < max; ++i) {
            mins[i] = Integer.MAX_VALUE;
        }
        for (int i = 0; i < n; ++i) {
            int min = Integer.MAX_VALUE; //минимальное в строке
            for (int j = 0; j < a[i].length; ++j) {
                min = Math.min(min, Math.min(a[i][j], mins[j]));
                mins[j] = min;
                System.out.println(mins[j] + " ");
            }
            System.out.println();
        }









        /*
        for (int i = 0; numbers[i] != null; i++) {
            int n = 0;
            /*for (int j = 0; j < numbers[i].length(); j++) {
                if (!Character.isWhitespace(numbers[i].charAt(j))) {
                    int end = j;
                    while (j < numbers[i].length() - 1 && !(Character.isWhitespace(numbers[i].charAt(j + 1)))) {
                        j++;
                    }
                    n++;
                }
            }
            if (n > max) {
                max = n;
            }
            a[i] = new int[max];
            len.add(n);
            int k = 0;
            for (int j = 0; j < numbers[i].length(); j++) {
                if (!Character.isWhitespace(numbers[i].charAt(j))) {
                    int begin = j;
                    while (j < numbers[i].length() - 1 && !(Character.isWhitespace(numbers[i].charAt(j + 1)))) {
                        j++;
                    }
                    int x = Integer.parseInt(numbers[i].substring(begin, j + 1));
                    if (k > 0) {
                        x = Math.min(x, a[i][k - 1]);
                    }
                    if (i > 0 && a[i - 1].length != 0 && k < a[i - 1].length) {
                        x = Math.min(x, a[i - 1][k]);
                    }
                    a[i][k] = x;
                    k++;
                }
            }

            for (int j = k; j < max; ++j) {
                a[i][j] = Integer.MAX_VALUE;
                if (j > 0) {
                    a[i][j] = a[i][j - 1];
                }
                a[i][j] = Math.min(a[i][j], a[i - 1][j]);
            }
        }
        for (int i = 0; i < len.size(); ++i) {
            for (int j = 0; j < len.get(i); ++j) {
                System.out.print(a[i][j] + " ");
            }
            System.out.println();
        }
    }
}  */
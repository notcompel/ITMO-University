
import java.io.*; //WORKING COD
import java.util.*;

public class Reverse {
    public static void main(String[] args) {
        try {
            MyScanner in = new MyScanner(System.in);

            String[] numbers = new String[1000000];
            int l = 0;
            while (in.hasNextLine()) {
                numbers[l] = in.nextLine();
                l++;
            }
            for (int i = l - 1; i >= 0; i--) {
                for (int j = numbers[i].length() - 1; j >= 0; j--) {
                    int n = 0;

                    if (!Character.isWhitespace(numbers[i].charAt(j))) {
                        int end = j;
                        while (j > 0 && !(Character.isWhitespace(numbers[i].charAt(j - 1)))) {
                            j--;
                        }
                        //String now = numbers[i].substring(j, end + 1) + " "
                        System.out.print(numbers[i].substring(j, end + 1) + " ");
                    }
                }
                System.out.println();
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}

/*
int prev = pos;
        char c;
        while (hasNextChar()) {
            c = nextChar();
            if (c == '\n') {
                pos = prev;
                return false;
            }
            if (!Character.isWhitespace(c)) {
                pos = prev;
                return true;
            }
        }
 */
/*
import java.util.Scanner;
import java.util.ArrayList;

public class Reverse {
    public static void main(String[] args) {

        Scanner in = new Scanner(System.in);
        String[] numbers = new String[1000000];
        int l = 0;
        while (in.hasNextLine()) {
            numbers[l] = in.nextLine();
            l++;
        }
        ArrayList<Integer> len = new ArrayList<Integer>();
        int[][] a = new int[l + 1][];
        int max = -1;
        for (int i = 0; numbers[i] != null; i++) {
            int n = 0;
            for (int j = 0; j < numbers[i].length(); j++) {
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
                    System.out.println("*");
                    if (k > 0) {
                        x = Math.min(x, a[i][k - 1]);
                    }

                    if (i > 0) {
                        x = Math.min(x, a[i - 1][k]);
                    }
                    a[i][k] = x;
                    k++;
                }

            }
            for (int j = k; k < max; ++k) {
                if (k > 0) {
                    a[i][k] = Math.min(x, a[i][k - 1]);
                }
                if (i > 0) {
                    a[i][k] = Math.min(x, a[i - 1][k]);
                }
                a[i][k] = Integer.MAX_VALUE;
            }
        }

        for (int i = 0; i < len.size(); ++i) {
            for (int j = 0; j < len.get(i); ++j) {
                System.out.print(a[i][j] + " ");
            }
            System.out.println();
        }
    }
}
/*import java.util.Scanner;
import java.util.ArrayList;

public class Reverse {
    public static void main(String[] args) {

        Scanner in = new Scanner(System.in);
        String[] numbers = new String[1000000];
        int ind = 0;
        while (in.hasNextLine()) {
            numbers[ind] = in.nextLine();
            ind++;
        }
        ArrayList<Integer> num = new ArrayList<Integer>();
        ArrayList<Integer> len = new ArrayList<Integer>();

        for (int i = 0; numbers[i] != null; i++) {
            int n = 0;
            for (int j = 0; j < numbers[i].length(); j++) {
                if (!Character.isWhitespace(numbers[i].charAt(j))) {
                    int begin = j;
                    while (j < numbers[i].length() - 1 && !(Character.isWhitespace(numbers[i].charAt(j + 1)))) {
                        j++;
                    }
                    n++;
                    num.add(Integer.parseInt(numbers[i].substring(begin, j + 1)));
                    //System.out.print(num.get(num.size() - 1) + " ");
                }
            }
            len.add(n);
        }
        int k = 0;

        int prev = Integer.MAX_VALUE;
        max = len.get(0);
        for (int i = 0; i < len.size(); ++i) {
            for (int j = 0; j < max; ++j) {
                if (j == 0) {
                    prev = Integer.MAX_VALUE;
                }
                if (num.get(k) > prev) {
                    num.set(k, prev);
                }
                if (i > 0 && num.get(k) > num.get(k - max)) {
                    num.set(k, num.get(k - max));
                }
                if (len.get(i) > max) {
                    max = len.get(i);
                }
                //System.out.print(num.get(k) + " ");
                prev = num.get(k);
                k++;
            }
        }

        k = 0;
        for (int i = 0; i < len.size(); ++i) {
            for (int j = 0; j < len.get(i); ++j) {
                if (j < )
                System.out.print(num.get(k) + " ");
                k++;
            }
            System.out.println();
        }
    }
}

/*import java.util.Scanner;
import java.util.ArrayList;

public class Reverse {
    public static void main(String[] args) {
        
        Scanner in = new Scanner(System.in);
        String[] numbers = new String[1000000];
        int l = 0;
        while (in.hasNextLine()) {
            numbers[l] = in.nextLine();
            l++;
        }
        ArrayList<ArrayList<Integer>> arr = new ArrayList<ArrayList<Integer>>(l + 1);
        for (int i = numbers.length - 1; numbers[i] != null; i--) {
            for (int j = numbers[i].length() - 1; j >= 0; j--) {
                int k = 0;
                if (!Character.isWhitespace(numbers[i].charAt(j))) {
                    int end = j;
                    while (j > 0 && !(Character.isWhitespace(numbers[i].charAt(j - 1)))) {
                        j--;
                    }
                    Integer x = Integer.parseInt(numbers[i].substring(j, end + 1));
                    if (i == 0 && k == 0) {
                        arr.get(i).add(x);
                    } else if (i == 0) {
                        arr.get(i).add(Math.min(arr.get(i).get(k - 1), x));
                    } else if (k == 0) {
                        arr.get(i).add(Math.min(arr.get(i - 1).get(k), x));
                    } else {
                        arr.get(i).add(Math.min(arr.get(i).get(k - 1), arr.get(i - 1).get(k), x));
                    }
                    k++;
                }
            }
        }
        System.out.println(1000);
    }
}


/*
import java.util.Scanner;
import java.util.ArrayList;

public class Reverse {
    public static void main(String[] args) {

        Scanner in = new Scanner(System.in);
        String[] numbers = new String[1000000];
        int i = 0;
        while (in.hasNextLine()) {
            numbers[i] = in.nextLine();
            i++;
        }
        int[][] a = new int[numbers.length];
        for (int i = numbers.length() - 1; numbers[i] != null; i--) {
            for (int j = numbers[i].length() - 1; j >= 0; j--) {
                int n = 0;
                if (!Character.isWhitespace(numbers[i].charAt(j))) {
                    int end = j;
                    while (j > 0 && !(Character.isWhitespace(numbers[i].charAt(j - 1)))) {
                        j--;
                    }
                    //System.out.print(numbers[i].substring(j, end + 1) + " ");
                }
            }
            System.out.println();
        }
    }
}
 */
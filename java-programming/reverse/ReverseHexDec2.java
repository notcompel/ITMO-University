import java.io.*;
import java.util.*;

public class ReverseHexDec2 { 
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
                        String x = numbers[i].substring(j, end + 1);
                        String tmp = "";
                        if (x.length() > 1 && x.charAt(0) == '0' && (x.charAt(1) == 'x' || x.charAt(1) == 'X')) {
                            tmp = x.substring(2, x.length());
                        } else {
                            tmp = Integer.toHexString(Integer.parseInt(x));
                        }
                        //String now = numbers[i].substring(j, end + 1) + " "
                        //System.out.print("0x" + a[i][j] + " ");
                        System.out.print("0x" + tmp + " ");
                    }
                }
                System.out.println();
            }
           /* MyScanner in = new MyScanner(System.in);

            String[][] a = new String[1][1];
            int n = 0;
            while (in.hasNextLine()) {
                int i = 0;
                a[n] = new String[1];

                String x = in.nextPart(0);
                do {
                    in.firstinline = false;

                    if (x.isEmpty()) {
                        break;
                    }
                    if (i == a[n].length) {
                        a[n] = Arrays.copyOf(a[n], a[n].length * 2);
                    }
                    if (x.length() > 1 && x.charAt(0) == '0' && (x.charAt(1) == 'x' || x.charAt(1) == 'X')) {
                        a[n][i] = x.substring(2, x.length());
                    } else {
                        a[n][i] = Integer.toHexString(Integer.parseInt(x));
                    }
                    i++;

                    x = in.nextPart(0);
                } while (in.hasNextChar() && !in.firstinline);
                a[n] = Arrays.copyOf(a[n], i);
                if (n >= a.length - 1) {
                    a = Arrays.copyOf(a, a.length * 2);
                }
                n++;
            }
            for (int i = n - 1; i >= 0; --i) {
                for (int j = a[i].length - 1; j >= 0; --j) {
                    System.out.print("0x" + a[i][j] + " ");
                }
                System.out.println();
            } */
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
} 




/*import java.io.*;
import java.util.*;

public class ReverseHexDec2 { /
    public static void main(String[] args) {
        try {
            MyScanner in = new MyScanner(System.in);

            String[][] a = new String[1][1];
            int n = 0;
            while (in.hasNextLine()) {
                int i = 0;
                a[n] = new String[1];
                char c = nextChar();
                StringBuilder sb;
                while (c != '\n' && c != '\r') {
                    String x = in.nextPart();
                    if (x.length() > 1 && x.charAt(0) == '0' && (x.charAt(1) == 'x' || x.charAt(1) == 'X')) {
                        a[n][i] = x.substring(2, x.length());
                    } else {
                        a[n][i] = Integer.toHexString(Integer.parseInt(x));
                    }

                    //System.out.print(a[n][i]);
                    i++;
                }
                a[n] = Arrays.copyOf(a[n], i);
                if (n >= a.length - 1) {
                    a = Arrays.copyOf(a, a.length * 2);
                }
                n++;
            }

            for (int i = n - 1; i >= 0; --i) {
                for (int j = a[i].length - 1; j >= 0; --j) {
                    System.out.print("0x" + a[i][j] + " ");
                }
                System.out.println();
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
/* import java.io.*;
import java.util.*;

public class ReverseHexDec2 {
    public static void main(String[] args) {
        try {
            MyScanner in = new MyScanner(System.in);

            String[][] a = new String[1][1];
            int n = 0;
            while (in.hasNextLine()) {
                int i = 0;
                a[n] = new String[1];
                while (in.hasNextCharInLine()) {
                    String x = in.nextPart();
                    if (x.length() > 1 && x.charAt(0) == '0' && (x.charAt(1) == 'x' || x.charAt(1) == 'X')) {
                        a[n][i] = x.substring(2, x.length());
                    } else {
                        a[n][i] = Integer.toHexString(Integer.parseInt(x));
                    }

                    //System.out.print(a[n][i]);
                    i++;
                }
                a[n] = Arrays.copyOf(a[n], i);
                if (n >= a.length - 1) {
                    a = Arrays.copyOf(a, a.length * 2);
                }
                n++;
            }
            
            for (int i = n - 1; i >= 0; --i) {
                for (int j = a[i].length - 1; j >= 0; --j) {
                    System.out.print("0x" + a[i][j] + " ");
                }
                System.out.println();
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
/*import java.io.*; //WORKING COD
import java.util.*;

public class ReverseHexDec2 {
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
                        String now = numbers[i].substring(j, end + 1);
                        if (now.length() > 1 && now.charAt(0) == '0' && (now.charAt(1) == 'x' || now.charAt(1) == 'X')) {
                            System.out.print(now + " ");
                        } else {
                            String x = Integer.toHexString(Integer.parseInt(now));
                            System.out.print("0x" + x + " ");
                        }

                    }
                }
                System.out.println();
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
} */
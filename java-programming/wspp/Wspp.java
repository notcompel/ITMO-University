import java.io.*;
import java.util.*;

public class Wspp {
    public static void main(String[] args) {
        LinkedHashMap<String, IntList> mp = new LinkedHashMap<String, IntList>();
        int index = 0;
        try {
            MyScanner in = new MyScanner(new File(args[0]));
            //MyScanner in = new MyScanner(System.in);
            try {
                while (in.hasNextChar()) {
                    String str = in.nextWord().toLowerCase();
                    if (str.isEmpty()) {
                        continue;
                    }
                    index++;
                    if (mp.containsKey(str)) {
                        IntList tmp = mp.get(str);
                        tmp.add(index);
                        mp.put(str, tmp);
                    } else {
                        IntList tmp = new IntList();
                        tmp.add(index);
                        mp.put(str, tmp);
                    }
                }
            } finally {
                in.close();
            }
        } catch (IOException e) {
            System.out.println("Cannot read input file " + e.getMessage());
        }
        /*for (Map.Entry<String, IntList> i : mp.entrySet()) {
            IntList tmp = i.getValue();
            System.out.print(i.getKey() + " ");
            System.out.print(tmp.len);
            for (int j = 0; j < tmp.len; j++) {
                System.out.print(" " + tmp.get(j));
            }
            System.out.println();
        } */
        try {
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(args[1]), "utf-8"));
            try {
                for (Map.Entry<String, IntList> i : mp.entrySet()) {
                    IntList tmp = i.getValue();
                    out.write(i.getKey() + " " + tmp.len);
                    for (int j = 0; j < tmp.len; j++) {
                        out.write(" " + tmp.get(j));
                    }
                    out.newLine();
                }
            } finally {
                out.close();
            }
        } catch (IOException e1) {
            System.out.println("Cannot write to output file " + e1.getMessage());
        }
    }
}

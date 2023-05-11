import java.io.*;
import java.util.*;

public class WsppSecondG {
    public static void main(String[] args) {
        Map<String, IntList> mp = new LinkedHashMap<String, IntList>();
        Map<String, Boolean> flags = new LinkedHashMap<String, Boolean>();
        int index = 0;
        try {
            MyScanner in = new MyScanner(new File(args[0]));
            //MyScanner in = new MyScanner(System.in);
            try {
                while (in.hasNextToken()) {
                    String str = in.nextPart(1).toLowerCase();
                    System.out.println(in.firstinline);
                    if (in.firstinline) {
                        for (Map.Entry<String, Boolean> i : flags.entrySet()) {
                            flags.put(i.getKey(), false);
                        }
                        in.firstinline = false;
                    }
                    if (str.isEmpty()) {
                        continue;
                    }
                    index++;
                    if (mp.containsKey(str)) {
                        IntList tmp = mp.get(str);
                        if (flags.get(str)) {
                            tmp.add(index);
                        }
                        flags.put(str, !flags.get(str));
                        tmp.set(0, tmp.get(0) + 1);
                        mp.put(str, tmp);

                    } else {
                        IntList tmp = new IntList();
                        tmp.add(1);
                        mp.put(str, tmp);
                        flags.put(str, true);
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
            System.out.print(i.getKey());
            for (int j = 0; j < tmp.len; j++) {
                System.out.print(" " + tmp.get(j));
            }
            System.out.println();
        }*/
        try {
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(args[1]), "utf-8"));
            try {
                for (Map.Entry<String, IntList> i : mp.entrySet()) {
                    IntList tmp = i.getValue();
                    out.write(i.getKey());
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









/* */
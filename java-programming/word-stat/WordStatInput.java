
import java.io.*;
import java.util.*;

public class WordStatInput {
    public static void main(String[] args) {
        Map<String, Integer> mp = new LinkedHashMap<String, Integer>();
        try {
            MyScanner in = new MyScanner(new File(args[0]));
            try {
                while (in.hasNextChar()) {
                    String str = in.nextWord().toLowerCase();
                    if (!str.isEmpty()) {
                        mp.put(str, mp.getOrDefault(str, 0) + 1);
                    }
                }
            } finally {
                in.close();
            }
        } catch (IOException e) {
            System.out.println("Cannot read input file " + e.getMessage());
        }

        try {
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(args[1]), "utf-8"));
            try {
                for (Map.Entry<String, Integer> i : mp.entrySet()) {
                    out.write(i.getKey() + " " + i.getValue());
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

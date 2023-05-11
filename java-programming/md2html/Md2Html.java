package md2html;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Md2Html {
    public static void main(String[] args) {
        StringBuilder text = new StringBuilder();
       /* Scanner in = new Scanner(System.in);
            StringBuilder paragraph = new StringBuilder();
                String s = "";
                while (in.hasNextLine()) {
                    s = in.nextLine();
                    while (s != null && !s.equals("")) {
                        paragraph.append(s);
                        paragraph.append('\n');
                        s = in.nextLine();
                    }
                    if (paragraph.length() != 0) {
                        paragraph = new StringBuilder(paragraph.toString().substring(0, paragraph.length() - 1));
                        new Parser(paragraph).toHtml(text);
                        text.append('\n');
                        paragraph = new StringBuilder();
                    }
                    System.out.println(text.toString());
                } */


        try {
            Scanner in = new Scanner(new File(args[0]), StandardCharsets.UTF_8);
            try {
                StringBuilder paragraph = new StringBuilder();
                String s = "";
                while (in.hasNextLine()) {
                     s = in.nextLine();
                     if (s == null) {
                         continue;
                     }
                    while (s != null && !s.equals("")) {
                        paragraph.append(s);
                        paragraph.append('\n');
                        if (in.hasNextLine()) {
                            s = in.nextLine();
                        } else {
                            break;
                        }
                    }

                    if (paragraph.length() != 0) {
                        paragraph = new StringBuilder(paragraph.toString().substring(0, paragraph.length() - 1));
                        new Parser(paragraph).toHtml(text);
                        text.append('\n');
                        paragraph = new StringBuilder();
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
                out.write(text.toString());
            } finally {
                out.close();
            }
        } catch (IOException e1) {
            System.out.println("Cannot write to output file " + e1.getMessage());
        }
    }
}

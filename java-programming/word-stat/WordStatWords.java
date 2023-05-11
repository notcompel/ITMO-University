import java.io.*;
import java.util.*;

public class WordStatWords {
    public static void main(String[] args) {
        try {
            Scanner in = new Scanner(new File(args[0]), "utf-8");
            //Scanner in = new Scanner(System.in);
            TreeMap<String, Integer> mp = new TreeMap<String, Integer>();

            ArrayList<String> arr = new ArrayList<String>();
            while (in.hasNextLine()) {
                String str = in.nextLine();
                for (int i = 0; i < str.length(); ++i) {
                    if (Character.isLetter(str.charAt(i)) || Character.getType(str.charAt(i)) == Character.DASH_PUNCTUATION || str.charAt(i) == '\'') {
                        int begin = i, j = i;
                        while (j < str.length() - 1 && (Character.isLetter(str.charAt(j + 1)) || Character.getType(str.charAt(j + 1)) == Character.DASH_PUNCTUATION || str.charAt(j + 1) == '\'')) {
                            j++;
                        }
                        String tmp = str.substring(begin, j + 1).toLowerCase();
                        if (mp.containsKey(tmp)) {
                            mp.put(tmp, mp.get(tmp) + 1);

                        } else {
                            mp.put(tmp, 1);
                            arr.add(tmp);
                        }
                        i = j + 1;
                    }
                }
            }
            in.close();
            /*for (Map.Entry<String, Integer> i: mp.entrySet()) {
                System.out.print(i.getKey() + " " + i.getValue() + "\n");
            } */

           try {
                Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(args[1]), "utf-8"));
                for (Map.Entry<String, Integer> i: mp.entrySet()) {
                    out.write(i.getKey() + " " + i.getValue() + "\n");
                }

                out.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
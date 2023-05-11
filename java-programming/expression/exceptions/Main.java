package expression.exceptions;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        String tmp = in.nextLine();
        try {
            //System.out.println(new ExpressionParser().parse(tmp).evaluate(1, 1, 1));
            System.out.println(new ExpressionParser().parse(tmp).toMiniString());
        } catch (ParsingException e) {
            System.out.println(e.getMessage());
        }
    }
}

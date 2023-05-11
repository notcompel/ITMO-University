package expression;

import expression.parser.ExpressionParser;
import expression.parser.Parser;

import java.math.BigInteger;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        String tmp = in.nextLine();
        System.out.println(new ExpressionParser().parse(tmp).toMiniString());
    }
}

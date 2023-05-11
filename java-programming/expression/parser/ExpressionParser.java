package expression.parser;
import expression.*;

import java.util.Map;

public class ExpressionParser implements Parser {
    public TripleExpression parse(final String source) {
        //System.err.println(source);
        return new Parser(source).parse();
    }

    private static class Parser extends BaseParser {
        Map<String, Integer> priority = Map.of(
                "unary", UnaryOperation.priority,
                "element", Element.priority,
                "*", Multiply.priority,
                "/", Divide.priority,
                "-", Subtract.priority,
                "+", Add.priority,
                "min", Min.priority,
                "max", Max.priority
        );

        public Parser(String source) {
            super(source);
        }
        public TripleExpression parse() {
            skipWhiteSpace();
            TripleExpression result = parseExpression(-1);
            skipWhiteSpace();

            if (!end()) {
                throw error("Expected end of input");
            }
            return result;
        }


        public TripleExpression parseExpression(int prevPrior) {
            TripleExpression output = takeElement();

            skipWhiteSpace();
            while (!end() && !test(')') && prevPrior < priority.get(checkNext())) {
                output = takeOperation(output);
            }

            return output;
        }


        private TripleExpression takeElement() {
            if (take("-")) {
                if (between('1', '9')) {
                    return new Const(parseNumber(true));
                } else {
                    return new Negate((Basic) parseExpression(priority.get("unary")));
                }
            } else if (take("l0")) {
                return new L0((Basic) parseExpression(priority.get("unary")));
            } else if (take("t0")) {
                return new T0((Basic) parseExpression(priority.get("unary")));
            } else if (take("(")) {
                TripleExpression output = parseExpression(-1);
                expect(')');
                return output;
            } else if (between('0', '9')) {
                return new Const(parseNumber(false));
            } else if (!end()) {
                return new Variable(takeWord());
            }
            return null;
        }

        private TripleExpression takeOperation(TripleExpression output) {
            if (take("+")) {
                output = new Add((Basic) output, (Basic)parseExpression(priority.get("+")));
            } else if (take("-")) {
                output = new Subtract((Basic) output, (Basic)parseExpression(priority.get("-")));
            } else if (take("/")) {
                output = new Divide((Basic) output, (Basic)parseExpression(priority.get("/")));
            } else if (take("*")) {
                output = new Multiply((Basic) output, (Basic)parseExpression(priority.get("*")));
            } else if (take("min")) {
                output = new Min((Basic) output, (Basic) parseExpression(priority.get("min")));
            } else if (take("max")) {
                output = new Max((Basic) output, (Basic) parseExpression(priority.get("max")));
            }
            return output;
        }

        private Integer parseNumber(boolean negate) {
            final StringBuilder number = new StringBuilder();
            if (negate) {
                number.append('-');
            }
            while (between('0', '9')) {
                number.append(take());
            }
            try {
                return Integer.parseInt(number.toString());
            } catch (NumberFormatException e) {
                throw error("Invalid number: " + number + " " + e.getMessage());
            }
        }

    }
}

//после скобки - число
//после числа - операция
//после операции - число или скоба
//после переменной - операция
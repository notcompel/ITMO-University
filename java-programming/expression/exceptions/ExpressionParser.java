package expression.exceptions;
import expression.*;

import java.util.Map;

public class ExpressionParser implements Parser {
    public TripleExpression parse(final String source) throws ParsingException {
        //System.err.println(source);
        return new Parser(source).parse();
    }

    private static class Parser extends BaseParser {
        Map<String, Integer> priority = Map.of(
                "unary", UnaryOperation.priority,
                "element", Element.priority,
                "**", CheckedPow.priority,
                "//", CheckedLog.priority,
                "*", CheckedMultiply.priority,
                "/", CheckedDivide.priority,
                "-", CheckedSubtract.priority,
                "+", CheckedAdd.priority,
                "min", Min.priority,
                "max", Max.priority
        );
        public Parser(String source) {
            super(source);
        }
        public TripleExpression parse() throws ParsingException {
            skipWhiteSpace();
            TripleExpression result = parseExpression(-1);
            skipWhiteSpace();

            if (!end()) {
                throw error("expected end of input");
            }
            return result;
        }

        public TripleExpression parseExpression(int prevPrior) throws ParsingException {
            TripleExpression output = takeElement();

            skipWhiteSpace();
            while (!end() && !checkNext().equals(")")) {
                String op = checkNext();
                if (!priority.containsKey(op)) {
                    throw error(messageAboutMissingOperation);
                }
                if (prevPrior >= priority.get(op)) {
                    break;
                }
                output = takeOperation(output);
            }

            if (output == null) {
                throw error(messageAboutMissingArgument);
            }
            return output;
        }

        private TripleExpression takeElement() throws ParsingException {
            if (take("-")) {
                if (between('1', '9')) {
                    return new Const(parseNumber(true));
                } else {
                    return new CheckedNegate((Basic) parseExpression(4));
                }
            } else if (take("l0")) {
                return new L0((Basic) parseExpression(4));
            } else if (take("t0")) {
                return new T0((Basic) parseExpression(4));
            } else if (take("abs")) {
                return new CheckedAbs((Basic) parseExpression(4));
            } else if (take("(")) {
                TripleExpression output = parseExpression(-1);
                if (!take(")")) {
                    throw error(messageAboutClosingParenthesis);
                }
                return output;
            } else if (between('0', '9')) {
                return new Const(parseNumber(false));
            } else if (checkNext().equals("x") || checkNext().equals("y") || checkNext().equals("z")) {
                return new Variable(take() + "");
            } else {
                throw error(messageAboutMissingArgument);
            }
            /* else if (!end() && output == null) {
                output = new Variable(takeWord());
            } */
        }

        private TripleExpression takeOperation(TripleExpression output) throws ParsingException {
           if (take("+")) {
                output = new CheckedAdd((Basic) output, (Basic)parseExpression(priority.get("+")));
            } else if (take("-")) {
                output = new CheckedSubtract((Basic) output, (Basic)parseExpression(priority.get("-")));
            } else if (take("//")) {
                output = new CheckedLog((Basic) output, (Basic) parseExpression(priority.get("//")));
            } else if (take("**")) {
                output = new CheckedPow((Basic) output, (Basic) parseExpression(priority.get("**")));
            } else if (take("/")) {
                output = new CheckedDivide((Basic) output, (Basic)parseExpression(priority.get("/")));
            } else if (take("*")) {
                output = new CheckedMultiply((Basic) output, (Basic)parseExpression(priority.get("*")));
            } else if (take("min")) {
                output = new Min((Basic) output, (Basic) parseExpression(priority.get("min")));
            } else if (take("max")) {
                output = new Max((Basic) output, (Basic) parseExpression(priority.get("max")));
            }  else if (!end()) {
                throw error(messageAboutMissingOperation);
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
                throw new OverflowException();
            }
        }

    }
}

//после скобки - число
//после числа - операция
//после операции - число или скоба
//после переменной - операция
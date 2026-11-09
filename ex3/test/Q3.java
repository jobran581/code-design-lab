package test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class Q3 {

	public static double calc(String expression) {
		List<String> postfix = toPostfix(tokenize(expression));
		double result = buildExpression(postfix).calculate();
		return roundTiny(result);
	}

	// removes tiny floating-point errors, e.g. 5.000000000000003 -> 5.0
	private static double roundTiny(double x) {
		if (Double.isNaN(x) || Double.isInfinite(x))
			return x;
		return BigDecimal.valueOf(x).setScale(10, RoundingMode.HALF_UP).doubleValue();
	}

	// splits the text into numbers, operators and parentheses (spaces are ignored)
	private static List<String> tokenize(String exp) {
		List<String> tokens = new ArrayList<>();
		int i = 0;
		while (i < exp.length()) {
			char c = exp.charAt(i);
			if (Character.isWhitespace(c)) {
				i++;
			} else if (Character.isDigit(c) || c == '.') {
				int start = i;
				while (i < exp.length() && (Character.isDigit(exp.charAt(i)) || exp.charAt(i) == '.'))
					i++;
				tokens.add(exp.substring(start, i));
			} else if ("+-*/()".indexOf(c) >= 0) {
				tokens.add(String.valueOf(c));
				i++;
			} else {
				throw new IllegalArgumentException("unexpected character: " + c);
			}
		}
		return tokens;
	}

	private static boolean isOperator(String t) {
		return t.equals("+") || t.equals("-") || t.equals("*") || t.equals("/");
	}

	private static int precedence(String op) {
		return (op.equals("*") || op.equals("/")) ? 2 : 1;
	}

	// Dijkstra's Shunting-yard: infix tokens -> postfix queue
	private static List<String> toPostfix(List<String> tokens) {
		List<String> queue = new ArrayList<>();
		Deque<String> stack = new ArrayDeque<>();
		for (String t : tokens) {
			if (isOperator(t)) {
				// pop operators of greater or equal precedence (equal keeps left-to-right order, e.g. 6-2-1)
				while (!stack.isEmpty() && isOperator(stack.peek())
						&& precedence(stack.peek()) >= precedence(t))
					queue.add(stack.pop());
				stack.push(t);
			} else if (t.equals("(")) {
				stack.push(t);
			} else if (t.equals(")")) {
				while (!stack.peek().equals("("))
					queue.add(stack.pop());
				stack.pop(); // pop "("
			} else {
				queue.add(t); // number
			}
		}
		while (!stack.isEmpty())
			queue.add(stack.pop());
		return queue;
	}

	// builds the Interpreter tree (Plus, Minus, Mul, Div, Number) from the postfix queue
	private static Expression buildExpression(List<String> postfix) {
		Deque<Expression> stack = new ArrayDeque<>();
		for (String t : postfix) {
			if (isOperator(t)) {
				Expression right = stack.pop();
				Expression left = stack.pop();
				switch (t) {
				case "+": stack.push(new Plus(left, right)); break;
				case "-": stack.push(new Minus(left, right)); break;
				case "*": stack.push(new Mul(left, right)); break;
				default:  stack.push(new Div(left, right)); break;
				}
			} else {
				stack.push(new Number(Double.parseDouble(t)));
			}
		}
		return stack.pop();
	}
}

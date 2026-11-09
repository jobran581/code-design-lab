package test;

import java.util.ArrayList;
import java.util.List;
import java.util.function.UnaryOperator;

public class MyStringBuilder {
	// ---------- DO NOT Change ----------------
	ArrayList<Character> buff;
	
	public MyStringBuilder() {
		buff=new ArrayList<>();
	}
	
	public MyStringBuilder(char...ts) {
		buff=new ArrayList<>();
		for(char t : ts)
			buff.add(t);
	}
	
	// -----------------------------------------
	
	// adds the characters of s to the end of the buffer
	public MyStringBuilder append(String s) {
		if (s != null)
			for (int i = 0; i < s.length(); i++)
				buff.add(s.charAt(i));
		return this; // returning this enables fluent chaining
	}

	// reverses the order of the characters in the buffer
	public MyStringBuilder reverse() {
		int i = 0, j = buff.size() - 1;
		while (i < j) {
			Character tmp = buff.get(i);
			buff.set(i++, buff.get(j));
			buff.set(j--, tmp);
		}
		return this;
	}

	// applies the given function to every character in the buffer
	public MyStringBuilder apply(UnaryOperator<Character> f) {
		for (int i = 0; i < buff.size(); i++)
			buff.set(i, f.apply(buff.get(i)));
		return this;
	}

	// builds the String directly from a char array
	@Override
	public String toString() {
		char[] chars = new char[buff.size()];
		for (int i = 0; i < chars.length; i++)
			chars[i] = buff.get(i);
		return new String(chars);
	}
}

public class StringHolder {//helper for parsing expressions
	protected final String expr;
	protected int pointer;

	StringHolder(String expr) {
		this.expr = expr;
		pointer = 0;
	}

	protected Character getChar(String charset) {//returns next char only if it is in charset
		Character ch = null;
		while ((pointer < expr.length()) && Character.isSpaceChar(ch = expr.charAt(pointer++)))
			;
		if (ch != null) {
			if (charset.indexOf(ch) != -1)
				return ch;
			pointer--;//retract pointer if ch is not in charset and returns null
		}
		return null;
	}

	protected boolean nextSequence(String sequence) {//returns true only if sequence is found at current position; in this case pointer is moved forward
		while ((pointer < expr.length()) && Character.isSpaceChar(expr.charAt(pointer)))
			pointer++;
		if (pointer + sequence.length() > expr.length())
			return false;
		for (int i = 0; i < sequence.length(); i++) {
			if (expr.charAt(pointer + i) != sequence.charAt(i))
				return false;
		}
		pointer += sequence.length();
		return true;
	}

	protected String getIdentifier() {//returns identifier, if present at current position
		Character ch = null;
		while ((pointer < expr.length()) && Character.isSpaceChar(ch = expr.charAt(pointer++)))
			;
		if (ch != null) {
			if (Character.isLetter(ch))	{
				StringBuilder sb = new StringBuilder().append(ch);
				while ((pointer < expr.length()) && (Character.isLetter(ch = expr.charAt(pointer)))) {
					sb.append(ch); pointer++;
				}
				return sb.toString();
			}
			pointer--;//retract pointer if ch is not a letter
		}
		return null;
	}

	protected boolean chars_available() {
		return pointer < expr.length();
	}
}//end of class StringHolder

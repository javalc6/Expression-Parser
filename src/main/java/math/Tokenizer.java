package math;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Tokenizer for the expression language.
 * Converts a raw string input into a stream of tokens using the unified Type enum.
 */
public class Tokenizer {
    private final String input;
    private int pos = 0;

    public Tokenizer(String input) {
        this.input = input;
    }

    /**
     * Scans the input string and returns a list of identified tokens.
     */
    public List<Token> tokenize() throws ParseException {
        List<Token> tokens = new ArrayList<>();
        
        while (pos < input.length()) {
            char ch = input.charAt(pos);

            // Skip whitespace
            if (Character.isWhitespace(ch)) {
                pos++;
                continue;
            }

            // Handle Numbers (literals)
            if (Character.isDigit(ch) || ch == '.') {
                tokens.add(readNumber());
            } 
            // Handle Identifiers and Keywords (and, or, true, false)
            else if (Character.isLetter(ch)) {
                tokens.add(readIdentifierOrKeyword());
            } 
            // Handle Operators and Structural Symbols (?, :, (, ), ==, !=, etc.)
            else {
                tokens.add(readOperatorOrSymbol());
            }
        }
        
        // Append EOF to signal the end of the stream to the parser
        tokens.add(new Token(Type.eof, "", pos));
        return tokens;
    }

    private Token readNumber() {
        int start = pos;
        boolean hasDot = false;
        while (pos < input.length()) {
            char ch = input.charAt(pos);
            if (ch == '.') {
                if (hasDot) break; // Only allow one decimal point
                hasDot = true;
            } else if (!Character.isDigit(ch)) {
                break;
            }
            pos++;
        }
        return new Token(Type.number, input.substring(start, pos), start);
    }

    private Token readIdentifierOrKeyword() {
        int start = pos;
        while (pos < input.length() && Character.isLetterOrDigit(input.charAt(pos))) {
            pos++;
        }
        String val = input.substring(start, pos);
        
        // Check for reserved keywords or boolean literals
        switch (val.toLowerCase()) {
            case "and":   return new Token(Type.and, val, start);
            case "or":    return new Token(Type.or, val, start);
            case "true":
            case "false": return new Token(Type.boolean_literal, val, start);
            default:      return new Token(Type.identifier, val, start);
        }
    }

    private Token readOperatorOrSymbol() throws ParseException {
        int start = pos;
        char ch = input.charAt(pos++);
        
        switch (ch) {
            case '+': return new Token(Type.add, "+", start);
            case '-': return new Token(Type.subtract, "-", start);
            case '*': return new Token(Type.multiply, "*", start);
            case '/': return new Token(Type.divide, "/", start);
            case '(': return new Token(Type.lparen, "(", start);
            case ')': return new Token(Type.rparen, ")", start);
            case '?': return new Token(Type.question, "?", start);
            case ':': return new Token(Type.colon, ":", start);
            case '!':
                if (match('=')) return new Token(Type.unequal, "!=", start);
                return new Token(Type.not, "!", start);
            case '=':
                if (match('=')) return new Token(Type.equal, "==", start);
                throw new ParseException("Expected '==' for equality comparison", start);
            case '<':
                if (match('=')) return new Token(Type.lte, "<=", start);
                return new Token(Type.lt, "<", start);
            case '>':
                if (match('=')) return new Token(Type.gte, ">=", start);
                return new Token(Type.gt, ">", start);
            default:
                throw new ParseException("Unexpected character: " + ch, start);
        }
    }

    /**
     * Helper to check the next character for multi-character operators.
     */
    private boolean match(char expected) {
        if (pos < input.length() && input.charAt(pos) == expected) {
            pos++;
            return true;
        }
        return false;
    }
}
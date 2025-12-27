package math;

public class Token {
    public final Type type;
    public final String value;
    public final int position;

    public Token(Type type, String value, int position) {
        this.type = type;
        this.value = value;
        this.position = position;
    }

    @Override
    public String toString() {
        return String.format("Token(%s, '%s', @%d)", type, value, position);
    }
}
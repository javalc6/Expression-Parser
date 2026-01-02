package math;

public enum Type {
    // Literals and Identifiers
    number,             // Numeric literal
    identifier,         // Constant (PI, E) or Variable or Function Name
    boolean_literal,    // true/false

    // Basic Arithmetic
    add,                // +
    subtract,           // -
    multiply,           // *
    divide,             // /
    minus,              // Unary minus

    // Logical Operators
    not,                // !
    or,                 // or
    and,                // and

    // Relational Operators
    equal,              // ==
    unequal,            // !=
    gte,                // >=
    lte,                // <=
    gt,                 // >
    lt,                 // <

    // Structural Symbols (Tokenizer specific)
    lparen,             // (
    rparen,             // )
    question,           // ?
    colon,              // :
    eof,                // End of file marker

    // Complex Nodes
    conditional_expression // Ternary ?:
}
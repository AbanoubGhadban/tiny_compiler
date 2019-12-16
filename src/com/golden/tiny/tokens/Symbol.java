package com.golden.tiny.tokens;

public class Symbol extends Token {
    public Symbol(String token, int line, int column, int index) {
        super(token, line, column, index);
    }

    public Symbol() {
        super();
    }

    public Symbol(int line, int column, int index) {
        super(line, column, index);
    }

    public Symbol(String token) {
        super(token);
    }
}

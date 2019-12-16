package com.golden.tiny.tokens;

public class Number extends Token {
    public Number(String token, int line, int column, int index) {
        super(token, line, column, index);
    }

    public Number(int line, int column, int index) {
        super(line, column, index);
    }

    public Number() {
        super();
    }

    public Number(String token) {
        super(token);
    }
}

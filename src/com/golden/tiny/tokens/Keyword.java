package com.golden.tiny.tokens;

public class Keyword extends Token {
    public Keyword(String token, int line, int column, int index) {
        super(token, line, column, index);
    }

    public Keyword(int line, int column, int index) {
        super(line, column, index);
    }

    public Keyword() {
        super();
    }

    public Keyword(String token) {
        super(token);
    }
}

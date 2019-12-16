package com.golden.tiny.tokens;

import com.golden.tiny.tokens.Token;

public class Identifier extends Token {
    public Identifier(String token, int line, int column, int index) {
        super(token, line, column, index);
    }

    public Identifier() {
        super();
    }

    public Identifier(int line, int column, int index) {
        super(line, column, index);
    }

    public Identifier(String token) {
        super(token);
    }
}

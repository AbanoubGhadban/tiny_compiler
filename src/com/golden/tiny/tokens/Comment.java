package com.golden.tiny.tokens;

import com.golden.tiny.tokens.Token;

public class Comment extends Token {
    public Comment(String token) {
        super(token);
    }

    public Comment(String token, int line, int column, int index) {
        super(token, line, column, index);
    }

    public Comment() {
        super();
    }

    public Comment(int line, int column, int index) {
        super(line, column, index);
    }
}

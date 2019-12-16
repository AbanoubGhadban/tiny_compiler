package com.golden.tiny.tokens;

import com.golden.tiny.statement_parts.IStatementPart;

public abstract class Token implements IStatementPart {
    public String getToken() {
        return token;
    }

    private String token;
    private int line, column, index;
    private boolean isAny;

    public void setToken(String token) {
        this.token = token;
    }

    public void setLine(int line) {
        this.line = line;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public Token(String token, int line, int column, int index) {
        this.token = token;
        this.line = line;
        this.column = column;
        this.index = index;
    }

    public Token(int line, int column, int index) {
        this.line = line;
        this.column = column;
        this.index = index;
    }

    public Token(String token) {
        this.token = token;
    }

    public Token() {
        isAny = true;
    }

    public int getLine() {
        return line;
    }

    public int getColumn() {
        return column;
    }

    public int getIndex() {
        return index;
    }

    public String getExpected() {
        if (isAny)
            return getClass().getSimpleName();
        return token;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Token))
            return false;
        return (this.getClass().equals(obj.getClass()) && (isAny || ((Token)obj).isAny | this.token.equals(((Token)obj).token)));
    }
}

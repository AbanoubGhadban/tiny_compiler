package com.golden.tiny;

import com.golden.tiny.tokens.*;
import com.golden.tiny.tokens.Number;

import java.util.LinkedList;
import java.util.List;

public class TinyScanner {
    static String[] operators = {":=", "+=", "-=", "*=", "/=", "=", "<", ">", "<=", ">=", ";", "+", "-", "*", "/", "(", ")"};
    static String[] keywords = {"if", "then", "write", "else", "read", "print", "loop", "do", "while", "exit", "break", "until", "repeat", "end"};

    private int i = 0, line = 1, column = 1;

    TinyScanner(String s) {
        this.s = s;
    }

    private String s;

    List<Token> scan() throws Exception {
        List<Token> tokens = new LinkedList<>();
        i = 0;
        Token tmpToken;
        skipSpaces();
        while (i < s.length()) {
            if ((tmpToken = checkComment()) != null) { }
            else if ((tmpToken = checkDouble()) != null) { }
            else if ((tmpToken = checkSymbol()) != null) { }
            else if ((tmpToken = checkKeyword()) != null) { }
            else if ((tmpToken = checkIdentifier()) != null) { }

            if (tmpToken == null) {
                throw new Exception("Invalid Token Found at line " + String.valueOf(line) + " and column " + String.valueOf(column));
            }
            tokens.add(tmpToken);
            skipSpaces();
        }
        return tokens;
    }

    private void skipSpaces() {
        while (i < s.length() && (Character.isWhitespace(s.charAt(i))) ) {
            if (s.charAt(i) == '\n') {
                ++line;
                column = 0;
            }
            ++i;
            ++column;
        }
    }

    private Comment checkComment() {
        Comment comment = new Comment(line, column, i);
        int state = 0;
        loop: while (i < s.length()) {
            switch (state) {
                case 0:
                    if (s.charAt(i) == '{') {
                        ++i;
                        ++column;
                        state = 1;
                    } else
                        break loop;
                    break;
                case 1:
                    if (s.charAt(i) == '}') {
                        ++i;
                        ++column;
                        break loop;
                    }
                    ++i;
                    ++column;
                    if (s.charAt(i) == '\n')
                        ++line;
            }
        }
        if (state == 0)
            return null;
        comment.setToken(s.substring(comment.getIndex(), i));
        return comment;
    }

    private int checkInt(String s, int i) {
        int state = 0, curI = i;
        while (curI < s.length()) {
            if (Character.isDigit(s.charAt(curI)))
                state = 1;
            else
                break;
            ++curI;
        }
        return state == 1? curI : -1;
    }

    private int checkSignedInt(String s, int i) {
        int state = 0, curI = i, tmp;
        while (curI < s.length()) {
            if (state == 0) {
                if (s.charAt(curI) == '+' || s.charAt(curI) == '-') {
                    state = 1;
                } else if ((tmp = checkInt(s, curI)) != -1) {
                    state = 2;
                    curI = tmp;
                    break;
                } else {
                    break;
                }
            } else {
                if ((tmp = checkInt(s, curI)) != -1) {
                    state = 2;
                    curI = tmp;
                }
                break;
            }
            ++curI;
        }
        return state == 2? curI : -1;
    }

    private Number checkDouble() {
        Number number = new Number(line, column, i);
        int state = 0, curI = i, tmp;
        loop: while (curI < s.length()) {
            switch (state) {
                case 0:
                    if ((tmp = checkSignedInt(s, curI)) != -1) {
                        curI = tmp;
                        state = 1;
                    } else if (s.charAt(curI) == '.' && (tmp = checkInt(s, curI+1)) != -1) {
                        state = 2;
                        curI = tmp;
                    } else
                        break loop;
                    break;
                case 1:
                    if (s.charAt(curI) == '.' && (tmp = checkInt(s, curI+1)) != -1) {
                        state = 2;
                        curI = tmp;
                    } else if ((s.charAt(curI) == 'E' || s.charAt(curI) == 'e') && (tmp = checkSignedInt(s, curI+1)) != -1) {
                        state = 3;
                        curI = tmp;
                        break loop;
                    } else
                        break loop;
                    break;
                case 2:
                    if ((s.charAt(curI) == 'E' || s.charAt(curI) == 'e') && (tmp = checkSignedInt(s, curI+1)) != -1) {
                        curI = tmp;
                        state = 3;
                    }
                    break loop;
            }
        }
        if (state == 0)
            return null;
        column += curI - i;
        i = curI;
        number.setToken(s.substring(number.getIndex(), i));
        return number;
    }

    private Keyword checkKeyword() {
        Keyword kw = new Keyword(line, column, i);
        int curI = i;
        while (curI < s.length() && Character.isAlphabetic(s.charAt(curI)))
            ++curI;
        String word = s.substring(i, curI);
        for (String keyword:keywords) {
            if (word.equals(keyword)) {
                kw.setToken(keyword);
                column += curI - i;
                i = curI;
                return kw;
            }
        }
        return null;
    }

    private Symbol checkSymbol() {
        Symbol sym = new Symbol(line, column, i);
        for (int j = 2; j >= 1; --j) {
            if ((i+j) > s.length())
                continue;
            String symbol = s.substring(i, i+j);
            for (String operator : operators) {
                if (symbol.equals(operator)) {
                    sym.setToken(operator);
                    i += operator.length();
                    return sym;
                }
            }
        }
        return null;
    }

    private Identifier checkIdentifier() {
        Identifier ident = new Identifier(line, column, i);
        int state = 0, curI = i;
        while (curI < s.length()) {
            if (state == 0) {
                if (Character.isAlphabetic(s.charAt(curI)) || s.charAt(curI) == '_')
                    state = 1;
                else
                    break;
            } else {
                if (!Character.isAlphabetic(s.charAt(curI)) && !Character.isDigit(s.charAt(curI)) && s.charAt(curI) != '_')
                    break;
            }
            ++curI;
        }
        if (state == 0)
            return null;
        column += curI - i;
        i = curI;
        ident.setToken(s.substring(ident.getIndex(), i));
        return ident;
    }
}

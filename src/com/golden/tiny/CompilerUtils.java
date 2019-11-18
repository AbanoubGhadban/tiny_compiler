package com.golden.tiny;

public class CompilerUtils {
    static String[] operators = {":=", "+=", "-=", "*=", "/=", "=", "<", ">", "<=", ">=", "+", "-", "*", "/", "(", ")"};
    static String[] keywords = {"if", "then", "write", "else", "read", "print", "loop", "do", "while", "exit", "break", "until", "repeat", "end"};

    public static int skipSpacesAndSemicolon(String s, int i) {
        while (i < s.length() && (Character.isWhitespace(s.charAt(i)) || s.charAt(i) == ';') )
            ++i;
        return i;
    }

    public static int checkComment(String s, int i) {
        int state = 0;
        loop: while (i < s.length()) {
            switch (state) {
                case 0:
                    if (s.charAt(i) == '{') {
                        ++i;
                        state = 1;
                    } else
                        break loop;
                    break;
                case 1:
                    if (s.charAt(i) == '}') {
                        ++i;
                        break loop;
                    }
                    ++i;
            }
        }
        return state == 0? -1 : i;
    }

    public static int checkInt(String s, int i) {
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

    public static int checkSignedInt(String s, int i) {
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

    public static int checkDouble(String s, int i) {
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
        return state == 0? -1 : curI;
    }

    public static int checkKeyword(String s, int i) {
        int curI = i;
        while (curI < s.length() && Character.isAlphabetic(s.charAt(curI)))
            ++curI;
        String word = s.substring(i, curI);
        for (String keyword:keywords) {
            if (word.equals(keyword))
                return curI;
        }
        return -1;
    }

    public static int checkSymbol(String s, int i) {
        for (int j = 2; j >= 1; --j) {
            if ((i+j) > s.length())
                continue;
            String symbol = s.substring(i, i+j);
            for (String operator : operators) {
                if (symbol.equals(operator))
                    return i + j;
            }
        }
        return -1;
    }

    public static int checkIdentifier(String s, int i) {
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
        return state == 0? -1 : curI;
    }
}

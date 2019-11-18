package com.golden.tiny;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {

    public static void main(String[] args) throws IOException {
        String filePath = "sample.txt";
        String s = Files.readString(Paths.get(filePath));

        int i = 0, tmp;
        i = CompilerUtils.skipSpacesAndSemicolon(s, i);
        while (i < s.length()) {
            if ((tmp = CompilerUtils.checkComment(s, i)) != -1) {
                System.out.println("Comment: \"" + s.substring(i+1, tmp-1).trim() + "\"");
            } else if ((tmp = CompilerUtils.checkDouble(s, i)) != -1) {
                System.out.println("Number: " + s.substring(i, tmp));
            } else if ((tmp = CompilerUtils.checkSymbol(s, i)) != -1) {
                System.out.println("Symbol: " + s.substring(i, tmp));
            } else if ((tmp = CompilerUtils.checkKeyword(s, i)) != -1) {
                System.out.println("Keyword: " + s.substring(i, tmp));
            } else if ((tmp = CompilerUtils.checkIdentifier(s, i)) != -1) {
                System.out.println("Identifier: " + s.substring(i, tmp));
            }

            if (tmp == -1) {
                System.out.println("Syntax Error at Character " + String.valueOf(i));
                break;
            }
            i = tmp;
            i = CompilerUtils.skipSpacesAndSemicolon(s, i);
        }
    }
}

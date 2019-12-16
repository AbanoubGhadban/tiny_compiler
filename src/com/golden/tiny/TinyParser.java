package com.golden.tiny;

import com.golden.tiny.exceptions.CompilerException;
import com.golden.tiny.statement_parts.*;
import com.golden.tiny.tokens.*;
import com.golden.tiny.tokens.Number;

import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

class TinyParser {
    private List<Token> tokens;
    private int i;
    List<Statement> statements;
    Stack<IComposedStatementPart> mainPartsStack;

    TinyParser(List<Token> tokens) {
        this.tokens = tokens;
        i = 0;
    }

    List<Statement> parseTokens() throws Exception {
        if (statements != null)
            return statements;

        i = 0;
        statements = new LinkedList<>();
        mainPartsStack = new Stack<>();
        statementSequence();
        return statements;
    }

    private void statementSequence() throws Exception {
        statement();
        while (i < tokens.size() && tokens.get(i).equals(new Symbol(";"))) {
            match(tokens.get(i));
            statement();
        }
    }

    private void match(Token... expectedTokens) throws Exception {
        if (i >= tokens.size())
            throwException(expectedTokens[0]);
        Token curToken = tokens.get(i);

        for (Token token : expectedTokens) {
            if (token.equals(curToken)) {
                if (!curToken.equals(new Symbol(";")))
                    mainPartsStack.peek().addPart(curToken);
                ++i;
                return;
            }
        }
        throwException(expectedTokens[0]);
    }

    private void factor() throws Exception {
        addMainPart(new Factor());
        Token token = tokens.get(i);
        if (token.equals(new Symbol("("))) {
            match(new Symbol("("));
            exp();
            match(new Symbol(")"));
        } else {
            match(new Number(), new Identifier());
        }
        mainPartsStack.pop();
    }

    private void exp() throws Exception {
        addMainPart(new Expression());
        simpleExpression();
        if (i < tokens.size()) {
            Token token = tokens.get(i);
            if (token.equals(new Symbol("=")) || token.equals(new Symbol(">")) || token.equals(new Symbol("<")) ||
                    token.equals(new Symbol("<=")) || token.equals(new Symbol(">=")) || token.equals(new Symbol("!="))) {
                match(token);

                if (i >= tokens.size())
                    throwException("Simple Expression");
                simpleExpression();
            }
        }
        mainPartsStack.pop();
    }

    private void simpleExpression() throws Exception {
        addMainPart(new SimpleExpression());
        term();
        while (i < tokens.size() && (tokens.get(i).equals(new Symbol("+")) || tokens.get(i).equals(new Symbol("-")))) {
            match(tokens.get(i));
            term();
        }
        mainPartsStack.pop();
    }

    private void term() throws Exception {
        addMainPart(new Term());
        factor();
        while (i < tokens.size() && (tokens.get(i).equals(new Symbol("*")) || tokens.get(i).equals(new Symbol("/")))) {
            match(tokens.get(i));
            factor();
        }
        mainPartsStack.pop();
    }

    private void statement() throws Exception {
        Token token = tokens.get(i);
        if (token.equals(new Keyword("if")))
            ifStmt();
        else if (token.equals(new Keyword("read")))
            readStmt();
        else if (token.equals(new Keyword("write")))
            writeStmt();
        else if (token.equals(new Keyword("repeat")))
            repeatStmt();
        else
            assignment();
    }

    private void assignment() throws Exception {
        addStatement(StatementType.ASSIGNMENT_STATEMENT);
        match(new Identifier());
        match(new Symbol(":="), new Symbol("'+="), new Symbol("-="), new Symbol("/="), new Symbol("*="));
        exp();
        mainPartsStack.pop();
    }

    private void repeatStmt() throws Exception {
        addStatement(StatementType.REPEAT_STATEMENT);
        match(new Keyword("repeat"));
        statementSequence();
        match(new Keyword("until"));
        exp();
        mainPartsStack.pop();
    }

    private void ifStmt() throws Exception {
        addStatement(StatementType.IF_STATEMENT);
        match(new Keyword("if"));
        match(new Symbol("("));
        exp();
        match(new Symbol(")"));
        statementSequence();
        if (i < tokens.size() && tokens.get(i).equals(new Keyword("else"))) {
            match(new Keyword("else"));
            statementSequence();
        }
        match(new Keyword("end"));
        mainPartsStack.pop();
    }

    private void readStmt() throws Exception {
        addStatement(StatementType.READ_STATEMENT);
        match(new Keyword("read"));
        match(new Identifier());
        mainPartsStack.pop();
    }

    private void writeStmt() throws Exception {
        addStatement(StatementType.WRITE_STATEMENT);
        match(new Keyword("write"));
        exp();
        mainPartsStack.pop();
    }

    private void addStatement(StatementType statementType) {
        Statement statement = new Statement(statementType);
        if (mainPartsStack.isEmpty()) {
            statements.add(statement);
        } else {
            mainPartsStack.peek().addPart(statement);
        }
        mainPartsStack.push(statement);
    }

    private void addMainPart(IComposedStatementPart mainPart) {
        mainPartsStack.peek().addPart(mainPart);
        mainPartsStack.push(mainPart);
    }

    private void throwException(Token expected) throws Exception {
        String msg;
        if (i < tokens.size()) {
            Token token = tokens.get(i);
            msg = "Error at line " + String.valueOf(token.getLine()) + ", column " + String.valueOf(token.getColumn()) + "\n";
            msg += "Expected '" + expected.getExpected() + "', found '" + token.getExpected() + "'";
        } else {
            msg = "Expected '" + expected.getExpected() + "' at end of file";
        }
        throw new CompilerException(msg);
    }

    private void throwException(String expected) throws Exception {
        String msg;
        if (i < tokens.size()) {
            Token token = tokens.get(i);
            msg = "Error at line " + String.valueOf(token.getLine()) + ", column " + String.valueOf(token.getColumn()) + "\n";
            msg += "Expected '" + expected + "', found '" + token.getExpected() + "'";
        } else {
            msg = "Expected '" + expected + "' at end of file";
        }
        throw new CompilerException(msg);
    }
}

package com.golden.tiny.statement_parts;

import com.golden.tiny.statement_parts.IComposedStatementPart;
import com.golden.tiny.statement_parts.IStatementPart;
import com.golden.tiny.statement_parts.StatementType;

import java.util.LinkedList;
import java.util.List;

public class Statement implements IComposedStatementPart {
    private List<IStatementPart> parts;
    private StatementType statementType;

    public Statement(StatementType statementType) {
        this.parts = new LinkedList<>();
        this.statementType = statementType;
    }

    @Override
    public List<IStatementPart> getParts() {
        return parts;
    }

    public StatementType getStatementType() {
        return statementType;
    }

    @Override
    public void addPart(IStatementPart part) {
        parts.add(part);
    }
}

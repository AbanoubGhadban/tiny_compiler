package com.golden.tiny.statement_parts;

import java.util.LinkedList;
import java.util.List;

public class SimpleExpression implements IComposedStatementPart {
    private List<IStatementPart> parts;

    public SimpleExpression() {
        parts = new LinkedList<>();
    }

    @Override
    public List<IStatementPart> getParts() {
        return parts;
    }

    @Override
    public void addPart(IStatementPart part) {
        parts.add(part);
    }
}
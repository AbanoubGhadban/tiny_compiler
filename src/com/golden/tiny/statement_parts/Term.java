package com.golden.tiny.statement_parts;

import com.golden.tiny.statement_parts.IComposedStatementPart;
import com.golden.tiny.statement_parts.IStatementPart;

import java.util.LinkedList;
import java.util.List;

public class Term implements IComposedStatementPart {
    private List<IStatementPart> parts;

    public Term() {
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

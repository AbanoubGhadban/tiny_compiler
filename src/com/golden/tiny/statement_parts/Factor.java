package com.golden.tiny.statement_parts;

import java.util.LinkedList;
import java.util.List;

public class Factor implements IComposedStatementPart {
    private List<IStatementPart> parts;

    public Factor() {
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

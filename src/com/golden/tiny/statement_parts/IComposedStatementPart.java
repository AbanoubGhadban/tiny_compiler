package com.golden.tiny.statement_parts;

import java.util.List;

public interface IComposedStatementPart extends IStatementPart {
    void addPart(IStatementPart part);
    List<IStatementPart> getParts();
}

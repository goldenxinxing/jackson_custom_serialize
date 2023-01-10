package org.example;

public abstract class ColumnType {
    protected abstract String getTypeName();
    public abstract Object encode(Object value, boolean rawResult);
    public abstract Object decode(Object value);
}


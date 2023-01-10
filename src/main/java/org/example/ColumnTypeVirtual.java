package org.example;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ColumnTypeVirtual extends ColumnType {
    public static final String TYPE_NAME = "VIRTUAL";

    private String path;

    private ColumnType type;

    public ColumnTypeVirtual(String path, ColumnType type) {
        this.path = path;
        this.type = type;
    }

    @Override
    protected String getTypeName() {
        return TYPE_NAME;
    }

    @Override
    public Object encode(Object value, boolean rawResult) {
        // TODO ②第二种实现方案是在此实现
        return value;
    }

    @Override
    public Object decode(Object value) {
        return value;
    }
}

package com.up.util.code;

/**
 * Created by jinhaishan on 17/9/27.
 */
public class FieldInfo {

    private String columnName;

    private String name;

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public FieldInfo(String columnName, String name) {
        this.columnName = columnName;
        this.name = name;
    }
}

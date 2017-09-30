package com.up.util.code;

import java.io.Serializable;
import java.util.List;

/**
 * @author jinhaishan
 * @create 17/9/11 18:53
 */
public class TableInfo implements Serializable{


    private static final long serialVersionUID = -9164411505554413184L;
    /**
     * 表名
     */
    private String tableNanme;

    /**
     * 表注释
     */
    private String tableComment;


    /**
     * 字段信息
     */
    private List<ColumnInfo> columnInfos;

    public String getTableNanme() {
        return tableNanme;
    }

    public void setTableNanme(String tableNanme) {
        this.tableNanme = tableNanme;
    }

    public List<ColumnInfo> getColumnInfos() {
        return columnInfos;
    }

    public void setColumnInfos(List<ColumnInfo> columnInfos) {
        this.columnInfos = columnInfos;
    }


    public String getTableComment() {
        return tableComment;
    }

    public void setTableComment(String tableComment) {
        this.tableComment = tableComment;
    }

    public TableInfo() {
    }

    public TableInfo(String tableNanme, String tableComment, List<ColumnInfo> columnInfos) {
        this.tableNanme = tableNanme;
        this.tableComment = tableComment;
        this.columnInfos = columnInfos;
    }

    @Override
    public String toString() {
        return "TableInfo{" +
                "tableNanme='" + tableNanme + '\'' +
                ", tableComment='" + tableComment + '\'' +
                ", columnInfos=" + columnInfos +
                '}';
    }
}

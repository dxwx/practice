package com.up.util.code;

/**
 * Created by jinhaishan on 17/9/27.
 */
public class MapperBuildUtil {

    public static StringBuilder getMapperContent(TableInfo tableInfo) {
        MapperInfo mapperInfo = new MapperInfo(tableInfo);
        StringBuilder header = mapperInfo.buildMapperHeader();
        StringBuilder mapperBegin = mapperInfo.buildMapperBegin();
        StringBuilder resultMap = mapperInfo.buildResultMap();
        StringBuilder sqls = mapperInfo.buildSqls();
        StringBuilder insert = mapperInfo.buildInsert();
        StringBuilder update = mapperInfo.buildUpdate();
        StringBuilder delete = mapperInfo.buildDelete();
        StringBuilder get = mapperInfo.buildGet();
        StringBuilder list = mapperInfo.buildList();
        StringBuilder totalCount = mapperInfo.buildTotalCount();
        StringBuilder pageWhere = mapperInfo.builPageWhere();
        StringBuilder tail = mapperInfo.builTail();
        return header.append(mapperBegin).append(resultMap).append(sqls).append(insert).append(update).append(delete).append(get).append(list).append(totalCount).append(pageWhere).append(tail);
    }
}

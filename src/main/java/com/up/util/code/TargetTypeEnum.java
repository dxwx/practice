package com.up.util.code;

/**
 * Created by jinhaishan on 17/9/26.
 * 生成的文件类型
 */
public enum TargetTypeEnum {
    ENTITY("", "Entity", "Entity.java", "entity"),

    DAO("I","Dao", "Dao.java", "dao"),

    MAPPER("","", "Mapper.xml", "mapper");


    /**
     * 类名前缀
     */
    private String classPreFix;


    /**
     * 类名后缀
     */
    private String classPostfix;

    /**
     * 文件名后缀
     */
    private String filePostfix;

    /**
     * 包名
     */
    private String packageName;

    public String getClassPreFix() {
        return classPreFix;
    }

    public String getClassPostfix() {
        return classPostfix;
    }

    public String getFilePostfix() {
        return filePostfix;
    }

    public String getPackageName() {
        return packageName;
    }

    TargetTypeEnum(String classPreFix, String classPostfix, String filePostfix, String packageName) {
        this.classPreFix = classPreFix;
        this.classPostfix = classPostfix;
        this.filePostfix = filePostfix;
        this.packageName = packageName;
    }
}

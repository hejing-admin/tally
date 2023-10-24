package com.hejing.tally.db;

/**
 * 表示收入或者支出具体类型的类
 */
public class TypeBean {
    int id;
    String typeName;  // 类型名称
    int imageId;  // 未被选中时的图片id
    int sImageId;  // 被选中后的图片id
    int kind;  // 收入为1, 支出为-1, 该元素表示该项目是收入还是支出

    public TypeBean() {

    }
    public TypeBean(int id, String typeName, int imageId, int sImageId, int kind) {
        this.id = id;
        this.typeName = typeName;
        this.imageId = imageId;
        this.sImageId = sImageId;
        this.kind = kind;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public int getsImageId() {
        return sImageId;
    }

    public void setsImageId(int sImageId) {
        this.sImageId = sImageId;
    }

    public int getKind() {
        return kind;
    }

    public void setKind(int kind) {
        this.kind = kind;
    }
}


























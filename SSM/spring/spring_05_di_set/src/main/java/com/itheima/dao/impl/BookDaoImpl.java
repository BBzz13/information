package com.itheima.dao.impl;

import com.itheima.dao.BookDao;

public class BookDaoImpl implements BookDao {

    private String databaseName;
    private int connectionNum;

    /**
     * setter注入需要提供要注入对象的set方法
     * @param connectionNum
     */
    public void setConnectionNum(int connectionNum) {
        this.connectionNum = connectionNum;
    }

    /**
     * setter注入需要提供要注入对象的set方法
     * @param databaseName
     */
    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    @Override
    public void save() {
        System.out.println("book dao save ..."+databaseName+","+connectionNum);
    }
}

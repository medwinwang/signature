package com.hyxf.fdd.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by admin on 2017/4/14.
 */
public class DynamicTables implements Serializable {

    private List<DynamicTable> dynamicTables;

    public List<DynamicTable> getDynamicTables() {
        return dynamicTables;
    }

    public void setDynamicTables(List<DynamicTable> dynamicTables) {
        this.dynamicTables = dynamicTables;
    }
}

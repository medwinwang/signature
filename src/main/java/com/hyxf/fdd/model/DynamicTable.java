package com.hyxf.fdd.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by admin on 2017/4/14.
 */
public class DynamicTable implements Serializable {

    private int pageBegin;

    private List<String> headers;

    private List<List<String>> datas;


    public int getPageBegin() {
        return pageBegin;
    }

    public void setPageBegin(int pageBegin) {
        this.pageBegin = pageBegin;
    }

    public List<String> getHeaders() {
        return headers;
    }

    public void setHeaders(List<String> headers) {
        this.headers = headers;
    }

    public List<List<String>> getDatas() {
        return datas;
    }

    public void setDatas(List<List<String>> datas) {
        this.datas = datas;
    }
}

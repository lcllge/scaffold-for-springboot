package com.example.demo.response;

import java.io.Serializable;
import java.util.List;

/**
 * @version V1.0.0
 * @ClassName: {@link ResultPage}
 * @Description: 后台返回分页DTO对象
 * @author: 兰州
 * @date: 2019/7/16 10:12
 * @Copyright:2019 All rights reserved.
 */
public class ResultPage<T> implements Serializable {

    private static final long serialVersionUID = 8029494783945793535L;
    private static final Integer SUCCESS = 200;

    private Integer count;

    private Integer code;

    private String msg;

    private List<T> data;

    public ResultPage() {
        this.msg = "success";
        this.code = SUCCESS;
    }

    public ResultPage(Integer count, List<T> data) {
        this.code = SUCCESS;
        this.msg = "success";
        this.count = count;
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        if (data == null || data.size() == 0) {
            this.code = 404;
            this.msg = "暂无数据";
        } else {
            this.count = data.size();
            this.data = data;
        }
    }
}

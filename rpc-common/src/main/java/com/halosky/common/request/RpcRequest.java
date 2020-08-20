package com.halosky.common.request;

import java.io.Serializable;

public class RpcRequest implements Serializable {


    private String className;

    private String methodName;

    private Object[] fields;

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Object[] getFields() {
        return fields;
    }

    public void setFields(Object[] fields) {
        this.fields = fields;
    }
}

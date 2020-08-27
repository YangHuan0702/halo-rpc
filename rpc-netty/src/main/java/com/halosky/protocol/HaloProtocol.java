package com.halosky.protocol;

import lombok.Data;

import java.io.Serializable;

@Data
public class HaloProtocol implements Serializable {

    private String className;

    private String methodName;

    private Class<?>[] args;

    private Object[] values;

}

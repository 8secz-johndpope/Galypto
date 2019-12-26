package com.example.cripto_photoaffix;

public class DataTransferer {
    private static final DataTransferer instance = new DataTransferer();
    private Object data;

    private DataTransferer() {
        data = null;
    }

    public static DataTransferer getInstance() {
        return instance;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object d) {
        data = d;
    }
}

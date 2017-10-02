package pl.yalgrin.gremphics.controller;

import java.util.HashMap;
import java.util.Map;

public class ControllerData<T> {
    private Map<String, T> data = new HashMap<>();

    public T getParameter(String name) {
        return data.get(name);
    }

    public void setParameter(String name, T param) {
        data.put(name, param);
    }

    public void setData(Map<String, T> data) {
        this.data = data;
    }

    public void putData(Map<String, T> data) {
        this.data.putAll(data);
    }
}

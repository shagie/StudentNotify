package net.shagie.student.couch;

import com.google.gson.annotations.SerializedName;

class RowWrapper<T> {
    @SerializedName("_id")
    private String id;
    private String key;
    private T value;

    public RowWrapper() {
    }

    public RowWrapper(String id, String key, T value) {
        this.id = id;
        this.key = key;
        this.value = value;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }
}

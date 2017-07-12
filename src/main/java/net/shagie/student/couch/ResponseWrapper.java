package net.shagie.student.couch;

import com.google.gson.annotations.SerializedName;

import java.util.List;

class ResponseWrapper<T> {
    @SerializedName("total_rows")
    private int totalRows;
    private int offset;
    private List<RowWrapper<T>> rows;

    public ResponseWrapper() {
    }

    public ResponseWrapper(int totalRows, int offset, List<RowWrapper<T>> rows) {
        this.totalRows = totalRows;
        this.offset = offset;
        this.rows = rows;
    }

    public int getTotalRows() {
        return totalRows;
    }

    public void setTotalRows(int totalRows) {
        this.totalRows = totalRows;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public List<RowWrapper<T>> getRows() {
        return rows;
    }

    public void setRows(List<RowWrapper<T>> rows) {
        this.rows = rows;
    }
}

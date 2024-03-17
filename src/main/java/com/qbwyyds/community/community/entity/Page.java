package com.qbwyyds.community.community.entity;

/**
 * 分装分页相关信息
 */
public class Page {
    //当前页的页码
    private int current = 1;
    //页面显示上线
    private int limit=10;
    //总的信息数，用于计算页面总数
    private int rows;
    //查询路径 用于分页服用
    private String path;

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        if (current >= 1){
            this.current = current;
        }
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        if (limit >= 1 && limit <= 100){
            this.limit = limit;
        }
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        if (rows >= 0){
            this.rows = rows;
        }
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    /**
     * 获取当前页的起始行
     * @return
     */
    public int getOffset(){
        return (current-1)*limit;
    }

    /**
     * 获取总页数
     * @return
     */
    public int getTotal(){
        if (rows%limit==0){
            return rows/limit;
        }else {
            return rows/limit+1;
        }
    }

    /**
     * 获取显示的起始和终止页码
     * @return
     */
    public int getFrom(){
        int from=current-2;
        return from < 1 ? 1:from;
    }

    public int getTo(){
        int to=current+2;
        int total=getTotal();
        return to > total ? total : to;
    }
    @Override
    public String toString() {
        return "Page{" +
                "current=" + current +
                ", limit=" + limit +
                ", rows=" + rows +
                ", path='" + path + '\'' +
                '}';
    }
}

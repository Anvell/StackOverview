package io.github.anvell.stackoverview.model;

public class Query {

    public String query;
    public boolean hasMore = false;
    public int page = 1;

    public Query(String query) {
        this.query = query;
    }

    public Query(String query, boolean hasMore, int page) {
        this.query = query;
        this.hasMore = hasMore;
        this.page = page;
    }
}
package com.newmanconnection.hello.db;

import com.newmanconnection.hello.Hello;
import org.servantscode.commons.db.EasyDB;
import org.servantscode.commons.search.InsertBuilder;
import org.servantscode.commons.search.QueryBuilder;
import org.servantscode.commons.search.UpdateBuilder;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class HelloDB extends EasyDB<Hello> {

    public HelloDB() {
        super(Hello.class, "name");
    }

    private QueryBuilder select() {
        return tables(selectAll());
    }

    private QueryBuilder tables(QueryBuilder selection) {
        return selection.from("hello");
    }

    public int getCount(String search) {
        return getCount(tables(count()).search(searchParser.parse(search)));
    }

    public List<Hello> getPage(String search, String sort, int start, int count) {
        return get(select().search(searchParser.parse(search)).page(sort, start, count));
    }

    public Hello get(int id) { return getOne(select().withId(id)); }

    public Hello create(Hello hello){
        InsertBuilder cmd = insertInto("hello").value("name", hello.getName());

        hello.setId(createAndReturnKey(cmd));
        return hello;
    }
    public Hello update(Hello hello){
        UpdateBuilder cmd = update("hello")
                .value("name", hello.getName())
                .withId(hello.getId());

        if(!update(cmd))
            throw new RuntimeException("Could not update hello with id: " + hello.getId());
        return hello;
    }

    public boolean delete(int id) {
        return delete(deleteFrom("hello").withId(id));
    }

    @Override
    protected Hello processRow(ResultSet rs) throws SQLException {
        Hello hello = new Hello();

        hello.setId(rs.getInt("id"));
        hello.setName(rs.getString("name"));
        return hello;
    }
}

package org.acme.vertx;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.sqlclient.Row;
import io.vertx.mutiny.sqlclient.RowSet;
import io.vertx.mutiny.sqlclient.Tuple;
import io.vertx.mutiny.mysqlclient.MySQLPool;


public class StorageObject {

    public Long id;
    public String name;
    public Long size;
    public String path;
    public static String TblName = "Objects";

    public StorageObject() {
    }

    public StorageObject(Long id, String name, Long size, String path) {
        this.id = id;
        this.name = name;
        this.size = size;
        this.path = path;
    }


    private static StorageObject from(Row row) {
        return new StorageObject(row.getLong("id"), row.getString("name"), row.getLong("size"), row.getString("path"));
    }

    public static Multi<StorageObject> findAll(MySQLPool client) {
        return client.query("SELECT * FROM " + StorageObject.TblName + " ORDER BY id ASC").execute().onItem()
                .transformToMulti(set -> Multi.createFrom().iterable(set)).onItem().transform(StorageObject::from);
    }

    public static Uni<StorageObject> findById(MySQLPool client, long id) {
        return client.preparedQuery("SELECT * FROM " + StorageObject.TblName + "  WHERE id = ?").execute(Tuple.of(id)).onItem()
                .transform(RowSet::iterator).onItem()
                .transform(iterator -> iterator.hasNext() ? from(iterator.next()) : null);
    }

    public Uni<Boolean> save(MySQLPool client) {
        return client.preparedQuery("INSERT into " + StorageObject.TblName + " (name, size, path) VALUES (?, ?, ?)")
                .execute(Tuple.of(name, size, path)).onItem()
                .transform(rs -> rs.rowCount() == 1);
    }

}

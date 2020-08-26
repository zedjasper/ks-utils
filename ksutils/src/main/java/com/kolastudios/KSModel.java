package com.kolastudios;

import com.orm.SugarRecord;

import java.util.List;

/**
 * An extension of SugarRecord. Adds the method findByField() that takes an arbitrary field and finds by value
 */
class KSModel extends SugarRecord {
    public KSModel(){}

    /**
     * Find a KSModel by field and value
     * @param type The type of object to return
     * @param field The field to search
     * @param value The value to search
     * @param <T> The expected return type
     * @return
     */
    public static <T> T findByField(Class<T> type, String field, String value) {
        List<T> list = find(type, field + "=?", new String[]{value}, null, null, "1");
        if (list.isEmpty()) return null;
        return list.get(0);
    }
}

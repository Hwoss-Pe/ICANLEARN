package com.utils;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

import java.lang.reflect.Array;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StringToArrayTypeHandler<T> extends BaseTypeHandler<T[]> {

    private final Class<T> type;

    public StringToArrayTypeHandler(Class<T> type) {
        if (type == null) {
            throw new IllegalArgumentException("必须设定类型");
        }
        this.type = type;
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, T[] parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, parameter[0].toString());
    }

    @Override
    public T[] getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return convertStringToArray(rs.getString(columnName));
    }

    @Override
    public T[] getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return convertStringToArray(rs.getString(columnIndex));
    }

    @Override
    public T[] getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return convertStringToArray(cs.getString(columnIndex));
    }

    private T[] convertStringToArray(String value) {
        // 在这里将字符串转换为数组
        T[] array = (T[]) Array.newInstance(String.class, 1);
        array[0] = (T) value;
        return array;
    }

}

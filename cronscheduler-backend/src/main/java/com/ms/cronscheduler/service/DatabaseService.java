package com.ms.cronscheduler.service;


import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DatabaseService {

    public List<List<Map<String, Object>>> fetchData(List<String> sqlQueries, String dbUrl, String dbUserName, String dbPassword) {
        List<List<Map<String, Object>>> resultList = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(dbUrl, dbUserName, dbPassword)) {
            for (String sqlQuery : sqlQueries) {
                List<Map<String, Object>> dataList = new ArrayList<>();
                try (PreparedStatement stmt = conn.prepareStatement(sqlQuery);
                     ResultSet rs = stmt.executeQuery()) {

                    ResultSetMetaData metaData = rs.getMetaData();
                    int columnCount = metaData.getColumnCount();

                    while (rs.next()) {
                        Map<String, Object> row = new HashMap<>();
                        for (int i = 1; i <= columnCount; i++) {
                            row.put(metaData.getColumnName(i), rs.getObject(i));
                        }
                        dataList.add(row);
                    }
                }
                resultList.add(dataList);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return resultList;
    }
}




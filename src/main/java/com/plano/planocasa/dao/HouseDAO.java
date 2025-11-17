package com.plano.planocasa.dao;

import com.plano.planocasa.DBUtil;
import com.plano.planocasa.model.House;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HouseDAO {

    public static Long save(House house) throws SQLException {
        String sql = "INSERT INTO HOUSES(NAME, ADDRESS) VALUES (?, ?)";
        try (Connection c = DBUtil.getConnection();
        PreparedStatement ps = c.prepareStatement(sql, new String[]{"ID"})) {
            ps.setString(1, house.getName());
            ps.setString(2, house.getAddress());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    long id = rs.getLong(1);
                    house.setId(id);
                    return id;
                }
            }
        }
        return null;
    }

    public static List<House> findAll() throws SQLException {
        List<House> list = new ArrayList<>();
        String sql = "SELECT ID, NAME, ADDRESS FROM HOUSES ORDER BY ID";
        try (Connection c = DBUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
           while (rs.next()) {
               House h = new House(rs.getLong("id"), rs.getString("name"), rs.getString("address"));
               list.add(h);
           }
        }
        return list;
    }
}

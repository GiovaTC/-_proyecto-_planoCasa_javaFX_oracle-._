package com.plano.planocasa;

import com.plano.planocasa.DBUtil;
import com.plano.planocasa.model.Room;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RoomDAO {

        public static void save(Room room) throws SQLException {
            String sql = "INSERT INTO ROOMS(HOUSE_ID, NAME, X, Y, WIDTH, HEIGHT, AREA) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            try (Connection c = DBUtil.getConnection();
            PreparedStatement ps = c.prepareStatement(sql, new String[]{"ID"})) {
                ps.setLong(1, room.getHouseId());
                ps.setString(2, room.getName());
                ps.setDouble(3, room.getX());
                ps.setDouble(4, room.getY());
                ps.setDouble(5, room.getWidth());
                ps.setDouble(6, room.getHeight());
                ps.setDouble(7, room.getArea());
                ps.executeUpdate();
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        room.setId(rs.getLong(1));
                    }
                }
            }
        }

        public static List<Room> findByHouseId(long houseId) throws SQLException {
            List<Room> list = new ArrayList<>();
            String sql = "SELECT ID, NAME, X, Y, WIDTH, HEIGHT, AREA FROM ROOMS WHERE HOUSE_ID = ? ORDER BY ID";
            try (Connection c = DBUtil.getConnection();
                 PreparedStatement ps = c.prepareStatement(sql)) {
                ps.setLong(1, houseId);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        Room r = new Room();
                        r.setId(rs.getLong("ID"));
                        r.setHouseId(houseId);
                        r.setName(rs.getString("NAME"));
                        r.setX(rs.getDouble("X"));
                        r.setY(rs.getDouble("Y"));
                        r.setWidth(rs.getDouble("WIDTH"));
                        r.setHeight(rs.getDouble("HEIGHT"));
                        r.setArea(rs.getDouble("AREA"));
                        list.add(r);
                    }
                }
            }
            return list;
        }
}

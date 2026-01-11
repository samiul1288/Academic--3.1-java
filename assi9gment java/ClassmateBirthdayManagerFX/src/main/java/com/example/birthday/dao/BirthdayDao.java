package com.example.birthday.dao;

import com.example.birthday.db.DB;
import com.example.birthday.model.Classmate;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BirthdayDao {

    public List<Classmate> getAll() throws SQLException {
        String sql = "SELECT id, name, dob, phone, notes FROM classmates ORDER BY id DESC";
        try (Connection con = DB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            List<Classmate> list = new ArrayList<>();
            while (rs.next()) {
                list.add(map(rs));
            }
            return list;
        }
    }

    public int add(Classmate c) throws SQLException {
        String sql = "INSERT INTO classmates(name, dob, phone, notes) VALUES(?,?,?,?)";
        try (Connection con = DB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, c.getName());
            ps.setDate(2, Date.valueOf(c.getDob()));
            ps.setString(3, c.getPhone());
            ps.setString(4, c.getNotes());
            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) return keys.getInt(1);
            }
            return 0;
        }
    }

    public void update(Classmate c) throws SQLException {
        String sql = "UPDATE classmates SET name=?, dob=?, phone=?, notes=? WHERE id=?";
        try (Connection con = DB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, c.getName());
            ps.setDate(2, Date.valueOf(c.getDob()));
            ps.setString(3, c.getPhone());
            ps.setString(4, c.getNotes());
            ps.setInt(5, c.getId());
            ps.executeUpdate();
        }
    }

    public void deleteById(int id) throws SQLException {
        String sql = "DELETE FROM classmates WHERE id=?";
        try (Connection con = DB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    public List<Classmate> searchByName(String keyword) throws SQLException {
        String sql = "SELECT id, name, dob, phone, notes FROM classmates WHERE name LIKE ? ORDER BY name ASC";
        try (Connection con = DB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, "%" + keyword + "%");
            try (ResultSet rs = ps.executeQuery()) {
                List<Classmate> list = new ArrayList<>();
                while (rs.next()) list.add(map(rs));
                return list;
            }
        }
    }

    public List<Classmate> searchByMonth(int month1to12) throws SQLException {
        String sql = "SELECT id, name, dob, phone, notes FROM classmates WHERE MONTH(dob)=? ORDER BY DAY(dob) ASC";
        try (Connection con = DB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, month1to12);
            try (ResultSet rs = ps.executeQuery()) {
                List<Classmate> list = new ArrayList<>();
                while (rs.next()) list.add(map(rs));
                return list;
            }
        }
    }

    public List<Classmate> birthdaysToday(LocalDate today) throws SQLException {
        String sql = "SELECT id, name, dob, phone, notes FROM classmates WHERE MONTH(dob)=? AND DAY(dob)=? ORDER BY name";
        try (Connection con = DB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, today.getMonthValue());
            ps.setInt(2, today.getDayOfMonth());
            try (ResultSet rs = ps.executeQuery()) {
                List<Classmate> list = new ArrayList<>();
                while (rs.next()) list.add(map(rs));
                return list;
            }
        }
    }

    private static Classmate map(ResultSet rs) throws SQLException {
        return new Classmate(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getDate("dob").toLocalDate(),
                rs.getString("phone"),
                rs.getString("notes")
        );
    }
}

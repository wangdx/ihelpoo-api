package com.ihelpoo.api.dao.impl;

import com.ihelpoo.api.dao.JdbcRecordSayDao;
import com.ihelpoo.api.model.RecordSay;
import com.ihelpoo.api.model.RecordSayList;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JdbcRecordSayDaoImpl implements JdbcRecordSayDao {
    private DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public RecordSayList getRecordSays(Integer uid) {
        RecordSayList recordSays = new RecordSayList();
        String sql = "SELECT sid, uid, content FROM i_record_say LIMIT 10";
        Connection conn = null;
        try {
            conn = dataSource.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            RecordSay recordSay = null;
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                recordSay = new RecordSay(
                        rs.getInt("sid"),
                        rs.getInt("uid"),
                        rs.getString("content")
                );
                recordSays.getList().add(recordSay);
            }
            rs.close();
            ps.close();
            return recordSays;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                }
            }
        }

    }

    @Override
    public RecordSay getRecordSay(Integer uid) {
        RecordSay recordSay = null;
        String sql = "SELECT sid, uid, content FROM i_record_say LIMIT 10";
        Connection conn = null;
        try {
            conn = dataSource.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                recordSay = new RecordSay(
                        rs.getInt("sid"),
                        rs.getInt("uid"),
                        rs.getString("content")
                );
            }
            rs.close();
            ps.close();
            return recordSay;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                }
            }
        }
    }
}

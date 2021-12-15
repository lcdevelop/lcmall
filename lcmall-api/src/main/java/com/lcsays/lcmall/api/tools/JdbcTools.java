package com.lcsays.lcmall.api.tools;

import com.lcsays.lcmall.api.utils.SensitiveInfoUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: lichuang
 * @Date: 21-9-3 15:10
 */

public class JdbcTools {

    public static void main(String[] args) throws Exception {
        if (args.length != 6) {
            System.out.println("args length != 6");
            return;
        }

        String host = args[0];
        String db = args[1];
        String user = args[2];
        String pass = args[3];
        String salt = args[4];
        String key = args[5];

        String connectionString = "jdbc:mysql://" + host + ":3306/" + db + "?useUnicode=true";
        System.out.println(connectionString);
        Class.forName("com.mysql.jdbc.Driver");
        Connection conn = DriverManager.getConnection(connectionString, user, pass);

//        updateUserTable(conn, salt, key);
//        updateWhiteListTable(conn, salt, key);

        int begin = 2740000;
        int finish = 2929845;
        int step = 10000;
        while (begin < finish) {
            updateWhiteListTable(conn, salt, key, begin, begin + step);
            begin += step;
        }

        conn.close();

    }

    private static void updateWhiteListTable(Connection conn, String salt, String key, int begin, int end) throws Exception {
        System.out.println(begin + " " + end);

        Map<Integer, String> id2phone = new HashMap<>();

        {
            String sql = "select id, phone_number from wx_marketing_whitelist where id between " + begin + " and " + end;
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int id = rs.getInt(1);
                String phoneNumber = rs.getString(2);
                id2phone.put(id, phoneNumber);
            }
            ps.close();
        }


        {
            String sql = "update wx_marketing_whitelist set user_phone=CASE id";
            for (Integer id : id2phone.keySet()) {
                String phoneNumber = id2phone.get(id);
                String safePhoneNumber;
                try {
                    safePhoneNumber = phoneNumber.substring(0, 3) + "****" + phoneNumber.substring(7, 11);
                } catch (Exception e) {
                    System.out.println(id + " " + phoneNumber);
                    throw e;
                }
                sql += " WHEN " + id + " THEN '" + safePhoneNumber + "' ";
            }

            sql += " END, ";
            sql += " user_phone_encrypt=CASE id";

            for (Integer id : id2phone.keySet()) {
                String phoneNumber = id2phone.get(id);
                String safePhoneNumber = phoneNumber.substring(0, 3) + "****" + phoneNumber.substring(7, 11);
                String userPhoneEncrypt = SensitiveInfoUtils.encrypt(phoneNumber, salt, key);
                sql += " WHEN " + id + " THEN '" + userPhoneEncrypt + "' ";
            }

            String idList = StringUtils.joinWith(",", id2phone.keySet());
            sql += " END WHERE id IN (" + idList.substring(1, idList.length()-1) + ")";

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.execute();
            ps.close();
        }
    }

    private static void updateUserTable(Connection conn, String salt, String key) throws Exception {

        Map<Integer, String> id2phone = new HashMap<>();

        {
            String sql = "select id, phone_number from wx_ma_user where phone_number is not null and phone_number!=''";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int id = rs.getInt(1);
                String phoneNumber = rs.getString(2);
                System.out.println(id + " " + phoneNumber);
                id2phone.put(id, phoneNumber);
            }
            ps.close();
        }

        {
            for (Integer id : id2phone.keySet()) {
                String phoneNumber = id2phone.get(id);
                String safePhoneNumber = phoneNumber.substring(0, 3) + "****" + phoneNumber.substring(7, 11);
                String userPhoneEncrypt = SensitiveInfoUtils.encrypt(phoneNumber, salt, key);
                String sql = "update wx_ma_user set user_phone='"
                        + safePhoneNumber + "', user_phone_encrypt='"
                        + userPhoneEncrypt + "' where id=" + id;
                System.out.println(sql);

                PreparedStatement ps = conn.prepareStatement(sql);
                ps.execute();
                ps.close();
            }
        }

    }
}

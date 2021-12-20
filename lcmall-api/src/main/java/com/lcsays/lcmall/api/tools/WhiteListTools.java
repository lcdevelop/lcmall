package com.lcsays.lcmall.api.tools;

import com.lcsays.lcmall.api.utils.SensitiveInfoUtils;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WhiteListTools {

    private void write2Mysql(Connection conn, String salt, String key, List<String> phoneNumbers, int batchNo) throws Exception {
        System.out.println("write batch " + phoneNumbers.size());

        String sql = "insert into wx_marketing_whitelist(batch_no, user_phone, user_phone_encrypt) VALUES";
        for (String phoneNumber: phoneNumbers) {
            String safePhoneNumber = phoneNumber.substring(0, 3) + "****" + phoneNumber.substring(7, 11);
            String userPhoneEncrypt = SensitiveInfoUtils.encrypt(phoneNumber, salt, key);
            sql += "(" + batchNo + ",'" + safePhoneNumber + "','" + userPhoneEncrypt + "'),";
        }

        sql = sql.substring(0, sql.length() - 1);

        System.out.println(sql);
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.execute();
        ps.close();
    }

    public void updateUserPhone(String[] args) throws Exception {
        if (args.length >= 7) {
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

            Map<Integer, String> id2phone = new HashMap<>();

            {
                String sql = "select id, phone_number from wx_marketing_whitelist where phone_number is not null and user_phone is null";
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
                    String sql = "update wx_marketing_whitelist set user_phone='"
                            + safePhoneNumber + "', user_phone_encrypt='"
                            + userPhoneEncrypt + "' where id=" + id;
                    System.out.println(sql);

                    PreparedStatement ps = conn.prepareStatement(sql);
                    ps.execute();
                    ps.close();
                }
            }



            conn.close();
        }
    }

    /**
     * 执行参数配置 host db user pass salt key whiteListFilePath batchNo
     * @param args
     * @throws Exception
     */
    public void loadWhiteList(String[] args) throws Exception {
        if (args.length == 8) {
            String host = args[0];
            String db = args[1];
            String user = args[2];
            String pass = args[3];
            String salt = args[4];
            String key = args[5];
            String whiteListFilePath = args[6];
            int batchNo = Integer.parseInt(args[7]);

            String connectionString = "jdbc:mysql://" + host + ":3306/" + db + "?useUnicode=true";
            System.out.println(connectionString);
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection(connectionString, user, pass);

            List<String> phoneNumbers = new ArrayList<>();
            int batch = 2;
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                    new FileInputStream(whiteListFilePath)))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    phoneNumbers.add(line);
                    if (phoneNumbers.size() >= batch) {
                        write2Mysql(conn, salt, key, phoneNumbers, batchNo);
                        phoneNumbers.clear();
                    }
                }
                if (phoneNumbers.size() > 0) {
                    write2Mysql(conn, salt, key, phoneNumbers, batchNo);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            conn.close();
        } else {
            System.out.println("args error");
        }
    }


    public static void main(String[] args) throws Exception {
        WhiteListTools tools = new WhiteListTools();
        // 批量从文件导入用户电话号码
//        tools.loadWhiteList(args);

        // 把userPhone为空的重新根据phoneNumber补充好
        tools.updateUserPhone(args);
    }
}

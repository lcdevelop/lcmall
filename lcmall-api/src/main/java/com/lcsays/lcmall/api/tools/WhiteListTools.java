package com.lcsays.lcmall.api.tools;

import com.lcsays.lcmall.api.utils.SensitiveInfoUtils;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

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
        tools.loadWhiteList(args);
    }
}

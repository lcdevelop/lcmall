package com.lcsays.gg.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: lichuang
 * @Date: 21-9-3 15:10
 */

public class TmpJdbcUtils {
    private static SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
    private static SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm:ss");
    private static Map<Integer, String> catMap = new HashMap<>();
    static {
        catMap.put(1, "大数据");
        catMap.put(2, "全栈技术");
        catMap.put(3, "数学知识");
        catMap.put(4, "职场人生");
        catMap.put(5, "生活轶事");
    }

    private static void writeBlog(int id, String title, String body, Timestamp ts, String image, int subjectId) throws IOException {
        File filePath = new File("D:\\Developer\\lcsays-hugo\\content\\tutorial\\tmp/" + id + ".md");
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(filePath));

        bufferedWriter.write("---");
        bufferedWriter.newLine();

        bufferedWriter.write("title: \"" + title + "\"");
        bufferedWriter.newLine();

        Date createDate = new Date(ts.getTime());
        bufferedWriter.write("date: " + sdf1.format(createDate) + "T" + sdf2.format(createDate) + "+08:00");
        bufferedWriter.newLine();

        if (title.split(" ").length > 1) {
            String tag = title.split(" ")[0];
            tag = tag.replaceAll("\\(Full", "");
            bufferedWriter.write("Tags: [\"" + tag + "\"]");
            bufferedWriter.newLine();
        }

        String subject = catMap.get(subjectId);
        bufferedWriter.write("Categories: [\"" + subject + "\"]");
        bufferedWriter.newLine();

        bufferedWriter.write("draft: false");
        bufferedWriter.newLine();

        bufferedWriter.write("---");
        bufferedWriter.newLine();

        String content = "![](" + image + ")";

        content += body
            .replaceAll("\r\n\r\n", "\r\n")
            .replaceAll("https://github.com/warmheartli", "https://github.com/lcdevelop")
            .replaceAll("SharEDITor", "lcsays")
            .replaceAll("bloglistbytag/\\?tagname=", "tags/")
            .replaceAll("blogshow/\\?blogId=", "tutorial/")
            .replaceAll("blogshow\\?blogId=", "tutorial/")
            .replaceAll("www.shareditor.com", "www.lcsays.com")
            .replaceAll("http://www\\.lcsays\\.com", "https://www.lcsays.com")
            .replaceAll("lcsays\\.com/blogshow/", "lcsays\\.com/tutorial/");
        bufferedWriter.write(content);

        bufferedWriter.close();
    }

    public static void mai2n(String[] args) {
        String s = "http://www.lcsays.com";
        System.out.println(s.replaceAll("http://www\\.lcsays\\.com", "https://www.lcsays.com"));
    }

    public static void main(String[] args) throws ClassNotFoundException, SQLException, IOException {
        String connectionString = "jdbc:mysql://rm-2zesy2n7p6j20x2udo.mysql.rds.aliyuncs.com:3306/db_shareditor?user=lichuang&password=fucking@2Bh8j1@m@uG&useUnicode=true&";
        Class.forName("com.mysql.jdbc.Driver");
        Connection conn = DriverManager.getConnection(connectionString);



        String sql = "select id, title, body, create_time, image, subject_id from web_blogpost";
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            int id = rs.getInt(1);
            String title = rs.getString(2);
            String body = rs.getString(3);
            Timestamp ts = rs.getTimestamp(4);
            String image = rs.getString(5);
            int subjectId = rs.getInt(6);
            writeBlog(id, title, body, ts, image, subjectId);
        }

        ps.close();
        conn.close();
    }
}

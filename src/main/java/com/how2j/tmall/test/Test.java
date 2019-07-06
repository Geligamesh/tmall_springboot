package com.how2j.tmall.test;

import org.hibernate.result.Output;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class Test {

    // public static final String URL = "jdbc:mysql://localhost:3306/tmall_springboot";
    // public static final String USER = "root";
    // public static final String PASSWORD = "root";

    public static void main(String[] args) throws Exception {

        DataOutputStream output = new DataOutputStream(new FileOutputStream("t.dat"));
        output.writeChars("AB");
        output.writeInt(1234);
        output.writeFloat(123.4f);
        output.close();
        // //1.加载驱动程序
        // Class.forName("com.mysql.jdbc.Driver");
        // //2. 获得数据库连接
        // Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
        // //3.操作数据库，实现增删改查
        // Statement stmt = conn.createStatement();
        // ResultSet rs = stmt.executeQuery("select id,orderCode,address from order_ where id = 178");
        // System.out.println();
    }
}


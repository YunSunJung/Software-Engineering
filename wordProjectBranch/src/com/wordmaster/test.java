package com.wordmaster;
import java.sql.Connection;
import model.DBConfig;

public class test {

	public static void main(String[] args) {
		 try {
		        Connection conn = DBConfig.getConnection();
		        System.out.println("Oracle 연결 성공!");
		    } catch (Exception e) {
		        e.printStackTrace();
		    }

	}

}

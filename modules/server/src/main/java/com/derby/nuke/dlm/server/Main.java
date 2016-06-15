package com.derby.nuke.dlm.server;

import org.springframework.context.support.FileSystemXmlApplicationContext;

public class Main {
	
	public static void main(String[] args) {
		FileSystemXmlApplicationContext context = new FileSystemXmlApplicationContext("classpath:spring.service.xml");
	    System.out.println("server is running……");
	}

}

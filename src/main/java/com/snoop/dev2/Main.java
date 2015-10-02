package com.snoop.dev2;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.concurrent.ExecutionException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

public class Main {

	public static void main(String[] args) {

		 try {

			File file = new File("/home/imatellan/git/NexusFidelizacion/pom.xml");
			JAXBContext jaxbContext = JAXBContext.newInstance(Project.class);

			String newBranch = executeCommand("git rev-parse --abbrev-ref HEAD");
			System.out.println(newBranch);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			Project pom = (Project) jaxbUnmarshaller.unmarshal(file);
			System.out.println(pom.version);
			
		  } catch (JAXBException e) {
			e.printStackTrace();
		  }

		}
	
	public static String executeCommand(String command) {
	    StringBuffer output = new StringBuffer();
	    Process p;
	    try {
	        p = Runtime.getRuntime().exec(command);
	        p.waitFor();
	        BufferedReader reader = 
	                        new BufferedReader(new InputStreamReader(p.getInputStream()));
	        String line = "";           
	        while ((line = reader.readLine())!= null) {
	            output.append(line + "\n");
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return output.toString();
	}
}

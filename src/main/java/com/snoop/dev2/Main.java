package com.snoop.dev2;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

public class Main {

	public static void main(String[] args) {

		 try {

			File file = new File("/home/imatellan/git/NexusFidelizacion/pom.xml");
			JAXBContext jaxbContext = JAXBContext.newInstance(Project.class);

			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			Project pom = (Project) jaxbUnmarshaller.unmarshal(file);
			System.out.println(pom.getVersion());

		  } catch (JAXBException e) {
			e.printStackTrace();
		  }

		}
	
}

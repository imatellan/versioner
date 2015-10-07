package com.snoop.dev2;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

public class WorkerPom {
	
	private String newVersion;
	private String path;
	private File inFile;
	private File outFile;
	private JAXBContext context;
	private POM pom;


	public WorkerPom(String newVersion, String path) {
		
		this.newVersion = newVersion;
		this.path = path;	
		this.inFile = new File(this.path + "pom.xml");
		this.outFile = new File(this.path + "0pom.xml");
		this.pom = null;
		try {
			this.context = JAXBContext.newInstance(POM.class);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		//Asigna un objeto POM a "this.pom"
		parsePOMtoObject();
//		ACA EMPEZE A TOCAR
		Main.primaryVersion;
		if(this.validatePom()){
			modifyPom(this.pom.version);
			backUpFile();
		}
		// Aca tengo que llamar a modificar los distintos poms
		
		if (this.pom.getModules().getModule() != null) {
			for (String module : this.pom.getModules().getModule()) {
				if(!module.contains("/")){
					new WorkerPom(newVersion, this.path + module +"/");
				}
			}
		}
	}
	
	private boolean validatePom() {
		if (this.pom.getVersion().compareTo(this.newVersion) < 0) {
			return true;
		} else {
			System.out.println("Revise la version de "+this.pom.getModules().getModule()+". \n Version actual: " + pom.version+"\n Version pretendida: " + this.newVersion);
			return false;
		}
	}
	
	//Esta funcion crea y asigna un objeto "POM.java" en la variable global "this.pom"
	//Éste objeto POM se obtiene de parsear el archivo XML ubicado en "this.inFile"
	public void parsePOMtoObject() {
		try {
			context = JAXBContext.newInstance(POM.class);
			System.out.println(inFile.getAbsolutePath());
			Unmarshaller jaxbUnmarshaller = context.createUnmarshaller();
			this.pom = (POM) jaxbUnmarshaller.unmarshal(inFile);
			if (this.pom == null)
				System.out.println("el pom es nulo");
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}

	public void modifyPom(String oldPomVersion) {

		FileWriter fw = null;
		BufferedWriter bw = null;
		BufferedReader br2 = null;
		FileReader fr = null;
		String cadenaInArchivoXML = "";
		try {
			// Abro el archivo y comienzo a leer, dejo todo en la variable
			// cadena "<<<<" es una serie de caracteres para poder parsear
			// y comenzar a escribir nuevamente el archivo
			fr = new FileReader(inFile);
			br2 = new BufferedReader(fr);
			String cadena = br2.readLine();
			while (cadena != null) {
				cadenaInArchivoXML += cadena + "<<<<";
				cadena = br2.readLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				br2.close();
				fr.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		;
		// reemplazo la version que tengo al momento de ejecutar el ejecutable
		cadenaInArchivoXML = cadenaInArchivoXML.replace(oldPomVersion, newVersion);
		String[] cadena = cadenaInArchivoXML.split("<<<<");
		try {
			fw = new FileWriter(outFile);
			bw = new BufferedWriter(fw);
			for (String line : cadena) {
				bw.write(line);
				bw.newLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				bw.close();
				fw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
//		 Marshaller marshaller;
//		 pom.setVersion(newVersion);
//		 FileWriter writer = null;
//		 try {
//		 writer = new FileWriter(outFile);
//		 } catch (IOException e) {
//		 e.printStackTrace();
//		 }
//		 try {
//		 marshaller = context.createMarshaller();
//		 marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
//		 marshaller.marshal(pom, writer);
//		 marshaller.marshal(pom, System.out);
//		 } catch (JAXBException e1) {
//		 e1.printStackTrace();
//		 }
	}
	
	private void backUpFile() {
		Calendar fecha = Calendar.getInstance();
		int anio = fecha.get(Calendar.YEAR);
		int mes = fecha.get(Calendar.MONTH) + 1;
		int dia = fecha.get(Calendar.DAY_OF_MONTH);
		int hora = fecha.get(Calendar.HOUR_OF_DAY);
		int minuto = fecha.get(Calendar.MINUTE);
		int segundo = fecha.get(Calendar.SECOND);
		File backupFile = new File(path + Integer.toString(anio) + "-" + Integer.toString(mes) + "-"
				+ Integer.toString(dia) + " " + Integer.toString(hora) + ":" + Integer.toString(minuto) + ":"
				+ Integer.toString(segundo) + " pom.xml");
		inFile.renameTo(backupFile);
		outFile.renameTo(inFile);
	}
}



	 
	
	 

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
	private String nuevaVersion;
	private String path;
	private File inFIle;
	private File outFile;
	private String newBranch;
	private JAXBContext context;
	private Project pom;

	public WorkerPom(String newVersion, String newBran, String path) {
		this.nuevaVersion = newVersion;
		this.path = path;
		this.newBranch = newBran;
		this.inFIle = new File(this.path + "pom.xml");
		this.outFile = new File(this.path + "0pom.xml");
		this.pom = null;
		try {
			this.context = JAXBContext.newInstance(Project.class);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		this.workPom();
	}

	private void intercambiarNombre() {
		Calendar fecha = Calendar.getInstance();
		int anio = fecha.get(Calendar.YEAR);
		int mes = fecha.get(Calendar.MONTH) + 1;
		int dia = fecha.get(Calendar.DAY_OF_MONTH);
		int hora = fecha.get(Calendar.HOUR_OF_DAY);
		int minuto = fecha.get(Calendar.MINUTE);
		int segundo = fecha.get(Calendar.SECOND);
		File backupFile = new File(path + Integer.toString(anio) + "-" + Integer.toString(mes) + "-"
				+ Integer.toString(dia) + " " + Integer.toString(hora) + "-" + Integer.toString(minuto) + "-"
				+ Integer.toString(segundo) + "pom.xml");
		inFIle.renameTo(backupFile);
	}

	public void leerPOM() {
		try {
			context = JAXBContext.newInstance(Project.class);
			Unmarshaller jaxbUnmarshaller = context.createUnmarshaller();
			// ACA EMPIEZO A LEER EL XML Y LO PASO AL OBJETO
			this.pom = (Project) jaxbUnmarshaller.unmarshal(inFIle);
			if (this.pom == null)
				System.out.println("el pom es nulo");
			// Aca tengo que llamar a modificar los distintos poms
			
			if(pom.getModules().getModule()!= null){
				for (String hola : pom.getModules().getModule()) {
					new WorkerPom(this.nuevaVersion, newBranch, this.path+hola+"/" );
				}
			}

		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}

	public void modificarPom(String oldPomVersion) {
		FileWriter fw = null;
		BufferedWriter bw = null;
		BufferedReader br2 = null;
		FileReader fr = null;
		String cadenaInArchivoXML = "";
		try {
			// Abro el archivo y comienzo a leer, dejo todo en la variable
			// cadena "<<<<" es una serie de caracteres para poder parsear
			// y comenzar a escribir nuevamente el archivo
			fr = new FileReader(inFIle);
			br2 = new BufferedReader(fr);
			String cadena = br2.readLine();
			while (cadena != null) {
				cadenaInArchivoXML += cadena;
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
		cadenaInArchivoXML = cadenaInArchivoXML.replace(oldPomVersion, nuevaVersion);
		try {
			fw = new FileWriter(outFile);
			bw = new BufferedWriter(fw);
			bw.write(cadenaInArchivoXML);
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
	}

	private void workPom() {
		leerPOM();
		if (this.pom.getVersion().compareTo(this.nuevaVersion) < 0) {
			nuevaVersion += this.newBranch.substring(0, this.newBranch.length() - 2) + "-SNAPSHOT";
			this.pom.version = this.pom.version.substring(0, this.pom.version.length());
			modificarPom(this.pom.version);
			intercambiarNombre();
		} else {
			System.out.println("Revise la version. La version actual es: " + pom.version);
		}
	}

	// private void escribirPOM() {
	// Marshaller marshaller;
	// FileWriter writer = null;
	// try {
	// writer = new FileWriter(outFile);
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// try {
	// marshaller = context.createMarshaller();
	// marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
	// marshaller.marshal(pom, writer);
	// } catch (JAXBException e1) {
	// e1.printStackTrace();
	// }
	// }
}
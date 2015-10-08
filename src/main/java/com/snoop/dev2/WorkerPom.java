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
	private POM pom;
	private String path;
	// private String newBranch;
	private String newVersion;
	private File inFile;
	private File outFile;
	private JAXBContext context;

	public WorkerPom(String path) {
		this.path = path;
		this.newVersion = newVersion;
		this.pom = null;
		// this.newBranch = null;
		this.inFile = new File(this.path + "pom.xml");
		this.outFile = new File(this.path + "0pom.xml");
		try {
			this.context = JAXBContext.newInstance(POM.class);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}

	private WorkerPom(String path, String newVersion) {
		this.path = path;
		this.inFile = new File(this.path + "pom.xml");
		this.outFile = new File(this.path + "0pom.xml");
		this.newVersion = newVersion;
		try {
			this.context = JAXBContext.newInstance(POM.class);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		this.pomToObject();
		workPom();
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

	// se supone que desde aca mando a trabajar al pom
	public void workPom() {
		// if (this.validatePom()) {
		modifyPom(this.pom.version);
		backUpFile();
		// }

		// Aca tengo que llamar a modificar los distintos poms
		if (this.pom.getModules().getModule() != null) {
			for (String module : this.pom.getModules().getModule()) {
				if (!module.contains("/")) {
					System.out.println(this.path + module + "/");
					new WorkerPom(this.path + module + "/",newVersion);
				}
			}
		}
	}

	// private boolean validatePom() {
	// if (this.pom.getVersion().compareTo(this.newVersion) < 0) {
	// return true;
	// } else {
	// System.out.println("Revise la version de " +
	// this.pom.getModules().getModule() + ". \n Version actual: "
	// + pom.version + "\n Version pretendida: " + this.newVersion);
	// return false;
	// }
	// }

	// PARSEA EL XML AL OBJETO POM
	public void pomToObject() {
		try {
			Unmarshaller unmarshaller = this.context.createUnmarshaller();
			this.pom = (POM) unmarshaller.unmarshal(this.inFile);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}

	// EMPIEZAN LOS GETTERS Y SETTERS
	public POM getPom() {
		return pom;
	}

	public File getInFile() {
		return inFile;
	}

	public String getNewVersion() {
		return newVersion;
	}

	public File getOutFile() {
		return outFile;
	}

	public String getPath() {
		return path;
	}

	public void setContext(JAXBContext context) {
		this.context = context;
	}

	public void setNewVersion(String newVersion) {
		this.newVersion = newVersion;
	}

	public void setPom(POM pom) {
		this.pom = pom;
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
	}

}

/**
 * 
 * //Asigna un objeto POM a "this.pom" parsePOMtoObject();
 * if(this.validatePom()){ modifyPom(this.pom.version); backUpFile(); } // Aca
 * tengo que llamar a modificar los distintos poms
 * 
 * if (this.pom.getModules().getModule() != null) { for (String module :
 * this.pom.getModules().getModule()) { if(!module.contains("/")){ new
 * WorkerPom(newVersion, this.path + module +"/"); } } }
 */

// Marshaller marshaller;
// pom.setVersion(newVersion);
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
// marshaller.marshal(pom, System.out);
// } catch (JAXBException e1) {
// e1.printStackTrace();
// }

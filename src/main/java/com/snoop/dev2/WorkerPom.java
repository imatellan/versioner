package com.snoop.dev2;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;

public class WorkerPom {
	private String nuevaVersion;
	private String path;
	private File inFIle;
	private File outFile;
	private String newBranch;

	public WorkerPom(String newVersion, String newBran, String path) {
		this.nuevaVersion = newVersion;
		this.path = path;
		this.newBranch = newBran;
		this.inFIle = new File(this.path + "pom.xml");
		this.outFile = new File(this.path + "0pom.xml");
		// Llamo a workPom
		this.workPom();
	}

	private void workPom() {

		intercambiarNombre();
	}

	private void intercambiarNombre() {
		Calendar fecha = Calendar.getInstance();
		int anio = fecha.get(Calendar.YEAR);
		int mes = fecha.get(Calendar.MONTH) + 1;
		int dia = fecha.get(Calendar.DAY_OF_MONTH);
		int hora = fecha.get(Calendar.HOUR_OF_DAY);
		int minuto = fecha.get(Calendar.MINUTE);
		int segundo = fecha.get(Calendar.SECOND);

		File auxiliar = new File(path + "pom.xml");
		File backupFile = new File(path + Integer.toString(anio) + "-" + Integer.toString(mes) + "-"
				+ Integer.toString(dia) + " " + Integer.toString(hora) + "-" + Integer.toString(minuto) + "-"
				+ Integer.toString(segundo) + "pom.xml");
		System.out.println(backupFile.getAbsolutePath());
		inFIle.renameTo(backupFile);
		outFile.renameTo(auxiliar);
	}
	
	public void modificarPom(String oldPomVersion) {
		String cadenaInArchivoXML = "";
		nuevaVersion += "." + newBranch + "-SNAPSHOT";
		try {
			FileReader fr = new FileReader(inFIle);
			BufferedReader br2 = new BufferedReader(fr);
			String cadena = br2.readLine();
			while (cadena != null) {
				cadenaInArchivoXML += cadena + "<<<<";
				cadena = br2.readLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		cadenaInArchivoXML = cadenaInArchivoXML.replace(oldPomVersion, nuevaVersion);
		String[] arrayCadena = cadenaInArchivoXML.split("<<<<");

		FileWriter fw = null;
		BufferedWriter bw = null;
		try {
			outFile = new File(this.path + "0pom.xml");
			fw = new FileWriter(outFile);
			bw = new BufferedWriter(fw);
			for (String cadenaAuxiliar : arrayCadena) {
				bw.write(cadenaAuxiliar);
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

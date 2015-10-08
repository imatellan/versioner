package com.snoop.dev2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.sun.xml.internal.bind.v2.schemagen.xmlschema.List;

public class Main {

	private static String newVersion;
	private static String newBranch;
	private static String path;
	private static POM pom;
	private static WorkerPom workerPom;
	public static String primaryVersion = null;

	// METODO PRINCIPAL
	public static void main(String[] args) {

		// Pido datos
		getNewVersion();
		getNewBranch();
		// Pregunto si tiene argumentos. En caso de que no, se pide el path por
		if (args.length > 0) {
			Main.path = args[0];
			modifyPath();
		} else {
			getPath();
		}
		// Execute the comand to create the new Branch
		executeCommand("git -C " + path + " checkout -b " + newBranch);
		setPrimaryVersion();
		// PRIMARYVERSION the last character is '.'
		newVersion = primaryVersion + newVersion + "." + newBranch + "-SNAPSHOT";
		System.out.println(newVersion);
		Main.workerPom.setNewVersion(Main.newVersion);
		//Work with pom.xml		
		Main.workerPom.workPom();
	}

	// Este metodo ejecuta comandos en consola.
	public static String executeCommand(String command) {
		StringBuffer output = new StringBuffer();
		Process p;
		try {
			// Ejecuta el comando
			p = Runtime.getRuntime().exec(command);
			p.waitFor();
			BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String line = "";
			while ((line = reader.readLine()) != null) {
				output.append(line + "\n");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return output.toString();
	}

	// pido y debería validar ciertas cosas del nuevo branch
	public static void getNewBranch() {
		Matcher m;
		do {
			System.out.println(
					"Ingrese el nuevo branch a crear. (Este nombre solo acepta Alphanumerics y _ [underscore]): \n");
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			try {
				Main.newBranch = br.readLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
			Pattern r = Pattern.compile("^[a-zA-Z0-9_]*$");
			m = r.matcher(Main.newBranch);
			// pregunto si al splitear con . tengo menos de dos campos.
		} while (!m.matches() || Main.newBranch.isEmpty());
	}

	public static void getNewVersion() {
		Matcher m;
		do {
			System.out.println("Ingrese la nueva Version que se creará. (Solo Números): \n");
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			try {
				Main.newVersion = br.readLine();
			} catch (IOException e) {

			}
			Pattern r = Pattern.compile("\\d+");
			m = r.matcher(Main.newVersion);
		} while (!m.matches());
	}

	public static void getPath() {
		System.out.println("Ingrese el path en donde ejecutar este proceso\n");
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		try {
			Main.path = br.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		modifyPath();
	}

	public static void modifyPath() {
		if (Main.path.charAt(Main.path.length() - 1) != '/') {
			Main.path += "/";
		}
	}

	// GETTERS AND SETTER OF primaryVersion
	public static String getPrimaryVersion() {
		return Main.primaryVersion;
	}

	private static void setPrimaryVersion() {
		Main.workerPom = new WorkerPom(path);
		// convierto el xml en un objeto pom
		workerPom.pomToObject();
		Main.primaryVersion = "";
		String cadena = workerPom.getPom().getVersion();
		String[] parseado = cadena.split("\\.");
		int i;
		// LLEGO AL ANTEULTIMO ELEMENTO DEL ARRAY
		for (i = 0; i < parseado.length - 1; i++) {
			Main.primaryVersion += parseado[i] + ".";
		}
		// SPLITEO EL ULTIMO ELEMENTO DEL ARRAY
		parseado = parseado[i].split("-");
		try {
			Integer.parseInt(parseado[0]);
			Main.primaryVersion += parseado[0] + ".";
		} catch (Exception e) {
		}
		// if (Main.primaryVersion.charAt(Main.primaryVersion.length() - 1) !=
		// '.') {
		// Main.primaryVersion += '.';
		// }
	}

	// public void commitBranchToGit() {
	//
	// }
}

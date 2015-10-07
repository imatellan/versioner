package com.snoop.dev2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

	private static String newVersion;
	private static String newBranch;
	private static String path;
	public static String primaryVersion = null;
	
	// METODO PRINCIPAL
	public static void main(String[] args) {
		getNewVersion();
		getNewBranch();
		getPath();
		
		
		// CON EL -C podes pasar a otro directorio sin hacer cd. Le indicas
		// sobre que directorio necesitas que trabaje
		// Creo el nuevo branch
		executeCommand("git -C " + path + " checkout -b " + newBranch);
		newVersion = primaryVersion+"."+newVersion+"."+newBranch+ "-SNAPSHOT";
			new WorkerPom(newVersion, path);
//		commitBranchToGit()
	}

	public static void getNewVersion() {
		Matcher m;
		do{
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
	
	// pido y debería validar ciertas cosas del nuevo branch
	public static void getNewBranch() {
		Matcher m;
		do {
			System.out.println("Ingrese el nuevo branch a crear. (Este nombre solo acepta Alphanumerics y _ [underscore]): \n");
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
	
//	public void commitBranchToGit() {
//
//	}
}

package com.snoop.dev2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.swing.text.AbstractDocument.BranchElement;

public class Main {

	private static String nuevaVersion;
	private static String path;
	private static String newBranch;

	// METODO PRINCIPAL
	public static void main(String[] args) {
		pedirNuevaVersion();
		obtenerPath();
		pedirNuevoBranch();

		// CON EL -C podes pasar a otro directorio sin hacer cd. Le indicas
		// sobre que directorio necesitas que trabaje
		// Creo el nuevo branch
		executeCommand("git -C " + path + " checkout -b " + newBranch);
		new WorkerPom(nuevaVersion, newBranch, path);
//		commitBranchToGit()
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

	public static void obtenerPath() {
		pedirPathEnDondeEjecutar();
		if (path.charAt(path.length() - 1) != '/') {
			path += "/";
		}
	}

	public static void pedirNuevaVersion() {
		System.out.println("Ingrese la nueva Version que se creara");
		System.out.println();
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		try {
			Main.nuevaVersion = br.readLine();
		} catch (IOException e) {

		}
	}

	public static void pedirPathEnDondeEjecutar() {
		System.out.println("Ingrese el path en donde ejecutar este proceso");
		System.out.println();
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		try {
			Main.path = br.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// pido y debería validar ciertas cosas del nuevo branch
	public static void pedirNuevoBranch() {
		do {
			System.out.println("Ingrese el nuevo branch a crear. Este nombre, no debe contener espacios ni puntos: ");
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			try {
				Main.newBranch = br.readLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
			// pregunto si al splitear con . tengo menos de dos campos.
		} while (newBranch.split(".").length < 2);
	}

//	public void commitBranchToGit() {
//
//	}
}

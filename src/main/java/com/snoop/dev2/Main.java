package com.snoop.dev2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {

	private static String nuevaVersion;
	private static String path;
	private static String newBranch;

	// METODO PRINCIPAL
	public static void main(String[] args) {
		pedirNuevaVersion();
		obtenerPath();
		newBranch = executeCommand("git rev-parse --abbrev-ref HEAD");
		new WorkerPom(nuevaVersion, newBranch, path);
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
}

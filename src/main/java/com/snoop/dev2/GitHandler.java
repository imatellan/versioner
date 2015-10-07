package com.snoop.dev2;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class GitHandler {
	public static void createNewBranch(String name) {
		executeCommand("git checkout -b " + name);
	}

	public static void createNewBranch(String name, String path) {
		executeCommand("git -C " + path + " checkout -b " + name);
	}

	public String obtenerBranchActual() {
		return executeCommand("git rev-parse --abbrev-ref HEAD", new String());
	}

	public String obtenerBranchActual(String path) {
		return executeCommand("git -C " + path + " rev-parse --abbrev-ref HEAD", new String());
	}

	private static String executeCommand(String command, String result) {
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
		result = output.toString();
		return result;
	}

	private static void executeCommand(String command) {
		@SuppressWarnings("unused")
		Process p;
		try {
			// Ejecuta el comando
			p = Runtime.getRuntime().exec(command);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

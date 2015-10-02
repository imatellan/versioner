package com.snoop.dev2;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.ExecutionException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

public class Main {

	private static String nuevaVersion;
	private static String path;
	private static File inFIle;
	private static File outFile;
	private static String pomXML;
	private static String newBranch;

	public static void main(String[] args) {

		pedirNuevaVersion();
		obtenerArchivo();
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(Project.class);
			newBranch = executeCommand("git rev-parse --abbrev-ref HEAD");
//			System.out.println(newBranch);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			Project pom = (Project) jaxbUnmarshaller.unmarshal(inFIle);
			if (pom.getVersion().compareTo(Main.nuevaVersion) < 0){
				modificarPom( pom.version );
			}else{
				System.out.println("Revise la version. La version actual es: "+ pom.version );
			}

		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}

	public static String executeCommand(String command) {
		StringBuffer output = new StringBuffer();
		Process p;
		try {
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

	public static void obtenerArchivo() {
		pedirPathEnDondeEjecutar();
		if (path.charAt(path.length() - 1) != '/') {
		}
		 path += "/";
		 pomXML = path + "pom.xml";
		 inFIle= new File(pomXML);
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
	
	public static void pedirNuevaVersion() {
		System.out.println("Ingrese la nueva Version que se creara");
		System.out.println();
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		try {
			Main.nuevaVersion = br.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void modificarPom(String oldPomVersion){
			String cadenaInArchivoXML= "";
			nuevaVersion+="."+newBranch;
			try{
				FileReader fr = new FileReader(inFIle);
				BufferedReader br2 = new BufferedReader(fr);
				String cadena= br2.readLine();
				while(cadena!=null){
					cadenaInArchivoXML+=cadena+"<<<<";
					cadena= br2.readLine();
				}
			}catch(IOException e){
				e.printStackTrace();
			}
			cadenaInArchivoXML= cadenaInArchivoXML.replace(oldPomVersion, nuevaVersion);
			String[] arrayCadena = cadenaInArchivoXML.split("<<<<");
			
			FileWriter fw=null;
			BufferedWriter bw=null;
			try{
				outFile= new File(Main.path+"0pom.xml");
				fw=new FileWriter(outFile);
				bw = new BufferedWriter(fw);
				for(String cadenaAuxiliar:arrayCadena){
					bw.write(cadenaAuxiliar);
					bw.newLine();
				}
			}catch(IOException e){
				e.printStackTrace();
			}finally{
				try {
					bw.close();
					fw.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
	}
}

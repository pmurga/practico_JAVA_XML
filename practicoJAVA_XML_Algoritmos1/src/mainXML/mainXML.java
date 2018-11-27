package mainXML;

import java.io.*;
import java.util.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;

/*
 * algunas referencias:
 * https://www.mkyong.com/java/how-to-read-xml-file-in-java-dom-parser/
 * https://www.tutorialspoint.com/java_xml/java_dom_parse_document.htm 
 */

public class mainXML {	
	
	final static String absolute_path = "/Users/pmurga/eclipse-workspace/practicoJAVA_XML_Algoritmos1/files/";
	static boolean isBeginning = true;

	
	public static void main(String[] args) {
		List<String> resultado = new ArrayList<>();
		library.XMLProcessor xml = new library.XMLProcessor();
		
		try {
			System.out.println("\n########################################################");
			System.out.println("\nInicio del Procesador de XML");
			System.out.println("\n########################################################");
			Thread.sleep(1000);
			System.out.println("\nCargando Reglas para el procesamiento del XML");
			System.out.println("\n########################################################");
			Node r = getRaiz(absolute_path + "reglas.xml");
			Thread.sleep(1000);
			System.out.println("\nCargando XML a procesar");
			System.out.println("\n########################################################");
			Node rDatos = getRaizDatos(r, absolute_path + "datos.xml", xml);
			Thread.sleep(1000);		
			procesar(r, rDatos, xml, resultado);
			Thread.sleep(1000);			
			System.out.println("\nFin del programa. Verificar resultado en --> " + absolute_path);
			System.out.println("\n########################################################");

		}catch (Exception e){
			
			System.out.println("##################################");
			System.out.println("##################################");
			System.out.println("\nVerificar cuál es el path absoluto definido en el main del proyecto. absolute_path --> " + absolute_path);
			System.out.println("");
			System.out.println("##################################");
			System.out.println("##################################");		
		}
		
	}	
	
	public static Node getRaiz(String reglas) {
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	        factory.setNamespaceAware(true);
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(reglas);
			Node raiz = document.getDocumentElement();
			return raiz;
		}
		catch (Exception e) {
			
			System.out.println("##################################");
			System.out.println("##################################");
			System.out.println("\nError al obtener la raiz");
			System.out.println("");
			System.out.println("##################################");
			System.out.println("##################################");			
			return null;
		}
	}	
	
	public static Node getRaizDatos(Node raiz, String datos, library.XMLProcessor xml) {
		String path = ((Element) raiz).getAttribute("path");
		return xml.root(datos, path);
	}
	
	public static void saveToFile(List<String> resultado, String string_final) {
		for(int i = 0; i < resultado.size(); i++) {
			string_final = string_final.replaceAll("\\$" + Integer.toString(i+1), resultado.get(i));
		}
		writeToFile(string_final);
	}	
	

	public static void writeToFile(String linea) {
		File file_salida = new File(absolute_path + "output.txt");
		
		//workaround para evitar que el file haga append infinitamente si se corre el programa más de una vez
		if (isBeginning){
			if (file_salida.exists()){file_salida.delete();}
			isBeginning = false;
		}
		
		try {
			BufferedWriter f = new BufferedWriter(new FileWriter(file_salida, true));
			f.write(linea);
			f.newLine();
			f.close();
		}
		catch (IOException e) {
			System.out.println("##################################");
			System.out.println("##################################");
			System.out.println("\nVerificar cuál es el path absoluto definido en el main del proyecto. absolute_path --> " + absolute_path);
			System.out.println("");
			System.out.println("##################################");
			System.out.println("##################################");
			return;
		}
	}
	
	public static void procesar(Node raiz, Node raizDatos, library.XMLProcessor xml, List<String> resultado) {

		List<String> resultado2 = new ArrayList<>(resultado);
		
		String nodo = raiz.getNodeName();
		String path = ((Element) raiz).getAttribute("path");
		String final_string = ((Element) raiz).getAttribute("final");
				
		NodeList hijos = raiz.getChildNodes();
		NodeList actuales = null;
		Node actual = null;
		
		
		if(esPrimero(nodo)) {
			actual = xml.getNode(raizDatos, path);
			leerDatos(resultado2, hijos, actual, xml);
			recorrer(hijos, actual, xml, resultado2);
			
			if(!final_string.equals("")) {saveToFile(resultado2, final_string);}
			
		}
		
		if(esIteracion(nodo)) {
			actuales = xml.getNodeList(raizDatos, path);
			List<String> resultado3;
			
			for (int i = 0; i < actuales.getLength(); i++) {
				resultado3 = new ArrayList<>(resultado2);
				actual = actuales.item(i);				
				leerDatos(resultado3, hijos, actual, xml);
				recorrer(hijos, actual, xml, resultado3);				
				if(!final_string.equals("")) {saveToFile(resultado3, final_string);}			
			}
		}
		
		if(esOpt(nodo)) {
			if(xml.existsNode(raizDatos, path)) {
				actual = xml.getNode(raizDatos, path);
				leerDatos(resultado2, hijos, actual, xml);
				recorrer(hijos, actual, xml, resultado2);
			}
			else {
				
				if(!final_string.equals("")) {saveToFile(resultado2, final_string);}
			}
		}
	}
	
	public static void leerDatos(List<String> resultado, NodeList hijos, Node actual, library.XMLProcessor xml) {
		for (int i = 0; i < hijos.getLength(); i++) {
			
			Node hijo = hijos.item(i);
			
			if (hijo.getNodeType() == Node.ELEMENT_NODE && esDato(hijo.getNodeName())) {
				NodeList nietos = hijo.getChildNodes();
				for (int j = 0; j < nietos.getLength(); j++) {
					Node nieto = nietos.item(j);
					if (nieto.getNodeType() == Node.ELEMENT_NODE) {
						resultado.add(xml.getNodeData(actual, ((Element) nieto).getAttribute("path")));
					}
				}
			}
		}
	}	
	
	public static boolean esDato(String tagNodo) {
		return tagNodo == "dato";
	}
	public static boolean esPrimero(String tagNodo) {
		return tagNodo == "primero";
	}
	public static boolean esOpt(String tagNodo) {
		return tagNodo == "opt";
	}
	public static boolean esIteracion(String tagNodo) {
		return tagNodo == "iteracion";
	}
	
	
	public static void recorrer(NodeList hijos, Node nodo_actual, library.XMLProcessor xml, List<String> resultado) {
		
		for (int i = 0; i < hijos.getLength(); i++) {
			Node hijo = hijos.item(i);
			
			if (hijo.getNodeType() == Node.ELEMENT_NODE && !esDato(hijo.getNodeName())) {
		
				procesar(hijo, nodo_actual, xml, resultado);
			}
			
		}
	}
}
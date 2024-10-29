package charly.pdfautomation.util;

import java.io.File;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

public class XMLProcessor {

	public static void procesarXML(File xmlFile, Map<String, String> data, Map<String, String[]> fieldMappings,
			File destinoDirectory) {
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(xmlFile);

			for (Map.Entry<String, String> entry : data.entrySet()) {
				String visualName = entry.getKey();
				String value = entry.getValue();
				String[] possibleFieldNames = fieldMappings.get(visualName);

				if (possibleFieldNames != null) {
					for (String fieldName : possibleFieldNames) {
						updateXMLField(doc, fieldName, value);
					}
				}
			}

			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File(destinoDirectory, xmlFile.getName()));
			transformer.transform(source, result);

			System.out.println("XML procesado y guardado exitosamente: " + xmlFile.getName());

		} catch (Exception e) {
			System.out.println("Error al procesar el XML: " + e.getMessage());
			e.printStackTrace();
		}
	}

	private static void updateXMLField(Document doc, String fieldPath, String value) throws Exception {
		XPath xpath = XPathFactory.newInstance().newXPath();
		Node node = (Node) xpath.evaluate(fieldPath, doc, XPathConstants.NODE);

		if (node != null) {
			node.setTextContent(value);
			System.out.println("Campo actualizado en el XML: " + fieldPath + " -> " + value);
		} else {
			System.out.println("Campo no encontrado en el XML: " + fieldPath);
		}
	}
}

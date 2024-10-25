package camposPDF;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.forms.fields.PdfFormField;
import com.itextpdf.forms.xfa.XfaForm;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;

public class leerCamposPDF {

	public static void main(String[] args) {
		try {
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setDialogTitle("Selecciona un archivo PDF");
			fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			int returnValue = fileChooser.showOpenDialog(null);
			if (returnValue == JFileChooser.APPROVE_OPTION) {
				File selectedFile = fileChooser.getSelectedFile();
				if (selectedFile.getName().toLowerCase().endsWith(".pdf")) {
					mostrarNombresCampos(selectedFile);
				} else {
					JOptionPane.showMessageDialog(null, "Por favor selecciona un archivo PDF.");
				}
			}
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Error al leer el archivo PDF: " + e.getMessage());
		}
	}

	private static void mostrarNombresCampos(File archivoPDF) throws IOException {
		String src = archivoPDF.getAbsolutePath();
		// Leer el archivo PDF
		PdfDocument pdfDoc = new PdfDocument(new PdfReader(src));

		// Verificar si el PDF tiene un formulario XFA
		XfaForm xfa = new XfaForm(pdfDoc);
		if (xfa.isXfaPresent()) {
			System.out.println("El archivo PDF contiene un formulario XFA.");
			// Extraer el XML asociado
			extraerYMostrarXML(xfa, archivoPDF);
		} else {
			// Si no tiene formulario XFA, proceder con el formulario AcroForm
			System.out.println("El archivo PDF contiene un formulario AcroForm.");
			PdfAcroForm form = PdfAcroForm.getAcroForm(pdfDoc, true);
			Map<String, PdfFormField> fields = form.getAllFormFields();

			// Usar un Set para almacenar los nombres de campos ya procesados
			Set<String> camposProcesados = new HashSet<>();

			System.out.println("Nombres de los campos y sus valores no vacíos en el archivo PDF:");
			fields.forEach((fieldName, field) -> {
				String fieldValue = field.getValueAsString();
				if (!fieldValue.isEmpty() && camposProcesados.add(fieldName)) {
					System.out.println("Campo: " + fieldName + " | Valor: " + fieldValue);
				}
			});
		}

		// Cerrar el documento PDF
		pdfDoc.close();
	}

	// Método para extraer el XML asociado de un formulario XFA
	private static void extraerYMostrarXML(XfaForm xfa, File archivoPDF) {
		try {
			// Obtener el documento DOM
			Document xmlDocument = xfa.getDomDocument();

			// Extraer el XML del formulario XFA a un archivo
			File xmlFile = new File(archivoPDF.getParent(), archivoPDF.getName().replace(".pdf", "_xfa.xml"));
			FileOutputStream fos = new FileOutputStream(xmlFile);

			// Usar Transformer para escribir el documento XML en un archivo
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty(OutputKeys.METHOD, "xml");
			transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

			DOMSource source = new DOMSource(xmlDocument);
			StreamResult result = new StreamResult(fos);
			transformer.transform(source, result);
			fos.close();

			System.out.println("XML extraído en: " + xmlFile.getAbsolutePath());

			// Leer y procesar el archivo XML
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(xmlFile);
			doc.getDocumentElement().normalize();

			// Obtener todos los campos del formulario XFA (generalmente bajo la etiqueta
			// "field")
			NodeList campos = doc.getElementsByTagName("field");

			System.out.println("Campos encontrados en el XML:");
			for (int i = 0; i < campos.getLength(); i++) {
				String nombreCampo = campos.item(i).getAttributes().getNamedItem("name").getNodeValue();
				System.out.println("Campo: " + nombreCampo);
			}

		} catch (Exception e) {
			System.out.println("Error al procesar el formulario XFA: " + e.getMessage());
		}
	}
}

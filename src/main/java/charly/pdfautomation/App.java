package charly.pdfautomation;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.SwingUtilities;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.forms.fields.PdfFormField;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;

import charly.pdfautomation.gui.GUI;
import charly.pdfautomation.model.Comunidad;

public class App {

	private static Map<String, String> data = new HashMap<>();
	private static Comunidad comunidad;
	private static File destinoDirectory; // Agregar variable para la carpeta de destino
	private GUI gui;
	private static String[] frases = { "¡Espero que tengas un día maravilloso!",
			"Recuerda sonreír y disfrutar cada momento.", "Cada día es una nueva oportunidad para ser feliz.",
			"La felicidad es un viaje, no un destino.", "Haz algo bonito hoy, ¡te lo mereces!",
			"¡Tú eres capaz de lograr grandes cosas!", "El sol siempre brilla después de la tormenta.",
			"Que tu día esté lleno de alegría y amor.", "¡Hoy es un buen día para empezar algo nuevo!",
			"La vida es corta, ¡haz que cada día cuente!", "No olvides lo increíble que eres.",
			"La sonrisa es el mejor maquillaje que puedes usar.", "Cada día trae nuevas posibilidades.",
			"Eres más fuerte de lo que piensas.", "La gratitud convierte lo que tenemos en suficiente.",
			"Hoy es un regalo, por eso se llama presente.",
			"La felicidad no es tener lo que quieres, sino querer lo que tienes.", "Nunca es tarde para ser feliz.",
			"La vida es mejor cuando sonríes.", "Eres una persona maravillosa, nunca lo olvides.",
			"La positividad es contagiosa, ¡espárcela!", "Hoy es un buen día para ser feliz.",
			"La vida es una aventura, disfrútala.", "Eres capaz de superar cualquier desafío.",
			"La felicidad se encuentra en las pequeñas cosas.", "Cada día es una nueva página en tu historia.",
			"La bondad es el lenguaje que los sordos pueden oír y los ciegos pueden ver.",
			"El optimismo es la fe que conduce al logro.", "La vida es demasiado corta para no ser feliz.",
			"Eres una luz en este mundo, ¡brilla siempre!", "Hoy te has levantado muy guapa!",
			"Gracias por usar mi programa, te quiero!", "La mejor madre del mundo", "Oleeee eres una crack",
			"Gracias por ser como eres", "Obi oba cada día te quiero más obi oba" };

	public App() {
		data = new HashMap<>();
		gui = new GUI(this);
	}

	public static void main(String[] args) {
		App app = new App();
		app.start();
	}

	// Método para capturar el valor
	public static void updateData(String key, String value) {
		data.put(key, value);
	}

	// Método para estilizar los botones
	@SuppressWarnings("unused")
	private static void styleButton(JButton button) {
		button.setFont(new Font("Helvetica", Font.BOLD, 16));
		button.setBackground(new Color(0, 122, 255)); // Color de fondo del botón
		button.setForeground(Color.WHITE);
		button.setFocusPainted(false);
		button.setBorder(BorderFactory.createLineBorder(new Color(0, 122, 255), 2));
		button.setAlignmentX(Component.CENTER_ALIGNMENT); // Centrar el botón
	}

	public static void procesarXML(File xmlFile) {
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(xmlFile);

			for (Map.Entry<String, String> entry : data.entrySet()) {
				String visualName = entry.getKey();
				String value = entry.getValue();

				String[] possibleFieldNames = comunidad.getFieldMappings().get(visualName);
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

	private static void updateXMLField(Document doc, String fieldPath, String value) throws XPathExpressionException {
		XPath xpath = XPathFactory.newInstance().newXPath();
		Node node = (Node) xpath.evaluate(fieldPath, doc, XPathConstants.NODE);

		if (node != null) {
			node.setTextContent(value);
			System.out.println("Campo actualizado en el XML: " + fieldPath + " -> " + value);
		} else {
			System.out.println("Campo no encontrado en el XML: " + fieldPath);
		}
	}

	public static void procesarPDFsEnDirectorio(File directory) throws IOException {
		File[] archivos = directory.listFiles((dir, name) -> name.toLowerCase().endsWith(".pdf"));
		if (archivos != null) {
			for (File archivo : archivos) {
				// Llama a rellenarPDF pasando todos los parámetros necesarios
				rellenarPDF(archivo, comunidad.getFieldMappings(), data, destinoDirectory);
			}
		} else {
			System.out.println("No se encontraron archivos PDF en la carpeta seleccionada.");
		}
	}

	private static void rellenarPDF(File archivoPDF, Map<String, String[]> fieldMappings, Map<String, String> data,
			File destinoDirectory) {
		try {
			String src = archivoPDF.getAbsolutePath();
			String tempFilePath = destinoDirectory.getAbsolutePath() + File.separator + archivoPDF.getName();

			// Abre el PDF existente
			PdfDocument pdfDoc = new PdfDocument(new PdfReader(src), new PdfWriter(tempFilePath));

			// Accede al formulario
			PdfAcroForm form = PdfAcroForm.getAcroForm(pdfDoc, true);

			// Recorre los datos para rellenar los campos

			for (Map.Entry<String, String> entry : data.entrySet()) {
				boolean fieldFound = false;
				String[] possibleFieldNames = fieldMappings.get(entry.getKey());

				if (possibleFieldNames != null) {
					for (String fieldName : possibleFieldNames) {
						System.out.println("Intentando acceder al campo: " + fieldName); // Mensaje de depuración
						PdfFormField field = form.getField(fieldName);
						if (field != null) {
							System.out.println("Propiedades del campo: " + field.getFieldFlags()); // Imprimir
																									// propiedades del
																									// campo
							System.out.println("Nombre del campo: " + field.getFieldName()); // Imprimir nombre del
																								// campo
							System.out.println("Valor actual del campo: " + field.getValueAsString()); // Imprimir valor
																										// actual del
																										// campo

							// Verificar si el campo es de solo lectura y quitar la bandera de solo lectura
							// si está presente
							if (field.isReadOnly()) {
								field.setReadOnly(false);
							}

							// Elimina todas las propiedades (flags) del campo
							// field.setFieldFlags(0);

							if (!field.isReadOnly()) { // Verificar que el campo no sea de solo lectura
								field.setValue(entry.getValue());
								fieldFound = true;
								break; // Salir del bucle si se ha encontrado el campo
							}
						}
					}
				}

				if (!fieldFound) {
					System.out.println(
							"Campo no encontrado en el archivo " + archivoPDF.getName() + ": " + entry.getKey());
				}
			}

			// Forzar la escritura de cambios
			pdfDoc.getWriter().flush();

			// Opcional: Aplanar el formulario para que los campos no sean editables
			// form.flattenFields();

			// Guarda los cambios y cierra el documento
			pdfDoc.close();
		} catch (IOException e) {
			System.out.println("Error procesando el archivo " + archivoPDF.getName() + ": " + e.getMessage());
		}
	}

	public void start() {
		SwingUtilities.invokeLater(() -> gui.createAndShowGUI());
	}

	public void setComunidad(Comunidad comunidad) {
		App.comunidad = comunidad;
	}

	public Comunidad getComunidad() {
		return comunidad;
	}

	public void setDestinoDirectory(File destinoDirectory) {
		App.destinoDirectory = destinoDirectory;
	}

	public File getDestinoDirectory() {
		return destinoDirectory;
	}

	public String getFraseAleatoria() {
		Random random = new Random();
		return frases[random.nextInt(frases.length)];
	}

}

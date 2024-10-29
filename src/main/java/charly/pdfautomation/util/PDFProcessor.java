package charly.pdfautomation.util;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.forms.fields.PdfFormField;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;

public class PDFProcessor {

	public static void procesarPDFsEnDirectorio(File directory, Map<String, String[]> fieldMappings,
			Map<String, String> data, File destinoDirectory) throws IOException {
		File[] archivos = directory.listFiles((dir, name) -> name.toLowerCase().endsWith(".pdf"));
		if (archivos != null) {
			for (File archivo : archivos) {
				rellenarPDF(archivo, fieldMappings, data, destinoDirectory);
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

			PdfDocument pdfDoc = new PdfDocument(new PdfReader(src), new PdfWriter(tempFilePath));
			PdfAcroForm form = PdfAcroForm.getAcroForm(pdfDoc, true);

			for (Map.Entry<String, String> entry : data.entrySet()) {
				boolean fieldFound = false;
				String[] possibleFieldNames = fieldMappings.get(entry.getKey());

				if (possibleFieldNames != null) {
					for (String fieldName : possibleFieldNames) {
						PdfFormField field = form.getField(fieldName);
						if (field != null && !field.isReadOnly()) {
							field.setValue(entry.getValue());
							fieldFound = true;
							break;
						}
					}
				}

				if (!fieldFound) {
					System.out.println(
							"Campo no encontrado en el archivo " + archivoPDF.getName() + ": " + entry.getKey());
				}
			}

			pdfDoc.getWriter().flush();
			pdfDoc.close();
		} catch (IOException e) {
			System.out.println("Error procesando el archivo " + archivoPDF.getName() + ": " + e.getMessage());
		}
	}
}

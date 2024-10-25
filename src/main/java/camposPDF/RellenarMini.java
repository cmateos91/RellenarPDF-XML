package camposPDF;

import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;

public class RellenarMini {
	public static void main(String[] args) throws Exception {
		PdfReader pdfReader = new PdfReader("D:\\TRABAJOPAPA\\ANDALUCIA\\PLANTILLA AUTORIZACION_ANDALUCIA.pdf");
		PdfWriter pdfWriter = new PdfWriter("D:\\TRABAJOPAPA\\ANDALUCIA\\test\\PLANTILLA AUTORIZACION_ANDALUCIA.pdf");
		PdfDocument pdfDocument = new PdfDocument(pdfReader, pdfWriter);

		// Accede al formulario
		PdfAcroForm form = PdfAcroForm.getAcroForm(pdfDocument, true);

		// Rellenar campos
		form.getField("form1[0].ANEXO[0].APARTADO_1[0].CUERPO[0].CAMPO_01[0]").setValue("Carlos");
		form.getField("form1[0].ANEXO[0].APARTADO_1[0].CUERPO[0].DNI_NIE_NIF[0]").setValue("123456789");

		/// Opcional: Aplanar el formulario para que los campos no sean editables
		/// después de rellenar
		form.flattenFields();

		// Guarda y cierra el documento
		pdfDocument.close();
	}
}

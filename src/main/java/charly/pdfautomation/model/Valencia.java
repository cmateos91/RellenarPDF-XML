package charly.pdfautomation.model;

import java.util.LinkedHashMap;
import java.util.Map;

public class Valencia implements Comunidad {
	@Override
	public Map<String, String[]> getFieldMappings() {
		Map<String, String[]> fieldMappings = new LinkedHashMap<>();

		// Mapear los campos del formulario con barras invertidas como en el PDF
		fieldMappings.put("Apellidos y nombre",
				new String[] { "form1[0].Pagina1[0].seccion\\.a[0].seccion\\.a[0].A_1[0]", "Titular" });

		fieldMappings.put("NIF", new String[] { "form1[0].Pagina1[0].seccion\\.a[0].seccion\\.a[0].A_2[0]", "NIFCIF" });

		fieldMappings.put("Domicilio",
				new String[] { "form1[0].Pagina1[0].seccion\\.a[0].seccion\\.a[0].A_5[0]", "Domicilio del titular" });

		fieldMappings.put("Código Postal",
				new String[] { "form1[0].Pagina1[0].seccion\\.a[0].seccion\\.a[0].A_6[0]", "CP" });

		fieldMappings.put("Localidad", new String[] { "form1[0].Pagina1[0].seccion\\.a[0].seccion\\.a[0].A_7[0]",
				"form1[0].Pagina1[1].seccion\\.a[0].seccion\\.a[0].A_7[0]" });

		fieldMappings.put("Provincia", new String[] { "form1[0].Pagina1[0].seccion\\.a[0].seccion\\.a[0].A_8[0]",
				"form1[0].Pagina1[1].seccion\\.a[0].seccion\\.a[0].A_8[0]" });

		fieldMappings.put("Teléfono", new String[] { "form1[0].Pagina1[0].seccion\\.a[0].seccion\\.a[0].A_9[0]",
				"form1[0].Pagina1[1].seccion\\.a[0].seccion\\.a[0].A_9[0]" });

		fieldMappings.put("Email", new String[] { "form1[0].Pagina1[0].seccion\\.a[0].seccion\\.a[0].A_11[0]",
				"form1[0].Pagina1[1].seccion\\.a[0].seccion\\.a[0].A_11[0]" });

		fieldMappings.put("Nombre del archivo", new String[] { "form1[0].Pagina1[0].NOM_ARCHIVO[0]" });

		fieldMappings.put("Provincia de la instalación",
				new String[] { "form1[0].Pagina1[1].seccion\\.a[0].A_PROV_INST[0]" });

		fieldMappings.put("Código Postal de la instalación",
				new String[] { "form1[0].Pagina1[1].seccion\\.a[0].A_CP_INST[0]" });

		return fieldMappings;
	}

	@Override
	public String getNombre() {
		// TODO Auto-generated method stub
		return null;
	}
}

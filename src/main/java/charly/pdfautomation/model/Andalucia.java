package charly.pdfautomation.model;

import java.util.LinkedHashMap;
import java.util.Map;

public class Andalucia implements Comunidad {
	@Override
	public Map<String, String[]> getFieldMappings() {
		Map<String, String[]> fieldMappings = new LinkedHashMap<>();

		// Datos del titular de la instalación
		fieldMappings.put("Apellidos y Nombre o Razon Social", new String[] { "cNombre",
				"/form1/ANEXO/APARTADO_1/CUERPO/CAMPO_01[1]", "/form1/ANEXO/APARTADO_4/CUERPO/FDO" });
		fieldMappings.put("DNI", new String[] { "cDNI", "/form1/ANEXO/APARTADO_1/CUERPO/DNI_NIE_NIF[1]" });
		fieldMappings.put("Domicilio", new String[] { "cDireccion", "/form1/ANEXO/APARTADO_1/CUERPO/DNI_NIE_NIF[2]" });
		fieldMappings.put("Código Postal", new String[] { "cCP", "/form1/ANEXO/APARTADO_1/CUERPO/CÓDIGO_POSTAL" });
		fieldMappings.put("Población", new String[] { "cLocalidad", "/form1/ANEXO/APARTADO_1/CUERPO/LOCALIDAD" });
		fieldMappings.put("Provincia", new String[] { "cProvincia", "/form1/ANEXO/APARTADO_1/CUERPO/PROVINCIA" });
		fieldMappings.put("Teléfono", new String[] { "cTelefono", "/form1/ANEXO/APARTADO_1/CUERPO/TELEFONO" });
		fieldMappings.put("FAX", new String[] { "FAX", "/form1/ANEXO/APARTADO_1/CUERPO/FAX" });
		fieldMappings.put("Correo electrónico",
				new String[] { "cEmail", "/form1/ANEXO/APARTADO_1/CUERPO/CORREO_ELECTRONICO" });

		// Apartado 2 - Datos de la instalación
		fieldMappings.put("Dirección de la instalación",
				new String[] { "cUbicacion", "/form1/ANEXO/APARTADO_2/TextField1[1]" });
		fieldMappings.put("Localidad de la instalación",
				new String[] { "cLocalidad_1", "/form1/ANEXO/APARTADO_2/TextField1[2]" });
		fieldMappings.put("Código Postal de la instalación",
				new String[] { "cCP_1", "/form1/ANEXO/APARTADO_2/TextField2" });
		fieldMappings.put("Provincia de la instalación",
				new String[] { "cProvincia_1", "/form1/ANEXO/APARTADO_2/TextField1[3]" });

		// Apartado 3 - Empresa instaladora
		fieldMappings.put("Razón social empresa instaladora",
				new String[] { "cRazon_Social", "/form1/ANEXO/APARTADO_3/TextField1[1]" });
		fieldMappings.put("CIF empresa instaladora", new String[] { "cCIF", "/form1/ANEXO/APARTADO_3/TextField1[2]" });
		fieldMappings.put("Nº de Registro empresa instaladora", new String[] { "cNumRegistro" });
		fieldMappings.put("Domicilio empresa instaladora", new String[] { "cDomicilioFiscal" });
		fieldMappings.put("Código Postal empresa instaladora", new String[] { "cCP_2" });
		fieldMappings.put("Localidad empresa instaladora", new String[] { "cLocalidad_2" });
		fieldMappings.put("Provincia empresa instaladora", new String[] { "cProvincia_2" });

		// Campos relacionados con filas (F1, F2, etc.)
		fieldMappings.put("Tipo Local F1", new String[] { "cTipoLocal1" });
		fieldMappings.put("Tipo Local F2", new String[] { "cTipoLocal2" });
		fieldMappings.put("Tipo Local F3", new String[] { "cTipoLocal3" });
		fieldMappings.put("Tipo Local F4", new String[] { "cTipoLocal4" });
		fieldMappings.put("Superficie F1", new String[] { "cSuperficie1" });
		fieldMappings.put("Superficie F2", new String[] { "cSuperficie2" });
		fieldMappings.put("Superficie F3", new String[] { "cSuperficie3" });
		fieldMappings.put("Superficie F4", new String[] { "cSuperficie4" });
		fieldMappings.put("Orientación F1", new String[] { "cOrientacion1" });
		fieldMappings.put("Orientación F2", new String[] { "cOrientacion2" });
		fieldMappings.put("Orientación F3", new String[] { "cOrientacion3" });
		fieldMappings.put("Orientación F4", new String[] { "cOrientacion4" });
		fieldMappings.put("Cargas F1", new String[] { "cCargas1" });
		fieldMappings.put("Cargas F2", new String[] { "cCargas2" });
		fieldMappings.put("Cargas F3", new String[] { "cCargas3" });
		fieldMappings.put("Cargas F4", new String[] { "cCargas4" });
		fieldMappings.put("Potencia Instalada F1", new String[] { "cPotenciaIns1" });
		fieldMappings.put("Potencia Instalada F2", new String[] { "cPotenciaIns2" });
		fieldMappings.put("Potencia Instalada F3", new String[] { "cPotenciaIns3" });
		fieldMappings.put("Potencia Instalada F4", new String[] { "cPotenciaIns4" });

		// Información adicional del generador
		fieldMappings.put("Marca del generador", new String[] { "cMarca" });
		fieldMappings.put("Modelo del generador", new String[] { "cModelo" });
		fieldMappings.put("Potenca calefacción", new String[] { "cPotenciaCalefaccion" });
		fieldMappings.put("COP Nominal Bomba Calor", new String[] { "cNominalBombCalor" });
		fieldMappings.put("Potencia frigorífica(KW)", new String[] { "cPotenciaFrigorifica" });
		fieldMappings.put("Potencia compresor(KW)", new String[] { "cPotenciaCompresor" });
		fieldMappings.put("Prestación energética", new String[] { "cPrestacionEnergetica" });
		fieldMappings.put("Potencia eléctrica Nominal(KW)", new String[] { "cPotElecNominal" });

		fieldMappings.put("Al 100% de carga", new String[] { "cRend100" });

		// Apartado 4 - Datos del titular del carné
		fieldMappings.put("Apellidos y Nombre TITULAR DEL CARNÉ",
				new String[] { "cTCNombre", "cTCNombre_1", "/form1/ANEXO/APARTADO_3/TextField1[1]" });
		fieldMappings.put("DNI TITULAR DEL CARNÉ", new String[] { "cTCDNI", "/form1/ANEXO/APARTADO_3/TextField1[2]" });
		fieldMappings.put("Domicilio TITULAR DEL CARNÉ", new String[] { "cTCDireccion" });
		fieldMappings.put("Código Postal TITULAR DEL CARNÉ", new String[] { "cTCCP" });
		fieldMappings.put("Población TITULAR DEL CARNÉ", new String[] { "cTCLocalidad" });
		fieldMappings.put("Provincia TITULAR DEL CARNÉ", new String[] { "cTCProvincia" });
		fieldMappings.put("Teléfono TITULAR DEL CARNÉ", new String[] { "cTCTelefono" });
		fieldMappings.put("Nº carné TITULAR DEL CARNÉ", new String[] { "cTCNumCarne" });
		fieldMappings.put("Categoría TITULAR DEL CARNÉ", new String[] { "cTCCategoria" });
		fieldMappings.put("Especialidad TITULAR DEL CARNÉ", new String[] { "cTCEspecialidad" });

		// Datos del día, lugar y año
		fieldMappings.put("Ciudad -- SEVILLA -- ",
				new String[] { "cCiudadHoy", "/form1/ANEXO/APARTADO_4/CUERPO/LUGAR" });
		fieldMappings.put("Día", new String[] { "cDia", "/form1/ANEXO/APARTADO_4/CUERPO/DIA" });
		fieldMappings.put("Mes", new String[] { "cMes" });
		fieldMappings.put("Año", new String[] { "cAnio", "/form1/ANEXO/APARTADO_4/CUERPO/ORDEN_AÑO" });

		return fieldMappings;
	}

	@Override
	public String getNombre() {
		// TODO Auto-generated method stub
		return "Andalucia";
	}
}

package charly.pdfautomation.util;

import charly.pdfautomation.model.Andalucia;
import charly.pdfautomation.model.Comunidad;
import charly.pdfautomation.model.Valencia;

public class ComunidadAutoFactory {
	public static Comunidad getComunidad(String nombre) {
		switch (nombre.toLowerCase()) {
		case "andalucia":
			return new Andalucia();
		// Aquí agregarías más casos para otras comunidades autónomas
		case "valencia":
			return new Valencia(); // Ejemplo si hay otra clase
		default:
			throw new IllegalArgumentException("Comunidad no soportada: " + nombre);
		}
	}
}
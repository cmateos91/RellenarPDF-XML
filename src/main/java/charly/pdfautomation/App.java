package charly.pdfautomation;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.swing.SwingUtilities;

import charly.pdfautomation.gui.GUI;
import charly.pdfautomation.model.Comunidad;

public class App {
	private static Map<String, String> data = new HashMap<>();
	private static Comunidad comunidad;
	private static File destinoDirectory;
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
	private GUI guiHandler;

	public App() {
		data = new HashMap<>();
		guiHandler = new GUI(this);
	}

	public static void main(String[] args) {
		App app = new App();
		app.start();
	}

	public void start() {
		SwingUtilities.invokeLater(() -> guiHandler.createAndShowGUI());
	}

	public static void updateData(String key, String value) {
		data.put(key, value);
	}

	// Getters y Setters
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
		if (frases.length == 0) {
			return "No hay frases disponibles";
		}
		Random random = new Random();
		return frases[random.nextInt(frases.length)];
	}

	public Map<String, String> getData() {
		return data;
	}
}

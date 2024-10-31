package charly.pdfautomation.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.io.File;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import charly.pdfautomation.App;
import charly.pdfautomation.model.Comunidad;
import charly.pdfautomation.util.ComunidadAutoFactory;
import charly.pdfautomation.util.PDFProcessor;
import charly.pdfautomation.util.XMLProcessor;

public class GUI {
	private JFrame frame;
	private App app;
	private JLabel destinoLabel;
	private Map<String, String[]> fieldMappings;
	private JPanel formPanel;
	private JComboBox<String> instaladorComboBox;
	private JComboBox<String> maquinaComboBox;
	private JPanel extraFieldsPanel;

	public GUI(App app) {
		this.app = app;
		this.formPanel = new JPanel();
		this.formPanel.setLayout(new BoxLayout(this.formPanel, BoxLayout.Y_AXIS));
	}

	public void createAndShowGUI() {
		frame = new JFrame("Rellenar Campos PDF");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(800, 800);
		frame.setLayout(new BorderLayout());
		frame.getContentPane().setBackground(new Color(245, 245, 250));

		createInitialView();

		frame.setVisible(true);
	}

	private void createInitialView() {
		JPanel mainPanel = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(10, 10, 10, 10);

		JLabel instructionLabel = new JLabel("Por favor, selecciona una comunidad autónoma y completa los campos.");
		instructionLabel.setFont(new Font("Helvetica", Font.PLAIN, 18));
		instructionLabel.setHorizontalAlignment(JLabel.CENTER);
		mainPanel.add(instructionLabel, gbc);

		JLabel comunidadLabel = new JLabel("Selecciona Comunidad Autónoma:");
		comunidadLabel.setFont(new Font("Helvetica", Font.BOLD, 16));
		comunidadLabel.setHorizontalAlignment(JLabel.CENTER);
		mainPanel.add(comunidadLabel, gbc);

		String[] comunidades = { "Andalucia", "Valencia" };
		JComboBox<String> comunidadComboBox = new JComboBox<>(comunidades);
		comunidadComboBox.setFont(new Font("Helvetica", Font.PLAIN, 16));
		comunidadComboBox.setBackground(Color.WHITE);
		comunidadComboBox.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
		mainPanel.add(comunidadComboBox, gbc);

		JButton selectButton = new JButton("Seleccionar");
		styleButton(selectButton);
		selectButton.addActionListener(e -> {
			String seleccion = (String) comunidadComboBox.getSelectedItem();
			Comunidad comunidad = ComunidadAutoFactory.getComunidad(seleccion);
			app.setComunidad(comunidad);
			createFormFields();
		});

		// Botón para mostrar/ocultar los JComboBox
		JButton toggleButton = new JButton("+");
		toggleButton.setFont(new Font("Helvetica", Font.BOLD, 20));
		toggleButton.addActionListener(e -> toggleExtraFields());
		mainPanel.add(toggleButton, gbc);

		// Crear el panel que contendrá los JComboBox
		extraFieldsPanel = new JPanel();
		extraFieldsPanel.setLayout(new BoxLayout(extraFieldsPanel, BoxLayout.Y_AXIS));
		extraFieldsPanel.setVisible(false); // Ocultar inicialmente

		// Crear los JComboBox para el instalador y la máquina
		instaladorComboBox = new JComboBox<>(new String[] { "", "Instalador 1", "Instalador 2", "Instalador 3" });
		instaladorComboBox.addActionListener(e -> handleInstaladorSelection());
		maquinaComboBox = new JComboBox<>(new String[] { "", "Máquina 1", "Máquina 2", "Máquina 3" });

		extraFieldsPanel.add(new JLabel("Selecciona el Instalador:"));
		extraFieldsPanel.add(instaladorComboBox);
		extraFieldsPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Espacio entre componentes
		extraFieldsPanel.add(new JLabel("Selecciona la Máquina:"));
		extraFieldsPanel.add(maquinaComboBox);

		// Añadir el panel de campos extra al mainPanel
		mainPanel.add(extraFieldsPanel, gbc);

		frame.add(mainPanel, BorderLayout.CENTER);

		mainPanel.add(selectButton, gbc);

		frame.add(mainPanel, BorderLayout.CENTER);
	}

	private void createFormFields() {
		frame.getContentPane().removeAll();

		formPanel = new JPanel();
		formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));

		fieldMappings = app.getComunidad().getFieldMappings();

		LocalDate currentDate = LocalDate.now();
		String day = String.valueOf(currentDate.getDayOfMonth());
		String month = String.valueOf(currentDate.getMonthValue());
		String year = String.valueOf(currentDate.getYear());

		for (Map.Entry<String, String[]> entry : fieldMappings.entrySet()) {
			String visualName = entry.getKey();
			addFormField(visualName, day, month, year);
		}

		addOutputSelectionButton();
		addUpdateButton(); // Agregar esta línea para incluir el botón de actualización
		addProcessButtons();

		handleInstaladorSelection();

		JScrollPane scrollPane = new JScrollPane(formPanel);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setPreferredSize(new Dimension(400, 600));

		// Agregar MouseWheelListener para ajustar la velocidad de desplazamiento
		scrollPane.addMouseWheelListener(new MouseWheelListener() {
			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				// Obtener la cantidad de desplazamiento
				int notches = e.getWheelRotation();
				// Aumentar la cantidad de desplazamiento
				int scrollAmount = notches * 30; // Ajusta el 30 según la velocidad deseada
				// Desplazar el JScrollPane
				JScrollBar verticalScrollBar = scrollPane.getVerticalScrollBar();
				verticalScrollBar.setValue(verticalScrollBar.getValue() + scrollAmount);
			}
		});

		frame.add(scrollPane, BorderLayout.CENTER);
		frame.revalidate();
		frame.repaint();
	}

	private void addFormField(String visualName, String day, String month, String year) {
		JLabel label = new JLabel(visualName + ":");
		label.setFont(new Font("Helvetica", Font.PLAIN, 16));
		label.setAlignmentX(Component.CENTER_ALIGNMENT);

		JTextField textField = new JTextField();
		textField.setPreferredSize(new Dimension(300, 30));
		textField.setFont(new Font("Helvetica", Font.PLAIN, 16));
		textField.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));

		if (visualName.equalsIgnoreCase("Día")) {
			textField.setText(day);
		} else if (visualName.equalsIgnoreCase("Mes")) {
			textField.setText(month);
		} else if (visualName.equalsIgnoreCase("Año")) {
			textField.setText(year);
		} else if (visualName.equalsIgnoreCase("Ciudad Actual")) {
			textField.setText("Sevilla");
		}

//		// DocumentListener para actualizar 'data' cada vez que el campo cambia
//		textField.getDocument().addDocumentListener(new DocumentListener() {
//			@Override
//			public void insertUpdate(DocumentEvent e) {
//				updateData();
//			}
//
//			@Override
//			public void removeUpdate(DocumentEvent e) {
//				updateData();
//			}
//
//			@Override
//			public void changedUpdate(DocumentEvent e) {
//				updateData();
//			}
//
//			private void updateData() {
//				app.getData().put(visualName, textField.getText());
//			}
//		});

		label.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
		textField.setBorder(
				BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(173, 216, 230), 2),
						BorderFactory.createEmptyBorder(5, 5, 5, 5)));

		formPanel.add(label);
		formPanel.add(textField);
		formPanel.add(Box.createRigidArea(new Dimension(0, 10)));
	}

	private void addOutputSelectionButton() {
		JButton selectOutputButton = new JButton("Seleccionar Carpeta de Salida");
		styleButton(selectOutputButton);
		selectOutputButton.addActionListener(e -> {
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setDialogTitle("Selecciona la carpeta de salida");
			fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			int returnValue = fileChooser.showOpenDialog(frame);
			if (returnValue == JFileChooser.APPROVE_OPTION) {
				File destinoDirectory = fileChooser.getSelectedFile();
				app.setDestinoDirectory(destinoDirectory);
				destinoLabel.setText(
						"<html>Carpeta seleccionada: <b>" + destinoDirectory.getAbsolutePath() + "</b></html>");

			}
		});

		destinoLabel = new JLabel("Carpeta seleccionada: Ninguna");
		destinoLabel.setFont(new Font("Helvetica", Font.PLAIN, 16));
		destinoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

		formPanel.add(destinoLabel);
		formPanel.add(Box.createRigidArea(new Dimension(0, 10)));
		formPanel.add(selectOutputButton);
		formPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Espacio extra antes del botón de actualización
	}

	private void addUpdateButton() {
		JButton updateButton = new JButton("Grabar Campos");
		styleLargeButton(updateButton); // Estiliza el botón
		updateButton.setBackground(new Color(0, 128, 0));
		updateButton.addActionListener(e -> {
			// Actualiza todos los campos antes de procesar el PDF
			updateFields(); // Llama al método para actualizar campos
			JOptionPane.showMessageDialog(frame, "Campos actualizados correctamente.");
		});

		// Agregar el botón al panel de botones o donde necesites
		formPanel.add(updateButton); // Suponiendo que tienes un panel para botones

	}

	private void addProcessButtons() {
		JButton processButton = new JButton("Procesar PDF");
		styleLargeButton(processButton); // Estiliza el botón
		processButton.setBackground(new Color(255, 0, 0)); // Cambia el fondo a rojo

		processButton.addActionListener(e -> {
			if (app.getDestinoDirectory() == null) {
				JOptionPane.showMessageDialog(frame, "Por favor, selecciona una carpeta de salida.");
				return;
			}

			// Procesar el PDF usando los datos ya actualizados
			try {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setDialogTitle("Selecciona la carpeta con los PDFs");
				fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int returnValue = fileChooser.showOpenDialog(frame);
				if (returnValue == JFileChooser.APPROVE_OPTION) {
					File selectedDirectory = fileChooser.getSelectedFile();
					PDFProcessor.procesarPDFsEnDirectorio(selectedDirectory, fieldMappings, app.getData(),
							app.getDestinoDirectory());
					JOptionPane.showMessageDialog(frame, "Procesamiento completado. " + app.getFraseAleatoria());
				}
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(frame, "Error al procesar los PDFs: " + ex.getMessage());
			}
		});

		// Agregar el botón de procesar PDF al panel de botones
		formPanel.add(processButton); // Suponiendo que tienes un panel para botones

		JButton processXMLButton = new JButton("Procesar XML");
		styleLargeButton(processXMLButton);
		processXMLButton.addActionListener(e -> {
			if (app.getDestinoDirectory() == null) {
				JOptionPane.showMessageDialog(frame, "Por favor, selecciona una carpeta de salida.");
				return;
			}
			try {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setDialogTitle("Selecciona el archivo XML");
				fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Archivos XML", "xml"));
				int returnValue = fileChooser.showOpenDialog(frame);
				if (returnValue == JFileChooser.APPROVE_OPTION) {
					File selectedXMLFile = fileChooser.getSelectedFile();
					XMLProcessor.procesarXML(selectedXMLFile, app.getData(), app.getComunidad().getFieldMappings(),
							app.getDestinoDirectory());
					JOptionPane.showMessageDialog(frame,
							"El archivo XML ha sido procesado correctamente." + app.getFraseAleatoria());
				}
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(frame, "Error al procesar el XML: " + ex.getMessage());
			}
		});
		// Crear un panel para los botones y añadirlos uno al lado del otro
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
		buttonPanel.add(processButton);
		buttonPanel.add(processXMLButton);

		// Añadir el panel de botones al formPanel
		formPanel.add(Box.createRigidArea(new Dimension(0, 10)));
		formPanel.add(buttonPanel);
	}

	private void handleInstaladorSelection() {
		String selectedInstalador = (String) instaladorComboBox.getSelectedItem();
		toggleFieldVisibility(selectedInstalador != null && !selectedInstalador.isEmpty());
	}

	// Método para actualizar los campos de texto
	private void updateFields() {
		for (Component comp : formPanel.getComponents()) {
			if (comp instanceof JTextField) {
				JTextField textField = (JTextField) comp;
				// Obtener la etiqueta correspondiente al campo de texto
				String label = ((JLabel) textField.getParent()
						.getComponent(textField.getParent().getComponentZOrder(textField) - 1)).getText();
				// Actualizar el mapa de datos con el valor del campo de texto
				app.getData().put(label.replace(":", "").trim(), textField.getText());
			}
		}
	}

	private void toggleFieldVisibility(boolean hideFields) {
		for (Component comp : formPanel.getComponents()) {
			if (comp instanceof JLabel) {
				JLabel label = (JLabel) comp;
				String labelText = label.getText().replace(":", "").trim();

				// Lista de campos que quieres ocultar
				String[] fieldsToHide = { "Apellidos y Nombre o Razon Social", "DNI", "Domicilio", "Código Postal",
						"Población", "Provincia", "Teléfono", "FAX", "Correo electrónico",
						"Dirección de la instalación", "Localidad de la instalación", "Código Postal de la instalación",
						"Provincia de la instalación", "Razón social empresa instaladora", "CIF empresa instaladora",
						"Nº de Registro empresa instaladora", "Domicilio empresa instaladora",
						"Código Postal empresa instaladora", "Localidad empresa instaladora",
						"Provincia empresa instaladora", "Tipo Local F1", "Tipo Local F2", "Tipo Local F3",
						"Tipo Local F4", "Superficie F1", "Superficie F2", "Superficie F3", "Superficie F4",
						"Orientación F1", "Orientación F2", "Orientación F3", "Orientación F4", "Cargas F1",
						"Cargas F2", "Cargas F3", "Cargas F4", "Potencia Instalada F1", "Potencia Instalada F2",
						"Potencia Instalada F3", "Potencia Instalada F4", "Marca del generador", "Modelo del generador",
						"Potenca calefacción", "COP Nominal Bomba Calor", "Potencia frigorífica(KW)",
						"Potencia compresor(KW)", "Prestación energética", "Potencia eléctrica Nominal(KW)",
						"Al 100% de carga" };

				if (Arrays.asList(fieldsToHide).contains(labelText)) {
					label.setVisible(!hideFields);
					// Ocultar también el campo de texto correspondiente
					Component nextComp = formPanel.getComponent(formPanel.getComponentZOrder(label) + 1);
					if (nextComp instanceof JTextField) {
						nextComp.setVisible(!hideFields);
					}
				}
			}
		}
		formPanel.revalidate();
		formPanel.repaint();
	}

	// Método para alternar la visibilidad de los campos extra
	private void toggleExtraFields() {
		extraFieldsPanel.setVisible(!extraFieldsPanel.isVisible());
		frame.revalidate(); // Actualiza el marco para mostrar u ocultar los campos
		frame.repaint(); // Redibuja el marco
	}

	// Método para estilizar botones grandes
	private void styleLargeButton(JButton button) {
		button.setFont(new Font("Helvetica", Font.BOLD, 20)); // Tamaño de fuente más grande
		button.setPreferredSize(new Dimension(350, 50)); // Aumenta el tamaño preferido
		button.setBackground(new Color(0, 122, 255));
		button.setForeground(Color.WHITE);
		button.setFocusPainted(false);
		button.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0), 3)); // Borde más grueso
		button.setAlignmentX(Component.CENTER_ALIGNMENT);
	}

	private void styleButton(JButton button) {
		button.setFont(new Font("Helvetica", Font.BOLD, 16));
		button.setBackground(new Color(0, 122, 255));
		button.setForeground(Color.WHITE);
		button.setFocusPainted(false);
		button.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0), 2));
		button.setAlignmentX(Component.CENTER_ALIGNMENT);
	}
}
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
import java.io.File;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
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
		frame.setSize(900, 800);
		frame.setLayout(new BorderLayout());
		frame.getContentPane().setBackground(new Color(240, 240, 250));

		createInitialView();

		frame.setVisible(true);
	}

	private void createInitialView() {
		JPanel mainPanel = new JPanel(new GridBagLayout());
		mainPanel.setBackground(new Color(240, 240, 250));
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(15, 15, 15, 15);

		JLabel titleLabel = new JLabel("Sistema de Relleno de Formularios PDF");
		titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
		titleLabel.setHorizontalAlignment(JLabel.CENTER);
		titleLabel.setForeground(new Color(50, 50, 100));
		mainPanel.add(titleLabel, gbc);

		JLabel instructionLabel = new JLabel("Por favor, selecciona una comunidad autónoma y completa los campos.");
		instructionLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		instructionLabel.setHorizontalAlignment(JLabel.CENTER);
		instructionLabel.setForeground(new Color(80, 80, 80));
		mainPanel.add(instructionLabel, gbc);

		JLabel comunidadLabel = new JLabel("Selecciona Comunidad Autónoma:");
		comunidadLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
		comunidadLabel.setHorizontalAlignment(JLabel.CENTER);
		mainPanel.add(comunidadLabel, gbc);

		String[] comunidades = { "Andalucia", "Valencia" };
		JComboBox<String> comunidadComboBox = new JComboBox<>(comunidades);
		comunidadComboBox.setFont(new Font("Segoe UI", Font.PLAIN, 16));
		comunidadComboBox.setBackground(Color.WHITE);
		comunidadComboBox.setBorder(
				BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
						BorderFactory.createEmptyBorder(5, 5, 5, 5)));
		mainPanel.add(comunidadComboBox, gbc);

		JButton selectButton = new JButton("Seleccionar");
		styleButton(selectButton);
		selectButton.addActionListener(e -> {
			String seleccion = (String) comunidadComboBox.getSelectedItem();
			Comunidad comunidad = ComunidadAutoFactory.getComunidad(seleccion);
			app.setComunidad(comunidad);
			createFormFields();
		});

		JButton toggleButton = new JButton("Campos Adicionales");
		styleButton(toggleButton);
		toggleButton.addActionListener(e -> toggleExtraFields());
		mainPanel.add(toggleButton, gbc);

		extraFieldsPanel = new JPanel();
		extraFieldsPanel.setLayout(new BoxLayout(extraFieldsPanel, BoxLayout.Y_AXIS));
		extraFieldsPanel.setBackground(new Color(245, 245, 255));
		extraFieldsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		extraFieldsPanel.setVisible(false);

		instaladorComboBox = new JComboBox<>(new String[] { "", "Instalador 1", "Instalador 2", "Instalador 3" });
		maquinaComboBox = new JComboBox<>(new String[] { "", "Máquina 1", "Máquina 2", "Máquina 3" });

		styleComboBox(instaladorComboBox);
		styleComboBox(maquinaComboBox);

		instaladorComboBox.addActionListener(e -> handleInstaladorSelection());

		extraFieldsPanel.add(createLabeledComponent("Selecciona el Instalador:", instaladorComboBox));
		extraFieldsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
		extraFieldsPanel.add(createLabeledComponent("Selecciona la Máquina:", maquinaComboBox));

		mainPanel.add(extraFieldsPanel, gbc);
		mainPanel.add(selectButton, gbc);

		frame.add(mainPanel, BorderLayout.CENTER);
	}

	private JPanel createLabeledComponent(String labelText, JComponent component) {
		JPanel panel = new JPanel(new BorderLayout(5, 0));
		panel.setOpaque(false);
		JLabel label = new JLabel(labelText);
		label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		panel.add(label, BorderLayout.WEST);
		panel.add(component, BorderLayout.CENTER);
		return panel;
	}

	private void createFormFields() {
		frame.getContentPane().removeAll();

		formPanel = new JPanel();
		formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
		formPanel.setBackground(new Color(245, 245, 255));
		formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

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
		addUpdateButton();
		addProcessButtons();

		handleInstaladorSelection();

		JScrollPane scrollPane = new JScrollPane(formPanel);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setPreferredSize(new Dimension(450, 600));
		scrollPane.getVerticalScrollBar().setUnitIncrement(16);
		scrollPane.setBorder(BorderFactory.createEmptyBorder());

		frame.add(scrollPane, BorderLayout.CENTER);
		frame.revalidate();
		frame.repaint();
	}

	private void addFormField(String visualName, String day, String month, String year) {
		JPanel fieldPanel = new JPanel(new BorderLayout(10, 5));
		fieldPanel.setOpaque(false);

		JLabel label = new JLabel(visualName + ":");
		label.setFont(new Font("Segoe UI", Font.BOLD, 14));
		label.setForeground(new Color(50, 50, 100));

		JTextField textField = new JTextField();
		textField.setPreferredSize(new Dimension(300, 30));
		textField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		textField.setBorder(
				BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(200, 200, 220), 1),
						BorderFactory.createEmptyBorder(5, 5, 5, 5)));

		if (visualName.equalsIgnoreCase("Día")) {
			textField.setText(day);
		} else if (visualName.equalsIgnoreCase("Mes")) {
			textField.setText(month);
		} else if (visualName.equalsIgnoreCase("Año")) {
			textField.setText(year);
		} else if (visualName.equalsIgnoreCase("Ciudad Actual")) {
			textField.setText("Sevilla");
		}

		fieldPanel.add(label, BorderLayout.WEST);
		fieldPanel.add(textField, BorderLayout.CENTER);

		formPanel.add(fieldPanel);
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
		destinoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		destinoLabel.setForeground(new Color(80, 80, 80));
		destinoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

		formPanel.add(destinoLabel);
		formPanel.add(Box.createRigidArea(new Dimension(0, 10)));
		formPanel.add(selectOutputButton);
		formPanel.add(Box.createRigidArea(new Dimension(0, 20)));
	}

	private void addUpdateButton() {
		JButton updateButton = new JButton("Grabar Campos");
		styleLargeButton(updateButton);
		updateButton.setBackground(new Color(0, 150, 0));
		updateButton.addActionListener(e -> {
			updateFields();
			JOptionPane.showMessageDialog(frame, "Campos actualizados correctamente.", "Actualización Exitosa",
					JOptionPane.INFORMATION_MESSAGE);
		});

		formPanel.add(updateButton);
		formPanel.add(Box.createRigidArea(new Dimension(0, 20)));
	}

	private void addProcessButtons() {
		JButton processButton = new JButton("Procesar PDF");
		styleLargeButton(processButton);
		processButton.setBackground(new Color(200, 0, 0));

		processButton.addActionListener(e -> {
			if (app.getDestinoDirectory() == null) {
				JOptionPane.showMessageDialog(frame, "Por favor, selecciona una carpeta de salida.", "Error",
						JOptionPane.ERROR_MESSAGE);
				return;
			}

			try {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setDialogTitle("Selecciona la carpeta con los PDFs");
				fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int returnValue = fileChooser.showOpenDialog(frame);
				if (returnValue == JFileChooser.APPROVE_OPTION) {
					File selectedDirectory = fileChooser.getSelectedFile();
					PDFProcessor.procesarPDFsEnDirectorio(selectedDirectory, fieldMappings, app.getData(),
							app.getDestinoDirectory());
					JOptionPane.showMessageDialog(frame, "Procesamiento completado. " + app.getFraseAleatoria(),
							"Éxito", JOptionPane.INFORMATION_MESSAGE);
				}
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(frame, "Error al procesar los PDFs: " + ex.getMessage(), "Error",
						JOptionPane.ERROR_MESSAGE);
			}
		});

		JButton processXMLButton = new JButton("Procesar XML");
		styleLargeButton(processXMLButton);
		processXMLButton.setBackground(new Color(0, 100, 200));

		processXMLButton.addActionListener(e -> {
			if (app.getDestinoDirectory() == null) {
				JOptionPane.showMessageDialog(frame, "Por favor, selecciona una carpeta de salida.", "Error",
						JOptionPane.ERROR_MESSAGE);
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
							"El archivo XML ha sido procesado correctamente." + app.getFraseAleatoria(), "Éxito",
							JOptionPane.INFORMATION_MESSAGE);
				}
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(frame, "Error al procesar el XML: " + ex.getMessage(), "Error",
						JOptionPane.ERROR_MESSAGE);
			}
		});

		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
		buttonPanel.setOpaque(false);
		buttonPanel.add(processButton);
		buttonPanel.add(processXMLButton);

		formPanel.add(buttonPanel);
	}

	private void handleInstaladorSelection() {
		String selectedInstalador = (String) instaladorComboBox.getSelectedItem();
		toggleFieldVisibility(selectedInstalador != null && !selectedInstalador.isEmpty());
	}

	private void updateFields() {
		for (Component comp : formPanel.getComponents()) {
			if (comp instanceof JPanel) {
				Component[] components = ((JPanel) comp).getComponents();
				for (Component innerComp : components) {
					if (innerComp instanceof JTextField) {
						JTextField textField = (JTextField) innerComp;
						String label = ((JLabel) ((JPanel) comp).getComponent(0)).getText();
						app.getData().put(label.replace(":", "").trim(), textField.getText());
					}
				}
			}
		}
	}

	private void toggleFieldVisibility(boolean hideFields) {
		for (Component comp : formPanel.getComponents()) {
			if (comp instanceof JPanel) {
				JPanel panel = (JPanel) comp;
				Component labelComp = panel.getComponent(0);
				if (labelComp instanceof JLabel) {
					JLabel label = (JLabel) labelComp;
					String labelText = label.getText().replace(":", "").trim();

					String[] fieldsToHide = { "Apellidos y Nombre o Razon Social", "DNI", "Domicilio", "Código Postal",
							"Población", "Provincia", "Teléfono", "FAX", "Correo electrónico",
							"Dirección de la instalación", "Localidad de la instalación",
							"Código Postal de la instalación", "Provincia de la instalación",
							"Razón social empresa instaladora", "CIF empresa instaladora",
							"Nº de Registro empresa instaladora", "Domicilio empresa instaladora",
							"Código Postal empresa instaladora", "Localidad empresa instaladora",
							"Provincia empresa instaladora", "Tipo Local F1", "Tipo Local F2", "Tipo Local F3",
							"Tipo Local F4", "Superficie F1", "Superficie F2", "Superficie F3", "Superficie F4",
							"Orientación F1", "Orientación F2", "Orientación F3", "Orientación F4", "Cargas F1",
							"Cargas F2", "Cargas F3", "Cargas F4", "Potencia Instalada F1", "Potencia Instalada F2",
							"Potencia Instalada F3", "Potencia Instalada F4", "Marca del generador",
							"Modelo del generador", "Potenca calefacción", "COP Nominal Bomba Calor",
							"Potencia frigorífica(KW)", "Potencia compresor(KW)", "Prestación energética",
							"Potencia eléctrica Nominal(KW)", "Al 100% de carga" };

					if (Arrays.asList(fieldsToHide).contains(labelText)) {
						// Cambia esto para eliminar el panel
						if (hideFields) {
							formPanel.remove(panel);
						} else {
							formPanel.add(panel);
						}
					}
				}
			}
		}
		formPanel.revalidate();
		formPanel.repaint();
	}

	private void toggleExtraFields() {
		extraFieldsPanel.setVisible(!extraFieldsPanel.isVisible());
		frame.revalidate();
		frame.repaint();
	}

	private void styleLargeButton(JButton button) {
		button.setFont(new Font("Segoe UI", Font.BOLD, 18));
		button.setPreferredSize(new Dimension(300, 50));
		button.setForeground(Color.WHITE);
		button.setFocusPainted(false);
		button.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(0, 0, 0), 1),
				BorderFactory.createEmptyBorder(10, 20, 10, 20)));
		button.setAlignmentX(Component.CENTER_ALIGNMENT);
	}

	private void styleButton(JButton button) {
		button.setFont(new Font("Segoe UI", Font.BOLD, 14));
		button.setBackground(new Color(100, 149, 237));
		button.setForeground(Color.WHITE);
		button.setFocusPainted(false);
		button.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(70, 130, 180), 1),
				BorderFactory.createEmptyBorder(8, 15, 8, 15)));
		button.setAlignmentX(Component.CENTER_ALIGNMENT);
	}

	private void styleComboBox(JComboBox<?> comboBox) {
		comboBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		comboBox.setBackground(Color.WHITE);
		comboBox.setBorder(
				BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
						BorderFactory.createEmptyBorder(5, 5, 5, 5)));
	}
}
package charly.pdfautomation.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.io.File;
import java.time.LocalDate;
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

public class GUI {
	private JFrame frame;
	private App app;
	private JLabel destinoLabel;
	private Map<String, String[]> fieldMappings;
	private JPanel formPanel;

	public GUI(App app) {
		this.app = app;
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
		addProcessButtons();

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
		}

		textField.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				app.updateData(visualName, textField.getText());
			}
		});

		textField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				app.updateData(visualName, textField.getText());
			}
		});

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
				destinoLabel.setText("Carpeta seleccionada: " + destinoDirectory.getAbsolutePath());
			}
		});

		destinoLabel = new JLabel("Carpeta seleccionada: Ninguna");
		destinoLabel.setFont(new Font("Helvetica", Font.PLAIN, 16));
		destinoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

		formPanel.add(destinoLabel);
		formPanel.add(Box.createRigidArea(new Dimension(0, 10)));
		formPanel.add(selectOutputButton);
	}

	private void addProcessButtons() {
		JButton processButton = new JButton("Procesar PDFs");
		styleButton(processButton);
		processButton.addActionListener(e -> {
			if (app.getDestinoDirectory() == null) {
				JOptionPane.showMessageDialog(frame, "Por favor, selecciona una carpeta de salida.");
				return;
			}
			try {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setDialogTitle("Selecciona la carpeta con los PDFs");
				fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int returnValue = fileChooser.showOpenDialog(frame);
				if (returnValue == JFileChooser.APPROVE_OPTION) {
					File selectedDirectory = fileChooser.getSelectedFile();
					app.procesarPDFsEnDirectorio(selectedDirectory);
					JOptionPane.showMessageDialog(frame, "Procesamiento completado. " + app.getFraseAleatoria());
				}
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(frame, "Error al procesar los PDFs: " + ex.getMessage());
			}
		});

		JButton processXMLButton = new JButton("Procesar XML");
		styleButton(processXMLButton);
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
					app.procesarXML(selectedXMLFile);
					JOptionPane.showMessageDialog(frame,
							"El archivo XML ha sido procesado correctamente." + app.getFraseAleatoria());
				}
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(frame, "Error al procesar el XML: " + ex.getMessage());
			}
		});

		formPanel.add(Box.createRigidArea(new Dimension(0, 10)));
		formPanel.add(processXMLButton);
		formPanel.add(Box.createRigidArea(new Dimension(0, 10)));
		formPanel.add(processButton);
	}

	private void styleButton(JButton button) {
		button.setFont(new Font("Helvetica", Font.BOLD, 16));
		button.setBackground(new Color(0, 122, 255));
		button.setForeground(Color.WHITE);
		button.setFocusPainted(false);
		button.setBorder(BorderFactory.createLineBorder(new Color(0, 122, 255), 2));
		button.setAlignmentX(Component.CENTER_ALIGNMENT);
	}
}
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Main {

	final static String viableDataType = "txt";
	
	JFrame jf;
	
	JTextArea output;
	JScrollPane outputPane;
	
	JButton importObject;
	JTextField path;
	
	JButton acceptButton;
	JCheckBox sortElements;
	
	FileUtilities fu;
	
	public static void main(String[] args) {
		new Main();
	}
	
	Main() {
		jf = new JFrame("Listen Zähler");
		fu = new FileUtilities();
		
		jf.setSize(400, 300);
		jf.setLocationRelativeTo(null);
		jf.setLayout(null);
		jf.setResizable(false);
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		displayGraphics();
		
		jf.setVisible(true);
		System.out.println("test");
	}
	
	void displayGraphics() {
		output = new JTextArea();
		output.setEditable(true);
		outputPane = new JScrollPane(output);
		outputPane.setBounds(10, 10, 375, 175);
		jf.add(outputPane);
		
		importObject = new JButton("Durchsuchen...");
		importObject.addActionListener(new AListener());
		importObject.setBounds(280, 200, 100, 30);
		jf.add(importObject);
		
		path = new JTextField("C:\\Users\\Tobi\\Schule\\Seminararbeit Sekundarstufe 2\\Umfrage\\Antworten\\einzelne Aspekte\\Daten Zähler.txt");
		path.setBounds(10, 200, 250, 30);
		jf.add(path);
		
		acceptButton = new JButton("gaartssa");
		acceptButton.addActionListener(new AListener());
		acceptButton.setBounds(280, 235, 100, 30);
		jf.add(acceptButton);
		
		sortElements = new JCheckBox("Zahlen Sortieren");
		sortElements.setBounds(10, 240, 120, 30);
		jf.add(sortElements);
	}
	
	int countObjects(boolean sortNumbers) {
		String path = this.path.getText();
		File f = new File(path);

		if (f.exists() && path.substring(path.lastIndexOf(".")+1).equals(viableDataType)){
			String[] fileInformation = fu.GetFileInformation(f, false);
			ArrayList<String> values = new ArrayList<String>();
			ArrayList<Integer> valueCounter = new ArrayList<Integer>();
			
			for (String s : fileInformation) {
				int index = values.indexOf(s);
				if (index == -1) {
					values.add(s);
					valueCounter.add(1);
				} else {
					valueCounter.set(index, valueCounter.get(index)+1);
				}
			}
			
			// sort elements
			if (sortNumbers) {
				ArrayList<Float> numbers = new ArrayList<Float>();
				for (int i = 0; i < values.size(); i++) {
					try {
					numbers.add(Float.parseFloat(values.get(i)));
					} catch (NumberFormatException e) {
						JOptionPane.showMessageDialog(jf,
								"Die Datei muss ausschließlich aus Zahlen bestehen, damit sie sortiert werden kann."
								+ "\nFehler in Zeile: " 
								+ (i+1),
								"Fehler!",
								JOptionPane.ERROR_MESSAGE);
						return 2;
					}
				}
				
				
				
				ArrayList<String> val = new ArrayList<String>();
				ArrayList<Integer> valCounter = new ArrayList<Integer>();
					
				int valueSize = values.size();
				for (int i = 0; i < valueSize; i++) {
					int index = numbers.indexOf(Collections.min(numbers));
						
					val.add(numbers.get(index).toString());
					valCounter.add(valueCounter.get(index));
						
					numbers.remove(index);
					valueCounter.remove(index);
				}
			
				System.out.println(val.get(1) + "   " + valCounter.get(1));
					
				values = val;
				valueCounter = valCounter;
				
			}
			
			String text = "";
			
			for (int i = 0; i < values.size(); i++) {
				text+=values.get(i) + " - " + valueCounter.get(i) + "\n";
			}
			
			int allNumbersSummed = 0;
			for (int i : valueCounter) allNumbersSummed+=i;
			
			text+= "\nAnzahl der Elemente: " + values.size() 
			+ "\nAlle nummern addiert: " + allNumbersSummed;
			
			output.setText(text);
			
			return 0;
		}
		
		JOptionPane.showMessageDialog(jf, 
				"Die angebene Datei existiert nicht oder ist keine Textdatei!"
				+ "\nDatei: " 
				+ f, 
				"Fehler!", 
				JOptionPane.ERROR_MESSAGE);
		return 1;
	}
	
	class AListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent ae) {
			if (ae.getSource() == acceptButton) {
				countObjects(sortElements.isSelected());
			} else if (ae.getSource() == importObject) {
				JFileChooser fc = new JFileChooser();
				fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
				fc.setFileFilter(new FileNameExtensionFilter("Text Files", "txt"));
				fc.showOpenDialog(null);
				if (fc.getSelectedFile() != null)path.setText(fc.getSelectedFile().getPath());
			}
		}
		
	}
}

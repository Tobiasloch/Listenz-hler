
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;
import java.util.Stack;

public class FileUtilities {
	
	Stack<BufferedReader> openedReader = new Stack<BufferedReader>();
	Stack<BufferedWriter> openedWriter = new Stack<BufferedWriter>();
	
	public void RemoveLine(File file, int line, boolean stayOpen) {
		String[] fileInformation = GetFileInformation(file, stayOpen);
		String[] array = new String[fileInformation.length];
		
		int index = 0;
		for (String s : fileInformation) {
			if (index == line || s == null) continue;
			array[index] = s;
			index++;
		}
		WriteInFile(file, array, true, stayOpen);
	}
	
	public static void CreateFile(File file) { // creates a new file
		try { // creates file
			Formatter f = new Formatter(file);
			f.close();
		} catch (FileNotFoundException e) { // if there is no file at the path
			e.printStackTrace();
		}
	}
	
	public void WriteInFile(File file, String[] text, boolean override, boolean stayOpen) { // writes a string array into a file; each index is a line
		try {
			FileWriter fw = new FileWriter(file, !override); // opens the file
			BufferedWriter bw = new BufferedWriter(fw); // is used to write into it
			
			for (String s : text) { // gets each element of the array
				if (s != null){
					bw.write(s); // writes the object into the string
					bw.newLine(); // cuts the line
				}
			}
			if (!stayOpen) bw.close(); // closes the file
			else openedWriter.push(bw);
			
		} catch (IOException e) { // if the path links to a file that doesnt exist
			e.printStackTrace();
		}
	}
	
	public void CloseAllFiles() { // closes all opened files
		if (!openedWriter.isEmpty()){
			for (BufferedWriter br : openedWriter) {
				try {
					br.close();
					openedWriter.pop();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			for (BufferedReader br : openedReader) {
				try {
					br.close();
					openedReader.pop();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public String[] GetFileInformation(File file, boolean stayOpen) {
		List<String> text = new ArrayList<String>(); // declares the list who is used to write the information form the file into it
		try {
			FileReader fr = new FileReader(file); // opens the file
			BufferedReader br = new BufferedReader(fr); // is used to get the text
			
			String s; // declares a string that gets the text
			do { // loop that is used to get all the lines from the file
				try {
					s = br.readLine(); // puts the text of the line into the string s and goes to the next one
					if (s != null)text.add(s); // puts it into the list and checks if its not empty
				} catch (IOException e) { // is used to check if there is no line left
					break;
				}
			}
			while (s != null); // if s isnt empty
			try {
				if (!stayOpen) br.close();
				else openedReader.push(br);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		} catch (FileNotFoundException e) { // checks if there is a file at the path
			e.printStackTrace();
		}
		
		// converts the list into an array
		String[] s = new String[text.size()];
		s = text.toArray(s);
		return s; // returns the array
	}
	
	public static File[] GetAllFoldersInFolder(File folder) {
		List<File> foldersInObject = new ArrayList<File>();
		Stack<File> folderStack = new Stack<File>();
		folderStack.push(folder);
		
		while (!folderStack.isEmpty()) {
			for (File f : folderStack.pop().listFiles()) {
				if (f.isDirectory()) {
					if (f != folder) foldersInObject.add(f);
					folderStack.push(f);
				}
			}
		}
		File[] f = new File[foldersInObject.size()];
		f = foldersInObject.toArray(f);
		
		return f;
	}
	
	public static File[] GetFilesWithSuffix(File folder, String suffix) {
		List<File> listOfFiles = new ArrayList<File>();
		
		for (File f : folder.listFiles()) {
			if (!f.isDirectory() && f.getName().indexOf(".") != -1 && f.getName().substring(f.getName().indexOf(".")).equals(suffix)) listOfFiles.add(f);
		}
		
		File[] f = new File[listOfFiles.size()];
		f = listOfFiles.toArray(f);
		
		return f;
	}
	
	public static File[] GetOnlyFilesInFolder(File folder) {
		List<File> listOfFiles = new ArrayList<File>();
		
		for (File f : folder.listFiles()) if (!f.isDirectory()) listOfFiles.add(f);
		
		File[] f = new File[listOfFiles.size()];
		f = listOfFiles.toArray(f);
		
		return f;
	}
	
	public static File[] GetAllFilesInFolder(File folder) {
		if (!folder.isDirectory()) return null;
			
		List<File> listOfFiles = new ArrayList<File>();
		File[] folders = GetAllFoldersInFolder(folder);
		
		for (File f : folder.listFiles()) listOfFiles.add(f);
		
		for (File f : folders) {
			if (f.listFiles().length != 0) {
				for (File g : f.listFiles()) {
					listOfFiles.add(g);
				}
			}
		}
		
		File[] f = new File[listOfFiles.size()];
		f = listOfFiles.toArray(f);
		
		return f;
	}
	
	public static String[] FilePathToString(File[] file) {
		String[] s = new String[file.length];
		for (int i = 0; i < file.length; i++) s[i] = file[i].getPath();
		return s;
	}
}

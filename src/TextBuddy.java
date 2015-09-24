// Matric Number: A0126159
// @author Jaime Lee Pabilona
// This program is a CLI (Command Line Interface) that can manipulate text in
// a file, and create it if it does not exist

import java.util.*;
import java.io.*;

public class TextBuddy{
	public static final String MESSAGE_WELCOME = "Welcome to TextBuddy.";
	public static final String MESSAGE_FILE_READY= " is ready for use";
	public static final String MESSAGE_COMMAND = "command: ";
	public static final String MESSAGE_ADD = "added to ";
	public static final String MESSAGE_DELETE = "deleted from ";
	public static final String MESSAGE_CLEAR = "all content deleted from ";
	public static final String MESSAGE_EMPTY = " is empty";
	public static final String MESSAGE_SORT = "File has been sorted";
	public static final String MESSAGE_SEARCH_NOT_FOUND = "Search term not found";
	public static final String MESSAGE_SEARCH_FOUND = "Search term \"%1$s\" found on line: %2$s";
	public static final int FIRST_WORD_LOCATION = 0;
	public static final int FILENAME_LOCATION = 0;

	private static Scanner sc = new Scanner(System.in);

	public static void main(String[] args) throws IOException{
		String fileName = args[FILENAME_LOCATION];
		File file = createFile(fileName);
		printString(MESSAGE_WELCOME + fileName + MESSAGE_FILE_READY);

		while(true){ 
			printString(MESSAGE_COMMAND);
			String userInput = sc.nextLine();
			String command = getFirstWord(userInput);
			String result = executeCommand(fileName, file, removeCommandFromUserInput(userInput), command);
			printString(result);
		}
	}

	// executes commands, returns results in a string
	public static String executeCommand(String fileName, File file, String userInput, String command) throws IOException {
		switch(command){
		case "add":
			addTextToFile(userInput, file, fileName);
			return ("added to " + fileName + ": \"" + userInput + "\"");
		case "display":
			return convertFileToString(file, fileName);
		case "delete":
			String content = deleteLine(Integer.valueOf(userInput), file); 
			return (MESSAGE_DELETE + fileName + ": \"" + content + "\"");
		case "clear":
			clearFile(file);
			return (MESSAGE_CLEAR + fileName);
		case "sort":
			return sortFile(file, fileName);
		case "search":
			return searchFile(fileName, userInput,  file);
		case "exit":
			System.exit(0);
		default: return("Command Error!");
		}
	}

	// sorts the file, checks for empty file
	private static String sortFile(File file, String fileName) {
		ArrayList<String> list;
		try {
			list = convertFileToArrayList(fileName, file);
			if (list.size()==0){
				return (fileName + MESSAGE_EMPTY);
			}
			else {
				return sortArrayList(list, fileName, file);
			}
		} catch (IOException e) {
			e.printStackTrace();
			return "error";
		}
	}

	// converts file contents to an arraylist and returns it 
	private static ArrayList<String> convertFileToArrayList(String fileName, File file) throws IOException {
		FileReader reader = new FileReader(file);
		BufferedReader bufferedReader = new BufferedReader(reader);
		String line;
		ArrayList<String> list = new ArrayList<String>();
		while ((line = bufferedReader.readLine()) != null) {
			list.add(line);
		}
		reader.close();
		return list;
	}

	// sorts arraylist and writes sorted list back to file
	private static String sortArrayList(ArrayList<String> list, String fileName, File file) throws IOException{
		clearFile(file);
		Collections.sort(list);
		for (int i=0;i<list.size();i++){
			addTextToFile(list.get(i), file, fileName);
		}
		return(MESSAGE_SORT);
	}

	// searches for string in the file
	private static String searchFile(String fileName, String word, File file) throws IOException{
		ArrayList<String> list = convertFileToArrayList(fileName, file);
		if (list.size()==0){
			return (fileName + MESSAGE_EMPTY);
		}else {
			int i;
			for (i=0;i<list.size();i++){
				if (list.get(i).contains(word)){
					break;
				}
			}
			if (list.size()==i){
				return (MESSAGE_SEARCH_NOT_FOUND);
			}else {
				return (String.format(MESSAGE_SEARCH_FOUND, word, Integer.toString(i + 1)));
			}
		}
	}

	// creates and returns a new file with specified file name
	public static File createFile(String fileName) {
		File file = new File("/users/Noob/desktop/" + fileName);
		try{
			if(!file.exists()){
				file.createNewFile();
			}
		} catch (IOException e){
			e.printStackTrace();
		}
		return file;
	}

	// prints string to console
	public static void printString(String string){
		System.out.println(string);
	}

	// returns first word in a string, normally the user's command
	public static String getFirstWord(String string){
		String firstWord = string.trim().split("\\s+")[FIRST_WORD_LOCATION];
		return firstWord;
	}

	// returns string with the first word removed
	public static String removeCommandFromUserInput(String string){
		return string.replace(getFirstWord(string),"").trim();
	}

	// adds a string to a new line in the text file
	public static void addTextToFile(String text, File file, String fileName) throws IOException{
		FileWriter writer = new FileWriter(file, true);
		writer.write(text);
		writer.write(System.lineSeparator());
		writer.close();
	}

	public static String convertFileToString(File file, String fileName) throws IOException{
		ArrayList<String> list = convertFileToArrayList(fileName, file);
		StringBuffer stringBuffer = new StringBuffer();
		for (int i=1;i<=list.size();i++){
			stringBuffer.append(""+ i + ". " +list.get(i-1));
			stringBuffer.append("\n");
		}
		return stringBuffer.toString();
	}
	
	// delete user specified line from file
	public static String deleteLine(Integer lineToDelete, File file) throws IOException{
		File tempFile = createFile("tempFile.txt");

		BufferedReader reader = new BufferedReader(new FileReader(file));
		FileWriter writer = new FileWriter(tempFile, true);

		String content = new String();
		String tempContent = new String();
		int currentLine = 1;
		while(true){
			tempContent = reader.readLine();
			if(tempContent == null){
				break;
			}
			if(currentLine != lineToDelete && tempContent != null){
				writer.write(tempContent);
				writer.write(System.lineSeparator());
			} else {
				content = tempContent;
			}
			currentLine++;
		}		
		reader.close();
		writer.close();

		if(!file.delete()){
			printString("Could not delete file");
		}

		if(!tempFile.renameTo(file)){
			printString("Could not rename file");
		}		
		return content;
	}

	// clears file contents
	public static void clearFile(File file) throws IOException{
		FileWriter writer = new FileWriter(file, false);
		writer.write("");
		writer.close();
	}
}
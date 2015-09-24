import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import org.junit.Test;

public class TextBuddyTest {
	TextBuddy TextBuddy = new TextBuddy();
	String fileName = "test.txt";
	@SuppressWarnings("static-access")
	File file = TextBuddy.createFile(fileName);

	@Test @SuppressWarnings("static-access")
	public void testAddItem() throws IOException{
		assertEquals("added to test.txt: \"Pikachu I choose you!\"", TextBuddy.executeCommand(fileName, file, "Pikachu I choose you!", "add"));
		assertEquals("added to test.txt: \"GOOO CHARIZARD\"", TextBuddy.executeCommand(fileName, file, "GOOO CHARIZARD", "add"));
	}

	@Test @SuppressWarnings("static-access")
	public void testDeleteItem() throws IOException{
		TextBuddy.executeCommand(fileName, file, "", "clear");
		assertEquals("added to test.txt: \"testing\"",TextBuddy.executeCommand(fileName, file, "testing", "add"));
		assertEquals("added to test.txt: \"123\"",TextBuddy.executeCommand(fileName, file, "123", "add"));
		assertEquals("deleted from test.txt: \"123\"",TextBuddy.executeCommand(fileName, file, "2", "delete"));
		TextBuddy.executeCommand(fileName, file, "", "clear");
	}

	@Test @SuppressWarnings("static-access")
	public void testDisplayAndClear() throws IOException{
		assertEquals("all content deleted from test.txt", TextBuddy.executeCommand(fileName, file, "", "clear"));
		TextBuddy.executeCommand(fileName, file, "CS2103", "add");
		assertEquals("1. CS2103\n",TextBuddy.executeCommand(fileName, file, "", "display"));
		TextBuddy.executeCommand(fileName, file, "", "clear");
		assertEquals("",TextBuddy.executeCommand(fileName, file, "", "display"));
	}

	@Test @SuppressWarnings("static-access")
	public void testSort() throws IOException{
		TextBuddy.executeCommand(fileName, file, "", "clear");
		assertEquals("test.txt is empty", TextBuddy.executeCommand(fileName, file, "", "sort"));
		TextBuddy.executeCommand(fileName, file, "zzzz", "add");
		TextBuddy.executeCommand(fileName, file, "asparagus", "add");
		TextBuddy.executeCommand(fileName, file, "melon", "add");
		assertEquals("File has been sorted", TextBuddy.executeCommand(fileName, file, "", "sort"));
		assertEquals("1. asparagus\n2. melon\n3. zzzz\n", TextBuddy.executeCommand(fileName, file, "", "display"));
		TextBuddy.executeCommand(fileName, file, "", "clear");
	}

	@Test @SuppressWarnings("static-access")
	public void testSearch() throws IOException{
		TextBuddy.executeCommand(fileName, file, "this melon is delicious", "add");
		TextBuddy.executeCommand(fileName, file, "cheese is awesome", "add");
		TextBuddy.executeCommand(fileName, file, "I am hungry while I'm coding", "add");
		assertEquals("Search term \"hungry\" found on line: 3", TextBuddy.executeCommand(fileName, file, "hungry", "search"));
	}
}
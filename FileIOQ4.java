import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class FileIOQ4 {

	public static void readFile(String fileName, LinkedListRefresh list) throws OutOfMemoryException {
		File file = new File(fileName); //file defined from parameter
		String name = ""; //String to hold a name
		
		try { //To catch if file is not there or if there is an error in reading it 
			FileReader fr = new FileReader(file); //Filereader declared and pointed to file
			BufferedReader br = new BufferedReader(fr); //Bufferedreader as it is quicker and makes more sense for this application
			String line; //to hold each line of the text doc
			
			while ((line = br.readLine()) != null) { //loop till end of file
				for (int i = 0; i < line.length(); i++) { 
					char c = line.charAt(i); //Go through each char in file
					// Process char
					if (c == '"') { // if the char is a " we can just skip it
						continue;
					} else {
						if (c == ',') { //, indicates the end of an entered name so we add the name to the linked list
							list.addFromFile(name);
							name = "";
						} else { // if none of the above are true then the char is added to the name currently stored
							name += c;
						}
					}
				}
			}
			list.addFromFile(name); //For final name
			br.close(); //close the buffered reader when done with it

		} catch (FileNotFoundException e) { //If the file is not found
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) { //If the reader fails
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

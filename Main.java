import java.util.Scanner;

public class Main {
	public static void main(String[] args) {
		boolean running = true; //To loop the menu
		String name; //for user inputs to the methods in menu
		String storedName; //For user inputs to the methods in menu
		Scanner input = new Scanner(System.in); //To take user input
		LinkedListRefresh list = new LinkedListRefresh(); //Initialise linked list
		try {
			FileIOQ4.readFile("names.txt", list);//populate with names from the text file
		} catch (OutOfMemoryException e) { //In case the file has too many names
			System.err.print(e);
		} 
		
		while(running == true) { //loop menu
			System.out.println("Would you like to:\n1.Add Before\n2.Add After\n3.Remove Before\n4.Remove After\n5.Search the List\n6.Print the List\n7.Exit"); 
			String choice = input.nextLine(); //users choice 
			
			switch(choice) {
			case "1":
				System.out.println("Please input a name to insert after"); 
				storedName = input.nextLine();
				System.out.println("Please input a name to insert"); 
				name = input.nextLine();
				try {
					list.insertBefore(storedName, name);
				} catch (OutOfMemoryException e) { //Catch any exceptions due to insufficient memory
					System.err.print(e);
				}
				break;
			case "2":
				System.out.println("Please input a name to insert before"); 
				storedName = input.nextLine();
				System.out.println("Please input a name to insert"); 
				name = input.nextLine();
				try {
					list.insertAfter(storedName, name);
				} catch (OutOfMemoryException e) { //Catch any exceptions due to insufficient memory
					System.err.print(e);
				}
				break;
			case "3":
				System.out.println("Please input a name to remove the name before it"); 
				storedName = input.nextLine();
				list.removeBefore(storedName);
				break;
			case "4":
				System.out.println("Please input a name to remove the name after it"); 
				storedName = input.nextLine();
				list.removeAfter(storedName);
				break;
			case "5":
				System.out.println("Please input a name to search for"); 
				name = input.nextLine();
				//Ternary operator to decide output on whether name is stored
				System.out.println((list.searchForAddress(name) == 0) ? "NAME NOT STORED" : "NAME STORED");
				break;
			case "6":
				list.printList();
				break;
			case "7":
				running = false; //To quit
				break;
			}
			
		}
		input.close(); //Close the scanner when it is no longer required
	}
}

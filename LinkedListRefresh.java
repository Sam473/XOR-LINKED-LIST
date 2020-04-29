
public class LinkedListRefresh {
	String[] memory = new String[12000]; //each node takes up 1 addr for data and 1 for XORptr
	int free; //pointer to next free space in memory
	int head = 2; //stores the last addr
	int tail = 2; //stores the first addr

	//Constructor to create the linked list
	public LinkedListRefresh() {
		memory[0] = "0"; //To stop errors with integer parsing if inserting after last element
		memory[2] = "0"; //First node
		memory[3] = "Head";
		free = 4; //Next free index of memory
		// put nodes in free list
		for (int i = 4; i < memory.length - 2; i += 2) {
			memory[i] = Integer.toString(i + 2); //Populate every address location with the next free index
		}
	}

	/**
	 * insert a name into the list after another name
	 * @param after - the element to insert after
	 * @param data - data to insert in linked list
	 * @throws OutOfMemoryException - if the memory array is full 
	 */
	public void insertAfter(String after, String data) throws OutOfMemoryException {
		int prevAddr = 0; //Hold the previous address for changing XOR pointers
		int currentAddr = tail; //Start from the start of the list to insert after an element
		
		while(true) { //Loop through all elements in the list
			if (memory[(currentAddr +1)].equals(after)) { //if the current index is the one we were searching for
				int node = alloc(); //Allocate some memory for the new node
				
				int nextAddr = addressXOR(Integer.parseInt(memory[currentAddr]), prevAddr); //to fix current addr XOR pointer
				int nextNextAddr = addressXOR(Integer.parseInt(memory[nextAddr]), currentAddr); //for fixing nextaddr XOR pointer
				
				memory[node + 1] = data; //put data in addr 2
				
				memory[node] = Integer.toString(addressXOR(currentAddr, nextAddr)); //set new node XOR pointer
				memory[currentAddr] = Integer.toString(addressXOR(prevAddr, node)); //set pointer before the node's XOR pointer
				memory[nextAddr] = Integer.toString(addressXOR(node, nextNextAddr)); //set the XOR pointer of the element after the new node
				
				if (currentAddr == head) { //if currentaddr is the head
					head = node; //element is the new head as it is inserted after
				}
				break; //element has been added
			} else if (currentAddr == head) {//if currentaddr is the head and not reached after
				//after is not stored therefore can't add element
				break;
			}
			//increment current and previous address
			int temp = currentAddr;
			currentAddr = addressXOR(Integer.parseInt(memory[currentAddr]), prevAddr);
			prevAddr = temp;
		}	
	}
	
	/**
	 * insert a name into the list before another name
	 * @param before - the element to insert before
	 * @param data - data to insert in linked list
	 * @throws OutOfMemoryException - if the memory array is full 
	 */
	public void insertBefore(String before , String data) throws OutOfMemoryException {//how do we tell where the list starts? Also re-comment
		int prevAddr = 0; //Hold the previous address for changing XOR pointers
		int currentAddr = head; //Start from the end of the list to insert before an element
		
		while(true) { //Loop through all elements in the list
			if (memory[(currentAddr +1)].equals(before)) { //if the current index is the one we were searching for
				int node = alloc(); //Allocate some memory for the new node
				
				int nextAddr = addressXOR(Integer.parseInt(memory[currentAddr]), prevAddr); //to fix current addr XOR pointer
				int nextNextAddr = addressXOR(Integer.parseInt(memory[nextAddr]), currentAddr); //for fixing nextaddr XOR pointer

				memory[node + 1] = data; //put data in addr 2
				
				memory[node] = Integer.toString(addressXOR(currentAddr, nextAddr)); //set new node XOR pointer
				memory[currentAddr] = Integer.toString(addressXOR(prevAddr, node)); //set pointer before the node's XOR pointer
				memory[nextAddr] = Integer.toString(addressXOR(node, nextNextAddr)); //set the XOR pointer of the element after the new node
				
				if (currentAddr == tail) { //current addr is the tail 
					tail = node; //element is the new tail as it is inserted after
				}
				break; //element has been added
			} else if (currentAddr == tail) { //if currentaddr is the tail and not reached after
				//after is not stored therefore can't add element
				break;
			}
			//increment current and previous address
			int temp = currentAddr;
			currentAddr = addressXOR(Integer.parseInt(memory[currentAddr]), prevAddr);
			prevAddr = temp;
		}	
	}
	
	/**
	 * prints the full linked list
	 */
	public void printList() {
		int addr = tail; //Start at the first element
		int prevAddr = 0; //to find the next addr
		
		do {
			System.out.format("Memory Location: %s, Data: %s\n", addr, memory[addr+1]); //print each element
			//increment current and previous address
			int temp = addr;
			addr = addressXOR(Integer.parseInt(memory[addr]), prevAddr);
			prevAddr = temp;  
		} while (prevAddr != head); //Loop while there are more nodes
	}
		
	/**
	 * Loop through linked list to search for the name
	 * @param name - the name to search for
	 * @return - the address of the name
	 */
	public int searchForAddress(String name) { //this should be called in inserts and deletions
		int searchAddr = tail; //Start at the first element
		int prevAddr = 0; //to find the next addr
		
		do {
			if (memory[searchAddr+1].equals(name)) { //If current element is the one it is looking for
				return searchAddr; //return the index
			} else { //if not found increment current and previous address
				int temp = searchAddr;
				searchAddr = addressXOR(Integer.parseInt(memory[searchAddr]), prevAddr);
				prevAddr = temp;
			}  
		} while (prevAddr != head); //Loop while there are more nodes
		return 0; //if not there, used in ternary expression in menu
	}
	
	/**
	 * Add names from the file included
	 * @param name - to add from the file
	 * @throws OutOfMemoryException - if the memory array is full 
	 */
	public void addFromFile(String name) throws OutOfMemoryException {
		int node = alloc(); //Allocate memory from array to new node
		
		memory[head] = Integer.toString(addressXOR(Integer.parseInt(memory[head]), node)); //change the prev addr to point to this
		memory[node] = Integer.toString(head); //this xor pointer is just last addr
		memory[node+1] = name; //add data for new node
		head = node; //Head = newest added element
	}
	
	/**
	 * To remove an element
	 * @param after - remove element after this element
	 */
	public void removeAfter(String after) {
		int prevAddr = 0; //For finding next address
		int currentAddr = tail; //Start from the start of the list to insert after an element
	
		try {
			while(true) {
				if (memory[(currentAddr +1)].equals(after)) { //if the current index is the one we were searching for

					int nextAddr = addressXOR(Integer.parseInt(memory[currentAddr]), prevAddr); //find the address of the element to be deleted
					int newNextAddr = addressXOR(Integer.parseInt(memory[nextAddr]), currentAddr); //find the address of the new element to be after the current one to be deleted
					int newNextNextAddr = addressXOR(Integer.parseInt(memory[newNextAddr]), nextAddr); //find the address of the element after that to amend XOR pointers
					
					free(nextAddr); //Add the deleted element's memory to the free list
					
					memory[currentAddr] = Integer.toString(addressXOR(newNextAddr, prevAddr)); //new next address' XOR pointer amended to remove deleted element
					memory[newNextAddr] = Integer.toString(addressXOR(currentAddr, newNextNextAddr)); //new next next address' XOR pointer amended to remove deleted element
					
					
					if (nextAddr == head) { //if deleted addr is the head
						head = currentAddr; //current element is the new head
					}
					break; //As element has been removed
				} else if (currentAddr == head) {//At the end of the list therefore element is not present
					break;
				}
				//increment current and previous addresses
				int temp = currentAddr;
				currentAddr = addressXOR(Integer.parseInt(memory[currentAddr]), prevAddr);
				prevAddr = temp;
			}
		} catch (NumberFormatException e) { //If you try to delete an element after the last element in the list
			System.out.println("This is the last item, there is no item to delete after this in the list");
		}
	}

	public void removeBefore(String before) {
		int prevAddr = 0; //For finding next address
		int currentAddr = head; //Start from the start of the list to insert after an element
		
		try {
			while(true) {
				if (memory[(currentAddr +1)].equals(before)) { //if the current index is the one we were searching for
					
					int nextAddr = addressXOR(Integer.parseInt(memory[currentAddr]), prevAddr); //find the address of the element to be deleted
					int newNextAddr = addressXOR(Integer.parseInt(memory[nextAddr]), currentAddr); //find the address of the new element to be after the current one to be deleted
					int newNextNextAddr = addressXOR(Integer.parseInt(memory[newNextAddr]), nextAddr); //find the address of the element after that to amend XOR pointers
					
					free(nextAddr); //Add the deleted element's memory to the free list
					
					memory[currentAddr] = Integer.toString(addressXOR(newNextAddr, prevAddr)); //new next address' XOR pointer amended to remove deleted element
					memory[newNextAddr] = Integer.toString(addressXOR(currentAddr, newNextNextAddr)); //new next next address' XOR pointer amended to remove deleted element
					
					
					if (nextAddr == tail) { //if deleted addr is the tail
						tail = currentAddr; //current element is the new tail
					}
					break; //As element has been removed
				} else if (currentAddr == tail) { //At the start of the list therefore element is not present
					break;
				}
				//increment current and previous addresses
				int temp = currentAddr;
				currentAddr = addressXOR(Integer.parseInt(memory[currentAddr]), prevAddr);
				prevAddr = temp;
			}
		} catch (NumberFormatException e) { //If you try to delete an element before the first element in the list
			System.out.println("This is the first item, there is no item to delete before this in the list");
		}
	}

	/**
	 * XOR two addresses for the pointer
	 * @param addr1 - first address to XOR
	 * @param addr2 - second address to XOR
	 * @return the XOR result
	 */
	public int addressXOR(int addr1, int addr2) {
		return (addr1 ^ addr2);
	}

	/**
	 * Allocates some memory from the array to a node
	 * @return - pointer to the allocated memory
	 * @throws OutOfMemoryException - if the memory array is full 
	 */
	private int alloc() throws OutOfMemoryException
	{
		//allocate 'memory' (2 array indexes)
		if (free == 0) { //If there is no available memory left in the array
			throw new OutOfMemoryException("System is out of memory, Cannot add any more items\n");
		}
		int allocatedMem = free; //put in next free location
		try {
			free = Integer.parseInt(memory[free]); //new free location = one I defined in constructor
		} catch (NumberFormatException e) { //This happens when memory has run out
			throw new OutOfMemoryException("System is out of memory, Cannot add any more items");
		} 
		return allocatedMem; //return the memory ptr
	}

	/**
	 * Method to free memory
	 * @param ptr - address to free
	 */
	private void free(int ptr)
	{
		memory[ptr] = Integer.toString(free); //populate data field with next free index
		free = ptr; //make the next free bit of memory equal to the memory freed
	}
}

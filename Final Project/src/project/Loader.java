package project;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/*
 * Create a class Loader. (If you want to try to write this part using streams, please give it a try.) 
 * Provide a method public static String load(MachineModel model, File file, int codeOffset, int memoryOffset). 
 * Give the method an int variable codeSize, initially 0. If either model or file are null, return null. 
 * Next open the file with try (Scanner input = new Scanner(file)) and first introduce a boolean called incode set to true. 
 * While input.hasNextLine(), read two lines line1, line2 from the file using nextLine() (which will be like the file 
 * the Assembler produced) and create a Scanner for this line: Scanner parser = new Scanner(line1 + " " + line2); 
 * Read the first int on the line (parser has the method nextInt()), there are three situations: (i) if you are incode 
 * and the first int is -1, simply change incode to false (that ends this iteration of the loop), (ii) if  you are 
 * incode but the first int is not -1, read the arg using parser.nextInt() again. Store the two values, which are opcode 
 * and arg in code at location codeOffset+codeSize using the setCode method in MachineModel, increment codeSize, (iii) if 
 * incode is false, the number that we read first is a location in memory so read the value using parser.nextInt(). 
 * Write the address and value to memory using model.setData(address+memoryOffset, value). The memory location MUST 
 * be offset by memoryOffset. Close the parser. That ends the while loop.
 * After the loop the return value is the String "" + codeSize. The return value in the catch for 
 * ArrayIndexOutOfBoundsException is "Array Index " + e.getMessage(), in the catch for NoSuchElementException it is 
 * "From Scanner: NoSuchElementException" and in the catch for FileNotFoundException, it is 
 * "File " + file.getName() + " Not Found"
 */

public class Loader {
	public static String load(MachineModel model, File file, int codeOffset, int memoryOffset) {
		int codeSize = 0;
		if(file == null|| model == null)
			return null;
		try (Scanner input = new Scanner(file)){
			boolean incode = true;
			while(input.hasNextLine()) {
				String line1 = input.nextLine();
				String line2 = input.nextLine();
				Scanner parser = new Scanner(line1 + " " + line2);
				int i = parser.nextInt();
				if(i == -1 && incode)
					incode = false;
				else if(i != -1 && incode) {
					int arg = parser.nextInt();
					model.setCode(codeOffset+codeSize, i , arg);
					codeSize++;
				}
				else if(!incode) {
					int val = parser.nextInt();
					model.setData(i+memoryOffset, val);
				}
				
			}input.close();
			return ""+codeSize;
		}
		catch(ArrayIndexOutOfBoundsException e) {
			return "Array Index " + e.getMessage();
		}
		catch(FileNotFoundException e) {
			return "File " + file.getName()+ " Not Found";
		}
	}
	public static void main(String[] args) {
		MachineModel model = new MachineModel();
		String s = Loader.load(model, new File("factorial8.pexe"),100,200);
		for(int i = 100; i < 100+Integer.parseInt(s); i++) {
			System.out.println(model.getOp(i));			
			System.out.println(model.getArg(i));			
		}
		for (int i = 200; i < 203; i++)
		System.out.println(i + " " + model.getData(i));
	}
}

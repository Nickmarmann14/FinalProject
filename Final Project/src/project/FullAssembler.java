package project;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class FullAssembler implements Assembler {

	ArrayList<String> instructionsRead = new ArrayList<>();
	
	@Override
	public int assemble(String inputFileName, String outputFileName, StringBuilder error) {
		try(Scanner sc = new Scanner(new File(inputFileName))){
			
		} catch(Exception e) {
			
		}
		return 0;
	}

}

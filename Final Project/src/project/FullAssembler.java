package project;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class FullAssembler implements Assembler {

	ArrayList<String> codeLines = new ArrayList<>();
	ArrayList<String> dataLines = new ArrayList<>();
	int lineCounter, errorLine = 0;
	boolean isCode = true;
	boolean blankFound = false;
	String nextLine;

	@Override
	public int assemble(String inputFileName, String outputFileName, StringBuilder error) {
		try(Scanner sc = new Scanner(new File(inputFileName))){
			
			while(sc.hasNextLine()) { // This is the first pass which divides code/data and finds whole code errors. The next loop should discover errors with individual lines of code.
				lineCounter++;
				nextLine = sc.nextLine();
				if(nextLine.trim().length()==0){
					if(!(blankFound)) {
						error.append("\nBlank Line Error on line"+ lineCounter); // Error 1
						errorLine = lineCounter;
					}
					blankFound = true;
				}
				if(nextLine.charAt(0)==' ' || nextLine.charAt(0)=='\t') {
					nextLine = nextLine.trim();
					error.append("\nBlank Start Error on line "+lineCounter); // Error 2
					errorLine = lineCounter;
				}
				if(isCode) {
					if(nextLine.trim().equals("DATA")) {
						if(nextLine.trim().toUpperCase().equals("DATA")) {
							error.append("\n \"DATA\" must be uppercase! Line: "+lineCounter); // Part of 3
							errorLine = lineCounter;
						}
						isCode = false;
					}
					else {
						codeLines.add(nextLine);
					}
				} else {
					if(nextLine.equals("DATA")) {
						error.append("\nYou cannot have two lines with DATA: line "+lineCounter); // Error 3
						errorLine = lineCounter;
					} else {
						dataLines.add(nextLine);
					}
				}
			}
			lineCounter = 0; // Resetting the line counter for the second pass. Be careful about the line number in the second pass. For example, account for the lack of "DATA" in the line count.
			
		} catch(FileNotFoundException e) {
			error.append("\nUnable to open the source file");
			return -1;
		}
		return errorLine;
	}

}

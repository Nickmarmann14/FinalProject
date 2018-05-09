package project;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Files;
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
					if(nextLine.trim().toUpperCase().equals("DATA")) {
						if(!(nextLine.trim().equals("DATA"))) {
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
		} catch(FileNotFoundException e) {
			error.append("\nUnable to open the source file");
			return -1;
		}
		lineCounter = 1; // Resetting the line counter for the second pass. Be careful about the line number in the second pass. For example, account for the lack of "DATA" in the line count.
		isCode = true;
		String s;
		for(int i = 0; i< codeLines.size()+dataLines.size(); i++) {
			try {
				if(i<codeLines.size())
					s = codeLines.get(i);
				else 
					s = dataLines.get(i);
				String[] parts = s.trim().split("\\s+");
				String word = parts[0].toUpperCase();
				if(!parts[0].equals(word)) {
					error.append("\nError on line " + (i+1) + ": mnemonic must be upper case");
					errorLine = i+1;
				}
				if(!InstrMap.toCode.containsKey(parts[0])) {
					error.append("\nError on line " + (i+1) + ": illegal mnemonic");
					errorLine = i+1;
				}
				if(Assembler.noArgument.contains(parts[0])) {
					if(parts.length!=1) {
						error.append("\nError on line " + (i+1) + ": this mnemonic cannot take arguments");
						errorLine = i+1;
					}
				}
				else {
					if(parts.length<2) {
						error.append("\nError on line " + (i+1) + ": this mnemonic is missing an argument");
						errorLine = i+1;
					}
					if(parts.length>2) {
						error.append("\nError on line " + (i+1) + ": this mnemonic has too many arguments");
						errorLine = i+1;
					}
				}
				int arg = Integer.parseInt(parts[1],16);
				int address = Integer.parseInt(parts[0],16);
				int value = Integer.parseInt(parts[1],16);
			} 
			catch(NumberFormatException e) {
				error.append("\nError on line " + (i+1) + ": argument is not a hex number");
				errorLine = i+1;				
			}	
		}
		return errorLine;
	}

}

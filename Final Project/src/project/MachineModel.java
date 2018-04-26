package project;

import java.util.Map;
import java.util.TreeMap;

public class MachineModel{

	public static final Map<Integer, Instruction>INSTRUCTIONS = new TreeMap<Integer, Instruction>();
	private cpu CPU = new cpu();
	private Memory memory = new Memory();
	private HaltCallback callback;
	private boolean withGUI;
	private Job[] jobs = new Job[2]; // Jobs will hold the state of the current program that's executing.
	private Job currentJob; 

	public MachineModel() {
		this(false, null);
	}
	public MachineModel(boolean GUI, HaltCallback cb){
		withGUI = GUI;
		callback = cb;
		//INSTRUCTION_MAP entry for "NOP"
		INSTRUCTIONS.put(0x0, arg -> {
			CPU.incrementIP(1);
		});
		//INSTRUCTION_MAP entry for "LODI"
		INSTRUCTIONS.put(0x1, arg -> {
			CPU.accumulator = arg;
			CPU.incrementIP(1);
		});
		//INSTRUCTION_MAP entry for "LOD"
		INSTRUCTIONS.put(0x2, arg -> {
			int arg1 = memory.getData(CPU.memoryBase+arg);
			CPU.accumulator = arg1;
			CPU.incrementIP(1);
		});
		//INSTRUCTION_MAP entry for "LODN"
		INSTRUCTIONS.put(0x3, arg -> {
			int arg1 = memory.getData(CPU.memoryBase+arg);
			int arg2 = memory.getData(CPU.memoryBase+arg1);
			CPU.accumulator = arg2;
			CPU.incrementIP(1);
		});
		//INSTRUCTION_MAP entry for "STO"
		INSTRUCTIONS.put(0x4,arg  -> {
			memory.setData(CPU.memoryBase+arg, CPU.accumulator);
			CPU.incrementIP(1);
		});
		//INSTRUCTION_MAP entry for "STON"
		INSTRUCTIONS.put(0x5,arg ->  {
			int arg1 = memory.getData(CPU.memoryBase+arg);
			memory.setData(CPU.memoryBase+arg1, CPU.accumulator);
			CPU.incrementIP(1);
		});
		//    //INSTRUCTION_MAP entry for "JMPR"
		//    INSTRUCTIONS.put(0x6 -> arg {
		//  	
		//    });
		//INSTRUCTION_MAP entry for "ADDI"
		INSTRUCTIONS.put(0xC, arg -> {
			CPU.accumulator += arg;
			CPU.incrementIP(1);
		});
		//INSTRUCTION_MAP entry for "ADD"
		INSTRUCTIONS.put(0xD, arg -> {
			int arg1 = memory.getData(CPU.memoryBase+arg);
			CPU.accumulator += arg1;
			CPU.incrementIP(1);
		});
		//INSTRUCTION_MAP entry for "ADDN"
		INSTRUCTIONS.put(0xE, arg -> {
			int arg1 = memory.getData(CPU.memoryBase+arg);
			int arg2 = memory.getData(CPU.memoryBase+arg1);
			CPU.accumulator += arg2;
			CPU.incrementIP(1);
		});
		//INSTRUCTION_MAP entry for "SUBI"
		INSTRUCTIONS.put(0xF, arg -> {
			CPU.accumulator -= arg;
			CPU.incrementIP(1);
		});
		//INSTRUCTION_MAP entry for "SUB"
		INSTRUCTIONS.put(0x10,arg -> {
			int arg1 = memory.getData(CPU.memoryBase+arg);
			CPU.accumulator-=arg1;
			CPU.incrementIP(1);
		});
		//INSTRUCTION_MAP entry for "SUBN"
		INSTRUCTIONS.put(0x11,arg -> {
			int arg1 = memory.getData(CPU.memoryBase+arg);
			int arg2 = memory.getData(CPU.memoryBase+arg1);
			CPU.accumulator-=arg2;
			CPU.incrementIP(1);
		});
		//INSTRUCTION_MAP entry for "MULI"
		INSTRUCTIONS.put(0x12, arg -> {
			CPU.accumulator *= arg;
			CPU.incrementIP(1);
		});
		//INSTRUCTION_MAP entry for "MUL"
		INSTRUCTIONS.put(0x13, arg -> {
			int arg1 = memory.getData(CPU.memoryBase+arg);
			CPU.accumulator *= arg1;
			CPU.incrementIP(1);
		});
		//INSTRUCTIONS_MAP entry for "MULN"
		INSTRUCTIONS.put(0x14, arg -> {
			int arg1 = memory.getData(CPU.memoryBase+arg);
			int arg2 = memory.getData(CPU.memoryBase+arg1);
			CPU.accumulator *= arg2;
			CPU.incrementIP(1);
		});
		//INSTRUCTIONS_MAP entry for "DIVI"
		INSTRUCTIONS.put(0x15, arg -> {
			if(arg == 0)
				throw new DivideByZeroException("Cannot divide by zero");
			CPU.accumulator /= arg;
			CPU.incrementIP(1);
		});
		//INSTRUCTIONS_MAP entry for "DIV"
		INSTRUCTIONS.put(0x16, arg -> {
			if(arg == 0)
				throw new DivideByZeroException("Cannot divide by zero");
			int arg1 = memory.getData(CPU.memoryBase+arg);
			CPU.accumulator /= arg1;
			CPU.incrementIP(1);
		});
		//INSTRUCTIONS_MAP entry for "DIVN"
		INSTRUCTIONS.put(0x17, arg -> {
			if(arg == 0)
				throw new DivideByZeroException("Cannot divide by zero");
			int arg1 = memory.getData(CPU.memoryBase+arg);
			int arg2 = memory.getData(CPU.memoryBase+arg1);
			CPU.accumulator /= arg2;
			CPU.incrementIP(1);
		});
		
	}
	public Job getCurrentJob() {
		return currentJob;
	}
	public void setJob(int i) {
		if(i!=0 || i!=1){
			throw new IllegalArgumentException("Input must be either 1 or 0!");
		}
		currentJob = jobs[i];
		CPU.accumulator = currentJob.getCurrentAcc();
		CPU.instructionPointer = currentJob.getCurrentIP();
		CPU.memoryBase = currentJob.getStartmemoryIndex();
	}
	
	public void setCurrentAcc(){
		currentJob.setCurrentAcc(CPU.accumulator);
	}
	public void setCurrentIP(){
		currentJob.setCurrentIP(CPU.instructionPointer);
	}
	private class cpu{
		private int accumulator;
		private int instructionPointer;
		private int memoryBase;

		public void incrementIP(int val){
			instructionPointer+=val;
		}
	}
}

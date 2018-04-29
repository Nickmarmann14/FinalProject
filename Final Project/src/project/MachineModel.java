package project;

import java.util.Map;
import java.util.TreeMap;

import projectview.States;

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
		
		//The piazza says there will need to be modifications for JumpI and Jmpzi, so if you're getting errors there don't worry about them just yet.
		
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
		
		jobs[0] = new Job();
		jobs[1] = new Job();
		currentJob = jobs[0];
		jobs[0].setStartcodeIndex(0);
		jobs[0].setStartmemoryIndex(0);
		jobs[0].setCurrentState(States.NOTHING_LOADED);
		jobs[1].setStartcodeIndex(Memory.CODE_MAX/4);
		jobs[1].setStartmemoryIndex(Memory.DATA_SIZE/2);
		jobs[1].setCurrentState(States.NOTHING_LOADED);
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
	
	public Instruction get(int key) {
		return INSTRUCTIONS.get(key);
	}
	
	public int getInstructionPointer() {
		return CPU.instructionPointer;
	}
	
	public int getAccumulator() {
		return CPU.accumulator;
	}
	
	public int getMemoryBase() {
		return CPU.memoryBase;
	}
	
	public void setInstructionPointer(int i) {
		CPU.instructionPointer = i;
	}
	
	public void setAccumulator(int i) {
		CPU.accumulator = i;
	}
	
	public void setMemoryBase(int i) {
		CPU.memoryBase = i;
	}
	
	public void clearJob() {
		memory.clearData(currentJob.getStartmemoryIndex(), currentJob.getStartmemoryIndex()+Memory.DATA_SIZE/2);
		memory.clear(currentJob.getStartmemoryIndex(), currentJob.getStartcodeIndex()+currentJob.getCodeSize());
		CPU.accumulator = 0;
		CPU.instructionPointer = currentJob.getStartcodeIndex();
		currentJob.reset();
	}
	
	public void step() {
		try {
			if(CPU.instructionPointer<currentJob.getStartcodeIndex() || CPU.instructionPointer >= currentJob.getStartcodeIndex() + currentJob.getCodeSize()) {
				throw new CodeAccessException("The code you are trying to access is out of range!");
			}
			
			get(memory.getOp(CPU.instructionPointer)).execute(memory.getArg(CPU.instructionPointer));
			
			
		} catch(Exception e) {
			callback.halt();
			throw e;
		}
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

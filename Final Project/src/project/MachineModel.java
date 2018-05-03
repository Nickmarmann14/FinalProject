package project;

import java.util.Map;
import java.util.TreeMap;

import projectview.States;

public class MachineModel{

	public static final Map<Integer, Instruction>INSTRUCTIONS = new TreeMap<Integer, Instruction>();
	private CPU cpu = new CPU();
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
			cpu.incrementIP();
		});
		//INSTRUCTION_MAP entry for "LODI"
		INSTRUCTIONS.put(0x1, arg -> {
			cpu.accumulator = arg;
			cpu.incrementIP();
		});
		//INSTRUCTION_MAP entry for "LOD"
		INSTRUCTIONS.put(0x2, arg -> {
			int arg1 = memory.getData(cpu.memoryBase+arg);
			cpu.accumulator = arg1;
			cpu.incrementIP();
		});
		//INSTRUCTION_MAP entry for "LODN"
		INSTRUCTIONS.put(0x3, arg -> {
			int arg1 = memory.getData(cpu.memoryBase+arg);
			int arg2 = memory.getData(cpu.memoryBase+arg1);
			cpu.accumulator = arg2;
			cpu.incrementIP();
		});
		//INSTRUCTION_MAP entry for "STO"
		INSTRUCTIONS.put(0x4,arg  -> {
			memory.setData(cpu.memoryBase+arg, cpu.accumulator);
			cpu.incrementIP();
		});
		//INSTRUCTION_MAP entry for "STON"
		INSTRUCTIONS.put(0x5,arg ->  {
			int arg1 = memory.getData(cpu.memoryBase+arg);
			memory.setData(cpu.memoryBase+arg1, cpu.accumulator);
			cpu.incrementIP();
		});
		//INSTRUCTION_MAP entry for "JMPR"
		INSTRUCTIONS.put(0x6, arg -> {
			cpu.instructionPointer=+arg;
		});
		//INSTRUCTION_MAP entry for "JUMP"
		INSTRUCTIONS.put(0x7, arg -> {
			cpu.instructionPointer=+(cpu.memoryBase+arg);
		});
		//INSTRUCTION_MAP entry for "JUMPI"
		INSTRUCTIONS.put(0x8, arg -> {
			cpu.instructionPointer= currentJob.getStartcodeIndex() + arg;

		});
		//INSTRUCTION_MAP entry for "JMPZR"
		INSTRUCTIONS.put(0x9, arg -> {
			cpu.instructionPointer=+arg;
			if(cpu.accumulator!=0)
				cpu.incrementIP();
		});
		//INSTRUCTION_MAP entry for "JMPZ"
		INSTRUCTIONS.put(0xA, arg -> {
			cpu.instructionPointer=+(cpu.memoryBase+arg);
			if(cpu.accumulator!=0)
				cpu.incrementIP();
		});
		//INSTRUCTION_MAP entry for "JMPZI"
		INSTRUCTIONS.put(0xB, arg -> {
			if(cpu.accumulator!=0) {	
				cpu.incrementIP();
			}
			else {
				cpu.instructionPointer = currentJob.getStartcodeIndex() + arg;
			}
		});
		//The piazza says there will need to be modifications for JumpI and Jmpzi, so if you're getting errors there don't worry about them just yet.
		//INSTRUCTION_MAP entry for "ADDI"
		INSTRUCTIONS.put(0xC, arg -> {
			cpu.accumulator += arg;
			cpu.incrementIP();
		});
		//INSTRUCTION_MAP entry for "ADD"
		INSTRUCTIONS.put(0xD, arg -> {
			int arg1 = memory.getData(cpu.memoryBase+arg);
			cpu.accumulator += arg1;
			cpu.incrementIP();
		});
		//INSTRUCTION_MAP entry for "ADDN"
		INSTRUCTIONS.put(0xE, arg -> {
			int arg1 = memory.getData(cpu.memoryBase+arg);
			int arg2 = memory.getData(cpu.memoryBase+arg1);
			cpu.accumulator += arg2;
			cpu.incrementIP();
		});
		//INSTRUCTION_MAP entry for "SUBI"
		INSTRUCTIONS.put(0xF, arg -> {
			cpu.accumulator -= arg;
			cpu.incrementIP();
		});
		//INSTRUCTION_MAP entry for "SUB"
		INSTRUCTIONS.put(0x10,arg -> {
			int arg1 = memory.getData(cpu.memoryBase+arg);
			cpu.accumulator-=arg1;
			cpu.incrementIP();
		});
		//INSTRUCTION_MAP entry for "SUBN"
		INSTRUCTIONS.put(0x11,arg -> {
			int arg1 = memory.getData(cpu.memoryBase+arg);
			int arg2 = memory.getData(cpu.memoryBase+arg1);
			cpu.accumulator-=arg2;
			cpu.incrementIP();
		});
		//INSTRUCTION_MAP entry for "MULI"
		INSTRUCTIONS.put(0x12, arg -> {
			cpu.accumulator *= arg;
			cpu.incrementIP();
		});
		//INSTRUCTION_MAP entry for "MUL"
		INSTRUCTIONS.put(0x13, arg -> {
			int arg1 = memory.getData(cpu.memoryBase+arg);
			cpu.accumulator *= arg1;
			cpu.incrementIP();
		});
		//INSTRUCTIONS_MAP entry for "MULN"
		INSTRUCTIONS.put(0x14, arg -> {
			int arg1 = memory.getData(cpu.memoryBase+arg);
			int arg2 = memory.getData(cpu.memoryBase+arg1);
			cpu.accumulator *= arg2;
			cpu.incrementIP();
		});
		//INSTRUCTIONS_MAP entry for "DIVI"
		INSTRUCTIONS.put(0x15, arg -> {
			if(arg == 0)
				throw new DivideByZeroException("Cannot divide by zero");
			cpu.accumulator /= arg;
			cpu.incrementIP();
		});
		//INSTRUCTIONS_MAP entry for "DIV"
		INSTRUCTIONS.put(0x16, arg -> {
			if(arg == 0)
				throw new DivideByZeroException("Cannot divide by zero");
			int arg1 = memory.getData(cpu.memoryBase+arg);
			cpu.accumulator /= arg1;
			cpu.incrementIP();
		});
		//INSTRUCTIONS_MAP entry for "DIVN"
		INSTRUCTIONS.put(0x17, arg -> {
			if(arg == 0)
				throw new DivideByZeroException("Cannot divide by zero");
			int arg1 = memory.getData(cpu.memoryBase+arg);
			int arg2 = memory.getData(cpu.memoryBase+arg1);
			cpu.accumulator /= arg2;
			cpu.incrementIP();
		});
		//INSTRUCTIONS_MAP entry for "ANDI"
		INSTRUCTIONS.put(0x18, arg -> {
			if(cpu.accumulator!=0&&arg!=0)
				cpu.accumulator = 1;
			else 
				cpu.accumulator = 0;
			cpu.incrementIP();
		});
		//INSTRUCTIONS_MAP entry for "AND"
		INSTRUCTIONS.put(0x19, arg -> {
			if(cpu.accumulator!=0&&memory.getData(cpu.memoryBase+arg)!=0)
				cpu.accumulator = 1;
			else
				cpu.accumulator = 0;
			cpu.incrementIP();
		});
		//INSTRUCTIONS_MAP entry for "NOT"
		INSTRUCTIONS.put(0x1A, arg -> {
			if(cpu.accumulator==0)
				cpu.accumulator = 1;
			else
				cpu.accumulator = 0;
			cpu.incrementIP();
		});
		//INSTRUCTIONS_MAP entry for "CMPL"
		INSTRUCTIONS.put(0x1B, arg -> {
			if(memory.getData(cpu.memoryBase+arg)<0)
				cpu.accumulator = 1;
			else
				cpu.accumulator = 0;
			cpu.incrementIP();
		});
		//INSTRUCTIONS_MAP entry for "CMPZ"
		INSTRUCTIONS.put(0x1C, arg -> {
			if(memory.getData(cpu.memoryBase+arg)==0)
				cpu.accumulator = 1;
			else
				cpu.accumulator = 0;
			cpu.incrementIP();
		});
		//INSTRUCTIONS_MAP entry for "HALT"
		INSTRUCTIONS.put(0x1F, arg ->{
			callback.halt();
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
	public int[] getData() {
		return memory.getAllData();
	}
	public int getData(int index) {
		return memory.getData(index);
	}
	public void setData(int index, int value) {
		memory.setData(index, value);
	}

	public int getChangedIndex() {
		return memory.getChangedIndex();
	}

	public int[] getCode() {
		return memory.getCode();
	}
	public void setCode(int index, int op, int arg) {
		memory.setCode(index, op, arg);
	}
	public int getOp(int i) {
		return memory.getOp(i);
	}
	public int getArg(int i) {
		return memory.getArg(i);
	}

	public Job getCurrentJob() {
		return currentJob;
	}

	public States getCurrentState() {
		return currentJob.getCurrentState();
	}

	public void setCurrentState(States currentState) {
		currentJob.setCurrentState(currentState);
	}

	public void setJob(int i) {
		if(i!=0 || i!=1){
			throw new IllegalArgumentException("Input must be either 1 or 0!");
		}
		currentJob = jobs[i];
		cpu.accumulator = currentJob.getCurrentAcc();
		cpu.instructionPointer = currentJob.getCurrentIP();
		cpu.memoryBase = currentJob.getStartmemoryIndex();
	}

	public void setCurrentAcc(){
		currentJob.setCurrentAcc(cpu.accumulator);
	}
	public void setCurrentIP(){
		currentJob.setCurrentIP(cpu.instructionPointer);
	}

	public Instruction get(int key) {
		return INSTRUCTIONS.get(key);
	}

	public int getInstructionPointer() {
		return cpu.instructionPointer;
	}

	public int getAccumulator() {
		return cpu.accumulator;
	}

	public int getMemoryBase() {
		return cpu.memoryBase;
	}

	public void setInstructionPointer(int i) {
		cpu.instructionPointer = i;
	}

	public void setAccumulator(int i) {
		cpu.accumulator = i;
	}

	public void setMemoryBase(int i) {
		cpu.memoryBase = i;
	}

	public void clearJob() {
		memory.clearData(currentJob.getStartmemoryIndex(), currentJob.getStartmemoryIndex()+Memory.DATA_SIZE/2);
		memory.clear(currentJob.getStartmemoryIndex(), currentJob.getStartcodeIndex()+currentJob.getCodeSize());
		cpu.accumulator = 0;
		cpu.instructionPointer = currentJob.getStartcodeIndex();
		currentJob.reset();
	}

	public void step() {
		try {
			if(cpu.instructionPointer<currentJob.getStartcodeIndex() || cpu.instructionPointer >= currentJob.getStartcodeIndex() + currentJob.getCodeSize()) {
				throw new CodeAccessException("The code you are trying to access is out of range!");
			}

			get(memory.getOp(cpu.instructionPointer)).execute(memory.getArg(cpu.instructionPointer));


		} catch(Exception e) {
			callback.halt();
			throw e;
		}
	}

	private class CPU{
		private int accumulator;
		private int instructionPointer;
		private int memoryBase;

		public void incrementIP(int val){
			instructionPointer+=val;
		}
		public void incrementIP() {
			instructionPointer++;
		}
	}
}

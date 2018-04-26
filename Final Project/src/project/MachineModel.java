package project;

import java.util.TreeMap

public class MachineModel{
  
  public static final Map<Integer, Instruction>INSTRUSTIONS = new TreeMap<Integer, Instruction>();
  private CPU cpu = new CPU();
  private Memory memory = new Memory();
  private HaltCallback callback;
  private boolean withGUI;
 
  public MachineModel() {
	  this(false, null);
  }
  public MachineModel(boolean GUI, HaltCallback cb){
    withGui = GUI;
    callback = cb;
    //INSTRUCTION_MAP entry for "NOP"
    INSTRUCTIONS.put(0x0, arg -> {
    	cpu.incrementIP(1);
    });
    //INSTRUCTION_MAP entry for "LODI"
    INSTRUCTIONS.put(0x1, arg -> {
    	cpu.accumulator = arg;
	cpu.incrementIP(1);
    });
    //INSTRUCTION_MAP entry for "LOD"
    INSTRUCTIONS.put(0x2, arg -> {
    	int ar1 = memory.getData(cpu.memoryBase+arg);
	cpu.accumulator = arg1;
	cpu.incrementIP(1);
    });
    //INSTRUCTION_MAP entry for "LODN"
    INSTRUCTIONS.put(0x3, arg -> {
    	int arg1 = memory.getData(cpu.memoryBase+arg);
	int arg2 = memory.getData(cpu.memoryBase+arg1);
	cpu.accumulator = arg2;
	cpu.incrementIP(1);
    });
    //INSTRUCTION_MAP entry for "STO"
    INSTRUCTIONS.put(0x4 -> arg {
    	memory.setData(cpu.memoryBase+arg, cpu.accumulator);
	cpu.incrementIP(1);
    });
    //INSTRUCTION_MAP entry for "STON"
    INSTRUCTIONS.put(0x5 -> arg {
    	int arg1 = memory.getData(cpu.memoryBase+arg);
	int arg2 = memory.setData(cpu.memoryBase+arg1, cpu.accumulator);
	cpu.incrementIP(1);
    });
    //INSTRUCTION_MAP entry for "JMPR"
    INSTRUCTIONS.put(0x6 -> arg {
  	
    });
    //INSTRUCTION_MAP entry for "ADDI"
    INSTRUCTIONS.put(0xC, arg -> {
      cpu.accumulator += arg;
      cpu.incrementIP(1);
    });
    //INSTRUCTION_MAP entry for "ADD"
    INSTRUCTIONS.put(0xD, arg -> {
       int arg1 = memory.getData(cpu.memoryBase+arg);
       cpu.accumulator += arg1;
       cpu.incrementIP(1);
    });
    //INSTRUCTION_MAP entry for "ADDN"
    INSTRUCTIONS.put(0xE, arg -> {
        int arg1 = memory.getData(cpu.memoryBase+arg);
        int arg2 = memory.getData(cpu.memoryBase+arg1);
        cpu.accumulator += arg2;
        cpu.incrementIP(1);
    });
    //INSTRUCTION_MAP entry for "SUBI"
    INSTRUCTIONS.put(0xF, arg -> {
        cpu.accumulator -= arg;
        cpu.incrementIP(1);
    });
    //INSTRUCTION_MAP entry for "SUB"
    INSTRUCTIONS.put(0x10,arg -> {
      int arg1 = memory.getData(cpu.memoryBase+arg);
      cpu.accumulator-=arg1;
      cpu.incrementIP(1);
    });
    //INSTRUCTION_MAP entry for "SUBN"
    INSTRUCTIONS.put(0x11,arg -> {
      int arg1 = memory.getData(cpu.memoryBase+arg);
      int arg2 = memory.getData(cpu.memoryBase+arg1);
      cpu.accumulator-=arg2;
      cpu.incrementIP(1);
    });
    //INSTRUCTION_MAP entry for "MULI"
    INSTRUCTIONS.put(0x12, arg -> {
      cpu.accumulator *= arg;
      cpu.incrementIP(1);
    });
    //INSTRUCTION_MAP entry for "MUL"
    INSTRUCTIONS.put(0x13, arg -> {
      int arg1 = memory.geData(cpu.memoryBase+arg);
      cpu.accumulator *= arg1;
      cpu.incrementIP(1);
    });
    //INSTRUCTIONS_MAP entry for "MULN"
    INSTRUCTIONS.put(0x14, arg -> {
    	int arg1 = memory.getData(cpu.memoryBase+arg);
	int arg2 = memory.getData(cpu.memoryBase+arg1);
	cpu.accumulator *= arg2;
	cpu.increment(1);
    });
    //INSTRUCTIONS_MAP entry for "DIVI"
    INSTRUCTIONS.put(0x15, arg -> {
    	if(arg == 0)
		throw new DivideByZeroException("Cannot divide by zero");
	cpu.accumulator /= arg;
	cpu.increment(1);
    });
    //INSTRUCTIONS_MAP entry for "DIV"
    INSTRUCTIONS.put(0x16, arg -> {
    	if(arg == 0)
		throw new DivideByZeroException("Cannot divide by zero");
	int arg1 = memory.getData(cpu.memoryBase+arg);
	cpu.accumulator /= arg1;
	cpu.increment(1);
    });
    //INSTRUCTIONS_MAP entry for "DIVN"
    INSTRUCTIONS.put(0x17, arg -> {
    	if(arg == 0)
		throw new DivideByZeroException("Cannot divide by zero");
	int arg1 = memory.getData(cpu.memoryBase+arg);
	int arg2 = memory.getData(cpu.memoryBase+arg1);
	cpu.accumulator /= arg2;
	cpu.increment(1);
    });
  }
  private class cpu{
    private int accumulator;
    private int instructionPointer;
    private int memoryBase;
    
    public void incrementIP(int val){
      instructionPointer+=val;
  }
}

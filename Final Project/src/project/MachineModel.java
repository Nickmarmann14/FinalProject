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
      int arg1 = memory.getData(cpu.memoryBase-arg);
      cpu.accumulator-=arg;
      cpu.incrementIP(1);
    });
  }
  private class cpu{
    private int accuulator;
    private int instructionPointer;
    private int memoryBase;
    
    public void incrementIP(int val){
      instructionPointer+=val;
  }
}

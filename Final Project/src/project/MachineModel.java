package project;

public class MachineModel{
  private class cpu{
    private int accuulator;
    private int instructionPointer;
    private int memoryBase;
    
    public void incrementIP(int val){
      instructionPointer+=val;
  }
}

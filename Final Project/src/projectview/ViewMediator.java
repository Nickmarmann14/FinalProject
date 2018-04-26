package projectview;

import java.util.Observable;

import javax.swing.JFrame;

import project.MachineModel;

public class ViewMediator extends Observable {
	/*
	 * This will be more useful later on it's just a shell right now
	 */
	private MachineModel model;
	private JFrame frame;
	
	public MachineModel getModel() {
		return model;
	}

	public void setModel(MachineModel model) {
		this.model = model;
	}

	public JFrame getFrame() {
		return frame;
	}

	public void step(){
	}
}

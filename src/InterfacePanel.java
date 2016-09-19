import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;


public class InterfacePanel extends JPanel{
	
	private static final long serialVersionUID = -5100592217993772648L;

	public interface ControlCallback{
		void newStateClicked();
		void newActionClicked();
	}

	private ControlCallback mListener;
	
	public InterfacePanel(int width, int height, ControlCallback listener){
		this.setSize(width, height);
		this.mListener = listener;
		JButton newStateButton = new JButton();
		newStateButton.setSize(width/2, height/2);
		newStateButton.setText("New State");
		newStateButton.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent e) {
				System.out.println("New State Created");
				mListener.newStateClicked();
			}
			
		});
		
		JButton createActionButton = new JButton();
		createActionButton.setSize(width/2, 20);
		createActionButton.setText("Create an Action");
		createActionButton.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent e) {
				System.out.println("New Action Created");
				mListener.newActionClicked();
			}
			
		});
		this.add(newStateButton);
		this.add(createActionButton);
	}
	
}

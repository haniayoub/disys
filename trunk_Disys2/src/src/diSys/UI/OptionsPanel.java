package diSys.UI;

@SuppressWarnings("serial")
public class OptionsPanel extends javax.swing.JPanel {

	/**
	* Auto-generated main method to display this 
	* JPanel inside a new JFrame.
	
	public static void main(String[] args) {
		JFrame frame = new JFrame();
		frame.getContentPane().add(new OptionsPanel());
		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}
		*/

	public OptionsPanel() {
		super();
		initGUI();
	}
	
	private void initGUI() {
		try {
			this.setPreferredSize(new java.awt.Dimension(440, 333));
			this.setLayout(null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

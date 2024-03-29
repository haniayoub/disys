package diSys.UI;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import javax.swing.WindowConstants;
import javax.swing.SwingUtilities;

@SuppressWarnings("serial")
public class SystemManegerLogTrace extends javax.swing.JFrame {
	private JPanel jPanel1;
	private JScrollPane jScrollPane1;
	private JTextArea LogTraceArea;

	{
		//Set Look & Feel
		try {
			javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
		} catch(Exception e) {
			e.printStackTrace();
		}
	}


	/**
	* Auto-generated main method to display this JFrame
	*/
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				SystemManegerLogTrace inst = new SystemManegerLogTrace();
				inst.setLocationRelativeTo(null);
				inst.setVisible(true);
			}
		});
	}
	
	public SystemManegerLogTrace() {
		super();
		initGUI();
	}
	
	private void initGUI() {
		try {
			setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			this.setTitle("System manager trace");
			this.setResizable(false);
			{
				jPanel1 = new JPanel();
				getContentPane().add(jPanel1, BorderLayout.CENTER);
				jPanel1.setLayout(null);
				jPanel1.setPreferredSize(new java.awt.Dimension(728, 427));
				{
					jScrollPane1 = new JScrollPane();
					jPanel1.add(jScrollPane1);
					jScrollPane1.setBounds(10, 11, 716, 415);
					{
						LogTraceArea = new JTextArea();
						jScrollPane1.setViewportView(LogTraceArea);
						LogTraceArea.setText("");
						LogTraceArea.setEditable(false);
						LogTraceArea.setAutoscrolls(true);
						LogTraceArea.setBounds(9, 11, 586, 305);
					}
				}
			}
			pack();
			this.setSize(742, 465);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void setText(String Text){
		LogTraceArea.setText(Text);
	}

}

package diSys.UI;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.rmi.RemoteException;

import javax.swing.AbstractListModel;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextPane;
import javax.swing.WindowConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import SystemAnalysis.AnalysisClient;

/**
* This code was edited or generated using CloudGarden's Jigloo
* SWT/Swing GUI Builder, which is free for non-commercial
* use. If Jigloo is being used commercially (ie, by a corporation,
* company or business for any purpose whatever) then you
* should purchase a license for each developer using Jigloo.
* Please visit www.cloudgarden.com for details.
* Use of Jigloo implies acceptance of these licensing terms.
* A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR
* THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED
* LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
*/
public class AnalysisPanel extends javax.swing.JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JButton RunButton;
	private JPanel jPanel1;
	private JCheckBox CheckDownloadTask;
	private JCheckBox CheckMultTask;
	private JLabel jLabel4;
	private JSlider jSlider3;
	private JSlider jSlider2;
	private JScrollPane jScrollPane1;
	private JTextPane jTextPane1;
	private JProgressBar jProgressBar1;
	private ButtonGroup buttonGroup1;
	private JSlider jSlider1;
	private JPanel jPanel4;
	private JLabel jLabel2;
	private JLabel jLabel1;
	private JRadioButton NewRadio;
	private JRadioButton RRRadio;
	private JPanel jPanel2;
	
	private int bufferSize = 1000;
	private static final int MaxTasksNum = 10000;
	private static final int MaxchuckSize = 500;

	{
		//Set Look & Feel
		try {
			javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public class ComboBoxDataModel extends AbstractListModel implements ComboBoxModel{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		JCheckBox[] choices;
		Object selectedItem;
		
		public ComboBoxDataModel(JCheckBox[] choices){
			this.choices = choices;
		}
		@Override
		public Object getSelectedItem() {
			return this.selectedItem;
		}

		@Override
		public void setSelectedItem(Object anItem) {
			this.selectedItem = anItem;
		}

		@Override
		public Object getElementAt(int index) {
			return (JCheckBox)choices[index];
		}

		@Override
		public int getSize() {
			return choices.length;
		}
	}


	/**
	* Auto-generated main method to display this 
	* JPanel inside a new JFrame.
	*/
	public static void main(String[] args) {
		JFrame frame = new JFrame();
		frame.getContentPane().add(new AnalysisPanel());
		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}
	
	public AnalysisPanel() {
		super();
		initGUI();
	}
	
	private void initGUI() {
		try {
			this.setPreferredSize(new java.awt.Dimension(682, 389));
			this.setBackground(new java.awt.Color(255,255,255));
			this.setLayout(null);
			{
				RunButton = new JButton();
				this.add(RunButton);
				RunButton.setText("Run");
				RunButton.setBounds(10, 355, 88, 23);
				RunButton.addMouseListener(new MouseAdapter() {
					public void mouseClicked(MouseEvent evt) {
						RunButtonMouseClicked(evt);
					}
				});
			}
			{
				jPanel1 = new JPanel();
				this.add(jPanel1);
				jPanel1.setBounds(15, 8, 138, 114);
				jPanel1.setBackground(new java.awt.Color(255,255,255));
				jPanel1.setBorder(BorderFactory.createTitledBorder("Task Types"));
				{
					CheckDownloadTask = new JCheckBox();
					jPanel1.add(CheckDownloadTask);
					CheckDownloadTask.setText("IO Task");
					CheckDownloadTask.setPreferredSize(new java.awt.Dimension(120, 23));
					CheckDownloadTask.setBackground(new java.awt.Color(255,255,255));
				}
				{
					CheckMultTask = new JCheckBox();
					jPanel1.add(CheckMultTask);
					CheckMultTask.setText("Computational Task ");
					CheckMultTask.setBackground(new java.awt.Color(255,255,255));
				}
			}
			{

				jPanel2 = new JPanel();
				this.add(jPanel2);
				jPanel2.setBackground(new java.awt.Color(255,255,255));
				jPanel2.setBorder(BorderFactory.createTitledBorder("Scheduler "));
				jPanel2.setBounds(156, 8, 108, 114);
				jPanel2.setLayout(null);
				{
					NewRadio = new JRadioButton();
					jPanel2.add(NewRadio);
					NewRadio.setSelected(true);
					//jPanel2.add(NewRadio);
					NewRadio.setText("Adaptive ");
					NewRadio.setBackground(new java.awt.Color(255,255,255));
					NewRadio.setBounds(12, 27, 78, 24);
				}
				{
					RRRadio = new JRadioButton();
					jPanel2.add(RRRadio);

					RRRadio.setText("RoundRobin");
					RRRadio.setBackground(new java.awt.Color(255,255,255));
					RRRadio.setBounds(12, 54, 84, 21);
				}

			}
			{
				jPanel4 = new JPanel();
				this.add(jPanel4);
				jPanel4.setBackground(new java.awt.Color(255,255,255));
				jPanel4.setBorder(BorderFactory.createTitledBorder("Task Configuration"));
				jPanel4.setBounds(265, 8, 407, 114);
				jPanel4.setLayout(null);
				{
					jLabel4 = new JLabel();
					jPanel4.add(jLabel4);
					jLabel4.setText("Chunk Size    :");
					jLabel4.setBounds(9, 53, 109, 14);
				}
				{
					jLabel1 = new JLabel();
					jPanel4.add(jLabel1);
					jLabel1.setText("Num Of Tasks:");
					jLabel1.setBounds(7, 23, 117, 14);
				}
				{
					jSlider1 = new JSlider();
					jPanel4.add(jSlider1);
					jSlider1.setMaximum(bufferSize);
					jSlider1.setValue(bufferSize/2);
					//getJProgressBar1().setValue(20);
					this.add(getJProgressBar1());
					jSlider1.setBounds(129, 84, 272, 23);
					jSlider1.setBackground(new java.awt.Color(255,255,255));
					jSlider1.addChangeListener(new ChangeListener() {
						public void stateChanged(ChangeEvent evt) {
							jSlider1StateChanged(evt);
						}
					});
					buttonGroup1 = new ButtonGroup();
					buttonGroup1.add(NewRadio);
					NewRadio.addMouseListener(new MouseAdapter() {
						public void mouseClicked(MouseEvent evt) {
							NewRadioMouseClicked(evt);
						}
					});
					buttonGroup1.add(RRRadio);
					RRRadio.addMouseListener(new MouseAdapter() {
						public void mouseClicked(MouseEvent evt) {
							RRRadioMouseClicked(evt);
						}
					});
					
				}
				{
					jLabel2 = new JLabel();
					jPanel4.add(jLabel2);
					getJSlider2().setMaximum(MaxTasksNum);
					getJSlider3().setMaximum(MaxchuckSize);
					getJSlider2().setValue(1000);
					getJSlider3().setValue(1);
					jPanel4.add(getJSlider2());
					jPanel4.add(getJSlider3());
					this.add(getJScrollPane1());
					jLabel2.setText("Buffer Capacity:"+jSlider1.getValue());
					jLabel2.setBounds(11, 84, 121, 14);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public JProgressBar getJProgressBar1() {
		if(jProgressBar1 == null) {
			jProgressBar1 = new JProgressBar();
			jProgressBar1.setBounds(108, 355, 564, 22);
		}
		return jProgressBar1;
	}


	private void jSlider1StateChanged(ChangeEvent evt) {
		//System.out.println("jSlider1.stateChanged, event="+evt);
		jLabel2.setText("Buffer Capacity: "+jSlider1.getValue());
		try {
			SystemManagerUI.sysManager.SetBufferCapacity(jSlider1.getValue());
		} catch (RemoteException e) {
				JOptionPane.showMessageDialog(this, e.getMessage(), "Error!", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public JTextPane getJTextPane1() {
		if(jTextPane1 == null) {
			jTextPane1 = new JTextPane();
			jTextPane1.setText("Press Run To start system analysis benchmark ...");
			jTextPane1.setBounds(10, 122, 662, 227);
		}
		return jTextPane1;
	}
	
	private JScrollPane getJScrollPane1() {
		if(jScrollPane1 == null) {
			jScrollPane1 = new JScrollPane();
			jScrollPane1.setBounds(10, 122, 662, 227);
			jScrollPane1.setBorder(BorderFactory.createTitledBorder("Log"));
			jScrollPane1.setBackground(new java.awt.Color(255,255,255));
			jScrollPane1.setViewportView(getJTextPane1());
		}
		return jScrollPane1;
	}
	

	private void RRRadioMouseClicked(MouseEvent evt) {
		try {
		if (RRRadio.isSelected())
			
				SystemManagerUI.sysManager.SetRRscheduler(true);
			
		else 
			SystemManagerUI.sysManager.SetRRscheduler(false);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(this, e.getMessage(), "Error!", JOptionPane.ERROR_MESSAGE);
		}
		//System.out.println("RRRadio.mouseClicked, event="+evt);
		//TODO add your code for RRRadio.mouseClicked
	}
	
	private void NewRadioMouseClicked(MouseEvent evt) {
		try {
			if (RRRadio.isSelected())
				
					SystemManagerUI.sysManager.SetRRscheduler(true);
				
			else 
				SystemManagerUI.sysManager.SetRRscheduler(false);
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				JOptionPane.showMessageDialog(this, e.getMessage(), "Error!", JOptionPane.ERROR_MESSAGE);
			}
	}
	
	private void RunButtonMouseClicked(MouseEvent evt) {
		System.out.println("Starting analysis run");
		//TODO add your code for RunButton.mouseClicked
		int num_of_tasks = getJSlider2().getValue();
		int chunck_size = getJSlider3().getValue();
		final AnalysisClient ac=new AnalysisClient(SystemManagerUI.sysmRi.Ip(),SystemManagerUI.sysmRi.Port(),chunck_size,num_of_tasks, new AnalysisClient.TaskType[] {AnalysisClient.TaskType.MatrixMul},this);
		Thread t=new Thread(new Runnable() {
			
			@Override
			public void run() {
				ac.run();
			}
		});
		t.start();
		jTextPane1.setText("Running analysis ...\nnum of tasks :"+num_of_tasks+"\nchunck size :"+chunck_size+"\nBuffer Capacity : "+jSlider1.getValue()+"\n-----------------------------------------------------");
	}
	
	private JSlider getJSlider2() {
		if(jSlider2 == null) {
			jSlider2 = new JSlider();
			jSlider2.setValue(bufferSize / 2);
			jSlider2.setMaximum(bufferSize);
			jSlider2.setBackground(new java.awt.Color(255,255,255));
			jSlider2.setBounds(128, 20, 274, 23);
			jSlider2.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent evt) {
					jSlider2StateChanged(evt);
				}
			});
		}
		return jSlider2;
	}
	
	private void jSlider2StateChanged(ChangeEvent evt) {
		jLabel1.setText("Num Of Tasks: "+jSlider2.getValue());
	}
	
	private JSlider getJSlider3() {
		if(jSlider3 == null) {
			jSlider3 = new JSlider();
			jSlider3.setValue(bufferSize / 2);
			jSlider3.setMaximum(bufferSize);
			jSlider3.setBackground(new java.awt.Color(255,255,255));
			jSlider3.setBounds(128, 51, 272, 23);
			jSlider3.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent evt) {
					jSlider3StateChanged(evt);
				}
			});
		}
		return jSlider3;
	}
	
	private void jSlider3StateChanged(ChangeEvent evt) {
		jLabel4.setText("Chunk Size : "+jSlider3.getValue());
		
	//	System.out.println("jSlider3.stateChanged, event="+evt);
		//TODO add your code for jSlider3.stateChanged
	}

}

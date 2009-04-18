package diSys.UI;

import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.rmi.RemoteException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import diSys.Common.SystemUpdates;

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
@SuppressWarnings("serial")
public class UpdatePnael extends javax.swing.JPanel {
	private JLabel jLabel1;
	private JLabel jLabel2;
	private JLabel jLabel3;
	private JButton jButtonUpdateToLast;
	private JButton UpdateButton;
	private JButton BrowseButton;
	private JTextField jTextField1;

	/**
	* Auto-generated main method to display this 
	* JPanel inside a new JFrame.
	
	public static void main(String[] args) {
		JFrame frame = new JFrame();
		frame.getContentPane().add(new UpdatePnael());
		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}
	*/
	public UpdatePnael() {
		super();
		initGUI();
	}
	
	private void initGUI() {
		try {
			this.setPreferredSize(new java.awt.Dimension(616, 300));
			this.setLayout(null);
			this.setBackground(new java.awt.Color(255,255,255));
			{
				jLabel1 = new JLabel();
				this.add(jLabel1);
				jLabel1.setText("Current Version :");
				jLabel1.setBounds(12, 12, 89, 14);
			}
			{
				jLabel2 = new JLabel();
				this.add(jLabel2);
				//jLabel2.setText("V2.0");
				jLabel2.setBounds(101, 12, 26, 14);
			}
			{
				jLabel3 = new JLabel();
				this.add(jLabel3);
				jLabel3.setText("Updates File:");
				jLabel3.setBounds(12, 47, 67, 14);
			}
			{
				jTextField1 = new JTextField();
				this.add(jTextField1);
				jTextField1.setText("D:\\study\\projects\\Distrebuted\\Disys\\diSysExample\\UpdateJar.jar");
				jTextField1.setBounds(12, 67, 486, 20);
			}
			{
				BrowseButton = new JButton();
				this.add(BrowseButton);
				BrowseButton.setText("Browse");
				BrowseButton.setBounds(510, 68, 84, 20);
				BrowseButton.addMouseListener(new MouseAdapter() {
					public void mouseClicked(MouseEvent evt) {
						BrowseButtonMouseClicked(evt);
					}
				});
			}
			{
				UpdateButton = new JButton();
				this.add(UpdateButton);
				UpdateButton.setText("Update");
				UpdateButton.setBounds(12, 269, 77, 20);
				UpdateButton.setPreferredSize(new java.awt.Dimension(77, 20));
				UpdateButton.addMouseListener(new MouseAdapter() {
					public void mouseClicked(MouseEvent evt) {
						UpdateButtonMouseClicked(evt);
					}
				});
			}
			{
				jButtonUpdateToLast = new JButton();
				this.add(jButtonUpdateToLast);
				jButtonUpdateToLast.setText("Update to this version");
				jButtonUpdateToLast.setBounds(139, 8, 147, 23);
				jButtonUpdateToLast.setPreferredSize(new java.awt.Dimension(147, 23));
				jButtonUpdateToLast.addMouseListener(new MouseAdapter() {
					public void mouseClicked(MouseEvent evt) {
						jButtonUpdateToLastMouseClicked(evt);
					}
				});
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void UpdateButtonMouseClicked(MouseEvent evt) {
		try {
			
			UpdateDialog dlg=new UpdateDialog(null,new SystemUpdates(this.jTextField1.getText()),this.jTextField1.getText());
			  dlg.setVisible(true);
		
		//	  if(dlg.update==true){
			  
			//  }
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void UpdateSysUI() {
		jLabel2.setText(""+SystemManagerUI.sysData.Version);
		
	}
	final public static JFileChooser fc = new JFileChooser(); 
	static String FileName="";
	private void BrowseButtonMouseClicked(MouseEvent evt) {
		
		int returnVal = fc.showOpenDialog(this);
	        if (returnVal == JFileChooser.APPROVE_OPTION) {
	        	FileName=fc.getSelectedFile().getAbsolutePath();
	        	this.jTextField1.setText(FileName);
	        }else{
	        	return;
	        }
	}
	
	private void jButtonUpdateToLastMouseClicked(MouseEvent evt) {
		try {
			int Userreply=JOptionPane.YES_OPTION;
			 Userreply=JOptionPane.showConfirmDialog(this, "Are you sure you want to Update to version :"+jLabel2.getText(), "Are you sure?", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
			if(Userreply==JOptionPane.NO_OPTION) return;
			SystemManagerUI.updateThread.pause();
			String message=SystemManagerUI.sysManager.UpdateToLastRevision(true);
			SystemManagerUI.sysManager=null;
			new WaitDialog(null,message+"\r\nWait until auto update is stabilized ...",30).setVisible(true);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, e.getMessage(), "Error!", JOptionPane.ERROR_MESSAGE);
		}
	}

}

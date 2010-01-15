package diSys.Common;

//import javax.mail.*;
//import javax.mail.internet.*;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.io.BufferedWriter;
import java.io.FileWriter;
public class MailPoster {
	
	//private static String smtpServer= "SMTP.Intel.com";
	private static String defaultFrom = "DisysMail.Do.Not.Reply@intel.com";
	public  static String newLine =	"<br/>"; //System.getProperty("line.separator");
	private static String mailPrefix= "Hi,"+newLine+"This is Disys automated mail."+newLine+newLine;
	private static String mailSuffix= newLine+newLine+"Thanks,"+newLine+"DisysTeam;";
	private static String[] defaultTo = {"hani.ayoub@intel.com", "gil.fridman@intel.com", "tomer.debby@intel.com"};
	private static String destPath = "W:\\Regression\\eMails";
	
	public static void writeToFile(String filePath, String content)
	{
		try 
		{
			Writer output = null;
		    File file = new File(filePath);
			output = new BufferedWriter(new FileWriter(file));
		    output.write(content);
		    output.close();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	public static void SendMail( String recipients[ ], String subject, String message , String from) //throws MessagingException
	{
		String mails = "";
		for(String to : recipients)
		{
			mails = 
				"<emails>"+
					"<email_x0020_address>"+to+"</email_x0020_address>"+
					"<enabled>true</enabled>"+
				"</emails>"+mails;
		}
		String html = 
			"<p style=\"line-height: 150%\">" +
			"<font face=\"Courier New\" size=\"2\">" +
			message+"<br/>"+
			"</font>"+
			"</p>";
		String xml = 
			"<?xml version=\"1.0\" standalone=\"yes\"?>"+
					"<NewDataSet>"+
					  "<xs:schema id=\"NewDataSet\" xmlns=\"\" xmlns:xs=\"http://www.w3.org/2001/XMLSchema\" xmlns:msdata=\"urn:schemas-microsoft-com:xml-msdata\">"+
					    "<xs:element name=\"NewDataSet\" msdata:IsDataSet=\"true\" msdata:MainDataTable=\"emails\" msdata:Locale=\"en-US\" msdata:EnforceConstraints=\"False\">"+
					      "<xs:complexType>"+
					        "<xs:choice minOccurs=\"0\" maxOccurs=\"unbounded\">"+
					          "<xs:element name=\"emails\">"+
					            "<xs:complexType>"+
					              "<xs:sequence>"+
					                "<xs:element name=\"email_x0020_address\" type=\"xs:string\" />"+
					                "<xs:element name=\"enabled\" type=\"xs:boolean\" minOccurs=\"0\" />"+
					              "</xs:sequence>"+
					            "</xs:complexType>"+
					          "</xs:element>"+
					        "</xs:choice>"+
					      "</xs:complexType>"+
					      "<xs:unique name=\"Constraint1\" msdata:PrimaryKey=\"true\">"+
					        "<xs:selector xpath=\".//emails\" />"+
					        "<xs:field xpath=\"email_x0020_address\" />"+
					      "</xs:unique>"+
					    "</xs:element>"+
					  "</xs:schema>"+
							"<emails>"+
								"<email_x0020_address>"+mails+"</email_x0020_address>"+
								"<enabled>true</enabled>"+
							"</emails>"+
				"</NewDataSet>";
		String fileName = "DisysMail_"+System.currentTimeMillis();
		writeToFile(destPath + "\\" + fileName + ".htm", html);
		writeToFile(destPath + "\\" + fileName + ".xml", xml);
		/*
	    boolean debug = false;
	
	     //Set the host smtp address
	     Properties props = new Properties();
	     props.put("mail.smtp.host", smtpServer);
	
	    // create some properties and get the default Session
	    Session session = Session.getDefaultInstance(props, null);
	    session.setDebug(debug);
	
	    // create a message
	    Message msg = new MimeMessage(session);
	
	    // set the from and to address
	    InternetAddress addressFrom = new InternetAddress(from);
	    msg.setFrom(addressFrom);
	
	    InternetAddress[] addressTo = new InternetAddress[recipients.length]; 
	    for (int i = 0; i < recipients.length; i++)
	    {
	        addressTo[i] = new InternetAddress(recipients[i]);
	    }
	    msg.setRecipients(Message.RecipientType.TO, addressTo);
	   
	
	    // Optional : You can also set your custom headers in the Email if you Want
	    msg.addHeader("MyHeaderName", "myHeaderValue");
	
	    // Setting the Subject and Content Type
	    msg.setSubject(subject);
	    msg.setContent(message, "text/plain");
	    Transport.send(msg);
	    */
	}
	
	public static void SendMail(String recipients[], String subject, String message)
	{
			SendMail(recipients, subject, mailPrefix + subject + newLine + message + mailSuffix, defaultFrom);
	}
	public static void SendMail(String subject, String message)
	{
			SendMail(defaultTo, subject, message);
	}
	
	public static void main(String[] args) throws Exception {
		SendMail("This is the subject", "This is the message: TST");
	}
}

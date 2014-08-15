package ie.brianhenry.veintobrain.server.core.email;


public class Start {
	public static void mail(){  
        //�������Ҫ�������ʼ�  
     MailSenderInfo mailInfo = new MailSenderInfo();   
     mailInfo.setMailServerHost("smtp.ucd.ie");   
     mailInfo.setMailServerPort("25");   
     mailInfo.setValidate(true);   
     mailInfo.setUserName("13203112");   
     mailInfo.setPassword("0");//�����������   
     mailInfo.setFromAddress("13203112@ucdconnect.ie");   
     mailInfo.setToAddress("brian.henry@ucdconnect.ie");   
     mailInfo.setSubject("Hi,Brian. I think I type the right name this time.");   
     mailInfo.setContent("I am Forrest");   
        //�������Ҫ�������ʼ�  
     SimpleMailSender sms = new SimpleMailSender();  
         sms.sendTextMail(mailInfo);//���������ʽ   
         SimpleMailSender.sendHtmlMail(mailInfo);//����html��ʽ  
   }  

}

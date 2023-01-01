package test.students.utils.email;




// Interface
public interface EmailService {

    // Method
    // To send a simple email
    String sendSimpleMail(EmailDetails details);

    // Method
    // To send an email with attachment
    void sendMailWithAttachment(EmailDetails details);
}


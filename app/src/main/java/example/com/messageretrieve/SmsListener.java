package example.com.messageretrieve;

public interface SmsListener {
    public void messageReceived(String sender, String messageText);
}

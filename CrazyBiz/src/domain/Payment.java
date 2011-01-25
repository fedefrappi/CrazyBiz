package domain;

public class Payment {
	private int id;
	private Payment.Type type;
	private String iban;
	private String paypalAccount;
	private Person sender;
	private Person recipient;
	
	public static enum Type{BANK, CASH, PAYPAL};
}

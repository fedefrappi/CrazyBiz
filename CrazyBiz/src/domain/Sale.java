package domain;

import java.math.BigDecimal;
import java.util.Date;

public class Sale {
	private int id;
	private BigDecimal price;
	private Date date;
	private Proposal winningProposal;
	private Shipment shipment;
	private Payment payment;
}

package it.crazybiz.server.domain;

import java.math.BigDecimal;
import java.util.Date;

public class Purchase {
	private int id;
	private BigDecimal price;
	private Date date;
	private Person seller;
	private Shipment shipment;
	private Payment payment;
}

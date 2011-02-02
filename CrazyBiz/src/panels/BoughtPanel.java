package panels;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.util.Date;

import com.example.crazybiz.Utils;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

public class BoughtPanel extends VerticalLayout implements Property.ValueChangeListener{

	private TextField price;
	private PopupDateField date;
	private TextField sellerName;
	private TextField sellerPhone;
	private TextField sellerEmail;
	private TextField sellerCountry;
	private TextField sellerCity;
		
	public BoughtPanel() {
		price = new TextField("Price");
		price.setValue(new BigDecimal(0.00));
		date = new PopupDateField();
        date.setInputPrompt("Start date");
        date.setResolution(PopupDateField.RESOLUTION_DAY);
        date.addListener(this);
        date.setImmediate(true);
        
        sellerName = new TextField("Name");
        sellerPhone = new TextField("Phone");
        sellerEmail = new TextField("Email");
        sellerCountry = new TextField("Country");
        sellerCity = new TextField("City");
        
        addComponent(price);
        addComponent(date);
        addComponent(sellerName);
        addComponent(sellerPhone);
        addComponent(sellerEmail);
        addComponent(sellerCountry);
        addComponent(sellerCity);
	}

	@Override
	public void valueChange(ValueChangeEvent event) {
		DateFormat dateFormatter = DateFormat.getDateInstance(DateFormat.SHORT);
        Object value = event.getProperty().getValue();
        if (value == null || !(value instanceof Date)) {
            getWindow().showNotification("Invalid date entered");
        } else {
            String dateOut = dateFormatter.format(value);
        }
	}

	public BigDecimal getPrice() {
		return Utils.parsePrice(price.getValue().toString());
	}

	public String getSellerName() {
		return sellerName.getValue().toString();
	}

	public Date getDate() {
		if(date.getValue() == null){
			return new Date(System.currentTimeMillis());
		}
		return (Date)date.getValue();
	}

	public String getSellerPhone() {
		return sellerPhone.getValue().toString();
	}

	public String getSellerEmail() {
		return sellerEmail.getValue().toString();
	}

	public String getSellerCountry() {
		return sellerCountry.getValue().toString();
	}

	public String getSellerCity() {
		return sellerCity.getValue().toString();
	}
	
	public void setSellerCity(String newSellerCity) {
		this.sellerCity.setValue(newSellerCity);
	}
	
	public void setSellerCountry(String newSellerCountry) {
		this.sellerCountry.setValue(newSellerCountry);
	}
	
	public void setSellerEmail(String newSellerEmail) {
		this.sellerEmail.setValue(newSellerEmail);
	}
	
	public void setSellerPhone(String newSellerPhone) {
		this.sellerPhone.setValue(newSellerPhone);
	}
	
	public void setSellerName(String newSellerName) {
		this.sellerName.setValue(newSellerName);
	}
	
	public void setDate(Date newDate) {
		this.date.setValue(newDate);
	}
	
	public void setPrice(BigDecimal newPrice) {
		this.price.setValue(newPrice);
	}
}

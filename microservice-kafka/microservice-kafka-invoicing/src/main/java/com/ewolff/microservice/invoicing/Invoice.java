package com.ewolff.microservice.invoicing;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

@Entity
public class Invoice {

	@Id
	private long id;

	@Embedded
	private Customer customer;

	private Date updated;

	@Embedded
	private Address billingAddress = new Address();

	@OneToMany(cascade = CascadeType.ALL)
	private List<InvoiceLine> invoiceLine;

	public Invoice() {
		super();
		invoiceLine = new ArrayList<InvoiceLine>();
	}

	public Invoice(long id, Customer customer, Date updated, Address billingAddress, List<InvoiceLine> invoiceLine) {
		super();
		this.id = id;
		this.customer = customer;
		this.updated = updated;
		this.billingAddress = billingAddress;
		this.invoiceLine = invoiceLine;
	}

	public Address getBillingAddress() {
		return billingAddress;
	}

	public void setBillingAddress(Address billingAddress) {
		this.billingAddress = billingAddress;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getId() {
		return id;
	}

	public Date getUpdated() {
		return updated;
	}

	public void setUpdated(Date created) {
		this.updated = created;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public List<InvoiceLine> getInvoiceLine() {
		return invoiceLine;
	}

	public void setInvoiceLine(List<InvoiceLine> invoiceLine) {
		this.invoiceLine = invoiceLine;
	}

	@Transient
	public void setOrderLine(List<InvoiceLine> orderLine) {
		this.invoiceLine = orderLine;
	}

	public void addLine(int count, Item item) {
		this.invoiceLine.add(new InvoiceLine(count, item));
	}

	public int getNumberOfLines() {
		return invoiceLine.size();
	}

	public double totalAmount() {
		return invoiceLine.stream().map((ol) -> ol.getCount() * ol.getItem().getPrice()).reduce(0.0,
				(d1, d2) -> d1 + d2);
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}

	@Override
	public boolean equals(Object obj) {
		return EqualsBuilder.reflectionEquals(this, obj);
	}
}

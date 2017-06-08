package com.ewolff.microservice.order.logic;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.ewolff.microservice.order.customer.Customer;
import com.ewolff.microservice.order.item.Item;

@Entity
@Table(name = "ORDERTABLE")
public class Order {

	@Id
	@GeneratedValue
	private long id;

	@ManyToOne
	private Customer customer;

	private Date updated;

	@Embedded
	@AttributeOverrides({ @AttributeOverride(name = "street", column = @Column(name = "SHIPPING_STREET")),
			@AttributeOverride(name = "zip", column = @Column(name = "SHIPPING_ZIP")),
			@AttributeOverride(name = "city", column = @Column(name = "SHIPPING_CITY")) })
	private Address shippingAddress = new Address();

	@Embedded
	@AttributeOverrides({ @AttributeOverride(name = "street", column = @Column(name = "BILLING_STREET")),
			@AttributeOverride(name = "zip", column = @Column(name = "BILLING_ZIP")),
			@AttributeOverride(name = "city", column = @Column(name = "BILLING_CITY")) })
	private Address billingAddress = new Address();

	@OneToMany(cascade = CascadeType.ALL)
	private List<OrderLine> orderLine;

	public Order() {
		super();
		orderLine = new ArrayList<OrderLine>();
		updated();
	}

	private void updated() {
		updated = new Date();
	}

	public Address getShippingAddress() {
		return shippingAddress;
	}

	public void setShippingAddress(Address shippingAddress) {
		updated();
		this.shippingAddress = shippingAddress;
	}

	public Address getBillingAddress() {
		return billingAddress;
	}

	public void setBillingAddress(Address billingAddress) {
		updated();
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
		updated();
	}

	public List<OrderLine> getOrderLine() {
		return orderLine;
	}

	public Order(Customer customer) {
		super();
		this.customer = customer;
		this.orderLine = new ArrayList<OrderLine>();
	}

	public void setOrderLine(List<OrderLine> orderLine) {
		this.orderLine = orderLine;
		updated();
	}

	public void addLine(int count, Item item) {
		this.orderLine.add(new OrderLine(count, item));
		updated();
	}

	public int getNumberOfLines() {
		return orderLine.size();
	}

	public double totalPrice() {
		return orderLine.stream().map((ol) -> ol.getCount() * ol.getItem().getPrice()).reduce(0.0, (d1, d2) -> d1 + d2);
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

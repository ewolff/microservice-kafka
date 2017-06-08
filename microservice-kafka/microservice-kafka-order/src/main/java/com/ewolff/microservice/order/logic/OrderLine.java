package com.ewolff.microservice.order.logic;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.ewolff.microservice.order.item.Item;

@Entity
public class OrderLine {

	@Column(name = "F_COUNT")
	private int count;

	@ManyToOne
	private Item item;

	@Id
	@GeneratedValue
	private long id;

	public void setCount(int count) {
		this.count = count;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public OrderLine() {
	}

	public OrderLine(int count, Item item) {
		this.count = count;
		this.item = item;
	}

	public int getCount() {
		return count;
	}

	public Item getItem() {
		return item;
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

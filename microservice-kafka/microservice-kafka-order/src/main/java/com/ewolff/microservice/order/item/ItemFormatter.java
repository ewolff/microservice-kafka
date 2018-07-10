package com.ewolff.microservice.order.item;

import java.text.ParseException;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

@Component
public class ItemFormatter implements Formatter<Item> {

	private ItemRepository itemRepository;

	@Autowired
	public ItemFormatter(ItemRepository itemRepository) {
		this.itemRepository = itemRepository;
	}

	@Override
	public String print(Item item, Locale locale) {
		return item.getItemId().toString();
	}

	@Override
	public Item parse(String text, Locale locale) throws ParseException {
		return itemRepository.findById(Long.parseLong(text)).get();
	}

}

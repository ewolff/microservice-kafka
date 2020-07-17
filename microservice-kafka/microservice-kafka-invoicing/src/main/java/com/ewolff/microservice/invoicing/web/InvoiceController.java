package com.ewolff.microservice.invoicing.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.ewolff.microservice.invoicing.InvoiceRepository;

@Controller
public class InvoiceController {

	private InvoiceRepository invoiceRepository;

	@Autowired
	public InvoiceController(InvoiceRepository invoiceRepository) {
		this.invoiceRepository = invoiceRepository;
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
	public ModelAndView Item(@PathVariable("id") long id) {
		return new ModelAndView("invoice", "invoice", invoiceRepository.findById(id).get());
	}

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public ModelAndView ItemList() {
		return new ModelAndView("invoicelist", "invoices", invoiceRepository.findAll());
	}

}

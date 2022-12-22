package com.ewolff.microservice.invoicing;

import java.util.Date;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface InvoiceRepository extends PagingAndSortingRepository<Invoice, Long>, CrudRepository<Invoice, Long> {

	@Query("SELECT max(i.updated) FROM Invoice i")
	Date lastUpdate();

}

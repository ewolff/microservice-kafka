package com.ewolff.microservice.order.customer;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(exported = false)
public interface CustomerRepository extends PagingAndSortingRepository<Customer, Long> {

	List<Customer> findByName(@Param("name") String name);

}

package com.ewolff.microservice.order.item;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(exported = false)
public interface ItemRepository extends PagingAndSortingRepository<Item, Long> {

	List<Item> findByName(@Param("name") String name);

	List<Item> findByNameContaining(@Param("name") String name);

	@Query("SELECT price FROM Item i WHERE i.itemId=?1")
	double price(long itemId);

}

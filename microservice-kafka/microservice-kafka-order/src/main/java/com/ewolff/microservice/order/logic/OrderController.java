package com.ewolff.microservice.order.logic;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import com.ewolff.microservice.order.customer.Customer;
import com.ewolff.microservice.order.customer.CustomerRepository;
import com.ewolff.microservice.order.item.ItemRepository;

@Controller
class OrderController {

	private OrderRepository orderRepository;

	private OrderService orderService;

	private CustomerRepository customerRepository;
	private ItemRepository itemRepository;

	private OrderController(OrderService orderService, OrderRepository orderRepository,
			CustomerRepository customerRepository, ItemRepository itemRepository) {
		super();
		this.orderRepository = orderRepository;
		this.customerRepository = customerRepository;
		this.itemRepository = itemRepository;
		this.orderService = orderService;
	}

	@ModelAttribute("items")
	public Iterable<com.ewolff.microservice.order.item.Item> items() {
		return itemRepository.findAll();
	}

	@ModelAttribute("customers")
	public Iterable<Customer> customers() {
		return customerRepository.findAll();
	}

	@GetMapping("/")
	public ModelAndView orderList() {
		return new ModelAndView("orderlist", "orders", orderRepository.findAll());
	}

	@GetMapping("/form.html")
	public ModelAndView form() {
		return new ModelAndView("orderForm", "order", new Order());
	}

	@PostMapping("/line")
	public ModelAndView addLine(Order order) {
		order.addLine(0, itemRepository.findAll().iterator().next());
		return new ModelAndView("orderForm", "order", order);
	}

	@GetMapping("/{id}")
	public ModelAndView get(@PathVariable("id") long id) {
		return new ModelAndView("order", "order", orderRepository.findById(id).get());
	}

	@PostMapping("/")
	public ModelAndView post(Order order) {
		order = orderService.order(order);
		return new ModelAndView("success");
	}

	@DeleteMapping("/{id}")
	public ModelAndView post(@PathVariable("id") long id) {
		orderRepository.deleteById(id);

		return new ModelAndView("success");
	}

}

package com.diegosimon.pagamento.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.diegosimon.pagamento.data.vo.VendaVO;
import com.diegosimon.pagamento.services.VendaService; 

@RestController
@RequestMapping("/venda")
public class VendaController {
	
	private final VendaService vendaService;
	private final PagedResourcesAssembler<VendaVO> assembler;
	
	@Autowired
	public VendaController(VendaService vendaService, PagedResourcesAssembler<VendaVO> assembler) {
		this.vendaService = vendaService;
		this.assembler = assembler;
	}
	
	@GetMapping(value = "/{id}", produces = {"application/json", "application/xml", "application/x-yml"})
	public VendaVO findById(@PathVariable(name = "id") Long id) {
		VendaVO product = this.vendaService.findByID(id);
		product.add(linkTo(methodOn(VendaController.class).findById(id)).withSelfRel());
		return product;
	}
	
	@GetMapping(produces = {"application/json", "application/xml", "application/x-yml"})
	public ResponseEntity<?> findAll(@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "limit", defaultValue = "12") int limit,
			@RequestParam(value = "direction", defaultValue = "asc") String direction) {
		
		var sortDirection = "desc".equalsIgnoreCase(direction) ? Direction.DESC : Direction.ASC;
		Pageable pageable = PageRequest.of(page, limit, Sort.by(sortDirection, "data")); 
		Page<VendaVO> produtos = this.vendaService.findAll(pageable);
		produtos.stream()
				.forEach(p -> p.add(linkTo(methodOn(VendaController.class).findById(p.getId())).withSelfRel()));
		PagedModel<EntityModel<VendaVO>> pageModel = assembler.toModel(produtos);
		
		return new ResponseEntity<>(pageModel, HttpStatus.OK);
	}
	
	@PostMapping(produces = {"application/json", "application/xml", "application/x-yml"}, 
			consumes = {"application/json", "application/xml", "application/x-yml"})
	public VendaVO create(@RequestBody VendaVO venda) {
		VendaVO produtoCriado = this.vendaService.create(venda);
		produtoCriado.add(linkTo(methodOn(VendaController.class).findById(venda.getId())).withSelfRel());
		return produtoCriado;
	}

}

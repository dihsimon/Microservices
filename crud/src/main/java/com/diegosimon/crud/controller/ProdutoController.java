package com.diegosimon.crud.controller;

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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.diegosimon.crud.data.vo.ProductVO;
import com.diegosimon.crud.service.ProdutoService;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo; 
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn; 

@RestController
@RequestMapping("/produto")
public class ProdutoController {

	private final ProdutoService produtoService;
	private final PagedResourcesAssembler<ProductVO> assembler;
	
	@Autowired
	public ProdutoController(ProdutoService produtoService, PagedResourcesAssembler<ProductVO> assembler) {
		super();
		this.produtoService = produtoService;
		this.assembler = assembler;
	}
	
	@GetMapping(value = "/{id}", produces = {"application/json", "application/xml", "application/x-yml"})
	public ProductVO findById(@PathVariable(name = "id") Long id) {
		ProductVO product = this.produtoService.findByID(id);
		product.add(linkTo(methodOn(ProdutoController.class).findById(id)).withSelfRel());
		return product;
	}
	
	@GetMapping(produces = {"application/json", "application/xml", "application/x-yml"})
	public ResponseEntity<?> findAll(@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "limit", defaultValue = "12") int limit,
			@RequestParam(value = "direction", defaultValue = "asc") String direction) {
		
		var sortDirection = "desc".equalsIgnoreCase(direction) ? Direction.DESC : Direction.ASC;
		Pageable pageable = PageRequest.of(page, limit, Sort.by(sortDirection, "nome")); 
		Page<ProductVO> produtos = this.produtoService.findAll(pageable);
		produtos.stream()
				.forEach(p -> p.add(linkTo(methodOn(ProdutoController.class).findById(p.getId())).withSelfRel()));
		PagedModel<EntityModel<ProductVO>> pageModel = assembler.toModel(produtos);
		
		return new ResponseEntity<>(pageModel, HttpStatus.OK);
	}
	
	@PostMapping(produces = {"application/json", "application/xml", "application/x-yml"}, 
			consumes = {"application/json", "application/xml", "application/x-yml"})
	public ProductVO create(@RequestBody ProductVO product) {
		ProductVO produtoCriado = this.produtoService.create(product);
		produtoCriado.add(linkTo(methodOn(ProdutoController.class).findById(produtoCriado.getId())).withSelfRel());
		return produtoCriado;
	}
	
	@PutMapping(produces = {"application/json", "application/xml", "application/x-yml"})
	public ProductVO update(@RequestBody ProductVO product) {
		ProductVO produtoCriado = this.produtoService.update(product);
		produtoCriado.add(linkTo(methodOn(ProdutoController.class).findById(product.getId())).withSelfRel());
		return produtoCriado;
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete (@PathVariable("id") Long id) {
		this.produtoService.delete(id);
		return ResponseEntity.ok().build();
	}
}


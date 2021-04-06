package com.diegosimon.crud.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.diegosimon.crud.data.vo.ProductVO;
import com.diegosimon.crud.entity.Produto;
import com.diegosimon.crud.exception.ResourceNotFoundException;
import com.diegosimon.crud.repository.ProdutoRepository;

@Service
public class ProdutoService {

	private ProdutoRepository produtoRepository;
	
	@Autowired
	public ProdutoService(ProdutoRepository produtoRepository) {
		this.produtoRepository = produtoRepository;
	}
	
	public ProductVO create(ProductVO product) {
		return ProductVO.create(this.produtoRepository.save(Produto.create(product)));
	}
	
	public Page<ProductVO> findAll(Pageable pageable){	
		var page = this.produtoRepository.findAll(pageable);
		return page.map(this::convertToProductVO);
	}
	
	private ProductVO convertToProductVO(Produto produto) {
		return ProductVO.create(produto);
	}
	
	public ProductVO findByID(Long id) {
		var entity = produtoRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("No records found for this ID"));
		return ProductVO.create(entity);
	}
	
	public ProductVO update(ProductVO productVo) {
		final Optional<Produto> optionProduto = this.produtoRepository.findById(productVo.getId());
		if(!optionProduto.isPresent()) {
			new ResourceNotFoundException("No records found for this ID");
		}
		return ProductVO.create(this.produtoRepository.save(Produto.create(productVo)));
	}
	
	public void delete(Long id) {
		var entity = produtoRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("No records found for this ID"));
		produtoRepository.delete(entity);
	}
}

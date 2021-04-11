package com.diegosimon.crud.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.diegosimon.crud.data.vo.ProdutoVO;
import com.diegosimon.crud.entity.Produto;
import com.diegosimon.crud.exception.ResourceNotFoundException;
import com.diegosimon.crud.message.ProdutoSendMessage;
import com.diegosimon.crud.repository.ProdutoRepository;

@Service
public class ProdutoService {

	private ProdutoRepository produtoRepository;
	
	private final ProdutoSendMessage produtoSendMessage;
	
	@Autowired
	public ProdutoService(ProdutoRepository produtoRepository, ProdutoSendMessage produtoSendMessage) {
		this.produtoRepository = produtoRepository;
		this.produtoSendMessage = produtoSendMessage;
	}
	
	public ProdutoVO create(ProdutoVO product) {
		final ProdutoVO produtoCriado = ProdutoVO.create(this.produtoRepository.save(Produto.create(product)));
		produtoSendMessage.sendMessage(produtoCriado);
		return produtoCriado;
	}
	
	public Page<ProdutoVO> findAll(Pageable pageable){	
		var page = this.produtoRepository.findAll(pageable);
		return page.map(this::convertToProductVO);
	}
	
	private ProdutoVO convertToProductVO(Produto produto) {
		return ProdutoVO.create(produto);
	}
	
	public ProdutoVO findByID(Long id) {
		var entity = produtoRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("No records found for this ID"));
		return ProdutoVO.create(entity);
	}
	
	public ProdutoVO update(ProdutoVO productVo) {
		final Optional<Produto> optionProduto = this.produtoRepository.findById(productVo.getId());
		if(!optionProduto.isPresent()) {
			new ResourceNotFoundException("No records found for this ID");
		}
		return ProdutoVO.create(this.produtoRepository.save(Produto.create(productVo)));
	}
	
	public void delete(Long id) {
		var entity = produtoRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("No records found for this ID"));
		produtoRepository.delete(entity);
	}
}

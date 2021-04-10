package com.diegosimon.pagamento.services;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.diegosimon.pagamento.data.vo.VendaVO;
import com.diegosimon.pagamento.entity.ProdutoVenda;
import com.diegosimon.pagamento.entity.Venda;
import com.diegosimon.pagamento.exception.ResourceNotFoundException;
import com.diegosimon.pagamento.repository.ProdutoVendaRepository;
import com.diegosimon.pagamento.repository.VendaRepository;

@Service
public class VendaService {
	
	private VendaRepository vendaRepository;
	private ProdutoVendaRepository produtoVendaRepository;
	
	@Autowired
	public VendaService(VendaRepository vendaRepository, ProdutoVendaRepository ProdutoVendaRepository) {
		this.vendaRepository = vendaRepository;
		this.produtoVendaRepository = ProdutoVendaRepository;
	}
	
	public VendaVO create(VendaVO vendaVo) {
		Venda venda = this.vendaRepository.save(Venda.create(vendaVo));
		List<ProdutoVenda> produtoSalvos = new ArrayList<>();
		vendaVo.getProdutos().forEach(p->{
			ProdutoVenda pv = ProdutoVenda.create(p);
			pv.setVenda(venda);
			produtoSalvos.add(this.produtoVendaRepository.save(pv));
		});
		venda.setProdutos(produtoSalvos);
		return VendaVO.create(venda);
	}
	
	public Page<VendaVO> findAll(Pageable pageable){	
		var page = this.vendaRepository.findAll(pageable);
		return page.map(this::convertToVendaVo);
	}
	
	private VendaVO convertToVendaVo(Venda venda) {
		return VendaVO.create(venda);
	}
	
	public VendaVO findByID(Long id) {
		var entity = this.vendaRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("No records found for this ID"));
		return VendaVO.create(entity);
	}

}

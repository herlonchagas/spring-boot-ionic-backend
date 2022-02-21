package com.nelioalves.cursomc.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.nelioalves.cursomc.domain.Categoria;
import com.nelioalves.cursomc.domain.Cliente;
import com.nelioalves.cursomc.dto.CategoriaDTO;
import com.nelioalves.cursomc.mapper.CategoriaMapper;
import com.nelioalves.cursomc.mapper.ClienteMapper;
import com.nelioalves.cursomc.repositories.CategoriaRepository;
import com.nelioalves.cursomc.services.exceptions.DataIntegrityException;
import com.nelioalves.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class CategoriaService {

	@Autowired
	private CategoriaRepository repo;

	public Categoria find(Integer id) {
		Optional<Categoria> obj = repo.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado! Id: " + id + ", Tipo: " + Categoria.class.getName()));
	}

	public Categoria insert(Categoria categoria) {
		categoria.setId(null);
		return repo.save(categoria);
	}
//
//	public Categoria update(Categoria categoria) {
//		find(categoria.getId());
//		return repo.save(categoria);
//	}
	
	public Categoria update(Categoria categoria) {
		var categoriaeNew = find(categoria.getId());
		updateDate(categoriaeNew, categoria);
		return repo.save(categoriaeNew);
	}


	public void delete(Integer id) {
		find(id);
		
		try {
			repo.deleteById(id);
		} catch (DataIntegrityViolationException e) {
			throw new DataIntegrityException("Não é possivel excluir uma categoria que possui Produtos");
		}
		
	}

	public List<Categoria> findAll() {
		
		return repo.findAll();
	}

	public List<CategoriaDTO> findAllCategoria() {
		
		List<Categoria> list = repo.findAll();
		List<CategoriaDTO> listDTO = list
				.stream()
				.map(categora -> CategoriaMapper.INSTANCE.categoriaToCategoriaDTO(categora))
				.collect(Collectors.toList());
	
		return listDTO;
	}
	
	public Page<CategoriaDTO> findPage(Integer page, Integer linesPerPage, String orderBy, String direction){
		PageRequest pageRequest =  PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		
		return repo.findAll(pageRequest).map(categora -> CategoriaMapper.INSTANCE.categoriaToCategoriaDTO(categora));
	}
	

	private void updateDate(Categoria categoriaeNew, Categoria categoria) {
		CategoriaMapper.INSTANCE.updateCategoriaData(categoria, categoriaeNew);
		
	}
	
}

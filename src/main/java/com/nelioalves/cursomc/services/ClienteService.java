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

import com.nelioalves.cursomc.domain.Cliente;
import com.nelioalves.cursomc.dto.ClienteDTO;
import com.nelioalves.cursomc.mapper.ClienteMapper;
import com.nelioalves.cursomc.repositories.ClienteRepository;
import com.nelioalves.cursomc.services.exceptions.DataIntegrityException;
import com.nelioalves.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class ClienteService {
	
	@Autowired
	private ClienteRepository repo;
	
	public Cliente find(Integer id) {
		Optional<Cliente> obj = repo.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado! Id: " + id + ", Tipo: " + Cliente.class.getName()));
	}
	

	public Cliente update(Cliente cliente) {
		var clienteNew = find(cliente.getId());
		updateDate(clienteNew, cliente);
		return repo.save(clienteNew);
	}

	public void delete(Integer id) {
		find(id);
		
		try {
			repo.deleteById(id);
		} catch (DataIntegrityViolationException e) {
			throw new DataIntegrityException("Não é possivel excluir uma cliente porque há entidades relacionadas");
		}
		
	}

	public List<Cliente> findAll() {
		
		return repo.findAll();
	}

	public List<ClienteDTO> findAllCliente() {
		
		List<Cliente> list = repo.findAll();
		List<ClienteDTO> listDTO = list
				.stream()
				.map(categora -> ClienteMapper.INSTANCE.clienteToClienteDTO(categora))
				.collect(Collectors.toList());
	
		return listDTO;
	}
	
	public Page<ClienteDTO> findPage(Integer page, Integer linesPerPage, String orderBy, String direction){
		PageRequest pageRequest =  PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		
		return repo.findAll(pageRequest).map(categora -> ClienteMapper.INSTANCE.clienteToClienteDTO(categora));
	}
	
	private void updateDate(Cliente clienteNew, Cliente cliente) {
		clienteNew.setEmail(cliente.getEmail());
		clienteNew.setNome(cliente.getNome());
		
	}
}

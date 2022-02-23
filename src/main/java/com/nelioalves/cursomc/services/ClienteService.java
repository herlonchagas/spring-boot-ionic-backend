package com.nelioalves.cursomc.services;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.nelioalves.cursomc.domain.Cidade;
import com.nelioalves.cursomc.domain.Cliente;
import com.nelioalves.cursomc.domain.Endereco;
import com.nelioalves.cursomc.domain.enums.TipoCliente;
import com.nelioalves.cursomc.dto.ClienteDTO;
import com.nelioalves.cursomc.dto.ClienteNewDTO;
import com.nelioalves.cursomc.mapper.ClienteMapper;
import com.nelioalves.cursomc.repositories.CidadeRepository;
import com.nelioalves.cursomc.repositories.ClienteRepository;
import com.nelioalves.cursomc.repositories.EnderecoRepository;
import com.nelioalves.cursomc.services.exceptions.DataIntegrityException;
import com.nelioalves.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class ClienteService {
	
	@Autowired
	private ClienteRepository clienteRepository;
	
	@Autowired
	private EnderecoRepository enderecoRepository;
	
	@Autowired
	private CidadeRepository cidadeRepository;
	
	public Cliente find(Integer id) {
		Optional<Cliente> obj = clienteRepository.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado! Id: " + id + ", Tipo: " + Cliente.class.getName()));
	}
	
	@Transactional
	public Cliente insert(Cliente cliente) {
		cliente.setId(null);
		cliente = clienteRepository.save(cliente);
		enderecoRepository.saveAll(cliente.getEnderecos());
		return cliente;
	}
	
	public Cliente update(Cliente cliente) {
		var clienteNew = find(cliente.getId());
		updateDate(clienteNew, cliente);
		return clienteRepository.save(clienteNew);
	}

	public void delete(Integer id) {
		find(id);
		
		try {
			clienteRepository.deleteById(id);
		} catch (DataIntegrityViolationException e) {
			throw new DataIntegrityException("Não é possivel excluir uma cliente porque há pedidos relacionadas");
		}
		
	}

	public List<Cliente> findAll() {
		
		return clienteRepository.findAll();
	}

	public List<ClienteDTO> findAllCliente() {
		
		List<Cliente> list = clienteRepository.findAll();
		List<ClienteDTO> listDTO = list
				.stream()
				.map(categora -> ClienteMapper.INSTANCE.clienteToClienteDTO(categora))
				.collect(Collectors.toList());
	
		return listDTO;
	}
	
	public Page<ClienteDTO> findPage(Integer page, Integer linesPerPage, String orderBy, String direction){
		PageRequest pageRequest =  PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		
		return clienteRepository.findAll(pageRequest).map(categora -> ClienteMapper.INSTANCE.clienteToClienteDTO(categora));
	}
	
	private void updateDate(Cliente clienteNew, Cliente cliente) {		
		ClienteMapper.INSTANCE.updateClienteData(cliente, clienteNew);			
	}

	public Cliente fromDTO( ClienteNewDTO clienteDto) {
		Cliente cliente = new Cliente(null, clienteDto.getNome(), clienteDto.getEmail(), clienteDto.getCpfOuCnpj(), TipoCliente.toEnum(clienteDto.getTipo()));
		Cidade cidade = cidadeRepository.findById(clienteDto.getCidadeId()).get();
		Endereco endereco = new Endereco(null,
				clienteDto.getLogradouro(),
				clienteDto.getNumero(),
				clienteDto.getComplemento(),
				clienteDto.getBairro(),
				clienteDto.getCep(),
				cliente,
				cidade);
		
		cliente.getEnderecos().add(endereco);
		cliente.getTelefones().add(clienteDto.getTelefone1());
		if (Objects.nonNull(clienteDto.getTelefone2()))
			cliente.getTelefones().add(clienteDto.getTelefone2());
		
		if (Objects.nonNull(clienteDto.getTelefone3()))
			cliente.getTelefones().add(clienteDto.getTelefone3());
	
		return cliente;	
	}


}

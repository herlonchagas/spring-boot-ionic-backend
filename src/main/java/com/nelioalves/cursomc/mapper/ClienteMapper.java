package com.nelioalves.cursomc.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.nelioalves.cursomc.domain.Cliente;
import com.nelioalves.cursomc.dto.ClienteDTO;

@Mapper
public interface ClienteMapper {
	
	ClienteMapper INSTANCE = Mappers.getMapper(ClienteMapper.class);
	
	ClienteDTO clienteToClienteDTO(Cliente categoria);
	
	Cliente clienteDTOToCliente(ClienteDTO categoriaDto);


}

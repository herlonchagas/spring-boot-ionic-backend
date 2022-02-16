package com.nelioalves.cursomc.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.nelioalves.cursomc.domain.Categoria;
import com.nelioalves.cursomc.dto.CategoriaDTO;

@Mapper
public interface CategoriaMapper {
	
	CategoriaMapper INSTANCE = Mappers.getMapper(CategoriaMapper.class);
	
	CategoriaDTO categoriaToCategoriaDTO(Categoria categoria);
	
	Categoria categoriaDTOToCategoria(CategoriaDTO categoriaDto);


}

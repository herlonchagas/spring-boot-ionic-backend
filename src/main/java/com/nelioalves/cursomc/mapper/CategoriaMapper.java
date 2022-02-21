package com.nelioalves.cursomc.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

import com.nelioalves.cursomc.domain.Categoria;
import com.nelioalves.cursomc.domain.Cliente;
import com.nelioalves.cursomc.dto.CategoriaDTO;

@Mapper
public interface CategoriaMapper {
	
	CategoriaMapper INSTANCE = Mappers.getMapper(CategoriaMapper.class);
	
	CategoriaDTO categoriaToCategoriaDTO(Categoria categoria);
	
	Categoria categoriaDTOToCategoria(CategoriaDTO categoriaDto);
	
	@BeanMapping(nullValuePropertyMappingStrategy =  NullValuePropertyMappingStrategy.IGNORE)
	void updateCategoriaData(Categoria categoria, @MappingTarget Categoria categoriaNew);


}

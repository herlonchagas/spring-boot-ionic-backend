package com.nelioalves.cursomc.services;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.nelioalves.cursomc.domain.Categoria;
import com.nelioalves.cursomc.repositories.CategoriaRepository;
import com.nelioalves.cursomc.services.exceptions.ObjectNotFoundException;



@ExtendWith(MockitoExtension.class)
public class CategoriaServicesTest {
	
	@InjectMocks
	CategoriaService categoriaService;
	
	@Mock
	CategoriaRepository repo;
	
	
	@Test
	void find() {
		Integer id = 1;		
		Optional<Categoria> categoria = Optional.of(new Categoria(1, "teste"));
		
		when(repo.findById(any())).thenReturn(categoria);	

		
		Categoria response = categoriaService.find(id);
		
		Assertions.assertNotNull(response);	
		
	}
	
	@Test
	void findException() {
		Integer id = 1;		
		Optional<Categoria> categoria = Optional.empty();
		
		when(repo.findById(any())).thenReturn(categoria);			
		
		Assertions.assertThrows(ObjectNotFoundException.class, () -> categoriaService.find(id));	
		
	}
	
	@Test
	void insert() {
		Integer id = 1;		
		Categoria categoria = new Categoria(1, "teste");
		
		when(repo.save(any())).thenReturn(categoria);	
		
		Categoria resp = categoriaService.insert(categoria);
		
		Assertions.assertNotNull(resp);	
		Assertions.assertEquals("teste", resp.getNome());
		
	}
	
}

package com.formacionbdi.springboot.app.item.models.service;

import java.util.List;

import org.springframework.stereotype.Component;

import com.formacionbdi.springboot.app.item.models.Item;


public interface ItemService {

	public List<Item> findAll();
	public Item findById(Long id, Integer cantidad);
	
}

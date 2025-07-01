package com.springboot.inventory.controller;

import com.springboot.inventory.dto.InventoryDTO;
import com.springboot.inventory.service.InventoryService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("api/v1/")
public class InventoryController {
    @Autowired
    private InventoryService inventoryService;

    @GetMapping("/getitems")
    public List<InventoryDTO> getItems(){
        return inventoryService.getAllItems();
    }

    @GetMapping("/item/{itemId}")
    public InventoryDTO getItemById(@PathVariable Integer itemId) {
        return inventoryService.getItemById(itemId);
    }

    @PostMapping("/additem")
    public InventoryDTO saveItem(@RequestBody InventoryDTO inventoryDTO) {
        return inventoryService.saveItem(inventoryDTO);
    }

    @PutMapping("/updateitem")
    public InventoryDTO updateItem(@RequestBody InventoryDTO inventoryDTO) {
        return inventoryService.updateItem(inventoryDTO);
    }

    @DeleteMapping("/deleteitem/{itemId}")
    public String deleteItem(@PathVariable Integer itemId) {
        return inventoryService.deleteItem(itemId);
    }
}

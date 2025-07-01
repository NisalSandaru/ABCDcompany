package com.springboot.inventory.repo;

import com.springboot.inventory.model.Inventory;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InventoryRepo extends CrudRepository<Inventory, Integer> {
    @Query(value = "SELECT * FROM inventory WHERE item_id = ?1", nativeQuery = true)
    Inventory getItemById(Integer itemId);
}

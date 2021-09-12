package com.example.demo.Tests;


import com.example.demo.controllers.ItemController;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import java.math.BigDecimal;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;



public class TestingTwoItemController {

    private ItemController itemController;
    private ItemRepository itemRepository = mock(ItemRepository.class);

    @Before
    public void setUp(){
        itemController = new ItemController();
        TestUtils.injectObjects(itemController, "itemRepository", itemRepository);
        Item item = new Item();
        item.setId(1L);
        item.setName("WhatIsLowQuality");
        BigDecimal price = BigDecimal.valueOf(0.99);
        item.setPrice(price);
        item.setDescription("You must be poor to buy me");
        Item item2 = new Item();
        item2.setId(2L);
        item2.setName("WhatIsHighQuality");
        BigDecimal price2 = BigDecimal.valueOf(9.99);
        item2.setPrice(price2);
        item2.setDescription("You must be rich to buy me");
        List<Item> the2items = new ArrayList<Item>();
        the2items.add(item);
        the2items.add(item2);
        when(itemRepository.findAll()).thenReturn(the2items);
        when(itemRepository.findById(1L)).thenReturn(java.util.Optional.of(item));
        when(itemRepository.findById(2L)).thenReturn(java.util.Optional.of(item));
        when(itemRepository.findByName("WhatIsLowQuality")).thenReturn(Collections.singletonList(item));
        when(itemRepository.findByName("WhatIsHighQuality")).thenReturn(Collections.singletonList(item));

    }

    @Test
    public void theItemsExists() {
        ResponseEntity<List<Item>> response = itemController.getItems();
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        List<Item> items = response.getBody();
        assertNotNull(items);
        assertEquals(2, items.size());
    }

    @Test
    public void theIdsOfTheItemExists() {
        ResponseEntity<Item> response = itemController.getItemById(1L);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        Item i = response.getBody();
        assertNotNull(i);
        ResponseEntity<Item> response2 = itemController.getItemById(2L);
        assertNotNull(response2);
        assertEquals(200, response.getStatusCodeValue());
        Item ii = response.getBody();
        assertNotNull(ii);
    }

    @Test
    public void nonToSeeHere() {
        ResponseEntity<Item> response = itemController.getItemById(3L);
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void iKnowTheseNames() {
        ResponseEntity<List<Item>> response = itemController.getItemsByName("WhatIsLowQuality");
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        List<Item> items = response.getBody();
        assertNotNull(items);
        assertEquals(1, items.size());
        ResponseEntity<List<Item>> response2 = itemController.getItemsByName("WhatIsHighQuality");
        assertNotNull(response2);
        assertEquals(200, response.getStatusCodeValue());
        List<Item> alsoitems = response.getBody();
        assertNotNull(alsoitems);
        assertEquals(1, alsoitems.size());
    }

    @Test
    public void iDoNotKnowThisName() {
        ResponseEntity<List<Item>> response = itemController.getItemsByName("WhatIsMediumQuality");
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }
}

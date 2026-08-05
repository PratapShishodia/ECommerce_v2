package com.ps.inventoryservice.service.impl;

import com.ps.inventoryservice.customExceptions.ResourceNotFoundException;
import com.ps.inventoryservice.model.dto.BulkInventoryRequestDTO;
import com.ps.inventoryservice.model.dto.InventoryRequestDTO;
import com.ps.inventoryservice.model.dto.InventoryResponseDTO;
import com.ps.inventoryservice.model.dto.StockOperationRequestDTO;
import com.ps.inventoryservice.model.entity.Inventory;
import com.ps.inventoryservice.model.mapper.InventoryDTOMapper;
import com.ps.inventoryservice.repository.InventoryRepo;
import com.ps.inventoryservice.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepo inventoryRepo;

    @Override
    @Transactional
    public InventoryResponseDTO createInventory(InventoryRequestDTO requestDTO) {
        Inventory inventory = InventoryDTOMapper.toEntity(requestDTO);
        inventory.setInventoryId(UUID.randomUUID().toString().split("-")[0]);
        inventory.setAvailableQuantity(requestDTO.getQuantity());
        inventory.setReservedQuantity(0);
        return InventoryDTOMapper.toDTO(inventoryRepo.save(inventory));
    }

    @Override
    public InventoryResponseDTO getInventoryByProductId(Long productId) {
        return InventoryDTOMapper.toDTO(inventoryRepo.findByProductId(productId).orElseThrow(()->new ResourceNotFoundException("Inventory","Product Id",String.valueOf(productId))));
    }

    @Override
    @Transactional
    public InventoryResponseDTO updateStock(Long productId, InventoryRequestDTO requestDTO) {
        Inventory inventory = inventoryRepo.findByProductId(productId).orElseThrow(()->new ResourceNotFoundException("Inventory","Product Id",String.valueOf(productId)));
        if(requestDTO.getQuantity() <= 0){
            throw new IllegalArgumentException(
                    "Quantity must be greater than zero.");
        }
        inventory.setQuantity(inventory.getQuantity() + requestDTO.getQuantity());
        inventory.setAvailableQuantity(inventory.getAvailableQuantity() + requestDTO.getQuantity());
        return InventoryDTOMapper.toDTO(inventoryRepo.save(inventory));
    }

    @Override
    @Transactional
    public InventoryResponseDTO reserveStock(StockOperationRequestDTO requestDTO) {

        Inventory inventory = inventoryRepo.findByProductId(requestDTO.getProductId()).orElseThrow(()->new ResourceNotFoundException("Inventory","Product Id",String.valueOf(requestDTO.getProductId())));
        if (inventory.getAvailableQuantity() < requestDTO.getQuantity()) {
            throw new IllegalArgumentException("Insufficient available stock.");
        }
        if(requestDTO.getQuantity() <= 0){
            throw new IllegalArgumentException(
                    "Quantity must be greater than zero.");
        }
        inventory.setAvailableQuantity(inventory.getAvailableQuantity() - requestDTO.getQuantity());
        inventory.setReservedQuantity(inventory.getReservedQuantity() + requestDTO.getQuantity());
        return InventoryDTOMapper.toDTO(inventoryRepo.save(inventory));
    }

    @Override
    @Transactional
    public InventoryResponseDTO releaseStock(StockOperationRequestDTO requestDTO) {
        Inventory inventory = inventoryRepo.findByProductId(requestDTO.getProductId()).orElseThrow(()->new ResourceNotFoundException("Inventory","Product Id",String.valueOf(requestDTO.getProductId())));
        System.out.println("INVENTORY RESERVERD STOCK "+ inventory.getReservedQuantity());
        System.out.println("DTO Quantity "+requestDTO.getQuantity());
        if (inventory.getReservedQuantity() < requestDTO.getQuantity()) {
            throw new IllegalArgumentException("Reserved quantity is less than requested quantity.");
        }
        if(requestDTO.getQuantity() <= 0){
            throw new IllegalArgumentException(
                    "Quantity must be greater than zero.");
        }
        inventory.setReservedQuantity(inventory.getReservedQuantity() - requestDTO.getQuantity());
        inventory.setAvailableQuantity(inventory.getAvailableQuantity() + requestDTO.getQuantity());
        return InventoryDTOMapper.toDTO(inventoryRepo.save(inventory));
    }

    @Override
    @Transactional
    public InventoryResponseDTO deductStock(StockOperationRequestDTO requestDTO) {
        Inventory inventory = inventoryRepo.findByProductId(requestDTO.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Inventory",
                        "Product Id",
                        String.valueOf(requestDTO.getProductId())));

        if (requestDTO.getQuantity() <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero.");
        }

        if (inventory.getReservedQuantity() < requestDTO.getQuantity()) {
            throw new IllegalArgumentException(
                    "Reserved quantity is less than requested quantity.");
        }

        if (inventory.getQuantity() < requestDTO.getQuantity()) {
            throw new IllegalArgumentException(
                    "Insufficient stock available.");
        }

        inventory.setReservedQuantity(
                inventory.getReservedQuantity() - requestDTO.getQuantity());

        inventory.setQuantity(
                inventory.getQuantity() - requestDTO.getQuantity());

        Inventory savedInventory = inventoryRepo.save(inventory);

        return InventoryDTOMapper.toDTO(savedInventory);
    }

    @Override
    public boolean checkAvailability(Long productId) {
        Inventory inventory = inventoryRepo.findByProductId(productId).orElseThrow(()->new ResourceNotFoundException("Inventory","Product Id",String.valueOf(productId)));
        return inventory.getAvailableQuantity() > 0;
    }

    @Override
    @Transactional
    public List<InventoryResponseDTO> bulkUpdate(BulkInventoryRequestDTO requestDTO) {
        List<InventoryResponseDTO> responseList = new ArrayList<>();

        for (InventoryRequestDTO dto : requestDTO.getInventories()) {

            Inventory inventory = inventoryRepo.findByProductId(dto.getProductId()).orElse(new Inventory());

            // If new inventory
            if (inventory.getProductId() == null) {
                inventory.setInventoryId(UUID.randomUUID().toString().split("-")[0]);
                inventory.setProductId(dto.getProductId());
                inventory.setReservedQuantity(0);
            }
            if(dto.getQuantity() < inventory.getReservedQuantity()){
                throw new IllegalArgumentException(
                        "Total quantity cannot be less than reserved quantity.");
            }
            inventory.setQuantity(dto.getQuantity());
            inventory.setAvailableQuantity(dto.getQuantity() - inventory.getReservedQuantity());
            inventory.setWarehouseLocation(dto.getWarehouseLocation());

            Inventory savedInventory = inventoryRepo.save(inventory);

            responseList.add(InventoryDTOMapper.toDTO(savedInventory));
        }

        return responseList;
    }
}

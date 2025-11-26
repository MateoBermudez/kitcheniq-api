package com.uni.kitcheniq.service;

import com.uni.kitcheniq.dto.*;
import com.uni.kitcheniq.enums.PurchaseOrderType;
import com.uni.kitcheniq.exception.NoItemFoundException;
import com.uni.kitcheniq.exception.NotEmployees;
import com.uni.kitcheniq.exception.SupplierNotFoundException;
import com.uni.kitcheniq.mapper.*;
import com.uni.kitcheniq.models.*;
import com.uni.kitcheniq.repository.*;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import com.uni.kitcheniq.dto.EmployeeRequest;
import com.uni.kitcheniq.exception.InvalidDataException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

@Service
@AllArgsConstructor
public class AdminService {

    private final InventoryItemRepository inventoryItemRepository;
    private final InventoryItemMapper inventoryItemMapper;
    private final PurchaseOrderMapper purchaseOrderMapper;
    private final PurchaseOrderRepository purchaseOrderRepository;
    private final SupplierRepository supplierRepository;
    private final PurchaseOrderItemMapper purchaseOrderItemMapper;
    private final PurchaseOrderItemRepository purchaseOrderItemRepository;
    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;
    private final SupplierMapper supplierMapper;
    private final EntityManager em;
    private final PasswordEncoder passwordEncoder;

    public String addInventoryItem(InventoryItemDTO inventoryItemDTO) {
        if (inventoryItemDTO == null) {
            throw new NoItemFoundException("Inventory item data is missing");
        }
        InventoryItem item = inventoryItemMapper.toInventoryItem(inventoryItemDTO);

        if (item.getSupplierid() == null) {
            throw new SupplierNotFoundException("Supplier data is missing");
        }

        inventoryItemRepository.save(item);
        return String.format("Successfully added item: %s", item.getName());
    }

    public List<InventoryItemDTO> getAllInventoryItems() {
        List<InventoryItem> items = inventoryItemRepository.findAll();
        List<InventoryItemDTO> itemDTOs = new ArrayList<>();
        for (InventoryItem item : items) {
            itemDTOs.add(inventoryItemMapper.toInventoryItemDTO(item));
        }

        return itemDTOs;
    }

    public PurchaseOrderDTO createPurchaseOrder(SupplierDTO supplierDTO) {
        PurchaseOrder purchaseOrder = PurchaseOrder.builder()
                .createdAt(java.time.LocalDateTime.now())
                .updatedAt(java.time.LocalDateTime.now())
                .status(PurchaseOrderType.PENDING)
                .totalAmount(0.0)
                .build();

        Optional<Supplier> supplier = supplierRepository.findById(supplierDTO.getId());
        if (supplier.isPresent()) {
            purchaseOrder.setSupplier(supplier.get());
        } else {
            throw new SupplierNotFoundException("Supplier not found with ID: " + supplierDTO.getId());
        }

        PurchaseOrder saved = purchaseOrderRepository.save(purchaseOrder);
        PurchaseOrderDTO orderDTO = purchaseOrderMapper.toDTO(saved);

        return orderDTO;
    }

    public PurchaseOrderDTO addItemsToOrder(PurchaseOrderItemDTO itemDTO) {
        PurchaseOrderItem item = purchaseOrderItemMapper.toEntity(itemDTO);
        purchaseOrderItemRepository.save(item);
        purchaseOrderRepository.addToTotalAmountById(item.getPurchaseOrder().getId(), item.getSubTotalPrice());
        PurchaseOrder saved = purchaseOrderRepository.findPurchaseOrderByIdWithItems(item.getPurchaseOrder().getId());

        PurchaseOrderDTO orderDTO = purchaseOrderMapper.toDTO(saved);
        return orderDTO;
    }

    public PurchaseOrderDTO eliminateItemsFromOrder(PurchaseOrderItemDTO itemDTO) {
        purchaseOrderItemRepository.deleteItemFromOrder(itemDTO.getItemId(), itemDTO.getOrderId());
        purchaseOrderRepository.updateTotalPriceById(itemDTO.getOrderId(), itemDTO.getSubTotal());

        PurchaseOrder saved = purchaseOrderRepository.findPurchaseOrderByIdWithItems(itemDTO.getOrderId());
        PurchaseOrderDTO orderDTO = purchaseOrderMapper.toDTO(saved);
        return orderDTO;
    }

    @Transactional
    public PurchaseOrderDTO finalizePurchaseOrder(Long orderId) {
        PurchaseOrder order = purchaseOrderRepository.findPurchaseOrderByIdWithItems(orderId);
        if (order.getTotalAmount() == 0) {
            purchaseOrderRepository.updateStatusById(orderId, PurchaseOrderType.CANCELLED);
            em.flush();
            em.refresh(order);
        }

        PurchaseOrder saved = purchaseOrderRepository.findPurchaseOrderByIdWithItems(orderId);
        PurchaseOrderDTO orderDTO = purchaseOrderMapper.toDTO(saved);
        return orderDTO;
    }

    @Transactional
    public String cancelPurchaseOrder(Long orderId) {
        PurchaseOrder order = purchaseOrderRepository.findPurchaseOrderByIdWithItems(orderId);
        for (PurchaseOrderItem item : order.getItems()) {
            purchaseOrderItemRepository.deleteItemFromOrder(item.getId(), orderId);
        }
        purchaseOrderRepository.deletePurchaseOrderById(orderId);
        return "Purchase order cancelled successfully";
    }

    public List<InventoryItemDTO> searchItemsByName (String name) {
        if (name == null || name.trim().isEmpty()) {
            return getAllInventoryItems();
        }
        List<InventoryItem> items = inventoryItemRepository.findByNameContainingIgnoreCase(name.trim());
        List<InventoryItemDTO> result = new ArrayList<>();
        for (InventoryItem item : items) {
            result.add(inventoryItemMapper.toInventoryItemDTO(item));
        }
        return result;
    }

    public List<EmployeeDTO> getAllEmployees() {
        Optional<List<Employee>> employees = employeeRepository.getAllEmployees();
        if (!employees.isPresent()) {
           throw new NotEmployees("No employees found");
        }
        List<EmployeeDTO> employeeDTOs = new ArrayList<>();
        for (Employee emp : employees.get()) {
            employeeDTOs.add(employeeMapper.toEmployeeDTO(emp));
        }
        return employeeDTOs;
    }

    @Transactional
    public String deleteEmployee(String id){
        Optional<Employee> employee = employeeRepository.findById(id);
        if (employee.isPresent()){
            employeeRepository.deleteById(id);
            return "Employee deleted successfully";
        } else {
            throw new NotEmployees("Employee not found with ID: " + id);
        }
    }

    public List<SupplierDTO> getAllSuppliers() {
        List<Supplier> suppliers = supplierRepository.findAll();
        if (suppliers.isEmpty()) {
            throw new SupplierNotFoundException("No suppliers found");
        } else {
            List<SupplierDTO> supplierDTOs = new ArrayList<>();
            for (Supplier sup : suppliers) {
                supplierDTOs.add(supplierMapper.toSupplierDTO(sup));
            }
            return supplierDTOs;
        }
    }

    public List<InventoryItemDTO> getInventoryItemsBySupplierId(String supplierId) {
        List<InventoryItem> items = inventoryItemRepository.getInventoryItemsBySupplierId(supplierId);
        if (items.isEmpty()) {
            throw new NoItemFoundException("No inventory items found for supplier ID: " + supplierId);
        }
        List<InventoryItemDTO> itemDTOs = new ArrayList<>();
        for (InventoryItem item : items) {
            itemDTOs.add(inventoryItemMapper.toInventoryItemDTO(item));
        }
        return itemDTOs;
    }


    @Transactional
    public String registerEmployee(EmployeeRequest employeeRequest) {
        if (!employeeRequest.getId().matches("\\d+")) {
            throw new InvalidDataException("ID must contain only numbers.");
        }
        if (employeeRepository.existsById(employeeRequest.getId())) {
            throw new InvalidDataException("Employee with this ID already exists.");
        }
        if (employeeRequest.getHourlyRate() <= 0) {
            throw new InvalidDataException("Hourly rate must be a positive number.");
        }

        Employee employee = employeeMapper.toEntity(employeeRequest);
        employee.setPassword(passwordEncoder.encode(employeeRequest.getPassword()));

        employeeRepository.save(employee);

        return "Employee registered successfully.";
    }

    @Transactional
    public String editEmployee(String id, EmployeeRequest employeeRequest) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new NotEmployees("Employee not found with ID: " + id));

        // Validar y actualizar campos
        if (employeeRequest.getName() != null) {
            employee.setName(employeeRequest.getName());
        }
        if (employeeRequest.getLastName() != null) {
            employee.setLastName(employeeRequest.getLastName());
        }
        if (employeeRequest.getEmployeeType() != null) {
            employee.setType(employeeRequest.getEmployeeType());
        }
        if (employeeRequest.getHourlyRate() != null) {
            if (employeeRequest.getHourlyRate() <= 0) {
                throw new InvalidDataException("Hourly rate must be a positive number.");
            }
            employee.setHourlyRate(employeeRequest.getHourlyRate());
        }
        if (employeeRequest.getPassword() != null && !employeeRequest.getPassword().isEmpty()) {
            employee.setPassword(passwordEncoder.encode(employeeRequest.getPassword()));
        }

        // Manejar cambio de ID (PK)
        if (employeeRequest.getId() != null && !employeeRequest.getId().equals(id)) {
            if (!employeeRequest.getId().matches("\\d+")) {
                throw new InvalidDataException("New ID must contain only numbers.");
            }
            if (employeeRepository.existsById(employeeRequest.getId())) {
                throw new InvalidDataException("An employee with the new ID already exists.");
            }
            // Como el ID es la clave primaria, eliminamos el antiguo y creamos uno nuevo
            employeeRepository.deleteById(id);
            employee.setId(employeeRequest.getId());
        }

        employeeRepository.save(employee);

        return "Employee updated successfully.";
    }

}

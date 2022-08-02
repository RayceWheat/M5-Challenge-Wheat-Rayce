package com.example.gamestoreinvoicing.controller;

import com.example.gamestoreinvoicing.model.*;
import com.example.gamestoreinvoicing.service.GameStoreInvoiceServiceLayer;
import com.example.gamestoreinvoicing.util.feign.GameStoreCatalog;
import com.netflix.discovery.converters.Auto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.ws.rs.Path;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/invoice")
public class InvoiceController {

    @Autowired
    GameStoreInvoiceServiceLayer service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Invoice purchaseItem(@RequestBody @Valid Invoice invoice){
        invoice = service.createNewInvoice(invoice);
        return invoice;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Invoice> findAllInvoices(){
        List<Invoice> allInvoices = service.getAllInvoices();
        if (allInvoices == null){
            throw new IllegalArgumentException("No invoices found");
        } else {
            return allInvoices;
        }
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Invoice findInvoice(@PathVariable("id") long invoiceId){
        Invoice invoice = service.getInvoice(invoiceId);
        if (invoice == null){
            throw new IllegalArgumentException("Invoice could not found with this id " + invoiceId);
        } else {
            return invoice;
        }
    }

    @GetMapping("/cname/{name}")
    @ResponseStatus(HttpStatus.OK)
    public List<Invoice> findInvoicesByCustomerName(@PathVariable String name){
        List<Invoice> invoiceList = service.getInvoicesByCustomerName(name);
        if (invoiceList == null){
            throw new IllegalArgumentException("No invoice found with this name: " + name);
        } else {
            return invoiceList;
        }
    }

}

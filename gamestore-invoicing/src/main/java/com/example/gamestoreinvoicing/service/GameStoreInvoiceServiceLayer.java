package com.example.gamestoreinvoicing.service;

import com.example.gamestoreinvoicing.model.*;
import com.example.gamestoreinvoicing.repository.InvoiceRepository;
import com.example.gamestoreinvoicing.repository.ProcessingFeeRepository;
import com.example.gamestoreinvoicing.repository.TaxRepository;
import com.example.gamestoreinvoicing.util.feign.GameStoreCatalog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;

@Component
public class GameStoreInvoiceServiceLayer {

    @Autowired
    private GameStoreCatalog client;

    void InvoiceController(GameStoreCatalog client){
        this.client = client;
    }

    private final BigDecimal PROCESSING_FEE = new BigDecimal("15.49");

    private final BigDecimal MAX_INVOICE_TOTAL = new BigDecimal("999.99");

    private final String GAME_ITEM_TYPE = "Game";
    private final String CONSOLE_ITEM_TYPE = "Console";
    private final String TSHIRT_ITEM_TYPE = "T-Shirt";


    TaxRepository taxRepository;

    InvoiceRepository invoiceRepository;

    ProcessingFeeRepository processingFeeRepository;

    @Autowired
    public GameStoreInvoiceServiceLayer(GameStoreCatalog client, TaxRepository taxRepo, InvoiceRepository invoiceRepo, ProcessingFeeRepository processingFeeRepo){
        this.client = client;
        this.invoiceRepository = invoiceRepo;
        this.taxRepository = taxRepo;
        this.processingFeeRepository = processingFeeRepo;
    }


    public Invoice createNewInvoice(Invoice newInvoice){
        if(newInvoice == null){
            throw new IllegalArgumentException("Invoice must not be bank");
        }

        if(newInvoice.getItemType() == null){
            throw new IllegalArgumentException("Item must be Console, TShrit or Game");
        }

        if(newInvoice.getQuantity()<=0){
            throw new IllegalArgumentException(newInvoice.getQuantity() + ": Unrecofgnzined Quantity. Must be > 0.");
        }

        //Checks Item type and correct price
        //Checks if enough quantity
        if (newInvoice.getItemType().equals(GAME_ITEM_TYPE)){
            Game tempGame = null;
            Optional<Game> returnVal = Optional.ofNullable(client.getGameById(newInvoice.getItemId()));

            if(returnVal.isPresent()){
                tempGame = returnVal.get();
            } else {
                throw new IllegalArgumentException("Requested item is unavailable");
            }

            if (newInvoice.getQuantity()> tempGame.getQuantity()){
                throw new IllegalArgumentException("Requested quantity is unavailable");
            }

            newInvoice.setUnitPrice(tempGame.getPrice());

        } else if (newInvoice.getItemType().equals(CONSOLE_ITEM_TYPE)) {
            Console tempCon = null;
            Optional<Console> returnVal = Optional.ofNullable(client.getConsoleById(newInvoice.getItemId()));

            if(returnVal.isPresent()){
                tempCon = returnVal.get();
            } else {
                throw new IllegalArgumentException("Requested item is unavailable.");
            }

            if (newInvoice.getQuantity()> tempCon.getQuantity()){
                throw new IllegalArgumentException("Requested quantity is unavailable.");
            }

            newInvoice.setUnitPrice(tempCon.getPrice());
        } else if (newInvoice.getItemType().equals(TSHIRT_ITEM_TYPE)) {
            TShirt tempTShirt = null;
            Optional<TShirt> returnVal = Optional.ofNullable(client.getTShirtById(newInvoice.getItemId()));

            if(returnVal.isPresent()){
                tempTShirt = returnVal.get();
            } else {
                throw new IllegalArgumentException("Requested item is unavailable.");
            }

            if(newInvoice.getQuantity() > tempTShirt.getQuantity()){
                throw new IllegalArgumentException("Requested quantity is unavailable.");
            }

            newInvoice.setUnitPrice(tempTShirt.getPrice());

        } else {
            throw new IllegalArgumentException(newInvoice.getItemType() +
                    ": Valid item types are only: T-Shirt, Console, Game");
        }

        newInvoice.setSubtotal(
                newInvoice.getUnitPrice().multiply(
                        new BigDecimal(newInvoice.getQuantity())).setScale(2, RoundingMode.HALF_UP));


        //Throw exception if subtotal is greater than 999.99
        if ((newInvoice.getSubtotal().compareTo(new BigDecimal(999.99)) > 0)){
            throw new IllegalArgumentException("Subtaol exceeds maxium purchase price of $999.99");
        }

        //Validate STatte and Calc tax...
        BigDecimal tempTaxRate;
        Optional<Tax> returnVal = taxRepository.findById(newInvoice.getState());

        if (returnVal.isPresent()){
            tempTaxRate = returnVal.get().getRate();
        } else {
            throw new IllegalArgumentException(newInvoice.getState() + ": Invalid State code.");
        }

        if(!tempTaxRate.equals(BigDecimal.ZERO))
            newInvoice.setTax(tempTaxRate.multiply(newInvoice.getSubtotal()));
        else
            throw new IllegalArgumentException(newInvoice.getState() + ": Invalid State code.");

        BigDecimal processingFee;
        Optional<ProcessingFee> returnVal2 = processingFeeRepository.findById(newInvoice.getItemType());

        if(returnVal2.isPresent()){
            processingFee = returnVal2.get().getFee();
        } else {
            throw new IllegalArgumentException("Request item is unavailable.");
        }

        newInvoice.setProcessingFee(processingFee);

        if(newInvoice.getQuantity() > 10){
            newInvoice.setProcessingFee(newInvoice.getProcessingFee().add(PROCESSING_FEE));
        }

        newInvoice.setTotal(newInvoice.getSubtotal().add(newInvoice.getProcessingFee()).add(newInvoice.getTax()));

        //Checks total for validation
        if((newInvoice.getTotal().compareTo(MAX_INVOICE_TOTAL) > 0)){
            throw new IllegalArgumentException("Subtotal exceeds maximum purchase price of $999.99");
        }

        invoiceRepository.save(newInvoice);

        return newInvoice;
    }

    public Invoice getInvoice(long id){
        Optional<Invoice> invoice = invoiceRepository.findById(id);
        if (invoice == null){
            return null;
        } else {
            return invoice.get();
        }
    }

    public List<Invoice> getAllInvoices(){
        List<Invoice> invoiceList = invoiceRepository.findAll();
        if (invoiceList == null){
            return null;
        } else {
            return invoiceList;
        }
    }

    public List<Invoice> getInvoicesByCustomerName(String name){
        List<Invoice> invoiceList = invoiceRepository.findByName(name);
        if (invoiceList == null){
            return null;
        } else {
            return invoiceList;
        }
    }

    public void deleteInvoice(long id){
        invoiceRepository.deleteById(id);
    }

}

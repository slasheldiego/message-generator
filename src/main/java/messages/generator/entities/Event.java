
package messages.generator.entities;

import java.util.Date;

public class Event{
    private Long id;
    private Date date;
    private int quantity;
    private double unitPrice;
    private Long customerId;
    private String country;
    private String description;

    public Event(Long id, 
                Date date, 
                int quantity, 
                double unitPrice,
                Long customerId,
                String country,
                String description){
        this.id = id;
        this.date = date;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.customerId = customerId;
        this.country = country;
        this.description = description;
    }

    public void setId(Long id){
        this.id = id;
    }

    public Long getId(){
        return this.id;
    }

    public void setDate(Date date){
        this.date = date;
    }

    public Date getDate(){
        return this.date;
    }

    public void setQuantity(int quantity){
        this.quantity = quantity;
    }

    public int getQuantity(){
        return this.quantity;
    }

    public void setUnitPrice(double unitPrice){
        this.unitPrice = unitPrice;
    }

    public Double getUnitPrice(){
        return this.unitPrice;
    }

    public void setCustomerId(Long customerId){
        this.customerId = customerId;
    }

    public Long getCustomerId(){
        return this.customerId;
    }

    public void setCountry(String country){
        this.country = country;
    }

    public String getCountry(){
        return this.country;
    }

    public void setDescription(String description){
        this.description = description;
    }

    public String getDescription(){
        return this.description;
    }
}
package model;

import manager.Basket;

import java.sql.Date;
import java.util.List;

public class Order
{
    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public enum Status
    {
        PENDING, ACCEPTED, COMPLETED, CANCELED
    }
    private int id;
    private List<Item> items;
    private Date orderTime;
    private Date requestedDeliveryTime;
    private Date deliveryTime;
    private String deliveryAddress;
    private int customerID;
    private int carrierID;
    private Status status;

    public Order(int id, List<Item> items, Date orderTime, Date requestedDeliveryTime, Date deliveryTime, String deliveryAddress, int customerID, int carrierID, Status status)
    {
        this.id = id;
        this.items = items;
        this.orderTime = orderTime;
        this.requestedDeliveryTime = requestedDeliveryTime;
        this.deliveryTime = deliveryTime;
        this.carrierID = carrierID;
        this.customerID = customerID;
        this.status = status;
    }

    public Date getOrderTime()
    {
        return orderTime;
    }

    public void setOrderTime(Date orderTime)
    {
        this.orderTime = orderTime;
    }

    public Date getDeliveryTime()
    {
        return deliveryTime;
    }

    public void setDeliveryTime(Date deliveryTime)
    {
        this.deliveryTime = deliveryTime;
    }

    public int getCarrierID()
    {
        return carrierID;
    }

    public void setCarrierID(int carrierID)
    {
        this.carrierID = carrierID;
    }

    public int getCustomerID()
    {
        return customerID;
    }

    public void setCustomerID(int customerID)
    {
        this.customerID = customerID;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getRequestedDeliveryTime() {
        return requestedDeliveryTime;
    }

    public void setRequestedDeliveryTime(Date requestedDeliveryTime) {
        this.requestedDeliveryTime = requestedDeliveryTime;
    }
}

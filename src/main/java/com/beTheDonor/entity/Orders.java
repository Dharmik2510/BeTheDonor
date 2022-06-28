package com.beTheDonor.entity;

import com.beTheDonor.model.PatientRiderModel;
import com.beTheDonor.model.ReadyToDeliverModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
@NamedNativeQuery(name = "Orders.getByCityName",
        query = "select  o.order_id, p.product_name,oi.quantity,au.firstname,da.address,da.city,da.postal_code,au.phone_number from product p " +
                "inner join order_item oi on p.product_id = oi.product_id " +
                "inner join orders o on oi.order_id = o.order_id " +
                "inner join delivery_address da on o.address_id = da.address_id " +
                "inner join application_user au on o.user_id = au.id " +
                "where city = ?1 and o.order_status = 'pending delivery'",
        resultSetMapping = "PatientRiderModel")
@SqlResultSetMapping(name = "PatientRiderModel",classes = @ConstructorResult(targetClass = PatientRiderModel.class,columns = { @ColumnResult(name = "order_id"),@ColumnResult(name = "product_name"),@ColumnResult(name = "quantity"),@ColumnResult(name = "firstname"),@ColumnResult(name = "address"),@ColumnResult(name = "city"),@ColumnResult(name = "postal_code"),@ColumnResult(name = "phone_number")}))
@NamedNativeQuery(name = "Orders.getPendingOrders",
        query = "select order_id, da.address , firstname , phone_number from application_user " +
                "inner join orders o on application_user.id = o.user_id " +
                "inner join delivery_address da on o.address_id = da.address_id " +
                "where order_status IN ('ready to deliver', 'delivered')",
                resultSetMapping = "ReadyToDeliverModel")
@SqlResultSetMapping(name = "ReadyToDeliverModel",classes = @ConstructorResult(targetClass = ReadyToDeliverModel.class,columns = { @ColumnResult(name = "order_id"),@ColumnResult(name = "address"),@ColumnResult(name = "firstname"),@ColumnResult(name = "phone_number")}))

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Orders {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date orderPlacedOn;
    @PrePersist
    private void onCreate() {
        orderPlacedOn = new Date();
    }
    @Column
    private Date orderPaidOn;
    @Column
    private Date orderDeliveredOn;
    @ManyToOne
    @JoinColumn(name="userId")
    private ApplicationUser userId;
    @Column
    private Double total;
    @Column
    private String orderStatus;
    @Column
    private Double riderTip;
    @ManyToOne
    @JoinColumn(name="addressId")
    private DeliveryAddress deliveryAddressId;

    public DeliveryAddress getDeliveryAddressId() {
        return deliveryAddressId;
    }

    public void setDeliveryAddressId(DeliveryAddress deliveryAddressId) {
        this.deliveryAddressId = deliveryAddressId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public ApplicationUser getUserId() {
        return userId;
    }

    public void setUserId(ApplicationUser userId) {
        this.userId = userId;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double totalPrice) {
        total = totalPrice;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String order_status) {
        this.orderStatus = order_status;
    }

    public Date getOrderPlacedOn() {
        return orderPlacedOn;
    }

    public void setOrderPlacedOn(Date timestamp) {
        this.orderPlacedOn = timestamp;
    }

    public Date getOrderPaidOn() {
        return orderPaidOn;
    }

    public void setOrderPaidOn(Date orderPaidOn) {
        this.orderPaidOn = orderPaidOn;
    }

    public Date getOrderDeliveredOn() {
        return orderDeliveredOn;
    }

    public void setOrderDeliveredOn(Date orderDeliveredOn) {
        this.orderDeliveredOn = orderDeliveredOn;
    }

    public Double getRiderTip() {
        return riderTip;
    }

    public void setRiderTip(Double riderTip) {
        this.riderTip = riderTip;
    }
}

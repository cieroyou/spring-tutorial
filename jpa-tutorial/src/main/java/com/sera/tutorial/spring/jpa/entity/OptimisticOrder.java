package com.sera.tutorial.spring.jpa.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 배송중 상태인 경우에는 배송지를 수정할 수 없다.
 */
@Entity
@Getter
@Table(name = "optimistic_orders")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OptimisticOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 배송상태
    @Enumerated(EnumType.STRING)
    private DeliveryStatus deliveryStatus;

    private String address;

    @Version
    private Integer version;

    public OptimisticOrder(String address) {
        this.deliveryStatus = DeliveryStatus.READY;
        this.address = address;
    }

    public void updateAddress(String address) {
        if (this.deliveryStatus != DeliveryStatus.READY) {
            throw new IllegalStateException("배송 전 상태에서만 배송지를 수정할 수 있다.");
        }
        this.address = address;
    }

    public void startDelivery() {
        if (this.deliveryStatus != DeliveryStatus.READY) {
            throw new IllegalStateException("배송 전 상태에서만 배송을 시작할 수 있다.");
        }
        this.deliveryStatus = DeliveryStatus.SHIPPING;
    }

    public void completeDelivery() {
        if (this.deliveryStatus != DeliveryStatus.SHIPPING) {
            throw new IllegalStateException("배송 중 상태에서만 배송을 완료할 수 있다.");
        }
        this.deliveryStatus = DeliveryStatus.DELIVERED;
    }

    public void failDelivery() {
        if (this.deliveryStatus != DeliveryStatus.SHIPPING) {
            throw new IllegalStateException("배송 중 상태에서만 배송을 실패할 수 있다.");
        }
        this.deliveryStatus = DeliveryStatus.FAILED_DELIVERY;
    }

    enum DeliveryStatus {
        READY, // 배송준비
        SHIPPING, // 배송중
        DELIVERED, // 배송완료
        FAILED_DELIVERY // 배송실패
    }
}

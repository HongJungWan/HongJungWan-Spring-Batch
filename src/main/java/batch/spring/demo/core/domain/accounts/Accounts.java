package batch.spring.demo.core.domain.accounts;

import batch.spring.demo.core.domain.orders.Orders;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@NoArgsConstructor
@Getter
@Entity
public class Accounts {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String orderItem;
    private Integer price;
    private Date orderDate;
    private Date accountDate;

    public Accounts(Orders orders) {
        this.id = orders.getId();
        this.orderItem = orders.getOrderItem();
        this.price = orders.getPrice();
        this.orderDate = orders.getOrderDate();
        this.accountDate = new Date();
    }

    @Override
    public String toString() {
        return "Accounts{" +
                "id=" + id +
                ", orderItem='" + orderItem + '\'' +
                ", price=" + price +
                ", orderDate=" + orderDate +
                ", accountDate=" + accountDate +
                '}';
    }
}
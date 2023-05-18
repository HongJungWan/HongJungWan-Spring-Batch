package batch.spring.demo.core.domain.orders;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
public class Orders {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String orderItem;
    private Integer price;
    private Date orderDate;

    @Override
    public String toString() {
        return "Orders{" +
                "id=" + id +
                ", orderItem='" + orderItem + '\'' +
                ", price=" + price +
                ", orderDate=" + orderDate +
                '}';
    }
}
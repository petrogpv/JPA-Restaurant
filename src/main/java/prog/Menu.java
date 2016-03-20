package prog;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
@Table(name="Menu")
@NamedQuery(name="Menu.getOne", query = "SELECT c FROM Menu c WHERE c.id = :id")
public class Menu {

    @Id
    @GeneratedValue
    private long id;

    @Column(nullable = false)
    private String name;
    private double weight;
    private double price;
    private boolean discount;


    public Menu() {}

    public Menu(String name, double weight, double price, boolean discount) {
        this.name = name;
        this.weight = weight;
        this.price = price;
        this.discount = discount;
    }

    public double getWeight() {
        return this.weight;
    }

    @Override
    public String toString() {
        return "Menu{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", weight=" + weight +
                ", price=" + price +
                ", discount=" + discount +
                '}';
    }
}

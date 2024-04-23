package org.ox.link_generator.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Invoice {
    @Id
    private String id;
    private Date date;
    private String customerId;
    private double amount;
    private String currency;
}

package org.ox.link_generator.repos;

import org.ox.link_generator.entities.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvoiceRepo extends JpaRepository<Invoice, String> {
}

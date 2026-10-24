package mx.edu.utez.sistematickets.repository;

import mx.edu.utez.sistematickets.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {

    // JOIN FETCH evita LazyInitializationException al armar el DTO con el usuario
    @Query("SELECT t FROM Ticket t JOIN FETCH t.usuario u WHERE u.id = :usuarioId ORDER BY t.id")
    List<Ticket> buscarPorUsuario(@Param("usuarioId") Long usuarioId);
}

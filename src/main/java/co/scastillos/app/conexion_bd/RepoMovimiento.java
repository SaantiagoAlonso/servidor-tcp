package co.scastillos.app.conexion_bd;

import co.scastillos.app.entidades.Movimiento;
import co.scastillos.app.entidades.Usuario;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.List;

public class RepoMovimiento {

    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("miUnidadPersistencia");

    public void guardar (Movimiento movimiento){
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.persist(movimiento);
        em.getTransaction().commit();
        em.close();
    }

    public Movimiento buscarPorId(Long id) {
        EntityManager em = emf.createEntityManager();
        Movimiento movimiento = em.find(Movimiento.class, id);
        em.close();
        return movimiento;
    }

    public List<Movimiento> listarTodos() {
        EntityManager em = emf.createEntityManager();
        List<Movimiento> movimientos = em.createQuery("SELECT m FROM Movimiento m", Movimiento.class).getResultList();
        em.close();
        return movimientos;
    }

    public void eliminar(Long id) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        Movimiento movimiento = em.find(Movimiento.class, id);
        if (movimiento != null) {
            em.remove(movimiento);
        }
        em.getTransaction().commit();
        em.close();
    }
}

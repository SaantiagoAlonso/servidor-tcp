package co.scastillos.app.conexion_bd;

import co.scastillos.app.entidades.Cuenta;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

public class RepoCuenta {
    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("miUnidadPersistencia");

    public void guardar(Cuenta cuenta) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.persist(cuenta);
        em.getTransaction().commit();
        em.close();
    }

    public Cuenta buscarPorId(Long id) {
        EntityManager em = emf.createEntityManager();
        Cuenta cuenta = em.find(Cuenta.class, id);
        em.close();
        return cuenta;
    }

    public List<Cuenta> listarTodas() {
        EntityManager em = emf.createEntityManager();
        List<Cuenta> cuentas = em.createQuery("SELECT c FROM Cuenta c", Cuenta.class).getResultList();
        em.close();
        return cuentas;
    }

    public void eliminar(Long id) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        Cuenta cuenta = em.find(Cuenta.class, id);
        if (cuenta != null) {
            em.remove(cuenta);
        }
        em.getTransaction().commit();
        em.close();
    }

//    public Double obtenerSaldoPorNumeroCuenta(Integer numeroCuenta) {
//        EntityManager em = emf.createEntityManager();
//        Double saldo = em.createQuery("SELECT c.saldo FROM Cuenta c WHERE c.NCuenta = :numeroCuenta", Double.class)
//                .setParameter("numeroCuenta", numeroCuenta)
//                .getSingleResult();
//        em.close();
//        return saldo;
//    }
//
//    public Double obtenerSaldoPorCedulaUsuario(Integer cedula) {
//        EntityManager em = emf.createEntityManager();
//        Double saldo = em.createQuery("SELECT c.saldo FROM Cuenta c WHERE c.usuario.cedula = :cedula", Double.class)
//                .setParameter("cedula", cedula)
//                .getSingleResult();
//        em.close();
//        return saldo;
//    }


    public Double obtenerSaldoPorNumeroCuenta(Integer numeroCuenta) {
        EntityManager em = emf.createEntityManager();
        Double saldo = null;
        try {
            List<Double> resultados = em.createQuery("SELECT c.saldo FROM Cuenta c WHERE c.NCuenta = :numeroCuenta", Double.class)
                    .setParameter("numeroCuenta", numeroCuenta)
                    .getResultList();

            if (!resultados.isEmpty()) {
                saldo = resultados.get(0); // Tomar el primer resultado si existe
            }
        } catch (Exception e) {
            System.out.println("Error al buscar el saldo: " + e.getMessage());
        } finally {
            em.close();
        }
        return saldo;
    }


    public Double obtenerSaldoPorCedulaUsuario(Integer cedula) {
        EntityManager em = emf.createEntityManager();
        Double saldo = null;
        try {
            List<Double> resultados = em.createQuery("SELECT c.saldo FROM Cuenta c WHERE c.usuario.cedula = :cedula", Double.class)
                    .setParameter("cedula", cedula)
                    .getResultList();

            if (!resultados.isEmpty()) {
                saldo = resultados.get(0); // Tomar el primer resultado si existe
            }
        } catch (Exception e) {
            System.out.println("Error al buscar el saldo: " + e.getMessage());
        } finally {
            em.close();
        }
        return saldo;
    }

    public Optional<Cuenta> buscarPorNCuenta(Integer nCuenta){
        EntityManager em = emf.createEntityManager();
        String query = "SELECT c FROM Cuenta c WHERE c.nCuenta = :nCuenta";
        return em.createQuery(query, Cuenta.class)
                .setParameter("nCuenta",nCuenta)
                .getResultStream()
                .findFirst();
    }
    public Optional<Cuenta> buscarPorCedula(Integer cedula){
        EntityManager em = emf.createEntityManager();
        String query = "SELECT c FROM Cuenta c WHERE c.usuario.cedula = :cedula";
        return em.createQuery(query, Cuenta.class)
                .setParameter("cedula",cedula)
                .getResultStream()
                .findFirst();
    }

//    public Cuenta buscarPorCedula(Integer cedula) {
//        EntityManager em = emf.createEntityManager();
//        Cuenta cuenta = em.find(Cuenta.class, cedula);
//        em.close();
//        return cuenta;
//    }



}

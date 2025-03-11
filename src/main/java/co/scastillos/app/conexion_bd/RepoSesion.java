//package co.scastillos.app.conexion_bd;
//
//import co.scastillos.app.entidades.Sesion;
//import jakarta.persistence.EntityManager;
//import jakarta.persistence.EntityManagerFactory;
//import jakarta.persistence.Persistence;
//
//public class RepoSesion {
//
//    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("miUnidadPersistencia");
//
//    public void guardar(Sesion sesion){
//        EntityManager em = emf.createEntityManager();
//        em.getTransaction().begin();
//        em.merge(sesion);
//        em.getTransaction().commit();
//        em.close();
//    }
////
//
//
//
//
//}

package hellojpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class JpaMain {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager(); //db connection 하나 받아왔다고 생각하면 된다.

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        /**
         * code
         */
        Member member = new Member();
        member.setId(1L);
        member.setName("HelloA");
        em.persist(member); //transaction 안에서 이루어져야 member 가 저장된다.

        tx.commit();
        em.close();
        emf.close();
    }
}

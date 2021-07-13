package hellojpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class JpaMain {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager(); //db connection 하나 받아왔다고 생각하면 된다.

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        /**
         * code
         */
        try {
            //등록
//            Member member = new Member();
//            member.setId(2L);
//            member.setName("HelloB");
//            em.persist(member); //transaction 안에서 이루어져야 member 가 저장된다.
//
//            tx.commit();

            //조회
//            Member findMember = em.find(Member.class, 1L);
//            System.out.println("findMember.id() = " + findMember.getId());
//            System.out.println("findMember.name() = " + findMember.getName());
//            tx.commit();

            //삭제
//            Member findMember = em.find(Member.class, 1L);
//            em.remove(findMember);
//            tx.commit();

            //수정
//            Member findMember = em.find(Member.class, 1L);
//            findMember.setName("HelloJPA");
            /*
            em.persist(findMember); 를 안해도 된다.
            jpa 가 관리를 하면서 변경이 되었는지 commit 시점에서 체크를 해서 변경 사항이 있으면 update query 가 나간다.
             */
//            tx.commit();
            
            //JPQL 
            //대상이 테이블이 아니고 객체이다.
            List<Member> result = em.createQuery("select m from Member as m", Member.class)
//                    .setFirstResult(5) /* pagination */
//                    .setMaxResults(8) /* pagination */
                    .getResultList();
            for (Member member : result) {
                System.out.println("member.getName() = " + member.getName());
            }
            tx.commit();

        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }
        emf.close();
    }
}

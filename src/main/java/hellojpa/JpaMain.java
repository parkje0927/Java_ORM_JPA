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
//            List<Member> result = em.createQuery("select m from Member as m", Member.class)
////                    .setFirstResult(5) /* pagination */
////                    .setMaxResults(8) /* pagination */
//                    .getResultList();
//            for (Member member : result) {
//                System.out.println("member.getName() = " + member.getName());
//            }
//            tx.commit();

            /**
             * 영속성 컨텍스트
             */
            //비영속
//            Member member = new Member();
//            member.setId(101L);
//            member.setName("Hello3");
//
//            //영속
//            em.persist(member);
//
//            //1차 캐시에 있는 것을 가져와서 select 쿼리가 안 날아감
//            Member findMember = em.find(Member.class, 101L);
//            System.out.println("findMember.getId() = " + findMember.getId());
//            System.out.println("findMember.getName() = " + findMember.getName());

//            Member findMember1 = em.find(Member.class, 101L); //이때만 쿼리 한 번 나가고
//            Member findMember2 = em.find(Member.class, 101L); //여기서는 1차 캐시에서 가져오므로 쿼리 안 날아감
//            System.out.println("result = " + (findMember1 == findMember2)); //1차 캐시로 인해 true
//
//            tx.commit();

            //쓰기 지연 SQL
            //아래 실행 후에는 쓰기 지연 SQL 저장소에 저장된다.
//            em.persist(memberA);

            //아래 명령어를 실행해야 트랜잭션을 실행한다.
//            tx.commit();

            //변경 감지
//            Member member = em.find(Member.class, 101L);
//
//            System.out.println("=============");
//            member.setName("HelloORM");
//
//            tx.commit();
//            System.out.println("=============");

            //플러시
//            Member member = new Member(200L, "member200");
//            em.persist(member);
//
//            em.flush();
//
//            //여기서 insert query 가 날아간다.
//            System.out.println("=============");
//            tx.commit();

            //객체지향스럽지 않게 설계
//            Team team = new Team();
//            team.setName("TeamA");
//            em.persist(team);
//
//            Member member = new Member();
//            member.setTeamId(team.getTeamId());
//            member.setUsername("Member1");
//            em.persist(member);

//            //단방향 연관관계
//            Team team = new Team();
//            team.setName("TeamA");
//            em.persist(team);
//
//            Member member = new Member();
//            member.setUsername("Member1");
//            member.setTeam(team);
//            em.persist(member);
//
//            //캐시에서 가져옴.
//            Member findMember = em.find(Member.class, member.getId());
//            Team findTeam = findMember.getTeam();
//            System.out.println("findTeam.toString() = " + findTeam.toString());
//
//            //업데이트하고 싶을 때는 findMember.setTeam("new team") 해주면 업데이트 된다.
//
//            //깔끔하게 지우고 깔끔하게 DB 에서 가져오도록 실행
//            em.flush();
//            em.clear();
//
//            //양방향 연관관계
//            Member findMember2 = em.find(Member.class, member.getId());
//            List<Member> members = findMember2.getTeam().getMembers();
//
//            for (Member m : members) {
//                System.out.println("m.getUsername() = " + m.getUsername());
//            }

//            Team team = new Team();
//            team.setName("TeamA");
//            em.persist(team);
//
//            Member member = new Member();
//            member.setUsername("Member1");
////            member.setTeam(team);
//            member.changeTeam(team);
//            em.persist(member);
//
//            team.getMembers().add(member);
//
//            em.flush();
//            em.clear();
//
//            Team findTeam = em.find(Team.class, team.getTeamId());
//            //flush, clear 안 하면 1차 캐시에 들어가 있다. 위 상태 그대로 들어가 있어서 아래 List 가져올 수 없다.
//            //따라서 team.getMembers().add(member); 이렇게와 같이 양쪽 다 세팅해줘야 한다.
//            List<Member> members = findTeam.getMembers();
//
//            for (Member member1 : members) {
//                System.out.println("member1 = " + member1.getUsername());
//            }

            Movie movie = new Movie();
            movie.setDirector("aaa");
            movie.setActor("bbb");
            movie.setName("바람과 함께 사라지다");
            movie.setPrice(10000);

            em.persist(movie);

            //1차 캐시 지우기
            em.flush();
            em.clear();

            //조인해서 가져온다.
            Movie findMovie = em.find(Movie.class, movie.getId());
            System.out.println("findMovie = " + findMovie);

            tx.commit();

        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }
        emf.close();
    }
}

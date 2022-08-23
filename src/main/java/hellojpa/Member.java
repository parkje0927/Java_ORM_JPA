package hellojpa;

import javax.persistence.*;

@Entity
//@SequenceGenerator(name = "member_seq_generator", sequenceName = "member_seq")
public class Member {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "name")
    private String username;

//    @Column(name = "TEAM_ID")
//    private Long teamId;

    //member 입장에서는 many 이고 team 은 one 이므로
    @ManyToOne
    @JoinColumn(name = "TEAM_ID")
    private Team team;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    //    //enum 에는 ORDINAL 은 사용하지 말기!!!
//    @Enumerated(EnumType.STRING)
//    private RoleType roleType;
//
//    @Temporal(TemporalType.TIMESTAMP)
//    private Date createdDate;
//
//    private LocalDate testLocalDate;
//    private LocalDateTime testLocalDateTime;
//
//    @Temporal(TemporalType.TIMESTAMP)
//    private Date lastModifiedDate;
//
//    @Lob //매우 큰 값일 경우
//    private String description;
//
//    @Transient //그냥 메모리에만 사용하고 db 에는 저장하지 말기 -> 컬럼이 생기지 않음
//    private int temp;
}

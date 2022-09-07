package hellojpa;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("A") //default = 클래스 이름
public class Album extends Item {

    private String artist;
}

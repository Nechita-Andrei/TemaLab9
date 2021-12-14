package demo;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "track")
public class Track {
    //prezentari conferinte
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description")
    private String description;

    @OneToOne
    @JoinColumn(name = "speaker_id",referencedColumnName = "id")
    private Person speaker;

    @Column(name = "room_id")
    private Long room_id;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "attendee", joinColumns = @JoinColumn(name = "track_id"), inverseJoinColumns = @JoinColumn(name = "person_id"))
    private Set<Person> attendees;

    public Track(){

    }
    public Track(String title, String description, Person speaker) {
        this.title = title;
        this.description = description;
        this.speaker = speaker;
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public long getRoomId() {
        return room_id;
    }

    public Person getSpeaker() {
        return speaker;
    }

    public Set<Person> getAttendees() {
        return attendees;
    }

    public void update(Track track){
        this.title=track.getTitle();
        this.description=track.getDescription();
        this.speaker=track.getSpeaker();
        this.room_id=track.getRoomId();
    }
}

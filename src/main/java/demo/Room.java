package demo;


import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "room")
public class Room {
    public Room(String name, int capacity) {
        this.name = name;
        this.capacity = capacity;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getCapacity() {
        return capacity;
    }

    public Set<Track> getTracks() {
        return tracks;
    }

    public Room(){

    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name",nullable = false)
    private String name;

    @Column(name = "capacity")
    private int capacity;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private Set<Track> tracks;
    public void addTrack(Track track){
        tracks.add(track);
    }
    public void removeTrack(Track track){
        tracks.remove(track);
    }

    public void update(Room room){
       this.capacity=room.getCapacity();
        this.name=room.getName();
    }


}

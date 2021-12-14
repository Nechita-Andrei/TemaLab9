package demo;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.SecondaryTable;
import java.net.URI;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("api")
public class MainController {
    //pentru proiectul mare trebuie si un service

    @Autowired
    private PersonRepo personRepo;

    @Autowired
    private TrackRepo trackRepo;

    @Autowired
    private RoomRepo roomRepo;


    @GetMapping(path = "/persons",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Iterable<Person>> getPersons(){
        Collection<Person> persons= (Collection<Person>) personRepo.findAll();
        if(!persons.isEmpty()){
            return ResponseEntity.ok(persons);
        }
        else {
            return ResponseEntity.noContent().build();
        }
    }

    @GetMapping(path = "/persons/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Person> getPerson(@PathVariable("id") long id){
        Optional<Person> person= personRepo.findById(id);
        if(person.isPresent()){
            return ResponseEntity.ok(person.get());
        }else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(path = "/persons",consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<Void> addPerson(String firstName, String lastName){
        Person person=new Person(firstName,lastName);
        personRepo.save(person);
        URI uri= WebMvcLinkBuilder.linkTo(MainController.class).slash("persons").slash(person.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @PutMapping(path = "/persons/{id}")
    public ResponseEntity<Void> changePerson(@PathVariable("id") long id, @RequestBody Person entity){
        Optional<Person> existingPerson=personRepo.findById(id);
        if(existingPerson.isPresent()){
            existingPerson.get().update(entity);
            personRepo.save(existingPerson.get());
            return ResponseEntity.noContent().build();
        }else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping(path = "/persons/{id}")
    public ResponseEntity<Void> removePerson(@PathVariable("id") long id){
        Optional<Person> existingPerson=personRepo.findById(id);
        if(existingPerson.isPresent()){
            personRepo.delete(existingPerson.get());
            return ResponseEntity.noContent().build();
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(path = "/persons/{personId}/tracks")
    public ResponseEntity<Set<Track>> getPersonTracks(@PathVariable("personId") long id){
        Optional<Person> person=personRepo.findById(id);
        if(person.isPresent()){
            Set<Track> tracks=person.get().getTracks();
            if(!tracks.isEmpty()){
                return ResponseEntity.ok(tracks);
            }
            else {
                return ResponseEntity.noContent().build();
            }
        }
        else {
           return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(path = "/persons/{personId}/tracks/{trackId}")
    public ResponseEntity<List<String>> addPersonTrack(@PathVariable("personId") long personId, @PathVariable("trackId") long trackId){
        Optional<Person> person=personRepo.findById(personId);
        if(person.isPresent()) {
            Optional<Track> track=trackRepo.findById(trackId);
            if(track.isPresent() && track.get().getSpeaker().getId()!=personId){
                person.get().addTrack(track.get());
                personRepo.save(person.get());
                return ResponseEntity.noContent().build();
            }
            else {
                return ResponseEntity.badRequest().build();
            }
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping(path = "/persons/{personId}/tracks/{trackId}")
    public ResponseEntity<List<String>> removePersonTrack(@PathVariable("personId") long personId, @PathVariable("trackId") long trackId){
        Optional<Person> person=personRepo.findById(personId);
        if(person.isPresent()) {
            Optional<Track> track=trackRepo.findById(trackId);
            if(track.isPresent()){
                person.get().removeTrack(track.get());
                personRepo.save(person.get());
                return ResponseEntity.noContent().build();
            }
            else {
                return ResponseEntity.badRequest().build();
            }
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(path = "/tracks",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Iterable<Track>> getTracks(){
        Collection<Track> tracks= (Collection<Track>) trackRepo.findAll();
        if(!tracks.isEmpty()){
            return ResponseEntity.ok(tracks);
        }
        else {
            return ResponseEntity.noContent().build();
        }
    }

    @GetMapping(path = "/tracks/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Track> getTrack(@PathVariable("id") long id){
        Optional<Track> track= trackRepo.findById(id);
        if(track.isPresent()){
            return ResponseEntity.ok(track.get());
        }else {
            return ResponseEntity.notFound().build();
        }
    }
    @PostMapping(path = "/tracks",consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<Void> addTrack(String title, String description, long speakerId){
        Optional<Person> speaker=personRepo.findById(speakerId);
        if(speaker.isPresent()){
            Track track=new Track(title,description,speaker.get());
            trackRepo.save(track);
            URI uri= WebMvcLinkBuilder.linkTo(MainController.class).slash("tracks").slash(track.getId()).toUri();
            return ResponseEntity.created(uri).build();
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping(path = "/tracks/{id}")
    public ResponseEntity<Void> changeTrack(@PathVariable("id") long id, @RequestBody Track entity){
        Optional<Track> existingTrack=trackRepo.findById(id);
        if(existingTrack.isPresent()){
            existingTrack.get().update(entity);
            trackRepo.save(existingTrack.get());
            return ResponseEntity.noContent().build();
        }else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping(path = "/tracks/{id}")
    public ResponseEntity<Void> removeTrack(@PathVariable("id") long id){
        Optional<Track> existingTrack=trackRepo.findById(id);
        if(existingTrack.isPresent()){
            trackRepo.delete(existingTrack.get());
            return ResponseEntity.noContent().build();
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(path = "/tracks/{trackId}/persons")
    public ResponseEntity<Set<Person>> getTrackPersons(@PathVariable("trackId") long id){
        Optional<Track> track=trackRepo.findById(id);
        if(track.isPresent()){
            Set<Person> persons=track.get().getAttendees();
            if(!persons.isEmpty()){
                return ResponseEntity.ok(persons);
            }
            else {
                return ResponseEntity.noContent().build();
            }
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping(path = "/rooms",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Iterable<Room>> getRooms(){
        Collection<Room> rooms= (Collection<Room>) roomRepo.findAll();
        if(!rooms.isEmpty()){
            return ResponseEntity.ok(rooms);
        }
        else {
            return ResponseEntity.noContent().build();
        }
    }
    @GetMapping(path = "/rooms/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Room> getRoom(@PathVariable("id") long id){
        Optional<Room> room= roomRepo.findById(id);
        if(room.isPresent()){
            return ResponseEntity.ok(room.get());
        }else {
            return ResponseEntity.notFound().build();
        }
    }
    @PostMapping(path = "/rooms",consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<Void> addRoom(String name, int capacity){
        Room room=new Room(name,capacity);
        roomRepo.save(room);
        URI uri= WebMvcLinkBuilder.linkTo(MainController.class).slash("rooms").slash(room.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @PutMapping(path = "/rooms/{id}")
    public ResponseEntity<Void> changeRoom(@PathVariable("id") long id, @RequestBody Room entity){
        Optional<Room> existingRoom=roomRepo.findById(id);
        if(existingRoom.isPresent()){
            existingRoom.get().update(entity);
            roomRepo.save(existingRoom.get());
            return ResponseEntity.noContent().build();
        }else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping(path = "/rooms/{id}")
    public ResponseEntity<Void> removeRoom(@PathVariable("id") long id){
        Optional<Room> existingRoom=roomRepo.findById(id);
        if(existingRoom.isPresent()){
            roomRepo.delete(existingRoom.get());
            return ResponseEntity.noContent().build();
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(path = "/rooms/{roomId}/tracks")
    public ResponseEntity<Set<Track>> getRoomTracks(@PathVariable("roomId") long id){
        Optional<Room> room=roomRepo.findById(id);
        if(room.isPresent()){
            Set<Track> tracks=room.get().getTracks();
            if(!tracks.isEmpty()){
                return ResponseEntity.ok(tracks);
            }
            else {
                return ResponseEntity.noContent().build();
            }
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(path = "/rooms/{roomId}/tracks/{trackId}")
    public ResponseEntity<List<String>> addRoomTrack(@PathVariable("roomId") long roomId, @PathVariable("trackId") long trackId){
        Optional<Room> room=roomRepo.findById(roomId);
        if(room.isPresent()) {
            Optional<Track> track=trackRepo.findById(trackId);
            if(track.isPresent()){
                room.get().addTrack(track.get());
               roomRepo.save(room.get());
                return ResponseEntity.noContent().build();
            }
            else {
                return ResponseEntity.badRequest().build();
            }
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping(path = "/rooms/{roomId}/tracks/{trackId}")
    public ResponseEntity<List<String>> removeRoomTrack(@PathVariable("roomId") long roomId, @PathVariable("trackId") long trackId){
        Optional<Room> room=roomRepo.findById(roomId);
        if(room.isPresent()) {
            Optional<Track> track=trackRepo.findById(trackId);
            if(track.isPresent()){
                room.get().removeTrack(track.get());
               roomRepo.save(room.get());
                return ResponseEntity.noContent().build();
            }
            else {
                return ResponseEntity.badRequest().build();
            }
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }




}

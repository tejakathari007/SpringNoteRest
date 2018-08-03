package com.stackroute.keepnote.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.stackroute.keepnote.dao.NoteDAO;
import com.stackroute.keepnote.model.Note;

/*
 * Annotate the class with @Controller annotation.@Controller annotation is used to mark 
 * any POJO class as a controller so that Spring can recognize this class as a Controller
 */

@RestController
@RequestMapping(value = "api/v1/")
public class NoteController {

	/*
	 * From the problem statement, we can understand that the application requires
	 * us to implement the following functionalities.
	 * 
	 * 1. display the list of existing notes from the persistence data. Each note
	 * should contain Note Id, title, content, status and created date. 2. Add a new
	 * note which should contain the note id, title, content and status. 3. Delete
	 * an existing note 4. Update an existing note
	 * 
	 */

	/*
	 * Autowiring should be implemented for the NoteDAO. Create a Note object.
	 * 
	 */

	private NoteDAO noteDao;

	@Autowired
	public NoteController(NoteDAO noteDao) {

		this.noteDao = noteDao;
	}
	
	@RequestMapping("/")
	public String hello(){
		return "Hello Kathari";
	}

	/*
	 * Define a handler method to read the existing notes from the database and add
	 * it to the ModelMap which is an implementation of Map, used when building
	 * model data for use with views. it should map to the default URL i.e. "/index"
	 */

	@RequestMapping(value = "/notes", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> getAllNotes() {
        
        List<Note> noteList=noteDao.getAllNotes();

        return new ResponseEntity<List<Note>>(noteList, HttpStatus.OK);

    }

	/*
	 * Define a handler method which will read the NoteTitle, NoteContent,
	 * NoteStatus from request parameters and save the note in note table in
	 * database. Please note that the CreatedAt should always be auto populated with
	 * system time and should not be accepted from the user. Also, after saving the
	 * note, it should show the same along with existing messages. Hence, reading
	 * note has to be done here again and the retrieved notes object should be sent
	 * back to the view using ModelMap This handler method should map to the URL
	 * "/add".
	 */

	@RequestMapping(value = "/note", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<?> addNoteToDB(@RequestBody Note note) {
		if (noteDao.saveNote(note)) {
			return new ResponseEntity<String>("{ \"message\": \"" + "Success" + "\"}", HttpStatus.OK);
		} else {
			return new ResponseEntity<String>("{ \"message\": \"" + "failure" + "\"}", HttpStatus.CONFLICT);
		}

	}

	/*
	 * Define a handler method which will read the NoteId from request parameters
	 * and remove an existing note by calling the deleteNote() method of the
	 * NoteRepository class.This handler method should map to the URL "/delete".
	 */
	@RequestMapping(value = "/note/{noteId}", method = RequestMethod.DELETE, produces = "application/json")
	public ResponseEntity<String> deleteNotefromDB(@PathVariable int noteId) {
		noteDao.deleteNote(noteId);
		return new ResponseEntity<String>("deleted", HttpStatus.OK);

	}

	/*
	 * Define a handler method which will update the existing note. This handler
	 * method should map to the URL "/note" resource with PUT.
	 */
	@RequestMapping(value = "/note", method = RequestMethod.PUT, produces = "application/json")
	public ResponseEntity<String> updateNoteToDB(@RequestBody Note note) {


		noteDao.UpdateNote(note);

		return new ResponseEntity<String>("updated", HttpStatus.OK);

	}

	@RequestMapping(value = "/note", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<?> getById(@RequestParam int noteId) {
		return new ResponseEntity<Note>(noteDao.getNoteById(noteId), HttpStatus.FOUND);

	}
}


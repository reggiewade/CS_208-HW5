package cs208;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;


@RestController
public class StudentsController
{

    //http://localhost:8080/students
    /**
     * GET /students
     *
     * @return a list of students (extracted from the students table in the database) as JSON
     */
    @GetMapping(value = "/students", produces = MediaType.APPLICATION_JSON_VALUE)
    List<Student> index()
    {
        List<Student> listOfStudents = Main.database.listAllStudents();

        return listOfStudents;
    }

    /**
     * GET /students/{id}
     *
     * @return the student with id = {id} (extracted from the students table in the database) as JSON
     *
     * @throws ResponseStatusException: a 404 status code if the student with id = {id} does not exist
     */
    @GetMapping(value = "/students/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    Student show(@PathVariable("id") int id)
    {
        System.out.println("id = " + id);

        Student studentWithID = Main.database.getStudentWithId(id);
        if (studentWithID == null)
        {
            System.out.println("No class with id " + id + " exists.");

            // return 404 status code (i.e., error that the student was not found)
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "class with id " + id + " not found"
            );
        }

        return studentWithID;
    }



    /**
     * POST /students
     * with the following form parameters:
     *      firstName
     *      lastName
     *      birthDate (in ISO format: yyyy-mm-dd)
     *
     * The parameters passed in the body of the POST request are used to create a new student.
     * The new student is inserted into the students table in the database.
     *
     * @return the created student (which was inserted into the database), as JSON
     */
    @PostMapping("/students")
    Student create(
            @RequestParam("first_name") String firstName,
            @RequestParam("last_name") String lastName,
            @RequestParam("birth_date") String birthDate
    )
    {
        System.out.println("first_name       = " + firstName);
        System.out.println("last_name = " + lastName);
        System.out.println("birth_date = " + birthDate);
    

        try
        {
            Student createdStudent = new Student(firstName, lastName, Date.valueOf(birthDate));
            Main.database.addNewStudent(createdStudent);
            return createdStudent;
        }
        catch (SQLException e)
        {
            throw new ResponseStatusException(
                    HttpStatus.UNPROCESSABLE_ENTITY, // 422 error code
                    "failed to add new class to the database"
            );
        }
    }



    /**
     * PUT /students/{id}
     * with the following form parameters:
     *      firstName
     *      lastName
     *      birthDate
     *
     * The parameters passed in the body of the PUT request are used to
     * update the existing student with id = {id} in the students table in the database.
     *
     * @return the updated student as JSON
     *
     * @throws ResponseStatusException: a 404 status code if the student with id = {id} does not exist
     */
    @PutMapping(value = "/students/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    Student update(
            @PathVariable("id") int id,
            @RequestParam("first_name") String firstName,
            @RequestParam("last_name") String lastName,
            @RequestParam("birth_date") String birthDate
    )
    {
        System.out.println("id          = " + id);
        System.out.println("first_name        = " + firstName);
        System.out.println("last_name       = " + lastName);
        System.out.println("birth_date = " + birthDate);

        try
        {
            Student studentToUpdate = Main.database.getStudentWithId(id);
            if (studentToUpdate == null)
            {
                throw new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "failed to update the student with id = " + id + " in the database because it does not exist"
                );
            }

            // override the values of all the fields from classToUpdate with the values from the parameters
            studentToUpdate.setFirstName(firstName);
            studentToUpdate.setLastName(lastName);
            studentToUpdate.setBirthDate(Date.valueOf(birthDate));

            Main.database.updateExistingStudentInformation(studentToUpdate);
            return studentToUpdate;
        }
        catch (SQLException e)
        {
            throw new ResponseStatusException(
                    HttpStatus.UNPROCESSABLE_ENTITY, // 422 error code
                    "failed to update the student with id = " + id + " in the database"
            );
        }
    }




    /**
     * PATCH /students/{id}
     * with the following optional form parameters:
     *      firstName
     *      lastName
     *      birthDate
     *
     * The optional parameters passed in the body of the PATCH request are used to
     * update the existing student with id = {id} in the students table in the database.
     *
     * @return the updated student as JSON
     *
     * @throws ResponseStatusException: a 404 status code if the student with id = {id} does not exist
     */
    @PatchMapping(value = "/student/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    Student updatePatch(
            @PathVariable("id") int id,
            @RequestParam(value = "first_name", required = false) String firstName,
            @RequestParam(value = "last_name", required = false) String lastName,
            @RequestParam(value = "birth_date", required = false) String birthDate
    )
    {
        System.out.println("id          = " + id);
        System.out.println("first_name        = " + firstName);
        System.out.println("last_name       = " + lastName);
        System.out.println("birth_date = " + birthDate);

        try
        {
            Student studentToUpdate = Main.database.getStudentWithId(id);
            if (studentToUpdate == null)
            {
                throw new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "failed to update the student with id = " + id + " in the database because it does not exist"
                );
            }

            if (firstName != null)
            {
                studentToUpdate.setFirstName(firstName);
            }

            if (lastName != null)
            {
                studentToUpdate.setLastName(lastName);
            }

            if (birthDate != null)
            {
                studentToUpdate.setBirthDate(Date.valueOf(birthDate));;
            }

            Main.database.updateExistingStudentInformation(studentToUpdate);
            return studentToUpdate;
        }
        catch (SQLException e)
        {
            throw new ResponseStatusException(
                    HttpStatus.UNPROCESSABLE_ENTITY, // 422 error code
                    "failed to update the student with id = " + id + " in the database"
            );
        }
    }



    /**
     * DELETE /students/{id}
     *
     * Deletes the student with id = {id} from the students table in the database.
     *
     * @throws ResponseStatusException: a 404 status code if the student with id = {id} does not exist
     */
    @DeleteMapping(value = "/students/{id}")
    void delete(@PathVariable("id") int id)
    {
        System.out.println("id = " + id);

        try {
            Student studentToDelete = Main.database.getStudentWithId(id);
            if (studentToDelete == null) {
                throw new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "failed to delete the class with id = " + id + " from the database because it does not exist"
                );
            }
            Main.database.deleteExistingStudent(id);
        }
        catch(SQLException e) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,
            "failed to delete the student with id = " + id + " from the database");
        }
    }

}
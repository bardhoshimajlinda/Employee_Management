package org.example;

import org.example.model.Movie;
import org.example.repository.MovieDAOImpl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/* Implement the class MovieDAOImpl (DAO - Data Access Object). Assume that an object representing an open database connection comes in the constructor of this class. Also implement a Movie class that represents a single row in the MOVIES table that you will use in implementing the MovieDAOImple class.
Implement the following operations. Each of them should be represented by a separate public method:

creating a MOVIES table
delete the MOVIES table
adding a record
delete record by identifier
update of the movie title with id data
searching for a movie by ID

In case of an exception SQLException, make the method throw an exceptionDatabaseActionException. This class should extend from the RuntimeException class.*/
public class MovieApp {
    public static void main(String[] args) {

        String connectionUrl = System.getenv("DB_URL");
        String username = System.getenv("DB_USER");
        String password = System.getenv("DB_PASSWORD");

        try (Connection connection = DriverManager.getConnection(connectionUrl, username, password)) {
            MovieDAOImpl movieDAO = new MovieDAOImpl(connection);

            movieDAO.createTable();
            movieDAO.createMovie(new Movie(1, "Movie1", "Action", 2020));
            movieDAO.updateMoviesTitle(1, "UpdatedTitle");
            Optional<Movie> movie = movieDAO.findMovieById(1);
            movie.ifPresent(System.out::println);

            List<Movie> allMovies = movieDAO.findAll();
            for (Movie m : allMovies) {
                System.out.println("ID: " + m.getId() +
                        ", Title: " + m.getTitle() +
                        ", Genre: " + m.getGenre() +
                        ", Year of Release: " + m.getYearOfRelease());
            }

//            movieDAO.deleteTable();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}


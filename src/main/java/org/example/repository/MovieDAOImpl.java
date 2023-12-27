package org.example.repository;

import org.example.model.DatabaseActionException;
import org.example.model.Movie;
import org.example.model.MovieDAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MovieDAOImpl implements MovieDAO {
    private final Connection connection;

    public MovieDAOImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void createTable() {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS MOVIES (" +
                    "id INTEGER AUTO_INCREMENT PRIMARY KEY," +
                    "title VARCHAR(255)," +
                    "genre VARCHAR(255)," +
                    "yearOfRelease INTEGER)");
        } catch (SQLException e) {
            throw new DatabaseActionException("Error creating table", e);
        }
    }

    @Override
    public void deleteTable() {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("DROP TABLE IF EXISTS MOVIES");
        } catch (SQLException e) {
            throw new DatabaseActionException("Error deleting table", e);
        }
    }

    @Override
    public void createMovie(final Movie movie) {
        String sql = "INSERT INTO MOVIES (title, genre, yearOfRelease) VALUES (?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, movie.getTitle());
            preparedStatement.setString(2, movie.getGenre());
            preparedStatement.setInt(3, movie.getYearOfRelease());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseActionException("Error creating movie", e);
        }
    }

    @Override
    public void deleteMovie(int id) {
        String sql = "DELETE FROM MOVIES WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseActionException("Error deleting movie", e);
        }
    }

    @Override
    public void updateMoviesTitle(int id, String newTitle) {
        String sql = "UPDATE MOVIES SET title = ? WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, newTitle);
            preparedStatement.setInt(2, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseActionException("Error updating movie title", e);
        }
    }

    @Override
    public Optional<Movie> findMovieById(int id) {
        String sql = "SELECT * FROM MOVIES WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(new Movie(
                        resultSet.getInt("id"),
                        resultSet.getString("title"),
                        resultSet.getString("genre"),
                        resultSet.getInt("yearOfRelease")
                ));
            }
        } catch (SQLException e) {
            throw new DatabaseActionException("Error finding movie by ID", e);
        }
        return Optional.empty();
    }

    @Override
    public List<Movie> findAll() {
        List<Movie> movies = new ArrayList<>();
        String sql = "SELECT * FROM MOVIES";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                movies.add(new Movie(
                        resultSet.getInt("id"),
                        resultSet.getString("title"),
                        resultSet.getString("genre"),
                        resultSet.getInt("yearOfRelease")
                ));
            }
        } catch (SQLException e) {
            throw new DatabaseActionException("Error finding all movies", e);
        }
        return movies;
    }
}

package ru.job4j.grabber;

import ru.job4j.Post;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class PsqlStore implements Store, AutoCloseable {

    final private Connection cnn;

    public PsqlStore(Properties cfg) throws SQLException {
        try {
            Class.forName(cfg.getProperty("driver-class-name"));
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
        cnn = DriverManager.getConnection(
                cfg.getProperty("url"),
                cfg.getProperty("username"),
                cfg.getProperty("password"));
    }

    @Override
    public void close() throws Exception {
        if (cnn != null) {
            cnn.close();
        }
    }

    @Override
    public void save(Post post) {
        try (PreparedStatement statement =
                     cnn.prepareStatement(
                             "insert into post(name, text, link, created) values (?,?,?,?)")) {
            statement.setString(1, post.getTitle());
            statement.setString(2, post.getDescription());
            statement.setString(3, post.getLink());
            statement.setTimestamp(4, Timestamp.valueOf(post.getCreated()));
            statement.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Post> getAll() {
        List<Post> posts = new ArrayList<>();
        try (PreparedStatement statement =
                     cnn.prepareStatement(
                             "select * from post")) {
            statement.execute();
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Post post = new Post(resultSet.getString("name"),
                        resultSet.getString("link"), resultSet.getString("text"),
                        resultSet.getTimestamp("created").toLocalDateTime());
                post.setId(resultSet.getInt("id"));
                posts.add(post);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return posts;
    }

    @Override
    public Post findById(int id) {
        try (PreparedStatement statement =
                     cnn.prepareStatement(
                             "select * from post where id = (?)")) {
            statement.setInt(1, id);
            statement.execute();
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new Post(resultSet.getString("name"),
                        resultSet.getString("link"), resultSet.getString("text"),
                        resultSet.getTimestamp("created").toLocalDateTime());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
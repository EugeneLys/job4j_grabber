package ru.job4j.grabber;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class PsqlStore implements Store {

    private Connection connection;

    public PsqlStore(Properties config) throws SQLException {
        try {
            Class.forName(config.getProperty("jdbc.driver"));
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
        connection = DriverManager.getConnection(config.getProperty("jdbc.url"),
                config.getProperty("jdbc.login"), config.getProperty("jdbc.password"));
    }

    private Post makePost(ResultSet resultSet) throws SQLException {
        return new Post(
                resultSet.getInt("id"),
                resultSet.getString("name"),
                resultSet.getString("text"),
                resultSet.getString("link"),
                resultSet.getTimestamp("created").toLocalDateTime()
        );
    }

    @Override
    public void save(Post post) {
        try (PreparedStatement statement = connection.prepareStatement("INSERT INTO post "
                + "(id, name, text, link, created) VALUES (?, ?, ?, ?, ?) ON CONFLICT (link) DO NOTHING")) {
            statement.setInt(1, post.getId());
            statement.setString(2, post.getTitle());
            statement.setString(3, post.getDescription());
            statement.setString(4, post.getLink());
            statement.setTimestamp(5, Timestamp.valueOf(post.getCreated()));
            statement.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Post> getAll() {
        List<Post> list = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM post")) {
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    list.add(makePost(resultSet));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public Post findById(int id) {
        Post post = null;
        try (PreparedStatement statement =
                     connection.prepareStatement("SELECT * FROM post WHERE id = ?")) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    if (resultSet.getInt("id") == id) {
                        post = makePost(resultSet);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return post;
    }

    @Override
    public void close() throws Exception {
        if (connection != null) {
            connection.close();
        }
    }
}

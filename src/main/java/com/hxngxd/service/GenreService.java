package com.hxngxd.service;

import com.hxngxd.database.DatabaseManager;
import com.hxngxd.entities.Genre;
import com.hxngxd.exceptions.DatabaseException;

public class GenreService extends Service<Genre> {

    private GenreService() {
    }

    private static class SingletonHolder {
        private static final GenreService instance = new GenreService();
    }

    public static GenreService getInstance() {
        return GenreService.SingletonHolder.instance;
    }

    @Override
    public void loadAll()
            throws DatabaseException {
        Genre.genreMap.clear();
        String query = "select * from genre";
        DatabaseManager.getInstance().select("getting genres", query, resultSet -> {
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                Genre genre = new Genre(
                        id,
                        resultSet.getString("name"),
                        resultSet.getString("description")
                );
                Genre.genreMap.put(id, genre);
            }
            return null;
        });
    }

}

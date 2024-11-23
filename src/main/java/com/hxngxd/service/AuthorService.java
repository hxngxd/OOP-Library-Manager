package com.hxngxd.service;

import com.hxngxd.database.DatabaseManager;
import com.hxngxd.entities.Author;
import com.hxngxd.exceptions.DatabaseException;
import com.hxngxd.utils.ImageHandler;

import java.sql.Date;

public class AuthorService extends Service<Author> {

    private AuthorService() {
    }

    private static class SingletonHolder {
        private static final AuthorService instance = new AuthorService();
    }

    public static AuthorService getInstance() {
        return AuthorService.SingletonHolder.instance;
    }

    @Override
    public void loadAll()
            throws DatabaseException {
        Author.authorSet.clear();
        Author.authorMap.clear();

        String query = "select * from author";
        DatabaseManager.getInstance().select("getting authors", query, resultSet -> {
            while (resultSet.next()) {
                int id = resultSet.getInt("id");

                Author author = new Author(id);

                author.setFirstName(resultSet.getString("firstName"));

                author.setLastName(resultSet.getString("lastName"));

                Date dob = resultSet.getDate("dateOfBirth");
                if (dob != null) {
                    author.setDayOfDeath(dob.toLocalDate());
                }

                byte[] photo = resultSet.getBytes("photo");
                if (photo != null) {
                    author.setImage(ImageHandler.byteArrayToImage(photo));
                }

                author.setBiography(resultSet.getString("biography"));

                Date dod = resultSet.getDate("dayOfDeath");
                if (dod != null) {
                    author.setDayOfDeath(dod.toLocalDate());
                }

                Author.authorMap.put(id, author);
            }
            return null;
        });
        Author.authorSet.addAll(Author.authorMap.values());
    }

}

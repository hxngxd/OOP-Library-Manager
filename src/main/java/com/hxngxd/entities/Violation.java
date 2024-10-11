package com.hxngxd.entities;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Lớp Violation đại diện cho một vi phạm của người dùng.
 */
public class Violation {

    private int id;
    private int user;
    private LocalDateTime violationDate;
    private String description;

    /**
     * Lấy danh sách vi phạm của người dùng.
     *
     * @param userId ID của người dùng.
     * @return Danh sách các vi phạm của người dùng.
     */
    public static List<Violation> getUsersViolations(int userId) {
        return null;
    }
}

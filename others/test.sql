drop database if exists libraryManagement;

create database if not exists libraryManagement character set utf8mb4 collate utf8mb4_unicode_ci;

use libraryManagement;

create table user
(
    id            int auto_increment primary key,
    firstName     varchar(127)                                       not null,
    lastName      varchar(127)                                       not null,
    dateOfBirth   date,
    photo         mediumblob,
    username      varchar(127)                                       not null unique,
    email         varchar(127)                                       not null unique,
    address       varchar(255),
    role          enum ('USER', 'MODERATOR', 'ADMIN')                not null default 'USER',
    accountStatus enum ('ACTIVE', 'INACTIVE', 'SUSPENDED', 'BANNED') not null default 'INACTIVE',
    dateAdded     datetime                                                    default current_timestamp,
    lastActive    datetime,
    passwordHash  varchar(255)                                       not null
);

alter table user
    auto_increment = 1;

create index idxUserRole on user (role);

create index idxUserAccountStatus on user (accountStatus);

create index idxUserUsername on user (username);

create index idxUserEmail on user (email);

create table author
(
    id          int auto_increment primary key,
    firstName   varchar(127) not null,
    lastName    varchar(127) not null,
    dateOfBirth date,
    photo       mediumblob,
    biography   text,
    dayOfDeath  date
);

create table book
(
    id                int auto_increment primary key,
    title             text not null,
    yearOfPublication int,
    shortDescription  text,
    coverImage        mediumblob,
    numberOfPages     int,
    dateAdded         datetime default current_timestamp,
    lastUpdated       datetime default current_timestamp on update current_timestamp,
    availableCopies   int  not null,
    totalCopies       int  not null
);

create index idxBookYear on book (yearOfPublication);

create table genre
(
    id          int auto_increment primary key,
    name        varchar(32) not null unique,
    description text
);

create table bookAuthor
(
    bookId   int not null,
    authorId int not null,
    primary key (bookId, authorId),
    foreign key (bookId) references book (id) on delete cascade,
    foreign key (authorId) references author (id) on delete cascade
);

create table bookGenre
(
    bookId  int not null,
    genreId int not null,
    primary key (bookId, genreId),
    foreign key (bookId) references book (id) on delete cascade,
    foreign key (genreId) references genre (id) on delete cascade
);

create table review
(
    id         int auto_increment primary key,
    userId     int not null,
    bookId     int not null,
    rating     int not null check (1 <= rating and rating <= 5),
    comment    text,
    reviewTime datetime default current_timestamp,
    foreign key (userId) references user (id) on delete cascade,
    foreign key (bookId) references book (id) on delete cascade
);

create index idxReviewUser on review (userId);

create index idxReviewBook on review (bookId);

create table borrowing
(
    id                  int auto_increment primary key,
    requesterId         int                                                                                                  not null,
    bookId              int                                                                                                  not null,
    handlerId           int                                                                                                  not null,
    requestDate         datetime                                                                                                      default current_timestamp,
    approvalDate        datetime,
    borrowDate          datetime,
    estimatedReturnDate date,
    actualReturnDate    date,
    status              enum ('PENDING', 'APPROVED', 'BORROWED', 'RETURNED_ON_TIME', 'OVERDUE', 'RETURNED_LATE', 'REJECTED') not null default 'PENDING',
    foreign key (requesterId) references user (id) on delete cascade,
    foreign key (bookId) references book (id) on delete cascade,
    foreign key (handlerId) references user (id)
);

create index idxBorrowingRequester on borrowing (requesterId);

create index idxBorrowingBook on borrowing (bookId);

create index idxBorrowingHandler on borrowing (handlerId);

create index idxBorrowingStatus on borrowing (status);

create table userSavedBook
(
    userId    int not null,
    bookId    int not null,
    savedDate datetime default current_timestamp,
    primary key (userId, bookId),
    foreign key (userId) references user (id) on delete cascade,
    foreign key (bookId) references book (id) on delete cascade
);

create table notification
(
    id               int auto_increment primary key,
    recipient        int                                                           default null,
    content          text,
    notificationType enum ('REMINDER', 'WARNING', 'INFORMATION', 'ALERT') not null,
    timestamp        datetime                                                      default current_timestamp,
    isRead           bool                                                 not null default false,
    foreign key (recipient) references user (id) on delete cascade
);

create index idxNotificationType on notification (notificationType);

insert into user(firstName, lastName, dateOfBirth, username, email, address, role, passwordHash)
values ('Hùng', 'Nguyễn Tường', '2005-11-07', '23020078', '23020078@vnu.edu.vn', null, 'ADMIN',
        '$2a$11$gCDeAx4PAivnsEqFxLnVmeAhx0X.PRVgmEPTTJqkl8XwwjrXc/LPK'),
       ('Minh', 'Hoàng Lê', '2005-09-07', '23020111', '23020111@vnu.edu.vn', null, 'USER',
        '$2a$12$dL/dIDK702tQBvAlu6Bto.5hz9ip8U33uEFV8tYRId2oS2IuAWoKq');

INSERT INTO user (firstName, lastName, dateOfBirth, username, email, address, role, passwordHash)
VALUES ('Alexis', 'Barnes', '1970-10-10', 'user1', 'user1@example.com', 'Holderville', 'MODERATOR',
        '$2b$12$Rs0z8sR.yq6G2C/6EuuwPuVzlfaLe/mgsHYW/i9UahEQ9qlxkKCzi'),
       ('James', 'Hernandez', '1985-11-22', 'user2', 'user2@example.com', 'New Brittany',
        'MODERATOR', '$2b$12$FKCaaY5fotXpv.3/r7x6eedveqT/F7ZHi67gStmIKrSZLzZHFqCRC'),
       ('Johnathan', 'Phillips', '1988-10-16', 'user3', 'user3@example.com', 'South Tracyview',
        'USER', '$2b$12$pcd4bGSRSI6EQ6es9yl00eH1Bk/zxDdORvolP3OzwiXB4RceCLopq'),
       ('Lisa', 'Green', '1966-06-28', 'user4', 'user4@example.com', 'New James', 'USER',
        '$2b$12$4qs7cp.gZfRnyFqz7dFw2uSCKRegmD6HkGdyb6P9NVRvtSg83EI0i'),
       ('Tammy', 'Benson', '1992-04-04', 'user5', 'user5@example.com', 'Lake Kelly', 'USER',
        '$2b$12$YXRzeT9Ya3IJw9Kk62Vc/.Umz7uDxAyRwZ5M5Yc5GPZ.qFPPKnZmS'),
       ('Joel', 'Franklin', '1990-08-18', 'user6', 'user6@example.com', 'Milesmouth', 'USER',
        '$2b$12$iPHpMoS4Piyksgtj7C8nx.dckci3FyBT9zwCt1Rv5FSn48azCPZEq'),
       ('John', 'Peters', '1987-04-26', 'user7', 'user7@example.com', 'Williamsburgh', 'USER',
        '$2b$12$r1Ru6GN6iCBOXsYTpadt1O27HGnRkh4JijRdLb3vFt2N.EIsKL0OG'),
       ('Joseph', 'Diaz', '1993-10-29', 'user8', 'user8@example.com', 'Lake Elizabeth', 'USER',
        '$2b$12$1VDOGwQL92LauNn4HOAeiOHx3iiWwrpSWLQw566SRCIDYXDTq83hO'),
       ('Billy', 'Potter', '2003-08-18', 'user9', 'user9@example.com', 'New Thomasfurt', 'USER',
        '$2b$12$8bG0TH3rBtAAp7Ob0ZNSUeOruTyiJbxwX6bv3I6XiGrkqP2Ly9r6S'),
       ('Anthony', 'Warren', '1987-04-10', 'user10', 'user10@example.com', 'Nicolechester', 'USER',
        '$2b$12$dheaDRvYiutHAp6DOO5.lOJ6xv0v8DWEjUxK57s2SiDlC4m8nJ3YG'),
       ('Sophia', 'Nguyen', '1995-03-14', 'user11', 'user11@example.com', 'Quynhborough', 'USER',
        ''),
       ('Benjamin', 'Tran', '1982-12-05', 'user12', 'user12@example.com', 'PhoCity', 'MODERATOR',
        ''),
       ('Chloe', 'Vo', '1990-06-21', 'user13', 'user13@example.com', 'Lanetown', 'USER', ''),
       ('Daniel', 'Le', '1978-09-18', 'user14', 'user14@example.com', 'Binhtown', 'USER', ''),
       ('Isabella', 'Pham', '1984-02-29', 'user15', 'user15@example.com', 'Tanburgh', 'MODERATOR',
        ''),
       ('Henry', 'Hoang', '2000-11-11', 'user16', 'user16@example.com', 'Kimtown', 'USER', ''),
       ('Emily', 'Dang', '1997-07-13', 'user17', 'user17@example.com', 'Thanhville', 'USER', ''),
       ('Lucas', 'Vu', '1992-01-30', 'user18', 'user18@example.com', 'VuCity', 'USER', ''),
       ('Mia', 'Dinh', '1985-08-27', 'user19', 'user19@example.com', 'Thienburgh', 'USER', ''),
       ('Ethan', 'Ly', '1999-05-05', 'user20', 'user20@example.com', 'AnhCity', 'USER', ''),
       ('Olivia', 'Bui', '1994-12-10', 'user21', 'user21@example.com', 'Longville', 'USER', ''),
       ('Alexander', 'Nguyen', '1986-04-19', 'user22', 'user22@example.com', 'Huytown', 'MODERATOR',
        ''),
       ('Lily', 'Tran', '1991-10-25', 'user23', 'user23@example.com', 'Binhburgh', 'USER', ''),
       ('Samuel', 'Ho', '1989-06-03', 'user24', 'user24@example.com', 'Anh City', 'USER', ''),
       ('Ava', 'Le', '2002-08-14', 'user25', 'user25@example.com', 'Namland', 'USER', ''),
       ('Mason', 'Pham', '1980-02-02', 'user26', 'user26@example.com', 'Truongville', 'MODERATOR',
        ''),
       ('Ella', 'Do', '1993-03-21', 'user27', 'user27@example.com', 'Quyenland', 'USER', ''),
       ('James', 'Ngoc', '1987-09-29', 'user28', 'user28@example.com', 'Thuville', 'USER', ''),
       ('Charlotte', 'Huynh', '2001-07-18', 'user29', 'user29@example.com', 'Hoangborough', 'USER',
        ''),
       ('Logan', 'Dang', '1996-11-12', 'user30', 'user30@example.com', 'Nam City', 'MODERATOR', ''),
       ('Amelia', 'Luong', '1998-12-05', 'user31', 'user31@example.com', 'Hanhbury', 'USER', ''),
       ('Ethan', 'Vu', '1983-05-10', 'user32', 'user32@example.com', 'Trangtown', 'USER', ''),
       ('Grace', 'Duong', '1979-06-14', 'user33', 'user33@example.com', 'Lanfield', 'USER', ''),
       ('Ryan', 'Dinh', '1990-10-30', 'user34', 'user34@example.com', 'Myburgh', 'USER', ''),
       ('Harper', 'Le', '1992-04-08', 'user35', 'user35@example.com', 'Nguyencity', 'USER', ''),
       ('Lucas', 'Dang', '1988-09-23', 'user36', 'user36@example.com', 'Phuongland', 'USER', ''),
       ('Sophia', 'Dao', '1995-07-01', 'user37', 'user37@example.com', 'Anhville', 'MODERATOR', ''),
       ('David', 'Ly', '1984-12-18', 'user38', 'user38@example.com', 'Minh City', 'USER', ''),
       ('Emma', 'Bach', '2003-03-15', 'user39', 'user39@example.com', 'Linhville', 'USER', ''),
       ('William', 'Lai', '1999-08-22', 'user40', 'user40@example.com', 'Hungtown', 'USER', ''),
       ('Zoe', 'Luu', '1985-02-11', 'user41', 'user41@example.com', 'Tienville', 'USER', ''),
       ('Nathan', 'Nguyen', '1997-05-30', 'user42', 'user42@example.com', 'Hung City', 'USER', ''),
       ('Emily', 'Pham', '1988-07-25', 'user43', 'user43@example.com', 'Khanhborough', 'MODERATOR',
        ''),
       ('Isaac', 'Ngoc', '2002-10-10', 'user44', 'user44@example.com', 'Vietland', 'USER', ''),
       ('Liam', 'Vo', '1994-04-14', 'user45', 'user45@example.com', 'Thu Village', 'USER', ''),
       ('Chloe', 'Tran', '2001-08-18', 'user46', 'user46@example.com', 'Son City', 'USER', ''),
       ('Mila', 'Le', '1993-11-03', 'user47', 'user47@example.com', 'Thanhland', 'USER', ''),
       ('Evelyn', 'Dinh', '1981-01-20', 'user48', 'user48@example.com', 'Linhborough', 'USER', ''),
       ('Jack', 'Bui', '1999-09-09', 'user49', 'user49@example.com', 'Nguyen City', 'MODERATOR',
        ''),
       ('Scarlett', 'Ly', '1996-03-15', 'user50', 'user50@example.com', 'Trucville', 'USER', ''),
       ('Hannah', 'Duong', '1989-06-24', 'user51', 'user51@example.com', 'Anhburg', 'USER', ''),
       ('Daniel', 'Hoang', '1991-12-31', 'user52', 'user52@example.com', 'Sonburgh', 'USER', ''),
       ('Bella', 'Vu', '1998-02-02', 'user53', 'user53@example.com', 'Kimland', 'USER', ''),
       ('Lucas', 'Tran', '1986-05-18', 'user54', 'user54@example.com', 'Trung City', 'MODERATOR',
        ''),
       ('Eleanor', 'Luong', '1995-07-07', 'user55', 'user55@example.com', 'Quangtown', 'USER', ''),
       ('Leo', 'Nguyen', '1990-11-11', 'user56', 'user56@example.com', 'Thuyville', 'USER', ''),
       ('Avery', 'Pham', '1984-08-29', 'user57', 'user57@example.com', 'Tuanburgh', 'USER', ''),
       ('Carter', 'Dinh', '1992-09-21', 'user58', 'user58@example.com', 'Binhfield', 'USER', ''),
       ('Grace', 'Dao', '2000-06-12', 'user59', 'user59@example.com', 'Giabury', 'USER', ''),
       ('Henry', 'Do', '1997-10-19', 'user60', 'user60@example.com', 'Anhtown', 'MODERATOR', '');

INSERT INTO book (title, numberOfPages, yearOfPublication, shortDescription, availableCopies,
                  totalCopies, coverImage)
VALUES ('1984', 328, 1949, 'Winston Smith sống trong một xã hội bị kiểm soát nghiêm ngặt.', 120,
        150, LOAD_FILE('D:/Documents/GitHub/Library Manager/others/coverImage/1984.png')),
       ('A Clockwork Orange', 192, 1962, 'Cuốn sách xoay quanh Alex, một thanh niên bạo lực.', 95,
        100,
        LOAD_FILE('D:/Documents/GitHub/Library Manager/others/coverImage/A Clockwork Orange.png')),
       ('A Farewell to Arms', 332, 1929, 'Câu chuyện tình yêu giữa Trung úy Henry và Catherine.',
        80, 120,
        LOAD_FILE('D:/Documents/GitHub/Library Manager/others/coverImage/A Farewell to Arms.png')),
       ('A Tale of Two Cities', 489, 1859, 'Câu chuyện về hai thành phố, London và Paris.', 100,
        150,
        LOAD_FILE('D:/Documents/GitHub/Library Manager/others/coverImage/A Tale of Two Cities.png')),
       ('Altered Carbon', 540, 2002, 'Một tương lai nơi ý thức con người có thể được chuyển đổi.',
        110, 130,
        LOAD_FILE('D:/Documents/GitHub/Library Manager/others/coverImage/Altered Carbon.png')),
       ('Anna Karenina', 864, 1877, 'Một tác phẩm kinh điển của văn học Nga.', 75, 100,
        LOAD_FILE('D:/Documents/GitHub/Library Manager/others/coverImage/Anna Karenina.png')),
       ('Atlas Shrugged', 1168, 1957, 'Tác phẩm phiêu lưu chính trị-kinh tế.', 50, 80,
        LOAD_FILE('D:/Documents/GitHub/Library Manager/others/coverImage/Atlas Shrugged.png')),
       ('Beloved', 324, 1987, 'Câu chuyện kể về Sethe, một phụ nữ nô lệ.', 60, 90,
        LOAD_FILE('D:/Documents/GitHub/Library Manager/others/coverImage/Beloved.png')),
       ('Brave New World', 288, 1932, 'Xã hội tương lai với công nghệ và kiểm soát tâm trí.', 130,
        150,
        LOAD_FILE('D:/Documents/GitHub/Library Manager/others/coverImage/Brave New World.png')),
       ('Crime and Punishment', 617, 1866, 'Cuốn tiểu thuyết về Raskolnikov, một sinh viên nghèo.',
        70, 120,
        LOAD_FILE('D:/Documents/GitHub/Library Manager/others/coverImage/Crime and Punishment.png')),
       ('The Great Gatsby', 180, 1925, 'Câu chuyện về sự phù hoa và sự sụp đổ của Jay Gatsby.', 85,
        100,
        LOAD_FILE('D:/Documents/GitHub/Library Manager/others/coverImage/The Great Gatsby.png'));

INSERT INTO author (firstName, lastName, dateOfBirth, biography)
VALUES ('George', 'Orwell', '1903-06-25', 'Nhà văn nổi tiếng với các tác phẩm phản địa đàng.'),
       ('Anthony', 'Burgess', '1917-02-25', 'Tác giả nổi tiếng với văn phong độc đáo.'),
       ('Ernest', 'Hemingway', '1899-07-21', 'Nhà văn Mỹ nổi tiếng với lối viết súc tích.'),
       ('Charles', 'Dickens', '1812-02-07', 'Nhà văn Anh được yêu thích trong thế kỷ 19.'),
       ('Richard K.', 'Morgan', '1965-09-24', 'Tác giả khoa học viễn tưởng người Anh.'),
       ('Leo', 'Tolstoy', '1828-09-09', 'Tác giả vĩ đại của văn học Nga.'),
       ('Ayn', 'Rand', '1905-02-02', 'Nhà triết học và tiểu thuyết gia người Mỹ.'),
       ('Toni', 'Morrison', '1931-02-18', 'Nhà văn da màu nổi tiếng với các tác phẩm về nô lệ.'),
       ('Aldous', 'Huxley', '1894-07-26', 'Nhà văn Anh với các tác phẩm về xã hội dystopia.'),
       ('Fyodor', 'Dostoevsky', '1821-11-11', 'Nhà văn Nga với các tác phẩm triết học sâu sắc.'),
       ('F. Scott', 'Fitzgerald', '1896-09-24', 'Nhà văn nổi tiếng với tác phẩm về Giấc mơ Mỹ.');

INSERT INTO genre (name, description)
VALUES ('novel', 'A long narrative work of fiction with complex characters.'),
       ('political', 'Involves themes related to political systems and governance.'),
       ('science fiction', 'Explores futuristic concepts and advanced technology.'),
       ('mentality', 'Examines psychological themes and the human mind.'),
       ('adventure', 'Features exciting journeys and bold actions.'),
       ('historical', 'Set in a past time period, often with real historical figures.'),
       ('romance', 'Focuses on romantic relationships and emotions.'),
       ('philosophy', 'Explores philosophical concepts through narrative.');

INSERT INTO bookAuthor (bookId, authorId)
VALUES (1, 1),   -- 1984 by George Orwell
       (2, 2),   -- A Clockwork Orange by Anthony Burgess
       (3, 3),   -- A Farewell to Arms by Ernest Hemingway
       (4, 4),   -- A Tale of Two Cities by Charles Dickens
       (5, 5),   -- Altered Carbon by Richard K. Morgan
       (6, 6),   -- Anna Karenina by Leo Tolstoy
       (7, 7),   -- Atlas Shrugged by Ayn Rand
       (8, 8),   -- Beloved by Toni Morrison
       (9, 9),   -- Brave New World by Aldous Huxley
       (10, 10), -- Crime and Punishment by Fyodor Dostoevsky
       (11, 11); -- The Great Gatsby by F. Scott Fitzgerald

INSERT INTO bookGenre (bookId, genreId)
VALUES (1, 1),
       (1, 3),
       (1, 2),  -- 1984
       (2, 4),
       (2, 1),  -- A Clockwork Orange
       (3, 5),
       (3, 6),
       (3, 1),  -- A Farewell to Arms
       (4, 6),
       (4, 1),  -- A Tale of Two Cities
       (5, 1),
       (5, 3),  -- Altered Carbon
       (6, 1),
       (6, 7),  -- Anna Karenina
       (7, 1),
       (7, 8),  -- Atlas Shrugged
       (8, 6),
       (8, 1),  -- Beloved
       (9, 1),
       (9, 3),  -- Brave New World
       (10, 1), -- Crime and Punishment
       (11, 1),
       (11, 6); -- The Great Gatsby

INSERT INTO review (userId, bookId, rating, comment)
VALUES (1, 8, 2, 'Cuốn sách này giúp tôi hiểu thêm về lịch sử.'),
       (1, 10, 1, 'Tôi rất thích cuốn sách này, đọc mãi không chán.'),
       (1, 5, 2, 'Một tác phẩm đáng đọc, để lại nhiều suy ngẫm.'),
       (2, 10, 3, 'Cuốn sách này giúp tôi hiểu thêm về lịch sử.'),
       (2, 1, 3, 'Một tác phẩm đáng đọc, để lại nhiều suy ngẫm.'),
       (2, 8, 2, 'Thật thất vọng về nội dung của cuốn sách này.'),
       (3, 6, 2, 'Thật thất vọng về nội dung của cuốn sách này.'),
       (3, 5, 3, 'Nhân vật được xây dựng tốt, câu chuyện lôi cuốn.'),
       (4, 11, 1, 'Một tác phẩm đáng đọc, để lại nhiều suy ngẫm.'),
       (4, 6, 4, 'Cuốn sách này giúp tôi hiểu thêm về lịch sử.'),
       (4, 7, 5, 'Tôi rất thích cuốn sách này, đọc mãi không chán.'),
       (4, 10, 1, 'Tôi rất thích cuốn sách này, đọc mãi không chán.'),
       (5, 9, 3, 'Tôi rất thích cuốn sách này, đọc mãi không chán.'),
       (5, 10, 5, 'Sách này thật sự rất hay và bổ ích.'),
       (5, 7, 4, 'Thật thất vọng về nội dung của cuốn sách này.'),
       (6, 9, 5, 'Tôi không thích cách viết của tác giả trong cuốn này.'),
       (6, 4, 1, 'Quá dài dòng, mất đi sự hấp dẫn ban đầu.'),
       (7, 3, 4, 'Tôi rất thích cuốn sách này, đọc mãi không chán.'),
       (7, 11, 5, 'Nhân vật được xây dựng tốt, câu chuyện lôi cuốn.'),
       (8, 5, 4, 'Cuốn sách này giúp tôi hiểu thêm về lịch sử.'),
       (8, 1, 1, 'Cốt truyện cuốn hút nhưng kết thúc hơi bất ngờ.'),
       (8, 4, 5, 'Thật thất vọng về nội dung của cuốn sách này.'),
       (8, 8, 3, 'Quá dài dòng, mất đi sự hấp dẫn ban đầu.'),
       (9, 4, 3, 'Cuốn sách này giúp tôi hiểu thêm về lịch sử.'),
       (10, 10, 2, 'Tôi không thích cách viết của tác giả trong cuốn này.'),
       (10, 5, 2, 'Tôi không thích cách viết của tác giả trong cuốn này.'),
       (10, 8, 4, 'Sách này thật sự rất hay và bổ ích.'),
       (10, 9, 5, 'Tôi rất thích cuốn sách này, đọc mãi không chán.'),
       (2, 7, 4, 'Cuốn sách rất ý nghĩa, tôi sẽ giới thiệu cho bạn bè.'),
       (3, 6, 5, 'Một câu chuyện đầy cảm xúc và sâu sắc.'),
       (6, 9, 4, 'Mình đã học được nhiều điều từ cuốn sách này.'),
       (7, 5, 2, 'Câu chuyện không thực sự thu hút, nhưng có vài chi tiết thú vị.'),
       (8, 10, 5, 'Sách này thật sự rất hay, tôi đọc một mạch không dừng lại được.'),
       (9, 8, 3, 'Nội dung hơi dài dòng nhưng có những bài học sâu sắc.'),
       (1, 4, 2, 'Cốt truyện khó theo dõi và không lôi cuốn như tôi mong đợi.'),
       (2, 1, 5, 'Tác phẩm này rất nổi bật với cốt truyện đặc biệt và ý nghĩa.'),
       (4, 3, 4, 'Một cuốn sách nên đọc để hiểu thêm về cuộc sống.'),
       (5, 11, 3, 'Có nhiều điểm hay nhưng không phù hợp với gu của tôi.'),
       (6, 7, 4, 'Tác giả đã mang đến một câu chuyện cực kỳ hấp dẫn.'),
       (8, 6, 1, 'Không như mong đợi, cảm giác hơi nhàm chán.'),
       (10, 9, 4, 'Tôi thấy cuốn sách này rất bổ ích và đáng đọc.'),
       (3, 5, 5, 'Một tác phẩm tuyệt vời, đầy cảm xúc và ý nghĩa.'),
       (7, 8, 2, 'Câu chuyện hơi dài và mất đi sự hấp dẫn giữa chừng.'),
       (1, 10, 4, 'Rất hay! Tôi sẽ giới thiệu cho những ai thích thể loại này.'),
       (2, 3, 3, 'Cuốn sách ổn nhưng không quá ấn tượng với tôi.'),
       (4, 11, 5, 'Cuốn sách này khiến tôi suy ngẫm rất nhiều về cuộc sống.'),
       (6, 7, 3, 'Nội dung thú vị, tuy nhiên một số phần hơi khó hiểu.'),
       (9, 10, 3, 'Những tình tiết rất thực tế và gần gũi.'),
       (3, 1, 4, 'Bài học từ cuốn sách này thật sự sâu sắc.'),
       (2, 6, 2, 'Cốt truyện có phần lặp lại nhưng vẫn hay.'),
       (8, 11, 4, 'Một cuốn sách đáng đọc để hiểu thêm về bản thân.'),
       (11, 1, 5, 'Cuốn sách thật sự gây ấn tượng mạnh, tôi sẽ đọc lại nhiều lần.'),
       (12, 3, 2, 'Mặc dù không hoàn toàn yêu thích, nhưng vẫn học được điều hay.'),
       (14, 5, 3, 'Một tác phẩm ổn, tuy nhiên có phần hơi dài.'),
       (15, 9, 5, 'Cuốn sách này giúp tôi mở rộng tầm nhìn về cuộc sống.'),
       (16, 6, 1, 'Không hấp dẫn như tôi nghĩ, hơi thất vọng.'),
       (17, 4, 4, 'Lời văn chạm đến trái tim, cuốn sách rất đáng đọc.'),
       (18, 11, 5, 'Tác phẩm xuất sắc, khuyến khích mọi người đọc.'),
       (19, 7, 2, 'Chưa thực sự ấn tượng, nhưng cũng có điểm tốt.'),
       (20, 10, 3, 'Nội dung khá thú vị, có một vài bài học bổ ích.'),
       (21, 8, 4, 'Cuốn sách làm tôi suy ngẫm về nhiều vấn đề trong cuộc sống.'),
       (22, 1, 3, 'Không thực sự như mong đợi, nhưng cũng đáng để đọc.'),
       (23, 6, 2, 'Cốt truyện không gây được nhiều ấn tượng cho tôi.'),
       (24, 5, 5, 'Tác giả thực sự có cách viết lôi cuốn và sinh động.'),
       (25, 9, 4, 'Một cuốn sách đáng suy ngẫm, tôi đã học được nhiều điều.'),
       (27, 10, 5, 'Một tác phẩm tuyệt vời, đáng để thưởng thức từng trang sách.'),
       (28, 3, 4, 'Tác giả đã làm rất tốt trong việc xây dựng cốt truyện.'),
       (29, 11, 3, 'Một cuốn sách ổn, nhưng không để lại nhiều ấn tượng.'),
       (30, 4, 5, 'Rất thú vị, tôi sẽ giới thiệu cho bạn bè đọc.'),
       (31, 7, 2, 'Nội dung hơi nhàm, không thu hút như tôi mong đợi.'),
       (32, 8, 4, 'Câu chuyện sâu sắc, tôi thực sự cảm thấy thích.'),
       (33, 5, 3, 'Tác phẩm ổn, nhưng không quá nổi bật.'),
       (34, 9, 5, 'Một cuốn sách đáng đọc, tôi rất ấn tượng với cách viết.'),
       (35, 1, 1, 'Rất khó hiểu, tôi không thể theo kịp câu chuyện.'),
       (36, 3, 4, 'Cốt truyện phát triển rất hợp lý và tự nhiên.'),
       (37, 10, 2, 'Không thực sự hay như tôi đã kỳ vọng.'),
       (38, 6, 5, 'Đây là một trong những tác phẩm hay nhất tôi từng đọc.'),
       (40, 8, 5, 'Tôi rất thích cách viết của tác giả, đầy cảm xúc và sâu sắc.');

INSERT INTO userSavedBook (userId, bookId, savedDate)
VALUES (1, 2, current_timestamp),
       (1, 5, current_timestamp),
       (1, 7, current_timestamp),
       (1, 9, current_timestamp),
       (1, 11, current_timestamp);
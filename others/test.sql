drop database if exists libraryManagement;

create database if not exists libraryManagement character set utf8mb4 collate utf8mb4_unicode_ci;

use libraryManagement;

create table user
(
    id             int auto_increment primary key,
    firstName      varchar(127)                                       not null,
    lastName       varchar(127)                                       not null,
    dateOfBirth    date,
    photo          mediumblob,
    username       varchar(127)                                       not null unique,
    email          varchar(127)                                       not null unique,
    address        varchar(255),
    role           enum ('USER', 'MODERATOR', 'ADMIN')                not null default 'USER',
    accountStatus  enum ('ACTIVE', 'INACTIVE', 'SUSPENDED', 'BANNED') not null default 'INACTIVE',
    dateCreated    datetime                                                    default current_timestamp,
    lastActive     datetime,
    violationCount int                                                         default 0,
    passwordHash   varchar(255)                                       not null
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

create table violation
(
    id            int auto_increment primary key,
    userId        int not null,
    violationDate datetime default current_timestamp,
    description   text,
    foreign key (userId) references user (id) on delete cascade
);

create index idxViolationUser on violation (userId);

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
values ('Hùng', 'Nguyễn Tường', '2005-11-07', '23020078', '23020078@vnu.edu.vn', 'Hà Nội', 'ADMIN',
        '$2a$11$gCDeAx4PAivnsEqFxLnVmeAhx0X.PRVgmEPTTJqkl8XwwjrXc/LPK');

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
        '$2b$12$dheaDRvYiutHAp6DOO5.lOJ6xv0v8DWEjUxK57s2SiDlC4m8nJ3YG');

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
VALUES (1, 8, 2, 'Cuốn sách này giúp tôi hiểu thêm về lịch sử.');
INSERT INTO review (userId, bookId, rating, comment)
VALUES (1, 10, 1, 'Tôi rất thích cuốn sách này, đọc mãi không chán.');
INSERT INTO review (userId, bookId, rating, comment)
VALUES (1, 5, 2, 'Một tác phẩm đáng đọc, để lại nhiều suy ngẫm.');
INSERT INTO review (userId, bookId, rating, comment)
VALUES (2, 10, 3, 'Cuốn sách này giúp tôi hiểu thêm về lịch sử.');
INSERT INTO review (userId, bookId, rating, comment)
VALUES (2, 1, 3, 'Một tác phẩm đáng đọc, để lại nhiều suy ngẫm.');
INSERT INTO review (userId, bookId, rating, comment)
VALUES (2, 8, 2, 'Thật thất vọng về nội dung của cuốn sách này.');
INSERT INTO review (userId, bookId, rating, comment)
VALUES (3, 6, 2, 'Thật thất vọng về nội dung của cuốn sách này.');
INSERT INTO review (userId, bookId, rating, comment)
VALUES (3, 5, 3, 'Nhân vật được xây dựng tốt, câu chuyện lôi cuốn.');
INSERT INTO review (userId, bookId, rating, comment)
VALUES (4, 11, 1, 'Một tác phẩm đáng đọc, để lại nhiều suy ngẫm.');
INSERT INTO review (userId, bookId, rating, comment)
VALUES (4, 6, 4, 'Cuốn sách này giúp tôi hiểu thêm về lịch sử.');
INSERT INTO review (userId, bookId, rating, comment)
VALUES (4, 7, 5, 'Tôi rất thích cuốn sách này, đọc mãi không chán.');
INSERT INTO review (userId, bookId, rating, comment)
VALUES (4, 10, 1, 'Tôi rất thích cuốn sách này, đọc mãi không chán.');
INSERT INTO review (userId, bookId, rating, comment)
VALUES (5, 9, 3, 'Tôi rất thích cuốn sách này, đọc mãi không chán.');
INSERT INTO review (userId, bookId, rating, comment)
VALUES (5, 10, 5, 'Sách này thật sự rất hay và bổ ích.');
INSERT INTO review (userId, bookId, rating, comment)
VALUES (5, 7, 4, 'Thật thất vọng về nội dung của cuốn sách này.');
INSERT INTO review (userId, bookId, rating, comment)
VALUES (6, 9, 5, 'Tôi không thích cách viết của tác giả trong cuốn này.');
INSERT INTO review (userId, bookId, rating, comment)
VALUES (6, 4, 1, 'Quá dài dòng, mất đi sự hấp dẫn ban đầu.');
INSERT INTO review (userId, bookId, rating, comment)
VALUES (7, 3, 4, 'Tôi rất thích cuốn sách này, đọc mãi không chán.');
INSERT INTO review (userId, bookId, rating, comment)
VALUES (7, 11, 5, 'Nhân vật được xây dựng tốt, câu chuyện lôi cuốn.');
INSERT INTO review (userId, bookId, rating, comment)
VALUES (8, 5, 4, 'Cuốn sách này giúp tôi hiểu thêm về lịch sử.');
INSERT INTO review (userId, bookId, rating, comment)
VALUES (8, 1, 1, 'Cốt truyện cuốn hút nhưng kết thúc hơi bất ngờ.');
INSERT INTO review (userId, bookId, rating, comment)
VALUES (8, 4, 5, 'Thật thất vọng về nội dung của cuốn sách này.');
INSERT INTO review (userId, bookId, rating, comment)
VALUES (8, 8, 3, 'Quá dài dòng, mất đi sự hấp dẫn ban đầu.');
INSERT INTO review (userId, bookId, rating, comment)
VALUES (9, 4, 3, 'Cuốn sách này giúp tôi hiểu thêm về lịch sử.');
INSERT INTO review (userId, bookId, rating, comment)
VALUES (10, 10, 2, 'Tôi không thích cách viết của tác giả trong cuốn này.');
INSERT INTO review (userId, bookId, rating, comment)
VALUES (10, 5, 2, 'Tôi không thích cách viết của tác giả trong cuốn này.');
INSERT INTO review (userId, bookId, rating, comment)
VALUES (10, 8, 4, 'Sách này thật sự rất hay và bổ ích.');
INSERT INTO review (userId, bookId, rating, comment)
VALUES (10, 9, 5, 'Tôi rất thích cuốn sách này, đọc mãi không chán.');

INSERT INTO userSavedBook (userId, bookId) VALUES (1, 1);
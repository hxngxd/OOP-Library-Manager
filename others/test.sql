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
    dateAdded         datetime      default current_timestamp,
    lastUpdated       datetime      default current_timestamp on update current_timestamp,
    availableCopies   int  not null default 1,
    totalCopies       int  not null default 3
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
    requestDate         datetime default current_timestamp,
    approvalDate        datetime,
    borrowDate          datetime,
    estimatedReturnDate date,
    actualReturnDate    datetime,
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

SET @basePath = 'D:/Documents/GitHub/Library Manager/others/coverImage/';
INSERT INTO book (title, numberOfPages, yearOfPublication, shortDescription, coverImage)
VALUES ('1984', 328, 1949, 'Winston Smith sống trong một xã hội bị kiểm soát nghiêm ngặt.',
        LOAD_FILE(CONCAT(@basePath, '1984.png'))),
       ('A Clockwork Orange', 192, 1962, 'Cuốn sách xoay quanh Alex, một thanh niên bạo lực.',
        LOAD_FILE(CONCAT(@basePath, 'A Clockwork Orange.png'))),
       ('A Farewell to Arms', 332, 1929, 'Câu chuyện tình yêu giữa Trung úy Henry và Catherine.',
        LOAD_FILE(concat(@basePath, 'A Farewell to Arms.png'))),
       ('A Tale of Two Cities', 489, 1859, 'Câu chuyện về hai thành phố, London và Paris.',
        LOAD_FILE(concat(@basePath, 'A Tale of Two Cities.png'))),
       ('Altered Carbon', 540, 2002, 'Một tương lai nơi ý thức con người có thể được chuyển đổi.',
        LOAD_FILE(concat(@basePath, 'Altered Carbon.png'))),
       ('Anna Karenina', 864, 1877, 'Một tác phẩm kinh điển của văn học Nga.',
        LOAD_FILE(concat(@basePath, 'Anna Karenina.png'))),
       ('Atlas Shrugged', 1168, 1957, 'Tác phẩm phiêu lưu chính trị-kinh tế.',
        LOAD_FILE(concat(@basePath, 'Atlas Shrugged.png'))),
       ('Beloved', 324, 1987, 'Câu chuyện kể về Sethe, một phụ nữ nô lệ.',
        LOAD_FILE(concat(@basePath, 'Beloved.png'))),
       ('Brave New World', 288, 1932, 'Xã hội tương lai với công nghệ và kiểm soát tâm trí.',
        LOAD_FILE(concat(@basePath, 'Brave New World.png'))),
       ('Crime and Punishment', 617, 1866, 'Cuốn tiểu thuyết về Raskolnikov, một sinh viên nghèo.',
        LOAD_FILE(concat(@basePath, 'Crime and Punishment.png'))),
       ('The Great Gatsby', 180, 1925, 'Câu chuyện về sự phù hoa và sự sụp đổ của Jay Gatsby.',
        LOAD_FILE(concat(@basePath, 'The Great Gatsby.png'))),
        ('The Catcher in the Rye', 277, 1951, 'Câu chuyện về Holden Caulfield, một thiếu niên mất phương hướng.',
        LOAD_FILE(concat(@basePath, 'The Catcher in the Rye.png'))),
        ('To Kill a Mockingbird', 281, 1960, 'Câu chuyện về một vụ án giết người ở thị trấn nhỏ Maycomb.',
        LOAD_FILE(concat(@basePath, 'To Kill a Mockingbird.png'))),
        ('Pride and Prejudice', 279, 1813, 'Câu chuyện về tình yêu giữa Elizabeth Bennet và Mr. Darcy.',
        LOAD_FILE(concat(@basePath, 'Pride and Prejudice.png'))),
        ('The Picture of Dorian Gray', 254, 1890, 'Câu chuyện về Dorian Gray, một người trẻ đẹp và giàu có.',
        LOAD_FILE(concat(@basePath, 'The Picture of Dorian Gray.png'))),
        ('The Hobbit', 310, 1937, 'Cuộc phiêu lưu của Bilbo Baggins để giành lại một ngôi nhà.',
        LOAD_FILE(concat(@basePath, 'The Hobbit.png'))),
        ('The Alchemist', 197, 1988, 'Câu chuyện về Santiago, một người chăn cừu.',
        LOAD_FILE(concat(@basePath, 'The Alchemist.png'))),
        ('The Hunger Games', 374, 2008, 'Cuộc thi sinh tồn còn giữa 24 người trẻ từ 12 quận.',
        LOAD_FILE(concat(@basePath, 'The Hunger Games.png'))),
        ('The Fault in Our Stars', 313, 2012, 'Câu chuyện tình yêu giữa Hazel Grace Lancaster và Augustus Waters.',
        LOAD_FILE(concat(@basePath, 'The Fault in Our Stars.png'))),
        ('War and Peace', 1225, 1869, 'Câu chuyện về cuộc sống của nhiều nhân vật trong thời kỳ chiến tranh Napoleon.',
        LOAD_FILE(concat(@basePath, 'War and Peace.png'))),
        ('Wuthering Heights', 464, 1847, 'Câu chuyện tình yêu giữa Heathcliff và Catherine Earnshaw.',
        LOAD_FILE(concat(@basePath, 'Wuthering Heights.png'))),
        ('The Little Prince', 96, 1943, 'Câu chuyện về một hoàng tử nhỏ từ hành tinh B-612.',
        LOAD_FILE(concat(@basePath, 'The Little Prince.png'))),
        ('Enders Game', 324, 1985, 'Câu chuyện về Ender Wiggin, một thiếu niên thiên tài.',
        LOAD_FILE(concat(@basePath, 'Enders Game.png'))),
        ('The Giver', 208, 1993, 'Câu chuyện về Jonas, một thiếu niên sống trong một xã hội không có cảm xúc.',
        LOAD_FILE(concat(@basePath, 'The Giver.png'))),
        ('Fahrenheit 451', 249, 1953, 'Câu chuyện về Guy Montag, một lính cứu hỏa.',
        LOAD_FILE(concat(@basePath, 'Fahrenheit 451.png'))),
        ('The Road', 287, 2006, 'Câu chuyện về một người cha và con trai trong một thế giới hậu tận thế.',
        LOAD_FILE(concat(@basePath, 'The Road.png'))),
        ('Harry Potter and the Sorcerers Stone', 309, 1997, 'Câu chuyện về Harry Potter, một phù thủy trẻ.',
        LOAD_FILE(concat(@basePath, 'Harry Potter and the Sorcerers Stone.png'))),
        ('The Adventures of Huckleberry Finn', 366, 1884, 'Câu chuyện về Huckleberry Finn và Jim, một người nô lệ.',
        LOAD_FILE(concat(@basePath, 'The Adventures of Huckleberry Finn.png'))),
        ('The Adventures of Sherlock Holmes', 307, 1892, 'Câu chuyện về Sherlock Holmes, một thám tử tài ba.',
        LOAD_FILE(concat(@basePath, 'The Adventures of Sherlock Holmes.png'))),
        ('The Body', 133, 1982, 'Câu chuyện về một nhóm bạn trẻ đi tìm xác chết của một cậu bé.',
        LOAD_FILE(concat(@basePath, 'The Body.png'))),
        ('Don Quixote', 863, 1605, 'Câu chuyện về Don Quixote, một hiệp sĩ tưởng tượng.',
        LOAD_FILE(concat(@basePath, 'Don Quixote.png'))),
        ('Dracula', 418, 1897, 'Câu chuyện về Count Dracula, một ma cà rồng.',
        LOAD_FILE(concat(@basePath, 'Dracula.png'))),
        ('Frankenstein', 280, 1818, 'Câu chuyện về Victor Frankenstein và quái vật của ông.',
        LOAD_FILE(concat(@basePath, 'Frankenstein.png'))),
        ('Gone with the Wind', 1037, 1936, 'Câu chuyện về Scarlett O Hara và Rhett Butler.',
        LOAD_FILE(concat(@basePath, 'Gone with the Wind.png'))),
        ('Invisible Man', 581, 1952, 'Câu chuyện về một người đàn ông da màu ẩn mình.',
        LOAD_FILE(concat(@basePath, 'Invisible Man.png'))),
        ('Jane Eyre', 507, 1847, 'Câu chuyện về Jane Eyre, một cô gái mồ côi.',
        LOAD_FILE(concat(@basePath, 'Jane Eyre.png'))),
        ('Life of Pi', 319, 2001, 'Câu chuyện về Pi Patel, một chàng trai Ấn Độ.',
        LOAD_FILE(concat(@basePath, 'Life of Pi.png'))),
        ('Lord of the Flies', 224, 1954, 'Câu chuyện về một nhóm trẻ em mắc kẹt trên một hòn đảo.',
        LOAD_FILE(concat(@basePath, 'Lord of the Flies.png'))),
        ('Rebecca', 380, 1938, 'Câu chuyện về một phụ nữ trẻ kết hôn với một người đàn ông giàu có.',
        LOAD_FILE(concat(@basePath, 'Rebecca.png'))),
        ('The Stranger', 123, 1942, 'Câu chuyện về Meursault, một người đàn ông không có cảm xúc.',
        LOAD_FILE(concat(@basePath, 'The Stranger.png'))),
        ('The Glass Castle', 288, 2005, 'Câu chuyện về Jeannette Walls và gia đình cô.',
        LOAD_FILE(concat(@basePath, 'The Glass Castle.png'))),
        ('The Grapes of Wrath', 464, 1939, 'Câu chuyện về gia đình Joad trong thời kỳ Đại suy thoái.',
        LOAD_FILE(concat(@basePath, 'The Grapes of Wrath.png'))),
        ('The Idiot', 656, 1869, 'Câu chuyện về Prince Myshkin, một người đàn ông ngây thơ.',
        LOAD_FILE(concat(@basePath, 'The Idiot.png'))),
        ('The Godfather', 448, 1969, 'Câu chuyện về gia đình Corleone.',
        LOAD_FILE(concat(@basePath, 'The Godfather.png'))),
        ('The Jungle', 413, 1906, 'Câu chuyện về Jurgis Rudkus, một người nhập cư Litva.',
        LOAD_FILE(concat(@basePath, 'The Jungle.png'))),
        ('The Iliad', 683, 800, 'Câu chuyện về cuộc chiến giữa Hy Lạp và Troia.',
        LOAD_FILE(concat(@basePath, 'The Iliad.png'))),
        ('The Metamorphosis', 201, 1915, 'Câu chuyện về Gregor Samsa, một người bị biến thành côn trùng.',
        LOAD_FILE(concat(@basePath, 'The Metamorphosis.png'))),
        ('The Trial', 255, 1925, 'Câu chuyện về Josef K., một người bị buộc tội mà không biết tội danh.',
        LOAD_FILE(concat(@basePath, 'The Trial.png'))),
        ('Snow Crash', 470, 1992, 'Câu chuyện về Hiro Protagonist, một hacker và samurai.',
        LOAD_FILE(concat(@basePath, 'Snow Crash.png'))),
        ('Native Son', 504, 1940, 'Câu chuyện về Bigger Thomas, một người da màu.',
        LOAD_FILE(concat(@basePath, 'Native Son.png')));

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
       ('F. Scott', 'Fitzgerald', '1896-09-24', 'Nhà văn nổi tiếng với tác phẩm về Giấc mơ Mỹ.'),
       ('J.D.', 'Salinger', '1919-01-01', 'Nhà văn nổi tiếng với tác phẩm về tuổi trẻ.'),
       ('Harper', 'Lee', '1926-04-28', 'Nhà văn nổi tiếng với tác phẩm về phân biệt chủng tộc.'),
       ('Jane', 'Austen', '1775-12-16', 'Nhà văn nổi tiếng với các tác phẩm về tình yêu và xã hội.'),
       ('Oscar', 'Wilde', '1854-10-16', 'Nhà văn nổi tiếng với các tác phẩm về xã hội và nghệ thuật.'),
       ('J.R.R.', 'Tolkien', '1892-01-03', 'Nhà văn nổi tiếng với các tác phẩm về thế giới thần thoại.'),
       ('Paulo', 'Coelho', '1947-08-24', 'Nhà văn nổi tiếng với các tác phẩm về tâm linh và triết lý sống.'),
       ('Suzanne', 'Collins', '1962-08-10', 'Nhà văn nổi tiếng với các tác phẩm về cuộc thi sinh tồn và xã hội dystopia.'),
       ('John', 'Green', '1977-08-24', 'Nhà văn nổi tiếng với các tác phẩm về tình yêu và tuổi trẻ.'),
       ('Leo', 'Tolstoy', '1828-09-09', 'Nhà văn nổi tiếng với các tác phẩm về tình yêu và xã hội Nga.'),
       ('Emily', 'Bronte', '1818-07-30', 'Nhà văn nổi tiếng với các tác phẩm về tình yêu và bi kịch.'),
       ('Antoine de', 'Saint-Exupéry', '1900-06-29', 'Nhà văn nổi tiếng với các tác phẩm về tình yêu.'),
       ('Orson Scott', 'Card', '1951-08-24', 'Nhà văn nổi tiếng với các tác phẩm về tương lai.'),
       ('Lois', 'Lowry', '1937-03-20', 'Nhà văn nổi tiếng với các tác phẩm về tương lai.'),
       ('Ray', 'Bradbury', '1920-08-22', 'Nhà văn nổi tiếng với các tác phẩm về tương lai.'),
       ('Cormac', 'McCarthy', '1933-07-20', 'Nhà văn nổi tiếng với các tác phẩm về tương lai.'),
       ('J.K.', 'Rowling', '1965-07-31', 'Nhà văn nổi tiếng với các tác phẩm về phù thủy.'),
       ('Mark', 'Twain', '1835-11-30', 'Nhà văn nổi tiếng với các tác phẩm về cuộc sống nông thôn.'),
       ('Arthur Conan', 'Doyle', '1859-05-22', 'Nhà văn nổi tiếng với các tác phẩm về thám tử Sherlock Holmes.'),
       ('Stephen', 'King', '1947-09-21', 'Nhà văn nổi tiếng với các tác phẩm kinh dị.'),
       ('Miguel de', 'Cervantes', '1547-09-29', 'Nhà văn nổi tiếng với các tác phẩm hài hước.'),
       ('Bram', 'Stoker', '1847-11-08', 'Nhà văn nổi tiếng với các tác phẩm kinh dị.'),
       ('Mary', 'Shelley', '1797-08-30', 'Nhà văn nổi tiếng với các tác phẩm kinh dị.'),
       ('Margaret', 'Mitchell', '1900-11-08', 'Nhà văn nổi tiếng với các tác phẩm lịch sử.'),
       ('Ralph', 'Ellison', '1914-03-01', 'Nhà văn nổi tiếng với các tác phẩm về phân biệt chủng tộc.'),
       ('Charlotte', 'Bronte', '1816-04-21', 'Nhà văn nổi tiếng với các tác phẩm về tình yêu và bi kịch.'),
       ('Yann', 'Martel', '1963-06-25', 'Nhà văn nổi tiếng với các tác phẩm về tâm linh.'),
       ('William', 'Golding', '1911-09-19', 'Nhà văn nổi tiếng với các tác phẩm về tâm lý.'),
       ('Daphne', 'du Maurier', '1907-05-13', 'Nhà văn nổi tiếng với các tác phẩm về tình yêu.'),
       ('Albert', 'Camus', '1913-11-07', 'Nhà văn nổi tiếng với các tác phẩm về tâm lý.'),
       ('Jeannette', 'Walls', '1960-04-21', 'Nhà văn nổi tiếng với các tác phẩm về gia đình.'),
       ('John', 'Steinbeck', '1902-02-27', 'Nhà văn nổi tiếng với các tác phẩm về gia đình.'),
       ('Fyodor', 'Dostoevsky', '1821-11-11', 'Nhà văn nổi tiếng với các tác phẩm về tâm lý.'),
       ('Mario', 'Puzo', '1920-10-15', 'Nhà văn nổi tiếng với các tác phẩm về mafia.'),
       ('Upton', 'Sinclair', '1878-09-20', 'Nhà văn nổi tiếng với các tác phẩm về xã hội.'),
       ('Homer', '', '800-01-01', 'Nhà văn cổ điển nổi tiếng với các tác phẩm về thần thoại.'),
       ('Franz', 'Kafka', '1883-07-03', 'Nhà văn nổi tiếng với các tác phẩm về tâm lý.'),
       ('Franz', 'Kafka', '1883-07-03', 'Nhà văn nổi tiếng với các tác phẩm về tâm lý.'),
       ('Neal', 'Stephenson', '1959-10-31', 'Nhà văn nổi tiếng với các tác phẩm về tương lai.'),
       ('Richard', 'Wright', '1908-09-04', 'Nhà văn nổi tiếng với các tác phẩm về phân biệt chủng tộc.');

INSERT INTO genre (name, description)
VALUES ('novel', 'A long narrative work of fiction with complex characters.'),
       ('political', 'Involves themes related to political systems and governance.'),
       ('science fiction', 'Explores futuristic concepts and advanced technology.'),
       ('mentality', 'Examines psychological themes and the human mind.'),
       ('adventure', 'Features exciting journeys and bold actions.'),
       ('historical', 'Set in a past time period, often with real historical figures.'),
       ('romance', 'Focuses on romantic relationships and emotions.'),
       ('philosophy', 'Explores philosophical concepts through narrative.'),
       ('dystopia', 'Describes a society that is undesirable or frightening.'),
       ('crime', 'Involves criminal activities and the justice system.'),
       ('biography', 'Tells the life story of a real person.'),
       ('fantasy', 'Features magical elements and mythical creatures.'),
       ('spiritual', 'Explores religious or spiritual themes.'),
       ('children', 'Written for children and young readers.'),
       ('young adult', 'Written for teenagers and young adults.'),
       ('classic', 'A work that is considered to be of high quality and lasting value.');

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
       (11, 11), -- The Great Gatsby by F. Scott Fitzgerald
       (12, 12), -- The Catcher in the Rye by J.D. Salinger
       (13, 13), -- To Kill a Mockingbird by Harper Lee
       (14, 14), -- Pride and Prejudice by Jane Austen
       (15, 15), -- The Picture of Dorian Gray by Oscar Wilde
       (16, 16), -- The Hobbit by J.R.R. Tolkien
       (17, 17), -- The Alchemist by Paulo Coelho
       (18, 18), -- The Hunger Games by Suzanne Collins
       (19, 19), -- The Fault in Our Stars by John Green
       (20, 20), -- War and Peace by Leo Tolstoy
       (21, 21), -- Wuthering Heights by Emily Bronte
       (22, 22), -- The Little Prince by Antoine de Saint-Exupéry
       (23, 23), -- Enders Game by Orson Scott Card
       (24, 24), -- The Giver by Lois Lowry
       (25, 25), -- Fahrenheit 451 by Ray Bradbury
       (26, 26), -- The Road by Cormac McCarthy
       (27, 27), -- Harry Potter and the Sorcerers Stone by J.K. Rowling
       (28, 28), -- The Adventures of Huckleberry Finn by Mark Twain
       (29, 29), -- The Adventures of Sherlock Holmes by Arthur Conan Doyle
       (30, 30), -- The Body by Stephen King
       (31, 31), -- Don Quixote by Miguel de Cervantes
       (32, 32), -- Dracula by Bram Stoker
       (33, 33), -- Frankenstein by Mary Shelley
       (34, 34), -- Gone with the Wind by Margaret Mitchell
       (35, 35), -- Invisible by Ralph Ellison
       (36, 36), -- Jane Eyre by Charlotte Bronte
       (37, 37), -- Life of Pi by Yann Martel
       (38, 38), -- Lord of the Flies by William Golding
       (39, 39), -- Rebecca by Daphne du Maurier
       (40, 40), -- The Stranger by Albert Camus
       (41, 41), -- The Glass Castle by Jeannette Walls
       (42, 42), -- The Grapes of Wrath by John Steinbeck
       (43, 43), -- The Idiot by Fyodor Dostoevsky
       (44, 44), -- The Godfather by Mario Puzo
       (45, 45), -- The Jungle by Upton Sinclair
       (46, 46), -- The Iliad by Homer
       (47, 47), -- The Metamorphosis by Franz Kafka
       (48, 48), -- The Trial by Franz Kafka
       (49, 49), -- Snow Crash by Neal Stephenson
       (50, 50); -- Native Son by Richard Wright;



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
       (11, 6), -- The Great Gatsby
       (12, 1),
       (12, 6), -- The Catcher in the Rye
       (13, 1),
       (13, 6), -- To Kill a Mockingbird
       (14, 1),
       (14, 6), -- Pride and Prejudice
       (15, 1),
       (15, 6), -- The Picture of Dorian Gray
       (16, 1),
       (16, 6), -- The Hobbit
       (17, 1),
       (17, 6), -- The Alchemist
       (18, 1),
       (18, 6), -- The Hunger Games
       (19, 1),
       (19, 6), -- The Fault in Our Stars
       (20, 1),
       (20, 6), -- War and Peace
       (21, 1),
       (21, 6), -- Wuthering Heights
       (22, 1),
       (22, 6), -- The Little Prince
       (23, 1),
       (23, 6), -- Enders Game
       (24, 1),
       (24, 6), -- The Giver
       (25, 1),
       (25, 6), -- Fahrenheit 451
       (26, 1),
       (26, 6), -- The Road
       (27, 1),
       (27, 6), -- Harry Potter and the Sorcerers Stone
       (28, 1),
       (28, 6), -- The Adventures of Huckleberry Finn
       (29, 1),
       (29, 6), -- The Adventures of Sherlock Holmes
       (30, 1),
       (30, 6), -- The Body
       (31, 1),
       (31, 6), -- Don Quixote
       (32, 1),
       (32, 6), -- Dracula
       (33, 1),
       (33, 6), -- Frankenstein
       (34, 1),
       (34, 6), -- Gone with the Wind
       (35, 1),
       (35, 6), -- Invisible Man
       (36, 1),
       (36, 6), -- Jane Eyre
       (37, 1),
       (37, 6), -- Life of Pi
       (38, 1),
       (38, 6), -- Lord of the Flies
       (39, 1),
       (39, 6), -- Rebecca
       (40, 1),
       (40, 6), -- The Stranger
     (41, 1),
     (41, 6), -- The Glass Castle
     (42, 1),
     (42, 6), -- The Grapes of Wrath
     (43, 1),
     (43, 6), -- The Idiot
     (44, 1),
     (44, 6), -- The Godfather
     (45, 1),
     (45, 6), -- The Jungle
     (46, 1),
     (46, 6), -- The Iliad
     (47, 1),
     (47, 6), -- The Metamorphosis
     (48, 1),
     (48, 6), -- The Trial
     (49, 1),
     (49, 6), -- Snow Crash
     (50, 1); -- Native Son

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
       (40, 8, 5, 'Tôi rất thích cách viết của tác giả, đầy cảm xúc và sâu sắc.'),
       (1, 12, 4, 'Cuốn sách này rất hay, tôi đã học được nhiều điều từ nó.'),
       (2, 13, 5, 'Một cuốn sách tuyệt vời, rất sâu sắc và cảm động.'),
       (3, 14, 3, 'Câu chuyện ổn, nhưng không để lại ấn tượng mạnh.'),
       (4, 15, 2, 'Cuốn sách này không hợp với tôi, cốt truyện khá nhàm chán.'),
       (5, 16, 4, 'Tôi rất thích cách viết của tác giả, câu chuyện rất hấp dẫn.'),
       (6, 17, 5, 'Một tác phẩm đáng đọc, tôi không thể bỏ xuống.'),
       (7, 18, 3, 'Cốt truyện khá hay, nhưng có phần lặp lại và dễ đoán.'),
       (8, 19, 4, 'Tác giả xây dựng nhân vật rất tốt, câu chuyện rất dễ hiểu.'),
       (9, 20, 5, 'Tôi yêu cuốn sách này, rất thú vị và đầy cảm hứng.'),
       (10, 21, 2, 'Tôi cảm thấy không có gì đặc biệt trong cuốn sách này.'),
       (11, 22, 4, 'Một cuốn sách rất thú vị, với nhiều bài học quý giá.'),
       (12, 23, 5, 'Tác phẩm này rất ấn tượng và đầy cảm xúc, tôi không thể dừng lại.'),
       (13, 24, 3, 'Tôi không thực sự thích cuốn sách này, nhưng vẫn có vài điểm hay.'),
       (14, 25, 5, 'Cuốn sách này thật sự tuyệt vời, tôi sẽ đọc lại nhiều lần.'),
       (15, 26, 4, 'Câu chuyện rất sâu sắc, mặc dù có một số đoạn hơi dài dòng.'),
       (16, 27, 2, 'Tôi không thể tiếp tục đọc cuốn sách này, cảm thấy khá thất vọng.'),
       (17, 28, 5, 'Một tác phẩm tuyệt vời, tôi rất thích cách tác giả xây dựng cốt truyện.'),
       (18, 29, 3, 'Câu chuyện khá thú vị, nhưng tôi cảm thấy một số chi tiết hơi thừa.'),
       (19, 30, 4, 'Tôi rất thích cuốn sách này, tuy nhiên có một số phần hơi dài.'),
       (20, 31, 5, 'Cuốn sách thật sự rất đáng đọc, tôi đã học được rất nhiều điều.'),
       (21, 32, 3, 'Nội dung ổn, nhưng tôi không thấy quá ấn tượng với cuốn sách này.'),
       (22, 33, 4, 'Một cuốn sách hay, có nhiều tình tiết hấp dẫn.'),
       (23, 34, 5, 'Tôi rất thích cách viết của tác giả, câu chuyện rất sâu sắc.'),
       (24, 35, 3, 'Cuốn sách này hơi dài dòng, nhưng vẫn có vài phần thú vị.'),
       (25, 36, 4, 'Tôi thích cuốn sách này, nhưng có một số tình tiết thiếu hấp dẫn.'),
       (26, 37, 5, 'Câu chuyện này thực sự rất tuyệt vời, tôi rất ấn tượng.'),
       (27, 38, 2, 'Cuốn sách không thể làm tôi hài lòng, khá thất vọng.'),
       (28, 39, 4, 'Một cuốn sách thú vị, nhưng có phần dễ đoán trước.'),
       (29, 40, 5, 'Tôi rất thích cuốn sách này, rất đáng đọc và suy ngẫm.'),
       (30, 12, 3, 'Cuốn sách này khá ổn, nhưng không phải là tác phẩm tôi yêu thích.'),
       (31, 13, 4, 'Tôi đã học được rất nhiều từ cuốn sách này, rất đáng đọc.'),
       (32, 14, 5, 'Cuốn sách tuyệt vời, không thể rời mắt khỏi trang sách.'),
       (33, 15, 2, 'Tôi không thích cuốn sách này, cảm thấy khá nhàm chán.'),
       (34, 16, 4, 'Câu chuyện khá hấp dẫn, mặc dù có một vài đoạn hơi dài.'),
       (35, 17, 5, 'Một tác phẩm xuất sắc, tôi sẽ giới thiệu cho mọi người.'),
       (36, 18, 3, 'Cuốn sách này ổn, nhưng tôi không thể cảm nhận được hết ý nghĩa của nó.'),
       (37, 19, 5, 'Tôi rất thích cuốn sách này, rất cảm động và đầy ý nghĩa.'),
       (38, 20, 4, 'Câu chuyện rất thú vị, nhưng một vài chi tiết vẫn chưa rõ ràng.'),
       (39, 21, 5, 'Một cuốn sách tuyệt vời, tôi đã học được nhiều điều từ nó.'),
       (40, 22, 2, 'Cuốn sách không thực sự thu hút tôi, mặc dù cốt truyện khá hay.');

INSERT INTO userSavedBook (userId, bookId)
VALUES (1, 5),
       (1, 12),
       (1, 7),
       (1, 18),
       (1, 22),
       (1, 30),
       (1, 15),
       (1, 37),
       (1, 50),
       (1, 41),
       (2, 2),
       (2, 8),
       (2, 21),
       (2, 33),
       (2, 10),
       (2, 48),
       (2, 14),
       (2, 39),
       (2, 17),
       (2, 28);
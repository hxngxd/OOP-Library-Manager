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
    availableCopies   int      default 100,
    totalCopies       int      default 100,
    averageRating     double   default 0
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
values ('Hung', 'Nguyen Tuong', '2005-11-07', '23020078', '23020078@vnu.edu.vn', 'Hà Nội', 'ADMIN',
        '$2a$11$gCDeAx4PAivnsEqFxLnVmeAhx0X.PRVgmEPTTJqkl8XwwjrXc/LPK'),
       ('Minh', 'Hoang Le Minh', '2005-09-07', '23020111', '23020111@vnu.edu.vn', 'Phường', 'MODERATOR',
        '$2a$11$EQFy4/NKDPQ0y1DBql9UtuCQmDOEQO/iY8LjLEhTRQcKz4X.OST9u');

select * from user;

select * from book;
import os
import pandas as pd
import mysql.connector
import re
import logging

# Set up logging configuration
logging.basicConfig(
    level=logging.INFO,
    format='%(asctime)s - %(levelname)s - %(message)s',
    handlers=[logging.StreamHandler(), logging.FileHandler('library_import.log', 'w', 'utf-8')]
)

# Define the path to the cover image folder
cover_image_folder = 'coverImage'  # Replace with the actual path if different

# Load the Excel file
file_path = 'Books_Authors_Genres_Complete.xlsx'  # Make sure this file is in the same directory
excel_data = pd.ExcelFile(file_path)

# Load the three sheets into dataframes
books_df = pd.read_excel(excel_data, sheet_name='Books')
authors_df = pd.read_excel(excel_data, sheet_name='Authors')
genres_df = pd.read_excel(excel_data, sheet_name='Genres')

# Establish a MySQL connection
connection = mysql.connector.connect(
    host='localhost',  # Replace with your MySQL host
    user='root',  # Replace with your MySQL username
    password='07112005',  # Replace with your MySQL password
    database='librarymanagement'  # Replace with your MySQL database name
)

cursor = connection.cursor()


# Helper function to read image as binary data
def read_image_as_blob(file_path):
    with open(file_path, 'rb') as file:
        binary_data = file.read()
    return binary_data


# Function to sanitize filenames (remove invalid characters and ignore straight and curly single quotes)
def sanitize_filename(title):
    # Remove invalid characters for Windows filenames and remove both straight (') and curly (‘, ’) quotes
    sanitized = re.sub(r'[\'‘’<>:"/\\|?*]', '', title)
    return sanitized


logging.info('Starting to insert authors into the database')

# 1. Insert authors into the author table
author_ids = {}
for index, row in authors_df.iterrows():
    first_name = row['First Name']
    last_name = row['Last Name']
    date_of_birth = row['Date of Birth'].strftime('%Y-%m-%d') if not pd.isnull(
        row['Date of Birth']) else 'NULL'
    biography = row['Biography'].replace("'", "''") if not pd.isnull(row['Biography']) else ''
    date_of_death = row['Date of Death'].strftime('%Y-%m-%d') if not pd.isnull(
        row['Date of Death']) else 'NULL'

    author_sql = f"INSERT INTO author (firstName, lastName, dateOfBirth, biography, dayOfDeath) VALUES ('{first_name}', '{last_name}', '{date_of_birth}', '{biography}', {f'NULL' if date_of_death == 'NULL' else f"'{date_of_death}'"});"
    cursor.execute(author_sql)
    author_id = cursor.lastrowid
    author_ids[f"{first_name} {last_name}"] = author_id

    logging.info(f"Inserted author: {first_name} {last_name}")

# Commit the author insertions
connection.commit()
logging.info('Authors have been committed to the database')

# 2. Insert genres into the genre table
logging.info('Starting to insert genres into the database')
for index, row in genres_df.iterrows():
    genre_name = row['Genre Name'].replace("'", "''")
    description = row['Description'].replace("'", "''")

    genre_sql = f"INSERT INTO genre (name, description) VALUES ('{genre_name}', '{description}');"
    cursor.execute(genre_sql)

    logging.info(f"Inserted genre: {genre_name}")

# Commit the genre insertions
connection.commit()
logging.info('Genres have been committed to the database')

# 3. Insert books, book-author, book-genre relationships, and cover images into the book table
total_books_with_images = 0
total_books = len(books_df)
missing_cover_books = []  # List to store books without cover images

logging.info('Starting to insert books into the database')

for index, row in books_df.iterrows():
    title = row['Title'].replace("'", "''")
    sanitized_title = sanitize_filename(title)  # Sanitize the title for file lookup
    number_of_pages = int(row['Number of Pages']) if not pd.isnull(
        row['Number of Pages']) else 'NULL'
    publication_year = int(row['Publication Year']) if not pd.isnull(
        row['Publication Year']) else 'NULL'
    description = row['Description'].replace("'", "''")
    authors = row['Author'].split(',')
    genres = row['Genre'].split(',')

    # Insert book into book table
    book_sql = f"INSERT INTO book (title, yearOfPublication, shortDescription, numberOfPages) VALUES ('{title}', {publication_year}, '{description}', {number_of_pages});"
    cursor.execute(book_sql)
    book_id = cursor.lastrowid

    logging.info(f"Inserted book: {title}")

    # Insert book-author relationships
    for author in authors:
        author = author.strip()
        author_id = author_ids.get(author)
        if author_id:
            book_author_sql = f"INSERT INTO bookAuthor (book, authorId) VALUES ({book_id}, {author_id});"
            cursor.execute(book_author_sql)
            logging.info(f"Inserted relationship: {title} -> {author}")

    # Insert book-genre relationships
    for genre in genres:
        genre = genre.strip()
        book_genre_sql = f"INSERT INTO bookGenre (book, genreId) VALUES ({book_id}, (SELECT id FROM genre WHERE name='{genre}'));"
        cursor.execute(book_genre_sql)
        logging.info(f"Inserted relationship: {title} -> {genre}")

    # Try to find a corresponding image file (.jpg or .png) for the book cover
    image_path_jpg = os.path.join(cover_image_folder, f"{sanitized_title}.jpg")
    image_path_png = os.path.join(cover_image_folder, f"{sanitized_title}.png")

    cover_image = None
    if os.path.exists(image_path_jpg):
        cover_image = read_image_as_blob(image_path_jpg)
        logging.info(f"Found JPG cover for: {title}")
    elif os.path.exists(image_path_png):
        cover_image = read_image_as_blob(image_path_png)
        logging.info(f"Found PNG cover for: {title}")
    else:
        # If no cover image found, add the book title to the missing list
        missing_cover_books.append(title)
        logging.warning(f"No cover image found for: {title}")

    if cover_image:
        # Update the book record in the database with the cover image
        update_query = "UPDATE book SET coverImage = %s WHERE id = %s"
        cursor.execute(update_query, (cover_image, book_id))
        total_books_with_images += 1

# Commit the changes for books and images
connection.commit()
logging.info('Books and cover images have been committed to the database')

# 4. Verification - Check the total number of books and books with cover images
cursor.execute("SELECT COUNT(*) FROM book;")
total_books_in_db = cursor.fetchone()[0]

cursor.execute("SELECT COUNT(*) FROM book WHERE coverImage IS NOT NULL;")
total_books_with_images_in_db = cursor.fetchone()[0]

# Close the database connection
cursor.close()
connection.close()

# Output the total counts for comparison
logging.info(f"Total books in the Excel file: {total_books}")
logging.info(f"Total books in the database: {total_books_in_db}")
logging.info(f"Total books with cover images: {total_books_with_images_in_db}")
logging.info(f"Total cover images added: {total_books_with_images}")

# Compare the numbers to check for missing cover images
if total_books_with_images == total_books_with_images_in_db:
    logging.info("All cover images have been added successfully.")
else:
    logging.warning("Some books are missing cover images.")

# Print the list of books that are missing cover images
if missing_cover_books:
    logging.warning("Books missing cover images:")
    for missing_book in missing_cover_books:
        logging.warning(f"- {missing_book}")
else:
    logging.info("All books have cover images.")

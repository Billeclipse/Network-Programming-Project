#ΔΗΜΙΟΥΡΓΙΑ ΒΑΣΗΣ ΔΕΔΟΜΕΝΩΝ DPFINAL_DB.
DROP DATABASE IF EXISTS DPFINAL_DB;
CREATE DATABASE DPFINAL_DB;
USE DPFINAL_DB;


#ΔΗΜΙΟΥΡΓΙΑ ΠΙΝΑΚΩΝ ΤΗΣ ΒΑΣΗΣ ΔΕΔΟΜΕΝΩΝ DPFINAL_DB.
CREATE TABLE Admin (
	admin_id INT(1) NOT NULL,
    admin_username VARCHAR(30) NOT NULL,
    admin_password VARCHAR(50) NOT NULL,
    PRIMARY KEY (admin_id)
);

CREATE TABLE Professors (
    professor_id INT AUTO_INCREMENT NOT NULL,
    professor_username VARCHAR(30) NOT NULL,
    professor_password VARCHAR(50) NOT NULL,
    professor_name VARCHAR(40),
    professor_surname VARCHAR(40),
    professor_grade VARCHAR(50),
    professor_valid TINYINT(1) NOT NULL DEFAULT 1,
    PRIMARY KEY (professor_id)
); 

CREATE TABLE Courses (
    course_id INT AUTO_INCREMENT NOT NULL,
    course_title_gr VARCHAR(60) NOT NULL,
    course_title_eng VARCHAR(60) NOT NULL,
    education_level TINYINT(1) NOT NULL,
    course_semester INT(1) NOT NULL,
    PRIMARY KEY (course_id)
);

CREATE TABLE Professors_Courses (
    course_id INT NOT NULL,
    professor_id INT NOT NULL,
    FOREIGN KEY (course_id)
        REFERENCES Courses (course_id),
    FOREIGN KEY (professor_id)
        REFERENCES Professors (professor_id),
    PRIMARY KEY (course_id , professor_id)
);

CREATE TABLE Learning_Objective_Categories (
    learning_objective_category_id INT(7) NOT NULL,
    learning_objective_category_title_gr VARCHAR(50) NOT NULL,
    learning_objective_category_title_eng VARCHAR(50) NOT NULL,    
    PRIMARY KEY (learning_objective_category_id)
);

CREATE TABLE Learning_Objectives (
    learning_objective_code VARCHAR(15) NOT NULL,
    learning_objective_title_gr VARCHAR(150) NOT NULL,
    learning_objective_title_eng VARCHAR(150) NOT NULL,
    learning_objective_category INT(7) NOT NULL,
    learning_objective_course INT(10) NOT NULL,
    FOREIGN KEY (learning_objective_category)
        REFERENCES Learning_Objective_Categories (learning_objective_category_id),
    FOREIGN KEY (learning_objective_course)
        REFERENCES Courses (course_id),
    PRIMARY KEY (learning_objective_code)
);

CREATE TABLE Prerequisites_Courses (
    prerequisite_course_id INT(10) NOT NULL,
    course_id INT NOT NULL,
    FOREIGN KEY (prerequisite_course_id)
        REFERENCES Courses (course_id),
    FOREIGN KEY (course_id)
        REFERENCES Courses (course_id),
    PRIMARY KEY (prerequisite_course_id , course_id)
);

CREATE TABLE Prerequisites_Learning_Objectives (
    prerequisite_learning_objective_code VARCHAR(15) NOT NULL,
    learning_objective_code VARCHAR(15) NOT NULL,
    FOREIGN KEY (prerequisite_learning_objective_code)
        REFERENCES Learning_Objectives (learning_objective_code),
    FOREIGN KEY (learning_objective_code)
        REFERENCES Learning_Objectives (learning_objective_code),
    PRIMARY KEY (prerequisite_learning_objective_code , learning_objective_code)
);

CREATE TABLE Students (
    student_id INT AUTO_INCREMENT NOT NULL,
    student_username VARCHAR(30) NOT NULL,
    student_password VARCHAR(50) NOT NULL, 
    student_name VARCHAR(40),
    student_surname VARCHAR(40),
    student_valid TINYINT(1) NOT NULL DEFAULT 0,
    PRIMARY KEY (student_id)
);

CREATE TABLE Favorite_Courses (
    course_id INT NOT NULL,
    student_id INT NOT NULL,
    FOREIGN KEY (course_id)
        REFERENCES Courses (course_id),
    FOREIGN KEY (student_id)
        REFERENCES Students (student_id),
    PRIMARY KEY (course_id , student_id)
);

CREATE TABLE Favorite_Learning_Objectives (
    learning_objective_code VARCHAR(15) NOT NULL,
    student_id INT NOT NULL,
    FOREIGN KEY (learning_objective_code)
        REFERENCES Learning_Objectives (learning_objective_code),
    FOREIGN KEY (student_id)
        REFERENCES Students (student_id),
    PRIMARY KEY (learning_objective_code , student_id)
);

CREATE TABLE Login_Instances (
    id INT NOT NULL,
    user_type VARCHAR(20) NOT NULL,
    authentication_key VARCHAR(50) NOT NULL,
    expires DATETIME NOT NULL,
    PRIMARY KEY (id,user_type)
);

CREATE TABLE Translation(
	translation_key VARCHAR(50) NOT NULL,
    translation_value TEXT NOT NULL,
    PRIMARY KEY (translation_key)
);

#ΕΙΣΑΓΩΓΗ ΔΕΔΟΜΕΝΩΝ ΕΛΕΝΧΟΥ ΣΤΟΥΣ ΠΙΝΑΚΕΣ ΤΗΣ ΒΑΣΗΣ ΔΕΔΟΜΕΝΩΝ DPFINAL_DB.
INSERT INTO Admin VALUES (0,"admin","$31$16$zknPRm-vDHHblTZc9M1qcP4EEMGWNtOO3oFYniJQA5w");
INSERT INTO Students VALUES (1, "billy", "$31$16$BHi9OUU71_4WkqYYHDfjuIymGKON1jmh--KG57NJhPY", "Βασίλειος", "Γιογουρτσόγλου",0);

LOAD DATA LOCAL INFILE 'C:/csv_files/Professors.csv' 
INTO TABLE Professors 
FIELDS TERMINATED BY ','
OPTIONALLY ENCLOSED BY '"'
LINES TERMINATED BY '\n'
(professor_username, professor_password,professor_name,professor_surname,professor_grade,professor_valid);

LOAD DATA LOCAL INFILE 'C:/csv_files/Courses.csv' 
INTO TABLE Courses 
FIELDS TERMINATED BY ','
OPTIONALLY ENCLOSED BY '"'
LINES TERMINATED BY '\n'
(course_title_gr, course_title_eng, education_level,course_semester);

LOAD DATA LOCAL INFILE 'C:/csv_files/Professors_Courses.csv' 
INTO TABLE Professors_Courses 
FIELDS TERMINATED BY ','
OPTIONALLY ENCLOSED BY '"'
LINES TERMINATED BY '\n'
(course_id, professor_id);

LOAD DATA LOCAL INFILE 'C:/csv_files/Prerequisites_Courses.csv' 
INTO TABLE Prerequisites_Courses 
FIELDS TERMINATED BY ','
OPTIONALLY ENCLOSED BY '"'
LINES TERMINATED BY '\n'
(prerequisite_course_id, course_id);

LOAD DATA LOCAL INFILE 'C:/csv_files/Learning_Objective_Categories.csv' 
INTO TABLE Learning_Objective_Categories 
FIELDS TERMINATED BY ','
OPTIONALLY ENCLOSED BY '"'
LINES TERMINATED BY '\n'
(learning_objective_category_id, learning_objective_category_title_gr, learning_objective_category_title_eng);

LOAD DATA LOCAL INFILE 'C:/csv_files/Learning_Objectives.csv' 
INTO TABLE Learning_Objectives 
FIELDS TERMINATED BY ','
OPTIONALLY ENCLOSED BY '"'
LINES TERMINATED BY '\n'
(learning_objective_code, learning_objective_title_gr, learning_objective_title_eng, learning_objective_category, learning_objective_course);

LOAD DATA LOCAL INFILE 'C:/csv_files/Prerequisites_Learning_Objectives.csv' 
INTO TABLE Prerequisites_Learning_Objectives 
FIELDS TERMINATED BY ','
OPTIONALLY ENCLOSED BY '"'
LINES TERMINATED BY '\n'
(prerequisite_learning_objective_code, learning_objective_code);

LOAD DATA LOCAL INFILE 'C:/csv_files/Translation.csv' 
INTO TABLE Translation 
FIELDS TERMINATED BY '|'
OPTIONALLY ENCLOSED BY '"'
LINES TERMINATED BY '\n'
(translation_key, translation_value);
--For courses table
INSERT INTO courses (code, title, credits, term, complete) 
VALUES ('PROG10082', 'Object Oriented Programming 1', 6.0, 1, true);
INSERT INTO courses (code, title, credits, term, complete) 
VALUES ('SYST10199', 'Web Programming', 3.0, 1, true);
INSERT INTO courses (code, title, credits) VALUES ('SYST10049', 'Web Development', 3.0);
INSERT INTO courses (code, title, credits) VALUES ('MATH18584', 'Computer Math Fundamentals', 4.0);
INSERT INTO courses (code, title, credits) VALUES ('COMM13729', 'The Art of Technical Communications', 3.0);
INSERT INTO courses (code, title, credits) VALUES ('PROG24178', 'Object Oriented Programming 2', 6.0);


--For Evaluations table
INSERT INTO evaluations (title, course, grade, max, weight, dueDate) 
VALUES ('Assignment 1', 'PROG10082', 9.0, 10, 10, '2021-06-16');
INSERT INTO evaluations (title, course, grade, max, weight, dueDate) 
VALUES ('Assignment 2', 'PROG10082', 8.0, 10, 20, '2021-06-30');
INSERT INTO evaluations (title, course, grade, max, weight, dueDate) 
VALUES ('Mid-Term', 'PROG10082', 9.0, 10, 25, '2021-07-16');
INSERT INTO evaluations (title, course, grade, max, weight, dueDate) 
VALUES ('Assignment 3', 'PROG10082', 7.0, 10, 10, '2021-07-19');
INSERT INTO evaluations (title, course, grade, max, weight, dueDate) 
VALUES ('Assignment 4', 'PROG10082', 9.0, 10, 10, '2021-07-31');
INSERT INTO evaluations (title, course, grade, max, weight, dueDate) 
VALUES ('Final', 'PROG10082', 9.0, 10, 25, '2021-08-16');

INSERT INTO evaluations (title, course, grade, max, weight, dueDate) 
VALUES ('Assignment 1', 'SYST10199', 9.0, 10, 10, '2021-06-16');
INSERT INTO evaluations (title, course, grade, max, weight, dueDate) 
VALUES ('Assignment 2', 'SYST10199', 6.0, 10, 20, '2021-06-30');
INSERT INTO evaluations (title, course, grade, max, weight, dueDate) 
VALUES ('Mid-Term', 'SYST10199', 9.0, 10, 25, '2021-07-16');
INSERT INTO evaluations (title, course, grade, max, weight, dueDate) 
VALUES ('Assignment 3', 'SYST10199', 8.0, 10, 10, '2021-07-19');
INSERT INTO evaluations (title, course, grade, max, weight, dueDate) 
VALUES ('Assignment 4', 'SYST10199', 7.0, 10, 10, '2021-07-31');
INSERT INTO evaluations (title, course, grade, max, weight, dueDate) 
VALUES ('Final', 'SYST10199', 6.0, 10, 25, '2021-08-16');
INSERT INTO evaluations (title, course, grade, max, weight, dueDate) 
VALUES ('Assignment 1', 'MATH18584', 7.0, 10, 10, '2021-07-11');


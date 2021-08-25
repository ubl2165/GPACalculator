--Courses Table not null means required
CREATE TABLE courses (
	code VARCHAR(9) PRIMARY KEY NOT NULL,
	title VARCHAR(255) NOT NULL,
	credits DECIMAL(3,1) NOT NULL,
	complete BOOLEAN DEFAULT FALSE,
	term INT,
	finalgrade DECIMAL(5,2)
);

--Evaluations Table not null means required
CREATE TABLE evaluations (
	id INT PRIMARY KEY AUTO_INCREMENT Not Null,
	title VARCHAR(255) NOT NULL,
	course VARCHAR(9) NOT NULL,
	grade DECIMAL(5,2),
	max DECIMAL(5,2),
	weight DECIMAL(5,2),
	dueDate DATE
);


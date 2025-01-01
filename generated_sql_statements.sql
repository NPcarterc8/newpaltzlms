-- SQL for studtend table
CREATE TABLE studtend_table (
  stu_id_ INTEGER,
  name_ TEXT,
  email TEXT,
  major TEXT,
  start_year INTEGER,
  end_year INTEGER,
  taken_class REAL
);

INSERT INTO studtend_table VALUES (1, 'test 1', 'test 1', 'cs', 2020, 2025, NULL);
INSERT INTO studtend_table VALUES (2, 'test 2', 'test 2', 'cs', 2020, 2024, NULL);
INSERT INTO studtend_table VALUES (3, 'test 3', 'test 3', 'cs', 2020, 2023, NULL);


-- SQL for class taken
CREATE TABLE class_taken (
  take_id_ INTEGER,
  stu_id INTEGER,
  class_id INTEGER
);

INSERT INTO class_taken VALUES (1, 1, 1);
INSERT INTO class_taken VALUES (2, 2, 1);
INSERT INTO class_taken VALUES (3, 2, 2);
INSERT INTO class_taken VALUES (4, 3, 1);
INSERT INTO class_taken VALUES (5, 3, 2);
INSERT INTO class_taken VALUES (6, 3, 3);


-- SQL for couyrse table 
CREATE TABLE couyrse_table (
  class_id INTEGER,
  name_ TEXT,
  co_depandces TEXT,
  pre_reg REAL
);

INSERT INTO couyrse_table VALUES (1, 'cpmsci 1', 'labs', NULL);
INSERT INTO couyrse_table VALUES (2, 'compsci 2', 'labs', 1.0);
INSERT INTO couyrse_table VALUES (3, 'compsci 3', 'labs', 2.0);



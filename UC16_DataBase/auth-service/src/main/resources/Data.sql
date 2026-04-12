CREATE TABLE measurements (
    id INT AUTO_INCREMENT PRIMARY KEY,
    value1 DOUBLE,
    unit1 VARCHAR(50),
    value2 DOUBLE,
    unit2 VARCHAR(50),
    operation VARCHAR(50),
    numeric_result DOUBLE,
    boolean_result BOOLEAN
);
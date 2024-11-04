create table
(
    code int primary key auto_increment,
    title char(30) not null
);
create table
(
    employee_id int primary key auto_increment,
    employee_name char(30) not null,
    designation_code int not null,
    dateOfBirth date not null,
    gender char(1),
    basic_salary decimal(12,2) not null,
    is_indian boolean not null,
    pan_number char(30) not null,
    aadhar_number char(30) not null
);
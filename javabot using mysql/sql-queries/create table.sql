use `whatsapp`;

CREATE TABLE IF NOT EXISTS `appointments` (
  `id` INT AUTO_INCREMENT PRIMARY KEY,
  `doctor_type` VARCHAR(50) NOT NULL,
  `user_phone` VARCHAR(15) NOT NULL,
  `appointment_fees` DECIMAL(10,2) NOT NULL,
  `registered_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `appointment_at` DATETIME DEFAULT CURRENT_TIMESTAMP
);

create table if not exists `doctors` (
`id` int AUTO_INCREMENT PRIMARY KEY,
  `doctor_type` VARCHAR(50) NOT NULL,
  `doctor_name` VARCHAR(50) NOT NULL,
  `doctor_phone` VARCHAR(15) NOT NULL,
  `appointment_fees` DECIMAL(10,2) NOT NULL
);

create table if not exists `orders` (
`id` int AUTO_INCREMENT PRIMARY KEY,
  `phone_number` VARCHAR(50) NOT NULL,
  `items` varchar(100) not null ,
  `total_cost` int NOT NULL,
  `place_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `delivered_to_location` varchar(50)
);

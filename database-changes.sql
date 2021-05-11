SET FOREIGN_KEY_CHECKS = 0;

LOCK TABLES `nerdygadgets`.`people` WRITE;
ALTER TABLE `nerdygadgets`.`people`
CHANGE COLUMN `PersonID` `PersonID` INT(11) NOT NULL AUTO_INCREMENT;

LOCK TABLES `nerdygadgets`.`customers` WRITE;
ALTER TABLE `nerdygadgets`.`customers`
CHANGE COLUMN `CustomerID` `CustomerID` INT(11) NOT NULL AUTO_INCREMENT;

LOCK TABLES `nerdygadgets`.`orders` WRITE;
ALTER TABLE `nerdygadgets`.`orders`
CHANGE COLUMN `OrderID` `OrderID` INT(11) NOT NULL AUTO_INCREMENT;
SET max_statement_time=0;

LOCK TABLES `nerdygadgets`.`orderlines` WRITE;
ALTER TABLE `nerdygadgets`.`orderlines`
CHANGE COLUMN `OrderLineID` `OrderLineID` INT(11) NOT NULL AUTO_INCREMENT;

LOCK TABLES `nerdygadgets`.`cities` WRITE;
ALTER TABLE `nerdygadgets`.`cities`
CHANGE COLUMN `CityID` `CityID` INT(11) NOT NULL AUTO_INCREMENT;

SET FOREIGN_KEY_CHECKS = 1;
UNLOCK TABLES;

ALTER TABLE `nerdygadgets`.`customers`
ADD COLUMN `Longitude` DOUBLE NULL AFTER `ValidTo`,
ADD COLUMN `Latitude` DOUBLE NULL AFTER `Longitude`;
ADD COLUMN `Altitude` DOUBLE NULL AFTER `Latitude`;

ALTER TABLE `nerdygadgets`.`people`
ADD COLUMN `Role` TINYINT NULL AFTER `ValidTo`;

/** Password for this user is 'nimda' */
INSERT INTO `nerdygadgets`.`people` (
    FullName, PreferredName, SearchName,
    IsPermittedToLogon, LogonName,
    IsExternalLogonProvider, HashedPassword,
    IsSystemUser, IsEmployee, IsSalesperson,
    PhoneNumber, EmailAddress, ValidFrom,
    ValidTo, LastEditedBy, Role
) VALUES (
    'Bezorger', 'Bezorger', 'Bezorger',
    1, 'bezorger', 0,
    '$2y$10$.D3CZ9FSjEYCOoZwlr.WjekQaijWBo4KTW0I3rpgm4Ou60cknIIXi', 0, 1,
    0, '+310612345678', 'bezorger@bezorger.nl',
    '2020-12-8 23:59:59', '9999-12-31 23:59:59', 1, 1
);

/** Password for this user is 'nimda' */
INSERT INTO `nerdygadgets`.`people` (
    FullName, PreferredName, SearchName,
    IsPermittedToLogon, LogonName,
    IsExternalLogonProvider, HashedPassword,
    IsSystemUser, IsEmployee, IsSalesperson,
    PhoneNumber, EmailAddress, ValidFrom,
    ValidTo, LastEditedBy, Role
) VALUES (
    'Admin', 'Admin', 'Admin',
    1, 'admin', 0,
    '$2y$10$.D3CZ9FSjEYCOoZwlr.WjekQaijWBo4KTW0I3rpgm4Ou60cknIIXi', 0, 1,
    0, '+310612345678', 'admin@admin.nl',
    '2020-12-8 23:59:59', '9999-12-31 23:59:59', 1, 2
);

/** Password for this user is 'nimda' */
INSERT INTO `nerdygadgets`.`people` (
    FullName, PreferredName, SearchName,
    IsPermittedToLogon, LogonName,
    IsExternalLogonProvider, HashedPassword,
    IsSystemUser, IsEmployee, IsSalesperson,
    PhoneNumber, EmailAddress, ValidFrom,
    ValidTo, LastEditedBy
) VALUES (
    'test', 'test', 'test',
    1, 'test', 0,
    '$2y$10$.D3CZ9FSjEYCOoZwlr.WjekQaijWBo4KTW0I3rpgm4Ou60cknIIXi', 0, 1,
    0, '+310612345678', 'test@test.nl',
    '2020-12-8 23:59:59', '9999-12-31 23:59:59', 1
);

DROP USER IF EXISTS 'nerdygadgets'@'localhost';

CREATE USER 'nerdygadgets'@'localhost' IDENTIFIED BY '^jnx$PK&hHg3Cz6y#V#S';
REVOKE ALL PRIVILEGES ON * . * FROM 'nerdygadgets'@'localhost';
GRANT SELECT, INSERT, UPDATE, DELETE ON * . * TO 'nerdygadgets'@'localhost';

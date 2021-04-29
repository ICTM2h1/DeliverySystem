ALTER TABLE `nerdygadgets`.`customers`
ADD COLUMN `Longitude` DOUBLE NULL AFTER `ValidTo`,
ADD COLUMN `Latitude` DOUBLE NULL AFTER `Longitude`;

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
    'Beheerder', 'Beheerder', 'Beheerder',
    1, 'beheerder', 0,
    '$2y$10$.D3CZ9FSjEYCOoZwlr.WjekQaijWBo4KTW0I3rpgm4Ou60cknIIXi', 0, 1,
    0, '+310612345678', 'beheerder@beheerder.nl',
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

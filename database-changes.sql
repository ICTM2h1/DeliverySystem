ALTER TABLE `nerdygadgets`.`customers`
ADD COLUMN `Longitude` DOUBLE NULL AFTER `ValidTo`,
ADD COLUMN `Latitude` DOUBLE NULL AFTER `Longitude`;

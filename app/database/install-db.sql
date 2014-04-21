SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema lingua
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `lingua` DEFAULT CHARACTER SET utf8 ;
USE `lingua` ;

-- -----------------------------------------------------
-- Table `lingua`.`notes`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `lingua`.`notes` (
  `note_id` INT(11) NOT NULL AUTO_INCREMENT,
  `foreign_lang` CHAR(5) NULL DEFAULT NULL,
  `foreign_note` VARCHAR(2000) NULL DEFAULT NULL,
  `local_lang` CHAR(5) NULL DEFAULT NULL,
  `local_note` VARCHAR(2000) NULL DEFAULT NULL,
  `additional_notes` VARCHAR(2000) NULL DEFAULT NULL,
  `source_url` VARCHAR(2000) NULL DEFAULT NULL,
  `translation_source` ENUM('Manual','Google') NULL DEFAULT NULL,
  PRIMARY KEY (`note_id`))
ENGINE = InnoDB
AUTO_INCREMENT = 72
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `lingua`.`supported_languages`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `lingua`.`supported_languages` (
  `language_code` CHAR(5) NOT NULL,
  PRIMARY KEY (`language_code`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

CREATE USER 'lingua';

GRANT SELECT, INSERT, DELETE, UPDATE ON TABLE `lingua`.`notes` TO 'lingua';
GRANT SELECT ON TABLE `lingua`.`supported_languages` TO 'lingua';

SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;

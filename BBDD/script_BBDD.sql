SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema `biblioteca`
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Table `biblioteca`.`Cubiculos`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `biblioteca`.`Cubiculos` (
  `idCubiculo` INT NOT NULL,
  `ocupado` INT NOT NULL,
  PRIMARY KEY (`idCubiculo`)
) ENGINE = InnoDB DEFAULT CHARACTER SET = utf8mb3;

-- -----------------------------------------------------
-- Table `biblioteca`.`Salas`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `biblioteca`.`Salas` (
  `idSala` INT NOT NULL,
  `ocupada` INT NOT NULL,
  PRIMARY KEY (`idSala`)
) ENGINE = InnoDB DEFAULT CHARACTER SET = utf8mb3;

-- -----------------------------------------------------
-- Table `biblioteca`.`Sensores`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `biblioteca`.`Sensores` (
  `idSensor` INT NOT NULL,
  `nombre` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`idSensor`)
) ENGINE = InnoDB DEFAULT CHARACTER SET = utf8mb3;

-- -----------------------------------------------------
-- Table `biblioteca`.`LecturaSensores`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `biblioteca`.`LecturaSensores` (
  `idLectura` INT NOT NULL AUTO_INCREMENT,
  `idSensor` INT NOT NULL,
  `valor` INT NULL DEFAULT NULL,
  `idCubiculo` INT NULL DEFAULT NULL COMMENT 'Null if the reading belongs to a room',
  `idSala` INT NULL DEFAULT NULL COMMENT 'Null if the reading belongs to a cubicle',
  `fechaHora` DATETIME NOT NULL,
  PRIMARY KEY (`idLectura`),
  INDEX `idx_idSala` (`idSala` ASC),
  INDEX `idx_idCubiculo` (`idCubiculo` ASC),
  INDEX `idx_idSensor` (`idSensor` ASC),
  CONSTRAINT `fk_LecturaSensores_Cubiculos`
    FOREIGN KEY (`idCubiculo`)
    REFERENCES `biblioteca`.`Cubiculos` (`idCubiculo`)
    ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_LecturaSensores_Salas`
    FOREIGN KEY (`idSala`)
    REFERENCES `biblioteca`.`Salas` (`idSala`)
    ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_LecturaSensores_Sensores`
    FOREIGN KEY (`idSensor`)
    REFERENCES `biblioteca`.`Sensores` (`idSensor`)
    ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB DEFAULT CHARACTER SET = utf8mb3;

-- -----------------------------------------------------
-- Table `biblioteca`.`Tarjetas`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `biblioteca`.`Tarjetas` (
  `idTarjeta` INT NOT NULL,
  PRIMARY KEY (`idTarjeta`)
) ENGINE = InnoDB DEFAULT CHARACTER SET = utf8mb3;

-- -----------------------------------------------------
-- Table `biblioteca`.`Usuarios`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `biblioteca`.`Usuarios` (
  `idUsuario` INT NOT NULL AUTO_INCREMENT,
  `passw` VARCHAR(45) NOT NULL,
  `email` VARCHAR(45) NOT NULL,
  `tipo` VARCHAR(20) NOT NULL,
  `idTarjeta` INT NOT NULL,
  PRIMARY KEY (`idUsuario`),
  UNIQUE INDEX `unique_email` (`email`),
  INDEX `idx_idTarjeta` (`idTarjeta` ASC),
  CONSTRAINT `fk_Usuarios_Tarjetas`
    FOREIGN KEY (`idTarjeta`)
    REFERENCES `biblioteca`.`Tarjetas` (`idTarjeta`)
    ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB DEFAULT CHARACTER SET = utf8mb3;

-- -----------------------------------------------------
-- Table `biblioteca`.`Reservas`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `biblioteca`.`Reservas` (
  `idReservas` INT NOT NULL AUTO_INCREMENT,
  `email_usuario` VARCHAR(45) NOT NULL,
  `idSala_sala` INT NOT NULL,
  `horaReserva` DATETIME NULL,
  PRIMARY KEY (`idReservas`),
  INDEX `idx_email_usuario` (`email_usuario` ASC),
  INDEX `idx_idSala` (`idSala_sala` ASC),
  CONSTRAINT `fk_Reservas_Usuarios`
    FOREIGN KEY (`email_usuario`)
    REFERENCES `biblioteca`.`Usuarios` (`email`)
    ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_Reservas_Salas`
    FOREIGN KEY (`idSala_sala`)
    REFERENCES `biblioteca`.`Salas` (`idSala`)
    ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB DEFAULT CHARACTER SET = utf8mb3;

ALTER TABLE `biblioteca`.`Usuarios` 
ADD COLUMN `nombreApellido` VARCHAR(100) NULL;

SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;

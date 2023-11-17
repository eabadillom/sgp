/**
 * Author:  Gabo
 * Created: 19/01/2023
 */
-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
-- -----------------------------------------------------
-- Schema sgp
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema sgp
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `sgp` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci ;
USE `sgp` ;

-- -----------------------------------------------------
-- Table `sgp`.`cat_area`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `sgp`.`cat_area` (
  `id_area` int unsigned NOT NULL AUTO_INCREMENT,
  `descripcion` varchar(45) NOT NULL,
  `activo` tinyint NOT NULL DEFAULT 1,
  PRIMARY KEY (`id_area`))
ENGINE=InnoDB
DEFAULT CHARSET=utf8mb4
COLLATE=utf8mb4_0900_ai_ci;

INSERT INTO `sgp`.`cat_area` (`descripcion`) VALUES ('Área 1');

-- -----------------------------------------------------
-- Table `sgp`.`cat_empresa`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `sgp`.`cat_empresa` (
  `id_empresa` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `descripcion` VARCHAR(45) CHARACTER SET 'utf8mb4' COLLATE 'utf8mb4_0900_ai_ci' NOT NULL,
  `activo` TINYINT NOT NULL DEFAULT 1,
  PRIMARY KEY (`id_empresa`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;

INSERT INTO `sgp`.`cat_empresa` (`descripcion`) VALUES ('Empresa 1');

-- -----------------------------------------------------
-- Table `sgp`.`cat_perfil`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `sgp`.`cat_perfil` (
  `id_perfil` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `descripcion` VARCHAR(45) NULL DEFAULT NULL,
  `activo` TINYINT NULL DEFAULT '1',
  PRIMARY KEY (`id_perfil`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;

INSERT INTO `sgp`.`cat_perfil` (`id_perfil`, `descripcion`, `activo`) VALUES(1, 'Administrador', 1);
INSERT INTO `sgp`.`cat_perfil` (`id_perfil`, `descripcion`, `activo`) VALUES(2, 'Administrador de usuarios', 1);
INSERT INTO `sgp`.`cat_perfil` (`id_perfil`, `descripcion`, `activo`) VALUES(3, 'Administrador de planta', 1);
INSERT INTO `sgp`.`cat_perfil` (`id_perfil`, `descripcion`, `activo`) VALUES(4, 'Gerente de planta', 1);
INSERT INTO `sgp`.`cat_perfil` (`id_perfil`, `descripcion`, `activo`) VALUES(5, 'Auxiliar general', 1);
INSERT INTO `sgp`.`cat_perfil` (`id_perfil`, `descripcion`, `activo`) VALUES(6, 'Montacarguista', 1);

-- -----------------------------------------------------
-- Table `sgp`.`cat_razon_social`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `sgp`.`cat_razon_social` (
  `id_razon_social` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `razon_socal` VARCHAR(45) NOT NULL,
  `rfc` VARCHAR(45) NOT NULL,
  `activo` TINYINT NOT NULL DEFAULT 1,
  `fecha_creacion` DATETIME NOT NULL,
  `fecha_mod` DATETIME NULL DEFAULT NULL,
  PRIMARY KEY (`id_razon_social`))
ENGINE=InnoDB
DEFAULT CHARSET=utf8mb4
COLLATE=utf8mb4_0900_ai_ci;

INSERT INTO `sgp`.`cat_razon_social` (`razon_socal`, `rfc`, `fecha_creacion`) VALUES ('INDUSTRIA DE REFRIGERACION KELANGAN', 'XAXX010101000', '2023-02-21 06:30:00');

-- -----------------------------------------------------
-- Table `sgp`.`cat_planta`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `sgp`.`cat_planta` (
  `id_planta` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `descripcion` VARCHAR(45) CHARACTER SET 'utf8mb4' COLLATE 'utf8mb4_0900_ai_ci' NOT NULL,
  `id_razon_social` INT UNSIGNED NULL,
  `activo` TINYINT NOT NULL DEFAULT 1,
  PRIMARY KEY (`id_planta`),
  INDEX `fk_planta_razon_idx` (`id_razon_social` ASC) VISIBLE,
  CONSTRAINT `fk_planta_razon`
    FOREIGN KEY (`id_razon_social`)
    REFERENCES `sgp`.`cat_razon_social` (`id_razon_social`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;

INSERT INTO `sgp`.`cat_planta` (`descripcion`, `id_razon_social`) VALUES ('Planta 1', 1);

-- -----------------------------------------------------
-- Table `sgp`.`cat_puesto`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `sgp`.`cat_puesto` (
  `id_puesto` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `descripcion` VARCHAR(45) CHARACTER SET 'utf8mb4' COLLATE 'utf8mb4_0900_ai_ci' NOT NULL,
  `activo` TINYINT NOT NULL DEFAULT 1,
  PRIMARY KEY (`id_puesto`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;

INSERT INTO `sgp`.`cat_puesto` (`descripcion`, `activo`) VALUES ('Gerente de Almacen', '1');
INSERT INTO `sgp`.`cat_puesto` (`descripcion`, `activo`) VALUES ('Subgerente de Almacen', '1');
INSERT INTO `sgp`.`cat_puesto` (`descripcion`, `activo`) VALUES ('Montacarguista', '1');
INSERT INTO `sgp`.`cat_puesto` (`descripcion`, `activo`) VALUES ('Auxiliar General', '1');
INSERT INTO `sgp`.`cat_puesto` (`descripcion`, `activo`) VALUES ('Aprendiz de Auxiliar General', '1');
INSERT INTO `sgp`.`cat_puesto` (`descripcion`, `activo`) VALUES ('Vigilante', '1');
INSERT INTO `sgp`.`cat_puesto` (`descripcion`, `activo`) VALUES ('Administrador General', '1');
INSERT INTO `sgp`.`cat_puesto` (`descripcion`, `activo`) VALUES ('Supervisor', '1');

-- -----------------------------------------------------
-- Table `sgp`.`det_empleado`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `sgp`.`det_empleado` (
  `id_empleado` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `num_empleado` VARCHAR(10) NOT NULL,
  `nombre` VARCHAR(45) NOT NULL,
  `primer_ap` VARCHAR(45) NOT NULL,
  `segundo_ap` VARCHAR(45) NULL DEFAULT NULL,
  `fecha_nacimiento` DATE NOT NULL,
  `fecha_registro` DATETIME NOT NULL,
  `fecha_modificacion` DATETIME NULL DEFAULT NULL,
  `id_perfil` INT UNSIGNED NOT NULL,
  `id_puesto` INT UNSIGNED NOT NULL,
  `curp` VARCHAR(18) NULL DEFAULT NULL,
  `rfc` VARCHAR(45) NOT NULL,
  `correo` VARCHAR(45) NULL DEFAULT NULL,
  `fecha_ingreso` DATETIME NOT NULL,
  `nss` VARCHAR(45) NULL DEFAULT NULL,
  `id_empresa` INT UNSIGNED NOT NULL,
  `activo` TINYINT NOT NULL DEFAULT 1,
  `id_planta` INT UNSIGNED NULL DEFAULT NULL,
  `id_area` INT UNSIGNED NULL,
  `fotografia` LONGTEXT NULL,
  `sueldo_diario` FLOAT UNSIGNED NOT NULL,
  PRIMARY KEY (`id_empleado`),
  INDEX `fk_empleado_perfil_idx` (`id_perfil` ASC) VISIBLE,
  INDEX `fk_empleado_puesto_idx` (`id_puesto` ASC) VISIBLE,
  INDEX `fk_empleado_empresa_idx` (`id_empresa` ASC) VISIBLE,
  INDEX `fk_empleado_planta_idx` (`id_planta` ASC) VISIBLE,
  INDEX `fk_empleado_area_idx` (`id_area` ASC) VISIBLE,
  CONSTRAINT `fk_empleado_empresa`
    FOREIGN KEY (`id_empresa`)
    REFERENCES `sgp`.`cat_empresa` (`id_empresa`),
  CONSTRAINT `fk_empleado_perfil`
    FOREIGN KEY (`id_perfil`)
    REFERENCES `sgp`.`cat_perfil` (`id_perfil`),
  CONSTRAINT `fk_empleado_planta`
    FOREIGN KEY (`id_planta`)
    REFERENCES `sgp`.`cat_planta` (`id_planta`),
  CONSTRAINT `fk_empleado_puesto`
    FOREIGN KEY (`id_puesto`)
    REFERENCES `sgp`.`cat_puesto` (`id_puesto`),
  CONSTRAINT `fk_empleado_area`
    FOREIGN KEY (`id_area`)
    REFERENCES `sgp`.`cat_area` (`id_area`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;

INSERT INTO `sgp`.`det_empleado` (`num_empleado`, `nombre`, `primer_ap`, `segundo_ap`, `fecha_nacimiento`, `fecha_registro`, `fecha_modificacion`, `id_perfil`, `id_puesto`, `curp`, `rfc`, `correo`, `fecha_ingreso`, `nss`, `id_empresa`, `activo`, `id_planta`, `id_area`, `fotografia`, `sueldo_diario`) VALUES ('0001','Administrador','Administrador','Administrador','1990-01-02 00:00:00','2023-02-26 00:00:00',NULL,1,3,'XEXX010101HNEXXXA4','XAXX010101000',NULL,'2022-01-01 00:00:00',NULL,1,1,1,1,NULL,500.00);
INSERT INTO `sgp`.`det_empleado` (`num_empleado`, `nombre`, `primer_ap`, `segundo_ap`, `fecha_nacimiento`, `fecha_registro`, `fecha_modificacion`, `id_perfil`, `id_puesto`, `curp`, `rfc`, `correo`, `fecha_ingreso`, `nss`, `id_empresa`, `activo`, `id_planta`, `id_area`, `fotografia`, `sueldo_diario`) VALUES ('0002','VICTOR HUGO ','LIRA','MONTIEL','1990-01-02 00:00:00','2023-02-26 00:00:00',NULL,4,3,'XEXX010101HNEXXXA4','XAXX010101000',NULL,'2022-01-01 00:00:00',NULL,1,1,1,1,NULL,240.00);
INSERT INTO `sgp`.`det_empleado` (`num_empleado`, `nombre`, `primer_ap`, `segundo_ap`, `fecha_nacimiento`, `fecha_registro`, `fecha_modificacion`, `id_perfil`, `id_puesto`, `curp`, `rfc`, `correo`, `fecha_ingreso`, `nss`, `id_empresa`, `activo`, `id_planta`, `id_area`, `fotografia`, `sueldo_diario`) VALUES ('0003','LIZETTE','DE LA SANCHA ','GUERRERO','1990-01-03 00:00:00','2023-02-26 00:00:00',NULL,4,3,'XEXX010101HNEXXXA4','XAXX010101000',NULL,'2022-01-01 00:00:00',NULL,1,1,1,1,NULL,419.99);
INSERT INTO `sgp`.`det_empleado` (`num_empleado`, `nombre`, `primer_ap`, `segundo_ap`, `fecha_nacimiento`, `fecha_registro`, `fecha_modificacion`, `id_perfil`, `id_puesto`, `curp`, `rfc`, `correo`, `fecha_ingreso`, `nss`, `id_empresa`, `activo`, `id_planta`, `id_area`, `fotografia`, `sueldo_diario`) VALUES ('0004','NESTOR','ORDAZ','MERIDA','1990-01-04 00:00:00','2023-02-26 00:00:00',NULL,4,3,'XEXX010101HNEXXXA4','XAXX010101000',NULL,'2022-01-01 00:00:00',NULL,1,1,1,1,NULL,321.00);
INSERT INTO `sgp`.`det_empleado` (`num_empleado`, `nombre`, `primer_ap`, `segundo_ap`, `fecha_nacimiento`, `fecha_registro`, `fecha_modificacion`, `id_perfil`, `id_puesto`, `curp`, `rfc`, `correo`, `fecha_ingreso`, `nss`, `id_empresa`, `activo`, `id_planta`, `id_area`, `fotografia`, `sueldo_diario`) VALUES ('0005','LAURA ISABEL ','LEYVA','GUTIERREZ','1990-01-05 00:00:00','2023-02-26 00:00:00',NULL,4,3,'XEXX010101HNEXXXA4','XAXX010101000',NULL,'2022-01-01 00:00:00',NULL,1,1,1,1,NULL,217.00);
INSERT INTO `sgp`.`det_empleado` (`num_empleado`, `nombre`, `primer_ap`, `segundo_ap`, `fecha_nacimiento`, `fecha_registro`, `fecha_modificacion`, `id_perfil`, `id_puesto`, `curp`, `rfc`, `correo`, `fecha_ingreso`, `nss`, `id_empresa`, `activo`, `id_planta`, `id_area`, `fotografia`, `sueldo_diario`) VALUES ('0006','NERI','PALACIOS','MARTINEZ','1990-01-06 00:00:00','2023-02-26 00:00:00',NULL,4,3,'XEXX010101HNEXXXA4','XAXX010101000',NULL,'2022-01-01 00:00:00',NULL,1,1,1,1,NULL,225.00);
INSERT INTO `sgp`.`det_empleado` (`num_empleado`, `nombre`, `primer_ap`, `segundo_ap`, `fecha_nacimiento`, `fecha_registro`, `fecha_modificacion`, `id_perfil`, `id_puesto`, `curp`, `rfc`, `correo`, `fecha_ingreso`, `nss`, `id_empresa`, `activo`, `id_planta`, `id_area`, `fotografia`, `sueldo_diario`) VALUES ('0007','JUAN FRANCISCO ','CRUZ','LOPEZ','1990-01-07 00:00:00','2023-02-26 00:00:00',NULL,4,3,'XEXX010101HNEXXXA4','XAXX010101000',NULL,'2022-01-01 00:00:00',NULL,1,1,1,1,NULL,375.00);
INSERT INTO `sgp`.`det_empleado` (`num_empleado`, `nombre`, `primer_ap`, `segundo_ap`, `fecha_nacimiento`, `fecha_registro`, `fecha_modificacion`, `id_perfil`, `id_puesto`, `curp`, `rfc`, `correo`, `fecha_ingreso`, `nss`, `id_empresa`, `activo`, `id_planta`, `id_area`, `fotografia`, `sueldo_diario`) VALUES ('0008','JOSE ALBERTO ','MARIN','GUTIERREZ','1990-01-08 00:00:00','2023-02-26 00:00:00',NULL,4,3,'XEXX010101HNEXXXA4','XAXX010101000',NULL,'2022-01-01 00:00:00',NULL,1,1,1,1,NULL,240.00);
INSERT INTO `sgp`.`det_empleado` (`num_empleado`, `nombre`, `primer_ap`, `segundo_ap`, `fecha_nacimiento`, `fecha_registro`, `fecha_modificacion`, `id_perfil`, `id_puesto`, `curp`, `rfc`, `correo`, `fecha_ingreso`, `nss`, `id_empresa`, `activo`, `id_planta`, `id_area`, `fotografia`, `sueldo_diario`) VALUES ('0009','EDWIN','SANCHEZ','JUAREZ','1990-01-09 00:00:00','2023-02-26 00:00:00',NULL,4,3,'XEXX010101HNEXXXA4','XAXX010101000',NULL,'2022-01-01 00:00:00',NULL,1,1,1,1,NULL,321.00);
INSERT INTO `sgp`.`det_empleado` (`num_empleado`, `nombre`, `primer_ap`, `segundo_ap`, `fecha_nacimiento`, `fecha_registro`, `fecha_modificacion`, `id_perfil`, `id_puesto`, `curp`, `rfc`, `correo`, `fecha_ingreso`, `nss`, `id_empresa`, `activo`, `id_planta`, `id_area`, `fotografia`, `sueldo_diario`) VALUES ('0010','NORI','LOPEZ','VELASCO','1990-01-10 00:00:00','2023-02-26 00:00:00',NULL,4,3,'XEXX010101HNEXXXA4','XAXX010101000',NULL,'2022-01-01 00:00:00',NULL,1,1,1,1,NULL,375.00);
INSERT INTO `sgp`.`det_empleado` (`num_empleado`, `nombre`, `primer_ap`, `segundo_ap`, `fecha_nacimiento`, `fecha_registro`, `fecha_modificacion`, `id_perfil`, `id_puesto`, `curp`, `rfc`, `correo`, `fecha_ingreso`, `nss`, `id_empresa`, `activo`, `id_planta`, `id_area`, `fotografia`, `sueldo_diario`) VALUES ('0011','JOSE','MARIN','SALAS','1990-01-11 00:00:00','2023-02-26 00:00:00',NULL,4,3,'XEXX010101HNEXXXA4','XAXX010101000',NULL,'2022-01-01 00:00:00',NULL,1,1,1,1,NULL,221.00);
INSERT INTO `sgp`.`det_empleado` (`num_empleado`, `nombre`, `primer_ap`, `segundo_ap`, `fecha_nacimiento`, `fecha_registro`, `fecha_modificacion`, `id_perfil`, `id_puesto`, `curp`, `rfc`, `correo`, `fecha_ingreso`, `nss`, `id_empresa`, `activo`, `id_planta`, `id_area`, `fotografia`, `sueldo_diario`) VALUES ('0012','VICTOR MANUEL ','MALDONADO','RUANO','1990-01-12 00:00:00','2023-02-26 00:00:00',NULL,4,3,'XEXX010101HNEXXXA4','XAXX010101000',NULL,'2022-01-01 00:00:00',NULL,1,1,1,1,NULL,240.00);
INSERT INTO `sgp`.`det_empleado` (`num_empleado`, `nombre`, `primer_ap`, `segundo_ap`, `fecha_nacimiento`, `fecha_registro`, `fecha_modificacion`, `id_perfil`, `id_puesto`, `curp`, `rfc`, `correo`, `fecha_ingreso`, `nss`, `id_empresa`, `activo`, `id_planta`, `id_area`, `fotografia`, `sueldo_diario`) VALUES ('0013','VELEZAN','LOPEZ','HERNANDEZ','1990-01-13 00:00:00','2023-02-26 00:00:00',NULL,4,3,'XEXX010101HNEXXXA4','XAXX010101000',NULL,'2022-01-01 00:00:00',NULL,1,1,1,1,NULL,236.00);
INSERT INTO `sgp`.`det_empleado` (`num_empleado`, `nombre`, `primer_ap`, `segundo_ap`, `fecha_nacimiento`, `fecha_registro`, `fecha_modificacion`, `id_perfil`, `id_puesto`, `curp`, `rfc`, `correo`, `fecha_ingreso`, `nss`, `id_empresa`, `activo`, `id_planta`, `id_area`, `fotografia`, `sueldo_diario`) VALUES ('0014','LUIS ALFREDO ','JIMENEZ','MACARIO','1990-01-14 00:00:00','2023-02-26 00:00:00',NULL,4,3,'XEXX010101HNEXXXA4','XAXX010101000',NULL,'2022-01-01 00:00:00',NULL,1,1,1,1,NULL,225.00);
INSERT INTO `sgp`.`det_empleado` (`num_empleado`, `nombre`, `primer_ap`, `segundo_ap`, `fecha_nacimiento`, `fecha_registro`, `fecha_modificacion`, `id_perfil`, `id_puesto`, `curp`, `rfc`, `correo`, `fecha_ingreso`, `nss`, `id_empresa`, `activo`, `id_planta`, `id_area`, `fotografia`, `sueldo_diario`) VALUES ('0015','ERENDIRA DANIELA ','VELARDE','CASTILLO','1990-01-15 00:00:00','2023-02-26 00:00:00',NULL,4,3,'XEXX010101HNEXXXA4','XAXX010101000',NULL,'2022-01-01 00:00:00',NULL,1,1,1,1,NULL,217.00);
INSERT INTO `sgp`.`det_empleado` (`num_empleado`, `nombre`, `primer_ap`, `segundo_ap`, `fecha_nacimiento`, `fecha_registro`, `fecha_modificacion`, `id_perfil`, `id_puesto`, `curp`, `rfc`, `correo`, `fecha_ingreso`, `nss`, `id_empresa`, `activo`, `id_planta`, `id_area`, `fotografia`, `sueldo_diario`) VALUES ('0016','LUIS MARIO ','CASTILLO','ORTIZ','1990-01-16 00:00:00','2023-02-26 00:00:00',NULL,4,3,'XEXX010101HNEXXXA4','XAXX010101000',NULL,'2022-01-01 00:00:00',NULL,1,1,1,1,NULL,225.00);
INSERT INTO `sgp`.`det_empleado` (`num_empleado`, `nombre`, `primer_ap`, `segundo_ap`, `fecha_nacimiento`, `fecha_registro`, `fecha_modificacion`, `id_perfil`, `id_puesto`, `curp`, `rfc`, `correo`, `fecha_ingreso`, `nss`, `id_empresa`, `activo`, `id_planta`, `id_area`, `fotografia`, `sueldo_diario`) VALUES ('0017','JONATHAN PABLO ','CORTES','ESQUIVEL','1990-01-17 00:00:00','2023-02-26 00:00:00',NULL,4,3,'XEXX010101HNEXXXA4','XAXX010101000',NULL,'2022-01-01 00:00:00',NULL,1,1,1,1,NULL,240.00);

-- -----------------------------------------------------
-- Table `sgp`.`bitacora_cat_perfil`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `sgp`.`bitacora_cat_perfil` (
  `id_bitacora` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `fecha_captura` DATETIME NULL DEFAULT NULL,
  `modificacion` TINYINT NULL DEFAULT NULL,
  `id_perfil` INT UNSIGNED NULL DEFAULT NULL,
  `id_empleado` INT UNSIGNED NULL DEFAULT NULL,
  PRIMARY KEY (`id_bitacora`),
  INDEX `fk_bitacora_perfil_idx` (`id_perfil` ASC) VISIBLE,
  INDEX `fk_bitacora_empleado` (`id_empleado` ASC) VISIBLE,
  CONSTRAINT `fk_bitacora_empleado`
    FOREIGN KEY (`id_empleado`)
    REFERENCES `sgp`.`det_empleado` (`id_empleado`),
  CONSTRAINT `fk_bitacora_perfil`
    FOREIGN KEY (`id_perfil`)
    REFERENCES `sgp`.`cat_perfil` (`id_perfil`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;

-- -----------------------------------------------------
-- Table `sgp`.`cat_articulo`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `sgp`.`cat_articulo` (
  `id_articulo` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `descripcion` VARCHAR(45) NULL DEFAULT NULL,
  `cantidadMax` INT NULL,
  `unidad` VARCHAR(45) NULL,
  `detalle` varchar(150) NULL,
  `activo` TINYINT NOT NULL DEFAULT 1,
  PRIMARY KEY (`id_articulo`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;

INSERT INTO `sgp`.`cat_articulo` (`descripcion`, `cantidadMax`, `unidad`) VALUES ('Carpeta Lefort', 1, 'PZ');
INSERT INTO `sgp`.`cat_articulo` (`descripcion`, `cantidadMax`, `unidad`) VALUES ('Cinta Canela', 1, 'ROLLO');
INSERT INTO `sgp`.`cat_articulo` (`descripcion`, `cantidadMax`, `unidad`) VALUES ('Clips', 1, 'CAJA');
INSERT INTO `sgp`.`cat_articulo` (`descripcion`, `cantidadMax`, `unidad`) VALUES ('Cloro', 5, 'LT');
INSERT INTO `sgp`.`cat_articulo` (`descripcion`, `cantidadMax`, `unidad`) VALUES ('Cojin para Entintar Sumadora', 1, 'PZ');
INSERT INTO `sgp`.`cat_articulo` (`descripcion`, `cantidadMax`, `unidad`) VALUES ('Cubeta', 1, 'PZ');
INSERT INTO `sgp`.`cat_articulo` (`descripcion`, `cantidadMax`, `unidad`) VALUES ('Cutter', 1, 'PZ');
INSERT INTO `sgp`.`cat_articulo` (`descripcion`, `cantidadMax`, `unidad`) VALUES ('Engrapadora', 1, 'PZ');
INSERT INTO `sgp`.`cat_articulo` (`descripcion`, `cantidadMax`, `unidad`) VALUES ('Escoba', 1, 'PZ');
INSERT INTO `sgp`.`cat_articulo` (`descripcion`, `cantidadMax`, `unidad`) VALUES ('Grapas', 1, 'CAJA');
INSERT INTO `sgp`.`cat_articulo` (`descripcion`, `cantidadMax`, `unidad`) VALUES ('Hojas o Navajas para Cutter', 10, 'HOJAS');
INSERT INTO `sgp`.`cat_articulo` (`descripcion`, `cantidadMax`, `unidad`) VALUES ('Jabón para Manos', 5, 'LT');
INSERT INTO `sgp`.`cat_articulo` (`descripcion`, `cantidadMax`, `unidad`) VALUES ('Jabón Roma', 1, 'BOLSA');
INSERT INTO `sgp`.`cat_articulo` (`descripcion`, `cantidadMax`, `unidad`) VALUES ('Jalador', 1, 'PZ');
INSERT INTO `sgp`.`cat_articulo` (`descripcion`, `cantidadMax`, `unidad`) VALUES ('Lápiz', 5, 'PZ');
INSERT INTO `sgp`.`cat_articulo` (`descripcion`, `cantidadMax`, `unidad`) VALUES ('Marcador Permanente', 3, 'PZ');
INSERT INTO `sgp`.`cat_articulo` (`descripcion`, `cantidadMax`, `unidad`) VALUES ('Marcatextos', 3, 'PZ');
INSERT INTO `sgp`.`cat_articulo` (`descripcion`, `cantidadMax`, `unidad`) VALUES ('Papel Bond', 1, 'PAQ');
INSERT INTO `sgp`.`cat_articulo` (`descripcion`, `cantidadMax`, `unidad`) VALUES ('Papel de Baño', 1, 'ROLLO');
INSERT INTO `sgp`.`cat_articulo` (`descripcion`, `cantidadMax`, `unidad`) VALUES ('Perforadora de Hojas', 1, 'PZ');
INSERT INTO `sgp`.`cat_articulo` (`descripcion`, `cantidadMax`, `unidad`) VALUES ('Playo', 1, 'ROLLO');
INSERT INTO `sgp`.`cat_articulo` (`descripcion`, `cantidadMax`, `unidad`) VALUES ('Pluma', 5, 'PZ');
INSERT INTO `sgp`.`cat_articulo` (`descripcion`, `cantidadMax`, `unidad`) VALUES ('Recogedor', 1, 'PZ');
INSERT INTO `sgp`.`cat_articulo` (`descripcion`, `cantidadMax`, `unidad`) VALUES ('Rollo Termico de 8 Cm', 1, 'PZ');
INSERT INTO `sgp`.`cat_articulo` (`descripcion`, `cantidadMax`, `unidad`) VALUES ('Rollos para Sumadora', 3, 'PZ');
INSERT INTO `sgp`.`cat_articulo` (`descripcion`, `cantidadMax`, `unidad`) VALUES ('Tabla Pisa Papel', 1, 'PZ');
INSERT INTO `sgp`.`cat_articulo` (`descripcion`, `cantidadMax`, `unidad`) VALUES ('Toallas Interbobladas', 1, 'PAQ');
INSERT INTO `sgp`.`cat_articulo` (`descripcion`, `cantidadMax`, `unidad`) VALUES ('Trapo', 1, 'PZ');

-- -----------------------------------------------------
-- Table `sgp`.`cat_prenda`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `sgp`.`cat_prenda` (
  `id_prenda` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `descripcion` VARCHAR(45) NULL DEFAULT NULL,
  `precio` DECIMAL(5,2) NULL,
  `cantidadMax` INT NULL,
  `detalle` varchar(150) NULL,
  `activo` TINYINT NOT NULL DEFAULT 1,
  PRIMARY KEY (`id_prenda`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;

INSERT INTO `sgp`.`cat_prenda` (`descripcion`, `precio`, `cantidadMax`, `detalle`) VALUES ('Botas', '600.00', '1', NULL);
INSERT INTO `sgp`.`cat_prenda` (`descripcion`, `precio`, `cantidadMax`, `detalle`) VALUES ('Pantalón', '200.00', '2', NULL);
INSERT INTO `sgp`.`cat_prenda` (`descripcion`, `precio`, `cantidadMax`, `detalle`) VALUES ('Polo', '70.00', '3', NULL);
INSERT INTO `sgp`.`cat_prenda` (`descripcion`, `precio`, `cantidadMax`, `detalle`) VALUES ('Camisa', '145.00', '3', NULL);
INSERT INTO `sgp`.`cat_prenda` (`descripcion`, `precio`, `cantidadMax`, `detalle`) VALUES ('Chaleco', '390.00', '2', NULL);
INSERT INTO `sgp`.`cat_prenda` (`descripcion`, `precio`, `cantidadMax`, `detalle`) VALUES ('Sudadera', '155.00', '2', NULL);
INSERT INTO `sgp`.`cat_prenda` (`descripcion`, `precio`, `cantidadMax`, `detalle`) VALUES ('Escafandra', '56.00', '2', NULL);
INSERT INTO `sgp`.`cat_prenda` (`descripcion`, `precio`, `cantidadMax`, `detalle`) VALUES ('Chaleco Térmico', '250.00', '1', NULL);
INSERT INTO `sgp`.`cat_prenda` (`descripcion`, `precio`, `cantidadMax`, `detalle`) VALUES ('Chamarra de Seguridad', '280.00','1', NULL);
INSERT INTO `sgp`.`cat_prenda` (`descripcion`, `precio`, `cantidadMax`, `detalle`) VALUES ('Lámpara de Cabeza', '130.00','1', NULL);
INSERT INTO `sgp`.`cat_prenda` (`descripcion`, `precio`, `cantidadMax`, `detalle`) VALUES ('Guante Blanco', '0.00', '4', NULL);
INSERT INTO `sgp`.`cat_prenda` (`descripcion`, `precio`, `cantidadMax`, `detalle`) VALUES ('Guante Negro', '0.00', '2', NULL);

-- -----------------------------------------------------
-- Table `sgp`.`det_inventario`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `sgp`.`det_inventario` (
  `id_inventario` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `id_prenda` INT UNSIGNED NULL DEFAULT NULL,
  `id_articulo` INT UNSIGNED NULL DEFAULT NULL,
  `cantidad` INT UNSIGNED NOT NULL DEFAULT 0,
  `activo` TINYINT NOT NULL DEFAULT 1,
  `visible` TINYINT NOT NULL DEFAULT 1,
  `fecha_captura` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id_inventario`),
  INDEX `fk_inventario_prenda_idx` (`id_prenda` ASC) VISIBLE,
  INDEX `fk_inventario_articulo_idx` (`id_articulo` ASC) VISIBLE,
  CONSTRAINT `fk_inventario_articulo`
    FOREIGN KEY (`id_articulo`)
    REFERENCES `sgp`.`cat_articulo` (`id_articulo`),
  CONSTRAINT `fk_inventario_prenda`
    FOREIGN KEY (`id_prenda`)
    REFERENCES `sgp`.`cat_prenda` (`id_prenda`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;

-- -----------------------------------------------------
-- Table `sgp`.`cat_tipo_bitacora`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `sgp`.`cat_tipo_bitacora` (
  `id_tipo` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `descripcion` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id_tipo`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;

-- -----------------------------------------------------
-- Table `sgp`.`bitacora_inventario`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `sgp`.`bitacora_inventario` (
  `id_bitacora` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `id_inventario` INT UNSIGNED NOT NULL,
  `id_empleado` INT UNSIGNED NOT NULL,
  `fecha_captura` DATETIME NOT NULL,
  `id_tipo` INT UNSIGNED NOT NULL,
  PRIMARY KEY (`id_bitacora`),
  INDEX `fk_bitacora_inventario_inventario_idx` (`id_inventario` ASC) VISIBLE,
  INDEX `fk_bitacora_inventario_empleado_idx` (`id_empleado` ASC) VISIBLE,
  INDEX `fk_bitacora_inventario_tipo_idx` (`id_tipo` ASC) VISIBLE,
  CONSTRAINT `fk_bitacora_inventario_empleado`
    FOREIGN KEY (`id_empleado`)
    REFERENCES `sgp`.`det_empleado` (`id_empleado`),
  CONSTRAINT `fk_bitacora_inventario_inventario`
    FOREIGN KEY (`id_inventario`)
    REFERENCES `sgp`.`det_inventario` (`id_inventario`),
  CONSTRAINT `fk_bitacora_inventario_tipo`
    FOREIGN KEY (`id_tipo`)
    REFERENCES `sgp`.`cat_tipo_bitacora` (`id_tipo`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;

-- -----------------------------------------------------
-- Table `sgp`.`cat_estatus_incidencia`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `sgp`.`cat_estatus_incidencia` (
  `id_estatus` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `descripcion` VARCHAR(45) CHARACTER SET 'utf8mb4' COLLATE 'utf8mb4_0900_ai_ci' NOT NULL,
  `activo` TINYINT NOT NULL DEFAULT 1,
  PRIMARY KEY (`id_estatus`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;

INSERT INTO `sgp`.`cat_estatus_incidencia` (`descripcion`, `activo`) VALUES ('Enviada', 1), ('Aprobada', 1), ('Rechazada', 1), ('Cancelada', 1);

-- -----------------------------------------------------
-- Table `sgp`.`cat_estatus_registro`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `sgp`.`cat_estatus_registro` (
  `id_estatus` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `descripcion` VARCHAR(45) CHARACTER SET 'utf8mb4' COLLATE 'utf8mb4_0900_ai_ci' NOT NULL,
  `activo` TINYINT NOT NULL DEFAULT 1,
  PRIMARY KEY (`id_estatus`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;

INSERT INTO `sgp`.`cat_estatus_registro` (`descripcion`) VALUES ('A tiempo'), ('Retardo'), ('Falta'), ('Justificado');

-- -----------------------------------------------------
-- Table `sgp`.`cat_talla`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `sgp`.`cat_talla` (
  `id_talla` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `descripcion` VARCHAR(45) NOT NULL,
  `activo` TINYINT NOT NULL DEFAULT 1,
  PRIMARY KEY (`id_talla`))
ENGINE=InnoDB
DEFAULT CHARSET=utf8mb4
COLLATE=utf8mb4_0900_ai_ci;

INSERT INTO `sgp`.`cat_talla` (`descripcion`) VALUES ('CH');
INSERT INTO `sgp`.`cat_talla` (`descripcion`) VALUES ('MD');
INSERT INTO `sgp`.`cat_talla` (`descripcion`) VALUES ('G/L');
INSERT INTO `sgp`.`cat_talla` (`descripcion`) VALUES ('XG');

-- -----------------------------------------------------
-- Table `sgp`.`cat_tipo_incidencia`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `sgp`.`cat_tipo_incidencia` (
  `id_tipo` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `descripcion` VARCHAR(45) NULL DEFAULT NULL,
  PRIMARY KEY (`id_tipo`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;

INSERT INTO `sgp`.`cat_tipo_incidencia` (`descripcion`) VALUES ('Permiso'), ('Vacaciones'), ('Prenda'), ('Artículo');

-- -----------------------------------------------------
-- Table `sgp`.`cat_tipo_solicitud`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `sgp`.`cat_tipo_solicitud` (
  `id_tipo_solicitud` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `descripcion` VARCHAR(45) NOT NULL,
  `activo` TINYINT NULL DEFAULT 1,
  PRIMARY KEY (`id_tipo_solicitud`))
ENGINE=InnoDB
DEFAULT CHARSET=utf8mb4
COLLATE=utf8mb4_0900_ai_ci;

INSERT INTO `sgp`.`cat_tipo_solicitud` (`descripcion`, `activo`) VALUES ('Permiso', '1');
INSERT INTO `sgp`.`cat_tipo_solicitud` (`descripcion`) VALUES ('Vacaciones');
INSERT INTO `sgp`.`cat_tipo_solicitud` (descripcion) values('Incapacidad Corta');
INSERT INTO `sgp`.`cat_tipo_solicitud` (descripcion) values('Incapacidad Larga');

-- -----------------------------------------------------
-- Table `sgp`.`det_biometrico`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `sgp`.`det_biometrico` (
  `id_biometrico` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `id_empleado` INT UNSIGNED NOT NULL,
  `fecha_captura` DATETIME NOT NULL,
  `activo` TINYINT NOT NULL DEFAULT 1,
  `huella` TEXT NOT NULL,
  `huella2` TEXT NOT NULL,
  PRIMARY KEY (`id_biometrico`),
  INDEX `fk_biometrico_empleado_idx` (`id_empleado` ASC) VISIBLE,
  CONSTRAINT `fk_biometrico_empleado`
    FOREIGN KEY (`id_empleado`)
    REFERENCES `sgp`.`det_empleado` (`id_empleado`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;

-- -----------------------------------------------------
-- Table `sgp`.`det_solicitud_articulo`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `sgp`.`det_solicitud_articulo` (
  `id_solicitud` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `id_articulo` INT UNSIGNED NOT NULL,
  `cantidad` INT NOT NULL DEFAULT '1',
  `aprobada` TINYINT NULL DEFAULT NULL,
  `fecha_cap` DATETIME NOT NULL,
  `fecha_mod` DATETIME NULL DEFAULT NULL,
  `id_empleado_sol` INT UNSIGNED NOT NULL,
  `id_empleado_rev` INT UNSIGNED NULL DEFAULT NULL,
  `descripcion_rechazo` varchar(150) NULL,
  PRIMARY KEY (`id_solicitud`),
  INDEX `fk_sol_material_idx` (`id_articulo` ASC) VISIBLE,
  INDEX `fk_emp_sol_articulo_idx` (`id_empleado_sol` ASC) VISIBLE,
  INDEX `fk_emp_revl_articulo_idx` (`id_empleado_rev` ASC) VISIBLE,
  CONSTRAINT `fk_emp_rev_articulo`
    FOREIGN KEY (`id_empleado_rev`)
    REFERENCES `sgp`.`det_empleado` (`id_empleado`),
  CONSTRAINT `fk_emp_sol_articulo`
    FOREIGN KEY (`id_empleado_sol`)
    REFERENCES `sgp`.`det_empleado` (`id_empleado`),
  CONSTRAINT `fk_sol_articulo`
    FOREIGN KEY (`id_articulo`)
    REFERENCES `sgp`.`cat_articulo` (`id_articulo`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;

-- -----------------------------------------------------
-- Table `sgp`.`det_solicitud_permiso`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `sgp`.`det_solicitud_permiso` (
  `id_solicitud` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `fecha_cap` DATETIME NOT NULL,
  `fecha_mod` DATETIME NULL DEFAULT NULL,
  `fecha_inicio` DATETIME NOT NULL,
  `fecha_fin` DATETIME NOT NULL,
  `aprobada` TINYINT NULL DEFAULT NULL,
  `id_empleado_sol` INT UNSIGNED NOT NULL,
  `id_tipo_solicitud` INT UNSIGNED NOT NULL,
  `id_empleado_rev` INT UNSIGNED NULL DEFAULT NULL,
  `descripcion_rechazo` varchar(150) NULL,
  PRIMARY KEY (`id_solicitud`),
  INDEX `fk_emp_rev_vacaciones_idx` (`id_empleado_sol` ASC) VISIBLE,
  INDEX `fk_emp_sol_vacaciones_idx` (`id_empleado_rev` ASC) VISIBLE,
  INDEX `fk_sol_tipo_sol_idx` (`id_tipo_solicitud` ASC) VISIBLE,
  CONSTRAINT `fk_emp_rev_vacaciones`
    FOREIGN KEY (`id_empleado_sol`)
    REFERENCES `sgp`.`det_empleado` (`id_empleado`),
  CONSTRAINT `fk_emp_sol_vacaciones`
    FOREIGN KEY (`id_empleado_rev`)
    REFERENCES `sgp`.`det_empleado` (`id_empleado`),
  CONSTRAINT `fk_sol_tipo_sol`
  FOREIGN KEY (`id_tipo_solicitud`)
  REFERENCES `sgp`.`cat_tipo_solicitud` (`id_tipo_solicitud`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;

-- -----------------------------------------------------
-- Table `sgp`.`det_solicitud_prenda`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `sgp`.`det_solicitud_prenda` (
  `id_solicitud` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `id_prenda` INT UNSIGNED NOT NULL,
  `cantidad` INT NOT NULL DEFAULT '1',
  `aprobada` TINYINT NULL DEFAULT NULL,
  `fecha_cap` DATETIME NOT NULL,
  `fecha_mod` DATETIME NULL DEFAULT NULL,
  `id_empleado_sol` INT UNSIGNED NOT NULL,
  `id_talla` INT UNSIGNED NOT NULL,
  `id_empleado_rev` INT UNSIGNED NULL DEFAULT NULL,
  `descripcion_rechazo` varchar(150) NULL,
  PRIMARY KEY (`id_solicitud`),
  INDEX `fk_sol_prenda_idx` (`id_prenda` ASC) VISIBLE,
  INDEX `fk_sol_emp_idx` (`id_empleado_sol` ASC) VISIBLE,
  INDEX `fk_sol_talla_idx` (`id_talla` ASC) VISIBLE,
  INDEX `fk_emp_revisor_idx` (`id_empleado_rev` ASC) VISIBLE,
  CONSTRAINT `fk_emp_rev_prenda`
    FOREIGN KEY (`id_empleado_rev`)
    REFERENCES `sgp`.`det_empleado` (`id_empleado`),
  CONSTRAINT `fk_emp_sol_prenda`
    FOREIGN KEY (`id_empleado_sol`)
    REFERENCES `sgp`.`det_empleado` (`id_empleado`),
  CONSTRAINT `fk_sol_talla`
    FOREIGN KEY (`id_talla`)
    REFERENCES `sgp`.`cat_talla` (`id_talla`),
  CONSTRAINT `fk_sol_prenda`
    FOREIGN KEY (`id_prenda`)
    REFERENCES `sgp`.`cat_prenda` (`id_prenda`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;

-- -----------------------------------------------------
-- Table `sgp`.`det_incidencia`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `sgp`.`det_incidencia` (
  `id_incidencia` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `id_tipo` INT UNSIGNED NULL DEFAULT NULL,
  `id_empleado` INT UNSIGNED NULL DEFAULT NULL,
  `id_estatus` INT UNSIGNED NULL DEFAULT NULL,
  `visible` TINYINT NULL DEFAULT NULL,
  `id_sol_articulo` INT UNSIGNED NULL DEFAULT NULL,
  `id_sol_prenda` INT UNSIGNED NULL DEFAULT NULL,
  `id_sol_permiso` INT UNSIGNED NULL DEFAULT NULL,
  `fecha_cap` DATETIME NOT NULL,
  `fecha_mod` DATETIME NULL DEFAULT NULL,
  `id_empleado_rev` INT UNSIGNED NULL,
  PRIMARY KEY (`id_incidencia`),
  INDEX `fk_incidencia_sol_prenda_idx` (`id_sol_prenda` ASC) VISIBLE,
  INDEX `fk_incidencia_sol_articulo_idx` (`id_sol_articulo` ASC) VISIBLE,
  INDEX `fk_incidencia_estatus_idx` (`id_estatus` ASC) VISIBLE,
  INDEX `fk_incidencia_tipo_idx` (`id_tipo` ASC) VISIBLE,
  INDEX `fk_incidencia_sol_permiso_idx` (`id_sol_permiso` ASC) VISIBLE,
  INDEX `fk_incidencia_empleado_idx` (`id_empleado` ASC) VISIBLE,
  INDEX `fk_incidencia_empleado_rev_idx` (`id_empleado_rev` ASC) VISIBLE,
  CONSTRAINT `fk_incidencia_estatus`
    FOREIGN KEY (`id_estatus`)
    REFERENCES `sgp`.`cat_estatus_incidencia` (`id_estatus`),
  CONSTRAINT `fk_incidencia_sol_articulo`
    FOREIGN KEY (`id_sol_articulo`)
    REFERENCES `sgp`.`det_solicitud_articulo` (`id_solicitud`),
  CONSTRAINT `fk_incidencia_sol_permiso`
    FOREIGN KEY (`id_sol_permiso`)
    REFERENCES `sgp`.`det_solicitud_permiso` (`id_solicitud`),
  CONSTRAINT `fk_incidencia_sol_prenda`
    FOREIGN KEY (`id_sol_prenda`)
    REFERENCES `sgp`.`det_solicitud_prenda` (`id_solicitud`),
  CONSTRAINT `fk_incidencia_tipo`
    FOREIGN KEY (`id_tipo`)
    REFERENCES `sgp`.`cat_tipo_incidencia` (`id_tipo`),
  CONSTRAINT `fk_incidencia_empleado` 
    FOREIGN KEY (`id_empleado`) 
    REFERENCES `sgp`.`det_empleado`(`id_empleado`),
  CONSTRAINT `fk_incidencia_empleado_rev `
    FOREIGN KEY (`id_empleado_rev`) 
    REFERENCES `sgp`.`det_empleado`(`id_empleado`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;

-- -----------------------------------------------------
-- Table `sgp`.`det_registro`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `sgp`.`det_registro` (
  `id_registro` INT NOT NULL AUTO_INCREMENT,
  `id_empleado` INT UNSIGNED NOT NULL,
  `fecha_entrada` DATETIME NULL DEFAULT NULL,
  `fecha_salida` DATETIME NULL DEFAULT NULL,
  `id_estatus` INT UNSIGNED NULL DEFAULT NULL,
  PRIMARY KEY (`id_registro`),
  INDEX `id_registro_empleado_idx` (`id_empleado` ASC) VISIBLE,
  INDEX `fk_registro_estatus_idx` (`id_estatus` ASC) VISIBLE,
  CONSTRAINT `fk_registro_empleado`
    FOREIGN KEY (`id_empleado`)
    REFERENCES `sgp`.`det_empleado` (`id_empleado`),
  CONSTRAINT `fk_registro_estatus`
    FOREIGN KEY (`id_estatus`)
    REFERENCES `sgp`.`cat_estatus_registro` (`id_estatus`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;

-- -----------------------------------------------------
-- Table `sgp`.`cat_percepciones`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `sgp`.`cat_percepciones` (
  `id_percepciones` INT NOT NULL AUTO_INCREMENT,
  `dias_aguinaldo` INT NOT NULL,
  `dias_vacaciones` INT NOT NULL,
  `prima_vacacional` FLOAT NOT NULL,
  `bono_puntualidad` FLOAT NOT NULL,
  `vale_despensa` FLOAT NOT NULL,
  `uma` FLOAT NOT NULL,
  `fecha_cap` datetime NULL,
  `activo` TINYINT NOT NULL DEFAULT 1,
  PRIMARY KEY (`id_percepciones`))
ENGINE=InnoDB
DEFAULT CHARSET=utf8mb4
COLLATE=utf8mb4_0900_ai_ci;

INSERT INTO `sgp`.`cat_percepciones` (`dias_aguinaldo`, `dias_vacaciones`, `prima_vacacional`, `bono_puntualidad`, `vale_despensa`, `uma`, `fecha_cap`) VALUES ('15', '12', '0.25', '0.10', '0.40', '103.74', '2023-02-01 00:00:00');
	
-- -----------------------------------------------------
-- Table `sgp`.`det_nomina`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `sgp`.`det_nomina`(
  `id_nomina` INT NOT NULL AUTO_INCREMENT,
  `id_empleado` INT UNSIGNED NOT NULL,
  `sueldo` FLOAT(10, 2) NOT NULL,
  `septimo_dia` FLOAT(10, 2) NOT NULL,
  `horas_extras` FLOAT(10, 2) NOT NULL,
  `destajos` FLOAT(10, 2) NOT NULL,
  `premios_eficiencia` FLOAT(10, 2) NOT NULL,
  `bono_puntualidad` FLOAT(10, 2) NOT NULL,
  `despensa` FLOAT(10, 2) NOT NULL,
  `otras_percepciones` FLOAT(10, 2) NOT NULL,
  `total_percepciones` FLOAT(10, 2) NOT NULL,
  `ret_inv_y_vida` FLOAT(10, 2) NOT NULL,
  `ret_cesantia` FLOAT(10, 2) NOT NULL,
  `ret_enf_y_mat_obrero` FLOAT(10, 2) NOT NULL,
  `prestamo_infonavit_fd` FLOAT(10, 2) NOT NULL,
  `prestamo_infonavit_cf` FLOAT(10, 2) NOT NULL,
  `subs_al_empleo_acreditado` FLOAT(10, 2) NOT NULL,
  `subs_al_empleo_mes` FLOAT(10, 2) NOT NULL,
  `isr_antes_de_subs_al_empleo` FLOAT(10, 2) NOT NULL,
  `isr_mes` FLOAT(10, 2) NOT NULL,
  `imss` FLOAT(10, 2) NOT NULL,
  `prestamo_fonacot` FLOAT(10, 2) NOT NULL,
  `ajuste_en_subsidio_para_el_empleo` FLOAT(10, 2) NOT NULL,
  `subs_entregado_que_no_correspondia` FLOAT(10, 2) NOT NULL,
  `ajuste_al_neto` FLOAT(10, 2) NOT NULL,
  `isr_de_ajuste_mensual` FLOAT(10, 2) NOT NULL,
  `isr_ajustado_por_subsidio` FLOAT(10, 2) NOT NULL,
  `ajuste_al_subsidio_causado` FLOAT(10, 2) NOT NULL,
  `pension_alimienticia` FLOAT(10, 2) NOT NULL,
  `otras_deducciones` FLOAT(10, 2) NOT NULL,
  `total_deducciones` FLOAT(10, 2) NOT NULL,
  `neto` FLOAT(10, 2) NOT NULL,
  `invalidez_y_vida` FLOAT(10, 2) NOT NULL,
  `cesantia_y_vejez` FLOAT(10, 2) NOT NULL,
  `enf_y_mat_patron` FLOAT(10, 2) NOT NULL,
  `fondo_retiro_sar_8` FLOAT(10, 2) NOT NULL,
  `impuesto_estatal` FLOAT(10, 2) NOT NULL,
  `riesgo_de_trabajo_9` FLOAT(10, 2) NOT NULL,
  `imss_empresa` FLOAT(10, 2) NOT NULL,
  `infonavit_empresa` FLOAT(10, 2) NOT NULL,
  `guarderia_imss_7` FLOAT(10, 2) NOT NULL,
  `otras_obligaciones` FLOAT(10, 2) NOT NULL,
  `total_obligaciones` FLOAT(10, 2) NOT NULL,
  `fecha_creacion` DATETIME NOT NULL,
  `id_empleado_creador` INT UNSIGNED NOT NULL,
  PRIMARY KEY (`id_nomina`),
  INDEX `fk_nomina_empleado_idx` (`id_empleado` ASC) INVISIBLE,
  CONSTRAINT `fk_nomina_empleado`
    FOREIGN KEY (`id_empleado`)
    REFERENCES `sgp`.`det_empleado` (`id_empleado`),
  CONSTRAINT `fk_nomina_empleado_creador`
    FOREIGN KEY (`id_empleado_creador`)
    REFERENCES `sgp`.`det_empleado` (`id_empleado`))
ENGINE=InnoDB
DEFAULT CHARSET=utf8mb4
COLLATE=utf8mb4_0900_ai_ci;

-- -----------------------------------------------------
-- Table `sgp`.`cat_tarifa_isr`
-- -----------------------------------------------------
CREATE TABLE `sgp`.`cat_tarifa_isr` (
  `id_isr` INT NOT NULL AUTO_INCREMENT,
  `limite_inferior` FLOAT(10,2) NOT NULL,
  `limite_superior` FLOAT(10,2) NOT NULL,
  `cuota_fija` FLOAT(10,2) NOT NULL,
  `porc_apl_exce_lim_inf` FLOAT(10,2) NOT NULL,
  `tipo` VARCHAR(45) NOT NULL,
  `fecha` DATE NOT NULL,
  PRIMARY KEY (`id_isr`))
ENGINE=InnoDB
DEFAULT CHARSET=utf8mb4
COLLATE=utf8mb4_0900_ai_ci;

INSERT INTO `sgp`.`cat_tarifa_isr` (`limite_inferior`, `limite_superior`, `cuota_fija`, `porc_apl_exce_lim_inf`, `tipo`, `fecha`) VALUES (0.01, 171.78, 0, 1.92, 's', '2023-01-02');
INSERT INTO `sgp`.`cat_tarifa_isr` (`limite_inferior`, `limite_superior`, `cuota_fija`, `porc_apl_exce_lim_inf`, `tipo`, `fecha`) VALUES (171.79, 1458.03, 3.29, 6.4, 's', '2023-01-02');
INSERT INTO `sgp`.`cat_tarifa_isr` (`limite_inferior`, `limite_superior`, `cuota_fija`, `porc_apl_exce_lim_inf`, `tipo`, `fecha`) VALUES (1458.04, 2562.35, 85.61, 10.88, 's', '2023-01-02');
INSERT INTO `sgp`.`cat_tarifa_isr` (`limite_inferior`, `limite_superior`, `cuota_fija`, `porc_apl_exce_lim_inf`, `tipo`, `fecha`) VALUES (2562.36, 2978.64, 205.8, 16, 's', '2023-01-02');
INSERT INTO `sgp`.`cat_tarifa_isr` (`limite_inferior`, `limite_superior`, `cuota_fija`, `porc_apl_exce_lim_inf`, `tipo`, `fecha`) VALUES (2978.65, 3566.22, 272.37, 17.92, 's', '2023-01-02');
INSERT INTO `sgp`.`cat_tarifa_isr` (`limite_inferior`, `limite_superior`, `cuota_fija`, `porc_apl_exce_lim_inf`, `tipo`, `fecha`) VALUES (3566.23, 7192.64, 377.65, 21.36, 's', '2023-01-02');
INSERT INTO `sgp`.`cat_tarifa_isr` (`limite_inferior`, `limite_superior`, `cuota_fija`, `porc_apl_exce_lim_inf`, `tipo`, `fecha`) VALUES (7192.65, 11336.57, 1152.27, 23.52, 's', '2023-01-02');
INSERT INTO `sgp`.`cat_tarifa_isr` (`limite_inferior`, `limite_superior`, `cuota_fija`, `porc_apl_exce_lim_inf`, `tipo`, `fecha`) VALUES (11336.58, 21643.3, 2126.95, 30, 's', '2023-01-02');
INSERT INTO `sgp`.`cat_tarifa_isr` (`limite_inferior`, `limite_superior`, `cuota_fija`, `porc_apl_exce_lim_inf`, `tipo`, `fecha`) VALUES (21643.31, 28857.78, 5218.92, 32, 's', '2023-01-02');
INSERT INTO `sgp`.`cat_tarifa_isr` (`limite_inferior`, `limite_superior`, `cuota_fija`, `porc_apl_exce_lim_inf`, `tipo`, `fecha`) VALUES (28857.79, 86573.34, 7527.59, 34, 's', '2023-01-02');
INSERT INTO `sgp`.`cat_tarifa_isr` (`limite_inferior`, `limite_superior`, `cuota_fija`, `porc_apl_exce_lim_inf`, `tipo`, `fecha`) VALUES (86573.35, 99999.99, 27150.83, 35, 's', '2023-01-02');
INSERT INTO `sgp`.`cat_tarifa_isr` (`limite_inferior`, `limite_superior`, `cuota_fija`, `porc_apl_exce_lim_inf`, `tipo`, `fecha`) VALUES (0.01, 746.04, 0, 1.92, 'm', '2023-01-02');
INSERT INTO `sgp`.`cat_tarifa_isr` (`limite_inferior`, `limite_superior`, `cuota_fija`, `porc_apl_exce_lim_inf`, `tipo`, `fecha`) VALUES (746.05, 6332.05, 14.32, 6.4, 'm', '2023-01-02');
INSERT INTO `sgp`.`cat_tarifa_isr` (`limite_inferior`, `limite_superior`, `cuota_fija`, `porc_apl_exce_lim_inf`, `tipo`, `fecha`) VALUES (6332.06, 11128.01, 371.83, 10.88, 'm', '2023-01-02');
INSERT INTO `sgp`.`cat_tarifa_isr` (`limite_inferior`, `limite_superior`, `cuota_fija`, `porc_apl_exce_lim_inf`, `tipo`, `fecha`) VALUES (11128.02, 12935.82, 893.63, 16, 'm', '2023-01-02');
INSERT INTO `sgp`.`cat_tarifa_isr` (`limite_inferior`, `limite_superior`, `cuota_fija`, `porc_apl_exce_lim_inf`, `tipo`, `fecha`) VALUES (12935.83, 15487.71, 1182.88, 17.92, 'm', '2023-01-02');
INSERT INTO `sgp`.`cat_tarifa_isr` (`limite_inferior`, `limite_superior`, `cuota_fija`, `porc_apl_exce_lim_inf`, `tipo`, `fecha`) VALUES (15487.72, 31236.49, 1640.18, 21.36, 'm', '2023-01-02');
INSERT INTO `sgp`.`cat_tarifa_isr` (`limite_inferior`, `limite_superior`, `cuota_fija`, `porc_apl_exce_lim_inf`, `tipo`, `fecha`) VALUES (31236.5, 49233, 5004.12, 23.52, 'm', '2023-01-02');
INSERT INTO `sgp`.`cat_tarifa_isr` (`limite_inferior`, `limite_superior`, `cuota_fija`, `porc_apl_exce_lim_inf`, `tipo`, `fecha`) VALUES (49233.01, 93993.9, 9236.89, 30, 'm', '2023-01-02');
INSERT INTO `sgp`.`cat_tarifa_isr` (`limite_inferior`, `limite_superior`, `cuota_fija`, `porc_apl_exce_lim_inf`, `tipo`, `fecha`) VALUES (93993.91, 125325.2, 22665.17, 32, 'm', '2023-01-02');
INSERT INTO `sgp`.`cat_tarifa_isr` (`limite_inferior`, `limite_superior`, `cuota_fija`, `porc_apl_exce_lim_inf`, `tipo`, `fecha`) VALUES (125325.21, 375975.61, 32691.18, 34, 'm', '2023-01-02');
INSERT INTO `sgp`.`cat_tarifa_isr` (`limite_inferior`, `limite_superior`, `cuota_fija`, `porc_apl_exce_lim_inf`, `tipo`, `fecha`) VALUES (375975.62, 999999.99, 117912.32, 35, 'm', '2023-01-02');

-- -----------------------------------------------------
-- Table `sgp`.`cat_subsidio`
-- -----------------------------------------------------
CREATE TABLE `sgp`.`cat_subsidio` (
  `id_subsidio` INT NOT NULL AUTO_INCREMENT,
  `para_ingresos_de` FLOAT(10,2) NOT NULL,
  `hasta_ingresos_de` FLOAT(10,2) NOT NULL,
  `cantidad_subsidio` FLOAT(10,2) NOT NULL,
  `fecha` DATE NOT NULL,
  PRIMARY KEY (`id_subsidio`))
ENGINE=InnoDB
DEFAULT CHARSET=utf8mb4
COLLATE=utf8mb4_0900_ai_ci;
  
INSERT INTO `sgp`.`cat_subsidio` (`para_ingresos_de`, `hasta_ingresos_de`, `cantidad_subsidio`, `fecha`) VALUES (0.01, 407.33, 93.73, '2023-01-02');
INSERT INTO `sgp`.`cat_subsidio` (`para_ingresos_de`, `hasta_ingresos_de`, `cantidad_subsidio`, `fecha`) VALUES (407.34, 610.96, 93.66, '2023-01-02');
INSERT INTO `sgp`.`cat_subsidio` (`para_ingresos_de`, `hasta_ingresos_de`, `cantidad_subsidio`, `fecha`) VALUES (610.97, 799.68, 93.66, '2023-01-02');
INSERT INTO `sgp`.`cat_subsidio` (`para_ingresos_de`, `hasta_ingresos_de`, `cantidad_subsidio`, `fecha`) VALUES (799.69, 814.66, 90.44, '2023-01-02');
INSERT INTO `sgp`.`cat_subsidio` (`para_ingresos_de`, `hasta_ingresos_de`, `cantidad_subsidio`, `fecha`) VALUES (814.67, 1023.75, 88.06, '2023-01-02');
INSERT INTO `sgp`.`cat_subsidio` (`para_ingresos_de`, `hasta_ingresos_de`, `cantidad_subsidio`, `fecha`) VALUES (1023.76, 1086.19, 81.55, '2023-01-02');
INSERT INTO `sgp`.`cat_subsidio` (`para_ingresos_de`, `hasta_ingresos_de`, `cantidad_subsidio`, `fecha`) VALUES (1086.20, 1228.57, 74.83, '2023-01-02');
INSERT INTO `sgp`.`cat_subsidio` (`para_ingresos_de`, `hasta_ingresos_de`, `cantidad_subsidio`, `fecha`) VALUES (1228.58, 1433.32, 67.83, '2023-01-02');
INSERT INTO `sgp`.`cat_subsidio` (`para_ingresos_de`, `hasta_ingresos_de`, `cantidad_subsidio`, `fecha`) VALUES (1433.33, 1638.07, 58.38, '2023-01-02');
INSERT INTO `sgp`.`cat_subsidio` (`para_ingresos_de`, `hasta_ingresos_de`, `cantidad_subsidio`, `fecha`) VALUES (1638.08, 1699.88, 50.12, '2023-01-02');
INSERT INTO `sgp`.`cat_subsidio` (`para_ingresos_de`, `hasta_ingresos_de`, `cantidad_subsidio`, `fecha`) VALUES (1699.89, 9999.99, 0, '2023-01-02');

-- -----------------------------------------------------
-- Table `sgp`.`cat_imss_cuotas`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `sgp`.`cat_imss_cuotas`(
	`id_imss_cuotas` INT UNSIGNED NOT NULL AUTO_INCREMENT,
    `riesgo_trabajo` FLOAT UNSIGNED NOT NULL,
    `enf_mat_esp_ct_fija` FLOAT UNSIGNED NOT NULL,
    `enf_mat_esp_ct_ad` FLOAT UNSIGNED NOT NULL,
    `enf_mat_gastos_med` FLOAT UNSIGNED NOT NULL,
    `enf_mat_dinero` FLOAT UNSIGNED NOT NULL,
    `inv_vida` FLOAT UNSIGNED NOT NULL,
    `ret_cesantia_vejez_retiro` FLOAT UNSIGNED NOT NULL,
    `ret_cesantia_vejez_ceav` FLOAT UNSIGNED NOT NULL,
    `guarderia` FLOAT UNSIGNED NOT NULL,
    `infonavit` FLOAT UNSIGNED NOT NULL,
    `cuota` VARCHAR(10) NOT NULL, 
    `fecha_cap` DATETIME NOT NULL,
    `activo` TINYINT NOT NULL DEFAULT 1,
    PRIMARY KEY (`id_imss_cuotas`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;

INSERT INTO `sgp`.`cat_imss_cuotas` (`riesgo_trabajo`, `enf_mat_esp_ct_fija`, `enf_mat_esp_ct_ad`, `enf_mat_gastos_med`, `enf_mat_dinero`,  `inv_vida`, `ret_cesantia_vejez_retiro`, `ret_cesantia_vejez_ceav`,`guarderia`, `infonavit`, `cuota`, `fecha_cap`, `activo`) 
							     VALUES (0, 0, 0.004, 0.00375, 0.0025, 0.00625, 0, 0.01125, 0, 0, 'T', '2023-01-01 00:00:00', 1),
                                        (0.0465325, 0.204, 0.011, 0.0105, 0.007, 0.0175, 0.02, 0.0315, 0.01, 0.05, 'P', '2023-01-01 00:00:00', 1);

SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema rickyrack
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema rickyrack
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `rickyrack` DEFAULT CHARACTER SET utf8 ;
USE `rickyrack` ;

-- -----------------------------------------------------
-- Table `rickyrack`.`usuario`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `rickyrack`.`usuario` (
  `idusuario` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `nombre` VARCHAR(30) NOT NULL,
  `password` VARCHAR(35) NOT NULL,
  `imagen` TINYINT UNSIGNED NOT NULL check(`imagen` between 1 and 4),
  `experiencia` SMALLINT UNSIGNED NOT NULL check(`experiencia` between 0 and 800),
  `nivel` TINYINT UNSIGNED NOT NULL check(`nivel` between 1 and 5),
  `hash` VARCHAR(8) NULL,
  PRIMARY KEY (`idusuario`),
  UNIQUE INDEX `username_UNIQUE` (`nombre` ASC) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `rickyrack`.`producto`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `rickyrack`.`producto` (
  `idproducto` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `categoria` TINYINT UNSIGNED NOT NULL check(`categoria` IN(0,1,2)),
  `titulo` VARCHAR(45) NOT NULL UNIQUE,
  `duracion` SMALLINT UNSIGNED NOT NULL,
  `sinopsis` VARCHAR(1000) NOT NULL,
  `genero` VARCHAR(45) NOT NULL,
  `autor` VARCHAR(50) NOT NULL,
  `fecha` DATE NOT NULL,
  PRIMARY KEY (`idproducto`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `rickyrack`.`multimedia`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `rickyrack`.`multimedia` (
  `idmultimedia` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `producto_idproducto` INT UNSIGNED NOT NULL,
  `ruta` VARCHAR(500) NOT NULL,
  `tipo` CHAR(1) NOT NULL check(`tipo` IN ('I','P')),
  PRIMARY KEY (`idmultimedia`),
  INDEX `fk_media_producto1_idx` (`producto_idproducto` ASC) ,
  CONSTRAINT `fk_media_producto1`
    FOREIGN KEY (`producto_idproducto`)
    REFERENCES `rickyrack`.`producto` (`idproducto`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `rickyrack`.`comentario`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `rickyrack`.`comentario` (
  `idcomentario` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `usuario_idusuario` INT UNSIGNED NOT NULL,
  `producto_idproducto` INT UNSIGNED NOT NULL,
  `comentario` VARCHAR(300) NOT NULL,
  PRIMARY KEY (`idcomentario`),
  INDEX `fk_comentario_usuario1_idx` (`usuario_idusuario` ASC) ,
  INDEX `fk_comentario_producto1_idx` (`producto_idproducto` ASC) ,
  CONSTRAINT `fk_comentario_usuario1`
    FOREIGN KEY (`usuario_idusuario`)
    REFERENCES `rickyrack`.`usuario` (`idusuario`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_comentario_producto1`
    FOREIGN KEY (`producto_idproducto`)
    REFERENCES `rickyrack`.`producto` (`idproducto`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `rickyrack`.`like`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `rickyrack`.`like` (
  `idlike` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `usuario_idusuario` INT UNSIGNED NOT NULL,
  `comentario_idcomentario` INT UNSIGNED NOT NULL,
  `dislike` TINYINT NOT NULL check(`dislike` IN(0,1)),
  PRIMARY KEY (`idlike`),
  INDEX `fk_like_usuario1_idx` (`usuario_idusuario` ASC) ,
  INDEX `fk_like_comentario1_idx` (`comentario_idcomentario` ASC) ,
  CONSTRAINT `fk_like_usuario1_idx`
    FOREIGN KEY (`usuario_idusuario`)
    REFERENCES `rickyrack`.`usuario` (`idusuario`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_like_comentario1_idx`
    FOREIGN KEY (`comentario_idcomentario`)
    REFERENCES `rickyrack`.`comentario` (`idcomentario`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `rickyrack`.`calificacion`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `rickyrack`.`calificacion` (
  `idcalificacion` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `usuario_idusuario` INT UNSIGNED NOT NULL,
  `producto_idproducto` INT UNSIGNED NOT NULL,
  `calificacion` TINYINT UNSIGNED NOT NULL check(`calificacion` between 1 and 5),
  PRIMARY KEY (`idcalificacion`),
  INDEX `fk_calificacion_usuario1_idx` (`usuario_idusuario` ASC) ,
  INDEX `fk_calificacion_producto1_idx` (`producto_idproducto` ASC) ,
  CONSTRAINT `fk_calificacion_usuario1`
    FOREIGN KEY (`usuario_idusuario`)
    REFERENCES `rickyrack`.`usuario` (`idusuario`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_calificacion_producto1`
    FOREIGN KEY (`producto_idproducto`)
    REFERENCES `rickyrack`.`producto` (`idproducto`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;

-- -----------------------------------------------------
-- INSERTS
-- -----------------------------------------------------

/* Usuarios */
INSERT INTO `usuario` (`nombre`,`password`,`imagen`,`experiencia`,`nivel`)
VALUES
  ("Jony","jon89",3,429,3),
  ("Navy","heyListenheyListen",4,500,5),
  ("Ash","PikachuElMejor123",3,230,4),
  ("Mario","MamaMia",3,120,2),
  ("Luigi","MarioVerde",4,201,1),
  ("Link","NoSoyZelda",3,300,1),
  ("Zelda","NoSoyLink",2,0,5),
  ("Rocio","123",1,120,3),
  ("Yoigo","Spiderman2020?",4,20,1),
  ("Antonia","MarioCasas2012",1,0,1);
  
/* Hash de Ejemplos */
INSERT INTO `usuario` (`nombre`,`password`,`imagen`,`experiencia`,`nivel`,`hash`)
VALUES
	("Mario12","Cachito",3,200,2,"5uVXBle"),
    ("Marina","PanDePipas",2,100,3,"T2aF1TN");

/*********************** PRODUCTOS ****************************/
/* Libros */
INSERT INTO `producto` (`categoria`,`titulo`,`duracion`,`sinopsis`,`genero`,`autor`,`fecha`)
VALUES
 (0,"Todas mis amigas",416,"Luna es alocada, divertida y la más graciosa, pero no siempre es así. ¿Quieres saber por qué? Penélope vive con su novio, y su vida ordenada es un poco aburrida ¿O quizá no tanto como parece? Edith es seria, responsable y pragmática, tanto que nunca se deja llevar, o casi nunca... ¿Y Noa? Noa no soporta a los hombres, pero al mismo tiempo va a perder el norte por uno:ÉL.Cuatro maneras de ver el mundo que se complementan para crear un universo propio donde el amor y la amistad podrán con todo.O con casi todo...","Romantica", "Susana Rubio",'2023-02-07'),
 (0,"Verity. La sombra de un engaño",416,"Lowen Ashleigh recibe un encargo que le cambiará la vida: Jeremy, el flamante marido de Verity Crawford,una de las autoras más importantes del momento, la contrata para terminar la serie de libros en la que trabajaba su mujer antes de sufrir un grave accidente que la ha dejado en coma. Cuando Lowen se instala en la mansión del matrimonio para poder trabajar con los materiales de Verity, descubre una autobiografía escondida en la que Verity hace confesiones escalofriantes. Lowen decide ocultarle el manuscrito a Jeremy, pero a medida que sus sentimientos por él comienzan a intensificarse, se da cuenta que ella podría beneficiarse si Jeremy leyera las palabras de su mujer.","Suspense", "Collen Hoover",'2020-02-08'),
 (0,"Escrayber.El legado del grimorio",498,"En Escrayber, Ashter Clark se adentra en un ciberespacio literario, donde aspira a formarse en la Universidad Autónoma de Escritores en la ciudad de Lybraria.Sin embargo, descubre que palabras y frases desaparecen de los libros en este mundo. Deberá emprender una misión épica, cruzándose con figuras icónicas como Sherlock Holmes, Drácula, El Principito, Elizabeth Bennet y muchos más para restaurar las historias distorsionadas. La trama fusiona fantasía y literatura clásica, ofreciendo una trepidante odisea en la que tecnología y letras colisionan.","Fantasía", "Daniel Sorbas",'2024-02-18'),
 (0,"1984",408,"En el año 1984 Londres es una ciudad lúgubre enla que la Policía del Pensamiento controla de forma asfixiante la vida de los ciudadanos. Winston Smith es un peón de este engranaje perverso y su cometido es reescribir la historia para adaptarla a lo que el Partido considera la versión oficial de los hechos. Hasta que decide replantearse la verdad del sistema que los gobierna y somete.","Ciencia-Ficcion", "George Orwell",'2023-02-08'),
 (0,"Como vender una casa encantada",440,"Cuando Louise se entera de que sus padres han muerto, teme volver a casa. No quiere dejar a su pequeña con su ex y volar a Charleston. No quiere enfrentarse al domicilio familiar, donde se amontonan los restos de la vida académica de su padre y de la constante obsesión de su madre por los títeres y los muñecos. No quiere aprender a vivir sin las dos personas que mejor la han conocido y más la han querido del mundo entero.","Ciencia-ficción", "Grady Hendrix",'2023-10-18');
 
 /* Peliculas */
INSERT INTO `producto` (`categoria`,`titulo`,`duracion`,`sinopsis`,`genero`,`autor`,`fecha`)
VALUES
  (1, "American Ganster",157,"Nueva York, 1968. Frank Lucas (Denzel Washington) es el taciturno chófer de un importante mafioso negro de Harlem. Cuando su jefe muere inesperadamente, Frank aprovecha la oportunidad para construir su propio imperio. Gracias a su talento, se convierte no sólo en el principal narcotraficante de la ciudad, inundando las calles con productos de mejor calidad y precio, sino también en un hombre público muy respetado. Richie Roberts (Russell Crowe), un policía incorruptible marginado por su honradez que conoce bien las calles, se da cuenta de que una persona ajena a los clanes trepa por la escalera del poder. Tanto Roberts como Lucas comparten un estricto código ético que les aparta de los demás y los convierte en dos figuras solitarias en lados opuestos de la ley. Cuando se encuentren, el enfrentamiento entre ellos será inevitable.","Thriller","Ridley Scott", '2007-10-03'),
  (1, "El cabo del miedo",130,"Max Cady (de Niro), un delincuente que acaba de ser puesto en libertad tras catorce años entre rejas, busca al abogado Sam Bowden (Nolte), para vengarse de él, pues lo considera responsable de su condena. La presión y el acoso que ejerce sobre la familia Bowden es cada vez más intensa y amenazadora. Remake del filme de J. Lee Thompson (El cabo del terror), de 1962.","Thriller","Martin Scorsese", '1991-02-05'),
  (1, "Tarzan",84,"Cuando Kala, una gorila hembra, encuentra un niño huérfano en la jungla, decide adoptarlo como su propio hijo a pesar de la oposición de Kerchak, el jefe de la manada. Junto a Terk, un gracioso mono y Tantor, un elefante algo neurótico, Tarzán crecerá en la jungla desarrollando los instintos de los animales y aprendiendo a deslizarse entre los árboles a velocidad de vértigo. Pero cuando una expedición se adentra en la jungla y Tarzán conoce a Jane, descubrirá quién es realmente y cuál es el mundo al que pertenece.","Animacion","Kevin Lima",'1999-04-01'),
  (1, "El resplandor", 146,"Jack Torrance se traslada con su mujer y su hijo de siete años al impresionante hotel Overlook, en Colorado, para encargarse del mantenimiento de las instalaciones durante la temporada invernal, época en la que permanece cerrado y aislado por la nieve. Su objetivo es encontrar paz y sosiego para escribir una novela. Sin embargo, poco después de su llegada al hotel, al mismo tiempo que Jack empieza a padecer inquietantes trastornos de personalidad, se suceden extraños y espeluznantes fenómenos paranormales.","Terror","Stanley Kubrick",'1980-07-10'),
  (1, "Terminator 2: El juicio final",135,"Sarah Connor, la madre soltera del rebelde John Connor, está ingresada en un psiquiátrico. Algunos años antes, un viajero del tiempo le había revelado que su hijo sería el salvador de la humanidad en un futuro -año 2029- dominado por las máquinas. Se convirtió entonces en una especie de guerrera y educó a su hijo John en tácticas de supervivencia. Esta es la razón por la que está recluida en un manicomio. Cuando un nuevo androide mejorado, un T-1000, llega del futuro para asesinar a John, un viejo modelo T-800 será enviado para protegerle.","Ciencia-ficcion","James Cameron",'1991-11-11');

/* Videojuegos */
INSERT INTO `producto` (`categoria`,`titulo`,`duracion`,`sinopsis`,`genero`,`autor`,`fecha`)
VALUES
	(2,"The Legend of Zelda: Ocarina of Time",27,"La historia del juego se enfoca en el joven héroe Link, quien emprende una aventura en el reino de Hyrule para detener a Ganondorf, rey de la tribu Gerudo, antes de que encuentre la Trifuerza, una reliquia sagrada capaz de concederle cualquier deseo a su poseedor. Para ello, debe viajar a través del tiempo y explorar varios templos con el fin de despertar a algunos sabios que tienen el poder para aprisionar de forma definitiva a Ganondorf. Se ha de mencionar que la música juega un papel muy importante en la trama del juego, puesto que el jugador tiene que aprender a tocar varias canciones con una ocarina.","Aventuras","Nintendo",'1998-11-21'),
    (2,"Subnautica",30,"Subnautica es un juego de supervivencia, aventura y terror ambientado en un mundo abierto y jugado desde una perspectiva en primera persona. El jugador controla al único superviviente de una nave espacial estrellada, llamada Aurora, en el ficticio planeta oceánico 4546B. Los restos de la nave explotan poco después de comenzar el juego, desde donde se puede explorar. Al entrar en la nave, la PDA (por sus siglas en inglés: Personal Digital Assistant) advierte que las criaturas que rodean la Aurora contienen restos de tejido humano dentro de su sistema digestivo, y continúa aconsejándole cosas sobre la exploración. El objetivo principal del jugador es explorar el mundo del juego y sobrevivir a los peligros del planeta, mientras que al mismo tiempo sigue la historia del juego. Subnautica le permite al jugador recolectar recursos, construir herramientas, bases e interactuar con la vida salvaje del planeta.","Supervivencia","Unkown Worlds",'2018-01-23'),
    (2,"Pokemon Diamante y Perla",41,"La región ficticia de Sinnoh —ambientada en Hokkaidō— incluyó 108 nuevas criaturas y es el escenario principal que da lugar al argumento:​ las aventuras de un entrenador Pokémon en un viaje para completar la Pokédex y convertirse en Campeón de la Liga Pokémon, en tanto que frustra los planes del Equipo Galaxia y se enfrenta al legendario Dialga en Diamante y Palkia en Perla.8​ Los títulos de cuarta generación siguen las convenciones habituales de la saga, donde el jugador maneja a un personaje en perspectiva cenital que se mueve a través de un mapa plagado de criaturas salvajes —que se capturan con Poké Balls— y entrenadores. Los combates consisten en un sistema por turnos y basado en movimientos. Al derrotar a un rival, el equipo del usuario adquiere puntos de experiencia que aumentan el nivel del Pokémon; tras alcanzar cierta cantidad de niveles puede evolucionar y cambiar de forma.","RPG","Game Freak",'2007-09-28'),
    (2,"Tetris",5,"La versión de Tetris para Game Boy se juega de manera idéntica a las versiones de otras plataformas. Una secuencia semi aleatoria de tetriminos (formas compuestas por cuatro bloques cuadrados cada uno) caen en el campo de juego, que tiene 10 bloques de ancho por 18 bloques de alto en la versión de Game Boy. El objetivo del juego es manipular estas tétradas, moviéndolas a los lados y girándolas en unidades de 90 grados, con el objetivo de crear una línea horizontal de bloques sin espacios. Cuando se crean una o más de estas líneas, desaparecen, y los bloques de encima (si los hay) se mueven hacia abajo por la cantidad de líneas borradas. Al igual que la mayoría de versiones estándar de Tetris, los bloques no caen automáticamente en espacios abiertos cuando se crea una línea.","Puzles","Nintendo",'1989-06-14'),
	(2,"Super Mario Bros",2,"Los jugadores controlan a Mario, o a su hermano Luigi (en el modo multijugador), mientras atraviesan el Reino Champiñón para rescatar a la Princesa Toadstool del Rey Koopa (más tarde llamado Bowser). Atraviesan etapas de desplazamiento lateral mientras evitan peligros como enemigos y pozos con la ayuda de potenciadores como Súper Champiñónes, Flores de fuego y la estrella. El juego fue diseñado por Shigeru Miyamoto y Takashi Tezuka como «una gran culminación» de los tres años de mecánica de juego y programación del equipo de Famicom, aprovechando sus experiencias trabajando en Devil World y los juegos de desplazamiento lateral Excitebike y Kung Fu para avanzar en su trabajo anterior en «juegos deportivos» de plataformas como Donkey Kong y Mario Bros. El diseño del primer nivel, World 1-1, sirve como un tutorial para el juego de plataformas.","Plataformas","Nintendo",'1985-09-13');

/************************* CALIFICACIONES **********************/
/* Mario Bros */
INSERT INTO `calificacion`(`usuario_idusuario`,`producto_idproducto`,`calificacion`)
VALUES
	(1,15,5),
	(2,15,5),
	(3,15,5),
	(4,15,5),
    (5,15,5),
    (6,15,5),
    (7,15,4);
/* Tetris */
INSERT INTO `calificacion`(`usuario_idusuario`,`producto_idproducto`,`calificacion`)
VALUES
    (6,14,3),
    (7,14,4),
    (8,14,4),
    (9,14,3),
    (5,14,3);
/* Pokemon Diamante y Perla */
INSERT INTO `calificacion`(`usuario_idusuario`,`producto_idproducto`,`calificacion`)
VALUES
    (3,13,5),
    (7,13,4),
    (1,13,4),
    (4,13,3),
    (5,13,5);
/* Subnautica */
INSERT INTO `calificacion`(`usuario_idusuario`,`producto_idproducto`,`calificacion`)
VALUES
    (1,12,5),
    (5,12,4),
    (3,12,5),
    (8,12,4),
    (2,12,5);
/* Ocarina of Time */
INSERT INTO `calificacion`(`usuario_idusuario`,`producto_idproducto`,`calificacion`)
VALUES
    (1,11,5),
    (6,11,4),
    (2,11,5),
    (8,11,5),
    (10,11,5);
/* Terminator 2 */
INSERT INTO `calificacion`(`usuario_idusuario`,`producto_idproducto`,`calificacion`)
VALUES
    (6,10,4),
    (7,10,4),
    (5,10,4),
    (4,10,3),
    (2,10,3);
/* Resplandor */
INSERT INTO `calificacion`(`usuario_idusuario`,`producto_idproducto`,`calificacion`)
VALUES
    (1,9,5),
    (9,9,5),
    (10,9,5),
    (4,9,4),
    (3,9,4);
/* Tarzan */
INSERT INTO `calificacion`(`usuario_idusuario`,`producto_idproducto`,`calificacion`)
VALUES
    (6,8,4),
    (10,8,4),
    (4,8,3),
    (2,8,4),
    (1,8,4);
/* El cabo del miedo */
INSERT INTO `calificacion`(`usuario_idusuario`,`producto_idproducto`,`calificacion`)
VALUES
    (5,7,4),
    (10,7,4),
    (4,7,3),
    (3,7,2),
    (2,7,3);
/* American Ganster */
INSERT INTO `calificacion`(`usuario_idusuario`,`producto_idproducto`,`calificacion`)
VALUES
    (9,6,4),
    (10,6,3),
    (8,6,3),
    (1,6,2),
    (2,6,3);
/* Vender Casa Encantada */
INSERT INTO `calificacion`(`usuario_idusuario`,`producto_idproducto`,`calificacion`)
VALUES
    (6,5,4),
    (5,5,3),
    (3,5,3),
    (1,5,5),
    (2,5,4);
/* 1984 */
INSERT INTO `calificacion`(`usuario_idusuario`,`producto_idproducto`,`calificacion`)
VALUES
    (9,4,2),
    (8,4,1),
    (7,4,1),
    (3,4,2),
    (1,4,3);
/* Escrabeytir */
INSERT INTO `calificacion`(`usuario_idusuario`,`producto_idproducto`,`calificacion`)
VALUES
    (2,3,4),
    (1,3,3),
    (10,3,5),
    (3,3,4),
    (7,3,4);
/* Verity */
INSERT INTO `calificacion`(`usuario_idusuario`,`producto_idproducto`,`calificacion`)
VALUES
    (4,2,4),
    (3,2,3),
    (2,2,3),
    (1,2,4),
    (7,2,4);
/* Todas mis amigas */
INSERT INTO `calificacion`(`usuario_idusuario`,`producto_idproducto`,`calificacion`)
VALUES
    (7,1,1),
    (2,1,1),
    (9,1,1),
    (4,1,2),
    (5,1,2);

/*Comentarios*/
INSERT INTO `comentario` (`usuario_idusuario`,`producto_idproducto`,`comentario`)
VALUES
	(5,15,"Siempre me toca ser el segundo"),
    (4,15,"Aplastas Goombas indefensos 10/10"),
    (2,15,"Hey listen!"),
    (10,15,"El juego de mi infancia"),
    (1,9,"Here's Johnny!"),
    (8,9,"Un clásico entre los amantes de las películas"),
    (9,9,"Insuperable"),
    (10,9,"Una de la mejores peliculas de terror de la historia"),
    (8,5,"Vendo casa con fantasmas que potencialmente te van a matar por 300.000€"),
    (9,5,"No se de que va el libro pero mola el titulo"),
    (10,5,"Está bien"),
    (11,5,"No es de lo mejor");

/*Multimedia*/    
INSERT INTO `multimedia` (`producto_idproducto`,`ruta`,`tipo`)
VALUES
  (1,".\\media\\img\\default\\Libro1.jpg","P"),
  (2,".\\media\\img\\default\\Libro2.jpg","P"), 
  (3,".\\media\\img\\default\\Libro3.jpg","P"),
  (4,".\\media\\img\\default\\Libro4.jpg","P"),
  (5,".\\media\\img\\default\\Libro5.jpg","P"),
  (6,".\\media\\img\\default\\Pelicula1.jpg","P"),
  (7,".\\media\\img\\default\\Pelicula2.jpg","P"),
  (8,".\\media\\img\\default\\Pelicula3.jpg","P"),
  (9,".\\media\\img\\default\\Pelicula4.jpg","P"),
  (10,".\\media\\img\\default\\Pelicula5.jpg","P"),
  (11,".\\media\\img\\default\\Videojuego1.jpg","P"),
  (12,".\\media\\img\\default\\Videojuego2.jpg","P"),
  (13,".\\media\\img\\default\\Videojuego3.jpg","P"),
  (14,".\\media\\img\\default\\Videojuego4.jpg","P"),
  (15,".\\media\\img\\default\\Videojuego5.jpg","P");
-- phpMyAdmin SQL Dump
-- version 5.1.1
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 03-12-2021 a las 20:39:36
-- Versión del servidor: 10.4.21-MariaDB
-- Versión de PHP: 8.0.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `playmotion`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `canciones`
--

CREATE TABLE `canciones` (
  `id` int(10) NOT NULL,
  `nombre` varchar(100) NOT NULL,
  `artista` varchar(100) NOT NULL,
  `album` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `emociones`
--

CREATE TABLE `emociones` (
  `id` int(10) NOT NULL,
  `emocion` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Volcado de datos para la tabla `emociones`
--

INSERT INTO `emociones` (`id`, `emocion`) VALUES
(1, 'mad'),
(4, 'critical'),
(5, 'hateful'),
(6, 'selfish'),
(7, 'angry'),
(8, 'hostile'),
(9, 'hurt'),
(10, 'skeptical'),
(11, 'irritated'),
(12, 'jealous'),
(13, 'frustrated'),
(14, 'sarcastic'),
(15, 'distant'),
(16, 'scared'),
(17, 'confused'),
(18, 'rejected'),
(19, 'helpless'),
(20, 'submissive'),
(21, 'insecure'),
(22, 'anxious'),
(23, 'bewildered'),
(24, 'discouraged'),
(25, 'insignificant'),
(26, 'inadequate'),
(27, 'embarrased'),
(28, 'overwhelmed'),
(29, 'joyful'),
(30, 'excited'),
(31, 'sensuous'),
(32, 'energetic'),
(33, 'selfish'),
(34, 'cheerful'),
(35, 'creative'),
(36, 'hopeful'),
(37, 'daring'),
(38, 'fascinating'),
(39, 'stimulating'),
(40, 'amused'),
(41, 'playful'),
(42, 'optimistic'),
(43, 'powerful'),
(44, 'aware'),
(45, 'proud'),
(46, 'respect'),
(47, 'appreciated'),
(48, 'important'),
(49, 'faithful'),
(50, 'confident'),
(51, 'discerning'),
(52, 'valueable'),
(53, 'worthwhile'),
(54, 'succesful'),
(55, 'surprised'),
(56, 'peaceful'),
(57, 'content'),
(58, 'thoughtful'),
(59, 'intimate'),
(60, 'loving'),
(61, 'trusting'),
(62, 'nurturing'),
(63, 'thankful'),
(64, 'secure'),
(65, 'serene'),
(66, 'responsive'),
(67, 'pensive'),
(68, 'relaxed'),
(69, 'sad'),
(70, 'guilty'),
(71, 'ashamed'),
(72, 'depressed'),
(73, 'lonely'),
(74, 'bored'),
(75, 'tired'),
(76, 'sleepy'),
(77, 'apathetic'),
(78, 'isolated'),
(79, 'inferior'),
(80, 'stupid'),
(81, 'remorseful');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `relaciones`
--

CREATE TABLE `relaciones` (
  `idusuario` int(11) NOT NULL,
  `idcancion` int(11) NOT NULL,
  `idemocion` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `usuarios`
--

CREATE TABLE `usuarios` (
  `id` int(10) NOT NULL,
  `usuario` varchar(100) NOT NULL,
  `nombre` varchar(100) NOT NULL,
  `contrasenia` varchar(100) NOT NULL,
  `fechanacimiento` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Volcado de datos para la tabla `usuarios`
--

INSERT INTO `usuarios` (`id`, `usuario`, `nombre`, `contrasenia`, `fechanacimiento`) VALUES
(4, 'code_groove', 'Juan Daniel Rodríguez Espinoza', '40bd001563085fc35165329ea1ff5c5ecbdbbeef', '1996-07-22'),
(5, 'mango', 'Daniel', '40bd001563085fc35165329ea1ff5c5ecbdbbeef', '1996-07-22');

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `canciones`
--
ALTER TABLE `canciones`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `emociones`
--
ALTER TABLE `emociones`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `usuarios`
--
ALTER TABLE `usuarios`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UQ_usuario` (`usuario`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `emociones`
--
ALTER TABLE `emociones`
  MODIFY `id` int(10) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=82;

--
-- AUTO_INCREMENT de la tabla `usuarios`
--
ALTER TABLE `usuarios`
  MODIFY `id` int(10) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;

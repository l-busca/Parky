-- phpMyAdmin SQL Dump
-- version 5.1.1
-- https://www.phpmyadmin.net/
--
-- Hôte : 127.0.0.1:3306
-- Généré le : dim. 29 mai 2022 à 22:34
-- Version du serveur : 5.7.36
-- Version de PHP : 7.4.26

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de données : `parky`
--

-- --------------------------------------------------------

--
-- Structure de la table `borne`
--

DROP TABLE IF EXISTS `borne`;
CREATE TABLE IF NOT EXISTS `borne` (
  `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `etat` enum('disponible','indisponible','reservee','occupe') NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;

--
-- Déchargement des données de la table `borne`
--

INSERT INTO `borne` (`id`, `etat`) VALUES
(1, 'indisponible'),
(2, 'indisponible'),
(3, 'indisponible');

-- --------------------------------------------------------

--
-- Structure de la table `client`
--

DROP TABLE IF EXISTS `client`;
CREATE TABLE IF NOT EXISTS `client` (
  `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `nom` varchar(50) NOT NULL,
  `prenom` varchar(50) NOT NULL,
  `adresse` varchar(100) NOT NULL,
  `numero` varchar(10) NOT NULL,
  `carte` char(64) NOT NULL,
  `mdp` char(64) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=8 DEFAULT CHARSET=latin1;

--
-- Déchargement des données de la table `client`
--

INSERT INTO `client` (`id`, `nom`, `prenom`, `adresse`, `numero`, `carte`, `mdp`) VALUES
(2, 'Busca', 'Ludovic', 'adresse de test', '0674997203', 'eda71b82d84a90e3716b3dac2ba561b938755cd371e4733cbebc51018d1c2c85', '6f28e903b309e7f3eac92fc74e7417f23ec330556259d8ca0c880771dd86172b'),
(3, 'Simon', 'Jordan', 'adresse de test', '0488956231', 'eda71b82d84a90e3716b3dac2ba561b938755cd371e4733cbebc51018d1c2c85', '9248dbb4d94adfc7455990248486f1f9d8ded09ba398ee72e24071d3652f89e5'),
(4, 'Lion', 'Boulon', 'adresse de test', '0789542321', 'eda71b82d84a90e3716b3dac2ba561b938755cd371e4733cbebc51018d1c2c85', '4f682b71153ffa91e608445d7ea1257e2076d0d95eab6336cd1aa94b49680f11'),
(5, 'Laurent', 'Yugi', 'adresse de test', '0714589632', 'eda71b82d84a90e3716b3dac2ba561b938755cd371e4733cbebc51018d1c2c85', '4f682b71153ffa91e608445d7ea1257e2076d0d95eab6336cd1aa94b49680f11'),
(6, 'Oui', 'Non', 'adresse de test', '0674882694', 'eda71b82d84a90e3716b3dac2ba561b938755cd371e4733cbebc51018d1c2c85', '4f682b71153ffa91e608445d7ea1257e2076d0d95eab6336cd1aa94b49680f11'),
(7, 'Jean', 'Mathieu', 'adresse de test', '0377889945', 'eda71b82d84a90e3716b3dac2ba561b938755cd371e4733cbebc51018d1c2c85', 'ff0e6c2dea2d0bee4b75bb243a73715465890ba0adf2a7de7da08d0ce37098b6');

-- --------------------------------------------------------

--
-- Structure de la table `parametre`
--

DROP TABLE IF EXISTS `parametre`;
CREATE TABLE IF NOT EXISTS `parametre` (
  `duree` int(11) NOT NULL,
  `duplicateur` double NOT NULL,
  `tarif_dissuasif` double NOT NULL,
  `max_prolongation` int(11) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Structure de la table `possede`
--

DROP TABLE IF EXISTS `possede`;
CREATE TABLE IF NOT EXISTS `possede` (
  `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `client` int(11) NOT NULL,
  `vehicule` char(9) NOT NULL,
  `temporaire` tinyint(1) NOT NULL,
  `actif` tinyint(1) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `client_FK` (`client`)
) ENGINE=MyISAM AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;

--
-- Déchargement des données de la table `possede`
--

INSERT INTO `possede` (`id`, `client`, `vehicule`, `temporaire`, `actif`) VALUES
(1, 2, '47-898-AB', 0, 1),
(2, 6, '78-845-AB', 0, 1);

-- --------------------------------------------------------

--
-- Structure de la table `reservation`
--

DROP TABLE IF EXISTS `reservation`;
CREATE TABLE IF NOT EXISTS `reservation` (
  `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `termine` tinyint(1) NOT NULL,
  `etat` enum('attente','present','retard','absent','annule') NOT NULL,
  `borne` int(11) NOT NULL,
  `possession` int(11) NOT NULL,
  `prix` double NOT NULL,
  `temps` int(11) NOT NULL,
  `date` datetime NOT NULL,
  `prolonge` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=10 DEFAULT CHARSET=latin1;

--
-- Déchargement des données de la table `reservation`
--

INSERT INTO `reservation` (`id`, `termine`, `etat`, `borne`, `possession`, `prix`, `temps`, `date`, `prolonge`) VALUES
(1, 0, 'attente', 1, 2, 10, 60, '2022-05-29 20:30:00', 0),
(2, 0, 'attente', 1, 1, 10, 60, '2022-05-29 19:10:49', 0),
(3, 0, 'attente', 1, 2, 10, 60, '2022-05-30 12:00:00', 0),
(4, 0, 'attente', 2, 2, 10, 120, '2022-05-31 12:00:00', 0),
(5, 0, 'attente', 2, 2, 10, 60, '2022-05-29 23:00:00', 0),
(7, 0, 'present', 1, 2, 10, 60, '2022-05-30 00:00:00', 0),
(8, 0, 'present', 2, 2, 10, 60, '2022-05-30 00:00:00', 0),
(9, 0, 'present', 3, 2, 10, 60, '2022-05-30 00:00:00', 0);

-- --------------------------------------------------------

--
-- Structure de la table `vehicule`
--

DROP TABLE IF EXISTS `vehicule`;
CREATE TABLE IF NOT EXISTS `vehicule` (
  `plaque` char(9) NOT NULL,
  PRIMARY KEY (`plaque`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Déchargement des données de la table `vehicule`
--

INSERT INTO `vehicule` (`plaque`) VALUES
('78-845-AB');
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;

# --------------------------------------------------------
# Host:                         reimanscar.com.br
# Server version:               5.5.35-cll
# Server OS:                    Linux
# HeidiSQL version:             6.0.0.3603
# Date/time:                    2014-02-13 15:27:34
# --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;

# Dumping database structure for reimansc_sgoa
DROP DATABASE IF EXISTS `reimansc_sgoa`;
CREATE DATABASE IF NOT EXISTS `reimansc_sgoa` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `reimansc_sgoa`;


# Dumping structure for table reimansc_sgoa.cliente
DROP TABLE IF EXISTS `cliente`;
CREATE TABLE IF NOT EXISTS `cliente` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `IdPessoa` int(11) NOT NULL,
  `Obs` varchar(200) DEFAULT NULL,
  `DataCadastro` date NOT NULL,
  `Ativo` tinyint(1) NOT NULL,
  PRIMARY KEY (`Id`),
  KEY `FK_ClientePessoa` (`IdPessoa`),
  CONSTRAINT `FK_Cliente_Pessoa` FOREIGN KEY (`IdPessoa`) REFERENCES `pessoa` (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;

# Dumping data for table reimansc_sgoa.cliente: ~9 rows (approximately)
/*!40000 ALTER TABLE `cliente` DISABLE KEYS */;
INSERT INTO `cliente` (`Id`, `IdPessoa`, `Obs`, `DataCadastro`, `Ativo`) VALUES
	(1, 8, '', '2014-01-19', 1),
	(2, 9, '', '2014-01-26', 1),
	(3, 2, 'TESTE', '2014-01-27', 1),
	(4, 3, '', '2014-01-27', 1),
	(5, 10, '', '2014-01-28', 1),
	(6, 11, 'cooperflux', '2014-01-28', 1),
	(7, 12, 'Serviço Tokio Marine', '2014-01-28', 1),
	(8, 13, 'cliente tokio marina ', '2014-02-05', 1),
	(9, 14, '', '2014-02-13', 1);
/*!40000 ALTER TABLE `cliente` ENABLE KEYS */;


# Dumping structure for table reimansc_sgoa.configemail
DROP TABLE IF EXISTS `configemail`;
CREATE TABLE IF NOT EXISTS `configemail` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `IdentificacaoEmail` varchar(100) DEFAULT NULL,
  `EmailEnvio` varchar(250) DEFAULT NULL,
  `Senha` varchar(2000) DEFAULT NULL,
  `ServidorNecessitaAutenticacao` tinyint(1) DEFAULT NULL,
  `ServidorSMTP` varchar(250) DEFAULT NULL,
  `Porta` int(11) DEFAULT NULL,
  `EmailRecebCliente` varchar(250) DEFAULT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

# Dumping data for table reimansc_sgoa.configemail: ~1 rows (approximately)
/*!40000 ALTER TABLE `configemail` DISABLE KEYS */;
INSERT INTO `configemail` (`Id`, `IdentificacaoEmail`, `EmailEnvio`, `Senha`, `ServidorNecessitaAutenticacao`, `ServidorSMTP`, `Porta`, `EmailRecebCliente`) VALUES
	(1, 'Reiman´s Car - Funilaria e Pintura', 'contato@reimanscar.com.br', 'taxi1010', 1, 'mail.reimanscar.com.br', 587, 'contato@reimanscar.com.br');
/*!40000 ALTER TABLE `configemail` ENABLE KEYS */;


# Dumping structure for table reimansc_sgoa.config_ordemservico
DROP TABLE IF EXISTS `config_ordemservico`;
CREATE TABLE IF NOT EXISTS `config_ordemservico` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `IdEtapaInicial` int(11) DEFAULT NULL,
  `IdEtapaInicialSeguradora` int(11) DEFAULT NULL,
  `IdEtapaFimConcerto` int(11) DEFAULT NULL,
  `IdEtapaCancelamentoConcerto` int(11) DEFAULT NULL,
  `IdEtapaConclusaoOrdemServico` int(11) DEFAULT NULL,
  PRIMARY KEY (`Id`),
  KEY `FK_config_ordemservico_etapa` (`IdEtapaInicial`),
  KEY `FK_config_ordemservico_etapa_2` (`IdEtapaInicialSeguradora`),
  KEY `FK_config_ordemservico_etapa_3` (`IdEtapaFimConcerto`),
  KEY `FK_config_ordemservico_etapa_4` (`IdEtapaCancelamentoConcerto`),
  KEY `FK_config_ordemservico_etapa_5` (`IdEtapaConclusaoOrdemServico`),
  CONSTRAINT `FK_config_ordemservico_etapa` FOREIGN KEY (`IdEtapaInicial`) REFERENCES `etapa` (`Id`),
  CONSTRAINT `FK_config_ordemservico_etapa_2` FOREIGN KEY (`IdEtapaInicialSeguradora`) REFERENCES `etapa` (`Id`),
  CONSTRAINT `FK_config_ordemservico_etapa_3` FOREIGN KEY (`IdEtapaFimConcerto`) REFERENCES `etapa` (`Id`),
  CONSTRAINT `FK_config_ordemservico_etapa_4` FOREIGN KEY (`IdEtapaCancelamentoConcerto`) REFERENCES `etapa` (`Id`),
  CONSTRAINT `FK_config_ordemservico_etapa_5` FOREIGN KEY (`IdEtapaConclusaoOrdemServico`) REFERENCES `etapa` (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

# Dumping data for table reimansc_sgoa.config_ordemservico: ~1 rows (approximately)
/*!40000 ALTER TABLE `config_ordemservico` DISABLE KEYS */;
INSERT INTO `config_ordemservico` (`Id`, `IdEtapaInicial`, `IdEtapaInicialSeguradora`, `IdEtapaFimConcerto`, `IdEtapaCancelamentoConcerto`, `IdEtapaConclusaoOrdemServico`) VALUES
	(1, 9, 5, 1, 3, 2);
/*!40000 ALTER TABLE `config_ordemservico` ENABLE KEYS */;


# Dumping structure for table reimansc_sgoa.cor
DROP TABLE IF EXISTS `cor`;
CREATE TABLE IF NOT EXISTS `cor` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `Descricao` varchar(250) NOT NULL,
  `Ativo` tinyint(1) NOT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

# Dumping data for table reimansc_sgoa.cor: ~10 rows (approximately)
/*!40000 ALTER TABLE `cor` DISABLE KEYS */;
INSERT INTO `cor` (`Id`, `Descricao`, `Ativo`) VALUES
	(1, 'Prata', 1),
	(2, 'Preto', 1),
	(3, 'Branco', 1),
	(4, 'Cinza', 1),
	(5, 'Azul', 1),
	(6, 'Vermelho', 1),
	(7, 'Marrom/Bege', 1),
	(8, 'Verde', 1),
	(9, 'Amarelo/Dourado', 1),
	(10, 'Laranja Taxi', 1);
/*!40000 ALTER TABLE `cor` ENABLE KEYS */;


# Dumping structure for table reimansc_sgoa.etapa
DROP TABLE IF EXISTS `etapa`;
CREATE TABLE IF NOT EXISTS `etapa` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `Descricao` varchar(250) NOT NULL,
  `Ativo` tinyint(1) NOT NULL,
  `IdSetor` int(11) NOT NULL,
  `IdTipoServico` int(11) NOT NULL,
  `VisivelWebSite` tinyint(4) NOT NULL,
  `EnviaEmailInicio` tinyint(4) NOT NULL,
  `EnviaEmailFim` tinyint(4) NOT NULL,
  PRIMARY KEY (`Id`),
  KEY `FK_etapa_setor` (`IdSetor`),
  KEY `FK_etapa_tiposervico` (`IdTipoServico`),
  CONSTRAINT `FK_etapa_setor` FOREIGN KEY (`IdSetor`) REFERENCES `setor` (`Id`),
  CONSTRAINT `FK_etapa_tiposervico` FOREIGN KEY (`IdTipoServico`) REFERENCES `tiposervico` (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

# Dumping data for table reimansc_sgoa.etapa: ~14 rows (approximately)
/*!40000 ALTER TABLE `etapa` DISABLE KEYS */;
INSERT INTO `etapa` (`Id`, `Descricao`, `Ativo`, `IdSetor`, `IdTipoServico`, `VisivelWebSite`, `EnviaEmailInicio`, `EnviaEmailFim`) VALUES
	(1, 'Reparos Finalizados', 1, 1, 1, 1, 1, 0),
	(2, 'O.S. Concluída', 1, 1, 1, 1, 0, 0),
	(3, 'O.S. Cancelada', 1, 1, 1, 1, 1, 0),
	(4, 'Serviços Funilaria', 1, 3, 2, 1, 1, 0),
	(5, 'Aguardando Liberação', 1, 1, 1, 1, 1, 1),
	(6, 'Serviços Preparação', 1, 2, 3, 1, 0, 0),
	(7, 'Aguardando pagamento', 1, 1, 1, 0, 0, 0),
	(8, 'Reparos Iniciados', 1, 3, 2, 1, 1, 0),
	(9, 'Serviços Desmontagem', 1, 3, 2, 1, 0, 0),
	(10, 'Serviços Lavagem', 1, 7, 2, 1, 0, 0),
	(11, 'Serviços Pintura', 1, 2, 3, 1, 0, 0),
	(12, 'Serviços Montagem', 1, 3, 2, 1, 0, 0),
	(13, 'Serviços Polimento', 1, 2, 3, 1, 0, 0),
	(14, 'Veículo Pronto Entrega', 1, 1, 1, 1, 1, 0);
/*!40000 ALTER TABLE `etapa` ENABLE KEYS */;


# Dumping structure for table reimansc_sgoa.funcionario
DROP TABLE IF EXISTS `funcionario`;
CREATE TABLE IF NOT EXISTS `funcionario` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `IdPessoa` int(11) NOT NULL,
  `Matricula` int(11) NOT NULL,
  `Obs` varchar(200) DEFAULT NULL,
  `DataCadastro` date NOT NULL,
  `Ativo` tinyint(1) NOT NULL,
  `IdSetor` int(11) NOT NULL,
  `IdProfissao` int(11) NOT NULL,
  `IdPerfil` int(11) DEFAULT NULL,
  `Senha` varchar(80) DEFAULT NULL,
  PRIMARY KEY (`Id`),
  KEY `FK_ClientePessoa` (`IdPessoa`),
  KEY `FK_funcionario_setor` (`IdSetor`),
  KEY `FK_funcionario_profissao` (`IdProfissao`),
  KEY `FK_funcionario_perfil` (`IdPerfil`),
  CONSTRAINT `FK_funcionario_perfil` FOREIGN KEY (`IdPerfil`) REFERENCES `perfil` (`Id`),
  CONSTRAINT `FK_funcionario_profissao` FOREIGN KEY (`IdProfissao`) REFERENCES `profissao` (`Id`),
  CONSTRAINT `FK_funcionario_setor` FOREIGN KEY (`IdSetor`) REFERENCES `setor` (`Id`),
  CONSTRAINT `funcionario_ibfk_1` FOREIGN KEY (`IdPessoa`) REFERENCES `pessoa` (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

# Dumping data for table reimansc_sgoa.funcionario: ~4 rows (approximately)
/*!40000 ALTER TABLE `funcionario` DISABLE KEYS */;
INSERT INTO `funcionario` (`Id`, `IdPessoa`, `Matricula`, `Obs`, `DataCadastro`, `Ativo`, `IdSetor`, `IdProfissao`, `IdPerfil`, `Senha`) VALUES
	(1, 8, 20131, NULL, '2013-11-21', 1, 1, 1, 1, '565d9a3631f5940b9facd0f153b5f569'),
	(2, 2, 20132, '', '2013-12-24', 1, 1, 2, 1, '1e48c4420b7073bc11916c6c1de226bb'),
	(3, 3, 20141, '', '2014-01-13', 1, 1, 4, 1, '94a63c28036769847b9597e0dd7abb1c');
/*!40000 ALTER TABLE `funcionario` ENABLE KEYS */;


# Dumping structure for table reimansc_sgoa.marca
DROP TABLE IF EXISTS `marca`;
CREATE TABLE IF NOT EXISTS `marca` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `Descricao` varchar(250) NOT NULL,
  `Ativo` tinyint(1) NOT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8;

# Dumping data for table reimansc_sgoa.marca: ~16 rows (approximately)
/*!40000 ALTER TABLE `marca` DISABLE KEYS */;
INSERT INTO `marca` (`Id`, `Descricao`, `Ativo`) VALUES
	(1, 'Audi', 1),
	(2, 'Chevrolet', 1),
	(3, 'Citroën', 1),
	(4, 'Fiat', 1),
	(5, 'Ford', 1),
	(6, 'Honda', 1),
	(7, 'Hyundai', 1),
	(8, 'Jac Motors', 1),
	(9, 'Mercedes-Benz', 1),
	(10, 'Mitsubishi', 1),
	(11, 'Nissan', 1),
	(12, 'Peugeot', 1),
	(13, 'Renault', 1),
	(14, 'Suzuki', 1),
	(15, 'Toyota', 1),
	(16, 'Volkswagen', 1);
/*!40000 ALTER TABLE `marca` ENABLE KEYS */;


# Dumping structure for table reimansc_sgoa.modelo
DROP TABLE IF EXISTS `modelo`;
CREATE TABLE IF NOT EXISTS `modelo` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `IdMarca` int(11) NOT NULL,
  `Descricao` varchar(250) NOT NULL,
  `Ativo` tinyint(1) NOT NULL,
  PRIMARY KEY (`Id`),
  KEY `ModeloFabricante_FKIndex1` (`IdMarca`),
  KEY `Descricao` (`Descricao`),
  CONSTRAINT `Fk_Modelo_Marca` FOREIGN KEY (`IdMarca`) REFERENCES `marca` (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=47 DEFAULT CHARSET=utf8;

# Dumping data for table reimansc_sgoa.modelo: ~46 rows (approximately)
/*!40000 ALTER TABLE `modelo` DISABLE KEYS */;
INSERT INTO `modelo` (`Id`, `IdMarca`, `Descricao`, `Ativo`) VALUES
	(1, 1, 'A3', 1),
	(2, 2, 'Corsa Hatch', 1),
	(3, 2, 'Corsa Sedan', 1),
	(4, 2, 'Prisma', 1),
	(5, 7, 'HB20', 1),
	(6, 16, 'Gol - G5', 1),
	(7, 16, 'Voyage', 1),
	(8, 13, 'Logan', 1),
	(9, 13, 'Sandero', 1),
	(10, 12, '206', 1),
	(11, 12, '207', 1),
	(12, 12, '308', 1),
	(13, 2, 'Classic', 1),
	(14, 4, 'Mille', 1),
	(15, 4, 'Novo Uno', 1),
	(16, 4, 'Palio', 1),
	(17, 4, 'Siena', 1),
	(18, 4, 'Strada', 1),
	(19, 4, 'Idea', 1),
	(20, 4, 'Dobló', 1),
	(21, 4, 'Novo Palio', 1),
	(22, 4, 'Palio Weekend', 1),
	(23, 16, 'Fox', 1),
	(24, 16, 'Polo', 1),
	(25, 16, 'Golf', 1),
	(26, 16, 'CrossFox', 1),
	(27, 16, 'Saveiro', 1),
	(28, 5, 'New Fiesta Hatch', 1),
	(29, 5, 'New Fiesta Sedan', 1),
	(30, 5, 'Fiesta Sedan', 1),
	(31, 5, 'Fiesta Hatch', 1),
	(32, 5, 'Focus', 1),
	(33, 2, 'Agile', 1),
	(34, 2, 'Celta', 1),
	(35, 2, 'Cobalt', 1),
	(36, 2, 'Cruze', 1),
	(37, 2, 'Onix', 1),
	(38, 2, 'Sonic', 1),
	(39, 2, 'Montana', 1),
	(40, 2, 'Spin', 1),
	(41, 13, 'Duster', 1),
	(42, 13, 'Clio', 1),
	(43, 13, 'Sandero Stepway', 1),
	(44, 13, 'Master', 1),
	(45, 16, 'Santana 2000', 1),
	(46, 5, 'Ka Flex', 1);
/*!40000 ALTER TABLE `modelo` ENABLE KEYS */;


# Dumping structure for table reimansc_sgoa.orcamento
DROP TABLE IF EXISTS `orcamento`;
CREATE TABLE IF NOT EXISTS `orcamento` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `Ano` int(11) NOT NULL,
  `Numero` int(11) NOT NULL DEFAULT '0',
  `DataCadastro` datetime NOT NULL,
  `IdFuncionarioCadastro` int(11) DEFAULT '0',
  `IdCliente` int(11) NOT NULL DEFAULT '0',
  `IdVeiculo` int(11) NOT NULL DEFAULT '0',
  `Situacao` char(1) NOT NULL,
  `IdSeguradora` int(11) DEFAULT NULL,
  `Terceiro` tinyint(1) NOT NULL,
  `Obs` varchar(200) DEFAULT NULL,
  `ValorPecas` double(10,2) DEFAULT NULL,
  `ValorAdicional` double(8,2) DEFAULT NULL,
  `ValorDesconto` double(8,2) DEFAULT NULL,
  `ValorTotal` double(10,2) DEFAULT NULL,
  `DataCancelamento` datetime DEFAULT NULL,
  `MotivoCancelamento` varchar(250) DEFAULT NULL,
  `IdFuncCancelamento` int(11) DEFAULT NULL,
  `IdOrdemServico` int(11) DEFAULT NULL,
  PRIMARY KEY (`Id`),
  KEY `FK_orcamento_cliente` (`IdCliente`),
  KEY `FK_orcamento_veiculo` (`IdVeiculo`),
  KEY `FK_orcamento_funcionario_2` (`IdFuncCancelamento`),
  KEY `FK_orcamento_seguradora` (`IdSeguradora`),
  KEY `FK_orcamento_funcionario` (`IdFuncionarioCadastro`),
  KEY `FK_orcamento_ordemservico` (`IdOrdemServico`),
  CONSTRAINT `FK_orcamento_cliente` FOREIGN KEY (`IdCliente`) REFERENCES `cliente` (`Id`),
  CONSTRAINT `FK_orcamento_funcionario` FOREIGN KEY (`IdFuncionarioCadastro`) REFERENCES `funcionario` (`Id`),
  CONSTRAINT `FK_orcamento_funcionario_2` FOREIGN KEY (`IdFuncCancelamento`) REFERENCES `funcionario` (`Id`),
  CONSTRAINT `FK_orcamento_ordemservico` FOREIGN KEY (`IdOrdemServico`) REFERENCES `ordemservico` (`Id`),
  CONSTRAINT `FK_orcamento_seguradora` FOREIGN KEY (`IdSeguradora`) REFERENCES `seguradora` (`Id`),
  CONSTRAINT `FK_orcamento_veiculo` FOREIGN KEY (`IdVeiculo`) REFERENCES `veiculo` (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8;

# Dumping data for table reimansc_sgoa.orcamento: ~10 rows (approximately)
/*!40000 ALTER TABLE `orcamento` DISABLE KEYS */;
/*!40000 ALTER TABLE `orcamento` ENABLE KEYS */;


# Dumping structure for table reimansc_sgoa.orcamento_anexo
DROP TABLE IF EXISTS `orcamento_anexo`;
CREATE TABLE IF NOT EXISTS `orcamento_anexo` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `IdOrcamento` int(11) NOT NULL,
  `NomeArquivo` varchar(250) NOT NULL,
  `Imagem` mediumblob NOT NULL,
  PRIMARY KEY (`Id`),
  KEY `FK_orcamento_anexo_orcamento` (`IdOrcamento`),
  CONSTRAINT `FK_orcamento_anexo_orcamento` FOREIGN KEY (`IdOrcamento`) REFERENCES `orcamento` (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

# Dumping data for table reimansc_sgoa.orcamento_anexo: ~0 rows (approximately)
/*!40000 ALTER TABLE `orcamento_anexo` DISABLE KEYS */;
/*!40000 ALTER TABLE `orcamento_anexo` ENABLE KEYS */;


# Dumping structure for table reimansc_sgoa.orcamento_tiposervico
DROP TABLE IF EXISTS `orcamento_tiposervico`;
CREATE TABLE IF NOT EXISTS `orcamento_tiposervico` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `IdOrcamento` int(11) NOT NULL,
  `IdTipoServico` int(11) NOT NULL,
  `Horas` decimal(5,2) NOT NULL,
  `ValorHora` double(8,2) NOT NULL,
  `Desconto` double(8,2) DEFAULT NULL,
  `Total` double(8,2) NOT NULL,
  PRIMARY KEY (`Id`),
  KEY `FK__tiposervico` (`IdTipoServico`),
  KEY `FK_orcamento_tiposervico_orcamento` (`IdOrcamento`),
  CONSTRAINT `FK_orcamento_tiposervico_orcamento` FOREIGN KEY (`IdOrcamento`) REFERENCES `orcamento` (`Id`),
  CONSTRAINT `FK__tiposervico` FOREIGN KEY (`IdTipoServico`) REFERENCES `tiposervico` (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

# Dumping data for table reimansc_sgoa.orcamento_tiposervico: ~0 rows (approximately)
/*!40000 ALTER TABLE `orcamento_tiposervico` DISABLE KEYS */;
/*!40000 ALTER TABLE `orcamento_tiposervico` ENABLE KEYS */;


# Dumping structure for table reimansc_sgoa.ordemservico
DROP TABLE IF EXISTS `ordemservico`;
CREATE TABLE IF NOT EXISTS `ordemservico` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `IdOrcamento` int(11) NOT NULL,
  `Situacao` char(1) NOT NULL,
  `DataAprovacao` datetime NOT NULL,
  `IdFuncAprovacao` int(11) NOT NULL,
  `Obs` varchar(250) DEFAULT NULL,
  `IdEtapaAtual` int(11) DEFAULT NULL,
  `DataCancelamento` datetime DEFAULT NULL,
  `MotivoCancelamento` varchar(250) DEFAULT NULL,
  `IdFuncCancelamento` int(11) DEFAULT NULL,
  `DataPrevEntrega` datetime DEFAULT NULL,
  PRIMARY KEY (`Id`),
  KEY `FK_ordemservico_orcamento` (`IdOrcamento`),
  KEY `FK_ordemservico_funcionario` (`IdFuncAprovacao`),
  KEY `FK_ordemservico_etapa` (`IdEtapaAtual`),
  KEY `FK_ordemservico_funcionario_2` (`IdFuncCancelamento`),
  CONSTRAINT `FK_ordemservico_etapa` FOREIGN KEY (`IdEtapaAtual`) REFERENCES `ordemservico_etapa` (`Id`),
  CONSTRAINT `FK_ordemservico_funcionario` FOREIGN KEY (`IdFuncAprovacao`) REFERENCES `funcionario` (`Id`),
  CONSTRAINT `FK_ordemservico_funcionario_2` FOREIGN KEY (`IdFuncCancelamento`) REFERENCES `funcionario` (`Id`),
  CONSTRAINT `FK_ordemservico_orcamento` FOREIGN KEY (`IdOrcamento`) REFERENCES `orcamento` (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

# Dumping data for table reimansc_sgoa.ordemservico: ~9 rows (approximately)
/*!40000 ALTER TABLE `ordemservico` DISABLE KEYS */;
/*!40000 ALTER TABLE `ordemservico` ENABLE KEYS */;


# Dumping structure for table reimansc_sgoa.ordemservico_etapa
DROP TABLE IF EXISTS `ordemservico_etapa`;
CREATE TABLE IF NOT EXISTS `ordemservico_etapa` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `DataCadastro` datetime NOT NULL,
  `IdOrdemServico` int(11) NOT NULL,
  `IdEtapa` int(11) NOT NULL,
  `DataEntrada` datetime DEFAULT NULL,
  `Situacao` char(1) NOT NULL,
  `DataSaida` datetime DEFAULT NULL,
  `IdFuncionario` int(11) DEFAULT NULL,
  `HorasTrabalhadas` decimal(8,2) DEFAULT NULL,
  PRIMARY KEY (`Id`),
  KEY `FK_ordemservico_etapa_etapa` (`IdEtapa`),
  KEY `FK_ordemservico_etapa_funcionario` (`IdFuncionario`),
  KEY `FK_ordemservico_etapa_ordemservico` (`IdOrdemServico`),
  CONSTRAINT `FK_ordemservico_etapa_etapa` FOREIGN KEY (`IdEtapa`) REFERENCES `etapa` (`Id`),
  CONSTRAINT `FK_ordemservico_etapa_funcionario` FOREIGN KEY (`IdFuncionario`) REFERENCES `funcionario` (`Id`),
  CONSTRAINT `FK_ordemservico_etapa_ordemservico` FOREIGN KEY (`IdOrdemServico`) REFERENCES `ordemservico` (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=46 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

# Dumping data for table reimansc_sgoa.ordemservico_etapa: ~41 rows (approximately)
/*!40000 ALTER TABLE `ordemservico_etapa` DISABLE KEYS */;
/*!40000 ALTER TABLE `ordemservico_etapa` ENABLE KEYS */;


# Dumping structure for table reimansc_sgoa.ordemservico_evento
DROP TABLE IF EXISTS `ordemservico_evento`;
CREATE TABLE IF NOT EXISTS `ordemservico_evento` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `IdOrdemServicoEtapa` int(11) NOT NULL,
  `DataOcorrencia` datetime NOT NULL,
  `TipoEvento` char(1) NOT NULL,
  `IdFuncionario` int(11) NOT NULL,
  `Descricao` varchar(250) NOT NULL,
  `Ativo` tinyint(1) NOT NULL,
  `DataInicioParada` date DEFAULT NULL,
  `NotificaViaEmail` tinyint(4) NOT NULL DEFAULT '0',
  PRIMARY KEY (`Id`),
  KEY `FK_ordemservico_etapa_funcionario` (`IdFuncionario`),
  KEY `FK_ordemservico_evento_ordemservico_etapa` (`IdOrdemServicoEtapa`),
  CONSTRAINT `FK_ordemservico_evento_ordemservico_etapa` FOREIGN KEY (`IdOrdemServicoEtapa`) REFERENCES `ordemservico_etapa` (`Id`),
  CONSTRAINT `ordemservico_evento_ibfk_1` FOREIGN KEY (`IdFuncionario`) REFERENCES `funcionario` (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=96 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

# Dumping data for table reimansc_sgoa.ordemservico_evento: ~91 rows (approximately)
/*!40000 ALTER TABLE `ordemservico_evento` DISABLE KEYS */;
/*!40000 ALTER TABLE `ordemservico_evento` ENABLE KEYS */;


# Dumping structure for table reimansc_sgoa.ordemservico_foto
DROP TABLE IF EXISTS `ordemservico_foto`;
CREATE TABLE IF NOT EXISTS `ordemservico_foto` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `IdOrdemServicoEvento` int(11) NOT NULL,
  `NomeArquivo` varchar(250) NOT NULL,
  `Imagem` mediumblob NOT NULL,
  PRIMARY KEY (`Id`),
  KEY `FK_ordemservico_anexo_ordemservico_etapa` (`IdOrdemServicoEvento`),
  CONSTRAINT `FK_ordemservico_anexo_ordemservico_etapa` FOREIGN KEY (`IdOrdemServicoEvento`) REFERENCES `ordemservico_evento` (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

# Dumping data for table reimansc_sgoa.ordemservico_foto: ~0 rows (approximately)
/*!40000 ALTER TABLE `ordemservico_foto` DISABLE KEYS */;
/*!40000 ALTER TABLE `ordemservico_foto` ENABLE KEYS */;


# Dumping structure for table reimansc_sgoa.perfil
DROP TABLE IF EXISTS `perfil`;
CREATE TABLE IF NOT EXISTS `perfil` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `Descricao` varchar(250) NOT NULL,
  `Ativo` tinyint(1) NOT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

# Dumping data for table reimansc_sgoa.perfil: ~2 rows (approximately)
/*!40000 ALTER TABLE `perfil` DISABLE KEYS */;
INSERT INTO `perfil` (`Id`, `Descricao`, `Ativo`) VALUES
	(1, 'Gerente', 1),
	(2, 'Operacional', 1);
/*!40000 ALTER TABLE `perfil` ENABLE KEYS */;


# Dumping structure for table reimansc_sgoa.pessoa
DROP TABLE IF EXISTS `pessoa`;
CREATE TABLE IF NOT EXISTS `pessoa` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `Nome` varchar(200) NOT NULL,
  `Tipo` char(1) NOT NULL,
  `Email` varchar(250) DEFAULT NULL,
  `TelefonePrimario` varchar(15) DEFAULT NULL,
  `TelefoneSecundario` varchar(15) DEFAULT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8;

# Dumping data for table reimansc_sgoa.pessoa: ~10 rows (approximately)
/*!40000 ALTER TABLE `pessoa` DISABLE KEYS */;
INSERT INTO `pessoa` (`Id`, `Nome`, `Tipo`, `Email`, `TelefonePrimario`, `TelefoneSecundario`) VALUES
	(2, 'Cesar Carlos Reimann', 'F', 'contato@reimanscar.com.br', '(41) 9912-3031', ''),
	(3, 'Marisa Reimann', 'F', 'marisareimann@hotmail.com', '(41) 3277-3031', ''),
	(8, 'Wellingthon Reimann', 'F', 'wreimann@hotmail.com', '(41) 8804-6378', '(41) 8830-1623'),
	(9, 'Associação Cotista Radio Taxi Seria', 'J', 'valdircorrea@taxiseria.com.br', '(41) 3346-4646', '(41) 9730-0006'),
	(10, 'Associação Radio Taxi Faixa Vermelha', 'J', 'radiotaxi@taxifaixavermelha.com.br', '(41) 3362-6262', ''),
	(11, 'P K Service Ltda', 'J', 'manutencao@cooperflux.com.br', '(41) 3018-1414', ''),
	(12, 'Paulo Roberto Gonçalves', 'F', 'pauloacbg@yahoo.com.br', '(41) 9962-1934', '(41) 3277-1494'),
	(13, 'Jonathas Dornelles Dos Santos', 'F', 'jbsilva70@hotmail.com', '(41) 9630-9650', '(41) 3331-5421'),
	(14, 'Fogo e Lazer Ltda', 'J', 'cenira@fogoelazer.com.br', '(41) 9241-6645', '');
/*!40000 ALTER TABLE `pessoa` ENABLE KEYS */;


# Dumping structure for table reimansc_sgoa.pessoaendereco
DROP TABLE IF EXISTS `pessoaendereco`;
CREATE TABLE IF NOT EXISTS `pessoaendereco` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `Logradouro` varchar(250) DEFAULT NULL,
  `IdPessoa` int(11) NOT NULL,
  `Predical` int(11) DEFAULT NULL,
  `CEP` char(12) DEFAULT NULL,
  `Bairro` varchar(200) DEFAULT NULL,
  `Municipio` varchar(200) DEFAULT NULL,
  `UF` char(2) DEFAULT NULL,
  `Complemento` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`Id`),
  KEY `FK_pessoaendereco_pessoa` (`IdPessoa`),
  CONSTRAINT `FK_pessoaendereco_pessoa` FOREIGN KEY (`IdPessoa`) REFERENCES `pessoa` (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;

# Dumping data for table reimansc_sgoa.pessoaendereco: ~9 rows (approximately)
/*!40000 ALTER TABLE `pessoaendereco` DISABLE KEYS */;
INSERT INTO `pessoaendereco` (`Id`, `Logradouro`, `IdPessoa`, `Predical`, `CEP`, `Bairro`, `Municipio`, `UF`, `Complemento`) VALUES
	(1, 'Rodolfo Bernardelli', 8, 711, '81580010', 'Uberaba', 'Curitiba', 'PR', ''),
	(3, '', 3, NULL, '', '', '', '', ''),
	(4, 'RODOLFO  BERNARDELLI  ', 2, 711, '81528001', 'UBERABA', 'CURITIBA', 'PR', ''),
	(5, 'AV PRES. WESCELAU BRAZ', 9, 3430, '81010000', 'LINDOIA', 'CURITIBA', 'PR', ''),
	(6, 'RUA DR. GULIN', 10, 1699, '', 'JARDIM SOCIAL', 'CURITIBA', 'PR', ''),
	(7, 'Teixeira de Lara', 11, 33, '', 'Jardim São Lucas', 'Colombo', 'pr', ''),
	(8, '', 12, NULL, '', '', 'curitiba', 'pr', ''),
	(9, '', 13, NULL, '', '', 'Curitiba', 'Pr', ''),
	(10, '', 14, NULL, '', '', 'Curitiba', 'Pr', '');
/*!40000 ALTER TABLE `pessoaendereco` ENABLE KEYS */;


# Dumping structure for table reimansc_sgoa.pessoafisica
DROP TABLE IF EXISTS `pessoafisica`;
CREATE TABLE IF NOT EXISTS `pessoafisica` (
  `Id` int(11) NOT NULL,
  `CPF` char(15) NOT NULL,
  `Sexo` char(1) NOT NULL,
  `DataNascimento` date DEFAULT NULL,
  PRIMARY KEY (`Id`),
  KEY `FK_Pessoa` (`Id`),
  CONSTRAINT `FK_Fisica_Pessoa` FOREIGN KEY (`Id`) REFERENCES `pessoa` (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

# Dumping data for table reimansc_sgoa.pessoafisica: ~6 rows (approximately)
/*!40000 ALTER TABLE `pessoafisica` DISABLE KEYS */;
INSERT INTO `pessoafisica` (`Id`, `CPF`, `Sexo`, `DataNascimento`) VALUES
	(2, '58669639904', 'M', '1958-09-03'),
	(3, '00379929961', 'F', '1964-08-08'),
	(8, '05809243940', 'M', '1987-09-12'),
	(12, '17175038968', 'M', NULL),
	(13, '04488665950', 'M', NULL);
/*!40000 ALTER TABLE `pessoafisica` ENABLE KEYS */;


# Dumping structure for table reimansc_sgoa.pessoajuridica
DROP TABLE IF EXISTS `pessoajuridica`;
CREATE TABLE IF NOT EXISTS `pessoajuridica` (
  `Id` int(11) NOT NULL,
  `CNPJ` char(18) NOT NULL,
  PRIMARY KEY (`Id`),
  KEY `FK_Pessoa` (`Id`),
  CONSTRAINT `FK_Juridica_Pessoa` FOREIGN KEY (`Id`) REFERENCES `pessoa` (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

# Dumping data for table reimansc_sgoa.pessoajuridica: ~4 rows (approximately)
/*!40000 ALTER TABLE `pessoajuridica` DISABLE KEYS */;
INSERT INTO `pessoajuridica` (`Id`, `CNPJ`) VALUES
	(9, '78713419000188'),
	(10, '77522225000132'),
	(11, '04126418000109'),
	(14, '02326542000184');
/*!40000 ALTER TABLE `pessoajuridica` ENABLE KEYS */;


# Dumping structure for table reimansc_sgoa.profissao
DROP TABLE IF EXISTS `profissao`;
CREATE TABLE IF NOT EXISTS `profissao` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `Descricao` varchar(250) NOT NULL,
  `Ativo` tinyint(1) NOT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

# Dumping data for table reimansc_sgoa.profissao: ~8 rows (approximately)
/*!40000 ALTER TABLE `profissao` DISABLE KEYS */;
INSERT INTO `profissao` (`Id`, `Descricao`, `Ativo`) VALUES
	(1, 'Analista de Sistemas', 1),
	(2, 'Gerente', 1),
	(3, 'Pintor', 1),
	(4, 'Administrador', 1),
	(5, 'Latoeiro', 1),
	(6, 'Auxiliar de Pintura', 1),
	(7, 'Ajudante', 1),
	(8, 'Eletricista', 1);
/*!40000 ALTER TABLE `profissao` ENABLE KEYS */;


# Dumping structure for table reimansc_sgoa.seguradora
DROP TABLE IF EXISTS `seguradora`;
CREATE TABLE IF NOT EXISTS `seguradora` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `Descricao` varchar(250) NOT NULL,
  `Ativo` tinyint(1) NOT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

# Dumping data for table reimansc_sgoa.seguradora: ~14 rows (approximately)
/*!40000 ALTER TABLE `seguradora` DISABLE KEYS */;
INSERT INTO `seguradora` (`Id`, `Descricao`, `Ativo`) VALUES
	(1, 'AZUL SEGUROS', 1),
	(2, 'BRADESCO SEGUROS', 1),
	(3, 'CAIXA SEGURADORA', 1),
	(4, 'HDI SEGUROS', 1),
	(5, 'ITAÚ SEGUROS', 1),
	(6, 'J. MALUCELLI SEGURADORA', 1),
	(7, 'LIBERTY SEGUROS', 1),
	(8, 'MARITIMA SEGUROS', 1),
	(9, 'MAPFRE SEGUROS', 1),
	(10, 'PORTO SEGURO CIA DE SEGUROS GERAIS', 1),
	(11, 'SANTANDER SEGUROS ', 1),
	(12, 'SUL AMERICA COMPANHIA NACIONAL DE SEGUROS', 1),
	(13, 'TOKIO MARINE BRASIL SEGURADORA', 1),
	(14, 'ZURICH BRASIL SEGUROS', 1);
/*!40000 ALTER TABLE `seguradora` ENABLE KEYS */;


# Dumping structure for table reimansc_sgoa.setor
DROP TABLE IF EXISTS `setor`;
CREATE TABLE IF NOT EXISTS `setor` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `Descricao` varchar(250) NOT NULL,
  `Ativo` tinyint(1) NOT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

# Dumping data for table reimansc_sgoa.setor: ~7 rows (approximately)
/*!40000 ALTER TABLE `setor` DISABLE KEYS */;
INSERT INTO `setor` (`Id`, `Descricao`, `Ativo`) VALUES
	(1, 'Setor Administrativo', 1),
	(2, 'Setor de Pintura', 1),
	(3, 'Setor de Funilaria', 1),
	(4, 'Setor de Mecânica', 1),
	(5, 'Setor Montagem ', 1),
	(6, 'Setor de Estoque', 1),
	(7, 'Setor Acabamento Finais', 1);
/*!40000 ALTER TABLE `setor` ENABLE KEYS */;


# Dumping structure for table reimansc_sgoa.tiposervico
DROP TABLE IF EXISTS `tiposervico`;
CREATE TABLE IF NOT EXISTS `tiposervico` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `Descricao` varchar(250) NOT NULL,
  `Ativo` tinyint(1) NOT NULL,
  `ValorHoraPadrao` decimal(6,2) NOT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

# Dumping data for table reimansc_sgoa.tiposervico: ~10 rows (approximately)
/*!40000 ALTER TABLE `tiposervico` DISABLE KEYS */;
INSERT INTO `tiposervico` (`Id`, `Descricao`, `Ativo`, `ValorHoraPadrao`) VALUES
	(1, 'Administração', 1, 29.00),
	(2, 'Funilaria', 1, 29.00),
	(3, 'Pintura', 1, 35.00),
	(4, 'Mecânica', 1, 29.00),
	(5, 'Eletricista', 1, 29.00);
/*!40000 ALTER TABLE `tiposervico` ENABLE KEYS */;


# Dumping structure for table reimansc_sgoa.veiculo
DROP TABLE IF EXISTS `veiculo`;
CREATE TABLE IF NOT EXISTS `veiculo` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `IdPessoa` int(11) DEFAULT NULL,
  `Placa` char(7) NOT NULL,
  `IdModelo` int(11) NOT NULL,
  `IdCor` int(11) NOT NULL,
  `AnoFabricacao` int(11) NOT NULL,
  `AnoModelo` int(11) NOT NULL,
  `Ativo` tinyint(1) NOT NULL,
  PRIMARY KEY (`Id`),
  KEY `FK_ClientePessoa` (`IdPessoa`),
  KEY `veiculo_modelo` (`IdModelo`),
  KEY `veiculo_cor` (`IdCor`),
  CONSTRAINT `veiculo_cor` FOREIGN KEY (`IdCor`) REFERENCES `cor` (`Id`),
  CONSTRAINT `veiculo_modelo` FOREIGN KEY (`IdModelo`) REFERENCES `modelo` (`Id`),
  CONSTRAINT `veiculo_pessoa` FOREIGN KEY (`IdPessoa`) REFERENCES `pessoa` (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

# Dumping data for table reimansc_sgoa.veiculo: ~10 rows (approximately)
/*!40000 ALTER TABLE `veiculo` DISABLE KEYS */;
INSERT INTO `veiculo` (`Id`, `IdPessoa`, `Placa`, `IdModelo`, `IdCor`, `AnoFabricacao`, `AnoModelo`, `Ativo`) VALUES
	(1, 8, 'ato3804', 2, 2, 2011, 2011, 1),
	(3, 9, 'AXG7484', 35, 3, 2013, 2013, 1),
	(4, 2, 'ANZ2734', 31, 1, 2006, 2006, 1),
	(5, 3, 'ATE1049', 1, 3, 2013, 2013, 1),
	(6, 11, 'avm4799', 44, 3, 2011, 2011, 1),
	(7, 10, 'avm4799', 44, 3, 2011, 2011, 1),
	(8, 12, 'awp1550', 45, 5, 1998, 1999, 1),
	(9, 13, 'lzc8761', 6, 5, 1997, 1998, 1),
	(10, 14, 'AWZ0488', 46, 3, 2013, 2013, 1);
/*!40000 ALTER TABLE `veiculo` ENABLE KEYS */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;

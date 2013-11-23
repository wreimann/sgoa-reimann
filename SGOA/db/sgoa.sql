# --------------------------------------------------------
# Host:                         127.0.0.1
# Server version:               5.5.28
# Server OS:                    Win64
# HeidiSQL version:             6.0.0.3603
# Date/time:                    2013-11-23 13:24:50
# --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;

# Dumping database structure for sgoa2
DROP DATABASE IF EXISTS `sgoa2`;
CREATE DATABASE IF NOT EXISTS `sgoa2` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `sgoa2`;


# Dumping structure for table sgoa2.cliente
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

# Dumping data for table sgoa2.cliente: ~0 rows (approximately)
DELETE FROM `cliente`;
/*!40000 ALTER TABLE `cliente` DISABLE KEYS */;
/*!40000 ALTER TABLE `cliente` ENABLE KEYS */;


# Dumping structure for table sgoa2.configemail
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

# Dumping data for table sgoa2.configemail: ~1 rows (approximately)
DELETE FROM `configemail`;
/*!40000 ALTER TABLE `configemail` DISABLE KEYS */;
INSERT INTO `configemail` (`Id`, `IdentificacaoEmail`, `EmailEnvio`, `Senha`, `ServidorNecessitaAutenticacao`, `ServidorSMTP`, `Porta`, `EmailRecebCliente`) VALUES
	(1, 'Reiman´s Car - Funilaria e Pintura', 'pe.reimann@gmail.com', '$gremio2013', 1, 'smtp.gmail.com', 587, 'wreimann@hotmail.com');
/*!40000 ALTER TABLE `configemail` ENABLE KEYS */;


# Dumping structure for table sgoa2.config_ordemservico
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

# Dumping data for table sgoa2.config_ordemservico: ~1 rows (approximately)
DELETE FROM `config_ordemservico`;
/*!40000 ALTER TABLE `config_ordemservico` DISABLE KEYS */;
INSERT INTO `config_ordemservico` (`Id`, `IdEtapaInicial`, `IdEtapaInicialSeguradora`, `IdEtapaFimConcerto`, `IdEtapaCancelamentoConcerto`, `IdEtapaConclusaoOrdemServico`) VALUES
	(1, NULL, NULL, 1, 3, 2);
/*!40000 ALTER TABLE `config_ordemservico` ENABLE KEYS */;


# Dumping structure for table sgoa2.cor
DROP TABLE IF EXISTS `cor`;
CREATE TABLE IF NOT EXISTS `cor` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `Descricao` varchar(250) NOT NULL,
  `Ativo` tinyint(1) NOT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

# Dumping data for table sgoa2.cor: ~9 rows (approximately)
DELETE FROM `cor`;
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
	(9, 'Amarelo/Dourado', 1);
/*!40000 ALTER TABLE `cor` ENABLE KEYS */;


# Dumping structure for table sgoa2.etapa
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
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

# Dumping data for table sgoa2.etapa: ~3 rows (approximately)
DELETE FROM `etapa`;
/*!40000 ALTER TABLE `etapa` DISABLE KEYS */;
INSERT INTO `etapa` (`Id`, `Descricao`, `Ativo`, `IdSetor`, `IdTipoServico`, `VisivelWebSite`, `EnviaEmailInicio`, `EnviaEmailFim`) VALUES
	(1, 'Reparos finalizado', 1, 1, 1, 1, 1, 0),
	(2, 'O.S. Concluída', 1, 1, 1, 1, 0, 0),
	(3, 'O.S. Cancelada', 1, 1, 1, 1, 1, 0);
/*!40000 ALTER TABLE `etapa` ENABLE KEYS */;


# Dumping structure for table sgoa2.funcionario
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
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

# Dumping data for table sgoa2.funcionario: ~1 rows (approximately)
DELETE FROM `funcionario`;
/*!40000 ALTER TABLE `funcionario` DISABLE KEYS */;
INSERT INTO `funcionario` (`Id`, `IdPessoa`, `Matricula`, `Obs`, `DataCadastro`, `Ativo`, `IdSetor`, `IdProfissao`, `IdPerfil`, `Senha`) VALUES
	(1, 1, 20131, NULL, '2013-11-21', 1, 1, 1, 1, '565d9a3631f5940b9facd0f153b5f569');
/*!40000 ALTER TABLE `funcionario` ENABLE KEYS */;


# Dumping structure for table sgoa2.marca
DROP TABLE IF EXISTS `marca`;
CREATE TABLE IF NOT EXISTS `marca` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `Descricao` varchar(250) NOT NULL,
  `Ativo` tinyint(1) NOT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8;

# Dumping data for table sgoa2.marca: ~16 rows (approximately)
DELETE FROM `marca`;
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


# Dumping structure for table sgoa2.modelo
DROP TABLE IF EXISTS `modelo`;
CREATE TABLE IF NOT EXISTS `modelo` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `IdMarca` int(11) NOT NULL,
  `Descricao` varchar(250) NOT NULL,
  `Ativo` tinyint(1) NOT NULL,
  PRIMARY KEY (`Id`),
  KEY `ModeloFabricante_FKIndex1` (`IdMarca`),
  CONSTRAINT `Fk_Modelo_Marca` FOREIGN KEY (`IdMarca`) REFERENCES `marca` (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

# Dumping data for table sgoa2.modelo: ~0 rows (approximately)
DELETE FROM `modelo`;
/*!40000 ALTER TABLE `modelo` DISABLE KEYS */;
/*!40000 ALTER TABLE `modelo` ENABLE KEYS */;


# Dumping structure for table sgoa2.orcamento
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
  PRIMARY KEY (`Id`),
  KEY `FK_orcamento_cliente` (`IdCliente`),
  KEY `FK_orcamento_veiculo` (`IdVeiculo`),
  KEY `FK_orcamento_funcionario_2` (`IdFuncCancelamento`),
  KEY `FK_orcamento_seguradora` (`IdSeguradora`),
  KEY `FK_orcamento_funcionario` (`IdFuncionarioCadastro`),
  CONSTRAINT `FK_orcamento_cliente` FOREIGN KEY (`IdCliente`) REFERENCES `cliente` (`Id`),
  CONSTRAINT `FK_orcamento_funcionario` FOREIGN KEY (`IdFuncionarioCadastro`) REFERENCES `funcionario` (`Id`),
  CONSTRAINT `FK_orcamento_funcionario_2` FOREIGN KEY (`IdFuncCancelamento`) REFERENCES `funcionario` (`Id`),
  CONSTRAINT `FK_orcamento_seguradora` FOREIGN KEY (`IdSeguradora`) REFERENCES `seguradora` (`Id`),
  CONSTRAINT `FK_orcamento_veiculo` FOREIGN KEY (`IdVeiculo`) REFERENCES `veiculo` (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

# Dumping data for table sgoa2.orcamento: ~0 rows (approximately)
DELETE FROM `orcamento`;
/*!40000 ALTER TABLE `orcamento` DISABLE KEYS */;
/*!40000 ALTER TABLE `orcamento` ENABLE KEYS */;


# Dumping structure for table sgoa2.orcamento_anexo
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

# Dumping data for table sgoa2.orcamento_anexo: ~0 rows (approximately)
DELETE FROM `orcamento_anexo`;
/*!40000 ALTER TABLE `orcamento_anexo` DISABLE KEYS */;
/*!40000 ALTER TABLE `orcamento_anexo` ENABLE KEYS */;


# Dumping structure for table sgoa2.orcamento_tiposervico
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

# Dumping data for table sgoa2.orcamento_tiposervico: ~0 rows (approximately)
DELETE FROM `orcamento_tiposervico`;
/*!40000 ALTER TABLE `orcamento_tiposervico` DISABLE KEYS */;
/*!40000 ALTER TABLE `orcamento_tiposervico` ENABLE KEYS */;


# Dumping structure for table sgoa2.ordemservico
DROP TABLE IF EXISTS `ordemservico`;
CREATE TABLE IF NOT EXISTS `ordemservico` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `IdOrcamento` int(11) NOT NULL,
  `Situacao` char(1) NOT NULL DEFAULT 'A',
  `DataAprovacao` datetime NOT NULL,
  `IdFuncAprovacao` int(11) NOT NULL,
  `Obs` varchar(250) DEFAULT NULL,
  `IdEtapaAtual` int(11) DEFAULT NULL,
  `DataCancelamento` datetime DEFAULT NULL,
  `MotivoCancelamento` varchar(250) DEFAULT NULL,
  `IdFuncCancelamento` int(11) DEFAULT NULL,
  `IdOrdemServico` int(11) DEFAULT NULL,
  PRIMARY KEY (`Id`),
  KEY `FK_ordemservico_orcamento` (`IdOrcamento`),
  KEY `FK_ordemservico_funcionario` (`IdFuncAprovacao`),
  KEY `FK_ordemservico_etapa` (`IdEtapaAtual`),
  KEY `FK_ordemservico_funcionario_2` (`IdFuncCancelamento`),
  KEY `FK_ordemservico_ordemservico` (`IdOrdemServico`),
  CONSTRAINT `FK_ordemservico_ordemservico` FOREIGN KEY (`IdOrdemServico`) REFERENCES `ordemservico` (`Id`),
  CONSTRAINT `FK_ordemservico_etapa` FOREIGN KEY (`IdEtapaAtual`) REFERENCES `ordemservico_etapa` (`Id`),
  CONSTRAINT `FK_ordemservico_funcionario` FOREIGN KEY (`IdFuncAprovacao`) REFERENCES `funcionario` (`Id`),
  CONSTRAINT `FK_ordemservico_funcionario_2` FOREIGN KEY (`IdFuncCancelamento`) REFERENCES `funcionario` (`Id`),
  CONSTRAINT `FK_ordemservico_orcamento` FOREIGN KEY (`IdOrcamento`) REFERENCES `orcamento` (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

# Dumping data for table sgoa2.ordemservico: ~0 rows (approximately)
DELETE FROM `ordemservico`;
/*!40000 ALTER TABLE `ordemservico` DISABLE KEYS */;
/*!40000 ALTER TABLE `ordemservico` ENABLE KEYS */;


# Dumping structure for table sgoa2.ordemservico_etapa
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

# Dumping data for table sgoa2.ordemservico_etapa: ~0 rows (approximately)
DELETE FROM `ordemservico_etapa`;
/*!40000 ALTER TABLE `ordemservico_etapa` DISABLE KEYS */;
/*!40000 ALTER TABLE `ordemservico_etapa` ENABLE KEYS */;


# Dumping structure for table sgoa2.ordemservico_evento
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

# Dumping data for table sgoa2.ordemservico_evento: ~0 rows (approximately)
DELETE FROM `ordemservico_evento`;
/*!40000 ALTER TABLE `ordemservico_evento` DISABLE KEYS */;
/*!40000 ALTER TABLE `ordemservico_evento` ENABLE KEYS */;


# Dumping structure for table sgoa2.ordemservico_foto
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

# Dumping data for table sgoa2.ordemservico_foto: ~0 rows (approximately)
DELETE FROM `ordemservico_foto`;
/*!40000 ALTER TABLE `ordemservico_foto` DISABLE KEYS */;
/*!40000 ALTER TABLE `ordemservico_foto` ENABLE KEYS */;


# Dumping structure for table sgoa2.perfil
DROP TABLE IF EXISTS `perfil`;
CREATE TABLE IF NOT EXISTS `perfil` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `Descricao` varchar(250) NOT NULL,
  `Ativo` tinyint(1) NOT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

# Dumping data for table sgoa2.perfil: ~2 rows (approximately)
DELETE FROM `perfil`;
/*!40000 ALTER TABLE `perfil` DISABLE KEYS */;
INSERT INTO `perfil` (`Id`, `Descricao`, `Ativo`) VALUES
	(1, 'Gerente', 1),
	(2, 'Operacional', 1);
/*!40000 ALTER TABLE `perfil` ENABLE KEYS */;


# Dumping structure for table sgoa2.pessoa
DROP TABLE IF EXISTS `pessoa`;
CREATE TABLE IF NOT EXISTS `pessoa` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `Nome` varchar(200) NOT NULL,
  `Tipo` char(1) NOT NULL,
  `Email` varchar(250) DEFAULT NULL,
  `TelefonePrimario` varchar(15) DEFAULT NULL,
  `TelefoneSecundario` varchar(15) DEFAULT NULL,
  PRIMARY KEY (`Id`),
  UNIQUE KEY `Email` (`Email`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

# Dumping data for table sgoa2.pessoa: ~1 rows (approximately)
DELETE FROM `pessoa`;
/*!40000 ALTER TABLE `pessoa` DISABLE KEYS */;
INSERT INTO `pessoa` (`Id`, `Nome`, `Tipo`, `Email`, `TelefonePrimario`, `TelefoneSecundario`) VALUES
	(1, 'Administrador do Sistema', 'F', 'wreimann@hotmail.com', NULL, NULL);
/*!40000 ALTER TABLE `pessoa` ENABLE KEYS */;


# Dumping structure for table sgoa2.pessoaendereco
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

# Dumping data for table sgoa2.pessoaendereco: ~0 rows (approximately)
DELETE FROM `pessoaendereco`;
/*!40000 ALTER TABLE `pessoaendereco` DISABLE KEYS */;
/*!40000 ALTER TABLE `pessoaendereco` ENABLE KEYS */;


# Dumping structure for table sgoa2.pessoafisica
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

# Dumping data for table sgoa2.pessoafisica: ~1 rows (approximately)
DELETE FROM `pessoafisica`;
/*!40000 ALTER TABLE `pessoafisica` DISABLE KEYS */;
INSERT INTO `pessoafisica` (`Id`, `CPF`, `Sexo`, `DataNascimento`) VALUES
	(1, '05809243940', 'M', '2013-11-21');
/*!40000 ALTER TABLE `pessoafisica` ENABLE KEYS */;


# Dumping structure for table sgoa2.pessoajuridica
DROP TABLE IF EXISTS `pessoajuridica`;
CREATE TABLE IF NOT EXISTS `pessoajuridica` (
  `Id` int(11) NOT NULL,
  `CNPJ` char(18) NOT NULL,
  PRIMARY KEY (`Id`),
  KEY `FK_Pessoa` (`Id`),
  CONSTRAINT `FK_Juridica_Pessoa` FOREIGN KEY (`Id`) REFERENCES `pessoa` (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

# Dumping data for table sgoa2.pessoajuridica: ~0 rows (approximately)
DELETE FROM `pessoajuridica`;
/*!40000 ALTER TABLE `pessoajuridica` DISABLE KEYS */;
/*!40000 ALTER TABLE `pessoajuridica` ENABLE KEYS */;


# Dumping structure for table sgoa2.profissao
DROP TABLE IF EXISTS `profissao`;
CREATE TABLE IF NOT EXISTS `profissao` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `Descricao` varchar(250) NOT NULL,
  `Ativo` tinyint(1) NOT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

# Dumping data for table sgoa2.profissao: ~1 rows (approximately)
DELETE FROM `profissao`;
/*!40000 ALTER TABLE `profissao` DISABLE KEYS */;
INSERT INTO `profissao` (`Id`, `Descricao`, `Ativo`) VALUES
	(1, 'Analista de Sistemas', 1);
/*!40000 ALTER TABLE `profissao` ENABLE KEYS */;


# Dumping structure for table sgoa2.seguradora
DROP TABLE IF EXISTS `seguradora`;
CREATE TABLE IF NOT EXISTS `seguradora` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `Descricao` varchar(250) NOT NULL,
  `Ativo` tinyint(1) NOT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

# Dumping data for table sgoa2.seguradora: ~14 rows (approximately)
DELETE FROM `seguradora`;
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


# Dumping structure for table sgoa2.setor
DROP TABLE IF EXISTS `setor`;
CREATE TABLE IF NOT EXISTS `setor` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `Descricao` varchar(250) NOT NULL,
  `Ativo` tinyint(1) NOT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

# Dumping data for table sgoa2.setor: ~4 rows (approximately)
DELETE FROM `setor`;
/*!40000 ALTER TABLE `setor` DISABLE KEYS */;
INSERT INTO `setor` (`Id`, `Descricao`, `Ativo`) VALUES
	(1, 'Setor Administrativo', 1),
	(2, 'Setor de Pintura', 1),
	(3, 'Setor de Funilaria', 1),
	(4, 'Setor de Mecânica', 1);
/*!40000 ALTER TABLE `setor` ENABLE KEYS */;


# Dumping structure for table sgoa2.tiposervico
DROP TABLE IF EXISTS `tiposervico`;
CREATE TABLE IF NOT EXISTS `tiposervico` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `Descricao` varchar(250) NOT NULL,
  `Ativo` tinyint(1) NOT NULL,
  `ValorHoraPadrao` decimal(6,2) NOT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

# Dumping data for table sgoa2.tiposervico: ~4 rows (approximately)
DELETE FROM `tiposervico`;
/*!40000 ALTER TABLE `tiposervico` DISABLE KEYS */;
INSERT INTO `tiposervico` (`Id`, `Descricao`, `Ativo`, `ValorHoraPadrao`) VALUES
	(1, 'Administração', 1, 29.00),
	(2, 'Funilaria', 1, 0.00),
	(3, 'Pintura', 1, 0.00),
	(4, 'Mecânica', 1, 0.00);
/*!40000 ALTER TABLE `tiposervico` ENABLE KEYS */;


# Dumping structure for table sgoa2.veiculo
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

# Dumping data for table sgoa2.veiculo: ~0 rows (approximately)
DELETE FROM `veiculo`;
/*!40000 ALTER TABLE `veiculo` DISABLE KEYS */;
/*!40000 ALTER TABLE `veiculo` ENABLE KEYS */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;

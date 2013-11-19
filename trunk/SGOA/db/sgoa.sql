# --------------------------------------------------------
# Host:                         127.0.0.1
# Server version:               5.5.28
# Server OS:                    Win64
# HeidiSQL version:             6.0.0.3603
# Date/time:                    2013-11-18 23:15:37
# --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;

# Dumping database structure for sgoa2
DROP DATABASE IF EXISTS `sgoa2`;
CREATE DATABASE IF NOT EXISTS `sgoa2` /*!40100 DEFAULT CHARACTER SET latin1 */;
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
/*!40000 ALTER TABLE `cliente` DISABLE KEYS */;
/*!40000 ALTER TABLE `cliente` ENABLE KEYS */;


# Dumping structure for table sgoa2.configemail
DROP TABLE IF EXISTS `configemail`;
CREATE TABLE IF NOT EXISTS `configemail` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `AssuntoEmail` varchar(100) DEFAULT NULL,
  `TextoEmail` varchar(1000) DEFAULT NULL,
  `IdentificacaoEmail` varchar(100) DEFAULT NULL,
  `Email` varchar(250) DEFAULT NULL,
  `Senha` varchar(2000) DEFAULT NULL,
  `ServidorNecessitaAutenticacao` tinyint(1) DEFAULT NULL,
  `ServidorSMTP` varchar(250) DEFAULT NULL,
  `Porta` int(11) DEFAULT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

# Dumping data for table sgoa2.configemail: ~0 rows (approximately)
/*!40000 ALTER TABLE `configemail` DISABLE KEYS */;
/*!40000 ALTER TABLE `configemail` ENABLE KEYS */;


# Dumping structure for table sgoa2.config_ordemservico
DROP TABLE IF EXISTS `config_ordemservico`;
CREATE TABLE IF NOT EXISTS `config_ordemservico` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `IdEtapaInicial` int(11) NOT NULL,
  `IdEtapaInicialSeguradora` int(11) NOT NULL,
  `IdEtapaFimConcerto` int(11) NOT NULL,
  `IdEtapaCancelamentoConcerto` int(11) NOT NULL,
  `IdEtapaConclusaoOrdemServico` int(11) NOT NULL,
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

# Dumping data for table sgoa2.config_ordemservico: ~0 rows (approximately)
/*!40000 ALTER TABLE `config_ordemservico` DISABLE KEYS */;
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
  `Automatica` tinyint(4) NOT NULL,
  `Decisao` tinyint(4) NOT NULL,
  `VisivelWebSite` tinyint(4) NOT NULL,
  `EnviaEmailInicio` tinyint(4) NOT NULL,
  `EnviaEmailFim` tinyint(4) NOT NULL,
  `IdImagem` int(11) NOT NULL,
  PRIMARY KEY (`Id`),
  KEY `FK_etapa_setor` (`IdSetor`),
  KEY `FK_etapa_tiposervico` (`IdTipoServico`),
  KEY `FK_etapa_imagem_etapa` (`IdImagem`),
  CONSTRAINT `FK_etapa_imagem_etapa` FOREIGN KEY (`IdImagem`) REFERENCES `imagem_etapa` (`Id`),
  CONSTRAINT `FK_etapa_setor` FOREIGN KEY (`IdSetor`) REFERENCES `setor` (`Id`),
  CONSTRAINT `FK_etapa_tiposervico` FOREIGN KEY (`IdTipoServico`) REFERENCES `tiposervico` (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

# Dumping data for table sgoa2.etapa: ~0 rows (approximately)
/*!40000 ALTER TABLE `etapa` DISABLE KEYS */;
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

# Dumping data for table sgoa2.funcionario: ~0 rows (approximately)
/*!40000 ALTER TABLE `funcionario` DISABLE KEYS */;
/*!40000 ALTER TABLE `funcionario` ENABLE KEYS */;


# Dumping structure for table sgoa2.imagem_etapa
DROP TABLE IF EXISTS `imagem_etapa`;
CREATE TABLE IF NOT EXISTS `imagem_etapa` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `Titulo` varchar(50) NOT NULL,
  `Descricao` varchar(250) NOT NULL,
  `Ativo` tinyint(1) NOT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

# Dumping data for table sgoa2.imagem_etapa: ~0 rows (approximately)
/*!40000 ALTER TABLE `imagem_etapa` DISABLE KEYS */;
/*!40000 ALTER TABLE `imagem_etapa` ENABLE KEYS */;


# Dumping structure for table sgoa2.marca
DROP TABLE IF EXISTS `marca`;
CREATE TABLE IF NOT EXISTS `marca` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `Descricao` varchar(250) NOT NULL,
  `Ativo` tinyint(1) NOT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

# Dumping data for table sgoa2.marca: ~0 rows (approximately)
/*!40000 ALTER TABLE `marca` DISABLE KEYS */;
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
  PRIMARY KEY (`Id`),
  KEY `FK_ordemservico_orcamento` (`IdOrcamento`),
  KEY `FK_ordemservico_funcionario` (`IdFuncAprovacao`),
  KEY `FK_ordemservico_etapa` (`IdEtapaAtual`),
  CONSTRAINT `FK_ordemservico_etapa` FOREIGN KEY (`IdEtapaAtual`) REFERENCES `ordemservico_etapa` (`Id`),
  CONSTRAINT `FK_ordemservico_funcionario` FOREIGN KEY (`IdFuncAprovacao`) REFERENCES `funcionario` (`Id`),
  CONSTRAINT `FK_ordemservico_orcamento` FOREIGN KEY (`IdOrcamento`) REFERENCES `orcamento` (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

# Dumping data for table sgoa2.ordemservico: ~0 rows (approximately)
/*!40000 ALTER TABLE `ordemservico` DISABLE KEYS */;
/*!40000 ALTER TABLE `ordemservico` ENABLE KEYS */;


# Dumping structure for table sgoa2.ordemservico_email
DROP TABLE IF EXISTS `ordemservico_email`;
CREATE TABLE IF NOT EXISTS `ordemservico_email` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `IdOrdemServicoEtapa` int(11) NOT NULL,
  `Destinatario` varchar(250) NOT NULL,
  PRIMARY KEY (`Id`),
  KEY `FK_ordemservico_anexo_ordemservico_etapa` (`IdOrdemServicoEtapa`),
  CONSTRAINT `ordemservico_email_ibfk_1` FOREIGN KEY (`IdOrdemServicoEtapa`) REFERENCES `ordemservico_etapa` (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

# Dumping data for table sgoa2.ordemservico_email: ~0 rows (approximately)
/*!40000 ALTER TABLE `ordemservico_email` DISABLE KEYS */;
/*!40000 ALTER TABLE `ordemservico_email` ENABLE KEYS */;


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
  PRIMARY KEY (`Id`),
  KEY `FK_ordemservico_etapa_funcionario` (`IdFuncionario`),
  KEY `FK_ordemservico_evento_ordemservico_etapa` (`IdOrdemServicoEtapa`),
  CONSTRAINT `FK_ordemservico_evento_ordemservico_etapa` FOREIGN KEY (`IdOrdemServicoEtapa`) REFERENCES `ordemservico_etapa` (`Id`),
  CONSTRAINT `ordemservico_evento_ibfk_1` FOREIGN KEY (`IdFuncionario`) REFERENCES `funcionario` (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

# Dumping data for table sgoa2.ordemservico_evento: ~0 rows (approximately)
/*!40000 ALTER TABLE `ordemservico_evento` DISABLE KEYS */;
/*!40000 ALTER TABLE `ordemservico_evento` ENABLE KEYS */;


# Dumping structure for table sgoa2.ordemservico_foto
DROP TABLE IF EXISTS `ordemservico_foto`;
CREATE TABLE IF NOT EXISTS `ordemservico_foto` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `IdOrdemServico` int(11) NOT NULL,
  `IdOrdemServicoEtapa` int(11) NOT NULL,
  `NomeArquivo` varchar(250) NOT NULL,
  `Imagem` mediumblob NOT NULL,
  PRIMARY KEY (`Id`),
  KEY `FK_ordemservico_anexo_ordemservico_etapa` (`IdOrdemServicoEtapa`),
  KEY `FK_ordemservico_foto_ordemservico` (`IdOrdemServico`),
  CONSTRAINT `FK_ordemservico_anexo_ordemservico_etapa` FOREIGN KEY (`IdOrdemServicoEtapa`) REFERENCES `ordemservico_etapa` (`Id`),
  CONSTRAINT `FK_ordemservico_foto_ordemservico` FOREIGN KEY (`IdOrdemServico`) REFERENCES `ordemservico` (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

# Dumping data for table sgoa2.ordemservico_foto: ~0 rows (approximately)
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

# Dumping data for table sgoa2.pessoa: ~0 rows (approximately)
/*!40000 ALTER TABLE `pessoa` DISABLE KEYS */;
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

# Dumping data for table sgoa2.pessoafisica: ~0 rows (approximately)
/*!40000 ALTER TABLE `pessoafisica` DISABLE KEYS */;
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
/*!40000 ALTER TABLE `pessoajuridica` DISABLE KEYS */;
/*!40000 ALTER TABLE `pessoajuridica` ENABLE KEYS */;


# Dumping structure for table sgoa2.profissao
DROP TABLE IF EXISTS `profissao`;
CREATE TABLE IF NOT EXISTS `profissao` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `Descricao` varchar(250) NOT NULL,
  `Ativo` tinyint(1) NOT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

# Dumping data for table sgoa2.profissao: ~0 rows (approximately)
/*!40000 ALTER TABLE `profissao` DISABLE KEYS */;
/*!40000 ALTER TABLE `profissao` ENABLE KEYS */;


# Dumping structure for table sgoa2.seguradora
DROP TABLE IF EXISTS `seguradora`;
CREATE TABLE IF NOT EXISTS `seguradora` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `Descricao` varchar(250) NOT NULL,
  `Ativo` tinyint(1) NOT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

# Dumping data for table sgoa2.seguradora: ~0 rows (approximately)
/*!40000 ALTER TABLE `seguradora` DISABLE KEYS */;
/*!40000 ALTER TABLE `seguradora` ENABLE KEYS */;


# Dumping structure for table sgoa2.setor
DROP TABLE IF EXISTS `setor`;
CREATE TABLE IF NOT EXISTS `setor` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `Descricao` varchar(250) NOT NULL,
  `Ativo` tinyint(1) NOT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

# Dumping data for table sgoa2.setor: ~0 rows (approximately)
/*!40000 ALTER TABLE `setor` DISABLE KEYS */;
/*!40000 ALTER TABLE `setor` ENABLE KEYS */;


# Dumping structure for table sgoa2.tiposervico
DROP TABLE IF EXISTS `tiposervico`;
CREATE TABLE IF NOT EXISTS `tiposervico` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `Descricao` varchar(250) NOT NULL,
  `Ativo` tinyint(1) NOT NULL,
  `ValorHoraPadrao` decimal(6,2) NOT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

# Dumping data for table sgoa2.tiposervico: ~0 rows (approximately)
/*!40000 ALTER TABLE `tiposervico` DISABLE KEYS */;
/*!40000 ALTER TABLE `tiposervico` ENABLE KEYS */;


# Dumping structure for table sgoa2.usuario
DROP TABLE IF EXISTS `usuario`;
CREATE TABLE IF NOT EXISTS `usuario` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `Senha` varchar(2000) DEFAULT NULL,
  `Tipo` char(1) NOT NULL,
  `IdPessoa` int(11) NOT NULL,
  `Ativo` tinyint(1) NOT NULL,
  PRIMARY KEY (`Id`),
  KEY `FK_UsuarioPessoa` (`IdPessoa`),
  CONSTRAINT `FK_Usuario_Pessoa` FOREIGN KEY (`IdPessoa`) REFERENCES `pessoa` (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

# Dumping data for table sgoa2.usuario: ~0 rows (approximately)
/*!40000 ALTER TABLE `usuario` DISABLE KEYS */;
/*!40000 ALTER TABLE `usuario` ENABLE KEYS */;


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
/*!40000 ALTER TABLE `veiculo` DISABLE KEYS */;
/*!40000 ALTER TABLE `veiculo` ENABLE KEYS */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
# --------------------------------------------------------
# Host:                         127.0.0.1
# Server version:               5.5.28
# Server OS:                    Win64
# HeidiSQL version:             6.0.0.3603
# Date/time:                    2013-10-19 19:03:20
# --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;

# Dumping database structure for sgoa
DROP DATABASE IF EXISTS `sgoa`;
CREATE DATABASE IF NOT EXISTS `sgoa` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `sgoa`;


# Dumping structure for table sgoa.cliente
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

# Data exporting was unselected.


# Dumping structure for table sgoa.configemail
DROP TABLE IF EXISTS `configemail`;
CREATE TABLE IF NOT EXISTS `configemail` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `AssuntoEmail` varchar(100) DEFAULT NULL,
  `TextoEmail` varchar(1000) DEFAULT NULL,
  `IdentificacaoEmail` varchar(100) DEFAULT NULL,
  `Email` varchar(250) DEFAULT NULL,
  `Senha` varchar(2000) DEFAULT NULL,
  `ServidorNecessitaAutenticacao` tinyint(1) DEFAULT NULL,
  `ServidorSMTP` varchar(250) DEFAULT NULL,
  `Porta` int(11) DEFAULT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

# Data exporting was unselected.


# Dumping structure for table sgoa.config_ordemservico
DROP TABLE IF EXISTS `config_ordemservico`;
CREATE TABLE IF NOT EXISTS `config_ordemservico` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `IdEtapaInicial` int(11) NOT NULL,
  `IdEtapaInicialSeguradora` int(11) NOT NULL,
  `IdEtapaFimConcerto` int(11) NOT NULL,
  `IdEtapaCancelamentoConcerto` int(11) NOT NULL,
  PRIMARY KEY (`Id`),
  KEY `FK_config_ordemservico_etapa` (`IdEtapaInicial`),
  KEY `FK_config_ordemservico_etapa_2` (`IdEtapaInicialSeguradora`),
  KEY `FK_config_ordemservico_etapa_3` (`IdEtapaFimConcerto`),
  KEY `FK_config_ordemservico_etapa_4` (`IdEtapaCancelamentoConcerto`),
  CONSTRAINT `FK_config_ordemservico_etapa` FOREIGN KEY (`IdEtapaInicial`) REFERENCES `etapa` (`Id`),
  CONSTRAINT `FK_config_ordemservico_etapa_2` FOREIGN KEY (`IdEtapaInicialSeguradora`) REFERENCES `etapa` (`Id`),
  CONSTRAINT `FK_config_ordemservico_etapa_3` FOREIGN KEY (`IdEtapaFimConcerto`) REFERENCES `etapa` (`Id`),
  CONSTRAINT `FK_config_ordemservico_etapa_4` FOREIGN KEY (`IdEtapaCancelamentoConcerto`) REFERENCES `etapa` (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

# Data exporting was unselected.


# Dumping structure for table sgoa.cor
DROP TABLE IF EXISTS `cor`;
CREATE TABLE IF NOT EXISTS `cor` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `Descricao` varchar(250) NOT NULL,
  `Ativo` tinyint(1) NOT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

# Data exporting was unselected.


# Dumping structure for table sgoa.etapa
DROP TABLE IF EXISTS `etapa`;
CREATE TABLE IF NOT EXISTS `etapa` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `Descricao` varchar(250) NOT NULL,
  `Ativo` tinyint(1) NOT NULL,
  `IdSetor` int(11) NOT NULL,
  `IdTipoServico` int(11) NOT NULL,
  `Automatica` tinyint(4) NOT NULL,
  `Decisao` tinyint(4) NOT NULL,
  `VisivelWebSite` tinyint(4) NOT NULL,
  `EnviaEmailInicio` tinyint(4) NOT NULL,
  `EnviaEmailFim` tinyint(4) NOT NULL,
  `IdImagem` int(11) NOT NULL,
  PRIMARY KEY (`Id`),
  KEY `FK_etapa_setor` (`IdSetor`),
  KEY `FK_etapa_tiposervico` (`IdTipoServico`),
  KEY `FK_etapa_imagem_etapa` (`IdImagem`),
  CONSTRAINT `FK_etapa_imagem_etapa` FOREIGN KEY (`IdImagem`) REFERENCES `imagem_etapa` (`Id`),
  CONSTRAINT `FK_etapa_setor` FOREIGN KEY (`IdSetor`) REFERENCES `setor` (`Id`),
  CONSTRAINT `FK_etapa_tiposervico` FOREIGN KEY (`IdTipoServico`) REFERENCES `tiposervico` (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

# Data exporting was unselected.


# Dumping structure for table sgoa.fluxo
DROP TABLE IF EXISTS `fluxo`;
CREATE TABLE IF NOT EXISTS `fluxo` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `Descricao` varchar(250) NOT NULL,
  `Ativo` tinyint(1) NOT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

# Data exporting was unselected.


# Dumping structure for table sgoa.fluxo_etapa
DROP TABLE IF EXISTS `fluxo_etapa`;
CREATE TABLE IF NOT EXISTS `fluxo_etapa` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `IdFluxo` int(11) NOT NULL,
  `IdEtapa` int(11) NOT NULL,
  `EnviaEmailEspecifico` tinyint(1) NOT NULL,
  `EmailEspecifico` varchar(250) NOT NULL,
  `EnviaEmailCliente` tinyint(1) NOT NULL,
  `EnviaEmailGerente` tinyint(1) NOT NULL,
  PRIMARY KEY (`Id`),
  KEY `FK_fluxo_etapa_fluxo` (`IdFluxo`),
  KEY `FK_fluxo_etapa_etapa` (`IdEtapa`),
  CONSTRAINT `FK_fluxo_etapa_etapa` FOREIGN KEY (`IdEtapa`) REFERENCES `etapa` (`Id`),
  CONSTRAINT `FK_fluxo_etapa_fluxo` FOREIGN KEY (`IdFluxo`) REFERENCES `fluxo` (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

# Data exporting was unselected.


# Dumping structure for table sgoa.funcionario
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

# Data exporting was unselected.


# Dumping structure for table sgoa.imagem_etapa
DROP TABLE IF EXISTS `imagem_etapa`;
CREATE TABLE IF NOT EXISTS `imagem_etapa` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `Titulo` varchar(50) NOT NULL,
  `Descricao` varchar(250) NOT NULL,
  `Ativo` tinyint(1) NOT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

# Data exporting was unselected.


# Dumping structure for table sgoa.marca
DROP TABLE IF EXISTS `marca`;
CREATE TABLE IF NOT EXISTS `marca` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `Descricao` varchar(250) NOT NULL,
  `Ativo` tinyint(1) NOT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

# Data exporting was unselected.


# Dumping structure for table sgoa.modelo
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

# Data exporting was unselected.


# Dumping structure for table sgoa.orcamento
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

# Data exporting was unselected.


# Dumping structure for table sgoa.orcamento_anexo
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

# Data exporting was unselected.


# Dumping structure for table sgoa.orcamento_tiposervico
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

# Data exporting was unselected.


# Dumping structure for table sgoa.ordemservico
DROP TABLE IF EXISTS `ordemservico`;
CREATE TABLE IF NOT EXISTS `ordemservico` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `IdOrcamento` int(11) NOT NULL,
  `Situacao` char(1) NOT NULL DEFAULT 'A',
  `DataAprovacao` datetime NOT NULL,
  `IdFluxo` int(11) DEFAULT NULL,
  `IdFuncAprovacao` int(11) NOT NULL,
  `Obs` varchar(250) DEFAULT NULL,
  `IdEtapaAtual` int(11) DEFAULT NULL,
  PRIMARY KEY (`Id`),
  KEY `FK_ordemservico_orcamento` (`IdOrcamento`),
  KEY `FK_ordemservico_fluxo` (`IdFluxo`),
  KEY `FK_ordemservico_funcionario` (`IdFuncAprovacao`),
  KEY `FK_ordemservico_etapa` (`IdEtapaAtual`),
  CONSTRAINT `FK_ordemservico_etapa` FOREIGN KEY (`IdEtapaAtual`) REFERENCES `ordemservico_etapa` (`Id`),
  CONSTRAINT `FK_ordemservico_fluxo` FOREIGN KEY (`IdFluxo`) REFERENCES `fluxo` (`Id`),
  CONSTRAINT `FK_ordemservico_funcionario` FOREIGN KEY (`IdFuncAprovacao`) REFERENCES `funcionario` (`Id`),
  CONSTRAINT `FK_ordemservico_orcamento` FOREIGN KEY (`IdOrcamento`) REFERENCES `orcamento` (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

# Data exporting was unselected.


# Dumping structure for table sgoa.ordemservico_email
DROP TABLE IF EXISTS `ordemservico_email`;
CREATE TABLE IF NOT EXISTS `ordemservico_email` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `IdOrdemServicoEtapa` int(11) NOT NULL,
  `Destinatario` varchar(250) NOT NULL,
  PRIMARY KEY (`Id`),
  KEY `FK_ordemservico_anexo_ordemservico_etapa` (`IdOrdemServicoEtapa`),
  CONSTRAINT `ordemservico_email_ibfk_1` FOREIGN KEY (`IdOrdemServicoEtapa`) REFERENCES `ordemservico_etapa` (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

# Data exporting was unselected.


# Dumping structure for table sgoa.ordemservico_etapa
DROP TABLE IF EXISTS `ordemservico_etapa`;
CREATE TABLE IF NOT EXISTS `ordemservico_etapa` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `DataCadastro` datetime NOT NULL,
  `IdOrdemServico` int(11) NOT NULL,
  `IdEtapa` int(11) NOT NULL,
  `DataEntrada` datetime NOT NULL,
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

# Data exporting was unselected.


# Dumping structure for table sgoa.ordemservico_evento
DROP TABLE IF EXISTS `ordemservico_evento`;
CREATE TABLE IF NOT EXISTS `ordemservico_evento` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `IdOrdemServicoEtapa` int(11) NOT NULL,
  `DataOcorrencia` datetime NOT NULL,
  `TipoEvento` char(1) NOT NULL,
  `IdFuncionario` int(11) NOT NULL,
  `Descricao` varchar(250) NOT NULL,
  `Ativo` tinyint(1) NOT NULL,
  PRIMARY KEY (`Id`),
  KEY `FK_ordemservico_etapa_funcionario` (`IdFuncionario`),
  KEY `FK_ordemservico_evento_ordemservico_etapa` (`IdOrdemServicoEtapa`),
  CONSTRAINT `FK_ordemservico_evento_ordemservico_etapa` FOREIGN KEY (`IdOrdemServicoEtapa`) REFERENCES `ordemservico_etapa` (`Id`),
  CONSTRAINT `ordemservico_evento_ibfk_1` FOREIGN KEY (`IdFuncionario`) REFERENCES `funcionario` (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

# Data exporting was unselected.


# Dumping structure for table sgoa.ordemservico_foto
DROP TABLE IF EXISTS `ordemservico_foto`;
CREATE TABLE IF NOT EXISTS `ordemservico_foto` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `IdOrdemServico` int(11) NOT NULL,
  `IdOrdemServicoEtapa` int(11) NOT NULL,
  `NomeArquivo` varchar(250) NOT NULL,
  `Imagem` mediumblob NOT NULL,
  PRIMARY KEY (`Id`),
  KEY `FK_ordemservico_anexo_ordemservico_etapa` (`IdOrdemServicoEtapa`),
  KEY `FK_ordemservico_foto_ordemservico` (`IdOrdemServico`),
  CONSTRAINT `FK_ordemservico_anexo_ordemservico_etapa` FOREIGN KEY (`IdOrdemServicoEtapa`) REFERENCES `ordemservico_etapa` (`Id`),
  CONSTRAINT `FK_ordemservico_foto_ordemservico` FOREIGN KEY (`IdOrdemServico`) REFERENCES `ordemservico` (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

# Data exporting was unselected.


# Dumping structure for table sgoa.perfil
DROP TABLE IF EXISTS `perfil`;
CREATE TABLE IF NOT EXISTS `perfil` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `Descricao` varchar(250) NOT NULL,
  `Ativo` tinyint(1) NOT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

# Data exporting was unselected.


# Dumping structure for table sgoa.pessoa
DROP TABLE IF EXISTS `pessoa`;
CREATE TABLE IF NOT EXISTS `pessoa` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `Nome` varchar(200) NOT NULL,
  `Tipo` char(1) NOT NULL,
  `Email` varchar(250) DEFAULT NULL,
  `TelefonePrimario` varchar(15) DEFAULT NULL,
  `TelefoneSecundario` varchar(15) DEFAULT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

# Data exporting was unselected.


# Dumping structure for table sgoa.pessoaendereco
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

# Data exporting was unselected.


# Dumping structure for table sgoa.pessoafisica
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

# Data exporting was unselected.


# Dumping structure for table sgoa.pessoajuridica
DROP TABLE IF EXISTS `pessoajuridica`;
CREATE TABLE IF NOT EXISTS `pessoajuridica` (
  `Id` int(11) NOT NULL,
  `CNPJ` char(18) NOT NULL,
  PRIMARY KEY (`Id`),
  KEY `FK_Pessoa` (`Id`),
  CONSTRAINT `FK_Juridica_Pessoa` FOREIGN KEY (`Id`) REFERENCES `pessoa` (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

# Data exporting was unselected.


# Dumping structure for table sgoa.profissao
DROP TABLE IF EXISTS `profissao`;
CREATE TABLE IF NOT EXISTS `profissao` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `Descricao` varchar(250) NOT NULL,
  `Ativo` tinyint(1) NOT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

# Data exporting was unselected.


# Dumping structure for table sgoa.seguradora
DROP TABLE IF EXISTS `seguradora`;
CREATE TABLE IF NOT EXISTS `seguradora` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `Descricao` varchar(250) NOT NULL,
  `Ativo` tinyint(1) NOT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

# Data exporting was unselected.


# Dumping structure for table sgoa.setor
DROP TABLE IF EXISTS `setor`;
CREATE TABLE IF NOT EXISTS `setor` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `Descricao` varchar(250) NOT NULL,
  `Ativo` tinyint(1) NOT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

# Data exporting was unselected.


# Dumping structure for table sgoa.tiposervico
DROP TABLE IF EXISTS `tiposervico`;
CREATE TABLE IF NOT EXISTS `tiposervico` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `Descricao` varchar(250) NOT NULL,
  `Ativo` tinyint(1) NOT NULL,
  `ValorHoraPadrao` decimal(6,2) NOT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

# Data exporting was unselected.


# Dumping structure for table sgoa.usuario
DROP TABLE IF EXISTS `usuario`;
CREATE TABLE IF NOT EXISTS `usuario` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `Senha` varchar(2000) DEFAULT NULL,
  `Tipo` char(1) NOT NULL,
  `IdPessoa` int(11) NOT NULL,
  `Ativo` tinyint(1) NOT NULL,
  PRIMARY KEY (`Id`),
  KEY `FK_UsuarioPessoa` (`IdPessoa`),
  CONSTRAINT `FK_Usuario_Pessoa` FOREIGN KEY (`IdPessoa`) REFERENCES `pessoa` (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

# Data exporting was unselected.


# Dumping structure for table sgoa.veiculo
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

# Data exporting was unselected.
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;

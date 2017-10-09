CREATE DATABASE  IF NOT EXISTS `XERUNG` /*!40100 DEFAULT CHARACTER SET latin1 */;
USE `XERUNG`;
-- MySQL dump 10.13  Distrib 5.5.37, for debian-linux-gnu (x86_64)
--
-- Host: 192.168.1.120    Database: XERUNG
-- ------------------------------------------------------
-- Server version	5.5.29-0ubuntu0.12.04.1

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `PHONEBOOKMASTER`
--

DROP TABLE IF EXISTS `PHONEBOOKMASTER`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `PHONEBOOKMASTER` (
  `PHONEBOOKID` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `PHONENUMBER` varchar(45) DEFAULT NULL,
  `STATUS` bit(1) DEFAULT NULL,
  PRIMARY KEY (`PHONEBOOKID`)
) ENGINE=InnoDB AUTO_INCREMENT=163 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `PUBLICGROUPCONTACTS`
--

DROP TABLE IF EXISTS `PUBLICGROUPCONTACTS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `PUBLICGROUPCONTACTS` (
  `GROUPID` int(11) NOT NULL,
  `CONTACTNO` varchar(45) NOT NULL,
  `CONTACTNAME` varchar(45) DEFAULT NULL,
  `EMAIL` varchar(45) DEFAULT NULL,
  `ADDRESS` varchar(45) DEFAULT NULL,
  `CITYNAME` varchar(45) DEFAULT NULL,
  `SEARCHKEY` text,
  PRIMARY KEY (`GROUPID`,`CONTACTNO`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `MASTER_COUNTRYCODE`
--

DROP TABLE IF EXISTS `MASTER_COUNTRYCODE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `MASTER_COUNTRYCODE` (
  `COUNTRYCODEID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `COUNTRYCODE` varchar(45) DEFAULT NULL,
  `COUNTRYNAME` varchar(45) DEFAULT NULL,
  `STATUSID` bit(1) DEFAULT NULL,
  `PHONENOMAXLEN` int(10) unsigned DEFAULT NULL,
  PRIMARY KEY (`COUNTRYCODEID`)
) ENGINE=InnoDB AUTO_INCREMENT=254 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `GROUP_MEMBER_DETAILS`
--

DROP TABLE IF EXISTS `GROUP_MEMBER_DETAILS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `GROUP_MEMBER_DETAILS` (
  `GROUPID` int(10) unsigned NOT NULL,
  `PHONENUMBER` varchar(20) NOT NULL,
  `ACCESSTYPEID` int(10) unsigned DEFAULT NULL,
  `REGISTERFLAG` bit(1) DEFAULT NULL,
  `ADDEDDATE` date DEFAULT NULL,
  `STATUSID` int(11) DEFAULT NULL,
  `ADDEDTIMESTAMP` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`PHONENUMBER`,`GROUPID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `GROUPTYPE_MASTER`
--

DROP TABLE IF EXISTS `GROUPTYPE_MASTER`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `GROUPTYPE_MASTER` (
  `GROUPTYPE` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `GROUPTYPE_NAME` varchar(45) DEFAULT NULL,
  `AUTOSUGGEST` int(11) DEFAULT '0',
  PRIMARY KEY (`GROUPTYPE`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `MASTER_OTP`
--

DROP TABLE IF EXISTS `MASTER_OTP`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `MASTER_OTP` (
  `OTPNO` int(10) unsigned NOT NULL,
  `CREATEDTIMESTAMP` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `STATUSID` bit(1) NOT NULL,
  PRIMARY KEY (`OTPNO`,`CREATEDTIMESTAMP`,`STATUSID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `PUSH_NOTIFICATION_KEY`
--

DROP TABLE IF EXISTS `PUSH_NOTIFICATION_KEY`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `PUSH_NOTIFICATION_KEY` (
  `MOBILENO` varchar(15) NOT NULL DEFAULT '0',
  `NOTIFICATION_KEY` varchar(500) NOT NULL DEFAULT '',
  `PHONETYPE` enum('1','2') DEFAULT NULL,
  `VERSION` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`MOBILENO`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `GROUP_ACCESS_TYPE`
--

DROP TABLE IF EXISTS `GROUP_ACCESS_TYPE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `GROUP_ACCESS_TYPE` (
  `ACCESSTYPEID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `ACCESSTYPEDESC` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`ACCESSTYPEID`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `MYCONTACTDETAILS`
--

DROP TABLE IF EXISTS `MYCONTACTDETAILS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `MYCONTACTDETAILS` (
  `UID` int(10) unsigned NOT NULL,
  `PHONENUMBERS` varchar(45) NOT NULL,
  `DISPLAYNAME` varchar(45) DEFAULT NULL,
  `STATUSID` bit(1) DEFAULT NULL,
  PRIMARY KEY (`UID`,`PHONENUMBERS`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `INVITATIONFLAGMASTER`
--

DROP TABLE IF EXISTS `INVITATIONFLAGMASTER`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `INVITATIONFLAGMASTER` (
  `INVITATIONFLAGID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `INVITATIONFLAGDESC` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`INVITATIONFLAGID`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `GROUP_INFO`
--

DROP TABLE IF EXISTS `GROUP_INFO`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `GROUP_INFO` (
  `GROUPID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `GROUPNAME` varchar(45) DEFAULT NULL,
  `MADEBYPHONENO` varchar(15) DEFAULT NULL,
  `DESCRITION` varchar(200) DEFAULT NULL,
  `GROUPTYPE` int(10) unsigned DEFAULT NULL,
  `TAGNAME` varchar(45) DEFAULT NULL,
  `CREATEDDATE` date DEFAULT NULL,
  `GROUPPHOTO` longtext,
  `CHANGEBYPHONENO` varchar(45) DEFAULT NULL,
  `GROUPACCESSTYPE` int(11) DEFAULT '1',
  `GROUPSEARCH` varchar(500) DEFAULT NULL,
  `CREATEUPDATETS` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`GROUPID`)
) ENGINE=MyISAM AUTO_INCREMENT=364 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `countrycodeRAW`
--

DROP TABLE IF EXISTS `countrycodeRAW`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `countrycodeRAW` (
  `COUNTRYNAME` varchar(22) DEFAULT NULL,
  `COUNTRYCODE` varchar(5) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `USER_INFO`
--

DROP TABLE IF EXISTS `USER_INFO`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `USER_INFO` (
  `UID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `PHONENUMBER` varchar(45) DEFAULT NULL,
  `ALTERNATENO` varchar(20) DEFAULT NULL,
  `EMAIL` varchar(45) DEFAULT NULL,
  `NAME` varchar(45) DEFAULT NULL,
  `ADDRESS` varchar(45) DEFAULT NULL,
  `CITYID` int(10) unsigned DEFAULT NULL,
  `STATEID` int(10) unsigned DEFAULT NULL,
  `COUNTRYCODEID` varchar(10) DEFAULT NULL,
  `OTPID` int(10) unsigned DEFAULT NULL,
  `STATUSID` int(11) unsigned DEFAULT NULL,
  `COMPANYNAME` varchar(45) DEFAULT NULL,
  `PROFESSION` varchar(45) DEFAULT NULL,
  `CREATEDDATE` timestamp NULL DEFAULT NULL,
  `PHOTO` longtext,
  `BLOODGROUP` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`UID`)
) ENGINE=InnoDB AUTO_INCREMENT=75 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ACCESSTYPEMASTER`
--

DROP TABLE IF EXISTS `ACCESSTYPEMASTER`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ACCESSTYPEMASTER` (
  `ACCESSTYPEID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `ACCESSTYPENAME` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`ACCESSTYPEID`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `MASTERCITY`
--

DROP TABLE IF EXISTS `MASTERCITY`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `MASTERCITY` (
  `CITYID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `COUNTRYID` int(10) unsigned DEFAULT NULL,
  `CITYNAME` varchar(45) DEFAULT NULL,
  `STATUSID` bit(1) DEFAULT NULL,
  PRIMARY KEY (`CITYID`),
  KEY `fk_MASTERCITY_1_idx` (`COUNTRYID`)
) ENGINE=InnoDB AUTO_INCREMENT=13888 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `MASTER_STATUS`
--

DROP TABLE IF EXISTS `MASTER_STATUS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `MASTER_STATUS` (
  `STATUSID` int(11) NOT NULL,
  `STATUSDESC` varchar(45) DEFAULT NULL,
  `CREATEDDATE` date DEFAULT NULL,
  PRIMARY KEY (`STATUSID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `INVITATION_DETAILS`
--

DROP TABLE IF EXISTS `INVITATION_DETAILS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `INVITATION_DETAILS` (
  `INVITATIONSENDTO` varchar(20) NOT NULL,
  `INVITATIONSENDBY` varchar(20) NOT NULL,
  `INVTATIONSENDDATE` varchar(45) DEFAULT NULL,
  `GROUPID` int(10) unsigned NOT NULL,
  `STATUSID` int(11) unsigned DEFAULT '2',
  `REGISTERFLAG` int(10) unsigned DEFAULT '0',
  `INVITATIONFLAGID` int(11) unsigned DEFAULT NULL,
  `NOTIFICATIONID` int(11) DEFAULT '0',
  PRIMARY KEY (`INVITATIONSENDTO`,`INVITATIONSENDBY`,`GROUPID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `MIPHONEBACKUP`
--

DROP TABLE IF EXISTS `MIPHONEBACKUP`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `MIPHONEBACKUP` (
  `UID` int(10) unsigned NOT NULL,
  `BACKUP` longtext,
  `LASTBACKUP` longtext,
  `CREATEDUPDATEDDATE` date DEFAULT NULL,
  PRIMARY KEY (`UID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping events for database 'XERUNG'
--

--
-- Dumping routines for database 'XERUNG'
--
/*!50003 DROP FUNCTION IF EXISTS `FUN_SPLIT_STR` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = '' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`%` FUNCTION `FUN_SPLIT_STR`(
  x text,
  delim VARCHAR(12),
  pos INT
) RETURNS text CHARSET latin1
RETURN REPLACE(SUBSTRING(SUBSTRING_INDEX(x, delim, pos),
       LENGTH(SUBSTRING_INDEX(x, delim, pos -1)) + 1),
        delim, '') ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP FUNCTION IF EXISTS `getcountryCode` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = '' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`%` FUNCTION `getcountryCode`( PCOUNTRYCODE VARCHAR(10)
, PCOUNTRYNAME VARCHAR(45)) RETURNS int(11)
BEGIN
    DECLARE L_COUNTRYCODE INTEGER default NULL;
    DECLARE L_COUNTRYCHECK INTEGER default 0;

    SELECT COUNTRYCODE INTO L_COUNTRYCODE FROM MASTER_COUNTRYCODE WHERE 
    UPPER(COUNTRYNAME)=UPPER(PCOUNTRYNAME) AND COUNTRYCODE=PCOUNTRYCODE;
    IF L_COUNTRYCODE IS NULL then
 
          SELECT COUNT(1) INTO  L_COUNTRYCHECK FROM MASTER_COUNTRYCODE WHERE 
          UPPER(COUNTRYNAME)=UPPER(PCOUNTRYNAME);
					IF L_COUNTRYCHECK>0 THEN

					UPDATE XERUNG.MASTER_COUNTRYCODE SET COUNTRYCODE=PCOUNTRYCODE WHERE 
					UPPER(COUNTRYNAME)=UPPER(PCOUNTRYNAME);
					RETURN PCOUNTRYCODE;
					ELSE
					INSERT INTO MASTER_COUNTRYCODE(COUNTRYCODE,COUNTRYNAME,STATUSID)
					VALUES(PCOUNTRYCODE,COUNTRYNAME);
					RETURN PCOUNTRYCODE;

					END IF;
    else
         RETURN L_COUNTRYCODE;
    END IF;

END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP FUNCTION IF EXISTS `GETGROUPTYPE` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = '' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`%` FUNCTION `GETGROUPTYPE`(
PGROUPTYPENAME VARCHAR(45)
) RETURNS int(11)
BEGIN

DECLARE L_GROUPTYPEID INTEGER default 0;
SELECT GROUPTYPE INTO L_GROUPTYPEID FROM GROUPTYPE_MASTER WHERE UPPER(GROUPTYPE_NAME)
 =UPPER(PGROUPTYPENAME);
IF L_GROUPTYPEID >0 then
 RETURN L_GROUPTYPEID;

else
  INSERT INTO GROUPTYPE_MASTER (GROUPTYPE_NAME) VALUES(TRIM(PGROUPTYPENAME));
  SELECT LAST_INSERT_ID() INTO L_GROUPTYPEID;
RETURN L_GROUPTYPEID;
END IF;


END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP FUNCTION IF EXISTS `GETGROUPTYPEID` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = '' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`%` FUNCTION `GETGROUPTYPEID`(
 PGROUPNAME VARCHAR(100)
) RETURNS int(11)
BEGIN
  DECLARE LGROUPTYPEID INTEGER default 0;

SELECT GROUPTYPE INTO LGROUPTYPEID FROM  GROUPTYPE_MASTER WHERE 
UPPER(GROUPTYPE_NAME)=UPPER(PGROUPNAME);
	IF LGROUPTYPEID>0 then
      return LGROUPTYPEID;
    ELSE 
      INSERT INTO GROUPTYPE_MASTER (GROUPTYPE_NAME,AUTOSUGGEST)
      VALUES(PGROUPNAME,0);
		SELECT LAST_INSERT_ID() INTO LGROUPTYPEID;
		return LGROUPTYPEID;

	END IF;


END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP FUNCTION IF EXISTS `insertPhoneNO` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = '' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`%` FUNCTION `insertPhoneNO`(
  PPHONENUMBER VARCHAR(20)
) RETURNS int(11)
BEGIN

DECLARE L_PHONEBOOKID INTEGER default 0;
 
 SELECT PHONEBOOKID INTO L_PHONEBOOKID FROM PHONEBOOKMASTER 
    WHERE PHONENUMBER=PPHONENUMBER;

		IF L_PHONEBOOKID>0 then
		RETURN L_PHONEBOOKID;
		else
		INSERT INTO PHONEBOOKMASTER (PHONENUMBER,STATUS)
		VALUES(PPHONENUMBER,1);

		SELECT LAST_INSERT_ID() INTO L_PHONEBOOKID;
		return L_PHONEBOOKID;
		END IF;


RETURN 1;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP FUNCTION IF EXISTS `OTPGEN` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = '' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`%` FUNCTION `OTPGEN`() RETURNS int(11)
BEGIN
 DECLARE L_NUMBER INTEGER;
  SELECT  FLOOR( 10000 + ( RAND( ) *89999 ) ) INTO  L_NUMBER FROM DUAL;

/*INSERT INTO MASTER_OTP (OTPNO,CREATEDTIMESTAMP,STATUSID) 
VALUES (L_NUMBER ,UTC_TIMESTAMP(),1) 
on duplicate key update CREATEDTIMESTAMP=UTC_TIMESTAMP() , STATUSID=1;*/

RETURN L_NUMBER;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP FUNCTION IF EXISTS `STRSPLITFF` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = '' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`%` FUNCTION `STRSPLITFF`(ASTRING blob,
                           ACOUNT SMALLINT) RETURNS blob
BEGIN
	
  DECLARE STRINGVAR BLOB;
SET STRINGVAR=REPLACE(SUBSTRING_INDEX(ASTRING,':',ACOUNT),CONCAT(SUBSTRING_INDEX(ASTRING,':',ACOUNT-1),':'),'');
RETURN STRINGVAR;
  END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `CREATEANDEDITGROUP` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = '' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`%` PROCEDURE `CREATEANDEDITGROUP`(
IN PGROUPID INTEGER,
IN PUID INTEGER,
IN PGROUPNAME VARCHAR(50),
IN PMADEBY VARCHAR(20),
IN PDESC VARCHAR(100),
IN PGROUPTYPEVAR VARCHAR(45),
IN PTAGNAME VARCHAR(100),
IN PGROUPPHOTO LONGTEXT,
IN PCHANGEBYPHONENO VARCHAR(100),
IN AFLAG INTEGER,
IN AGROUPMEMBERLIST TEXT,
OUT POUTV LONGTEXT
)
BEGIN
/*
  This procedure is called to create the group
  AFLAG=1  FOR UPDATE GROUP INFO
  AFLAG =0 FOR NEW GROUP EDIT
  AFLAG =2 FOR ADD NEW MEMBER BY ADMIN
 AFLAG =3 FOR ADD NEW MEMBER BY USER TO ADMIN 
REGISTER FLAG =1 FOR REGISTER
 REGISTER FLAG =0 FOR NON REGISTER

PMADEBY IS ALSO USED TO GROUP REQUEST SEND 
*/
 DECLARE AGROUPCHECK INTEGER default 0;
DECLARE L_GROUPTYPEID INTEGER default 0;
DECLARE L_GROUPID INTEGER default 0;
DECLARE L_SEARCH VARCHAR(500);


IF PGROUPID=0 AND AFLAG=0 THEN

			SELECT COUNT(1) INTO AGROUPCHECK  FROM GROUP_INFO WHERE UPPER(GROUPNAME) =UPPER(PGROUPNAME);

			IF AGROUPCHECK>0 THEN

			SET POUTV='{"STATUS":"Already exist"}';

			ELSEIF AGROUPCHECK=0 then
			    SELECT GETGROUPTYPE(PGROUPTYPEVAR) INTO L_GROUPTYPEID;
			
				INSERT INTO GROUP_INFO (GROUPNAME,MADEBYPHONENO,DESCRITION,GROUPTYPE,TAGNAME
				,CREATEDDATE,GROUPPHOTO,GROUPSEARCH,CREATEUPDATETS)
				VALUES(PGROUPNAME,PMADEBY,PDESC,L_GROUPTYPEID,PTAGNAME,UTC_DATE(),
                PGROUPPHOTO,CONCAT(PGROUPNAME ,' ',PDESC,' ',PTAGNAME),UTC_TIMESTAMP);

				SELECT LAST_INSERT_ID() INTO L_GROUPID;
          
				INSERT INTO GROUP_MEMBER_DETAILS(GROUPID,PHONENUMBER,ACCESSTYPEID,REGISTERFLAG,ADDEDDATE,
				STATUSID,ADDEDTIMESTAMP)VALUES(L_GROUPID,PMADEBY,1,1,UTC_DATE,1,UTC_TIMESTAMP())
				ON DUPLICATE KEY UPDATE PHONENUMBER=PMADEBY;

			CALL INSERTGROUPMEMBER(PMADEBY,3,PUID,L_GROUPID,AGROUPMEMBERLIST);
			   SET POUTV='{"STATUS":"SUCCESS"}';
			END IF;
 -- SET POUTV='{"STATUS":"SUCCESS"}';
ELSEIF PGROUPID>0 AND AFLAG=1  THEN



				SET @QUERY=CONCAT('UPDATE GROUP_INFO SET CHANGEBYPHONENO=''',PCHANGEBYPHONENO,'''  ');

				IF LENGTH(PDESC)>0 THEN
				SET @QUERY=CONCAT(@QUERY,' ,DESCRITION=''',PDESC,''' ');
				END IF;
				/*IF LENGTH(PGROUPTYPEVAR)>0 THEN
				SELECT GETGROUPTYPE(PGROUPTYPEVAR) INTO L_GROUPTYPEID;
				SET @QUERY=CONCAT(@QUERY,' ,GROUPTYPE= ''',L_GROUPTYPEID, '''');
				END IF;*/
				IF LENGTH(PTAGNAME)>0 THEN
				SET @QUERY=CONCAT(@QUERY,' ,TAGNAME=''',PTAGNAME,''' ');
				END IF;
				IF LENGTH(PGROUPPHOTO)>0 THEN
				SET @QUERY=CONCAT(@QUERY,' ,GROUPPHOTO= ''',PGROUPPHOTO,''' ');
				END IF;
                  SET @QUERY=CONCAT(@QUERY,' , CREATEUPDATETS= UTC_TIMESTAMP()');
				SET @QUERY=CONCAT(@QUERY,' WHERE  GROUPID =',PGROUPID,'');

				PREPARE QUERY FROM @QUERY;
				EXECUTE QUERY ;
				deallocate prepare QUERY;

                 COMMIT;
					SELECT CONCAT(GROUPNAME ,' ',DESCRITION,' ',TAGNAME) INTO L_SEARCH
					FROM XERUNG.GROUP_INFO WHERE GROUPID=PGROUPID;
					UPDATE GROUP_INFO SET GROUPSEARCH=L_SEARCH
					WHERE GROUPID=PGROUPID;


				SET POUTV='{"STATUS":"SUCCESS"}';

ELSEIF PGROUPID>0 AND AFLAG =2 THEN

	CALL INSERTGROUPMEMBER(PMADEBY,3,PUID,PGROUPID,AGROUPMEMBERLIST);
	SET POUTV='{"STATUS":"SUCCESS"}';

/*ELSEIF AFLAG =3 AND PGROUPID>0 THEN

CALL INSERTGROUPMEMBER(PMADEBY,0,PUID,PGROUPID,AGROUPMEMBERLIST);
SET POUTV='{"STATUS":"SUCCESS"}';
*/
END IF;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `DELETEGROUPMEMBER` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = '' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`%` PROCEDURE `DELETEGROUPMEMBER`(
IN PUID INTEGER,
IN PGROUPID INTEGER,
IN PPHONENUMBER VARCHAR(20),
OUT POUT TEXT
)
BEGIN

	DECLARE ASUPERADMIN INTEGER default 0;
    DECLARE AUSER INTEGER default 0;
    SET SQL_SAFE_UPDATES = 0;
    
	SELECT COUNT(1) INTO ASUPERADMIN FROM GROUP_INFO WHERE GROUPID = PGROUPID AND MADEBYPHONENO=PPHONENUMBER;
	
    IF ASUPERADMIN>0 THEN
    
		SET POUT='{"STATUS":"FAILED"}';
        
	ELSEIF ASUPERADMIN=0 THEN
    
    	SELECT COUNT(1) INTO AUSER FROM GROUP_MEMBER_DETAILS WHERE GROUPID=PGROUPID AND PHONENUMBER=PPHONENUMBER;
		IF AUSER>0 THEN
			DELETE FROM GROUP_MEMBER_DETAILS WHERE GROUPID=PGROUPID AND PHONENUMBER=PPHONENUMBER;
            DELETE FROM INVITATION_DETAILS WHERE INVITATIONSENDTO=PPHONENUMBER AND GROUPID=PGROUPID;
			UPDATE GROUP_INFO SET CREATEUPDATETS=UTC_TIMESTAMP();
			SET POUT='{"STATUS":"SUCCESS"}';
        
		ELSEIF AUSER=0 THEN
    
			SET POUT='{"STATUS":"USER DOES NOT EXIST"}';
        
		END IF;        
	END IF;

END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `FETCHBACKUP` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = '' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`%` PROCEDURE `FETCHBACKUP`(
  IN PUID INTEGER,
  OUT POUT LONGTEXT
)
BEGIN
DECLARE L_BACKUP LONGTEXT;
SELECT BACKUP INTO L_BACKUP FROM MIPHONEBACKUP WHERE UID=PUID;

  SET POUT= CONCAT('{"UID":',PUID,' ,"BACKUP":',ifnull(L_BACKUP,0),'}');

END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `FETCHCITYLIST` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = '' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`%` PROCEDURE `FETCHCITYLIST`(
IN PCOUNTRYID INTEGER,
OUT P_OUT TEXT
)
BEGIN


SET SESSION group_concat_max_len =18446744073709551615;

SELECT GROUP_CONCAT('{"CITYID":',CITYID,' ,"CITYNAME":"',CITYNAME,'"}') INTO P_OUT FROM (
SELECT CITYID,CITYNAME FROM XERUNG.MASTERCITY WHERE COUNTRYID=PCOUNTRYID
)AS T;

SET P_OUT=CONCAT('[',P_OUT,']');

END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `FETCHGROUPMEMBER` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = '' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`%` PROCEDURE `FETCHGROUPMEMBER`(
IN PUID INTEGER,
IN PTIMESTAMP VARCHAR(100),
IN PGROUPID INTEGER,

OUT POUT longtext
)
BEGIN

DECLARE L_PHONENUMBER VARCHAR(20) default NULL;
DECLARE L_ADMINCHECK INTEGER default 0;
DECLARE L_GROUPIDIN VARCHAR(100);
DECLARE L_MEMCOUNT INTEGER default 0;

 SET SESSION group_concat_max_len = 18446744073709551615;

SELECT PHONENUMBER INTO L_PHONENUMBER FROM USER_INFO  WHERE USER_INFO.UID=PUID;  

IF PGROUPID =0 then
SELECT  GROUP_CONCAT(DISTINCT GROUPID) INTO L_GROUPIDIN FROM GROUP_MEMBER_DETAILS WHERE 
PHONENUMBER=L_PHONENUMBER;


SELECT GROUP_CONCAT('{"MADEBYPHONENO":"',ifnull(MADEBYPHONENO,''),'","MADEBYNAME":"',ifnull(NAME,''),'"
,"GROUPID":"',ifnull(GROUPID,0),'","GROUPNAME":"',IFNULL(GROUPNAME,''),'","TAGNAME":"',ifnull(TAGNAME,''),'","DESCRITION":"',ifnull(DESCRITION,''),'"
,"GROUPPHOTO":"',IFNULL(TRIM(REPLACE(REPLACE(REPLACE(GROUPPHOTO, '\n', ' '), '\r', ' '), '\t', ' ')),''),'","MEMBERCOUNT":"',CNT,'","ADMINFLAG":"',ADMINFLAG,'","GROUPACCESSTYPE":"1"}')
  INTO @POUT FROM (
SELECT USER_INFO.NAME,GROUP_INFO.MADEBYPHONENO,GROUP_INFO.GROUPID,GROUP_INFO.GROUPNAME,
GROUP_INFO.TAGNAME,GROUP_INFO.DESCRITION,
GROUP_INFO.GROUPPHOTO ,
(SELECT COUNT(1) FROM GROUP_MEMBER_DETAILS WHERE GROUPID=GROUP_INFO.GROUPID
AND REGISTERFLAG=1 AND STATUSID=1) AS CNT
-- ,CASE WHEN GROUP_INFO.MADEBYPHONENO=L_PHONENUMBER THEN 1 ELSE 0 END AS ADMINFLAG
,CASE WHEN GROUP_MEMBER_DETAILS.ACCESSTYPEID=1 THEN 1 ELSE 0 END AS ADMINFLAG
from  GROUP_INFO 
INNER JOIN USER_INFO USER_INFO 
ON (USER_INFO.PHONENUMBER=GROUP_INFO.MADEBYPHONENO)
INNER JOIN GROUP_MEMBER_DETAILS GROUP_MEMBER_DETAILS 
ON (GROUP_MEMBER_DETAILS.GROUPID=GROUP_INFO.GROUPID)
where   GROUP_MEMBER_DETAILS.PHONENUMBER=L_PHONENUMBER AND 
GROUP_INFO.GROUPACCESSTYPE=1 )AS T;



 SET POUT=CONCAT('[',ifnull(@POUT,''),']');
ELSE

SET L_GROUPIDIN=PGROUPID;

IF LENGTH(L_GROUPIDIN)>0 THEN

SELECT COUNT(*) INTO L_MEMCOUNT FROM GROUP_MEMBER_DETAILS  WHERE PHONENUMBER=L_PHONENUMBER AND GROUPID=PGROUPID;

IF L_MEMCOUNT>0 THEN

SET @QUERY=CONCAT('SELECT @POUT:=GROUP_CONCAT("{""GROUPID"":",GROUPID," ,""GROUPNAME"":""",GROUPNAME,""" 
,""TAGNAME"":""",TAGNAME,""",""DESCRITION"":""",DESCRITION,""",""GROUPPHOTO"":""",IFNULL(TRIM(REPLACE(REPLACE(REPLACE(GROUPPHOTO, ''\n'', '' ''), ''\r'', '' ''), ''\t'', '' '')),''''),""",
""MEMBER"":""",IFNULL(MEMBER,''''),""",""ADMINFLAG"":",ADMINFLAG,"}") FROM (');

SET @QUERY=CONCAT(@QUERY,'select 1 AS UID,GROUPID,GROUPNAME,TAGNAME,DESCRITION,GROUPPHOTO,
GROUP_CONCAT(CONCAT_WS(''|'',MNAME,PHONENUMBER,ACCESSTYPEID,
IFNULL(CONCAT(IFNULL(ADDRESS,'''') ,'' '',IFNULL(LCITYNAME,''''),'' '',IFNULL(MNAME,''''),'' '',IFNULL(COMPANYNAME,'''')),''NULL''),UID,IFNULL(BLOODGROUP,''''),IFNULL(LCITYNAME,''''),IFNULL(PROFESSION,'''')) SEPARATOR ''#'') AS  MEMBER
,CASE WHEN MADEBYPHONENO=',L_PHONENUMBER,' THEN 1 ELSE 0 END AS ADMINFLAG  from (
SELECT GROUP_INFO.MADEBYPHONENO,GROUP_INFO.GROUPID,GROUP_INFO.GROUPNAME,GROUP_INFO.TAGNAME,GROUP_INFO.DESCRITION,
GROUP_INFO.GROUPPHOTO,
GROUP_MEMBER_DETAILS.PHONENUMBER ,GROUP_MEMBER_DETAILS.ACCESSTYPEID
,GROUP_MEMBER_DETAILS.REGISTERFLAG,USER_INFO.NAME AS MNAME
,USER_INFO.ADDRESS,(select CITYNAME FROM MASTERCITY WHERE CITYID=USER_INFO.CITYID)as LCITYNAME
,USER_INFO.PROFESSION,USER_INFO.UID,USER_INFO.COMPANYNAME,USER_INFO.BLOODGROUP
FROM GROUP_MEMBER_DETAILS GROUP_MEMBER_DETAILS
LEFT OUTER JOIN GROUP_INFO GROUP_INFO ON (GROUP_MEMBER_DETAILS.GROUPID=GROUP_INFO.GROUPID)
INNER JOIN USER_INFO USER_INFO 
ON (USER_INFO.PHONENUMBER=GROUP_MEMBER_DETAILS.PHONENUMBER)
WHERE GROUP_MEMBER_DETAILS.REGISTERFLAG=1 AND GROUP_MEMBER_DETAILS.STATUSID=1
  AND GROUP_INFO.GROUPID IN (',L_GROUPIDIN,')   
');
    

        SET @QUERY=CONCAT(@QUERY,' ) AS T GROUP BY GROUPID ) AS X ');
 
                PREPARE QUERY FROM @QUERY;
				EXECUTE QUERY ;
				deallocate prepare QUERY;

 SET POUT=CONCAT('[',ifnull(@POUT,''),']');
ELSE SET POUT=CONCAT('[]');
END IF;
  else
 SET POUT=CONCAT('[]');
END IF;
END IF;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `FETCHGROUPMEMBERDELTA` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = '' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`%` PROCEDURE `FETCHGROUPMEMBERDELTA`(
IN PUID INTEGER,
IN PTIMESTAMP VARCHAR(100),
IN PGROUPID INTEGER,
IN PPAGENUMBER INTEGER,
IN PRECORDCOUNT INTEGER,

OUT POUT longtext
)
BEGIN

DECLARE L_PHONENUMBER VARCHAR(20) default NULL;
DECLARE L_ADMINCHECK INTEGER default 0;
DECLARE L_GROUPIDIN VARCHAR(100);
DECLARE L_SKIPRECORD INTEGER;
DECLARE L_ADMINFLAG INTEGER;
DECLARE L_GROUPCOUNT INTEGER default 0;
DECLARE L_MEMCOUNT INTEGER default 0;

 SET L_SKIPRECORD = (PPAGENUMBER-1)*PRECORDCOUNT;

 SET SESSION group_concat_max_len = 18446744073709551615;

SELECT PHONENUMBER INTO L_PHONENUMBER FROM USER_INFO  WHERE USER_INFO.UID=PUID;  

IF PGROUPID =0 then
select COUNT(DISTINCT GROUPID) INTO L_GROUPCOUNT FROM GROUP_MEMBER_DETAILS WHERE 
PHONENUMBER=L_PHONENUMBER;
-- SELECT COUNT(1) INTO L_GROUPCOUNT FROM GROUP_INFO WHERE MADEBYPHONENO=L_PHONENUMBER;
SELECT  GROUP_CONCAT(DISTINCT GROUPID) INTO L_GROUPIDIN FROM GROUP_MEMBER_DETAILS WHERE 
PHONENUMBER=L_PHONENUMBER;


IF LENGTH(PTIMESTAMP)>0 THEN

SELECT GROUP_CONCAT('{"MADEBYPHONENO":"',ifnull(MADEBYPHONENO,''),'","MADEBYNAME":"',ifnull(NAME,''),'"
,"GROUPID":"',ifnull(GROUPID,0),'","GROUPNAME":"',IFNULL(GROUPNAME,''),'","TAGNAME":"',ifnull(TAGNAME,''),'","DESCRITION":"',ifnull(DESCRITION,''),'"
,"GROUPPHOTO":"',IFNULL(TRIM(REPLACE(REPLACE(REPLACE(GROUPPHOTO, '\n', ' '), '\r', ' '), '\t', ' ')),''),'","MEMBERCOUNT":"',CNT,'","ADMINFLAG":"',ADMINFLAG,'","GROUPACCESSTYPE":"1"
,"CREATEUPDATETS":"',CREATEUPDATETS,'"}')
  INTO @POUT FROM (
SELECT USER_INFO.NAME,GROUP_INFO.MADEBYPHONENO,GROUP_INFO.GROUPID,GROUP_INFO.GROUPNAME,
GROUP_INFO.TAGNAME,GROUP_INFO.DESCRITION,
GROUP_INFO.GROUPPHOTO ,
(SELECT COUNT(1) FROM GROUP_MEMBER_DETAILS WHERE GROUPID=GROUP_INFO.GROUPID
AND REGISTERFLAG=1 AND STATUSID=1) AS CNT
-- ,CASE WHEN GROUP_INFO.MADEBYPHONENO=L_PHONENUMBER THEN 1 ELSE 0 END AS ADMINFLAG
,CASE WHEN GROUP_MEMBER_DETAILS.ACCESSTYPEID=1 THEN 1 ELSE 0 END AS ADMINFLAG
,GROUP_INFO.CREATEUPDATETS
from  GROUP_INFO 
INNER JOIN USER_INFO USER_INFO 
ON (USER_INFO.PHONENUMBER=GROUP_INFO.MADEBYPHONENO)
INNER JOIN GROUP_MEMBER_DETAILS GROUP_MEMBER_DETAILS 
ON (GROUP_MEMBER_DETAILS.GROUPID=GROUP_INFO.GROUPID)
where   GROUP_MEMBER_DETAILS.PHONENUMBER=L_PHONENUMBER AND 
GROUP_INFO.GROUPACCESSTYPE=1
AND GROUP_INFO.CREATEUPDATETS>=PTIMESTAMP 
order by GROUP_INFO.CREATEUPDATETS  LIMIT L_SKIPRECORD ,PRECORDCOUNT

)AS T;
else
SELECT GROUP_CONCAT('{"MADEBYPHONENO":"',ifnull(MADEBYPHONENO,''),'","MADEBYNAME":"',ifnull(NAME,''),'"
,"GROUPID":"',ifnull(GROUPID,0),'","GROUPNAME":"',IFNULL(GROUPNAME,''),'","TAGNAME":"',ifnull(TAGNAME,''),'","DESCRITION":"',ifnull(DESCRITION,''),'"
,"GROUPPHOTO":"',IFNULL(TRIM(REPLACE(REPLACE(REPLACE(GROUPPHOTO, '\n', ' '), '\r', ' '), '\t', ' ')),''),'","MEMBERCOUNT":"',CNT,'","ADMINFLAG":"',ADMINFLAG,'","GROUPACCESSTYPE":"1"
,"CREATEUPDATETS":"',CREATEUPDATETS,'"}')
  INTO @POUT FROM (
SELECT USER_INFO.NAME,GROUP_INFO.MADEBYPHONENO,GROUP_INFO.GROUPID,GROUP_INFO.GROUPNAME,
GROUP_INFO.TAGNAME,GROUP_INFO.DESCRITION,
GROUP_INFO.GROUPPHOTO ,
(SELECT COUNT(1) FROM GROUP_MEMBER_DETAILS WHERE GROUPID=GROUP_INFO.GROUPID
AND REGISTERFLAG=1 AND STATUSID=1) AS CNT
-- ,CASE WHEN GROUP_INFO.MADEBYPHONENO=L_PHONENUMBER THEN 1 ELSE 0 END AS ADMINFLAG
,CASE WHEN GROUP_MEMBER_DETAILS.ACCESSTYPEID=1 THEN 1 ELSE 0 END AS ADMINFLAG
,GROUP_INFO.CREATEUPDATETS
from  GROUP_INFO 
INNER JOIN USER_INFO USER_INFO 
ON (USER_INFO.PHONENUMBER=GROUP_INFO.MADEBYPHONENO)
INNER JOIN GROUP_MEMBER_DETAILS GROUP_MEMBER_DETAILS 
ON (GROUP_MEMBER_DETAILS.GROUPID=GROUP_INFO.GROUPID)
where   GROUP_MEMBER_DETAILS.PHONENUMBER=L_PHONENUMBER AND 
GROUP_INFO.GROUPACCESSTYPE=1 order by GROUP_INFO.CREATEUPDATETS LIMIT L_SKIPRECORD ,PRECORDCOUNT
 )AS T;

END IF;

 SET POUT=CONCAT('',L_GROUPCOUNT,' ~ [',ifnull(@POUT,''),']');
ELSE

SET L_GROUPIDIN=PGROUPID;

IF LENGTH(L_GROUPIDIN)>0 THEN
-- """,""ADDEDTIMESTAMP"":""",IFNULL(ADDEDTIMESTAMP,''''),"""


select CASE WHEN MADEBYPHONENO=L_PHONENUMBER THEN 1 ELSE 0 END INTO  L_ADMINFLAG from GROUP_INFO 
where GROUP_INFO.GROUPID IN (L_GROUPIDIN) ;

SET @QUERY=CONCAT('SELECT @POUT:=GROUP_CONCAT("{""MEMBER"":""",IFNULL(MEMBER,''''),""",""ADMINFLAG"":",',L_ADMINFLAG,',"}") FROM (');

SELECT COUNT(*) INTO L_MEMCOUNT FROM GROUP_MEMBER_DETAILS  WHERE PHONENUMBER=L_PHONENUMBER AND GROUPID=PGROUPID; 

 IF L_MEMCOUNT>0 THEN

SET @QUERY=CONCAT(@QUERY,'select 
GROUP_CONCAT(CONCAT_WS(''|'',MNAME,PHONENUMBER,ACCESSTYPEID,
IFNULL(CONCAT(IFNULL(ADDRESS,'''') ,'' '',IFNULL(LCITYNAME,''''),'' '',IFNULL(MNAME,''''),'' '',IFNULL(COMPANYNAME,'''')),''NULL'')
,UID,IFNULL(BLOODGROUP,''''),IFNULL(LCITYNAME,'''')
,IFNULL(PROFESSION,''''),IFNULL(ADDEDTIMESTAMP,'''')
) SEPARATOR ''#'') AS  MEMBER
 from (
SELECT GROUP_MEMBER_DETAILS.GROUPID,
GROUP_MEMBER_DETAILS.PHONENUMBER ,GROUP_MEMBER_DETAILS.ACCESSTYPEID
,GROUP_MEMBER_DETAILS.REGISTERFLAG,USER_INFO.NAME AS MNAME
,USER_INFO.ADDRESS,(select CITYNAME FROM MASTERCITY WHERE CITYID=USER_INFO.CITYID)as LCITYNAME
,USER_INFO.PROFESSION,USER_INFO.UID,USER_INFO.COMPANYNAME,USER_INFO.BLOODGROUP
,GROUP_MEMBER_DETAILS.ADDEDTIMESTAMP
FROM GROUP_MEMBER_DETAILS GROUP_MEMBER_DETAILS

INNER JOIN USER_INFO USER_INFO 
ON (USER_INFO.PHONENUMBER=GROUP_MEMBER_DETAILS.PHONENUMBER)
WHERE GROUP_MEMBER_DETAILS.REGISTERFLAG=1 AND GROUP_MEMBER_DETAILS.STATUSID=1
  AND GROUP_MEMBER_DETAILS.GROUPID IN (',L_GROUPIDIN,')');
-- LEFT OUTER JOIN GROUP_INFO GROUP_INFO ON (GROUP_MEMBER_DETAILS.GROUPID=GROUP_INFO.GROUPID)
-- GROUP_INFO.MADEBYPHONENO,GROUP_INFO.GROUPID,GROUP_INFO.GROUPNAME,GROUP_INFO.TAGNAME,GROUP_INFO.DESCRITION,GROUP_INFO.GROUPPHOTO,
IF LENGTH(PTIMESTAMP)>0 THEN
    SET @QUERY=CONCAT(@QUERY,' AND GROUP_MEMBER_DETAILS.ADDEDTIMESTAMP>="',PTIMESTAMP,'"');
 END IF;

        SET @QUERY=CONCAT(@QUERY,'  order by GROUP_MEMBER_DETAILS.ADDEDTIMESTAMP  LIMIT ',L_SKIPRECORD,' ,',PRECORDCOUNT,'
                  ) AS T GROUP BY GROUPID ) AS X ');
             PREPARE QUERY FROM @QUERY;
				EXECUTE QUERY ;
				deallocate prepare QUERY;

 SET POUT=CONCAT('[',ifnull(@POUT,''),']');
 else  
 SET POUT=CONCAT('[]');
 END IF;
else
 SET POUT=CONCAT('[]');
END IF;
END IF;

END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `FETCHINVITATIONDETAILS` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = '' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`%` PROCEDURE `FETCHINVITATIONDETAILS`(
IN PUID INTEGER,
IN PGROUPID INTEGER,
IN PCOUNTRYCODEID INTEGER,
IN PFLAG INTEGER,
IN PSTATUSID INTEGER,
IN ADMINFLAG INTEGER,
OUT POUT TEXT

)
BEGIN

DECLARE L_PHONENUMBER VARCHAR(20);
DECLARE L_PHONENOMAXLEN INT default 0;
DECLARE L_INVCOUNT INTEGER default 0;


SELECT PHONENUMBER INTO L_PHONENUMBER FROM USER_INFO USER_INFO WHERE USER_INFO.UID=PUID;  
SELECT PHONENOMAXLEN INTO L_PHONENOMAXLEN FROM MASTER_COUNTRYCODE 
WHERE COUNTRYCODEID=PCOUNTRYCODEID;

IF PFLAG=1 THEN

  -- IF ADMINFLAG=1 then
	

	    SELECT CONCAT('[',IFNULL(SENDTO,''),']') INTO @SENDTO FROM (
		SELECT  GROUP_CONCAT('{"INVITATIONSENDTO":"',INVITATION_DETAILS.INVITATIONSENDTO,'"}')  AS SENDTO
		FROM XERUNG.INVITATION_DETAILS INVITATION_DETAILS
		WHERE INVITATIONSENDBY=L_PHONENUMBER 
       AND INVITATIONSENDTO!=L_PHONENUMBER
        AND GROUPID=PGROUPID 
		AND INVITATIONFLAGID=1
		AND INVITATION_DETAILS.STATUSID=2) AS T;

        SET POUT=CONCAT(@SENDTO);



ELSEIF PFLAG=2 then

SELECT COUNT(1) INTO L_INVCOUNT FROM INVITATION_DETAILS WHERE INVITATIONSENDTO=L_PHONENUMBER
AND STATUSID=2 and  INVITATIONSENDBY!=L_PHONENUMBER;

	SELECT CONCAT('[',IFNULL(DATAB,'{}'),',{"COUNT":"',L_INVCOUNT,'"}]') INTO @SENDBY FROM (
	SELECT GROUP_CONCAT('{"GROUPNAME":"',GROUPNAME,'","INVITATIONSENDBY":"',INVITATIONSENDBY,'","NAME":"',NAME,'","GROUPID":"',GROUPID,'","INVITATIONFLAGID":"',INVITATIONFLAGID,'"}') AS DATAB FROM (
	SELECT INVITATION_DETAILS.INVITATIONSENDBY 
	,USER_INFO.NAME,GROUP_INFO.GROUPNAME,GROUP_INFO.GROUPID,INVITATION_DETAILS.INVITATIONFLAGID
	FROM XERUNG.INVITATION_DETAILS INVITATION_DETAILS
	INNER JOIN USER_INFO USER_INFO ON USER_INFO.PHONENUMBER=INVITATION_DETAILS.INVITATIONSENDBY
	INNER JOIN GROUP_INFO GROUP_INFO ON GROUP_INFO.GROUPID=INVITATION_DETAILS.GROUPID
	WHERE INVITATION_DETAILS.INVITATIONSENDTO=L_PHONENUMBER 
	AND INVITATION_DETAILS.INVITATIONFLAGID in (1,2) 
   -- AND INVITATION_DETAILS.REGISTERFLAG=1
	AND INVITATION_DETAILS.STATUSID=2 
    and  INVITATION_DETAILS.INVITATIONSENDBY!=L_PHONENUMBER
    and  INVITATION_DETAILS.INVITATIONSENDTO=L_PHONENUMBER
	) AS T) AS X;
 SET POUT=CONCAT(@SENDBY);
END IF;

END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `FETCHSENDMSGANDNOTIFICATIONDATA` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = '' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`%` PROCEDURE `FETCHSENDMSGANDNOTIFICATIONDATA`(
 IN PSTRING TEXT,
 IN PFLAG INTEGER,
 IN PPHONETYPE VARCHAR(2),
 OUT POUT VARCHAR(1000)
)
BEGIN
DECLARE L_NUMBER VARCHAR(50);
DECLARE L_NAME VARCHAR(100);
DECLARE L_REGISTERFLAG INTEGER DEFAULT 0;
DECLARE COUNTVAR INTEGER ;
DECLARE DELIMITVAR INTEGER ;
DECLARE LNOLIST VARCHAR(100);

	-- SELECT MITYUNGINFRAONE.STRSPLITFF1(PSTRING,1) INTO L_STRING;
	SELECT STRSPLITFF(PSTRING,1) INTO DELIMITVAR;

	SET COUNTVAR=2;
	IF DELIMITVAR<>0 THEN

		BEGIN
			WHILE COUNTVAR < DELIMITVAR  DO

				BEGIN
					SET L_NAME=STRSPLITFF(PSTRING,COUNTVAR);
					SET L_NUMBER=STRSPLITFF(PSTRING,COUNTVAR+1);
                    SELECT L_NUMBER;
                  IF LENGTH(LNOLIST)>0 THEN
					SET LNOLIST=CONCAT(LNOLIST,',', L_NUMBER);
			      else
                  SET LNOLIST=CONCAT(L_NUMBER);
                END IF;
					SET COUNTVAR=COUNTVAR+2; 
				END;
			END WHILE; 
		END;
	END IF;
SELECT LNOLIST;

END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `GETCOUNTRYDATA` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = '' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`%` PROCEDURE `GETCOUNTRYDATA`(
IN PFLAG INTEGER,
OUT POUT TEXT
)
BEGIN
SET SESSION group_concat_max_len = 18446744073709551615;
SELECT CONCAT('[',CDATA,']') into POUT FROM (
SELECT GROUP_CONCAT('{"COUNTRYCODEID":',COUNTRYCODEID,',"PHONECOUNTRYCODE":"',COUNTRYCODE,'",
"COUNTRYNAME":"',COUNTRYNAME,'","PHONENOMAXLEN":"',IFNULL(PHONENOMAXLEN,0),'"}') AS CDATA
FROM (SELECT COUNTRYCODEID,COUNTRYCODE,COUNTRYNAME,PHONENOMAXLEN FROM MASTER_COUNTRYCODE
WHERE COUNTRYCODE IS NOT NULL) AS X) AS T;


END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `GETGLOBALNOTIFDATA` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = '' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`%` PROCEDURE `GETGLOBALNOTIFDATA`(
IN PVERSION  varchar(10),
IN PPHONETYPE varchar(10),
OUT POUT LONGTEXT
)
BEGIN

/*
PPHONETYPE=1 FOR ANDROID
PPHONETYPE=2 FOR IOS
*/

 select group_concat(NOTIFICATION_KEY separator '~') into POUT from PUSH_NOTIFICATION_KEY 
where VERSION=PVERSION and PHONETYPE=PPHONETYPE;
   

END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `GETNOTIFICATIONDATA` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = '' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`%` PROCEDURE `GETNOTIFICATIONDATA`(
IN PPHONENUMBER VARCHAR(20),
IN PFLAG INTEGER,
IN PPHONETYPE INTEGER,
OUT POUT TEXT

)
BEGIN
 /*
  THIS PROCEDURE IS MADE TO FETCH THE NOTIFICATION DATA
*/

  SET SESSION group_concat_max_len = 18446744073709551615;

IF PFLAG=1 THEN
SELECT CONCAT_WS('~',UID,upper(NAME)) INTO @ADMINDATA FROM USER_INFO WHERE PHONENUMBER=PPHONENUMBER;


 SELECT CONCAT('',@ADMINDATA,'#[',ifnull(NOTIDATA,''),']') INTO POUT FROM(
SELECT GROUP_CONCAT('{"INVITATIONSENDTO":"',INVITATIONSENDTO,'","NOTIFICATION_KEY":"',
CASE WHEN REGISTERFLAG=1 then (SELECT NOTIFICATION_KEY FROM PUSH_NOTIFICATION_KEY 
where MOBILENO=INVITATIONSENDTO)else 0 end 
,'","INVITATIONFLAGID":"',INVITATIONFLAGID,'","GROUPNAME":"',upper(GROUPNAME),'","GROUPID":"',GROUPID,'","TAGNAME":"',TAGNAME,'"
,"PHONETYPEID":"',CASE WHEN REGISTERFLAG=1 then (SELECT PHONETYPE FROM PUSH_NOTIFICATION_KEY 
where MOBILENO=INVITATIONSENDTO)else 0 end ,'"}') 
AS NOTIDATA
 from (
SELECT INVITATIONSENDTO ,REGISTERFLAG,INVITATIONFLAGID ,GROUP_INFO.GROUPNAME,
GROUP_INFO.GROUPID,GROUP_INFO.TAGNAME
FROM INVITATION_DETAILS INVITATION_DETAILS
INNER JOIN GROUP_INFO GROUP_INFO ON GROUP_INFO.GROUPID=INVITATION_DETAILS.GROUPID
WHERE INVITATION_DETAILS.INVITATIONSENDBY=PPHONENUMBER
AND INVITATION_DETAILS.INVITATIONSENDTO!=PPHONENUMBER
AND NOTIFICATIONID=0 ) as t) AS X;

ELSE

UPDATE INVITATION_DETAILS SET NOTIFICATIONID=1 
WHERE INVITATION_DETAILS.INVITATIONSENDBY=PPHONENUMBER AND NOTIFICATIONID=0;
SET POUT='1';
END IF;

END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `GetOtpNumber` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = '' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`%` PROCEDURE `GetOtpNumber`(
  IN PPHONENUMBER VARCHAR(15), 
   OUT POUT  VARCHAR(20)
)
BEGIN
DECLARE L_OTPID INTEGER  default 0;
    SELECT OTPID INTO L_OTPID FROM USER_INFO WHERE PHONENUMBER =PPHONENUMBER;
    SET POUT=L_OTPID;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `GROUPSEARCH` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = '' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`%` PROCEDURE `GROUPSEARCH`(
IN PGROUPID INTEGER,
IN PSERACHTEXT VARCHAR(100),
IN PGROUPACCESSTYPE INTEGER,
OUT POUT LONGTEXT
)
BEGIN
/*
 THIS PROCEDURE IS MADE TO SEARCH THE GROUP AND ITS MEMBER
PGROUPACCESSTYPE=1 FOR PRIVATE GROUP
PGROUPACCESSTYPE=2 FOR PUBLIC GROUP
*/
SET SESSION group_concat_max_len = 18446744073709551615;

 IF PGROUPID=0 and LENGTH(PSERACHTEXT)>0 then
 -- SEARCH FROM KEY BOTH PUBLIC AND PRIVATE
		SELECT CONCAT('[',ifnull(GROUPDETAIL,''),']') INTO POUT FROM (
		SELECT GROUP_CONCAT("{""GROUPID"":",GROUPID," ,""GROUPNAME"":""",GROUPNAME,""" 
		,""TAGNAME"":""",TAGNAME,""",""DESCRITION"":""",DESCRITION,""",
        ""GROUPPHOTO"":""",IFNULL(TRIM(REPLACE(REPLACE(REPLACE(GROUPPHOTO, '\n', ' '), '\r', ' '), '\t', ' ')),''),""",""GROUPACCESSTYPE"":""",GROUPACCESSTYPE,"""}") AS GROUPDETAIL FROM (
		SELECT GROUPID,GROUPNAME,DESCRITION,TAGNAME,GROUPPHOTO,GROUPACCESSTYPE FROM GROUP_INFO 
		WHERE MATCH (GROUPSEARCH)
		AGAINST (CONCAT('+',PSERACHTEXT,'') IN BOOLEAN MODE) ) as t) AS X;

 ELSEIF PGROUPID=0 AND LENGTH(PSERACHTEXT)=0 then
-- RETURN ALL PUBLIC GROUP
		SELECT CONCAT('[',ifnull(GROUPDETAIL,''),']') INTO POUT FROM (
		SELECT GROUP_CONCAT("{""GROUPID"":""",GROUPID,""" ,""GROUPNAME"":""",GROUPNAME,""" 
		,""TAGNAME"":""",TAGNAME,""",""DESCRITION"":""",DESCRITION,""",""GROUPPHOTO"":""",
         IFNULL(TRIM(REPLACE(REPLACE(REPLACE(GROUPPHOTO, '\n', ' '), '\r', ' '), '\t', ' ')),''),""",""GROUPACCESSTYPE"":""",GROUPACCESSTYPE,"""}") AS GROUPDETAIL FROM (
		SELECT GROUPID,GROUPNAME,DESCRITION,TAGNAME,GROUPPHOTO,GROUPACCESSTYPE FROM GROUP_INFO 
		WHERE  GROUPACCESSTYPE=PGROUPACCESSTYPE) as t) AS X;



   ELSEIF PGROUPID>0 AND PGROUPACCESSTYPE=1 then
      -- RETURN ALL PRIVATE GROUP MEMBER GROUP
		SELECT CONCAT('[',ifnull(ADMINMEMBERS,''),']')INTO POUT FROM (
		SELECT group_concat('{"NAME":"',USER_INFO.NAME,'","PHONENUMBER":"',GROUP_MEMBER_DETAILS.PHONENUMBER,'"}') 
		AS ADMINMEMBERS  FROM GROUP_MEMBER_DETAILS GROUP_MEMBER_DETAILS
		INNER JOIN USER_INFO USER_INFO ON USER_INFO.PHONENUMBER=GROUP_MEMBER_DETAILS.PHONENUMBER
		WHERE GROUPID=PGROUPID AND ACCESSTYPEID=1) AS X;



  ELSEIF PGROUPID>0 AND PGROUPACCESSTYPE=2 AND LENGTH(PSERACHTEXT)=0 then
 -- RETURN ALL PUBLIC GROUP MEMBER GROUP
-- select 'hello';
			SELECT CONCAT('[',ifnull(MEMBERS,''),']')INTO POUT FROM (
			SELECT group_concat('{"NAME":"',CONTACTNAME,'","PHONENUMBER":"',CONTACTNO,'"
			,"ADDRESS":"',TRIM(REPLACE(REPLACE(REPLACE(ADDRESS, '\n', ' '), '\r', ' '), '\t', '')),'" ,"CITYNAME":"',CITYNAME,'"}') 
			-- ,"SEARCHKEY":"',SEARCHKEY,'"
			AS MEMBERS  FROM 
			(SELECT CONTACTNAME,CONTACTNO,ADDRESS,CITYNAME,SEARCHKEY
			FROM PUBLICGROUPCONTACTS
			WHERE GROUPID=PGROUPID limit 0,15)as t
			) AS X;

 ELSEIF PGROUPID>0 AND PGROUPACCESSTYPE=2 AND LENGTH(PSERACHTEXT)>0 then
               -- SEARCH  PUBLIC GROUP MEMBER 
			SELECT CONCAT('[',ifnull(MEMBER,''),']') INTO POUT FROM (
			SELECT group_concat('{"NAME":"',CONTACTNAME,'","PHONENUMBER":"',CONTACTNO,'"
			,"ADDRESS":"',ADDRESS,'" ,"CITYNAME":"',CITYNAME,'"}')
			-- ,"SEARCHKEY":"',SEARCHKEY,'"  
			AS MEMBER FROM 
			(SELECT CONTACTNAME,CONTACTNO,
            TRIM(REPLACE(REPLACE(REPLACE(ADDRESS, '\n', ' '), '\r', ' '), '\t', ' ')) as ADDRESS,CITYNAME,SEARCHKEY
			FROM PUBLICGROUPCONTACTS
			WHERE MATCH (SEARCHKEY)
			AGAINST (CONCAT('+',PSERACHTEXT,'') IN BOOLEAN MODE)  and 
			GROUPID=PGROUPID limit 0,15)as t
			) AS X;

END IF;

END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `INSERTGROUPMEMBER` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = '' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`%` PROCEDURE `INSERTGROUPMEMBER`(
IN PSENDBY VARCHAR(50),
IN PFLAG INTEGER,
IN PUID INTEGER,
IN PGROUPID INTEGER,
IN PSTRING TEXT
)
BEGIN
 DECLARE L_NUMBER VARCHAR(50);
 DECLARE L_NAME VARCHAR(100);
 DECLARE L_REGISTERFLAG INTEGER DEFAULT 0;
DECLARE L_STRING TEXT;

DECLARE COUNTVAR INTEGER ;
DECLARE DELIMITVAR INTEGER ;

	-- SELECT MITYUNGINFRAONE.STRSPLITFF1(PSTRING,1) INTO L_STRING;
	SELECT STRSPLITFF(PSTRING,1) INTO DELIMITVAR;

	SET COUNTVAR=2;
IF DELIMITVAR<100 THEN
	IF DELIMITVAR<>0 THEN

		BEGIN
			WHILE COUNTVAR < DELIMITVAR-2  DO

				BEGIN
                     SET L_NAME=STRSPLITFF(PSTRING,COUNTVAR);
					 SET L_NUMBER=STRSPLITFF(PSTRING,COUNTVAR+1);
			
						-- SELECT COUNT(1) INTO L_REGISTERFLAG  FROM USER_INFO WHERE 
						-- RIGHT(REPLACE(PHONENUMBER, ' ', ''),8)=RIGHT(REPLACE(L_NUMBER, ' ', ''),8) AND STATUSID=1;
                   
                        SELECT COUNT(1) INTO L_REGISTERFLAG  FROM USER_INFO WHERE PHONENUMBER=L_NUMBER AND STATUSID=1;

						IF PFLAG=3 THEN
							
							INSERT INTO INVITATION_DETAILS(INVITATIONSENDTO,INVITATIONSENDBY,
							INVTATIONSENDDATE,GROUPID,STATUSID,REGISTERFLAG,INVITATIONFLAGID) 
							VALUES(L_NUMBER,PSENDBY,utc_date(),PGROUPID,2,L_REGISTERFLAG,1)
                             on duplicate key UPDATE INVTATIONSENDDATE=UTC_DATE();	
						ELSE
							SET @QUERY=CONCAT('INSERT INTO INVITATION_DETAILS(INVITATIONSENDTO,INVITATIONSENDBY,
							INVTATIONSENDDATE,GROUPID,STATUSID,REGISTERFLAG,INVITATIONFLAGID) 
							SELECT PHONENUMBER ,',L_NUMBER,' ,UTC_DATE() ,',PGROUPID,',2,',L_REGISTERFLAG,',2 FROM 
							GROUP_MEMBER_DETAILS WHERE GROUPID = ',PGROUPID,' AND ACCESSTYPEID =1');

							-- SELECT @QUERY;

							/*PREPARE QUERY FROM @QUERY;
							EXECUTE QUERY ;
							deallocate prepare QUERY;*/

					 END IF;
					SET COUNTVAR=COUNTVAR+2; 
				END;
			END WHILE; 
		END;
	END IF;
END IF;

END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `INVITATIONACCEPT` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = '' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`%` PROCEDURE `INVITATIONACCEPT`(
IN PPHONENUMBER VARCHAR(100),
IN PGROUPID INTEGER,
IN PCOUNTRYCODEID INTEGER,
IN PFLAG INTEGER,
IN PSTATUSID INTEGER,
OUT POUT TEXT
)
BEGIN

/*
PSTATUSID=4 for accept
PSTATUSID=0 for Rejected
*/

-- RIGHT(PPHONENUMBER,PPHONEMAXLEN)
DECLARE L_PHONENOMAXLEN INT default 0;
DECLARE L_PHONENO VARCHAR(20);
DECLARE L_SENDTO VARCHAR(50);
DECLARE L_SENDBY VARCHAR(50);
DECLARE L_CHECKADMIN INTEGER default 0;

SET SQL_SAFE_UPDATES = 0;

SET L_SENDTO= FUN_SPLIT_STR(PPHONENUMBER,'#',1);
SET L_SENDBY=FUN_SPLIT_STR(PPHONENUMBER,'#',2);

-- SELECT PHONENOMAXLEN INTO L_PHONENOMAXLEN FROM MASTER_COUNTRYCODE WHERE COUNTRYCODEID=PCOUNTRYCODEID;

SELECT COUNT(1) INTO L_CHECKADMIN FROM   GROUP_MEMBER_DETAILS WHERE PHONENUMBER=L_SENDTO
AND GROUPID=PGROUPID AND ACCESSTYPEID=1;

IF PSTATUSID=4 THEN

		IF L_CHECKADMIN>0 THEN 

			INSERT INTO GROUP_MEMBER_DETAILS
			(GROUPID,PHONENUMBER,ACCESSTYPEID,REGISTERFLAG,ADDEDDATE,STATUSID,ADDEDTIMESTAMP)
			VALUES(PGROUPID,L_SENDBY,2,1,UTC_DATE(),1,UTC_TIMESTAMP)
			ON DUPLICATE KEY UPDATE STATUSID=1,ADDEDTIMESTAMP=UTC_TIMESTAMP;
		ELSE

			INSERT INTO GROUP_MEMBER_DETAILS
			(GROUPID,PHONENUMBER,ACCESSTYPEID,REGISTERFLAG,ADDEDDATE,STATUSID,ADDEDTIMESTAMP)
			VALUES(PGROUPID,L_SENDTO,2,1,UTC_DATE(),1,UTC_TIMESTAMP)
			ON DUPLICATE KEY UPDATE STATUSID=1,ADDEDTIMESTAMP=UTC_TIMESTAMP;
		END IF;

END IF;



UPDATE INVITATION_DETAILS SET STATUSID=PSTATUSID
, REGISTERFLAG=1  WHERE GROUPID=PGROUPID
AND INVITATIONSENDTO=L_SENDTO;

SET POUT=concat('{"STATUS":"SUCCESS"}');

END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `INVITATIONSEND` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = '' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`%` PROCEDURE `INVITATIONSEND`(
 IN PSENDBYNO VARCHAR(20),
 IN PSENDTONO text,
 IN PGROUPID INTEGER,
 IN PINVITATIONFLAGID INTEGER,
 OUT POUT VARCHAR(100)
)
BEGIN

/*
PINVITATIONFLAGID=1 FOR ADMINTOUSER
PINVITATIONFLAGID=2 FOR USERTOADMIN
*/

/*DECLARE L_REGISTERFLAG INTEGER default 0;

		SELECT COUNT(1) INTO L_REGISTERFLAG  FROM USER_INFO WHERE PHONENUMBER
		=PSENDTONO AND STATUSID=1;

		INSERT INTO INVITATION_DETAILS(INVITATIONSENDTO,INVITATIONSENDBY,
		INVTATIONSENDDATE,GROUPID,STATUSID,REGISTERFLAG,INVITATIONFLAGID) 
		VALUES(PSENDTONO,PSENDBYNO,utc_date(),PGROUPID,2,L_REGISTERFLAG,PINVITATIONFLAGID)
		on duplicate key UPDATE INVTATIONSENDDATE=UTC_DATE();
*/

DECLARE L_NUMBER VARCHAR(50);
DECLARE L_NAME VARCHAR(100);
DECLARE L_REGISTERFLAG INTEGER DEFAULT 0;
DECLARE COUNTVAR INTEGER ;
DECLARE DELIMITVAR INTEGER ;
DECLARE L_REJECTNO VARCHAR(500);
DECLARE L_REJECTCHECK INTEGER default 0;

	-- SELECT MITYUNGINFRAONE.STRSPLITFF1(PSTRING,1) INTO L_STRING;
	SELECT STRSPLITFF(PSENDTONO,1) INTO DELIMITVAR;

	SET COUNTVAR=2;

IF DELIMITVAR<100 THEN
	IF DELIMITVAR<>0 THEN

		BEGIN
			WHILE COUNTVAR < DELIMITVAR  DO

				BEGIN
					SET L_NAME=STRSPLITFF(PSENDTONO,COUNTVAR);
					SET L_NUMBER=STRSPLITFF(PSENDTONO,COUNTVAR+1);
                   
					-- SELECT COUNT(1) INTO L_REGISTERFLAG  FROM USER_INFO WHERE 
					-- RIGHT(REPLACE(PHONENUMBER, ' ', ''),8)=RIGHT(REPLACE(L_NUMBER, ' ', ''),8) AND STATUSID=1;
                    SELECT COUNT(1) INTO  L_REJECTCHECK FROM INVITATION_DETAILS
                    WHERE  STATUSID=0 AND INVITATIONSENDBY=PSENDBYNO AND GROUPID=PGROUPID;
                    IF L_REJECTCHECK>0 then
							IF LENGTH(L_REJECTNO)>0 THEN
							  SET L_REJECTNO=CONCAT(L_REJECTNO,'~',L_NUMBER);
							ELSE
							  SET L_REJECTNO=CONCAT(L_NUMBER);
							END IF;
                     END IF;

					 SELECT COUNT(1) INTO L_REGISTERFLAG  FROM USER_INFO WHERE PHONENUMBER=L_NUMBER AND STATUSID=1;

					INSERT INTO INVITATION_DETAILS(INVITATIONSENDTO,INVITATIONSENDBY,
					INVTATIONSENDDATE,GROUPID,STATUSID,REGISTERFLAG,INVITATIONFLAGID) 
					VALUES(L_NUMBER,PSENDBYNO,utc_date(),PGROUPID,2,L_REGISTERFLAG,PINVITATIONFLAGID)
					on duplicate key UPDATE INVTATIONSENDDATE=UTC_DATE();	
			     
					SET COUNTVAR=COUNTVAR+2; 
				END;
			END WHILE; 
		END;
	END IF;
END IF;
set POUT=CONCAT('{"STATUS":"SUCCESS","REJECTNO":"',IFNULL(L_REJECTNO,''),'"}');


END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `MIBACKUPSTORE` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = '' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`%` PROCEDURE `MIBACKUPSTORE`(
 IN PUID  INTEGER,
 IN PCONTACTLIST LONGTEXT,
 IN PFLAG INTEGER,
 OUT POUT INTEGER 
)
BEGIN
/*
 SAVE CONTACT
  PFLAG=1 FOR TOTAL PHONE CONTACT BACKUP
  PFLAG=2 FOR PHONE NUMBER AND DISPLAY NAME

*/
DECLARE COUNTVAR INTEGER ;
DECLARE DELIMITVAR INTEGER ;
 DECLARE L_NUMBER VARCHAR(50);
 DECLARE L_NAME VARCHAR(100);
 DECLARE L_REGISTERFLAG INTEGER DEFAULT 0;
DECLARE L_LASTBACKUP LONGTEXT default NULL;




IF PFLAG=1 THEN
SELECT BACKUP INTO L_LASTBACKUP FROM MIPHONEBACKUP WHERE UID=PUID;

 INSERT INTO MIPHONEBACKUP (UID ,BACKUP )
 VALUES(PUID,PCONTACTLIST) ON duplicate KEY UPDATE BACKUP=PCONTACTLIST,
LASTBACKUP=L_LASTBACKUP ,CREATEDUPDATEDDATE=UTC_DATE();

ELSEIF PFLAG=2 then

	SELECT STRSPLITFF(PCONTACTLIST,1) INTO DELIMITVAR;
	SET COUNTVAR=2;
     IF DELIMITVAR<1500 THEN
    IF DELIMITVAR<>0 THEN

		BEGIN
			WHILE COUNTVAR < DELIMITVAR  DO

				BEGIN
                  set L_REGISTERFLAG=0;
					SET L_NAME=STRSPLITFF(PCONTACTLIST,COUNTVAR);
					SET L_NUMBER=STRSPLITFF(PCONTACTLIST,COUNTVAR+1);
				
                     SELECT COUNT(1) INTO L_REGISTERFLAG  FROM USER_INFO
                     WHERE PHONENUMBER=L_NUMBER AND STATUSID=1;

                    -- SELECT insertPhoneNO(L_NUMBER);

					INSERT INTO MYCONTACTDETAILS(UID,PHONENUMBERS,DISPLAYNAME,STATUSID) 
					VALUES (PUID,L_NUMBER,L_NAME,1)ON DUPLICATE KEY UPDATE DISPLAYNAME=L_NAME;
       			
					SET COUNTVAR=COUNTVAR+2; 
				END;
			END WHILE; 
		END;
	END IF;
  END IF;
END IF;
SET POUT=1;


END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `MIREGISTRATION` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = '' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`%` PROCEDURE `MIREGISTRATION`(
	PPHONENUMBER	VARCHAR(45),
	PEMAIL	VARCHAR(45)    ,
	PNAME	VARCHAR(45)    ,
	PADDRESS	VARCHAR(45)    ,
	PCITYID	INTEGER,
	PSTATEID	INTEGER,
	PCOUNTRYCODEID	VARCHAR(45),
	PCOUNTRYNAME VARCHAR(45),
	POTPID	INTEGER,
	PSTATUSID	INTEGER,
	PPROFESSION  varchar(200),
    PLOGINFLAG  INTEGER,
    OUT POUT  varchar(40)
)
BEGIN
/*
 THIS PROCEDURE IS MADE TO REGISTER THE USER
return
STATUS=2 already exists send loginflag=1
STATUS=0 not exists  send loginflag=0
status=1 fro sucess full login
status=3 fro sucess full register complete and login
status=4 for wrong otp
*/


  DECLARE L_OTP INTEGER default 0;
DECLARE L_ALREADYEXIST INTEGER default 0;
DECLARE L_OTPCHECK INTEGER default 0; 
DECLARE L_STATUSCHECK INTEGER default 0;
DECLARE L_UID INTEGER default 0;
DECLARE L_EMAILCHECK INTEGER default 0;
DECLARE L_PRIMARYNO VARCHAR(20);
DECLARE L_ALTERNATENO VARCHAR(20);
DECLARE L_PPROFESSION VARCHAR(100);
DECLARE L_COMPANY VARCHAR(100);
DECLARE L_COUNTRYCODEID VARCHAR(20);
DECLARE L_BLOODGROUP VARCHAR(20);



if PLOGINFLAG=1 then -- for profile update

SET L_PRIMARYNO= FUN_SPLIT_STR(PPHONENUMBER,'#',1);
SET L_ALTERNATENO=FUN_SPLIT_STR(PPHONENUMBER,'#',2);

SET L_COMPANY= FUN_SPLIT_STR(PPROFESSION,'#',1);
SET L_PPROFESSION=FUN_SPLIT_STR(PPROFESSION,'#',2);
-- SET L_COUNTRYCODEID=null;
-- SET L_BLOODGROUP=null;
 SET L_COUNTRYCODEID= FUN_SPLIT_STR(PCOUNTRYCODEID,'#',1);
 SET L_BLOODGROUP=FUN_SPLIT_STR(PCOUNTRYCODEID,'#',2);


   SELECT COUNT(1) INTO L_EMAILCHECK  FROM USER_INFO 
    WHERE EMAIL=PEMAIL AND  PHONENUMBER!=L_PRIMARYNO;
   -- insert into Test (TESTT) values ('1');
    IF L_EMAILCHECK=0 THEN
        --  insert into Test (TESTT) values (L_COMPANY);
		UPDATE USER_INFO SET EMAIL=PEMAIL ,NAME=PNAME,ADDRESS=PADDRESS,CITYID=PCITYID
		,STATEID=PSTATEID,COUNTRYCODEID=L_COUNTRYCODEID ,BLOODGROUP=L_BLOODGROUP,STATUSID=1,
		PROFESSION=L_PPROFESSION ,CREATEDDATE=UTC_timestamp() ,ALTERNATENO=L_ALTERNATENO
         , COMPANYNAME=L_COMPANY
		WHERE PHONENUMBER=L_PRIMARYNO;

       update GROUP_MEMBER_DETAILS  set ADDEDTIMESTAMP=utc_timestamp()
       WHERE PHONENUMBER=L_PRIMARYNO;

       SET POUT=CONCAT('{"STATUS":1}'); 

		ELSE
       SET POUT=CONCAT('{"STATUS":3}'); 
		END IF;
else
SELECT STATUSID INTO L_STATUSCHECK FROM USER_INFO WHERE PHONENUMBER=PPHONENUMBER;

IF L_STATUSCHECK=0 then
		SELECT insertPhoneNO(PPHONENUMBER);
        SELECT OTPGEN() INTO L_OTP;
		INSERT INTO USER_INFO (PHONENUMBER,OTPID,STATUSID,CREATEDDATE) 
		VALUES(PPHONENUMBER,L_OTP,2,UTC_timestamp());
		-- SET POUT=CONCAT('{"STATUS":0,"OTPFLAG":',L_OTP,'}'); 
        SET POUT=CONCAT('{"STATUS":0,"OTP":',L_OTP,'}'); 

ELSEIF L_STATUSCHECK =2 and  POTPID>0  THEN 
       SELECT STATUSID ,UID INTO L_OTPCHECK,L_UID  FROM USER_INFO 
		WHERE PHONENUMBER=PPHONENUMBER AND OTPID=POTPID;

      IF L_OTPCHECK>0 then

			SELECT COUNT(1) INTO L_EMAILCHECK  FROM USER_INFO 
			WHERE EMAIL=PEMAIL AND  PHONENUMBER!=PPHONENUMBER;
  
			IF L_EMAILCHECK=0 THEN

			SELECT getcountryCode(PCOUNTRYCODEID,PCOUNTRYNAME);
			UPDATE USER_INFO SET EMAIL=PEMAIL ,NAME=PNAME,ADDRESS=PADDRESS,CITYID=PCITYID
			,STATEID=PSTATEID,COUNTRYCODEID=PCOUNTRYCODEID,STATUSID=1,
			PROFESSION=PPROFESSION ,CREATEDDATE=UTC_timestamp() 
			WHERE PHONENUMBER=PPHONENUMBER;

             UPDATE INVITATION_DETAILS SET REGISTERFLAG=1 WHERE INVITATIONSENDTO=PPHONENUMBER;

			SET POUT=CONCAT('{"STATUS":1,"UID":',L_UID,'}'); 
			ELSE
			SET POUT=CONCAT('{"STATUS":3}'); 
			END IF;
     ELSE
       SET POUT=CONCAT('{"STATUS":4}'); 
      END IF;
   
ELSEIF L_STATUSCHECK =2 and  POTPID=0  THEN 
      SELECT OTPGEN() INTO L_OTP;
       UPDATE USER_INFO SET OTPID = L_OTP WHERE PHONENUMBER=PPHONENUMBER;
       SET POUT=CONCAT('{"STATUS":0,"OTP":',L_OTP,'}');


  ELSEIF L_STATUSCHECK=1 AND POTPID>0 THEN
        SELECT STATUSID ,UID INTO L_OTPCHECK,L_UID  FROM USER_INFO 
		WHERE PHONENUMBER=PPHONENUMBER AND OTPID=POTPID;

     
				IF L_OTPCHECK>0 then
				SET POUT=CONCAT('{"STATUS":1,"UID":',L_UID,'}');
				ELSE
				SET POUT=CONCAT('{"STATUS":4}'); 
				END IF;
ELSEIF L_STATUSCHECK=1 AND POTPID=0 THEN
       SELECT OTPGEN() INTO L_OTP;
       UPDATE USER_INFO SET OTPID = L_OTP WHERE PHONENUMBER=PPHONENUMBER;
	   SET POUT=CONCAT('{"STATUS":2,"OTP":',L_OTP,'}'); 
END IF;
end if;

  

END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `NOTIFICATIONKEYINSERT` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = '' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`%` PROCEDURE `NOTIFICATIONKEYINSERT`(
  IN PMOBILENO VARCHAR(15),
  IN PKEY  VARCHAR(500),
  IN PPHONETYPE VARCHAR(2),
  IN PVERSION VARCHAR(10),
  OUT POUT VARCHAR(100)
)
BEGIN



     INSERT INTO PUSH_NOTIFICATION_KEY (MOBILENO,NOTIFICATION_KEY,PHONETYPE,VERSION) 
   VALUES (PMOBILENO,PKEY,PPHONETYPE,PVERSION) 
   ON DUPLICATE KEY UPDATE NOTIFICATION_KEY=PKEY,VERSION=PVERSION,PHONETYPE=PPHONETYPE;

SET POUT='{"STATUS":"SUCCESS"}';
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `PROFILEFETCH` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = '' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`%` PROCEDURE `PROFILEFETCH`(
IN PUID INTEGER,
OUT POUT LONGTEXT
)
BEGIN
 DECLARE L_COUNTRYNAME VARCHAR(45) default NULL;
 DECLARE L_CITYNAME VARCHAR(45);
 DECLARE L_CITYID INTEGER default 0;

 SET SESSION group_concat_max_len =18446744073709551615;
-- "PHOTO":"',IFNULL(PHOTO,''),'",
-- "PHOTO":"',IFNULL(TRIM(REPLACE(REPLACE(REPLACE(PHOTO, '\n', ' '), '\r', ' '), '\t', ' ')),''),'",

SELECT GROUP_CONCAT('{"PHONENUMBER":"',IFNULL(PHONENUMBER,''),'",
"EMAIL":"',IFNULL(EMAIL,''),'",
"NAME":"',IFNULL(NAME,''),'",
"ADDRESS":"',IFNULL(ADDRESS,''),'",
"COUNTRYCODEIDPHONE":"',IFNULL(PCID,''),'",
"COUNTRYCODEID":"',IFNULL(COUNTRYCODEID,''),'",
"COUNTRYNAME":"',IFNULL(COUNTRYNAME,''),'",
"PROFESSION":"',IFNULL(PROFESSION,''),'",
"ALTERNATENO":"',IFNULL(ALTERNATENO,''),'",
"PHOTO":"',IFNULL(TRIM(REPLACE(REPLACE(REPLACE(PHOTO, '\n', ' '), '\r', ' '), '\t', ' ')),''),'",
"COMPANYNAME":"',IFNULL(COMPANYNAME,'0'),'",
"BLOODGROUP":"',IFNULL(BLOODGROUP,'0'),'",
') INTO POUT FROM (

SELECT USER_INFO.PHONENUMBER,USER_INFO.EMAIL,USER_INFO.NAME,USER_INFO.ADDRESS,
USER_INFO.COUNTRYCODEID AS PCID,USER_INFO.PROFESSION
,MC.COUNTRYNAME,MC.COUNTRYCODEID,USER_INFO.ALTERNATENO,USER_INFO.PHOTO
,USER_INFO.COMPANYNAME,USER_INFO.BLOODGROUP
FROM XERUNG.USER_INFO USER_INFO 
INNER JOIN XERUNG.MASTER_COUNTRYCODE MC ON (
REPLACE(MC.COUNTRYCODE, '+', '')=REPLACE(USER_INFO.COUNTRYCODEID, '+', '')
-- MC.COUNTRYCODE=USER_INFO.COUNTRYCODEID
-- MC.COUNTRYCODE=USER_INFO.COUNTRYCODEID
)
WHERE USER_INFO.UID =PUID

) AS T;


SELECT CITYID INTO L_CITYID FROM  XERUNG.USER_INFO WHERE UID =PUID;
		IF L_CITYID>0 then
		SELECT CITYNAME INTO L_CITYNAME FROM MASTERCITY WHERE CITYID=L_CITYID;
		SET POUT=CONCAT(POUT,'"CITYNAME":"',IFNULL(L_CITYNAME,''),'","CITYID":',L_CITYID,'}');
		ELSE
		SET POUT=CONCAT(POUT,'"CITYNAME":"','','","CITYID":','""','}');
	
		END IF;




END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `UPDATEACCESSFLAGANDPHOTO` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = '' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`%` PROCEDURE `UPDATEACCESSFLAGANDPHOTO`(
IN PPHONENUMBER VARCHAR(20),
IN PPHOTO LONGTEXT,
IN PFLAG INTEGER,
OUT POUT VARCHAR(50)
)
BEGIN
/*
 THIS PROCEDURE IS MADE TO UPDATE USER ACCESS FLAG AND PHOTO
*/

   IF PFLAG=1 then
     UPDATE USER_INFO SET PHOTO = PPHOTO WHERE PHONENUMBER=PPHONENUMBER;
	   SET POUT=CONCAT('{"STATUS":"SUCCESS"}'); 

     ELSEIF PFLAG=2 THEN
     UPDATE GROUP_MEMBER_DETAILS SET ACCESSTYPEID = 1 WHERE PHONENUMBER=PPHONENUMBER;
	   SET POUT=CONCAT('{"STATUS":"SUCCESS"}'); 
   END IF;

END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2017-10-09 17:56:33

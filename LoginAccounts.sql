--------------------------------------------------------
--  File created - Thursday-November-02-2017   
--------------------------------------------------------
--------------------------------------------------------
--  DDL for Table LOGIN
--------------------------------------------------------

  CREATE TABLE "DBHATTI"."LOGIN" 
   (	"UNAME" VARCHAR2(20 BYTE), 
	"PASSWORD" VARCHAR2(20 BYTE), 
	"DESIGNATION" VARCHAR2(10 BYTE)
   ) SEGMENT CREATION IMMEDIATE 
  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 NOCOMPRESS LOGGING
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS" ;
REM INSERTING into DBHATTI.LOGIN
SET DEFINE OFF;
Insert into DBHATTI.LOGIN (UNAME,PASSWORD,DESIGNATION) values ('mfisher','mfisher','student');
Insert into DBHATTI.LOGIN (UNAME,PASSWORD,DESIGNATION) values ('tregan','tregan','student');
Insert into DBHATTI.LOGIN (UNAME,PASSWORD,DESIGNATION) values ('jmick','jmick','student');
Insert into DBHATTI.LOGIN (UNAME,PASSWORD,DESIGNATION) values ('jander','jander','student');
Insert into DBHATTI.LOGIN (UNAME,PASSWORD,DESIGNATION) values ('jharla','jharla','student');
Insert into DBHATTI.LOGIN (UNAME,PASSWORD,DESIGNATION) values ('aneela','aneela','student');
Insert into DBHATTI.LOGIN (UNAME,PASSWORD,DESIGNATION) values ('mjones','mjones','student');
Insert into DBHATTI.LOGIN (UNAME,PASSWORD,DESIGNATION) values ('jmoyer','jmoyer','student');
Insert into DBHATTI.LOGIN (UNAME,PASSWORD,DESIGNATION) values ('kogan','kogan','instructor');
Insert into DBHATTI.LOGIN (UNAME,PASSWORD,DESIGNATION) values ('rchirkova','rchirkova','instructor');
Insert into DBHATTI.LOGIN (UNAME,PASSWORD,DESIGNATION) values ('chealey','chealey','instructor');
--------------------------------------------------------
--  DDL for Index SYS_C00414467
--------------------------------------------------------

  CREATE UNIQUE INDEX "DBHATTI"."SYS_C00414467" ON "DBHATTI"."LOGIN" ("UNAME") 
  PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS" ;
--------------------------------------------------------
--  Constraints for Table LOGIN
--------------------------------------------------------

  ALTER TABLE "DBHATTI"."LOGIN" MODIFY ("UNAME" NOT NULL ENABLE);
 
  ALTER TABLE "DBHATTI"."LOGIN" MODIFY ("PASSWORD" NOT NULL ENABLE);
 
  ALTER TABLE "DBHATTI"."LOGIN" MODIFY ("DESIGNATION" NOT NULL ENABLE);
 
  ALTER TABLE "DBHATTI"."LOGIN" ADD CHECK (designation IN ('student', 'ta', 'instructor')) ENABLE;
 
  ALTER TABLE "DBHATTI"."LOGIN" ADD PRIMARY KEY ("UNAME")
  USING INDEX PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS"  ENABLE;

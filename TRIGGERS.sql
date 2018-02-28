CREATE VIEW STUDENT_HOMEWORK_SCORE (studentID,hID,attNo,score) AS Select S.SID,S.HID,S.ATTNO, SUM (S.QSCORE) From Submission S GROUP BY S.SID,S.HID,S.ATTNO ;

-----
CREATE OR REPLACE TRIGGER TA_ENROLL_TRIGGER
BEFORE INSERT
  ON TA
FOR EACH ROW
DECLARE
    row_count_enroll NUMBER;
    row_count_grad NUMBER;
BEGIN
    SELECT count(SID) into row_count_enroll FROM ENROLL WHERE SID = :NEW.SID AND CID = :NEW.CID;
    IF row_count_enroll > 0 THEN
        RAISE_APPLICATION_ERROR (-20001, 'Cant be a TA if you are enrolled');
    END IF;
    SELECT count(SID) into row_count_grad FROM STUDENT WHERE SID = :NEW.SID AND LVL = 'grad';
    IF row_count_grad < 1 THEN
        RAISE_APPLICATION_ERROR (-20002, 'Cant be a TA if you are not grad');
    END IF;
END;
/
-------------------
CREATE OR REPLACE TRIGGER TA_UPDATE_LOGIN_TRIGGER
AFTER INSERT
  ON TA
FOR EACH ROW
BEGIN
    UPDATE LOGIN L SET L.DESIGNATION='ta' WHERE L.uName = :NEW.SID;
END;

--- Reverse TRIGGER -- 
CREATE OR REPLACE TRIGGER TA_UPDATE_LOGIN_DELETE_TRIGGER
AFTER DELETE on TA
for each row
begin
    UPDATE LOGIN L SET L.DESIGNATION='student' WHERE L.uName = :OLD.SID;
end;
------------------- NO MORE DOING ---

CREATE OR REPLACE TRIGGER noQs_update 
BEFORE INSERT ON HASQ 
FOR EACH ROW
DECLARE
num_ques NUMBER(30);
BEGIN
  SELECT COUNT(*) INTO num_ques from HASQ WHERE hID = :new.hID;
  num_ques := num_ques+1;
  UPDATE HOMEWORK H SET H.noQs = num_ques WHERE H.HID = :new.hID;
END;
--- NOT CHECKED YET :P --- CHECKED BUT NOT WORKING -- NO MORE DOING--

CREATE OR REPLACE TRIGGER noQs_update_on_delete 
AFTER DELETE ON HASQ 
FOR EACH ROW
DECLARE
num_ques NUMBER(30);
BEGIN
  SELECT COUNT(*) INTO num_ques from HASQ WHERE hID = :old.hID;
  num_ques := num_ques-1;
  UPDATE HOMEWORK H SET H.noQs = num_ques WHERE H.HID = :new.hID;
END;


--- https://stackoverflow.com/questions/6415340/raise-application-error-issue
--- http://www.dba-oracle.com/t_raise_application_error.htm
--- https://stackoverflow.com/questions/9654623/how-to-create-a-trigger-in-oracle-which-will-restrict-insertion-and-update-queri


---- AUTO INC THINGS ----

----------------------- TOPIC --------------------------
CREATE SEQUENCE topic_seq START WITH 1;
/
CREATE OR REPLACE TRIGGER trg_topic_id
before insert on TOPIC
for each row
begin
    select topic_seq.nextval into :new.tID from dual;
end;
/
CREATE OR REPLACE PROCEDURE insert_topic(v_tname TOPIC.tname%TYPE,
    v_tid out topic.tid%TYPE)
as
BEGIN
    INSERT INTO TOPIC(tname)
    VALUES(v_tname)
    returning tid into v_tid;
END;

----------------------- QUESTION  --------------------------
CREATE SEQUENCE question_seq START WITH 1;
/
CREATE OR REPLACE TRIGGER trg_question_id
before insert on QUESTION
for each row
begin
    select question_seq.nextval into :new.qID from dual;
end;
/
CREATE OR REPLACE PROCEDURE insert_question(v_question QUESTION.question%TYPE,
                                  v_difficulty QUESTION.difficulty%TYPE,
                                  v_hint QUESTION.hint%TYPE,
                                  v_solution QUESTION.solution%TYPE,
                                  v_qType QUESTION.qType%TYPE,
                                  v_qid out QUESTION.qid%TYPE)
as
begin
    insert into QUESTION(question, difficulty, hint, solution, qType)
    values(v_question, v_difficulty, v_hint, v_solution, v_qType)
    returning qid into v_qid;
end;

----------------------- PARAMETERS  --------------------------
CREATE SEQUENCE parameter_seq START WITH 1;
\
CREATE OR REPLACE TRIGGER trg_parameter_id
before insert on PARAMETERS
for each row
begin
    select parameter_seq.nextval into :new.parID from dual;
end;
\
CREATE OR REPLACE PROCEDURE insert_parameters(v_par1 PARAMETERS.par1%TYPE,
                                  v_par2 PARAMETERS.par2%TYPE,
                                  v_par3 PARAMETERS.par3%TYPE,
                                  v_par4 PARAMETERS.par4%TYPE,
                                  v_par5 PARAMETERS.par5%TYPE,
                                  v_parid out PARAMETERS.parid%TYPE)
as
begin
    insert into PARAMETERS(par1, par2, par3, par4, par5)
    values(v_par1, v_par2, v_par3, v_par4, v_par5)
    returning parid into v_parid;
end;


----------------------- ANSWER  --------------------------
CREATE SEQUENCE answer_seq START WITH 1;
\
CREATE OR REPLACE TRIGGER trg_answer_id
before insert on ANSWER
for each row
begin
    select answer_seq.nextval into :new.ansID from dual;
end;
\
CREATE OR REPLACE PROCEDURE insert_answer(v_answer ANSWER.answer%TYPE,
                                  v_ansid out ANSWER.ansid%TYPE)
as
begin
    insert into ANSWER(answer)
    values(v_answer)
    returning ansid into v_ansid;
end;

----------------------- HOMEWORK  --------------------------
CREATE SEQUENCE homework_seq START WITH 1;
\
create or replace trigger trg_homework_id
before insert on HOMEWORK
for each row
begin
    select homework_seq.nextval into :new.hID from dual;
end;
\
CREATE OR REPLACE PROCEDURE insert_homework(v_cid HOMEWORK.cid%TYPE,
                                  v_hname HOMEWORK.hname%TYPE,
                                  v_deadline VARCHAR,
                                  v_points HOMEWORK.points%TYPE,
                                  v_penaltypoints HOMEWORK.penaltypoints%TYPE,
                                  v_noqs HOMEWORK.noqs%TYPE,
                                  v_noas HOMEWORK.noas%TYPE,
                                  v_startdate VARCHAR,
                                  v_ttype HOMEWORK.ttype%TYPE,
                                  v_scoringpolicy HOMEWORK.scoringpolicy%TYPE,
                                  v_tid HOMEWORK.tid%TYPE,
                                  v_hid out HOMEWORK.hid%TYPE)
is
default_difficulty NUMBER;
begin

    IF v_ttype = 'standard' THEN
        default_difficulty := 0;
    END IF;
    IF v_ttype = 'adaptive' THEN
        default_difficulty := 3;
    END IF;

    insert into HOMEWORK(cid, hname, deadline, points, difficulty, penaltypoints, noqs, noas, startdate, ttype, scoringpolicy, tid)
    values(v_cid, v_hname, TO_TIMESTAMP(v_deadline, 'yyyy-mm-dd hh24:mi:ss'), v_points, default_difficulty, v_penaltypoints, v_noqs, v_noas, TO_TIMESTAMP(v_startdate, 'yyyy-mm-dd hh24:mi:ss'), v_ttype, v_scoringpolicy, v_tid)
    returning hid into v_hid;
end;

----------------------- ADEPTIVE QUESTION ADD HASQ --------------------------------
---- THIS TRIGGER ADDS ALL THE QUESTIONS BELONGING TO TOPIC ID TO THE HOMEWORK ----

CREATE OR REPLACE PROCEDURE HASQ_UPDATE (v_tID IN NUMBER,
                                        v_hID IN NUMBER)
 IS 
CURSOR CUR IS SELECT Q.qID FROM QUESTION Q, QTC qtc WHERE Q.qID = qtc.qID AND qtc.tID = v_tID; 
BEGIN 
	FOR qind IN CUR LOOP 
			INSERT INTO HASQ (qID, hID) VALUES (qind.qID, v_hID);
    END LOOP qind; 
END;

-------------
create or replace trigger trg_adeptive_add 
after insert on HOMEWORK
for each row
begin
    IF :NEW.ttype = 'adaptive' THEN
        HASQ_UPDATE(:NEW.tID, :NEW.hID);
    END IF;
end;

-------- Update HW PROCEDURE to add questions to new_hID from old_hID -----
CREATE OR REPLACE PROCEDURE HASQ_HW_UPDATE (v_hID_old IN NUMBER,
                                            v_hID_new IN NUMBER)
 IS 
CURSOR CUR IS SELECT Q.qID FROM HASQ Q WHERE Q.hID = v_hID_old; 
BEGIN 
	FOR qind IN CUR LOOP 
			INSERT INTO HASQ (qID, hID) VALUES (qind.qID, v_hID_new);
    END LOOP qind; 
END;
-------------
CREATE OR REPLACE PROCEDURE update_homework(v_cid HOMEWORK.cid%TYPE,
                                  v_hname HOMEWORK.hname%TYPE,
                                  v_deadline VARCHAR,
                                  v_points HOMEWORK.points%TYPE,
                                  v_penaltypoints HOMEWORK.penaltypoints%TYPE,
                                  v_noqs HOMEWORK.noqs%TYPE,
                                  v_noas HOMEWORK.noas%TYPE,
                                  v_startdate VARCHAR,
                                  v_ttype HOMEWORK.ttype%TYPE,
                                  v_scoringpolicy HOMEWORK.scoringpolicy%TYPE,
                                  v_tid HOMEWORK.tid%TYPE,
                                  v_hid_old HOMEWORK.hid%TYPE,
                                  v_hid out HOMEWORK.hid%TYPE)
is
default_difficulty NUMBER;
begin

    IF v_ttype = 'standard' THEN
        default_difficulty := 0;
    END IF;
    IF v_ttype = 'adaptive' THEN
        default_difficulty := 3;
    END IF;


    insert into HOMEWORK(cid, hname, deadline, points, difficulty, penaltypoints, noqs, noas, startdate, ttype, scoringpolicy, tid)
    values(v_cid, v_hname, TO_TIMESTAMP(v_deadline, 'yyyy-mm-dd hh24:mi:ss'), v_points, default_difficulty, v_penaltypoints, v_noqs, v_noas, TO_TIMESTAMP(v_startdate, 'yyyy-mm-dd hh24:mi:ss'), v_ttype, v_scoringpolicy, v_tid)
    returning hid into v_hid;
    IF v_ttype = 'standard' THEN
        HASQ_HW_UPDATE(v_hid_old, v_hid);
    END IF;
end;

----- Scoring Policy -----
---
create global temporary table final_report
   (sID VARCHAR(20), hID NUMBER, final_score NUMBER) on commit preserve rows;

---
CREATE OR REPLACE PROCEDURE get_final_report (v_cid HOMEWORK.cid%TYPE,
                                                prc out sys_refcursor)
is
CURSOR CUR IS SELECT shs.hID, h.scoringpolicy FROM STUDENT_HOMEWORK_SCORE shs, HOMEWORK h where shs.hID=h.hID AND h.cID = v_cid; 
begin
    DELETE FROM final_report;
    FOR qind IN CUR LOOP 
                IF qind.scoringpolicy = 'highest' THEN
                    INSERT INTO final_report (sid, hid, final_score) select STUDENTID, HID, MAX(score) from STUDENT_HOMEWORK_SCORE shs WHERE shs.hID = qind.hID group by HID,STUDENTID;
                END IF;

                IF qind.scoringpolicy = 'average' THEN
                    INSERT INTO final_report (sid, hid, final_score) select STUDENTID, HID, AVG(score) from STUDENT_HOMEWORK_SCORE shs WHERE shs.hID = qind.hID group by HID,STUDENTID;
                END IF;

                IF qind.scoringpolicy = 'latest' THEN
                    INSERT INTO final_report (sid, hid, final_score) select shs.studentid, shs.hid, shs.score from STUDENT_HOMEWORK_SCORE shs INNER JOIN (select studentId, hid, MAX(attno) atn from STUDENT_HOMEWORK_SCORE group by hid,studentid) shs1 ON shs.HID = shs1.hid AND shs.STUDENTID = shs1.studentId AND shs.ATTNO = shs1.atn AND shs.hID = qind.hID; 
                END IF;
    END LOOP qind; 
    open prc for select DISTINCT * from final_report;
end;

------ TRYING SCHEDULER -----

CREATE OR REPLACE PROCEDURE TA_TENURE_CHECK
IS
CURSOR CUR IS SELECT * FROM TA;
BEGIN
    FOR qind in CUR LOOP
        IF SYSDATE > qind.endDate THEN
            DELETE FROM TA WHERE sID = qind.sID;
        END IF;
    END LOOP qind;
END; 

--- No privillages to schedule a job so doing it at the application level


---- TRIGGER TO CHECK HW is WITHIN Course Duration ----

CREATE OR REPLACE TRIGGER HW_WITHIN_COURSE_DURATION
BEFORE INSERT on HOMEWORK
FOR EACH ROW
DECLARE
c_endDate DATE;
c_startDate DATE;
BEGIN
    SELECT T.endDate, T.startDate INTO c_endDate, c_startDate FROM TAUGHT T WHERE T.cID = :NEW.cID;
    IF :NEW.deadline > c_endDate THEN
        RAISE_APPLICATION_ERROR (-20003, 'Cant add hw ending after course end-date');
    END IF;
    IF :NEW.startDate < c_startDate THEN
        RAISE_APPLICATION_ERROR (-20003, 'Cant add hw ending before course start-date');
    END IF;
END;

---- TRIGGER TO UPDATE AVERAGE DIFFICULTY --- 

CREATE OR REPLACE TRIGGER noQs_standard_update 
BEFORE INSERT ON HASQ 
FOR EACH ROW
DECLARE
num_ques NUMBER := 0;
sum_d NUMBER := 0;
avg_d NUMBER := 0;
temp_d NUMBER := 0;
v_ttype VARCHAR2(20);
BEGIN
    SELECT ttype INTO v_ttype FROM HOMEWORK WHERE hID = :NEW.hID;
    IF v_ttype = 'standard' THEN
        SELECT COUNT(*) INTO num_ques FROM HASQ WHERE hID = :NEW.hID;
        num_ques := num_ques + 1;
        SELECT NVL(SUM(Q.difficulty),0) INTO sum_d FROM HASQ H, QUESTION Q WHERE H.hID = :NEW.hID AND H.qID = Q.qID;
        SELECT Q.difficulty INTO temp_d FROM QUESTION Q WHERE Q.qID = :NEW.qID;
        sum_d := sum_d + temp_d;
        avg_d := sum_d / num_ques;
        UPDATE HOMEWORK SET DIFFICULTY = avg_d WHERE hID = :NEW.hID;
    END IF;
END;

---- PROC TO HANDLE HASQ DELETE DIFFICULTY ------

CREATE OR REPLACE PROCEDURE DELETE_FROM_HASQ(v_hID HASQ.hID%TYPE,
                                            v_qID HASQ.qID%TYPE)
IS
num_ques NUMBER;
sum_d NUMBER;
avg_d NUMBER;
v_ttype VARCHAR2(20);
BEGIN
    SELECT ttype INTO v_ttype FROM HOMEWORK WHERE hID = v_hID;
    IF v_ttype = 'standard' THEN
        SELECT COUNT(*) INTO num_ques FROM HASQ WHERE hID = v_hID AND qID <> v_qID;
        num_ques := num_ques;
        SELECT NVL(SUM(Q.difficulty),0) INTO sum_d FROM HASQ H, QUESTION Q WHERE H.hID = v_hID AND H.qID = Q.qID AND Q.qID <> v_qID;
        avg_d := sum_d / num_ques;
        UPDATE HOMEWORK SET DIFFICULTY = avg_d WHERE hID=v_hID;
    END IF;
    DELETE FROM HASQ WHERE hID=v_hID AND qID=v_qID;
END;

---- PROFESSOR to LOGIN -----

---- STUDENT to LOGIN -----

--- DELETE 1 DOES NOT WORK---
CREATE OR REPLACE TRIGGER noQs_standard_update_d 
AFTER DELETE ON HASQ 
FOR EACH ROW
BEGIN
    DELETE FROM DELETE_TRIG_UPDATES;
    INSERT INTO DELETE_TRIG_UPDATES VALUES ('hID', :OLD.hID);
    UPDATE_HW_DIFF_D;
END;



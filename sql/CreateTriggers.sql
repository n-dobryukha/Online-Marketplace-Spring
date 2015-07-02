CREATE TRIGGER USERS_BIS_TRG
	BEFORE INSERT ON USERS
	FOR EACH ROW
BEGIN
	IF INSERTING AND :NEW.ID IS NULL THEN
		SELECT USERS_PK_SEQ.NEXTVAL INTO :NEW.ID FROM SYS.DUAL;
	END IF;
END;
/
CREATE TRIGGER ITEMS_BIS_TRG
	BEFORE INSERT ON ITEMS
	FOR EACH ROW 
BEGIN
	IF INSERTING AND :NEW.ID IS NULL THEN
		SELECT ITEMS_PK_SEQ.NEXTVAL INTO :NEW.ID FROM SYS.DUAL;
	END IF;
END;
/
CREATE TRIGGER BIDS_BIS_TRG
	BEFORE INSERT ON BIDS
	FOR EACH ROW 
BEGIN
	IF INSERTING AND :NEW.ID IS NULL THEN
		SELECT BIDS_PK_SEQ.NEXTVAL INTO :NEW.ID FROM SYS.DUAL;
	END IF;
END;
/
Commit;
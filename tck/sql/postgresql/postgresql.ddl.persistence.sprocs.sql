drop table Integer_Tab ;
create table Integer_Tab (MAX_VAL INTEGER, MIN_VAL INTEGER, NULL_VAL INTEGER NULL) ;

DROP PROCEDURE Integer_Proc(out pmax integer,out pmen integer,out pnull integer);
CREATE OR REPLACE PROCEDURE Integer_Proc(out pmax integer,out pmin integer,out pnull integer) language 'plpgsql' as ' begin select * into pmax, pmin, pnull from Integer_Tab; end ;';

DROP PROCEDURE GetEmpOneFirstNameFromOut(out OUT_PARAM text) ;
CREATE OR REPLACE PROCEDURE GetEmpOneFirstNameFromOut (out OUT_PARAM text) language 'plpgsql' as ' begin SELECT FIRSTNAME into OUT_PARAM FROM EMPLOYEE WHERE ID=1; END;';

DROP PROCEDURE GetEmpFirstNameFromOut(in IN_PARAM int, out OUT_PARAM text) ;
CREATE OR REPLACE PROCEDURE GetEmpFirstNameFromOut (in IN_PARAM int, out OUT_PARAM text) language 'plpgsql' as ' begin SELECT FIRSTNAME into OUT_PARAM FROM EMPLOYEE WHERE ID=IN_PARAM; END ;';

DROP PROCEDURE GetEmpLastNameFromInOut(inout INOUT_PARAM text) ;
CREATE OR REPLACE PROCEDURE GetEmpLastNameFromInOut (inout INOUT_PARAM text) language 'plpgsql' as ' begin SELECT LASTNAME into INOUT_PARAM FROM EMPLOYEE WHERE ID=CAST(INOUT_PARAM AS int); END ;';

DROP PROCEDURE GetEmpASCFromRS() ;
CREATE OR REPLACE PROCEDURE GetEmpASCFromRS(out ref refcursor) LANGUAGE plpgsql AS ' BEGIN OPEN ref FOR SELECT ID, FIRSTNAME, LASTNAME, HIREDATE, SALARY from EMPLOYEE ORDER BY ID ASC; END; ';

DROP PROCEDURE GetEmpIdFNameLNameFromRS(in IN_PARAM int) ;
CREATE OR REPLACE PROCEDURE GetEmpIdFNameLNameFromRS(in IN_PARAM int, out ref refcursor) LANGUAGE plpgsql AS ' BEGIN OPEN ref FOR  SELECT ID, FIRSTNAME, LASTNAME from EMPLOYEE WHERE ID=IN_PARAM; END; ';

DROP PROCEDURE GetEmpIdUsingHireDateFromOut(in IN_PARAM DATE, out OUT_PARAM int) ;
CREATE OR REPLACE PROCEDURE GetEmpIdUsingHireDateFromOut (in IN_PARAM DATE, out OUT_PARAM int) language 'plpgsql' as ' begin SELECT ID into OUT_PARAM FROM EMPLOYEE WHERE HIREDATE=IN_PARAM; END ;';

DROP PROCEDURE UpdateEmpSalaryColumn() ;
CREATE OR REPLACE PROCEDURE UpdateEmpSalaryColumn() language 'plpgsql' as ' begin UPDATE EMPLOYEE set SALARY=0.00; END ;';

DROP PROCEDURE DeleteAllEmp() ;
CREATE OR REPLACE PROCEDURE DeleteAllEmp() language 'plpgsql' as ' begin DELETE FROM EMPLOYEE; END ;';
